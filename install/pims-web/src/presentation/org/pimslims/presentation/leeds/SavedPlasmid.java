/**
 * 
 */
package org.pimslims.presentation.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Creator;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * @author Petr Troshin
 * 
 *         Represents an Entry Clone or an Expression Construct, recorded in the PiMS database.
 * 
 */

@Deprecated
// Leeds code is no longer supported
public class SavedPlasmid extends AbstractSavedPlasmid implements FormFieldsNames {

    /**
     * Plasmid.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getName() + " Made on: " + this.getDate() + " by " + this.getDesignedBy();
    }

    public SavedPlasmid(final Experiment experiment) {
        super(experiment);
    }

    /*
     * Returns "Entry clone"
     */
    public String getExperimentTypeName() {
        return this.experiment.getExperimentType().getName();
    }

    /**
     * Returns vector, or expresssion construct
     * 
     * @return the name of the vector
     */
    public String getDerived() {
        Sample sample = this.getInputSample(FormFieldsNames.VECTOR);
        if (null != sample) {
            return sample.getName();
        }
        sample = this.getInputSample(FormFieldsNames.EXPRESSION_CONSTRUCT);
        if (null != sample) {
            return sample.getName();
        }
        return "";
    }

    /*
     * Would be like this is Holder name is changeable Sample sample = getOutSample(1); Holder sample1Holder =
     * sample.getHolder(); sample1Holder.setName(box1);
     */

    /**
     * TODO check uses - sometimes Marker is Entry Clone Plasmid.getMarker
     * 
     * @return
     */
    public String getAntibioticResistance() {
        if (null != this.getParameter(FormFieldsNames.ANTIBIOTIC_RESISTANCE)) {
            final String ret = this.getParameter(FormFieldsNames.ANTIBIOTIC_RESISTANCE).getValue();
            if (null != ret) {
                return ret;
            }
        }
        // legacy data
        return this.getParameter("Marker").getValue();
    }

    public String getReversePrimer() {
        final Sample sample = this.getReversePrimerSample();
        if (null == sample) {
            return null;
        }
        return sample.get_Hook();
    }

    public String getRsite() {
        final Parameter par = this.getParameter(FormFieldsNames.rsite);
        if (par != null) {
            return par.getValue();
        }
        return "";
    }

    // To be used on the JSPs
    public boolean getForwardPrimerRecorded() {
        return this.isPrimerRecorded(this.getForwardPrimer());
    }

    public boolean getReversePrimerRecorded() {
        return this.isPrimerRecorded(this.getReversePrimer());
    }

    private boolean isPrimerRecorded(final String primerHook) {
        if (null == primerHook) {
            return false;
        }
        final Sample sample = this.experiment.get_Version().get(primerHook);
        final Collection<SampleComponent> scomp = sample.getSampleComponents();
        if (scomp == null || scomp.isEmpty()) {
            return false;
        }
        return true;
    }

    /*
     * Cannot change: Box Primers Derived from
     */
    public void updateSavedPlasmid(final LeedsConstructBean leedsPlasmid) throws ConstraintException,
        AccessException, ClassNotFoundException {
        // Update locations and other common info first
        super.updatePlasmid(leedsPlasmid);

        final String csName = this.getCloneSaverName();
        final String csPosition = this.getCloneSaverPosition();

        if ((leedsPlasmid.csName != null && leedsPlasmid.csName.trim().length() > 0)
            && (csName == null || !csName.equals(leedsPlasmid.csName))) {
            final Sample sample = this.getOutSample(3);
            // There was no clonesaver previously recorded
            if (sample == null) {
                final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
                final SampleCategory plasmid =
                    rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                        FormFieldsNames.RECOMBINANT_PLASMID);
                // Was Creator.recordRefSample(rw, leedsPlasmid.csName);
                LeedsConstructBean.recordCloneSaver(rw, leedsPlasmid.csName, leedsPlasmid.csPosition,
                    plasmid, this.experiment);

            } else {
                this.setCloneSaverName(leedsPlasmid.csName);
            }
        }

        if ((csPosition == null || !csPosition.equals(leedsPlasmid.csPosition))) {
            this.setCloneSaverPosition(leedsPlasmid.csPosition);
        }
        if (!this.getAntibioticResistance().equals(leedsPlasmid.getAntibioticResistance())) {
            this.setAntibioticResistance(leedsPlasmid.getAntibioticResistance());
        }
        if (!this.getRsite().equals(leedsPlasmid.rsite)) {
            this.setRsite(leedsPlasmid.rsite);
        }
        if (this.getTag() == null || !this.getTag().equals(leedsPlasmid.getTag())) {
            this.setTag(leedsPlasmid.tag);
        }
    }

    /*
     * @Override public void setForwardPrimer(String fprimer) throws ConstraintException, AccessException {
     * setLocation(1, location1); } @Override public void setReversePrimer(String location2) throws
     * ConstraintException, AccessException { setLocation(2, location2); } @Override public void
     * setDerivedFrom(String cloneSaverName) throws ConstraintException, AccessException { setLocation(3,
     * cloneSaverName); }
     */

    public void setCloneSaverName(final String cloneSaverName) throws ConstraintException, AccessException,
        ClassNotFoundException {
        this.setLocation(3, cloneSaverName);
    }

    public void setCloneSaverPosition(final String cloneSaverPosition) throws ConstraintException {
        this.setPosition(3, cloneSaverPosition);
    }

    /**
     * @param derived the vector name to set
     * @throws RuntimeException;
     */
    public void setDerived(final String derived) {
        throw new RuntimeException(
            "Name of InputSample is unchangeable. The derived from is the name of InputSample");
    }

    /**
     * @param marker the marker to set
     * @throws ConstraintException
     */

    void setAntibioticResistance(final String antibioticResistance) throws ConstraintException {
        final Set<Parameter> params = this.experiment.getParameters();
        for (final Parameter param : params) {
            if (param.getName().equals(FormFieldsNames.ANTIBIOTIC_RESISTANCE)) {
                param.setValue(antibioticResistance);
            }
        }
    }

    /**
     * @param rsite the rsite to set
     * @throws ConstraintException
     */
    public void setRsite(final String rsite) throws ConstraintException {
        final Parameter param = this.getParameter(FormFieldsNames.rsite);
        param.setValue(rsite);
    }

    // TODO specify more details in the protocol
    public static Collection<Experiment> getEntryCloneExperiments(final ReadableVersion rw) {
        final Map expTypeParam = org.pimslims.lab.Util.getNewMap();
        expTypeParam.put(ExperimentType.PROP_NAME, FormFieldsNames.entryClone);
        final Collection<ExperimentType> expType = rw.findAll(ExperimentType.class, expTypeParam);
        if (expType == null || expType.size() == 0) {
            return Collections.EMPTY_SET;
        }
        final Collection<Experiment> experiments = expType.iterator().next().getExperiments();
        final ArrayList<Experiment> entryCExp = new ArrayList<Experiment>();
        for (final Experiment experiment : experiments) {
            if (SavedPlasmid.isValidEntryClone(experiment)) {
                entryCExp.add(experiment);
            }
        }
        // for (Experiment experiment : experiments) {
        // Pro experiment.getProtocol();
        // }
        return entryCExp;
    }

    public static Collection<Experiment> getExpressionConstructExperiments(final ReadableVersion rw) {

        // first get the legacy experiments
        final ExperimentType type = SavedPlasmid.getLegacyExpressionConstructExperimentType(rw);
        final Collection<Experiment> experiments = type.getExperiments();
        final ArrayList<Experiment> ret = new ArrayList<Experiment>();
        for (final Experiment experiment : experiments) {
            if (SavedPlasmid.isValidExpressionConstruct(experiment)) {
                ret.add(experiment);
            }
        }

        // now get the ones done with new protocol
        final Protocol protocol =
            rw.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.EXPRESSION_CONSTRUCT_PROTOCOL);
        if (null != protocol) {
            ret.addAll(protocol.getExperiments());
        }
        return ret;
    }

    private static ExperimentType getLegacyExpressionConstructExperimentType(final ReadableVersion rw) {
        final Map expTypeParam = org.pimslims.lab.Util.getNewMap();
        expTypeParam.put(ExperimentType.PROP_NAME, "Expression");
        final Collection<ExperimentType> expType = rw.findAll(ExperimentType.class, expTypeParam);
        final ExperimentType type = expType.iterator().next();
        return type;
    }

    public static ArrayList<SavedPlasmid> getPlasmids(final Collection<Experiment> experiments) {
        final ArrayList<SavedPlasmid> plasmids = new ArrayList<SavedPlasmid>();
        for (final Experiment experiment : experiments) {
            assert experiment != null : "Experiment is null!";
            plasmids.add(new SavedPlasmid(experiment));
        }
        return plasmids;
    }

/*
    public SavedPlasmid getPlasmid(final ReadableVersion rv, final String experimentHook) {
        return new SavedPlasmid((Experiment) rv.get(experimentHook));
    } */

    /*
     * Method mandates all input samples has samples set in order for the experiment to be valid EntryClone
     * experiment 
     */
    public static boolean isValidEntryClone(final Experiment experiment) {
        if (!SavedPlasmid.isValidConstruct(experiment)) {
            return false;
        }
        final ExperimentType eType = experiment.getExperimentType();
        if (eType == null || !(eType.getName().equalsIgnoreCase(FormFieldsNames.entryClone))) {
            return false;
        }

        return true;
    }

    public static boolean isValidExpressionConstruct(final Experiment experiment) {
        if (FormFieldsNames.RECOMBINATION.equals(experiment.getExperimentType().getName())) {
            return true; // new style expression construct
        }
        // otherwise check for legacy data
        if (!SavedPlasmid.isValidConstruct(experiment)) {
            return false;
        }
        final ExperimentType eType = experiment.getExperimentType();
        if (eType == null
            || !(eType.getName().equalsIgnoreCase(FormFieldsNames.LEGACY_EXPRESSION_CONSTRUCT_TYPE))) {
            return false;
        }

        final Protocol prot = experiment.getProtocol();
        if (prot != null
            && !(prot.getName().equalsIgnoreCase(FormFieldsNames.LEGACY_EXPRESSION_CONSTRUCT_TYPE) || prot
                .getName().equalsIgnoreCase(FormFieldsNames.EXPRESSION_CONSTRUCT_PROTOCOL))) {
            return false;
        }

        return true;
    }

    /*
     * Method mandates all input samples has samples set in order for the experiment to be valid EntryClone
     * experiment 
     */
    public static boolean isValidDeepFrozenCulture(final Experiment experiment) {
        if (!SavedPlasmid.isValidConstruct(experiment)) {
            return false;
        }

        final ExperimentType eType = experiment.getExperimentType();
        if (eType == null || !(eType.getName().equalsIgnoreCase(FormFieldsNames.dfrozen))) {
            return false;
        }

        return true;
    }

    /**
     * @param experiment
     */
    static boolean isValidConstruct(final Experiment experiment) {
        if (experiment == null) {
            return false;
        }
        final Collection<InputSample> inSamples = experiment.getInputSamples();
        if (inSamples.size() == 0) {
            return false;
        } /* this condition removed by Chris, so can make primerless construct for target
                                                                                                                                                                                                                                                                                                for (final InputSample inSample : inSamples) {
                                                                                                                                                                                                                                                                                                    final Sample sample = inSample.getSample();
                                                                                                                                                                                                                                                                                                    if (sample == null) {
                                                                                                                                                                                                                                                                                                        return false;
                                                                                                                                                                                                                                                                                                    }
                                                                                                                                                                                                                                                                                                } */
        final Set<OutputSample> outSamples = experiment.getOutputSamples();
        if (outSamples.size() < 1) {
            return false;
        }
        return true;
    }

    public String getForwardPrimer() {
        final Sample sample = this.getForwardPrimerSample();
        if (null == sample) {
            return null;
        }
        return sample.get_Hook();
    }

    /**
     * @return the Clone saver name
     */
    public String getCloneSaverName() {
        final Sample sample = this.getOutSample(3);
        if (sample == null) {
            return null;
        }
        final Holder sampleHolder = sample.getHolder();
        if (sampleHolder == null) {
            return null;
        }
        final Location location = ContainerUtility.getCurrentLocation(sampleHolder);
        assert location != null : "Current location is null";
        return location.get_Hook();

    }

    public String getCloneSaverPosition() {
        final Sample cloneSaver = this.getOutSample(3);
        if (cloneSaver != null) {
            return PrimerBeanReader.getPosition(cloneSaver);
        }
        return "";
    }

    /**
     * @param tag the tag to set
     * @throws ConstraintException
     * @throws ClassNotFoundException
     * @throws AccessException
     */
    public void setTag(final String tag) throws ConstraintException, AccessException, ClassNotFoundException {
        final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
        Parameter param = this.getParameter(FormFieldsNames.tag);
        if (param == null) {
            param =
                (Parameter) Creator.recordParameters(rw, FormFieldsNames.tag, "String", tag, this.experiment);
        } else {
            param.setValue(tag);
        }
    }

    public String getTag() {
        final Parameter p = this.getParameter(FormFieldsNames.tag);
        if (p != null) {
            return this.getParameter(FormFieldsNames.tag).getValue();
        }
        return "";
    }

    public Collection<SavedDeepFrozenCulture> getDeepFrozenCultures() {
        final ArrayList<SavedDeepFrozenCulture> deepFrozenCultures = new ArrayList<SavedDeepFrozenCulture>();
        final Sample sample = this.getOutSample(1);
        final Collection<InputSample> inSamples = sample.getInputSamples();
        if (inSamples == null) {
            return null;
        }
        for (final InputSample inSample : inSamples) {
            final Experiment exp = inSample.getExperiment();
            if (exp.getExperimentType().getName().equals(FormFieldsNames.dfrozen)) {
                deepFrozenCultures.add(new SavedDeepFrozenCulture(exp));
            }
        }
        return deepFrozenCultures;
    }

    public static Collection<Experiment> getDeepFrozenCultures(final ReadableVersion rv) {
        final Map expTypeParam = org.pimslims.lab.Util.getNewMap();
        expTypeParam.put(ExperimentType.PROP_NAME, FormFieldsNames.dfrozen);
        final Collection<ExperimentType> expType = rv.findAll(ExperimentType.class, expTypeParam);
        if (expType == null || expType.size() == 0) {
            return Collections.EMPTY_SET;
        }
        final Collection<Experiment> experiments = expType.iterator().next().getExperiments();
        final ArrayList<Experiment> deepFrCultures = new ArrayList<Experiment>();
        for (final Experiment experiment : experiments) {
            if (SavedPlasmid.isValidDeepFrozenCulture(experiment)) {
                deepFrCultures.add(experiment);
            }
        }
        return deepFrCultures;
    }

    // Sample name is not changeable through the interface & ends with (1)
    public static String getConstructName(final String sampleName) {
        return sampleName.substring(0, sampleName.indexOf("("));
    }

    /**
     * SavedPlasmid.getForwardPrimerSample
     * 
     * @return
     */
    public Sample getForwardPrimerSample() {
        return this.getInputSample(ConstructBeanWriter.FPRIMER);
    }

    /**
     * SavedPlasmid.getReversePrimerSample
     * 
     * @return
     */
    public Sample getReversePrimerSample() {
        return this.getInputSample(ConstructBeanWriter.RPRIMER);
    }

    /**
     * SavedPlasmid.getEntryCloneHook
     * 
     * @return
     */
    public String getEntryCloneHook() {
        final Sample sample = this.getInputSample(FormFieldsNames.ENTRY_CLONE_INPUT_SAMPLE);
        if (null == sample) {
            return null;
        }
        return sample.get_Hook();
    }

    /*
     * public Collection<DeepFrozenCulture> getDeepFrozenCultures() { ExperimentGroup eGroup =
     * experiment.getExperimentGroup(); if(eGroup == null) return null; Set<Experiment> exps =
     * eGroup.getExperiments(); ArrayList<DeepFrozenCulture> cultures = new ArrayList<DeepFrozenCulture>();
     * for(Experiment exp : exps) { if(exp.getExperimentType().getName().equals(FormFieldsNames.dfrozen)) {
     * cultures.add(new DeepFrozenCulture(exp)); } } return cultures; } public void
     * addDeepFrozenCulture(Experiment dCultureExperiment) throws AccessException, ConstraintException,
     * ClassNotFoundException { if(dCultureExperiment == null) return; ExperimentGroup eGroup =
     * this.experiment.getExperimentGroup(); if(eGroup==null) { // Create new group WritableVersion rw
     * =(WritableVersion)dCultureExperiment.get_Version(); List exps =
     * org.pimslims.command.leeds.Utils.makeList(this.experiment); exps.add(dCultureExperiment); HashMap prop =
     * new HashMap(); prop.put(ExperimentGroup.PROP_NAME, getName());
     * prop.put(ExperimentGroup.PROP_EXPERIMENTS, exps); prop.put(ExperimentGroup.PROP_PURPOSE, "Group deep
     * frozen culture stock"); //ExperimentGroup.PROP_NAME ExperimentGroup expg = Util.create(rw,
     * ExperimentGroup.class, prop); } else { eGroup.addExperiment(dCultureExperiment); } } public void
     * addDeepFrozenCulture(String dCultureExperimentHook) throws AccessException, ConstraintException,
     * ClassNotFoundException { if(! Utils.isHookValid(dCultureExperimentHook)) throw new
     * InvalidParameterException("Must be a hook!");
     * addDeepFrozenCulture((Experiment)experiment.get_Version().get(dCultureExperimentHook)); } public void
     * removeDeepFrozenCulture(Experiment experiment) throws ConstraintException { ExperimentGroup eGroup =
     * experiment.getExperimentGroup(); if(eGroup==null) return; eGroup.removeExperiment(experiment); }
     */
}
