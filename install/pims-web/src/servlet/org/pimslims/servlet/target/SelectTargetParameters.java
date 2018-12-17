/**
 * 
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
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
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class SelectTargetParameters extends PIMSServlet {

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
            writer.print("Please select some Targets first!");
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        final String headerName = "List Experiment Parameters of Targets";
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {

            // get used protocols of targets
            final Collection<Protocol> targetProtocols =
                TargetUtility.getTargetProtocols(targetHooks, version);

            // get ParameterDefinitions of protocols
            boolean foundParameter = false;
            final Map<Protocol, java.util.List<ParameterDefinition>> protocolParameters =
                new HashMap<Protocol, java.util.List<ParameterDefinition>>();
            for (final Protocol protocol : targetProtocols) {
                if (protocol != null) {
                    final java.util.List<ParameterDefinition> pds = protocol.getParameterDefinitions();
                    if (pds != null && pds.size() > 0) {
                        protocolParameters.put(protocol, pds);
                        foundParameter = true;
                    }
                }
            }
            // when no parameter found
            if (!foundParameter) {
                this.writeHead(request, response, "No Experiment Parameter Found");
                writer
                    .print("There are no Experiment Parameters found in the Experiments for the selected Targets");
                PIMSServlet.writeFoot(writer, request);
                return;
            }

            this.writeHead(request, response, headerName);
            request.setAttribute("protocolParameters", protocolParameters);
            request.setAttribute("targetHooks", targetHooks);

            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_OK);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/SelectParametersOfTargets.jsp");

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

}
