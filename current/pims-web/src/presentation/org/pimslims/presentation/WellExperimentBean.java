package org.pimslims.presentation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.exception.AccessException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.create.TargetFactory;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.experiment.OutputSampleBean;

/**
 * Represents an experiment in a particular well of a plate. Created by PlateReader.
 * 
 * @author cm65
 * 
 */
public class WellExperimentBean extends ModelObjectBean {

    /**
     * For sorting well experiment beans This will need extending for multi-plate experiments
     * 
     * A01..A12, B01..B12, ... H01..H12
     * 
     * @author cm65
     * 
     */
    public static class ColumnOrder implements Comparator {

        public int compare(final Object arg0, final Object arg1) {

            final WellExperimentBean experiment0 = (WellExperimentBean) arg0;
            final WellExperimentBean experiment1 = (WellExperimentBean) arg1;
            if ("".equals(experiment0.column)) {
                return experiment0.getName().compareTo(experiment1.getName());
            }

            // sort by row within column
            final Integer column0 = experiment0.columnAsInt;
            final Integer column1 = experiment1.columnAsInt;
            final int col = column0.compareTo(column1);
            if (0 != col) {
                return col;
            }

            final Integer row0 = experiment0.rowAsInt;
            final Integer row1 = experiment1.rowAsInt;

            return row0.compareTo(row1);
        }
    }

    /**
     * For sorting well experiment beans This will need extending for multi-plate experiments
     * 
     * A01..H01, A02..H02, ... A12..H12
     * 
     * 
     * @author cm65
     * 
     */
    public static class RowOrder implements Comparator {

        public int compare(final Object arg0, final Object arg1) {

            final WellExperimentBean experiment0 = (WellExperimentBean) arg0;
            final WellExperimentBean experiment1 = (WellExperimentBean) arg1;
            if ("".equals(experiment0.row)) {
                return experiment0.getName().compareTo(experiment1.getName());
            }

            final Integer row0 = experiment0.rowAsInt;
            final Integer row1 = experiment1.rowAsInt;
            final int row = row0.compareTo(row1);
            if (0 != row) {
                return row;
            }

            final Integer column0 = experiment0.columnAsInt;
            final Integer column1 = experiment1.columnAsInt;

            return column0.compareTo(column1);
        }
    }

    private final String plate;

    private final String plateHook;

    private final String group;

    //private final String groupHook;

    final String row;

    final int rowAsInt;

    final String column;

    /**
     * as for column, but with leading zero if <10
     */
    private final String columnPadded;

    final int columnAsInt;

    //private final String status;

    @Deprecated
    private String outputSampleName = null;

    @Deprecated
    private String outputSampleHook = null;

    private final String constructName;

    private final String constructHook;

    /**
     * parameter name => value
     */
    private final Map<String, String> parameters;

    private final Map<String, Long> parameterIds;

    private final Map<String, String> inputSampleNames;

    /**
     * name => hook of input sample
     */
    private final Map<String, String> inputSampleHooks;

    /**
     * name => dbId of InputSample
     */
    // private final Map<String, String> inputSampleDbIds;
    /**
     * name => dbId of actual sample, or null if none set
     */
    //private final Map<String, Long> inputSampleSampleIds;
    /**
     * in microlitres
     */
    private final Map<String, Float> inputSampleVolumes;

    /**
     * Set 0 to indicate that this values has not been set It is relevant only for the plates which has
     * TrialMolComponent associated with experiment through
     * expblueprint->bluePrintComponent->TrialMolcomponents
     */
    private int pcrProductSize = 0;

    private float proteinMW = 0;

    private final Long dbId;

    @Deprecated
    private Long refOutputSampleDbId = 0L;

    private final List<InputSampleBean> inputSampleBeans;

    private final Map<String, OutputSampleBean> outputSampleBeans;

    private final ModelObjectShortBean researchObjective;

    private ModelObjectShortBean target;

    /**
     * Constructor for WellExperimentBean This constructor is easier to use, but less efficient for a plate
     * experiment
     * 
     * @param experiment
     * @param holderName
     * @param holderHook
     * @param row
     * @param column
     * @param samplesForCategory
     * @throws AccessException
     */
    public WellExperimentBean(final Experiment experiment, final String holderName, final String holderHook,
        final String groupName, final String groupHook, final int row, final int column,
        final Map<SampleCategory, Collection<Sample>> samplesForCategory) throws AccessException {
        this(experiment, experiment.getParameters(), experiment.getInputSamples(), experiment
            .getOutputSamples(), holderName, holderHook, groupName, groupHook, row, column,
            samplesForCategory, (ResearchObjective) experiment.getProject());
    }

    /**
     * TODO use the experiment object, instead of all these parms
     * 
     * @param holderName
     * @param holderHook
     * @param experimentHook
     * @param experimentStatus
     * @param row
     * @param column
     * @param samplesForCategory
     * @throws AccessException
     */
    public WellExperimentBean(final Experiment experiment, final Collection<Parameter> parms,
        final Collection<InputSample> iss, final Collection<OutputSample> oss, final String holderName,
        final String holderHook, final String groupName, final String groupHook, final int row,
        final int column, final Map<SampleCategory, Collection<Sample>> samplesForCategory,
        final ResearchObjective ro) throws AccessException {

        // the full constructor of ModelObjectBean is too expensive, so just do this:
        super.init(experiment);
        super.processAttributes(experiment);

        if (null == ro) {
            this.researchObjective = null;
        } else {
            this.researchObjective = new ModelObjectShortBean(ro);
        }

        /* org.apache.log4j.Logger.getLogger("org.hibernate.SQL").debug(
            this.getClass().getName() + " constructor"); */

        this.values = Collections.unmodifiableMap(this.values);
        this.plate = holderName;
        this.plateHook = holderHook;
        this.group = groupName;
        //this.groupHook = groupHook;
        if (-1 == row) {
            this.row = ""; // group is not in a plate
        } else {
            this.row = HolderFactory.ROWS[row];
        }
        this.rowAsInt = row;
        if (-1 == column) {
            this.column = ""; // group is not in a plate
        } else {
            this.column = new Integer(HolderFactory.COLUMNS[column]).toString();
        }
        this.columnAsInt = column;
        if (-1 == column) {
            this.columnPadded = "";
        } else if (Integer.parseInt(this.column) < 10) {
            this.columnPadded = "0" + this.column;
        } else {
            this.columnPadded = this.column;
        }

        if (null == ro) {
            this.constructName = "";
            this.constructHook = null;
        } else {
            this.constructName = ro.get_Name();
            this.constructHook = ro.get_Hook();
            final Target target = TargetFactory.getPimsTarget(ro);
            if (null == target) {
                this.target = null;
            } else {
                this.target = new ModelObjectShortBean(target);
            }
        }

        this.dbId = experiment.getDbId();

        // process the parameters
        this.parameters = new HashMap<String, String>();
        this.parameterIds = new HashMap<String, Long>();

        if (null != parms) {
            for (final Parameter parm : parms) {
                //this.parameterBeans.add(new ParameterBean(parm));
                this.parameterIds.put(parm.getName(), parm.getDbId());
                String value = parm.getValue();
                if ("Boolean".equals(parm.getParamType())) {
                    // coerce value to one expected by JSP
                    if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) {
                        value = "true";
                    } else if ("no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        value = "false";
                    }
                }
                this.parameters.put(parm.getName(), value);
            }
        }

        // process the output sample
        final Collection<OutputSample> outputs = oss;
        // was assert (outputs.size() == 1) : "Experiment has none or too many outputs: " + experiment.getName();
        if (1 == outputs.size()) {
            final OutputSample output = outputs.iterator().next();
            final Sample os = output.getSample();
            if (null == os) {
                this.outputSampleName = "";
                this.outputSampleHook = null;
            } else {
                this.outputSampleName = os.get_Name();
                this.outputSampleHook = os.get_Hook();
            }
            final RefOutputSample ros = output.getRefOutputSample();
            if (null == ros) {
                this.refOutputSampleDbId = null;
            } else {
                this.refOutputSampleDbId = ros.getDbId();
            }
        }
        // process the output samples
        this.outputSampleBeans = new HashMap<String, OutputSampleBean>();
        for (final Iterator i = oss.iterator(); i.hasNext();) {
            final OutputSample outputSample = (OutputSample) i.next();
            final OutputSampleBean outputSampleBean = new OutputSampleBean(outputSample);
            this.outputSampleBeans.put(outputSample.get_Name(), outputSampleBean);
        }

        // process the input samples
        this.inputSampleBeans = new ArrayList<InputSampleBean>();
        if (null != iss) {
            for (final Iterator i = iss.iterator(); i.hasNext();) {
                final InputSample inputSample = (InputSample) i.next();

                Collection<Sample> samples = new ArrayList();
                if (null != inputSample.getRefInputSample()) {
                    final SampleCategory category = inputSample.getRefInputSample().getSampleCategory();
                    if (samplesForCategory.containsKey(category)) {
                        samples = samplesForCategory.get(category);
                    }
                }
                final InputSampleBean inputSampleBean = new InputSampleBean(inputSample, samples);
                this.inputSampleBeans.add(inputSampleBean);
            }
        }

        // and the old way:
        this.inputSampleNames = new HashMap<String, String>();
        this.inputSampleHooks = new HashMap<String, String>();
        this.inputSampleVolumes = new HashMap<String, Float>();
        //this.inputSampleSampleIds = new HashMap<String, Long>();

        if (null != iss) {
            for (final InputSample is : iss) {
                this.inputSampleVolumes.put(is.getName(), new Float(this.inputVolume(is)));
                this.inputSampleHooks.put(is.getName(), is.get_Hook());
                final Sample sample = is.getSample();
                if (null == sample) {
                    continue;
                }
                this.inputSampleNames.put(is.getName(), sample.getName());
                //this.inputSampleSampleIds.put(is.getName(), sample.getDbId());
            }
        }
        /*this.inputSampleDbIds = new HashMap(this.inputSampleHooks);
        for (final Iterator i = this.inputSampleDbIds.keySet().iterator(); i.hasNext();) {
            final String key = (String) i.next();
            final String hook = this.inputSampleDbIds.get(key);
            final String dbId = hook.split(":")[1];
            this.inputSampleDbIds.put(key, dbId);
        }*/
/*
        org.apache.log4j.Logger.getLogger("org.hibernate.SQL").debug(
            this.getClass().getName() + " constructor done"); */
    }

    private float inputVolume(final InputSample is) {

        final Measurement amount =
            Measurement.getMeasurement(InputSample.PROP_AMOUNT, is, InputSample.PROP_AMOUNTUNIT,
                InputSample.PROP_AMOUNTDISPLAYUNIT);

        if (null == amount) {
            return 0f;
        }

        if (null != is.getRefInputSample()) {
            amount.setDisplayUnit(is.getRefInputSample().getDisplayUnit());
        }

        return amount.getDisplayValue();
    }

    /**
     * @return Returns the column.
     */
    public String getColumn() {
        return this.column;
    }

    /**
     * @return Returns the plate.
     */
    public String getPlate() {
        return this.plate;
    }

    /**
     * @return Returns the group name
     */
    public String getGroupName() {
        return this.group;
    }

    public String getEscapedPlate() {
        return StringEscapeUtils.escapeXml(this.plate);
    }

    /**
     * @return Returns the row.
     */
    public String getRow() {
        return this.row;
    }

    /**
     * @return name => value for all parameters
     * @Deprecated use getParameterBeans
     */
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    /**
     * @return Returns the inputSampleHooks.
     */
    @Deprecated
    public Map<String, String> getInputSampleHooks() {
        return this.inputSampleHooks;
    }

    /**
     * @return Returns the inputSampleNames.
     */
    @Deprecated
    public Map<String, String> getInputSampleNames() {
        return this.inputSampleNames;
    }

    /**
     * @return Returns the inputSampleVolumes.
     */
    @Deprecated
    public Map<String, Float> getInputSampleVolumes() {
        return this.inputSampleVolumes;
    }

    public Map<String, Long> getParameterIds() {
        return this.parameterIds;
    }

    public int getColumnAsInt() {
        return this.columnAsInt;
    }

    public int getRowAsInt() {
        return this.rowAsInt;
    }

    public String getStatus() {
        return (String) this.getValues().get(Experiment.PROP_STATUS);
    }

    public String getOutputSampleName() {
        return this.outputSampleName;
    }

    public String getOutputSampleHook() {
        return this.outputSampleHook;
    }

    public String getPlateHook() {
        return this.plateHook;
    }

    public ModelObjectShortBean getConstruct() {
        return this.researchObjective;
    }

    /**
     * @return the constructName
     */
    public String getConstructName() {
        return this.constructName;
    }

    /**
     * @return the constructHook
     */
    public String getConstructHook() {
        return this.constructHook;
    }

    /**
     * @return the constructName
     */
    public String getTargetName() {
        if (null == this.target) {
            return "";
        } else {
            return this.target.getName();
        }
    }

    /**
     * @return the constructHook
     */
    public String getTargetHook() {
        if (null == this.target) {
            return null;
        } else {
            return this.target.getHook();
        }
    }

    /**
     * Set PCRProduct length Please note that it is optional field
     */
    public void setPcrProductSize(final int pcrProductSize) {
        this.pcrProductSize = pcrProductSize;
    }

    /**
     * 
     * @return -1 if there is no TrialMolcomponent of a type "PCR product" found or if any other object before
     *         this in chain has not been located
     */
    public int getPcrProductSize() {
        return this.pcrProductSize;
    }

    /**
     * Set Protein Molecular Weight Please note that it is optional field
     */
    public void setProteinMW(final float proteinMW) {
        this.proteinMW = proteinMW;
    }

    /**
     * 
     * @return -1 if there is no TrialMolcomponent of a type "Protein" found or if any other object before
     *         this in chain has not been located
     */
    public String getProteinMW() {
        final Float f = new Float(this.proteinMW);
        final int i = f.intValue();
        //return i / 1000;
        //return this.proteinMW;
        final DecimalFormat myFormatter = new DecimalFormat("#####");
        final String result = myFormatter.format(i / 1000);
        //if (result.equals("0")) {
        //    result = " ";
        //}
        return result;
    }

    /**
     * @return
     */
    @Override
    public Long getDbId() {
        return this.dbId;
    }

    /**
     * @return
     */
    public Long getRefOutputSampleDbId() {
        return this.refOutputSampleDbId;
    }

    /**
     * @return
     */
    public List<InputSampleBean> getInputSamples() {
        return this.inputSampleBeans;
    }

    /**
     * @return Returns the endDate.
     * 
     *         public String getEndDate() { return ValueFormatter.formatDate((Calendar)
     *         this.getValues().get(Experiment.PROP_ENDDATE)); }
     * 
     *         /**
     * @return Returns the startDate.
     * 
     *         public String getStartDate() { return ValueFormatter.formatDate((Calendar)
     *         this.getValues().get(Experiment.PROP_STARTDATE)); }
     */

    /**
     * @return
     */
    public Map<String, OutputSampleBean> getOutputSamples() {
        return new HashMap(this.outputSampleBeans);
    }

    /**
     * @return Returns the columnPadded.
     */
    public String getColumnPadded() {
        return this.columnPadded;
    }

}
