/**
 * 
 */
package org.pimslims.presentation.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.Util;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;

/**
 * @author Petr Troshin
 */

@Deprecated
// Leeds code is no longer supported
public class SavedDeepFrozenCulture extends AbstractSavedPlasmid implements FormFieldsNames {

    public SavedDeepFrozenCulture(final Experiment experiment) {
        super(experiment);
    }

    public void updatePlasmid(final DeepFrozenCultureBean leedsPlasmid) throws ConstraintException,
        AccessException, ClassNotFoundException {

        super.updatePlasmid(leedsPlasmid);

        if (this.getOrganism() == null || !this.getOrganism().equals(leedsPlasmid.getOrganism())) {
            this.setOrganism(leedsPlasmid.organism);
        }
        if (this.getStrain() == null || !this.getStrain().equals(leedsPlasmid.getStrain())) {
            this.setStrain(leedsPlasmid.strain);
        }

        // set hook of entry clone
        if (this.getEntryCloneHook() == null
            || !this.getEntryCloneHook().equals(leedsPlasmid.getEntryCloneHook())) {
            this.setEntryClone(leedsPlasmid.getEntryCloneHook());
        }

    }

    void setStrain(final String strainHook) throws AccessException, ConstraintException {
        Sample strainSample = null;
        boolean inSampleNotFound = true;
        final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
        if (Util.isHookValid(strainHook)) {
            // should test this bit
            strainSample = this.experiment.get_Version().get(strainHook);
            assert strainSample != null;
            final Collection<InputSample> inSamples = this.experiment.getInputSamples();
            for (final InputSample inSample : inSamples) {
                final Sample sample = inSample.getSample();
                final Collection<SampleCategory> scategories = sample.getSampleCategories();
                assert scategories != null : "Input sample categories wrong. Cannot proceed";
                for (final SampleCategory scat : scategories) {
                    if (scat.getName().equals(FormFieldsNames.strain)) {
                        inSample.setSample(strainSample);
                        inSampleNotFound = false;
                        break;
                    }
                }
            }
            if (inSampleNotFound) {
                Creator.recordInputSamples(rw, strainSample, this.experiment, FormFieldsNames.strain);
            }
        } else {
            strainSample = DeepFrozenCultureBean.recordSample(rw, FormFieldsNames.strain, strainHook);
            Creator.recordInputSamples(rw, strainSample, this.experiment, FormFieldsNames.strain);
        }

    }

    public String getStrain() {
        final Sample strain = this.getInSample(FormFieldsNames.STRAIN_INPUT);
        if (strain != null) {
            return strain.get_Hook();
        }
        return "";
    }

    /*
     * @return Sample by its Category
     */
    public Sample getInSample(final String sampleCategory) {
        final Collection<InputSample> samples = this.experiment.getInputSamples();
        if (samples == null) {
            return null;
        }
        for (final InputSample inSample : samples) {
            final Sample sample = inSample.getSample();
            final Collection<SampleCategory> scategories = sample.getSampleCategories();
            if (scategories == null) {
                break;
            }
            for (final SampleCategory scat : scategories) {
                if (scat.getName().equals(sampleCategory)) {
                    return sample;
                }
            }
        }
        return null;
    }

    /*
     * @return hook of an Organism
     */
    public String getOrganism() {
        String orgName = "";
        final Parameter par = this.getParameter(FormFieldsNames.organism);
        if (par != null) {
            orgName = par.getValue();
        }
        return orgName;
    }

    /*
     * Method assumes that parameter was created before
     */
    void setOrganism(final String organism) throws ConstraintException, AccessException {
        final Parameter par = this.getParameter(FormFieldsNames.organism);
        if (par != null) {
            par.setValue(organism);
        } else {
            final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
            Creator.recordParameters(rw, FormFieldsNames.organism, "String", organism, this.experiment);
        }
    }

    /*
     * Method assumes that parameter was created before public void setGenotype(String genotype) throws
     * ConstraintException, AccessException, ClassNotFoundException { Parameter par =
     * getParameter(FormFieldsNames.genotype); if(par != null) { par.setValue(genotype); } else {
     * WritableVersion rw = (WritableVersion)experiment.get_Version(); Creator.recordParameters(rw,
     * FormFieldsNames.genotype, "String", genotype, experiment); } }
     */

    /**
     * specify introduced plasmid TODO but isn't this an Expression Construct rather than an entry clone? Note
     * this may be called by reflection
     * 
     * @param hook
     * @throws ConstraintException
     * @throws AccessException
     * @throws ClassNotFoundException
     */
    void setEntryClone(final String hook) throws ConstraintException, AccessException {
        assert Util.isHookValid(hook) : "Maker hook is invalid!";
        final Sample marker = this.experiment.get_Version().get(hook);
        final Collection<InputSample> inSamples = this.experiment.getInputSamples();
        boolean inSampleNotFound = true;
        if (inSamples == null) {
            this.recordEntryClone(marker);
        } else {
            for (final InputSample inSample : inSamples) {
                final Sample sample = inSample.getSample();
                final Collection<SampleCategory> scategories = sample.getSampleCategories();
                for (final SampleCategory scat : scategories) {
                    if (scat.getName().equals(FormFieldsNames.RECOMBINANT_PLASMID)) {
                        inSample.setSample(marker);
                        inSampleNotFound = false;
                        break;
                    }
                }
            }
            if (inSampleNotFound) {
                this.recordEntryClone(marker);
            }
        }
    }

    private void recordEntryClone(final Sample entryClone) throws AccessException, ConstraintException {
        final WritableVersion rw = (WritableVersion) this.experiment.get_Version();
        Creator.recordInputSamples(rw, entryClone, this.experiment, FormFieldsNames.entryClone);
    }

    /**
     * Get Entry Clone Marker is recorded as InputSample->Sample 3 TODO isn't this the expression construct?
     * 
     * @return Hook to the Entry Clone Marker (Introduced Plasmid) is a plasmid, plasmid sample number is 3 in
     *         getInSample
     */
    public String getEntryCloneHook() {

        final Sample marker = this.getInputSample(FormFieldsNames.ENTRY_CLONE_INPUT_SAMPLE);
        //System.out.println("Marker " + marker);
        if (marker != null) {
            return marker.get_Hook();
        }
        return null;
    }

    /*
     * @return Entry Clone experiment 
     * TODO isn't this the expression construct?
     */
    public Experiment getEntryClone() {
        final String sampleHook = this.getEntryCloneHook();
        if (Util.isHookValid(sampleHook)) {
            final Sample sample = this.experiment.get_Version().get(sampleHook);
            final OutputSample outSample = sample.getOutputSample();
            if (outSample != null) {
                final Experiment entryClone = outSample.getExperiment();
                if (SavedPlasmid.isValidEntryClone(entryClone)
                    || SavedPlasmid.isValidExpressionConstruct(entryClone)) {
                    return entryClone;
                }
            }
        }
        return null;
    }

    /*
     * Method mandates all input samples has samples set in order for the experiment to be valid EntryClone
     * experiment 
     
    public static boolean isValidExpConstruct(final Experiment experiment) {
        if (experiment == null) {
            return false;
        }
        final Collection<OutputSample> outSamples = experiment.getOutputSamples();
        if (outSamples == null || outSamples.size() == 0) {
            return false;
        }
        for (final OutputSample outSample : outSamples) {
            final Sample sample = outSample.getSample();
            if (sample == null) {
                return false;
            }
        }
        final ExperimentType eType = experiment.getExperimentType();
        if (eType == null || !eType.getName().equalsIgnoreCase(FormFieldsNames.dfrozen)) {
            return false;
        }

        final Protocol prot = experiment.getProtocol();
        if (prot != null && !prot.getName().equalsIgnoreCase(FormFieldsNames.expression)) {
            return false;
        }

        return true;
    }
    */

    // TODO specify more details in the protocol
    public static Collection<Experiment> getExpConstructExperiments(final ReadableVersion rw) {
        final ExperimentType type = SavedDeepFrozenCulture.getExperimentType(rw);
        final Collection<Experiment> experiments = type.getExperiments();
        final ArrayList<Experiment> entryCExp = new ArrayList<Experiment>();
        for (final Experiment experiment : experiments) {
            if (SavedPlasmid.isValidDeepFrozenCulture(experiment)) {
                entryCExp.add(experiment);
            }
        }
        // for (Experiment experiment : experiments) {
        // Pro experiment.getProtocol();
        // }
        return entryCExp;
    }

    /**
     * DeepFrozenCulture.getDeepFrozenCultureExperimentType
     * 
     * @param rw
     * @return
     */
    public static ExperimentType getExperimentType(final ReadableVersion rw) {
        final Map expTypeParam = Util.getNewMap();
        expTypeParam.put(ExperimentType.PROP_NAME, FormFieldsNames.dfrozen);
        final Collection<ExperimentType> expType = rw.findAll(ExperimentType.class, expTypeParam);
        if (expType.size() != 1) {
            throw new IllegalStateException("Experiment type not found: " + FormFieldsNames.dfrozen);
        }
        final ExperimentType type = expType.iterator().next();
        return type;
    }

    public static List<SavedDeepFrozenCulture> getPlasmids(final Collection<Experiment> experiments) {
        if (experiments == null) {
            return null;
        }
        final ArrayList<SavedDeepFrozenCulture> plasmids = new ArrayList<SavedDeepFrozenCulture>();
        for (final Experiment experiment : experiments) {
            if (experiment == null) {
                continue;
            }
            plasmids.add(new SavedDeepFrozenCulture(experiment));
        }
        return plasmids;
    }
/*
    public DeepFrozenCulture getPlasmid(final ReadableVersion rv, final String experimentHook) {
        return new DeepFrozenCulture((Experiment) rv.get(experimentHook));
    } */

}
