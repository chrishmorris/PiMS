/**
 * pims-web org.pimslims.servlet BarcodeImageServlet.java
 * 
 * @author Marc Savitsky
 * @date 9 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.GraphGenerationException;
import org.pimslims.graph.implementation.LocationGraphModel;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.Containable;
import org.pimslims.presentation.barcodeGraph.BarcodeGraphNode;
import org.pimslims.presentation.barcodeGraph.GraphModelBean;

/**
 * Servlet for generating Graph svg data
 */
public class BarcodeGraphServlet extends PIMSServlet {

    private static final String info = "Shows a graph created by GraphViz software ";

    /**
     * 
     */
    public BarcodeGraphServlet() {
        super();
    }

    /**
     * BarcodeGraphServlet.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // set contentType as image/svg+xml
        // response.setContentType(MIME_IMAGE_SVG);

        try {
            Class.forName("java.awt.Graphics2D");
        } catch (final ClassNotFoundException e1) {
            throw new ServletException("Your PiMS installation needs -Djava.awt.headless=true", e1);
        }

        final ServletContext scontext = this.getServletContext();

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        ReadableVersion version = null;
        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            final String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() < 1) {
                this.writeErrorHead(request, response, "No record specified",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final ModelObject object = version.get(pathInfo.substring(1));

            if (object == null) { // http 404 error
                final Object message =
                    org.pimslims.properties.PropertyGetter.getStringProperty("error.graph.no_hook",
                        "Not found: " + pathInfo.substring(1));
                response.sendError(404,
                    (message != null && message.toString().length() > 0) ? message.toString()
                        : DiagramServlet.ErrorMessage_NoHook);
                return;
            }

            // get url context to generate url for graph nodes

            final StringBuffer sb = new StringBuffer(request.getRequestURL());
            final String URL_CONTEXT =
                sb.substring(0, sb.indexOf(request.getRequestURI())) + request.getContextPath() + "/View/";

            request.setAttribute("imageModel",
                this.generateGraph((Containable) object, URL_CONTEXT, scontext));
            request.setAttribute("headerTitle", object.getClass().getSimpleName() + ": " + object.get_Name());
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/BarcodeGraph.jsp");
            dispatcher.forward(request, response);

            version.commit();

        } catch (final GraphGenerationException e) {
            throw new ServletException(e);
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

    /* private void showBadPathPage(final HttpServletRequest request, final HttpServletResponse response)
         throws IOException {
         this.writeErrorHead(request, response, "Installation problem", HttpServletResponse.SC_BAD_REQUEST);
         final PrintWriter writer = response.getWriter();
         writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
         writer.print("<p>PIMS is unable to show your graph. Please show this page to your adminstrator.</p>");
         writer.print("</div><br/>");
         writer.print("<a href=\"../Installation\">View installation details</a>");
     } */

    /*
     * creates graph model for ModelObject
     */
    private BarcodeGraphNode generateGraph(final Containable object, final String url_context,
        final ServletContext scontext) throws IOException, GraphGenerationException {

        LocationGraphModel graphModel = null;

        final LocationGraphModel model = new LocationGraphModel();
        graphModel = model.createGraphModel(object);

        return GraphModelBean.getModel(graphModel);

    }// End of generateGraph

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return BarcodeGraphServlet.info;
    }

}
