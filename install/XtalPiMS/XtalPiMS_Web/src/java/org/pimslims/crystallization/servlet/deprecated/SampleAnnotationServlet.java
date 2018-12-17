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
public class SampleAnnotationServlet extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4268720996900638923L;

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
//            JSONArray annotations = new JSONArray();
//
//            SampleService sampleService = getDataStorage().getSampleService();
//            Sample sample = sampleService.find(Long.parseLong(sampleId));
//            ScoreService scoreService = getDataStorage().getScoreService();
//            Collection<UserScore> scores = scoreService.findHumanScores(sample, null);
//
//            Iterator<UserScore> sIt = scores.iterator();
//            while (sIt.hasNext()) {
//                UserScore score = sIt.next();
//
//                JSONObject plate = new JSONObject();
//                plate.put("Barcode", score.getDrop().getPlate().getBarcode());
//                //plate.put("Owner", plateExperiment.getOwner().getUsername());
//                plate.put("Well", score.getDrop().getWellPosition().toString());
//                plate.put("Score", score.getValue().getValue());
//                plate.put("Description", score.getValue().getDescription());
//
//                annotations.put(plate);
//
//            }
//
//            sort(annotations, true);
//
//            JSONObject output = new JSONObject();
//            output.put("annotations", annotations);
//            out.print(output);
//        } catch (BusinessException ex) {
//            ex.printStackTrace();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        } finally {
//            out.close();
//        }
    }

//    private void sort(JSONArray annotations, boolean asc) {
//        boolean done = false;
//        try {
//            while (!done) {
//                done = true;
//                for (int i = 0; i < annotations.length() - 1; i++) {
//                    if (asc) {
//                        if ((Integer) annotations.getJSONObject(i).get("Score") > (Integer) annotations.getJSONObject(i + 1).get("Score")) {
//                            JSONObject obj = annotations.getJSONObject(i);
//                            annotations.put(i, annotations.getJSONObject(i + 1));
//                            annotations.put(i + 1, obj);
//                            done = false;
//                        }
//
//                    } else {
//                        if ((Integer) annotations.getJSONObject(i).get("Score") < (Integer) annotations.getJSONObject(i + 1).get("Score")) {
//                            JSONObject obj = annotations.getJSONObject(i);
//                            annotations.put(i, annotations.getJSONObject(i + 1));
//                            annotations.put(i + 1, obj);
//                            done = false;
//                        }
//
//                    }
//                }
//
//            }
//        } catch (JSONException ex) {
//            Logger.getLogger(SampleAnnotationServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//
//    }


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
