/**
 * 
 */
package org.pimslims.utils.orders.operon;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.properties.PropertyGetter;

/**
 * @author Jon Diprose
 */
public class TestPrimerOrderFormImpl extends TestCase {

    private static final String UNIQUE = "pofi" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    static {
        PropertyGetter.setWorkingDirectory("WebContent/WEB-INF/");
    }

    private final AbstractModel model;

    /**
     * @param name
     */
    public TestPrimerOrderFormImpl(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test case for loadTemplate
     * 
     * @throws IOException
     */
    public void testLoadTemplate() throws IOException {

        final PrimerOrderFormImpl of = new PrimerOrderFormImpl();
        final HSSFWorkbook wb = of.loadTemplate();

        final String s1a1 = "CUSTOMER CONTACT, SHIPPING + INVOICE DETAILS";
        final String sxa1 = "OLIGO ID";
        final String sxa2 = "SEQUENCE";

        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row = sheet.getRow(sheet.getFirstRowNum());
        int cellNum = row.getFirstCellNum();
        HSSFCell cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), s1a1);

        sheet = wb.getSheetAt(1);
        row = sheet.getRow(sheet.getFirstRowNum());
        cellNum = row.getFirstCellNum();
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa1);
        cellNum++;
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa2);

        sheet = wb.getSheetAt(2);
        row = sheet.getRow(sheet.getFirstRowNum());
        cellNum = row.getFirstCellNum();
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa1);
        cellNum++;
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa2);

        sheet = wb.getSheetAt(3);
        row = sheet.getRow(sheet.getFirstRowNum());
        cellNum = row.getFirstCellNum();
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa1);
        cellNum++;
        cell = row.getCell(cellNum);
        Assert.assertEquals(cell.getRichStringCellValue().toString(), sxa2);

    }

    /**
     * Test case for findPrimer.
     * 
     * TODO Requires existing primer sample for testing. How do I get some?
     * 
     * @throws ConstraintException
     * @throws AccessException
     */

    public void testFindPrimer() throws AccessException, ConstraintException {

        final WritableVersion version =
            this.model.getTestVersion();
        final String myName = "MyPrimer";
        final String mySequence = "AAGTTCTGTTTCAGGGCCCGacatacgtccaggccctctttgac";
        try {

            final HashMap<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Substance.PROP_NAME, myName);
            attributes.put(Molecule.PROP_MOLTYPE, "DNA");
            attributes.put(Molecule.PROP_SEQUENCE, mySequence);
            attributes.put(Primer.PROP_DIRECTION, "forward");
            attributes.put(Primer.PROP_ISUNIVERSAL, false);
            attributes.put(Primer.PROP_MELTINGTEMPERATURE, new Float("42.4"));
            final Primer primer = version.create(org.pimslims.model.molecule.Primer.class, attributes);

            attributes.clear();
            attributes.put(AbstractSample.PROP_NAME, myName);
            attributes.put(AbstractSample.PROP_SAMPLECATEGORIES, new HashSet());
            final Sample sample = version.create(org.pimslims.model.sample.Sample.class, attributes);

            attributes.clear();
            attributes.put(SampleComponent.PROP_REFCOMPONENT, primer);
            attributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
            version.create(org.pimslims.model.sample.SampleComponent.class, attributes);

            final PrimerOrderFormImpl of = new PrimerOrderFormImpl();
            final Sample s = (Sample) version.get(sample.get_Hook());
            final Primer p = of.findPrimer(s, version);

            Assert.assertEquals("sequence ok", mySequence, p.getSequence());

        } finally {
            version.abort(); // not testing persistence here
        }

    }

    public void testGenerateOrderForm() throws AccessException, ConstraintException, IOException {

        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ExperimentGroup group =
                new ExperimentGroup(version, TestPrimerOrderFormImpl.UNIQUE, "test");
            final ExperimentType type = new ExperimentType(version, TestPrimerOrderFormImpl.UNIQUE);
            final Experiment experiment =
                new Experiment(version, TestPrimerOrderFormImpl.UNIQUE, TestPrimerOrderFormImpl.NOW,
                    TestPrimerOrderFormImpl.NOW, type);
            group.addExperiment(experiment);
            final Sample sample = new Sample(version, TestPrimerOrderFormImpl.UNIQUE + "o");
            new OutputSample(version, experiment).setSample(sample);
            final HolderType holderType = new HolderType(version, TestPrimerOrderFormImpl.UNIQUE);
            final Holder plate = new Holder(version, TestPrimerOrderFormImpl.UNIQUE, holderType);
            sample.setContainer(plate);
            sample.setRowPosition(1);
            sample.setColPosition(1);
            final Sample primerSample = new Sample(version, TestPrimerOrderFormImpl.UNIQUE + "f");
            final Substance primer =
                new Primer(version, "Forward", true, "DNA", TestPrimerOrderFormImpl.UNIQUE);
            final SampleComponent component = new SampleComponent(version, primer, primerSample);
            primerSample.addSampleComponent(component);
            new InputSample(version, experiment).setSample(primerSample);
            final PrimerOrderFormImpl of = new PrimerOrderFormImpl();
            of.loadFromOrderExperiment(version, plate.get_Hook());
            Assert.assertEquals(1, of.getPrimers().size());
            of.generateOrder();
            //TODO test the workbook

        } finally {
            version.abort(); // not testing persistence here
        }

    }
    /**
     * Test case for generateOrder.
     * 
     * TODO Requires existing primer samples for testing. How do I get some?
     */
    /*
     * public void testGenerateOrder() { WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
     * String forwardhook = "org.pimslims.model.experiment.ExperimentGroup:17311"; String reversehook =
     * "org.pimslims.model.experiment.ExperimentGroup:17553"; PrimerOrderFormImpl of = new PrimerOrderFormImpl(); try {
     * of.processPlate(version, forwardhook); of.processPlate(version, reversehook); HSSFWorkbook wb =
     * of.generateOrder(); checkGeneratedWorkbook(wb); } catch (Exception e) { e.printStackTrace();
     * fail(e.getMessage()); } finally { version.abort(); // not testing persistence here } }
     */

    /**
     * Test case for saveOrderForm.
     * 
     * TODO Requires existing primer samples for testing. How do I get some?
     */
    /*
     * public void testSaveOrderForm() throws IOException { WritableVersion version =
     * model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR); String forwardhook =
     * "org.pimslims.model.experiment.ExperimentGroup:17311"; String reversehook =
     * "org.pimslims.model.experiment.ExperimentGroup:17553"; PrimerOrderFormImpl of = new PrimerOrderFormImpl(); try {
     * of.processPlate(version, forwardhook); of.processPlate(version, reversehook);
     * of.saveOrderForm("PCR119", version); //Annotation annotation = expt.findFirst(PROP_ANNOTATION);
     * //org.pimslims.util.File file = FileImpl.getFile(annotation); //POIFSFileSystem fs = new
     * POIFSFileSystem(new java.io.FileInputStream(file.getFile())); //HSSFWorkbook wb = new HSSFWorkbook(fs);
     * //checkGeneratedWorkbook(wb); } catch (Exception e) { e.printStackTrace(); fail(e.getMessage()); }
     * finally { version.abort(); // not testing persistence here } //PrimerOrderFormImpl of = new
     * PrimerOrderFormImpl(); //WritableVersion wv; // = Model.getWritableVersion() ? //Experiment expt =
     * (Sample) wv.get(EXPT_HOOK); //Sample sample = (Sample) wv.get(SAMPLE_HOOK); //of.addPrimer(sample);
     * //of.saveOrderForm(expt, wv); // TODO find file, read into workbook and check as for testGenerateOrder
     * //Annotation annotation = expt.findFirst(PROP_ANNOTATION); //org.pimslims.util.File file =
     * FileImpl.getFile(annotation); //POIFSFileSystem fs = new POIFSFileSystem(new
     * java.io.FileInputStream(file.getFile())); //HSSFWorkbook wb = new HSSFWorkbook(fs);
     * //checkGeneratedWorkbook(wb); }
     */

}
