package org.pimslims.lab;

import java.util.ArrayList;
import java.util.List;

import org.pimslims.lab.primer.PimsStandardPrimerDesigner;
import org.pimslims.lab.primer.YorkPrimerBean;
import org.pimslims.lab.primer.YorkPrimerUtility;

import junit.framework.TestCase;

public class TestYorkPrimerUtility extends TestCase {

    private static final String FTYPE = "F"; // primer type
    
    private static final String PROCESSED = "The tag is processed";

    private static List<YorkPrimerBean> YPB;

    private static final String[] PRIMARRAY = { "ATGATGATGATG", "ATGATGATGATGTTG", "ATATATGGTTACCT" };

    private static final float WRONGTM = 55;
    
    private static final Integer PRLEN = 12; 
    
    private static List<String> primers = new ArrayList<String>();
    {
        for (int i = 0; i < PRIMARRAY.length; i++) {
            primers.add(PRIMARRAY[i]);
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestYorkPrimerUtility.class);
    }

    public TestYorkPrimerUtility(String arg0) {
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
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * Primer Beans ArrayList is populated
     */
    public void testMakeYPBs() {
        YPB = PimsStandardPrimerDesigner.makeYPBs(primers, FTYPE);
        assertNotNull(YPB);
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * Primer Beans ArrayList is not populated if String array is empty
     */
    public void testDontMakeYPBs() {
        // How to test for making failure to make beans if primer array is empty
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * first primer bean contains expected sequence
     */
    public void testBeanHasSeq() {
        YPB = PimsStandardPrimerDesigner.makeYPBs(primers, FTYPE);
        assertEquals(PRIMARRAY[0], YPB.get(0).getPrimerSeq());
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * first primer bean Tm value
     */
    public void testBeanHasTm() {
        float wrongTm = WRONGTM;
        YPB = PimsStandardPrimerDesigner.makeYPBs(primers, FTYPE);
        assertTrue(YPB.get(0).getPrimerTm() >= 0);
        assertFalse(YPB.get(0).getPrimerTm() == null);
        assertFalse(YPB.get(0).getPrimerTm() == wrongTm);
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * first primer bean contains expected primerType
     */
    public void testPrimerType() {
        String revType = "R";
        YPB = PimsStandardPrimerDesigner.makeYPBs(primers, revType);
        assertFalse(YPB.get(0).getPrimerType() == FTYPE);
        assertTrue(YPB.get(0).getPrimerType() == revType);
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.makeYPBs(ArrayList<String>, String)' Test that
     * first primer bean contains expected sequence length
     */
    public void testPrimerLength() {
        Integer prLength = PRLEN;
        YPB = PimsStandardPrimerDesigner.makeYPBs(primers, FTYPE);
        assertEquals(YPB.get(0).getPrimerLength(), prLength);
        assertFalse(YPB.get(0).getPrimerLength() == PRIMARRAY[1].length());
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.processTag(String)' Test empty string
     */
    public void testEmptyTag() {
        String emptyTag = "";
        String testTag = YorkPrimerUtility.processTag(emptyTag);
        assertTrue("Empty string returned as empty string", "".equals(testTag));
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.processTag(String)' Test processing of string
     * without ': ' match
     */
    public void testNocolon() {
        String noColon = "AAGGCCTT";
        String testTag = YorkPrimerUtility.processTag(noColon);
        assertTrue("The tag is not processed", "".equals(testTag));
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.processTag(String)' Test no processing of string
     * with ':', need ': '
     */
    public void testColonNoSpace() {
        String noSpace = "Test:AAGGCCTT";
        String testTag = YorkPrimerUtility.processTag(noSpace);
        assertEquals("The tag is not processed", testTag, "");
    }
    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.processTag(String)' 
     * Test no processing of 'none' option
     */
    public void testNone() {
        String none = "none";
        String testTag = YorkPrimerUtility.processTag(none);
        assertEquals("The tag is not processed", testTag, "");
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.processTag(String)' Test processing of string with
     * correct format
     */
    public void testCorrectFormat() {
        String goodTag = "Test: AAGGCCTT";
        String testTag = YorkPrimerUtility.processTag(goodTag);
        assertEquals(PROCESSED, "AAGGCCTT", testTag);
        assertFalse(PROCESSED, goodTag == testTag);
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.createEndSeqs(String)' Test processing of string
     * with correct format
     */
    public void testCorrectString() {
        String goodTag = "LIC-F: AAGGCCTT";
        String[] goodEnds = YorkPrimerUtility.createEndSeqs(goodTag);
        assertTrue(PROCESSED, goodEnds != null);
        assertTrue("The tag was LIC-F", goodEnds[0].equalsIgnoreCase("QWRTHHHHHH"));

    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.createEndSeqs(String)' Test empty string not
     * processed
     */
    public void testEmptyString() {
        String emptyTag = "";
        String[] goodEnds = YorkPrimerUtility.createEndSeqs(emptyTag);
        assertTrue("The tag was empty", goodEnds[0] == null);
        // assertTrue("The tag was LIC-F",
        // goodEnds[0].equalsIgnoreCase("QWRTHHHHHH"));

    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.createEndSeqs(String)' Test wrong format string is
     * not processed
     */
    public void testWrongString() {
        String badTag = "AAGGCCTT";
        String[] goodEnds = YorkPrimerUtility.createEndSeqs(badTag);
        assertTrue("The tag was not processed", goodEnds[0] == null);
    }

    /*
     * Test method for 'org.pimslims.lab.YorkPrimerUtility.createEndSeqs(String)' Test wrong format string is
     * not processed
     */
    public void testCleavableTag() {
        String cleavableTag = "LIC-3CF: GATCGATCGATCGATC";
        String[] goodEnds = YorkPrimerUtility.createEndSeqs(cleavableTag);
        assertTrue(PROCESSED, goodEnds != null);
        assertFalse("The tag was not processed", goodEnds[0] == null);
        assertTrue("The tag was LIC-3CF", goodEnds[0].equalsIgnoreCase("QWRTHHHHHHQWRT"));
        assertTrue("The tag was LIC-3CF", goodEnds[1].equalsIgnoreCase("QWRT"));
    }

}
