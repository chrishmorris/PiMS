package org.pimslims.presentation;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

public class TargetBeanReaderTest extends TestCase {

    private static final String TARGET_NAME = "testTarget" + System.currentTimeMillis();

    private static final String LOCAL_NAME = "localName" + System.currentTimeMillis();

    private static final String PROTEIN_NAME = "testProtein" + System.currentTimeMillis();

    private static final String ORGANISM_NAME = "org" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TargetBeanReaderTest.class);
    }

    private AbstractModel model;

    public TargetBeanReaderTest(final String methodName) {
        super(methodName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRead() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Molecule protein = new Molecule(version, "protein", TargetBeanReaderTest.PROTEIN_NAME);
            final Target target = new Target(version, TargetBeanReaderTest.TARGET_NAME, protein);
            target.setAliasNames(Collections.singletonList(TargetBeanReaderTest.LOCAL_NAME));
            final Organism species = new Organism(version, TargetBeanReaderTest.ORGANISM_NAME);
            target.setSpecies(species);
            final ResearchObjective blueprint = new ResearchObjective(version, target.getName(), "test");
            blueprint.setLocalName(target.getName());
            final ResearchObjectiveElement component =
                new ResearchObjectiveElement(version, "target", "test", blueprint);
            component.setTarget(target);

            final TargetBean tb = new TargetBean();
            TargetBeanReader.readTarget(tb, target);

            Assert.assertEquals(Target.class.getName(), tb.getClassName());
            Assert.assertEquals(TargetBeanReaderTest.PROTEIN_NAME, tb.getProtein_name());
            Assert.assertEquals(TargetBeanReaderTest.TARGET_NAME, tb.getName());
            Assert.assertEquals(TargetBeanReaderTest.LOCAL_NAME + "; ", tb.getAliases());
            Assert.assertEquals(0, tb.getNumConstructs().intValue());
            Assert.assertEquals(target.get_Hook(), tb.getPimsTargetHook());
            final String roHook = tb.getResearchObjectiveHook();
            Assert.assertNotNull(roHook);
            final ResearchObjective ro = version.get(roHook);
            Assert.assertEquals(1, ro.getResearchObjectiveElements().size());
            Assert.assertEquals(TargetBeanReaderTest.ORGANISM_NAME, tb.getOrganismBean().getName());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testProteinSeq() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Molecule protein =
                new Molecule(version, "protein", "testProtein" + System.currentTimeMillis());
            protein.setSequence("WWW");
            final Target t = new Target(version, "testTarget" + System.currentTimeMillis(), protein);

            final TargetBean tb = new TargetBean();
            TargetBeanReader.readTarget(tb, t);

            Assert.assertEquals("WWW", tb.getProtSeq());
            Assert.assertEquals(protein.get_Hook() + ":" + Molecule.PROP_SEQUENCE, tb.getProtSeqHook());
            Assert.assertEquals(protein.get_Hook() + ":" + Substance.PROP_NAME, tb
                .getProteinNameHook());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testDNASeq() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final Molecule protein =
                new Molecule(version, "protein", "testProtein" + System.currentTimeMillis());
            final Target t = new Target(version, "testTarget" + System.currentTimeMillis(), protein);
            final Molecule dna = new Molecule(version, "DNA", "testDNA" + System.currentTimeMillis());
            dna.setSequence("A\n AA"); // may contain white space
            t.addNucleicAcid(dna);

            final TargetBean tb = new TargetBean();
            TargetBeanReader.readTarget(tb, t);

            Assert.assertEquals("AAA", tb.getDnaSeq());
            Assert.assertEquals(dna.get_Hook() + ":" + Molecule.PROP_SEQUENCE, tb.getDnaSeqHook());

        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
