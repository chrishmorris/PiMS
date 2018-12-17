/**
 * pims-web org.pimslims.servlet.recordfetch DisplayBioEntry.java
 * 
 * @author Peter Troshin aka pvt43
 * @date 4 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.servlet.recordfetch;

import java.io.IOException;
import java.net.ConnectException;

import javax.naming.ServiceUnavailableException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.servlet.PIMSServlet;

public class DisplayBioEntry extends PIMSServlet {

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Get a record from biological database by its accession, and display";
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        //final PrintWriter writer = response.getWriter();
        if (pathInfo != null && pathInfo.trim().length() > 0) {
            pathInfo = pathInfo.substring(1);
        } else {
            request.setAttribute("message", "Usage: databasaName:accession");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        final int delIdx = pathInfo.indexOf(":");
        String dbid = null;
        String database = null;

        if (delIdx > 0) {
            database = pathInfo.substring(0, delIdx);
            dbid = pathInfo.substring(delIdx + 1);
        } else {
            request.setAttribute("message", "Usage: databasaName:accession");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String rec = null;
        try {
            dbid = dbid.trim();
            System.out.println(database + ":" + dbid);
            rec = DBFetch.getDBrecord(database, dbid);
        } catch (final ConnectException cex) {
            cex.printStackTrace();
            request
                .setAttribute(
                    "message",
                    "Cannot connect to the server. This might be because of your proxy settings. Please contact your system administrator.");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            request.setAttribute("message", "Sorry cannot find record " + StringEscapeUtils.escapeXml(dbid)
                + " in database " + database);
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        } catch (final ServiceUnavailableException e) {
            e.printStackTrace();
            request.setAttribute("message", "Sorry the service requested is unavailable.");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }
        if (rec == null || rec.equals("")) {
            request.setAttribute("message", "Sorry cannot find record " + StringEscapeUtils.escapeXml(dbid)
                + " in database " + database);
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/OneMessage.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // succeeded
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/BarePreview.jsp");
        request.setAttribute("database", database);
        request.setAttribute("dbid", dbid);
        // This text must not be escaped as it may profoundly influence its parsing
        // In addition it is not coming from the form but from trusted database
        request.setAttribute("record", rec);

        rd.forward(request, response);

    }

}
