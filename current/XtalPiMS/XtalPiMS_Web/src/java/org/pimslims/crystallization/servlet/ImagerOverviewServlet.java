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
public class ImagerOverviewServlet extends XtalPIMSServlet {

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
		InstallationProperties props = ModelImpl.getInstallationProperties();
		String formulatrixBaseUrl = props
				.getProperty("imagerJSON.formulatrix.baseUrl");
		if (null == formulatrixBaseUrl) {
			formulatrixBaseUrl = "";
		}
		String rigakuBaseUrl = props.getProperty("imagerJSON.rigaku.baseUrl");
		if (null == rigakuBaseUrl) {
			rigakuBaseUrl = "";
		}
		request.setAttribute("formulatrixBaseUrl", formulatrixBaseUrl);
		request.setAttribute("rigakuBaseUrl", rigakuBaseUrl);
		RequestDispatcher rd = request
				.getRequestDispatcher("/ImagerOverview.jsp");
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
		return "Short description";
	}
	// </editor-fold>
}
