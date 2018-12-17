/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.PIMSAccess;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.TrialPlateDAO;
import org.pimslims.crystallization.implementation.TrialServiceUtils;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class PlateCreateServlet extends PIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7643062815567251311L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final ReadableVersion rv = this.getReadableVersion(request, response);
		try {
			request.setAttribute("accessObjects",
					PIMSServlet.getPossibleCreateOwners(rv));
			// HolderTypes TODO these were loaded as holder types
			final Collection<CrystalType> types = rv.findAll(CrystalType.class,
					Collections.EMPTY_MAP);
			request.setAttribute("holderTypes",
					ModelObjectShortBean.getModelObjectShortBeans(types));
			// Sceens
			final Collection<RefHolder> screens = rv.findAll(RefHolder.class,
					Collections.EMPTY_MAP);
			request.setAttribute("screens",
					ModelObjectShortBean.getModelObjectShortBeans(screens));

			// User
			final User user = super.getCurrentUser(rv, request);
			if (user != null) {
				request.setAttribute("currentUser", new ModelObjectShortBean(
						user));
			}
			// final Collection<User> users = rv.getAll(User.class);
			// request.setAttribute("allUsers",
			// ModelObjectShortBean.getModelObjectShortBeans(users));

			request.setAttribute("labNotebooks",
					PIMSServlet.getPossibleCreateOwners(rv));
			// TODO find relevant protein samples from MRU, those with category
			// "Purified protein"
			rv.commit();
			final RequestDispatcher rd = request
					.getRequestDispatcher("/PlateCreate.jsp");
			rd.forward(request, response);
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!rv.isCompleted()) {
				rv.abort();
			}
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		// TODO populate labNotebooks
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@Override
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		processPostRequest(request, response);
	}

	private void processPostRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		final WritableVersion wv = getWritableVersion(request, response);
		try {
			// plate type
			final HolderType holderType = wv.get(request
					.getParameter("holderTypeId"));
			final PlateType plateType = new PlateType();
			plateType.setName(holderType.getName());
			// trialPlate
			final TrialPlate plate = new TrialPlate(plateType);
			// Screen
			final RefHolder refHolder = wv
					.get(request.getParameter("screenId"));
			final Screen screen = new Screen();
			screen.setName(refHolder.getName());
			screen.setId(refHolder.getDbId());
			// owner
			final User owner = wv.get(request.getParameter("ownerId"));
			if (owner != null) {
				final Person xOwner = new Person();
				xOwner.setUsername(owner.getName());
				xOwner.setId(owner.getDbId());
				plate.setOwner(xOwner);
			}
			// operator
			final User operator = wv.get(request.getParameter("operatorId"));
			if (operator != null) {
				final Person xOperator = new Person();
				xOperator.setUsername(operator.getName());
				xOperator.setId(operator.getDbId());
				plate.setOwner(xOperator);
			}
			// group
			String bookName = request.getParameter("_OWNER");
			final LabNotebook book = wv.findFirst(LabNotebook.class,
					LabNotebook.PROP_NAME, bookName);
			wv.setDefaultOwner(book);
			final UserGroup userGroup = getUserGroup(book, owner);
			if (null == userGroup) {
				throw new BusinessException(
						"That user"
								+ " is not permitted to create records in lab notebook: "
								+ bookName);
			}
			final Group group = new Group();
			group.setName(userGroup.getName());
			group.setId(userGroup.getDbId());
			plate.setBarcode(request.getParameter("barcode"));
			plate.setDescription(request.getParameter("description"));
			plate.setScreen(screen);
			plate.setGroup(group);

			final TrialPlateDAO dao = new TrialPlateDAO();
			Holder holder = (Holder) dao.create(plate, wv);
			String sampleHook = request.getParameter("sample");
			if (null != sampleHook && !"".equals(sampleHook)) {
				Sample sample = wv.get(sampleHook);
				assert null != sampleHook : "No such sample: " + sampleHook;
				TrialServiceUtils.createInputSample(wv,
						HolderFactory.getExperimentGroup(holder),
						"Purified protein", sample, 100f / 1000000000f);
			}

			wv.commit();
			this.redirect(response, request.getContextPath()
					+ "/ViewPlate.jsp?barcode=" + plate.getBarcode());

		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final BusinessException e) {
			throw new ServletException(e);
		} catch (AccessException e) {
			throw new ServletException(e);
		} finally {
			if (!wv.isCompleted()) {
				wv.abort();
			}
		}

	}

	public static UserGroup getUserGroup(final LabNotebook project,
			final User owner) {
		for (final UserGroup ug : owner.getUserGroups()) {
			for (final Permission permission : ug.getPermissions()) {
				if (permission.getAccessObject().equals(project)) {
					if (permission.getOpType().equals(PIMSAccess.CREATE)) {
						return ug;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
