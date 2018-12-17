/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class ScreenServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = -5043567937091577805L;

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 */
	@SuppressWarnings("unchecked")
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("id");
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			ScreenService screenService = dataStorage.getScreenService();
			Screen screen = screenService.findByName(name);

			JSONObject object = new JSONObject();
			object.put("Name", screen.getName());
			object.put("ManufacturerName", screen.getManufacturerName());
			object.put("Type", screen.getScreenType().toString());
			if (screen.getConditionPositions() != null) {
				JSONArray array = new JSONArray();
				List<WellPosition> keys = new ArrayList<WellPosition>();
				Iterator<WellPosition> it = screen.getConditionPositions()
						.keySet().iterator();
				while (it.hasNext()) {
					keys.add(it.next());
				}

				Collections.sort(keys);
				it = keys.iterator();
				while (it.hasNext()) {
					WellPosition well = it.next();
					JSONObject obj = new JSONObject();
					obj.put("Well", well.toString());
					Condition condition = screen.getConditionPositions().get(
							well);
					if (condition != null) {
						obj.put("LocalName", condition.getLocalName());
						obj.put("LocalNumber", condition.getLocalNumber());
						obj.put("ManufacturerName",
								condition.getManufacturerName());
						obj.put("ManufacturerScreenName",
								condition.getManufacturerScreenName());
						obj.put("ManufacturerCode",
								condition.getManufacturerCode());
						obj.put("ManufacturerCatCode",
								condition.getManufacturerCatalogueCode());
						obj.put("VolatileCondition",
								condition.isVolatileCondition());
						obj.put("FinalpH", condition.getFinalpH());
						obj.put("Id", condition.getId());
						String components = "";
						if ((condition.getComponents() != null)
								&& (!condition.getComponents().isEmpty())) {
							Iterator<ComponentQuantity> cIt = condition
									.getComponents().iterator();
							components += "<table class='list' style='width:100%'><tr><th style='width:50%'>Chemical</th><th>Quantity</th><th>Type</th></tr>";
							while (cIt.hasNext()) {
								ComponentQuantity cq = cIt.next();
								components += "<tr><td>"
										+ cq.getComponent().getChemicalName()
										+ "</td><td>" + cq.getQuantity()
										+ cq.getUnits() + "</td><td>"
										+ cq.getComponentType() + "</td></tr>";
							}
							components += "</table>";
						}
						obj.put("Components", components);
					}

					array.add(obj);
				}

				object.put("records", array);
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
