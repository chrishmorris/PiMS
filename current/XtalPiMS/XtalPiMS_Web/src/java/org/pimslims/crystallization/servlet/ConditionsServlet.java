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
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.view.ComponentView;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;

/**
 * Servlet implementation class for Servlet: ConditionsServlet
 * 
 */
public class ConditionsServlet extends XtalPIMSServlet {
	static final long serialVersionUID = 1L;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ConditionsServlet() {
		super();
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
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final CommonRequestParams crp = CommonRequestParams
				.parseRequest(request);

		final String localName = request
				.getParameter(ConditionView.PROP_LOCAL_NAME);
		final String localNumber = request
				.getParameter(ConditionView.PROP_LOCAL_NUMBER);
		final String finalPH = request
				.getParameter(ConditionView.PROP_FINAL_PH);
		final String manufacturerCatCode = request
				.getParameter(ConditionView.PROP_MANUFACTURER_CAT_CODE);
		final String manufacturerCode = request
				.getParameter(ConditionView.PROP_MANUFACTURER_CODE);
		final String manufacturerName = request
				.getParameter(ConditionView.PROP_MANUFACTURER_NAME);
		final String manufacturerScreenName = request
				.getParameter(ConditionView.PROP_MANUFACTURER_SCREEN_NAME);
		final String volatileCondition = request
				.getParameter(ConditionView.PROP_VOLATILE_CONDITION);
		final String saltCondition = request
				.getParameter(ConditionView.PROP_SALT_CONDITION);
		final String well = request.getParameter(ConditionView.PROP_WELL);
		final String componentsName = request.getParameter("componentName");

		DataStorage dataStorage = null;
		try {

			dataStorage = openResources(request);
			final ConditionService conditionService = dataStorage
					.getConditionService();
			final BusinessCriteria criteria = new BusinessCriteria(
					conditionService);

			if ((null != localName) && (!"".equals(localName))
					&& (!"all".equals(localName))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_LOCAL_NAME, localName, true, true));
				crp.put(ConditionView.PROP_LOCAL_NAME,
						localName);
			}
			if ((localNumber != null) && (!localNumber.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_LOCAL_NUMBER, localNumber, true,
						true));
				crp.put(ConditionView.PROP_LOCAL_NUMBER,
						localNumber);
			}
			if ((finalPH != null) && (!finalPH.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_FINAL_PH, finalPH, true, true));
				crp.put(ConditionView.PROP_FINAL_PH,
						finalPH);
			}
			if ((manufacturerCatCode != null)
					&& (!manufacturerCatCode.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_MANUFACTURER_CAT_CODE,
						manufacturerCatCode, true, true));
				crp.put(
						ConditionView.PROP_MANUFACTURER_CAT_CODE,
						manufacturerCatCode);
			}
			if ((manufacturerCode != null) && (!manufacturerCode.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_MANUFACTURER_CODE, manufacturerCode,
						true, true));
				crp.put(
						ConditionView.PROP_MANUFACTURER_CODE, manufacturerCode);
			}
			if ((manufacturerName != null) && (!manufacturerName.equals(""))
					&& (!manufacturerName.equals("all"))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_MANUFACTURER_NAME, manufacturerName,
						true, true));
				crp.put(
						ConditionView.PROP_MANUFACTURER_NAME, manufacturerName);
			}
			if ((manufacturerScreenName != null)
					&& (!manufacturerScreenName.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ConditionView.PROP_MANUFACTURER_SCREEN_NAME,
						manufacturerScreenName, true, true));
				crp.put(
						ConditionView.PROP_MANUFACTURER_SCREEN_NAME,
						manufacturerScreenName);
			}
			if ((saltCondition != null) && (!saltCondition.equals(""))
					&& (!saltCondition.equals("all"))) {
				if (saltCondition.equals("true")) {
					criteria.add(BusinessExpression.Equals(
							ConditionView.PROP_SALT_CONDITION,
							new Boolean(true), true));
				} else {
					criteria.add(BusinessExpression.Equals(
							ConditionView.PROP_SALT_CONDITION, new Boolean(
									false), true));
				}
				crp.put(ConditionView.PROP_SALT_CONDITION,
						saltCondition);
			}
			if ((volatileCondition != null) && (!volatileCondition.equals(""))
					&& (!volatileCondition.equals("all"))) {
				if (volatileCondition.equals("true")) {
					criteria.add(BusinessExpression.Equals(
							ConditionView.PROP_VOLATILE_CONDITION, new Boolean(
									true), true));
				} else {
					criteria.add(BusinessExpression.Equals(
							ConditionView.PROP_VOLATILE_CONDITION, new Boolean(
									false), true));
				}
				crp.put(
						ConditionView.PROP_VOLATILE_CONDITION,
						volatileCondition);
			}
			if ((well != null) && (!well.equals(""))) {
				criteria.add(BusinessExpression.Like(ConditionView.PROP_WELL,
						well, true, true));
				crp.put(ConditionView.PROP_WELL, well);
			}
			if ((componentsName != null) && (!componentsName.equals(""))) {
				criteria.add(BusinessExpression.Like(ComponentView.PROP_NAME,
						componentsName, true, true));
				crp.put("componentName", componentsName);
			}

			final JSONObject object = new JSONObject();
			if (null != request.getParameter("count")) {
				final Integer count = conditionService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
					if (!crp.getSort().equals(ConditionView.PROP_LOCAL_NUMBER)) {
						criteria.addOrder(BusinessOrder
								.ASC(ConditionView.PROP_LOCAL_NUMBER));
					}
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
					if (!crp.getSort().equals(ConditionView.PROP_LOCAL_NUMBER)) {
						criteria.addOrder(BusinessOrder
								.ASC(ConditionView.PROP_LOCAL_NUMBER));
					}
				}
				Collection<ConditionView> conditions = null;
				// TODO Replace this if clause - too much data returned without
				// it at the moment
				if (criteria.getCriteria().size() > 0) {
					conditions = conditionService.findViews(criteria);
				}

				final JSONArray array = new JSONArray();

				if (null != conditions) {
					final Iterator<ConditionView> it = conditions.iterator();
					while (it.hasNext()) {
						final ConditionView condition = it.next();

						array.add(condition.toJSON());
					}
				}

				object.put("records", array);
				if (null != conditions) {
					object.put("recordsReturned", conditions.size());
				}
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
						"conditionsDataTable");
			}
			closeResources(dataStorage);
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
