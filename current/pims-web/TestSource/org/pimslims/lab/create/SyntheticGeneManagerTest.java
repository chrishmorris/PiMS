/**
 * pims-web syntheticGene SyntheticGeneManagerTest.java
 * 
 * @author susy
 * @date 20 Oct 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.test.POJOFactory;

/**
 * SyntheticGeneManagerTest
 * 
 */
public class SyntheticGeneManagerTest extends TestCase {

    private final AbstractModel model = ModelImpl.getModel();

    private static final String UNIQUE = "sg" + System.currentTimeMillis();

    private static final String PROTOCOL_NAME = "PiMS Synthetic Gene";

    private static final String SAMPLE_CAT = "Synthetic gene";

    //private static final String TARGET_NAME = SyntheticGeneManagerTest.UNIQUE + "targetName";

    private static final String TARGET = "target";

    private static final String DNASEQ = "GATCGACTGACTGACTGATC";

    private static final String PROTSEQ = "QWERTY";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SyntheticGeneManager.class);
    }

    /*
     * Test method for 'org.pimslims.logic.syntheticGene.SyntheticGeneManager()'
     */
    public void testSyntheticGeneManager() {

        final SyntheticGeneManager sgm = new SyntheticGeneManager();
        Assert.assertNotNull(sgm);
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#createSyntheticGene(org.pimslims.dao.WritableVersion, org.pimslims.model.target.Target, java.lang.String)}
     * .
     */
    public final void testCreateSyntheticGene() throws AccessException {
        //Target target = null;
        final WritableVersion version = this.model.getTestVersion();

        try {
            //Based on TargetFactory.java
            final Target target = POJOFactory.createTarget(version);
            final LabNotebook accObj = POJOFactory.create(version, LabNotebook.class);
            //target.setAccess(accObj);
            version.setDefaultOwner(accObj);
            //System.out.println("Target Access is: " + target.getAccess());

            target.setWhyChosen(SyntheticGeneManagerTest.TARGET);
            final ResearchObjective dummyResObj = POJOFactory.createExpBlueprint(version);
            dummyResObj.setCommonName(target.getName());
            final ResearchObjectiveElement targResObjEl = POJOFactory.createBlueprintComponent(version);
            targResObjEl.setComponentType(SyntheticGeneManagerTest.TARGET);
            targResObjEl.setWhyChosen(target.getWhyChosen());
            targResObjEl.setResearchObjective(dummyResObj);
            targResObjEl.setTarget(target);

            final String sampleName = "name" + SyntheticGeneManagerTest.UNIQUE;

            final Sample sgene =
                SyntheticGeneManager.createSyntheticGene(version, sampleName, target,
                    SyntheticGeneManagerTest.DNASEQ, SyntheticGeneManagerTest.PROTSEQ);
            final Experiment experiment = sgene.getOutputSample().getExperiment();
            final SampleCategory samCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                    SyntheticGeneManagerTest.SAMPLE_CAT);
            final Set<SampleCategory> samCats = sgene.getSampleCategories();

            //test the sample components are made
            final Set<SampleComponent> samComps = sgene.getSampleComponents();
            SampleComponent samComp = null;
            Molecule mol = null;
            Molecule protMol = null;
            ComponentCategory compCat = null;
            for (final SampleComponent comp : samComps) {

                if (comp.getName().contains(SyntheticGeneManagerTest.SAMPLE_CAT)) {
                    samComp = comp;

                    final Substance absc = samComp.getRefComponent();
                    mol = (Molecule) absc;
                    //System.out.println("Molecule is called: " + mol.get_Name());
                }
                if (comp.getName().contains("protein")) {
                    samComp = comp;

                    final Substance absc2 = samComp.getRefComponent();
                    protMol = (Molecule) absc2;
                }
            }
            final Set<ComponentCategory> compCats = mol.getCategories();
            for (final ComponentCategory cat : compCats) {
                if (cat.getName().contains(SyntheticGeneManagerTest.SAMPLE_CAT)) {
                    compCat = cat;
                }
            }

            //Check that the Synthetic Gene ResearchObjectiveElement is linked to the Target 
            final ResearchObjective expResOb = (ResearchObjective) experiment.getProject();
            //System.out.println("Experiment Access is: " + experiment.getAccess());

            //System.out.println("Target RO is called: " + expResOb.getName());

            final Set<ResearchObjectiveElement> expResObELs = expResOb.getResearchObjectiveElements();

            //Test that the Experiment is linked to the Syntheic gene ResObjElement
            Assert.assertTrue("Experiment has >0 Res Obj element", expResObELs.size() > 0);
            for (final ResearchObjectiveElement expRO : expResObELs) {
                if (!expRO.getComponentType().equals("target")) {
                    Assert.assertEquals("The Res Obj element's ComponentType is Synthetic gene",
                        SyntheticGeneManagerTest.SAMPLE_CAT, expRO.getComponentType());
                    Assert.assertEquals("ROE has a DNA sequence", expRO.getMolecule().getSequence(),
                        SyntheticGeneManagerTest.DNASEQ);
                    final Collection<Molecule> mols = expRO.getTrialMolComponents();
                    for (final Molecule molecule : mols) {
                        Assert.assertEquals("ROE has a protein molecule", molecule, protMol);
                    }
                }
            }

            //Test that the target is linked to the Synthetic gene ResObjElement
            final Set<ResearchObjectiveElement> targResObELs = target.getResearchObjectiveElements();
            for (final ResearchObjectiveElement targROE : targResObELs) {
                if (!targROE.getComponentType().equals("target")) {
                    Assert.assertEquals("The Res Obj element's ComponentType is Synthetic gene",
                        SyntheticGeneManagerTest.SAMPLE_CAT, targROE.getComponentType());
                    Assert.assertEquals("ResearchObjectiveElement has target's access", target.getAccess()
                        .getName(), targROE.getAccess().getName());
                }
            }
            Assert.assertNotNull("Synthetic Gene Sample created", sgene);
            Assert.assertEquals("The sample name is set", sampleName, sgene.getName());
            Assert.assertEquals("The experiment has the sgSample's access", sgene.getAccess().getName(),
                experiment.getAccess().getName());
            Assert.assertEquals("The Experiment name is the same as the Sample name", sampleName,
                experiment.getName());
            Assert.assertEquals("The protocol is called PiMS Synthetic Gene",
                SyntheticGeneManagerTest.PROTOCOL_NAME, experiment.getProtocol().getName());
            Assert.assertTrue("The synthetic gene sample cat is Synthetic gene", samCats.contains(samCat));
            Assert.assertEquals("The experiment is linked to the dummy ResObj for the Target",
                target.getName(), experiment.getProject().getName());
            Assert.assertTrue("Sample has a sample component", samComps.size() > 0);
            Assert.assertEquals("Sample component has DNA seqence", SyntheticGeneManagerTest.DNASEQ,
                mol.getSequence());
            Assert.assertEquals("Molecule has same access as sGene sample", sgene.getAccess().getName(), mol
                .getAccess().getName());
            Assert.assertEquals("Sample component has protein seqence", SyntheticGeneManagerTest.PROTSEQ,
                protMol.getSequence());
            Assert.assertEquals("Protein molecule has same access as sGene sample", sgene.getAccess()
                .getName(), protMol.getAccess().getName());

            Assert.assertEquals("Sample component is Synthetic gene", SyntheticGeneManagerTest.SAMPLE_CAT,
                compCat.getName());

            //Test the experiment has parameters and Vector InputSample
            if (SyntheticGeneManagerTest.PROTOCOL_NAME.equals(experiment.getProtocol().getName())) {
                final Set<Parameter> parameters = experiment.getParameters();
                Assert.assertTrue("Experiment has parameters", parameters.size() > 0);
                final List<InputSample> inputs = experiment.getInputSamples();
                for (final InputSample input : inputs) {
                    Assert.assertEquals("Experiment has a vector InputSample", "Vector", input.getName());
                }
            }

            //Test the creation of SyntheticGeneBeans for the Target
            final List<SyntheticGeneBean> sgBeans =
                (List<SyntheticGeneBean>) SyntheticGeneManager.makeSGBeansForTarget(version, target);
            Assert.assertNotNull(sgBeans);
            Assert.assertEquals("Made a bean: Target has 1 Synthetic gene experiment", 1, sgBeans.size());

            //Test bean attributes are set
            final SyntheticGeneBean sgBean = sgBeans.get(0);
            Assert.assertEquals("Bean has a name", sgene.getName(), sgBean.getName());
            Assert.assertEquals("Bean has a Lab notebook set", accObj.getName(), sgBean.getLabNotebook());
            Assert.assertEquals("Bean has an sgenen hook set", sgene.get_Hook(), sgBean.getSgeneHook());
            Assert.assertEquals("Bean has an Experiment set", experiment.getName(), sgBean.getExperiment()
                .getName());
            Assert.assertEquals("Bean has an DNA sequence set", protMol.getSequence(),
                sgBean.getProteinSeq());
            Assert.assertEquals("Bean has an protein sequence set", mol.getSequence(), sgBean.getDnaSeq());

        } catch (final ConstraintException e) {
            Assert.fail("Constraint exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#makeSGeneExperiment(org.pimslims.dao.WritableVersion, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public final void testmakeSGeneExperiment() {
        //Target target = null;
        final WritableVersion version = this.model.getTestVersion();
        final String sampleName = "name" + SyntheticGeneManagerTest.UNIQUE;
        final String expType = SyntheticGeneManagerTest.SAMPLE_CAT;
        final String protocolName = SyntheticGeneManagerTest.PROTOCOL_NAME;
        final String wrongExpType = "name" + SyntheticGeneManagerTest.UNIQUE;
        try {
            final Experiment experiment =
                SyntheticGeneManager.makeSGeneExperiment(version, sampleName, expType, protocolName);
            Assert.assertNull("No AccessObject -derived from Target", experiment.getAccess());
            //System.out.println("Access is: " + experiment.getAccess());

            SyntheticGeneManager.makeSGeneExperiment(version, sampleName, wrongExpType, protocolName);
        } catch (final AccessException e) {
            Assert.fail("Access exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail("Constraint exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final IllegalStateException e) {
            //thats fine, no action needed here
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#makeSGeneExperiment(org.pimslims.dao.WritableVersion, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public final void testNoProtocol() {
        //Target target = null;
        final WritableVersion version = this.model.getTestVersion();
        final String sampleName = "name" + SyntheticGeneManagerTest.UNIQUE;
        final String expType = SyntheticGeneManagerTest.SAMPLE_CAT;
        final String protocolName = SyntheticGeneManagerTest.PROTOCOL_NAME + SyntheticGeneManagerTest.UNIQUE;

        try {

            SyntheticGeneManager.makeSGeneExperiment(version, sampleName, expType, protocolName);
            Assert.fail("Should be exception for missing protocol");
        } catch (final AccessException e) {
            Assert.fail("Access exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail("Constraint exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            //thats fine, no action needed here
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#findSgOutputSample(org.pimslims.model.sample.Sample, java.lang.String, org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testProtocolHasNoOutputSamples() {
        final WritableVersion version = this.model.getTestVersion();

        Sample sample = null;
        final String protocolName = SyntheticGeneManagerTest.PROTOCOL_NAME;
        Experiment experiment;
        try {
            experiment = POJOFactory.createExperiment(version);
            sample = SyntheticGeneManager.findSgOutputSample(sample, protocolName, experiment);

        } catch (final ConstraintException e) {
            Assert.fail("Access exception thrown" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final IllegalStateException e) {
            //thats fine no action needed here
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#findSgOutputSample(org.pimslims.model.sample.Sample, java.lang.String, org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testTargetHasNoROEs() {
        final WritableVersion version = this.model.getTestVersion();

        try {
            final Target target = POJOFactory.createTarget(version);
            final Experiment experiment = POJOFactory.createExperiment(version);

            SyntheticGeneManager.linkResObjToExperiment(target, experiment, null);

        } catch (final ConstraintException e) {
            Assert.fail("Assertion error for Target with no ResearchObjectiveElements" + e.getMessage());
            throw new RuntimeException(e);
        } catch (final AssertionError e) {
            //thats fine no action needed here
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#makeSgeneResObj(org.pimslims.dao.WritableVersion, org.pimslims.model.target.ResearchObjective, org.pimslims.model.molecule.Molecule)}
     * .
     */
    public final void testMakeSgeneResObj() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective resObj = POJOFactory.createExpBlueprint(version);
            final Molecule molecule = POJOFactory.create(version, Molecule.class);
            final ResearchObjectiveElement roe =
                SyntheticGeneManager.makeSgeneResObjEle(version, resObj, molecule);
            Assert.assertEquals("The Res Obj element's ComponentType is Synthetic gene",
                SyntheticGeneManagerTest.SAMPLE_CAT, roe.getComponentType());
            Assert.assertEquals("The Res Obj element's WhyChosen is Synthetic gene",
                SyntheticGeneManagerTest.SAMPLE_CAT, roe.getWhyChosen());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            version.abort();
        }

    }

    /**
     * Test method for {@link syntheticGene.SyntheticGeneManager#setCompCatforSgene(final WritableVersion
     * version, final Map<String, Object> molAtts, final String sname)}.
     */
    public final void testNoComponentCategory() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            //Molecule molecule = POJOFactory.create(version, Molecule.class);
            final Map<String, Object> atts = new HashMap<String, Object>();
            final String sequence = SyntheticGeneManagerTest.DNASEQ;
            final String mtype = "DNA";
            final String sname = SyntheticGeneManagerTest.UNIQUE;
            atts.put(Molecule.PROP_SEQUENCE, sequence);
            atts.put(Molecule.PROP_MOLTYPE, mtype);
            atts.put(Substance.PROP_NAME, sname);

            SyntheticGeneManager.setCompCatforSgene(version, atts, sname);
            Assert.fail("Should be exception for missing ComponentCategory");
        } catch (final IllegalStateException e) {
            //thats fine no action needed here
        } finally {
            version.abort();
        }

    }

    /**
     * Test method for
     * {@link CreateSyntheticGeneManager#save(org.pimslims.dao.WritableVersion, org.pimslims.presentation.construct.SyntheticGeneBean)}
     * .
     */
    public final void testSave() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SyntheticGeneBean sgb = new SyntheticGeneBean();

            final LabNotebook accObj = POJOFactory.create(version, LabNotebook.class);
            sgb.setAccess(new ModelObjectBean(accObj));

            final String sampleName = "name" + SyntheticGeneManagerTest.UNIQUE;
            sgb.setSgeneName(sampleName);

            final Target target = POJOFactory.createTarget(version);
            final ResearchObjective dummyResObj = POJOFactory.createExpBlueprint(version);
            dummyResObj.setCommonName(target.getName());
            final ResearchObjectiveElement targResObjEl = POJOFactory.createBlueprintComponent(version);
            targResObjEl.setComponentType(SyntheticGeneManagerTest.TARGET);
            targResObjEl.setWhyChosen(target.getWhyChosen());
            targResObjEl.setResearchObjective(dummyResObj);
            targResObjEl.setTarget(target);

            final TargetBean targetBean = new TargetBean(target);
            sgb.setTargetBean(targetBean);

            sgb.setDnaSeq(SyntheticGeneManagerTest.DNASEQ);

            final String hookFromBean = SyntheticGeneManager.save(version, sgb);
            Assert.assertNotNull(hookFromBean);
            Assert.assertTrue("Hook is a Sample hook", hookFromBean.contains("sample.Sample"));

        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#getSyntheticGeneParts(org.pimslims.model.sample.Sample)}.
     */
    public final void testGetSyntheticGeneParts() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();

        try {
            final Target target = POJOFactory.createTarget(version);
            final LabNotebook accObj = POJOFactory.create(version, LabNotebook.class);
            target.setAccess(accObj);
            //System.out.println("Target Access is: " + target.getAccess());

            target.setWhyChosen(SyntheticGeneManagerTest.TARGET);
            final ResearchObjective dummyResObj = POJOFactory.createExpBlueprint(version);
            dummyResObj.setCommonName(target.getName());
            final ResearchObjectiveElement targResObjEl = POJOFactory.createBlueprintComponent(version);
            targResObjEl.setComponentType(SyntheticGeneManagerTest.TARGET);
            targResObjEl.setWhyChosen(target.getWhyChosen());
            targResObjEl.setResearchObjective(dummyResObj);
            targResObjEl.setTarget(target);

            final String sampleName = "name" + SyntheticGeneManagerTest.UNIQUE;

            final Sample sgene =
                SyntheticGeneManager.createSyntheticGene(version, sampleName, target,
                    SyntheticGeneManagerTest.DNASEQ, SyntheticGeneManagerTest.PROTSEQ);
            final Experiment experiment = sgene.getOutputSample().getExperiment();
            final Set<SampleComponent> samComps = sgene.getSampleComponents();
            final List<SampleComponent> scomps = new ArrayList(samComps);
            final Molecule mol1 = (Molecule) scomps.get(0).getRefComponent();
            final Molecule mol2 = (Molecule) scomps.get(1).getRefComponent();
            final Map<String, Object> roeMap = new HashMap<String, Object>();
            roeMap.put(ResearchObjectiveElement.PROP_COMPONENTTYPE, SyntheticGeneManagerTest.SAMPLE_CAT);
            //final ResearchObjective resOb = experiment.getProject();
            //final ResearchObjectiveElement roe = resOb.findFirst("researchObjectiveElements", roeMap);
            final List<ModelObject> theParts = SyntheticGeneManager.getSyntheticGeneParts(sgene);

            //System.out.println("Number of Synthetic gene parts: " + theParts.size());
            Assert.assertTrue("The synthetic gene sample has 5 parts", theParts.size() == 5);
            Assert.assertTrue("The parts contains an Experiment", theParts.contains(experiment));
            Assert.assertTrue("The parts contains a Sample", theParts.contains(sgene));
            Assert.assertTrue("The parts contains a Molecule", theParts.contains(mol1));
            Assert.assertTrue("The parts contains a Molecule", theParts.contains(mol2));
            //Assert.assertTrue("The parts contains a ResObjElement", theParts.contains(roe));

            // now delete
            for (final Iterator iterator = theParts.iterator(); iterator.hasNext();) {
                final ModelObject modelObject = (ModelObject) iterator.next();
                //System.out.println(modelObject.get_Hook());
                modelObject.delete();
            }

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link syntheticGene.SyntheticGeneManager#isSynthGene(org.pimslims.model.sample.Sample)}.
     */
    public final void testIsSyntheticGene() {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final SampleCategory samCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME,
                    SyntheticGeneManagerTest.SAMPLE_CAT);

            final Sample sample = POJOFactory.createSample(version);
            Assert.assertFalse(SyntheticGeneManager.isSynthGene(sample));

            if (null != samCat) {
                sample.addSampleCategory(samCat);
                Assert.assertTrue(SyntheticGeneManager.isSynthGene(sample));
            }
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            version.abort();
        }
    }

}
