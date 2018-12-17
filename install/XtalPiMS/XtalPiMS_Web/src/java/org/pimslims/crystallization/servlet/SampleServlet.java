/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class SampleServlet extends XtalPIMSServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 7683389290849457621L;

	/**
	 * TODO needs proper error handling - must pass exception to client
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
		String sampleId = request.getParameter("sample");
		DataStorage dataStorage = null;
		try {
			dataStorage = openResources(request);
			SampleService sampleService = dataStorage.getSampleService();
			Sample sample = sampleService.find(Long.parseLong(sampleId));
			JSONObject object = new JSONObject();
			object.put(Sample.PROP_ID, sample.getId());
			object.put(Sample.PROP_NAME, sample.getName());
			object.put(Sample.PROP_DESCRIPTION, sample.getDescription());
			object.put(Sample.PROP_PH, sample.getPH());
			object.put(Sample.PROP_MOLECULAR_WEIGHT,
					sample.getMolecularWeight());
			object.put(Sample.PROP_NUM_SUB_UNITS, sample.getNumSubUnits());
			object.put(Sample.PROP_BATCH_REFERENCE, sample.getBatchReference());
			object.put(Sample.PROP_ORIGIN, sample.getOrigin());
			object.put(Sample.PROP_TYPE, sample.getType());
			object.put(Sample.PROP_CELLULAR_LOCATION,
					sample.getCellularLocation());
			object.put(Sample.PROP_CONCENTRATION, sample.getConcentration());
			object.put(Sample.PROP_GI_NUMBER, sample.getGiNumber());
			object.put(Sample.PROP_SAFETY_INFORMATION, "");
			object.put("ConstructLink", sample.getConstructLink().getUrl());
			object.put("ConstructLinkText", sample.getConstructLink().getName());
			object.put("SampleLink", sample.getSampleLink().getUrl());
			object.put("SampleLinkText", sample.getSampleLink().getName());
			object.put("TargetLink", sample.getTargetLink().getUrl());
			object.put("TargetLinkText", sample.getTargetLink().getName());
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
