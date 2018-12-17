/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class ScreensServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6591089049715560736L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		CommonRequestParams crp = CommonRequestParams.parseRequest(request);

		String name = request.getParameter(ScreenView.PROP_NAME);
		String manufacturerName = request
				.getParameter(ScreenView.PROP_MANUFACTURER_NAME);
		String type = request.getParameter(ScreenView.PROP_TYPE);

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			System.out.println("ScreensServlet processRequest:");

			ScreenService screenService = dataStorage.getScreenService();
			BusinessCriteria criteria = new BusinessCriteria(screenService);
			if ((name != null) && (!name.equals(""))) {
				criteria.add(BusinessExpression.Like(ScreenView.PROP_NAME,
						name, true, true));
				crp.put(ScreenView.PROP_NAME, name);
			}
			if ((manufacturerName != null) && (!manufacturerName.equals("all"))) {
				criteria.add(BusinessExpression.Like(
						ScreenView.PROP_MANUFACTURER_NAME, manufacturerName,
						true, true));
				crp.put(ScreenView.PROP_MANUFACTURER_NAME,
						manufacturerName);
			} else {
				crp.put(ScreenView.PROP_MANUFACTURER_NAME,
						"all");
			}
			if ((type != null) && (!type.equals("all"))) {
				criteria.add(BusinessExpression.Equals(ScreenView.PROP_TYPE,
						type, true));
				crp.put(ScreenView.PROP_TYPE, type);
			} else {
				crp.put(ScreenView.PROP_TYPE, "all");
			}

			JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				int screenCount = screenService.findViewCount(criteria);

				object.put("count", screenCount);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				Collection<ScreenView> screens = screenService
						.findViews(criteria);

				JSONArray array = new JSONArray();
				if (screens != null) {

					Iterator<ScreenView> it = screens.iterator();
					while (it.hasNext()) {
						ScreenView screen = it.next();

						array.add(screen.toJSON());
					}
				}

				object.put("records", array);
				object.put("recordsReturned", screens.size());
			}

			this.setContentTypeJson(response);
			this.setNoCache(response);
			response.getWriter().print(object);
		} catch (BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			// Only store for the non-count request!
			if (null == request.getParameter("count")) {
				crp.storeSessionBookmark(request.getSession(),
						"screensDataTable");
			}
			closeResources(dataStorage);
		}

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
