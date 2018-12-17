/**
 * V4_3-web org.pimslims.spreadsheet SpreadsheetTest.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.xmlbeans.XmlException;

/**
 * SpreadsheetTest
 * See https://forge.epn-campus.eu/projects/ispyb3/repository/revisions/940/entry/ISPyB_project/client/src/uk/ac/ehtpx/xlsparser/ehtpx/eHTPXXLSParser.java
 */
public class SpreadsheetTest extends TestCase {

    private static final String UNIQUE = "spread" + System.currentTimeMillis();

    /**
     * Constructor for SpreadsheetTest
     * 
     * @param name
     */
    public SpreadsheetTest(final String name) {
        super(name);
    }

    /**
     * SpreadsheetTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * SpreadsheetTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

	public void testClassPath() throws ClassNotFoundException {
		Class.forName(XmlException.class.getName());
	}

    public void testToArray() {
        final Object[][] array = this.makeOneCellSpreadsheet().toArray(0);
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(1, array[0].length);
        Assert.assertEquals(SpreadsheetTest.UNIQUE, array[0][0]);
    }

    public void testRectangular() throws InvalidFormatException, IOException {
        final SpreadSheet sheet = new SpreadSheet();
        final Sheet poiSheet = sheet.book.getSheetAt(0);
        poiSheet.createRow(0);
        final Row row = poiSheet.createRow(1);
        final Cell cell = row.createCell(1);
        cell.setCellValue(SpreadsheetTest.UNIQUE);

        final Object[][] array = sheet.toArray(0);
        Assert.assertEquals(2, array.length);
        Assert.assertEquals(2, array[1].length);
        Assert.assertEquals(2, array[0].length);
        Assert.assertEquals("", array[0][0]);
    }

    public void testIgnoreEmptyColumn() throws InvalidFormatException, IOException {
        final SpreadSheet sheet = new SpreadSheet();
        final Sheet poiSheet = sheet.book.getSheetAt(0);
        final Row row = poiSheet.createRow(0);
        final Cell cell = row.createCell(0);
        cell.setCellValue(SpreadsheetTest.UNIQUE);
        row.createCell(1);

        final Object[][] array = sheet.toArray(0);
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(1, array[0].length);
    }

    public void testIgnoreEmptyRow() throws InvalidFormatException, IOException {
        final SpreadSheet sheet = new SpreadSheet();
        final Sheet poiSheet = sheet.book.getSheetAt(0);
        final Row row = poiSheet.createRow(0);
        final Cell cell = row.createCell(0);
        cell.setCellValue(SpreadsheetTest.UNIQUE);
        poiSheet.createRow(1).createCell(0);

        final Object[][] array = sheet.toArray(0);
        Assert.assertEquals(1, array.length);
    }

    private SpreadSheet makeOneCellSpreadsheet() {
        try {
            return new SpreadSheet(new String[][] { new String[] { SpreadsheetTest.UNIQUE } });
        } catch (final InvalidFormatException e) {
            // Cant happen
            throw new RuntimeException(e);
        } catch (final IOException e) {
            // cant happen
            throw new RuntimeException(e);
        }
    }

    public void testSpreadsheetInputStream() throws IOException, InvalidFormatException {
        final InputStream is = this.getClass().getResourceAsStream("Empty.xlsx");
        final SpreadSheet sheet = new SpreadSheet(is);
        Assert.assertEquals(3, sheet.getNumberOfSheets());
    }

    public void testSave() throws IOException, InvalidFormatException {
        final ByteArrayInputStream is = this.makeInputStream();
        final Object[][] array = new SpreadSheet(is).toArray(0);
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(1, array[0].length);
        Assert.assertEquals(SpreadsheetTest.UNIQUE, array[0][0]);
    }

    private ByteArrayInputStream makeInputStream() throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.makeOneCellSpreadsheet().write(os);
        final ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    public void testTemplate() throws IOException, InvalidFormatException {
        final InputStream template = this.makeInputStream();
        final SpreadSheet sheet =
            new SpreadSheet(template, new String[][] { new String[] { SpreadsheetTest.UNIQUE + "two" } });
        final Object[][] array = sheet.toArray(0);
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(1, array[0].length);
        Assert.assertEquals(SpreadsheetTest.UNIQUE + "two", array[0][0]);

    }

	public void testDiamondSpreadsheet() throws IOException,
			InvalidFormatException {
		final InputStream is = this.getClass().getResourceAsStream(
				"ispyb-template-DLS-1.0.6.xls");
		final SpreadSheet sheet = new SpreadSheet(is);
		Assert.assertEquals(14, sheet.getNumberOfSheets());
		// TODO what are the other two, Excel only shows 12

		Object[][] sheet0 = sheet.toArray(0);
		try {
			assertEquals("ehtpx5", sheet0[0][0]);
			assertEquals("Dewar", sheet0[2][2]);
			assertEquals("Puck", sheet0[1][2]);
			assertEquals("Template version", sheet0[0][2]);
			assertTrue(sheet0[0][3] instanceof Formula);
			assertEquals("RIGHT(A1,LEN(A1)-LEN(\"ehtpx\"))", ((Formula) sheet0[0][3]).getCellFormula());
		} catch (AssertionFailedError e) {
			for (int i = 0; i < sheet0.length; i++) {
				Object[] row = sheet0[i];
				for (int j = 0; j < row.length; j++) {
					System.out.print(row[j] + ",");
				}
				System.out.print("\n");
			}
			throw e;
		}
	}

	public void testFind() throws InvalidFormatException, IOException {
		int[] coordinates = SpreadSheet.find(
				new String[][] { new String[] { "" } }, UNIQUE);
		assertNull(coordinates);

		String[][] sheet2x3 = new String[][] { new String[] { "", "", UNIQUE },
				new String[] { "", "", UNIQUE } };
		coordinates = SpreadSheet.find(sheet2x3, UNIQUE);
		assertEquals(0, coordinates[0]);
		assertEquals(2, coordinates[1]);

		String[][] sheet3x2 = new String[][] { new String[] { "", "" },
				new String[] { "", "" }, new String[] { UNIQUE, UNIQUE } };
		coordinates = SpreadSheet.find(sheet3x2, UNIQUE);
		assertEquals(2, coordinates[0]);
		assertEquals(0, coordinates[1]);
	}

	public void testAddSheet() {
		SpreadSheet book = this.makeOneCellSpreadsheet();
		assertEquals(1, book.getNumberOfSheets());
		book.addSheet();
		assertEquals(2, book.getNumberOfSheets());
	}

	public void testFromArray() {
		SpreadSheet book = this.makeOneCellSpreadsheet();
		book.fromArray(0, new Object[][] {new Object[] {1f, null, new Formula("RIGHT(A1,LEN(A1)-LEN(\"ehtpx\"))")}});
		Object[][] array = book.toArray(0);
		assertEquals(1, array.length);
		assertEquals(3, array[0].length);
		assertTrue(array[0][2] instanceof Formula);
		assertEquals(1d, array[0][0]);
	}
	
	
}
