/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.crystallization.servlet.deprecated;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ian
 */
public class SamplePlatesServlet extends XtalPIMSServlet {
   
    /**
     * 
     */
    private static final long serialVersionUID = -3072452796065719859L;

    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        response.setContentType("text/json;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        String sampleId = request.getParameter("sample");
//        try {
//            SampleService sampleService = getDataStorage().getSampleService();
//            Sample sample = sampleService.find(Long.parseLong(sampleId));
//            PlateExperimentService plateExperimentService = getDataStorage().getPlateExperimentService();
//            Collection<PlateExperimentView> plateExperiments = plateExperimentService.findBySample(sample, null);
//            JSONArray plates = new JSONArray(); 
//            Iterator<PlateExperimentView> it = plateExperiments.iterator();
//            while (it.hasNext()) {
//                PlateExperimentView plateExperiment = it.next();
//                JSONObject plate = new JSONObject();
//                plate.put("CreateDate", plateExperiment.getCreateDate().getTimeInMillis());
//                plate.put("Description", plateExperiment.getDescription());
//                plate.put("Barcode", plateExperiment.getBarcode());
//                //plate.put("Owner", plateExperiment.getOwner().getUsername());
//                plates.put(plate);      
//            }
//            
//            JSONObject output = new JSONObject();
//            output.put("plates", plates);
//            out.print(output);
//        } catch (BusinessException ex) {
//            ex.printStackTrace();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        } finally { 
//            out.close();
//        }
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
