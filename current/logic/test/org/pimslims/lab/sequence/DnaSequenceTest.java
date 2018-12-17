package org.pimslims.lab.sequence;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.ReadableVersion;

public class DnaSequenceTest extends TestCase {

    public DnaSequenceTest(final String arg0) {
        super(arg0);
    }

    public final void testGetLength() {
        Assert.assertEquals(2, new DnaSequence("AT").getLength());
    }

    public final void testBadSequence() {
        try {
            new DnaSequence("ATN");
            //TODO Assert.fail("Could create ambiguous sequence");
        } catch (final IllegalArgumentException e) {
            // that's fine
        }
    }

    public final void testWithWhiteSpace() {
        final DnaSequence seq = new DnaSequence("ATATATATAT GC");
        Assert.assertEquals(12, seq.getLength());
        Assert.assertEquals("ATATATATATGC", seq.getSequence());
    }

    public final void testGetTm() {
        final DnaSequence seq = new DnaSequence(DnaSequenceTest.SEQUENCE);
        Assert.assertTrue("The Tm is greater than zero", seq.getTm() > 0);
    }

    /*
     * Test method for 'org.pimslims.lab.ConstructAnnotator.annotate(ConstructBean)' TODO Test that mass
     * calculation works public void testCalcMass() throws IllegalSymbolException, NullPointerException {
     * float mass = new DnaSequence(SEQUENCE).getMass(); assertTrue("Mass is greater than zero", mass >0); }
     */

    private static final String SEQUENCE = "GATCGATCGAGATCGATCGAGATCGATCGAGATCGATCGAGATCGATCGA"; // test

    private final ReadableVersion version = null;

    // sequence
    // OK
    // for
    // NA
    // and
    // protein

    public final void testGetGCContent() {
        final DnaSequence seq = new DnaSequence(DnaSequenceTest.SEQUENCE);
        final float gcContent = seq.getGCContent();
        Assert.assertTrue("% Gc in SEQUENCE is 50% ", gcContent == 50.0);
    }

    public final void testNoSequence() {
        final DnaSequence seq = new DnaSequence("");
        Assert.assertEquals(0f, seq.getGCContent());
        Assert.assertEquals(0f, seq.getTm());
        // TODO assertEquals(0.0, seq.getMass());
    }

    public final void testGetSequence() {
        Assert
            .assertEquals(DnaSequenceTest.SEQUENCE, new DnaSequence(DnaSequenceTest.SEQUENCE).getSequence());
    }

    public final void testGetReverseComplement() {
        Assert.assertEquals(new DnaSequence("CATTGG"), new DnaSequence("CCAATG").getReverseComplement());
    }

    private static final String SEQ25 = "CCATGCATGCATGCATGCATGCATG"; // 25

    public final void testEquals() {
        Assert.assertEquals(new DnaSequence("CATG"), new DnaSequence("CATG"));
    }

    public final void testHashCode() {
        Assert.assertEquals(new DnaSequence("CATG").hashCode(), new DnaSequence("CATG").hashCode());
    }

    // bases
    private static final String FASTA =
        ">description\n" + DnaSequenceTest.SEQ25 + DnaSequenceTest.SEQ25 + DnaSequenceTest.SEQ25 + "AAAA\n" // 79 bases
            + DnaSequenceTest.SEQ25 + "\n";

    public final void testGetFastA() {
        Assert.assertEquals(DnaSequenceTest.FASTA, new DnaSequence(DnaSequenceTest.SEQ25
            + DnaSequenceTest.SEQ25 + DnaSequenceTest.SEQ25 + "AAAA" + DnaSequenceTest.SEQ25)
            .getFastA("description"));
    }

    public final void testChunkSequence() {
        int numChunks;
        final int FIVE = 5;
        List<String> chunks = new ArrayList<String>();
        chunks = new DnaSequence(DnaSequenceTest.SEQUENCE).chunkSeq();
        final int lastRow = chunks.size();
        final String[] chunkArray = chunks.get(lastRow - 1).split(" ");
        numChunks = chunkArray.length;
        Assert.assertEquals("Sequence has 5 blocks of 10", FIVE, numChunks);

        //shorter sequence
        chunks = new DnaSequence(DnaSequenceTest.SEQ25).chunkSeq();
        Assert.assertEquals("One element", 1, chunks.size());

        //longer sequence
        final String LONGSEQ =
            DnaSequenceTest.SEQUENCE + DnaSequenceTest.SEQUENCE + DnaSequenceTest.SEQ25 + "GATC";
        chunks = new DnaSequence(LONGSEQ).chunkSeq();
        Assert.assertEquals("Two elements", 2, chunks.size());
        Assert
            .assertEquals(
                "GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA GATCGATCGA ",
                chunks.get(0));

        //sequence with terminal stop codons -should be removed
        final String THREESTOPS = DnaSequenceTest.SEQ25 + "***";
        chunks = SequenceUtility.chunkSeq(THREESTOPS);
        Assert.assertFalse("No terminal stop codons", chunks.get(0).endsWith("*"));
    }

    public final void testCompareProtSeqs() {
        String protSeq = "";
        String dnaSeq = "";
        String[] comparrison = null;
        comparrison = SequenceUtility.compProtSeqs("protein", protSeq, dnaSeq, this.version);
        Assert.assertNull("No alignment without sequences", comparrison);

        protSeq = "DRSRSIEIDRDRSRSI";
        comparrison = SequenceUtility.compProtSeqs("protein", protSeq, dnaSeq, this.version);
        Assert.assertNull("No alignment without DNA Sequence", comparrison);

        protSeq = "";
        dnaSeq = DnaSequenceTest.SEQUENCE.substring(0, 48); //SEQUENCE = 50 nucleotides
        comparrison = SequenceUtility.compProtSeqs("protein", protSeq, dnaSeq, this.version);
        Assert.assertNull("No alignment without protein sequence", comparrison);

        protSeq = "DRSRSIEIDRDRSRSI";
        dnaSeq = DnaSequenceTest.SEQUENCE.substring(0, 48); //SEQUENCE = 50 nucleotides
        comparrison = SequenceUtility.compProtSeqs("protein", protSeq, dnaSeq, this.version);
        Assert.assertNotNull("2 sequences can have alignment", comparrison);
        //check effect of stop codons:
        final String intStopDNA = "AGTGAGTAACTAGTATGATAA";
        final String intStopProt = "GE*LV**";
        comparrison = SequenceUtility.compProtSeqs("protein", intStopProt, intStopDNA, this.version);
        Assert.assertNotNull("2 sequences alignment with stops", comparrison);
        //for (final String row : comparrison) {
        //    System.out.println(row);
        //}
    }
}
