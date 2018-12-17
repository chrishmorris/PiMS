/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.service.PlateInspectionService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
@Deprecated
// I don't think this is in use - Chris
public class TimeCourseSliderServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = -5946953783430331785L;

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
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			String barcode = request.getParameter("barcode");
			String well = request.getParameter("well");
			WellPosition wellPosition = new WellPosition(well);

			JSONArray history = new JSONArray();
			ImageType imageInfo = ImageType.COMPOSITE;
			ImageService imageService = dataStorage.getImageService();
			HumanScoreService scoreService = dataStorage.getHumanScoreService();
			PlateInspectionService plateInspectionService = dataStorage
					.getPlateInspectionService();
			// isn't that a bit slow?
			List<Image> historyList = (List<Image>) imageService.findAll(null);// .findImages(barcode,
																				// wellPosition,
																				// imageInfo,
																				// false,
																				// null);

			@SuppressWarnings("rawtypes")
			Comparator comparator = new Comparator() {

				/**
				 * .compare
				 * 
				 * @see java.util.Comparator#compare(java.lang.Object,
				 *      java.lang.Object)
				 */
				public int compare(Object arg0, Object arg1) {
					Image image0 = (Image) arg0;
					Image image1 = (Image) arg1;
					return image0.getImageDate().compareTo(
							image1.getImageDate());
				}
			};
			// was BeanComparator comparator = new BeanComparator("imageDate");
			Collections.sort(historyList, comparator);
			Calendar timeZero = null;
			Image iImage = historyList.get(0);
			if (iImage != null) {
				timeZero = iImage.getImageDate();
			}
			Iterator<Image> hit = historyList.iterator();
			while (hit.hasNext()) {
				Image hImage = hit.next();

				// PlateInspection plateInspection =
				// plateInspectionService..findByImage(hImage);

				JSONObject hObj = new JSONObject();
				hObj.put("URL", hImage.getImagePath());
				// hObj.put("Inspection", plateInspection.getInspectionName());
				hObj.put(
						"Date",
						String.format("%1$td/%1$tm/%1$tY %1$tT",
								hImage.getImageDate()));
				if ((hImage.getDrop().getSamples() != null)
						&& (!hImage.getDrop().getSamples().isEmpty())) {
					JSONArray sArray = new JSONArray();
					Iterator<SampleQuantity> sIt = hImage.getDrop()
							.getSamples().iterator();
					while (sIt.hasNext()) {
						SampleQuantity sampleQuantity = sIt.next();
						JSONObject sObj = new JSONObject();
						sObj.put("SampleName", sampleQuantity.getSample()
								.getName());
						sObj.put("SampleId", sampleQuantity.getSample().getId());
						sObj.put("SampleQuantity", sampleQuantity.getQuantity()
								+ sampleQuantity.getUnit());
						sArray.add(sObj);
					}

					hObj.put("Samples", sArray);
				}
				if ((hImage.getDrop().getReservoir() != null)
						&& (hImage.getDrop().getReservoir().getCondition() != null)) {
					// TODO what about additives? Note duplicated code with
					// ImageListServlet
					hObj.put("Screen", hImage.getDrop().getReservoir()
							.getCondition().getLocalName());
					hObj.put("Condition", hImage.getDrop().getReservoir()
							.getCondition().getManufacturerName()
							+ ", "
							+ hImage.getDrop().getReservoir().getCondition()
									.getManufacturerScreenName()
							+ " ("
							+ hImage.getDrop().getReservoir().getCondition()
									.getManufacturerCode() + ")");
					Iterator<ComponentQuantity> cIt = hImage.getDrop()
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
					hObj.put("Chemicals", componentArray);
				}
				BusinessCriteria criteria = null;
				// image.getDrop().getReservoir().getCondition().getComponents().get(0).
				ImageService microImageService = dataStorage.getImageService();
				Collection<ImageView> microImages = imageService
						.findViews(criteria);// .findImages(barcode,
												// hImage.getDrop().getWellPosition(),
												// ImageType.ZOOMED, false,
												// null);
				if ((microImages != null) && (!microImages.isEmpty())) {
					JSONArray mArray = new JSONArray();

					Iterator<ImageView> mIt = microImages.iterator();
					while (mIt.hasNext()) {
						ImageView mImage = mIt.next();
						JSONObject mObject = new JSONObject();
						mObject.put("Url", mImage.getUrl());
						mObject.put(
								"Date",
								String.format("%1$td/%1$tm/%1$tY %1$tT",
										mImage.getDate()));
						mObject.put("Plate", mImage.getBarcode());
						mObject.put("Well", mImage.getWell());

						mArray.add(mObject);
					}
					hObj.put("microImages", mArray);
				}

				long timeDifferenceInMilliSec = hImage.getImageDate()
						.getTimeInMillis() - timeZero.getTimeInMillis();
				long timeDifferenceInSeconds = (long) ((double) timeDifferenceInMilliSec / 1000.0);
				long timeDifferenceInHours = (long) ((double) timeDifferenceInSeconds / (60.0 * 60.0));
				String timeDiff = "+" + timeDifferenceInHours + "hrs";
				/*
				 * Interval interval = new Interval(timeZero.getTimeInMillis(),
				 * hImage.getImageDate().getTimeInMillis()); Period period = new
				 * Period(interval.toPeriod()); String timeDiff = ""; if
				 * (period.getYears() != 0) { timeDiff += period.getYears() +
				 * " Years "; if (period.getMonths() != 0) { timeDiff +=
				 * period.getMonths() + " Months"; } } else if
				 * (period.getMonths() != 0) { timeDiff += period.getMonths() +
				 * " Months "; if (period.getWeeks() != 0) { int days =
				 * period.getWeeks() * 7 + period.getDays(); timeDiff += days +
				 * " Days"; } } else if (period.getWeeks() != 0) { int days =
				 * period.getWeeks() * 7 + period.getDays(); timeDiff += days +
				 * " Days"; } else if (period.getDays() != 0) { timeDiff +=
				 * period.getDays() + " Days"; } else if (period.getHours() !=
				 * 0) { timeDiff += period.getHours() + " Hours"; } else if
				 * (period.getMinutes() != 0) { timeDiff = period.getMinutes() +
				 * " Minutes"; } else { timeDiff = "Time Zero"; }
				 */
				/**
				 * TODO: THIS DOES NOT WORK!!
				 */
				// String timeDifference = "+" +
				// period.toStandardHours().getHours() + "hrs";
				hObj.put("Age", timeDiff);
				hObj.put("ImagingSystem", hImage.getLocation().getName() + " ("
						+ hImage.getLocation().getTemperature() + "&deg;C)");

				Collection<ScoreView> hScoreList = scoreService
						.findViews(criteria);
				boolean hHuman = false;
				String hColor = "";
				Iterator<ScoreView> hSit = hScoreList.iterator();
				while (hSit.hasNext()) {
					ScoreView score = hSit.next();
					String tempColor = ColorUtil.convertColorToHex(score
							.getColour());
					// if (score.getHumanAnnotator() != null) {
					hColor = tempColor;
					hHuman = true;
					// } else if (score.getSoftwareAnnotator() != null) {
					// if (!hHuman) {
					// hColor = tempColor;
					// }
					// }

				}
				hObj.put("Color", hColor);
				history.add(hObj);
			}
			JSONObject obj = new JSONObject();
			obj.put("history", history);
			out.print(obj);

		} catch (BusinessException ex) {
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

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
