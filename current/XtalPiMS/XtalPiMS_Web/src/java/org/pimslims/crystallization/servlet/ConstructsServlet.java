/*
 * To change this template, choose Tools | Templates and open the template in the editor.
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
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class ConstructsServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2988985972820811027L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final CommonRequestParams crp = CommonRequestParams
				.parseRequest(request);

		final String constructId = request.getParameter("id");
		final String group = request.getParameter(ConstructView.PROP_GROUP);
		final String constructName = request
				.getParameter(ConstructView.PROP_NAME);
		final String owner = request.getParameter(ConstructView.PROP_OWNER);
		final String description = request
				.getParameter(ConstructView.PROP_DESCRIPTION);

		DataStorage dataStorage = null;
		try {

			dataStorage = openResources(request);

			final ConstructService constructService = dataStorage
					.getConstructService();

			final BusinessCriteria criteria = new BusinessCriteria(
					constructService);

			if ((constructId != null) && (!constructId.equals(""))) {
				criteria.add(BusinessExpression.Equals(ConstructView.PROP_ID,
						constructId, true));
				crp.put(ConstructView.PROP_ID, constructId);
			}
			if ((constructName != null) && (!constructName.equals(""))) {
				criteria.add(BusinessExpression.Like(ConstructView.PROP_NAME,
						constructName, true, true));
				crp.put(ConstructView.PROP_NAME, constructName);
			}
			if ((description != null) && (!description.equals(""))) {
				criteria.add(BusinessExpression
						.Like(ConstructView.PROP_DESCRIPTION, description,
								true, true));
				crp.put(ConstructView.PROP_DESCRIPTION, description);
			}
			if ((owner != null) && (!owner.equals(""))) {
				criteria.add(BusinessExpression.Like(ConstructView.PROP_OWNER,
						owner, true, true));
				crp.put(ConstructView.PROP_OWNER, owner);
			}
			if ((group != null) && (!group.equals(""))) {
				criteria.add(BusinessExpression.Like(ConstructView.PROP_GROUP,
						group, true, true));
				crp.put(ConstructView.PROP_GROUP, group);
			}

			final JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				final Integer count = constructService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				Collection<ConstructView> constructs = null;

				constructs = constructService.findViews(criteria);

				final JSONArray array = new JSONArray();

				if (constructs != null) {
					final Iterator<ConstructView> it = constructs.iterator();
					while (it.hasNext()) {
						final ConstructView construct = it.next();

						final JSONObject obj = new JSONObject();

						obj.put(ConstructView.PROP_NAME,
								construct.getConstructName());
						obj.put(ConstructView.PROP_ID,
								construct.getConstructId());
						obj.put(ConstructView.PROP_DESCRIPTION,
								construct.getDescription());
						// obj.put(ConstructView.PROP_CONSTRUCT_NAME,
						// "<a href='http://www.oppf.ox.ac.uk/optic/oppf_details.php?oppf_id="
						// + construct.getId() + "'>" + construct.getName() +
						// "</a>");

						if (construct.getGroup() != null) {
							obj.put(ConstructView.PROP_GROUP,
									"<a href='ViewGroup.jsp?name="
											+ construct.getGroup() + "'>"
											+ construct.getGroup() + "</a>");
						} else {
							obj.put(ConstructView.PROP_GROUP, "");
						}
						if (construct.getOwner() != null) {
							obj.put(ConstructView.PROP_OWNER, construct
									.getOwner().toString());
						} else {
							obj.put(ConstructView.PROP_OWNER, "");
						}
						if (construct.getTargetName() != null) {
							obj.put(ConstructView.PROP_TARGET_NAME,
									"<a href='ViewTarget.jsp?target="
											+ construct.getTargetId() + "'>"
											+ construct.getTargetName()
											+ "</a>");
						} else {
							obj.put(ConstructView.PROP_TARGET_NAME, "");
						}
						array.add(obj);
					}
				}

				object.put("records", array);
				object.put("recordsReturned", constructs.size());
			}

			this.setContentTypeJson(response);
			this.setNoCache(response);
			response.getWriter().print(object);
		} catch (final BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			// Only store for the non-count request!
			if (null == request.getParameter("count")) {
				crp.storeSessionBookmark(request.getSession(),
						"constructsDataTable");
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
		processRequest(request, response);
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
