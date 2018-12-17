package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: ListScreenTypesServlet
 * 
 */
public class ListScreensServlet extends XtalPIMSServlet {
    static final long serialVersionUID = 1L;

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ListScreensServlet() {
        super();
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
        final RequestDispatcher rd = request.getRequestDispatcher("/ViewScreens.jsp");
        rd.forward(request, response);
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
        doGet(request, response);
    }
}
