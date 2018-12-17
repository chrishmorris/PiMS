/**
 * V2_2-pims-web org.pimslims.csv CsvPrinterTest.java
 * 
 * @author cm65
 * @date 29 May 2008
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2008 cm65   * 
    * 
 * . 
  */
package org.pimslims.spreadsheet;

import java.io.IOException;
import java.io.StringWriter;

import org.pimslims.spreadsheet.CsvPrinter;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * CsvPrinterTest
 * 
 */
public class CsvPrinterTest extends TestCase {

    /**
     * @param name
     */
    public CsvPrinterTest(final String name) {
        super(name);
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvPrinter#CsvPrinter(java.io.Writer)}.
     * 
     * @throws IOException
     */
    public final void testCsvPrinter() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.close();
        Assert.assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvPrinter#println(java.lang.String[])}.
     * 
     * @throws IOException
     */
    public final void testSimple() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.println(new String[] { "a", "b" });
        printer.close();
        Assert.assertEquals("a,b\n", writer.toString());
    }

    public final void testComma() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.println(new String[] { "a,a", "b" });
        printer.close();
        Assert.assertEquals("\"a,a\",b\n", writer.toString());
    }

    public final void testQuote() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.println(new String[] { "a\"a", "b" });
        printer.close();
        Assert.assertEquals("\"a\\\"a\",b\n", writer.toString());
    }

    public final void testEscape() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.println(new String[] { "a\\a", "b" });
        printer.close();
        Assert.assertEquals("a\\a,b\n", writer.toString());
    }

    public final void testCommaAndEscape() throws IOException {
        final StringWriter writer = new StringWriter();
        final CsvPrinter printer = new CsvPrinter(writer);
        printer.println(new String[] { "a,a\\a", "b" });
        printer.close();
        Assert.assertEquals("\"a,a\\\\a\",b\n", writer.toString());
    }
}
