/*
 * MySamples.java
 *
 * Created on 16 October 2007, 14:08
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
public class MySamples extends XtalPIMSServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9153390124236660782L;

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        try {
//           
//            getDataStorage().openResources(request.getRemoteUser(), request.isUserInRole(getDataStorage().ADMINISTRATOR), request.isUserInRole(getDataStorage().ADMINISTRATOR));
//            SampleService sampleService = getDataStorage().getSampleService();
//            PersonService personService = getDataStorage().getPersonService();
//            Collection<Sample> samples = sampleService.findByUser(personService.findByUsername(request.getRemoteUser()), null);
//            
//            if (samples != null) {
//                JSONArray array = new JSONArray();
//                Iterator<Sample> it = samples.iterator();
//                
//                while (it.hasNext()) {
//                    Sample sample = it.next();
//                    JSONObject obj = new JSONObject();
//                    
//                    obj.put("Name", sample.getName());
//                    obj.put("Description", sample.getDescription());
//                    array.put(obj);
//                }
//                JSONObject object = new JSONObject();
//                object.put("samples", array);
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
