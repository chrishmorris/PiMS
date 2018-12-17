/**
 * pims-web syntheticGene SyntheticGeneManager.java
 * 
 * @author susy
 * @date 20 Oct 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.experiment.ExperimentWriter;

/**
 * SyntheticGeneManager To record a Synthetic Gene based on a Target Records an Experiment with teh Output
 * Sample to represent the Synthetic gene 2 molecules as Sample components for the DNA and (optional) protein
 * sequence A ResearchObjectiveElement
 * 
 */
public class SyntheticGeneManager {

    public static Sample createSyntheticGene(final WritableVersion version, final String sampleName,
        final Target target, final String dnaSeq, final String protSeq) throws ConstraintException,
        AccessException {
        Sample sgSample = null;
        final String name = sampleName;
        final String sgName = "Synthetic gene"; //also used to find Component category and for ExpType 
        final String protocolName = "PiMS Synthetic Gene";
        final String sgSeq = dnaSeq.toUpperCase();
        final String pSeq = protSeq;
        final Target sgTarget = target;

        //Need to get the dbId for the correct Protocol
        final Experiment experiment =
            SyntheticGeneManager.makeSGeneExperiment(version, name, sgName, protocolName);

        sgSample = SyntheticGeneManager.findSgOutputSample(sgSample, protocolName, experiment);

        //Synthetic gene needs a DNA sequence as Sample Component
        final Molecule dnaMol = SyntheticGeneManager.createSGeneMolecule(version, name, sgName, sgSeq, "DNA");
        final Molecule protMol =
            SyntheticGeneManager.createSGeneMolecule(version, name, sgName, pSeq, "protein");

        SampleFactory.LinkSampleWithMolComponent(version, sgSample, dnaMol);
        SampleFactory.LinkSampleWithMolComponent(version, sgSample, protMol);

        //Link experiment to Target
        SyntheticGeneManager.linkResObjToExperiment(sgTarget, experiment, dnaMol);

        //Make ResearchObjectiveElement
        final Project sgRo = experiment.getProject();
        final ResearchObjectiveElement sgRoe =
            SyntheticGeneManager.makeSgeneResObjEle(version, (ResearchObjective) sgRo, dnaMol);

        //Link Protein molecule to ResearchObjectiveElenment
        sgRoe.addTrialMolComponent(protMol);
        return sgSample;
    }

    /**
     * SyntheticGeneManager.createSGeneMolecule
     * 
     * @param version
     * @param name
     * @param sgName
     * @param sgSeq
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    private static Molecule createSGeneMolecule(final WritableVersion version, final String name,
        final String sgName, final String sgSeq, final String molType) throws AccessException,
        ConstraintException {
        final Map<String, Object> molAtts = new HashMap<String, Object>();
        final String sequence = sgSeq;
        final String mtype = molType;
        final String sname = sgName;

        molAtts.put(Molecule.PROP_SEQUENCE, sequence);
        molAtts.put(Molecule.PROP_MOLTYPE, mtype);
        if (mtype.equals("DNA")) {
            molAtts.put(Substance.PROP_NAME, name + " " + sname);
        } else {
            molAtts.put(Substance.PROP_NAME, name + " " + mtype);
        }
        final Molecule dnaMol = SyntheticGeneManager.setCompCatforSgene(version, molAtts, sname);
        return dnaMol;
    }

    /**
     * SyntheticGeneManager.setCompCatforSgene
     * 
     * @param version
     * @param molAtts
     * @param sname
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static Molecule setCompCatforSgene(final WritableVersion version,
        final Map<String, Object> molAtts, final String sname) throws AccessException, ConstraintException {
        final Molecule dnaMol = version.create(Molecule.class, molAtts);
        final ComponentCategory componentCategory =
            version.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, sname);
        if (null == componentCategory) {
            throw new IllegalStateException("Component category not found: " + componentCategory);
        } else {
            dnaMol.addCategory(componentCategory);
        }
        return dnaMol;
    }

    /**
     * SyntheticGeneManager.linkResObjToExperiment
     * 
     * @param target
     * @param experiment
     * @throws ConstraintException
     * @throws AssertionError
     */
    public static void linkResObjToExperiment(final Target target, final Experiment experiment,
        final Molecule molecule) throws ConstraintException, AssertionError {
        //String resObName;
        final Target sgTarget = target;
        final Set<ResearchObjectiveElement> roes = sgTarget.getResearchObjectiveElements();
        if (roes.size() > 0) {
            for (final ResearchObjectiveElement roe : roes) {
                if (roe.getComponentType().equals("target")) {
                    final ResearchObjective sgRo = roe.getResearchObjective();
                    //resObName = sgRo.getCommonName(); //??What is this for??
                    experiment.setProject(sgRo);
                }
            }
        } else {
            throw new AssertionError("No ResearchObjectiveElements for the target");
        }
    }

    /**
     * SyntheticGeneManager.findSgOutputSample
     * 
     * @param sgSample
     * @param protocolName
     * @param experiment
     * @return
     */
    public static Sample findSgOutputSample(Sample sgSample, final String protocolName,
        final Experiment experiment) {
        final Set<OutputSample> oss = experiment.getOutputSamples();
        if (oss.size() > 0) {
            for (final OutputSample os : oss) {
                if (null != os.getSample()) {
                    sgSample = os.getSample();
                }
            }
        } else {
            throw new IllegalStateException(protocolName + " should have 1 output sample");
        }
        return sgSample;
    }

    /**
     * SyntheticGeneManager.makeSGeneExperiment
     * 
     * @param version
     * @param sampleName
     * @param expType
     * @param protocolName
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static Experiment makeSGeneExperiment(final WritableVersion version, final String sampleName,
        final String expType, final String protocolName) throws AccessException, ConstraintException {
        final java.util.Set<Protocol> protocolList = new java.util.HashSet<Protocol>();

        //Make a map for a new Experiment
        final Map<String, Object> expAttrMap = SyntheticGeneManager.makeExpMap(version, expType);

        final Protocol protocol = version.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        if (null == protocol) {
            throw new IllegalArgumentException("Protocol not found: " + protocolName);
        } else {
            // Add the Protocol to the list
            protocolList.add(protocol);

            expAttrMap.put(Experiment.PROP_PROTOCOL, protocolList);

            //Experiment needs a name
            expAttrMap.put(Experiment.PROP_NAME, sampleName);
            //Experiment needs end date
            expAttrMap.put(Experiment.PROP_STARTDATE, java.util.Calendar.getInstance());
            expAttrMap.put(Experiment.PROP_ENDDATE, java.util.Calendar.getInstance()); // LATER
        }

        final Experiment experiment = version.create(Experiment.class, expAttrMap);
        ExperimentWriter.createOutputSamplesForExperiment(version, experiment, protocol);
        ExperimentFactory.createProtocolParametersForExperiment(version, protocol, experiment);
        HolderFactory.createInputSamplesForExperiment(version, experiment, protocol);
        return experiment;
    }

    /**
     * 
     * SyntheticGeneManager.makeExpMap Prepares a Map to search for Experiments of a type
     * 
     * @param version
     * @return Map HashMap to find Experiments
     */
    public static Map<String, Object> makeExpMap(final ReadableVersion version, final String expTypeName) {
        final String typeOfExp = expTypeName;
        final Map<String, Object> expAttrMap = new java.util.HashMap<String, Object>();

        //Need to get the dbId for the correct ExperimentType
        final java.util.Set<ExperimentType> expTypeList = new java.util.HashSet<ExperimentType>();

        // code to see if the ExperimentType is in the database
        // retrieves ExperimentType with name  matching PiMS Synthetic Gene = value in expTypeAttrMap
        SyntheticGeneManager.setSGeneExpType(version, typeOfExp, expAttrMap, expTypeList);
        return expAttrMap;
    }

    /**
     * SyntheticGeneManager.setSGeneExpType
     * 
     * @param version
     * @param typeOfExp
     * @param expAttrMap
     * @param expTypeList
     */
    private static void setSGeneExpType(final ReadableVersion version, final String typeOfExp,
        final Map<String, Object> expAttrMap, final java.util.Set<ExperimentType> expTypeList) {
        final ExperimentType expType =
            version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, typeOfExp);
        if (null == expType) {
            throw new IllegalStateException("Experiment type not found: " + typeOfExp);
        } else {
            // Add the ExperimentType to the list
            expTypeList.add(expType);
            expAttrMap.put(Experiment.PROP_EXPERIMENTTYPE, expTypeList);
        }
    }

    /**
     * SyntheticGeneManager.makeSgeneResObj. Makes a new ResearchObjectiveElement for the Synthetic gene
     */
    public static ResearchObjectiveElement makeSgeneResObjEle(final WritableVersion version,
        final ResearchObjective resOb, final Molecule mol) throws AccessException, ConstraintException {
        ResearchObjectiveElement resObjEle = null;
        final ResearchObjective resObj = resOb;
        final LabNotebook access = resObj.getAccess();

        // Create the ResearchObjectiveElement representing the Synthetic Gene
        final Map<String, Object> resObEleMap = new HashMap<String, Object>();
        if (null != access) {
            resObEleMap.put(LabBookEntry.PROP_ACCESS, access);
        }
        resObEleMap.put(ResearchObjectiveElement.PROP_RESEARCHOBJECTIVE, resOb);
        resObEleMap.put("componentType", "Synthetic gene");
        resObEleMap.put("whyChosen", "Synthetic gene");

        resObEleMap.put(ResearchObjectiveElement.PROP_MOLECULE, mol);
        resObjEle = version.create(ResearchObjectiveElement.class, resObEleMap);

        return resObjEle;
    }

    /**
     * SyntheticGeneManager.save. Makes a new Synthetic gene sample from a SyntheticGeneBean
     */

    public static String save(final WritableVersion version, final SyntheticGeneBean bean)
        throws ConstraintException, AccessException {
        String sgHook = "";
        if (null != bean.getTargetBean().getHook()) {
            final Target target = version.get(bean.getTargetBean().getHook());

            if (null != bean.getSgeneName() && !"".equals(bean.getSgeneName()) && null != bean.getDnaSeq()
                && !"".equals(bean.getDnaSeq())) {
                final Sample sgSample =
                    SyntheticGeneManager.createSyntheticGene(version, bean.getSgeneName(), target,
                        bean.getDnaSeq(), bean.getProteinSeq());
                sgHook = sgSample.get_Hook();
                //System.out.println("The hook is: " + sgHook);
            } else {
                System.out.println("Name or DNA sequence have not been set");
            }
        } else {
            System.out.println("No hook");
        }
        return sgHook;
    }

    /*
     * SyntheticGenemanager.getSyntheticGeneParts
     * 
     * @param sample the Sample representing the Synthetic gene 
     * @return a list of the lab book pages that make it up, sorted so that it is permitted to delete them in
     *         order
     */
    public static List<ModelObject> getSyntheticGeneParts(final Sample sample) {
        final Set<ModelObject> deleteFirst = new HashSet();
        // can't delete the molecules while there are samples linked to them
        final Set<Substance> deleteLast = new HashSet();
        String roeHook = "";
        // process the SyntheticGene Experiment, use`ConstructBeanWriter methods
        final Experiment experiment = sample.getOutputSample().getExperiment();
        deleteFirst.add(experiment);
        for (final Iterator iterator = experiment.getOutputSamples().iterator(); iterator.hasNext();) {
            final OutputSample os = (OutputSample) iterator.next();
            final Sample expsample = os.getSample();
            if (null != expsample) {
                deleteFirst.add(expsample); // add these before the components
                for (final Iterator iterator2 = expsample.getSampleComponents().iterator(); iterator2
                    .hasNext();) {
                    final SampleComponent sc = (SampleComponent) iterator2.next();
                    final Molecule rc = (Molecule) sc.getRefComponent(); // e.g DNA and protein molecules
                    if (rc.getMolType().equals("DNA")) {
                        final Collection<ResearchObjectiveElement> resoes = rc.getResearchObjectiveElements();
                        for (final ResearchObjectiveElement resoe : resoes) {
                            if (resoe.getComponentType().equals("Synthetic gene")) {
                                roeHook = resoe.get_Hook();
                                //System.out.println("resoeHook is: " + roeHook);
                            }
                        }
                    }
                    deleteLast.add(rc); // add these after the samples
                }
            }
        }
        final List<ModelObject> toDelete = new ArrayList<ModelObject>(5);
        final ResearchObjective ro = (ResearchObjective) experiment.getProject();
        if (null != ro) {
            final Set<ResearchObjectiveElement> roes = ro.getResearchObjectiveElements();
            if (roes.size() > 0) {
                for (final ResearchObjectiveElement roe : roes) {
                    if (roe.getComponentType().equals("Synthetic gene") && roe.get_Hook().equals(roeHook)) {
                        //System.out.println("roeHook is: " + roe.get_Hook());
                        toDelete.add(roe);
                        deleteLast.addAll(roe.getTrialMolComponents());
                        deleteLast.addAll(roe.getTrialMolecules());
                        final Molecule molecule = roe.getMolecule();
                        if (null != molecule) {
                            deleteLast.add(molecule);
                        }
                        //final ResearchObjective sgRo = roe.getResearchObjective();
                    }
                }
            }
        }
        toDelete.addAll(deleteFirst);
        toDelete.addAll(deleteLast);
        return toDelete;
    }

    /*
     * SyntheticGenemanager.isSynthGene
     * @param sample the Sample representing the Synthetic gene 
     * @return a Boolean true if sample is a Synthetic gene sample
     */
    public static Boolean isSynthGene(final Sample sample) {
        Boolean sgSample = false;
        final Set<SampleCategory> samCats = sample.getSampleCategories();
        for (final SampleCategory samCat : samCats) {
            if (samCat.get_Name().equals("Synthetic gene")) {
                sgSample = true;
                return sgSample;
            }
        }
        return sgSample;
    }

    /**
     * Find all of the Synthetic gene experiments If the ResearchObjective.commonName = target.name get the
     * output sample an make a bean
     * 
     * @param target Target linked to the synthetic gene org.pimslims.model.target.Target
     * @param version org.pimslims.dao.ReadableVersion
     * @return ArrayList of SyntheticGeneBeans
     */
    public static Collection<SyntheticGeneBean> makeSGBeansForTarget(final ReadableVersion version,
        final Target target) {
        final List<SyntheticGeneBean> sgBeans = new ArrayList<SyntheticGeneBean>();
        //final Target sgTarget = target;
        final String targetName = target.getName();
        final Map<String, Object> expMap = SyntheticGeneManager.makeExpMap(version, "Synthetic gene");
        final Collection<Experiment> sgExpts = version.findAll(Experiment.class, expMap);
        if (sgExpts.size() > 0) {
            for (final Experiment exp : sgExpts) {
                if (((ResearchObjective) exp.getProject()).getCommonName().equals(targetName)) {
                    final Set<OutputSample> oss = exp.getOutputSamples();
                    if (oss.size() > 0) {
                        for (final OutputSample os : oss) {
                            if (null != os.getSample()) {
                                final Sample sgSample = os.getSample();
                                final SyntheticGeneBean sgb = new SyntheticGeneBean(sgSample);
                                sgBeans.add(sgb);
                            }
                        }
                    }
                }
            }
        }
        return sgBeans;
    }
}