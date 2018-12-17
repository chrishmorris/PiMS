package org.pimslims.utils.sequenator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pimslims.dao.WritableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.utils.ExcelConnector;
import org.pimslims.utils.ExcelConnectorImpl;
import org.pimslims.utils.experiment.Utils;

/**
 * <p>
 * Load instrument setup excel template and manipulate with it.
 * </p>
 * 
 * @author Petr Troshin
 */
public class InstrumentSheet {

    public static final String priority = "100";

    public static final String srGroup1 = "Sequencing_Results_Group";

    public static final String instrProt = "StdSeq50cm-POP7";

    public static final String analysProt = "Seq50cmPOP7_BDTv3KBv5.2";

    /**
     * The name of the instrument template
     */
    private static String INSTRUMENT_SETUP_TEMPLATE = "InstrumentSetup.xls";

    final ExcelConnector connector;

    protected final HSSFWorkbook workbook;

    protected final HSSFSheet sheet;

    public static final String[] header =
        new String[] { "Well", "Sample Name", "Comment", "Priority", "Results Group 1",
            "Instrument Protocol 1", "Analysis Protocol 1" };

    public InstrumentSheet() {
        try {

            this.connector = new ExcelConnectorImpl();

            final InputStream is =
                InstrumentSheet.class.getResourceAsStream(InstrumentSheet.INSTRUMENT_SETUP_TEMPLATE);
            assert (null != is) : "Instrument  Template is not found ["
                + InstrumentSheet.INSTRUMENT_SETUP_TEMPLATE + "]";

            this.workbook = this.connector.readTemplate(is);
            this.sheet = this.workbook.getSheetAt(0);

            if (!this.isValidInstrumentTemplate()) {
                throw new IOException("Wrong template!");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InstrumentSheet(final File file) {
        try {

            this.connector = new ExcelConnectorImpl();

            final InputStream is = new FileInputStream(file);
            assert (null != is) : "Instrument Template is not found ["
                + InstrumentSheet.INSTRUMENT_SETUP_TEMPLATE + "]";

            this.workbook = this.connector.readTemplate(is);
            this.sheet = this.workbook.getSheetAt(0);
            if (!this.isValidInstrumentTemplate()) {
                throw new IOException("Wrong template!");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InstrumentSheet(final InputStream stream) {
        try {
            this.connector = new ExcelConnectorImpl();
            assert (null != stream) : "Empty stream!";
            this.workbook = this.connector.readTemplate(stream);
            this.sheet = this.workbook.getSheetAt(0);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidInstrumentTemplate() {
        return this.connector.validateHeaderData(InstrumentSheet.header, this.sheet);
    }

    public byte[] writeShortSetupSheet(final ExperimentGroup eg, final String runName,
        final WritableVersion rw) throws IOException {

        final Set<Experiment> exps = eg.getExperiments();
        assert exps != null && !exps.isEmpty();

        if (exps.size() > 96) {
            throw new RuntimeException("Cannot fit the data in the givemn array!");
        }

        final ArrayList<Experiment> sortedExps = new ArrayList<Experiment>(exps);
        Collections.sort(sortedExps, SequencingOrder.nameComporator);

        // write container name = run     
        this.writeContainerName(runName);

        int rowC = 0;
        int prevIdx = -1;
        final String[][] data = new String[96][7];
        for (final Experiment exp : sortedExps) {
            assert exp.getProtocol().getName().equals(SequencingOrder.soProtocolName);

            final String well = this.getWell(exp);
            int curidx = PlatePlanner.positions.indexOf(well);
            if (prevIdx == -1) {
                prevIdx = curidx;
            }
            // Add empty rows up until the control if any
            if (curidx - prevIdx > 1) {
                for (int i = prevIdx + 1; i < curidx; i++) {
                    this.addDataLine(PlatePlanner.positions.get(i), "BLANK", "", data, rowC++);
                }
            }
            prevIdx = curidx;
            curidx = PlatePlanner.positions.indexOf(well);
            final String sampleName =
                Utils.getParameterValue(exp, SequencingOrder.TemplateName) + "_"
                    + Utils.getParameterValue(exp, SequencingOrder.primerName);

            this.addDataLine(well, sampleName, exp.getName(), data, rowC);
            rowC++;
        }
        this.connector.update(data, this.sheet, 5, (short) 0);
        return this.connector.write(this.workbook);
    }

    void addDataLine(final String well, final String sampleName, final String expName, final String data[][],
        final int rowIdx) {
        data[rowIdx][0] = well; // well
        data[rowIdx][1] = sampleName;
        data[rowIdx][2] = expName; // this is reaction number - has to be SSE exp 
        data[rowIdx][3] = InstrumentSheet.priority; // priority
        data[rowIdx][4] = InstrumentSheet.srGroup1; // result group
        data[rowIdx][5] = InstrumentSheet.instrProt; // instrument prot
        data[rowIdx][6] = InstrumentSheet.analysProt; // analisys prot
    }

    /**
     * InstrumentSheet.getWellNumber
     * 
     * @param exp
     * @return
     */
    private String getWell(final Experiment exp) {
        assert exp.getOutputSamples().size() == 1;
        return HolderFactory.getPositionInHolder(exp); //HolderFactory.getColumn(HolderFactory.getColumn(exp));
    }

    void writeContainerName(final String runName) {
        assert !Util.isEmpty(runName);
        final HSSFRow cnamerow = this.connector.getRow("Container Name", this.sheet);
        final HSSFRow nextR = this.sheet.getRow(cnamerow.getRowNum() + 1);
        final HSSFCell runNumCell = nextR.getCell(0, HSSFRow.CREATE_NULL_AS_BLANK);
        final HSSFRichTextString text = new HSSFRichTextString(runName);
        runNumCell.setCellValue(text);
    }
} // class end
