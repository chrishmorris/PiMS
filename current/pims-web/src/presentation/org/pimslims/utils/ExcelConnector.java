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

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * ExcelConnector
 * 
 */
public interface ExcelConnector {

    HSSFRow getRow(String value, HSSFSheet sheet);

    HSSFWorkbook readTemplate(String fileName) throws IOException;

    HSSFWorkbook readTemplate(InputStream stream) throws IOException;

    HSSFSheet getFirstSheet(HSSFWorkbook workbook);

    boolean validateHeaderData(String[] headerNames, HSSFSheet sheet);

    void setCellValue(HSSFRow row, int cellNumer, String value);

    HSSFCell getCellAt(int verticalPos, int horizontalPos, HSSFSheet sheet);

    HSSFCell getCell(String value, HSSFSheet sheet);

    HSSFCell getCell(String value, HSSFRow row);

    void update(final String[][] data, HSSFSheet sheet) throws IOException;

    /**
     * Write two dimensional array to the sheet, starting from left top conner.
     * 
     * ExcelConnector.write
     * 
     * @param data
     * @param verticalPos
     * @param horizontalPos
     * @return
     */
    void update(final String[][] data, HSSFSheet sheet, int verticalPos, int horizontalPos)
        throws IOException;

    void update(final String[][] data, DataType[] dataType, HSSFSheet sheet, int verticalPos,
        int horizontalPos) throws IOException;

    void update(ExcelSheetData data, HSSFSheet sheet, int verticalPos, int horizontalPos) throws IOException;

    byte[] write(HSSFWorkbook workbook) throws IOException;

    void setRowStyle(HSSFRow row, HSSFCellStyle style, int start, int end);

    void setRowStyle(HSSFRow row, HSSFCellStyle style);

    HSSFCell getNextCell(HSSFCell cell);

    enum DataType {
        NUMERIC, STRING
    }
}
