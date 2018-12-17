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
import uk.ac.diamond.ispyb.client.IspybServiceStub.AnomalousScattering;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.ScreenPlan;
import uk.ac.diamond.ispyb.client.LoginExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.AnomalousScatterer;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BLSample;
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
import uk.ac.diamond.ispyb.client.IspybServiceStub.ShipmentInfo;
import uk.ac.diamond.ispyb.client.IspybServiceStub.SweepInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Temperature_type3;

public class InPlateTest extends AbstractTest {
	
	

    /**
	 * @param certificatePassword
	 * @param certificateFilePath
	 */
	public InPlateTest() {
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
    private static final String PLATE_BARCODE = UNIQUE + "c";

    /**
     * DEWAR_BARCODE String
     */
    private static final String DEWAR_BARCODE = UNIQUE + "d";


    

    @After
    public void tearDown() {
        // no action required
    }

    
    

    /**
     * EndToEndTest.makeDiffractionPlan TODO move this to IspbyBeanFactory when it is tested
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
        DiffractionPlan ret= makeDiffractionPlan(projectUuid, planUuid,
				crystalUuid, collectionPlan, sweep, screen);
        return ret;
    }

	/**
	 * @return
	 */
	private CollectPlan makeCollectionPlan() {
		CollectPlan collection;
		collection = new CollectPlan();
        ExperimentType_type3 type =  ExperimentType_type3.value6;  // this is probably "OSC"
		collection.setExperimentType(type );
		
		collection.setAnneal(false);
		
		Temperature_type3 temp = Temperature_type3.value2; // this is probably "100K"
		collection.setTemperature(temp);
		ProcessingLevel_type1 level = ProcessingLevel_type1.value1; // this is probably "Collect"
		collection.setProcessingLevel(level );
		return collection;
	}

    

    @Test
    public void testWellShipping() throws BadDataExceptionException, IOException, LoginExceptionException,
        NotFoundException {
        

        Crystal crystal = makeUniqueCrystal("cs"); // crystal form
        List<Crystal> crystals = Collections.singletonList(crystal);
        CrystalDetails details = makeCrystalDetails(crystals, PROJECT_UUID);
        this.client.crystalDetails(details);

        // now make the shipping
        CrystalShipping shipping = makePlateShipping(crystal, "cs", Calendar.getInstance());
        validate(shipping);
        Holder holder = shipping.getDewar()[0].getContainer()[0].getHolder()[0];
        assertEquals(details.getCrystalDetailsSequence_type0()[0].getCrystal().getProtein().getAcronym(),
            holder.getCrystalIdentifier().getCrystalIdentifierSequence_type0().getProteinAcronym());

        String response = this.client.crystalShipping(shipping);
        assertEquals("OK", response);
    }

    


    /**
     * @param crystal
     * @param deliveryDate 
     * @return
     */
    private CrystalShipping makePlateShipping(Crystal crystal, String suffix, Calendar deliveryDate) {
        DeliveryAgent agent = makeDeliveryAgent("Jim", Calendar.getInstance(), deliveryDate);
		return makeShipping(NEW_SHIPPING_NAME + suffix, getRacks(Collections.singletonList(crystal),
            new Holder[] { makeLoop(crystal, "1", PLATE_BARCODE+":A01") }, suffix), PROJECT_UUID, agent);
    }

    private Dewar[] getRacks(List<Crystal> crystals, Holder[] holders, String suffix) {
        Dewar ret = makeDewar(DEWAR_BARCODE + suffix, holders, makePuck(PLATE_BARCODE, holders));

        return new Dewar[] { ret };
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


}
