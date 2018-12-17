/**
 * 
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
package org.pimslims.servlet.construct;

import java.io.IOException;
import java.sql.SQLException;
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
import org.pimslims.lab.ConstructUtility;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.spot.CreateExpressionObjective;

public class AddPrimersTest extends TestCase {

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
    private static final String CONSTRUCT_NAME = "construct_" + AddPrimersTest.UNIQUE;

    private static final String COMMENTS = "comments" + AddPrimersTest.UNIQUE;

    private static final String DETAILS = "details" + AddPrimersTest.UNIQUE;

    private static final String REVERSE_EXTENSION = "CCC";

    private static final String FORWARD_EXTENSION = "GGGG";

    private static final String FORWARD_TAG = "CX";

    private static final String REVERSE_TAG = "NX";

    private final AbstractModel model;

    /**
     * @param name
     */
    public AddPrimersTest(final String name) {
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

    public void testProcessPrimers() throws IllegalSymbolException, IllegalAlphabetException {
        final ConstructBean cb = new ConstructBean(new TargetBean(), new PrimerBean(), new PrimerBean());
        cb.setDnaSeq("AAACCCTTTGGG");
        cb.setFwdOverlapLen(new Integer("CCCTTT".length()));
        cb.setFwdPrimer("CCCTTT");

        /* SymbolList symL = DNATools.createDNA(cb.getDnaSeq());
        symL = DNATools.reverseComplement(symL); */
        cb.setRevOverlapLen(new Integer("AAAGGG".length()));
        cb.setRevPrimer("AAAGGG");
    }

    /**
     * 
     * 
     * @throws IOException
     * @throws ServletException
     */
    public final void testDoPostNoTarget() throws ServletException, IOException {
        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        //parms.put("target_dna_seq", new String[] { "atgaaa" });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });
        final HttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    public void testGetOverlapForm() throws AccessException, ConstraintException, ServletException,
        AbortedException, IOException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String roeHook = null;
        try {
            final Molecule protein = new Molecule(version, "protein", AddPrimersTest.UNIQUE + "po");
            final Molecule dna = new Molecule(version, "DNA", AddPrimersTest.UNIQUE + "dnao");
            dna.setSequence(AddPrimersTest.TARGET_DNA);
            final Target target = new Target(version, AddPrimersTest.UNIQUE + "o", protein);
            target.addNucleicAcid(dna);
            final Project eo =
                new ResearchObjective(version, AddPrimersTest.UNIQUE + "o", AddPrimersTest.UNIQUE);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, AddPrimersTest.UNIQUE + "o", AddPrimersTest.UNIQUE, eo);
            roe.setTarget(target);
            roe.setApproxBeginSeqId(1);
            roe.setApproxEndSeqId(new Integer(dna.getSequence().length() / 3));
            roeHook = roe.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();

        // note no parms.put("forward_primer", new String[] { AddPrimersTest.FORWARD_OVERLAP });
        // note no parms.put("reverse_primer", new String[] { AddPrimersTest.REVERSE_OVERLAP });
        parms.put(AddPrimers.DESIRED_TM, new String[] { "70" });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo("/" + roeHook);
        servlet.doGet(request, response);

        // test response
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/construct/DesignOverlaps.jsp", request.getDispatchedPath());
        final ConstructBean cb = (ConstructBean) request.getAttribute("constructBean");
        // could Assert.assertNotNull(cb.getExpressedProt());
        // could Assert.assertNotNull(cb.getFinalProt());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ResearchObjectiveElement roe = version.get(roeHook);
            final Target target = version.get(roe.getTarget().get_Hook());
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final Molecule protein = target.getProtein();
            version.delete(target.getNucleicAcids());
            // could target.getCreator().delete();
            target.delete();
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    public final void testGetExtensionsForm() throws ServletException, IOException, ConstraintException,
        AccessException, AbortedException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String roeHook = null;
        try {
            final Molecule protein = new Molecule(version, "protein", AddPrimersTest.UNIQUE + "p2c");
            final Molecule dna = new Molecule(version, "DNA", AddPrimersTest.UNIQUE + "dna2c");
            dna.setSequence(AddPrimersTest.TARGET_DNA);
            final Target target = new Target(version, AddPrimersTest.UNIQUE + "2c", protein);
            target.addNucleicAcid(dna);
            final Project eo = new ResearchObjective(version, AddPrimersTest.UNIQUE, AddPrimersTest.UNIQUE);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, AddPrimersTest.UNIQUE, AddPrimersTest.UNIQUE, eo);
            roe.setTarget(target);
            roe.setApproxBeginSeqId(1);
            roe.setApproxEndSeqId(new Integer(dna.getSequence().length() / 3));
            roeHook = roe.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();

        parms.put("forward_primer", new String[] { AddPrimersTest.FORWARD_OVERLAP });
        parms.put("reverse_primer", new String[] { AddPrimersTest.REVERSE_OVERLAP });
        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo("/" + roeHook);
        servlet.doGet(request, response);

        // test response
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        Assert.assertEquals("/JSP/construct/DesignExtensions.jsp", request.getDispatchedPath());
        final ConstructBean cb = (ConstructBean) request.getAttribute("constructBean");
        Assert.assertNotNull(cb.getExpressedProt());
        Assert.assertNotNull(cb.getFinalProt());
        Assert.assertEquals(AddPrimersTest.FORWARD_OVERLAP, cb.getFwdPrimer());
        Assert.assertEquals(cb.getFwdPrimer().length(), cb.getFwdOverlapLen().intValue());
        Assert.assertNotNull(AddPrimersTest.REVERSE_OVERLAP, cb.getRevPrimer());
        Assert.assertEquals(cb.getRevPrimer().length(), cb.getRevOverlapLen().intValue());
        Assert.assertNotNull(cb.getPcrProductSeq());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final ResearchObjectiveElement roe = version.get(roeHook);
            final Target target = version.get(roe.getTarget().get_Hook());
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final Molecule protein = target.getProtein();
            version.delete(target.getNucleicAcids());
            // could target.getCreator().delete();
            target.delete();
            protein.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    //TODO test non-standard start codon

    private void delete(final WritableVersion version, final Target target, final ResearchObjective construct)
        throws AccessException, ConstraintException {
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
        if (null != target.getCreator()) {
            target.getCreator().delete();
        }
        target.delete();
        protein.delete();
    }

    private void checkPrimers(final ConstructBean bean) {
        final List<PrimerBean> primers = bean.getPrimers();
        Assert.assertEquals(2, primers.size());
        final Iterator<PrimerBean> primerIterator = primers.iterator();
        PrimerBean primer = primerIterator.next();
        Assert.assertEquals(AddPrimersTest.FORWARD_OVERLAP, primer.getOverlap());
        Assert.assertEquals(AddPrimersTest.FORWARD_OVERLAP.length(), primer.getOverlapLength());
        Assert.assertNotNull(primer.getTmfullasFloat());
        Assert.assertNotNull(primer.getTmGeneFloat());
        Assert.assertEquals(AddPrimersTest.FORWARD_OVERLAP_TM, primer.getTmGeneFloat(), .01f);
        Assert.assertEquals(AddPrimersTest.FORWARD_EXTENSION, primer.getExtension());
        primer = primerIterator.next();
        Assert.assertEquals(AddPrimersTest.REVERSE_OVERLAP, primer.getOverlap());
        Assert.assertEquals(AddPrimersTest.REVERSE_OVERLAP.length(), primer.getOverlapLength());
        Assert.assertEquals(AddPrimersTest.REVERSE_OVERLAP_TM, primer.getTmGeneFloat(), .01f);
        Assert.assertEquals(AddPrimersTest.REVERSE_TAG, primer.getTag());
    }

    public final void testSave() throws ServletException, IOException, ConstraintException, AccessException,
        AbortedException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String roeHook = null;
        try {
            final Molecule protein = new Molecule(version, "protein", AddPrimersTest.UNIQUE + "p");
            final Molecule dna = new Molecule(version, "DNA", AddPrimersTest.UNIQUE + "dna");
            dna.setSequence(AddPrimersTest.TARGET_DNA);
            final Target target = new Target(version, AddPrimersTest.UNIQUE, protein);
            target.addNucleicAcid(dna);
            // could final User user = new User(version, AddPrimersTest.UNIQUE + "save");
            final Project eo =
                new ResearchObjective(version, AddPrimersTest.CONSTRUCT_NAME, AddPrimersTest.UNIQUE);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, AddPrimersTest.UNIQUE, AddPrimersTest.UNIQUE, eo);
            roe.setTarget(target);
            roe.setApproxBeginSeqId(1);
            roe.setApproxEndSeqId(new Integer(dna.getSequence().length() / 3));
            roeHook = roe.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        // was parms.put("wizard_step", new String[] { "Save" });

        parms.put("forward_primer", new String[] { AddPrimersTest.FORWARD_OVERLAP });
        parms.put("reverse_primer", new String[] { AddPrimersTest.REVERSE_OVERLAP });
        parms.put("forward_extension", new String[] { AddPrimersTest.FORWARD_EXTENSION });
        parms.put("reverse_extension", new String[] { AddPrimersTest.REVERSE_EXTENSION });
        parms.put("add_stop", new String[] { "CTA" });
        parms.put(CreateExpressionObjective.N_TAG, new String[] { AddPrimersTest.FORWARD_TAG });
        parms.put(CreateExpressionObjective.C_TAG, new String[] { AddPrimersTest.REVERSE_TAG });

        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });

        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo("/" + roeHook);
        servlet.doPost(request, response);
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            // test it was done OK
            final Target target = ((ResearchObjectiveElement) version.get(roeHook)).getTarget();
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final ResearchObjectiveElement component = bcs.iterator().next();
            final ResearchObjective construct = component.getResearchObjective();
            Assert.assertEquals(AddPrimersTest.CONSTRUCT_NAME, construct.getCommonName());
            final ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            // could assertEquals(person.getName(), bean.getPersonName());
            // could Assert.assertEquals(userHook, bean.getUserHook());

            Assert.assertNotNull(bean.getExpressedProt());
            Assert.assertNotNull(bean.getFinalProt());
            Assert.assertEquals(AddPrimersTest.FORWARD_EXTENSION + AddPrimersTest.FORWARD_OVERLAP,
                bean.getFwdPrimer());
            Assert.assertEquals(AddPrimersTest.REVERSE_EXTENSION + "CTA" + AddPrimersTest.REVERSE_OVERLAP,
                bean.getRevPrimer());
            Assert.assertNotNull(bean.getPcrProductSeq());

            Assert.assertFalse("".equals(bean.getFinalProt()));
            this.checkPrimers(bean);

            Assert.assertEquals(AddPrimersTest.FORWARD_TAG, bean.getForwardTag());
            Assert.assertEquals(AddPrimersTest.REVERSE_EXTENSION + "CTA", bean.getReversePrimer()
                .getExtension());

            Assert.assertTrue(ConstructUtility.isSpotConstruct(construct));

            this.delete(version, target, construct);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public void test3dna() throws ServletException, ConstraintException, AccessException, AbortedException,
        IOException, ClassNotFoundException, SQLException {

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        String roeHook = null;
        try {
            final Molecule dna = new Molecule(version, "DNA", AddPrimersTest.UNIQUE + "dna3");
            dna.setSequence(AddPrimersTest.TARGET_DNA);
            final Target target = new Target(version, AddPrimersTest.UNIQUE + "d", dna);
            // was target.addNucleicAcid(dna); 
            target.setAliasNames(Collections.singleton(PIMSTarget.DNA_TARGET));
            //final User user = new User(version, AddPrimersTest.UNIQUE + "dsave");
            final Project eo =
                new ResearchObjective(version, AddPrimersTest.CONSTRUCT_NAME + "d", AddPrimersTest.UNIQUE);
            final ResearchObjectiveElement roe =
                new ResearchObjectiveElement(version, AddPrimersTest.UNIQUE + "d", AddPrimersTest.UNIQUE, eo);
            roe.setTarget(target);
            roe.setApproxBeginSeqId(1);
            roe.setApproxEndSeqId(new Integer(AddPrimersTest.TARGET_DNA.length() / 3));
            roeHook = roe.get_Hook();
            assert PIMSTarget.isDNATarget(roe.getTarget());
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("forward_extension", new String[] { AddPrimersTest.FORWARD_EXTENSION });
        parms.put("reverse_extension", new String[] { AddPrimersTest.REVERSE_EXTENSION });
        parms.put("dna_target", new String[] { "dnaTarget" });

        final String forwardOverlap = AddPrimersTest.DNA_TARGET_SEQUENCE.substring(0, 8);
        parms.put("forward_primer", new String[] { forwardOverlap });
        parms.put("reverse_primer", new String[] { "CTATGC" });
        parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { Access.REFERENCE });

        final MockHttpServletRequest request = new MockHttpServletRequest("post", parms);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setPathInfo("/" + roeHook);
        servlet.doPost(request, response);

        // test response
        Assert.assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            // test it was done OK
            final Target target = ((ResearchObjectiveElement) version.get(roeHook)).getTarget();
            final Set<ResearchObjectiveElement> bcs = target.getResearchObjectiveElements();
            Assert.assertEquals(1, bcs.size());
            final ResearchObjectiveElement component = bcs.iterator().next();
            final ResearchObjective construct = component.getResearchObjective();
            Assert.assertEquals(AddPrimersTest.CONSTRUCT_NAME + "d", construct.getCommonName());
            final ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            //final User user = version.get(userHook);

            Assert.assertEquals(forwardOverlap, bean.getFwdOverlap());
            Assert.assertEquals(AddPrimersTest.FORWARD_EXTENSION, bean.getForwardExtension());

            Assert.assertEquals("CTATGC", bean.getRevOverlap());
            Assert.assertEquals(AddPrimersTest.REVERSE_EXTENSION, bean.getReverseExtension());
            Assert.assertNull(bean.getExpressedProt());
            Assert.assertNull(bean.getFinalProt());
/* TODO
            Assert.assertEquals(FORWARD_EXTENSION
                + DNA_TARGET_SEQUENCE + "GGG", // reverse complement of .REVERSE_EXTENSION, 
                bean.getPcrProductSeq()); */
            //TODO this.checkPrimers(bean);
            Assert.assertEquals(AddPrimersTest.REVERSE_EXTENSION, bean.getReversePrimer().getExtension());

            this.delete(version, target, construct);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

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
            final Molecule protein = new Molecule(version, "DNA", AddPrimersTest.UNIQUE + "savedna");
            protein.setSequence(AddPrimersTest.DNA_TARGET_SEQUENCE);
            final Target target = new Target(version, AddPrimersTest.UNIQUE + "save", protein);
            target.setAliasNames(Collections.singleton(PIMSTarget.DNA_TARGET));
            targetHook = target.get_Hook();
            final User user = new User(version, AddPrimersTest.UNIQUE);
            userHook = user.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        final AddPrimers servlet = new AddPrimers();
        servlet.init(new MockServletConfig(this.model));
        final Map<String, String[]> parms = new HashMap<String, String[]>();
        parms.put("target_dna_seq", new String[] { AddPrimersTest.DNA_TARGET_SEQUENCE });
        parms.put("target_dna_start", new String[] { "1" });
        parms.put("target_dna_end",
            new String[] { Integer.toString(AddPrimersTest.DNA_TARGET_SEQUENCE.length()) });
        parms.put("wizard_step", new String[] { "4dna" });
        parms.put("dna_target", new String[] { "dnaTarget" });

        parms.put("personHook", new String[] { userHook });
        parms.put("construct_id", new String[] { AddPrimersTest.CONSTRUCT_NAME + "save" });

        parms.put("forward_primer", new String[] { AddPrimersTest.FORWARD_OVERLAP });
        parms.put("reverse_primer", new String[] { AddPrimersTest.REVERSE_OVERLAP });
        parms.put("forward_extension", new String[] { AddPrimersTest.FORWARD_EXTENSION });
        parms.put("reverse_extension", new String[] { AddPrimersTest.REVERSE_EXTENSION });
        parms.put(CreateExpressionObjective.N_TAG, new String[] { AddPrimersTest.FORWARD_TAG });
        parms.put(CreateExpressionObjective.C_TAG, new String[] { AddPrimersTest.REVERSE_TAG });

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
            Assert.assertEquals(AddPrimersTest.CONSTRUCT_NAME + "save", construct.getCommonName());
            Assert.assertEquals(AddPrimersTest.COMMENTS, construct.getDetails());
            final ConstructBean bean = ConstructBeanReader.readConstruct(construct);
            final User user = version.get(userHook);
            Assert.assertEquals(AddPrimersTest.COMMENTS, bean.getComments());
            Assert.assertEquals(AddPrimersTest.DETAILS, bean.getDescription());
            Assert.assertEquals(userHook, bean.getUserHook());
            Assert.assertFalse("".equals(bean.getFinalProt()));

            // check pcr product sequence
            Assert.assertEquals(
                AddPrimersTest.FORWARD_EXTENSION + AddPrimersTest.DNA_TARGET_SEQUENCE + "GGG", // reverse complement of REVERSE_EXTENSION, 
                bean.getPcrProductSeq());
            this.checkPrimers(bean);
            Assert.assertEquals(AddPrimersTest.REVERSE_EXTENSION, bean.getReversePrimer().getExtension());

            this.delete(version, target, construct);
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
