/*
 * MyConstructs.java
 *
 * Created on 16 October 2007, 17:09
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
public class MyConstructs extends XtalPIMSServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5335632795333637511L;

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        try {
//            response.setContentType("text/json;charset=UTF-8");
//            getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(getDataStorage().ADMINISTRATOR), request.isUserInRole(getDataStorage().ADMINISTRATOR));
//            ConstructService constructService = getDataStorage().getConstructService();
//            PersonService personService = getDataStorage().getPersonService();
//            Collection<Construct> constructs = constructService.findByUser(personService.findByUsername(request.getRemoteUser()), false, null);
//           
//            if (constructs != null) {
//                JSONArray array = new JSONArray();
//                Iterator<Construct> it = constructs.iterator();
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
