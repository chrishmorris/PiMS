package org.pimslims.utils.sequenator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.reference.HolderType;
import org.pimslims.utils.ExcelConnector;
import org.pimslims.utils.ExcelConnectorImpl;
import org.pimslims.utils.ExcelSheetData;
import org.pimslims.utils.StyleManager;
import org.pimslims.utils.experiment.Utils;

/**
 * <p>
 * Load plate sequence setup template and manipulate with it.
 * </p>
 * 
 * @author Petr Troshin
 */
public class SequencePrepSheet {

    public static final String ssPlatesequence = "SequenceSetupPlate";

    public static final String seqPlateName = "Sequence_Plate";

    /**
     * Prefix to use in description generation
     */
    public static final String DESCRIPTION = "Sequencing plate preparation";

    /**
     * Mime type for excel documents
     */
    public static final String MSExcel_MIMETYPE = "application/vnd.ms-excel";

    /**
     * The name of the plate setup template
     */
    private static String PLATE_SETUP_TEMPLATE = "PlateSetUp.xls";

    private static String PLATE_SETUP_TEMPLATE_FOR_ROBOT = "PlateSetUpforRobot.xls";

    final ExcelConnector connector;

    protected final HSSFWorkbook workbook;

    protected final HSSFSheet sheet;

    public static final String[] header =
        new String[] { "Well", "Sample Name", "Primer Name", "Premix", "Template", "Primer", "Water",
            "Total Vol." };

    public static enum TemplateType {
        manual, robotics
    }

    public SequencePrepSheet(final TemplateType type) {
        try {

            this.connector = new ExcelConnectorImpl();

            InputStream is = null;
            if (type == TemplateType.manual) {
                is = SequencePrepSheet.class.getResourceAsStream(SequencePrepSheet.PLATE_SETUP_TEMPLATE);
            } else {
                is =
                    SequencePrepSheet.class
                        .getResourceAsStream(SequencePrepSheet.PLATE_SETUP_TEMPLATE_FOR_ROBOT);
            }
            assert (null != is) : "Platesetup Template is not found ["
                + SequencePrepSheet.PLATE_SETUP_TEMPLATE + "]";

            this.workbook = this.connector.readTemplate(is);
            this.sheet = this.workbook.getSheetAt(0);
            if (!this.isValidPlateSetUpTemplate()) {
                throw new IOException("Wrong template!");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SequencePrepSheet(final File file) {
        try {

            this.connector = new ExcelConnectorImpl();

            final InputStream is = new FileInputStream(file);
            assert (null != is) : "Platesetup Template is not found ["
                + SequencePrepSheet.PLATE_SETUP_TEMPLATE + "]";

            this.workbook = this.connector.readTemplate(is);
            this.sheet = this.workbook.getSheetAt(0);
            if (!this.isValidPlateSetUpTemplate()) {
                throw new IOException("Wrong template!");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SequencePrepSheet(final InputStream stream) {
        try {
            this.connector = new ExcelConnectorImpl();
            assert (null != stream) : "Empty stream!";
            this.workbook = this.connector.readTemplate(stream);
            this.sheet = this.workbook.getSheetAt(0);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Make rows of a different color if
     * 
     * 1) sample name changes
     * 
     * 2) primer name changes
     * 
     * 3) total volume changes
     * 
     * But keep the same color for the same volume and the same primers throughout the sheet
     * SequencePrepSheet.getNiceSpreadsheet
     * 
     * @param seqSetupExps
     * @param onPrimerChange
     * @param onTotalVolumeChange
     * @return
     * @throws IOException
     */
    public byte[] getNiceSpreadsheet(final List<SeqSetupExperiment> seqSetupExps) throws IOException {

        final StyleManager manager = new StyleManager();
        final ExcelSheetData dataSheet = new ExcelSheetData(seqSetupExps.size());

        final HSSFCellStyle standardStyle = this.connector.getCellAt(7, (short) 2, this.sheet).getCellStyle();
        HSSFFont font = this.workbook.getFontAt(standardStyle.getFontIndex());
        final Iterator<SeqSetupExperiment> it = seqSetupExps.iterator();
        int counter = 0;
        SeqSetupExperiment prevExp = null;
        HSSFCellStyle style = standardStyle;
        Short color = null;
        while (it.hasNext()) {
            final SeqSetupExperiment exp = it.next();

            // Vary row color based on the values 
            if (prevExp != null && SeqSetupExperiment.highlight(exp, prevExp)) {
                color = manager.getNextColor(exp.getTotalVolume(), color);
                style = this.workbook.createCellStyle();
                //TODO better to copy the font from template, unfortunately POI does not cater for it at 3.2 version 
                font = this.workbook.createFont();
                font.setColor(color);
                font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                style.cloneStyleFrom(standardStyle);
                style.setFont(font);

            }

            dataSheet.addDataRow(counter++, exp.toArray(), style);
            prevExp = exp;
        }
        this.connector.update(dataSheet, this.sheet, 3, 0);
        return this.connector.write(this.workbook);
    }

    /*
    private String headerToCSVString() {
        String head = "";
        for (int i = 0; i < SequencePrepSheet.header.length; i++) {
            head += SequencePrepSheet.header[i] + ",";
        }
        return head.substring(head.length() - 1) + "\n";
    }
    */
/*
    private String getHeaderForProtocol() {
        String header = "";
        header += "Well" + ",";
        header += "Sample Name" + ",";
        header += "Primer Name" + ",";
        header += "Premix" + ",";
        header += "Template" + ",";
        header += "Primer" + ",";
        header += "Water" + ",";
        header += "Total Vol." + "\n";
        return header;
    }
*/

    /**
     * This constructor for test case use only!!
     * 
     * Constructor for SequencePrepSheet
     * 
     * @param testing
     */
    protected SequencePrepSheet(final boolean testing) {
        try {

            this.connector = new ExcelConnectorImpl();
            final InputStream is = SequencePrepSheet.class.getResourceAsStream("TestPlateSetUp.xlt");
            assert (null != is) : "TestPlatesetup Template is not found ["
                + SequencePrepSheet.PLATE_SETUP_TEMPLATE + "]";

            this.workbook = this.connector.readTemplate(is);
            this.sheet = this.workbook.getSheetAt(0);
            if (!this.isValidPlateSetUpTemplate()) {
                throw new IOException("Wrong template!");
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HSSFRow getHeaderRow() {
        return this.connector.getRow("Well", this.sheet);
    }

    public List<SeqSetupExperiment> extractSeqSetupExpValues() {
        final List<SeqSetupExperiment> ssel = new ArrayList<SeqSetupExperiment>(100);
        final int firstDataRowNum = this.getHeaderRow().getRowNum() + 1;
        for (int i = 0; i < 96; i++) {
            final HSSFRow nextrow = this.sheet.getRow(i + firstDataRowNum);
            final SeqSetupExperiment sse = new SeqSetupExperiment();
            sse.templateName = nextrow.getCell(1).getRichStringCellValue().getString();
            // If there is no sample name, assume that this is the end of the data 
            // e.g. complete sheet is not provided

            if (Util.isEmpty(sse.templateName)) {
                assert i != 0 : "The first value is empty! The value is required. Cannot proceed.";
                break;
            }

            sse.wellNum = nextrow.getCell(0).getRichStringCellValue().getString();
            sse.primerName = nextrow.getCell(2).getRichStringCellValue().getString();
            sse.premixVol = nextrow.getCell(3).getNumericCellValue();
            sse.templateVol = nextrow.getCell(4).getNumericCellValue();
            sse.primerVol = nextrow.getCell(5).getNumericCellValue();
            //sse.totalVol = nextrow.getCell((short) 6).getNumericCellValue();
            //System.out.println(i + " value on sheet:" + sse.wellNum);
            //System.out.println(i + " value on sheet:" + sse.primerName);
            ssel.add(sse);
        }
        return ssel;
    }

    private boolean isValidPlateSetUpTemplate() {
        return this.connector.validateHeaderData(SequencePrepSheet.header, this.sheet);
    }

    public static final String getNextRunNumber(final ReadableVersion rv) {
        return "A" + Utils.getNextNumber(rv, "runnumber");
    }

    private void writeRunNumberAndDate(final String runNumber) {
        assert !Util.isEmpty(runNumber);
        final HSSFRow frow = this.sheet.getRow(0);
        final HSSFCell runNumCell = frow.getCell(0);
        final HSSFCell dateCell = frow.getCell(1);
        final HSSFRichTextString text = new HSSFRichTextString(runNumber);
        runNumCell.setCellValue(text);
        dateCell.setCellValue(new Date());
    }

    public String getRunNumber() {
        final HSSFRow frow = this.sheet.getRow(0);
        return frow.getCell(0).getRichStringCellValue().getString();
    }

    public byte[] writeShortSetupSheet(final List<SequencingOrder> soders, final HolderType htype,
        final String runNumber) throws IOException {

        //        final List<String> positions = HolderFactory.getPositions(htype);
        short rowC = 0;
        // Write RunNumber and Date at the top of the sheet
        this.writeRunNumberAndDate(runNumber);
        final String[][] data = new String[96][2];
        for (final SequencingOrder so : soders) {
            assert (so != null && so.getExperiments() != null) : "Empty sequencing order!";

            for (final Experiment exp : so.getExperiments()) {
                if (rowC > 95) {
                    throw new RuntimeException("Cannot fit the data in the givemn array!");
                }
                data[rowC][0] = Utils.getParameterValue(exp, SequencingOrder.TemplateName);
                data[rowC][1] = Utils.getParameterValue(exp, SequencingOrder.primerName);
                rowC++;
            }
        }
        this.connector.update(data, this.sheet, 3, (short) 1);
        return this.connector.write(this.workbook);
    }

    /*
     * Premix   Template    Primer  Water   Total Vol.
     *   14.0    2.0           2.0              20.0
     *
     */
    public byte[] writeShortSetupSheet(final ArrayList<Experiment> experiments, final HolderType htype,
        final String runNumber, final DefaultsHelper defaults) throws IOException {

        // final List<String> positions = HolderFactory.getPositions(htype);
        short rowC = -1;
        short notEmptyRowC = 0;
        // Write RunNumber and Date at the top of the sheet
        this.writeRunNumberAndDate(runNumber);

        final String[][] data = new String[this.getRequiredSize(experiments)][8];
        final ExcelConnector.DataType[] types =
            new ExcelConnector.DataType[] { ExcelConnector.DataType.STRING, ExcelConnector.DataType.STRING,
                ExcelConnector.DataType.STRING, ExcelConnector.DataType.NUMERIC,
                ExcelConnector.DataType.NUMERIC, ExcelConnector.DataType.NUMERIC,
                ExcelConnector.DataType.NUMERIC, ExcelConnector.DataType.NUMERIC };

        for (final Experiment exp : experiments) {
            if (rowC > 95) {
                throw new RuntimeException("Cannot fit the data in the array given !");
            }
            rowC++;
            if (exp == null) {
                continue;
            }
            defaults.setExperiment(exp);

            final String template = Utils.getParameterValue(exp, SequencingOrder.TemplateName);
            final String primer = Utils.getParameterValue(exp, SequencingOrder.primerName);

            data[notEmptyRowC][0] = PlatePlanner.positions.get(rowC);
            data[notEmptyRowC][1] = template;
            data[notEmptyRowC][2] = primer;
            data[notEmptyRowC][3] = Integer.toString(defaults.getPremixVol());
            data[notEmptyRowC][4] = Integer.toString(defaults.getTemplateVol());
            data[notEmptyRowC][5] = Integer.toString(defaults.getPrimerVol());
            data[notEmptyRowC][6] = ""; // empty column
            data[notEmptyRowC][7] = Integer.toString(defaults.getTotalVol());
            notEmptyRowC++;
        }

        this.connector.update(data, types, this.sheet, 3, (short) 0);
        return this.connector.write(this.workbook);
    }

    int getRequiredSize(final List<Experiment> experiments) {
        int counter = 0;
        for (final Experiment exp : experiments) {
            if (exp != null) {
                counter++;
            }
        }
        return counter;
    }
    /*
    public static void main(final String[] args) {

        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);

        try {
            final HolderType holderType =
                version.findFirst(HolderType.class, AbstractHolderType.PROP_NAME, "96 deep well");

            final SequencePrepSheet sps = new SequencePrepSheet(SequencePrepSheet.TemplateType.manual);
            // Populate OLIGO_ID and SEQUENCE
            final HSSFRow row = sps.connector.getRow("Well", sps.sheet);
            final SequencingOrder so = new SequencingOrder("SO_1", version);
            final SequencingOrder so1 = new SequencingOrder("SO_2", version);
            final SequencingOrder so2 = new SequencingOrder("SO_3", version);
            final SequencingOrder so4 = new SequencingOrder("SO_4", version);
            final SequencingOrder so5 = new SequencingOrder("SO_5", version);
            final ArrayList<SequencingOrder> sol = new ArrayList<SequencingOrder>();
            sol.add(so);
            sol.add(so1);
            sol.add(so2);
            sol.add(so4);
            sol.add(so5);
            final byte[] wb = sps.writeShortSetupSheet(sol, holderType, version);
            version.commit();

            final File file = new File("Test.xls");
            final FileOutputStream fs = new FileOutputStream(file);
            fs.write(wb);
            fs.close();

        } catch (final AbortedException e) {
            e.printStackTrace();

        } catch (final ConstraintException e) {
            e.printStackTrace();

        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort(); // not testing persistence here
            }
        }
    }
    */

} // class end
