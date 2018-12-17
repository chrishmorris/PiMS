/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author Jon Diprose
 */
public class AutoCompleteServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1923832501483440551L;

	/**
	 * Get list of group names from specified DataStorage matching the specified
	 * query. The match is a case-insensitive like.
	 * 
	 * COULD Add a method to GroupService that does just this, to save all the
	 * mapping to/from GroupView objects.
	 * 
	 * @param dataStorage
	 *            - the DataStorage to be searched
	 * @param query
	 *            - the query to match (case-insensitive like)
	 * @return The list of group names
	 * @throws BusinessException
	 *             If anything goes wrong
	 */
	protected List<String> listGroups(final DataStorage dataStorage,
			final String query) throws BusinessException {
		GroupService groupService = dataStorage.getGroupService();
		BusinessCriteria criteria = new BusinessCriteria(groupService);
		criteria.add(BusinessExpression.Like(GroupView.PROP_NAME, query, true,
				true));
		criteria.addOrder(BusinessOrder.ASC(GroupView.PROP_NAME));
		Collection<GroupView> groups = groupService.findViews(criteria);
		List<String> results = new ArrayList<String>(groups.size());
		for (GroupView group : groups) {
			results.add(group.getGroupName());
		}
		return results;
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Map params = request.getParameterMap();
		if (1 != params.size()) {
			throw new ServletException("Only one search type allowed");
		}

		DataStorage dataStorage = null;
		try {

			dataStorage = openResources(request);
			List<String> results = null;

			if (params.containsKey("group")) {
				results = listGroups(dataStorage, request.getParameter("group"));
			}

			// Additional search types here

			else {
				throw new ServletException("Unknown search type: "
						+ params.keySet().iterator().next());
			}

			if (null == results) {
				throw new ServletException("Search failed for: "
						+ params.keySet().iterator().next());
			}

			response.setContentType("text/plain");
			this.setNoCache(response);
			for (String result : results) {
				response.getWriter().println(result);
			}
		} catch (BusinessException ex) {
			throw new ServletException(ex);
		} finally {
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
		return "AutoComplete Servlet";
	}
	// </editor-fold>
}
