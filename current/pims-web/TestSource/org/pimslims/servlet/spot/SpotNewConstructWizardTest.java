/**
 * current-pims-web org.pimslims.servlet.spot SpotNewConstructWizardTest.java
 * 
 * @author cm65
 * @date 27 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.pimslims.access.Access;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.file.CaliperFile;
import org.pimslims.lab.primer.YorkPrimerBean;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.construct.ConstructBeanWriterTest;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.PIMSServlet;

/**
 * SpotNewConstructWizardTest
 * 
 */
@Deprecated
// obsolete
public class SpotNewConstructWizardTest extends TestCase {

    /**
     * DNA_TARGET_SEQUENCE String
     */
    private static final String DNA_TARGET_SEQUENCE = "ATGGAGGTCCTTAAATTGCAGTTAAGACAATGGCAGGCG";

    /**
     * TARGET_DNA String
     */
    private static final String TARGET_DNA =
        new DnaSequence(
            "ATGGAGGTCCTTAAATTGCAGTTAAGACAATGGCAGGCGGAGAAATTAGGCGAAGCAATTAATGCTTTAAAGCATGGCAAGACTCTTTTGCTTAACGCAA\r\n"
                + "AGCCCGGCTTAGGTAAGACGGTCTTCGTTGAAGTATTAGGTATGCAACTGAAAAAGAAGGTTCTGATATTTACCAGAACACATAGTCAGTTGGACAGCAT\r\n"
                + "TTACAAGAACGCCAAACTTTTGGGCTTAAAGACGGGCTTTCTGATAGGTAAGTCAGCTTCATGTATTTACGCTCAGGGTGATGAGGAACCAGATGAAATA\r\n"
                + "AACTGTAGTAAGTGCAGACTGAAGGATAAAATAAAGACAATAGAGGACAAAGAGCCTTCAAAACTAATAGAGGAGTTTAAAGATGCTGTAGATTACTGTC\r\n"
                + "CCTATTATTCATTAAGGGCAAACCTTAAGGATAAGGACGTAATAGCGATGACTTATCCATATTTATTTCAGAAACCGATAAGGAATTCCGTCTTCTGTAA\r\n"
                + "TAAGGATGATTGCTTAAAGCTTGAGGACTATCTGATCGTAATAGACGAAGCCCATAATTTGCTTGAAGCGGATAAGTGGTTTACGAGGAAAATAAGTCGT\r\n"
                + "AAAATGTTGGAGAGAGCGTTAAAAGAGATAGAAATTGTCGAAAGGCTTAACAGAATAGATGCGAAGAAGGTAAAAGACTACATCAACTTGCTTATCGATT\r\n"
                + "ATATGAGTAAACTAATCAAGGACGGGAGGTGTCACGAATTATCATTAATGCCATTACCAGATAGGGAAACAAACGGCGAGTTAATAGTAGTAACCAGAGC\r\n"
                + "GTATTTAAATATAGATGAGGGACCGGTAAAGAAGTCAAGTTTGAAGTCGTTACTAAAGTTTGTTGAGATGAAAGGTGATCTGTATAACTGCAACGGAAGT\r\n"
                + "CTGGTTAAAGTACCTTCAGACGTTAACCAATTAATTGAAGACGCACTTAACGTTAAGACGTTTAAAGTTTTGATGAGCGGTACTTTACCCGAATCTTTAA\r\n"
                + "CACTTACAAATTCGTACAAGATTGTAGTAAACGAGAGTTACGGAAGGGGAGAGTACTATTACTGCCCAAACGTAACTTCGGAACTCCGTAAGAGGAACTC\r\n"
                + "CAACATCCCCATTTACTCCATATTGCTTAAGAGGATTTATGAAAACAGCAGTAAGAGTGTTCTTGTTTTCTTTCCCAGCTACGAAATGTTAGAAAGTGTC\r\n"
                + "AGAATCCATCTATCGGGAATTCCGGTTATTGAGGAGAATAAAAAGACCAGACACGAAGAGGTTTTAGAGCTGATGAAGACCGGCAAATATCTCGTAATGT\r\n"
                + "TGGTAATGCGTGCTAAAGAGAGTGAGGGAGTGGAGTTTAGAGAAAAGGAGAACCTATTTGAAAGTTTAGTGCTTGCAGGATTGCCTTATCCTAACGTTTC\r\n"
                + "GGATGATATGGTAAGGAAGAGAATAGAGAGGCTGAGCAAATTAACTGGTAAAGATGAGGACTCAATAATACACGACTTAACTGCAATAGTGATTAAGCAG\r\n"
                + "ACTATCGGCAGGGCTTTTAGGGATCCTAACGATTATGTTAAGATTTACCTTTGTGATTCAAGGTATAGGGAATATTTTGCTGACCTGGGTATTTCCGAAA\r\n"
                + "AAGAGATTAAGCTTTTTGCATAG").getSequence();

    /**
     * FORWARD_OVERLAP String
     */
    private static final String FORWARD_OVERLAP = "ATGGAGGTCCTTAAATTGCAGTTAAG";

    private static final float FORWARD_OVERLAP_TM = 60.07f;

    /**
     * REVERSE_OVERLAP String
     */
    private static final String REVERSE_OVERLAP = "TGCAAAAAGCTTAATCTCTTTTTCGGAAAT";

    private static final float REVERSE_OVERLAP_TM = 59.93f;

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    /**
     * 
     */
    private static final String CONSTRUCT_NAME = "construct_" + SpotNewConstructWizardTest.UNIQUE;

    private static final String COMMENTS = "comments" + SpotNewConstructWizardTest.UNIQUE;

    private static final String DETAILS = "details" + SpotNewConstructWizardTest.UNIQUE;

    private static final String REVERSE_EXTENSION = "CCC";

    private static final String FORWARD_EXTENSION = "GGGG";

    private static final String FORWARD_TAG = "CX";

    private static final String REVERSE_TAG = "NX";

    private final AbstractModel model;

    /**
     * @param name
     */
    public SpotNewConstructWizardTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testDoGetNoTarget() throws ServletException, IOException {
        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        //parms.put("target_dna_seq", new String[] { "atgaaa" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("nonesuch");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    }

    public final void testDoGetProteinTarget() throws ServletException, IOException, ConstraintException,
        AbortedException, AccessException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String targetHook = null;
        try {
            final Molecule protein =
                new Molecule(version, "protein", "prot" + SpotNewConstructWizardTest.UNIQUE);
            final Molecule dna = new Molecule(version, "DNA", "dna" + SpotNewConstructWizardTest.UNIQUE);
            dna.setSequence("ATGAAA");
            final Target target = new Target(version, "pt" + SpotNewConstructWizardTest.UNIQUE, protein);
            target.addNucleicAcid(dna);
            targetHook = target.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        //parms.put("target_dna_seq", new String[] { "atgaaa" });
        final MockHttpServletRequest request = new MockHttpServletRequest("get", parms);
        request.setPathInfo("/" + targetHook);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);
        Assert.assertNotNull(request.getAttribute("constructBean"));
        final List<String> chunks = (List<String>) request.getAttribute("chunks");
        Assert.assertEquals(1, chunks.size());
        Assert.assertEquals("ATGAAA", chunks.get(0));

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Target target = version.get(targetHook);
            final Molecule protein = target.getProtein();
            version.delete(target.getNucleicAcids());
            target.delete();
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /*
     * to test step 1 -processed wizard step 2
     */
    public void test1dna() throws ServletException, IllegalSymbolException, IllegalAlphabetException,
        ConstraintException, AccessException, AbortedException, IOException, ClassNotFoundException,
        SQLException {
        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String> parms = new HashMap<String, String>();
        parms.put("wizard_step", "1dna");

        final List<YorkPrimerBean> rprimerBeans = new ArrayList<YorkPrimerBean>();
        final List<YorkPrimerBean> primerBeans = new ArrayList<YorkPrimerBean>();
        final ConstructBean cb =
            new ConstructBean(null, new PrimerBean(PrimerBean.FORWARD), new PrimerBean(PrimerBean.REVERSE));
        parms.put("desired_tm", "55.0");
        parms.put("submit", "change");
        System.out.println("DesiredTm is: " + cb.getDesiredTm());
        //cb.setDesiredTm(65.0f);
        cb.setDnaSeq("GTAGTCGTCGTGTAGTCGTC" + "GTGTAGTCGTCGTGTAGTCG" + "TCGTGTAGTCGTCGTGTAGT"
            + "CGTCGTGTAGTCGTCGTGTA" + "GTCGTCGTGTGTAGTCGTCG" + "TGTAGTCGTCGTGTAGTCGT" + "CGTGTAGT");
        cb.setTargetDnaStart(3);
        cb.setTargetDnaEnd(56);
        CreateExpressionObjective.designPrimers(parms, cb, primerBeans, rprimerBeans);
        //System.out.println("DesiredTm is now: " + cb.getDesiredTm());
        //System.out.println("Tm in ParameterMap is: " + parms.get("desired_tm"));
        //System.out.println("Tm in ConstructBean is: " + cb.getDesiredTm());

        Assert.assertTrue(0 < primerBeans.size());
        Assert.assertTrue(0 < rprimerBeans.size());
    }

    /**
     * SpotNewConstructWizardTest.testStep1 Defines the expression objective and desired TM
     * 
     * @throws ServletException
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     * @throws AbortedException
     */
    public final void testStep1() throws ServletException, IOException, ConstraintException, AccessException,
        AbortedException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String targetHook = null;
        String personHook = null;
        try {
            final Molecule protein =
                new Molecule(version, "protein", SpotNewConstructWizardTest.UNIQUE + "ps1");
            final Molecule dna = new Molecule(version, "DNA", SpotNewConstructWizardTest.UNIQUE + "dS1");
            dna.setSequence(SpotNewConstructWizardTest.TARGET_DNA);
            final Target target = new Target(version, SpotNewConstructWizardTest.UNIQUE + "One", protein);
            target.addNucleicAcid(dna);
            targetHook = target.get_Hook();
            final Person person = new Person(version, SpotNewConstructWizardTest.UNIQUE);
            personHook = person.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("target_prot_start", new String[] { "1" });
        parms.put("target_prot_end",
            new String[] { Integer.toString(SpotNewConstructWizardTest.TARGET_DNA.length() / 3) });
        parms.put("wizard_step", new String[] { "1" });
        parms.put("design_type", new String[] { "York" });

        parms.put("pims_target_hook", new String[] { targetHook });
        parms.put("personHook", new String[] { personHook });
        parms.put("construct_id", new String[] { SpotNewConstructWizardTest.CONSTRUCT_NAME });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });

        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);

        // test response
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/spot/SpotNewConstructWizardStep2c.jsp", request.getDispatchedPath());
        final ConstructBean cb = (ConstructBean) request.getAttribute("constructBean");
        Assert.assertEquals(SpotNewConstructWizardTest.TARGET_DNA, cb.getDnaSeq());
        Assert.assertEquals(SpotNewConstructWizardTest.TARGET_DNA.length(), cb.getReverseSeq().length());
        Assert.assertEquals(60f, cb.getDesiredTm());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            this.checkNoConstructAndDelete(version, targetHook, personHook);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /* 
     * _anchor=box2&construct_id=less2&target_prot_start=1&target_prot_end=257&labNotebookId=org.pimslims.model.core.AccessObject%3A14603&desired_tm=60
     * &Submit=Save+Construct
     * &pims_target_hook=org.pimslims.model.target.Target%3A15549
     * &target_prot_seq=VSILSLVGLGISKKFITDSAIETLSNSDIIIFDRYTSRSCDINVDVLRRLVKGEREFIEADRSLLENNSKAIIDYLDKGYNVSIASIGDALIATTHVSLLIEAKHRGHEVKVIPGISVHCYLISKSLLSSYKFGKSVTVTFPYNDFIDPTPYNVIKDNKERGLHTILYLDLKNEKAMTANEALQILLRLEERHKKSVLSKSDIIIVGARLGCDDERIIALKVEEATSFDFGNTPHIIIIPGNLHYMEADAIKWILRS
     * &target_dna_seq=GTGAGCATCTTAAGTCTAGTAGGTCTTGGAATCTCTAAGAAATTCATAACTGATAGTGCTATAGAGACACTAAGTAATTCTGATATTATAATTTTTGATAGATATACATCTAGATCTTGTGATATAAATGTGGACGTATTAAGGAGATTGGTAAAGGGAGAGAGAGAATTCATTGAAGCGGATAGGAGTTTATTAGAAAATAACTCAAAGGCTATCATAGACTATTTAGACAAGGGTTATAATGTAAGTATAGCCAGTATTGGAGATGCATTAATAGCCACAACCCATGTTTCGCTACTTATAGAGGCTAAACATCGAGGACATGAGGTAAAAGTGATTCCAGGGATTTCAGTACATTGCTATCTTATATCAAAGTCTCTATTATCTTCCTATAAATTTGGTAAGTCAGTTACAGTTACATTTCCTTATAACGATTTCATAGATCCTACTCCCTATAATGTGATTAAGGATAACAAAGAAAGAGGTCTTCATACCATATTATATCTGGATTTGAAAAACGAGAAAGCAATGACTGCTAACGAAGCACTTCAGATCTTATTACGACTTGAAGAGAGGCATAAGAAAAGTGTTCTTTCAAAATCTGATATTATAATAGTTGGTGCTAGATTAGGGTGTGATGACGAGAGAATTATTGCACTTAAAGTAGAAGAAGCTACTTCATTCGACTTCGGAAATACACCACATATTATTATAATTCCTGGTAACCTTCATTATATGGAGGCTGATGCTATAAAATGGATATTGAGGAGT
     * &wizard_step=1
     * */
    public void testPrimerless() throws AccessException, ConstraintException, AbortedException,
        ServletException, IOException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String targetHook = null;
        String userHook = null;
        try {
            final Molecule protein =
                new Molecule(version, "protein", SpotNewConstructWizardTest.UNIQUE + "p1");
            final Molecule dna = new Molecule(version, "DNA", SpotNewConstructWizardTest.UNIQUE + "dnaS1");
            dna.setSequence(SpotNewConstructWizardTest.TARGET_DNA);
            final Target target = new Target(version, SpotNewConstructWizardTest.UNIQUE + "One", protein);
            target.addNucleicAcid(dna);
            targetHook = target.get_Hook();
            final User person = new User(version, SpotNewConstructWizardTest.UNIQUE);
            userHook = person.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("target_prot_start", new String[] { "2" });
        parms.put("target_prot_end",
            new String[] { Integer.toString(SpotNewConstructWizardTest.TARGET_DNA.length() / 3) });
        parms.put("target_prot_seq", new String[] { ConstructBeanWriterTest.EXPRESSED_PROTEIN });
        parms.put("wizard_step", new String[] { "1" });
        parms.put("design_type", new String[] { "York" });

        parms.put("pims_target_hook", new String[] { targetHook });
        parms.put("personHook", new String[] { userHook });
        parms.put("construct_id", new String[] { SpotNewConstructWizardTest.CONSTRUCT_NAME });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });
        parms.put("Submit", new String[] { "Save Construct" });

        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            // test it was done OK
            final Target target = version.get(targetHook);
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final ResearchObjectiveElement element = bcs.iterator().next();
            final ResearchObjective construct = element.getResearchObjective();
            Assert.assertTrue(construct.getCommonName().endsWith(SpotNewConstructWizardTest.CONSTRUCT_NAME));
            final ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            final User user = version.get(userHook);
            Assert.assertEquals(userHook, bean.getUserHook());
            //TODO Assert.assertFalse("".equals(bean.getFinalProt()));

            Assert.assertEquals(4, element.getTrialMolComponents().size());
            Molecule insert = null;
            Molecule expressed = null;
            for (final Molecule bc : element.getTrialMolComponents()) {
                for (final ComponentCategory category : bc.getCategories()) {
                    if (CaliperFile.PCR_PRODUCT.equals(category.getName())) {
                        insert = bc;
                    }
                    if (ConstructBean.EXPRESSED_PROTEIN.equals(category.getName())) {
                        expressed = bc;
                    }
                }
            }
            Assert.assertNotNull(insert);
            Assert.assertEquals(SpotNewConstructWizardTest.TARGET_DNA.substring(3), insert.getSequence());
            Assert.assertNotNull(expressed);
            //TODO Assert.assertEquals(----, expressed.getSequence());

            this.delete(version, target, construct, user);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /*_anchor=box2&construct_id=test&target_dna_start=1&target_dna_end=230&dna_target=dnaTarget&pims_target_hook=org.pimslims.model.target.Target%3A3236028&target_name=testDnaTarget
     * &target_dna_seq=AAAACTGGGGACCGGTCAGTTTTTTTTACCCCCCCCGTTTTTTTTTAAAACTGGGGACCGGTCAGTTTTTTTTACCCCCCCCGTTTTTTTTTAAAACTGGGGACCGGTCAGTTTTTTTTACCCCCCCCGTTTTTTTTTAAAACTGGGGACCGGTCAGTTTTTTTTACCCCCCCCGTTTTTTTTTAAAACTGGGGACCGGTCAGTTTTTTTTACCCCCCCCGTTTTTTTTT
    &wizard_step=1dna&Submit=Next+%3E%3E%3E */
    public final void testPostStep1dna() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String targetHook = null;
        String personHook = null;
        try {
            final Molecule dna = new Molecule(version, "DNA", SpotNewConstructWizardTest.UNIQUE + "dna1");
            dna.setSequence(SpotNewConstructWizardTest.TARGET_DNA);
            final Target target = new Target(version, SpotNewConstructWizardTest.UNIQUE + "OneDna", dna);
            targetHook = target.get_Hook();
            final Person person = new Person(version, SpotNewConstructWizardTest.UNIQUE);
            personHook = person.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final CreateExpressionObjective servlet = new CreateExpressionObjective();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("target_dna_start", new String[] { "1" });
        parms.put("target_dna_end",
            new String[] { Integer.toString(SpotNewConstructWizardTest.TARGET_DNA.length()) });
        parms.put("wizard_step", new String[] { "1dna" });
        parms.put("dna_target", new String[] { "dnaTarget" });
        //parms.put("design_type", new String[] { "York" });

        parms.put("pims_target_hook", new String[] { targetHook });
        parms.put("personHook", new String[] { personHook });
        parms.put("construct_id", new String[] { SpotNewConstructWizardTest.CONSTRUCT_NAME });
        parms.put("comments", new String[] { SpotNewConstructWizardTest.COMMENTS });
        parms.put("description", new String[] { SpotNewConstructWizardTest.DETAILS });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });

        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);

        // test response
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/dnaTarget/DNAConstructWizardStep2.jsp", request.getDispatchedPath());
        final ConstructBean cb = (ConstructBean) request.getAttribute("constructBean");
        Assert.assertEquals(SpotNewConstructWizardTest.TARGET_DNA, cb.getDnaSeq());
        Assert.assertEquals(SpotNewConstructWizardTest.TARGET_DNA.length(), cb.getReverseSeq().length());
        Assert.assertEquals(60f, cb.getDesiredTm());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            this.checkNoConstructAndDelete(version, targetHook, personHook);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    private void checkNoConstructAndDelete(final WritableVersion version, final String targetHook,
        final String personHook) throws AccessException, ConstraintException {
        final Target target = version.get(targetHook);
        final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
        Assert.assertEquals(0, bcs.size());
        final Person person = version.get(personHook);
        final Molecule protein = target.getProtein();
        version.delete(target.getNucleicAcids());
        target.delete();
        protein.delete();
        person.delete();
    }

    private void delete(final WritableVersion version, final Target target,
        final ResearchObjective construct, final User user) throws AccessException, ConstraintException {
        if (null != construct) {
            final Set<Experiment> experiments = construct.getExperiments();
            for (final Iterator iterator = experiments.iterator(); iterator.hasNext();) {
                final Experiment experiment = (Experiment) iterator.next();
                final Set<OutputSample> outputSamples = experiment.getOutputSamples();
                for (final Iterator iterator2 = outputSamples.iterator(); iterator2.hasNext();) {
                    final OutputSample outputSample = (OutputSample) iterator2.next();
                    outputSample.getSample().delete();
                }
                experiment.delete();
            }
            construct.delete();
        }
        final Molecule protein = target.getProtein();
        version.delete(target.getNucleicAcids());
        target.delete();
        protein.delete();
        if (null != user) {
            user.delete();
        }
    }

    @Deprecated
    // now in AddPrimersTest
    private void checkPrimers(final ConstructBean bean) {
        final List<PrimerBean> primers = bean.getPrimers();
        Assert.assertEquals(2, primers.size());
        final Iterator<PrimerBean> primerIterator = primers.iterator();
        PrimerBean primer = primerIterator.next();
        Assert.assertEquals(SpotNewConstructWizardTest.FORWARD_OVERLAP, primer.getOverlap());
        Assert.assertEquals(SpotNewConstructWizardTest.FORWARD_OVERLAP.length(), primer.getOverlapLength());
        Assert.assertNotNull(primer.getTmfullasFloat());
        Assert.assertEquals(SpotNewConstructWizardTest.FORWARD_OVERLAP_TM, primer.getTmGeneFloat(), .01f);
        Assert.assertEquals(SpotNewConstructWizardTest.FORWARD_EXTENSION, primer.getExtension());
        primer = primerIterator.next();
        Assert.assertEquals(SpotNewConstructWizardTest.REVERSE_OVERLAP, primer.getOverlap());
        Assert.assertEquals(SpotNewConstructWizardTest.REVERSE_OVERLAP.length(), primer.getOverlapLength());
        Assert.assertEquals(SpotNewConstructWizardTest.REVERSE_OVERLAP_TM, primer.getTmGeneFloat(), .01f);
        //TODO Assert.assertEquals(SpotNewConstructWizardTest.REVERSE_TAG, primer.getTag());
    }

    /* 
     * Example parameters:
     * forward_extension=CATG&reverse_extension=GCAATTCGCTCGAGTCA
     * &dna_target=dnaTarget&pims_target_hook=org.pimslims.model.target.Target%3A351965
    &target_dna_seq=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    &target_dna_start=1&target_dna_end=110
    &construct_id=001030dna.asdfsdfa
    &desired_tm=60.0
    &forward_primer=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    &reverse_primer=TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
    &Submit=Next+%3E%3E%3E
     * */

    /* Example parameters:
     * 
     * 
     *  reverse_extension=, 
     *  target_name=001030dna, 
     *  reverse_primer=TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT, 
     *  target_dna_seq=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAATAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA, 
     *  target_dna_end=111, pims_target_hook=org.pimslims.model.target.Target:351965, 
     *  target_dna_start=1, wizard_step=4dna, construct_id=001030dna.dna, 
     *  forward_primer=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA, 
     *  personHook=org.pimslims.model.people.Person:62446, new_fExtName=, dna_target=dnaTarget, 
     *  fwd_overlap=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA, description=, desired_tm=60.0, Submit=Save Construct, 
     *  rev_overlap=TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT, 
     *  _anchor=box4, forward_extension=, new_rExtName=, comments=, 
     * */
    /*
     * Save a DNA construct
     */
    public void xtestSaveDna() throws ServletException, IllegalSymbolException, IllegalAlphabetException,
        ConstraintException, AccessException, AbortedException, IOException, ClassNotFoundException,
        SQLException {

        WritableVersion version = this.model.getTestVersion();
        String targetHook = null;
        String userHook = null;
        try {
            final Molecule protein =
                new Molecule(version, "DNA", SpotNewConstructWizardTest.UNIQUE + "savedna");
            protein.setSequence(SpotNewConstructWizardTest.DNA_TARGET_SEQUENCE);
            final Target target = new Target(version, SpotNewConstructWizardTest.UNIQUE + "save", protein);
            target.setAliasNames(Collections.singleton(PIMSTarget.DNA_TARGET));
            targetHook = target.get_Hook();
            final User user = new User(version, SpotNewConstructWizardTest.UNIQUE);
            userHook = user.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final SpotNewConstructWizard servlet = new SpotNewConstructWizard();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("target_dna_seq", new String[] { SpotNewConstructWizardTest.DNA_TARGET_SEQUENCE });
        parms.put("target_dna_start", new String[] { "1" });
        parms.put("target_dna_end",
            new String[] { Integer.toString(SpotNewConstructWizardTest.DNA_TARGET_SEQUENCE.length()) });
        parms.put("wizard_step", new String[] { "4dna" });
        parms.put("dna_target", new String[] { "dnaTarget" });

        parms.put("pims_target_hook", new String[] { targetHook });
        parms.put("personHook", new String[] { userHook });
        parms.put("construct_id", new String[] { SpotNewConstructWizardTest.CONSTRUCT_NAME + "save" });

        parms.put("forward_primer", new String[] { SpotNewConstructWizardTest.FORWARD_OVERLAP });
        parms.put("reverse_primer", new String[] { SpotNewConstructWizardTest.REVERSE_OVERLAP });
        parms.put("forward_extension", new String[] { SpotNewConstructWizardTest.FORWARD_EXTENSION });
        parms.put("reverse_extension", new String[] { SpotNewConstructWizardTest.REVERSE_EXTENSION });
        parms.put(CreateExpressionObjective.N_TAG, new String[] { SpotNewConstructWizardTest.FORWARD_TAG });
        parms.put(CreateExpressionObjective.C_TAG, new String[] { SpotNewConstructWizardTest.REVERSE_TAG });

        parms.put("comments", new String[] { SpotNewConstructWizardTest.COMMENTS });
        parms.put("description", new String[] { SpotNewConstructWizardTest.DETAILS });

        final HttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());

        version = this.model.getTestVersion();
        try {
            // test it was done OK
            final Target target = version.get(targetHook);
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final ResearchObjectiveElement component = bcs.iterator().next();
            final ResearchObjective construct = component.getResearchObjective();
            Assert
                .assertEquals(SpotNewConstructWizardTest.CONSTRUCT_NAME + "save", construct.getCommonName());
            Assert.assertEquals(SpotNewConstructWizardTest.COMMENTS, construct.getDetails());
            final ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            final User user = version.get(userHook);
            Assert.assertEquals(SpotNewConstructWizardTest.COMMENTS, bean.getComments());
            Assert.assertEquals(SpotNewConstructWizardTest.DETAILS, bean.getDescription());
            //TODO assertEquals(person.getName(), bean.getPersonName());
            Assert.assertEquals(userHook, bean.getUserHook());
            Assert.assertFalse("".equals(bean.getFinalProt()));

            // check pcr product sequence
            Assert.assertEquals(SpotNewConstructWizardTest.FORWARD_EXTENSION
                + SpotNewConstructWizardTest.DNA_TARGET_SEQUENCE + "GGG", // reverse complement of SpotNewConstructWizardTest.REVERSE_EXTENSION, 
                bean.getPcrProductSeq());
            this.checkPrimers(bean);
            Assert.assertEquals(SpotNewConstructWizardTest.REVERSE_EXTENSION, bean.getReversePrimer()
                .getExtension());

            this.delete(version, target, construct, user);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
