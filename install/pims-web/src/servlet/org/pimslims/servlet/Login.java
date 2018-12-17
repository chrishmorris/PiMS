/*
 * Created on 13-Jul-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.accessControl.User;

/**
 * A simple page to force a logon. This page has a security constraint requiring that the user is logged on.
 * 
 * The actual login form is currently shown by login.html
 * 
 * @version 0.2
 */
public class Login extends PIMSServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "force log in to PIMS";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        View.getCurrentPerspective(request);
        // TODO if license specifies server name, server IP address, mask for client IP, client locale
        // then check they are acceptable
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            throw new ServletException("PiMS is not running");
        }
        try {
            final String username = PIMSServlet.getUsername(request);
            if (!Access.ADMINISTRATOR.equals(username)) {
                final User user = version.findFirst(User.class, User.PROP_NAME, username);
                if (null == user) {
                    Logout.logout(request);
                    throw new ServletException("Username not found in PiMS database: " + username);
                }
                if (Boolean.FALSE.equals(user.getIsActive())) {
                    Logout.logout(request);
                    throw new ServletException("Please ask your PiMS administrator to activate userid: "
                        + username);
                }
            }
        } finally {
            version.abort();
        }
        this.redirect(response, request.getContextPath() + "/");
    }
}
