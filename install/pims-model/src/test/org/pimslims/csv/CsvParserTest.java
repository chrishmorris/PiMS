/**
 * V2_2-pims-web org.pimslims.csv LabeledCsvParserTest.java
 * 
 * @author cm65
 * @date 29 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 *
 * 
 *           .
 */
package org.pimslims.csv;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * LabeledCsvParserTest
 * 
 */
public class CsvParserTest extends TestCase {

    /**
     * @param name
     */
    public CsvParserTest(final String name) {
        super(name);
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvParser#LabeledCsvParser(org.pimslims.csv.CsvParser)}.
     * 
     * @throws IOException
     */
    public final void testLabeledCsvParserReader() throws IOException {
        final Reader reader = new StringReader("");
        new CsvParser(reader);
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvParser#getLabels()}.
     * 
     * @throws IOException
     */
    public final void testGetLabels() throws IOException {
        final Reader reader = new StringReader("a,\"b,b\",\"c\\\"c\"");
        final CsvParser parser = new CsvParser(reader);
        final String[] labels = parser.getLabels();
        Assert.assertEquals(3, labels.length);
        Assert.assertEquals("a", labels[0]);
        Assert.assertEquals("b,b", labels[1]);
        Assert.assertEquals("c\"c", labels[2]);
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvParser#getLine()}.
     * 
     * @throws IOException
     */
    public final void testGetLine() throws IOException {
        final Reader reader = new StringReader("a\n1");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        Assert.assertNotNull(parser.getLine());
        Assert.assertNull(parser.getLine());
    }

    /**
     * Test method for {@link org.pimslims.csv.CsvParser#getValueByLabel(java.lang.String)}.
     * 
     * @throws IOException
     */
    public final void testGetValueByLabel() throws IOException {
        final Reader reader = new StringReader("a,b,c\n1,2");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        parser.getLine();
        Assert.assertEquals("1", parser.getValueByLabel("a"));
        Assert.assertEquals("2", parser.getValueByLabel("b"));

        final Integer value = parser.getIntegerByLabel("b");
        Assert.assertEquals(2, value.intValue());
        Assert.assertNull(parser.getIntegerByLabel("c"));
        try {
            Assert.assertNull(parser.getValueByLabel("nonesuch"));
            Assert.fail("No such label");
        } catch (final IllegalArgumentException e) {
            // good;
        }
        Assert.assertNull(parser.getValueByLabel("c"));
    }

    public final void testComma() throws IOException {
        final Reader reader = new StringReader("a,b\n\"a,a\",b\\n");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        parser.getLine();
        Assert.assertEquals("a,a", parser.getValueByLabel("a"));
    }

    public final void testQuote() throws IOException {
        final Reader reader = new StringReader("a,b\n\"a\\\"a\",b\\n");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        parser.getLine();
        Assert.assertEquals("a\"a", parser.getValueByLabel("a"));
    }

    public final void testEscape() throws IOException {
        final Reader reader = new StringReader("a,b\na\\a,b\\n");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        parser.getLine();
        Assert.assertEquals("a\\a", parser.getValueByLabel("a"));
    }

    public final void testCommaAndEscape() throws IOException {
        final Reader reader = new StringReader("a,b\n\"a,a\\\\a\",b\\n");
        final CsvParser parser = new CsvParser(reader);
        parser.getLabels();
        parser.getLine();
        Assert.assertEquals("a,a\\a", parser.getValueByLabel("a"));
    }

/*
   


    public final void testCommaAndEscape() throws IOException {
        
       
        new String[] { "a,a\\a", "b" });
       
        "\"a,a\\\\a\",b\n", 
    }*/
}
