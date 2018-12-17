/**
 * pims-web org.pimslims.servlet.tarointegration TaroListener.java
 * 
 * @author pvtroshin
 * @date 22 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvtroshin The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.tarointegration;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.lab.Util;
import org.pimslims.servlet.PIMSServlet;

/**
 * TaroListener
 * 
 */
@Deprecated
// not in use
public class TaroListener extends PIMSServlet {

    /**
     * PIMSServlet.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Servlet to notify PIMS of the TARO query ID after PIMS submit the target to TARO for processing";
    }

    /**
     * TaroListener.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        String token = request.getPathInfo();
        if (Util.isEmpty(token)) {
            request.setAttribute("message", "Token is not found!");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }
        token = token.substring(1);

        System.out.println("POST recieved: " + token);
        /* could
         * TokenManager decrypt
         * get target name
         * find the target & update DB
         *  
         */
    }
}
