/**
 * V4_3-web org.pimslims.spreadsheet MultiPlateScreenSpreadsheetTest.java
 * 
 * @author cm65
 * @date 1 Dec 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

/**
 * MultiPlateScreenSpreadsheetTest
 * 
 */
public class MultiPlateScreenSpreadsheetTest extends TestCase {

    private static final String UNIQUE = "mpss" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * Constructor for MultiPlateScreenSpreadsheetTest
     * 
     * @param name
     */
    public MultiPlateScreenSpreadsheetTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /**
     * MultiPlateScreenSpreadsheetTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * MultiPlateScreenSpreadsheetTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNoSuchKeyword() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Protocol protocol = this.makeProtocol(version);
            final ExperimentGroup toSave =
                new ExperimentGroup(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "toS", "test");
            final Sample sample = this.makeWell(version, protocol, toSave);
            final Parameter parm = new Parameter(version, sample.getOutputSample().getExperiment());
            parm.setName("nonesuch");
            parm.setValue(MultiPlateScreenSpreadsheetTest.UNIQUE + "v");

            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            final SpreadSheet sheet = helper.getSheet(toSave);
            final HolderType holderType = new HolderType(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            final ExperimentGroup loaded =
                helper.getExperimentGroup(sheet, MultiPlateScreenSpreadsheetTest.UNIQUE + "L", version,
                    protocol, MultiPlateScreenSpreadsheetTest.NOW, holderType);
            Assert.assertEquals(1, helper.getErrors().size());
            final SpreadsheetError error = helper.getErrors().iterator().next();
            Assert.assertEquals(parm.getName(), error.getKeyword());

        } finally {
            version.abort();
        }
    }

    public void testParameter() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Protocol protocol = this.makeProtocol(version);
            final ExperimentGroup toSave =
                new ExperimentGroup(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "toS", "test");
            final ParameterDefinition pdef =
                new ParameterDefinition(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "p", "String",
                    protocol);
            final Sample sample = this.makeWell(version, protocol, toSave);
            final Parameter parm = new Parameter(version, sample.getOutputSample().getExperiment());
            parm.setName(pdef.getName());
            parm.setValue(MultiPlateScreenSpreadsheetTest.UNIQUE + "v");
            parm.setParameterDefinition(pdef);

            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            final SpreadSheet sheet = helper.getSheet(toSave);
            final String barcode = sample.getHolder().getName();
            sample.getHolder().delete(); // check it can make a holder
            version.flush();
            final HolderType holderType = new HolderType(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            final ExperimentGroup loaded =
                helper.getExperimentGroup(sheet, MultiPlateScreenSpreadsheetTest.UNIQUE + "L", version,
                    protocol, MultiPlateScreenSpreadsheetTest.NOW, holderType);
            Assert.assertEquals(0, helper.getErrors().size());

            Assert.assertEquals(toSave.getExperiments().size(), loaded.getExperiments().size());
            final Experiment loadedExp = loaded.getExperiments().iterator().next();
            Assert.assertNotNull(loadedExp);
            Assert.assertEquals(protocol, loadedExp.getProtocol());
            Assert.assertEquals(1, loadedExp.getOutputSamples().size());
            final OutputSample os = loadedExp.getOutputSamples().iterator().next();
            Assert.assertNotNull(os.getRefOutputSample());
            final Sample loadedSample = os.getSample();
            Assert.assertNotNull(loadedSample);
            Assert.assertEquals(os.getRefOutputSample().getSampleCategory(), loadedSample
                .getSampleCategories().iterator().next());
            Assert.assertEquals(barcode, loadedSample.getContainer().getName());
            Assert.assertEquals(sample.getRowPosition(), loadedSample.getRowPosition());
            Assert.assertEquals(sample.getColPosition(), loadedSample.getColPosition());

            // test parameter
            Assert.assertEquals(1, loadedExp.getParameters().size());
            final Parameter loadedParm = loadedExp.getParameters().iterator().next();
            Assert.assertEquals(parm.getName(), loadedParm.getName());
            Assert.assertEquals(parm.getValue(), loadedParm.getValue());
            Assert.assertEquals(pdef, loadedParm.getParameterDefinition());
        } finally {
            version.abort();
        }
    }

    public void testInputSample() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Protocol protocol = this.makeProtocol(version);
            final ExperimentGroup toSave =
                new ExperimentGroup(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "toS", "test");
            final SampleCategory category =
                new SampleCategory(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "in");
            final RefInputSample ris = new RefInputSample(version, category, protocol);
            final Sample output = this.makeWell(version, protocol, toSave);
            final InputSample isToSave = new InputSample(version, output.getOutputSample().getExperiment());
            final Sample input = new Sample(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "in");
            isToSave.setSample(input);
            isToSave.setRefInputSample(ris);
            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            // could also support amounts

            final SpreadSheet sheet = helper.getSheet(toSave);
            final HolderType holderType = new HolderType(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            final ExperimentGroup loaded =
                helper.getExperimentGroup(sheet, MultiPlateScreenSpreadsheetTest.UNIQUE + "L", version,
                    protocol, MultiPlateScreenSpreadsheetTest.NOW, holderType);

            Assert.assertEquals(toSave.getExperiments().size(), loaded.getExperiments().size());
            final Experiment loadedExp = loaded.getExperiments().iterator().next();
            Assert.assertNotNull(loadedExp);
            Assert.assertEquals(0, helper.getErrors().size());

            // test input
            Assert.assertEquals(1, loadedExp.getInputSamples().size());
            final InputSample loadedIS = loadedExp.getInputSamples().iterator().next();
            Assert.assertEquals(isToSave.getName(), loadedIS.getName());
            Assert.assertEquals(ris, loadedIS.getRefInputSample());
            Assert.assertEquals(input, loadedIS.getSample());
        } finally {
            version.abort();
        }
    }

    public void testProject() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Protocol protocol = this.makeProtocol(version);
            final ExperimentGroup toSave =
                new ExperimentGroup(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "toS", "test");
            final SampleCategory category =
                new SampleCategory(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "in");
            final Sample output = this.makeWell(version, protocol, toSave);
            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            final ResearchObjective project =
                new ResearchObjective(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "ro", "test");
            output.getOutputSample().getExperiment().setProject(project);
            final Molecule protein = new Molecule(version, "protein", MultiPlateScreenSpreadsheetTest.UNIQUE);
            final Target target = new Target(version, MultiPlateScreenSpreadsheetTest.UNIQUE, protein);
            final Organism species = new Organism(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "o");
            target.setSpecies(species);
            new ResearchObjectiveElement(version, "Target", "test", project).setTarget(target);

            final SpreadSheet sheet = helper.getSheet(toSave);
            final HolderType holderType = new HolderType(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
            holderType.setMaxColumn(1);
            holderType.setMaxRow(1);
            final ExperimentGroup loaded =
                helper.getExperimentGroup(sheet, MultiPlateScreenSpreadsheetTest.UNIQUE + "L", version,
                    protocol, MultiPlateScreenSpreadsheetTest.NOW, holderType);

            Assert.assertEquals(toSave.getExperiments().size(), loaded.getExperiments().size());
            final Experiment loadedExp = loaded.getExperiments().iterator().next();
            Assert.assertNotNull(loadedExp);
            Assert.assertEquals(0, helper.getErrors().size());

            // test project
            Assert.assertEquals(project, loadedExp.getProject());
        } finally {
            version.abort();
        }
    }

    public void testTarget() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Protocol protocol = this.makeProtocol(version);
            final ExperimentGroup toSave =
                new ExperimentGroup(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "toS", "test");
            final SampleCategory category =
                new SampleCategory(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "in");
            final Sample output = this.makeWell(version, protocol, toSave);
            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            final ResearchObjective project =
                new ResearchObjective(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "ro", "test");
            output.getOutputSample().getExperiment().setProject(project);
            final Molecule protein = new Molecule(version, "protein", MultiPlateScreenSpreadsheetTest.UNIQUE);
            final Target target = new Target(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "t", protein);
            final Organism species = new Organism(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "o");
            target.setSpecies(species);
            new ResearchObjectiveElement(version, "Target", "test", project).setTarget(target);

            final MultiPlateScreen bean = helper.makeBean(toSave);
            Assert.assertEquals(target.getName(),
                bean.getValue(MultiPlateScreenSpreadsheetTest.UNIQUE + "plate", "A01", "Target"));
            Assert.assertEquals(species.getName(),
                bean.getValue(MultiPlateScreenSpreadsheetTest.UNIQUE + "plate", "A01", "Organism"));

        } finally {
            version.abort();
        }
    }

    private Sample makeWell(final WritableVersion version, final Protocol protocol,
        final ExperimentGroup toSave) throws ConstraintException {
        Sample sample;
        final Experiment experiment =
            new Experiment(version, MultiPlateScreenSpreadsheetTest.UNIQUE,
                MultiPlateScreenSpreadsheetTest.NOW, MultiPlateScreenSpreadsheetTest.NOW,
                protocol.getExperimentType());
        experiment.setProtocol(protocol);
        sample = new Sample(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
        final Container holder = new Holder(version, MultiPlateScreenSpreadsheetTest.UNIQUE + "plate", null);
        sample.setContainer(holder);
        sample.setRowPosition(1);
        sample.setColPosition(1);
        new OutputSample(version, experiment).setSample(sample);
        toSave.addExperiment(experiment);
        return sample;
    }

    private Protocol makeProtocol(final WritableVersion version) throws ConstraintException {
        Protocol protocol;
        final ExperimentType type = new ExperimentType(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
        protocol = new Protocol(version, MultiPlateScreenSpreadsheetTest.UNIQUE, type);
        final SampleCategory category = new SampleCategory(version, MultiPlateScreenSpreadsheetTest.UNIQUE);
        new RefOutputSample(version, category, protocol);
        return protocol;
    }

    //TODO test status
    /**
     * MultiPlateScreenSpreadsheetTest.main Load and re-export a spreadsheet
     * 
     * @param args
     */
    public static void main(final String[] args) {
        if (3 != args.length) {
            System.out.println("Usage: protocol holdertype inputFile. Args were:");
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i]);
            }
            return;
        }
        final File inputFile = new File(args[2]);
        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            final Protocol protocol = version.findFirst(Protocol.class, Protocol.PROP_NAME, args[0]);
            if (null == protocol) {
                throw new RuntimeException("Protocol not found: " + args[0]);
            }
            final HolderType holderType =
                version.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, args[1]);
            if (null == holderType) {
                throw new RuntimeException("HolderType not found: " + args[1]);
            }

            final MultiPlateScreenSpreadsheetHelper helper = new MultiPlateScreenSpreadsheetHelper();
            final InputStream is = new FileInputStream(inputFile);
            final SpreadSheet sheet = new SpreadSheet(is);
            final ExperimentGroup loaded =
                helper.getExperimentGroup(sheet, MultiPlateScreenSpreadsheetTest.UNIQUE + "L", version,
                    protocol, MultiPlateScreenSpreadsheetTest.NOW, holderType);
            final Collection<SpreadsheetError> errors = helper.getErrors();
            for (final Iterator iterator = errors.iterator(); iterator.hasNext();) {
                final SpreadsheetError error = (SpreadsheetError) iterator.next();
                System.out.println(error.toString());
            }

        } catch (final FileNotFoundException e) {
            System.err.println("File not found: " + inputFile.getAbsolutePath());
            throw new RuntimeException(e);
        } catch (final InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
