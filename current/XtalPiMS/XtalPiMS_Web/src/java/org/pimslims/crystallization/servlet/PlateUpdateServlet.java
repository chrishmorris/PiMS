/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.TrialPlateDAO;
import org.pimslims.crystallization.dao.view.InspectionViewDAO;
import org.pimslims.crystallization.servlet.view.ViewByName;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
public class PlateUpdateServlet extends PIMSServlet {

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
			// ---------------------------------------------------------------------
			// general holder info
			final String holderHook = ViewByName.getHook(request, rv);
			final Holder holder = rv.get(holderHook);
			request.setAttribute("holder", holder);
			// HolderTypes
			final HolderType holderType = (CrystalType) holder.getHolderType();
			final Set<HolderType> types = Collections.singleton(holderType);
			request.setAttribute("holderTypes",
					ModelObjectShortBean.getModelObjectShortBeans(types));
			// Owner
			final User user = holder.getFirstSample().getOutputSample()
					.getExperiment().getCreator();
			if (user != null) {
				request.setAttribute("owner", new ModelObjectShortBean(user));
			}
			final Collection<User> users = rv.getAll(User.class);
			request.setAttribute("allUsers",
					ModelObjectShortBean.getModelObjectShortBeans(users));
			// Operator
			final User operator = holder.getFirstSample().getOutputSample()
					.getExperiment().getOperator();
			if (null != operator) {
				request.setAttribute("currentOperator",
						new ModelObjectShortBean(operator));
			}
			// Sceens
			final Collection<RefHolder> screens = rv.findAll(RefHolder.class,
					Collections.EMPTY_MAP);
			request.setAttribute("screens",
					ModelObjectShortBean.getModelObjectShortBeans(screens));
			String currentScreenHook = null;
			if (!holder.getRefHolderOffsets().isEmpty()) {
				currentScreenHook = holder.getRefHolderOffsets().iterator()
						.next().getRefHolder().get_Hook();
			}
			request.setAttribute("currentScreenHook", currentScreenHook);

			// groups
			if (user != null && user.getUserGroups() != null
					&& !user.getUserGroups().isEmpty()) {
				request.setAttribute("groups", ModelObjectShortBean
						.getModelObjectShortBeans(rv.getCurrentProjects()));
			}
			if (holder.getAccess() != null) {
				request.setAttribute("currentGroupHook", holder.getAccess()
						.get_Hook());

			}

			// destroyed
			if (holder.getEndDate() != null) {
				request.setAttribute("isDestroyed", false);
			} else {
				request.setAttribute("isDestroyed", true);
			}

			// ----------------------------------------------------------------------------
			// For Inspections
			//
			final InspectionViewDAO dao = new InspectionViewDAO(rv);
			final BusinessCriteria criteria = new BusinessCriteria(dao);
			criteria.add(BusinessExpression.Equals(InspectionView.PROP_BARCODE,
					holder.getName(), true));
			criteria.addOrder(BusinessOrder.DESC(InspectionView.PROP_DATE));
			final Collection<InspectionView> inspectionViews = dao
					.findViews(criteria);
			request.setAttribute("inspections", inspectionViews);

			// ----------------------------------------------------------------------------
			// Finished & forward
			//
			rv.commit();
			final RequestDispatcher rd = request
					.getRequestDispatcher("/PlateUpdate.jsp");
			rd.forward(request, response);

		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final BusinessException e) {
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
			final String pathInfo = request.getPathInfo();
			final String plateHook = pathInfo.replace("/", "");
			final Holder holder = wv.get(plateHook);
			// plate type
			final HolderType holderType = wv.get(request
					.getParameter("holderTypeId"));
			final PlateType plateType = new PlateType();
			plateType.setName(holderType.getName());
			// Screen
			final RefHolder refHolder = wv
					.get(request.getParameter("screenId"));
			final Screen screen = new Screen();
			screen.setName(refHolder.getName());
			screen.setId(refHolder.getDbId());

			// trialPlate
			final TrialPlate trialPlate = new TrialPlate(plateType);
			trialPlate.setBarcode(request.getParameter("barcode"));
			trialPlate.setDescription(request.getParameter("description"));
			trialPlate.setScreen(screen);
			trialPlate.setId(holder.getDbId());
			trialPlate.setCreateDate(holder.getStartDate());
			// owner
			final User owner = wv.get(request.getParameter("ownerId"));
			if (owner != null) {
				final Person xOwner = new Person();
				xOwner.setUsername(owner.getName());
				xOwner.setId(owner.getDbId());
				trialPlate.setOwner(xOwner);
			}
			// operator
			final User operator = wv.get(request.getParameter("operatorId"));
			if (operator != null) {
				final Person xOperator = new Person();
				xOperator.setUsername(operator.getName());
				xOperator.setId(operator.getDbId());
				trialPlate.setOperator(xOperator);
			}
			// group
			if (request.getParameter("groupId") != null) {
				// group
				final LabNotebook project = wv.get(request
						.getParameter("groupId"));
				final UserGroup pGroup = PlateCreateServlet.getUserGroup(
						project, owner);
				final Group group = new Group();
				group.setId(pGroup.getDbId());
				group.setName(pGroup.getName());
				trialPlate.setGroup(group);
			}
			// destroyed
			final String isDestroyed = request.getParameter("isDestroyed");
			if (isDestroyed.equalsIgnoreCase("yes")) {
				if (holder.getEndDate() == null) {
					trialPlate.setDestroyDate(Calendar.getInstance());
				}
			} else {
				trialPlate.setDestroyDate(null);
			}
			// description
			trialPlate.setDescription(request.getParameter("description"));
			// update
			final TrialPlateDAO dao = new TrialPlateDAO();

			dao.update(trialPlate, wv);
			wv.commit();
			this.redirect(response, request.getContextPath()
					+ "/ViewPlate.jsp?barcode=" + trialPlate.getBarcode());
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final BusinessException e) {
			throw new ServletException(e);
		} finally {
			if (!wv.isCompleted()) {
				wv.abort();
			}
		}

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
