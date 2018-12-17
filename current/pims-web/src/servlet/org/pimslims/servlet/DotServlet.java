package org.pimslims.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.graph.DotGraphUtil;
import org.pimslims.graph.GraphGenerationException;
import org.pimslims.graph.GrappaModelLoader;
import org.pimslims.graph.IGraph;
import org.pimslims.graph.implementation.AbstractGraphModel;
import org.pimslims.graph.implementation.ComplexGraphModel;
import org.pimslims.graph.implementation.ExperimentAndSampleModel;
import org.pimslims.graph.implementation.ExperimentGroupGraphModel;
import org.pimslims.graph.implementation.ProtocolGraphModel;
import org.pimslims.graph.implementation.TargetGraphModel;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.utils.ExecRunnerException;

/**
 * Servlet for generating Graph svg data
 * 
 * @author Ekaterina Pilicheva
 * @since 04 April 2006
 */
public class DotServlet extends PIMSServlet {

    private static final String info = "Shows a graph created by GraphViz software ";

    public static final String EXPERIMENTSSAMPLES = "ExperimentsSamples";

    private static final String TARGET = "Target";

    private static final String EXPERIMENTGROUP = "ExperimentGroup";

    private static final String PROTOCOL = "Protocol";

    private static final String COMPLEX = "Complex";

    private static final String LOCATION = "Location";

    // context type svg image

    public static final String MIME_IMAGE_SVG = "image/svg+xml";

    // image format

    public static final String FORMAT_SVG = DotGraphUtil.FORMAT_SVG;

    /**
     * 
     */
    public DotServlet() {
        super();
    }

    /**
     * creates graph image and puts it into servlet output stream
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        float height = 900;
        if (null != request.getParameter("height")) {
            height = Float.parseFloat(request.getParameter("height"));
        }
        float width = 500;
        if (null != request.getParameter("width")) {
            width = Float.parseFloat(request.getParameter("width"));
        }

        String format = request.getParameter("format");
        String mimeType = null;
        if (null == format) {
            final String accept = request.getHeader("Accept");
            if (accept.contains("image/png")) {
                format = "png";
                mimeType = "image/png";
            } else if (accept.contains("image/svg+xml")) {
                format = "svg";
                mimeType = "image/svg+xml";
            } else if (accept.contains("text/html") || accept.contains("*/*")) {
                // TODO could provide whole HTML Page
                format = "cmapx";
                mimeType = "text/html";
            } else {
                throw new ServletException("Unable to provide: " + accept);
            }
        }
        response.setContentType(mimeType);

        final String pathInfo = request.getPathInfo();
        // get request parameter 'graphType'

        // get a full path to dot.exe from servlet context
        final String dot_path = org.pimslims.properties.PropertyGetter.getStringProperty("dot_path", "dot");

        ReadableVersion version = null;
        version = this.getReadableVersion(request, response);
        try {
            // get a read transaction

            String hook;
            if (pathInfo == null || pathInfo.length() < 1) {
                hook = request.getParameter("hook");
            } else {
                hook = pathInfo.substring(1);
            }
            final ModelObject object = this.getRequiredObject(version, request, response, hook);
            if (object == null) {
                return;
            }

            // make the graph
            final StringBuffer sb = new StringBuffer(request.getRequestURL());
            final String url_context =
                sb.substring(0, sb.indexOf(request.getRequestURI())) + request.getContextPath() + "/View/";
            final String data =
                DotServlet.generateGraphModelAndInput(object, url_context, AbstractGraphModel.MAX_NODES,
                    width, height);
            final String digest = this.digest(data);
            final String etag = "\"" + digest + "\""; // quotes are required by HTTP spec
            response.setHeader("ETag", etag);
            final String ifNoneMatch = request.getHeader("If-None-Match");
            if (etag.equals(ifNoneMatch)) {
                // this allows the browser to cache the image if it has not changed
                // This could be a significant performance saving
                //TODO must include format in digest
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            // System.out.println(data); // to see the dot file
            if ("dot".equals(format)) { // for debugging
                final OutputStream out = response.getOutputStream();
                out.write(data.getBytes());
                return;
            }
            byte[] graph = data.getBytes();
            if (!"dot".equals(format)) {
                graph =
                    new DotGraphUtil().runDot(
                        org.pimslims.properties.PropertyGetter.getStringProperty("dot_path", "dot"), format,
                        data);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            //TODO could also set Last-Modified header 
            // restore the apostrophes in the javascript, 
            response.setHeader("cache-control", "private");

            if ("cmapx".equals(format)) {
                this.printImageMap(response, graph);
            } else {
                // for png
                final OutputStream out = response.getOutputStream();
                out.write(graph);
            }

            version.commit();

        } catch (final GraphGenerationException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final ExecRunnerException.BadPathException e) {
            this.showBadPathPage(request, response, dot_path);
        } finally {
            if (version != null && !version.isCompleted()) {
                version.abort();
            }
        }

    }// End of doGet

    /**
     * DotServlet.printImageMap
     * 
     * @param response
     * @param graph
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private void printImageMap(final HttpServletResponse response, final byte[] graph) throws IOException,
        UnsupportedEncodingException {
        // for including cmapx in page
        final PrintWriter writer = response.getWriter();
        final String map2 = new String(graph, "UTF-8").replace("\\\\&#39;", "\\'");
        final String map = map2.replace("&#39;", "'");
        writer.print(map);
    }

    /**
     * GraphServlet.digest
     * 
     * @param data
     * @return
     */
    private String digest(final String data) {
        try {
            final MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(data.getBytes());
            final byte digest[] = algorithm.digest();
            final StringBuffer hex = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hex.append(Integer.toHexString(0xFF & digest[i]));
            }
            return hex.toString();

        } catch (final NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new UnsupportedOperationException();
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

    /**
     * GraphServlet.getModel TODO end duplication of code with T2CReport
     * 
     * @param object
     * @param url_context
     * @param graphType
     * @return
     */
    public static String generateGraphModelAndInput(final ModelObject object, final String url_context,
        final int maxNodes, final float width, final float height) throws GraphGenerationException {
        AbstractGraphModel model = null;
        if (object instanceof Target) {
            model = new TargetGraphModel(object);
        } else if (object instanceof ResearchObjective) {
            model = new ComplexGraphModel(object);
        } else if (object instanceof ExperimentGroup) {
            model = new ExperimentGroupGraphModel(object);
        } else /*if (object instanceof Holder) {
               graphType = GraphServlet.LOCATION;
               } else */if (object instanceof Protocol) {
            model = new ProtocolGraphModel(object);
        } else if (object instanceof Workflow) {
            model = new ProtocolGraphModel(object);
        } else {
            model = new ExperimentAndSampleModel(object, maxNodes);
        }

        final IGraph graphModel = model.createGraphModel(object);
        return new GrappaModelLoader(graphModel, width, height).produceData();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return DotServlet.info;
    }

}
