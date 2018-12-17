/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.presentation.mru.MRUController;

/**
 * @author cm65
 * 
 */
@javax.servlet.annotation.WebServlet("/public/ErrorPage")
public class ErrorPage extends PIMSServlet {

    /*
     * for GET and POST
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        MRUController.clearAll();

        final Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        java.io.PrintWriter writer;
        try {
            writer = response.getWriter();
        } catch (final java.lang.IllegalStateException e) {
            //JSP already opened
            e.printStackTrace();
            exception.printStackTrace();
            return;
        }
/*
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
            || "true".equals(request.getParameter("isAJAX"))
            || request.getHeader("Accept").contains("text/javascript")
            || request.getHeader("Accept").contains("applciation/json")) {
            // json response preferred
            if (exception instanceof DuplicateKeyConstraintException) {
                final DuplicateKeyConstraintException dkce = (DuplicateKeyConstraintException) exception;
                final String message =
                    "There is already a: " + dkce.object.getClass().getSimpleName() + " with the same: "
                        + dkce.attributeName;
                //TODO also make  suggestion: {"hook.of.Object:1234": {  name:"Fred 2"  }
                writer.write("{ error: '" + message + "' }");
            } else {
                writer.write("{ error: '" + ErrorPage.getExceptionSummary(exception) + "' }");
            }
            return;
        } */

        // special handling for some exceptions
        Throwable cause = exception;
        while (null != cause) {
            if (cause instanceof IllegalArgumentException
                && cause.getMessage().startsWith("Unknown PIMS user name:")) {
                ErrorPage.reportNoSuchPimsUser(request, response, response.getWriter());
                return;
            } else if (cause instanceof AccessException) {
                final RequestDispatcher rd = request.getRequestDispatcher("/public/Denied");
                rd.forward(request, response);
                return;
            } else if (cause instanceof ConstraintException) {
                final RequestDispatcher rd = request.getRequestDispatcher("/update/ConstraintErrorPage");
                rd.forward(request, response);
                return;
            }

            cause = cause.getCause();

        }

        ErrorPage.printStackTrace(request, response, writer, exception);
        return;
    }

    public static void printStackTrace(final HttpServletRequest request, final HttpServletResponse response,
        final java.io.PrintWriter writer, final Throwable exception) {
        // special message for common admin error
        if (null != exception && exception.getClass().getName().contains("ServletException")) {
            if (((ServletException) exception).getRootCause() instanceof java.lang.IllegalArgumentException
                && ((ServletException) exception).getRootCause().getMessage()
                    .contains("Unknown PIMS user name:")) {
                ErrorPage.reportNoSuchPimsUser(request, response, writer);
                return;
            }
        }

        PIMSServlet.writeCriticalErrorHead(request, response, "Sorry, there has been an error",
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
        writer.print("<p>There was a problem inside PIMS, and it was unable to complete your request.</p>");
        writer
            .print("<p>It would be most helpful if you could paste the developer information below into an email and send it to "
                + "<a href='mailto:pims-users@dl.ac.uk'>pims-users@dl.ac.uk</a> - and, if possible, tell us what you were doing just before this happened. "
                + "This will help us determine what went wrong, so that we can fix it.</p>");
        writer.print("<p>Thanks,<br/>PIMS Development Team</p>");
        writer.print("</div><br/>");

        if (null == exception) {
            writer.print("Sorry, the cause of the error is unknown.");
        } else {
            writer.print(ErrorPage.getDeveloperInformationBox(request, exception, true));
        }
        PIMSServlet.writeFoot(writer, request);

    }

    /**
     * @param request
     * @param response
     * @param writer
     */
    public static void reportNoSuchPimsUser(final HttpServletRequest request,
        final HttpServletResponse response, final java.io.PrintWriter writer) {
        final HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        // writeHead(request, response, exception.getLocalizedMessage());
        PIMSServlet.writeCriticalErrorHead(request, response, "Permission denied",
            HttpServletResponse.SC_FORBIDDEN);
        final String path = request.getContextPath();
        writer.print("<p class=error>That username is not the name of a PIMS user. "
            + "Please show this error message to your PIMS administrator.</p>"
            + "<p>That username is a Tomcat user, but not defined as a PIMS user." + "<a href=\"" + path
            + "/Create/org.pimslims.model.accessControl.User\">Create a PIMS user</a></p>");
        writer.print("<p ></p>");
        PIMSServlet.writeFoot(writer, request);
    }

    static String getDeveloperInformationBox(final HttpServletRequest request, final Throwable exception,
        final boolean printStackTrace) {
        String returnValue = "";

        returnValue +=
            ("<span style=\"color:blue; font-size:large; font-weight:bold; font-family:arial,helvetica,sans-serif\">Developer information</span>");

        returnValue += ("<table style=\"border:1px solid blue;font-size:smaller;padding:5px\">"); // begin
        // Developer
        // Information
        // box

        returnValue +=
            ("<tr><td>Exception:</td><td>" + ErrorPage.getExceptionSummary(exception) + "</td></tr>");
        if (printStackTrace && exception != null) {
            returnValue +=
                ("<tr><td>Caused by:</td><td>" + ErrorPage.getExceptionStackTrace(exception) + "</td></tr>");

        }
        returnValue += ("<tr><td>User:</td><td>" + request.getRemoteUser() + "</td></tr>");
        returnValue += ("<tr><td>Method:</td><td>" + request.getMethod() + "</td></tr>");

        String uri = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (null == uri) {
            uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        }
        if (null == uri) {
            uri = request.getRequestURI();
        }
        if (!uri.contains("password")) {
            returnValue += ("<tr><td>Request URI:</td><td>" + uri + "</td></tr>");
        }

        if (request.getParameterMap().size() > 0) {
            returnValue += ("<tr><td>Parameters:</td><td>");
            for (final Object key : request.getParameterMap().keySet()) {
                if ("password".equals(key)) {
                    continue;
                }
                final Object[] value = (Object[]) request.getParameterMap().get(key);
                final List valueList = Arrays.asList(value);
                if (valueList.size() > 0) {
                    returnValue +=
                        (key + "=" + StringEscapeUtils.escapeHtml(valueList.get(0).toString()) + ", ");
                }

            }
            returnValue += ("</td></tr>");
        }
        if (request.getAttributeNames().hasMoreElements()) {
            returnValue += ("<tr><td>Attributes:</td><td>");

            returnValue += ("</td></tr>");
        }

        returnValue += ("</table>"); // end of Developer Information box

        return returnValue;
    }

    static String getExceptionSummary(final Throwable exception) {
        if (exception == null) {
            return "<b>" + "Cause of error unknown" + "</b><br />";
        }
        return "<b>" + StringEscapeUtils.escapeHtml(exception.toString()) + "</b><br />";

    }

    static String getExceptionStackTrace(final Throwable exception) {
        if (exception == null) {
            return "";
        }
        String trace = "";

        Throwable cause = exception;
        while (null != cause.getCause()) {
            cause = cause.getCause();
        }

        if (cause != null) {
            if (cause != exception) {
                trace += (ErrorPage.getExceptionSummary(cause));
            }

            final StackTraceElement[] stackTrace = cause.getStackTrace();
            if (stackTrace != null) {
                for (int i = 0; i < stackTrace.length; i++) {
                    final String location = stackTrace[i].toString();
                    if (-1 != location.indexOf("org.pimslims") // it's one of
                        // our classes
                        || -1 != location.indexOf("_jsp._jspService") // it's
                        // a
                        // JSP
                        || -1 != location.indexOf("_JSP._jspService") // it's
                    // a
                    // JSP
                    // Fragment
                    ) {
                        trace += ("at <b>" + StringEscapeUtils.escapeHtml(location) + "</b><br />");
                    } else {
                        trace += ("at " + StringEscapeUtils.escapeHtml(location) + "<br />");
                    }
                }
            }
        }

        return trace;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "An exception has occurred - show an error message";
    }

}
