/**
 * V4_3-web org.pimslims.bioinf.newtarget GenBankMultipleCDSNucleotideParserTest.java
 * 
 * @author cm65
 * @date 13 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf.newtarget;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.bioinf.BioInfException;

/**
 * GenBankMultipleCDSNucleotideParserTest
 * 
 */
public class GenBankMultipleCDSNucleotideParserTest extends TestCase {

    /**
     * Constructor for GenBankMultipleCDSNucleotideParserTest
     * 
     * @param name
     */
    public GenBankMultipleCDSNucleotideParserTest(final String name) {
        super(name);
    }

    /**
     * GenBankMultipleCDSNucleotideParserTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * GenBankMultipleCDSNucleotideParserTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for
     * {@link org.pimslims.bioinf.newtarget.GenBankMultipleCDSNucleotideParser#getDnaSequence()}.
     */
    public void testGetDnaSequence() throws UnsupportedEncodingException, IOException, BioInfException {
        final String gb = PIMSTargetWriterTester.read("X89810.gb");
        final GenBankMultipleCDSNucleotideParser parser =
            new GenBankMultipleCDSNucleotideParser(gb, null, "CAA61933");
        final String seq = parser.getDnaSequence();
        System.out.println(seq);
        Assert.assertTrue("DNA seq expected", 4 < seq.length());
        Assert.assertTrue(seq.startsWith("ATGA"));
    }

}
