/*
 * ScoreImageServlet.java Created on 11 October 2007, 13:37
 */

package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.util.ScoreUtil;
import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author Ian Berry
 * @version
 */
public class ScoreImageServlet extends XtalPIMSServlet {
    /**
     * 
     */
    private static final long serialVersionUID = -6180887003074837982L;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        //Do nothing
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        DataStorage dataStorage = null;
        try {
            dataStorage = openResources(request);

            final String barcode = request.getParameter("barcode");
            final String well = request.getParameter("well");
            //The inspection name (not currently used)
            //String name = request.getParameter("name");
            final String scheme = request.getParameter("scheme");
            final String annotation = request.getParameter("annotation");
            String username = request.getRemoteUser();

            ScoreUtil.setScore(dataStorage, barcode, well, scheme, annotation, username);
            dataStorage.commit();
        } catch (final BusinessException ex) {
            throw new ServletException(ex);
        } finally {
            closeResources(dataStorage);
        }

    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>

}
