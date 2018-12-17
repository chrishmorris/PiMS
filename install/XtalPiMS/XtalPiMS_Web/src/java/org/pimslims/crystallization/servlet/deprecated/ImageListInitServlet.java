/*
 * ImageListInitServlet.java
 *
 * Created on 04 October 2007, 14:17
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
public class ImageListInitServlet extends XtalPIMSServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2580082378012173619L;

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        ImageInfo imageInfo = new ImageInfo(ImageInfo.ImageQuality.LOW_RES, ImageInfo.ImageType.COMPOSITE);
//        
//        //Get all the possible parameters that could be passed to this routine...
//        String barcode = request.getParameter("barcode");
//        String well = request.getParameter("well");
//        String subposition = request.getParameter("sub");
//        String inspectionName = request.getParameter("name");
//        String listType = request.getParameter("type");
//        
//        List<Image> imageList = null;
//        DataStorage dataStorage = DataStorageFactory.getDataStorage();
//        try {     
//            ImageSearch imageSearch = new ImageSearch();
//            imageList = imageSearch.findImages(listType, barcode, well, subposition, inspectionName, imageInfo);
//            
//            PlateInspectionService plateInspectionService = dataStorage.getPlateInspectionService();
//            
//            out.println("<script type='text/javascript' language='javascript'>");
//            out.println("barcode=" + imageList.get(0).getPlate().getBarcode().getBarcode());
//            out.println("well=" + imageList.get(0).getWellPosition().getWellAsString());
//            out.println("sub=" + imageList.get(0).getWellPosition().getSubPosition());
//            out.println("name=" + plateInspectionService.findByImage(imageList.get(0)).getInspectionName());
//            out.println("</script>");
//        } catch (BusinessException ex) {
//            ex.printStackTrace();
//        }
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
        return "Short description";
    }
    // </editor-fold>
}
