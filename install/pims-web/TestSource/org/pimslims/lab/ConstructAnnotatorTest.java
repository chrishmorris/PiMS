package org.pimslims.lab;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.construct.ConstructAnnotator;
import org.pimslims.presentation.construct.ConstructBean;

public class ConstructAnnotatorTest extends TestCase {

    // test  sequence  OK  for  NA  and protein
    private static final String SEQUENCE = "CGATCG";

    private static final String NOSEQUENCE = ""; // test no sequence

    private static final String BADSEQUENCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // sequenced

    // with
    // non-standard
    // characters
    // including
    // O
    // and
    // U
    // "mivprdvkevtrmlirlnmlmrrfkerggypimyvadrmgdrhdavvlivdilyeerera" +
    // "eavvkevldllgahnikvxdhkldsfmvpfrevwrfvamvavnh";
    private static final String AMBIGUOUSSEQUENCE = "ABCDEFGHIJKLMNPQRSTVWXYZ"; // sequenced

    // with
    // non-standard
    // characters

    static final String REVERSE_TAG = "CCGACTATCGACT";

    static final String FORWARD_EXTENSION = "AACACGTGTG";

    static final String REV_PRIMER = "CCGACTATCGACTTAGAGTGGAACC";

    /**
     * reverse complement of reverse primer
     */
    //private static final String REVERSE = "GGCT";
    static final String FWD_PRIMER = "AACACGTGTGTTGATCAAGCCT";

    static final String REV_OVERLAP = "TAGAGTGGAACC";

    protected static final String FWD_OVERLAP = "TTGATCAAGCCT";

    protected static final String CONSTRUCT_ID = "SSO1439.Z" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ConstructAnnotatorTest.class);
    }

    /*
     * Test method for 'org.pimslims.lab.ConstructAnnotator.annotate(ConstructBean)' Test the 'annotate'
     * method
     What was this supposed to test?
    public void testAnnotate() throws BioException {

        final PrimerBean forward = new PrimerBean();
        forward.setSequence(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean();
        reverse.setSequence(ConstructAnnotatorTest.REV_PRIMER);
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb
            .setFinalProt("AASLIKPCAYEKQGLIDHAIGSYKVLHDKISESYYKVISRRLERYGIVLDLNGVKEIVKDVVVLHDMGKAGEYYQNQFDDNCNPLKSNPLKSRFSFIYHELGSALFFYNDYEPINVEKAEEVRSLLTLAVINHLNAIRGISDYLLNRFPDNFDEEMIKLSKYGSILLENLRGIISKSLKVRDYSFTDYHDMLYAFSKKSDKYLKLYNLFLAPIMLGDNLDSSLVRNNGSKTRFVGILEGELNGGSTLCCCC");
        
        if (null != cb.getFwdPrimer()) {
            final DnaSequence fDna = new DnaSequence(cb.getFwdPrimer());
            cb.setFwdPrimerTm(fDna.getTm());
            cb.setFwdPrimerGc(fDna.getGCContent());
        }
        if (null != cb.getRevPrimer()) {
            final DnaSequence rDNA = new DnaSequence(cb.getRevPrimer());
            cb.setRevPrimerTm(rDNA.getTm());
            cb.setRevPrimerGc(rDNA.getGCContent());
        }
        final ProteinSequence finalProtein = new ProteinSequence(cb.getFinalProt());
        cb.setWeight((double) finalProtein.getMass());
        cb.setExtinction((double) finalProtein.getExtinctionCoefficient());
        cb.setProtPi((double) finalProtein.getPI());
        cb.setLen((long) finalProtein.getLength());

        Assert.assertTrue((cb.getFwdPrimer() == ConstructAnnotatorTest.FWD_PRIMER)
            || (cb.getFwdPrimer() != ConstructAnnotatorTest.REV_PRIMER));
        Assert.assertTrue(cb.getRevPrimer() == ConstructAnnotatorTest.REV_PRIMER);
        // System.out.println("cb.getWeight("+CB.getWeight()+")");
    } */

    public void testBadAnnotate() {

        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence(ConstructAnnotatorTest.REV_PRIMER);
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setFinalProt(ConstructAnnotatorTest.BADSEQUENCE);
        cb.setDnaSeq(null);
        try {
            ConstructBean.annotate(cb);
            Assert.assertNull(cb.getFinalProteinLength());
        } catch (final IllegalStateException e) {
            // that's fine
        } catch (final IllegalArgumentException e) {
            // that's fine
        }
    }

    public void testAmbiguousAnnotate() {

        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence(ConstructAnnotatorTest.REV_PRIMER);
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setFinalProt(ConstructAnnotatorTest.AMBIGUOUSSEQUENCE);
        cb.setDnaSeq("");
        try {
            ConstructBean.annotate(cb);
            // was Assert.assertNotNull(cb.getFinalProteinLength()); // BioJava1.5 can handle all residues in
            // this sequence
        } catch (final IllegalStateException e) {
            // catches attempt to annotate w/o primers or final protein
            Assert.fail("annotate.IllegalStateException");
        } catch (final IllegalArgumentException e) {
            // that's fine
        }
    }

    public void testAnnotateOverlaps() {

        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence(ConstructAnnotatorTest.REV_PRIMER);
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setFinalProt("QWERTY");
        cb.setDnaSeq(ConstructAnnotatorTest.REV_OVERLAP + "CCCC" + ConstructAnnotatorTest.FWD_OVERLAP);
        try {
            ConstructBean.annotate(cb);
            // was Assert.assertNotNull(cb.getFinalProteinLength()); // BioJava1.5 can handle all residues in
            Assert.assertEquals(null, reverse.getGcGeneFloat());
            Assert.assertEquals(null, reverse.getTmGeneFloat());
            Assert.assertEquals(null, forward.getGcGeneFloat());
            Assert.assertEquals(null, forward.getTmGeneFloat());
            //Add overlaps
            reverse.setLengthOnGeneString(Integer.toString(ConstructAnnotatorTest.REV_OVERLAP.length()));
            reverse.setExtension("CCC");
            forward.setLengthOnGeneString(Integer.toString(ConstructAnnotatorTest.FWD_OVERLAP.length()));
            forward.setExtension("GGGG");
            ConstructBean.annotate(cb);
            final DnaSequence testRol = new DnaSequence(ConstructAnnotatorTest.REV_OVERLAP);
            Assert.assertEquals(testRol.getGCContent(), reverse.getGcGeneFloat());
            Assert.assertEquals(testRol.getTm(), reverse.getTmGeneFloat());
            final DnaSequence testFol = new DnaSequence(ConstructAnnotatorTest.FWD_OVERLAP);
            Assert.assertEquals(testFol.getGCContent(), forward.getGcGeneFloat());
            Assert.assertEquals(testFol.getTm(), forward.getTmGeneFloat());
        } catch (final IllegalStateException e) {
            // catches attempt to annotate w/o primers or final protein
            e.printStackTrace();
            Assert.fail("annotate.IllegalStateException");
        } catch (final IllegalArgumentException e) {
            // that's fine
        }
    }

    public void testPrimerCleaner() {
        final String dirtyseq = "GTCAGTCA'-}[-;.HIJK";
        final String CLEANED = "GTCAGTCAHIJK";
        String cleanSeq = "";
        cleanSeq = ConstructAnnotator.cleanPrimer(dirtyseq);
        Assert.assertEquals(CLEANED, cleanSeq);
        Assert.assertEquals(ConstructAnnotatorTest.NOSEQUENCE, ConstructAnnotator
            .cleanPrimer(ConstructAnnotatorTest.NOSEQUENCE));
    }

    public void testPcrProduct() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence("CCCC");
        forward.setLengthOnGene(4);
        forward.setExtension(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("CCCCC");
        reverse.setLengthOnGene(5);
        reverse.setExtension("AAA");
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setDnaSeq(forward.getOverlap() + ConstructAnnotatorTest.SEQUENCE + "GGGGG");
        final String product = ConstructAnnotator.getPCRProductSequence(cb);
        Assert.assertEquals(forward.getExtension() + forward.getOverlap() + ConstructAnnotatorTest.SEQUENCE
            + "GGGGG" + "TTT", product);

    }

    public void testProteinSequence() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence("ATG");
        forward.setLengthOnGene(3);
        forward.setExtension(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("CCCCCC");
        reverse.setLengthOnGene(6);
        reverse.setExtension("AAA");
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setDnaSeq(forward.getOverlap() + ConstructAnnotatorTest.SEQUENCE + "GGGGGG");

        ConstructBean.annotate(cb);
        Assert.assertEquals("MRSGG", ConstructAnnotator.getDnaSeqTranslated(cb));
        Assert.assertEquals("MRSGG", cb.getFinalProt());
    }

    public void testProteinSequenceGTGMutated() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence("ATG");
        forward.setLengthOnGene(3);
        forward.setExtension(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("CCCCCC");
        reverse.setLengthOnGene(6);
        reverse.setExtension("AAA");
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setDnaSeq("GTG" + ConstructAnnotatorTest.SEQUENCE + "GGGGGG");

        ConstructBean.annotate(cb);
        Assert.assertEquals("MRSGG", ConstructAnnotator.getDnaSeqTranslated(cb));
        Assert.assertEquals("MRSGG", cb.getFinalProt());
    }

    public void testPcrProductGTG() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence("ATGCCCC");
        forward.setLengthOnGene(7);
        forward.setExtension(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("CCCCC");
        reverse.setLengthOnGene(5);
        reverse.setExtension("AAA");
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setDnaSeq("GTGCCCC" + ConstructAnnotatorTest.SEQUENCE + "GGGGG");
        final String product = ConstructAnnotator.getPCRProductSequence(cb);
        Assert.assertEquals(forward.getExtension() + forward.getOverlap() + ConstructAnnotatorTest.SEQUENCE
            + "GGGGG" + "TTT", product);

    }

    public void testReverse() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence(ConstructAnnotatorTest.FWD_PRIMER);
        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("CAATG");
        final ConstructBean cb = new ConstructBean(null, forward, reverse);
        cb.setDnaSeq(ConstructAnnotatorTest.SEQUENCE);
        final String product = ConstructAnnotator.getPCRProductSequence(cb);
        Assert.assertEquals(ConstructAnnotatorTest.FWD_PRIMER + ConstructAnnotatorTest.SEQUENCE + "CATTG",
            product);
    }

}
