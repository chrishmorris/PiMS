/**
 * web org.pimslims.data XlsReader.java
 * 
 * @author bl67
 * @date 22 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
 * 
 *           Copyright (c) 2008 bl67
 * 
 * 
 */
package org.pimslims.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * XlsReader
 * 
 */
@Deprecated
public class XlsReader implements Iterator {
    {
        // Disable the jxl warnings
        java.lang.System.setProperty("jxl.nowarnings", "true");
    }

    private final List<String> fieldNames;

    private Map<String, Integer> fieldPositions;

    int maxCol = 0;

    int maxRow = 0;

    protected int currentRow = 1; //0 is the head

    Sheet sheet = null;

    Workbook workbook = null;

    private InputStream xlsFileInputStream;

    /**
     * @param filerName
     * @throws FileNotFoundException
     */
    public XlsReader(final String filerName, final List<String> fieldNames) throws FileNotFoundException {
        super();

        this.fieldNames = new LinkedList<String>(fieldNames);

        this.initProperty(filerName);
    }

    /**
     * @param filerName
     * @throws FileNotFoundException
     */
    public XlsReader(final InputStream inStream, final List<String> fieldNames) throws FileNotFoundException {
        super();
        this.fieldNames = new LinkedList<String>(fieldNames);

        this.initProperty(inStream);
    }

    /**
     * @param filerName
     * @throws FileNotFoundException
     */
    private void initProperty(final String filerName) throws FileNotFoundException {
        this.xlsFileInputStream = new FileInputStream(filerName);
        Workbook workbook;
        try {
            workbook = this.getWorkbook(this.xlsFileInputStream);
        } catch (final BiffException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.sheet = workbook.getSheet(0);
        this.maxRow = this.getRowNumber();
        this.maxCol = this.getColNumber();
        this.getFieldPositions();
    }

    /**
     * @param filerName
     * @throws FileNotFoundException
     */
    private void initProperty(final InputStream fileStream) throws FileNotFoundException {
        this.xlsFileInputStream = fileStream;
        Workbook workbook;
        try {
            workbook = this.getWorkbook(this.xlsFileInputStream);
        } catch (final BiffException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.sheet = workbook.getSheet(0);
        this.maxRow = this.getRowNumber();
        this.maxCol = this.getColNumber();
        this.getFieldPositions();
    }

    /**
     * get positions of all fieldnames and put into fieldPositions
     */
    private void getFieldPositions() {
        this.fieldPositions = new HashMap<String, Integer>();
        for (final String fieldName : this.fieldNames) {
            this.fieldPositions.put(fieldName, this.getFieldPosition(fieldName));
        }

    }

    /**
     * @return the position(column number) of fieldName
     */
    private Integer getFieldPosition(final String fieldName) {
        for (int col = 0; col < this.maxCol; col++) {
            if (this.getTextFromSheet(0, col).equalsIgnoreCase(fieldName)) {
                return new Integer(col);
            }
        }
        throw new RuntimeException("Can not find field:" + fieldName);
    }

    private int getColNumber() {
        final int sheetMaxRow = this.sheet.getRows();
        final int sheetMaxCol = this.sheet.getColumns();
        int realColNumber = sheetMaxCol;
        for (int i = sheetMaxCol - 1; i > 0; i--) {
            boolean empty = true;
            for (int j = 0; j < sheetMaxRow; j++) {
                final String contents = this.getTextFromSheet(j, i);
                if (contents != null && contents.length() != 0) {
                    empty = false;
                    break;
                }
            }
            if (!empty) {
                realColNumber = i + 1;
                break;
            }
        }
        return realColNumber;
    }

    private boolean isEmptyRow(final int rowNumber) {
        final int sheetMaxCol = this.sheet.getColumns();
        boolean empty = true;
        for (int j = 0; j < sheetMaxCol; j++) {
            final String contents = this.getTextFromSheet(rowNumber, j);
            if (contents != null && contents.length() != 0) {
                empty = false;
                break;
            }
        }
        //   if (empty)
        //       System.out.println("found empty row at:" + (rowNumber + 1));
        return empty;
    }

    private int getRowNumber() {
        final int sheetMaxRow = this.sheet.getRows();
        int realRowNumber = sheetMaxRow;
        for (int i = sheetMaxRow - 1; i > 0; i--) {
            if (!this.isEmptyRow(i)) {
                realRowNumber = i + 1;
                break;
            }
        }
        return realRowNumber;
    }

    private String getTextFromSheet(final int row, final int col) {
        final Cell cell = this.sheet.getCell(col, row);
        if (cell.getContents() != null && cell.getContents().trim().length() > 0) {
            final String returnValue = cell.getContents().trim();

            return returnValue;
        }
        return null;
    }

    private Workbook getWorkbook(final InputStream xlsFileInputStream) throws BiffException, IOException {
        if (this.workbook == null) {
            this.workbook = Workbook.getWorkbook(this.xlsFileInputStream);
        }
        return this.workbook;

    }

    /**
     * close opened file
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        this.xlsFileInputStream.close();
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (this.currentRow < this.maxRow) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see java.util.Iterator#next()
     */
    public Map<String, String> next() {
        this.currentRow += 1;
        if (this.isEmptyRow(this.currentRow - 1)) {
            return this.next();
        }

        return this.getValue();
    }

    public void iteratorReset() {
        this.currentRow = 1;
    }

    /**
     * @return
     */
    private Map<String, String> getValue() {
        final Map<String, String> returnValue = new HashMap<String, String>();
        for (final String fieldName : this.fieldNames) {
            returnValue.put(fieldName, this.getTextFromSheet(this.currentRow - 1, this.fieldPositions
                .get(fieldName)));
        }
        return returnValue;
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        // TODO Auto-generated method stub

    }
}
