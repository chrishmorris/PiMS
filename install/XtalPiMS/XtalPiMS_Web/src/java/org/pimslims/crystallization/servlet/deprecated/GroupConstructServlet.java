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
public class GroupConstructServlet extends XtalPIMSServlet {
   
    /**
     * 
     */
    private static final long serialVersionUID = -616891245578289711L;

    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        try {
//            response.setContentType("text/json;charset=UTF-8");
//            String groupName = request.getParameter("name");
//            GroupService groupService = getDataStorage().getGroupService();
//            
//            Group group = groupService.find(groupName);
//            ConstructService constructService = getDataStorage().getConstructService();
//            Collection<Construct> constructs = constructService.findByGroup(group, null);
//            
//            if (constructs != null) {
//                JSONArray array = new JSONArray();
//                Iterator<Construct> it = constructs.iterator();
//                
//                while (it.hasNext()) {
//                    Construct construct = it.next();
//                    JSONObject obj = new JSONObject();
//                    
//                    obj.put("Name", construct.getName());
//                    obj.put("Description", construct.getDescription());
//                    array.put(obj);
//                }
//                JSONObject object = new JSONObject();
//                object.put("constructs", array);
//                PrintWriter out = response.getWriter();
//                out.print(object);
//                out.close();
//            }
//        } catch (BusinessException ex) {
//            ex.printStackTrace();
//        } catch (JSONException ex) {
//            ex.printStackTrace();
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
