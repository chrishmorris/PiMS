/**
 * ispybToPims org.pimslims.ispyb ExperimentFactoryTest.java
 * 
 * @author cm65
 * @date 1 Aug 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.ispyb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.FlushMode;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersionImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.Sample;
import org.pimslims.util.File;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BLSample;
import uk.ac.diamond.ispyb.client.IspybServiceStub.BeamlineExportedInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollection;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DataCollectionInformation;
import uk.ac.diamond.ispyb.client.IspybServiceStub.EnergyScan;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Image;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Session;

/**
 * ExperimentFactoryTest
 * 
 */
public class DiffractionExperimentFactoryTest extends AbstractTest {

    private static final String UNIQUE = "def" + System.currentTimeMillis();

    private static final Calendar START = Calendar.getInstance();
    static {
        START.setTimeInMillis(START.getTimeInMillis() - 5 * 60 * 1000);
    }

    private static final Calendar END = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * 
     */
    public DiffractionExperimentFactoryTest() {
		super();
        this.model = ModelImpl.getModel();
    }

    @Test
    public void testDates() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            assertNotNull(experiment);
            assertEquals(dci.getDataCollection().getStartTime(), experiment.getStartDate());
            assertEquals(dci.getDataCollection().getEndTime(), experiment.getEndDate());
            assertNotNull(experiment.getProtocol());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testCrystal() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        Crystal crystal = new Crystal();
        crystal.setAlpha(90d);
        dci.setCrystal(crystal);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm = experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Crystal Form: Alpha");
            assertNotNull(parm);
            assertEquals("90.0", parm.getValue());
            assertNotNull(parm.getParameterDefinition());

            // this value seems to relate to a specific sample
            // That's a misunderstanding - the "Crystal" bean describes a crystal form
            // note that this is an image of the crystal from the home lab, not a diffraction image
            assertNull(experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Image Url"));
        } finally {
            version.abort();
        }
    }

    //TODO test crystallization solution, protein

    @Test
    public void testOneGroup() throws IOException {
        BeamlineExportedInformation result = new BeamlineExportedInformation();
        DataCollection dc = new DataCollection();
        dc.setStartTime(START);
        dc.setEndTime(END);
        Crystal crystal = new Crystal();
        crystal.setCrystalUUID(UNIQUE);
        DataCollectionInformation dci1 = makeDataCollectionInformation("one");
        dci1.setCrystal(crystal);
        result.addDataCollectionInformation(dci1);
        DataCollectionInformation dci2 = makeDataCollectionInformation("two");
        dci2.setCrystal(crystal);
        result.addDataCollectionInformation(dci2);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Collection<ExperimentGroup> groups =
                new DiffractionExperimentFactory(version).getExperimentGroups(result);
            assertEquals(1, groups.size());
            ExperimentGroup group = groups.iterator().next();
            assertEquals(2, group.getExperiments().size());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testTwoGroups() throws IOException {
        BeamlineExportedInformation result = new BeamlineExportedInformation();
        DataCollection dc = new DataCollection();
        dc.setStartTime(START);
        dc.setEndTime(END);
        Crystal crystal1 = new Crystal();
        crystal1.setCrystalUUID(UNIQUE + "one");
        DataCollectionInformation dci1 = makeDataCollectionInformation("one");
        dci1.setCrystal(crystal1);
        result.addDataCollectionInformation(dci1);
        DataCollectionInformation dci2 = makeDataCollectionInformation("two");
        Crystal crystal2 = new Crystal();
        crystal2.setCrystalUUID(UNIQUE + "two");
        dci2.setCrystal(crystal2);
        result.addDataCollectionInformation(dci2);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Collection<ExperimentGroup> groups =
                new DiffractionExperimentFactory(version).getExperimentGroups(result);
            assertEquals(2, groups.size());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testImage() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        Image image = new Image();
        image.setFileLocation("http://www.pims-lims.org/docs/images/logo.gif");
        image.setFileName("logo.gif");
        dci.addImage(image);

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Collection<File> files = experiment.get_Files();
            assertEquals(1, files.size());
            File file = files.iterator().next();
            assertNotNull(file);
            //TODO other tests - what does DLS return for an image?
        } finally {
            version.abort();
        }
    }

    @Test
    public void testSample() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            List<InputSample> iss = experiment.getInputSamples();
            assertEquals(1, iss.size());
            InputSample is = iss.iterator().next();
            assertNotNull(is.getRefInputSample());
			Sample sample = is.getSample();
            assertEquals(dci.getBlSample().getName(), sample.getName());
            Holder loop = sample.getHolder();
            //TODO assertNotNull(loop.getHolderType());
            //TODO check there is a ResearchObjective
            assertEquals(dci.getBlSample().getCode(), loop.getName());
        } finally {
            version.abort();
        }
    }
    
    //TODO test experiment type, anomalous scatterer, temperature, processing level - DLS should report a diffraction plan
     
    //TODO test anneal parameter - element must be added to Collectplan.xsd

    private DataCollectionInformation makeDataCollectionInformation(String suffix) {
        DataCollectionInformation dci;
        dci = new DataCollectionInformation();
        DataCollection dc = new DataCollection();
        dc.setStartTime(START);
        dc.setEndTime(END);
        dci.setDataCollection(dc);
        BLSample blSample = new BLSample();
        blSample.setCode("BC" + UNIQUE + suffix);
        blSample.setName("s" + UNIQUE + suffix);
        dci.setBlSample(blSample);
        return dci;
    }

    @Test
    public void testStringParameter() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        dci.getDataCollection().setExperimentType("test");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Data Collection: Experiment Type");
            assertNotNull(parm);
            assertEquals(dci.getDataCollection().getExperimentType(), parm.getValue());
            assertNotNull(parm.getParameterDefinition());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testIntegerParameter() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        dci.getDataCollection().setStartImageNumber(1);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Data Collection: Start Image Number");
            assertNotNull(parm);
            assertEquals("1", parm.getValue());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testFloatParameter() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        dci.getDataCollection().setResolution(1.0f);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Data Collection: Resolution");
            assertNotNull(parm);
            assertEquals("1.0", parm.getValue());
            assertNotNull(parm.getParameterDefinition());
        } finally {
            version.abort();
        }
    }
    

    @Test
    public void testNan() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        dci.getDataCollection().setBeamSizeHorizontal(Float.NaN);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Data Collection: Beam Size Horizontal");
            assertNotNull(parm);
            assertEquals(parm.getValue(), "", parm.getValue());
            assertNotNull(parm.getParameterDefinition());
        } finally {
            version.abort();
        }
    }

    @Test
    public void testBooleanParameter() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        dci.getDataCollection().setPrintableForReport(true);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Data Collection: Printable For Report");
            assertNotNull(parm);
            assertEquals("true", parm.getValue());
        } finally {
            version.abort();
        }
    }
    

    @Test
    public void testSession() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        Session session = new Session();
        session.setBeamLineName(UNIQUE);
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, session);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Session: Beam Line Name");
            assertNotNull(parm);
            assertEquals(UNIQUE, parm.getValue());
        } finally {
            version.abort();
        }
    }
    
    
    //TODO dci.setXFEFluorescenceSpectrum(arg0)
    
    //TODO dc.setAnomalousScattering(arg0); 
    
    @Test
    public void testEnergyScan() throws ConstraintException, IOException {
        DataCollectionInformation dci = makeDataCollectionInformation("");
        EnergyScan scan = new EnergyScan();
        scan.setPeakEnergy(12655.0f);
		dci.setEnergyScan(scan );
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Experiment experiment = new DiffractionExperimentFactory(version).getExperiment(dci, null);
            Parameter parm =
                experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "Energy Scan: Peak Energy");
            assertNotNull(parm);
            assertEquals("12655.0", parm.getValue());
        } finally {
            version.abort();
        }
    }
    
    // round trip test
    @Test
    public void testYorkSend() throws IOException, AbortedException, ConstraintException, BadDataExceptionException, NotFoundException {
        BeamlineExportedInformation result = this.client.getResults(makeShipmentInfo("mx1221-40", "diamond_2011-08-10_i02"));
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Collection<ExperimentGroup> groups =
                new DiffractionExperimentFactory(version).getExperimentGroups(result);
            ExperimentGroup group = groups.iterator().next();
            System.out.println("Crystal forms: "+groups.size());

    		PimsBeanFactory factory = new PimsBeanFactory(PROJECT_UUID, "uuid:");
    		//TODO now put all samples in pucks in dewars
    		
    		CrystalShipping shipping = factory.makeShipping(group );
    		factory.validate(shipping);
    		// This fails: String response = this.client.crystalShipping(shipping); assertEquals("OK", response);
    		// because the proteinid is not known
        } finally {
            if (!version.isCompleted()) {version.abort();}
        }
    }
    

	@Test
    public void testYork() throws IOException, AbortedException, ConstraintException, BadDataExceptionException, NotFoundException {

    	String projectUUID = "mx1221-40";
		String shipmentName = "diamond_2011-08-10_i02";
		WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        Collection<ExperimentGroup> groups = null;
        try {
			long start = System.currentTimeMillis();
			BeamlineExportedInformation result = this.client.getResults(makeShipmentInfo(projectUUID, shipmentName));        
            groups = new DiffractionExperimentFactory(version).getExperimentGroups(result);
            System.out.println("Crystal forms: "+groups.size());
			System.out.println("Time to get York shipment: "
					+ ((System.currentTimeMillis() - start) / 1000) + "s");
			// dont version.commit();
        } finally {
            if (!version.isCompleted()) {version.abort();}
        }
        assertTrue(groups.size()>1);
    }
    
}
