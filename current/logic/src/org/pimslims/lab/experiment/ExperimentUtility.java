/**
 * pims-web org.pimslims.lab.experiment ExperimentUtility.java
 * 
 * @author Marc Savitsky
 * @date 14 Jan 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.experiment;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;

/**
 * ExperimentUtility
 * 
 */

public class ExperimentUtility {

    //TODO still in use, is this really old?
    public static final String SPOTCONSTRUCT_DESIGN_OLD = "SPOTConstructDesign";

    // new name, for all fresh V1_0 installations
    public static final String SPOTCONSTRUCT_DESIGN = "SPOT Construct Design";

    public static final String SPOTCONSTRUCT_DESIGN_PROTOCOL = "SPOT Construct Primer Design";

    public static final String OPPFCONSTRUCT_DESIGN_PROTOCOL = "OPPF Construct Primer Design";

    public static final String SDMCONSTRUCT_DESIGN_PROTOCOL = "SPOT Construct SDM Primer Design";

    public static Experiment getPrimerDesignExperiment(final Project expBlueprint) {

        Experiment experiment = null;
        for (final Experiment exp : expBlueprint.getExperiments()) {
            experiment = exp;
            if (exp.getProtocol() != null) {
                if (exp.getProtocol().getName().equalsIgnoreCase(ExperimentUtility.SPOTCONSTRUCT_DESIGN_OLD)) {
                    break;
                }
                if (exp.getProtocol().getName()
                    .equalsIgnoreCase(ExperimentUtility.SPOTCONSTRUCT_DESIGN_PROTOCOL)) {
                    break;
                }
                if (exp.getProtocol().getName()
                    .equalsIgnoreCase(ExperimentUtility.OPPFCONSTRUCT_DESIGN_PROTOCOL)) {
                    break;
                }
                if (exp.getProtocol().getName()
                    .equalsIgnoreCase(ExperimentUtility.SDMCONSTRUCT_DESIGN_PROTOCOL)) {
                    break;
                }
            }
        }

        return experiment;
    }

    public static Sample getForwardPrimer(final Experiment primerdesignExperiment) {

        for (final OutputSample outputSample : primerdesignExperiment.getOutputSamples()) {
            //System.out.println("Reading OS: " + outputSample.getName()); //TODO remove
            final Sample sample = outputSample.getSample();
            if (null == sample) {
                System.out.println("No sample for: " + outputSample.getName()); //TODO remove
                continue;
            }
            final Set<SampleComponent> sampleComps = sample.getSampleComponents();
            for (final Iterator iterator = sampleComps.iterator(); iterator.hasNext();) {
                final SampleComponent scomp = (SampleComponent) iterator.next();
                final ModelObject object = scomp.getRefComponent();
                if (null != object && object instanceof Primer) {
                    final org.pimslims.model.molecule.Primer primer =
                        (org.pimslims.model.molecule.Primer) object;
                    if (primer.getDirection().equals("forward")) {
                        return sample;
                    }
                }
            }
        }
        return null;
    }

    // find reverse or anti-sense primer
    public static Sample getReversePrimer(final Experiment primerdesignExperiment) {

        for (final OutputSample outputSample : primerdesignExperiment.getOutputSamples()) {
            final Sample sample = outputSample.getSample();
            //"Primer could only have one sample component";
            if (null != sample && sample.getSampleComponents() != null
                && sample.getSampleComponents().size() == 1) {
                final Set<SampleComponent> sampleComps = sample.getSampleComponents();
                final SampleComponent scomp = sampleComps.iterator().next();

                final ModelObject object = sample.get_Version().get(scomp.getRefComponent().get_Hook());

                if (null != object && object instanceof Primer) {

                    final org.pimslims.model.molecule.Primer primer =
                        (org.pimslims.model.molecule.Primer) object;

                    if (primer.getDirection().equals("reverse")) {
                        return sample;
                    }
                }
            }
        }
        return null;
    }

    public static Sample getForwardPrimerSample(final Experiment primerdesignExperiment) {

        for (final OutputSample outputSample : primerdesignExperiment.getOutputSamples()) {
            final Sample sample = outputSample.getSample();
            //"Primer could only have one sample component";
            if (null != sample && sample.getSampleComponents() != null
                && sample.getSampleComponents().size() == 1) {
                final Set<SampleComponent> sampleComps = sample.getSampleComponents();
                final SampleComponent scomp = sampleComps.iterator().next();

                final ModelObject object = sample.get_Version().get(scomp.getRefComponent().get_Hook());

                if (null != object && object instanceof Primer) {

                    final org.pimslims.model.molecule.Primer primer =
                        (org.pimslims.model.molecule.Primer) object;

                    if (primer.getDirection().equals("forward")) {
                        return sample;
                    }
                }
            }
        }
        return null;
    }

    public static Sample getReversePrimerSample(final Experiment primerdesignExperiment) {

        for (final OutputSample outputSample : primerdesignExperiment.getOutputSamples()) {
            final Sample sample = outputSample.getSample();
            //"Primer could only have one sample component";
            if (null != sample && sample.getSampleComponents() != null
                && sample.getSampleComponents().size() == 1) {
                final Set<SampleComponent> sampleComps = sample.getSampleComponents();
                final SampleComponent scomp = sampleComps.iterator().next();

                final ModelObject object = sample.get_Version().get(scomp.getRefComponent().get_Hook());

                if (null != object && object instanceof Primer) {

                    final org.pimslims.model.molecule.Primer primer =
                        (org.pimslims.model.molecule.Primer) object;

                    if (primer.getDirection().equals("reverse")) {
                        return sample;
                    }
                }
            }
        }
        return null;
    }

    public static Sample getTemplateSample(final Experiment primerdesignExperiment) {

        for (final OutputSample outputSample : primerdesignExperiment.getOutputSamples()) {
            final Sample sample = outputSample.getSample();
            //"Primer could only have one sample component";
            if (null != sample && sample.getSampleComponents() != null
                && sample.getSampleComponents().size() == 1) {
                final Set<SampleComponent> sampleComps = sample.getSampleComponents();
                final SampleComponent scomp = sampleComps.iterator().next();

                final ModelObject object = sample.get_Version().get(scomp.getRefComponent().get_Hook());

                if (null != object && object instanceof Molecule && !(object instanceof Primer)) {
                    return sample;
                }
            }
        }
        return null;
    }

    /*
     * PiMS 1538 Construct Primers should be set automatically in PCR Experiment 
     *           when Construct is selected
     */
    public static void setExpBlueprintSamples(final Experiment experiment,
        final ResearchObjective expBlueprint) {

        ExperimentUtility.setExpBlueprintSamples(experiment, expBlueprint, false);
    }

    public static void setExpBlueprintSamples(final Experiment experiment,
        final ResearchObjective expBlueprint, final boolean force) {

        if (null == expBlueprint) {
            return;
        }

        final Experiment primerdesignExperiment = ExperimentUtility.getPrimerDesignExperiment(expBlueprint);

        if (null == primerdesignExperiment) {
            return;
        }

        final Sample forwardPrimerSample = ExperimentUtility.getForwardPrimerSample(primerdesignExperiment);
        final Sample reversePrimerSample = ExperimentUtility.getReversePrimerSample(primerdesignExperiment);
        final Sample templateSample = ExperimentUtility.getTemplateSample(primerdesignExperiment);

        final Collection<InputSample> ios = experiment.getInputSamples();
        try {
            for (final InputSample input : ios) {

                if (force == true || null == input.getSample()) {

                    if (null != forwardPrimerSample) {
                        for (final SampleCategory sampleCategory : forwardPrimerSample.getSampleCategories()) {
                            if (sampleCategory == input.getRefInputSample().getSampleCategory()) {
                                input.setSample(forwardPrimerSample);
                            }
                        }
                    }

                    if (null != reversePrimerSample) {
                        for (final SampleCategory sampleCategory : reversePrimerSample.getSampleCategories()) {
                            if (sampleCategory == input.getRefInputSample().getSampleCategory()) {
                                input.setSample(reversePrimerSample);
                            }
                        }
                    }

                    if (null != templateSample) {
                        for (final SampleCategory sampleCategory : templateSample.getSampleCategories()) {
                            if (null != input.getRefInputSample()
                                && sampleCategory == input.getRefInputSample().getSampleCategory()) {
                                input.setSample(templateSample);
                            }
                        }
                    }
                }
            }

        } catch (final ConstraintException e) {
            System.out.println("ConstraintException [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();
        }
    }

    public static void clearExpBlueprintSamples(final Experiment experiment) {

        final Project expBlueprint = experiment.getProject();
        if (null == expBlueprint) {
            return;
        }

        final Experiment primerdesignExperiment = ExperimentUtility.getPrimerDesignExperiment(expBlueprint);

        if (null == primerdesignExperiment) {
            return;
        }

        final Sample forwardPrimerSample = ExperimentUtility.getForwardPrimerSample(primerdesignExperiment);
        final Sample reversePrimerSample = ExperimentUtility.getReversePrimerSample(primerdesignExperiment);
        final Sample templateSample = ExperimentUtility.getTemplateSample(primerdesignExperiment);

        final Collection<InputSample> ios = experiment.getInputSamples();
        try {
            for (final InputSample input : ios) {

                if (null != forwardPrimerSample) {
                    for (final SampleCategory sampleCategory : forwardPrimerSample.getSampleCategories()) {
                        if (sampleCategory == input.getRefInputSample().getSampleCategory()) {
                            input.setSample(null);
                        }
                    }
                }

                if (null != reversePrimerSample) {
                    for (final SampleCategory sampleCategory : reversePrimerSample.getSampleCategories()) {
                        if (sampleCategory == input.getRefInputSample().getSampleCategory()) {
                            input.setSample(null);
                        }
                    }
                }

                if (null != templateSample) {
                    for (final SampleCategory sampleCategory : templateSample.getSampleCategories()) {
                        if (sampleCategory == input.getRefInputSample().getSampleCategory()) {
                            input.setSample(null);
                        }
                    }
                }
            }

        } catch (final ConstraintException e) {
            System.out.println("ConstraintException [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();
        }
    }

}
