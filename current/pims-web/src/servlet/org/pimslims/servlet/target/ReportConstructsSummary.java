/**
 * 
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ReportConstructsSummary extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show custom list of experiments";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }
        // get selected Target
        final Collection<String> targetHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), Target.class.getName());
        // return when not target selected
        final PrintWriter writer = response.getWriter();
        if ((targetHooks == null || targetHooks.size() == 0)) {
            this.writeHead(request, response, "No Target Selected");
            writer.print("Please select target first!");
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        final String headerName = "Construct Summary of Targets";
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {

            // get ExperimentParameterBeans
            final Collection<ConstructBean> beans =
                ReportConstructsSummary.getConstructBeans(targetHooks, version);

            this.writeHead(request, response, headerName);
            request.setAttribute("_List_", beans);
            request.setAttribute("headerName", headerName);
            request.setAttribute("resultSize", beans.size());
            request.setAttribute("includeHeader", new Boolean(false));
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/browseConstructSummary.jsp");
            //TODO check if this should be forward
            rd.include(request, response);
            PIMSServlet.writeFoot(writer, request);
            version.commit();
        } catch (final AbortedException e1) {
            this.writeHead(request, response, headerName);
            writer.print(" Sorry, there has been a problem, please try again.");
            PIMSServlet.writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    static Collection<ConstructBean> getConstructBeans(final Collection<String> targetHooks,
        final ReadableVersion version) {
        // get target
        final Collection<Target> targets = new HashSet<Target>();
        for (final String targetHook : targetHooks) {
            targets.add((Target) version.get(targetHook));
        }

        // get ConstructBean
        final Collection<ConstructBean> results = new HashSet<ConstructBean>();
        for (final Target target : targets) {
            final Collection<ResearchObjective> expbs = TargetUtility.getTargetExpBlueprint(target);
            for (final ResearchObjective expb : expbs) {
                // only add spot construct
                if (org.pimslims.lab.ConstructUtility.isSpotConstruct(expb)) {
                    final ConstructBean cb = ConstructBeanReader.readConstruct(expb);
                    results.add(cb);
                }
            }
        }
        return results;
    }

}
