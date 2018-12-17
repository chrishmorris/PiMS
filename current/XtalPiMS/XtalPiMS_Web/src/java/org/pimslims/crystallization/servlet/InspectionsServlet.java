/*
 * InspectionsServlet.java Created on 15 October 2007, 09:11
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
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.view.InspectionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class InspectionsServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5426283999733080041L;

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

		String barcode = request.getParameter("barcode");
		String inspectionStartDate = request
				.getParameter("inspectionStartDate");
		String inspectionEndDate = request.getParameter("inspectionEndDate");
		String imager = request.getParameter("imager");
		String temperature = request.getParameter("temperature");

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			PlateInspectionService plateInspectionService = dataStorage
					.getPlateInspectionService();

			BusinessCriteria criteria = new BusinessCriteria(
					plateInspectionService);

			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Like(
						InspectionView.PROP_BARCODE, barcode, true, true));
				crp.put("barcode", barcode);
			}

			final DateHandler dh = new DateHandler();
			Calendar calStart = null;
			Calendar calEnd = null;
			try {
				calStart = dh.parseDate(inspectionStartDate);
				crp.put("startDate", inspectionStartDate);
			} catch (ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			try {
				calEnd = dh.parseDate(inspectionEndDate);
				crp.put("endDate", inspectionEndDate);
			} catch (ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			dh.addCriterion(criteria, InspectionView.PROP_DATE, calStart,
					calEnd);

			if ((imager != null) && (!imager.equals(""))
					&& (!imager.equals("all"))) {
				criteria.add(BusinessExpression.Like(
						InspectionView.PROP_IMAGER, imager, true, true));
				crp.put("imager", imager);
			} else {
				crp.put("imager", "all");
			}
			if ((temperature != null) && (!temperature.equals(""))
					&& (!temperature.equals("all"))) {
				try {
					Float temp = Float.parseFloat(temperature);
					criteria.add(BusinessExpression.Equals(
							InspectionView.PROP_TEMPERATURE, temp, true));
					crp.put("temperature", temperature);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			} else {
				crp.put("temperature", "all");
			}

			JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				Integer count = plateInspectionService.findViewCount(criteria);

				object.put("count", count);
			} else {

				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());

				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				Collection<InspectionView> inspections = null;

				inspections = plateInspectionService.findViews(criteria);

				JSONArray array = new JSONArray();
				if (inspections != null) {

					Iterator<InspectionView> it = inspections.iterator();
					while (it.hasNext()) {
						InspectionView inspection = it.next();

						JSONObject obj = new JSONObject();

						obj.put(InspectionView.PROP_BARCODE,
								inspection.getBarcode());
						obj.put(InspectionView.PROP_DATE, inspection.getDate()
								.getTimeInMillis());
						obj.put(InspectionView.PROP_DETAILS,
								inspection.getDetails());

						if (inspection.getImager() != null) {
							obj.put(InspectionView.PROP_IMAGER,
									inspection.getImager() + " ("
											+ inspection.getTemperature()
											+ "&deg;C)");
						}
						obj.put(InspectionView.PROP_INSPECTION_NAME,
								inspection.getInspectionName());

						array.add(obj);
					}
				}

				object.put("records", array);
				object.put("recordsReturned", inspections.size());
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
						"inspectionsDataTable");
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
		return "Retrieves details of all the inspections in date order...";
	}
	// </editor-fold>
}
