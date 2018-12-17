/**
 * 
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Jon Diprose, Bill Lin
 * 
 */
public class ViewTrialDropsServlet extends PIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = -438790991838955887L;

	public static final String VTD_PREF = "vtd";

	// private static final String VTD_DEFAULT = "3";

	// private static String vtd = VTD_DEFAULT;

	/**
	 * @return the vtd
	 * 
	 *         public static String getVtd() { return vtd; }
	 */

	/**
	 * @param vtd
	 *            the vtd to set
	 * 
	 *            public static void setVtd(final String vtd) {
	 *            ViewTrialDropsServlet.vtd = vtd; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {

		// calculate links for sub positions
		final String barcode = req.getParameter("barcode");
		String subposition = req.getParameter("subPosition");
		// default subPosition
		if (subposition == null || subposition.length() == 0) {
			subposition = "1";
		}
		final int subIntPosition = new Integer(subposition);
		String link = "";
		String inspectionName = null;
		for (final Object key : req.getParameterMap().keySet()) {
			if (!"subPosition".equalsIgnoreCase(key.toString())) {
				link = link + key + "=" + req.getParameter(key.toString())
						+ "&amp;";
			}
			if ("name".equalsIgnoreCase(key.toString())) {
				inspectionName = req.getParameter(key.toString());
			}
		}
		final ReadableVersion rv = this.getReadableVersion(req, resp);
		try {
			final Holder holder = rv.findFirst(Holder.class, Holder.PROP_NAME,
					barcode);
			if (holder != null) {
				final HolderType type = (CrystalType) holder.getHolderType();
				final int subs = type.getMaxSubPosition();
				if (subs > 2) {
					final Map<String, String> subLinks = new LinkedHashMap<String, String>();
					for (int i = 1; i < subs; i++) {
						// // or if i!=holderType.getResSubPosition()
						if (i != subIntPosition) {
							subLinks.put("View Subposition " + i, link
									+ "subPosition=" + i);
						}
					}
					if (subIntPosition > 0) {
						subLinks.put("View All Subpositions ", link
								+ "&subPosition=-1");
					}
					req.setAttribute("subPositionLinks", subLinks);
				}

				// inspection name

				final ScheduledTask lastInspection = getLastInspectionName(holder);
				req.setAttribute("inspection", lastInspection);

			}
		} finally {
			rv.abort();
		}

		req.getRequestDispatcher("/ViewTrialDrops" + /* pref + */".jsp")
				.forward(req, resp);
	}

	private ScheduledTask getLastInspectionName(final Holder holder) {
		ScheduledTask lastInspection = null;
		Calendar lastDate = null;
		for (final ScheduledTask inspection : holder.getScheduledTasks()) {
			if (inspection.getCompletionTime() != null) {
				if (lastDate == null) {
					lastDate = inspection.getCompletionTime();
					lastInspection = inspection;

				} else if (inspection.getCompletionTime().after(lastDate)) {
					lastDate = inspection.getCompletionTime();
					lastInspection = inspection;

				}
			}
		}
		return lastInspection;
	}

	@Override
	public String getServletInfo() {
		return "redirect view trialdrops";
	}
}
