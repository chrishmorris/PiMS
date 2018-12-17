/**
 * pims-web org.pimslims.lab.sequence AmbiguousDnaSequenceTest.java
 * 
 * @author Marc Savitsky
 * @date 11 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.sequence;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.seq.Sequence;

/**
 * AmbiguousDnaSequenceTest
 * 
 */
public class AmbiguousDnaSequenceTest extends TestCase {

    private static final String SEQUENCE = "GATCGATCGAGATCGATCGAGATCGATCGAGATCGATCGAGATCGATCGA"; // test

    /**
     * Constructor for AmbiguousDnaSequenceTest
     * 
     * @param name
     */
    public AmbiguousDnaSequenceTest(final String name) {
        super(name);
    }

    /**
     * AmbiguousDnaSequenceTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * AmbiguousDnaSequenceTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testGetLength() {
        Assert.assertEquals(2, new AmbiguousDnaSequence("AT").getLength());
    }

    public final void testWhiteSpace() {
        try {
            new AmbiguousDnaSequence("AAAAAAAAAA TTTTTTTT\r\n");

        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            Assert.fail("Couldn't create sequence with white space");
        }
    }

    public final void testBadSequence() {
        try {
            final AmbiguousDnaSequence adna = new AmbiguousDnaSequence("ATN");
            final Sequence sequence = adna.getBiojavaSequence("ATN");
            Assert.assertNotNull(sequence);

        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            Assert.fail("Couldn't create ambiguous sequence");
        }
    }

    public final void testCountRegexpInString() {
        try {
            Assert.assertEquals(10, AmbiguousDnaSequence.countRegexpInString(
                AmbiguousDnaSequenceTest.SEQUENCE, "GAT"));
        } catch (final IllegalArgumentException e) {
            Assert.fail("Couldn't create ambiguous sequence");
        }
    }

}
