/**
 * 
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ReportTargetParameters extends PIMSServlet {

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
        // get selected ParameterDefinition
        final java.util.List<String> pdHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), ParameterDefinition.class.getName());
        // get selected Target
        final Collection<String> targetHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), Target.class.getName());
        // return when not exp selected
        final PrintWriter writer = response.getWriter();
        if ((pdHooks == null || pdHooks.size() == 0)) {
            this.writeHead(request, response, "No ParameterDefinition Selected");
            writer.print("Please select some Parameters to comapare");
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        if ((targetHooks == null || targetHooks.size() == 0)) {
            this.writeHead(request, response, "No Target Selected");
            writer.print("Please select some Targets");
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        final String headerName = "List Experiment Parameters of Targets";
        final ReadableVersion version = this.getReadableVersion(request, response);

        //190710 Need referrer for breadcrumb trail
        try {

            // get ExperimentParameterBeans
            final Collection<ConstructParameterBean> beans =
                ReportTargetParameters.getConstructParameters(targetHooks, pdHooks, version);
            final String referrer = request.getHeader("referer");

            //get the TargetGroup hook if it exists
            final Map<String, String[]> parms = request.getParameterMap();
            String tgrHook = "";
            String tgrName = "";
            if (null != parms.get("tgrHook")) {
                tgrHook = request.getParameter("tgrHook");
            }
            if (null != parms.get("tgrName")) {
                tgrName = request.getParameter("tgrName");
            }

            this.writeHead(request, response, headerName);
            request.setAttribute("_List_", beans);
            request.setAttribute("headerName", headerName);
            request.setAttribute("resultSize", beans.size());
            request.setAttribute("referrer", referrer);
            request.setAttribute("tgrHook", tgrHook);
            request.setAttribute("tgrName", tgrName);
            request.setAttribute("includeHeader", new Boolean(false));

            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/browseParametersOfTargets.jsp");

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

    static Collection<ConstructParameterBean> getConstructParameters(final Collection<String> targetHooks,
        final java.util.List<String> pdHooks, final ReadableVersion version) {
        // get target
        final Collection<Target> targets = new HashSet<Target>();
        for (final String targetHook : targetHooks) {
            targets.add((Target) version.get(targetHook));
        }
        // get ParameterDefinition
        final java.util.List<ParameterDefinition> pds = new LinkedList<ParameterDefinition>();
        for (final String pdHook : pdHooks) {
            pds.add((ParameterDefinition) version.get(pdHook));
        }

        // get ConstructParameterBean
        final Collection<ConstructParameterBean> results = new HashSet<ConstructParameterBean>();
        for (final Target target : targets) {
            final Collection<ResearchObjective> expbs = TargetUtility.getTargetExpBlueprint(target);
            for (final ResearchObjective expb : expbs) {
                if (expb.getExperiments() != null && expb.getExperiments().size() > 0) {
                    results.add(new ConstructParameterBean(target, expb, pds));
                }
            }

        }

        return results;
    }

}
