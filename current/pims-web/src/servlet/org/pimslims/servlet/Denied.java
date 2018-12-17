/*
 * Created on 13-Jul-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.metamodel.ModelObject;

/**
 * Handler for error 403
 * 
 * @version 0.2
 */
@javax.servlet.annotation.WebServlet("/public/Denied")
public class Denied extends PIMSServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "handler for error 403";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final java.io.PrintWriter writer = response.getWriter();
        PIMSServlet.writeCriticalErrorHead(request, response, "Permission denied",
            HttpServletResponse.SC_FORBIDDEN);

        final String username = PIMSServlet.getUsername(request);
        if (null == username) {
            writer.print("<p class=error>Unknown username and password combination.</p>" + "<a href=\""
                + request.getContextPath() + "/Login\">Log in to PIMS</a><br />" + "<a href=\""
                + request.getContextPath()
                + "/help/AccessRights.jsp\">Help on setting up PIMS users</><br />");
        } else {
            writer.print("User " + username + " is not permitted to do that operation.<br />");
            final ModelObject object = (ModelObject) request.getAttribute("modelObject");
            if (null != object) {
                writer.print("You were trying to update or delete a record owned by: " + object.get_Owner()
                    + "<br/>");
            }

            final String uri = request.getRequestURI();
            if (!uri.endsWith("Denied")) {
                writer.print("You requested: " + uri + "<br />");
            }
        }
        PIMSServlet.writeFoot(writer, request);
        return;

    }
}
