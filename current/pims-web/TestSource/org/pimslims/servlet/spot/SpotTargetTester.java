/**
 * current-pims-web org.pimslims.servlet.spot SpotTargetTester.java
 * 
 * @author susy
 * @date 07-Feb-2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 susy
 * 
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.TargetBeanReader;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.test.POJOFactory;

/**
 * SpotTargetTester
 * 
 */
public class SpotTargetTester extends TestCase {
    private final AbstractModel model;

    /**
     * Constructor for SpotTargetTester.
     * 
     * @param arg0
     */
    public SpotTargetTester(final String arg0) {
        super(arg0);
        this.model = ModelImpl.getModel();
    }

    private static final String ALIAS = PIMSTarget.DNA_TARGET;

    private static final String DNASEQ = "GTACGTCTAAGCCT";

    private static final String DNASEQ2 = "GGGAACCCTT";

    private static final String UNIQUE = "st" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    public void testDoGetBad() throws ServletException, IOException {
        final SpotTarget servlet = new SpotTarget();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public void testDoGetNonesuch() throws ServletException, IOException {
        final SpotTarget servlet = new SpotTarget();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/nonesuch");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }

    public void testDoGet() throws ServletException, IOException, ConstraintException, AbortedException,
        AccessException {

        // make a target
        String hook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            hook = target.get_Hook();
            final ResearchObjective ro = new ResearchObjective(version, SpotTargetTester.UNIQUE, "TestGet");
            new ResearchObjectiveElement(version, "nonstandard", "TestGET" + SpotTargetTester.UNIQUE, ro)
                .setTarget(target);
            this.createExperiment(version, ro, "");
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SpotTarget servlet = new SpotTarget();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + hook);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        final long start = System.currentTimeMillis();
        servlet.doGet(request, response);
        final long time = System.currentTimeMillis() - start;
        System.out.println(servlet.getClass().getName() + " using " + time + "ms");
        Assert.assertTrue("Too long: " + time, time < 2000);

        final Collection expBeans = (Collection) request.getAttribute("experimentBeans");
        Assert.assertEquals(1, expBeans.size());
        final Collection trialBeans = (Collection) request.getAttribute("crystalTrialExps");
        Assert.assertEquals(0, trialBeans.size());

        // tidy up
        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = (Target) version.get(hook);
            final Molecule protein = target.getProtein();
            final Set<ResearchObjectiveElement> elements = target.getResearchObjectiveElements();
            final Set<ExperimentType> types = new HashSet();
            for (final Iterator iterator = elements.iterator(); iterator.hasNext();) {
                final ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator.next();
                final ResearchObjective ro = roe.getResearchObjective();
                final Set<Experiment> experiments = ro.getExperiments();
                for (final Iterator iterator2 = experiments.iterator(); iterator2.hasNext();) {
                    final Experiment experiment = (Experiment) iterator2.next();
                    types.add(experiment.getExperimentType());
                    experiment.delete();
                }
                ro.delete();
            }
            target.delete();
            protein.delete();
            version.delete(types);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public final void testNoConstruct() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final ResearchObjective ro = new ResearchObjective(version, SpotTargetTester.UNIQUE, "Test");
            new ResearchObjectiveElement(version, "nonstandard", "Test", ro).setTarget(target);
            final Collection<ConstructResultBean> milestones = SpotTarget.getMilestones(version, target);
            Assert.assertEquals(0, milestones.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testToDelete() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final ResearchObjective ro = new ResearchObjective(version, SpotTargetTester.UNIQUE, "Test");
            final ResearchObjectiveElement element =
                new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "Test", ro);
            element.setTarget(target);
            target.addNucleicAcid(new Molecule(version, "DNA", SpotTargetTester.UNIQUE + "dna"));

            final List<ModelObjectShortBean> linked = SpotTarget.getLinkedRecords(target);
            Assert.assertEquals(4, linked.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testNoMilestones() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final ResearchObjective ro = new ResearchObjective(version, SpotTargetTester.UNIQUE, "Test");
            final ResearchObjectiveElement element =
                new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "Test", ro);
            element.setTarget(target);
            final Collection<ConstructResultBean> milestones = SpotTarget.getMilestones(version, target);
            Assert.assertEquals(0, milestones.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testMilestones() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final TargetStatus status = new TargetStatus(version, SpotTargetTester.UNIQUE);
            final ResearchObjective ro = new ResearchObjective(version, SpotTargetTester.UNIQUE, "Test");
            new ResearchObjectiveElement(version, ConstructUtility.SPOTCONSTRUCT, "Test", ro)
                .setTarget(target);
            final Experiment experiment = this.createExperiment(version, ro, "tm");
            new Milestone(version, SpotTargetTester.NOW, status, experiment);
            final Collection<ConstructResultBean> milestones = SpotTarget.getMilestones(version, target);
            Assert.assertEquals(1, milestones.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testNoExperiments() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final List<ExperimentBean> targetExpBeans = new ArrayList();
            final Collection<PlateBean> crystalTrialExps = new LinkedList<PlateBean>();
            SpotTarget.setExperimentBeans(version, target, targetExpBeans, crystalTrialExps);
            final ResearchObjective ro =
                new ResearchObjective(version, SpotTargetTester.UNIQUE, SpotTargetTester.UNIQUE);
            final ResearchObjectiveElement element =
                new ResearchObjectiveElement(version, SpotTargetTester.UNIQUE, SpotTargetTester.UNIQUE, ro);
            element.setTarget(target);
            Assert.assertEquals(0, targetExpBeans.size());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testTargetExperiments() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Target target = SpotTargetTester.makeTarget(version);
            final ResearchObjective ro =
                new ResearchObjective(version, SpotTargetTester.UNIQUE, SpotTargetTester.UNIQUE);
            final ResearchObjectiveElement element =
                new ResearchObjectiveElement(version, SpotTargetTester.UNIQUE, SpotTargetTester.UNIQUE, ro);
            element.setTarget(target);
            final Experiment experiment = this.createExperiment(version, ro, "te");
            new InputSample(version, experiment);
            new InputSample(version, experiment);
            version.flush();
            // also make a crystal trial
            final ExperimentType trial =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Trials");
            final Experiment exp2 =
                new Experiment(version, "trial" + SpotTargetTester.UNIQUE, SpotTargetTester.NOW,
                    SpotTargetTester.NOW, trial);
            exp2.setProject(ro);
            exp2.setStatus("OK");
            final ExperimentGroup group =
                new ExperimentGroup(version, SpotTargetTester.UNIQUE, SpotTargetTester.UNIQUE);
            exp2.setExperimentGroup(group);

            final List<ExperimentBean> targetExpBeans = new ArrayList();
            final Collection<PlateBean> crystalTrialExps = new LinkedList<PlateBean>();
            SpotTarget.setExperimentBeans(version, target, targetExpBeans, crystalTrialExps);

            Assert.assertEquals(1, crystalTrialExps.size());
            final PlateBean pb = crystalTrialExps.iterator().next();
            Assert.assertEquals(group.get_Hook(), pb.getExpGroupHook());

            Assert.assertEquals(1, targetExpBeans.size());
            final ExperimentBean bean = targetExpBeans.iterator().next();
            Assert.assertEquals(experiment.getName(), bean.getName());
            Assert.assertEquals(experiment.get_Hook(), bean.getExperimentHook());
            Assert.assertEquals(SpotTargetTester.NOW, bean.getExperimentDate());
            Assert.assertEquals("OK", bean.getStatus());
            Assert.assertEquals(experiment.getExperimentType().getName(), bean.getExperimentTypeName());
            Assert.assertEquals(experiment.getExperimentType().get_Hook(), bean.getExperimentTypeHook());
            Assert.assertEquals(null, bean.getMilestone());
            Assert.assertEquals(null, bean.getProtocol());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    private Experiment createExperiment(final WritableVersion version, final ResearchObjective ro,
        final String suffix) throws ConstraintException {
        Experiment experiment;
        {
            final ExperimentType type =
                new ExperimentType(version, "type" + SpotTargetTester.UNIQUE + suffix);
            experiment =
                new Experiment(version, "exp" + SpotTargetTester.UNIQUE + suffix, SpotTargetTester.NOW,
                    SpotTargetTester.NOW, type);
            experiment.setProject(ro);
            experiment.setStatus("OK");
        }
        return experiment;
    }

    /*
     * method to test if Target id DNA Target
     */
    public final void testIsDNATarget() {
        final WritableVersion version = this.model.getTestVersion();
        final List aliases = new java.util.ArrayList<String>();
        aliases.add(SpotTargetTester.ALIAS);
        try {
            final Target t = SpotTargetTester.makeTarget(version);
            Boolean isDNATarg = PIMSTarget.isDNATarget(t);
            Assert.assertFalse("Target is a DNA Target", isDNATarg);

            t.setAliasNames(aliases);
            isDNATarg = PIMSTarget.isDNATarget(t);
            Assert.assertFalse("Target is not a DNA Target", isDNATarg);
            final TargetBean tb = new TargetBean(t);
            Assert.assertTrue("Target bean for DNA Target", tb.getAliases().contains(SpotTargetTester.ALIAS));

            //Need a DNA 'protein'
            final Molecule dnaTarg = POJOFactory.createProtein(version);
            dnaTarg.setMolType("DNA");
            t.setProtein(dnaTarg);
            isDNATarg = PIMSTarget.isDNATarget(t);
            Assert.assertTrue("Target is a DNA Target", isDNATarg);

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
            //throw new RuntimeException(e);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /*
     * method to test if Target id DNA Target
     */
    public final void testcalcGCContent() {
        final WritableVersion version = this.model.getTestVersion();
        final List aliases = new java.util.ArrayList<String>();
        aliases.add(SpotTargetTester.ALIAS);
        try {
            final Target t2 = SpotTargetTester.makeTarget(version);
            final TargetBean tb2 = new TargetBean(t2);
            Float dnaSeqGC = null;
            final Molecule protein = POJOFactory.createProtein(version);
            final Molecule dna = POJOFactory.create(version, Molecule.class);
            dna.setSequence(SpotTargetTester.DNASEQ);
            final Collection seqs = new ArrayList<Molecule>();
            seqs.add(dna);
            //Molecule for a DNA Target
            final Molecule dnatarg = POJOFactory.createProtein(version);
            dnatarg.setMolType("DNA");
            dnatarg.setSequence(SpotTargetTester.DNASEQ2);
            final Target t3 = SpotTargetTester.makeTarget(version);
            t3.setProtein(dnatarg);
            final TargetBean tb3 = new TargetBean();

            dnaSeqGC = SpotTarget.calcGCContent(t2, tb2);
            //System.out.println("CG content is: "+dnaSeqGC);
            Assert.assertEquals("No DNA Sequence so can't set GC content", dnaSeqGC, 0.0f);

            //add a protein
            t2.setProtein(protein);
            TargetBeanReader.readTarget(tb2, t2);
            dnaSeqGC = SpotTarget.calcGCContent(t2, tb2);
            Assert.assertEquals("Still no DNA Sequence so can't set GC content", dnaSeqGC, 0.0f);

            //add a dna nucleic acid with DNA sequence
            t2.setNucleicAcids(seqs);
            Assert.assertEquals("DNA sequence is set", t2.getNucleicAcids().size(), 1);
            TargetBeanReader.readTarget(tb2, t2);
            dnaSeqGC = SpotTarget.calcGCContent(t2, tb2);
            Assert.assertEquals("Target has DNA Sequence", dnaSeqGC, 50.0f);

            //make DNA Target, needs MolType DNA and aliases set
            t3.setAliasNames(aliases);
            final Boolean isDNATarg2 = PIMSTarget.isDNATarget(t3);
            Assert.assertTrue("Target is a DNA Target", isDNATarg2);
            Assert.assertTrue("Moltype is DNA", "dna".equalsIgnoreCase(t3.getProtein().getMolType()));
            TargetBeanReader.readTarget(tb3, t3);
            dnaSeqGC = SpotTarget.calcGCContent(t3, tb3);
            Assert.assertEquals("Target has DNA Sequence", dnaSeqGC, 60.0f);

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /* this test written to test a specific fault
    public final void testTargetGroupAccess() {

        final WritableVersion version = this.model.getWritableVersion("marc");

        try {
            final Target target = version.findFirst(Target.class, Target.PROP_NAME, "OPTIC9324");

            if (null != target) {
                // The call target.getTargetGroups has not applied any access control.
                // If we make a separate version.get(hook) call, access control is applied.
                final Collection<TargetGroup> targetGroups = target.getTargetGroups();
                for (final TargetGroup tg : targetGroups) {
                    System.out.println("testTargetGroupAccess(" + tg.get_Name() + ")");
                    org.pimslims.metamodel.ModelObject object = null;
                    object = version.get(tg.get_Hook());
                    Assert.assertNotNull("No Access to object [" + tg.get_Name() + "]", object);
                    //if (null != object) {
                    //    System.out.println("testTargetGroupAccess group(" + object.get_Name() + ")");
                    //} else {
                    //    System.out.println("testTargetGroupAccess NO ACCESS(" + tg.get_Hook() + ")");
                    //}
                }
            }

        } finally {
            version.abort(); // not testing persistence
        }
    }
    */

    /*
     * method to make a target for use in testing
     */
    static Target makeTarget(final WritableVersion version) throws ConstraintException {
        final Target target = POJOFactory.createTarget(version);
        final String commonName = "target" + new Date() + target.getName();
        target.setName(commonName);
        Assert.assertEquals("Check the target name", target.get_Name(), commonName);
        return target;

    }

}
