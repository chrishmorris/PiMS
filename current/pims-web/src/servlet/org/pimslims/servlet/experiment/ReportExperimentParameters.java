/**
 * 
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.experiment.ExperimentParameterBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ReportExperimentParameters extends PIMSServlet {

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
        // get selected exp and exp group
        final Collection<String> expHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), Experiment.class.getName());
        final Collection<String> expGroupHooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), ExperimentGroup.class.getName());

        // return when not exp selected
        final PrintWriter writer = response.getWriter();
        if ((expHooks == null || expHooks.size() == 0)
            && (expGroupHooks == null || expGroupHooks.size() == 0)) {
            this.writeHead(request, response, "No Experiment Selected");
            writer.print("Please select experiments or experiment group first!");
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        String headerName = "List Experiment Parameters";
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {

            // get ExperimentParameterBeans
            final Collection<ExperimentParameterBean> beans =
                ReportExperimentParameters.getExperimentParameters(expHooks, expGroupHooks, version);
            if (beans.size() == 0) {
                this.writeHead(request, response, "No Experiment found");
                writer.print("Please select experiments or experiment group first!");
                PIMSServlet.writeFoot(writer, request);
                return;
            }
            String protocolName = null;
            for (final ExperimentParameterBean bean : beans) {
                if (protocolName == null) {
                    protocolName = bean.getExperimentProtocolName();
                }
                if (protocolName == null || (!bean.getExperimentProtocolName().equals(protocolName))) {
                    this.writeHead(request, response, "Can not compare these experiments");
                    writer
                        .println("Can not compare these selected experiments as they are using different protocols or have no protocol!");
                    writer.println("<br/>Please select experiments which using same protocol!");
                    writer
                        .println("<br/>Tips: Before you do the search, select an \'Experiment Type\' and then select a \'Protocol\'");
                    PIMSServlet.writeFoot(writer, request);
                    return;
                }

            }
            headerName = "Parameters of selected " + protocolName + " experiments";
            this.writeHead(request, response, "List " + headerName);
            request.setAttribute("_List_", beans);
            request.setAttribute("headerName", headerName);
            request.setAttribute("resultSize", beans.size());
            request.setAttribute("includeHeader", new Boolean(false));
            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/browseExperimentAndParameters.jsp");
            rd.forward(request, response);
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

    static Collection<ExperimentParameterBean> getExperimentParameters(final Collection<String> expHooks,
        final Collection<String> expGroupHooks, final ReadableVersion version) {
        final Collection<ExperimentParameterBean> beans = new LinkedList<ExperimentParameterBean>();
        // get ExperimentParameterBean from experiment
        for (final String hook : expHooks) {
            final Experiment exp = version.get(hook);
            beans.add(new ExperimentParameterBean(exp));
        }
        // get ExperimentParameterBean from experimentGroup
        for (final String hook : expGroupHooks) {
            final ExperimentGroup expGroup = version.get(hook);
            for (final Experiment exp : expGroup.getExperiments()) {
                beans.add(new ExperimentParameterBean(exp));
            }
        }
        return beans;
    }

}
