package org.pimslims.bioinf.local;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.alignment.SmithWaterman;
import org.biojava.bio.symbol.AlphabetManager;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Creator;
import org.pimslims.lab.StuffProducer;
import org.pimslims.model.molecule.Molecule;

/**
 * @author Petr Troshin aka pvt43
 */

public class SWSearchTester extends TestCase {

    // preload some classes, otherwise the performance tests are misleading
    static {
        try {
            Class.forName(SmithWaterman.class.getName());
            Class.forName(AlphabetManager.class.getName());
        } catch (final ClassNotFoundException e) {
            // can't run if these are missing
            throw new RuntimeException(e);
        }
    }

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion wv = null;

    private Molecule protein = null;

    private static final String PROTEIN_SEQUENCE = "DGDGLA";

    private static final String LONG_PROTEIN_SEQUENCE =
        "MLSEKYKFKN SLLTHNSQII KSLIFSVLLI GLLCAFLLDL ALGSVDIPIN EVVNILLGQE"
            + "PEKATHAHII LKFRLPKALT ATLAGAALGV SGLQMQTLFK NPLAGPFVLG ISSGASLGVA"
            + "LVVLTASVTT PTLLTDLGII SDFGLVIAAS LGAASVLGMM LVVSRRVQDT MTLLILGLLF"
            + "GYATSAIVSI LLQFSSKERI QNYIMWTFGS FAGVTWKQLI VLIPVIVLSL LVAVLQSKSL";

    private Molecule dna = null;

    private static final String DNA_SEQUENCE = "TTTCCC";

    /**
     * to save usual standard error stream
     */
    private PrintStream stderr = null;

    /**
     * To keep the data printed by aligner
     */
    private ByteArrayOutputStream quietOut = null;

    /**
     * Constructor for TestPlasmid.
     * 
     * @param methodName
     */
    public SWSearchTester(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    //

    void recordProtein() throws AccessException, ConstraintException, ClassNotFoundException {
        this.protein =
            Creator.recordProtein(this.wv, "protein" + StuffProducer.getlong(),
                SWSearchTester.PROTEIN_SEQUENCE);

    }

    void recordDNA() throws AccessException, ConstraintException, ClassNotFoundException {
        this.dna = Creator.recordDNA(this.wv, "dna" + StuffProducer.getlong(), SWSearchTester.DNA_SEQUENCE);
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws AccessException, ConstraintException, ClassNotFoundException {
        this.wv = this.model.getTestVersion();
        Assert.assertNotNull(this.wv);
        this.recordProtein();
        this.recordDNA();
        // The aligner library prints a lot to stderr,
        // enough be a nuisance
        this.stderr = System.err;
        this.quietOut = new ByteArrayOutputStream();
        final PrintStream quietPrint = new PrintStream(this.quietOut);
        System.setErr(quietPrint);
    }

    /*
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        Assert.assertFalse(this.wv.isCompleted());
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
        System.setErr(this.stderr);
        // to see output: System.out.println(quietOut.toString());
    }

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SWSearchTester.class);
    }

    public void testAlign() {
        SWSearch search = null;
        search = new SWSearch("Template" + System.currentTimeMillis(), SWSearchTester.PROTEIN_SEQUENCE);

        final String protSeq2 =
            "MLSEKYKFKN SLLTHNSQII KSLIFSVLLI GLLCAFLLDL ALGSVDIPIN EVVNILLGQE"
                + "PEKATHAHII LKFRLPKALT ATLAGAALGV SGLQMQTLFK NPLAGPFVLG ISSGASLGVA"
                + "LVVLTASVTT PTLLTDLGII SDFGLVIAAS LGAASVLGMM LVVSRRVQDT MTLLILGLLF"
                + "GYATSAIVSI LLQFSSKERI QNYIMWTFGS FAGVTWKQLI VLIPVIVLSL LVAVLQSKSL"
                + "NALLLGETYA RSLGLTVQKT RFSIITSASI LAGAITAFCG PIAFLGVAIP HLCRSLFNTS"
                + "DHRMLIPSVI IMGAILALIA DLFSQLWVSQ MVLPLNAITA LIGTPVVTWV ILRRNSQKSF" + "PS";
        Assert.assertEquals("Sequence string are equal", SWSearchTester.PROTEIN_SEQUENCE,
            this.protein.getSequence());
        final PimsAlignment alignment = search.align("protseq2", protSeq2, false, this.wv, null, false);
        // http: //www.timeshighereducation.co.uk/story.asp?sectioncode=26&storycode=402284&c=1
        Assert.assertNotNull(alignment);

        this.print(alignment);
        Assert.assertEquals("Identity", 2, alignment.getIdentity());
        Assert.assertEquals("Score", 11, alignment.getScore());
        Assert.assertEquals("Extend", 1, alignment.getExtensionPenalty());
        Assert.assertEquals("Gaps", 0, alignment.getGaps());
        //  System.out.println(aligment.getSummary());
    }

    private void print(final PimsAlignment alignment) {
        final String[] formatted = alignment.getFormatted();
        for (final String row : formatted) {
            System.out.println(row);
        }
    }

    public void testLongAlign() {
        SWSearch search = null;
        search = new SWSearch("Template" + System.currentTimeMillis(), SWSearchTester.LONG_PROTEIN_SEQUENCE);

        final PimsAlignment alignment =
            search.align("protseq2", SWSearchTester.LONG_PROTEIN_SEQUENCE, false, this.wv, null, false);
        Assert.assertEquals("Identity", 240, alignment.getIdentity());
        Assert.assertEquals("Gaps", 0, alignment.getGaps());

    }

    public void testSequenceGetter() {
        final Collection<Molecule> dnas = SequenceGetter.getDNAs(this.wv);
        Assert.assertNotNull(dnas);
        Assert.assertFalse(dnas.isEmpty());
    }

    /* this test depends on DB contented
    public void xtestAlignAll() {
        final Collection<Molecule> dnas = SequenceGetter.getDNAs(this.wv);
        Assert.assertNotNull(dnas);
        Assert.assertFalse(dnas.isEmpty());
        final long start = System.currentTimeMillis();
        SWSearch search = null;
        search = new SWSearch("test", this.dnaSeq);
        final Collection<PimsAlignment> alignmentws = search.align(dnas, true, wv);
        final long seconds = (System.currentTimeMillis() - start) / 1000;
        Assert.assertTrue("slow", seconds < 30);
        System.out.println(seconds + "s used for DB Multiple Align with " + dnas.size() + " DNAs");
        Assert.assertNotNull(alignmentws);
        Assert.assertFalse(alignmentws.isEmpty());
    } */

    /*TODO make this performance test more accurate
    public void xtestMultipleAlignFind() throws ConstraintException {
        final Molecule mc = new Molecule(this.wv, "DNA", "sws" + System.currentTimeMillis());
        mc.setSeqString(this.dnaSeq);
        final Collection<Molecule> dnas = Collections.singleton(mc);
        Assert.assertNotNull(dnas);
        Assert.assertFalse(dnas.isEmpty());
        SWSearch search = null;
        final long start = System.currentTimeMillis();
        search = new SWSearch("test", this.dnaSeq);
        final Collection<PimsAlignment> alignmentws = search.align(dnas, true, wv);
        final long ms = System.currentTimeMillis() - start;
        Assert.assertTrue("slow: " + ms + "ms", ms < 30);
        Assert.assertNotNull(alignmentws);
        Assert.assertFalse(alignmentws.isEmpty());
        /*
         * for (AlignmentWrapper alignw : alignmentws) { System.out.println(" All: " +
         * alignw.getAlignment().getScore()); }
       
    }  */

    public void testDefaultParameters() {
        //final Collection<Molecule> dnas = SequenceGetter.getDNAs(this.wv);
        //Assert.assertNotNull(dnas);
        //Assert.assertFalse(dnas.isEmpty());
        final SWSearch search = new SWSearch("Template", SWSearchTester.DNA_SEQUENCE);
        Assert.assertEquals(1, search.searchParam.extend);
        final PimsAlignment aligment =
            search.align("test", this.dna.getSequence(), false, this.wv, null, true);
        Assert.assertEquals(1, search.searchParam.extend);
        Assert.assertEquals("Identity", 6, aligment.getIdentity());
        Assert.assertEquals("Score", 6, aligment.getScore());
        Assert.assertEquals("Extend", 1, aligment.getExtensionPenalty());
        Assert.assertEquals("Gaps", 20, aligment.getOpeningPenalty());

    }

    public void testDnaIdentity() {
        final SWSearch search = new SWSearch("Template", SWSearchTester.DNA_SEQUENCE, "IDENTITY", 3, 1);
        final PimsAlignment alignment =
            search.align("Test", SWSearchTester.DNA_SEQUENCE, false, this.wv, null, true);
        this.print(alignment);
        Assert.assertEquals(SWSearchTester.DNA_SEQUENCE.length(), alignment.getIdentity());
        //Assert.assertEquals("IDENTITY", alignment.getMatrixId());
        Assert.assertEquals("Score", 6, alignment.getScore());
    }

/*
    public void testGetMatrixList() {
        SWSearch search = null;
        Collection<String> mxl = null;
        search = new SWSearch("Template", SWSearchTester.PROTEIN_SEQUENCE);
        mxl = AlignmentParameters.getMatrixList();

        for (final String mx : mxl) {
            // Test that all matrixes from list can be loaded
            search.setParameters(mx, 10f, 0.5f);
            Assert.assertNotNull(search);
        }
    } */

    /* Could do this, but biojava does not support similarity:
    public void testGetSimilarity() {
        SWSearch search = null;
        search = new SWSearch("Template", "AAAAKKKWWWCDDDD");
        search.setParameters("PAM200", 3f, 0.1f);
        // R is like K, C is not like M
        final PimsAlignment alw = search.align("Test", "AAAARRRWWWMDDDD", false, this.wv, null, false);
        Assert.assertEquals(14, alw.getSimilarity());
        Assert.assertEquals("93.3", alw.getPercentSimilarity());
    } */

    public void testGetPercentIdentity() {
        SWSearch search = null;
        //final Collection<String> mxl = null;
        search = new SWSearch("Template", "AAMMMMVVVV", "PAM200", 3, 1);
        final PimsAlignment alw = search.align("Test", "MMMMWWVVVV", false, this.wv, null, false);
        this.print(alw);
        Assert.assertEquals("Length of template", 6, alw.getTemplateLength());
        Assert.assertEquals(6, alw.getIdentity());
        Assert.assertEquals("Identity percent", "100", alw.getPercentIdentity());

    }

    public void testGaps() {
        SWSearch search = null;
        search = new SWSearch("Template", "AAAAWWWCCRRRR", "PAM200", 3, 1);
        final PimsAlignment alw = search.align("Test", "AAAAMMMMWWWRRRR", false, this.wv, null, false);
        this.print(alw);
        Assert.assertEquals(4, alw.getGaps());
        Assert.assertEquals("Gaps percent", "30.77", alw.getPercentGaps());

    }

    public void testScore() {
        SWSearch search = null;
        search = new SWSearch("Template", "AAAAWWWCCRRRR", "PAM200", 3, 1);
        final PimsAlignment alw = search.align("Test", "AAAAMMMMWWWRRRR", false, this.wv, null, false);
        this.print(alw);
        // note that the value with j aligner was 71.6
        Assert.assertEquals(82, alw.getScore());
    }

    public void testMultipleAlign() throws ConstraintException {
        SWSearch search = null;
        search =
            new SWSearch(
                "Template",
                "AAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRRAAAAWWWCCRRRR",
                "PAM200", 3, 1);
        this.protein
            .setSequence("AAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRRAAAAMMMMWWWRRRR");
        search.setCutoff(87.3f);
        final Collection<PimsAlignment> alignments =
            search.align(Collections.singleton(this.protein), false, this.wv);
        Assert.assertEquals(1, alignments.size());
        final PimsAlignment alignment = alignments.iterator().next();
        Assert.assertEquals(this.protein.getName(), alignment.getHitName());
        Assert.assertEquals(this.protein.get_Hook(), alignment.getHook());
    }

    public void testCutOff() throws ConstraintException {
        SWSearch search = null;
        search = new SWSearch("Template", "AAAAWWWCCRRRR", "PAM200", 3, 1);
        this.protein.setSequence("AAAAMMMMWWWRRRR");
        search.setCutoff(89f);
        final Collection<PimsAlignment> alignments =
            search.align(Collections.singleton(this.protein), false, this.wv);
        Assert.assertEquals(0, alignments.size());
    }

/* was 
    public void testSequences() {
        SWSearch search = null;
        search = new SWSearch("Template", "XXXXAAAAWWWCCRRRR", "PAM200", 3, 1);
        final PimsAlignment alw = search.align("Test", "AAAAMMMMWWWRRRRYYYY", false, this.wv, null, false);
        this.print(alw);
        Assert.assertEquals("AAAAWWWCCRRRR".length(), alw.getTemplateLength());
        Assert.assertEquals("AAAAMMMMWWWRRRR".length(), alw.getHitSeqLength());
    } */

    public void testPatternCleanUp() {
        SWSearch search = null;
        final String dnat = "AAGGTTTGCCCCC";
        final String dna = "AAGGTAACCTTG";
        search = new SWSearch("Template", dnat);
        final PimsAlignment alw = search.align("Test", dna, false, this.wv, null, true);
        Assert.assertTrue(SequenceGetter.isDNA(dnat));
        Assert.assertTrue(SequenceGetter.isDNA(dna));

        final String pattern = alw.getFormatted()[1].trim();
        for (int i = 0; i < pattern.length(); i++) {
            Assert.assertTrue(pattern.charAt(i) == ' ' || pattern.charAt(i) == '|');
        }
    }

    public void testPatternCleanUp2() {
        SWSearch search = null;
        final String proteint = "MVTATGCVWWMMV";
        final String protein2 = "MFFATACVWWGMV";
        //final Collection<String> mxl = null;
        search = new SWSearch("Template", proteint, "PAM200", 3, 1);
        final PimsAlignment alw = search.align("Test", protein2, false, this.wv, null, false);
        Assert.assertFalse(SequenceGetter.isDNA(proteint));
        Assert.assertFalse(SequenceGetter.isDNA(protein2));

        final String pattern = alw.getFormatted()[1].trim();
        for (int i = 0; i < pattern.length(); i++) {
            Assert.assertTrue(
                "Sequence should contain similarity information as well e.g. . or :",
                pattern.charAt(i) == ' ' || pattern.charAt(i) == '|' || pattern.charAt(i) == '.'
                    || pattern.charAt(i) == ':');
        }

    }
}
