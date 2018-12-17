/*
 * Created on 18.07.2005 TODO Error Messages passing
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.InstrumentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.ExperimentWriter;
import org.pimslims.presentation.mru.MRU;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.servlet.Create;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.standard.ViewSample;

public class CreateExperiment extends Create {

    /**
     * PARM_METACLASSNAME String
     */
    static final String PARM_METACLASSNAME = "METACLASSNAME";

    public static final long serialVersionUID = 123243546;

    /**
     * SAMPLE parameter name
     */
    public static final String SAMPLE = "_Sample";

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create a model object";
    }

    /**
     * 
     */
    public CreateExperiment() {
        super();
    }

    /**
     * Specific subclasses will need to override this
     * 
     * @param request
     * @return the name of the metaclass to create, or name:role
     */
    @Override
    protected String getMetaClassName(final HttpServletRequest request) throws ServletException {
        return Experiment.class.getName();
    }

    /**
     * Create.addSpecialObjects
     * 
     * @param response
     * @param rv
     */
    @Override
    protected void addSpecialObjects(final HttpServletRequest request, final ReadableVersion version)
        throws ServletException {

        // for suggesting experiment name
        String projectName = "";
        String experimentTypeName = "";

        final String typeHook = request.getParameter(Experiment.PROP_EXPERIMENTTYPE);
        // protocols
        if (null != typeHook) {
            final ExperimentType type = version.get(typeHook);
            final Collection<Protocol> protocols =
                CreateExperimentOld.activeProtocols(version.findAll(Protocol.class,
                    Protocol.PROP_EXPERIMENTTYPE, type));
            request.setAttribute("protocols", ModelObjectShortBean.getModelObjectShortBeans(protocols));
        } //TODO else filter protocols for sample or instrument

        // sample
        Sample sample = null;
        final String sampleHook = request.getParameter(CreateExperiment.SAMPLE);
        if (null != sampleHook && !"".equals(sampleHook)) {
            sample = version.get(sampleHook);
            projectName =
                org.pimslims.presentation.experiment.DefaultExperimentName.makeConstructName(sample
                    .get_Name());
            MRUController.addObject(PIMSServlet.getUsername(request), sample);
            request.setAttribute("sample", new ModelObjectShortBean(sample));

            request.setAttribute("notebookHook", sample.getAccess().get_Hook());
            final List<RefInputSampleBean> refInputBeans = ViewSample.getRefInputBeans(sample);
            final Collection<ModelObjectBean> protocols = new ArrayList(refInputBeans.size());
            for (final Iterator iterator = refInputBeans.iterator(); iterator.hasNext();) {
                final RefInputSampleBean refInputSampleBean = (RefInputSampleBean) iterator.next();
                protocols.add(refInputSampleBean.getProtocol());
            }
            request.setAttribute("protocols", protocols);
        }
        final List<MRU> samples =
            MRUController.getMRUs(PIMSServlet.getUsername(request), Sample.class.getName());
        //TODO filter for protocol
        request.setAttribute("samples", samples);
        //TODO also offer list of samples for project or instrument or protocol

        // project
        ResearchObjective project = null;
        final String projectHook = request.getParameter(Experiment.PROP_PROJECT);
        if (null != projectHook && !"".equals(projectHook)) {
            project = version.get(projectHook);
        } else if (null != sample && null != sample.getOutputSample()) {
            project = (ResearchObjective) sample.getOutputSample().getExperiment().getProject();
        }
        if (null != project) {
            projectName = project.get_Name();
            MRUController.addObject(PIMSServlet.getUsername(request), project);
            request.setAttribute("project", new ModelObjectShortBean(project));
        }
        final List<MRU> projects =
            MRUController.getMRUs(PIMSServlet.getUsername(request), ResearchObjective.class.getName());
        request.setAttribute("projects", projects);

        ExperimentType type = null;
        final String protocolHook = request.getParameter(Experiment.PROP_PROTOCOL);
        if (null != protocolHook && !"".equals(protocolHook)) {
            final Protocol protocol = version.get(protocolHook);
            MRUController.addObject(PIMSServlet.getUsername(request), protocol);
            type = protocol.getExperimentType();
        }
        final List<MRU> recent =
            MRUController.getMRUs(PIMSServlet.getUsername(request), Protocol.class.getName());
        request.setAttribute("recentProtocols", recent);

        // experiment types
        if (null == type && null != typeHook && !"".equals(typeHook)) {
            type = version.get(typeHook);
        }
        if (null != type) {
            experimentTypeName = type.get_Name();
            request.setAttribute("experimentType", new ModelObjectShortBean(type));
        }
        request.setAttribute("experimentTypes",
            ModelObjectShortBean.getModelObjectShortBeans(CreateExperiment.activeExperimentTypes(version)));

        // instruments
        request.setAttribute("instruments", CreateExperiment.getInstruments(version));
        final String instrumentHook = request.getParameter(Experiment.PROP_INSTRUMENT);
        if (null != instrumentHook && !"".equals(instrumentHook)) {
            experimentTypeName = version.get(instrumentHook).get_Name();
        }

        request.setAttribute("suggestedName",
            version.getUniqueName(Experiment.class, projectName + "-" + experimentTypeName));
    }

    /**
     * CreateExperiment.getInstruments
     * 
     * @param version
     * @return
     * @throws ServletException
     */
    public static List<ModelObjectShortBean> getInstruments(final ReadableVersion version)
        throws ServletException {
        return ModelObjectShortBean.getModelObjectShortBeans(PIMSServlet.getAll(version, Instrument.class));
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);

        final String pathInfo = this.getMetaClassName(request);

        final int classRolesepIdx = pathInfo.indexOf(":");

        final java.util.Map<String, String[]> httpParms = new HashMap(request.getParameterMap());
        final Map errorMessages = new HashMap();

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }
        try {
            final String protocolHook =
                request.getParameter(Experiment.class.getName() + ":" + Experiment.PROP_PROTOCOL);
            final Protocol protocol = version.get(protocolHook);

            // ExperimentType is required by model
            final String typeHook =
                request.getParameter(Experiment.class.getName() + ":" + Experiment.PROP_EXPERIMENTTYPE);
            if (null == typeHook || "".equals(typeHook)) {
                org.pimslims.presentation.mru.MRUController.addObject(PIMSServlet.getUsername(request),
                    protocol);
                final ExperimentType type = protocol.getExperimentType();
                httpParms.put(Experiment.class.getName() + ":" + Experiment.PROP_EXPERIMENTTYPE,
                    new String[] { type.get_Hook() });
            }

            final Map<String, Object> params =
                Create.parseValues(version, httpParms,
                    this.getModel().getMetaClass(Experiment.class.getName()), errorMessages);

            if (errorMessages.size() != 0) {
                final HttpSession session = request.getSession();
                //TODO shouldn't this be request?
                session.setAttribute("errorMessages", errorMessages);
                session.setAttribute("formValues", new HashMap(httpParms));
                version.abort();
                // TODO, send 200 response
                this.redirect(response, request.getContextPath() + "/Create/" + pathInfo);
                return;
            }

            // Everything OK create a model object
            String hook = null;

            String owner = request.getParameter("_OWNER");
            if ("".equals(owner)) {
                owner = "reference";
            }
            params.put(Experiment.PROP_STATUS, "To_be_run");
            final Experiment experiment = version.create(owner, Experiment.class, params);
            ExperimentWriter.createOutputSamplesForExperiment(version, experiment, protocol);

            // record inputs
            HolderFactory.createInputSamplesForExperiment(version, experiment, protocol);
            final String sampleHook = request.getParameter(CreateExperiment.SAMPLE);
            if (null != sampleHook && !"".equals(sampleHook)) {
                this.setSample(version, experiment, sampleHook);
            }
            ExperimentFactory.createProtocolParametersForExperiment(version, protocol, experiment);

            /* TODO 
             * if (null != this.form.get_Input()) {
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(InputSample.PROP_NAME, name);
            final InputSample is = experiment.findFirst(Experiment.PROP_INPUTSAMPLES, criteria);
            assert null != is : "No such input sample in protocol: " + name;
            is.setSample(input);
            experiment.setProject(ExperimentFactory.getTargetOrConstruct(input));
            }

            if (null == this.form.getExperimentName() || "".equals(this.form.getExperimentName())) {
            final ExperimentNameFactory enf =
                PropertyGetter.getInstance("Experiment.Name.Factory", DefaultExperimentName.class);
            experiment.setName(enf.suggestExperimentName(this.wv, experiment, inputSampleName));
            } else {
            experiment.setName(this.form.getExperimentName());
            }
             * */

            ExperimentUtility.setExpBlueprintSamples(experiment, (ResearchObjective) experiment.getProject(),
                false);
            version.commit();
            hook = experiment.get_Hook();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);

        } catch (final AccessException aex) {
            //PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            //writer.print("You are not allowed to make these changes");
            request.setAttribute("javax.servlet.error.exception", aex);
            request.getRequestDispatcher("/public/Denied").forward(request, response);
            return;
        } catch (final ConstraintException cex) {
            version.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * CreateExperiment.setSample
     * 
     * Assigns the sample as an input of the experiment. This uses the first suitable input. Most protocols
     * have no more than one input for any given sample category. Exceptions are complexation, and bicistronic
     * infusion, where there are two equivalent inputs. In principle, there could be two inputs, using samples
     * in different uses. In these cases, the right thing to do is design sample categories to distinguish
     * these uses.
     * 
     * @param version
     * @param experiment
     * @param sampleHook
     * @throws ConstraintException
     */
    private void setSample(final WritableVersion version, final Experiment experiment, final String sampleHook)
        throws ConstraintException {
        final Sample sample = version.get(sampleHook);
        final Set<SampleCategory> categories = sample.getSampleCategories();
        final List<InputSample> iss = experiment.getInputSamples();
        for (final Iterator iterator = iss.iterator(); iterator.hasNext();) {
            final InputSample is = (InputSample) iterator.next();
            if (categories.contains(is.getRefInputSample().getSampleCategory())) {
                is.setSample(sample);
                return;
            }
        }
        throw new IllegalArgumentException("No suitable role for sample: " + sample.get_Name());
    }

    /**
     * 
     * CreateExperiment.activeExperimentTypes return a collection of experimentTypes with protocols
     * 
     * @param version
     * @return
     * @throws ServletException
     */
    //TODO do this with HQL
    public static Collection<ModelObject> activeExperimentTypes(final ReadableVersion version)
        throws ServletException {

        final Collection<ModelObject> protocols =
            PIMSServlet.getAll(version, org.pimslims.model.protocol.Protocol.class);

        final Set<ModelObject> experimentTypes = new TreeSet<ModelObject>();
        for (final ModelObject object : protocols) {
            final Protocol protocol = (Protocol) object;
            experimentTypes.add(protocol.getExperimentType());
        }
        return experimentTypes;
    }

    /**
     * CreateExperiment.getProtocols
     * 
     * @param type
     * @return TODO move this to data model
     */
    public static Collection<Protocol> getProtocols(final InstrumentType type) {
        final ReadableVersion version = type.get_Version();
        return version.findAll(Protocol.class, Protocol.PROP_INSTRUMENTTYPE, type);
    }

}
