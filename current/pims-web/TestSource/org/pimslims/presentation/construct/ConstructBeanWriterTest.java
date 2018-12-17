package org.pimslims.presentation.construct;

// Written by Johan?

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.test.AbstractTestCase;

public class ConstructBeanWriterTest extends AbstractTestCase {

    /**
     * 
     */
    static final String DESCRIPTION = "Testing construct writer.. Description field";

    /**
     * 
     */
    static final String COMMENTS = "Testing construct writer.. Comments field";

    public static final String FINAL_PROTEIN =
        "AASLIKPCAYEKQGLIDHAIGSYKVLHDKISESYYKVISRRLERYGIVLDLNGVKEIVKDVVVLHDMGKAGEYYQNQFDDNCNPLKSNPLKSRFSFIYHELGSALFFYNDYEPINVEKAEEVRSLLTLAVINHLNAIRGISDYLLNRFPDNFDEEMIKLSKYGSILLENLRGIISKSLKVRDYSFTDYHDMLYAFSKKSDKYLKLYNLFLAPIMLGDNLDSSLVRNNGSKTRFVGILEGELNGGSTLCCCC";

    public static final String SCIENTIST = "me" + System.currentTimeMillis();

    public static final String TARGET_NAME = "test Target" + System.currentTimeMillis();

    /**
     * FORWARD_TAG DNA equivalent of intended protein tag In general, this might include a contribution from
     * the vector
     */
    public static final String FORWARD_TAG = "AACACGTGTG";

    public static final String FORWARD_EXTENSION = "AACACGTGTG";

    public static final String FWD_OVERLAP = "TTGATCAAGCCT";

    static final String FWD_PRIMER = ConstructBeanWriterTest.FORWARD_EXTENSION
        + ConstructBeanWriterTest.FWD_OVERLAP;

    static final String REVERSE_TAG = "CCGACTATCGACT";

    public static final String REV_OVERLAP = "TAGAGTGGAACC";

    static final String REV_PRIMER = ConstructBeanWriterTest.REVERSE_TAG
        + ConstructBeanWriterTest.REV_OVERLAP;

    public static final String DNA_SEQUENCE =
        "TTGATCAAGCCTTGTGCTTATGAGAAGCAAGGTTTAATTGATCACGCGATTGGTTCTTATAAGGTCTTACATGATAAAATAAGTGAGTCCTATTATAAGGTT"
            + "ATATCAAGGAGATTGGAGAGATATGGAATAGTTCTTGATCTAAACGGTGTTAAGGAAATAGTTAAGGACGTTGTTGTACTTCATGATATGGGAAAAGCCGGGG"
            + "AATATTACCAAAATCAGTTTGATGATAATTGTAATCCCCTAAAGAGTAATCCCCTAAAGA"
            + "GTAGATTCTCTTTCATATATCACGAACTCGGTTCGGCTCTATTTTTCTATAACGATTATGAACCTATTAACGTTGAGAAGGCGGAGGAAGTTAGGTCTTTGT"
            + "TGACTTTAGCTGTTATAAATCACTTGAATGCAATTAGGGGGATATCTGACTATTTATTAAATAGGTTTCCAGACAATTTTGATGAAGAGATGATAAAGCTTAGT"
            + "AAGTACGGGAGTATTTTGCTGGAAAACTTGAGAGGCATTATTTCTAAAAGTTTAAAAGT"
            + "TAGGGATTATAGTTTTACTGATTATCACGATATGCTGTATGCATTTTCTAAGAAGAGCGATAAGTATTTAAAGTTATATAATCTTTTTTTAGCCCCCATAATG"
            + "TTGGGGGATAATTTAGATAGTAGTCTAGTTCGAAATAATGGAAGTAAAACTAGATTTGTTGGCATATTGGAGGGTGAATTGAATGGAGGTTCCACTCTA";

    public static final String PCR_PRODUCT_SEQ = ConstructBeanWriterTest.FORWARD_EXTENSION
        + ConstructBeanWriterTest.DNA_SEQUENCE + "AGTCGATAGTCGG";

    public static final String EXPRESSED_PROTEIN =
        "NNLLHHHHHHAASLIKPCAYEKQGLIDHAIGSYKVLHDKISESYYKVISRRLERYGIVLDLNGVKEIVKDVVVLHDMGKAGEYYQNQFDDNCNPLKSN"
            + "PLKSRFSFIYHELGSALFFYNDYEPINVEKAEEVRSLLTLAVINHLNAIRGISDYLLNRFPDNFDEEMIKLSKYGSILLENLRGIISKSLKVRDYSFT"
            + "DYHDMLYAFSKKSDKYLKLYNLFLAPIMLGDNLDSSLVRNNGSKTRFVGILEGELNGGSTLCCCC";

    protected static final String CONSTRUCT_ID = "T" + System.currentTimeMillis() + ".full";

    private static final String UNIQUE = "cbw" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ConstructBeanWriterTest.class);
    }

    private AbstractModel model;

    public ConstructBeanWriterTest() {
        super();
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

    public void testNoTarget() throws ConstraintException, AccessException, ServletException,
        ClassNotFoundException, SQLException {
        // ExperimentCreationForm form = new ExperimentCreationForm();
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        final PrimerBean f = new PrimerBean(true);
        f.setSequence("CGCGCG");
        f.setTag(ConstructBeanWriterTest.FORWARD_TAG);
        f.setLengthOnGene(ConstructBeanWriterTest.FWD_OVERLAP.length());
        final PrimerBean r = new PrimerBean(false);
        r.setSequence("ATATAT");
        r.setTag(ConstructBeanWriterTest.REVERSE_TAG);
        r.setLengthOnGene(ConstructBeanWriterTest.REV_OVERLAP.length());
        final ConstructBean cb = new ConstructBean(null, f, r);

        cb.setConstructId(ConstructBeanWriterTest.UNIQUE);
        cb.setDnaSeq(ConstructBeanWriterTest.DNA_SEQUENCE);
        cb.setProtSeq("null");
        cb.setDescription(ConstructBeanWriterTest.DESCRIPTION);
        cb.setComments(ConstructBeanWriterTest.COMMENTS);
        cb.setExpressedProt(ConstructBeanWriterTest.EXPRESSED_PROTEIN);
        cb.setFinalProt(ConstructBeanWriterTest.FINAL_PROTEIN);

        cb.setTargetProtStart(1);
        cb.setTargetProtEnd(244);
        // cb.setDateOfEntry(DateFormat.getDateInstance(DateFormat.MEDIUM,
        // Locale.UK).parse(((AppDataString) a).getValue()));
        cb.setDateOfEntry(new Date());

        try {

            // Write the ExpBlueprint
            ConstructBeanWriter.createNewConstruct(wv, cb);

        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void testPrimerlessConstruct() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final Molecule protein = new Molecule(wv, "protein", "test" + System.currentTimeMillis());
            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME, protein);
            final TargetBean tb = new TargetBean(target);
            final ConstructBean written = new ConstructBean(tb);
            written.setName(ConstructBeanWriterTest.CONSTRUCT_ID);
            written.setPcrProductSeq(ConstructBeanWriterTest.PCR_PRODUCT_SEQ);
            written.setExpressedProt(ConstructBeanWriterTest.EXPRESSED_PROTEIN);
            //TODO written.setTargetProtStart(2);
            //TODO written.setTargetProtEnd(243);
            final ResearchObjective ro = ConstructBeanWriter.createNewConstruct(wv, written);

            // test new RO
            Assert.assertEquals(1, ro.getResearchObjectiveElements().size());
            final ResearchObjectiveElement element = ro.getResearchObjectiveElements().iterator().next();
            Assert.assertEquals(4, element.getTrialMolComponents().size());
            Molecule insert = null;
            for (final Molecule bc : element.getTrialMolComponents()) {
                for (final ComponentCategory category : bc.getCategories()) {
                    if (CaliperFile.PCR_PRODUCT.equals(category.getName())) {
                        insert = bc;
                    }
                }
            }
            Assert.assertNotNull(insert);
            Assert.assertEquals(1, ro.getExperiments().size());
            final Experiment experiment = ro.getExperiments().iterator().next();
            Assert.assertEquals(1, experiment.getInputSamples().size());

            // test bean
            final ConstructBean read = ConstructBeanReader.readConstruct(ro);
            Assert.assertEquals(ConstructBeanWriterTest.PCR_PRODUCT_SEQ, read.getPcrProductSeq());
            Assert.assertEquals(ConstructBeanWriterTest.EXPRESSED_PROTEIN, read.getExpressedProt());
            Assert.assertTrue(read.isPrimerless());
            Assert.assertNotNull(read.getVectorInputSampleHook());

            //test can add primers later
            read.setFwdPrimer(ConstructBeanWriterTest.FWD_OVERLAP);
            read.setRevPrimer(ConstructBeanWriterTest.REV_OVERLAP);
            read.suggestPrimerNames();
            assert null != read.getName();
            // calculate protein sequences and their properties
            ConstructBean.annotate(read);
            ConstructBeanWriter.addSequences(wv, read, ro.getResearchObjectiveElements().iterator().next());
            // no, already done ConstructBeanWriter.createPrimerDesignExperiment(wv, read, ro);
        } finally {
            wv.abort();
        }
    }

    public void testSequenceOnly() throws ConstraintException, AccessException {
        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ConstructBean written = new ConstructBean(new TargetBean());
            written.setFinalProt(ConstructBeanWriterTest.FINAL_PROTEIN);
            written.setName(ConstructBeanWriterTest.CONSTRUCT_ID);
            //written.setPcrProductSeq(ConstructBeanWriterTest.PCR_PRODUCT_SEQ);
            //written.setExpressedProt(ConstructBeanWriterTest.EXPRESSED_PROTEIN);
            final ResearchObjective ro = ConstructBeanWriter.createNewConstruct(wv, written);

            // test new RO
            Assert.assertEquals(1, ro.getResearchObjectiveElements().size());
            final ResearchObjectiveElement element = ro.getResearchObjectiveElements().iterator().next();
            Assert.assertEquals(4, element.getTrialMolComponents().size());

            Assert.assertEquals(1, ro.getExperiments().size());

            // test bean
            final ConstructBean read = ConstructBeanReader.readConstruct(ro);
            Assert.assertEquals(ConstructBeanWriterTest.FINAL_PROTEIN, read.getFinalProt());
            Assert.assertTrue(read.isPrimerless());
        } finally {
            wv.abort();
        }
    }

    public void testWrite() throws AbortedException, ConstraintException, AccessException, ServletException,
        ClassNotFoundException, SQLException {

        WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        String hook = null;
        try {
            final ResearchObjective eb = ConstructBeanWriterTest.createConstructBean(wv);
            Assert.assertEquals(ConstructBeanWriterTest.CONSTRUCT_ID, eb.getCommonName());
            Assert.assertEquals(ConstructBeanWriterTest.COMMENTS, eb.getDetails());
            Assert.assertEquals(ConstructBeanWriterTest.DESCRIPTION, eb.getFunctionDescription());
            Assert.assertNotNull(eb.getOwner());

            final ConstructBean bean = ConstructBeanReader.readConstruct(eb);
            Assert.assertNotNull(bean);
            //was Assert.assertNull(bean.getVectorInputSampleHook());
            Assert.assertFalse(bean.isPrimerless());
            Assert.assertEquals(ConstructBeanWriterTest.FINAL_PROTEIN, bean.getFinalProt());
            Assert
                .assertEquals(ConstructBeanWriterTest.TARGET_NAME + "F2", bean.getFwdPrimerBean().getName());
            Assert.assertEquals("AACACGTGTGTTGATCAAGCCT", bean.getFwdPrimerBean().getSequence());
            Assert.assertEquals(ConstructBeanWriterTest.TARGET_NAME + "R243", bean.getRevPrimerBean()
                .getName());

            hook = eb.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // test again after commit
        wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ResearchObjective ro = wv.get(hook);
            Assert.assertNotNull("not saved", ro);
            Assert.assertEquals(ConstructBeanWriterTest.CONSTRUCT_ID, ro.getCommonName());
            Assert.assertEquals(ConstructBeanWriterTest.COMMENTS, ro.getDetails());
            Assert.assertEquals(ConstructBeanWriterTest.DESCRIPTION, ro.getFunctionDescription());
            Assert.assertNotNull(ro.getOwner());

            Assert.assertEquals(1, ro.getExperiments().size());
            Assert.assertEquals(ro.getName() + "-SCD administrator 1", ro.getExperiments().iterator().next()
                .getName());

            // now tidy up
            final Target target = ro.getResearchObjectiveElements().iterator().next().getTarget();
            this.deleteTarget(wv, target);
            this.deleteConstruct(wv, ro);
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testCommonPrimer() throws AbortedException, ConstraintException, AccessException,
        ServletException, ClassNotFoundException, SQLException {

        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {

            final Molecule protein = new Molecule(wv, "protein", "test" + System.currentTimeMillis());
            final Molecule dna = new Molecule(wv, "DNA", "testDna" + System.currentTimeMillis());
            dna.setSequence(ConstructBeanWriterTest.DNA_SEQUENCE);
            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME, protein);
            target.addNucleicAcid(dna);

            final User user = new User(wv, ConstructBeanWriterTest.SCIENTIST);
            ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, user, "a",
                ConstructBeanWriterTest.FORWARD_EXTENSION);
            ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, user, "b",
                ConstructBeanWriterTest.FORWARD_EXTENSION);

        } finally {
            wv.abort();
        }
    }

    public void testDifferentSequence() throws AbortedException, ConstraintException, AccessException,
        ServletException, ClassNotFoundException, SQLException {

        final WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {

            final Molecule protein = new Molecule(wv, "protein", "test" + System.currentTimeMillis());
            final Molecule dna = new Molecule(wv, "DNA", "testDna" + System.currentTimeMillis());
            dna.setSequence(ConstructBeanWriterTest.DNA_SEQUENCE);
            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME, protein);
            target.addNucleicAcid(dna);

            final User user = new User(wv, ConstructBeanWriterTest.SCIENTIST);
            final ResearchObjective construct1 =
                ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, user, "a",
                    ConstructBeanWriterTest.FORWARD_EXTENSION);
            final String primer1 = ConstructBeanReader.readConstruct(construct1).getFwdPrimer();
            Assert.assertNotNull(primer1);
            final ResearchObjective construct2 =
                ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, user, "b",
                    ConstructBeanWriterTest.FORWARD_EXTENSION + "CCCC");
            final String primer2 = ConstructBeanReader.readConstruct(construct2).getFwdPrimer();
            Assert.assertFalse(primer1.equals(primer2));

        } finally {
            wv.abort();
        }
    }

    public void testOwnership() throws AbortedException, ConstraintException, AccessException,
        ServletException, ClassNotFoundException, SQLException {

        WritableVersion wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        String hook = null;
        try {
            final Molecule protein = new Molecule(wv, "protein", "test" + System.currentTimeMillis());
            final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME + "o", protein);
            final LabNotebook owner = new LabNotebook(wv, ConstructBeanWriterTest.UNIQUE);
            target.setAccess(owner);
            final ResearchObjective eb =
                ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, new User(wv,
                    ConstructBeanWriterTest.SCIENTIST), "", ConstructBeanWriterTest.FORWARD_EXTENSION);
            Assert.assertEquals(ConstructBeanWriterTest.CONSTRUCT_ID, eb.getCommonName());
            Assert.assertEquals(ConstructBeanWriterTest.COMMENTS, eb.getDetails());
            Assert.assertEquals(ConstructBeanWriterTest.DESCRIPTION, eb.getFunctionDescription());
            Assert.assertNotNull(eb.getOwner());
            Assert.assertEquals(owner, eb.getAccess());
            hook = eb.get_Hook();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // test again after commit
        wv = this.model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
        try {
            final ResearchObjective ro = wv.get(hook);
            Assert.assertNotNull("not saved", ro);
            Assert.assertEquals(ConstructBeanWriterTest.CONSTRUCT_ID, ro.getCommonName());
            Assert.assertEquals(ConstructBeanWriterTest.COMMENTS, ro.getDetails());
            Assert.assertEquals(ConstructBeanWriterTest.DESCRIPTION, ro.getFunctionDescription());
            Assert.assertNotNull(ro.getOwner());

            Assert.assertEquals(1, ro.getExperiments().size());
            final Experiment experiment = ro.getExperiments().iterator().next();
            Assert.assertEquals(ro.getName() + "-SCD administrator 1", experiment.getName());

            // now tidy up
            final LabNotebook owner = ro.getAccess();
            final Target target = ro.getResearchObjectiveElements().iterator().next().getTarget();
            this.deleteTarget(wv, target);
            this.deleteConstruct(wv, ro);
            final Collection<LabBookEntry> others =
                wv.findAll(LabBookEntry.class, LabBookEntry.PROP_ACCESS, owner);
            wv.delete(others);
            owner.delete();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * Write the specified ConstructBean to the specified WritableVersion, returning the ResearchObjective
     * that is the key object for the ConstructBean
     * 
     * @param wv
     * @param cb
     * @return
     * @throws ConstraintException
     * @throws ServletException
     * @throws AccessException
     */
    public static ResearchObjective createConstructBean(final WritableVersion wv) throws ConstraintException,
        AccessException {
        final Molecule protein = new Molecule(wv, "protein", "test" + System.currentTimeMillis());
        final Molecule dna = new Molecule(wv, "DNA", "testDna" + System.currentTimeMillis());
        dna.setSequence(ConstructBeanWriterTest.DNA_SEQUENCE);
        final Target target = new Target(wv, ConstructBeanWriterTest.TARGET_NAME, protein);
        target.addNucleicAcid(dna);

        return ConstructBeanWriterTest.createConstructBeanForTarget(wv, target, new User(wv,
            ConstructBeanWriterTest.SCIENTIST), "", ConstructBeanWriterTest.FORWARD_EXTENSION);
    }

    public static ResearchObjective createConstructBeanForTarget(final WritableVersion wv,
        final Target target, final User user, final String suffix, final String forwardExtension)
        throws ConstraintException, AccessException {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructBeanWriterTest.FWD_PRIMER);
        forward.setLengthOnGeneString(Integer.toString(ConstructBeanWriterTest.FWD_OVERLAP.length()));
        forward.setExtension(forwardExtension);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence(ConstructBeanWriterTest.REV_PRIMER);
        reverse.setLengthOnGeneString(Integer.toString(ConstructBeanWriterTest.REV_OVERLAP.length()));
        reverse.setTag(ConstructBeanWriterTest.REVERSE_TAG);

        final ConstructBean cb = new ConstructBean(new TargetBean(target), forward, reverse);

        cb.setConstructId(ConstructBeanWriterTest.CONSTRUCT_ID + suffix);
        Assert.assertEquals("full" + suffix, cb.getNameSuffix());
        cb.setDnaSeq(ConstructBeanWriterTest.DNA_SEQUENCE); //TODO set in target
        cb.setProtSeq("null");
        cb.setDescription(ConstructBeanWriterTest.DESCRIPTION);
        cb.setComments(ConstructBeanWriterTest.COMMENTS);
        //was cb.setForwardTag(ConstructBeanWriterTest.FORWARD_TAG);
        //was cb.setReverseTag(ConstructBeanWriterTest.REVERSE_TAG);
        cb.setExpressedProt(ConstructBeanWriterTest.EXPRESSED_PROTEIN);
        cb.setFinalProt(ConstructBeanWriterTest.FINAL_PROTEIN);
        cb.setTargetProtStart(2);
        cb.setTargetProtEnd(243);
        // was cb.setPersonName(SCIENTIST);
        cb.setUserHook(user.get_Hook());
        // cb.setDateOfEntry(DateFormat.getDateInstance(DateFormat.MEDIUM,
        // Locale.UK).parse(((AppDataString) a).getValue()));
        cb.setDateOfEntry(new Date());
        cb.setPcrProductSeq(ConstructBeanWriterTest.PCR_PRODUCT_SEQ);

        // Write the ExpBlueprint
        final ResearchObjective wveb = ConstructBeanWriter.createNewConstruct(wv, cb);
        return wveb;
    }

    /**
     * @param wv
     * @param target
     * @throws AccessException
     * @throws ConstraintException
     */
    private void deleteTarget(final WritableVersion wv, final Target target) throws AccessException,
        ConstraintException {
        wv.delete(target.getExternalDbLinks());
        wv.delete(target.getMilestones());
        wv.delete(target.getNucleicAcids());
        target.delete();
        target.getProtein().delete();
    }

    /**
     * @param wv
     * @param eb
     * @throws AccessException
     * @throws ConstraintException
     */
    private void deleteConstruct(final WritableVersion wv, final ResearchObjective eb)
        throws AccessException, ConstraintException {

        final Set<ResearchObjectiveElement> elements = eb.getResearchObjectiveElements();
        for (final Iterator iterator = elements.iterator(); iterator.hasNext();) {
            final ResearchObjectiveElement roe = (ResearchObjectiveElement) iterator.next();
            wv.delete(roe.getTrialMolComponents());
            wv.delete(roe.getTrialMolecules());
        }

        final Set<Experiment> experiments = eb.getExperiments();
        for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            final Experiment experiment = (Experiment) iterator.next();
            final Set<OutputSample> oss = experiment.getOutputSamples();
            for (final Iterator iterator2 = oss.iterator(); iterator2.hasNext();) {
                final OutputSample os = (OutputSample) iterator2.next();
                final Sample sample = os.getSample();
                if (null != sample) {
                    final Set<SampleComponent> components = sample.getSampleComponents();
                    for (final Iterator iterator3 = components.iterator(); iterator3.hasNext();) {
                        final SampleComponent component = (SampleComponent) iterator3.next();
                        final Substance molecule = component.getRefComponent();
                        component.delete();
                        molecule.delete();
                    }
                    sample.delete();
                }
            }
            experiment.delete();
        }
        wv.delete(eb.getMilestones());
        if (null != eb.getOwner()) {
            eb.getOwner().delete();
        }
        eb.delete();
    }

    /**
     * @param cb
     */
    static void checkBeanProperties(final ConstructBean cb) {

        Assert.assertEquals(1, cb.getTargetBeans().size());
        final TargetBean tb = cb.getTargetBeans().iterator().next();
        Assert.assertNotNull(tb.getHook());
        Assert.assertEquals(Target.class.getName(), tb.getClassName());
        Assert.assertEquals(ConstructBeanWriterTest.COMMENTS, cb.getComments());
        Assert.assertEquals(ConstructBeanWriterTest.DESCRIPTION, cb.getDescription());
        Assert.assertEquals(ConstructBeanWriterTest.SCIENTIST, cb.getUserName());

        // was Assert.assertTrue(cb.getFwdPrimerHook(), cb.getFwdPrimerHook().endsWith("seqString"));
        // was Assert.assertTrue(cb.getRevPrimerHook().endsWith("seqString"));
        /* was Assert.assertEquals(50.0f, cb.getRevOverlapGc());
        Assert.assertNotNull(cb.getFwdOverlapGc());
        Assert.assertNotNull(cb.getFwdOverlapTm());
        Assert.assertNotNull(cb.getRevOverlapTm()); */
        Assert.assertEquals(ConstructBeanWriterTest.PCR_PRODUCT_SEQ, cb.getPcrProductSeq());
        Assert.assertNotNull(cb.getPcrProductGC());
        Assert.assertNotNull(cb.getFinalProt());
        Assert.assertEquals(ConstructBeanWriterTest.FINAL_PROTEIN, cb.getFinalProt());
        Assert.assertEquals(ConstructBeanWriterTest.EXPRESSED_PROTEIN, cb.getExpressedProt());
    }

}
