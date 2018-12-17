package org.pimslims.presentation;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

public class TargetBeanWriterTest extends TestCase {

    /**
     * 
     */
    private static final String TARGET_NAME = "MRSA111" + System.currentTimeMillis();

    //private static final String HYPERLINK = "http://unipropt.org/234235245.html";

    private static final String GI_NUMBER = "6422432";

    private static final String PROTEIN_NAME = "testProtein" + System.currentTimeMillis();

    private static final String PROJECT_NAME = "proj" + System.currentTimeMillis();

    private static final String UNIQUE = "tbw" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(TargetBeanWriterTest.class);
    }

    private AbstractModel model;

    public TargetBeanWriterTest(final String methodName) {
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

    public void testCreate() throws ConstraintException, ServletException, AccessException {

        final TargetBean tb = new TargetBean();
        tb.setProtSeq(("MVKKLAACANVNDDEE"));
        tb.setProtein_name(TargetBeanWriterTest.PROTEIN_NAME);
        tb.setDnaSeq(("AGTGATATGGAGATTATAGG"));
        tb.setGi_number(TargetBeanWriterTest.GI_NUMBER);
        //tb.setHyperlink(null);
        tb.setComments(("No Comments"));
        tb.setFunc_desc(("Puts togethr two parts"));
        tb.setTarget_id((TargetBeanWriterTest.TARGET_NAME));

        final WritableVersion version = this.model.getTestVersion();
        try {
            // prepare
            final Organism organism = new Organism(version, "test" + System.currentTimeMillis());
            tb.setOrganism(BeanFactory.newBean(organism));
            //final Project project = new Project(version, "test", "test" + System.currentTimeMillis());
            //tb.setProjectHook(project.get_Hook());
            final Person person = new Person(version, TargetBeanWriterTest.UNIQUE);
            new User(version, TargetBeanWriterTest.UNIQUE).setPerson(person);
            tb.setPersonHook(person.get_Hook());

            // create it
            final Target target = TargetBeanWriter.createNewTarget(version, tb);

            // check results
            Assert.assertTrue(target.getProtein().getName().equals(TargetBeanWriterTest.PROTEIN_NAME));
            Assert.assertEquals(organism, target.getSpecies());
            Assert.assertEquals("protein", target.getProtein().getMolType());
            final Collection<Molecule> dnaMolecules = target.getNucleicAcids();
            Assert.assertEquals(1, dnaMolecules.size());
            Assert.assertEquals("DNA", dnaMolecules.iterator().next().getMolType());
            Assert.assertEquals(TargetBeanWriterTest.TARGET_NAME, target.getName());
            // no longer Assert.assertTrue(target.getProjects().contains(project));
            Assert.assertEquals(1, target.getExternalDbLinks().size());
            final Iterator<ExternalDbLink> iterator = target.getExternalDbLinks().iterator();
            final ExternalDbLink ext1 = iterator.next();
            //final ExternalDbLink ext2 = iterator.next();
            Assert.assertTrue(ext1.getDatabaseName().getName(),
                "GenBank".equals(ext1.getDatabaseName().getName()));
            //Assert.assertNotNull(target.getCreatorPerson());

            final TargetBean bean = new TargetBean();
            TargetBeanReader.readTarget(bean, target);

            Assert.assertNotNull(bean);
            Assert.assertEquals(TargetBeanWriterTest.GI_NUMBER, bean.getGi_number());
            //Assert.assertEquals(null, bean.getHyperlink());
            Assert.assertEquals(TargetBeanWriterTest.TARGET_NAME, bean.getName());
            //Assert.assertEquals(person.getName(), bean.getCreator().getName());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // TODO assertEquals(HYPERLINK,
        // ((DbRef)version.get(bean.getHyperlinkHook())).getUrl());

    }

    public void testCreateNoDNA() throws ConstraintException, ServletException, AccessException {

        final TargetBean tb = new TargetBean();
        tb.setProtSeq(("MVKKLAACANVNDDEE"));
        tb.setProtein_name(TargetBeanWriterTest.PROTEIN_NAME);
        tb.setDnaSeq("");
        tb.setGi_number(TargetBeanWriterTest.GI_NUMBER);
        tb.setComments(("No Comments"));
        tb.setFunc_desc(("test"));
        tb.setTarget_id((TargetBeanWriterTest.TARGET_NAME));

        final WritableVersion version = this.model.getTestVersion();
        try {
            // prepare
            final Organism organism = new Organism(version, "test" + System.currentTimeMillis());
            tb.setOrganism(BeanFactory.newBean(organism));

            //final Project project = new Project(version, "test", "test" + System.currentTimeMillis());
            //tb.setProjectHook(project.get_Hook());

            // create it
            final Target target = TargetBeanWriter.createNewTarget(version, tb);

            // check results
            Assert.assertTrue(target.getProtein().getName().equals(TargetBeanWriterTest.PROTEIN_NAME));
            final Collection<Molecule> dnaMolecules = target.getNucleicAcids();
            Assert.assertEquals(1, dnaMolecules.size());
            final Molecule dna = dnaMolecules.iterator().next();
            Assert.assertEquals("DNA", dna.getMolType());

            final TargetBean bean = new TargetBean();
            TargetBeanReader.readTarget(bean, target);

            Assert.assertNotNull(bean);
            Assert.assertEquals(TargetBeanWriterTest.GI_NUMBER, bean.getGi_number());
            Assert.assertEquals(TargetBeanWriterTest.TARGET_NAME, bean.getName());
            Assert.assertEquals(dna.get_Hook() + ":seqString", bean.getDnaSeqHook());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // TODO assertEquals(HYPERLINK,
        // ((DbRef)version.get(bean.getHyperlinkHook())).getUrl());

    }

    public void testProject() throws ConstraintException, ServletException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final TargetBean tb = new TargetBean();
            tb.setProtSeq(("MVKKLAACANVNDDEE"));
            tb.setProtein_name(TargetBeanWriterTest.PROTEIN_NAME);
            tb.setDnaSeq(("AGTGATATGGAGATTATAGG"));
            tb.setGi_number(TargetBeanWriterTest.GI_NUMBER);
            //tb.setHyperlink(TargetBeanWriterTest.HYPERLINK);
            tb.setFunc_desc(("Puts togethr two parts"));
            tb.setTarget_id((TargetBeanWriterTest.TARGET_NAME));

            final Organism organism = new Organism(version, "test" + System.currentTimeMillis());
            tb.setOrganism(BeanFactory.newBean(organism));
            //final Project project =
            //    new Project(version, TargetBeanWriterTest.PROJECT_NAME, TargetBeanWriterTest.PROJECT_NAME);
            //tb.setProjectHook(project.get_Hook());
            version.setDefaultOwner(new LabNotebook(version, TargetBeanWriterTest.PROJECT_NAME));

            final Target target = TargetBeanWriter.createNewTarget(version, tb);
            Assert.assertNotNull("not reference data", target.getAccess());
            Assert.assertEquals(TargetBeanWriterTest.PROJECT_NAME, target.getAccess().getName());

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // TODO assertEquals(HYPERLINK,
        // ((DbRef)version.get(bean.getHyperlinkHook())).getUrl());

    }

    public void testCreateDNATarget() throws ConstraintException, ServletException, AccessException {
        // ExperimentCreationForm form = new ExperimentCreationForm();
        final WritableVersion version = this.model.getTestVersion();
        try {
            final TargetBean tb2 = new TargetBean();
            tb2.setDnaName("DNA name");
            tb2.setDnaSeq("AGTGATATGGAGATTATAGG");
            tb2.setGi_number(TargetBeanWriterTest.GI_NUMBER);
            //tb2.setHyperlink(TargetBeanWriterTest.HYPERLINK);
            tb2.setComments("DNA Target");
            tb2.setFunc_desc("Promoter");
            tb2.setTarget_id("NPTarget" + System.currentTimeMillis());
            //final String target_id = tb2.getTarget_id();
            tb2.setProtein_name("*-*-*" + "NPTarget" + System.currentTimeMillis());

            final Organism organism = new Organism(version, "test" + System.currentTimeMillis());
            tb2.setOrganism(BeanFactory.newBean(organism));
            //final Project project = new Project(version, "test", "test" + System.currentTimeMillis());
            //tb2.setProjectHook(project.get_Hook());

            final ModelObject modelObject = TargetBeanWriter.createNewTarget(version, tb2);

            final Target t2 = (Target) modelObject;
            //assertTrue(t2.getProtein().getName().startsWith("*-*-*NPTarget"));
            Assert.assertTrue(t2.getProtein().getName().equalsIgnoreCase("DNA name"));
            Assert.assertEquals(organism, t2.getSpecies());
            Assert.assertTrue("DNA".equals(t2.getProtein().getMolType()));
            final Collection<Molecule> dnaMolecules = t2.getNucleicAcids();
            Assert.assertEquals(0, dnaMolecules.size());
            Assert.assertEquals("DNA", t2.getProtein().getMolType());
            Assert.assertTrue("*DNA Target*".equals(t2.getAliases().iterator().next().getName()));

            final TargetBean bean2 = new TargetBean();
            TargetBeanReader.readTarget(bean2, t2);

            Assert.assertNotNull(bean2);
            Assert.assertEquals(TargetBeanWriterTest.GI_NUMBER, bean2.getGi_number());
            //Assert.assertEquals(TargetBeanWriterTest.HYPERLINK, bean2.getHyperlink());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        // TODO assertEquals(HYPERLINK,
        // ((DbRef)version.get(bean.getHyperlinkHook())).getUrl());

    }

}
