/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Instrument;

/**
 * 
 * @author ian
 */
public class ListImagersServlet extends XtalPIMSServlet {
	/**
     * 
     */
	private static final long serialVersionUID = 8821496942115654304L;

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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		ReadableVersion version = this.getReadableVersion(request, response);
		try {
			JSONArray array = new JSONArray();
			for (final Instrument instrument : version.getAll(Instrument.class,
					0, 100)) {
				JSONObject obj = new JSONObject();
				// location1.setId(instrument.getDbId());
				obj.put("name", instrument.getName());
				// location1.setPressure(instrument.getPressure());
				obj.put("temperature", instrument.getTemperature());
				array.add(obj);
			}
			JSONObject object = new JSONObject();
			object.put("imagers", array);
			out.print(object);

		} finally {
			version.close();
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
