package org.pimslims.presentation.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.pimslims.lab.Measurement;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.sample.SampleBean;

// TODO maybe just extend ModelObjectShortBean
public class InputSampleBean extends ModelObjectBean {

    private String inputSampleHook;

    private String inputSampleName;

    private final String inputSampleDescription;

    private String refSampleName;

    private final String refSampleDescription;

    private String sampleHook;

    private String sampleName;

    private final String sampleCategoryName;

    private final Measurement amount;

    /**
     * Samples which can be chosen for this input sample
     */
    private List<SampleBean> possibleSamples = new ArrayList();

    private String refSampleHook;

    private final String refInputSampleHook;

    private final Long refInputSampleDbId;

    private final Experiment experiment;

    private final Sample sample;

    public InputSampleBean(final InputSample inputSample) {
        // a whole super() call is too expensive
        super.init(inputSample);
        super.processAttributes(inputSample);
        this.setInputSampleHook(inputSample.get_Hook());
        this.setInputSampleName(inputSample.getName());

        // process amount
        this.amount =
            Measurement.getMeasurement(InputSample.PROP_AMOUNT, inputSample, InputSample.PROP_AMOUNTUNIT,
                InputSample.PROP_AMOUNTDISPLAYUNIT);

        this.experiment = inputSample.getExperiment();
        this.inputSampleDescription = inputSample.getRole();

        final RefInputSample ris = inputSample.getRefInputSample();
        if (null == ris) {
            this.refInputSampleHook = null;
            this.refInputSampleDbId = null;
            this.setRefSampleHook(null);
            this.refSampleDescription = "";
            this.sampleCategoryName = "";
        } else {
            this.refInputSampleHook = ris.get_Hook();
            this.refInputSampleDbId = ris.getDbId();
            final SampleCategory category = ris.getSampleCategory();
            this.setRefSampleHook(category.get_Hook());
            this.refSampleDescription = category.getDetails();
            this.sampleCategoryName = category.getName();
            this.setRefSampleName(category.getName());
        }

        this.sample = inputSample.getSample();
        if (null != this.sample) {
            this.sampleName = this.sample.getName();
            this.sampleHook = this.sample.get_Hook();
        }
    }

    /**
     * @param inputSample
     * @param samples possible samples to use for this input
     * @param cache cache to use for possible samples calculations
     */
    public InputSampleBean(final InputSample inputSample, final Collection<Sample> samples) {

        this(inputSample);
        this.possibleSamples = new ArrayList(SampleBean.getSampleBeans(samples));

        // process the sample
        final Sample sample = inputSample.getSample();
        if (null == sample) {
            this.setSampleHook(MRUController.NONE);
        } else {
            this.setSampleHook(sample.get_Hook());
            this.setSampleName(sample.getName());
            if (!this.possibleSamples.contains(sample)) {
                this.possibleSamples.add(0, new SampleBean(sample));
            }
        }
    }

    public String getInputSampleHook() {
        return this.inputSampleHook;
    }

    public String getRefInputSampleHook() {
        return this.refInputSampleHook;
    }

    private void setInputSampleHook(final String in_name) {
        this.inputSampleHook = in_name;
    }

    public String getInputSampleName() {
        return this.inputSampleName;
    }

    private void setInputSampleName(final String in_name) {
        this.inputSampleName = in_name;
    }

    public String getRefSampleName() {
        return this.refSampleName;
    }

    private void setRefSampleName(final String in_name) {
        this.refSampleName = in_name;
    }

    public String getSampleHook() {
        return this.sampleHook;
    }

    private void setSampleHook(final String in_name) {
        this.sampleHook = in_name;
    }

    public String getSampleName() {
        return this.sampleName;
    }

    private void setSampleName(final String in_name) {
        this.sampleName = in_name;
    }

    public String getSampleCategoryName() {
        return this.sampleCategoryName;
    }

    /**
     * @return .e.g "50mL"
     */
    public Measurement getAmount() {
        return this.amount;
    }

    public String getRefSampleHook() {
        return this.refSampleHook;
    }

    private void setRefSampleHook(final String in_name) {
        this.refSampleHook = in_name;
    }

    /**
     * @return Returns the samples to show in the drop down list.
     */
    public Collection<SampleBean> getSamples() {
        return this.possibleSamples;
    }

    public TreeMap<String, List<SampleBean>> getSamplesByUser() {

        final TreeMap<String, List<SampleBean>> samplesByUser = new TreeMap<String, List<SampleBean>>();

        final List<String> possiblePersons = new LinkedList<String>();
        for (final SampleBean sample : this.possibleSamples) {
            final String name = sample.getAssignTo();
            if (!possiblePersons.contains(name)) {
                possiblePersons.add(name);
            }
        }

        // add samples for each person
        for (final String name : possiblePersons) {
            final List<SampleBean> samplesByPerson = new LinkedList<SampleBean>();
            for (final SampleBean sample : this.possibleSamples) {
                final String assignee = sample.getAssignTo();
                if (assignee.equals(name)) {
                    samplesByPerson.add(sample);
                }
            }
            assert samplesByPerson.size() >= 1 : "expTypes of " + name + " should not empty";
            samplesByUser.put(name, samplesByPerson);
        }
        /*
         * for (Iterator iter = samplesByUser.entrySet().iterator(); iter.hasNext();) { Map.Entry entry =
         * (Map.Entry)iter.next(); String key = (String)entry.getKey(); List<SampleBean> values = (List<SampleBean>)entry.getValue();
         * for (SampleBean sample:values) { System.out.println("InputSampleBean
         * ["+key+":"+sample.getName()+"]"); } }
         */
        // TODO sort current users sampleBeans to top
        return samplesByUser;
    }

    /**
     * @return Returns the inputSampleDescription.
     */
    public String getInputSampleDescription() {
        return this.inputSampleDescription;
    }

    /**
     * @return Returns the refSampleDescription.
     */
    public String getRefSampleDescription() {
        return this.refSampleDescription;
    }

    /**
     * @return
     */
    public Long getRefInputSampleDbId() {
        return this.refInputSampleDbId;
    }

    /**
     * @return
     */
    public String getExperimentName() {
        return this.experiment.get_Name();
    }

    /**
     * @return
     */
    public ModelObjectBean getExperimentBean() {
        return new ModelObjectBean(this.experiment);
    }

    /**
     * @return
     */
    public ModelObjectBean getSampleBean() {
        if (null == this.sample) {
            return null;
        }
        return new ModelObjectBean(this.sample);
    }

}
