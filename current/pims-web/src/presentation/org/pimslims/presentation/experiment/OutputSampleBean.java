package org.pimslims.presentation.experiment;

import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;

public class OutputSampleBean {

    private String outputSampleHook;

    private String outputSampleName;

    private String refSampleName;

    private String sampleHook;

    private String sampleName;

    private Measurement amount;

    private String unit;

    private String refSampleHook;

    private final String description;

    private final String refSampleDescription;

    private Long refOutputSampleDbId;

    private Integer rowPosition;

    private Integer colPosition;

    private ModelObjectShortBean holder;

    private final boolean mayDelete;

    public OutputSampleBean(final OutputSample outputSample) {
        super();
        this.setOutputSampleHook(outputSample.get_Hook());
        this.setOutputSampleName(outputSample.getName());
        this.description = outputSample.getRole();

        this.mayDelete = outputSample.get_MayDelete();

        // process the sample
        final Sample sample = outputSample.getSample();
        if (null != sample) {
            this.setSampleHook(sample.get_Hook());
            this.setSampleName(sample.getName());
            final Measurement m =
                Measurement.getMeasurement(Sample.PROP_CURRENTAMOUNT, sample, Sample.PROP_AMOUNTUNIT,
                    Sample.PROP_AMOUNTDISPLAYUNIT);
            String amountAsString = null;
            if (null != m) {
                amountAsString = m.toString();
            }
            this.amount = m;

            //final SampleBean sampleBean = new SampleBean(sample);
            this.setColPosition(sample.getColPosition());
            this.setRowPosition(sample.getRowPosition());
            if (null == sample.getHolder()) {
                this.holder = null;
            } else {
                this.holder = new ModelObjectShortBean(sample.getHolder());
            }
        }

        // process the sample category
        final RefOutputSample ros = outputSample.getRefOutputSample();
        SampleCategory category = null;
        this.refOutputSampleDbId = null;
        if (ros != null) {
            this.refOutputSampleDbId = ros.getDbId();
            category = ros.getSampleCategory();
        }
        if (null == category) {
            this.refSampleDescription = "";
        } else {
            this.refSampleDescription = category.getDetails();
            this.setRefSampleHook(category.get_Hook());
            this.setRefSampleName(category.getName());
        }
    }

    /**
     * @return
     */
    public Long getRefOutputSampleDbId() {
        return this.refOutputSampleDbId;
    }

    public String getOutputSampleHook() {
        return this.outputSampleHook;
    }

    public void setOutputSampleHook(final String in_name) {
        this.outputSampleHook = in_name;
    }

    public String getOutputSampleName() {
        return this.outputSampleName;
    }

    public void setOutputSampleName(final String in_name) {
        this.outputSampleName = in_name;
    }

    public String getRefSampleName() {
        return this.refSampleName;
    }

    public void setRefSampleName(final String in_name) {
        this.refSampleName = in_name;
    }

    public String getSampleHook() {
        return this.sampleHook;
    }

    public void setSampleHook(final String in_name) {
        this.sampleHook = in_name;
    }

    public String getSampleName() {
        return this.sampleName;
    }

    public void setSampleName(final String in_name) {
        this.sampleName = in_name;
    }

    public Measurement getAmount() {
        return this.amount;
    }

    @Deprecated
    // not used
    public void setAmount(final String in_name) {
        this.amount = Measurement.getMeasurement(in_name);
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(final String in_name) {
        this.unit = in_name;
    }

    public String getRefSampleHook() {
        return this.refSampleHook;
    }

    public void setRefSampleHook(final String in_name) {
        this.refSampleHook = in_name;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return Returns the refSampleDescription.
     */
    public String getRefSampleDescription() {
        return this.refSampleDescription;
    }

    public Integer getRowPosition() {
        return this.rowPosition;
    }

    public String getRowAlphaPosition() {
        if (null == this.rowPosition) {
            return "";
        }
        return HolderFactory.ROWS[this.rowPosition - 1];
    }

    public void setRowAlphaPosition(final String s) {
        int i = 0;
        for (i = 0; i < HolderFactory.ROWS.length; i++) {
            if (HolderFactory.ROWS[i].equals(s)) {
                break;
            }
        }
        this.rowPosition = i + 1;
    }

    public void setRowPosition(final Integer rowPosition) {
        this.rowPosition = rowPosition;
    }

    public Integer getColPosition() {
        return this.colPosition;
    }

    public void setColPosition(final Integer colPosition) {
        this.colPosition = colPosition;
    }

    public ModelObjectShortBean getHolder() {
        return this.holder;
    }

    public boolean getMayDelete() {
        return this.mayDelete;
    }

}
