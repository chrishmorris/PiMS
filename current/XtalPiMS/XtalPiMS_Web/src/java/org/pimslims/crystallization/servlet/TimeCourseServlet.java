/*
 * TimeCourseServlet.java Created on 26 September 2007, 13:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.pimslims.business.crystallization.model.Image;
import org.pimslims.business.crystallization.model.ImageType;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 * @version
 */
@Deprecated
// I don't think this is in use, it seems to be ImagesServlet now - Chris
public class TimeCourseServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 3243023042369805263L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String barcode = request.getParameter("barcode");
			String well = request.getParameter("well");
			WellPosition wellPosition = new WellPosition(well);

			JSONArray history = new JSONArray();
			ImageType imageInfo = ImageType.COMPOSITE;
			ImageService imageService = dataStorage.getImageService();
			HumanScoreService scoreService = dataStorage.getHumanScoreService();
			// isn't that a bit slow?
			List<Image> historyList = (List<Image>) imageService.findAll(null);

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

			Iterator<Image> hit = historyList.iterator();
			while (hit.hasNext()) {
				Image hImage = hit.next();
				JSONObject hObj = new JSONObject();
				hObj.put("URL", hImage.getImagePath());
				hObj.put("Barcode", barcode);
				hObj.put("Well", hImage.getDrop().getWellPosition().toString());
				hObj.put("Date", hImage.getImageDate().getTimeInMillis());
				Collection<ScoreView> hScoreList = scoreService.findViews(null);
				boolean hHuman = false;
				String hColor = "";
				Iterator<ScoreView> hSit = hScoreList.iterator();
				while (hSit.hasNext()) {
					ScoreView score = hSit.next();
					String tempColor = ColorUtil.convertColorToHex(score
							.getColour());

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
			out.print(history);

			out.close();

		} catch (BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			closeResources(dataStorage);
		}

	}

	// protected void processRequest2(HttpServletRequest request,
	// HttpServletResponse response)
	// throws ServletException, IOException {
	// response.setContentType("text/html;charset=UTF-8");
	// PrintWriter out = response.getWriter();
	// ViewWells2 viewWells =
	// (ViewWells2)request.getSession().getAttribute("viewWells");
	//
	//
	// String indexStr = request.getParameter("index");
	// int index = -1;
	// if (indexStr != null) {
	// index = Integer.parseInt(indexStr);
	// }
	// int imageWidth = 100; // 0 means the actual image width! buut the default
	// is 100!
	// if (request.getParameter("width") != null) {
	// imageWidth = Integer.parseInt(request.getParameter("width"));
	// }
	// try {
	//
	// List<Image> imageList = viewWells.getTimeCourse(index, dataStorage);
	// ScoreService scoreService = dataStorage.getScoreService();
	// PlateInspectionService plateInspectionService =
	// dataStorage.getPlateInspectionService();
	// out.println("<table id='timecourse_table' class='list' style='empty-cells:show;border:0;'>");
	// out.println("<tr>");
	// out.println("<th>");
	// out.println("<span style='float: left;'>Timecourse</span>");
	// out.println("<span style='float: right;'>");
	// out.println("<img src='" + request.getContextPath() +
	// "/images/pdf.png' alt='PDF |' title='Create PDF of this search' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;'/>");
	//
	// out.println("<img src='" + request.getContextPath() +
	// "/images/fileprint.png' alt='Print |' title='Print this search' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;'/>");
	//
	// out.println("<img src='" + request.getContextPath() +
	// "/images/mail_forward.png' alt='Mail |' title='Mail Link to Search to Someone' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;'/>");
	//
	// out.println("<img src='" + request.getContextPath() +
	// "/images/network.png' alt='Link' title='Direct Link to search' style='border:0; padding-right: 3px; padding-left: 3px; vertical-align: text-bottom;'/>");
	// out.println("</span>");
	//
	//
	// out.println("</th>");
	// out.println("</tr>");
	// out.println("<tr>");
	// out.println("<td>");
	//
	// out.println("<table style='border:0'><tr>");
	//
	// Iterator<Image> it = imageList.iterator();
	// while (it.hasNext()) {
	// Image image = it.next();
	//
	// List<SoftwareScore> autoScores = scoreService.findSoftwareScores(image);
	// List<SoftwareScore> humanScores = scoreService.findHumanScores(image);
	// String colourString = "#FFFFFF";
	// if ((humanScores != null) && (!humanScores.isEmpty())) {
	// //can only take the first one for now!
	// colourString =
	// ColorUtil.convertColorToHex(humanScores.get(0).getValue().getColour());
	// } else if ((autoScores != null) && (!autoScores.isEmpty())) {
	// colourString =
	// ColorUtil.convertColorToHex(autoScores.get(0).getValue().getColour());
	// }
	//
	// out.println("<td class='hoverbox' style='background-color:" +
	// colourString + ";padding:2px'>");
	// out.println("<a href='#'");
	// String plateInspectionName =
	// plateInspectionService.findByImage(image).getInspectionName();
	// String style = "";
	//
	// //out.println("<img id='" + id + "' " + style + " onclick='show" +
	// listType + "Image(\"" + listType + "\",\"" +
	// image.getPlate().getBarcode().getBarcode() + "\",\"" +
	// image.getWellPosition().getWellAsString() + "\",\"" +
	// image.getWellPosition().getSubPosition() + "\",\"" + plateInspectionName
	// + "\")' width='" + imageWidth + "' src='" + image.getImageURLAsString() +
	// "' alt='" + image.getImageURLAsString() + "'/>");
	// //out.println("<img class='preview' onclick='show" + listType +
	// "Image(\"" + listType + "\",\"" +
	// image.getPlate().getBarcode().getBarcode() + "\",\"" +
	// image.getWellPosition().getWellAsString() + "\",\"" +
	// image.getWellPosition().getSubPosition() + "\",\"" + plateInspectionName
	// + "\")' width='" + imageWidth + "' src='" + image.getImageURLAsString() +
	// "' alt='" + image.getImageURLAsString() + "'/>");
	// out.println("<img " + style + " width='" + imageWidth + "' src='" +
	// image.getImageURLAsString() + "' alt='" + image.getImageURLAsString() +
	// "'/>");
	// out.println("<img class='preview' width='" + imageWidth + "' src='" +
	// image.getImageURLAsString() + "' alt='" + image.getImageURLAsString() +
	// "'/>");
	//
	//
	// out.println("</a>");
	// out.println("</td>");
	//
	//
	// }
	// out.println("</tr></table>");
	// out.println("</tr></table>");
	//
	// } catch (BusinessException ex) {
	// ex.printStackTrace();
	// }
	//
	//
	//
	//
	// }

	// protected void processRequest2(HttpServletRequest request,
	// HttpServletResponse response)
	// throws ServletException, IOException {
	// response.setContentType("text/html;charset=UTF-8");
	// PrintWriter out = response.getWriter();
	// ImageInfo imageInfo = new ImageInfo(ImageInfo.ImageQuality.LOW_RES,
	// ImageInfo.ImageType.COMPOSITE);
	// String barcode = request.getParameter("barcode");
	// // String well = request.getParameter("well");
	// // String subposition = request.getParameter("sub");
	// // WellPosition wellPosition = new WellPosition();
	// // wellPosition.setWell(well);
	// // wellPosition.setSubPosition(subposition);
	// //FacesContext fc = FacesContext.getCurrentInstance();
	// String inspectionName = request.getParameter("name");
	// try {
	// //DataStorage dataStorage = DataStorageFactory.getDataStorage();
	// //XtalSessionBean sessionBean = (XtalSessionBean)
	// fc.getApplication().createValueBinding("#{XtalSessionBean}").getValue(fc);
	//
	//
	// PlateExperimentService plateExperimentService =
	// viewdataStorage.getPlateExperimentService();
	// PlateExperimentView plateExperiment =
	// plateExperimentService.findByPlate(barcode).get(0);
	// FIX THIS
	// PlateInspectionService plateInspectionService =
	// dataStorage.getPlateInspectionService();
	// PlateInspection plateInspection =
	// plateInspectionService.findByInspectionName(inspectionName);
	// ImageService imageService = dataStorage.getImageService();
	// List<Image> timeCourseImages =
	// imageService.findImages(plateExperiment.getPlate().getBarcode(),
	// plateInspection, imageInfo);
	//
	// //ImageService imageService = dataStorage.getImageService();
	// //List<Image> timeCourseImages = imageService.findAllImages(new
	// Barcode(barcode), wellPosition, imageInfo);
	// Iterator<Image> imageIt = timeCourseImages.iterator();
	// int j = 0;
	// int c = 0;
	// out.println("<table border='0'><tr>");
	// Random generator = new Random();
	// while (imageIt.hasNext()) {
	// Image timeCourseImage = imageIt.next();
	// if (j == 12) {
	// j = 1;
	// out.print("</tr><tr>");
	// } else {
	// j++;
	// }
	//
	// int r = generator.nextInt( 256 );
	// int g = generator.nextInt( 256 );
	// int b = generator.nextInt( 256 );
	// Color color = new Color(r, g, b);
	// Integer i = color.getRed();
	// String red = toHexString(color.getRed(), 2, '0');
	// String green = toHexString(color.getGreen(), 2, '0');
	// String blue = toHexString(color.getBlue(), 2, '0');
	//
	// String colourString = "#" + red + green + blue;
	// out.println("<td style='background-color:" + colourString +
	// ";padding:2px'><img onclick='ss.goto_slide(#{rowIndex})' width='25' src='"
	// + timeCourseImage.getImageURLAsString() + "' alt='" +
	// timeCourseImage.getImageURLAsString() + "'/></td>");
	// c++;
	// }
	// out.println("</tr></table>");
	// } catch (ReferenceSyntaxException ex) {
	// ex.printStackTrace();
	// } catch (EvaluationException ex) {
	// ex.printStackTrace();
	// } catch (BusinessException ex) {
	// ex.printStackTrace();
	// }
	//
	// out.close();
	// }

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
