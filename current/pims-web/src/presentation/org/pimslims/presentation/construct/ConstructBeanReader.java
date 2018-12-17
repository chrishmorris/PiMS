package org.pimslims.presentation.construct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ConstructMileStoneUtil;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PimsQuery;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.TargetBean;

/**
 * Factory class to populate the fields of a Presentation layer ConstructBean
 * 
 * @author Johan van Niekerk
 */
public class ConstructBeanReader {

    /**
     * ConstructBeanReader.readConstruct
     * 
     * @param exp
     * @return
     */
    public static ConstructBean readConstruct(final Experiment exp) {
        return ConstructBeanReader.readConstruct((ResearchObjective) exp.getProject(), exp);
    }

    /**
     * ConstructBeanReader.readConstruct
     * 
     * @param eb
     * @return
     */
    public static ConstructBean readConstruct(final ResearchObjective ro) {
        final Experiment experiment = ExperimentUtility.getPrimerDesignExperiment(ro); // may be null at this stage
        return ConstructBeanReader.readConstruct(ro, experiment);
    }

    /**
     * ConstructBeanReader.readConstruct
     * 
     * @param ro
     * @param target
     * @param cb
     */
    private static ConstructBean readConstruct(final ResearchObjective ro, final Experiment experiment) {
        Target target = null;
        TargetBean tb = new TargetBean();
        if (null != ro) {
            target = ConstructBeanReader.getTarget(ro);
            if (null != target) {
                tb = new TargetBean(target);
            }
        }

        PrimerBean forward = new PrimerBean(true);
        PrimerBean reverse = new PrimerBean(false);
        if (null != experiment) {
            final Sample forwardPrimer = ExperimentUtility.getForwardPrimer(experiment);
            if (null != forwardPrimer) {
                forward = PrimerBeanReader.readPrimer(forwardPrimer);
            }
            final Sample reversePrimer = ExperimentUtility.getReversePrimer(experiment);
            if (null != reversePrimer) {
                reverse = PrimerBeanReader.readPrimer(reversePrimer);
            }
        }
        ConstructBean cb = null;
        if (null == ro) {
            cb = new ConstructBean(tb, forward, reverse);
        } else {
            cb = new ConstructBean(ro, tb, forward, reverse);
            ConstructBeanReader.processResearchObjective(ro, target, cb);
        }
        if (null != experiment) {
            ConstructBeanReader.doReadPrimerDesignExperiment(experiment, cb);
        }
        return cb;
    }

    private static void processResearchObjective(final ResearchObjective eb, final Target target,
        final ConstructBean cb) {

        cb.setPimsExpBlueprintHook(eb.get_Hook());
        cb.setConstructId(eb.getCommonName());
        cb.setLocalName(eb.getLocalName());
        cb.setCreationDate(eb.getCreationDate());
        cb.setMayDelete(eb.get_MayDelete());

        // Set Description
        cb.setDescription(eb.getFunctionDescription());
        // Set the comments
        cb.setComments(eb.getDetails());

        final Collection<ResearchObjectiveElement> elements = eb.getResearchObjectiveElements();
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            ConstructBeanReader.processResearchObjectiveElement(target, cb, element);
        }

        if (null != cb.getFinalProt()) {
            ConstructAnnotator.annotateFinalProt(cb);
        }

        // System.out.println("Prots "+((Molecule)a).getSeqString());
        // readBioinfExperiment(version,cb,eb);

        final User scientist = eb.getOwner();
        if (null != scientist) {
            cb.setUserName(scientist.getName());
            cb.setUserHook(scientist.get_Hook());
        }

        //cb.setProgressList(ConstructBeanReader.getConstructProgress(eb));
        cb.setLatestMilestone(ConstructMileStoneUtil.getLatestMilestone(eb));
        //For Synthetic gene
        if (eb.getSimilarityDetails().contains("Synthetic gene ::")) {
            String[] sgBits = new String[3];
            sgBits = eb.getSimilarityDetails().split("::");
            final String sgSampleName = sgBits[1].trim();
            final String sgSampleHook = sgBits[2].trim();
            cb.setSgSampleHook(sgSampleHook);
            cb.setSgSampleName(sgSampleName);
            //cb.setConstructId(sgSampleName);

        }
    }

    private static void processResearchObjectiveElement(final Target target, final ConstructBean cb,
        final ResearchObjectiveElement element) {
        if (null != target) {
            if (!PIMSTarget.isDNATarget(target)) {
                // we don't want these to change the primer beans!
                cb.setTargetProtEnd(element.getApproxEndSeqId());
                cb.setTargetProtStart(element.getApproxBeginSeqId());
            } else {
                cb.setTargetDnaEnd(element.getApproxEndSeqId());
                cb.setTargetDnaStart(element.getApproxBeginSeqId());
                cb.setDnaTarget("DnaTarget");
            }
        }

        // Set DNA sequence
        if (element.getMolecule() != null) {
            cb.setDnaSeq(element.getMolecule().getSequence());
            cb.setWildDnaSeq(element.getMolecule().getSequence());
        }

        //PiMS 1516 ComponentCategories
        for (final Molecule bc : element.getTrialMolComponents()) {

            for (final ComponentCategory category : bc.getCategories()) {

                if (category.getName().equals(ConstructBean.EXPRESSED_PROTEIN)) {
                    cb.setExpressedProt(bc.getSequence());
                    cb.setExpressedProteinHook(bc.get_Hook() + ":seqString");
                }

                if (category.getName().equals(ConstructBean.FINAL_PROTEIN)) {
                    cb.setFinalProt(bc.getSequence());
                    cb.setFinalProteinHook(bc.get_Hook() + ":seqString");
                }

                if (category.getName().equals(ConstructBeanForList.PROTEIN)) {
                    cb.setProtSeq(bc.getSequence());
                    cb.setProtSeqHook(bc.get_Hook() + ":seqString");
                }

                if (category.getName().equals(CaliperFile.PCR_PRODUCT)) {
                    cb.setPcrProductSeq(bc.getSequence());
                    cb.setPcrProductSeqHook(bc.get_Hook() + ":seqString");
                    cb.setPcrProductGC(new Double(ConstructBeanReader.getGCContent(cb.getPcrProductSeq())));
                }
            }
        }
    }

    private static void doReadPrimerDesignExperiment(final Experiment primerdesignExperiment,
        final ConstructBean cb) {
        if (null != primerdesignExperiment.getProtocol()
            && primerdesignExperiment.getProtocol().getName()
                .equals(ExperimentUtility.SDMCONSTRUCT_DESIGN_PROTOCOL)) {
            cb.setSDMConstruct(true);
        }

        //TODO move this earlier, so readPrimer methods can override the overlap length
        ConstructBeanReader.readPrimerDesignExptParams(cb, primerdesignExperiment);
        // note that sets the overlaps (length on gene)
        final Sample template = ExperimentUtility.getTemplateSample(primerdesignExperiment);
        if (null != template) {
            cb.setTemplateName(template.getName());
        }

        // process input samples
        final ModelObject vector =
            primerdesignExperiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_NAME, "Vector");
        if (null != vector) {
            cb.setVectorInputSampleHook(vector.get_Hook());
        }
    }

    /**
     * 
     */
    @Deprecated
    // obsolete
    public static Collection<List<ModelObjectBean>> getConstructProgress(final ResearchObjective eb) {

        final Collection<List<ModelObjectBean>> progressList = new ArrayList<List<ModelObjectBean>>();
        final Collection<Experiment> experiments = ConstructBeanReader.getNonCrystallizationExperiments(eb);
        final Collection<Experiment> terminalExperiments = new HashSet<Experiment>();

        // TODO this needs improvement for e.g. expression screening

        for (final Experiment experiment : experiments) {
            if (!ConstructBeanReader.isCharacterisationExperiment(experiment)) {
                final Collection<OutputSample> outputSamples = experiment.getOutputSamples();
                for (final OutputSample outputSample : outputSamples) {
                    final Sample sample = outputSample.getSample();
                    //System.out.println("ConstructBeanReader.terminalExperiments(" + experiment + ":"
                    //    + sample.getName() + ":" + sample.getInputSamples().size() + ")");
                    if (!ConstructBeanReader.isTemplate(sample)) {
                        if (null != sample) {
                            if (sample.getInputSamples().isEmpty()) {
                                terminalExperiments.add(experiment);
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("ConstructBeanReader.terminalExperiments(" + terminalExperiments.size() + ")");

        for (final Experiment experiment : terminalExperiments) {
            //System.out.println("ConstructBeanReader.terminalExperiment(" + experiment.get_Name() + ")");
            final List<ModelObjectBean> progressExperiments = new ArrayList<ModelObjectBean>();
            progressExperiments.add(BeanFactory.newBean(experiment));
            ConstructBeanReader.ancestry(experiment.getInputSamples(), eb, progressExperiments);
            Collections.reverse(progressExperiments);
            progressList.add(progressExperiments);
        }

        return progressList;
    }

    private static void ancestry(final Collection<InputSample> inputSamples, final ResearchObjective eb,
        final List<ModelObjectBean> progressExperiments) {

        //System.out.println("ConstructBeanReader.ancestry(" + inputSamples.size() + ")");
        for (final InputSample inputSample : inputSamples) {
            final Sample sample = inputSample.getSample();
            if (null != sample) {
                final OutputSample outputSample = sample.getOutputSample();
                if (null != outputSample) {
                    final Experiment experiment = outputSample.getExperiment();
                    if (experiment.getProject() == eb
                        && !ConstructBeanReader.isCharacterisationExperiment(experiment)) {
                        //System.out.println("ConstructBeanReader.ancestry add experiment("
                        //    + experiment.getName() + ")");
                        final ModelObjectBean experimentBean = BeanFactory.newBean(experiment);
                        if (!progressExperiments.contains(experimentBean)) {
                            progressExperiments.add(experimentBean);
                            ConstructBeanReader.ancestry(experiment.getInputSamples(), eb,
                                progressExperiments);
                        }
                    }
                }
            }
        }
    }

    private static boolean isTemplate(final Sample sample) {

        if (null != sample) {
            if (sample.getName().endsWith("T")) {
                final OutputSample outputSample = sample.getOutputSample();
                if (null != outputSample) {
                    final Experiment experiment = outputSample.getExperiment();
                    if (null != experiment) {
                        if (ConstructBeanReader.isConstructDesignExperiment(experiment)) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    //TODO should return collection, it may be polycistronic
    private static Target getTarget(final ResearchObjective expBlueprint) {
        Target ret = null;
        final Collection<ResearchObjectiveElement> c = expBlueprint.getResearchObjectiveElements();
        for (final Iterator iterator = c.iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            if (ret != null && element.getTarget() != null) {
                throw new AssertionError("Sorry, PiMS does not support polycistronic constructs yet.");
            }
            ret = element.getTarget();
        }
        return ret;
    }

    private static String PrimerDesignExperimentTypeHook = null;

    public static ExperimentType getPrimerDesignExperimentType(final ReadableVersion version) {
        // Find the ExperimentType with the
        // specified name 
        ExperimentType et = null;
        if (ConstructBeanReader.PrimerDesignExperimentTypeHook != null) {
            et = version.get(ConstructBeanReader.PrimerDesignExperimentTypeHook);
        }
        if (et != null
            && (et.getName().equalsIgnoreCase(ExperimentUtility.SPOTCONSTRUCT_DESIGN_OLD) || et.getName()
                .equalsIgnoreCase(ExperimentUtility.SPOTCONSTRUCT_DESIGN))) {
            return et;
        }

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", ExperimentUtility.SPOTCONSTRUCT_DESIGN_OLD);
        et = version.findFirst(ExperimentType.class, m);
        if (null == et) {
            m.put("name", ExperimentUtility.SPOTCONSTRUCT_DESIGN);
            et = version.findFirst(ExperimentType.class, m);

        }
        assert null != et : "Cannot find primer design experiment type";
        ConstructBeanReader.PrimerDesignExperimentTypeHook = et.get_Hook();
        return et;
    }

    /**
     * Find the Parameters from the primer design Experiment
     */
    private static void readPrimerDesignExptParams(final ConstructBean cb, final Experiment e) {

        // Load the Parameters from the primerDesign Experiment
        final Collection<Parameter> params = e.getParameters();

        // Get an iterator for this collection
        final Iterator<Parameter> iter = params.iterator();

        // Loop over all the elements in the Collection
        while (iter.hasNext()) {
            final Parameter p = iter.next(); // Get this Parameter

            // Get the parameter name
            String pName = p.getName();
            if (null == pName && null != p.getParameterDefinition()) { // for legacy data
                pName = p.getParameterDefinition().getName();
            }

            if (null == p.getValue()) {
                continue;
            }

            // note that forward and reverse tags are now read by the Leeds code 

            // for legacy constructs, that have no org.pimslims.model.molecule.Primer
            if (StringUtils.equals(ConstructBean.FORWARD_OVERLAP, pName)) {
                if (p.getValue().length() != 0) {
                    try {
                        final int length = Integer.parseInt(p.getValue().toString());
                        cb.setFwdOverlapLen(length);
                    } catch (final NumberFormatException e1) {
                        // obsolete format has the actual primer sequence in here
                        cb.setFwdOverlapLen(p.getValue().length());
                    }
                }

            } else if (StringUtils.equals(ConstructBean.REVERSE_OVERLAP, pName)) {
                if (p.getValue().length() != 0) {
                    try {
                        final int length = Integer.parseInt(p.getValue().toString());
                        cb.setRevOverlapLen(length);
                    } catch (final NumberFormatException e1) {
                        // obsolete format has the actual primer sequence in here
                        cb.setFwdOverlapLen(p.getValue().length());
                    }
                }
            }

        }

    }

    static float getGCContent(final String sequence) {
        if (sequence == null) {
            return 0;
        }
        try {
            return new DnaSequence(sequence).getGCContent();
        } catch (final IllegalArgumentException e) {
            return 0;
        }
    }

    public static Collection<Experiment> getNonCrystallizationExperiments(final ResearchObjective ro) {
        final String selectHQL =
            " select distinct experiment from Experiment as experiment "
                + " left join fetch experiment.milestones left join fetch experiment.inputSamples "
                + " where experiment.researchObjective=:ro "
                + " and ( experiment.experimentType.name!='Trials' or experiment.experimentGroup is null)";

        final ReadableVersion rv = ro.get_Version();
        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL, Experiment.class);
        query.setEntity("ro", ro);

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Collection results = query.list();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
        return results;
    }

    public static boolean isCharacterisationExperiment(final Experiment experiment) {
        final ExperimentType type = experiment.getExperimentType();
        return ConstructBeanReader.characterisations.contains(type.getName());
    }

    public static final Collection<String> characterisations = new HashSet<String>();
    static {
        ConstructBeanReader.characterisations.add("Information");
        ConstructBeanReader.characterisations.add("Mass spec");
        ConstructBeanReader.characterisations.add("Characterisation");
    }

    public static final Collection<String> constructdesigns = new HashSet<String>();
    static {
        ConstructBeanReader.constructdesigns.add("Optic Construct Design");
        ConstructBeanReader.constructdesigns.add("SPOT Construct Design");
        ConstructBeanReader.constructdesigns.add("SPOTConstructDesign");
        ConstructBeanReader.constructdesigns.add(FormFieldsNames.spotcloningDesign);
    }

    public static boolean isConstructDesignExperiment(final Experiment experiment) {
        final ExperimentType type = experiment.getExperimentType();
        return ConstructBeanReader.constructdesigns.contains(type.getName());
    }

}
