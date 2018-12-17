/**
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * We need to speed this up
 * 
 * @author Johan van Niekerk
 * 
 */
public class SpotConstructMilestone extends
/* common servlet that provides utility methods */PIMSServlet {

    @Override
    public String getServletInfo() {
        return "SPoT construct Milestone page";
    }

    /**
     * Note that this servlet uses the writeHead() method, and the JSP contains no header. This is because
     * this servlet displays a progress bar. If no progress bar is used, it is better to show the header in
     * the JSP page.
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
        request.getPathInfo();
        final String commonName = request.getParameter(("commonName"));
        if (null == commonName) {
            this.writeErrorHead(request, response, "Construct not specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        try {
            ResearchObjective eb = null;
            final HashMap m = new HashMap();
            m.put("commonName", commonName);
            final Collection c = version.findAll(org.pimslims.model.target.ResearchObjective.class, m);
            final Iterator i = c.iterator();
            if (i.hasNext()) {
                final ModelObject mo = (ModelObject) i.next();
                eb = (ResearchObjective) mo;
            } else {
                this.writeErrorHead(request, response, "Construct not specified",
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final ConstructBean cb = ConstructBeanReader.readConstruct(eb);
            final Collection<Experiment> experiments = eb.getExperiments();
            final List<ConstructResultBean> spotConstructMilestones = new ArrayList(experiments.size());
            final Iterator i2 = experiments.iterator();

            while (i2.hasNext()) {
                final Experiment experiment = (Experiment) i2.next();
                final ConstructResultBean sptcm = new ConstructResultBean(version, experiment, cb);
                spotConstructMilestones.add(sptcm);
            }
            Collections.sort(spotConstructMilestones);
            for (final ConstructResultBean rb : spotConstructMilestones) {
                if (rb.getMilestoneName() != null) {
                    rb.setLastResult(true);
                    break;
                }
            }

            final TargetBean tb = cb.getTargetBean();

            // SPOTTarget(version, ((ResearchObjectiveElement)
            // eb.findFirst(ResearchObjective.PROP_RESEARCHOBJECTIVEELEMENTS)).getTarget());
            // show a progress bar
            // ProgressListener progressBar = new
            // ProgressListener(results.size(), writer, "Formating results...");
            request.setAttribute("targetBean", tb);
            request.setAttribute("constructBean", cb);
            request.setAttribute("spotConstructMilestones", spotConstructMilestones);
            // Collection attributes = ServletUtil.getFilledAttributeNames(
            // results );
            // request.setAttribute("attributes", attributes);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/SpotConstructMilestone.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

}
