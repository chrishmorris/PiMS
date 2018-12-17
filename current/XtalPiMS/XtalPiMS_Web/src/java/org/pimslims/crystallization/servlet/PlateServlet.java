/*
 * PlateServlet.java Created on 19 October 2007, 14:54
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
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class PlateServlet extends XtalPIMSServlet {

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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// CommonRequestParams crp = CommonRequestParams.parseRequest(request);

		String barcode = request.getParameter("barcode");

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			PlateExperimentService plateExperimentService = dataStorage
					.getPlateExperimentService();

			BusinessCriteria criteria = new BusinessCriteria(
					plateExperimentService);
			criteria.add(BusinessExpression.Equals(
					PlateExperimentView.PROP_BARCODE, barcode, true));

			Collection<PlateExperimentView> plateExperiments = plateExperimentService
					.findViews(criteria);

			JSONObject object = new JSONObject();
			JSONArray array = new JSONArray();
			if (plateExperiments != null) {

				Iterator<PlateExperimentView> it = plateExperiments.iterator();
				while (it.hasNext()) {
					PlateExperimentView plate = it.next();

					JSONObject obj = new JSONObject();

					obj.put(PlateExperimentView.PROP_BARCODE,
							plate.getBarcode());
					obj.put(PlateExperimentView.PROP_CREATE_DATE, plate
							.getCreateDate().getTimeInMillis());
					obj.put(PlateExperimentView.PROP_STATUS, plate.getStatus());
					obj.put(PlateExperimentView.PROP_OWNER, plate.getOwner());

					if (plate.getImager() != null) {
						obj.put(PlateExperimentView.PROP_IMAGER,
								plate.getImager() + " ("
										+ plate.getTemperature() + "&deg;C)");
					}
					if (plate.getLastImageDate() != null) {
						obj.put(PlateExperimentView.PROP_LAST_IMAGE_DATE, plate
								.getLastImageDate().getTimeInMillis());
					}
					obj.put(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS,
							plate.getNumberOfCrystals());
					obj.put(PlateExperimentView.PROP_DESCRIPTION,
							plate.getDescription());
					obj.put(PlateExperimentView.PROP_SAMPLE_ID,
							plate.getSampleId());
					obj.put(PlateExperimentView.PROP_SAMPLE_NAME,
							plate.getSampleName());
					obj.put(PlateExperimentView.PROP_CONSTRUCT_NAME,
							plate.getConstructName());
					obj.put(PlateExperimentView.PROP_CONSTRUCT_ID,
							plate.getConstructId());

					array.add(obj);
				}
			}

			object.put("records", array);
			object.put("recordsReturned", plateExperiments.size());
			object.put("startIndex", 0);

			this.setContentTypeJson(response);
			this.setNoCache(response);
			response.getWriter().print(object);

		} catch (BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			closeResources(dataStorage);
			// Nothing to do
		}
		// response.setContentType("text/json");
		// PrintWriter out = response.getWriter();
		// String barcode = request.getParameter("barcode");
		// try {
		// TrialService plateService = getDataStorage().getTrialService();
		// TrialPlate plate = plateService.findTrialPlate(barcode);
		// ScreenService screenService = getDataStorage().getScreenService();
		// Screen screen = screenService.findByPlate(plate);
		// PlateExperimentService plateExperimentService =
		// getDataStorage().getPlateExperimentService();
		// Collection<PlateExperimentView> plateExperiments =
		// plateExperimentService.findByPlate(plate, null);
		// LocationService locationService =
		// getDataStorage().getLocationService();
		// Location location = locationService.findCurrentPlateLocation(plate);
		// JSONArray array = new JSONArray();
		// Iterator<PlateExperimentView> it = plateExperiments.iterator();
		// JSONObject object = new JSONObject();
		// object.put("Barcode", plate.getBarcode());
		// object.put("PlateType", plate.getPlateType().getName());
		// object.put("Imager", location.getName());
		// object.put("ImagerTemp", location.getTemperature());
		// object.put("Screen", screen.getName());
		// object.put("CreateDate", plate.getCreateDate().getTimeInMillis());
		// if (plate.getDestroyDate() != null) {
		// object.put("DestroyDate", plate.getDestroyDate().getTimeInMillis());
		// }
		// while (it.hasNext()) {
		// PlateExperimentView plateExperiment = it.next();
		//
		// JSONObject obj = new JSONObject();
		// obj.put("CreateDate",
		// plateExperiment.getCreateDate().getTimeInMillis());
		// obj.put("Description", plateExperiment.getDescription());
		// obj.put("Owner", plateExperiment.getOwner());
		// obj.put("Operator", plateExperiment.getRunBy());
		// object.put("Operator", plateExperiment.getRunBy());
		//
		// array.put(obj);
		// }
		// object.put("Experiments", array);
		// out.print(object);
		// } catch (BusinessException ex) {
		// ex.printStackTrace();
		// } catch (JSONException ex) {
		// ex.printStackTrace();
		// }
		//
		//
		//
		//
		//
		// out.close();
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
		return "Short description";
	}
	// </editor-fold>
}
