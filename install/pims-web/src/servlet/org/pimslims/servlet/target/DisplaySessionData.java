/*
 * Created on 28.07.2005 TODO Remove this class to appropriate place
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Petr Troshin
 */
public class DisplaySessionData extends HttpServlet {

    /**
     * 
     */
    public DisplaySessionData() {
        super();
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        java.io.PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        writer.print("<html><body><table>");
        if (session.isNew()) {
            writer.print("<br><h3>Session is empty</h3>");
        } else {
            Enumeration er = session.getAttributeNames();
            while (er.hasMoreElements()) {
                String nam = (String) er.nextElement();
                writer.print(nam + "<br>");
                if (nam.startsWith("_class_")) {
                    Map attr = (Map) session.getAttribute(nam);
                    for (Iterator iter = attr.entrySet().iterator(); iter.hasNext();) {
                        Map.Entry elem = (Map.Entry) iter.next();
                        writer.print("<tr><td>" + elem.getKey() + "</td>");
                        writer.print("<td>" + ((String[]) elem.getValue())[0] + "</td></tr>\n");
                    }
                }
            }
        }
        writer.print("</body></html>");
    }
}
