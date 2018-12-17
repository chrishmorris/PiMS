// ----------------------------------------------------------------------------------------------
// SPOTTarget.java SPOT target bean
// Created by Johan van Niekerk SSPF-Dundee
//
// 23 March 2006
// ----------------------------------------------------------------------------------------------

package org.pimslims.presentation.construct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanWriter;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.experiment.DefaultExperimentName;
import org.pimslims.presentation.experiment.ExperimentNameFactory;
import org.pimslims.properties.PropertyGetter;

/**
 * Factory class to populate the fields of a Presentation layer ConstructBean
 * 
 * @author Johan van Niekerk
 */
public class ConstructBeanWriter {

    /**
     * TEMPLATE String
     */
    private static final String TEMPLATE = "Template";

    //TODO this will cause errors when two constructs made at once
    private static String PrimerDesignProtocolHook = null;

    /**
     * Creates an ExperimentBlueprint representing a construct from fields in ConstructBean
     * 
     * @param wv PiMS Writeable version
     * @param cb org.pimslims.presentation.construct.ConstructBean
     * @return org.pimslims.model.target.ResearchObjective
     * @throws ServletException when unable to find a Target for the Construct
     * @throws ClassNotFoundException
     * @throws ConstraintException when datamodel constraint is violoated
     * @throws AccessException when caller does not have access permission
     * @throws SQLException
     */
    public static ResearchObjective createNewConstruct(final WritableVersion wv, final ConstructBean cb)
        throws ConstraintException, AccessException {
        cb.suggestPrimerNames();
        // find the project
        final Collection<TargetBean> targetBeans = cb.getTargetBeans();
        for (final Iterator iterator = targetBeans.iterator(); iterator.hasNext();) {
            final TargetBean targetBean = (TargetBean) iterator.next();
            if (null == targetBean.getHook()) {
                continue;
            }
            final Target t = (Target) wv.get(targetBean.getHook());
            wv.setDefaultOwner(t.get_Owner());
        }

        // Create the ExpBlueprint
        LabNotebook project = null;
        if (null != cb.getAccess()) {
            project = wv.get(cb.getAccess().getHook());
        }

        final Map<String, Object> m = new HashMap<String, Object>();
        if (null != project) {
            m.put(LabBookEntry.PROP_ACCESS, project);
        }
        m.put(ResearchObjective.PROP_COMMONNAME, cb.getName());
        m.put(ResearchObjective.PROP_WHYCHOSEN, "Not specified");
        //For synthetic gene
        if (null != cb.getSgSampleHook() && "" != cb.getSgSampleHook()) {
            m.put(ResearchObjective.PROP_SIMILARITYDETAILS, "Synthetic gene ::" + cb.getSgSampleName() + "::"
                + cb.getSgSampleHook());
        }
        final ResearchObjective ro = wv.create(org.pimslims.model.target.ResearchObjective.class, m);

        // Add the various sequences via a ResearchObjectiveElement
        for (final Iterator iterator = targetBeans.iterator(); iterator.hasNext();) {
            final TargetBean targetBean = (TargetBean) iterator.next();
            Target t = null;
            if (null != targetBean.getHook()) {
                t = (Target) wv.get(targetBean.getHook());
            }
            ConstructBeanWriter.createResearchObjectiveElement(wv, cb, ro, t);

        }

        // Add the bioinformatics data via an Experiment
        ConstructBeanWriter.createPrimerDesignExperiment(wv, cb, ro);

        ro.setFunctionDescription(cb.getDescription());
        ro.setDetails(cb.getComments());

        if (null != cb.getUserHook()) {
            final User scientist = wv.get(cb.getUserHook());
            ro.setOwner(scientist);
        }
        return ro;
    }

    /**
     * @param wv PiMS Writeable version
     * @param cb org.pimslims.presentation.construct.ConstructBean
     * @param eb org.pimslims.model.target.ResearchObjective
     * @param t org.pimslims.model.target.Target
     * @throws AccessException
     * @throws ConstraintException
     */
    static void createResearchObjectiveElement(final WritableVersion wv, final ConstructBean cb,
        final ResearchObjective eb, final Target t) throws AccessException, ConstraintException {

        ResearchObjectiveElement bpc = null;

        // Create the ResearchObjectiveElement representing the target
        final Map<String, Object> m = new HashMap<String, Object>();
        m.put(ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, eb);
        m.put("componentType", ConstructUtility.SPOTCONSTRUCT);
        m.put("whyChosen", "Not specified");
        if (!PIMSTarget.isDNATarget(t)) {
            m.put("approxBeginSeqId", cb.getTargetProtStart());
            m.put("approxEndSeqId", cb.getTargetProtEnd());
        }
        //DNATarget does not have protein sequence in construct
        else {
            m.put("approxBeginSeqId", new Integer(cb.getTargetDnaStart().intValue()));
            m.put("approxEndSeqId", new Integer(cb.getTargetDnaEnd().intValue()));
        }
        if (null != t) {
            m.put("target", t);
        }
        m.put(ResearchObjectiveElement.PROP_MOLECULE,
            ConstructBeanWriter.createMolComp(wv, ConstructBeanWriter.TEMPLATE, cb.getDnaSeq(), "DNA", cb));
        bpc = wv.create(ResearchObjectiveElement.class, m);

        ConstructBeanWriter.addSequences(wv, cb, bpc);

    }

    /**
     * ConstructBeanWriter.addSequences records construct design
     * 
     * @param version the current transaction
     * @param cb the ContructBean
     * @param target the Target
     * @param roe the element representing the target sequence
     * @throws ConstraintException
     * @throws AccessException
     */
    public static void addSequences(final WritableVersion version, final ConstructBean cb,
        final ResearchObjectiveElement roe) throws ConstraintException, AccessException {
        //only create these if Target is not a DNA Target
        if (!PIMSTarget.isDNATarget(roe.getTarget())) {
            // Create and apply the Molecule representing dnaSeq (proteinSeq)
            roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, ConstructBeanForList.PROTEIN,
                cb.getProtSeq(), "protein", cb));

            // Create and apply the Molecule representing dnaSeq (expressed protein)
            roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version,
                ConstructBean.EXPRESSED_PROTEIN, cb.getExpressedProt(), "protein", cb));

            // Create and apply the Molecule representing dnaSeq (final protein)
            roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, ConstructBean.FINAL_PROTEIN,
                cb.getFinalProt(), "protein", cb));
        }

        // Create PCR Product
        roe.addTrialMolComponent(ConstructBeanWriter.createMolComp(version, CaliperFile.PCR_PRODUCT,
            cb.getPcrProductSeq(), "DNA", cb));
    }

    /**
     * @param wv org.pimslims.dao.WritableVersion
     * @param category String name of org.pimslims.model.reference.ComponentCategory.name
     * @param seq String sequence for org.pimslims.model.molecule.Molecule
     * @param molType String molecule type for org.pimslims.model.molecule.Molecule
     * @param cb org.pimslims.model.target.ResearchObjective
     * @return org.pimslims.model.molecule.Molecule
     * @throws AccessException
     * @throws ConstraintException
     */
    public static Molecule createMolComp(final WritableVersion wv, final String category, final String seq,
        final String molType, final ModelObjectShortBean cb) throws AccessException, ConstraintException {
        return ConstructBeanWriter.createMolComp(wv, category, seq, molType, cb.getName());
    }

    public static Molecule createMolComp(final WritableVersion wv, final String category, final String seq,
        final String molType, final String cbName) throws AccessException, ConstraintException {

        final String categoryName = ConstructBeanWriter.spotPimsMapping.get(category);

        // Create a MolComponent
        final Map<String, Object> m = new HashMap<String, Object>();
        final String name = cbName + " " + categoryName;
        m.put(Substance.PROP_NAME, name);
        m.put(Molecule.PROP_MOLTYPE, molType);
        m.put(Molecule.PROP_SEQUENCE, seq);

        Molecule molComponent = wv.findFirst(Molecule.class, Substance.PROP_NAME, name);
        if (molComponent == null) {
            molComponent = wv.create(Molecule.class, m);
        } else {
            molComponent.setMolType(molType);
            molComponent.setSequence(seq);
        }
        final ComponentCategory componentCategory =
            wv.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, categoryName);
        if (null != componentCategory) {
            molComponent.addCategory(componentCategory);
        }

        return molComponent;
    }

    public static void createPrimerDesignExperiment(final WritableVersion wv, final ConstructBean cb,
        final ResearchObjective eb) throws AccessException, ConstraintException {

        Protocol protocol = null;
        if (cb.getSdmConstruct()) {
            protocol = ConstructBeanWriter.getSDMPrimerDesignProtocol(wv);
        } else {
            protocol = ConstructBeanWriter.getPrimerDesignProtocol(wv);
        }
        //TODO use new protocol for primerless constructs

        final Date d = cb.getDateOfEntry();
        if (null == d) {
            throw new IllegalArgumentException("dateOfEntry must not be null!");
        }

        // Create the Experiment that represents the primer design
        final Experiment primerDesignExperiment =
            PrimerBeanWriter.createExperiment(wv, wv.getCurrentDefaultOwner(),
                "Primer Design " + System.currentTimeMillis(), ExperimentUtility.SPOTCONSTRUCT_DESIGN);
        primerDesignExperiment.setProject(eb);
        primerDesignExperiment.setProtocol(protocol);
        final ExperimentNameFactory enf =
            PropertyGetter.getInstance("Experiment.Name.Factory", DefaultExperimentName.class);
        primerDesignExperiment.setName(enf.suggestExperimentName(wv, primerDesignExperiment, null));

        // add parameters and outputSamples
        //was ConstructBeanWriter.createPrimerDesignParameters(wv, primerDesignExperiment, protocol, cb);
        ConstructBeanWriter.createPrimerDesignOutputSamples(wv, primerDesignExperiment, protocol, cb);

        // add input samples
        // was if (cb.isPrimerless()), now for all constructs for Albert Einstein college
        final InputSample is = new InputSample(wv, primerDesignExperiment);
        is.setName("Vector");
        //TODO set RefInputSample from protocol
        is.setRefInputSample((RefInputSample) protocol.findFirst(Protocol.PROP_REFINPUTSAMPLES,
            RefInputSample.PROP_NAME, "Vector"));
        assert null == cb.getVectorInputSampleHook();
        cb.setVectorInputSampleHook(is.get_Hook());
        //}
    }

    //TODO should this actually be a PCR protocol?
    protected static Protocol getPrimerDesignProtocol(final ReadableVersion version) {

        Protocol protocol = null;
        if (ConstructBeanWriter.PrimerDesignProtocolHook != null) {
            protocol = version.get(ConstructBeanWriter.PrimerDesignProtocolHook);
        }

        if (protocol != null) {
            if (protocol.getName().equalsIgnoreCase(ExperimentUtility.SPOTCONSTRUCT_DESIGN_PROTOCOL)
                || protocol.getName().equalsIgnoreCase(ExperimentUtility.OPPFCONSTRUCT_DESIGN_PROTOCOL)) {
                return protocol;
            }
        }

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", ExperimentUtility.SPOTCONSTRUCT_DESIGN_PROTOCOL);
        protocol = version.findFirst(Protocol.class, m);
        if (null == protocol) {
            m.put("name", ExperimentUtility.SPOTCONSTRUCT_DESIGN_OLD);
            protocol = version.findFirst(Protocol.class, m);
        }

        assert null != protocol : "Could not find SPOT Construct Primer Design protocol";
        ConstructBeanWriter.PrimerDesignProtocolHook = protocol.get_Hook();
        return protocol;
    }

    //TODO make this the QuikChange protocol
    protected static Protocol getSDMPrimerDesignProtocol(final ReadableVersion version) {

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", ExperimentUtility.SDMCONSTRUCT_DESIGN_PROTOCOL);
        final Protocol protocol = version.findFirst(Protocol.class, m);

        assert null != protocol : "Could not find SPOT SDM Construct Primer Design protocol";
        return protocol;
    }

    @Deprecated
    // this is the old way to save details
    private static void createPrimerDesignParameters(final WritableVersion wv,
        final Experiment primerDesignExperiment, final Protocol protocol, final ConstructBean cb)
        throws AccessException, ConstraintException {

        final Map<String, ModelObject> parameterDefinitions =
            ConstructBeanWriter.findOrCreatePrimerDesignParameterDefinitions(wv, protocol, cb);
        if (!cb.getSdmConstruct()) {
            if (0 != cb.getPrimers().size()) {
                ConstructBeanWriter.createParameter(wv, primerDesignExperiment, parameterDefinitions,
                    cb.getFwdPrimer(), ConstructBeanWriter.FPRIMER);
                ConstructBeanWriter.createParameter(wv, primerDesignExperiment, parameterDefinitions,
                    cb.getRevPrimer(), ConstructBeanWriter.RPRIMER);
            }
        }
    }

    private static void createParameter(final WritableVersion wv, final Experiment primerDesignExperiment,
        final Map<String, ModelObject> parameterDefinitions, final Object value, final String pdName)
        throws AccessException, ConstraintException {

        if (parameterDefinitions.containsKey(pdName) && value != null) {
            final ParameterDefinition parameterDefinition =
                (ParameterDefinition) parameterDefinitions.get(pdName);
            final Map<String, Object> m = new HashMap<String, Object>();
            m.put(Parameter.PROP_EXPERIMENT, primerDesignExperiment);
            m.put(Parameter.PROP_PARAMETERDEFINITION, parameterDefinition);
            m.put(Parameter.PROP_PARAMTYPE, parameterDefinition.getParamType());
            m.put(Parameter.PROP_VALUE, value.toString());
            m.put(Parameter.PROP_NAME, pdName);
            wv.create(Parameter.class, m);
        }
    }

    static Map<String, ModelObject> findOrCreatePrimerDesignParameterDefinitions(final WritableVersion wv,
        final Protocol primerDesignProtocol, final ModelObjectShortBean cb) throws AccessException,
        ConstraintException {

        final Map<String, ModelObject> parameterDefinitions = new HashMap<String, ModelObject>();

        final Collection<ParameterDefinition> c = primerDesignProtocol.getParameterDefinitions();

        // Find relevant ParameterDefintions

        for (final ParameterDefinition pd : c) {

            // Get the name from its ParameterDefinition
            final String name = pd.getName();
            parameterDefinitions.put(name, pd);
        }

        return parameterDefinitions;
    }

    private static void createPrimerDesignOutputSamples(final WritableVersion version,
        final Experiment experiment, final Protocol protocol, final ConstructBean cb) throws AccessException,
        ConstraintException {

        for (final Iterator iterator = cb.getPrimers().iterator(); iterator.hasNext();) {
            final PrimerBean bean = (PrimerBean) iterator.next();
            assert null != bean.getDirection();
            assert null != bean.getName() && !"".equals(bean.getName());
            final RefOutputSample refOutputSample =
                (RefOutputSample) protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES,
                    RefOutputSample.PROP_NAME, bean.getFormFieldsDirection());
            ConstructBeanWriter.createPrimerOutputSample(version, experiment, refOutputSample, bean);
        }

        if (null != protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES, RefOutputSample.PROP_NAME,
            ConstructBeanWriter.TEMPLATE)) {
            ConstructBeanWriter.createTemplateOutputSample(version, cb, experiment,
                (RefOutputSample) protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES,
                    RefOutputSample.PROP_NAME, ConstructBeanWriter.TEMPLATE));
        }

    }

    private static OutputSample createPrimerOutputSample(final WritableVersion version,
        final Experiment experiment, final RefOutputSample ros, final PrimerBean pb) throws AccessException,
        ConstraintException {

        final Sample sample = PrimerBeanWriter.writePrimer(version, pb, experiment);
        final OutputSample os = sample.getOutputSample();
        os.setRefOutputSample(ros);
        return os;
    }

    private static OutputSample createTemplateOutputSample(final WritableVersion version,
        final ConstructBean cb, final Experiment experiment, final RefOutputSample ros)
        throws AccessException, ConstraintException {

        final HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(AbstractSample.PROP_NAME, cb.getTargetName() + "_Template");
        attributes.put(AbstractSample.PROP_SAMPLECATEGORIES, ros.getSampleCategory());
        attributes.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        Sample sample = version.findFirst(Sample.class, attributes);
        if (null == sample) {
            sample = version.create(org.pimslims.model.sample.Sample.class, attributes);
        }
        for (final ResearchObjectiveElement blueprintComponent : ((ResearchObjective) experiment.getProject())
            .getResearchObjectiveElements()) {
            attributes.clear();
            final Molecule molecule = blueprintComponent.getMolecule();
            attributes.put(SampleComponent.PROP_REFCOMPONENT, molecule);
            attributes.put(SampleComponent.PROP_RESEARCHOBJECTIVEELEMENT, blueprintComponent);
            attributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            if (null != molecule) {
                version.create(org.pimslims.model.sample.SampleComponent.class, attributes);
            }
        }

        attributes.clear();
        attributes.put(OutputSample.PROP_SAMPLE, sample);
        attributes.put(OutputSample.PROP_EXPERIMENT, experiment);
        attributes.put(OutputSample.PROP_NAME, cb.getName() + "T");
        attributes.put(OutputSample.PROP_REFOUTPUTSAMPLE, ros);

        return version.create(org.pimslims.model.experiment.OutputSample.class, attributes);
    }

    /**
     * ConstructBeanWriter.getConstructParts
     * 
     * @param eb the ResearchObjective representing the construct
     * @return a list of the lab book pages that make it up, sorted so that it is permitted to delete them in
     *         order
     */
    public static List<ModelObject> getConstructParts(final ResearchObjective eb) {
        final Set<ModelObject> deleteFirst = new HashSet();
        // can't delete the molecules while there are samples linked to them
        final Set<Substance> deleteLast = new HashSet();

        // process the construct design experiments
        for (final Iterator iterator = eb.getExperiments().iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            ConstructBeanWriter.getExperimentParts(deleteFirst, deleteLast, experiment);
        }

        ConstructBeanWriter.getRoParts(eb, deleteLast);
        final List<ModelObject> toDelete = new ArrayList<ModelObject>(17);
        toDelete.add(eb);
        toDelete.addAll(deleteFirst);
        toDelete.addAll(deleteLast);
        return toDelete;
    }

    private static void getRoParts(final Project ro, final Set<Substance> deleteLast) {
        // process the elements
        for (final Iterator iterator = ((ResearchObjective) ro).getResearchObjectiveElements().iterator(); iterator
            .hasNext();) {
            final ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            deleteLast.addAll(element.getTrialMolComponents());
            deleteLast.addAll(element.getTrialMolecules());
            final Molecule molecule = element.getMolecule();
            if (null != molecule) {
                deleteLast.add(molecule);
            }
        }
    }

    private static void getExperimentParts(final Set<ModelObject> deleteFirst,
        final Set<Substance> deleteLast, final Experiment experiment) {
        final String typeName = experiment.getExperimentType().getName();
        if (!ExperimentUtility.SPOTCONSTRUCT_DESIGN.equals(typeName)
            && !ExperimentUtility.SPOTCONSTRUCT_DESIGN_OLD.equals(typeName)) {
            // don't delete these
        } else {
            deleteFirst.add(experiment);
            for (final Iterator iterator2 = experiment.getOutputSamples().iterator(); iterator2.hasNext();) {
                final OutputSample os = (OutputSample) iterator2.next();
                final Sample sample = os.getSample();
                if (null != sample) {
                    deleteFirst.add(sample); // add these before the components
                    for (final Iterator iterator3 = sample.getSampleComponents().iterator(); iterator3
                        .hasNext();) {
                        final SampleComponent sc = (SampleComponent) iterator3.next();
                        final Substance rc = sc.getRefComponent(); // e.g primer
                        deleteLast.add(rc); // add these after the samples
                    }
                }
            }
        }
    }

    /**
     * ConstructBeanWriter.getConstructParts
     * 
     * @param exp
     * @return
     */
    @Deprecated
    // tested but not used
    public static List<ModelObject> getConstructParts(final Experiment exp) {
        final Set<ModelObject> deleteFirst = new HashSet();
        // can't delete the molecules while there are samples linked to them
        final Set<Substance> deleteLast = new HashSet();
        ConstructBeanWriter.getExperimentParts(deleteFirst, deleteLast, exp);

        final List<ModelObject> toDelete = new ArrayList<ModelObject>(17);
        final Project ro = exp.getProject();
        if (null != ro) {
            toDelete.add((ModelObject) ro);
            ConstructBeanWriter.getRoParts(ro, deleteLast);
        }
        toDelete.addAll(deleteFirst);
        toDelete.addAll(deleteLast);
        return toDelete;
    }

    @Deprecated
    // obsolete
    private static final HashMap<String, String> spotPimsMapping = new HashMap<String, String>();

    static {

        ConstructBeanWriter.spotPimsMapping.put(ConstructBeanForList.PROTEIN, ConstructBeanForList.PROTEIN);
        ConstructBeanWriter.spotPimsMapping.put(ConstructBeanWriter.TEMPLATE, ConstructBeanWriter.TEMPLATE);
        ConstructBeanWriter.spotPimsMapping.put(ConstructBean.EXPRESSED_PROTEIN,
            ConstructBean.EXPRESSED_PROTEIN);
        ConstructBeanWriter.spotPimsMapping.put(ConstructBean.FINAL_PROTEIN, ConstructBean.FINAL_PROTEIN);
        ConstructBeanWriter.spotPimsMapping.put(CaliperFile.PCR_PRODUCT, CaliperFile.PCR_PRODUCT);
        ConstructBeanWriter.spotPimsMapping.put("dna", "Nucleic acid");
        ConstructBeanWriter.spotPimsMapping.put("protein", ConstructBeanForList.PROTEIN); //obsolete
    }

    public static final String RPRIMER = "Reverse Primer";

    public static final String FPRIMER = "Forward Primer"; // input sample name
}
