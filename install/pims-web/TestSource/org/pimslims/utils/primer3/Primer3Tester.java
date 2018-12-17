package org.pimslims.utils.primer3;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.utils.ExecRunnerException.BadPathException;

public class Primer3Tester extends TestCase {

    private final String primer3_path = "C:\\temp\\primer3-1.1.0-beta\\src\\primer3_core";

    private static final String sequence =
        "CGGAGACGGTCCTGCTGCTGCCGCAGTCCTGCCAGCTGTCCGACAATGTCGTCCCACCTAGTCGAGCCG"
            + "CCGCCGCCCCTGCACAACAACAACAACAACTGCGAGGAAAATGAGCAGTCTCTGCCCCCGCCGGCCGGCCTCAACAGTTCCTGGGTGGAGC"
            + "TACCCATGAACAGCAGCAATGGCAATGATAATGGCAATGGGAAAAATGGGGGGCTGGAACACGTACCATCCTCATCCTCCATCCACAATGG"
            + "AGACATGGAGAAGATTCTTTTGGATGCACAACATGAATCAGGACAGAGTAGTTCCAGAGGCAGTTCTCACTGTGACAGCCCTTCGCCACAA"
            + "GAAGATGGGCAGATCATGTTTGATGTGGAAATGCACACCAGCAGGGACCATAGCTCTCAGTCAGAAGAAGAAGTTGTAGAAGGAGAGAAGG"
            + "AAGTCGAGGCTTTGAAGAAAAGTGCGGACTGGGTATCAGACTGGTCCAGTAGACCCGAAAACATTCCACCCAAGGAGTTCCACTTCAGACA"
            + "CCCTAAACGTTCTGTGTCTTTAAGCATGAGGAAAAGTGGAGCCATGAAGAAAGGGGGTATTTTCTCCGCAGAATTTCTGAAGGTGTTCATT"
            + "CCATCTCTCTTCCTTTCTCATGTTTTGGCTTTGGGGCTAGGCATCTATATTGGAAAGCGACTGAGCACACCCTCTGCCAGCACCTACTGAG"
            + "GGAAAGGAAAAGCCCCTGGAAATGCGTGTGACCTGTGAAGTGGTGTATTGTCACAGTAGCTTATTTGAACTTGAGACCATTGTAAGCATGA"
            + "CCCAACCTACCACCCTGTTTTTACATATCCAATTCCAGTAACTCTCAAATTCAATATTTTATTCAAACTCTGTTGAGGCATTTTACTAACC"
            + "TTATACCCTTTTTGGCCTGAAGACATTTTAGAATTTCCTAACAGAGTTTACTGTTGTTTAGAAATTTGCAAGGGCTTCTTTTCCGCAAATG"
            + "CCACCAGCAGATTATAATTTTGTCAGCAATGCTATTATCTCTAATTAGTGCCACCAGACTAGACCTGTATCATTCATGGTATAAATTTTAC"
            + "TCTTGCAACATAACTACCATCTCTCTCTTAAAACGAGATCAGGTTAGCAAATGATGTAAAAGAAGCTTTATTGTCTAGTTGTTTTTTTTCC"
            + "CCCAAGACAAAGGCAAGTTTCCCTAAGTTTGAGTTGATAGTTATTAAAAAGAAAACAAAACAAAAAAAAAAGGCAAGGCACAACAAAAAAA"
            + "TATCCTGGGCAAT";

    public Primer3Tester(final String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("Primer3Tester.setUp()");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.out.println("Primer3Tester.tearDown()");
    }

    public void testRunPrimer3() throws BadPathException, Primer3Exception {

        System.out.println("Primer3Tester.testRunPrimer3()");
        final Primer3Model model = new Primer3Model();
        model.setIncludedRegion("46,660");
        model.setPrimerProductSizeRange("660-660");
        model.setPrimerSequenceID("AB004788_46_705");
        model.setSequence(Primer3Tester.sequence);

        final Primer3Results results = Primer3.runPrimer3(this.primer3_path, model.toBytes());

        Assert.assertEquals("Forward Primer incorrect", results.getForwardPrimer().getPrimerSeq(),
            "ATGTCGTCCCACCTAGTCGAGCC");

        Assert.assertEquals("Reverse Primer incorrect", results.getReversePrimer().getPrimerSeq(),
            "TCAGTAGGTGCTGGCAGAGGGTG");

    }

    public void testMakePrimers() {

        System.out.println("Primer3Tester.testMakePrimers()");

        final Primer3 u = new Primer3();
        // byte[] data = u.getInput(model).toByteArray();
        final List primers =
            u.makePrimers(new String[] { Primer3Tester.sequence }, new Float("70.0").floatValue());

        Assert.assertEquals("Forward Primer incorrect", (String) primers.get(0), "ATGTCGTCCCACCTAGTCGAGCC");

        Assert.assertEquals("Reverse Primer incorrect", (String) primers.get(1), "TCAGTAGGTGCTGGCAGAGGGTG");

    }

    public void testCalcTm() throws BadPathException, Primer3Exception {

        System.out.println("Primer3Tester.testCalcTm()");

        final Primer3 u = new Primer3();
        // byte[] data = u.getInput(model).toByteArray();
        final List primers =
            u.makePrimers(new String[] { Primer3Tester.sequence }, new Float("70.0").floatValue());

        final float fpTm = Primer3.calcTm((String) primers.get(0));
        final float rpTm = Primer3.calcTm((String) primers.get(1));

        // System.out.println("Tm calculated ["+fpTm+","+rpTm+"]");

        Assert.assertEquals("Forward Primer Tm incorrect", fpTm, new Float("67.86").floatValue());

        Assert.assertEquals("Reverse Primer Tm incorrect", rpTm, new Float("68.05").floatValue());

    }
}
