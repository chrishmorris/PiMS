package org.pimslims.ispyb;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import org.junit.Test;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;

import uk.ac.diamond.ispyb.client.BadDataExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.DiffractionPlan;
import uk.ac.diamond.ispyb.client.LoginExceptionException;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Crystal;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalDetails;
import uk.ac.diamond.ispyb.client.IspybServiceStub.CrystalShipping;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Dewar;
import uk.ac.diamond.ispyb.client.IspybServiceStub.Protein;


public class PimsBeanFactoryTest {
	
	private static final String UNIQUE = "pbf"+System.currentTimeMillis();
	private static final Calendar NOW = Calendar.getInstance();
	private final AbstractModel model;
	
	

	/**
	 * 
	 */
	public PimsBeanFactoryTest() {
		super();
		this.model = ModelImpl.getModel();
	}

	@Test 
	public void testConstructor() {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
		assertNotNull(factory);
	}
	
	public void testMakeProtein() throws ConstraintException {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
		WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
			ResearchObjective ro = new ResearchObjective(version, UNIQUE, "test");
			Protein protein = factory.makeProtein(ro);
			assertNotNull("Ispyb requires acronym", protein.getAcronym());
    		factory.validate(protein);
        } finally {
            version.abort(); // not testing persistence
        }
	}

	@Test
	public void testCrystalDetails() throws ConstraintException {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

    		ExperimentGroup group = createExperimentGroup(version);
            
			CrystalDetails details = factory.makeCrystalDetails(group);
    		factory.validate(details);
        } finally {
            version.abort(); // not testing persistence
        }
	}

	@Test
	public void testDewars() throws ConstraintException {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

    		ExperimentGroup group = createExperimentGroup(version);
            
			Dewar[] dewars = factory.makeDewars(group );
    		assertNotNull(dewars);
    		assertTrue(1==dewars.length);
    		factory.validate(dewars[0]);
        } finally {
            version.abort(); // not testing persistence
        }
	}

	/**
	 * @param version
	 * @return
	 * @throws ConstraintException
	 */
	private ExperimentGroup createExperimentGroup(WritableVersion version)
			throws ConstraintException {
		ExperimentGroup group = new ExperimentGroup(version, UNIQUE, "test");
		Experiment experiment = createExperiment(version);
		experiment.setExperimentGroup(group);
		return group;
	}

	private Experiment createExperiment(WritableVersion version)
			throws ConstraintException {
		Experiment experiment;
		Sample crystal = new Sample(version, UNIQUE);
		Holder loop = new Holder(version, "loop"+UNIQUE);
		AbstractHolder puck = new Holder(version, "puck"+UNIQUE);
		AbstractHolder dewar = new Holder(version, "dewar"+UNIQUE);;
		puck.setParentHolder(dewar );
		loop.setParentHolder(puck );
		crystal.setHolder(loop );
		ExperimentType type = new ExperimentType(version, UNIQUE);
		experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
		
		ResearchObjective ro = new ResearchObjective(version, UNIQUE, "test");
		experiment.setResearchObjective(ro );
		//Parameter parm = new Parameter(wv, experiment);
		//parm.setValue(VALUE);
		new InputSample(version, experiment).setSample(crystal);
		return experiment;
	}

    @Test
	public void testShipping() throws ConstraintException {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
        	ExperimentGroup group = createExperimentGroup(version);
    		CrystalShipping shipping = factory.makeShipping(group );
    		factory.validate(shipping);
        } finally {
            version.abort(); // not testing persistence
        }
	}
    
    @Test
	public void testDiffractionPlan() throws ConstraintException {
		PimsBeanFactory factory = new PimsBeanFactory("beamtimeAllocation", "uuid:");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {

    		Experiment experiment = createExperiment(version);
    		Parameter parameter = new Parameter(version, experiment);
    		parameter.setName("Anomalous Scatterer");
    		parameter.setValue("Se");
            
			DiffractionPlan plan = factory.makeDiffractionPlan(experiment);
    		factory.validate(plan);
    		assertEquals(parameter.getValue(), plan.getDiffractionPlanSequence_type0().getCollectPlan().getAnomalousScattererElement());
        } finally {
            version.abort(); // not testing persistence
        }
	}
    

    // Dont run this automatically, it makes records in the live Ipsyb 
    // @Test
    public void testWS() throws BadDataExceptionException, IOException, LoginExceptionException,
        NotFoundException, ConstraintException {
    	PimsBeanFactory factory = new PimsBeanFactory(AbstractTest.PROJECT_UUID, "uuid:");
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
		try {
    		ExperimentGroup group = createExperimentGroup(version);

			Client client = new Client();
            Properties properties = new Properties();
		    InputStream inStream = EndToEndTest.class.getResourceAsStream("/Properties");
		    properties.load(inStream);
		    boolean loggedIn =
		        client.authenticate(properties.getProperty("fedid"), properties.getProperty("password"));
			CrystalDetails details = factory.makeCrystalDetails(group);
	        client.crystalDetails(details);
    		CrystalShipping shipping = factory.makeShipping(group );
	        String response = client.crystalShipping(shipping);
	        assertEquals("OK", response);
        } finally {
            version.abort(); // not testing persistence
        }
    }
}
