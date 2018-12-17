/*
 * GroupPlateServlet.java
 *
 * Created on 16 October 2007, 21:49
 */

package org.pimslims.crystallization.servlet.deprecated;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ian Berry
 * @version
 */
public class GroupPlateServlet extends XtalPIMSServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -703927559183022990L;

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException error
     * @throws java.io.IOException error
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        response.setContentType("text/json;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        String groupName = request.getParameter("name");
//        if (groupName == null) {
//            out.println("No matching group");
//            out.close();
//            return;
//        }
//        try {
//            
//            GroupService groupService = getDataStorage().getGroupService();
//            Group group = groupService.find(groupName);
//            if (group != null) {
//                PlateExperimentService plateExperimentService = getDataStorage().getPlateExperimentService();
//                Collection<PlateExperimentView> plateExperiments = plateExperimentService.findPlateViews("stepheng", groupName, null, null);//.findSimpleByGroup(group, null);
//                JSONArray array = new JSONArray();
//                Iterator<PlateExperimentView> it = plateExperiments.iterator();
//                while (it.hasNext()) {
//                    PlateExperimentView plateExperiment = it.next();
//                    JSONObject obj = new JSONObject();
//                    
//                    obj.put("Barcode", plateExperiment.getBarcode());
//                    obj.put("CreateDate", String.format("%1$td/%1$tm/%1$tY %1$tT", plateExperiment.getCreateDate()));
//                    obj.put("Description", plateExperiment.getDescription());
//                    array.put(obj);
//                }
//                JSONObject object = new JSONObject();
//                object.put("plates", array);
//                out.print(object);
//            } else {
//                out.println("No matching group");
//            }
//        } catch (BusinessException ex) {
//            ex.printStackTrace();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//        
//        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Gets a list of plates for a given group";
    }
    // </editor-fold>
}
