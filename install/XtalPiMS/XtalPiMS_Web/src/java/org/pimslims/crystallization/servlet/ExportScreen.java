/*
 * PlateServlet.java Created on 19 October 2007, 14:54
 */
package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.xml.ScreenExportUtil;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Bill Lin
 * @version
 */
public class ExportScreen extends PIMSServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7643062815567251311L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //curren values
        final String pathInfo = request.getPathInfo();
        final String screenName = pathInfo.replace("/", "");
        try {
            final PrintWriter writer = response.getWriter();
            final String exportString = ScreenExportUtil.export(screenName);
            response.setContentType("application/xml");
            writer.print(exportString);
        } catch (final BusinessException e) {
            throw new ServletException(e);
        } catch (final JAXBException e) {
            throw new ServletException(e);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
    // the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "export screen";
    }
    // </editor-fold>
}
