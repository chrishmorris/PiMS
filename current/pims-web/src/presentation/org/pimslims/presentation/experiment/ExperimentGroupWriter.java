/**
 * 
 */
package org.pimslims.presentation.experiment;

import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.spreadsheet.SpreadsheetError;

/**
 * @author cm65
 * 
 */
public class ExperimentGroupWriter {

    /**
     * the current transaction
     */
    private final WritableVersion version;

    /**
     * the experiment group we are using
     */
    private final ExperimentGroup group;

    private final Collection<SpreadsheetError> errors = new ArrayList<SpreadsheetError>();

    /**
     * @param version
     * @param group
     */
    public ExperimentGroupWriter(final WritableVersion version, final ExperimentGroup group) {
        this.version = version;
        this.group = group;
    }

    public void setValuesFromSpreadSheetForCreate(final Reader reader) throws IOException,
        ConstraintException {
        this.setValuesFromSpreadSheet(reader, true);
    }

    public void setValuesFromSpreadSheetForAmend(final Reader reader) throws IOException, ConstraintException {
        this.setValuesFromSpreadSheet(reader, false);
    }

    private void setValuesFromSpreadSheet(final Reader reader, final boolean createMode) throws IOException,
        ConstraintException {

        final CsvParser parser = new CsvParser(reader);
        final ArrayList<String> labels = new ArrayList(Arrays.asList(parser.getLabels()));
        //System.out.println("ExperimentGroupWriter.setValuesFromSpreadSheet [" + labels.size() + "]");

        // process standard column headers
        final String wellName = this.findLabel(labels, "Well");

        String experimentName = this.findLabel(labels, "Experiment");
        if (null == experimentName) {
            // for mammalian expression in OPPF
            experimentName = this.findLabel(labels, "Job Description");
        }

        // for PIMS-3575
        final String startDate = this.findLabel(labels, "Start Date");
        final String endDate = this.findLabel(labels, "Finish Date");
        // note that the values in Start Time and End Time from the mammalian expression robot make no sense
        labels.remove("Start Time");
        labels.remove("Finish Time");

        String plateName = null;
        if (!createMode) {
            plateName = this.findLabel(labels, "PlateId");
        }

        final String groupName = this.findLabel(labels, "GroupId");
        final String targetName = this.findLabel(labels, "Target");
        String constructName = this.findLabel(labels, "Project");
        if (null == constructName) {
            constructName = this.findLabel(labels, "Construct");
        }

        /* TODO there are performance problems with this. For each row there is:
         * - a select to find the experiment
         * - a select to get its parameters
         * - a select to get its inputs
         * See PlateExperimentDAO for a better approach.
         * We need 100% test coverage before doing this transformation.
         * */
        // now iterate through the file
        int numLines = 0;
        while (parser.getLine() != null) {
            numLines++;
            // get the experiment
            Experiment experiment = null;
            String well = null;
            if (null != experimentName) {
                well = this.getExperimentName(parser.getValueByLabel(experimentName));
                experiment = HolderFactory.getExperimentByName(this.group, well);
                if (null == experiment) {
                    experiment =
                        HolderFactory.getExperimentByName(this.group, this.getExperimentName("" + numLines));
                    experiment.setName(parser.getValueByLabel(experimentName));
                }
            }
            if (null == experiment) { // try again
                if (null != plateName && null != wellName) {
                    well = parser.getValueByLabel(wellName);
                    final Holder holder =
                        this.version.findFirst(Holder.class, AbstractHolder.PROP_NAME,
                            parser.getValueByLabel(plateName));
                    if (null == holder) {
                        throw new IllegalArgumentException("No such plate: "
                            + parser.getValueByLabel(plateName));
                    }
                    experiment = ExperimentGroupWriter.getExperimentByPosition(holder, well);
                } else if (null != wellName) {
                    well = parser.getValueByLabel(wellName);
                    experiment = ExperimentGroupWriter.getExperimentByPosition(this.group, well);
                } else {
                    throw new IllegalArgumentException(
                        "CSV file must contain 'Well' or 'Experiment' or 'Job Description' column");
                }
            }

            if (null == experiment) {
                //System.out.println("ExperimentGroupWriter.setValuesFromSpreadSheet no experiment ["
                //    + identity + "]");
                //System.out.println("new CsvError 1 [" + identity + ":" + "" + ":No such experiment]");
                this.errors.add(new SpreadsheetError(well, "", "No such experiment"));
                continue;
            }
            if (null != groupName) {
                final String groupId = parser.getValueByLabel(groupName);
                if (!this.group.getName().equals(groupId)) {
                    // check plate name
                    throw new IllegalArgumentException("Plate not found in this experiment group: " + groupId);
                }
            }

            // process the target and construct
            ResearchObjective project = null;
            //final String thisTargetName =
            String blueprintName = "";
            if (null != constructName) {
                blueprintName = parser.getValueByLabel(constructName);
            } else if (null != targetName) {
                blueprintName = parser.getValueByLabel(targetName);
            }
            if (null == blueprintName) {
                blueprintName = "";
            }
            blueprintName = blueprintName.trim();

            if (blueprintName != null && !"".equals(blueprintName)) {
                final Map<String, Object> attr = new HashMap<String, Object>();
                attr.put(ResearchObjective.PROP_COMMONNAME, blueprintName);
                project = this.version.findFirst(ResearchObjective.class, attr);
                if (null == project) {
                    project = new ResearchObjective(this.version, blueprintName, "From Spreadsheet");
                } else if (ServletUtil.validString(targetName)) {
                    // check the target matches that in the spreadsheet if possible
                    final String thisTargetName = parser.getValueByLabel(targetName);

                    if (ServletUtil.validString(thisTargetName)) {
                        for (final ResearchObjectiveElement bpc : project.getResearchObjectiveElements()) {
                            final String constructTargetName = bpc.getTarget().getName();

                            if (!constructTargetName.equals(thisTargetName)) {
                                throw new ConstraintException("The target in the spreadsheet ("
                                    + thisTargetName + ") does not match the construct target ("
                                    + constructTargetName + ")");
                            }
                        }
                    }
                }

                experiment.setProject(project);
            }

            // set the dates
            if (null != startDate && !"".equals(parser.getValueByLabel(startDate))) {
                final Date date = this.parseDate(parser.getValueByLabel(startDate));
                final Calendar start = Calendar.getInstance();
                start.setTime(date);
                experiment.setStartDate(start);
            }
            if (null != endDate && !"".equals(parser.getValueByLabel(endDate))) {
                final Date date = this.parseDate(parser.getValueByLabel(endDate));
                final Calendar end = Calendar.getInstance();
                end.setTime(date);
                experiment.setEndDate(end);
            }

            // get the parameters
            final Collection<Parameter> parmCol = experiment.getParameters();
            final Map<String, Parameter> parms = new HashMap<String, Parameter>(parmCol.size());
            for (final Iterator iter = parmCol.iterator(); iter.hasNext();) {
                final Parameter parm = (Parameter) iter.next();
                parms.put(parm.getName(), parm);
            }

            // get the input samples
            final Map<String, InputSample> inputSamples =
                new HashMap<String, InputSample>(experiment.getInputSamples().size());
            for (final Iterator iter = experiment.getInputSamples().iterator(); iter.hasNext();) {
                final InputSample is = (InputSample) iter.next();
                inputSamples.put(is.getName(), is);
            }

            // process the parameters and input samples
            for (final ListIterator iter = labels.listIterator(); iter.hasNext();) {
                final String label = (String) iter.next();
                final String value = parser.getValueByLabel(label);
                //System.out.println("ExperimentGroupWriter.setValuesFromSpreadSheet [" + experiment.getName()
                //    + ":" + label + ":" + value + "]");
                if (parms.containsKey(label)) {
                    parms.get(label).setValue(value);

                } else if (inputSamples.containsKey(label)) {
                    final InputSample is = inputSamples.get(label);
                    if (null == value || "".equals(value) || "None".equals(value) || "[None]".equals(value)) {
                        is.setSample(null);
                    } else {
                        // process the sample
                        final Map<String, Object> attr = new HashMap<String, Object>();
                        attr.put(AbstractSample.PROP_NAME, value);
                        final Sample sample = this.version.findFirst(Sample.class, attr);
                        assert null != sample : "No such sample: " + value;
                        is.setSample(sample);

                        if (null != sample) {
                            if (null == experiment.getProject()) {
                                if (null != sample.getOutputSample()) {
                                    final Experiment exp = sample.getOutputSample().getExperiment();
                                    if (null != exp) {
                                        experiment.setProject(exp.getProject());
                                    }
                                }
                            }
                        }
                    }
                    if (iter.hasNext()) {
                        // process the amount
                        final String amountLabel = (String) iter.next();
                        if (amountLabel.startsWith(label + " ")) {
                            final String unit = amountLabel.substring(label.length() + 1);
                            String amount = parser.getValueByLabel(amountLabel);
                            if (null == amount || "".equals(amount)) {
                                amount = "0";
                            }
                            final Measurement m = Measurement.getMeasurement(amount + unit);
                            is.setAmountUnit(m.getStorageUnit());
                            is.setAmountDisplayUnit(m.getDisplayUnit());
                            is.setAmount(m.getFloatValue());
                        } else {
                            iter.previous(); // not an amount column
                        }
                    }
                } else {
                    if (!"".equals(value)) {
                        //System.out.println("new CsvError 2 [" + identity + ":" + label + ":" + value + "]");
                        this.errors.add(new SpreadsheetError(well, label, value));
                    }
                }
            }
        }
        if (numLines < this.group.getExperiments().size()) {
            throw new IllegalArgumentException("Some experiments missing from spreadsheet");
        }
    }

    /**
     * ExperimentGroupWriter.parseDate TODO also accept time
     * 
     * @param valueByLabel
     * @return
     */
    private Date parseDate(final String value) {
        try {
            return DateFormat.getDateInstance().parse(value);
        } catch (final ParseException e) {
            // try again ;
        }
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(value);
        } catch (final ParseException e) {
            throw new RuntimeException("Unknown date: " + value);
        }
    }

    private String findLabel(final ArrayList<String> labels, final String label) {
        String ret = null;
        if (labels.contains(label)) {
            labels.remove(label);
            ret = label;
        } else if (labels.contains(label.toLowerCase())) {
            labels.remove(label.toLowerCase());
            ret = label.toLowerCase();
        }
        return ret;
    }

    /**
     * ExperimentGroupWriter.getExperimentByPosition
     * 
     * @param holder
     * @param well
     * @return
     */
    private static Experiment getExperimentByPosition(final Holder holder, final String well) {
        final Map<String, Object> criteria = new HashMap(2);
        criteria.put(Sample.PROP_COLPOSITION,
            HolderFactory.getColumn(well, (HolderType) holder.getHolderType()));
        criteria.put(Sample.PROP_ROWPOSITION, 1 + HolderFactory.getRow(well));
        final Sample out = holder.get_Version().findFirst(Sample.class, criteria);
        return out.getOutputSample().getExperiment();
    }

    /**
     * @return Returns the errors.
     */
    public Collection<SpreadsheetError> getErrors() {
        return this.errors;
    }

    /**
     * ExperimentGroupWriter.getExperimentName
     * 
     * @param identity full name of experiment, or index in group (1..)
     * @return full name of experiment
     */
    private String getExperimentName(final String identity) {
        try {
            final Integer number = new Integer(identity);
            final NumberFormat nf = new DecimalFormat("00");
            return this.group.get_Name() + ":" + nf.format(number);
        } catch (final NumberFormatException e) {
            return identity;
        }
    }

    /**
     * @param group Experiment group, all in one plate
     * @param position
     * @return the experiment at that position in the group
     * 
     */
    public static Experiment getExperimentByPosition(final ExperimentGroup group, final String position) {
        //TODO convert position A1 =>A01
        final Collection experiments = group.getExperiments();
        assert 0 < experiments.size() : "No experiments for group";
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            final String outputPosition = HolderFactory.getPositionInHolder(experiment);
            // System.out.println(experiment.getName()+" "+outputPosition);
            if (HolderFactory.positionEquals(position, outputPosition)) {
                return experiment;
            }
        }
        return null; // not found
    }
}
