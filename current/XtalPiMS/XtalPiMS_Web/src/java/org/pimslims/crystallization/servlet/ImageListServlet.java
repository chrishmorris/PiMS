/*
 * ImageListServlet.java
 *
 * Created on 26 September 2007, 22:31
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.SampleQuantity;

/**
 * 
 * @author ian
 * @version
 */
@Deprecated
// obsolete and broken
public class ImageListServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 1125419267530084981L;

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
		response.setContentType("text/json;charset=UTF-8");
		String barcode = request.getParameter("barcode");
		String inspectionName = request.getParameter("name");
		PrintWriter out = response.getWriter();
		Collection<Image> imageList = null;
		if ((barcode == null) || (inspectionName == null)) {
			return;
		}
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			// Then we are doing a plate!

			// TODO FIX THIS
			// PlateInspectionService plateInspectionService =
			// getDataStorage().getPlateInspectionService();
			// PlateInspection plateInspection =
			// plateInspectionService.findByInspectionName(inspectionName);
			// ImageType imageInfo = ImageType.COMPOSITE;
			//
			// ImageService imageService = getDataStorage().getImageService();
			// imageList = imageService.findImages(barcode, plateInspection,
			// imageInfo, null);
			// ScoreService scoreService = getDataStorage().getScoreService();

			if (imageList == null) {
				return;
			}
			JSONArray array = new JSONArray();
			Iterator<Image> it = imageList.iterator();
			while (it.hasNext()) {
				Image image = it.next();
				JSONObject obj = new JSONObject();

				obj.put("URL", image.getImagePath());
				obj.put("Barcode", barcode);
				obj.put("Well", image.getDrop().getWellPosition().toString());
				obj.put("Date", image.getImageDate().getTimeInMillis());

				obj.put("Inspection", inspectionName);
				obj.put("Width", image.getSizeX());
				obj.put("Height", image.getSizeY());
				obj.put("WidthPerPixel", image.getXLengthPerPixel());
				obj.put("HeightPerPixel", image.getYLengthPerPixel());
				obj.put("Description", image.getDrop().getDescription());

				// obj.put("Imager", plateInspection.getLocation().getName());
				// obj.put("ImagerTemp",
				// plateInspection.getLocation().getTemperature());
				// Interval interval = new
				// Interval(image.getDrop().getPlate().getCreateDate().getTimeInMillis(),
				// plateInspection.getInspectionDate().getTimeInMillis());
				// Period period = new Period(interval.toPeriod());
				// String timeDiff = "";
				// if (period.getYears() != 0) {
				// timeDiff += period.getYears() + " Years ";
				// if (period.getMonths() != 0) {
				// timeDiff += period.getMonths() + " Months";
				// }
				// } else if (period.getMonths() != 0) {
				// timeDiff += period.getMonths() + " Months ";
				// if (period.getWeeks() != 0) {
				// int days = period.getWeeks()*7 + period.getDays();
				// timeDiff += days + " Days";
				// }
				// } else if (period.getWeeks() != 0) {
				// int days = period.getWeeks()*7 + period.getDays();
				// timeDiff += days + " Days";
				// } else if (period.getDays() != 0) {
				// timeDiff += period.getDays() + " Days";
				// } else if (period.getHours() != 0){
				// timeDiff += period.getHours() + " Hours";
				// } else {
				// timeDiff = "+ 0 hours";
				// }
				// obj.put("TimePoint", timeDiff);
				if ((image.getDrop().getSamples() != null)
						&& (!image.getDrop().getSamples().isEmpty())) {
					JSONArray sArray = new JSONArray();
					Iterator<SampleQuantity> sIt = image.getDrop().getSamples()
							.iterator();
					while (sIt.hasNext()) {
						SampleQuantity sampleQuantity = sIt.next();
						JSONObject sObj = new JSONObject();
						sObj.put("SampleName", sampleQuantity.getSample()
								.getName());
						sObj.put("SampleId", sampleQuantity.getSample().getId());
						sObj.put("SampleQuantity", sampleQuantity.getQuantity()
								+ sampleQuantity.getUnit());
						if (sampleQuantity.getSample().getConstruct() != null) {
							sObj.put("ConstructName", sampleQuantity
									.getSample().getConstruct().getName());
							sObj.put("ConstructId", sampleQuantity.getSample()
									.getConstruct().getId());
						}
						sArray.add(sObj);
					}

					obj.put("Samples", sArray);
				}

				if ((image.getDrop().getReservoir() != null)
						&& (image.getDrop().getReservoir().getCondition() != null)) {
					// TODO what about additives? Note duplicated code with
					// TimeCourseSliderServlet
					obj.put("Screen", image.getDrop().getReservoir()
							.getCondition().getLocalName());
					obj.put("Condition", image.getDrop().getReservoir()
							.getCondition().getManufacturerName()
							+ ", "
							+ image.getDrop().getReservoir().getCondition()
									.getManufacturerScreenName()
							+ " ("
							+ image.getDrop().getReservoir().getCondition()
									.getManufacturerCode() + ")");
					Iterator<ComponentQuantity> cIt = image.getDrop()
							.getReservoir().getCondition().getComponents()
							.iterator();
					JSONArray componentArray = new JSONArray();
					while (cIt.hasNext()) {
						ComponentQuantity cq = cIt.next();
						JSONObject componentObject = new JSONObject();
						componentObject.put("Name", cq.getComponent()
								.getChemicalName());
						componentObject.put("Quantity", "" + cq.getQuantity()
								+ cq.getUnits());

						componentArray.add(componentObject);
					}
					obj.put("Chemicals", componentArray);
				}
				// image.getDrop().getReservoir().getCondition().getComponents().get(0).
				// Collection<Image> microImages =
				// imageService.findImages(barcode,
				// image.getDrop().getWellPosition(), ImageType.ZOOMED, false,
				// null);
				// if ((microImages != null) && (!microImages.isEmpty())) {
				// JSONArray mArray = new JSONArray();
				//
				// Iterator<Image> mIt = microImages.iterator();
				// while (mIt.hasNext()) {
				// Image mImage = mIt.next();
				// JSONObject mObject = new JSONObject();
				// mObject.put("Url", mImage.getImagePath());
				// mObject.put("Date", String.format("%1$td/%1$tm/%1$tY %1$tT",
				// mImage.getImageDate()));
				// mObject.put("Plate",
				// mImage.getDrop().getPlate().getBarcode());
				// mObject.put("Well",
				// mImage.getDrop().getWellPosition().toString());
				//
				// mArray.put(mObject);
				// }
				// obj.put("microImages", mArray);
				// }

				obj.put("x1", -1);
				obj.put("y1", -1);
				obj.put("x2", -1);
				obj.put("y2", -1);
				// JSONArray scores = new JSONArray();
				// Collection<SoftwareScore> scoreList =
				// scoreService.findSoftwareScores(image, null);
				//
				// String color = "";
				// Iterator<SoftwareScore> sit = scoreList.iterator();
				// while (sit.hasNext()) {
				// SoftwareScore score = sit.next();
				// JSONObject scobj = new JSONObject();
				// scobj.put("Date", score.getDate().getTimeInMillis());
				// scobj.put("Description", score.getValue().getDescription());
				// scobj.put("Color",
				// ColorUtil.convertColorToHex(score.getValue().getColour()));
				// scobj.put("Name", score.getSoftwareAnnotator().getName());
				// scobj.put("Version",
				// score.getSoftwareAnnotator().getVersion());
				// scobj.put("Type", "Software");
				//
				// color = (String) scobj.get("Color");
				// scores.put(scobj);
				// }
				// Collection<UserScore> userScoreList =
				// scoreService.findHumanScores(image, null);
				//
				// Iterator<UserScore> uit = userScoreList.iterator();
				// while (uit.hasNext()) {
				// UserScore score = uit.next();
				// JSONObject scobj = new JSONObject();
				// scobj.put("Date", score.getDate().getTimeInMillis());
				// scobj.put("Description", score.getValue().getDescription());
				// scobj.put("Color",
				// ColorUtil.convertColorToHex(score.getValue().getColour()));
				//
				// scobj.put("Name", score.getUser().getGivenName() + " " +
				// score.getUser().getFamilyName());
				// scobj.put("Version", "-");
				// scobj.put("Type", "Human");
				// color = (String) scobj.get("Color");
				// //human = true;
				//
				// scores.put(scobj);
				// }

				// obj.put("Scores", scores);
				// obj.put("Color", color);
				// Details of the screen....

				// //IGNORE BELOW!!

				// JSONArray history = new JSONArray();

				// List<Image> historyList = imageService.findAllImages(new
				// Barcode(barcode), image.getWellPosition(), imageInfo);
				// Iterator<Image> hit = historyList.iterator();
				// while (hit.hasNext()) {
				// Image hImage = hit.next();
				// JSONObject hObj = new JSONObject();
				// hObj.put("URL", hImage.getImageURLAsString());
				// hObj.put("Barcode", barcode);
				// hObj.put("Well", hImage.getWellPosition().getWellAsString());
				// hObj.put("Sub", hImage.getWellPosition().getSubPosition());
				// hObj.put("Date", hImage.getImageDate());
				// List<SoftwareScore> hScoreList =
				// scoreService.findScores(hImage);
				// boolean hHuman = false;
				// String hColor = "";
				// Iterator<SoftwareScore> hSit = hScoreList.iterator();
				// while (hSit.hasNext()) {
				// SoftwareScore score = hSit.next();
				// String tempColor =
				// ColorUtil.convertColorToHex(score.getValue().getColour());
				// if (score.getHumanAnnotator() != null) {
				// hColor = tempColor;
				// hHuman = true;
				// } else if (score.getSoftwareAnnotator() != null) {
				// if (!hHuman) {
				// hColor = tempColor;
				// }
				// }
				//
				// }
				// hObj.put("Color", hColor);
				// history.put(hObj);
				// }
				// obj.put("History", history);
				array.add(obj);
			}
			JSONObject object = new JSONObject();
			object.put("images", array);
			out.print(object);

			// } catch (BusinessException ex) {
			// ex.printStackTrace();
		} catch (Exception ex) {
			throw new ServletException(ex);
		} finally {
			out.close();
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
	// </editor-fold>
}
