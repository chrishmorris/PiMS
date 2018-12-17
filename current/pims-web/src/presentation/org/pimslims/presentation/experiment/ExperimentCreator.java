package org.pimslims.presentation.experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.properties.PropertyGetter;

/**
 * This is a presentation model for creating experiments.
 * 
 * For viewing and editing a experiment, the previous code is probably fine. This class manages only the early
 * stages, and creates the experiment. Then the user can edit the new experiment.
 * 
 * @author cm65
 * @date July 2006
 */
@Deprecated
// obsolete
public class ExperimentCreator {

    /**
     * @author cm65
     * 
     */
    public static class ExperimentCreationForm {

        public String experimentTypeHook;

        public String protocolHook;

        public String projectHook;

        /**
         * e.g. "insert:org.pimslims.model.sample.Sample:5235"
         */
        private String input;

        private String experimentBlueprintHook;

        public String experimentName;

        public String get_Input() {
            return this.input;
        }

        public void set_Input(final String string) {
            this.input = string;
        }

        public void setProtocolHook(final String hook) {
            this.protocolHook = hook;
        }

        public void setExperimentTypeHook(final String hook) {
            this.experimentTypeHook = hook;
        }

        public void setExperimentBlueprintHook(final String parameter) {
            this.experimentBlueprintHook = parameter;
        }

        public String getExperimentBlueprintHook() {
            return this.experimentBlueprintHook;
        }

        public void setExperimentName(final String parameter) {
            this.experimentName = parameter;
        }

        public String getExperimentName() {
            return this.experimentName;
        }

        public void setLabNotebookHook(final String hook) {
            this.projectHook = hook;
        }

    }

    /**
     * ${name}:${sampleHook}
     */
    private static final Pattern INPUT = Pattern.compile("^([^:]+):([a-zA-Z.]+:\\d+)$");

    private final ExperimentCreationForm form;

    // private final ReadableVersion rv;
    private final WritableVersion wv;

    /**
     * Constructor to use to start with
     * 
     * @param form
     * @param rv
     */
    public ExperimentCreator(final ReadableVersion rv) {
        super();
        this.form = new ExperimentCreationForm();
        // this.rv = rv;
        this.wv = null;
    }

    /**
     * Constructor to use part way through
     * 
     * @param form
     * @param rv
     */
    public ExperimentCreator(final ExperimentCreationForm form, final ReadableVersion rv) {
        super();
        this.form = form;
        // this.rv = rv;
        this.wv = null;
    }

    /**
     * Constructor to use when ready to save
     * 
     * @param form
     * @param wv
     */
    public ExperimentCreator(final ExperimentCreationForm form, final WritableVersion wv) {
        super();
        this.form = form;
        // this.rv = wv;
        this.wv = wv;
    }

    /**
     * Scientific logic to create the experiment
     * 
     * @return the new experiment
     * @throws AccessException
     * @throws ConstraintException
     */
    public Experiment save() throws AccessException, ConstraintException {

        // prepare the attributes for the experiment
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Experiment.PROP_STATUS, "To_be_run");
        final ModelObject experimentType = this.wv.get(this.form.experimentTypeHook);
        attributes.put(Experiment.PROP_EXPERIMENTTYPE, experimentType);
        final Protocol protocol = (Protocol) this.wv.get(this.form.protocolHook);

        if (this.form.projectHook != null) {
            final ModelObject project = this.wv.get(this.form.projectHook);
            attributes.put(LabBookEntry.PROP_ACCESS, project);
        }
        // this is a temp name
        attributes.put(Experiment.PROP_NAME, "temp" + System.currentTimeMillis());

        attributes.put(Experiment.PROP_STARTDATE, java.util.Calendar.getInstance());
        attributes.put(Experiment.PROP_ENDDATE, java.util.Calendar.getInstance()); // LATER
        // remove

        // process specified input
        Sample input = null;
        String name = null;
        String inputSampleName = null;
        if (null != this.form.get_Input()) {
            final Matcher m = ExperimentCreator.INPUT.matcher(this.form.get_Input());
            if (!m.matches()) {
                throw new AssertionError("Invalid value for input: " + this.form.get_Input());
            }
            final String hook = m.group(2);
            name = m.group(1);
            input = this.wv.get(hook);
            inputSampleName = input.getName();
            if (null != input.getAccess()) {
                final String owner = input.getAccess().getName();
                final MetaClass metaclass = this.wv.getMetaClass(Experiment.class);
                if (this.wv.mayCreate(owner, metaclass, attributes)) {
                    this.wv.setDefaultOwner(owner);
                }
            }
        }

        final Experiment experiment = this.wv.create(Experiment.class, attributes);

        experiment.setProtocol(protocol);
        ExperimentFactory.createProtocolParametersForExperiment(this.wv, protocol, experiment);
        HolderFactory.createInputSamplesForExperiment(this.wv, experiment, protocol);

        if (null != this.form.get_Input()) {
            final Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(InputSample.PROP_NAME, name);
            final InputSample is = experiment.findFirst(Experiment.PROP_INPUTSAMPLES, criteria);
            assert null != is : "No such input sample in protocol: " + name;
            is.setSample(input);
            //was experiment.setProject(ExperimentFactory.getTargetOrConstruct(input));
        }
        if (null != this.form.getExperimentBlueprintHook()) {
            final ResearchObjective eb = this.wv.get(this.form.getExperimentBlueprintHook());
            experiment.setProject(eb);
        }

        if (null == this.form.getExperimentName() || "".equals(this.form.getExperimentName())) {
            final ExperimentNameFactory enf =
                PropertyGetter.getInstance("Experiment.Name.Factory", DefaultExperimentName.class);
            experiment.setName(enf.suggestExperimentName(this.wv, experiment, inputSampleName));
        } else {
            experiment.setName(this.form.getExperimentName());
        }
        ExperimentWriter.createOutputSamplesForExperiment(this.wv, experiment, protocol);

        return experiment;

    }

    @Deprecated
    // tested but not used
    static void setProtocol(final WritableVersion wv, final Protocol protocol, final Experiment experiment)
        throws ConstraintException, AccessException {
        experiment.setProtocol(protocol);
        ExperimentFactory.createProtocolParametersForExperiment(wv, protocol, experiment);
        ExperimentWriter.createOutputSamplesForExperiment(wv, experiment, protocol);
        HolderFactory.createInputSamplesForExperiment(wv, experiment, protocol);
    }
}
