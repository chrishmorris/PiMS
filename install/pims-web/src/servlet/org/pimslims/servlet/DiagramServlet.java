package org.pimslims.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.DotGraphUtil;
import org.pimslims.utils.SVG;

/**
 * Servlet for generating Graph svg data
 * 
 * @author Ekaterina Pilicheva
 * @since 04 April 2006 soon to be obsolete
 */
public class DiagramServlet extends PIMSServlet {

    public static final String ErrorMessage_NoHook =
        "there is no object in the system that can be accessed by this url";

    public static final String ErrorMessage_EmptyGraph = "Graph is empty";

    private static final String info = "Shows a graph created by GraphViz software ";

    public static final String EXPERIMENTSSAMPLES = "ExperimentsSamples";

    public static final String TARGET = "Target";

    public static final String EXPERIMENTGROUP = "ExperimentGroup";

    public static final String PROTOCOL = "Protocol";

    public static final String COMPLEX = "Complex";

    public static final String LOCATION = "Location";

    @Deprecated
    // there should be no default
    public static final String DEFAULTGRAPHTYPE = "ExperimentsSamples";

    // context type svg image

    public static final String MIME_IMAGE_SVG = "image/svg+xml";

    // image format

    public static final String FORMAT_SVG = DotGraphUtil.FORMAT_SVG;

    /**
     * 
     */
    public DiagramServlet() {
        super();
    }

    /**
     * creates graph image as svg and puts it into servlet output stream usage: call this servlet from html:
     * <embed src="Graph" name="chart" width="800" height="300" type="image/svg+xml"
     * pluginspage="http://www.adobe.com/svg/viewer/install/"/>
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // set contentType as image/svg+xml
        // response.setContentType(MIME_IMAGE_SVG);

        final ServletContext scontext = this.getServletContext();
        final HttpSession session = request.getSession();

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // get a full path to dot.exe from servlet context
        String dot_path = org.pimslims.properties.PropertyGetter.getStringProperty("dot_path", "dot");
        if (dot_path == null || dot_path.length() < 1) {
            dot_path = "dot";
        }

        ReadableVersion version = null;
        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            // get url context to generate url for graph nodes
            final StringBuffer sb = new StringBuffer(request.getRequestURL());
            final String URL_CONTEXT =
                sb.substring(0, sb.indexOf(request.getRequestURI())) + request.getContextPath() + "/View/";

            /* create svg graph and write it to outputStream
            final SVG svg =
                new SVG(new String(this.generateGraph(object, URL_CONTEXT, dot_path, scontext, graphType)));

            session.setAttribute("svg", svg); */

            request.setAttribute("hook", request.getPathInfo().substring(1));
            //request.setAttribute("height", svg.getHeight());
            //request.setAttribute("width", svg.getWidth());
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/core/diagram.jsp");
            dispatcher.forward(request, response);

            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (version != null && !version.isCompleted()) {
                version.abort();
            }
        }

    }// End of doGet

    //TODO no, this is a read operation, do it on GET.
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("SVGServlet.doPost");
        //final ServletContext scontext = this.getServletContext();
        final HttpSession session = request.getSession();

        final SVG svg = (SVG) session.getAttribute("svg");
        final OutputStream outstr = response.getOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(outstr);
        oos.writeObject(svg);
        oos.flush();
        oos.close();

    }// End of doPost

    private void showBadPathPage(final HttpServletRequest request, final HttpServletResponse response,
        final String dotPath) throws IOException {
        this.writeErrorHead(request, response, "Installation problem", HttpServletResponse.SC_BAD_REQUEST);
        final PrintWriter writer = response.getWriter();
        writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
        writer.print("<p>PIMS is unable to show your graph. Please show this page to your adminstrator.</p>");
        writer.print("<p>The GraphViz executable dot was not found at: " + dotPath + "</p>");
        writer.print("</div><br/>");
        writer.print("<a href=\"../Installation\">View installation details</a>");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return DiagramServlet.info;
    }

}
