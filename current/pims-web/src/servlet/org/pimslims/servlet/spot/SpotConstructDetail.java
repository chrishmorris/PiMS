package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * View of a Construct
 * 
 * @author Johan van Niekerk
 */
public class SpotConstructDetail extends
/* common servlet that provides utility methods */PIMSServlet {

    /**
     * Code to satisfy Serializable Interface
     */
    private static final long serialVersionUID = 7370485794442757732L;

    /**
     * @return Servlet descriptor string
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "A view of a construct";
    }

    /**
     * Load the relevant construct and target details and dispatch to a jsp for display
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }
        final String pathInfo = request.getPathInfo();
        if (null == pathInfo || 2 > pathInfo.length()) {

            this.writeErrorHead(request, response, "Construct not specified",
                HttpServletResponse.SC_NOT_FOUND);
            return;

        }
        final String hook = pathInfo.substring(1);
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        try {
            final ModelObject object = this.getRequiredObject(version, request, response, hook);
            if (null == object) {
                return; // error message has already been sent
            }
            ConstructBean cb = null;
            if (object instanceof Experiment) {
                final Experiment experiment = (Experiment) object;
                cb = ConstructBeanReader.readConstruct(experiment);
            } else if (object instanceof ResearchObjective) {
                final ResearchObjective ro = (ResearchObjective) object;
                // check which sort of experiment blueprint this is
                if (!org.pimslims.lab.ConstructUtility.isSpotConstruct(ro)) {
                    final org.pimslims.model.target.Target target =
                        org.pimslims.lab.create.TargetFactory.getPimsTarget(ro);
                    if (null != target) {
                        final RequestDispatcher rd =
                            request.getRequestDispatcher("/View/" + target.get_Hook());
                        version.abort();
                        rd.forward(request, response);
                        return;
                    }
                    final RequestDispatcher rd =
                        request.getRequestDispatcher("/expert/View/" + ro.get_Hook());
                    version.abort();
                    rd.forward(request, response);
                    return;
                }
                cb = ConstructBeanReader.readConstruct(ro);
                SpotConstructDetail.setExperimentBeans(version, ro, request);
            }

            // Annotate the SPOTConstruct
            // now done in readConstruct ConstructAnnotator.annotate(cb);

            final TargetBean tb = cb.getTargetBeans().iterator().next(); //TODO pass whole collection
            // Put our details into the request
            request.setAttribute("constructBean", cb);
            request.setAttribute("mayUpdate", new Boolean(object.get_MayUpdate()));
            request.setAttribute("owner", object.get_Owner());

            request.setAttribute("experimentMetaClass",
                this.getModel().getMetaClass(Experiment.class.getName()));

            // Pass on to the JSP
            if (tb.isDNATarget()) {
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/dnaTarget/DNAConstructDetail.jsp");
                dispatcher.forward(request, response);
            } else {
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/view/org.pimslims.model.target.ResearchObjective.jsp");
                dispatcher.forward(request, response);
            }

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    static void setExperimentBeans(final ReadableVersion version, final ResearchObjective ro,
        final ServletRequest request) {

        final Collection<PlateBean> crystalTrialExps = new LinkedList<PlateBean>();
        final List<ExperimentBean> experimentBeans = new ArrayList<ExperimentBean>();
        SpotTarget.setExperimentBeans(experimentBeans, crystalTrialExps, ro);

        Collections.sort(experimentBeans);
        request.setAttribute("experimentBeans", experimentBeans);
        request.setAttribute("crystalTrialExps", crystalTrialExps);
    }
}
