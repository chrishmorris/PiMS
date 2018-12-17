/**
 * pims-web org.pimslims.presentation PrimerBeanTest.java
 * 
 * @author Marc Savitsky
 * @date 4 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.Sample;
import org.pimslims.test.AbstractTestCase;

/**
 * PrimerBeanTest
 * 
 */
public class PrimerBeanTest extends AbstractTestCase {

    PrimerBean bean;

    private final String name = "PrimerBeanTest";

    private final String barcode = "1234567890";

    private final String position = "C06";

    private final String direction = "reverse";

    private final String sequence = "ATGCAGTTAGCAT";

    private final String seller = "Invitrogen/Life Technologies";

    private final String boxbarcode = "F11";

    private final String box = "3";

    private final String location = "Freezer MPSI 1";

    private final String particularity = "Desalted";

    private final String restrictionsite = "pstI";

    private final String lengthongene = "7";

    private final String tmfull = "50.3";

    private final String tmgene = "60.3";

    private final String tmseller = "55.0";

    private final String gcgene = "58.0";

    private final String od = null;

    private final Float amount = new Float("56.4");

    private final Float concentration = new Float("0.5");

    private final Float molecularmass = new Float("32164");

    /**
     * @param name
     */
    public PrimerBeanTest(final String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.wv = this.getWV();

        this.bean = new PrimerBean("forward".equals(this.direction));
        this.bean.setName(this.name);
        this.bean.setPositionBarcode(this.barcode);
        this.bean.setPosition(this.position);
        this.bean.setSeller(this.seller);
        this.bean.setAmount(this.amount.toString());
        this.bean.setConcentration(this.concentration.toString());
        this.bean.setDirection(this.direction);
        this.bean.setSequence(this.sequence);
        this.bean.setTmfull(this.tmfull);
        this.bean.setMolecularMass(this.molecularmass.toString());
        // was this.bean.setGcfull(new Double(new DnaSequence(this.sequence).getGCContent()).toString());
        this.bean.setBox(this.box);
        this.bean.setBoxBarcode(this.boxbarcode);
        this.bean.setLocation(this.location);
        this.bean.setParticularity(this.particularity);
        this.bean.setTmGene(this.tmgene);
        this.bean.setTmseller(this.tmseller);
        this.bean.setGcgene(this.gcgene);
        this.bean.setLengthOnGeneString(this.lengthongene);
        this.bean.setRestrictionsite(this.restrictionsite);
        this.bean.setOD(this.od);
        this.bean.setTag("HHHHHH");

        try {
            PrimerBeanWriter.writePrimer(this.wv, this.bean, null);
        } catch (final AccessException e) {
            this.wv.abort();
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            this.wv.abort();
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

    }

    public void testSetExtension() {
        PrimerBean b =
            new PrimerBean("name", " box", " position", "1", null, "5", " particularity", " restrictionsite",
                " seller", "AAATTTTT", " location", "50", " tmgene", " tmseller", PrimerBean.FORWARD, "tag");
        Assert.assertEquals("name", b.getName());
        Assert.assertEquals("TTTTT", b.getOverlap());
        Assert.assertEquals("AAA", b.getExtension());
        b.setExtension("CCCC");
        Assert.assertEquals("TTTTT", b.getOverlap());
        Assert.assertEquals("CCCC", b.getExtension());
        Assert.assertEquals("tag", b.getTag());

        b =
            new PrimerBean("name", " box", " position", "1", null, "5", " particularity", " restrictionsite",
                " seller", "GGGCCCCC", " location", "50", " tmgene", " tmseller", PrimerBean.REVERSE, "tag");
        Assert.assertEquals("name", b.getName());
        Assert.assertEquals("CCCCC", b.getOverlap());
        Assert.assertEquals("GGG", b.getExtension());
        b.setExtension("TTTT");
        Assert.assertEquals("CCCCC", b.getOverlap());
        Assert.assertEquals("TTTT", b.getExtension());
        Assert.assertEquals("tag", b.getTag());
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        Assert.assertFalse(this.wv.isCompleted());
        if (this.wv.isCompleted()) {
            // can't tidy up
            return;
        }
        if (null != this.wv) {
            this.wv.abort(); // not testing persistence
        }
    }

    public void testPrimer() {

        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, this.name);

        Assert.assertEquals("amount", myBean.getAmount(), this.bean.getAmount());
        Assert.assertEquals("amount per OD", myBean.getAmountperOD(), this.bean.getAmountperOD());
        Assert.assertEquals("HHHHHH", myBean.getTag());
    }

    public void testNotPrimer() throws ConstraintException {

        final Sample sample = new Sample(this.wv, "not" + System.currentTimeMillis());
        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, sample.getName());
        Assert.assertNull(myBean);
    }

    @SuppressWarnings("deprecation")
    public void testPrimerHolder() {

        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, this.name);

        final Map<String, Object> holderProp = new HashMap<String, Object>();
        holderProp.put(AbstractHolder.PROP_NAME, this.box);
        final Holder holder = this.wv.findFirst(Holder.class, holderProp);

        Assert.assertEquals("box", myBean.getBox(), holder.get_Hook());
        Assert.assertEquals("boxbarcode", myBean.getBox(), holder.get_Hook());

        final Map<String, Object> locationProp = new HashMap<String, Object>();
        locationProp.put(Location.PROP_NAME, this.location);
        final Location location = this.wv.findFirst(Location.class, locationProp);

        Assert.assertEquals("location", myBean.getLocation(), location.get_Hook());
    }

    public void testPrimerPrimer() {

        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, this.name);

        Assert.assertEquals("direction", myBean.getDirection(), this.direction);
        Assert.assertEquals("sequence", myBean.getSequence(), this.sequence);
        // Assert.assertEquals("molecular mass", myBean.getMolecularMass(), this.molecularmass);
        Assert.assertEquals("tmfull", myBean.getTmfull(), this.tmfull);
    }

    public void testPrimerSample() {

        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, this.name);

        Assert.assertEquals("name", myBean.getName(), this.name);
        //assertEquals("batchnum", myBean..getBatchNum(), barcode);
        Assert.assertEquals("position", myBean.getPosition(), this.position);

        final Map<String, Object> organisationProp = new HashMap<String, Object>();
        organisationProp.put(Organisation.PROP_NAME, this.seller);
        final Organisation organisation = this.wv.findFirst(Organisation.class, organisationProp);

        Assert.assertEquals("seller", myBean.getSeller(), organisation.get_Hook());
        Assert.assertEquals("amount", myBean.getAmount(), this.amount);
    }

    public void testPrimerExperiment() {

        final PrimerBean myBean = PrimerBeanReader.readPrimer(this.wv, this.name);

        Assert.assertEquals("particularity", myBean.getParticularity(), this.particularity);
        Assert.assertEquals("tmgene", myBean.getTmGene(), this.tmgene);
        Assert.assertEquals("tmseller", myBean.getTmseller(), this.tmseller);
        Assert.assertEquals("gcgene", myBean.getGCGene(), this.gcgene);
        Assert.assertEquals("lengthongene", myBean.getLengthOnGeneString(), this.lengthongene);
        Assert.assertEquals("restrictionsite", myBean.getRestrictionsite(), this.restrictionsite);
        Assert.assertEquals("od", myBean.getOD(), this.od);
    }

    public void testGetOtherName() {
        Assert.assertEquals("xxxr", PrimerBeanReader.getOtherPrimerName("xxxf"));
        Assert.assertEquals("xxxf1", PrimerBeanReader.getOtherPrimerName("xxxr1"));
        Assert.assertEquals("xrfRFxF", PrimerBeanReader.getOtherPrimerName("xrfRFxR"));
        Assert.assertEquals("xrfRFxR1", PrimerBeanReader.getOtherPrimerName("xrfRFxF1"));
    }
}
