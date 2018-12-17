/*
 * Created on 13-Apr-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.util.InstallationProperties;

/**
 * Provides error reporting to help diagnose installation problems. This is not a JSP, to minimise its
 * dependencies - it is there to help find installation defects.
 * 
 * @version 0.1
 */
public class Installation extends PIMSServlet {

    /**
     * START_UP_EXCEPTION String
     */
    public static final String START_UP_EXCEPTION = "startUpException";

    private ServletConfig config = null;

    /**
     * 
     */
    public Installation() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "Provide installation information and restart PIMS";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final javax.servlet.ServletContext context = this.config.getServletContext();
        final Throwable exception = (Throwable) context.getAttribute(Installation.START_UP_EXCEPTION);
        if (null != exception) {
            this.printStackTrace(request, response, context, exception);
            return;
        }

        PIMSServlet.writeCriticalErrorHead(request, response, "Installation information", 0);
        final java.io.PrintWriter writer = response.getWriter();

        // make form to restart
        final AbstractModel model = this.getModel();
        if (null == model) {
            writer.print("<p class=error>PIMS is not started</p>");
        } else {
            writer.print("<p>PIMS is started.</p>");
        }
        writer.print("<form method=\"post\" ><input type=submit value=Restart></form>\n");

        writer
            .print("<table summary=\"Your PIMS installation\"><tr><th></th><th>Actual</th><th>Expected</th></tr>");

        // database details
        // get tomcat Context
        Context envCtx = null;
        try {
            final Context initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("java:comp/env");

            InstallationProperties p;
            p = new InstallationProperties(envCtx);

            writer.print("<tr><td>Database driver</td><td>" + p.getProperty("db.className")
                + "</td><td>oracle.jdbc.driver.OracleDriver or org.postgresql.Driver</td></tr>");
            writer
                .print("<tr><td>Database url</td><td>"
                    + p.getProperty("db.url")
                    + "</td><td>jdbc:oracle:thin:@localhost:1521:pims or jdbc:postgresql://localhost/pims</td></tr>");
            writer.print("<tr><td>Database username</td><td>" + p.getProperty("db.username")
                + "</td><td>pimsupdate</td></tr>");
            writer.print("<tr><td>Database password</td><td>" + "********" + "</td><td></td></tr>");
            writer.print("<tr><td>Upload Directory</td><td>" + p.getProperty("uploadDirectory")
                + "</td><td>/var/pimsUploads</td></tr>");

            // Graphviz
            writer.print("<tr><td>Path to dot software</td><td>" + p.getProperty("dot_path")
                + "</td><td>dot</td></tr>");
            //CHECKSTYLE:OFF rare case when we need to catch all exceptions
        } catch (final RuntimeException e) {
            writer.print("Cannot get installation properties: " + e.getLocalizedMessage());
            //CHECKSTYLE:ON
        } catch (final NamingException e) {
            e.printStackTrace(writer);
        }
        // java details
        writer.print("<tr><td>Java Version</td><td>" + System.getProperty("java.version")
            + "</td><td>1.5.0_06</td></tr>");
        writer.print("<tr><td>Java Home</td><td>" + System.getProperty("java.home")
            + "</td><td>E:\\Program Files\\Java\\jdk1.5.0_06\\jre or /usr/lib64/jvm/java/</td></tr>");

        // memory details
        final Runtime runtime = Runtime.getRuntime();
        writer.print("<tr><td>Max memory</td><td>" + runtime.maxMemory() + "</td><td></td></tr>");
        writer.print("<tr><td>Free memory</td><td>" + runtime.freeMemory() + "</td><td></td></tr>");
        writer.print("<tr><td>Total memory</td><td>" + runtime.totalMemory() + "</td><td></td></tr>");

        // servlet context details
        // ServletConfig config = getServletConfig();
        writer.print("<tr><td>Servlet API Version</td><td>" + context.getMajorVersion() + "."
            + context.getMinorVersion() + "</td><td>2.4</td></tr>");
        writer.print("<tr><td>Servlet Container</td><td>" + context.getServerInfo()
            + "</td><td>Apache Tomcat/5.0.28</td></tr>");

        // operating system details
        writer.print("<tr><td>Operating System Name and Version</td><td>" + System.getProperty("os.name")
            + " " + System.getProperty("os.version") + "</td><td>Windows XP 5.1 or Linux 2.6</td></tr>");
        writer.print("</table>");

        PIMSServlet.writeFoot(writer, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ServletContext context = this.config.getServletContext();
        context.log("Restarting PIMS ...");
        AbstractModel model = this.getModel();
        if (null != model) {
            model.disconnect();
            model = null;
        }
        try {
            Context envCtx = null;
            try {
                final Context initCtx = new InitialContext();
                envCtx = (Context) initCtx.lookup("java:comp/env");
            } catch (final NamingException e) {
                e.printStackTrace();
            }
            model = ModelImpl.getModel(envCtx);
            // LATER tell servlets to reinit and context.setAttribute("model",
            // model);
            context.log("PIMS restarted");
            // CHECKSTYLE:OFF must report any error to user
        } catch (final Exception ex) {
            //          CHECKSTYLE:ON

            this.printStackTrace(request, response, context, ex);
            return;

        }
        PIMSServlet.writeCriticalErrorHead(request, response, "PIMS restarted", HttpServletResponse.SC_OK);
        response.getWriter().print(
            "<p>PIMS sucessfully connected to the database. "
                + "If you restart the web application, PIMS should now run correctly.</p>");
    }

    private void printStackTrace(final HttpServletRequest request, final HttpServletResponse response,
        final ServletContext context, final Throwable exception) throws IOException {
        Throwable cause = exception;
        while (null != cause.getCause()) {
            if (cause instanceof java.sql.SQLException) {
                break;
            }
            cause = cause.getCause();

        }
        context.log("PIMS restart failed", cause);
        PIMSServlet.writeCriticalErrorHead(request, response, "Error restarting PIMS",
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        cause.printStackTrace(response.getWriter());
        PIMSServlet.writeFoot(response.getWriter(), request);
    }
}
