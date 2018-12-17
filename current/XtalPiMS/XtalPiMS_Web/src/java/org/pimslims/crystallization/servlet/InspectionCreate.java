/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 * 
 *          Record a microscope inspection
 */
public class InspectionCreate extends PIMSServlet {

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
	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		final ReadableVersion rv = this.getReadableVersion(request, response);
		try {
			// ---------------------------------------------------------------------
			// general holder info
			final String holderName = request.getParameter("holder");
			final Holder holder = rv.findFirst(Holder.class, Holder.PROP_NAME,
					holderName);
			request.setAttribute("holder", holder);

			// inpsection time
			final Calendar inspectionTime = Calendar.getInstance();
			request.setAttribute("inspectionTime", inspectionTime);

			// new inspection name
			String inspectionName = holder.getName();
			// year,month,date
			inspectionName += "-" + inspectionTime.get(Calendar.YEAR)
					+ inspectionTime.get(Calendar.MONTH)
					+ inspectionTime.get(Calendar.DAY_OF_MONTH);
			// hour,min,sec
			inspectionName += "-" + inspectionTime.get(Calendar.HOUR_OF_DAY)
					+ inspectionTime.get(Calendar.MINUTE)
					+ inspectionTime.get(Calendar.SECOND);
			request.setAttribute("inspectionName", inspectionName);

			// Imagers
			final Collection<Instrument> imagers = rv.getAll(Instrument.class);
			request.setAttribute("imagers", imagers);

			Instrument defaultImager = findImager(rv);
			request.setAttribute("defaultImagerID", defaultImager.getDbId());

			// ----------------------------------------------------------------------------
			// Finished & forward
			//
			rv.commit();
			final RequestDispatcher rd = request
					.getRequestDispatcher("/InspectionCreate.jsp");
			rd.forward(request, response);

		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!rv.isCompleted()) {
				rv.abort();
			}
		}
	}

	// TODO what if they have two microscopes, which are significantly
	// different?
	private Instrument findImager(final ReadableVersion rv)
			throws ServletException {
		final Instrument defaultImager = rv.findFirst(Instrument.class,
				Instrument.PROP_NAME, "Microscope");
		if (defaultImager == null) {
			throw new ServletException(
					"Can not find an Instrument called 'Microscope'. Please contact your xtalPiMS administrator.");
		}
		return defaultImager;
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
		processPostRequest(request, response);
	}

	private void processPostRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final WritableVersion wv = getWritableVersion(request, response);

		try {
			final String plateID = request.getParameter("holderID");
			final Holder holder = wv.get(Holder.class, new Long(plateID));
			// name
			final String inspectionName = request
					.getParameter("inspectionName");
			// Screen
			final Instrument imager = wv.get(Instrument.class,
					new Long(request.getParameter("imagerID")));
			// time
			final Calendar time = Calendar.getInstance();
			wv.setDefaultOwner(holder.getAccess());
			final ScheduledTask newInspection = new ScheduledTask(wv,
					inspectionName, time, holder);
			newInspection.setCompletionTime(time);
			newInspection.setInstrument(imager);
			newInspection.setDetails(request.getParameter("description"));

			wv.commit();
			this.redirect(response, request.getContextPath()
					+ "/Update/Inspection?name=" + inspectionName);
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!wv.isCompleted()) {
				wv.abort();
			}
		}

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
