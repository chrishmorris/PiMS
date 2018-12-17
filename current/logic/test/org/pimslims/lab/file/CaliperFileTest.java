/**
 * pims-web org.pimslims.servlet.plateExperiment SpreadsheetGetterTest.java
 * 
 * @author Marc Savitsky
 * @date 13 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

/**
 * SpreadsheetGetterTest
 * 
 */
public class CaliperFileTest extends TestCase {

    /**
     * BASES_FROM_VECTOR int
     */
    private static final Integer BASES_FROM_VECTOR = 100;

    /**
     * GROUP_NAME String
     */
    private static final String GROUP_NAME = "cf" + System.currentTimeMillis();

    private final AbstractModel model;

    final String inputString =
        "Plate Name,Sample Name,Size [BP],Conc. (ng/ul),% Purity,Expected Fragment [BP],Type,Molarity (nmol/l)"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,40.0000000000001,0,,,?,0"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,100,1,,,L,15.1515"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,300,1,,,L,5.0505"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,500,1,,,L,3.0303"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,700,1,,,L,2.1645"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,1100,1,,,L,1.37740909090909"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,1900,1,,,L,0.797447368421053"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,2900,1,,,L,0.522465517241379"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,4900,1,,,L,0.309214285714286"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder1,7000,2.5,,,UM,0.541125"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,51.0606060606061,0,,,?,0"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,100,1,,,L,15.1515"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,300,1,,,L,5.0505"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,500,1,,,L,3.0303"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,700,1,,,L,2.1645"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,1100,1,,,L,1.37740909090909"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,1900,1,,,L,0.797447368421053"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,2900,1,,,L,0.522465517241379"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,4900,1,,,L,0.309214285714286"
            + "\nCaliper_GX_2010-03-17_10-45-17,Ladder2,7000,2.5,,,UM,0.541125"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,36.211009174312,0.134278227152875,23.189901223455,,,5.61850278436889"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,52.4311926605505,0.130876796095689,22.6024727780023,,,3.78206116515795"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,616.277850589779,0.249355915705409,43.0638620817447,,,0.613054023147976"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,4789.27916120577,0.0645265733622169,11.1437639167981,,,0.0204138105837933"
            + "\nCaliper_GX_2010-03-17_10-45-17,A1,7000,2.5,,,UM,0.541125"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,51.5862708719853,0.130540018805267,4.41812201350909,,,3.83411528202192"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,73.0333951762524,0.0977812731109325,3.30940350088967,,,2.02856919901491"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,889.760818332246,2.65367015322729,89.8133662601589,,,4.51886422712868"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,5145.45454545454,0.0726580282327838,2.45910822544231,,,0.0213951577075252"
            + "\nCaliper_GX_2010-03-17_10-45-17,B1,7000,2.5,,,UM,0.541125"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,52.6981897627964,0.155858069222468,0.565551419832127,,,4.48114735336007"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,72.1972534332084,0.212751246740582,0.771995766824541,,,4.46485200156828"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,1175.46816479401,24.6552628248728,89.464850722859,,,31.7800367444682"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,1417.71535580524,1.67632894222617,6.08277914715135,,,1.79153719850298"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,2359.40558173251,0.237300110088331,0.861074533107519,,,0.152388069513814"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,2619.88643228223,0.200314830250018,0.7268686005575,,,0.115847393731844"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,3161.070720423,0.125054374194169,0.453776177483503,,,0.0599404922629953"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,3992.31108173606,0.29573236724883,1.07310363218449,,,0.112235466391115"
            + "\nCaliper_GX_2010-03-17_10-45-17,C1,7000,2.5,,,UM,0.541125"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,15,5,,,LM,505.05"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,35.6828057107388,0.187233535075719,1.06786962419683,,,7.95024059962301"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,49.9022346368716,0.131487315474971,0.749926075508445,,,3.99226622798772"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,74.4630664183737,0.186904920139945,1.0659953984721,,,3.80307988068781"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,1411.06145251396,16.0987361660562,91.8176935170185,,,17.2862776872991"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,2179.87024689133,0.782994635358464,4.46573946633137,,,0.544231622710211"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,3299.05794720123,0.1460136851995,0.832775918472763,,,0.0670593359288236"
            + "\nCaliper_GX_2010-03-17_10-45-17,D1,7000,2.5,,,UM,0.541125";

    private static final String filePath = "C:\\Temp\\Caliper\\2010-10-08\\";

    private static final String fileName = "Caliper_GX_PCR139_PCR1_2010-03-02_09-52-33_PeakTable.csv";

/*
    private static final String verificationFile =
        "Caliper_GX_PCR139_verification_2010-03-26_10-27-34_PeakTable.csv"; */

    private static final Calendar NOW = Calendar.getInstance();

    private static final String UNIQUE = "cf" + System.currentTimeMillis();

    private static final List<String> POSSIBLE_VALUES = new ArrayList<String>(4);
    static {
        CaliperFileTest.POSSIBLE_VALUES.add("Unscored");
        CaliperFileTest.POSSIBLE_VALUES.add("No");
        CaliperFileTest.POSSIBLE_VALUES.add("Maybe");
        CaliperFileTest.POSSIBLE_VALUES.add("Yes");
    }

    /**
     * Constructor for SpreadsheetGetterTest
     * 
     * @param name
     */
    public CaliperFileTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * OPPFExperimentNameTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * OPPFExperimentNameTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetResultforWell() throws UnsupportedEncodingException, IOException {

        Assert.assertEquals(616,
            CaliperFile.getResultforWell("A1", CaliperFile.parseStringToIS(this.inputString)));
        Assert.assertEquals(1411,
            CaliperFile.getResultforWell("D1", CaliperFile.parseStringToIS(this.inputString)));
        Assert.assertEquals(616,
            CaliperFile.getResultforWell("A01", CaliperFile.parseStringToIS(this.inputString)));
    }

    public void testGetPCRProductSize() throws ConstraintException, AccessException {

        final WritableVersion version = this.model.getTestVersion();
        try {
            //final File file = new File(CaliperFileTest.filePath + CaliperFileTest.fileName);
            final CaliperFile caliperFile = new CaliperPCRFile();

            final Experiment experiment = POJOFactory.createExperiment(version);
            final String sequence = "AAAAAAAAAATTTT";
            final ResearchObjective researchObjective = this.createResearchObjective(version, sequence);
            experiment.setProject(researchObjective);

            final int result = caliperFile.getPCRProductSize(experiment);
            Assert.assertEquals(14, result);

        } finally {
            version.abort();
        }

    }

    private ResearchObjective createResearchObjective(final WritableVersion version, final String product)
        throws ConstraintException, AccessException {
        final ResearchObjective researchObjective = POJOFactory.createExpBlueprint(version);
        final ResearchObjectiveElement element = POJOFactory.createBlueprintComponent(version);
        researchObjective.addResearchObjectiveElement(element);

        final Molecule trialMolComponent =
            POJOFactory.create(version, org.pimslims.model.molecule.Molecule.class);
        element.addTrialMolComponent(trialMolComponent);

        final ComponentCategory category =
            version.findFirst(org.pimslims.model.reference.ComponentCategory.class,
                ComponentCategory.PROP_NAME, CaliperFile.PCR_PRODUCT);

        trialMolComponent.addCategory(category);
        trialMolComponent.setSequence(product);
        return researchObjective;
    }

    public void testGetResult() {

        final String[] values = new String[] { "Unscored", "No", "Maybe", "Yes" };
        String result = CaliperFile.getResult("A1", 616, 614, values);
        Assert.assertEquals("Yes", result);

        result = CaliperFile.getResult("A2", 714, 614, values);
        Assert.assertEquals("Maybe", result);

        result = CaliperFile.getResult("A3", 61, 614, values);
        Assert.assertEquals("No", result);
    }

    public void testIsCaliperFile() {

        Assert.assertTrue(CaliperFile
            .isCaliperFile("Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.csv"));
        Assert.assertFalse(CaliperFile
            .isCaliperFile("Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.xls"));

    }

    public void testIsCaliperPCRFile() {

        final WritableVersion version = this.model.getTestVersion();
        try {
            //SetUp
            final User creator = POJOFactory.createUser(version);

            this.makeGroup(version, creator, CaliperFile.CALIPER_PROTOCOL, "PCR139_PCR1");
            this.makeGroup(version, creator, CaliperVerificationFile.OPPF_VERIFICATION, "PCR139_verification");

            //now the test
            final IFileType type = new CaliperPCRFile();

            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.csv"));
            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.xls"));
            Assert.assertTrue(type.isOfThisType(version,
                "Caliper_GX_PCR139_PCR1_2010-05-21_12-03-04_PeakTable.csv"));
            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_PCR1_2010-05-21_12-03-04_PeakTable.xls"));

        } catch (final Exception e) {
            e.printStackTrace();
            Assert.assertNotNull(e);

        } finally {
            version.abort();
        }
    }

    public void testIsCaliperVerificationFile() {

        final WritableVersion version = this.model.getTestVersion();
        try {
            //SetUp
            final User creator = POJOFactory.createUser(version);

            this.makeGroup(version, creator, CaliperFile.CALIPER_PROTOCOL, "PCR139_PCR1");
            this.makeGroup(version, creator, CaliperVerificationFile.OPPF_VERIFICATION, "PCR139_verification");

            //now the test
            final IFileType type = new CaliperVerificationFile();

            Assert.assertTrue(type.isOfThisType(version,
                "Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.csv"));
            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_verification_2010-05-21_12-03-04_PeakTable.xls"));
            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_PCR1_2010-05-21_12-03-04_PeakTable.xls"));
            Assert.assertFalse(type.isOfThisType(version,
                "Caliper_GX_PCR139_PCR1_2010-05-21_12-03-04_PeakTable.xls"));

        } catch (final Exception e) {
            e.printStackTrace();
            Assert.assertNotNull(e);

        } finally {
            version.abort();
        }
    }

    public void testRefData() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final Protocol protocol =
                version.findFirst(Protocol.class, Protocol.PROP_NAME, CaliperFile.CALIPER_PROTOCOL);
            Assert.assertNotNull(protocol);
            final ParameterDefinition parm =
                protocol.findFirst(Protocol.PROP_PARAMETERDEFINITIONS, ParameterDefinition.PROP_NAME,
                    CaliperVerificationFile.BASES_FROM_VECTOR);
            Assert.assertNotNull(parm);
        } finally {
            version.abort();
        }

    }

    public void TODOtestProcessToPims() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            //SetUp
            final User creator = POJOFactory.createUser(version);
            this.makeGroup(version, creator, CaliperFile.CALIPER_PROTOCOL, UNIQUE);

            //now the test
            final File file = new File(CaliperFileTest.filePath + CaliperFileTest.fileName);
            final CaliperFile caliperResultFile =
                new CaliperPCRFile(file.getName(), CaliperFile.parseStringToIS(this.inputString));

            final ModelObject group = caliperResultFile.process(version);
            Assert.assertNotNull(group);

            final Experiment c1 = getExperimentByPosition((ExperimentGroup) group, "C01");
            final Parameter parm = c1.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, "__SCORE");
            Assert.assertNotNull(parm);
            Assert.assertEquals("No", parm.getValue());

        } finally {
            version.abort();
        }

    }

    // not a good method, if there is more than one plate
    public static Experiment getExperimentByPosition(final ExperimentGroup group, final String position) {
        //TODO convert position A1 =>A01
        final Collection experiments = group.getExperiments();
        assert 0 < experiments.size() : "No experiments for group";
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();
            final String outputPosition = HolderFactory.getPositionInHolder(experiment);
            // System.out.println(experiment.getName()+" "+outputPosition);
            if (HolderFactory.positionEquals(position, outputPosition)) {
                return experiment;
            }
        }
        return null; // not found
    }

    private ExperimentGroup makeGroup(final WritableVersion version, final User creator,
        final String protocolName, final String name) throws AccessException, ConstraintException {

        final ExperimentGroup group =
            POJOFactory.create(version, ExperimentGroup.class, ExperimentGroup.PROP_NAME, name);
        // was group.setCreator(creator);

        final Holder holder = POJOFactory.createHolder(version);
        final HolderType holderType =
            POJOFactory.create(version, org.pimslims.model.reference.HolderType.class);
        holderType.setMaxColumn(8);
        holderType.setMaxRow(12);
        holder.setHolderType(holderType);

        Protocol protocol = version.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        if (null == protocol) {
            protocol = POJOFactory.createProtocol(version);
            protocol.setName(protocolName);
            final RefInputSample refInputSample = POJOFactory.createRefInputSample(version);
            protocol.addRefInputSample(refInputSample);
            final RefOutputSample refOutputSample = POJOFactory.create(version, RefOutputSample.class);
            protocol.addRefOutputSample(refOutputSample);
            new ParameterDefinition(version, "__SCORE", "String", protocol)
                .setPossibleValues(CaliperFileTest.POSSIBLE_VALUES);
        }

        group.addExperiment(this.makeExperiment(version, holder, protocol));
        group.addExperiment(this.makeExperiment(version, holder, protocol));
        group.addExperiment(this.makeExperiment(version, holder, protocol));
        group.addExperiment(this.makeExperiment(version, holder, protocol));

        return group;
    }

    private Experiment makeExperiment(final WritableVersion version, final Holder holder,
        final Protocol protocol) throws ConstraintException, AccessException {
        final Experiment experiment = POJOFactory.createExperiment(version);
        experiment.setProtocol(protocol);
        final OutputSample output = POJOFactory.createOutputSample(version, experiment);
        final Sample sample = POJOFactory.createSample(version);
        sample.setHolder(holder);
        output.setSample(sample);
        return experiment;
    }

    public void testgetBarcode() throws Exception {
        Assert.assertEquals("PCR139_PCR1", CaliperFile.getBarcode(CaliperFileTest.fileName));
    }

    /* this test fails, because the file is not available, but it fails silently, because of the catch
     * And it doesn't test anything useful
    public void testGetVector() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final File file = new File(CaliperFileTest.filePath + CaliperFileTest.verificationFile);
            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile(file.getName(), new FileInputStream(file));
            // this tests only the parsing of the file name from the bar code
            Assert.assertEquals("PCR139_verification", caliperResultFile.getBarcode());

            // this is pointless, it tests the experiment group in the database, not the processing of the file
            final ExperimentGroup group =
                CaliperFile.findFromExperimentGroup(version, caliperResultFile.getBarcode());
            Assert.assertNotNull(group);

            if (!group.getExperiments().isEmpty()) {
                final Experiment experiment = group.getExperiments().iterator().next();
                final RefSample vector = CaliperVerificationFile.getVector(experiment);
                Assert.assertNotNull(vector);
                Assert.assertEquals("pOPINF Vector", vector.getName());
            }

        } catch (final Exception e) {
            e.printStackTrace();
            Assert.assertNotNull(e);

        } finally {
            version.abort();
        }
    } */

    /* this test fails, because the file is not available, but it fails silently, because of the catch
    // also the experiment group must exist
    public void testGetVectorSize() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final File file = new File(CaliperFileTest.filePath + CaliperFileTest.verificationFile);
            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile(file.getName(), new FileInputStream(file));
            Assert.assertEquals("PCR139_verification", caliperResultFile.getBarcode());

            final ExperimentGroup group =
                CaliperFile.findFromExperimentGroup(version, caliperResultFile.getBarcode());
            Assert.assertNotNull(group);

            if (!group.getExperiments().isEmpty()) {
                final Experiment experiment = group.getExperiments().iterator().next();
                final int size = caliperResultFile.getPCRProductSize(experiment);

                Assert.assertEquals(2866, size);
            }

        } catch (final Exception e) {
            e.printStackTrace();
            Assert.assertNotNull(e);

        } finally {
            version.abort();
        }
    } */

    public void testFindFromExperimentGroup() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, CaliperFileTest.GROUP_NAME + "_verification", "test");
            //final File file = new File(CaliperFileTest.filePath + CaliperFileTest.verificationFile);
            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile("Caliper_GX_" + CaliperFileTest.GROUP_NAME
                    + "_verification_2010-03-26_10-27-34_PeakTable.csv", CaliperFile.parseStringToIS(""));
            Assert.assertEquals(CaliperFileTest.GROUP_NAME + "_verification", caliperResultFile.getBarcode());

            Assert.assertEquals(group,
                CaliperFile.findFromExperimentGroup(version, caliperResultFile.getBarcode()));

            if (!group.getExperiments().isEmpty()) {
                final Experiment experiment = group.getExperiments().iterator().next();
                final RefSample vector = CaliperVerificationFile.getVector(experiment);
                Assert.assertNotNull(vector);
                Assert.assertEquals("pOPINF Vector", vector.getName());
            }

        } finally {
            version.abort();
        }
    }

    public void testGetVectorAlone() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {

            final ExperimentType type = new ExperimentType(version, CaliperFileTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, CaliperFileTest.GROUP_NAME, CaliperFileTest.NOW, CaliperFileTest.NOW,
                    type);
            final String vectorName = CaliperFileTest.UNIQUE + " vector";
            final RefSample refSample = this.createVector(version, experiment, vectorName);

            final RefSample vector = CaliperVerificationFile.getVector(experiment);
            Assert.assertNotNull(vector);
            Assert.assertEquals(refSample, vector);

        } finally {
            version.abort();
        }
    }

    private RefSample createVector(final WritableVersion version, final Experiment experiment,
        final String vectorName) throws ConstraintException {
        final Sample sample = new Sample(version, CaliperFileTest.UNIQUE);
        new InputSample(version, experiment).setSample(sample);
        final SampleCategory category =
            version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, CaliperVerificationFile.VECTOR);
        sample.setSampleCategories(Collections.singleton(category));
        final RefSample refSample = new RefSample(version, vectorName);
        sample.setRefSample(refSample);
        return refSample;
    }

    public void testGetVectorSizeNone() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, CaliperFileTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, CaliperFileTest.GROUP_NAME, CaliperFileTest.NOW, CaliperFileTest.NOW,
                    type);
            //final File file = new File(CaliperFileTest.filePath + CaliperFileTest.verificationFile);
            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile("Caliper_GX_" + CaliperFileTest.GROUP_NAME
                    + "_verification_2010-03-26_10-27-34_PeakTable.csv", CaliperFile.parseStringToIS(""));

            final int size = caliperResultFile.getPCRProductSize(experiment);

            Assert.assertEquals(-1, size);

        } finally {
            version.abort();
        }
    }

    //TODO use an experiment parameter "added from vector"    
    public void testGetPopinfPCRSize() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, CaliperFileTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, CaliperFileTest.GROUP_NAME, CaliperFileTest.NOW, CaliperFileTest.NOW,
                    type);
            final String sequence = "AAAAAAAAAATTTT";
            final ResearchObjective researchObjective = this.createResearchObjective(version, sequence);
            experiment.setProject(researchObjective);
            final String vectorName = "pOPINF";
            this.createVector(version, experiment, vectorName);

            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile("Caliper_GX_" + CaliperFileTest.GROUP_NAME
                    + "_verification_2010-03-26_10-27-34_PeakTable.csv", CaliperFile.parseStringToIS(""));

            final int size = caliperResultFile.getPCRProductSize(experiment);

            Assert.assertFalse(-1 == size);
            Assert.assertEquals(14 + 175, size);

        } finally {
            version.abort();
        }
    }

    //TODO use an experiment parameter "added from vector"    
    public void testGetPCRSize() throws Exception {

        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, CaliperFileTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, CaliperFileTest.GROUP_NAME, CaliperFileTest.NOW, CaliperFileTest.NOW,
                    type);
            final Parameter parameter = new Parameter(version, experiment);
            parameter.setName("Bases from vector");
            parameter.setValue(CaliperFileTest.BASES_FROM_VECTOR.toString());
            final String sequence = "AAAAAAAAAATTTT";
            final ResearchObjective researchObjective = this.createResearchObjective(version, sequence);
            experiment.setProject(researchObjective);

            final CaliperVerificationFile caliperResultFile =
                new CaliperVerificationFile("Caliper_GX_" + CaliperFileTest.GROUP_NAME
                    + "_verification_2010-03-26_10-27-34_PeakTable.csv", CaliperFile.parseStringToIS(""));

            final int size = caliperResultFile.getPCRProductSize(experiment);

            Assert.assertFalse(-1 == size);
            Assert.assertEquals(sequence.length() + CaliperFileTest.BASES_FROM_VECTOR, size);

        } finally {
            version.abort();
        }
    }

    //TODO test CaliperVerificationFile.previousExperiment
}
