/**
 * current-pims-web org.pimslims.lab.experiment ExperimentWriter.java
 * 
 * @author pvt43
 * @date 2 Jun 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 *
 * 
 */
package org.pimslims.utils.experiment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.create.ValueConverter;

/**
 * ExperimentWriter This class is to be used to create/update experiment with the values provided in the map
 * which fields correspond with protocol details.
 * 
 * Please find example of this class use
 * 
 * @see {@link ExperimentWizardCreatorTester}
 */
@Deprecated
// no longer supported
public class ExperimentWizardCreator {

    private final WritableVersion version;

    /*
     * Resulting Experiment 
     */
    private Experiment experiment;

    /*
     * Protocol to create experiment based upon
     */
    private final Protocol protocol;

    /**
     * Class to decode field names
     * 
     */
    private final ExperimentWizardFieldsDecoder decoder;

    /**
     * 
     * Constructor for ExperimentWizardCreator
     */
    public ExperimentWizardCreator(final WritableVersion rw, final Protocol protocol,
        final ExperimentWizardFieldsDecoder decoder) throws AccessException, ConstraintException {
        this.version = rw;
        this.protocol = protocol;
        this.decoder = decoder;
        //this.validateProtocol();
        this.writeExperiment();
    }

    public Experiment getExperiment() {
        return this.experiment;
    }

    void writeExperiment() throws AccessException, ConstraintException {
        this.experiment = this.recordExperiment(this.version, this.protocol.getExperimentType());
        this.setExperimentProperties();
        this.setParameters();
        this.setSamplesforInput();
        this.setSamplesforOutput();
    }

    /*
     * Values from here will be all redefined later!
     */
    public Experiment recordExperiment(final WritableVersion rw, final ModelObject expType)
        throws AccessException, ConstraintException {

        final Map<String, Object> experimentProp = Util.getNewMap();
        String experimentName = this.decoder.getExpFieldValue(Experiment.PROP_NAME);

        if (Util.isEmpty(experimentName)) { // Time consuming || !Creator.isExperimentNameUnique(experimentName, rw)
            experimentName += System.currentTimeMillis();
        }

        // experimentProp.put(Experiment.PROP_EXPERIMENTGROUP,
        // getExperimentGroup(plasmid));

        assert experimentName != null : "Name is null! ";
        experimentProp.put(Experiment.PROP_NAME, experimentName);

        experimentProp.put(Experiment.PROP_STARTDATE, Util.getCalendar(null));
        experimentProp.put(Experiment.PROP_ENDDATE, Util.getCalendar(null));

        final String comments = this.decoder.getExpFieldValue(LabBookEntry.PROP_DETAILS);
        if (comments != null) {
            experimentProp.put(LabBookEntry.PROP_DETAILS, comments.trim());
        }

        experimentProp.put(Experiment.PROP_EXPERIMENTTYPE, Util.makeList(expType));
        experimentProp.put(Experiment.PROP_STATUS, "OK");

        Experiment experiment;

        // Dataowner may not be set properly if the experiment has been already
        // created
        // experiment = (Experiment)Util.create(dataOwner, Experiment.class,
        // experimentProp);

        experiment = (Experiment) Util.create(rw, Experiment.class, experimentProp);

        return experiment;
    }

    /**
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    private void setExperimentProperties() throws ConstraintException, AccessException {

        final String startDate = this.decoder.getExpFieldValue(Experiment.PROP_STARTDATE);
        if (!Util.isEmpty(startDate)) {
            this.experiment.setStartDate(ExperimentWizardCreator.getCalendar(startDate));
        }
        final String endDate = this.decoder.getExpFieldValue(Experiment.PROP_ENDDATE);
        if (!Util.isEmpty(endDate)) {
            this.experiment.setEndDate(ExperimentWizardCreator.getCalendar(endDate));
        }
        final String lastEditedDate = this.decoder.getExpFieldValue(LabBookEntry.PROP_LASTEDITEDDATE);
        if (!Util.isEmpty(lastEditedDate)) {
            this.experiment.setLastEditedDate(ExperimentWizardCreator.getCalendar(lastEditedDate));
        }

        this.experiment.setProtocol(this.protocol);

        final HashMap<String, Object> expAttr = new HashMap<String, Object>();

        this.setExperimentValue(LabBookEntry.PROP_DETAILS, expAttr);
        this.setExperimentValue(LabBookEntry.PROP_CREATOR, expAttr);
        this.setExperimentValue(Experiment.PROP_OPERATOR, expAttr);
        this.setExperimentValue(LabBookEntry.PROP_LASTEDITOR, expAttr);
        this.setExperimentValue(Experiment.PROP_STATUS, expAttr);

        this.experiment.set_Values(expAttr);

    }

    private void setExperimentValue(final String propertyName, final Map attrMap) {
        this.setExperimentValue(propertyName, null, attrMap);
    }

    /**
     * @param ris
     * @param refSname
     * @param attrMap
     */
    private void setExperimentValue(final String propertyName, final Object altValue, final Map attrMap) {
        final String sVal = this.decoder.getExpFieldValue(propertyName);
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(Experiment.class));
    }

    private void setValue(final String value, final String propertyName, final Object altValue,
        final Map attrMap, final MetaClass metaClass) {

        final ValueConverter vc = new ValueConverter(metaClass, new HashMap());
        if (value != null) {
            if (Util.isHookValid(value)) {
                attrMap.put(propertyName, this.version.get(value));
            } else {
                attrMap.put(propertyName, vc.getConvertedValue(propertyName, value));
            }
        } else if (altValue != null) {

            attrMap.put(propertyName, altValue); // assume for now that altValue are always converted to appropriate wrapper, e.g. Boolean or Numeric as opposed to String
        } else {
            // do not set anything, its ok
        }
    }

    private Map<String, OutputSample> setOutputSamples(final Map<String, RefOutputSample> pmap,
        final Map<String, String> defOutputSamples) throws AccessException, ConstraintException {
        final Map<String, OutputSample> outSamples = new HashMap();
        for (final String key : pmap.keySet()) {
            final RefOutputSample ros = pmap.get(key);
            assert ros != null : "Reference Output Sample '" + key + "' is not found within the protocol!";

            // Create InputSample based on the ReferenceInputSample properties
            final String refSname = ros.getName();
            final Map attrMap = new HashMap();
            attrMap.put(OutputSample.PROP_EXPERIMENT, this.experiment);
            attrMap.put(OutputSample.PROP_REFOUTPUTSAMPLE, ros);
            attrMap.put(OutputSample.PROP_NAME, refSname);
            attrMap.put(OutputSample.PROP_AMOUNT,
                defOutputSamples.get(this.decoder.getInputSampleFieldValue(refSname)));
            attrMap.put(OutputSample.PROP_AMOUNTUNIT, defOutputSamples.get(refSname));
            attrMap.put(OutputSample.PROP_AMOUNTDISPLAYUNIT, defOutputSamples.get(refSname));

            this.setOutputSampleValue(refSname, OutputSample.PROP_AMOUNT, ros.getAmount(), attrMap);
            this.setOutputSampleValue(refSname, OutputSample.PROP_AMOUNTUNIT, ros.getUnit(), attrMap);
            this.setOutputSampleValue(refSname, OutputSample.PROP_AMOUNTDISPLAYUNIT, ros.getDisplayUnit(),
                attrMap);

            final OutputSample outSample = this.version.create(OutputSample.class, attrMap);
            outSamples.put(outSample.getName(), outSample);
        }
        return outSamples;
    }

    /**
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    private void setSamplesforOutput() throws ConstraintException, AccessException {

        // Set/Create/update new Samples which are undefined in experiment
        final Map<String, String> defOutputSamples =
            this.decoder.getPrmsSubset(ExperimentWizardFieldsDecoder.code.O);
        final Map<String, RefOutputSample> pmap = this.makeOutSampleMap(this.protocol.getRefOutputSamples());
        final Map<String, OutputSample> outSamples = this.setOutputSamples(pmap, defOutputSamples);
        final Map<String, Sample> samples = new HashMap<String, Sample>();
        final Map<String, RefSample> refSamples = this.getRefSamples(pmap);

        for (final String key : this.decoder.getPrmsSubset(ExperimentWizardFieldsDecoder.code.S).keySet()) {
            final RefOutputSample ros = pmap.get(key);
            if (ros == null) {
                continue; // This may be a RefInputSample 
            }

            final String refOutSname = ros.getName();
            final RefSample refSample = refSamples.get(refOutSname);
            final String sampleHook = this.decoder.getSampleFieldValue(refOutSname);
            //If sample exist
            if (!Util.isEmpty(sampleHook)) {

                final Map<String, String> props = this.decoder.getAllSampleProps(refOutSname);
                assert props.size() == 0 : "WARNING: Some properties defined but will not be set!"
                    + " If the hook or the name only specified no other properties can be added!";

                final Sample sample = this.getSample(refOutSname, sampleHook);
                if (sample != null) {
                    outSamples.get(refOutSname).setSample(sample);
                    //sample.setOutputSample(outSamples.get(refOutSname));
                    if (refSample != null) {
                        sample.setRefSample(refSample);
                    }
                    samples.put(refOutSname, sample);
                    //"Cannot edit the properties of a sample if given by hook or by name!";
                    continue;
                } else {
                    throw new RuntimeException("Cannot find sample specified by hook or name. Data given:"
                        + sampleHook);
                }

            }

            // If a new sample needs to be created 
            final Map<String, Object> attrMap = Util.getNewMap();

            final Map<String, String> sprop = this.decoder.getAllSampleProps(refOutSname);
            for (final String propName : sprop.keySet()) {
                this.setSampleValue(refOutSname, propName, sprop.get(propName), attrMap);
            }

            this.applyPolicy(attrMap);

            attrMap.put(Sample.PROP_OUTPUTSAMPLE, outSamples.get(refOutSname));
            if (refSample != null) {
                attrMap.put(Sample.PROP_REFSAMPLE, refSamples.get(refOutSname));
            }
            final Sample sample = this.version.create(Sample.class, attrMap);

            samples.put(refOutSname, sample);
        }
        this.setSampleComponentsforSamples(pmap, samples);
    }

    private Sample getSample(final String refSampleName, final String hookOrName) {
        Sample sample = null;
        if (Util.isHookValid(hookOrName)) {
            sample = this.version.get(hookOrName);
        } else {
            // Assume this is a name for search and try to get this sample 
            sample = this.version.findFirst(Sample.class, AbstractSample.PROP_NAME, hookOrName);
        }
        return sample;
    }

    private RefSample getRefSample(final String refSampleName, final String hookOrName) {
        RefSample rsample = null;
        if (Util.isHookValid(hookOrName)) {
            rsample = this.version.get(hookOrName);
        } else {
            // Assume this is a name for search and try to get this sample 
            rsample = this.version.findFirst(RefSample.class, AbstractSample.PROP_NAME, hookOrName);
        }
        return rsample;
    }

    private void applyPolicy(final Map<String, Object> attrMap) {

        if (this.decoder.getSampleRecordingPolicy().equals(
            ExperimentWizardFieldsDecoder.SampleRecordingBehavior.always_create_sample)) {
            if (!this.isSampleNameSupplied(attrMap)
                || !this.isSampleNameUnique((String) attrMap.get(AbstractSample.PROP_NAME))) {
                this.makeSampleValid(attrMap);
            }
        }
        if (this.decoder.getSampleRecordingPolicy() == ExperimentWizardFieldsDecoder.SampleRecordingBehavior.not_create_if_name_exists) {
            if (!this.isSampleNameSupplied(attrMap)) {
                this.makeSampleValid(attrMap);
            }
        }
    }

    private void makeSampleValid(final Map<String, Object> prop) {
        String name = (String) prop.get(AbstractSample.PROP_NAME);
        name = this.getUniqueName(name);
        prop.put(AbstractSample.PROP_NAME, name);
    }

    private Map<String, InputSample> setInputSamples(final Map<String, RefInputSample> pmap,
        final Map<String, String> defInputSamples) throws AccessException, ConstraintException {

        final Map<String, InputSample> insamples = new HashMap();

        for (final String key : pmap.keySet()) {
            final RefInputSample ris = pmap.get(key);
            assert ris != null : "Reference Input Sample '" + key + "' is not found within the protocol!";

            // Create InputSample based on the ReferenceInputSample properties
            final String refSname = ris.getName();
            final Map attrMap = new HashMap();
            attrMap.put(InputSample.PROP_EXPERIMENT, this.experiment);
            attrMap.put(InputSample.PROP_REFINPUTSAMPLE, ris);
            attrMap.put(InputSample.PROP_NAME, refSname);
            attrMap.put(InputSample.PROP_AMOUNT,
                defInputSamples.get(this.decoder.getInputSampleFieldValue(refSname)));
            attrMap.put(InputSample.PROP_AMOUNTUNIT, defInputSamples.get(refSname));
            attrMap.put(InputSample.PROP_AMOUNTDISPLAYUNIT, defInputSamples.get(refSname));

            this.setInputSampleValue(refSname, InputSample.PROP_AMOUNT, ris.getAmount(), attrMap);
            this.setInputSampleValue(refSname, InputSample.PROP_AMOUNTUNIT, ris.getUnit(), attrMap);
            this.setInputSampleValue(refSname, InputSample.PROP_AMOUNTDISPLAYUNIT, ris.getDisplayUnit(),
                attrMap);

            final InputSample inSample = this.version.create(InputSample.class, attrMap);
            insamples.put(inSample.getName(), inSample);
        }
        return insamples;
    }

    /**
     * If sample name is supplied - assume that this is a new sample, therefore create one. If sample with
     * same name is recorded, then make it configurable a) Throw exception b) Modify the name.
     * 
     * If hook for the sample is supplied - do not allow editing the sample (may be in the future)
     * 
     * @throws ConstraintException
     * @note AccessException This assumes that experiment.InputSample.name = protocol.RefInputSample.Name (!)
     */
    private void setSamplesforInput() throws AccessException, ConstraintException {

        // Set/Create/update new Samples which are undefined in experiment
        final Map<String, String> defInputSamples =
            this.decoder.getPrmsSubset(ExperimentWizardFieldsDecoder.code.I);

        final Map<String, RefInputSample> pmap = this.makeInSampleMap(this.protocol.getRefInputSamples());

        final Map<String, InputSample> insamples = this.setInputSamples(pmap, defInputSamples);
        final Map<String, RefSample> refSamples = this.getRefSamples(pmap);
        final Map<String, Sample> samples = new HashMap<String, Sample>();

        for (final String key : this.decoder.getPrmsSubset(ExperimentWizardFieldsDecoder.code.S).keySet()) {
            final RefInputSample ris = pmap.get(key);
            if (ris == null) {
                // This is OK as this might be an refOutput sample!
                continue;
            }
            final String refSname = ris.getName();
            final String sampleHook = this.decoder.getSampleFieldValue(refSname);
            final RefSample refSample = refSamples.get(refSname);
            //If sample exist
            if (!Util.isEmpty(sampleHook)) {

                final Map<String, String> props = this.decoder.getAllSampleProps(refSname);
                assert props.size() == 0 : "WARNING: Some properties defined but will not be set!"
                    + " If the hook or the name only specified no other properties can be added!";

                final Sample sample = this.getSample(refSname, sampleHook);

                if (sample != null) {
                    insamples.get(refSname).setSample(sample);
                    //sample.setInputSamples(Collections.singletonList(insamples.get(refSname)));
                    if (refSample != null) {
                        sample.setRefSample(refSample);
                    }
                    samples.put(refSname, sample);
                    //"Cannot edit the properties of a sample if given by hook or by name!";
                    continue;
                } else {
                    throw new RuntimeException("Cannot find sample specified by hook or name. Data given:"
                        + sampleHook);
                }
            }
            // If a new sample needs to be created 
            final Map<String, Object> attrMap = Util.getNewMap();

            final Map<String, String> sprop = this.decoder.getAllSampleProps(refSname);
            for (final String propName : sprop.keySet()) {
                this.setSampleValue(refSname, propName, sprop.get(propName), attrMap);
            }

            this.applyPolicy(attrMap);

            attrMap.put(Sample.PROP_INPUTSAMPLES, Util.makeList(insamples.get(refSname)));
            if (refSample != null) {
                attrMap.put(Sample.PROP_REFSAMPLE, refSamples.get(refSname));
            }
            final Sample sample = this.version.create(Sample.class, attrMap);
            samples.put(refSname, sample);
        }
        this.setSampleComponentsforSamples(pmap, samples);

    }

    private Map<String, RefSample> getRefSamples(final Map<String, ? extends ModelObject> pmap)
        throws AccessException, ConstraintException {

        final Map<String, RefSample> refSamples = new HashMap();
        String refSname = null;
        for (final String key : pmap.keySet()) {
            final ModelObject mobj = pmap.get(key);

            assert mobj != null : "Reference Input/Output Sample '" + key
                + "' is not found within the protocol!";
            if (mobj instanceof RefInputSample) {
                refSname = ((RefInputSample) mobj).getName();
            } else {
                refSname = ((RefOutputSample) mobj).getName();
            }

            // Get refsample by name or hook
            final String refSampleHookorName = this.decoder.getRefSampleFieldValue(refSname);
            //If sample exist
            if (!Util.isEmpty(refSampleHookorName)) {

                final Map<String, String> props = this.decoder.getAllRefSampleProps(refSname);
                assert props.size() == 0 : "WARNING: Some properties defined but will not be set!"
                    + " If the hook or the name only specified no other properties can be added!";

                final RefSample rsample = this.getRefSample(refSname, refSampleHookorName);
                if (rsample != null) {
                    refSamples.put(refSname, rsample);
                    //"Cannot edit the properties of a sample if given by hook or by name!";
                    continue;
                } else {
                    throw new RuntimeException("Cannot find RefSample specified by hook or name. Data given:"
                        + refSampleHookorName);
                }
            }

            // Proceed further only if some properties of RefSample are defined
            if (this.decoder.getAllRefSampleProps(refSname).isEmpty()) {
                continue;
            }

            // Create SampleComponent 
            final Map attrMap = new HashMap();

            // Set the values defined 
            for (final String propName : this.decoder.getAllRefSampleProps(refSname).keySet()) {
                this.setRefSampleValue(refSname, propName, null, attrMap);
            }
            this.applyPolicy(attrMap);

            final RefSample rsample = this.version.create(RefSample.class, attrMap);
            refSamples.put(refSname, rsample);
        }
        return refSamples;
    }

    private void setSampleComponentsforSamples(final Map<String, ? extends ModelObject> pmap,
        final Map<String, Sample> samples) throws AccessException, ConstraintException {

        for (final String key : pmap.keySet()) {
            final ModelObject mobj = pmap.get(key);
            String refSname = null;
            if (mobj instanceof RefInputSample) {
                refSname = ((RefInputSample) mobj).getName();
            } else {
                refSname = ((RefOutputSample) mobj).getName();
            }

            assert refSname != null : "Reference Input/Output Sample '" + key
                + "' is not found within the protocol!";

            // Do not record samplecomponent is no properties of is was defined!
            if (this.decoder.getAllSampleComponentProps(refSname).isEmpty()) {
                continue;
            }

            // Create SampleComponent 
            final Map attrMap = new HashMap();

            // Satisfy mandatory requirements
            final Sample sample = samples.get(refSname);
            assert sample != null : "SampleComponent cannot be defined without a sample! Please defined a sample for '"
                + refSname + "' sampleComponent!";

            attrMap.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            final Map<String, Object> prop = Util.getNewMap();
            prop.put(Substance.PROP_NAME, "Dummy MolComponent");
            prop.put(Molecule.PROP_MOLTYPE, "other");
            prop.put(LabBookEntry.PROP_DETAILS, "used by WizardExperimentCreate for SampleComponent recording. "
                + "No meaningful information is recorded here. Needed to sutisfy mandatory relation.");

            final Substance comp = Util.getOrCreate(this.version, Molecule.class, prop);
            assert comp != null : "Cannot record sampleComponent without Substance one must be defined!";

            attrMap.put(SampleComponent.PROP_REFCOMPONENT, comp);
            // Set the values defined 
            for (final String propName : this.decoder.getAllSampleComponentProps(refSname).keySet()) {
                this.setSampleComponentValue(refSname, propName, null, attrMap);
            }
            this.version.create(SampleComponent.class, attrMap);

        }

    }

    boolean isSampleNameSupplied(final Map<String, Object> prop) {
        final String name = (String) prop.get(AbstractSample.PROP_NAME);
        if (Util.isEmpty(name)) {
            return false;
        }
        return true;
    }

    //TODO use isNameUnique() to avoid duplication
    private boolean isSampleNameUnique(final String name) {
        final AbstractSample asample =
            this.version.findFirst(AbstractSample.class, AbstractSample.PROP_NAME, name);
        if (asample != null) {
            return false;
        }
        return true;
    }

    @Deprecated
    // there is a method in the DM
    String getUniqueName(String name) {
        if (Util.isEmpty(name)) {
            name = "name" + "_" + System.currentTimeMillis();
        } else {
            name = name + "_" + System.currentTimeMillis();
        }
        return name;
    }

    /**
     * @param ris
     * @param refSname
     * @param attrMap TODO look how values are extracted !
     */
    private void setSampleValue(final String name, final String propertyName, final Object altValue,
        final Map attrMap) {
        final String sVal = this.decoder.getSampleFieldValue(name, propertyName);
        if (!Util.isEmpty(sVal)) {
            // System.out.println("name: " + name + " pname: " + propertyName + " Val: " + sVal);
        }
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(Sample.class));
    }

    private void setRefSampleValue(final String name, final String propertyName, final Object altValue,
        final Map attrMap) {
        final String sVal = this.decoder.getRefSampleFieldValue(name, propertyName);
        if (!Util.isEmpty(sVal)) {
            // System.out.println("name: " + name + " pname: " + propertyName + " Val: " + sVal);
        }
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(RefSample.class));
    }

    private void setSampleComponentValue(final String name, final String propertyName, final Object altValue,
        final Map attrMap) {
        final String sVal = this.decoder.getSampleComponentFieldValue(name, propertyName);
        if (!Util.isEmpty(sVal)) {
            //  System.out.println("name: " + name + " pname: " + propertyName + " Val: " + sVal);
        }
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(SampleComponent.class));
    }

    /**
     * @param ris
     * @param refSname
     * @param attrMap
     */
    private void setInputSampleValue(final String name, final String propertyName, final Object altValue,
        final Map attrMap) {
        final String sVal = this.decoder.getInputSampleFieldValue(name, propertyName);
        if (!Util.isEmpty(sVal)) {
            // System.out.println("name: " + name + " pname: " + propertyName + " Val: " + sVal);
        }
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(InputSample.class));
    }

    private void setOutputSampleValue(final String name, final String propertyName, final Object altValue,
        final Map attrMap) {
        final String sVal = this.decoder.getOutputSampleFieldValue(name, propertyName);
        if (!Util.isEmpty(sVal)) {
            // System.out.println("name: " + name + " pname: " + propertyName + " Val: " + sVal);
        }
        this.setValue(sVal, propertyName, altValue, attrMap,
            this.experiment.get_Version().getMetaClass(OutputSample.class));
    }

    /**
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    private void setParameters() throws AccessException, ConstraintException {
        // find all the existing ones

        final Collection<Parameter> parms = this.experiment.getParameters();
        // Update the values of all parameters already created for experiment
        for (final Iterator<Parameter> iter = parms.iterator(); iter.hasNext();) {
            final Parameter parm = iter.next();
            final String newVal = this.decoder.getPropertiesMap().get(parm.getName());
            parm.setValue(newVal);
            this.decoder.getPropertiesMap().remove(parm.getName());
        }

        // Create parameter for which there are values defined
        final Map<String, ParameterDefinition> pmap =
            this.makeParamMap(this.protocol.getParameterDefinitions());
        for (final String key : this.decoder.getPrmsSubset(ExperimentWizardFieldsDecoder.code.P).keySet()) {
            final ParameterDefinition pd = pmap.get(key);
            //System.out.println("key " + key);
            //System.out.println("Va " + this.prop.get(key));

            assert pd != null : "Parameter definition " + key + " is not found within the protocol!";
            ExperimentWizardCreator.createParameter(this.experiment, pd,
                this.decoder.getParameterFieldValue(key), this.version);
        }
    }

    public static Parameter createParameter(final Experiment experiment, final ParameterDefinition pd,
        final String paramValue, final WritableVersion rw) throws AccessException, ConstraintException {
        final Map attrMap = new HashMap();
        assert experiment != null && pd != null : "Experiment and/or ParameterDefinition is NULL. Cannot create new Parameter. Check the protocol.";
        attrMap.put(Parameter.PROP_EXPERIMENT, experiment);
        attrMap.put(Parameter.PROP_PARAMETERDEFINITION, pd);
        attrMap.put(Parameter.PROP_NAME, pd.getName());
        attrMap.put(Parameter.PROP_PARAMTYPE, pd.getParamType());
        attrMap.put(Parameter.PROP_VALUE, paramValue);

        return rw.create(Parameter.class, attrMap);
    }

    Map<String, ParameterDefinition> makeParamMap(final Collection<ParameterDefinition> params) {
        final HashMap<String, ParameterDefinition> pmap =
            new HashMap<String, ParameterDefinition>(params.size());
        for (final ParameterDefinition pd : params) {
            pmap.put(pd.getName(), pd);
        }
        return pmap;
    }

    Map<String, RefInputSample> makeInSampleMap(final Collection<RefInputSample> refInSamples) {
        final HashMap<String, RefInputSample> pmap = new HashMap<String, RefInputSample>(refInSamples.size());
        for (final RefInputSample refInSample : refInSamples) {
            pmap.put(refInSample.getName(), refInSample);
        }
        return pmap;
    }

    /*
    HashMap<String, InputSample> makeInSampleMap(final Collection<InputSample> inSamples) {
        final HashMap<String, InputSample> pmap = new HashMap<String, InputSample>(inSamples.size());
        for (final InputSample inSample : inSamples) {
            pmap.put(inSample.getName(), inSample);
        }
        return pmap;
    }
    */
    /*
    HashMap<String, OutputSample> makeOutSampleMap(final Collection<OutputSample> outSamples) {
        final HashMap<String, OutputSample> pmap = new HashMap<String, OutputSample>(outSamples.size());
        for (final OutputSample outSample : outSamples) {
            pmap.put(outSample.getName(), outSample);
        }
        return pmap;
    }
    */
    Map<String, RefOutputSample> makeOutSampleMap(final Collection<RefOutputSample> refOutSamples) {
        final HashMap<String, RefOutputSample> pmap =
            new HashMap<String, RefOutputSample>(refOutSamples.size());
        for (final RefOutputSample refOutSample : refOutSamples) {
            pmap.put(refOutSample.getName(), refOutSample);
        }
        return pmap;
    }

    @Deprecated
    // used only in unsupported code
    public static Calendar getCalendar(final String date) {
        if (Util.isEmpty(date)) {
            return Calendar.getInstance();
        }
        try {
            //System.out.println("Trying to parse the date from: " + date.getClass().getCanonicalName());
            //System.out.println("date: " + date);
            final Date d = new SimpleDateFormat(Utils.date_format).parse(date);
            final Calendar ret = Calendar.getInstance();
            ret.setTimeInMillis(d.getTime());
            return ret;
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Sample getSample(final RefOutputSample refouts, final Set<OutputSample> samples) {
        for (final OutputSample outs : samples) {
            if (outs.getRefOutputSample().getName().equals(refouts.getName())) {
                return outs.getSample();
            }
        }
        return null;
    }

    public static Sample getSample(final RefInputSample refins, final List<InputSample> samples) {
        for (final InputSample ins : samples) {
            if (ins.getRefInputSample().getName().equals(refins.getName())) {
                return ins.getSample();
            }
        }
        return null;
    }
}
