/*
 * Created on 01.09.2005 @author Susy Griffiths based on BrowseTargets.java, modified to create list of
 * Targets in a group
 */
package org.pimslims.servlet.report;

import java.io.IOException;
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
import org.pimslims.model.target.Target;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Susy Griffiths
 */
public class TargetExperimentReport extends PIMSServlet {

    /**
     * 
     */
    public TargetExperimentReport() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Prepare the list of Experiments and html header, then forward to the presentation page";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }

        // Collection results = null;
        final PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 2 > pathInfo.length()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("No object specified");
            return;
        }
        final String hook = pathInfo.substring(1);
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }

        Target target = null;
        target = (Target) rv.get(hook);
        final String targetName = target.get_Name();

        try {
            final String results = target.getName();
            // Collection<ResearchObjectiveElement> results =
            // target.findAll(targetName, null);
            if ("".equals(results)) {
                this.writeHead(request, response, "Experiments for Target: " + targetName);
                writer.print(" No Experiments are recorded for Target " + targetName + "<br />");
                PIMSServlet.writeFoot(writer, request);
                return;
            }

            // now show the list
            this.writeHead(request, response, "Experiments for this Target: " + targetName);
            writer.print("The target name is " + results);
            // ProgressListener progress = new ProgressListener(results.size(),
            // writer, "Formatting target list...");

            final HttpSession session = request.getSession();
            //TODO session.setAttribute("beans", ServletUtil.makeTargetBeans(results, progress));
            //TODO shouldn't this be request?
            session.setAttribute("pagerSize", "20");
            request.setAttribute("includeHeader", new Boolean(false));
            session.setAttribute("resultAttr", results);
            // session.setAttribute("hook", hook);
            // request.setAttribute("gpAttr", targetName);
            // request.setAttribute("gpAttr", targetName);

            final ServletContext sCont = this.getServletContext();
            final RequestDispatcher dispatcher =
                sCont.getRequestDispatcher("/JSP/report/Target_experiment.jsp");
            dispatcher.forward(request, response);
            PIMSServlet.writeFoot(writer, request);
            rv.commit();
        } catch (final AbortedException e1) {
            this.writeHead(request, response, "Experiments for Target: " + targetName);
            writer.print(" Sorry, there has been a problem, please try again.");
            PIMSServlet.writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

} // servlet end
