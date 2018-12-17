/**
 * pims-web org.pimslims.presentation StockUtilityTest.java
 * 
 * @author susy
 * @date 2 Dec 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.presentation.servlet.utils.ValueFormatter;
import org.pimslims.test.POJOFactory;

/**
 * StockUtilityTest
 * 
 */
public class StockUtilityTest extends TestCase {

    private final AbstractModel model;

    /**
     * Constructor for PlasmidStockBeanTest
     * 
     * @param name
     */
    public StockUtilityTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    private static final String ANTIBIOTIC = "Tet";

    private static final String ANTIBIOTIC2 = "Kanamycin";

    private static final String REFSAMPLENAME = "pRef Vector";

    private static final String DETAILS = "The experiment details";

    private static final Float VOLUME = 25.0f;

    private static final String HOLDERNAME = "Holder name";

    private static final String NAME = "pLASMID";

    private static final String NAME2 = "CELL";

    private static final String CONCENTRATION = "25";

    private static final String STRAIN = "oocytes";

    private static final String UNIQUE = "test" + System.currentTimeMillis();

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#getResistances(org.pimslims.model.sample.RefSample)}.
     */
    public final void testGetResistances() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            //Vector InputSample
            final SampleCategory sampleCat;
            if (null != version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector")) {
                sampleCat = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
            } else {
                sampleCat = POJOFactory.createSampleCategory(version);
                sampleCat.setName("Vector");
            }
            final Sample sample = POJOFactory.createSample(version);
            sample.addSampleCategory(sampleCat);

            //Sample component is molecule.Construct with MoleFeature for antibiotic resistance
            final Construct construct = POJOFactory.create(version, Construct.class);
            final MoleculeFeature abRes = POJOFactory.create(version, MoleculeFeature.class);
            final MoleculeFeature abRes2 = POJOFactory.create(version, MoleculeFeature.class);
            abRes.setName(StockUtilityTest.ANTIBIOTIC);
            abRes2.setName(StockUtilityTest.ANTIBIOTIC2);
            abRes.setFeatureType("resistance");
            abRes2.setFeatureType("resistance");
            construct.addMoleculeFeature(abRes);
            construct.addMoleculeFeature(abRes2);
            final SampleComponent sampleComp = POJOFactory.createSampleComponent(version, construct, sample);
            final RefSample refVector = POJOFactory.createRefSample(version);
            refVector.setName(StockUtilityTest.REFSAMPLENAME);
            refVector.addSampleComponent(sampleComp);
            sample.setRefSample(refVector);
            final String res1 = StockUtility.getResistances(refVector);
            Assert.assertTrue(res1.contains("Tet"));
            Assert.assertTrue(res1.contains("Kan"));

            //Now test setVector
            sample.setRefSample(refVector);
            final Experiment experiment = POJOFactory.createExperiment(version);
            final AbstractStockBean stockBean = new AbstractStockBean(experiment);
            StockUtility.setVectorDetails(experiment, stockBean);
            Assert.assertNull(stockBean.getVector());
            Assert.assertNull(stockBean.getPimsVectorHook());

            final InputSample inputSample = POJOFactory.createInputSample(version, experiment);
            inputSample.setSample(sample);
            final AbstractStockBean stockBean2 = new CellStockBean(experiment);
            StockUtility.setVectorDetails(experiment, stockBean2);
            Assert.assertEquals("pRef", stockBean2.getVector());
            Assert.assertTrue(stockBean2.getPimsVectorHook().startsWith(
                "org.pimslims.model.sample.RefSample:"));

        } finally {
            version.abort();
        }

    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#makeInitials(org.pimslims.model.experiment.Experiment)}.
     */
    public final void xtestmakeInitials() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            //System.out.println("the owner is: " + StockUtility.makeInitials(experiment));
            //Assert.assertTrue("reference".equals(StockUtility.makeInitials(experiment)));

            final Person pers = POJOFactory.createPerson(version);
            pers.setGivenName("Fred");
            pers.setFamilyName("Flintstone");
            final List<String> initList = new java.util.ArrayList();
            initList.add("A");
            initList.add("b");
            initList.add("C");
            pers.setMiddleInitials(initList);
            final User user = POJOFactory.createUser(version);
            pers.addUser(user);
            //experiment.setCreator(user);
            //Assert.assertTrue("FABCF".equals(StockUtility.makeInitials(experiment)));
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#makeInitials(org.pimslims.model.experiment.Experiment)}.
     */
    public final void xtestSurnameInitials() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            //System.out.println("the owner is: " + StockUtility.makeInitials(experiment));

            final Person persOnlySurname = POJOFactory.createPerson(version);
            persOnlySurname.setFamilyName("Test");
            persOnlySurname.setGivenName("");
            final User user = POJOFactory.createUser(version);
            persOnlySurname.addUser(user);
            //experiment.setCreator(user);
            //System.out.println("Initials are: " + StockUtility.makeInitials(experiment));
            // Assert.assertTrue("T".equals(StockUtility.makeInitials(experiment)));

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#experimentDetails(org.pimslims.model.experiment.Experiment, org.pimslims.presentation.AbstractStockBean)}
     * .
     */
    public final void testExperimentDetails() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            final Calendar date = Calendar.getInstance();
            date.setTimeInMillis(0L);
            experiment.setStartDate(date);
            final AbstractStockBean stockBean = new AbstractStockBean(experiment);
            AbstractStockBean.experimentDetails(experiment, stockBean);
            Assert.assertTrue(stockBean.getPimsExperimentHook().startsWith(
                "org.pimslims.model.experiment.Experiment:"));
            Assert.assertEquals(date, stockBean.getExperimentDate());
            final String theDate = ValueFormatter.formatDate(stockBean.getExperimentDate());
            Assert.assertEquals("0", theDate);
            Assert.assertEquals(theDate, stockBean.getDateAsString());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#setVector(org.pimslims.model.experiment.Experiment, org.pimslims.presentation.AbstractStockBean)}
     * .
     */
    public final void testStockDescription() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            final AbstractStockBean stockBean = new AbstractStockBean(experiment);
            //Description not set
            StockUtility.stockDescription(experiment, stockBean);
            Assert.assertEquals("", stockBean.getDescription());

            //Use Experiment details
            final Experiment experiment2 = POJOFactory.createExperiment(version);
            experiment2.setDetails(StockUtilityTest.DETAILS);
            final AbstractStockBean stockBean2 = new AbstractStockBean(experiment2);
            StockUtility.stockDescription(experiment2, stockBean2);
            Assert.assertEquals(StockUtilityTest.DETAILS, stockBean2.getDescription());

            //Test use of ResearchObjective details for bean.details
            final ResearchObjective resOb = POJOFactory.create(version, ResearchObjective.class);
            resOb.setFunctionDescription("N-term His tag");
            experiment2.setProject(resOb);
            final AbstractStockBean stockBean3 = new AbstractStockBean(experiment2);
            StockUtility.stockDescription(experiment2, stockBean3);
            Assert.assertEquals(experiment2.getResearchObjective().getFunctionDescription(),
                stockBean3.getDescription());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.StockUtility#processOutputSample(org.pimslims.model.experiment.Experiment, org.pimslims.presentation.AbstractStockBean)}
     * .
     */
    public final void testProcessOutputSample() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            final AbstractStockBean stockBean = new AbstractStockBean(experiment);
            final Experiment experiment2 = POJOFactory.createExperiment(version);
            final AbstractStockBean stockBean2 = new AbstractStockBean(experiment2);
            //Nothing set
            StockUtility.processOutputSample(experiment, stockBean);
            Assert.assertNull(stockBean.getPimsSampleHook());
            Assert.assertNull(stockBean.getVolume());
            Assert.assertNull(stockBean.getPimsHolderHook());
            Assert.assertNull(stockBean.getRack());
            Assert.assertNull(stockBean.getRackPosition());
            Assert.assertNull(stockBean.getStockName());

            //Set testable values
            final Holder holder = POJOFactory.createHolder(version);
            holder.setName(StockUtilityTest.HOLDERNAME);

            final OutputSample plasmidOS = POJOFactory.createOutputSample(version, experiment);
            final Sample plasmidS = POJOFactory.createSample(version);
            plasmidS.setName(StockUtilityTest.NAME);
            final SampleCategory plasmidCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Plasmid");
            if (null != plasmidCat) {
                plasmidS.addSampleCategory(plasmidCat);
            }
            plasmidS.setCurrentAmount(StockUtilityTest.VOLUME);
            plasmidOS.setSample(plasmidS);

            final OutputSample cellOS = POJOFactory.createOutputSample(version, experiment2);
            final Sample cellS = POJOFactory.createSample(version);
            cellS.setName(StockUtilityTest.NAME2);
            final SampleCategory cellCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Cell");
            if (null != cellCat) {
                cellS.addSampleCategory(cellCat);
            }
            cellS.setCurrentAmount(StockUtilityTest.VOLUME / 1000000);
            cellOS.setSample(cellS);

            plasmidS.setHolder(holder);
            plasmidS.setColPosition(2);
            plasmidS.setRowPosition(4);

            StockUtility.processOutputSample(experiment, stockBean);
            Assert.assertNotNull(stockBean.getPimsSampleHook());
            Assert.assertEquals(StockUtilityTest.VOLUME, stockBean.getVolume() / 1000000);
            Assert.assertNotNull(stockBean.getPimsHolderHook());
            Assert.assertEquals(StockUtilityTest.HOLDERNAME, stockBean.getRack());
            Assert.assertEquals("D02", stockBean.getRackPosition());
            //TODO Assert.assertEquals(StockUtilityTest.NAME, stockBean.getStockName());

            StockUtility.processOutputSample(experiment2, stockBean2);
            //TODO Assert.assertNotNull(stockBean2.getPimsSampleHook());
            //TODO Assert.assertEquals(StockUtilityTest.VOLUME, stockBean2.getVolume());
            //TODO Assert.assertEquals(StockUtilityTest.NAME2, stockBean2.getStockName());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlasmidStockBean#getPlasmidStockBean(org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testPlasmidStockBean() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            version.flush();
            final PlasmidStockBean psBean = PlasmidStockBean.getPlasmidStockBean(experiment);
            Assert.assertTrue(psBean.getConcentration() == null);
            Assert.assertNull(psBean.getStrainPrepdFrom());

            final Parameter param = POJOFactory.createParameter(version);
            param.setName("Concentration");
            param.setValue("");
            final List<Parameter> params = new java.util.ArrayList<Parameter>();
            params.add(param);
            experiment.setParameters(params);
            final PlasmidStockBean psBean1 = PlasmidStockBean.getPlasmidStockBean(experiment);
            Assert.assertEquals(0.0f, psBean1.getConcentration());

            final Parameter param1 = POJOFactory.createParameter(version);
            param1.setName("Concentration");
            //TODO Set to ""
            param1.setValue(StockUtilityTest.CONCENTRATION);
            final Parameter param2 = POJOFactory.createParameter(version);
            param2.setName("Strain in the test");
            param2.setValue(StockUtilityTest.STRAIN);
            final List<Parameter> parameters = new java.util.ArrayList<Parameter>();
            parameters.add(param1);
            parameters.add(param2);
            experiment.setParameters(parameters);
            //final PlasmidStockBean psBean2 = new PlasmidStockBean(experiment);
            final PlasmidStockBean psBean2 = PlasmidStockBean.getPlasmidStockBean(experiment);
            Assert.assertEquals(Float.parseFloat(StockUtilityTest.CONCENTRATION), psBean2.getConcentration());
            Assert.assertEquals(StockUtilityTest.STRAIN, psBean2.getStrainPrepdFrom());
        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlasmidStockBean#getCellStockBean(org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testCellStockBean() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            //Cell stock bean nothing set
            final CellStockBean csBean = CellStockBean.getCellStockBean(experiment);
            Assert.assertTrue(csBean.getStrain() == null);
            Assert.assertTrue(csBean.getHostAbRes() == null);

            //Cell stock bean2 -with Parameters
            final Parameter param1 = POJOFactory.createParameter(version);
            param1.setName("Strain in the test");
            param1.setValue(StockUtilityTest.STRAIN);

            final Parameter param2 = POJOFactory.createParameter(version);
            param2.setName("Antibiotic from Host");
            param2.setValue("Tetracycline");

            final List<Parameter> parameters = new java.util.ArrayList<Parameter>();
            parameters.add(param1);
            parameters.add(param2);
            experiment.setParameters(parameters);
            final CellStockBean csBean2 = CellStockBean.getCellStockBean(experiment);
            Assert.assertEquals(StockUtilityTest.STRAIN, csBean2.getStrain());
            Assert.assertEquals(StockUtilityTest.ANTIBIOTIC, csBean2.getHostAbRes());

            //Cells stock bean3 Experiment with parameters and Vector
            final SampleCategory sampleCat;
            if (null != version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector")) {
                sampleCat = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
            } else {
                sampleCat = POJOFactory.createSampleCategory(version);
                sampleCat.setName("Vector");
            }
            final Sample sample = POJOFactory.createSample(version);
            sample.addSampleCategory(sampleCat);

            //Sample component is molecule.Construct with MoleFeature for antibiotic resistance
            final Construct construct = POJOFactory.create(version, Construct.class);
            final MoleculeFeature abRes = POJOFactory.create(version, MoleculeFeature.class);
            abRes.setName(StockUtilityTest.ANTIBIOTIC2);
            abRes.setFeatureType("resistance");
            construct.addMoleculeFeature(abRes);
            final SampleComponent sampleComp = POJOFactory.createSampleComponent(version, construct, sample);
            final RefSample refVector = POJOFactory.createRefSample(version);
            refVector.setName(StockUtilityTest.REFSAMPLENAME);
            refVector.addSampleComponent(sampleComp);
            sample.setRefSample(refVector);

            final InputSample inputSample = POJOFactory.createInputSample(version, experiment);
            inputSample.setSample(sample);
            final CellStockBean csBean3 = CellStockBean.getCellStockBean(experiment);
            StockUtility.setVectorDetails(experiment, csBean3);
            Assert.assertEquals("Kan, Tet", csBean3.getHostAbRes());

            //Cell Stock bean 4 experiment with Vector but no parameter value
            final Experiment experiment2 = POJOFactory.createExperiment(version);
            final InputSample inputSample2 = POJOFactory.createInputSample(version, experiment2);
            inputSample2.setSample(sample);
            final List<Parameter> parameters2 = new java.util.ArrayList<Parameter>();
            param2.setValue("");
            parameters2.add(param2);
            experiment2.setParameters(parameters2);
            final CellStockBean csBean4 = CellStockBean.getCellStockBean(experiment2);
            StockUtility.setVectorDetails(experiment2, csBean4);
            Assert.assertEquals("Kan", csBean4.getHostAbRes());

            //Cell Stock bean 5 experiment with Vector and value for Host Ab res not in Map
            final Experiment experiment3 = POJOFactory.createExperiment(version);
            final InputSample inputSample3 = POJOFactory.createInputSample(version, experiment3);
            inputSample3.setSample(sample);
            final List<Parameter> parameters3 = new java.util.ArrayList<Parameter>();
            final String notInMap = "Not in Map";
            param2.setValue(notInMap);
            parameters3.add(param2);
            experiment3.setParameters(parameters3);
            final CellStockBean csBean5 = CellStockBean.getCellStockBean(experiment3);
            StockUtility.setVectorDetails(experiment2, csBean4);
            Assert.assertEquals("Kan, " + notInMap, csBean5.getHostAbRes());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlasmidStockBean#getCellStockBean(org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testPlasmidInputSample() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Experiment experiment = POJOFactory.createExperiment(version);
            //Cell stock bean nothing set
            final CellStockBean csBean = CellStockBean.getCellStockBean(experiment);
            Assert.assertNull(csBean.getPlasmid());
            Assert.assertNull(csBean.getPimsPlasmidHook());

            //Cell stock bean with Plasmid Sample
            final SampleCategory sampleCat;
            if (null != version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Plasmid")) {
                sampleCat = version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Plasmid");
            } else {
                sampleCat = POJOFactory.createSampleCategory(version);
                sampleCat.setName("Plasmid");
            }
            final Sample sample = POJOFactory.createSample(version);
            sample.addSampleCategory(sampleCat);
            sample.setName(StockUtilityTest.NAME);
            final InputSample inputSample = POJOFactory.createInputSample(version, experiment);
            inputSample.setSample(sample);

            final CellStockBean csBean2 = CellStockBean.getCellStockBean(experiment);
            Assert.assertTrue(csBean2.getPimsPlasmidHook().startsWith("org.pimslims.model.sample.Sample:"));
            Assert.assertEquals(StockUtilityTest.NAME, csBean2.getPlasmid());

        } finally {
            version.abort();
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.PlasmidStockBean#getCellStockBean(org.pimslims.model.experiment.Experiment)}
     * .
     */
    public final void testMakeExpMap() throws ConstraintException, AccessException, IllegalStateException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final String expTypeName = StockUtilityTest.UNIQUE;
            final ExperimentType expType = POJOFactory.createExperimentType(version);
            expType.setName(expTypeName);
            Map<String, Object> expAttrMap = new java.util.HashMap<String, Object>();

            expAttrMap = SyntheticGeneManager.makeExpMap(version, expTypeName);
            Assert.assertEquals(1, expAttrMap.size());
        } finally {
            version.abort();
        }
    }
}
