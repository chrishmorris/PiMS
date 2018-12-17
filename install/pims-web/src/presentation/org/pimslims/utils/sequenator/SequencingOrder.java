/**
 * V2_2-pims-web org.pimslims.utils.sequenator SequencingOrder.java
 * 
 * @author pvt43
 * @date 21 Aug 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.util.File;
import org.pimslims.utils.experiment.Utils;
import org.pimslims.utils.sequenator.SOrdersManager.ExpStatus;

/**
 * SequencingOrder
 * 
 */
public class SequencingOrder implements Comparable<SequencingOrder> {

    public static enum OrderProcessingStatus {
        New, LayoutDone, PlateSetupDone, Completed;

        public String getStatus(final Experiment exp) {
            switch (this) {
                case New:
                    return SOrdersManager.ExpStatus.To_be_run.toString();
                case LayoutDone:
                    return SOrdersManager.ExpStatus.In_process.toString();
                case PlateSetupDone:
                    return SOrdersManager.ExpStatus.In_process.toString();
                case Completed:
                    // TODO implement this as a separate policy
                    // Make sure to defined the statuses only for those experiments
                    // for which they was not defined. Do not touch the others as they may be rated 
                    // manually earlier
                    if (exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.OK.toString())
                        || exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.Failed.toString())) {
                        // Ignore experiments which were rated
                        return exp.getStatus();
                    } else {
                        // Rate experiments based on whether any files were uploaded or not
                        if (SequencingOrder.getIsSequencingExpCompleted(exp) == CompletionState.Completed) {
                            return SOrdersManager.ExpStatus.OK.toString();
                        } else {
                            return SOrdersManager.ExpStatus.Failed.toString();
                        }
                    }
                default:
                    throw new RuntimeException("Cannot recognise the status!");
            }
        }
    }

    public final static String reactionControl = "R.Control";

    public final static String instrumentControl = "I.Control";

    public boolean isControl() {
        if (this.getId().equals(SequencingOrder.reactionControl)
            || this.getId().equals(SequencingOrder.instrumentControl)) {
            return true;
        }
        return false;
    }

    /**
     * SequencingOrder.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String order = "OrderID:" + this.orderId + "\n";
        order += "Number of Samples:" + this.getSize() + "\n";
        return order;
    }

    public static boolean isControl(final String orderId) {
        assert !Util.isEmpty(orderId);
        if (orderId.trim().equals(SequencingOrder.reactionControl)
            || orderId.trim().equals(SequencingOrder.instrumentControl)) {
            return true;
        }
        return false;
    }

    public static boolean isInstrumentControl(final String orderId) {
        assert !Util.isEmpty(orderId);
        if (orderId.trim().equals(SequencingOrder.instrumentControl)) {
            return true;
        }
        return false;
    }

    public static boolean isReactionControl(final String orderId) {
        assert !Util.isEmpty(orderId);
        if (orderId.trim().equals(SequencingOrder.reactionControl)) {
            return true;
        }
        return false;
    }

    /**
     * A run can have only nil or one Reaction and nil or one Instrument control This are held in
     * SequencingOrder nonetheless to aid with handling However finding control requires to know an experiment
     * hook Order ID for controls are ambiguous
     * 
     * SequencingOrder.getControlHook
     * 
     * @return TODO look at whether this is necessary
     */
    public Experiment getControl() {
        assert this.isControl();
        assert this.getExperiments().size() == 1;
        if (this.getOneExperiment() != null) {
            return this.getOneExperiment();
        }
        return null;
    }

    /**
     * For jsp pages bean style call
     * 
     * SequencingOrder.getIsControl
     * 
     * @return
     */
    public boolean getIsControl() {
        return this.isControl();
    }

    public static String calculateCompletionStatus(final Experiment exp) {
        // Now see if the content of the file make sense
        // Calculate the percentage of sequence read
        final String readp = SequencingOrder.percentRead(exp);
        if (!Util.isEmpty(readp)) {
            final int percent = Integer.parseInt(readp);
            if (percent > 99) {
                return SOrdersManager.ExpStatus.OK.toString();
            }
        }
        // If cannot calculate percentage than try assessing by the ambiguous letter count 
        final String ambPerc = SequencingOrder.ambiguousLettersPercentage(exp);
        if (!Util.isEmpty(ambPerc)) {
            final int aper = Integer.parseInt(ambPerc);
            if (aper < 30) {
                return SOrdersManager.ExpStatus.OK.toString();
            }
        }

        return SOrdersManager.ExpStatus.Failed.toString();
    }

    public static enum CompletionState {
        NotCompleted, PartlyCompleted, Completed;
    }

    static Comparator<SequencingOrder> traficLightComparator = new Comparator<SequencingOrder>() {
        public int compare(final SequencingOrder so1, final SequencingOrder so2) {
            return so1.getIsCompleted().compareTo(so2.getIsCompleted());
        }
    };

    /* This should match exactly with data/protocols/Leeds_SpecificUI_Sequencing_Order */
    static final String primerName = "Primer Name";

/* This should match exactly with data/protocols/Leeds_SpecificUI_Sequencing_Order */
    public static final String TemplateName = "Template Name";

    /*
     * These must correspond to Protocol name in XML file protocol description
     */
    public static String soProtocolName = "Sequencing order";

    public static String sSetupProtocolName = "Leeds sequencing setup";

    String orderId;

    final List<Experiment> exps;

    public SequencingOrder(final String orderId, final List<Experiment> experiments) {
        assert !Util.isEmpty(orderId) : "OrderId must not be empty";
        /*
        assert orderId.trim().startsWith(Utils.ORDERNAMEPREFIX) : "Order Id must start with "
            + Utils.ORDERNAMEPREFIX + " which follow the number";
            */
        assert experiments != null && experiments.size() > 0 : "Experiments must be defined in the order!";

        this.orderId = orderId;
        this.exps = experiments;
    }

    public SequencingOrder(final String orderId, final ReadableVersion rv) {
        assert !Util.isEmpty(orderId) : "OrderId must not be empty";
        /*
        assert orderId.trim().startsWith(Utils.ORDERNAMEPREFIX) : "Order Id must start with "
            + Utils.ORDERNAMEPREFIX + " which follow the number";
        */
        this.orderId = orderId;
        final List<Experiment> experiments = Utils.getExperimentsFromOrder(orderId, rv);
        assert experiments != null && experiments.size() > 0 : "Experiments must be defined in the order!";
        this.exps = experiments;
    }

    public static boolean isValidSoId(final String soid) {
        if (Util.isEmpty(soid)) {
            return false;
        }
        if (!soid.trim().startsWith(Utils.ORDERNAMEPREFIX)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * Constructor for SequencingOrder from recorded experiments
     * 
     * @param experiments
     */
    public SequencingOrder(final List<Experiment> experiments) {

        assert experiments != null && !experiments.isEmpty() : "Cannot create a Sequencing Order- No experiments provided!";
        final String orderId =
            Utils.getParameterValue(experiments.iterator().next(), SequencingInputDataParser.orderParamName);

        assert !Util.isEmpty(orderId) : "OrderId must not be empty";
        /*
        assert orderId.trim().startsWith(Utils.ORDERNAMEPREFIX) : "Order Id must start with "
            + Utils.ORDERNAMEPREFIX + " which follow the number";
        */
        this.orderId = orderId;
        this.exps = experiments;
    }

    public boolean isNewOrder() {
        if (this.isControl()) {
            return false;
        }
        return this.getProcessingStep() == OrderProcessingStatus.New;
    }

    public boolean isCompletedOrder(final ExperimentGroup eg) {
        if (this.isControl()) {
            return false;
        }
        if (eg == null) {
            return false;
        }
        final PlateNoteManager notem = new PlateNoteManager(eg);
        return notem.isPlateCompletionConfirmed();
        // was return this.getProcessingStep() == OrderProcessingStatus.Completed;
    }

    /**
     * @see SOrdersManager.rateResults()
     */
    void rateResults() throws ConstraintException {

        for (final Experiment exp : this.getExperiments()) {
            SequencingOrder.rateResult(exp);
        }
    }

    public static void rateResult(final Experiment exp) throws ConstraintException {
        final String status = SequencingOrder.calculateCompletionStatus(exp);
        exp.setStatus(status);
    }

    /**
     * @see pims.tld SequencingOrder.percentRead
     * @param exp
     * @return
     */
    public static String percentRead(final Experiment exp) {
        final String seq = SequencingOrder.getSequenceString(exp);
        if (seq == null) {
            // Nothing is read
            return "0";
        }
        final String reqRead = Utils.getParameterValue(exp, "Required Read Length");
        if (Util.isEmpty(reqRead)) {
            // Cannot calculate percentage as required read length is not defined 
            return "";
        }
        final double reqReadNum = Double.parseDouble(reqRead);
        if (reqReadNum < 10 || reqReadNum > 100000) {
            return ""; // the value is incorrect no point to calculate anything
        }
        final String frag = DnaSequenceAnalyser.getWellReadFragment(seq);
        if (frag.length() == 0) {
            return "";
        }
        //System.out.println("F:" + frag);
        //System.out.println("S:" + reqReadNum + ":" + frag.length());
        //System.out.println("SS:" + frag.length() * 100 / reqReadNum);
        return NumberFormat.getIntegerInstance().format((frag.length() * 100 / reqReadNum));
    }

    static String getSequenceString(final Experiment exp) {
        assert exp != null;
        String fastaSequence = "";
        final File file = SequencingOrder.getSequence(exp);
        if (file == null || file.getFile() == null) {
            return null;
        }
        assert file.getFile().length() < 1000000 : "File appears to be too big! (More than 1Mb)";
        try {
            fastaSequence = Util.readFileToString(file.getFile());
        } catch (final IOException e) {
            // Should not happen here
            throw new RuntimeException(e);
        }
        if (!Util.isEmpty(fastaSequence)) {
            return SequencingOrder.getSequenceFromFasta(fastaSequence);
        }
        return null;
    }

    /**
     * @see pims.tld SequencingOrder.getPercentAmbiguousLetters
     * 
     * @param sequence
     * @return
     */
    public static String ambiguousLettersPercentage(final Experiment experiment) {
        assert experiment != null;
        final String sequence = SequencingOrder.getSequenceString(experiment);
        if (Util.isEmpty(sequence)) {
            return "";
        }
        return SequencingOrder.ambiguousLettersPercentage(sequence);
    }

    static String ambiguousLettersPercentage(String sequence) {
        assert !Util.isEmpty(sequence);
        sequence = DnaSequenceAnalyser.cleanSequence(sequence);
        final StringBuffer sb = new StringBuffer(sequence);
        int dnaCounter = 0;
        for (int i = 0; i < sb.length(); i++) {
            final char c = sb.charAt(i);
            if (c == 'A' || c == 'C' || c == 'G' || c == 'T') {
                dnaCounter++;
            }
        }
        String percent = "100";

        if (dnaCounter != 0) {
            percent =
                (NumberFormat.getIntegerInstance().format(new Double((double) (sb.length() - dnaCounter)
                    * 100 / sb.length())));
        }
        return percent;
    }

    public static File getSequence(final Experiment exp) {
        return ServletUtil.getFile(exp, ".seq");
    }

    /**
     * Comparable<SequencingOrder>.compareTo
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final SequencingOrder o) {
        final Long orderNum = Long.valueOf(o.getNumber());
        final Long thisONum = Long.valueOf(this.getNumber());
        return thisONum.compareTo(orderNum);
    }

    // Should be sorted by name
    public List<Experiment> getExperiments() {
        return this.exps;
    }

    // This look at whether this order has any meaningful results 
    // i.e. experiment rated as OK and there are files to export
    public boolean getHasExportableFiles() {
        boolean hasResults = false;
        for (final Experiment exp : this.exps) {
            if (!exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.OK.toString())) {
                continue;
            }
            final Collection<File> files = exp.get_Files();
            if (files.isEmpty()) {
                continue;
            }
            for (final File f : files) {
                if (f.getExtension().equalsIgnoreCase(".ab1") || f.getExtension().equalsIgnoreCase(".seq")
                    || f.getExtension().equalsIgnoreCase(".scf")) {
                    hasResults = true;
                }
            }

        }
        return hasResults;
    }

    private long getNumber() {
        if (this.isControl()) {
            return 9187873; // This is used only for hash calculation
        }
        return Long.valueOf(this.orderId.trim().substring(Utils.ORDERNAMEPREFIX.length()));
    }

    public Experiment getOneExperiment() {
        return this.exps.iterator().next();
    }

    public ExperimentGroup getPlate() {
        return this.getOneExperiment().getExperimentGroup();
    }

    public String getPi() {
        return Utils.getParameterValue(this.getOneExperiment(), "Principal Investigator");
    }

    public String getUser() {
        return Utils.getParameterValue(this.getOneExperiment(), "User");
    }

    public String getUserEmail() {
        return Utils.getParameterValue(this.getOneExperiment(), "User Email");
    }

    public String getSamplesReturn() {
        return Utils.getParameterValue(this.getOneExperiment(), "Return Samples?");
    }

    public String getId() {
        return this.orderId;
    }

    public Integer getIntId() {
        if (this.isControl()) {
            return 0; // for controls
        }
        return Integer.parseInt(this.orderId.trim().substring(Utils.ORDERNAMEPREFIX.length()));
    }

    public Calendar getDate() {
        return this.exps.iterator().next().getStartDate();
    }

    public int getSize() {
        return this.exps.size();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SequencingOrder)) {
            return false;
        }
        final SequencingOrder so = (SequencingOrder) obj;
        if (this.exps != null && so.exps == null) {
            return false;
        }
        if (this.exps == null && so.exps != null) {
            return false;
        }
        if (this.exps.size() != so.exps.size()) {
            return false;
        }
        if (!this.orderId.equals(so.orderId)) {
            return false;
        }
        final ArrayList res = new ArrayList();
        for (final Experiment exp : this.exps) {
            for (final Experiment exp1 : so.exps) {
                if (exp.equals(exp1)) {
                    res.add("1");
                }
            }
        }
        if (res.size() != this.exps.size()) {
            return false;
        }
        return true;
    }

    // Parameters names must correspond to parameters names from
    // the Leeds_SpecificUI_Sequencing_Order protocol!!
    public static String getSampleName(final Experiment exp) {
        return Utils.getWizardParameterValue(exp, SequencingOrder.TemplateName);
    }

    // Parameters names must correspond to parameters names from
    // the Leeds_SpecificUI_Sequencing_Order protocol!!
    public static String getPrimerName(final Experiment exp) {
        return Utils.getWizardParameterValue(exp, SequencingOrder.primerName);
    }

    @Override
    public int hashCode() {
        return (int) (this.getDate().getTimeInMillis() / (this.getSize() * this.getNumber()));
    }

    public TreeMap<String, Integer> getRequiredPrimerVolume(final boolean includingStandardPrimers) {
        final TreeMap<String, Integer> rVolumes = new TreeMap<String, Integer>();
        if (this.isControl()) {
            // Return Nothing for controls 
            return rVolumes;
        }
        SequencingOrder.getCount(rVolumes, this.exps, SequencingOrder.primerName, includingStandardPrimers);

        return SequencingOrder.countToVolume(rVolumes, 4, 10); // 4 uL per a single reaction is required, plus 10uL extra
    }

    public static Comparator<Experiment> nameComporator = new Comparator<Experiment>() {
        public int compare(final Experiment o1, final Experiment o2) {
            // This is a direct number comparison, 
            // as experiment name = reaction number assigned by 
            // calling refnumber sequence
            return Integer.decode(o1.getName()).compareTo(Integer.decode(o2.getName()));
        }
    };

    /**
     * SequencingOrder.getCount
     * 
     * @param rVolumes
     */
    static void getCount(final TreeMap<String, Integer> rVolumes, final List<Experiment> experiments,
        final String key, final boolean includingStandardPrimers) {
        for (final Experiment exp : experiments) {
            String primerName = Utils.getParameterValue(exp, key);
            primerName = primerName.trim();
            if (!includingStandardPrimers) {
                if (StandardPrimerUtil.isStandardPrimer(exp.get_Version(), primerName)) {
                    continue;
                }
            }
            final Integer nMatches = rVolumes.get(primerName);
            if (nMatches == null) {
                rVolumes.put(primerName, 1);
            } else {
                rVolumes.put(primerName, nMatches + 1);
            }
        }
    }

    static TreeMap<String, Integer> countToVolume(final TreeMap<String, Integer> samples,
        final int singleVolume, final int overheadVolume) {
        for (final String key : samples.keySet()) {
            final int totalCount = samples.get(key);
            samples.put(key, totalCount * singleVolume + overheadVolume);
        }
        return samples;
    }

    public TreeMap<String, Integer> getRequiredTemplateVolume() {
        final TreeMap<String, Integer> rVolumes = new TreeMap<String, Integer>();
        if (this.isControl()) {
            // Return Nothing for controls 
            return rVolumes;
        }
        // last parameter has nothing to do with templates and to avoid any potential side effects it is set to true
        SequencingOrder.getCount(rVolumes, this.exps, SequencingOrder.TemplateName, true);

        return SequencingOrder.countToVolume(rVolumes, 10, 10); // 10 uL per a single reaction is required, plus 10uL extra

    }

    /*
     * Method assumes that the status of experiments in the order are the same!!! 
     * This may need to change in the future
     * @returns
     * one of SequencingOrder.OrderProcessingStatus
     */
    public Enum getStatus() {
        final Experiment exp = this.exps.iterator().next();

        final String status = exp.getStatus();
        System.out.println(exp + " " + status);

        if (status.equals(SOrdersManager.ExpStatus.To_be_run.toString())) {
            return SOrdersManager.ExpStatus.To_be_run;
        }
        if (status.equals(SOrdersManager.ExpStatus.In_process.toString())) {
            return SOrdersManager.ExpStatus.In_process;
        }
        if (status.equals(SOrdersManager.ExpStatus.OK.toString())) {
            return SOrdersManager.ExpStatus.OK;
        }
        if (status.equals(SOrdersManager.ExpStatus.Failed.toString())) {
            return SOrdersManager.ExpStatus.Failed;
        }
        throw new AssertionError("Please check that SOrdersManager statuses match Experiment statuses!");
    }

    public Enum getProcessingStep() {
        final Experiment exp = this.exps.iterator().next();
        final String status = exp.getStatus();

        // New
        if (status.equals(SOrdersManager.ExpStatus.To_be_run.toString())) {
            return SequencingOrder.OrderProcessingStatus.New;
        }
        // In_process
        if (status.equals(SOrdersManager.ExpStatus.In_process.toString())) {
            if (this.isLayoutComplete()) {
                return SequencingOrder.OrderProcessingStatus.LayoutDone;
            }
            if (this.isSeqSetupCompleted()) {
                return SequencingOrder.OrderProcessingStatus.PlateSetupDone;
            }
        }
        // Done
        if (status.equals(SOrdersManager.ExpStatus.OK.toString())
            || status.equals(SOrdersManager.ExpStatus.Failed.toString())) {
            return SequencingOrder.OrderProcessingStatus.Completed;
        }
        throw new AssertionError(
            "Please check that SOrdersManager statuses match Experiment statuses! Violating experiment is : "
                + exp);
    }

    /**
     * Evaluate whether the results are loaded or not SequencingOrder.isOrderCompleted
     * 
     * @return
     */
    public CompletionState getIsCompleted() {
        int completed = 0;
        //int partCompleted = 0;
        int notCompleted = 0;
        for (final Experiment exp : this.exps) {
            // If the completion was forced
            if (exp.getStatus().equals(ExpStatus.OK.toString())
                || exp.getStatus().equals(ExpStatus.Failed.toString())) {
                completed++;
                // The status alone suffice do not check whether files are uploaded or not  
                continue;
            }
            switch (SequencingOrder.getIsSequencingExpCompleted(exp)) {
                case Completed:
                    completed++;
                    break;
                case PartlyCompleted:
                    //partCompleted++;
                    break;
                case NotCompleted:
                    notCompleted++;
                    break;
            }
        }
        if (completed == this.exps.size()) {
            return CompletionState.Completed;
        }
        if (notCompleted == this.exps.size()) {
            return CompletionState.NotCompleted;
        }
        return CompletionState.PartlyCompleted;
    }

    public static CompletionState getIsSequencingExpCompleted(final Experiment exp) {
        final Collection<File> files = exp.get_Files();
        if (files == null || files.isEmpty()) {
            return CompletionState.NotCompleted;
        }
        if (files.size() < 3) {
            return CompletionState.PartlyCompleted;
        }
        // More or equal to 3 files 
        return CompletionState.Completed;
    }

    private void setProcessingStep(final SequencingOrder.OrderProcessingStatus step)
        throws ConstraintException {
        for (final Experiment exp : this.exps) {
            exp.setStatus(step.getStatus(exp));
        }
    }

    void revertPlannedRunToNewOrder() throws ConstraintException, AccessException {
        AbstractModelObject holder = null;
        for (final Experiment exp : this.exps) {
            // Remove controls 
            if (this.isControl()) {
                exp.delete();
                continue;
            }
            // Remove experiments from the plate
            final ExperimentGroup eg = exp.getExperimentGroup();
            assert eg != null;
            exp.remove(Experiment.PROP_EXPERIMENTGROUP, eg);
            // Reset the status 
            exp.setStatus(ExpStatus.To_be_run.toString());
            assert exp.getOutputSamples().size() == 1;
            final OutputSample osample = exp.getOutputSamples().iterator().next();
            final Sample sampleForOut = osample.getSample();
            assert sampleForOut != null;
            holder = sampleForOut.getHolder();
            // Remove holder
            if (holder != null) {
                sampleForOut.remove(Sample.PROP_HOLDER, holder);
            }
        }
        if (holder != null) {
            holder.delete();
        }
    }

    public void updateProcessingStep(final SequencingOrder.OrderProcessingStatus step)
        throws ConstraintException {
        switch (step) {
            case LayoutDone:
                if (this.canUpdate(step)) {
                    this.setProcessingStep(step);
                }
                break;
            case New:
                throw new UnsupportedOperationException(
                    "By default all orders have status 'New'. Processed orders cannot be updated to New.");
            case PlateSetupDone:
                if (this.canUpdate(step)) {
                    this.setProcessingStep(step);
                }
                break;
            case Completed:
                if (this.canUpdate(step)) {
                    this.setProcessingStep(step);
                }
                break;
        }
    }

    /**
     * One can only move a sequencing order step by step along the pipeline.
     * 
     * Processing steps cannot be jumped
     * 
     * SequencingOrder.canUpdate
     * 
     * @param step
     * @return
     */
    boolean canUpdate(final SequencingOrder.OrderProcessingStatus step) {
        switch (step) {
            case LayoutDone:
                if (this.getProcessingStep() == SequencingOrder.OrderProcessingStatus.New) {
                    return true;
                }
                throw new AssertionError("LayoutDone processing step can be set only for the new orders!"
                    + " Violating Order " + this.orderId + " Exps: " + this.getExperiments());
                //return false;
            case New:
                throw new UnsupportedOperationException(
                    "By default all orders have status 'New'. Processed orders cannot be updated to New.");
            case PlateSetupDone:
                if (this.getProcessingStep() == SequencingOrder.OrderProcessingStatus.LayoutDone) {
                    return true;
                }
                throw new AssertionError(
                    "PlateSetupDone processing step can only be defined for orders that reached LayoutDone processing step!"
                        + " Violating Order " + this.orderId + " Exps: " + this.getExperiments());
                //return false;
            case Completed:
                // Allow to set Completed twice
                if (this.getProcessingStep() == SequencingOrder.OrderProcessingStatus.PlateSetupDone
                    || this.getProcessingStep() == SequencingOrder.OrderProcessingStatus.Completed) {
                    return true;
                }
                throw new AssertionError(
                    "Completed processing step can only be reached by the orders that reached PlateSetupDone processing step!"
                        + " Violating Order " + this.orderId + " Exps: " + this.getExperiments());
                //return false;
            default:
                throw new RuntimeException("Cannot recognise the processing step!");
        }
    }

    /*
     * Assume for now that all experiments within the order has reached the same stage
     */
    boolean isSeqSetupCompleted() {
        for (final Experiment exp : this.exps) {
            if ((exp.getStatus().equals(ExpStatus.Failed.toString())
                || exp.getStatus().equals(ExpStatus.OK.toString()) || exp.getStatus().equals(
                ExpStatus.In_process.toString()))
                && this.isNextExperimentRecorded()) {
                return true;
            }
        }
        return false;
    }

    /*
    * All experiments have similar status no need to iterate
    */
    boolean isLayoutComplete() {
        for (final Experiment exp : this.exps) {
            if (exp.getStatus().equalsIgnoreCase(ExpStatus.In_process.toString())
                && !this.isNextExperimentRecorded()) {
                return true;
            }
        }
        return false;
    }

    /*
     * Only then all experiments statuses are defined as OK or Failed the order is Complete!
     */
    boolean isComplete() {
        for (final Experiment exp : this.exps) {
            if (exp.getStatus().equalsIgnoreCase(ExpStatus.In_process.toString())
                || exp.getStatus().equalsIgnoreCase(ExpStatus.Unknown.toString())
                || exp.getStatus().equalsIgnoreCase(ExpStatus.To_be_run.toString())) {
                return false;
            }
        }
        return true;
    }

    private boolean isNextExperimentRecorded() {
        final Experiment experiment = this.getOneExperiment();
        final Protocol prot = experiment.getProtocol();
        if (prot == null || !experiment.getProtocol().getName().equals(SequencingOrder.soProtocolName)) {
            throw new AssertionError(
                "isNextExperimentRecorded method can only work with Sequencing order protocol");
        }
        final Set<OutputSample> osamples = experiment.getOutputSamples();
        assert osamples.size() == 1 : "Only one Output is expected!";

        final OutputSample os = osamples.iterator().next();
        final Sample sample = os.getSample();
        // If next experiment recorded, then DNA sample is used as output for 
        // SeqSetup (sequence prep) experiment 
        if (sample != null && !sample.getInputSamples().isEmpty()) {
            return true;
        }

        return false;
    }

    public static String getSequenceFromFasta(String fastaSequence) {
        assert !Util.isEmpty(fastaSequence);
        fastaSequence = fastaSequence.trim();
        //System.out.println("FS" + fastaSequence);
        final int nameStart = fastaSequence.indexOf(">");
        if (nameStart < 0) {
            // Assume this is a pure sequence
            // Get rid of new lines and return 
            return fastaSequence.replaceAll("\n", "");
        }
        fastaSequence = fastaSequence.substring(nameStart);
        final int nameEnd = fastaSequence.indexOf("\n");

        assert nameEnd >= 0 : "Name must be separated by a new line from the sequence in FASTA!";
        // name = fastaSequence.substring(nameStart, nameEnd);
        fastaSequence = fastaSequence.substring(nameEnd);
        //TODO validate sequence!
        return fastaSequence.replaceAll("\n", "");
    }

}
