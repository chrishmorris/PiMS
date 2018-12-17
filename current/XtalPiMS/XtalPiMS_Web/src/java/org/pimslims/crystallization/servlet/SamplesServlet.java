/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;

/**
 * 
 * @author ian
 */
public class SamplesServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 82189882782311474L;

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

		final String id = request.getParameter(SampleView.PROP_ID);
		final String batchReference = request
				.getParameter(SampleView.PROP_BATCH_REFERENCE);
		final String cellularLocation = request
				.getParameter(SampleView.PROP_CELLULAR_LOCATION);
		final String minConcentration = request
				.getParameter("minConcentration");
		final String maxConcentration = request
				.getParameter("maxConcentration");
		final String startDate = request.getParameter("startDate");
		final String endDate = request.getParameter("endDate");
		final String description = request
				.getParameter(SampleView.PROP_DESCRIPTION);
		final String giNumber = request.getParameter(SampleView.PROP_GI_NUMBER);
		final String name = request.getParameter(SampleView.PROP_NAME);
		final String minNumSubUnits = request.getParameter("minNumSubUnits");
		final String maxNumSubUnits = request.getParameter("maxNumSubUnits");
		final String origin = request.getParameter(SampleView.PROP_ORIGIN);
		final String minPH = request.getParameter("minPH");
		final String maxPH = request.getParameter("maxPH");
		final String type = request.getParameter(SampleView.PROP_TYPE);
		final String minMolecularWeight = request
				.getParameter("minMolecularWeight");
		final String maxMolecularWeight = request
				.getParameter("maxMolecularWeight");
		final String groupName = request.getParameter(SampleView.PROP_GROUP);
		final String constructName = request
				.getParameter(SampleView.PROP_CONSTRUCT_NAME);
		final String constructId = request
				.getParameter(SampleView.PROP_CONSTRUCT_ID);
		final String owner = request.getParameter(SampleView.PROP_OWNER);
		final String targetName = request
				.getParameter(SampleView.PROP_TARGET_NAME);

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			final SampleService sampleService = dataStorage.getSampleService();
			final BusinessCriteria criteria = new BusinessCriteria(
					sampleService);
			if ((id != null) && (!id.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_ID,
						Long.parseLong(id), true));
				crp.put(SampleView.PROP_ID, id);
			}
			if ((description != null) && (!description.equals(""))) {
				criteria.add(BusinessExpression.Like(
						SampleView.PROP_DESCRIPTION, description, true, true));
				crp.put(SampleView.PROP_DESCRIPTION,
						description);
			}
			if ((owner != null) && (!owner.equals(""))) {
				criteria.add(BusinessExpression.Like(SampleView.PROP_OWNER,
						owner, true, true));
				crp.put(SampleView.PROP_OWNER, owner);
			}
			if ((groupName != null) && (!groupName.equals(""))) {
				criteria.add(BusinessExpression.Like(SampleView.PROP_GROUP,
						groupName, true, true));
				crp.put(SampleView.PROP_GROUP, groupName);
			}
			if ((constructName != null) && (!constructName.equals(""))) {
				criteria.add(BusinessExpression.Like(
						SampleView.PROP_CONSTRUCT_NAME, constructName, true,
						true));
				crp.put(SampleView.PROP_CONSTRUCT_NAME,
						constructName);
			}
			if ((constructId != null) && (!constructId.equals("0"))
					&& (!constructId.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_CONSTRUCT_ID, constructId, true));
			}
			if ((targetName != null) && (!targetName.equals(""))) {
				criteria.add(BusinessExpression.Like(
						SampleView.PROP_TARGET_NAME, targetName, true, true));
				crp.put(SampleView.PROP_TARGET_NAME,
						targetName);
			}
			if ((batchReference != null) && (!batchReference.equals(""))) {
				criteria.add(BusinessExpression.Like(
						SampleView.PROP_BATCH_REFERENCE, batchReference, true,
						true));
				crp.put(SampleView.PROP_BATCH_REFERENCE,
						batchReference);
			}
			if ((name != null) && (!name.equals(""))) {
				criteria.add(BusinessExpression.Like(SampleView.PROP_NAME,
						name, true, true));
				crp.put(SampleView.PROP_NAME, name);
			}
			if ((type != null) && (!type.equals(""))) {
				criteria.add(BusinessExpression.Equals(SampleView.PROP_TYPE,
						type, true));
				crp.put(SampleView.PROP_TYPE, type);
			}
			if ((origin != null) && (!origin.equals(""))) {
				criteria.add(BusinessExpression.Like(SampleView.PROP_ORIGIN,
						origin, true, true));
				crp.put(SampleView.PROP_ORIGIN, origin);
			}
			if ((giNumber != null) && (!giNumber.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						SampleView.PROP_GI_NUMBER, giNumber, true));
				crp.put(SampleView.PROP_GI_NUMBER,
						giNumber);
			}
			if ((cellularLocation != null) && (!cellularLocation.equals(""))) {
				criteria.add(BusinessExpression.Like(
						SampleView.PROP_CELLULAR_LOCATION, cellularLocation,
						true, true));
				crp.put(SampleView.PROP_CELLULAR_LOCATION,
						cellularLocation);
			}
			// TODO Deal with min and max separately
			if ((minMolecularWeight != null)
					&& (!minMolecularWeight.equals(""))
					&& (maxMolecularWeight != null)
					&& (!maxMolecularWeight.equals(""))) {
				criteria.add(BusinessExpression.Between(
						SampleView.PROP_MOLECULAR_WEIGHT,
						Double.parseDouble(minMolecularWeight),
						Double.parseDouble(maxMolecularWeight)));
				crp.put("minMolecularWeight",
						minMolecularWeight);
				crp.put("maxMolecularWeight",
						maxMolecularWeight);
			}
			// TODO Deal with min and max separately
			if ((minConcentration != null) && (!minConcentration.equals(""))
					&& (maxConcentration != null)
					&& (!maxConcentration.equals(""))) {
				criteria.add(BusinessExpression.Between(
						SampleView.PROP_CONCENTRATION,
						Double.parseDouble(minConcentration),
						Double.parseDouble(maxConcentration)));
				crp.put("minConcentration",
						minConcentration);
				crp.put("maxConcentration",
						maxConcentration);
			}
			// TODO Deal with min and max separately
			if ((minPH != null) && (!minPH.equals("")) && (maxPH != null)
					&& (!maxPH.equals(""))) {
				criteria.add(BusinessExpression.Between(SampleView.PROP_PH,
						Double.parseDouble(minPH), Double.parseDouble(maxPH)));
				crp.put("minPH", minPH);
				crp.put("maxPH", maxPH);
			}
			// TODO Deal with min and max separately
			if ((minNumSubUnits != null) && (!minNumSubUnits.equals(""))
					&& (maxNumSubUnits != null) && (!maxNumSubUnits.equals(""))) {
				criteria.add(BusinessExpression.Between(
						SampleView.PROP_NUM_SUB_UNITS,
						Integer.parseInt(minNumSubUnits),
						Integer.parseInt(maxNumSubUnits)));
				crp.put("minNumSubUnits", minNumSubUnits);
				crp.put("maxNumSubUnits", maxNumSubUnits);
			}

			final DateHandler dh = new DateHandler();
			Calendar calStart = null;
			Calendar calEnd = null;
			try {
				calStart = dh.parseDate(startDate);
				crp.put("startDate", startDate);
			} catch (ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			try {
				calEnd = dh.parseDate(endDate);
				crp.put("endDate", endDate);
			} catch (ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			dh.addCriterion(criteria, SampleView.PROP_CREATE_DATE, calStart,
					calEnd);

			// Now create the JSON
			final JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				final Integer count = sampleService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				final Collection<SampleView> samples = sampleService
						.findViews(criteria);

				final JSONArray array = new JSONArray();

				if (samples != null) {
					final Iterator<SampleView> it = samples.iterator();
					while (it.hasNext()) {
						final SampleView sample = it.next();

						array.add(sample.toJSON());
					}
				}

				object.put("records", array);
				object.put("recordsReturned", samples.size());
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
						"samplesDataTable");
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
