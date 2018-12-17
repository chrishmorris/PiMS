/*
 * ListAnnotationsServlet.java Created on 19 October 2007, 10:30
 */

package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.crystallization.model.ScoringScheme;
import org.pimslims.business.crystallization.service.ScoringSchemeService;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class ListAnnotationsServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = -2005304793194712990L;

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
		final String scheme = request.getParameter("scheme");
		response.setContentType("text/html;charset=UTF-8");
		final PrintWriter out = response.getWriter();
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			out.println("<select id='annotation' onchange='annotate(\""
					+ scheme
					+ "\");' style='font-family:arial,helvetica,sans-serif;font-size: 12px; font-weight: normal;'>");
			out.println("<option value='' selected='selected'></option>");
			final ScoringSchemeService scoringSchemeService = dataStorage
					.getScoringSchemeService();
			final ScoringScheme scoringScheme = scoringSchemeService
					.findByName(scheme);
			if (scoringScheme != null) {
				final Iterator<ScoreValue> it = scoringScheme.getScores()
						.iterator();
				while (it.hasNext()) {
					final ScoreValue scoreValue = it.next();
					out.println("<option id='annotation_"
							+ scoreValue.getDescription()
							+ "' value='"
							+ scoreValue.getDescription()
							+ "' style='background-color: "
							+ ColorUtil.convertColorToHex(scoreValue
									.getColour()) + "'>"
							+ scoreValue.getDescription() + "</option>");
				}

				out.println("</select>");
			}
		} catch (final BusinessException ex) {
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
