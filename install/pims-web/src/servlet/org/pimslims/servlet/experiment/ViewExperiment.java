/**
 * 
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.WorkflowItem;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.experiment.ExperimentReader;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.OutputSampleBean;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class ViewExperiment extends PIMSServlet {

    /**
     * 
     */
    public ViewExperiment() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of an experiment";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        final java.io.PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Experiment experiment = (Experiment) version.get(pathInfo); // e.g.
            // Example/org.pimslims.model.experiment.Experiment:42355
            if (null == experiment) {
                this.writeErrorHead(request, response, "Experiment not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ViewExperiment.forward(request, response, experiment, version);
            version.commit();
        } catch (final AbortedException e1) {
            this.log("example aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } catch (final AccessException e) {
            // should not happen in read
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }

    }

    /**
     * 
     */
    public static void forward(final HttpServletRequest request, final HttpServletResponse response,
        final Experiment experiment, final ReadableVersion version) throws ServletException, AccessException,
        AbortedException, ConstraintException, IOException {

        final String UserName = PIMSServlet.getUsername(request);
        final ExperimentReader emc = new ExperimentReader(version, experiment);
        // no longer ExperimentTree.loadExperimentTree(request, version);

        //final String expType = emc.getExperiment().getExperimentType().get_Name();

        final Collection<Sample> except = ExperimentReader.getOutputsFromGroup(experiment);
        final List<InputSampleBean> inputsamples =
            ExperimentReader.getInputSamples(experiment, UserName, except);

        // Note this code is used in PiMS 2625 customising for PCR 
        if (null != experiment.getProject()) {
            try {
                final ConstructBean cb =
                    ConstructBeanReader.readConstruct((ResearchObjective) experiment.getProject());
                request.setAttribute("pcrProductSize", cb.getPcrProductSize());
            } catch (final AssertionError e) {
                // fails for polycistronic constructs at present
            }

            request.setAttribute("researchObjective",
                new ModelObjectShortBean((ModelObject) experiment.getProject()));
        }

        request.setAttribute("inputsamples", inputsamples);
        final List<OutputSampleBean> outputsamples = emc.getOutputSamples();
        request.setAttribute("outputsamples", outputsamples);
        request.setAttribute("experimentType", BeanFactory.newBean(experiment.getExperimentType()));
        if (null != experiment.getProtocol()) {
            request.setAttribute("protocol", BeanFactory.newBean(experiment.getProtocol()));
        } else {
            request.setAttribute("protocol", null);
        }
        /*if (null != experiment.getProject()) {
            final ConstructBean cb = ConstructBeanReader.readConstruct(experiment.getProject());
            request.setAttribute("researchObjective", cb);
        } */

        final List<Parameter> parameters;
        parameters = ExperimentReader.getParameters(experiment);
        request.setAttribute("parameters", parameters);
        final MetaClass metaClass = experiment.get_MetaClass();
        request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));

        final Boolean isLocked = Boolean.TRUE.equals(experiment.getIsLocked());
        request.setAttribute("mayUpdate", emc.get_MayUpdate() && !isLocked);
        request.setAttribute("mayUnlock",
            ((ReadableVersionImpl) version).getAccessController().mayUnlock(experiment));
        request.setAttribute("milestoneName", ViewExperiment.getMilestoneName(experiment));
        if (experiment.getMilestones() != null && experiment.getMilestones().size() > 0) {
            request.setAttribute("milestoneAchieved", true);
        } else {
            request.setAttribute("milestoneAchieved", false);
        }
        request.setAttribute("modelObject", BeanFactory.newBean(emc.getExperiment()));
        final Holder plate = emc.getPlate();
        if (null != plate) {
            request.setAttribute("plateName", plate.get_Name());
            request.setAttribute("plateHook", plate.get_Hook());
        }

        final ExperimentGroup experimentGroup = experiment.getExperimentGroup();
        if (null != experimentGroup) {
            request.setAttribute("experimentGroup", BeanFactory.newBean(experimentGroup));
        }
        request.setAttribute("bean", BeanFactory.newBean(experiment));
        // get possible expb from mru
        // A map of hook & name of related target or expb
        final Map<String, String> expbFromMru = ViewExperiment.getExpbFromMru(version, UserName, experiment);
        request.setAttribute("PossibleExpb", expbFromMru);

        request.setAttribute("owner", emc.getOwner());
        request.setAttribute("writers", PIMSServlet.findWriters(experiment.getAccess()));

        //final Collection<DbName> dbNames =
        //    PIMSServlet.getAll(version, org.pimslims.model.reference.DbName.class);

        final List<ModelObjectBean> dbNames = new ArrayList<ModelObjectBean>();
        dbNames.addAll(ModelObjectBean.getModelObjectBeans(PIMSServlet.getAll(version,
            org.pimslims.model.reference.Database.class)));

        // too slow final Collection<ModelObjectBean> holders = ViewSample.getHolders(version);
        // request.setAttribute("holders", holders);

        // PiMS 2758 sort unspecified to the top of the list
        Collections.sort(dbNames, new Comparator<ModelObjectBean>() {
            public int compare(final ModelObjectBean c1, final ModelObjectBean c2) {
                if (c1.getName().equals("unspecified")) {
                    return -1;
                }
                if (c2.getName().equals("unspecified")) {
                    return 1;
                }
                return c1.getName().compareTo(c2.getName());
            }
        });

        request.setAttribute("dbnames", dbNames);
        if (null != experiment.getProtocol()) {
            request.setAttribute("protocolNameEscaped",
                URLEncoder.encode(experiment.getProtocol().getName(), "UTF-8"));
        }

        String url;
        if (request.getParameter("print") != null && request.getParameter("print").equals("true")) {
            url = "/JSP/experiment/ExperimentPrintPreview.jsp";
        } else {
            url = "/JSP/experiment/ExperimentDetails.jsp";
        }

        final RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private static String getMilestoneName(final Experiment experiment) {
        final ExperimentType expType = experiment.getExperimentType();
        final Set<WorkflowItem> workflows = expType.getWorkflowItems();
        if (workflows.size() != 1) {
            return null;
        }
        final WorkflowItem workflowItem = workflows.iterator().next();
        if (null == workflowItem.getStatus()) {
            return expType.getName();
        }
        return workflowItem.getStatus().getName();
    }

    /**
     * get expb candidates from target & expb MRU, but exclude the current expb used in experiment
     * 
     * @param version
     * @param userName
     * @param experiment
     * @return
     */
    static Map<String, String> getExpbFromMru(final ReadableVersion version, final String userName,
        final Experiment experiment) {
        if (experiment != null && experiment.getProject() != null) {
            return MRUController.getPossibleMRUItems(version, ResearchObjective.class, experiment
                .getResearchObjective().get_Hook(), false);
        }
        return MRUController.getPossibleMRUItems(version, ResearchObjective.class, null, false);

    }

}
