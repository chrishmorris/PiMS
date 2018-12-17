/**
 * V2_2-pims-web org.pimslims.utils ExcelConnector.java
 * 
 * @author pvt43
 * @date 29 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.pimslims.lab.Util;

/**
 * ExcelConnector
 * 
 */
public class ExcelConnectorImpl implements ExcelConnector {

    public HSSFRow getRow(final String value, final HSSFSheet sheet) {

        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            final HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            if (row.getPhysicalNumberOfCells() > 0) {
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    final HSSFCell cell = row.getCell(j);
                    // This is fine apparently some rows what looks fine may not have cells
                    if (cell == null) {
                        continue;
                    }
                    //System.out.println("A:" + cell.getRichStringCellValue().getString());
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING
                        && cell.getRichStringCellValue().getString().trim().equals(value)) {
                        return row;
                    }
                }
            }
        }

        // Get the row, offset from the first row of the sheet
        return null;
    }

    public HSSFWorkbook readTemplate(final String fileName) throws IOException {
        final InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
        assert (null != is) : fileName + " Excel Template is not found";

        final POIFSFileSystem fs = new POIFSFileSystem(is);
        return new HSSFWorkbook(fs);
    }

    public HSSFWorkbook readTemplate(final InputStream stream) throws IOException {
        if (null == stream) {
            throw new IOException("Specified stream is empty or closed");
        }
        final POIFSFileSystem fs = new POIFSFileSystem(stream);
        return new HSSFWorkbook(fs);
    }

    public HSSFSheet getFirstSheet(final HSSFWorkbook workbook) {
        return workbook.getSheetAt(0);
    }

    /**
     * headerNames names order must be the same as on the template, otherwise method return false
     * ExcelConnectorImpl.validateHeaderData
     * 
     * @param headerNames
     * @param sheet
     * @return
     */
    public boolean validateHeaderData(final String[] headerNames, final HSSFSheet sheet) {

        assert (sheet != null && headerNames != null && headerNames.length != 0) : "Method call parameters looks incorrect!";

        final HSSFRow headerRow = this.getRow(headerNames[0], sheet);
        if (headerRow == null) {
            System.out.println("Header row is not found by name: " + headerNames[0]);
            return false;
        }

        //    Find the first cell matching the header
        final Iterator<Cell> cells = headerRow.cellIterator();
        Cell firstHeaderCell = null;
        // Header cell may not be the first
        while (cells.hasNext()) {
            final Cell cell = cells.next();
            // Assume header cells are names therefore strings
            if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                continue;
            }
            if (!cell.getRichStringCellValue().getString().trim().equalsIgnoreCase(headerNames[0])) {
                continue;
            } else {
                firstHeaderCell = cell;
                break;
            }
        }
        // Check the rest of the names
        for (short i = 0; i < headerNames.length; i++) {
            final HSSFCell cell = headerRow.getCell((firstHeaderCell.getColumnIndex() + i));
            if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                throw new RuntimeException("Header Cell type must be String!");
            }
            if (!cell.getRichStringCellValue().getString().trim().equalsIgnoreCase(headerNames[i])) {
                System.out.println("Excel Template may be wrong! Header name is "
                    + cell.getRichStringCellValue().getString() + " where " + headerNames[i]
                    + " is expected!");
                return false;
            }
        }
        return true;
    }

    /**
     * ExcelConnector.getCellAt
     * 
     * @see org.pimslims.utils.ExcelConnector#getCellAt(int, int, org.apache.poi.hssf.usermodel.HSSFSheet)
     */
    public HSSFCell getCellAt(final int rowIdx, final int columnIdx, final HSSFSheet sheet) {
        if (sheet == null) {
            throw new RuntimeException("Spreadsheet is null!");
        }
        if (sheet.getLastRowNum() < rowIdx) {
            throw new RuntimeException("Shreadsheet does not have that many rows. Actual number of rows is "
                + sheet.getLastRowNum() + " but the row requested is " + rowIdx);
        }
        final HSSFRow row = sheet.getRow(rowIdx);
        if (row.getLastCellNum() < columnIdx) {
            throw new RuntimeException("Given row does not have that many cells. Actual number of cells is "
                + row.getLastCellNum() + " but the cell requested is " + columnIdx);
        }
        final HSSFCell cell = row.getCell(columnIdx);
        return cell;
    }

    public HSSFCell getNextCell(final HSSFCell cell) {
        return this.getCellAt(cell.getRowIndex(), cell.getColumnIndex() + 1, cell.getSheet());
    }

    /**
     * ExcelConnector.write
     * 
     * @see org.pimslims.utils.ExcelConnector#write(java.lang.String[][])
     */
    public void update(final String[][] data, final HSSFSheet sheet) throws IOException {
        this.update(data, sheet, 0, (short) 0);
    }

    /**
     * ExcelConnector.write Currently only String values can be recorded!
     * 
     * @throws IOException
     * 
     * @see org.pimslims.utils.ExcelConnector#write(java.lang.String[][], int, int)
     */
    public void update(final String[][] data, final HSSFSheet sheet, final int verticalPos,
        final int horizontalPos) throws IOException {
        if (sheet == null) {
            throw new IOException("Sheet is null! Please check your template!");
        }
        /*
        if (data.length > sheet.getPhysicalNumberOfRows()) {
            throw new IOException(
                "template sheet does not have enough rows to accomodate all the data! Actual number of rows are: "
                    + sheet.getPhysicalNumberOfRows() + " but " + data.length + " number of rows required!");
        }*/

        assert verticalPos >= 0 : "VerticalPos cannot be negative!";
        assert horizontalPos >= 0 : "horizontalPos cannot be negative!";
        /*
        if (sheet.getPhysicalNumberOfRows() < verticalPos) {
            throw new IOException("Template does not have enough rows to start from " + verticalPos
                + " Please adjust a template or a verticalPos parameter accordingly");
        }*/
        for (int i = 0; i < data.length; i++) {

            HSSFRow row = sheet.getRow(verticalPos + i);

            if (row == null) {
                row = sheet.createRow(verticalPos + i);
            }

            assert row != null;
            assert data[i] != null && data[i].length > 0;

            for (short j = 0; j < data[i].length; j++) {
                /*
                if (row.getPhysicalNumberOfCells() < horizontalPos) {
                    throw new IOException("Template does not have enough cells to start from "
                        + horizontalPos
                        + " Please adjust a template or a horizontalPos parameter accordingly");
                }

                if (row.getPhysicalNumberOfCells() < data[i].length) {
                    throw new IOException("template sheet does not have enough cells in the row number " + i
                        + "  to accomodate all the data! Actual number of cells are: "
                        + row.getPhysicalNumberOfCells() + " but " + data[i].length + " cells are required!");
                }
                */

                this.setCellValue(row, (short) (j + horizontalPos), data[i][j]);
            }
        }

    }

    /**
     * ExcelConnector.write Currently only String values can be recorded!
     * 
     * @throws IOException
     * 
     * @see org.pimslims.utils.ExcelConnector#write(java.lang.String[][], int, int)
     */
    public void update(final String[][] data, final DataType[] dataTypes, final HSSFSheet sheet,
        final int verticalPos, final int horizontalPos) throws IOException {
        if (sheet == null) {
            throw new IOException("Sheet is null! Please check your template!");
        }
        /*
        if (data.length > sheet.getPhysicalNumberOfRows()) {
            throw new IOException(
                "template sheet does not have enough rows to accomodate all the data! Actual number of rows are: "
                    + sheet.getPhysicalNumberOfRows() + " but " + data.length + " number of rows required!");
        }*/

        assert verticalPos >= 0 : "VerticalPos cannot be negative!";
        assert horizontalPos >= 0 : "horizontalPos cannot be negative!";
        /*
        if (sheet.getPhysicalNumberOfRows() < verticalPos) {
            throw new IOException("Template does not have enough rows to start from " + verticalPos
                + " Please adjust a template or a verticalPos parameter accordingly");
        }*/
        assert data != null && data[0] != null;
        assert dataTypes.length == data[0].length;

        for (int i = 0; i < data.length; i++) {

            HSSFRow row = sheet.getRow(verticalPos + i);

            if (row == null) {
                row = sheet.createRow(verticalPos + i);
            }

            assert row != null;
            assert data[i] != null && data[i].length > 0;

            for (short j = 0; j < data[i].length; j++) {
                /*
                if (row.getPhysicalNumberOfCells() < horizontalPos) {
                    throw new IOException("Template does not have enough cells to start from "
                        + horizontalPos
                        + " Please adjust a template or a horizontalPos parameter accordingly");
                }

                if (row.getPhysicalNumberOfCells() < data[i].length) {
                    throw new IOException("template sheet does not have enough cells in the row number " + i
                        + "  to accomodate all the data! Actual number of cells are: "
                        + row.getPhysicalNumberOfCells() + " but " + data[i].length + " cells are required!");
                }
                */
                final HSSFCell cell = row.getCell((j + horizontalPos), Row.CREATE_NULL_AS_BLANK);
                //System.out.println("CI " + cell.getColumnIndex() + " RI " + cell.getRowIndex());
                //System.out.println("CT " + cell.getCellType());
                //System.out.println("RN " + row.getRowNum());
                //System.out.println("POSI " + (j + horizontalPos));
                final String value = data[i][j];
                //System.out.println("DTA " + value);
                //System.out.println("HPOS " + dataTypes[j + horizontalPos]);
                switch (dataTypes[j + horizontalPos]) {
                    case NUMERIC:
                        if (!Util.isEmpty(value)) {
                            final Double numValue = Double.parseDouble(value);
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(numValue);
                        }
                        break;
                    case STRING:
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(new HSSFRichTextString(value));
                        break;
                    default:
                        // If no datatype provided assume its a String
                        System.out
                            .println("WARNING! No dataType provided for org.pimslims.utils.ExcelConnector#update method");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(new HSSFRichTextString(value));
                        break;
                }
            }
        }

    }

    /**
     * ExcelConnector.write Currently only String values can be recorded!
     * 
     * @throws IOException
     * 
     * @see org.pimslims.utils.ExcelConnector#write(java.lang.String[][], int, int)
     */
    public void update(final ExcelSheetData data, final HSSFSheet sheet, final int verticalPos,
        final int horizontalPos) throws IOException {
        if (sheet == null) {
            throw new IOException("Sheet is null! Please check your template!");
        }
        if (data.getRowNum() > sheet.getPhysicalNumberOfRows()) {
            throw new IOException(
                "template sheet does not have enough rows to accomodate all the data! Actual number of rows are: "
                    + sheet.getPhysicalNumberOfRows() + " but " + data.getRowNum()
                    + " number of rows required!");
        }
        assert verticalPos >= 0 : "VerticalPos cannot be negative!";
        assert horizontalPos >= 0 : "horizontalPos cannot be negative!";
        if (sheet.getPhysicalNumberOfRows() < verticalPos) {
            throw new IOException("Template does not have enough rows to start from " + verticalPos
                + " Please adjust a template or a verticalPos parameter accordingly");
        }
        for (int i = 0; i < data.getRowNum(); i++) {

            final HSSFRow row = sheet.getRow(verticalPos + i);
            assert row != null;
            assert data.getRow(i) != null && data.getRow(i).length > 0;

            for (short j = 0; j < data.getRow(i).length; j++) {
                if (row.getPhysicalNumberOfCells() < horizontalPos) {
                    throw new IOException("Template does not have enough cells to start from "
                        + horizontalPos
                        + " Please adjust a template or a horizontalPos parameter accordingly");
                }

                if (row.getPhysicalNumberOfCells() < data.getRow(i).length) {
                    throw new IOException("template sheet does not have enough cells in the row number " + i
                        + "  to accomodate all the data! Actual number of cells are: "
                        + row.getPhysicalNumberOfCells() + " but " + data.getRow(i).length
                        + " cells are required!");
                }
                this.setCellValue(row, (j + horizontalPos), data.getRow(i)[j]);
            }
        }

    }

    public void setCellValue(final HSSFRow row, final int cellNum, final String value) {
        final HSSFCell cell = row.getCell(cellNum, Row.CREATE_NULL_AS_BLANK);
        cell.setCellValue(value);
    }

    public void setCellValue(final HSSFRow row, final int cellNum, final ExcelSheetData.CellData cellValues) {
        final HSSFCell cell = row.getCell(cellNum);
        final Object value = cellValues.value;
        cell.setCellStyle(cellValues.style);
        if (value == null) {
            System.out.println("setCellValue() - the value to set is null");
            return;
        }
        boolean valueSet = false;
        if (value instanceof String) {
            if (cell.getCellType() != Cell.CELL_TYPE_STRING) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }
            this.setCellValue(row, cellNum, (String) value);
            valueSet = true;
        }
        if (value instanceof Double) {
            if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            }
            cell.setCellValue((Double) value);
            valueSet = true;
        }
        assert valueSet : "Value is not set! It is instance of " + value.getClass().getCanonicalName();
    }

    public byte[] write(final HSSFWorkbook workbook) throws IOException {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        return stream.toByteArray();
    }

    /**
     * ExcelConnectorImpl.setRowStyle
     * 
     * @see org.pimslims.utils.ExcelConnector#setRowStyle(org.apache.poi.hssf.usermodel.HSSFRow,
     *      org.apache.poi.hssf.usermodel.HSSFCellStyle)
     */
    public void setRowStyle(final HSSFRow row, final HSSFCellStyle style, int start, int end) {

        assert style != null;
        assert row != null;
        assert start >= 0;

        // Special case
        if (start == 0 && end == 0) {
            start = 0;
            end = row.getLastCellNum();
        }
        if (start >= end) {
            throw new RuntimeException(
                "Start position must be greater the end position! Current values are: start=" + start
                    + " end=" + end);
        }
        if (row.getPhysicalNumberOfCells() < end) {
            throw new RuntimeException("Number of cells is lesser than the end parameter of the method.");
        }

        for (int i = start; i < end; i++) {
            final HSSFCell cell = row.getCell(i);
            cell.setCellStyle(style);
        }
    }

    public void setRowStyle(final HSSFRow row, final HSSFCellStyle style) {
        this.setRowStyle(row, style, 0, (short) 0);
    }

    /**
     * ExcelConnector.getCell
     * 
     * @see org.pimslims.utils.ExcelConnector#getCell(java.lang.String,
     *      org.apache.poi.hssf.usermodel.HSSFSheet)
     */
    public HSSFCell getCell(final String value, final HSSFSheet sheet) {
        assert value != null && value.trim().length() > 0 : "Cannot find cell with an empty value!";
        final HSSFRow row = this.getRow(value, sheet);
        if (row == null) {
            return null;
        }
        return this.getCell(value, row);
    }

    /**
     * ExcelConnector.getCell
     * 
     * @see org.pimslims.utils.ExcelConnector#getCell(java.lang.String, org.apache.poi.hssf.usermodel.HSSFRow)
     */
    public HSSFCell getCell(final String value, final HSSFRow row) {
        assert value != null && value.trim().length() > 0 : "Cannot find cell with an empty value!";
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            final HSSFCell cell = row.getCell(i);
            if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING) {
                continue;
            }
            if (cell.getRichStringCellValue().getString().equals(value.trim())) {
                return cell;
            }
        }
        return null;
    }

}
