/**
 * current-pims-web org.pimslims.presentation ConstructBeanTest.java
 * 
 * @author bl67
 * @date 28 Sep 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.presentation.construct;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;

/**
 * ConstructBeanTest
 * 
 */
public class ConstructBeanTest extends TestCase {
    /**
     * 
     */
    private static final String YES = ConstructBean.TRUE;

    /**
     * 
     */
    private static final String NO = ConstructBean.FALSE;

    private final TargetBean targetBean = new TargetBean();

    ConstructBean cb = new ConstructBean(this.targetBean, new PrimerBean(true), new PrimerBean(false));

    /**
     * Test method for {@link org.pimslims.presentation.construct.ConstructBean#getGcContent()}.
     */
    public void testGetGcContent() {
        this.cb.setDnaSeq("AGGA");
        Assert.assertTrue(50.0 == this.cb.getGcContent());
    }

    /**
     * Test method for {@link org.pimslims.presentation.construct.ConstructBean#getIsStopCodon()}.
     */
    public void testGetIsStopCodon() {
        this.cb.setDnaSeq("AGGA");
        Assert.assertEquals(ConstructBeanTest.NO, this.cb.getIsStopCodon());
        this.cb.setDnaSeq("TAA");
        Assert.assertEquals(ConstructBeanTest.YES, this.cb.getIsStopCodon());
        this.cb.setDnaSeq("TAg");
        Assert.assertEquals(ConstructBeanTest.YES, this.cb.getIsStopCodon());
        this.cb.setDnaSeq("aTga");
        Assert.assertEquals(ConstructBeanTest.YES, this.cb.getIsStopCodon());
    }

    /**
     * Test method for {@link org.pimslims.presentation.construct.ConstructBean#getIsDoMainConstruct()}.
     */
    public void testGetIsDoMainConstruct() {
        this.cb.setDnaSeq("AGGA");
        this.targetBean.setDnaSeq("CAGGAC");
        Assert.assertEquals(ConstructBeanTest.YES, this.cb.getIsDoMainConstruct());
        this.cb.setDnaSeq("AggA");
        Assert.assertEquals(ConstructBeanTest.YES, this.cb.getIsDoMainConstruct());
        this.cb.setDnaSeq("AgCgA");
        Assert.assertEquals(ConstructBeanTest.NO, this.cb.getIsDoMainConstruct());
        this.cb.setDnaSeq("CAGGAC");
        Assert.assertEquals(ConstructBeanTest.NO, this.cb.getIsDoMainConstruct());
    }

    public void testGetPCRProductSequence() {
        final PrimerBean forward = new PrimerBean(true);
        forward.setSequence("AAGTTCTGTTTCAGGGCCCGacagaggagctgaaaaaacttgcaaagca");
        forward.setLengthOnGeneString("29");

        final PrimerBean reverse = new PrimerBean(false);
        reverse.setSequence("ATGGTCTAGAAAGCTTTAagttgcagatccaggcagccg");
        reverse.setLengthOnGeneString("21");

        final ConstructBean c = new ConstructBean(null, forward, reverse);
        c.setDnaSeq("XXX");
        //Assert.assertNotNull(c.getRevPrimer());
        //Assert.assertNotNull(c.getRevOverlapLen());
        //System.out.println("[" + getPCRProductSequence(c, 29, 21) + "]");

    }

    public void testDnaTargetConstruct() {
        final ConstructBean c2 = new ConstructBean(null, null, null);
        c2.setDnaSeq("GTCAGTCAGTCA");
        c2.setDnaTarget("DnaTarget");
        final String startDs = "1";
        final String endDs = "12";
        if ("dnaTarget".equals(this.cb.getDnaTarget())) {
            c2.setTargetDnaStart(Integer.parseInt(startDs));
            c2.setTargetDnaEnd(Integer.parseInt(endDs));
            Assert.assertTrue("Last residue", c2.getTargetDnaEnd().equals(c2.getDnaSeq().length()));
            Assert.assertTrue("First residue", c2.getTargetDnaStart().equals(1));
        }
    }
}
