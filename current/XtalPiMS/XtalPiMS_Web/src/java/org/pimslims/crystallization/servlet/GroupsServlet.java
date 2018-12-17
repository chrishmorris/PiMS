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
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class GroupsServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1923832501483440551L;

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

		String name = request.getParameter(GroupView.PROP_NAME);
		String organisation = request.getParameter(GroupView.PROP_ORGANISATION);

		DataStorage dataStorage = null;
		try {

			dataStorage = openResources(request);

			GroupService groupService = dataStorage.getGroupService();

			BusinessCriteria criteria = new BusinessCriteria(groupService);

			if ((name != null) && (!name.equals(""))) {
				criteria.add(BusinessExpression.Like(GroupView.PROP_NAME, name,
						true, true));
				crp.put(GroupView.PROP_NAME, name);
			}

			if ((organisation != null) && (!organisation.equals(""))) {
				criteria.add(BusinessExpression.Like(
						GroupView.PROP_ORGANISATION, organisation, true, true));
				crp.put(GroupView.PROP_ORGANISATION,
						organisation);
			}

			JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				Integer count = groupService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {

					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				Collection<GroupView> groups = groupService.findViews(criteria);

				JSONArray array = new JSONArray();
				if (groups != null) {

					Iterator<GroupView> it = groups.iterator();
					while (it.hasNext()) {
						GroupView group = it.next();

						array.add(group.toJSON());
					}
				}

				object.put("records", array);
				object.put("recordsReturned", groups.size());
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
						"groupsDataTable");
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
