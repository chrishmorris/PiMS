/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.business.exception.BusinessException;

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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException 
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json");
        PrintWriter out = response.getWriter();
    	DataStorage dataStorage=null;
        try {
        	dataStorage= openResources(request);
            LocationService locationService = dataStorage.getLocationService();
            Collection<Location> locations = locationService.findAll(null);
            Iterator<Location> it = locations.iterator();
            JSONArray array = new JSONArray();
            while (it.hasNext()) {
                Location location = it.next();
                JSONObject obj = new JSONObject();
                obj.put("name", location.getName());
                obj.put("temperature", location.getTemperature());
                array.add(obj);
            }
            JSONObject object = new JSONObject();
            object.put("imagers", array);
            out.print(object);
            
        } catch (BusinessException ex) {
            Logger.getLogger(ListImagersServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
            closeResources(dataStorage);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
