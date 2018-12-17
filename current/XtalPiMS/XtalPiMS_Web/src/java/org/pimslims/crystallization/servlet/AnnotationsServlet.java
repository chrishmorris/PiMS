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
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.BusinessOrder;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.HumanScoreService;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;

/**
 * 
 * @author Ian Berry, Jon Diprose
 */
public class AnnotationsServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3171873997158438725L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws javax.servlet.ServletException
	 *             error
	 * @throws java.io.IOException
	 *             error
	 */
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final CommonRequestParams crp = CommonRequestParams
				.parseRequest(request);

		String barcode = request.getParameter(ScoreView.PROP_BARCODE); // often
																		// specified
		if (null != barcode) {
			barcode = barcode.trim();
		}
		final String startDate = request.getParameter("startDate"); // usually
																	// today
		final String endDate = request.getParameter("endDate"); // not seen in
																// use
		final String description = request
				.getParameter(ScoreView.PROP_DESCRIPTION); // not seen in use
		final String inspectionName = request
				.getParameter(ScoreView.PROP_INSPECTION_NAME); // not seen in
																// use
		final String name = request.getParameter(ScoreView.PROP_NAME); // not
																		// seen
																		// in
																		// use
		final String type = request.getParameter(ScoreView.PROP_TYPE); // not
																		// seen
																		// in
																		// use
		final String version = request.getParameter(ScoreView.PROP_VERSION); // not
																				// seen
																				// in
																				// use
		final String well = request.getParameter(ScoreView.PROP_WELL); // not
																		// seen
																		// in
																		// use
		final String sampleName = request
				.getParameter(ScoreView.PROP_SAMPLE_NAME); // not seen in use
		// TODO Add sampleId, constructName, constructId, groupName

		DataStorage dataStorage = null;
		try {

			dataStorage = openResources(request);

			final HumanScoreService humanScoreService = dataStorage
					.getHumanScoreService();

			final BusinessCriteria criteria = new BusinessCriteria(
					humanScoreService);

			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE,
						barcode, true));
				crp.put(ScoreView.PROP_BARCODE, barcode);
			}
			if ((description != null) && (!description.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ScoreView.PROP_DESCRIPTION, description, true, true));
				crp.put(ScoreView.PROP_DESCRIPTION, description);
			}
			if ((inspectionName != null) && (!inspectionName.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ScoreView.PROP_INSPECTION_NAME, inspectionName, true,
						true));
				crp.put(ScoreView.PROP_INSPECTION_NAME, inspectionName);
			}
			if ((name != null) && (!name.equals(""))) {
				criteria.add(BusinessExpression.Like(ScoreView.PROP_NAME, name,
						true, true));
				crp.put(ScoreView.PROP_NAME, name);
			}
			if ((type != null) && (!type.equals(""))) {
				criteria.add(BusinessExpression.Equals(ScoreView.PROP_TYPE,
						type, true));
				crp.put(ScoreView.PROP_TYPE, type);
			}
			if ((version != null) && (!version.equals(""))) {
				criteria.add(BusinessExpression.Like(ScoreView.PROP_VERSION,
						version, true, true));
				crp.put(ScoreView.PROP_VERSION, version);
			}
			if ((well != null) && (!well.equals(""))) {
				WellPosition pos = new WellPosition(well);
				criteria.add(BusinessExpression.Equals("row", pos.getRow(),
						true));
				criteria.add(BusinessExpression.Equals("col", pos.getColumn(),
						true));
				criteria.add(BusinessExpression.Equals("sub",
						pos.getSubPosition(), true));
				crp.put(ScoreView.PROP_WELL, well);
			}
			if ((sampleName != null) && (!sampleName.equals(""))) {
				criteria.add(BusinessExpression.Like(
						ScoreView.PROP_SAMPLE_NAME, sampleName, true, true));
				crp.put(ScoreView.PROP_SAMPLE_NAME, sampleName);
			}

			/*
			 * DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); String today
			 * = df.format(new Date()); if (((startDate != null) &&
			 * (!startDate.equals("")) && (endDate != null) &&
			 * (!endDate.equals(""))) && ((!startDate.equals("1/1/2000")) &&
			 * (!endDate .equals(today)))) { // Need to parse dates and then add
			 * them with a between // statement... try { Calendar startCal =
			 * Calendar.getInstance(); Calendar endCal = Calendar.getInstance();
			 * startCal.setTime(df.parse(startDate));
			 * endCal.setTime(df.parse(endDate)); endCal.add(Calendar.DATE, 1);
			 * startCal.add(Calendar.DATE, -1);
			 * criteria.add(BusinessExpression.Between( ScoreView.PROP_DATE,
			 * startCal, endCal)); crp.getSessionBookmark().append("_startDate-"
			 * + startDate); crp.getSessionBookmark().append("_endDate-" +
			 * endDate); } catch (ParseException ex) { ex.printStackTrace(); } }
			 */

			final DateHandler dh = new DateHandler();
			Calendar calStart = null;
			Calendar calEnd = null;
			try {
				calStart = dh.parseDate(startDate);
				crp.put("startDate", startDate);
			} catch (final ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			try {
				calEnd = dh.parseDate(endDate);
				crp.put("endDate", endDate);
			} catch (final ParseException e) {
				// Indicative of a dodgy date - just swallow it
			}
			dh.addCriterion(criteria, ScoreView.PROP_DATE, calStart, calEnd);

			// TODO a lot of copies of this code, it could be a method

			final JSONObject object = new JSONObject();

			// if count wanted .... but never is
			if (null != request.getParameter("count")) {
				final Integer count = humanScoreService.findViewCount(criteria);
				object.put("count", count);
			}

			else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				final Collection<ScoreView> scores = humanScoreService
						.findViews(criteria);

				final JSONArray array = new JSONArray();

				if (null != scores) {
					final Iterator<ScoreView> it = scores.iterator();
					while (it.hasNext()) {
						final ScoreView score = it.next();

						array.add(score.toJSON());
					}
				}

				object.put("records", array);
				object.put("recordsReturned", scores.size());
			}

			this.setContentTypeJson(response);
			this.setNoCache(response);
			response.getWriter().print(object);

		} catch (final BusinessException ex) {

			throw new ServletException(ex);

		} finally {
			// Only store for the non-count request!
			// TODO no never store, because never seems to be retrieved
			if (null == request.getParameter("count")) {
				crp.storeSessionBookmark(request.getSession(),
						"annotationsDataTable" // TODO null for no store
				);
			}
			closeResources(dataStorage);
		}
		/*
		 * HumanScoreService scoreService =
		 * this.getDataStorage().getHumanScoreService(); BusinessCriteria
		 * criteria = new BusinessCriteria(scoreService);
		 * criteria.setMaxResults(this.getMaxResults());
		 * criteria.setFirstResult(this.getFirstResult()); if
		 * (this.isAscending()) {
		 * criteria.addOrder(BusinessOrder.ASC(this.getSort())); } else {
		 * criteria.addOrder(BusinessOrder.DESC(this.getSort())); } Calendar cal
		 * = Calendar.getInstance(); cal.setTime(new Date()); List<ScoreView>
		 * scores = null; String construct = request.getParameter("construct");
		 * String barcode = request.getParameter("barcode"); String group =
		 * request.getParameter("group"); PlateInspectionService
		 * plateInspectionService =
		 * this.getDataStorage().getPlateInspectionService(); if (group != null)
		 * { GroupService groupService =
		 * this.getDataStorage().getGroupService(); scores = (List<ScoreView>)
		 * scoreService.findViews(criteria); } else if (barcode != null) {
		 * scores = (List<UserScore>) scoreService.findViews(criteria); } else
		 * if (construct != null) { ConstructService constructService =
		 * this.getDataStorage().getConstructService(); scores =
		 * (List<UserScore>) scoreService.findViews(criteria); } else { //a user
		 * scores = (List<UserScore>) scoreService.findViews(criteria); }
		 * JSONObject object = new JSONObject(); if
		 * (request.getParameter("count") != null) { object.put("count",
		 * scores.size()); } else { JSONArray array = new JSONArray();
		 * Iterator<UserScore> it = scores.iterator(); while (it.hasNext()) {
		 * UserScore score = it.next(); JSONObject scobj = new JSONObject();
		 * scobj.put("Barcode", score.getDrop().getPlate().getBarcode());
		 * scobj.put("Well", score.getDrop().getWellPosition().toString());
		 * scobj.put("Date", score.getDate().getTimeInMillis());
		 * scobj.put("Description", score.getValue().getDescription());
		 * scobj.put("Color",
		 * ColorUtil.convertColorToHex(score.getValue().getColour()));
		 * scobj.put("Name", score.getUser().toString()); scobj.put("Version",
		 * "-"); scobj.put("Type", "Human"); //@TODO put back in //
		 * PlateInspection plateInspection =
		 * plateInspectionService.findLatestByPlate
		 * (score.getDrop().getPlate().getBarcode()); // scobj.put("Inspection",
		 * plateInspection.getInspectionName()); array.put(scobj); }
		 * object.put("annotations", array); } out.print(object); } catch
		 * (BusinessException ex) { ex.printStackTrace(); } catch (JSONException
		 * ex) { ex.printStackTrace(); } finally { out.close(); }
		 */
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
		return "Annotations Servlet";
	}
	// </editor-fold>
}
