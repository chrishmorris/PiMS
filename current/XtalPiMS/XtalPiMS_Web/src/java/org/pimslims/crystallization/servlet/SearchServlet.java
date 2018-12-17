/*
 * SearchServlet.java Created on 17 October 2007, 23:37
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Ian Berry
 * @version
 */
@Deprecated
// does nothing
public class SearchServlet extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 8115628075658562360L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //        try {
        //            String barcode = request.getParameter("barcode");
        //            String text = request.getParameter("text");
        //            JSONArray array = new JSONArray();
        //
        //            getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(getDataStorage().ADMINISTRATOR), request.isUserInRole(getDataStorage().ADMINISTRATOR));
        //            TrialService plateService = getDataStorage().getTrialService();
        //            PlateExperimentService plateExperimentService = getDataStorage().getPlateExperimentService();
        //
        //            if (barcode != null) {
        //                Collection<TrialPlate> plates = plateService.findTrialPlateByPartialBarcode(barcode, null);
        //                if (plates != null) {
        //                    Iterator<TrialPlate> it = plates.iterator();
        //                    while (it.hasNext()) {
        //                        TrialPlate plate = it.next();
        //                        JSONObject obj = new JSONObject();
        //
        //                        obj.put("Barcode", plate.getBarcode());
        //                        obj.put("Description", plate.getDescription());
        //                        if (plate.getCreateDate() != null) {
        //                            obj.put("CreateDate", plate.getCreateDate().getTimeInMillis());
        //                        } else {
        //                            obj.put("CreateDate", 0);
        //                        }
        //                        array.put(obj);
        //                    }
        //                }
        //            } else if (text != null) {
        //                Collection<PlateExperimentView> plateExperiments = plateExperimentService.findByDescription(text, null);
        //                if (plateExperiments != null) {
        //                    Iterator<PlateExperimentView> it = plateExperiments.iterator();
        //                    while (it.hasNext()) {
        //                        PlateExperimentView plateExperiment = it.next();
        //                        JSONObject obj = new JSONObject();
        //
        //                        obj.put("Barcode", plateExperiment.getBarcode());
        //                        obj.put("Description", plateExperiment.getDescription());
        //                        array.put(obj);
        //                    }
        //                }
        //            }
        //
        //
        //            JSONObject object = new JSONObject();
        //            object.put("results", array);
        //
        //
        //            response.setContentType("text/json;charset=UTF-8");
        //            PrintWriter out = response.getWriter();
        //            out.print(object);
        //
        //        } catch (BusinessException ex) {
        //            ex.printStackTrace();
        //        } catch (JSONException ex) {
        //            ex.printStackTrace();
        //        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
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
