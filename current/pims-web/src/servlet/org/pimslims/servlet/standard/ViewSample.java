/**
 * 
 */
package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.experiment.ExperimentReader;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.mru.MRU;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.presentation.sample.SampleLocationBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 */
public class ViewSample extends PIMSServlet {

    /**
     * 
     */
    public ViewSample() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Custom view of a sample";
    }

    /*
     * (non-Javadoc)
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
            MetaClass metaClass = null; // type to show, if any
            final Sample sample = (Sample) version.get(pathInfo); // e.g.
            // Example/org.pimslims.model.experiment.Experiment:42355
            if (null == sample) {
                this.writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Experiment experiment = null;
            //final Project expBlueprint = ExperimentFactory.getTargetOrConstruct(sample);
            final OutputSample os = sample.getOutputSample();
            if (null != os) {
                experiment = os.getExperiment();
            }

            final Set<SampleCategory> categories = sample.getSampleCategories();
            request.setAttribute("categories", ModelObjectShortBean.getModelObjectShortBeans(categories));
            if (null != sample.getRefSample()) {
                request.setAttribute("refSample", new ModelObjectBean(sample.getRefSample()));
            }
            final Set<SampleComponent> components = sample.getSampleComponents();
            request.setAttribute("components", ModelObjectBean.getModelObjectBeans(components));
            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(sample)); // slow
            metaClass = sample.get_MetaClass();
            request.setAttribute("metaClass", metaClass);
            //request.setAttribute("refInputSamples", ViewSample.getRefInputSamples(sample));

            //TODO remove, do this in CreateExperiment only
            request.setAttribute("refInputBeans", ViewSample.getRefInputBeans(sample));

            final String userName = PIMSServlet.getUsername(request);

            //TODO make two different methods
            request.setAttribute("usedIn", ViewSample.getUsedIn(sample, userName));
            request.setAttribute("couldUseIn", ViewSample.getCouldUse(sample, userName));

            request.setAttribute("mayUpdate", sample.get_MayUpdate());
            request.setAttribute("owner", sample.get_Owner());
            request.setAttribute("currentLocation", SampleLocationBean.getCurrentLocation(sample));
            request.setAttribute("currentLocationTrail", SampleLocationBean.getCurrentLocationTrail(sample));
/*
            final Collection<User> people =
                ViewSample.getAllSorted(version, org.pimslims.model.accessControl.User.class);
            request.setAttribute("userPersons", ModelObjectBean.getModelObjectBeans(people)); */
            if (sample.getAssignTo() != null) {
                request.setAttribute("personAssignedTo", new ModelObjectBean(sample.getAssignTo()));
            }

            /* was for PIMS-2953, but is very slow if you have crystallization data
            request.setAttribute("holders", ViewSample.getHolders(version));   
            */
            if (null != sample.getHolder()) {
                request.setAttribute("holder", BeanFactory.newBean(sample.getHolder()));
            }

            if (experiment != null) {
                request.setAttribute("experiment", new ModelObjectShortBean(experiment));
                final List<Parameter> parameters = ExperimentReader.getParameters(experiment);
                request.setAttribute("parameters", parameters);

                if (null != experiment.getExperimentGroup()) {
                    request.setAttribute("experimentGroup",
                        new ModelObjectShortBean(experiment.getExperimentGroup()));
                }
            }
/*
            if (expBlueprint != null) {
                request.setAttribute("expBlueprint", new ModelObjectShortBean((ModelObject) expBlueprint));
            } */

            final Measurement measurement =
                Measurement.getMeasurement(Sample.PROP_CURRENTAMOUNT, sample, Sample.PROP_AMOUNTUNIT,
                    Sample.PROP_AMOUNTDISPLAYUNIT);
            request.setAttribute("amount", measurement);
            final Measurement concentration =
                Measurement.getMeasurement(Sample.PROP_CONCENTRATION, sample, Sample.PROP_CONCENTRATIONUNIT,
                    Sample.PROP_CONCDISPLAYUNIT);
            request.setAttribute("concentration", concentration);

            if (new Float(0f) < concentration.getValue()) {
                request.setAttribute("divideEnabled", new Boolean(true));
            }
            request.setAttribute("sample", BeanFactory.newBean(sample));
            request.setAttribute("primer", PrimerBeanReader.readPrimer(sample));

            // TODO set of associated samples
            request.setAttribute("associates", this.getAssociates(sample));
            response.setStatus(HttpServletResponse.SC_OK);

            //FOR SYNTHETIC GENE SAMPLES
            final SampleCategory samCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Synthetic gene");
            if (categories.contains(samCat)) {
                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/read/SyntheticGene/" + pathInfo);
                dispatcher.forward(request, response);
                version.commit();
            } else {

                final RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/JSP/view/org.pimslims.model.sample.Sample.jsp");
                dispatcher.forward(request, response);
                version.commit();
            }
        } catch (final AbortedException e1) {
            throw new ServletException(e1);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }

    }

    /**
     * @param sample
     * @return all the roles it can play as input for a protocol
     */
    public static List<RefInputSampleBean> getRefInputBeans(final Sample sample) {

        final Set<SampleCategory> categories = sample.getSampleCategories();
        Set<Workflow> workflows = Collections.EMPTY_SET;
        if (null != sample.getOutputSample()) {
            final Project project = sample.getOutputSample().getExperiment().getProject();
            if (null != project) {
                workflows = project.getWorkflows();
            }
        }
        final List<RefInputSampleBean> ret = new ArrayList<RefInputSampleBean>();
        for (final Iterator iter = categories.iterator(); iter.hasNext();) {
            final SampleCategory category = (SampleCategory) iter.next();

            for (final RefInputSample refInputSample : category.getRefInputSamples()) {
                final Protocol protocol = refInputSample.getProtocol();
                if (null != protocol.getIsForUse() && !protocol.getIsForUse()) {
                    continue;
                }
                if (!workflows.isEmpty()) {
                    // this sample has defined workflows, respect them
                    final Collection<Workflow> w =
                        sample.get_Version().findAll(Workflow.class, Workflow.PROP_PROTOCOLS, protocol); //TODO protocol.getWorkflows();
                    w.retainAll(workflows);
                    if (w.isEmpty()) {
                        continue;
                    }
                }

                // filter by isotope labelling
                if (org.pimslims.lab.NMR.isNmr(protocol)) {
                    if (org.pimslims.lab.NMR.isSuitable(sample, protocol)) {
                        ret.add(new RefInputSampleBean(refInputSample));
                    }
                } else {
                    ret.add(new RefInputSampleBean(refInputSample));
                }
            }
        }
        // too slow Collections.sort(refInputSamplesForNextExpt, RefInputSampleBean.NUMBER_OF_EXPERIMENTS);
        return ret;
    }

    /**
     * 
     * ViewSample.getInputBeans
     * 
     * @param sample
     * @param userName
     * @return beans representing all recently viewed experiments that this could be an input to TODO include
     *         all the experiments this was actually an input to
     */
    public static List<InputSampleBean> getCouldUse(final AbstractSample sample, final String userName) {
        final List<InputSampleBean> inputSampleBeans = new ArrayList<InputSampleBean>();

        final List<MRU> MRUs =
            new ArrayList<MRU>(MRUController.getMRUs(userName, Experiment.class.getName()));
        for (final ModelObjectShortBean mru : MRUs) {
            final Experiment experiment = sample.get_Version().get(mru.getHook());
            final Collection<InputSample> inputSamples = experiment.getInputSamples();
            for (final InputSample inputSample : inputSamples) {
                if (sample == inputSample.getSample()) {
                    continue;
                }
                final RefInputSample ris = inputSample.getRefInputSample();
                if (null == ris) {
                    continue;
                }
                if (sample.getSampleCategories().contains(ris.getSampleCategory())) {
                    inputSampleBeans.add(new InputSampleBean(inputSample));
                }
            }
        }
        return inputSampleBeans;
    }

    public static List<InputSampleBean> getUsedIn(final Sample sample, final String userName) {
        final Set<InputSample> iss = sample.getInputSamples();
        final List<InputSampleBean> ret = new ArrayList(iss.size());
        for (final Iterator iterator = iss.iterator(); iterator.hasNext();) {
            final InputSample inputSample = (InputSample) iterator.next();
            ret.add(new InputSampleBean(inputSample));
        }
        return ret;
    }

    public static Collection getAllSorted(final ReadableVersion version, final Class javaClass)
        throws ServletException {

        final List list = new ArrayList();
        list.addAll(PIMSServlet.getAll(version, javaClass));
        Collections.sort(list, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return list;
    }

    private Collection<ModelObjectBean> getAssociates(final Sample sample) {

        final OutputSample output = sample.getOutputSample();
        if (null == output) {
            return Collections.EMPTY_SET;
        }

        final String role = output.getRole();
        final Experiment experiment = output.getExperiment();
        if (null == experiment) {
            return Collections.EMPTY_SET;
        }

        final Collection<ModelObjectBean> associates = new HashSet<ModelObjectBean>();
        for (final OutputSample outputSample : experiment.getOutputSamples()) {
            if (outputSample.getRole().equals(role)) {
                final Sample associate = outputSample.getSample();
                if (null != associate) {
                    if (associate.getDbId() != sample.getDbId()) {
                        associates.add(new ModelObjectBean(associate));
                    }
                }
            }
        }
        return associates;
    }

    @Deprecated
    // seems not to be used 
    private static Collection<ModelObjectBean> getHolders(final ReadableVersion version)
        throws ServletException {

        final Collection<Holder> holders =
            ViewSample.getAllSorted(version, org.pimslims.model.holder.Holder.class);
        final List<ModelObjectBean> beans = new ArrayList<ModelObjectBean>();
        for (final Holder holder : holders) {
            if (!HolderFactory.isPlate(holder.getHolderType())) {
                beans.add(new ModelObjectBean(holder));
            }
        }
        Collections.sort(beans);
        return beans;
    }

}
