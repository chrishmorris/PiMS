/**
 * V2_2-pims-web org.pimslims.utils ExcelSheetData.java
 * 
 * @author pvt43
 * @date 3 Nov 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * ExcelSheetData
 * 
 */
public class ExcelSheetData {

    static class CellData {
        HSSFCellStyle style;

        Object value; // Can be either Double or String (also Date but this is not yet supported)

        CellData(final Object data, final HSSFCellStyle style) {
            this.value = data;
            this.style = style;
        }

        HSSFCellStyle getStyle() {
            return this.style;
        }
    }

    CellData[][] data;

    public ExcelSheetData(final int rowNum) {
        assert rowNum > 0;
        this.data = new CellData[rowNum][];
    }

    public static ExcelSheetData convert(final String[][] data, final HSSFCellStyle style) {
        final ExcelSheetData datasheet = new ExcelSheetData(data.length);
        datasheet.data = new CellData[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                datasheet.data[i][j] = new CellData(data[i][j], style);
            }
        }
        return datasheet;
    }

    public int getRowNum() {
        return this.data.length;
    }

    public CellData[] getRow(final int i) {
        assert i >= 0 && this.data.length > i;
        return this.data[i];
    }

    public void addDataRow(final int rowNum, final Object[] cells, final HSSFCellStyle style) {
        if (cells == null || cells.length == 0) {
            System.out.println("ExcelDataSheet.addDataRow is called with no data to add!");
            return;
        }
        assert rowNum >= 0 && this.data.length > rowNum;
        this.data[rowNum] = new CellData[cells.length];
        for (int i = 0; i < cells.length; i++) {
            this.data[rowNum][i] = new CellData(cells[i], style);
        }
    }
}
