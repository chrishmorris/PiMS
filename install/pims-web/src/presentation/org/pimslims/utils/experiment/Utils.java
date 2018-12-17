/**
 * current-pims-web org.pimslims.utils.experiment Utils.java
 * 
 * @author pvt43
 * @date 17 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 pvt43 Foundation; either *
 * 
 */
package org.pimslims.utils.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.sample.SampleUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.util.File;
import org.pimslims.utils.sequenator.NoteManager;
import org.pimslims.utils.sequenator.SOrdersManager;
import org.pimslims.utils.sequenator.SeqSetupExperiment;
import org.pimslims.utils.sequenator.SequencingInputDataParser;
import org.pimslims.utils.sequenator.SequencingOrder;

/**
 * Utils
 * 
 */
@Deprecated
// obsolete
public class Utils {

    public static final String ORDERNAMEPREFIX = "SO_";

    public static Collection<Sample> getAntibiotics(final ReadableVersion version) {
        final SampleCategory antibiotic =
            version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Antibiotic");
        final ArrayList<Sample> sl = new ArrayList<Sample>();
        for (final AbstractSample as : antibiotic.getAbstractSamples()) {
            if (as instanceof Sample) {
                sl.add((Sample) as);
            }
        }
        return sl;
    }

    /**
     * Utils.hasValidExpForCopying
     * 
     * @web function
     * @param exps
     * @param hasExpstoCopy
     * @return
     */
    public static boolean hasValidExpForCopying(final Collection<Experiment> exps) {
        boolean hasExpForCopying = false;
        for (final Experiment exp : exps) {
            if (!exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.Failed.toString())) {
                continue;
            }
            final NoteManager nm = new NoteManager(exp);
            if (nm.isMarkedforReprocessing()) {
                hasExpForCopying = true;
            }
        }
        return hasExpForCopying;
    }

    public static boolean hasFailedExperiments(final Collection<Experiment> exps) {
        for (final Experiment exp : exps) {
            if (exp.getStatus().equalsIgnoreCase(SOrdersManager.ExpStatus.Failed.toString())) {
                return true;
            }
        }
        return false;
    }

    public static Integer getSize(final Object obj) {
        if (obj == null) {
            return 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        if (obj instanceof String[]) {
            return ((String[]) obj).length;
        }
        if (obj instanceof String) {
            return ((String) obj).length();
        }
        return 0;
    }

    public static ParameterDefinition getParameterDefinition(final Protocol protocol, final String paramName) {
        for (final ParameterDefinition pd : protocol.getParameterDefinitions()) {
            if (pd.getName().equalsIgnoreCase(paramName)) {
                return pd;
            }
        }
        return null;
    }

    /**
     * This method generates field names for use by UpdateExperiment servlet
     * 
     * Utils.getFieldName
     * 
     * @param experiment
     * @param property
     * @return Input can be for Parameters : P.Template Name (i.e. "P." plus parameter name)
     * 
     *         return
     * 
     *         org.pimslims.model.experiment.Parameter:114069:value
     * 
     *         InputSamples I.Antibiotic or I.Antibiotic:Sample.PROP_DETAILS (i.e. "I." plus inputSample name)
     * 
     *         return
     * 
     *         org.pimslims.model.experiment.InputSample:114076:sample
     *         org.pimslims.model.experiment.InputSample:114076:value
     *         org.pimslims.model.experiment.InputSample:114076:unit
     * 
     *         OutputSamples O.Cell(i.e. "O." plus outputSample name)
     * 
     *         return
     * 
     *         org.pimslims.model.sample.Sample:114071:name org.pimslims.model.sample.Sample:114071:value
     *         org.pimslims.model.sample.Sample:114071:units
     * 
     */
    public static String getFieldName(final Experiment experiment, final String property) {

        String prop = null;
        // Parameter requested
        if (property.startsWith("P.")) {
            prop = property.substring("P.".length());
            final Parameter p = Utils.getParameter(experiment, prop);
            // Should be assert but its purely handled when in tag  
            if (p == null) {
                System.out.println("WARNING: Cannot find Parameter by Name '" + prop + "' in experiment "
                    + experiment);
            }
            return p.get_Hook() + ":" + Parameter.PROP_VALUE;
        }

        // InputSample requested
        if (property.startsWith("I.")) {
            prop = property.substring("I.".length());
            final int sep = prop.indexOf(":");
            String subProp = "";
            if (sep > 0) {
                subProp = prop.substring(sep);
                prop = prop.substring(0, sep - 1);
            }
            for (final InputSample inSample : experiment.getInputSamples()) {
                if (inSample.getName().equalsIgnoreCase(prop)) {
                    return subProp.length() > 0 ? inSample.get_Hook() + ":" + subProp : inSample.get_Hook()
                        + ":" + InputSample.PROP_SAMPLE;
                }
            }
        }

        // OutputSample requested
        if (property.startsWith("O.")) {
            prop = property.substring("O.".length());
            final int sep = prop.indexOf(":");
            String subProp = "";
            if (sep > 0) {
                subProp = prop.substring(sep);
                prop = prop.substring(0, sep - 1);
            }
            for (final OutputSample outSample : experiment.getOutputSamples()) {
                if (outSample.getName().equalsIgnoreCase(prop)) {
                    return subProp.length() > 0 ? outSample.get_Hook() + ":" + subProp : outSample.get_Hook()
                        + ":" + OutputSample.PROP_SAMPLE;
                }
            }

        }

        return "";
    }

    public static Parameter getParameter(final Experiment experiment, final String pname) {

        for (final Parameter p : experiment.getParameters()) {
            final String paramName = p.getName();
            if (!Util.isEmpty(paramName) && paramName.trim().equalsIgnoreCase(pname.trim())) {
                return p;
            }
        }

        return null;
    }

    public static String getWizardParameterValue(final Experiment experiment, final String pname) {

        // Parameter requested
        assert (!Util.isEmpty(pname) && pname.startsWith("P.")) : "WARNING Parameter property should not be NULL and should start with 'P.' ! Current value is: "
            + pname;

        if (experiment == null) {
            System.out.println("WARNING: Experiment is NULL! ");
            return "";
        }

        final String prop = pname.substring("P.".length());
        final Parameter p = Utils.getParameter(experiment, prop);
        if (p == null) {
            System.out.println("getWizardParameterValue():WARNING: Cannot find Parameter by Name '" + prop
                + "' in experiment " + experiment);
        }
        return p != null ? p.getValue() : "";

    }

    public static String getParameterValue(final Experiment experiment, final String pname) {

        // Parameter requested
        assert (!Util.isEmpty(pname)) : "WARNING Parameter property should not be NULL! Current value is: "
            + pname;

        if (experiment == null) {
            System.out.println("WARNING: Experiment is NULL! ");
            return "";
        }

        final Parameter p = Utils.getParameter(experiment, pname);
        if (p == null) {
            System.out.println("getParameterValue():WARNING: Cannot find Parameter by Name '" + pname
                + "' in experiment " + experiment);
        }
        return p != null ? p.getValue() : "";

    }

    /*
     * key: ${exp._Hook}:Experiment.PROP_DETAILS
     * This should not be used with any complex values! 
     * This need TLD definition
     */
    public static String getObjectSimpleProperty(final String key, final ReadableVersion rv) {
        final int sep = key.lastIndexOf(":");
        if (sep < 0) {
            return "";
        }
        final String hook = key.substring(0, sep);
        if (!Util.isHookValid(hook)) {
            return "";
        }
        final ModelObject mo = rv.get(hook);
        if (mo == null) {
            return "";
        }
        final Object value = mo.get_Values().get(key.substring(sep + 1));

        return value.toString();
    }

    public static String getObjectSimpleProperty(final String prop, final ModelObject mObj) {
        // This should be an assert statement, but asserts are not handled nicely 
        // if they are in the custom EL function call

        if (mObj == null) {
            return "";
        }
        if (Util.isEmpty(prop)) {
            System.out.println("WARNING: No property must be specified for Value function!");
        }

        boolean propExist = false;
        for (final String key : mObj.get_Values().keySet()) {
            if (key.equals(prop)) {
                propExist = true;
                break;
            }
        }
        if (!propExist) {
            System.out.println("Requested property is not defined in the passed object! Property:" + prop
                + " Object:" + mObj);
        }
        return mObj.get_Value(prop).toString();
    }

    public static long getNextNumber(final ReadableVersion rv, final String dbSequenceName) {
        final ReadableVersionImpl rvim = (ReadableVersionImpl) rv;
        return rvim.getNextVal(dbSequenceName);
    }

    private static List<Experiment> getAllExperimentsFromOrder(final String orderId, final ReadableVersion rv) {
        final Map<String, Object> searchprms = Util.getNewMap();
        searchprms.put(Parameter.PROP_NAME, SequencingInputDataParser.orderParamName);
        searchprms.put(Parameter.PROP_VALUE, orderId);
        final Collection<Parameter> prms = rv.findAll(Parameter.class, searchprms);
        final ArrayList<Experiment> exps = new ArrayList<Experiment>();
        for (final Parameter p : prms) {
            final Experiment exp = p.getExperiment();
            if (exp != null) {
                exps.add(exp);
            }
        }
        return exps;
    }

    public static List<Experiment> getExperimentsFromOrder(final String orderId, final ReadableVersion rv) {
        final List<Experiment> exps = Utils.getAllExperimentsFromOrder(orderId, rv);
        final ArrayList<Experiment> filteredExp = new ArrayList<Experiment>();
        for (final Experiment exp : exps) {
            // Do not return reference controls which status is unknown
            if (!exp.getStatus().equals(SOrdersManager.ExpStatus.Unknown.toString())) {
                filteredExp.add(exp);
            }
        }
        return filteredExp;
    }

    public static List<Experiment> getRefControlExperimentsFromOrder(final String orderId,
        final ReadableVersion rv) {
        final List<Experiment> exps = Utils.getAllExperimentsFromOrder(orderId, rv);
        final ArrayList<Experiment> filteredExp = new ArrayList<Experiment>();
        for (final Experiment exp : exps) {
            // Return reference controls only
            if (exp.getStatus().equals(SOrdersManager.ExpStatus.Unknown.toString())) {
                filteredExp.add(exp);
            }
        }
        return filteredExp;
    }

    public static void addExperimentToOrder(final String orderId, final Experiment experiment)
        throws AccessException, ConstraintException {
        final WritableVersion rw = (WritableVersion) experiment.get_Version();

        final Protocol protocol = experiment.getProtocol();
        assert protocol != null : "No protocol in experiment. Cannot add experiment into the sequencing order.";
        ParameterDefinition orderPd = null;
        for (final ParameterDefinition pd : protocol.getParameterDefinitions()) {
            if (pd.getName().equalsIgnoreCase(SequencingInputDataParser.orderParamName)) {
                orderPd = pd;
                break;
            }
        }
        if (orderPd == null) {
            throw new AssertionError("Protocol " + protocol
                + " does not contain 'Order ID' parameter. Cannot proceed.");
        }
        ExperimentWizardCreator.createParameter(experiment, orderPd, orderId, rw);
    }

    public static Experiment getReactionControl(final ExperimentGroup expGroup) {
        assert expGroup != null;
        final Set<Experiment> exps = expGroup.getExperiments();
        assert exps.size() > 0;
        assert exps.iterator().next().getProtocol().getName()
            .equalsIgnoreCase(SequencingOrder.soProtocolName);

        for (final Experiment exp : exps) {
            final String orderId = Utils.getParameterValue(exp, "Order ID");
            if (SequencingOrder.isReactionControl(orderId)) {
                return exp;
            }
        }
        // no reaction controls found
        return null;
    }

    public static String getOrderId(final ReadableVersion rv) {
        final long next = Utils.getNextNumber(rv, "sequencingOrder");
        return Utils.ORDERNAMEPREFIX + next;
    }

    public static long getSeqRefNumber(final ReadableVersion rv) {
        final long next = Utils.getNextNumber(rv, "seqrefnum");
        return next;
    }

    public static boolean canModifyOrder(final List<Experiment> exps) {
        if (exps == null || exps.isEmpty()) {
            return true; // possibly the new order
        }

        final boolean canModify = true;
        for (final Experiment exp : exps) {
            if (!exp.get_MayUpdate()) {
                return false; // assume that experiment in one order have the same user credentials
            }
            // If at lease one sample from order has been already processed 
            // it should not be modifiable.
            final SequencingOrder so = new SequencingOrder(exps);

            return so.isNewOrder();
        }
        return canModify;
    }

    public static ModelObject getFirstRecord(final List<ModelObject> objects) {
        if (!objects.isEmpty()) {
            return objects.iterator().next();
        }
        return null;
    }

    public static String getFastaName(String fastaSequence) {
        assert !Util.isEmpty(fastaSequence);
        final int nameStart = fastaSequence.indexOf(">");
        assert nameStart >= 0 : "Sequence does not seem to be in FASTA!";
        fastaSequence = fastaSequence.substring(nameStart);
        final int nameEnd = fastaSequence.indexOf("\n");
        assert nameEnd > 0 : "Cannot determine the end of the sequence name!";
        return fastaSequence.substring(1, nameEnd);
    }

    public static Protocol getSOProtocol(final ReadableVersion rv) {
        return rv.findFirst(Protocol.class, Protocol.PROP_NAME, SequencingOrder.soProtocolName);
    }

    public static Protocol getSeqPlateSetupProtocol(final ReadableVersion rv) {
        return rv.findFirst(Protocol.class, Protocol.PROP_NAME, SequencingOrder.sSetupProtocolName);
    }

    public static void reobtainCollection(final List<Experiment> experiments, final ReadableVersion rv) {
        assert experiments != null && !experiments.isEmpty();
        for (int i = 0; i < new ArrayList(experiments).size(); i++) {
            final Experiment staleExp = experiments.get(i);
            if (staleExp == null) {
                continue;
            }
            final Experiment revivedExp = rv.get(staleExp.get_Hook());
            experiments.set(i, revivedExp);
        }
    }

/*
    @Deprecated
    // obsolete, used only in unsupported code
    public static void linkExperiments(final ExperimentGroup eg, final List<Experiment> exps,
        final Holder holderForSSetupExps) throws ConstraintException {
        //Assume that the order of experiments in both collections is the same e.g. col1(0)= well A01 == col2(0)
        // The comparator arranges experiments in by their names. Their names has been generated using seqrefnum 
        // sequence and are in order of the rows in a spreadsheet

        final ArrayList<Experiment> expsWithOutput = new ArrayList<Experiment>(eg.getExperiments());
        Collections.sort(expsWithOutput, SequencingOrder.nameComporator);

        int counter = 0;
        assert expsWithOutput.size() == exps.size() : "The number of sequencing order experiments ("
            + expsWithOutput.size() + ") does not match the number if sequencing prep experiments ("
            + exps.size() + ")!";

        for (final Experiment outExp : expsWithOutput) {

            final Sample sample = Utils.getSampleFromOutput(outExp);
            assert sample != null : "No output sample found!";

            final Experiment sseExp = exps.get(counter);
            // Set the name of sequencing setup experiment the same as refnumber of sequencing order with prefix
            sseExp.setName(SeqSetupExperiment.EXP_NAME_PREFIX + outExp.getName());
            final Collection<InputSample> sseinSamples = sseExp.getInputSamples();
            for (final InputSample iSample : sseinSamples) {
                if (iSample.getName().equals("Template")) {
                    iSample.setSample(sample);
                    break;
                }
            }
            final Set<OutputSample> sseoutSamples = sseExp.getOutputSamples();

            assert sseoutSamples.size() == 1 : "Only one output sample is expected in Sequencing Setup Experiment but found "
                + sseoutSamples.size() + " Experiment: " + sseExp;
            final OutputSample oSample = sseoutSamples.iterator().next();
            // set Sample for Output for Sequencing Setup Experiment to position them the same way as Sequencing Order exps
            // Have to copy a sample as only one sample can be OutputSample. 
            final Sample sout = SampleUtility.copySample(sample);
            // Make sure to put these samples in the sample holder to be able to view them in a plate
            sout.setHolder(holderForSSetupExps);
            oSample.setSample(sout);
            counter++;
        }
    }
*/
    /*
    * PIMS well A1 but spreadsheet well must be A01 otherwise the sequencing robot will not recognise it
    */
    public static String convertWellNumber(final String wellNum) {
        assert wellNum != null : "Well number is not defined (NULL)!";
        // Determine direction e.g. A1 to A01 or the other way around 
        if (wellNum.trim().length() == 3) {
            final String midChar = wellNum.substring(1, 2);
            // return 10,11,12 columns without modification
            if (midChar.equals("0")) {
                return wellNum.substring(0, 1) + wellNum.substring(2);
            } else {
                // No need to convert e.g. columns 10,11,12 
                return wellNum;
            }
        }
        // Current format is PIMS return robotics format  
        if (wellNum.trim().length() == 2) {
            return new StringBuffer(wellNum).insert(1, "0").toString();
        }
        throw new AssertionError("Cannot recognise well number format! Given well name is:" + wellNum);
    }

    static String addedOrderStyle = "";

    /**
     * @see PlatePlanner.getOrdersStyles
     * @web function
     */
    public static String getStyle(final Experiment exp, final HashMap<String, String> orderStyles) {

        if (exp == null) {
            // valid case some wells in a plate may not have any experiments
            return "empty";
        }
        final String orderId = Utils.getOrderId(exp);
        if (SequencingOrder.isReactionControl(orderId)) {
            return "reaction_control";
        }
        if (SequencingOrder.isInstrumentControl(orderId)) {
            return "instrument_control";
        }
        return orderStyles.get(orderId);
    }

    public static String getOrderId(final Experiment exp) {
        assert exp != null;
        assert exp.getProtocol().getName().equalsIgnoreCase(SequencingOrder.soProtocolName);
        return Utils.getParameterValue(exp, "Order ID");
    }

    public static Sample getSampleFromOutput(final Experiment experiment) {
        final Set<OutputSample> osamples = experiment.getOutputSamples();
        assert osamples.size() == 1 : "Only one sample expected! Cannot proceed. Experiment given is:"
            + experiment;
        return osamples.iterator().next().getSample();
    }

    public static Sample getSampleFromInput(final Experiment experiment, final String name) {
        final InputSample is = Utils.getInputSample(experiment, name);
        if (is != null) {
            return is.getSample();
        }
        return null;
    }

    public static InputSample getInputSample(final Experiment experiment, final String name) {
        final Collection<InputSample> isamples = experiment.getInputSamples();
        assert isamples.size() > 0;
        for (final InputSample isample : isamples) {
            if (isample.getName().equalsIgnoreCase(name)) {
                return isample;
            }
        }
        return null;
    }

    public static ExperimentGroup getSeqPrepPlate(final ExperimentGroup sOrderPlate) {
        assert sOrderPlate != null;
        final Set<Experiment> exps = sOrderPlate.getExperiments();
        final Experiment exp = exps.iterator().next();
        assert exp.getProtocol() == Utils.getSOProtocol(sOrderPlate.get_Version()) : "Sequencing order experiment is expected but get "
            + exp.getProtocol();

        final Experiment sseExp = Utils.getSeqPrepExperiment(exp);
        if (sseExp != null) {
            return sseExp.getExperimentGroup();
        }

        return null;
    }

    public static Experiment getSeqPrepExperiment(final Experiment sOrderExperiment) {
        assert sOrderExperiment != null;
        assert sOrderExperiment.getProtocol().getName().equalsIgnoreCase(SequencingOrder.soProtocolName);

        final Sample linkingSample = Utils.getSampleFromOutput(sOrderExperiment);
        /* this only happens in weird multigroups configurations where the plate is build 
        // from several group experiments 
        if (linkingSample == null) {
            return null;
        }
        */
        final Set<InputSample> isl = linkingSample.getInputSamples();
        if (isl.isEmpty()) {
            // No Sequencing Prep Experiment has been recorded yet
            return null;
        }
        for (final InputSample is : isl) {
            if (is.getName().equalsIgnoreCase("Template")) {
                final Experiment sseExp = is.getExperiment();
                if (sseExp != null) {
                    return sseExp;
                }
            }
        }
        return null;
    }

    public static boolean isInstrumentControl(final Experiment sOrderExperiment) {
        assert sOrderExperiment != null;
        final String orderId = Utils.getParameterValue(sOrderExperiment, "Order ID");
        assert !Util.isEmpty(orderId);
        return SequencingOrder.isInstrumentControl(orderId);
    }

    public static boolean isReactionControl(final Experiment sOrderExperiment) {
        assert sOrderExperiment != null;
        final String orderId = Utils.getParameterValue(sOrderExperiment, "Order ID");
        assert !Util.isEmpty(orderId);
        return SequencingOrder.isReactionControl(orderId);
    }

    public static boolean isControl(final Experiment sOrderExperiment) {
        assert sOrderExperiment != null;
        final String orderId = Utils.getParameterValue(sOrderExperiment, "Order ID");
        assert !Util.isEmpty(orderId);
        return SequencingOrder.isControl(orderId);
    }

    /**
     * 
     * Utils.getOtherFiles
     * 
     * @web function
     * @param exp
     * @return
     */
    public static Collection<File> getOtherFiles(final Experiment exp) {
        final Collection<File> files = new ArrayList<File>();
        for (final File file : exp.get_Files()) {
            if (file.getTitle().toLowerCase().endsWith(".seq")
                || file.getTitle().toLowerCase().endsWith(".ab1")
                || file.getTitle().toLowerCase().endsWith(".scf")) {
                // do not return 
            } else {
                files.add(file);
            }
        }
        return files;
    }

    public static Experiment getSequencingOrderExperiment(final Experiment seqSetupExperiment) {
        assert seqSetupExperiment != null;
        assert seqSetupExperiment.getProtocol().getName()
            .equalsIgnoreCase(SequencingOrder.sSetupProtocolName);

        final Sample linkingSample = Utils.getSampleFromInput(seqSetupExperiment, "Template");
        final OutputSample outsample = linkingSample.getOutputSample();
        assert outsample != null;
        return outsample.getExperiment();
    }

    public static ExperimentGroup getSequencingOrderPlate(final ExperimentGroup seqSetupPlate) {
        assert seqSetupPlate != null;
        final Set<Experiment> exps = seqSetupPlate.getExperiments();
        final Experiment exp = exps.iterator().next();
        assert exp.getProtocol() == Utils.getSeqPlateSetupProtocol(seqSetupPlate.get_Version()) : "Sequencing setup experiment is expected but get "
            + exp.getProtocol();

        final Experiment sorderExp = Utils.getSequencingOrderExperiment(exp);

        if (sorderExp != null) {
            return sorderExp.getExperimentGroup();
        }
        return null;
    }
}
