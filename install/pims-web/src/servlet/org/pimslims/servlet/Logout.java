/*
 * Created on 13-Jul-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * End a PIMS session
 * 
 * @version 0.2
 */
public class Logout extends PIMSServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "log out from PIMS";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        Logout.logout(request);
        this.redirect(response, request.getContextPath() + "/");
    }

    /**
     * Logout.logout
     * 
     * @param request
     */
    public static void logout(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }
}
