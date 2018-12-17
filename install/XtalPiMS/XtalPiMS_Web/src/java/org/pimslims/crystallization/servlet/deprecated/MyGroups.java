/*
 * MyGroups.java
 *
 * Created on 16 October 2007, 17:14
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
public class MyGroups extends XtalPIMSServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3646129160946111051L;

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        try {
//            response.setContentType("text/json;charset=UTF-8");
//            getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(getDataStorage().ADMINISTRATOR), request.isUserInRole(getDataStorage().ADMINISTRATOR));
//            GroupService groupService = getDataStorage().getGroupService();
//            PersonService personService = getDataStorage().getPersonService();
//            Collection<Group> groups = groupService.findByPerson(personService.findByUsername(request.getRemoteUser()), null);
//           
//            if (groups != null) {
//                JSONArray array = new JSONArray();
//                Iterator<Group> it = groups.iterator();
//                while (it.hasNext()) {
//                    Group group = it.next();
//                    JSONObject obj = new JSONObject();
//                    
//                    obj.put("Name", group.getName());
//                    obj.put("Organisation", "OPPF");
//                    array.put(obj);
//                }
//                JSONObject object = new JSONObject();
//                object.put("groups", array);
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
        return "Short description";
    }

    // </editor-fold>
}
