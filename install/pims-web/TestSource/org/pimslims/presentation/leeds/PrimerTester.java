package org.pimslims.presentation.leeds;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.PrimerBeanReader;
import org.pimslims.presentation.PrimerBeanWriter;
import org.pimslims.presentation.construct.ConstructBeanWriter;

public class PrimerTester extends TestCase {

    /**
     * SEQUENCE String
     */
    private static final String SEQUENCE = "acgtacgtacgtacgt";

    /**
     * FORWARD_PRIMER_NAME String
     */
    private static final String FORWARD_PRIMER_NAME = "testf" + System.currentTimeMillis();

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion wv = null;

    PrimerBean fprimer;

    PrimerBean rprimer;

    static final String name = "+pMPSIL0196R1";

    static final String rsequence = PrimerTester.SEQUENCE;

    static final String particularity = "Desalted";

    /**
     * Constructor for TestPlasmid.
     * 
     * @param methodName
     */
    public PrimerTester(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.wv = this.model.getTestVersion();
        Assert.assertNotNull(this.wv);
        this.createBarePrimer();
        this.createComplexPrimer();
    }

    private void createBarePrimer() {
        this.fprimer =
            new PrimerBean(PrimerTester.FORWARD_PRIMER_NAME, PrimerTester.SEQUENCE, PrimerBean.FORWARD,
                "ftag");
    }

    public void createComplexPrimer() throws AccessException, ConstraintException {
        this.rprimer = new PrimerBean(PrimerTester.name, PrimerTester.rsequence, false, "");
        this.rprimer.setParticularity(PrimerTester.particularity);
        this.rprimer.setRestrictionsite("pstI");
        this.rprimer.setLengthOnGeneString("7");
        this.rprimer.setTmfull("50.3");
        this.rprimer.setTmGene("60.3");
        this.rprimer.setTmseller("55.0");

        PrimerBeanWriter.writePrimer(this.wv, this.rprimer, null);

        Assert.assertEquals("Tm seller is wrong", "55.0",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getTmseller());
    }

    public void testCreateBarePrimer() throws AccessException, ConstraintException {
        final Sample sample = PrimerBeanWriter.writePrimer(this.wv, this.fprimer, null);
        Assert.assertEquals(1, sample.getSampleComponents().size());

        final PrimerBean bean = PrimerBeanReader.readPrimer(sample);
        Assert.assertEquals(PrimerTester.FORWARD_PRIMER_NAME, bean.getName());
        Assert.assertEquals(PrimerTester.SEQUENCE.toUpperCase(), bean.getSequence());
        Assert.assertTrue(bean.isForward());
        Assert.assertFalse(bean.isReverse());
        Assert.assertEquals("ftag", bean.getTag());
    }

    public void testUpdate() throws AccessException, ConstraintException {
        final Sample sample = PrimerBeanWriter.writePrimer(this.wv, this.fprimer, null);
        sample.setName(PrimerTester.FORWARD_PRIMER_NAME + "2");
        final SampleComponent component = sample.getSampleComponents().iterator().next();
        final Primer primer = (Primer) component.getRefComponent();
        primer.setLengthOnGene(22);
        primer.setSequence("ATG" + PrimerTester.SEQUENCE.toUpperCase());

        final PrimerBean bean = PrimerBeanReader.readPrimer(sample);
        Assert.assertEquals(PrimerTester.FORWARD_PRIMER_NAME + "2", bean.getName());
        Assert.assertEquals("ATG" + PrimerTester.SEQUENCE.toUpperCase(), bean.getSequence());
        Assert.assertEquals(22, bean.getOverlapLength());
    }

    public void testGetRestrictionSite() {
        Assert.assertEquals("Restriction site is wrong", "pstI",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getRestrictionsite());
    }

    public void testGetSequence() {
        Assert.assertEquals("Sequence is wrong", PrimerTester.rsequence.toUpperCase(), PrimerBeanReader
            .readPrimer(this.wv, PrimerTester.name).getSequence());
    }

    public void testGetGCFull() {
        Assert.assertEquals("Sequence GC content is wrong", 50.0f,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getGcFull());
    }

    public void testGetLength() {
        Assert.assertEquals("Sequence length is wrong", PrimerTester.rsequence.length(), PrimerBeanReader
            .readPrimer(this.wv, PrimerTester.name).getLength());
    }

    public void testGetParticularity() {
        Assert.assertEquals("Particularity is wrong", PrimerTester.particularity, PrimerBeanReader
            .readPrimer(this.wv, PrimerTester.name).getParticularity());
    }

    public void testSetLengthOnGene() throws ConstraintException, AccessException, ClassNotFoundException {
        this.rprimer.setLengthOnGeneString("6");

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Length on the gene set wrong", "6",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getLengthOnGeneString());
    }

    public void testSetSequence() throws ConstraintException, AccessException, ClassNotFoundException {
        final String seqA = "AAAAAAAA";
        final String seqB = "GGGGGGGG";
        this.rprimer.setSequence(seqA);

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Sequence set wrong", this.rprimer.getSequence(), seqA);
        this.rprimer.setSequence(seqB);
        Assert.assertEquals("Sequence set wrong", this.rprimer.getSequence(), seqB);

    }

    public void testSetAmount() throws ConstraintException, AccessException, ClassNotFoundException {
        final String amount = "32.2";
        this.rprimer.setAmount(amount);

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Amount set wrong", Float.valueOf(amount),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getAmount());
    }

    public void testSetOD() throws ConstraintException, AccessException, ClassNotFoundException {
        final String od = "33.3";
        this.rprimer.setOD(od);

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("OD set wrong", od, PrimerBeanReader.readPrimer(this.wv, PrimerTester.name)
            .getOD());

    }

    public void testgetAmountperOD() throws ConstraintException, AccessException, ClassNotFoundException {
        final String od = "31.3";
        final String am = "421.1";

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Amount per OD calculated wrong", null,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getAmount());
        this.rprimer.setAmount(am);
        Assert.assertEquals("Amount per OD calculated wrong", null,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getAmountperOD());
        this.rprimer.setOD(od);
        final NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        Assert.assertEquals("Amount per OD calculated wrong", "13.45", this.rprimer.getAmountperOD());

    }

    public void testSetSequenceFromPrimerBean() throws ConstraintException, AccessException,
        ClassNotFoundException {
        final String seq = "AAAAAAAA";
        this.rprimer.setSequence(seq);

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Sequence set wrong", seq, PrimerBeanReader
            .readPrimer(this.wv, PrimerTester.name).getSequence());
    }

    public void testSetParticularity() throws ConstraintException, AccessException, ClassNotFoundException {
        final String particularity = "HPLC purified";
        this.rprimer.setParticularity(particularity);
        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Particularity was set wrong", particularity,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getParticularity());
    }

    public void testSetTmFull() throws ConstraintException, AccessException, ClassNotFoundException {
        final String tmfull = "72.5";
        this.rprimer.setTmfull(tmfull);

        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Tm full set wrong", tmfull,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getTmfull());
    }

    public void testSetRestrictionSite() throws ConstraintException, AccessException, ClassNotFoundException {
        final String rsite = "BstAI";
        this.rprimer.setRestrictionsite(rsite);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Restriction site set wrong", rsite,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getRestrictionsite());
    }

    public void testSetGCGene() throws ConstraintException, AccessException, ClassNotFoundException {
        final String gcgene = "33";
        this.rprimer.setGcgene(gcgene);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("GC gene set wrong", gcgene,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getGCGene());
    }

    public void testSetTmSellerFromPrimerBean() throws ConstraintException, AccessException,
        ClassNotFoundException {
        final String tmseller = "33.0";
        this.rprimer.setTmseller(tmseller);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Tm seller set wrong", PrimerBeanReader.readPrimer(this.wv, PrimerTester.name)
            .getTmseller(), tmseller);
    }

    public void testSetLengthOnGeneFromPrimerBean() throws ConstraintException, AccessException,
        ClassNotFoundException {
        final String lengthongene = "21";
        this.rprimer.setLengthOnGeneString(lengthongene);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Length on gene set wrong", lengthongene,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getLengthOnGeneString());
    }

    public void testSetConcentration() throws ConstraintException, AccessException, ClassNotFoundException {
        final float concentration = 56f;
        this.rprimer.setConcentration(new Float(concentration).toString());
        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Concentration set wrong", 56f,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getConcentration());
    }

    public void testSetPosition() throws ConstraintException, AccessException, ClassNotFoundException {
        final String position = "A3";
        this.rprimer.setPosition(position);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Position set wrong", "A03",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getPosition());
    }

    public void testSetPositionBarcode() throws ConstraintException, AccessException, ClassNotFoundException {

        final String position = "A3";
        final String barcode1 = "987654321999";
        final String barcode2 = "987654321999";

        this.rprimer.setPosition(position);
        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertTrue(PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getPositionBarcode()
            .equals(""));
        this.rprimer.setPositionBarcode(barcode1);
        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Set Position barcode fail", barcode1,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getPositionBarcode());
        this.rprimer.setPositionBarcode("12");

        Assert.assertEquals("Position set wrong", "A03",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getPosition());
        Assert.assertEquals("Set Position barcode fail", barcode2,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getPositionBarcode());
    }

    public void testSetNewBox() throws ConstraintException, AccessException, ClassNotFoundException {
        final String box = "vincentBox";
        this.rprimer.setBox(box);

        PrimerBeanWriter.update(this.wv, this.rprimer);

        final Map<String, Object> holdProp = new HashMap<String, Object>();
        holdProp.put(AbstractHolder.PROP_NAME, box);
        final Holder vincentHolder = this.wv.findFirst(org.pimslims.model.holder.Holder.class, holdProp);
        Assert.assertEquals("Box set wrong", vincentHolder.get_Hook(),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getBox());
    }

    public void testEditBox() throws AccessException, ConstraintException, ClassNotFoundException {
        Holder vincentHolder = null;

        final Map<String, Object> holdProp = new HashMap<String, Object>();
        final Map<String, Object> holdCatProp = new HashMap<String, Object>();
        holdCatProp.put(HolderCategory.PROP_NAME, "superbox");
        final HolderCategory hcat =
            this.wv.create(org.pimslims.model.reference.HolderCategory.class, holdCatProp);
        holdProp.put(AbstractHolder.PROP_NAME, "oldVincentBox");
        holdProp.put(AbstractHolder.PROP_HOLDERCATEGORIES, Util.makeList(hcat));
        vincentHolder = this.wv.create(org.pimslims.model.holder.Holder.class, holdProp);
        //System.out.println("PrimerTester.setBox(" + vincentHolder.get_Name() + ":"
        //    + vincentHolder.get_Hook() + ")");
        this.rprimer.setBox(vincentHolder.get_Hook());
        PrimerBeanWriter.update(this.wv, this.rprimer);

        Assert.assertEquals("Box set wrong", vincentHolder.get_Hook(),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getBox());
    }

    public void testSetBoxBarcode() throws AccessException, ConstraintException, ClassNotFoundException {
        Holder vincentHolder = null;
        final String barcode = "934567890"; // only 9 digits overwise may be bigger
        // that Integer

        final Map<String, Object> holdProp = new HashMap<String, Object>();
        final Map<String, Object> holdCatProp = new HashMap<String, Object>();
        holdCatProp.put(HolderCategory.PROP_NAME, "superbox");
        final HolderCategory hcat =
            this.wv.create(org.pimslims.model.reference.HolderCategory.class, holdCatProp);
        holdProp.put(AbstractHolder.PROP_NAME, "oldVincentBox");
        holdProp.put(AbstractHolder.PROP_HOLDERCATEGORIES, Util.makeList(hcat));
        vincentHolder = this.wv.create(org.pimslims.model.holder.Holder.class, holdProp);
        this.rprimer.setBox(vincentHolder.get_Hook());
        Assert.assertNull(this.rprimer.getBoxBarcode());
        this.rprimer.setBoxBarcode(barcode);
        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Set Barcode fails", barcode,
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getBoxBarcode());
        // Far too big number for an Integer
        // thus this fails
        this.rprimer.setBoxBarcode("12345678901");
        Assert.assertNotSame("Set Barcode fails", "12345678901",
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getBoxBarcode());

        Assert.assertEquals("Box set wrong", vincentHolder.get_Hook(),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getBox());
    }

    @SuppressWarnings("deprecation")
    public void testSetLocation() throws ConstraintException, AccessException, ClassNotFoundException {

        this.rprimer.setBox("vincentBox");
        this.rprimer.setLocation("freezer MPSI 1");
        PrimerBeanWriter.update(this.wv, this.rprimer);

        final Map<String, Object> locProp = new HashMap<String, Object>();
        locProp.put(Location.PROP_NAME, "freezer MPSI 1");
        final Location loc = this.wv.findFirst(org.pimslims.model.location.Location.class, locProp);
        Assert.assertEquals("Location set wrong", loc.get_Hook(),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getLocation());
    }

    public void testSetName() throws AccessException, ConstraintException, ClassNotFoundException {
        final String newName = "textPrimer";

        this.rprimer.setName(newName);
        PrimerBeanWriter.writePrimer(this.wv, this.rprimer, null);

        Assert.assertEquals("Name set wrong", "textPrimer", PrimerBeanReader.readPrimer(this.wv, newName)
            .getName());
    }

    public void testSetSeller() throws ConstraintException, AccessException, ClassNotFoundException {
        final String seller = "Invitrogen";

        this.rprimer.setSeller(seller);
        PrimerBeanWriter.update(this.wv, this.rprimer);

        Organisation organ =
            (Organisation) this.wv.get(PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getSeller());
        Assert.assertEquals("Organisation set wrong", seller, organ.get_Name());

        final Map<String, Object> orgProp = new HashMap<String, Object>();
        orgProp.put(Organisation.PROP_NAME, "MPSI");
        organ = null;

        organ =
            (Organisation) Util.getOrCreate(this.wv, org.pimslims.model.people.Organisation.class, null,
                orgProp);
        this.rprimer.setSeller(organ.get_Hook());
        PrimerBeanWriter.update(this.wv, this.rprimer);
        Assert.assertEquals("Organisation set wrong", organ.get_Hook(),
            PrimerBeanReader.readPrimer(this.wv, PrimerTester.name).getSeller());

    }

    public void testCleanLocation() {
        final String loc = " :Freezer MPSI 1 ";
        Assert.assertEquals("Location clean failed ", "Freezer MPSI 1", PrimerForm.cleanLocation(loc));
    }

    public void testBeanDirection() {
        Assert.assertTrue(this.fprimer.isDirect());
        this.fprimer.setDirection(null);
        Assert.assertNull(this.fprimer.isDirect());
        Assert.assertFalse(this.rprimer.isDirect());
        Assert.assertNotNull(this.rprimer.isDirect());
    }

    public void testPrimerDirection() {

        final String r = ConstructBeanWriter.RPRIMER.substring(0, 7).toLowerCase();
        final String f = ConstructBeanWriter.FPRIMER.substring(0, 7).toLowerCase();

        Assert.assertEquals("Direction set wrong ", r, new PrimerBean(PrimerTester.name,
            PrimerTester.rsequence, false, "").getDirection());
        Assert.assertEquals(Boolean.TRUE, new Boolean(this.fprimer.isDirect()));
        Sample fprsample = null;
        try {
            // fprsample = fprimer.create(wv);
            fprsample = PrimerBeanWriter.writePrimer(this.wv, this.fprimer, null);
            Assert.assertEquals(Boolean.TRUE, fprsample.getIsActive());
        } catch (final AccessException e) {
            Assert.fail(e.getMessage());
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        }
        // was Assert.assertEquals(FormFieldsNames.FPRIMER, fprsample.getRefSample().getName());
        Assert.assertEquals(1, fprsample.getSampleCategories().size());
        Assert.assertEquals(ConstructBeanWriter.FPRIMER, fprsample.getSampleCategories().iterator().next()
            .getName());
        final String direction =
            new PrimerBean(PrimerTester.name, PrimerTester.rsequence, true, "").getDirection();
        Assert.assertEquals("Direction set wrong: " + direction, f, direction);
    }

    public void testToString() {
        final PrimerBean primerbean = new PrimerBean(PrimerTester.name, PrimerTester.rsequence, false, "");
        primerbean.toString();
        this.fprimer.toString();
        this.fprimer.setDirection(null);
        this.fprimer.toString();
    }

    /*
     * @see TestCase#tearDown()
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

        // these objects are created each test
        this.fprimer = null;
        this.rprimer = null;
        this.wv = null;

        //System.out.println("Total memory [" + Runtime.getRuntime().totalMemory() + "]");
        //System.out.println(" Used memory ["
        //    + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "]");
        //System.out.println(" Free memory [" + Runtime.getRuntime().freeMemory() + "]");
    }

}
