/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.servlet.deprecated;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.exception.BusinessException;

/**
 *
 * @author ian
 */
public class AllSamples extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            response.setContentType("text/json;charset=UTF-8");
            getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(DataStorage.ADMINISTRATOR), request.isUserInRole(DataStorage.ADMINISTRATOR));
            SampleService sampleService = getDataStorage().getSampleService();
            //Collection<Sample> samples = sampleService.findByUser(personService.findByUsername(request.getRemoteUser()));
            Collection<Sample> samples = sampleService.findAll(null);
            if (samples != null) {
                JSONArray array = new JSONArray();
                Iterator<Sample> it = samples.iterator();

                while (it.hasNext()) {
                    Sample sample = it.next();
                    JSONObject obj = new JSONObject();

                    obj.put("Name", sample.getName());
                    obj.put("Description", sample.getDescription());
                    array.add(obj);
                }
                JSONObject object = new JSONObject();
                object.put("samples", array);
                PrintWriter out = response.getWriter();
                out.print(object);
                out.close();
            }
        } catch (BusinessException ex) {
            ex.printStackTrace();
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
