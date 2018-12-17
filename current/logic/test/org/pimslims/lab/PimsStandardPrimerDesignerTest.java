package org.pimslims.lab;

import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.lab.primer.DesignPrimers;
import org.pimslims.lab.primer.PimsStandardPrimerDesigner;

public class PimsStandardPrimerDesignerTest extends TestCase {

    private static final int KNOWN_LENGTH = 32; // Length of F primer designed

    // for PuRE
    // private static final int KNOWN_LENGTH = 34; // Length of F primer
    // designed for 5
    private static final int REV_LENGTH = 26; // Length of R primer designed

    // for PuRE

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PimsStandardPrimerDesignerTest.class);
    }

    public PimsStandardPrimerDesignerTest(final String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.getStandardPrimerDesigner()'
     */
    public void testGetStandardPrimerDesigner() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        Assert.assertNotNull(d);
    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.makePrimers(String, float)'
     */
    public void testNoGene() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        try {
            d.makePrimers(new String[] { "" }, 50f);
            Assert.fail("cant make primers from empty DNA sequence");
        } catch (final IllegalArgumentException e) {
            // that's fine
        }
    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.makePrimers(String, float)'
     */
    public void testSimpleGene() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        final Collection<String> primers =
            d.makePrimers(new String[] { "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA" + "AAAAAAAAAAAAAAA" }, 50f);
        Assert.assertTrue(1 <= primers.size());

    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.makePrimers(String, float)' Using PuRE DNA
     * sequence and known primer length of 32 designed for Tm = 65
     */
    public void testFancyGene() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        final List<String> primers =
            d.makePrimers(
                new String[] { "ATGAAATCACTAGTTGGAGTCATAATGGGAAGCACGTCAGACTGGGAAACAATGAAATATGCTTGTGACATTTTAGATGAATTAAATATACCGTATGAGAAAAAGGTTGTATCCGCTCATCGGACTCCGGATTATATGTTTGAATATGCAGAGACGGCTCGTGAACGTGGATTGAAAGTTATTATTGCTGGAGCTGGTGGAGCAGCGCATTTACCAGGAATGGTTGCAGCGAAGACGAATCTTCCTGTAATCGGAGTTCCAGTTCAATCAAAAGCGTTAAACGGCTTAGATTCATTATTATCCATCGTCCAAATGCCAGGAGGGGTTCCAGTTGCAACTGTTGCAATTGGTAAGGCTGGTTCAACAAATGCTGGTTTACTTGCTGCACAAATACTTGGATCATTCCATGATGACATACATGATGCATTAGAATTGAGAAGAGAAGCAATTGAAAAAGATGTGCGCGAAGGTAGTGAGCTAGTATGA" },

                /*
                 * "ATGCAAAAGGTAAAATTACCCCTGACTCTTGATCCGGTTCGTACCGCTCAAAAACGCCTTGATTACCAGGGTATCTATACCCCCGATCAGGTTGAGCGCG"+
                 * "TTGCCGAATCTGTAGTCAGTGTGGACAGTGATGTGGAATGCTCCATGTCGTTCGCTATCGATAACCAGCGTCTCGCCGTTTTGACTGGCGATGCGGTCGT"+
                 * "TACGGTGTCGCTCGAATGCCAGCGTTGCGGGAAACCGTTTACCCATCAGGTTCACACAACGTATTGTTTTAGCCCTGTCCGTTCCGACGAGCAGGCTGAA"+
                 * "GCACTGCCGGAAGCGTATGAGCCGATTGAGGTAAACGAATTCGGCGAAATCGATCTGCTGGCGACGGTTGAAGATGAAATCATCCTCGCCTTGCCGGTAG"+
                 * "TTCCGGTGCATGATTCTGAACACTGTGAAGTGTCCGAGGCGGACATGGTCTTTGGCGAACTGCCTGATGAAGCGCAAAAGCCAAACCCATTTGCCGTATT"+
                 * "AGCCAGCTTAAAGCGTAAGTAA",
                 */// sequence for Traget 5
                65f);
        Assert.assertEquals(PimsStandardPrimerDesignerTest.KNOWN_LENGTH, primers.get(0).length());
    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.makePrimers(String, float)' Using DNA
     * sequence with a run of >3 GC Tm = 65
     */
    public void test3GC() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        final List<String> primers = d.makePrimers(
        // "ATGAAATCACTAGTTGGAGTCATACGGGGCCGGTAGAAGCACGTCAGACTGGGAAACAATGAAATATGCTTGTGACATTTTAGATGAATTAAATATACCGTATGAGAAAAAGGTTGTATCCGCTCATCGGACTCCGGATTATATGTTTGAATATGCAGAGACGGCTCGTGAACGTGGATTGAAAGTTATTATTGCTGGAGCTGGTGGAGCAGCGCATTTACCAGGAATGGTTGCAGCGAAGACGAATCTTCCTGTAATCGGAGTTCCAGTTCAATCAAAAGCGTTAAACGGCTTAGATTCATTATTATCCATCGTCCAAATGCCAGGAGGGGTTCCAGTTGCAACTGTTGCAATTGGTAAGGCTGGTTCAACAAATGCTGGTTTACTTGCTGCACAAATACTTGGATCATTCCATGATGACATACATGATGCATTAGAATTGAGAAGAGAAGCAATTGAAAAAGATGTGCGCGAAGGTAGTGAGCTAGTATGA",
        // //TODO susy replace this with a real sequence
            new String[] { "ATGAAATCGCTAGTTGGCGGGGCCGGCCGCGCCGATGAAATCGCTAGTTGGCGGGGCCGGCCGCGCCG" }, 65f);
        Assert.assertEquals(23, primers.get(0).length());
    }

    /*
     * Test method for 'org.pimslims.lab.PimsStandardPrimerDesigner.makePrimers(String, float)' Using PuRE DNA
     * sequence and known primer length of 32 designed for Tm = 65
     */
    public void testRevComp() {
        final DesignPrimers d = PimsStandardPrimerDesigner.getStandardPrimerDesigner();
        final List<String> primers =
            d.makePrimers(
                new String[] { "TACTAGCTCACTACCTTCGCGCACATCTTTTTCAATTGCTTCTCTTCTCAATTCTAATGCATCATGTATGTCATCATGGAATGATCCAAGTATTTGTGCAGCAAGTAAACCAGCATTTGTTGAACCAGCCTTACCAATTGCAACAGTTGCAACTGGAACCCCTCCTGGCATTTGGACGATGGATAATAATGAATCTAAGCCGTTTAACGCTTTTGATTGAACTGGAACTCCGATTACAGGAAGATTCGTCTTCGCTGCAACCATTCCTGGTAAATGCGCTGCTCCACCAGCTCCAGCAATAATAACTTTCAATCCACGTTCACGAGCCGTCTCTGCATATTCAAACATATAATCCGGAGTCCGATGAGCGGATACAACCTTTTTCTCATACGGTATATTTAATTCATCTAAAATGTCACAAGCATATTTCATTGTTTCCCAGTCTGACGTGCTTCCCATTATGACTCCAACTAGTGATTTCAT" },
                65f);
        Assert.assertEquals(PimsStandardPrimerDesignerTest.REV_LENGTH, primers.get(0).length());
    }
}
