package org.pimslims.ispyb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import junit.framework.Assert;

import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.junit.After;
import org.junit.Test;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.AnomalousScatterer;
import uk.ac.diamond.ispyb.client.IspybServiceStub.AnomalousScattering;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BLSample;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CollectPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollection;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollectionInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DeliveryAgent;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Dewar;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ExperimentType_type3;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Holder;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Image;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ProcessingLevel_type1;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ScreenPlan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SweepInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Temperature_type3;
import uk.ac.diamond.ispyb.client.LoginExceptionException;

public class EndToEndTest extends AbstractTest {

    /**
     * @param certificatePassword
     * @param certificateFilePath
     */
    public EndToEndTest() {
        super();
    }

    /**
     * ACTUAL_SHIPPING_NAME String
     */
    private static final String ACTUAL_SHIPPING_NAME = "test1307700021250";

    /**
     * SHIPPING_NAME String
     */
    private static final String NEW_SHIPPING_NAME = "shipping" + System.currentTimeMillis();

    /**
     * PUCK_BARCODE String
     */
    private static final String PUCK_BARCODE = UNIQUE + "c";

    /**
     * DEWAR_BARCODE String
     */
    private static final String DEWAR_BARCODE = UNIQUE + "d";

    private static final String LOOP_BARCODE = UNIQUE + "loop";

    @Test
    public void testClient() {
        new Client();
    }

    @After
    public void tearDown() {
        // no action required
    }

    @Test
    public void testBadAuthenticate() throws BadDataExceptionException, LoginExceptionException {
        boolean loggedIn = new Client().authenticate("nonesuch", "nonesuch");
        Assert.assertTrue(!loggedIn);
    }

    @Test
    public void testAuthenticate() throws BadDataExceptionException, LoginExceptionException, IOException {
        Client client = new Client();
        boolean loggedIn = login(client);
        Assert.assertTrue(loggedIn);
    }

    @Test
    public void testBasicCrystalDetails() throws AxisFault, BadDataExceptionException, NotFoundException,
        MalformedURIException {

        CrystalDetails details =
            makeCrystalDetails(Collections.singleton(makeUniqueCrystal("b")), PROJECT_UUID);
        validate(details);
        String response = this.client.crystalDetails(details);
        assertEquals("OK", response);

    }

    @Test
    public void testSweepInformation() {
        SweepInformation sweep = getDefaultSweep(2.0d);
        validate(sweep);
    }

    @Test
    public void testDiffractionPlan() throws AxisFault, BadDataExceptionException, NotFoundException {
        try {
            DiffractionPlan plan = makeDiffractionPlan(null);
            validate(plan);
            String response = this.client.diffractionPlan(plan);
            assertEquals("OK", response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testAnomalousScatterer() throws AxisFault, BadDataExceptionException, NotFoundException {

        // actually this bean does not seem to be used:
        AnomalousScatterer scatterer = new AnomalousScatterer();
        scatterer.setElement("Se");
        scatterer.setAssessmentMethod("");
        validate(scatterer);

        DiffractionPlan plan = makeDiffractionPlan("Se");
        validate(plan);
        String response = this.client.diffractionPlan(plan);
        assertEquals("OK", response);
    }

    /**
     * EndToEndTest.makeDiffractionPlan TODO move this to IspbyBeanFactory when it is tested
     * 
     * @param scatterer TODO
     * 
     * @return
     */
    DiffractionPlan makeDiffractionPlan(String scatterer) {
        String projectUuid = PROJECT_UUID;
        String planUuid = DEWAR_BARCODE;// will probably be URL of experiment in PiMS
        String crystalUuid = CRYSTAL_UUID;
        CollectPlan collectionPlan = makeCollectionPlan();
        collectionPlan.setAnomalousScattererElement(scatterer);
        SweepInformation sweep = getDefaultSweep(2.0d);
        ScreenPlan screen = null;
        DiffractionPlan ret =
            makeDiffractionPlan(projectUuid, planUuid, crystalUuid, collectionPlan, sweep, screen);
        return ret;
    }

    /**
     * @return
     */
    private CollectPlan makeCollectionPlan() {
        CollectPlan collection;
        collection = new CollectPlan();
        ExperimentType_type3 type = ExperimentType_type3.value6; // this is probably "OSC"
        collection.setExperimentType(type);

        collection.setAnneal(false);

        Temperature_type3 temp = Temperature_type3.value2; // this is probably "100K"
        collection.setTemperature(temp);
        ProcessingLevel_type1 level = ProcessingLevel_type1.value1; // this is probably "Collect"
        collection.setProcessingLevel(level);
        return collection;
    }

    @Test
    public void testBadCrystalShipping() throws IOException, LoginExceptionException, NotFoundException {
        CrystalShipping shipping = new CrystalShipping();
        shipping.setProjectUUID("nonesuch");
        shipping.setDeliveryAgent(makeDeliveryAgent("Fedex", Calendar.getInstance(), Calendar.getInstance()));
        Crystal crystal = makeUniqueCrystal("bcs");
        Holder[] holders = new Holder[] { makeLoop(crystal, "1", LOOP_BARCODE) };
        shipping.setDewar(getDewars(Collections.singletonList(crystal), holders, "bcs"));
        shipping.setName("test" + System.currentTimeMillis());
        validate(shipping);

        try {
            this.client.crystalShipping(shipping);
            fail("Bad beamtime allocation accepted");
        } catch (BadDataExceptionException e) {
            // that's OK
        } catch (AxisFault e) {
            assertTrue(e.getMessage().contains("no proposal record found"));
        }
    }

    @Test
    public void testCrystalShipping() throws BadDataExceptionException, IOException, LoginExceptionException,
        NotFoundException {
        Matcher m2 =
            Client.SQL_ERROR
                .matcher("problem with sql query for user:  cm65 uk.ac.diamond.service.IspybServiceSkeleton#crystalShippingORA-00904: \"TLS\": invalid identifier\r\n");
        assertTrue(m2.matches());

        Crystal crystal = makeUniqueCrystal("cs");
        List<Crystal> crystals = Collections.singletonList(crystal);
        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
        this.client.crystalDetails(details);

        // now make the shipping
        CrystalShipping shipping = makeUniqueShipping(crystal, "cs", Calendar.getInstance());
        validate(shipping);
        Holder holder = shipping.getDewar()[0].getContainer()[0].getHolder()[0];
        assertEquals(details.getCrystalDetailsSequence_type0()[0].getCrystal().getProtein().getAcronym(),
            holder.getCrystalIdentifier().getCrystalIdentifierSequence_type0().getProteinAcronym());

        String response = this.client.crystalShipping(shipping);
        assertEquals("OK", response);
    }

    @Test
    public void test31March() throws BadDataExceptionException, IOException, LoginExceptionException,
        NotFoundException {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MONTH, Calendar.MARCH);
        date.set(Calendar.DAY_OF_MONTH, 31);

        Crystal crystal = makeUniqueCrystal("cs");
        List<Crystal> crystals = Collections.singletonList(crystal);
        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
        this.client.crystalDetails(details);

        // now make the shipping
        CrystalShipping shipping = makeUniqueShipping(crystal, "cs", date);
        validate(shipping);
        String response = this.client.crystalShipping(shipping);
        assertEquals("OK", response);
    }

    //TODO YSBL do this, but e-HTPX does not support it @Test 
    public void testNoDeliveryDate() throws BadDataExceptionException, IOException, LoginExceptionException,
        NotFoundException {

        Crystal crystal = makeUniqueCrystal("cs");
        List<Crystal> crystals = Collections.singletonList(crystal);
        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
        this.client.crystalDetails(details);

        // now make the shipping
        CrystalShipping shipping = makeUniqueShipping(crystal, "cs", null);
        validate(shipping);
        String response = this.client.crystalShipping(shipping);
        assertEquals("OK", response);
    }

    /**
     * @param crystal
     * @param deliveryDate
     * @return
     */
    private CrystalShipping makeUniqueShipping(Crystal crystal, String suffix, Calendar deliveryDate) {
        DeliveryAgent agent = makeDeliveryAgent("Jim", Calendar.getInstance(), deliveryDate);
        return makeShipping(
            NEW_SHIPPING_NAME + suffix,
            getDewars(Collections.singletonList(crystal),
                new Holder[] { makeLoop(crystal, "1", LOOP_BARCODE) }, suffix), PROJECT_UUID, agent);
    }

    private Dewar[] getDewars(List<Crystal> crystals, Holder[] holders, String suffix) {
        Dewar ret = makeDewar(DEWAR_BARCODE + suffix, holders, makePuck(PUCK_BARCODE, holders));

        return new Dewar[] { ret };
    }

    @Test
    public void testGetBadDeliveryStatus() throws IOException, LoginExceptionException, NotFoundException {
        //ShipmentId shipmentId = new ShipmentId();
        //shipmentId.setShipmentId("nonesuch");
        String projectUUID = "nonesuch";
        String shipmentName = "nonesuch";

        ShipmentInfo info = new ShipmentInfo();
        info.setProjectUUID(projectUUID);
        info.setShipmentName(shipmentName);

        try {
            this.client.getDeliveryStatus(info);
            fail("Bad shipment id accepted");
        } catch (BadDataExceptionException e) {
            // that's OK

        } catch (AxisFault e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no shipping record found"));
        }
    }

    @Test
    public void testGetDeliveryStatus() throws IOException, LoginExceptionException, NotFoundException,
        BadDataExceptionException {

        String response = this.client.getDeliveryStatus(getActualShipment());
        System.out.println("status= " + response);
        assertEquals(response, "sent to DLS", response);
    }

    private ShipmentInfo getActualShipment() {
        ShipmentInfo info = new ShipmentInfo();
        info.setProjectUUID(PROJECT_UUID);
        info.setShipmentName(ACTUAL_SHIPPING_NAME);
        return info;
    }

    private ShipmentInfo myShipment() {
        ShipmentInfo info;
        String projectUUID = PROJECT_UUID;
        // the shipping sent has the following name below not ACTUAL_SHIPPING_NAME
        String shipmentName = "shipping1310982857926cs";//ACTUAL_SHIPPING_NAME;

        info = new ShipmentInfo();
        info.setProjectUUID(projectUUID);
        info.setShipmentName(shipmentName);
        return info;
    }

    //    /**
    //     * @param suffix
    //     * @return
    //     * @throws MalformedURIException
    //     * @throws BadDataExceptionException
    //     * @throws AxisFault
    //     * @throws NotFoundException
    //     */
    //    private ShipmentId sendUniqueShipment(String suffix) throws MalformedURIException,
    //        BadDataExceptionException, AxisFault, NotFoundException {
    //        ShipmentId shipmentId;
    //        Crystal crystal = makeUniqueCrystal(suffix);
    //        List<Crystal> crystals = Collections.singletonList(crystal);
    //        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
    //        this.client.crystalDetails(details);
    //
    //        // now make the shipping
    //        CrystalShipping shipping = makeUniqueShipping(crystal, suffix);
    //
    //        String response = this.client.crystalShipping(shipping);
    //        assertEquals("OK", response);
    //        shipmentId = new ShipmentId();
    //        shipmentId.setShipmentId(shipping.getName());
    //        return shipmentId;
    //    }

    @Test
    public void testGetBadResults() throws IOException, LoginExceptionException, NotFoundException {

        try {

            ShipmentInfo info = new ShipmentInfo();
            info.setProjectUUID(PROJECT_UUID);
            info.setShipmentName("nonesuch");
            BeamlineExportedInformation results = this.client.getResults(info);
            fail("Bad shipment id accepted");
        } catch (BadDataExceptionException e) {
            // that's OK
        } catch (AxisFault e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no"));
        }
    }

    // TODO we don't have an uncollected shipment for this test any more @Test
    public void testGetResultsNotYet() throws IOException, LoginExceptionException, NotFoundException,
        BadDataExceptionException {

        try {
            BeamlineExportedInformation results = this.client.getResults(myShipment());
            fail("results should not yet be available");
        } catch (AxisFault e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no blSample found"));
        }
    }

    @Test
    public void testGetResults() throws AxisFault, BadDataExceptionException, NotFoundException {
        //this.client.getResults(getActualShipment());
        // get the result object
        // wrong: BeamlineExportedInformation result = this.client.getResults(myShipment());
        BeamlineExportedInformation result = this.client.getResults(getActualShipment());
        DataCollectionInformation[] dcis = result.getDataCollectionInformation();
        assertEquals(1, dcis.length);
        DataCollectionInformation dci = dcis[0];
        assertNotNull(dci);

        // test the data collection information
        BLSample sample = dci.getBlSample();
        assertNotNull(sample);
        String status = sample.getBlSampleStatus();
        assertNull(status);
        String barcode = sample.getCode();
        assertTrue(barcode.endsWith("loop"));
        Crystal crystal = dci.getCrystal();
        assertNotNull(crystal);
        String crystalUUID = crystal.getCrystalUUID();
        assertTrue(crystalUUID.startsWith(CRYSTAL_FORM_PREFIX));
        DataCollection dc = dci.getDataCollection();
        assertNotNull(dc);
        //TODO test dc attributes
        Image[] images = dci.getImage();
        assertEquals(1, images.length);
        Image image = images[0];
        assertNotNull(image);
        //TODO test image attributes
    }

    @Test
    public void testGetYorkResults() throws AxisFault, BadDataExceptionException, NotFoundException {
        //this.client.getResults(getActualShipment());
        // get the result object
        BeamlineExportedInformation result =
            this.client.getResults(makeShipmentInfo("mx1221-40", "diamond_2011-08-10_i02"));
        DataCollectionInformation[] dcis = result.getDataCollectionInformation();
        assertEquals(115, dcis.length);
        DataCollectionInformation dci = dcis[0];
        assertNotNull(dci);

        // test the data collection information
        BLSample sample = dci.getBlSample();
        assertNotNull(sample);
        String status = sample.getBlSampleStatus();
        assertNull(status);
        String barcode = sample.getCode();
        System.out.println("Loop barcode: " + barcode);
        Crystal crystal = dci.getCrystal();
        assertNotNull(crystal);
        String crystalUUID = crystal.getCrystalUUID();
        System.out.println("CrystalUUID: " + crystalUUID);
        DataCollection dc = dci.getDataCollection();
        assertNotNull(dc);
        AnomalousScattering scattering = dc.getAnomalousScattering();
        if (null != scattering) {
            System.out.println("Scatterer: " + scattering.getElement());
        }

        Image[] images = dci.getImage();
        assertTrue(1 <= images.length);
        Image image = images[0];
        assertNotNull(image);
        System.out.println(image.getFileLocation() + "/" + image.getFileName());
    }

    @Test
    public void testGetBadReturnShipment() throws IOException, LoginExceptionException, NotFoundException {
        String projectUUID = PROJECT_UUID;
        String shipmentName = "nonesuch";

        ShipmentInfo info = new ShipmentInfo();
        info.setProjectUUID(projectUUID);
        info.setShipmentName(shipmentName);
        try {
            String response = this.client.getReturnShipment(info);
            fail("Bad shipment id accepted: " + response);
        } catch (BadDataExceptionException e) {
            // that's OK
        } catch (AxisFault e) {
            assertTrue(e.getMessage(), e.getMessage().contains("no shipping record found"));
        }
    }

    @Test
    public void testGetReturnShipmentNotYet() throws IOException, LoginExceptionException, NotFoundException,
        BadDataExceptionException {
        //ShipmentId shipmentId = sendUniqueShipment("ret");
        String projectUUID = PROJECT_UUID;
        String shipmentName = ACTUAL_SHIPPING_NAME;//SHIPPING_NAME;

        ShipmentInfo info = new ShipmentInfo();
        info.setProjectUUID(projectUUID);
        info.setShipmentName(shipmentName);

        String response = this.client.getReturnShipment(info);
        assertEquals(response, "sent to DLS", response);
    }

    //      @Test
    //      public void mytestDiffractioPlan() throws AxisFault, BadDataExceptionException, NotFoundException{
    //    	
    //      DiffractionPlan plan = new DiffractionPlan();
    //      plan.setProjectUUID(PROJECT_UUID);
    //      plan.setDiffractionPlanUUID(DEWAR_BARCODE);
    //      //DiffractionPlanSequence_type0 
    //      //plan.setDiffractionPlanSequence_type0(arg0);
    //      plan.setCrystalUUID(CRYSTAL_UUID);
    //      //plan.setCrystalIdentifier(arg0);
    //      
    //      String response = this.client.diffractionPlan(plan);
    //    	
    //    }

}
