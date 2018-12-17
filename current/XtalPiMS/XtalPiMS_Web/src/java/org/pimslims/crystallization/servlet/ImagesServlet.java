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
import org.pimslims.business.crystallization.service.ImageService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.servlet.util.DateHandler;
import org.pimslims.crystallization.util.ImageURL;

/**
 * 
 * @author ian
 */
public class ImagesServlet extends XtalPIMSServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7415769129526677757L;

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

		String barcode = request.getParameter(ScoreView.PROP_BARCODE);
		if (null != barcode) {
			barcode = barcode.trim();
		}
		final String well = request.getParameter(ImageView.PROP_WELL);
		final String startDate = request.getParameter("startDate");
		final String endDate = request.getParameter("endDate");
		final String inspectionName = request
				.getParameter(ImageView.PROP_INSPECTION_NAME);
		final String instrument = request
				.getParameter(ImageView.PROP_INSTRUMENT);
		final String temperature = request
				.getParameter(ImageView.PROP_TEMPERATURE);
		final String type = request.getParameter(ImageView.PROP_TYPE);
		final String screen = request.getParameter(ImageView.PROP_SCREEN);
		final String description = request
				.getParameter(ImageView.PROP_DESCRIPTION);
		// samples?
		// conditions?
		// components?
		// annotations?

		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);

			final ImageService imageService = dataStorage.getImageService();

			final BusinessCriteria criteria = new BusinessCriteria(imageService);

			prepareSearchCriteria(crp, barcode, well, inspectionName,
					instrument, temperature, type, screen, description,
					criteria);

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
			dh.addCriterion(criteria, ImageView.PROP_DATE, calStart, calEnd);

			final JSONObject object = new JSONObject();
			if (request.getParameter("count") != null) {
				final Integer count = imageService.findViewCount(criteria);

				object.put("count", count);
			} else {
				criteria.setMaxResults(crp.getMaxResults());
				criteria.setFirstResult(crp.getFirstResult());
				if (crp.isAscending()) {
					criteria.addOrder(BusinessOrder.ASC(crp.getSort()));
				} else {
					criteria.addOrder(BusinessOrder.DESC(crp.getSort()));
				}

				final Collection<ImageView> images = imageService
						.findViews(criteria);
				final JSONArray array = new JSONArray();
				if (images != null) {
					final Iterator<ImageView> it = images.iterator();
					while (it.hasNext()) {
						final ImageView image = it.next();

						final JSONObject obj = image.toJSON();

						// this is obsolete, only used by PlateDb
						// could
						// this.getServletContext.getAttribute("ImageURL");
						if (image.getImageType().equals(ImageView.TYPE_ZOOMED)) {
							obj.put(ImageView.PROP_URL,
									ImageURL.getUrl_zoomedimages()
											+ image.getUrl());
						} else if (image.getImageType().equals(
								ImageView.TYPE_COMPOSITE)) {
							obj.put(ImageView.PROP_URL,
									ImageURL.getUrl_compositeimages()
											+ image.getUrl());
						} else if (image.getImageType().equals(
								ImageView.TYPE_SLICE)) {
							obj.put(ImageView.PROP_URL,
									ImageURL.getUrl_sliceimages()
											+ image.getUrl());
						}

						array.add(obj);
					}
				}

				object.put("records", array);
				object.put("recordsReturned", images.size());
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
						"imagesDataTable");
			}
			closeResources(dataStorage);
		}

	}

	private void prepareSearchCriteria(final CommonRequestParams crp,
			final String barcode, final String well,
			final String inspectionName, final String instrument,
			final String temperature, final String type, final String screen,
			final String description, final BusinessCriteria criteria)
			throws BusinessException {
		if ((barcode != null) && (!barcode.equals(""))) {
			criteria.add(BusinessExpression.Equals(ImageView.PROP_BARCODE,
					barcode, true));
			crp.put(ImageView.PROP_BARCODE, barcode);
		}
		if ((well != null) && (!well.equals(""))) {
			criteria.add(BusinessExpression.Equals(ImageView.PROP_WELL, well,
					true));
			crp.put(ImageView.PROP_WELL, well);
		}
		if ((inspectionName != null) && (!inspectionName.equals(""))) {
			criteria.add(BusinessExpression.Equals(
					ImageView.PROP_INSPECTION_NAME, inspectionName, true));
			crp.put(ImageView.PROP_INSPECTION_NAME, inspectionName);
		}
		if ((instrument != null) && (!instrument.equals(""))) {
			criteria.add(BusinessExpression.Like(ImageView.PROP_INSTRUMENT,
					instrument, true, true));
			crp.put(ImageView.PROP_INSTRUMENT, instrument);
		}
		if ((description != null) && (!description.equals(""))) {
			criteria.add(BusinessExpression.Like(ImageView.PROP_DESCRIPTION,
					description, true, true));
			crp.put(ImageView.PROP_DESCRIPTION, description);
		}
		if ((type != null) && (!type.equals(""))) {
			criteria.add(BusinessExpression.Equals(ImageView.PROP_TYPE, type,
					true));
			crp.put(ImageView.PROP_TYPE, type);
		}
		if ((temperature != null) && (!temperature.equals(""))) {
			criteria.add(BusinessExpression.Equals(ImageView.PROP_TEMPERATURE,
					temperature, true));
			crp.put(ImageView.PROP_TEMPERATURE, temperature);
		}
		if ((screen != null) && (!screen.equals(""))) {
			criteria.add(BusinessExpression.Equals(ImageView.PROP_SCREEN,
					screen, true));
			crp.put(ImageView.PROP_SCREEN, screen);
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
