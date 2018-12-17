package org.pimslims.lab;

import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;

public class ThreeLetterProteinSeqTest extends TestCase {

    /**
     * translation of DNA_SEQ
     */
    private static final String PROT_SEQ =
        "MKSLVGVIMGSTSDWETMKYACDILDELNIPYEKKVVSAHRTPDYMFEYAETARERGLK"
            + "VIIAGAGGAAHLPGMVAAKTNLPVIGVPVQSKALNGLDSLLSIVQMPGGVPVATVAIGKAGSTNA"
            + "GLLAAQILGSFHDDIHDALELRREAIEKDVREGSELV*";

    /**
     * 
     */
    private static final String DNA_SEQ =
        "ATGAAATCACTAGTTGGAGTCATAATGGGAAGCACGTCAGACTGGGAAACAATGAAATA"
            + "TGCTTGTGACATTTTAGATGAATTAAATATACCGTATGAGAAAAAGGTTGTATCCGCTCATCGGA"
            + "CTCCGGATTATATGTTTGAATATGCAGAGACGGCTCGTGAACGTGGATTGAAAGTTATTATTGCT"
            + "GGAGCTGGTGGAGCAGCGCATTTACCAGGAATGGTTGCAGCGAAGACGAATCTTCCTGTAATCGG"
            + "AGTTCCAGTTCAATCAAAAGCGTTAAACGGCTTAGATTCATTATTATCCATCGTCCAAATGCCAG"
            + "GAGGGGTTCCAGTTGCAACTGTTGCAATTGGTAAGGCTGGTTCAACAAATGCTGGTTTACTTGCT"
            + "GCACAAATACTTGGATCATTCCATGATGACATACATGATGCATTAGAATTGAGAAGAGAAGCAAT"
            + "TGAAAAAGATGTGCGCGAAGGTAGTGAGCTAGTATGA";

    private static final String OTHER_PROTSEQ = "QWERTYQWERTYQWERTYQWERTYQWERTYQWERTY";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ThreeLetterProteinSeqTest.class);
    }

    /*
     * Test method for 'org.pimslims.lab.ThreeLetterProteinSeq.ThreeLetterProteinSeq()'
     */
    public void testThreeLetterProteinSeq() {

        final ThreeLetterProteinSeq tlps = new ThreeLetterProteinSeq();
        Assert.assertNotNull(tlps);

    }

    /*
     * Test method for 'org.pimslims.lab.ThreeLetterProteinSeq.getThreeLetterSeq(String)'
     */
    public void testGetThreeLetterSeq() {
        final String seq = "QWERTY";
        final String tlcSeq = ThreeLetterProteinSeq.createThreeLetterSeq(seq);
        Assert.assertNotNull(tlcSeq);
        //System.out.println("The sequence " + seq + " converted to " + tlcSeq);

    }

    public void testConvertToTriplets() {
        final String seq = "QWERTY";
        final String tlcSeq = ThreeLetterProteinSeq.createThreeLetterSeq(seq);
        final String tripletSeq = ThreeLetterProteinSeq.convertToTriplets(tlcSeq);
        Assert.assertNotNull(tripletSeq);
        //System.out.println("The sequence " + seq + " converted to " + tripletSeq);
    }

    /**
     * Test method for ThreeLetterProteinSeq CreateTripletSeq
     */
    public void testCreateTripletSeq() {
        final ThreeLetterProteinSeq tlps = new ThreeLetterProteinSeq();
        final String seq = ThreeLetterProteinSeqTest.OTHER_PROTSEQ;
        final String molType = "protein";
        final ArrayList seqArray = tlps.createTripletSeq(seq, molType);
        Assert.assertTrue(seqArray.size() == 2);

    }

    /**
     * Test method for ThreeLetterProteinSeq CreateTripletSeq for DNA
     */
    public void testCreateDNATripletSeq() {
        final ThreeLetterProteinSeq tlps = new ThreeLetterProteinSeq();
        final String seq =
            "CAATGGGAACGTACTTATCAATGGGAACGTACTTATCAATGGGAACGTACTTA"
                + "TCAATGGGAACGTACTTATCAATGGGAACGTACTTATCAATGGGAACGTACTTAT";
        final String molType = "dna";
        final ArrayList seqArray = tlps.createTripletSeq(seq, molType);
        Assert.assertTrue(seqArray.size() == 2);

    }

    /**
     * Test method for ThreeLetterProteinSeq Make2SeqArray for DNA and protein seq
     */
    public void testMake2SeqArray() {
        //System.out.println("The lengths of sequence 1 and 2 are :" + seq1.length() + " and " + seq2.length());
        final ArrayList seqArray =
            ThreeLetterProteinSeq.make2SeqArray(ThreeLetterProteinSeqTest.DNA_SEQ,
                ThreeLetterProteinSeqTest.PROT_SEQ);
        Assert.assertTrue(seqArray.size() > 0);

    }

    /**
     * Test method for ThreeLetterProteinSeq Make2SeqArray for DNA and protein seq
     */
    public void testTranslate() {
        final String protSeq = ThreeLetterProteinSeq.translate(ThreeLetterProteinSeqTest.DNA_SEQ);
        Assert.assertEquals(ThreeLetterProteinSeqTest.PROT_SEQ, protSeq);
    }

    /**
     * Test method for ThreeLetterProteinSeq dnaAndProtArray for DNA
     */
    public void testDnaAndProtArray() {
        final String seq = ThreeLetterProteinSeqTest.DNA_SEQ;
        Assert.assertNotNull(seq);
        //System.out.println("Testing method to prepare array from DNA seq: ");
        final ArrayList seqArray = ThreeLetterProteinSeq.dnaAndProtArray(seq);
        Assert.assertTrue(seqArray.size() > 0);

    }

    public void testDnaAndProtArrayBadSeq() {
        final String seq = "ATG===TGA";
        Assert.assertNotNull(seq);
        try {
            ThreeLetterProteinSeq.dnaAndProtArray(seq);
            Assert.fail("bad sequence");
        } catch (final IllegalArgumentException e) {
            // that's fine
        }

    }

    public void testCompareTranslatedSeq() {
        //final String dnaSeq = ThreeLetterProteinSeqTest.DNA_SEQ;
        final String protSeq = ThreeLetterProteinSeqTest.PROT_SEQ;
        final String diffseq = ThreeLetterProteinSeqTest.OTHER_PROTSEQ;
        String transSeq = "";
        final String stopCodons = ThreeLetterProteinSeqTest.DNA_SEQ + "TAATGATAG";
        final String intStopDNA = "AGTGAGTAACTAGTATGATAA";
        final String intStopProt = "SE*LV**";
        final String noSeq = "";

        transSeq = ThreeLetterProteinSeq.translate(ThreeLetterProteinSeqTest.DNA_SEQ);
        Assert.assertEquals(protSeq, transSeq);
        Assert.assertFalse("The protein and translated sequences do not match", transSeq.equals(diffseq));

        Assert.assertTrue(ThreeLetterProteinSeq.compareSeqs(ThreeLetterProteinSeqTest.PROT_SEQ,
            ThreeLetterProteinSeqTest.DNA_SEQ));
        Assert.assertFalse(ThreeLetterProteinSeq.compareSeqs(ThreeLetterProteinSeqTest.OTHER_PROTSEQ,
            ThreeLetterProteinSeqTest.DNA_SEQ));
        Assert.assertTrue("Dont include terminal stop codons in the comparrison", ThreeLetterProteinSeq
            .compareSeqs(protSeq, stopCodons));
        Assert.assertTrue("Include internal stop codons in the comparrison", ThreeLetterProteinSeq
            .compareSeqs(intStopProt, intStopDNA));
        Assert.assertTrue("Cant make sequence alignment if no sequence", ThreeLetterProteinSeq.compareSeqs(
            noSeq, noSeq));
        Assert.assertTrue("Cant make sequence alignment if no sequence", ThreeLetterProteinSeq.compareSeqs(
            null, null));
    }

    private static String Q44292_DNA =
        "ATGACACACGTTTCCAGAAG AAAATTTCTTTTCACCACAG GCGCGGCGGCGGCGGCTTCT ATTTTGGTACATGGCTGTAC TTCCAATGGTTCTCAATCAG CTACCACAGGAGAACAAGCG CCTTCAGCAGCACCAGCCGC TAATGTCTCAGCCGCTAACG CACCCAAGGTAGAAACGACT AAAGCCAAGTTAGGATTTAT CCCTCTTACTGATGCGGCGC CCCTCATCATTGCTAAAGAA AAGGGCTTCTTTGCTAAATA TGGCATGACAGATATCGAAG TCATCAAGCAAAAATCTTGC CCTGTCACCCGCGATAACTT AAAAATTGGCTCATCTGGTG GTGGCATCGATGGCGCACAT ATCCTTAGTCCCATGCCTTA CCTCATGACCATCAATGATA AAGTGCCAATGTATATTTTG GCTAGGTTAAACACTAATGG TCAGGCTATTTCTGTGGCGG AGAAATTTAAAGAACTTAAT GTCAACCTAGAAAGTAAATC CCTTAAAGACGCAGCGATTA AAGCCAAAGCTGACAAGAAA GCCTTGAAAATGGGTATTAC CTTTCCCGGTGGTACACACG ATTTGTGGATGCGCTATTGG TTAGCGGCTGGTGGTATTAA TCCTGATCAAGATGTGGTTT TGGAAGCTGTACCACCACCG CAAATGGTGGCAAATATGAA AGTCAATACTGTTGATGGTT TCTGTGTAGGAGAACCTTGG AATGCTCAGTTGGTCAACCA AAAAATAGGTTATTCTGCTC TAGTTACAGGCGAATTGTGG AAAGATCATCCAGAAAAAGC CTTTAGTATGCGGCAAGATT GGATTGAGCAAAATCCCAAT GCAGCCCAGGCAATTTTGAT GGCAATCTTGGAAGCACAAC AATGGTGCGACAAGGCAGAA AACAAAGAAGAAATGTGTAA AATCTGCTCTGATCGTAAAT ACTTTAATGTTGCTGCCGCA GATATTATCGAGAGAGCTAA AGGCAATATCGATTATGGTG ATGGTCGTAAGGAGCAAAAC TTCGCCCATCGGATGAAATT CTGGGCAGATAATGCTTCCT ATCCTTATAAGAGTCACGAT ATTTGGTTTTTAACTGAAGA TATTCGCTGGGGTTATTTAC CAAAAGATACCAAAGTTCAA GACATTGTTAATCAAGTCAA TAAAGAAGACTTGTGGAAAA AAGCGGCGAAAGCGATTGGT GTGGCTGATGCGGAAATTCC TGCTAGCAGTTCCCGTGGGG TGGAAACTTTCTTTGATGGC GTGAAATTCGACCCAGAAAA GCCAGAAGAATACTTAAATA GTTTGAAAATCAAAAAAGTC TAA";

    private static String Q44292_PROTEIN =
        "MTHVSRRKFLFTTGAAAAASILVHGCTSNGSQSATTGEQAPSAAPAANVSAANAPKVETTKAKLGFIPLTDAAPLIIAKEKGFFAKYGMTDIEVIKQKSWPVTRDNLKIGSSGGGIDGAHILSPMPYLMTINDKVPMYILARLNTNGQAISVAEKFKELNVNLESKSLKDAAIKAKADKKALKMGITFPGGTHDLWMRYWLAAGGINPDQDVVLEAVPPPQMVANMKVNTVDGFCVGEPWNAQLVNQKIGYSALVTGELWKDHPEKAFSMRQDWIEQNPNAAQAILMAILEAQQWCDKAENKEEMCKICSDRKYFNVAAADIIERAKGNIDYGDGRKEQNFAHRMKFWADNASYPYKSHDIWFLTEDIRWGYLPKDTKVQDIVNQVNKEDLWKKAAKAIGVADAEIPASSSRGVETFFDGVKFDPEKPEEYLNSLKIKKV";

    public void testQ44292() {
        final String dna = new DnaSequence(ThreeLetterProteinSeqTest.Q44292_DNA).getSequence();
        Assert.assertFalse(ThreeLetterProteinSeq.compareSeqs(ThreeLetterProteinSeqTest.Q44292_PROTEIN, dna));
    }

    public void testLengthWithoutTer() {
        final int pslength = ThreeLetterProteinSeqTest.PROT_SEQ.length();
        final String protein = ThreeLetterProteinSeqTest.PROT_SEQ.substring(0, pslength - 1); //ends with *

        Assert.assertEquals(protein.length(), ThreeLetterProteinSeq
            .lengthWithoutTer(ThreeLetterProteinSeqTest.DNA_SEQ));
    }
}
