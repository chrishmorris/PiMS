/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ModelImpl;
import org.pimslims.util.InstallationProperties;

/**
 * 
 * @author ian
 */
public class ImagerUsageServlet extends XtalPIMSServlet {

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
		String serial = request.getParameter("serial");
		InstallationProperties props = ModelImpl.getInstallationProperties();
		String formulatrixBaseUrl = props
				.getProperty("imagerJSON.formulatrix.baseUrl");
		if (null == formulatrixBaseUrl) {
			formulatrixBaseUrl = "";
		}
		request.setAttribute("formulatrixBaseUrl", formulatrixBaseUrl);
		request.setAttribute("serial", serial);
		RequestDispatcher rd = request.getRequestDispatcher("/ImagerUsage.jsp");
		rd.forward(request, response);
	}

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
		throw new ServletException("POST not supported");
	}

	/**
	 * Returns a short description of the servlet.
	 */
	@Override
	public String getServletInfo() {
		return "Serves a page that fetches imager usage information over a specified period, broken down by imager and user.";
	}
	// </editor-fold>
}
