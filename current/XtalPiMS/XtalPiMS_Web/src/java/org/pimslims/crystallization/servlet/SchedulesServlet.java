package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.pimslims.business.crystallization.service.ScheduleService;
import org.pimslims.business.crystallization.view.ScheduleView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;

/**
 * Servlet implementation class for Servlet: SchedulesServlet
 * 
 */
public class SchedulesServlet extends
		org.pimslims.crystallization.servlet.XtalPIMSServlet {
	static final long serialVersionUID = 1L;

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

		String barcode = request.getParameter(ScoreView.PROP_BARCODE);
		if (null != barcode) {
			barcode = barcode.trim();
		}
		String dateToImage = request
				.getParameter(ScheduleView.PROP_DATE_TO_IMAGE);
		String instrument = request.getParameter(ScheduleView.PROP_IMAGER);
		String priority = request.getParameter(ScheduleView.PROP_PRIORITY);
		String state = request.getParameter(ScheduleView.PROP_STATE);
		String temperature = request
				.getParameter(ScheduleView.PROP_TEMPERATURE);

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			ScheduleService scheduleService = dataStorage.getScheduleService();
			BusinessCriteria criteria = new BusinessCriteria(scheduleService);
			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						ScheduleView.PROP_BARCODE, barcode, true));
				crp.put(ScheduleView.PROP_BARCODE, barcode);
			}
			if ((instrument != null) && (!instrument.equals(""))) {
				criteria.add(BusinessExpression.Like(ScheduleView.PROP_IMAGER,
						instrument, true, true));
				crp.put(ScheduleView.PROP_IMAGER, instrument);
			}
			if ((priority != null) && (!priority.equals(""))
					&& (!priority.equals("all"))) {
				criteria.add(BusinessExpression.Equals(
						ScheduleView.PROP_PRIORITY, priority, true));
				crp.put(ScheduleView.PROP_PRIORITY, priority);
			}
			if ((state != null) && (!state.equals(""))
					&& (!state.equals("all"))) {
				criteria.add(BusinessExpression.Like(ScheduleView.PROP_STATE,
						state, true, true));
				crp.put(ScheduleView.PROP_STATE, state);
			}
			if ((temperature != null) && (!temperature.equals(""))) {
				try {
					Double.parseDouble(temperature);
					criteria.add(BusinessExpression.Equals(
							ScheduleView.PROP_TEMPERATURE, temperature, true));
					crp.put(ScheduleView.PROP_TEMPERATURE, temperature);
				} catch (NumberFormatException e) {
					// Indicative of a dodgy temperature - just swallow it
				}
			}

			// TODO Is this code unnecessary? Is this a hangover of a change
			// from
			// dateToImage to startDate & endDate? JMD thinks so, cos a
			// precision
			// of +-12hours is not enough to identify a specific entry...
			if ((dateToImage != null) && (!dateToImage.equals(""))
					&& (endDate != null) && (!endDate.equals(""))) {
				// Need to parse dates and then add them with a between
				// statement...
				try {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					Calendar calToImage = Calendar.getInstance();

					calToImage.setTime(df.parse(dateToImage));
					criteria.add(BusinessExpression.Equals(
							ScheduleView.PROP_DATE_TO_IMAGE, calToImage, true));
					crp.put("dateToImage", dateToImage);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
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
			dh.addCriterion(criteria, ScheduleView.PROP_DATE_TO_IMAGE,
					calStart, calEnd);

			// Now create the JSON
			JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				Integer count = scheduleService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				Collection<ScheduleView> schedules = scheduleService
						.findViews(criteria);

				JSONArray array = new JSONArray();

				if (schedules != null) {
					Iterator<ScheduleView> it = schedules.iterator();
					while (it.hasNext()) {
						ScheduleView schedule = it.next();

						array.add(schedule.toJSON());
					}
				}

				object.put("records", array);
				object.put("recordsReturned", schedules.size());
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
						"schedulesDataTable");
			}
			closeResources(dataStorage);
		}
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
