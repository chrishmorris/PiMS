package org.pimslims.presentation.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.util.PDFTextStripper;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Pdf DocumentTest.java
 * 
 * @author cm65
 * @date 28 Sep 2012
 * 
 *       Protein Information Management System
 * @version: 4.3
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */

/**
 * DocumentTest
 * 
 */
public class DocumentTest extends TestCase {

    private static final float MAX_VALUE = (PDPage.PAGE_SIZE_A4.getUpperRightX() - TableI.COLUMN_1)
        / PdfDocument.FONT_SIZE;

    /**
     * TEN_EMS String
     */
    private static final String TEN_EMS = "mmmmmmmmmm";

    String text = null;

    private final ContentHandler contextHandler = new ContentHandler() {

        @Override
        public void characters(final char[] arg0, final int arg1, final int arg2) throws SAXException {
            // System.out.println(arg0);
            // text.append(arg0);
        }

        @Override
        public void endDocument() throws SAXException {
        }

        @Override
        public void endElement(final String arg0, final String name, final String arg2) throws SAXException {
            System.out.println("//" + name);
        }

        @Override
        public void endPrefixMapping(final String arg0) throws SAXException {
        }

        @Override
        public void ignorableWhitespace(final char[] arg0, final int arg1, final int arg2)
            throws SAXException {

        }

        @Override
        public void processingInstruction(final String arg0, final String arg1) throws SAXException {
        }

        @Override
        public void setDocumentLocator(final Locator arg0) {
        }

        @Override
        public void skippedEntity(final String arg0) throws SAXException {
        }

        @Override
        public void startDocument() throws SAXException {
        }

        @Override
        public void startElement(final String namespace, final String name, final String arg2,
            final Attributes attributes) throws SAXException {
            // could System.out.println(namespace + ":" + name + ":" + arg2);
            final String[] strings;
            for (int i = 0; i < attributes.getLength(); i++) {
                System.out.println(name + " " + attributes.getLocalName(i) + "=" + attributes.getValue(i));
            }
        }

        @Override
        public void startPrefixMapping(final String arg0, final String arg1) throws SAXException {
        }
    };

    public void XtestLog() throws ClassNotFoundException {
        final Class c = Class.forName("org.apache.commons.logging.impl.SLF4JLocationAwareLog");

    }

    public void test() throws IOException, SAXException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final DocumentI document = new PdfDocument(os, "title", "footer");
        document.close();
        os.close();
        final byte[] byteArray = os.toByteArray();
        Assert.assertTrue(0 < byteArray.length);
        this.getText(new ByteArrayInputStream(byteArray));
        this.assertContains("title");
        this.assertContains("footer");
    }

    public void testImage() throws IOException, SAXException {
        final String fileName = "test.png";
        final ByteArrayOutputStream os = this.createDocumentWithImage(fileName);
        final PDDocument pdocument = PDDocument.load(new ByteArrayInputStream(os.toByteArray()));
        final Iterator iter = pdocument.getDocumentCatalog().getAllPages().iterator();
        Assert.assertTrue(iter.hasNext());
        while (iter.hasNext()) {
            final PDPage page = (PDPage) iter.next();
            final PDResources resources = page.getResources();
            Assert.assertEquals(1, resources.getImages().size());
        }
    }

    public void testPrintImage() throws MalformedURLException, IOException {
        final File file = new File("test.pdf");
        final FileOutputStream os = new FileOutputStream(file);
        doCreateDocumentWithImage("graph.png", os);
    }

    private ByteArrayOutputStream createDocumentWithImage(final String fileName) throws IOException,
        MalformedURLException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        doCreateDocumentWithImage(fileName, os);
        return os;
    }

    /**
     * DocumentTest.doCreateDocumentWithImage
     * 
     * @param fileName
     * @param os
     * @throws IOException
     * @throws MalformedURLException
     */
    private void doCreateDocumentWithImage(final String fileName, final OutputStream os) throws IOException,
        MalformedURLException {
        final DocumentI document = new PdfDocument(os, "title", "footer");
        final byte[] png = this.getPng(fileName);
        document.addImage(png);
        document.close();
        os.close();
    }

    public void test64BitPng() throws MalformedURLException, IOException {
        this.createDocumentWithImage("test64.png");
    }

    /**
     * Check we can load a tif. However, note that the standard is complex, may need other tests.
     * 
     * DocumentTest.testTif
     * 
     * @throws MalformedURLException
     * @throws IOException
     */

    public void testTif() throws MalformedURLException, IOException {
        this.createDocumentWithImage("test.tif");
    }

    public void testJpeg() throws MalformedURLException, IOException {
        this.createDocumentWithImage("test.jpg");
    }

    private byte[] getPng(final String fileName) throws IOException {
        final InputStream resource = this.getClass().getResourceAsStream(fileName);
        final byte[] png = new byte[200000];
        resource.read(png);
        return png;
    }

    public void testTable() throws IOException, SAXException { /*
                                                                * final File
                                                                * file = new
                                                                * File
                                                                * ("test.pdf");
                                                                * final
                                                                * FileOutputStream
                                                                * os = new
                                                                * FileOutputStream
                                                                * (file); //
                                                                */
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final DocumentI document = new PdfDocument(os, "title", "footer");
        TableI table = addTable(document, "table");
        document.close();
        os.close(); // /*
        this.getText(new ByteArrayInputStream(os.toByteArray()));
        this.assertContains("table");
        this.assertContains("key");
        this.assertContains("value");
        // */
        assertEquals(1, table.getRows());
    }

    /**
     * DocumentTest.addTable
     * 
     * @param document
     * @param title TODO
     * @return
     * @throws IOException
     */
    private TableI addTable(final DocumentI document, String title) throws IOException {
        final TableI table = document.createTable(title);

        final String key = "key";
        table.addKeyAndValue(key, "value " + title);
        document.addTable(table);
        return table;
    }

    public void testTableAtPageBreak() throws IOException {
        /* 
          final File file = new File("test.pdf"); final FileOutputStream os =
          new FileOutputStream(file); // 		 */
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final DocumentI document = new PdfDocument(os, "title", "footer");
        float top = ((PdfDocument) document).getY();
        addTable(document, "table");
        float height = top - ((PdfDocument) document).getY();
        int t = 0;
        while (t++ < PdfDocument.PAGE_HEIGHT / height) {
            addTable(document, "table" + t);
        }
        addTable(document, "last");
        assertTrue(0 < ((PdfDocument) document).getY());
        assertEquals(2, ((PdfDocument) document).getNumberOfPages());
        document.close();
        /* */
        this.getText(new ByteArrayInputStream(os.toByteArray()));
        this.assertContains("table");
        this.assertContains("key");
        this.assertContains("value");
        this.assertContains("last");
        /*			 */
        os.close();
    }

    public void testInsertNewlinesForSpaces() throws IOException {
        String tooWide = makeWideString(30f, "   ");
        doTestInsert(tooWide);
    }

    public void testInsertNewlinesForHyphens() throws IOException {
        String tooWide = makeWideString(30f, "-");
        doTestInsert(tooWide);
    }

    public void testInsertNewlinesIfLong() throws IOException {
        String tooWide = makeWideString(30f, "");
        String inserted = DrawnTable.insertNewlines(tooWide + " " + tooWide, 22);
        System.out.println(inserted);
        Matcher m = Pattern.compile("\n").matcher(inserted);
        assertTrue(m.find());
        assertTrue(m.find());
        assertTrue(m.find());
        assertTrue(m.find());
        assertFalse(m.find()); // should be four lines
    }

    public void testDontBreakAfterEverySpace() throws IOException {
        String tooWide = makeWideString(30f, " ");
        String inserted = DrawnTable.insertNewlines(tooWide + " " + tooWide, 22);
        System.out.println(inserted);
        Matcher m = Pattern.compile("\n").matcher(inserted);
        assertTrue(m.find());
        assertTrue(m.find());
        assertTrue(m.find());
        assertFalse(m.find()); // should be three lines, with a newline after each
    }

    /**
     * DocumentTest.doTestInsert
     * 
     * @param tooWide
     * @throws IOException
     */
    private void doTestInsert(String tooWide) throws IOException {
        String inserted = DrawnTable.insertNewlines(tooWide, 22);
        System.out.println(inserted);
        Matcher m = Pattern.compile("\n").matcher(inserted);
        assertTrue(m.find());
        assertTrue(2 * TEN_EMS.length() < m.start());
        assertTrue(3 * TEN_EMS.length() > m.start());
        assertTrue(m.find());
        assertFalse(m.find()); // should be two
    }

    public void testLongValue() throws IOException, SAXException {
        /*
         * final File file = new File("test.pdf"); final FileOutputStream os =
         * new FileOutputStream(file); // //
         */final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final DocumentI document = new PdfDocument(os, "title", "footer");
        final TableI table = document.createTable("table");

        final float row0 = ((DelayedTable) table).getY();
        table.addKeyAndValue("short", "value");
        final float row1 = ((DelayedTable) table).getY();
        table.addKeyAndValue("long", "a" + "\n" + "b");
        final float row2 = ((DelayedTable) table).getY();
        // assert it is occupying double height
        Assert.assertTrue(1.5 * (row0 - row1) < row1 - row2);

        // make a value that does not fit in second column
        String tooWide = makeWideString(60, " ");
        table.addKeyAndValue("very long", tooWide);
        // assert that it is occupying double height
        final float row3 = ((DelayedTable) table).getY();
        Assert.assertTrue(1.5 * (row0 - row1) < row2 - row3);

        document.addTable(table);
        document.close();
        os.close();
    }

    public void testLongTableTitle() throws IOException, SAXException {
        /*  
        final File file = new File("test.pdf");
        final FileOutputStream os = new FileOutputStream(file);
        // */final ByteArrayOutputStream os = new ByteArrayOutputStream();

        final DocumentI document = new PdfDocument(os, "title", "footer");
        float before = ((PdfDocument) document).getY();
        TableI table1 = document.createTable("table");
        table1.addKeyAndValue("short", "value");
        document.addTable(table1);
        float between = ((PdfDocument) document).getY();
        TableI table2 = document.createTable(makeWideString(100, " "));
        table2.addKeyAndValue("short", "value");
        document.addTable(table2);
        float after = ((PdfDocument) document).getY();

        // assert title is occupying double height
        Assert.assertTrue("" + (before - between) + " " + (between - after),
            1.3 * (before - between) < between - after);

        document.close();
        os.close();
    }

    /**
     * DocumentTest.makeWideString
     * 
     * @param ems
     * @param separator
     * @return
     */
    private String makeWideString(final float ems, String separator) {
        String tooWide = "";
        while (10 * (tooWide.length() / 11) < ems) {
            tooWide += DocumentTest.TEN_EMS + separator;
        }
        return tooWide;
    }

    public void TODOtestTableWithImage() throws IOException, SAXException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.makeDocumentWithImageInTable(os);
        final PDDocument pdocument = PDDocument.load(new ByteArrayInputStream(os.toByteArray()));
        final Iterator iter = pdocument.getDocumentCatalog().getAllPages().iterator();
        int images = 0;
        while (iter.hasNext()) {
            final PDPage page = (PDPage) iter.next();
            final PDResources resources = page.getResources();
            images += resources.getImages().size();
        }
        Assert.assertEquals(3, images);
    }

    private TableI makeDocumentWithImageInTable(final OutputStream os) throws IOException {
        final DocumentI document = new PdfDocument(os, "title", "footer");
        final TableI table = document.createTable("table");

        table.addKeyAndValue("key", "value");
        table.addImage(this.getPng("test.png"), "image/png", "screenshot");
        table.addImage(this.getPng("test.tif"), "image/tiff", "vase");
        table.addImage(this.getPng("test.jpg"), "image/jpg", "cartoon");
        document.addTable(table);
        document.close();
        os.close();
        return table;
    }

    public void testPrintTableWithImage() throws IOException, SAXException {
        final File file = new File("test.pdf");
        final FileOutputStream os = new FileOutputStream(file);
        TableI table = this.makeDocumentWithImageInTable(os);
        System.out.println("Saved: " + file.getAbsolutePath());
        assertEquals(4, table.getRows());
    }

    public void save() throws IOException {
        final File file = new File("test.pdf");
        final FileOutputStream os = new FileOutputStream(file);
        final DocumentI document = new PdfDocument(os, "title", "footer");
        final byte[] png = this.getPng("test.png");
        document.addImage(png);

        final TableI table = document.createTable("table");
        table.addKeyAndValue("key", "value");
        table.addImage(this.getPng("test.tif"), "image/tiff", "imageTitle");
        document.addTable(table);

        final TableI table2 = document.createTable("table2");
        table.addKeyAndValue("key3", "value3");
        document.addTable(table2);

        document.close();
        os.close();
        System.out.println("Saved: " + file.getAbsolutePath());
    }

    /**
     * DocumentTest.assertContains
     * 
     * @param string
     */
    private void assertContains(final String string) {
        Assert.assertTrue(this.text, this.text.toString().contains(string));
    }

    private void getText(final InputStream is) throws IOException {
        final PDDocument doc = PDDocument.load(is);
        this.text = new PDFTextStripper().getText(doc);
        Assert.assertNotNull(this.text);
    }

    //
    public void xtestCoordinates() {
        System.out.println("left: " + PDPage.PAGE_SIZE_A4.getLowerLeftX() + " right: "
            + PDPage.PAGE_SIZE_A4.getUpperRightX() + " top: " + PDPage.PAGE_SIZE_A4.getUpperRightY()
            + " bottom: " + PDPage.PAGE_SIZE_A4.getLowerLeftY());
    }
}
