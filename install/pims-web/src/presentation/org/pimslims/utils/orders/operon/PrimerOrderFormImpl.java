/**
 * 
 */
package org.pimslims.utils.orders.operon;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.plateExperiment.PlateReader;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.util.File;
import org.pimslims.utils.orders.PrimerOrderForm;

/**
 * <p>
 * Generate a Primer Order Form for Operon. The forward and reverse primers can go on the same order form. The
 * primers must know their position on a plate.
 * </p>
 * 
 * <p>
 * Primer samples without a Primer SampleComponent will be ignored.
 * </p>
 * 
 * <p>
 * Primer samples with rowPosition or colPosition null will be ignored.
 * </p>
 * 
 * <p>
 * If multiple primers are put in the same position, only the last primer added will be ordered.
 * </p>
 * 
 * <p>
 * Forward primers will be ordered in plate 1. Reverse primers will be ordered in plate 2.
 * </p>
 * 
 * @author Jon Diprose
 */
public class PrimerOrderFormImpl implements PrimerOrderForm {

    /**
     * Prefix to use in description generation
     */
    private static final String DESCRIPTION = "Operon Primer Order Form for expt ";

    /**
     * Prefix to use in filename generation
     */
    private static final String PREFIX = "OperonOrderForm_";

    /**
     * Suffix to use in filename generation
     */
    private static final String SUFFIX = ".xls";

    /**
     * Mime type for excel documents
     */
    private static final String MIMETYPE = "application/vnd.ms-excel";

    /**
     * The name of the Operon order form template
     */
    //private static String OPERON_TEMPLATE = "OPERON.xlt";
    /**
     * The name of the Operon order form
     */
    private String orderId;

    /**
     * Collection of primers to be placed on the order form
     */
    private final Collection<PrimerBean> primers = new ArrayList<PrimerBean>();

    /**
     * Zero-arg constructor
     */
    public PrimerOrderFormImpl() {
        super();
    }

    /**
     * Load the Operon order form template and return the workbook.
     * 
     * @return
     * @throws IOException
     */
    protected final HSSFWorkbook loadTemplate() throws IOException {

        final java.io.File file =
            PropertyGetter.getFileProperty("PrimerOrderTemplate", "conf/PrimerOrderTemplate.xlt");

        final InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            System.out.println("Primer Order Template not found: " + file.getAbsolutePath());
            throw e;
        }

        final POIFSFileSystem fs = new POIFSFileSystem(is);
        return new HSSFWorkbook(fs);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.orders.PrimerOrderForm#addPrimer(org.pimslims.model.sample.Sample)
     */
    public final void addPrimer(final PrimerBean primer) {
        this.primers.add(primer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.orders.PrimerOrderForm#removePrimer(org.pimslims.model.sample.Sample)
     */
    public final void removePrimer(final PrimerBean primer) {
        if (this.primers.contains(primer)) {
            this.primers.remove(primer);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.orders.PrimerOrderForm#addAll(java.util.Collection)
     */
    public final void addAll(final Collection<PrimerBean> beans) {
        this.primers.addAll(beans);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.orders.PrimerOrderForm#removeAll()
     */
    public final void removeAll() {
        this.primers.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.utils.orders.PrimerOrderForm#saveOrderForm(org.pimslims.model.experiment.Experiment,
     *      org.pimslims.dao.WritableVersion)
     */
    public final File saveOrderForm(final ModelObject object, final WritableVersion version)
        throws AccessException, ConstraintException, IOException {

        final HSSFWorkbook wb = this.generateOrder();
        // Fix to poi problem where excel doesn't open file
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        wb.write(stream);

        // attach the order file to the experiment group
        ExperimentGroup expGroup;

        if (object instanceof Holder) {
            expGroup = HolderFactory.getExperimentGroup((Holder) object);
        } else {
            assert object instanceof ExperimentGroup;
            expGroup = (ExperimentGroup) object;
        }
        version.setDefaultOwner(expGroup.getAccess());
        final File file =
            version.createFile(stream.toByteArray(), PrimerOrderFormImpl.PREFIX + this.orderId
                + PrimerOrderFormImpl.SUFFIX, expGroup);
        // endFix File file = version.createFile(wb.getBytes(), PREFIX + orderId + SUFFIX);
        file.setDescription(PrimerOrderFormImpl.DESCRIPTION + this.orderId);
        file.setMimeType(PrimerOrderFormImpl.MIMETYPE);

        /*
         * for (Iterator iter = expGroup.getAnnotations().iterator(); iter.hasNext();) { Annotation annotation =
         * (Annotation)iter.next(); if (annotation.getName().equals(file.getName())) return
         * annotation.get_Hook()+"/"+file.getName(); }
         */
        return file;

    }

    /**
     * Populate the order form and return the workbook
     * 
     * @return
     * @throws IOException
     */
    protected HSSFWorkbook generateOrder() throws IOException {

        final HSSFWorkbook wb = this.loadTemplate();

        // Get the heading sheet and set the plate identifier
        if (null != this.orderId) {
            final HSSFSheet detailSheet = wb.getSheetAt(0);

            for (int i = detailSheet.getFirstRowNum(); i < detailSheet.getLastRowNum(); i++) {
                final HSSFRow row = detailSheet.getRow(i);

                if (row.getPhysicalNumberOfCells() > 2) {
                    final HSSFCell cell = row.getCell(0);
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING
                        && cell.getStringCellValue().equals("Plate names/identifiers")) {
                        final HSSFCell myCell = row.getCell(1);
                        myCell.setCellValue(this.orderId);
                    }
                }
            }
        }

        for (final PrimerBean primerSample : this.primers) {

            int sheetNum = 1;
            if (primerSample.isReverse()) {
                sheetNum = 2;
            }

            // Get the appropriate sheet
            final HSSFSheet sheet = wb.getSheetAt(sheetNum);

            // If the sample is in a plate
            if ((null != primerSample.getCol()) && (null != primerSample.getRow())) {

                // Map pims plate coords into operon plate coords
                // Should produce a rowNum from 1 to 96
                final int rowNum =
                    ((primerSample.getCol().intValue() - 1) * 8) + primerSample.getRow().intValue();

                // Get the row, offset from the first row of the sheet
                final HSSFRow row = sheet.getRow(rowNum + sheet.getFirstRowNum());

                // Populate OLIGO_ID and SEQUENCE
                int cellNum = row.getFirstCellNum();
                HSSFCell cell = row.getCell(cellNum);
                cell.setCellValue(primerSample.getName());
                cellNum++;
                cell = row.getCell(cellNum);
                cell.setCellValue(primerSample.getSequence());

            }

        }

        return wb;
    }

    /**
     * Find the Primer SampleComponent for the specified sample
     * 
     * @param sample - the sample to check
     * @return The Primer, or null if no instance of primer is found
     */
    protected final Primer findPrimer(final Sample sample, final ReadableVersion rv) {

        final Set<SampleComponent> components = sample.getSampleComponents();

        for (final SampleComponent component : components) {
            final Substance ac = component.getRefComponent();
            if (Primer.class.equals(ac.get_MetaClass().getJavaClass())) {
                return (Primer) rv.get(ac.get_Hook());
            }
        }

        return null;

    }

    /*
     * Load PrimerOrderForm from Primer Order Experiment
     */
    public final void loadFromOrderExperiment(final WritableVersion version, final String hook)
        throws AccessException {

        final ModelObject object = version.get(hook);
        Holder holder;
        PlateReader reader = null;

        if (object instanceof Holder) {
            holder = (Holder) object;
        } else {
            assert object instanceof ExperimentGroup;
            holder = HolderFactory.getPlate((ExperimentGroup) object);
        }

        reader = new PlateReader(version, holder, null);

        this.orderId = holder.getName();

        for (final WellExperimentBean experiment : reader.getExperiments()) {

            // we must have a virtual output as this tells us the well within
            // the plate
            final Sample outSample = (Sample) version.get(experiment.getOutputSampleHook());

            for (final InputSampleBean isbean : experiment.getInputSamples()) {

                final InputSample inSample = (InputSample) version.get(isbean.getInputSampleHook());
                final Sample sample = this.getPrimerSample(inSample);

                if (null != sample) {
                    final Primer primer = this.findPrimer(sample, version);

                    final PrimerBean mySample = new PrimerBean();
                    mySample.setName(sample.getName());
                    mySample.setSequence(primer.getSequence());
                    mySample.setDirection(primer.getDirection());
                    mySample.setCol(outSample.getColPosition());
                    mySample.setRow(outSample.getRowPosition());
                    mySample.setSub(outSample.getSubPosition());
                    this.addPrimer(mySample);
                }
            }
        }
    }

    /*
     * From this InputSample, search back through the experiment tree to return the parent primer sample
     * return null if not found
     */
    private final Sample getPrimerSample(final InputSample input) {

        final Sample sample = input.getSample();
        if (null == sample) {
            return null;
        }
        if (this.isPrimer(sample)) {
            return sample;
        }

        for (final InputSample inputSample : sample.getOutputSample().getExperiment().getInputSamples()) {
            if (null != this.getPrimerSample(inputSample)) {
                return this.getPrimerSample(inputSample);
            }
        }

        return null;
    }

    private final boolean isPrimer(final Sample sample) {

        for (final SampleComponent sampleComponent : sample.getSampleComponents()) {
            final Substance ac = sampleComponent.getRefComponent();

            if (Primer.class.equals(ac.get_MetaClass().getJavaClass())) {
                return true;
            }
        }
        return false;
    }

    public Collection<PrimerBean> getPrimers() {
        return this.primers;
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            System.out
                .println("Usage: PrimerOrderFormImpl [-o order_plate_hook] [-f forward_primer_plate_hook -r reverse_primer_plate_hook]");
        }

        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        // String forwardhook = "org.pimslims.model.experiment.ExperimentGroup:17311";
        // String reversehook = "org.pimslims.model.experiment.ExperimentGroup:17553";
        // String orderhook = "org.pimslims.model.experiment.ExperimentGroup:20064";

        String hook = null;

        for (int i = 0; i < args.length; i++) {

            final String s = args[i];
            if (s.charAt(0) == '-') {
                final char ch = s.charAt(1);
                switch (ch) {

                    case 'o':
                        hook = args[++i];
                        break;

                    default:
                        break;
                }
            }
        }

        try {

            final PrimerOrderFormImpl of = new PrimerOrderFormImpl();

            if (null != hook) {
                of.loadFromOrderExperiment(version, hook);
            }

            of.saveOrderForm(version.get(hook), version);
            version.commit();

        } catch (final AbortedException e) {
            e.printStackTrace();

        } catch (final AccessException e) {
            e.printStackTrace();

        } catch (final ConstraintException e) {
            e.printStackTrace();

        } catch (final IOException e) {
            e.printStackTrace();

        } finally {
            if (!version.isCompleted()) {
                version.abort(); // not testing persistence here
            }
        }
    }
}
