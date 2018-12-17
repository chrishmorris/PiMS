/**
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * 
 * @author Johan van Niekerk
 * 
 */
public class SpotConstructMilestoneDetail extends
/* common servlet that provides utility methods */PIMSServlet {

    @Override
    public String getServletInfo() {
        return "SPoT construct Milestone page";
    }

    /**
     * 
     * @baseUrl read/ViewMilestone/org.pimslims.model.expBlueprint.BlueprintStatus:22156
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

        response.getWriter();
        final String hook = request.getPathInfo().substring(1);

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        try {
            final Milestone milestone = (Milestone) version.get(hook);
            final ResearchObjective eb = milestone.getResearchObjective();
            ModelObjectShortBean cb = null;
            if (null != eb) {
                cb = ConstructBeanReader.readConstruct(eb);
            }

            final Target target = milestone.getTarget();

            final ConstructResultBean spcm = new ConstructResultBean(version, milestone);
            if (null != target) {
                final TargetBean tb = new TargetBean(target);
                request.setAttribute("targetBean", tb);
            }
            request.setAttribute("constructBean", cb);
            request.setAttribute("spotConstructMilestone", spcm);
            request.setAttribute("experiment", spcm.getExperiment());
            request.setAttribute("bean", BeanFactory.newBean(milestone));

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/SpotConstructMilestoneDetail.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
