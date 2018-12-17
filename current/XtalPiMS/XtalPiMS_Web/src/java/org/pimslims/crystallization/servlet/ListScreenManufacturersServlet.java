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
import org.pimslims.business.crystallization.service.ScreenService;
import org.pimslims.business.exception.BusinessException;



/**
 *
 * @author ian
 */
public class ListScreenManufacturersServlet extends XtalPIMSServlet {
   
    /**
     * 
     */
    private static final long serialVersionUID = 5080543383191848358L;

    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
    	DataStorage dataStorage=null;
        try {
        	dataStorage= openResources(request);
            ScreenService screenService = dataStorage.getScreenService();
            Collection<String> manufacturers =  screenService.findManufacturerNames();
            Iterator<String> it = manufacturers.iterator();
            JSONArray array = new JSONArray();
            while (it.hasNext()) {
                String manufacturerName = it.next();
                array.add(manufacturerName);
            }
            JSONObject object = new JSONObject();
            object.put("manufacturers", array);
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
