/**
 * V2_2-pims-web org.pimslims.utils ExcelConnectorTester.java
 * 
 * @author Petr
 * @date 29 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Petr The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * ExcelConnectorTester
 * 
 */
public class ExcelConnectorTester extends TestCase {

    //private final AbstractModel model;

    private ExcelConnector conn;

    HSSFWorkbook workbook;

    /**
     * @param name
     */
    public ExcelConnectorTester(final String name) {
        super(name);
        //this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.conn = new ExcelConnectorImpl();

        try {
            final InputStream io = this.getClass().getResourceAsStream("TestPlateSetUp.xlt");
            Assert.assertNotNull(io);
            this.workbook = this.conn.readTemplate(io);
            io.close();
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.conn = null;
        this.workbook = null;
    }

    public void testTemplateLoading() {
        Assert.assertNotNull(this.workbook);
    }

    public void testGetRow() {
        Assert.assertNotNull(this.conn.getFirstSheet(this.workbook));
        Assert.assertEquals(this.workbook.getSheetAt(0), this.conn.getFirstSheet(this.workbook));
        Assert.assertNotNull(this.conn.getRow("Well", this.conn.getFirstSheet(this.workbook)));
        // Row numeration starts from 0
        Assert.assertEquals(2, this.conn.getRow("Well", this.conn.getFirstSheet(this.workbook)).getRowNum());
        Assert.assertEquals(2, this.conn.getRow("Total Vol.", this.conn.getFirstSheet(this.workbook))
            .getRowNum());
    }

    public void testValidateHeader() {

        Assert.assertTrue(this.conn.validateHeaderData(new String[] { "Well", "Sample Name", "Primer Name",
            "Premix", "Template", "Primer", "Water", "Total Vol." }, this.workbook.getSheetAt(0)));
        // Case insensitive
        Assert.assertTrue(this.conn.validateHeaderData(new String[] { "Well", "Sample Name", "Primer Name",
            "Premix", "Template", "Primer", "Water", "total Vol." }, this.workbook.getSheetAt(0)));
        // Header can start from any position, position before and after in the row will be ignored
        Assert.assertTrue(this.conn.validateHeaderData(new String[] { "Sample Name", "Primer Name", "Premix",
            "Template", "Primer", "Water" }, this.workbook.getSheetAt(0)));
    }

    public void testGetSetCellValueAt() {
        final HSSFCell cell = this.conn.getCellAt(8, (short) 2, this.workbook.getSheetAt(0));
        Assert.assertNotNull(cell);
        Assert.assertEquals("S1AAAF", cell.getRichStringCellValue().getString());
        final HSSFRow row = this.conn.getFirstSheet(this.workbook).getRow(8);
        Assert.assertNotNull(row);
        Assert.assertEquals(row.getCell(2).getRichStringCellValue().getString(), cell
            .getRichStringCellValue().getString());
        this.conn.setCellValue(row, 2, "Testing...");
        Assert.assertNotSame("S1AAAF", cell.getRichStringCellValue().getString());
        Assert.assertEquals("Testing...", cell.getRichStringCellValue().getString());
    }

    public void testUpdate() {
        final String[] data1Column = new String[] { "AA0", "BB0" };
        final String[] data2Column = new String[] { "AA1", "BB1" };
        final String[] data3Column = new String[] { "AA2", "BB2" };
        final String[][] data = new String[][] { data1Column, data2Column, data3Column };
        final HSSFSheet sheet = this.workbook.getSheetAt(0);

        final String v50 = this.conn.getCellAt(5, 0, sheet).getRichStringCellValue().getString();
        final String v51 = this.conn.getCellAt(5, 1, sheet).getRichStringCellValue().getString();
        final String v70 = this.conn.getCellAt(7, 0, sheet).getRichStringCellValue().getString();
        final String v71 = this.conn.getCellAt(7, 1, sheet).getRichStringCellValue().getString();
        try {
            this.conn.update(data, sheet, 5, (short) 0);
            Assert.assertEquals("AA0", this.conn.getCellAt(5, 0, sheet).getRichStringCellValue().getString());
            Assert.assertEquals("BB0", this.conn.getCellAt(5, 1, sheet).getRichStringCellValue().getString());
            Assert.assertEquals("AA2", this.conn.getCellAt(7, 0, sheet).getRichStringCellValue().getString());
            Assert.assertEquals("BB2", this.conn.getCellAt(7, 1, sheet).getRichStringCellValue().getString());

            Assert.assertNotSame(v50, this.conn.getCellAt(5, 0, sheet).getRichStringCellValue().getString());
            Assert.assertNotSame(v51, this.conn.getCellAt(5, 1, sheet).getRichStringCellValue().getString());
            Assert.assertNotSame(v70, this.conn.getCellAt(7, 0, sheet).getRichStringCellValue().getString());
            Assert.assertNotSame(v71, this.conn.getCellAt(7, 1, sheet).getRichStringCellValue().getString());

            final HSSFCell cell = this.conn.getCellAt(5, 1, this.workbook.getSheetAt(0));
            final HSSFCellStyle oldStyle = cell.getCellStyle();
            this.conn.update(ExcelSheetData.convert(data, oldStyle), sheet, 5, (short) 0);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }

    }

    public void testUpdateWithStyle() {
        final String[] data1Column = new String[] { "AA0", "BB0" };
        final String[] data2Column = new String[] { "AA1", "BB1" };
        final String[] data3Column = new String[] { "AA2", "BB2" };
        final String[][] data = new String[][] { data1Column, data2Column, data3Column };
        final HSSFSheet sheet = this.workbook.getSheetAt(0);
        final String v50 = this.conn.getCellAt(15, 0, sheet).getRichStringCellValue().getString();
        final String v51 = this.conn.getCellAt(15, 1, sheet).getRichStringCellValue().getString();
        final String v70 = this.conn.getCellAt(17, 0, sheet).getRichStringCellValue().getString();
        final String v71 = this.conn.getCellAt(17, 1, sheet).getRichStringCellValue().getString();

        try {

            final HSSFCell cell = this.conn.getCellAt(15, (short) 1, this.workbook.getSheetAt(0));
            final HSSFCellStyle oldStyle = cell.getCellStyle();
            final ExcelSheetData datasheet = ExcelSheetData.convert(data, oldStyle);
            this.conn.update(datasheet, sheet, 15, (short) 0);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        Assert.assertEquals("AA0", this.conn.getCellAt(15, 0, sheet).getRichStringCellValue().getString());
        Assert.assertEquals("BB0", this.conn.getCellAt(15, 1, sheet).getRichStringCellValue().getString());
        Assert.assertEquals("AA2", this.conn.getCellAt(17, 0, sheet).getRichStringCellValue().getString());
        Assert.assertEquals("BB2", this.conn.getCellAt(17, 1, sheet).getRichStringCellValue().getString());

        Assert.assertNotSame(v50, this.conn.getCellAt(15, 0, sheet).getRichStringCellValue().getString());
        Assert.assertNotSame(v51, this.conn.getCellAt(15, 1, sheet).getRichStringCellValue().getString());
        Assert.assertNotSame(v70, this.conn.getCellAt(17, 0, sheet).getRichStringCellValue().getString());
        Assert.assertNotSame(v71, this.conn.getCellAt(17, 1, sheet).getRichStringCellValue().getString());

    }

    public void testWrite() {
        byte[] workb = null;
        try {
            workb = this.conn.write(this.workbook);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }

        final String[] data1Column = new String[] { "AA0", "BB0" };
        final String[] data2Column = new String[] { "AA1", "BB1" };
        final String[] data3Column = new String[] { "AA2", "BB2" };
        final String[][] data = new String[][] { data1Column, data2Column, data3Column };
        final HSSFSheet sheet = this.workbook.getSheetAt(0);
        byte[] upWorkb = null;
        try {
            this.conn.update(data, sheet, 5, (short) 0);
            upWorkb = this.conn.write(this.workbook);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
        // Load template again
        byte[] templ = null;
        try {
            final InputStream io = this.getClass().getResourceAsStream("TestPlateSetUp.xlt");
            Assert.assertNotNull(io);
            final HSSFWorkbook template = this.conn.readTemplate(io);
            io.close();
            templ = this.conn.write(template);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
        // Template is not modified
        Assert.assertEquals(workb.length, templ.length);
        Assert.assertTrue(Arrays.equals(templ, workb));
        Assert.assertFalse(Arrays.equals(upWorkb, workb));
        Assert.assertNotSame(workb.length, upWorkb.length);
    }

    public void testSetStyle() {

        final HSSFCellStyle cstyle = this.workbook.createCellStyle();
        final HSSFCell cell = this.conn.getCellAt(5, (short) 1, this.workbook.getSheetAt(0));
        final HSSFCellStyle oldStyle = cell.getCellStyle();

        //arial is the default font        
        final HSSFFont f = this.workbook.createFont();
        //set font 1 to 12 point type
        f.setFontHeightInPoints((short) 22);
        // make it blue
        f.setColor(HSSFColor.AQUA.index);
        cstyle.setFont(f);
        cstyle.setFillForegroundColor(HSSFColor.AQUA.index);
        cell.setCellStyle(cstyle);

        this.conn.setRowStyle(this.conn.getRow("Well", this.conn.getFirstSheet(this.workbook)), cstyle);

        cstyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);

        final HSSFCell otherCell =
            this.conn.getRow("Total Vol.", this.conn.getFirstSheet(this.workbook)).getCell(7);
        Assert.assertEquals(cstyle, otherCell.getCellStyle());
        Assert.assertNotSame(oldStyle, otherCell.getCellStyle());

        /*
          final File file = new File("Test.xls");
          
              final FileOutputStream fs = new FileOutputStream(file);
              fs.write(this.conn.write(this.workbook));
              fs.close();

         
        */
    }

    public void testGetCell() {
        final HSSFSheet sheet = this.workbook.getSheetAt(0);
        final HSSFRow row = this.conn.getRow("Primer", sheet);
        final HSSFCell cell = this.conn.getCell("Primer", sheet);
        final HSSFCell sameCell = this.conn.getCell("Primer", row);
        Assert.assertNotNull(cell);
        Assert.assertEquals(5, cell.getColumnIndex());
        Assert.assertEquals(sameCell, cell);
    }

    public void testGetNextCell() {
        final HSSFSheet sheet = this.workbook.getSheetAt(0);
        final HSSFCell cell = this.conn.getCell("A01", sheet);
        final HSSFCell ncell = this.conn.getNextCell(cell);
        // A01  pGEM3Zf_T7P

        Assert.assertNotNull(cell);
        Assert.assertNotNull(ncell);
        Assert.assertEquals(0, cell.getColumnIndex());
        Assert.assertEquals(cell.getColumnIndex() + 1, ncell.getColumnIndex());
        Assert.assertEquals(cell.getRowIndex(), ncell.getRowIndex());
        Assert.assertEquals("pGEM3Zf_T7P", ncell.getRichStringCellValue().getString());
    }
}
