package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * 
 * @baseUrl http://localhost:8080/current/target/TargetExperiments?commonName=CAG38863
 * 
 * 
 * @author Marc Savitsky
 * 
 */
@Deprecated
// they are now listed on the view of the target
public class SpotTargetExperiments extends
/* common servlet that provides utility methods */PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Target Experiments";
    }

    /**
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // Get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        // always use a transaction in a try/catch block
        try {

            final Target t = CreateExpressionObjective.getTarget(request, version);
            if (null == t) {
                this.writeErrorHead(request, response, "Target not found", HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Get a SPOTTarget
            final TargetBean tb = new TargetBean(t);

            // Put our results into the request
            request.setAttribute("targetBean", tb);
            final ArrayList experimentBeans = new ArrayList();
            final List<ExperimentBean> targetExpBeans = new ArrayList();
            final Collection<PlateBean> crystalTrialExps = new LinkedList<PlateBean>();
            SpotTarget.setExperimentBeans(version, t, targetExpBeans, crystalTrialExps);
            request.setAttribute("experimentList", experimentBeans);

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/SpotTargetExperiments.jsp");
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException ex) {
            throw new ServletException(ex);
        } catch (final ConstraintException ex) {
            throw new ServletException(ex);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

}
