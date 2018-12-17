/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.view.ConditionViewDAO;
import org.pimslims.crystallization.dao.view.ScreenTypeViewDAO;
import org.pimslims.crystallization.implementation.view.ScreenTypeView;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.holder.RefHolderSource;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
@WebServlet("/update/EditScreen/*")
public class ScreenUpdate extends PIMSServlet {

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
			// curren values
			final String pathInfo = request.getPathInfo();
			final String screenName = pathInfo.replace("/", "");
			final RefHolder screen = rv.findFirst(RefHolder.class,
					RefHolder.PROP_NAME, screenName);
			if (null == screen) {
				// could make 404
				throw new ServletException("No such screen: " + screenName);
			}
			request.setAttribute("screen", screen);

			String currentTypeName = "";
			for (final HolderCategory type : screen.getHolderCategories()) {
				if (!type.getName().equalsIgnoreCase("Screen")) {
					currentTypeName = type.getName();
				}
			}
			request.setAttribute("currentTypeName", currentTypeName);
			if (screen.getRefHolderSources().size() > 0) {
				request.setAttribute("currentManufacturer", screen
						.getRefHolderSources().iterator().next().getSupplier()
						.getName());
			}

			// screen type
			final ScreenTypeViewDAO screenTypeDAO = new ScreenTypeViewDAO(rv);
			final BusinessCriteria criteria = new BusinessCriteria(
					screenTypeDAO);
			final Collection<ScreenTypeView> views = screenTypeDAO
					.findViews(criteria);
			request.setAttribute("screenTypeViews", views);
			// manufacturers
			final Collection<Organisation> manufacturers = rv.getAll(
					Organisation.class, 0, 150);
			request.setAttribute("manufacturers", manufacturers);
			// ---------------------------------------------------------------------------
			// size
			final int maxRow = 8;
			final int maxCol = 12;
			final String[] rows = new String[maxRow];
			final String[] cols = new String[maxCol];
			System.arraycopy(HolderFactory.ROWS, 0, rows, 0, maxRow);
			System.arraycopy(HolderFactory.COLUMNS, 0, cols, 0, maxCol);
			request.setAttribute("rows", rows);
			request.setAttribute("cols", cols);
			// ----------------------------------------------------------------------------
			// Conditions
			final Collection<ConditionView> conditionViews = getConditions(rv,
					screenName);
			request.setAttribute("conditions", conditionViews);

			// ----------------------------------------------------------------------------
			// Finished & forward
			//
			rv.commit();
			final RequestDispatcher rd = request
					.getRequestDispatcher("/ScreenUpdate.jsp");
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

	private Collection<ConditionView> getConditions(final ReadableVersion rv,
			final String screenName) throws BusinessException {
		final ConditionViewDAO conditionDAO = new ConditionViewDAO(rv);
		final BusinessCriteria condCriteria = new BusinessCriteria(conditionDAO);
		condCriteria.add(BusinessExpression.Equals(
				ConditionView.PROP_LOCAL_NAME, screenName, true));
		final Collection<ConditionView> conditionViews = conditionDAO
				.findViews(condCriteria);
		// sort
		final List<ConditionView> sortedViews = new LinkedList<ConditionView>();
		sortedViews.addAll(conditionViews);
		Collections.sort(sortedViews, new RowOrder());
		return sortedViews;
	}

	public static class RowOrder implements Comparator {

		public int compare(final Object arg0, final Object arg1) {

			final ConditionView view0 = (ConditionView) arg0;
			final ConditionView view1 = (ConditionView) arg1;
			final WellPosition well0 = new WellPosition(view0.getWell());
			final WellPosition well1 = new WellPosition(view1.getWell());

			final Integer row0 = well0.getRow();
			final Integer row1 = well1.getRow();
			final int row = row0.compareTo(row1);
			if (0 != row) {
				return row;
			}

			final Integer column0 = well0.getColumn();
			final Integer column1 = well1.getColumn();

			return column0.compareTo(column1);
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
			final String screenName = pathInfo.replace("/", "");
			final RefHolder screen = wv.findFirst(RefHolder.class,
					RefHolder.PROP_NAME, screenName);
			screen.setName(request.getParameter("screenName"));
			// HolderCategory
			final String typeName = request.getParameter("typeName");
			final HolderCategory hc = wv.findFirst(HolderCategory.class,
					HolderCategory.PROP_NAME, typeName);
			final HolderCategory hcScreen = wv.findFirst(HolderCategory.class,
					HolderCategory.PROP_NAME, "Screen");
			Collection<HolderCategory> categories = new HashSet(2);
			categories.add(hc);
			categories.add(hcScreen);
			screen.setHolderCategories(categories);

			// manufacturers
			final String orgName = request.getParameter("manufacturerName");
			final Organisation org = wv.findFirst(Organisation.class,
					Organisation.PROP_NAME, orgName);
			if (screen.getRefHolderSources().size() > 0) {
				wv.delete(screen.getRefHolderSources());
				wv.flush();
			}
			final RefHolderSource newSource = new RefHolderSource(wv,
					screenName, screen, org);
			screen.setDetails(request.getParameter("description"));

			wv.commit();
			this.redirect(response, request.getContextPath()
					+ "/update/EditScreen/" + screen.getName());
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final AccessException e) {
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
		return "update screen";
	}
	// </editor-fold>
}
