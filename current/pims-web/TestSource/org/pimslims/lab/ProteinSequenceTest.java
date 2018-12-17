package org.pimslims.lab;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.pimslims.lab.sequence.ProteinSequence;

public class ProteinSequenceTest extends TestCase {

    private static final String SEQUENCE = "QWERTY";

    public ProteinSequenceTest(final String arg0) {
        super(arg0);
    }

    public final void testGetMass() {

        final ProteinSequence seq = new ProteinSequence(ProteinSequenceTest.SEQUENCE);
        final float mass = seq.getMass();
        Assert.assertTrue("Mass is greater than zero", mass > 0);

    }

    public final void testGetLength() throws BioException {
        Assert.assertEquals(2, new ProteinSequence("QW").getLength());
    }

    public final void testWithWhiteSpace() throws BioException {
        final ProteinSequence seq = new ProteinSequence("QWQWQWQWQW ER");
        Assert.assertEquals(12, seq.getLength());
        Assert.assertEquals("QWQWQWQWQWER", seq.getSequence());
    }

    public final void testNoSequence() throws BioException {
        final ProteinSequence seq = new ProteinSequence("");
        Assert.assertEquals(0f, seq.getMass());
    }

    public final void testGetSequence() throws BioException {
        Assert.assertEquals(ProteinSequenceTest.SEQUENCE,
            new ProteinSequence(ProteinSequenceTest.SEQUENCE).getSequence());
    }

    private static final String SEQ25 = "QWERTYQWERTYQWERTYQWERTYP"; // 25

    // bases
    private static final String FASTA = ">description\n" + ProteinSequenceTest.SEQ25
        + ProteinSequenceTest.SEQ25 + ProteinSequenceTest.SEQ25 + "IIII\n" // 79 bases
        + ProteinSequenceTest.SEQ25 + "\n";

    public final void testGetFastA() throws BioException {
        Assert.assertEquals(ProteinSequenceTest.FASTA,
            new ProteinSequence(ProteinSequenceTest.SEQ25 + ProteinSequenceTest.SEQ25
                + ProteinSequenceTest.SEQ25 + "IIII" + ProteinSequenceTest.SEQ25).getFastA("description"));
    }

    /*
     * Test method for 'org.pimslims.lab.sequence.getExtinctionCoefficient()' Test that extinction
     * coefficient calculation works
     */
    public void testGetExtinctionCoefficient() throws BioException {
        final float eC = new ProteinSequence(ProteinSequenceTest.SEQUENCE).getExtinctionCoefficient();
        Assert.assertTrue(eC != 0.0);
        // NOTE assert below fails since calculation is 6.1 for empty string
        // assertTrue("Can't calculate Extinction coeff for non-existent
        // sequence!",noEC==0.0);
    }

    /*
     * Test method for 'org.pimslims.lab.sequence.getAbsPt1percent()'
     * Test that calculation of absorbance of a 0.1% solution works
     */
    public void testGetAbsPt1percent() throws BioException {
        final float abs01pc = new ProteinSequence(ProteinSequenceTest.SEQUENCE).getAbsPt1percent();
        Assert.assertTrue(abs01pc != 0.0);
    }

    /*
     * Test method for 'org.pimslims.lab.ConstructAnnotator.annotate(ConstructBean)' Test that isoelectric
     * point calculation works
     */
    public void testIsoelectricPoint() throws BioException {
        final float pI = new ProteinSequence(ProteinSequenceTest.SEQUENCE).getPI();

        Assert.assertTrue("pI is greater than zero", pI > 0);
    }

}
