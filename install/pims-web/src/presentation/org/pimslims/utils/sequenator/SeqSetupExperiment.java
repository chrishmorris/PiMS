/**
 * V2_2-pims-web org.pimslims.utils.sequenator SeqSetupExperiment.java
 * 
 * @author pvt43
 * @date 16 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.servlet.utils.ValueFormatter;
import org.pimslims.utils.experiment.ExperimentWizardCreator;
import org.pimslims.utils.experiment.ExperimentWizardFieldsDecoder;
import org.pimslims.utils.experiment.Utils;

/**
 * SeqSetupExperiment
 * 
 * see also {@link SeqSetupExperimentTester}
 */
public class SeqSetupExperiment {

    String wellNum;

    String templateName;

    String primerName;

    double premixVol;

    double templateVol;

    double primerVol;

    public static final String EXP_NAME_PREFIX = "SS";

    // Below fields are relevant only for this object created from recorded experiment
    double waterVol;

    Experiment exp;

    /**
     * These are sample names which are recorded as a part of ref data. They names are always the same PS
     * Cannot record Water as this name is taken by RefSample
     */
    public static final String swater = "Water_1";

    public static final String spremix = "Premix";

    /**
     * Constructor for SeqSetupExperiment
     */
    public SeqSetupExperiment(final Experiment exp) {
        assert exp.getProtocol().getName().equals(SequencingOrder.sSetupProtocolName);
        final InputSample template = Utils.getInputSample(exp, "Template");
        final InputSample primer = Utils.getInputSample(exp, "Primer");
        final InputSample premix = Utils.getInputSample(exp, "Premix");
        final InputSample water = Utils.getInputSample(exp, "Water");
        assert template != null;
        assert primer != null;
        assert water != null;
        assert premix != null;
        this.premixVol = premix.getAmount();
        this.templateVol = template.getAmount();
        this.primerVol = primer.getAmount();
        // This is recorded assume that use may have changed calculated volume
        this.waterVol = water.getAmount();
        this.wellNum = Utils.convertWellNumber(HolderFactory.getPositionInHolder(exp));
        this.exp = exp;
        this.templateName =
            Utils.getParameterValue(Utils.getSequencingOrderExperiment(exp), SequencingOrder.TemplateName);
        this.primerName =
            Utils.getParameterValue(Utils.getSequencingOrderExperiment(exp), SequencingOrder.primerName);
    }

    /*
    private String trimTimeStamp(final String name) {
        assert !Util.isEmpty(name);
        assert name.contains("_"); // Separator between the name and the timestamp
        System.out.println("Name :" + name.substring(0, name.indexOf("_")));
        return name.substring(0, name.indexOf("_"));
    }
    */

    /**
     * Constructor for SeqSetupExperiment
     */
    public SeqSetupExperiment() {
        // There has to be default constructor as this class used as a bean  
    }

    /**
     * All fields constructor
     * 
     * Constructor for SeqSetupExperiment
     * 
     * @param premixVol
     * @param primerName
     * @param sampleName
     * @param templateVol
     * @param totalVol
     * @param waterVol
     * @param wellNum
     */

    /**
     * SeqSetupExperiment.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String seqexp = "Well " + this.wellNum + "\n";
        seqexp += "Sample Name " + this.templateName + "\n";
        seqexp += "Primer Name " + this.primerName + "\n";
        seqexp += "Premix " + this.premixVol + "\n";
        seqexp += "Template " + this.templateVol + "\n";
        seqexp += "Primer " + this.primerVol + "\n";
        seqexp += "Water " + this.calculateWaterAmount() + "\n";
        return seqexp;
    }

    public String toHTMLString() {
        String seqexp = "Sequencing Setup Experiment Details: " + "<br/>";
        seqexp += "Well " + this.wellNum + "<br/>";
        seqexp += "Sample Name " + this.templateName + "<br/>";
        seqexp += "Primer Name " + this.primerName + "<br/>";
        seqexp += "Premix " + this.premixVol + "<br/>";
        seqexp += "Template " + this.templateVol + "<br/>";
        seqexp += "Primer " + this.primerVol + "<br/>";
        //seqexp += "Water " + this.calculateWaterAmount() + "<br/>";
        return seqexp;
    }

    public boolean isValid() {
        if (this.premixVol >= 0 && this.primerVol >= 0 && this.templateVol >= 0) {
            return true;
        }
        return false;
    }

    // This will be overriden later with "SS" + seqrefnum
    public String getFullSampleName() {
        return this.templateName + "_" + this.primerName;
    }

    public double getTotalVolume() {
        return 20; // Max volume of the well
    }

    // 20mL = total volume of the well
    double calculateWaterAmount() {
        if (this.premixVol > 0 && this.primerVol > 0 && this.templateVol > 0) {
            return 20.0 - this.premixVol - this.primerVol - this.templateVol;
        }
        return 0;
    }

    public static boolean highlight(final SeqSetupExperiment experiment,
        final SeqSetupExperiment nextExperiment) {

        if (!experiment.primerName.equals(nextExperiment.primerName)) {
            return true;
        }
        if (experiment.getTotalVolume() != nextExperiment.getTotalVolume()) {
            return true;
        }

        return false;
    }

    public Object[] toArray() {
        return new Object[] { this.wellNum, this.templateName, this.primerName, this.premixVol,
            this.templateVol, this.primerVol, this.calculateWaterAmount(), this.getTotalVolume() };
    }

    /*
    public String toCSVString() {
        return this.wellNum + "," + this.templateName + "," + this.primerName + "," + this.premixVol + ","
            + this.templateVol + "," + this.primerVol + "," + this.calculateWaterAmount() + ","
            + this.getTotalVolume() + "\n";
    } */

    public Experiment toExperiment(final WritableVersion rw) throws ConstraintException, AccessException {
        final Protocol protocol = Utils.getSeqPlateSetupProtocol(rw);
        assert protocol != null : "Cannot find Sequencing Plate setup protocol!";

        final String prefix = ExperimentWizardFieldsDecoder.getPropertyPrefix("E1", protocol.get_Hook());
        final HashMap<String, String> prop = this.getEncodedPropertyMap(rw, prefix);

        final ExperimentWizardFieldsDecoder fd = new ExperimentWizardFieldsDecoder(prop);
        /*
        fd
            .setSampleRecordingPolicy(ExperimentWizardFieldsDecoder.SampleRecordingBehavior.not_create_if_name_exists);
            */
        final ExperimentWizardCreator wiz = new ExperimentWizardCreator(rw, protocol, fd);

        return wiz.getExperiment();
    }

    public static List<Experiment> record(final List<SeqSetupExperiment> seqSetExps, final WritableVersion rw)
        throws ConstraintException, AccessException {
        final List<Experiment> exps = new ArrayList<Experiment>(seqSetExps.size());
        int count = 0;
        for (final SeqSetupExperiment ssexp : seqSetExps) {
            final Experiment exp = ssexp.toExperiment(rw);
            System.out.println("Experiment " + ++count + " recorded" + "from total " + seqSetExps.size());
            exps.add(exp);
        }
        return exps;
    }

    @Deprecated
    // not supported
    public HashMap<String, String> getEncodedPropertyMap(final ReadableVersion rv, final String prefix) {

        final HashMap<String, String> prop = new HashMap<String, String>();
        // Experiment properties 
        // The name will be overriden later when experiment is mapped to plate 
        prop.put(prefix + Experiment.PROP_NAME, this.getFullSampleName() + "_" + rv.getUsername());
        prop.put(prefix + LabBookEntry.PROP_DETAILS, "Sequencing Setup Experiment");
        // One day after the start day
        prop.put(prefix + Experiment.PROP_STARTDATE,
            org.pimslims.utils.sequenator.SeqSetupExperiment.getCurrentDate());
        prop.put(prefix + Experiment.PROP_ENDDATE,
            org.pimslims.lab.Utils.getDateFormat().format((new Date().getTime() + 1000 * 3600 * 24)));
        prop.put(prefix + Experiment.PROP_STATUS, "OK");

        // Experiment parameters properties (value of corresponding parameter)
        // Experiment InputSamples

        // If primer with the same name were recorded just use this one, if not then record a new one
        // These samples do not hold any specific experiment related info therefore
        // it is safe to link them to many experiments

        // Taking into account that i may not be able to determine that 
        // the sample with the same name has been recorded by the different project 
        // i am forced to always create a new sample.
        // In attempt to avoid recording the excessive number of samples 
        // i will attach a user id to the sample name
        if (!SOrdersManager.isNameUnique(rv, this.primerName + "_" + rv.getUsername(), Sample.class)) {
            prop.put(prefix + "S.Primer", this.primerName + "_" + rv.getUsername());
        } else {
            prop.put(prefix + "S.Primer," + AbstractSample.PROP_NAME,
                this.primerName + "_" + rv.getUsername());
        }

        // Amount must be in L but supplied in mL therefore / by 1000 
        prop.put(prefix + "I.Primer," + InputSample.PROP_AMOUNT,
            new Double(SeqSetupExperiment.normalize(this.primerVol)).toString());
        prop.put(prefix + "S.Premix", SeqSetupExperiment.spremix);
        prop.put(prefix + "I.Premix," + InputSample.PROP_AMOUNT,
            new Double(SeqSetupExperiment.normalize(this.premixVol)).toString());
        prop.put(prefix + "S.Water", SeqSetupExperiment.swater);
        prop.put(prefix + "I.Water," + InputSample.PROP_AMOUNT,
            new Double(SeqSetupExperiment.normalize(this.calculateWaterAmount())).toString());

        // Template sample will be defined later by coping from Sequencing Order
        //prop.put(prefix + "S.Template", SeqSetupExperiment.stemplate);
        prop.put(prefix + "I.Template," + InputSample.PROP_AMOUNT,
            new Double(SeqSetupExperiment.normalize(this.templateVol)).toString());

        // Experiment OutputSamples
        // This is to be set later 
        //prop.put(prefix + "O.DNA," + AbstractSample.PROP_NAME, "DNA" + System.currentTimeMillis());

        // PIMS counts well as A1...A9.. etc, however the sequencing machine software
        // can only understand wells in the form: A01...A09..
        // This method does the conversion. 
        /*
        final int cpos = HolderFactory.getColumn(Utils.convertWellNumber(this.wellNum), null);
        final int rpos = HolderFactory.getRow(Utils.convertWellNumber(this.wellNum), null);

        System.out.println("WELL" + this.wellNum);
        System.out.println("CONV COL:" + cpos + " ROW:" + rpos);

        prop.put(prefix + "S.DNA," + Sample.PROP_COLPOSITION, new Integer(cpos).toString());
        prop.put(prefix + "S.DNA," + Sample.PROP_ROWPOSITION, new Integer(rpos).toString());
        */
        return prop;
    }

    /**
     * Takes amount in uL return amount in L SeqSetupExperiment.normalize
     * 
     * @param amountInuL
     * @return
     */
    static double normalize(final double amountInuL) {
        if (amountInuL != 0) {
            return amountInuL / 1000000;
        }
        return 0.0;
    }

    // Robot cannot aliquot decimal volumes 
    long getNormalizedWaterVol() {
        return Math.round(SeqSetupExperiment.deNormalize(this.waterVol));
    }

    long getNormalizedPrimerVol() {
        return Math.round(SeqSetupExperiment.deNormalize(this.primerVol));
    }

    long getNormalizedPremixVol() {
        return Math.round(SeqSetupExperiment.deNormalize(this.premixVol));
    }

    long getNormalizedTemplateVol() {
        return Math.round(SeqSetupExperiment.deNormalize(this.templateVol));
    }

    /**
     * Current date in the default PIMS format
     * 
     * @return
     */
    @Deprecated
    // used only in unsupported code
    public static String getCurrentDate() {
        return ValueFormatter.formatDate(Calendar.getInstance());
    }

    /**
     * Takes amount in L return amount in uL SeqSetupExperiment.deNormalize
     * 
     * @param amountInL
     * @return
     */
    static double deNormalize(final double amountInL) {
        if (amountInL != 0) {
            return amountInL * 1000000;
        }
        return 0.0;
    }
}
