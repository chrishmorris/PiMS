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

import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class ConstructServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = -819612629559045577L;

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
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String constructId = request.getParameter("id");
		if ((constructId == null) || (constructId.equals(""))) {
			out.close();
			return;
		}
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			ConstructService constructService = dataStorage
					.getConstructService();
			Construct construct = constructService.find(Long
					.parseLong(constructId));
			JSONObject object = new JSONObject();
			object.put(Construct.PROP_ID, construct.getId());
			object.put(Construct.PROP_NAME, construct.getName());
			object.put(Construct.PROP_DESCRIPTION, construct.getDescription());

			object.put("constructLinkURL", construct.getConstructLink()
					.getUrl());
			object.put("constructLinkTitle", construct.getConstructLink()
					.getName());
			if (construct.getGroup() != null) {
				object.put(Construct.PROP_GROUP,
						"<a href='ViewGroup.jsp?group="
								+ construct.getGroup().getName() + "'>"
								+ construct.getGroup().getName() + "</a>");
			} else {
				object.put(Construct.PROP_GROUP, "");
			}
			if (construct.getOwner() != null) {
				object.put(Construct.PROP_OWNER, construct.getOwner()
						.toString());
			} else {
				object.put(Construct.PROP_OWNER, "");
			}
			if (construct.getTarget() != null) {
				object.put(Construct.PROP_TARGET,
						"<a href='ViewTarget.jsp?target="
								+ construct.getTarget().getId() + "'>"
								+ construct.getTarget().getName() + "</a>");
			} else {
				object.put(Construct.PROP_TARGET, "");
			}
			out.print(object);
		} catch (BusinessException ex) {
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
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
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
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
	// </editor-fold>
}
