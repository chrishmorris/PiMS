/**
 * V4_3-web org.pimslims.spreadsheet SpreadSheet.java
 * 
 * @author cm65
 * @date 28 Nov 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * SpreadSheet
 * 
 */
public class SpreadSheet {

    final Workbook book;

    /**
     * Constructor for SpreadSheet
     * 
     * @param is
     */
    public SpreadSheet(final InputStream is) throws InvalidFormatException, IOException {
        this(is, new String[][] {});
    }

    /**
     * Constructor for SpreadSheet This one is just for testing
     */
    SpreadSheet() throws InvalidFormatException, IOException {
        this(null, new String[][] {});
    }

    /**
     * Constructor for SpreadSheet
     * 
     * @param strings
     */
    public SpreadSheet(final String[][] values) throws InvalidFormatException, IOException {
        this(null, values);
    }

    /**
     * Constructor for SpreadSheet
     * 
     * @param template
     * @param strings
     */
    public SpreadSheet(final InputStream template, final Object[][] values) throws InvalidFormatException,
        IOException {
        super();
        if (null == template) {
            this.book = new XSSFWorkbook();
            this.book.createSheet();
        } else {
            this.book = WorkbookFactory.create(template);
        }
        final Sheet sheet = this.book.getSheetAt(0);
        addCells(values, sheet);
    }

	private void addCells(final Object[][] values, final Sheet sheet) {
		for (int rowNum = 0; rowNum < values.length; rowNum++) {
            final Row row = sheet.createRow(rowNum);
            for (int colNum = 0; colNum < values[rowNum].length; colNum++) {
                Object object = values[rowNum][colNum];
                if (null==object) {
                	row.createCell(colNum).setCellType(Cell.CELL_TYPE_BLANK);
                	// or make no cell at all
                } else if (object instanceof Calendar) {
                	// does this count as numeric?
                	row.createCell(colNum).setCellValue((Calendar)object);
                } else if (object instanceof Boolean) {
				    row.createCell(colNum).setCellType(Cell.CELL_TYPE_BOOLEAN);
                	row.createCell(colNum).setCellValue((Boolean)object);
                } else if (object instanceof Number ) {
				    row.createCell(colNum).setCellType(Cell.CELL_TYPE_NUMERIC);
                	row.createCell(colNum).setCellValue(((Number)object).doubleValue());
                } else if (object instanceof Formula) {
				    row.createCell(colNum).setCellType(Cell.CELL_TYPE_FORMULA);
                	row.createCell(colNum).setCellFormula(((Formula)object).getCellFormula());
                } else {
				    row.createCell(colNum).setCellType(Cell.CELL_TYPE_STRING);
				    row.createCell(colNum).setCellValue(object.toString());
                }
            }
            // or if the whole row was blank cells, delete it
        }
	}
    

	public void fromArray(int i, Object[][] values) {
		this.addCells(values, this.book.getSheetAt(i));
		
	}

    /**
     * SpreadSheet.toArray
     * 
     * @return
     */
    public Object[][] toArray(final int sheetIndex) {
        final List<List<Object>> ret = new ArrayList<List<Object>>();
        final Sheet sheet = this.book.getSheetAt(sheetIndex);
        int columns = 0;
        int rows = 0;
        final List<Object> emptyRow = new ArrayList<Object>();
        for (short rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            final Row row = sheet.getRow(rowNum);
            boolean isEmpty = true;
            List<Object> rowList;
            if (null == row) {
                rowList = emptyRow;
            } else {
                rowList = new ArrayList<Object>();
                for (short column = 0; column < row.getLastCellNum(); column++) {
                    final Cell cell = row.getCell(column, Row.CREATE_NULL_AS_BLANK);
                    Object value;
                    final int cellType = cell.getCellType();
                    switch (cellType) {
                        case Cell.CELL_TYPE_FORMULA: 
                            value = new Formula(cell.getCellFormula());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue() ? "yes" : "no";
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            value = new Double(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_ERROR:
                        default:
                            throw new RuntimeException("Unexpected cell type: " + cellType + " at row: "
                                + cell.getRowIndex() + " column: " + cell.getColumnIndex());
                    }
					assert null != value;
                    rowList.add(value);
                    if (!"".equals(value)) {
                        isEmpty = false;
                        if (column >= columns) {
                            columns = column + 1;
                        }
                    }
                }
            }
            if (isEmpty) {
                rowList = emptyRow;
            }
            ret.add(rowList);
            if (rowList != emptyRow && rows <= rowNum) {
                rows = rowNum + 1;
            }
        }
        // now convert to String[][]
        final Object[][] array = new Object[rows][columns];
        for (int row = 0; row < rows; row++) {
            final List<Object> rowList = ret.get(row);
            for (int column = 0; column < columns; column++) {
                if (column < rowList.size()) {
                    array[row][column] = rowList.get(column);
                } else {
                    array[row][column] = "";
                }
            }
        }
        return array;
    }

    public int getNumberOfSheets() {
        return this.book.getNumberOfSheets();
    }

    public void write(final OutputStream arg0) throws IOException {
        this.book.write(arg0);
    }

	/**
	 * SpreadSheet.find searches for a string, delivering match nearest top left
	 * corner
	 * 
	 * @param sheet
	 *            the rectangular array to search
	 * @param toFind
	 *            search string
	 * @return null if no match, or coordinates of first match
	 */
	public static int[] find(Object[][] sheet, String toFind) {
		int taxi = 0; // distance from 0,0 in taxi driver metric, i.e.
						// row+column
		int rows = sheet.length;
		int columns = sheet[0].length;
		found: for (taxi = 0; taxi <= rows + columns - 2; taxi++) {
			// slide down a diagonal
			int row = 0;
			int column = taxi;
			if (column >= columns) {
				// can't start there
				row = rows - 1;
				column = taxi - row;
			}
			while (column >= 0) {
				if (toFind.equals(sheet[row][column])) {
					return new int[] { row, column };
				}
				row++;
				column--;
			}
		}
		// not found
		return null;

	}

	public void addSheet() {
		this.book.cloneSheet(0);	
		Integer sheets = this.getNumberOfSheets();
		this.book.setSheetName(sheets-1, sheets.toString());
	}
	public void cloneSheet(int sheet) {
		this.book.cloneSheet(sheet);		
	}


}
