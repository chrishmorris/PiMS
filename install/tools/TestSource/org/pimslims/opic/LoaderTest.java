/**
 * tools org.pimslims.opic LoaderTest.java
 * 
 * @author cm65
 * @date 21 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.opic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Set;

import junit.framework.TestCase;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;

/**
 * LoaderTest
 * 
 */
public class LoaderTest extends TestCase {

    /**
     * BOX_1_1_1 String
     */
    private static final String BOX = "RRL 10ul aliquots (Ios)";

    /**
     * SHELF String
     */
    private static final String SHELF = "Shelf II";

    /**
     * FREEZER_NAME String
     */
    private static final String FREEZER_NAME = "OPIC -80 storage    ";

    private final AbstractModel model;

    /**
     * Constructor for LoaderTest
     * 
     * @param name
     */
    public LoaderTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testNewFreezer() throws IOException, RowsExceededException, WriteException,
        ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME));
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            AbstractModelObject freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            //TODO test holder type is "Fridge"
        } finally {
            version.abort();
        }
    }

    public void testNewShelf() throws IOException, RowsExceededException, WriteException, ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            assertEquals(1, freezer.getSubHolders().size());
            assertEquals(SHELF, freezer.getSubHolders().iterator().next().getName());
            //TODO test holder type is "Shelf"
        } finally {
            version.abort();
        }
    }

    public void test1_1_1() throws IOException, RowsExceededException, WriteException, ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        sheet.addCell(new Label(0, 1, "Row 1"));
        sheet.addCell(new Label(0, 2, "Position\r\n"));
        sheet.addCell(new Label(1, 2, "Box description\r\n"));
        sheet.addCell(new Label(0, 3, "1.1.1"));
        sheet.addCell(new Label(1, 3, BOX + "\r\n"));

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(1, boxes.size());
            AbstractHolder box = boxes.iterator().next();
            assertEquals(BOX, box.getName());
            assertEquals(new Integer(1), box.getRowPosition());
            assertEquals(new Integer(1), box.getColPosition());
            assertEquals(new Integer(1), box.getSubPosition());
            //TODO test holder type is "Box"
        } finally {
            version.abort();
        }
    }

    public void test2_1_1() throws IOException, RowsExceededException, WriteException, ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        sheet.addCell(new Label(0, 1, "Row 1"));
        sheet.addCell(new Label(0, 2, "Position\r\n"));
        sheet.addCell(new Label(1, 2, "Box description\r\n"));
        sheet.addCell(new Label(0, 3, "1.1.1"));
        sheet.addCell(new Label(1, 3, ""));
        // row 4 of spreadsheet is blank
        sheet.addCell(new Label(0, 5, "Row 2"));
        sheet.addCell(new Label(0, 6, "Position\r\n"));
        sheet.addCell(new Label(1, 6, "Box description\r\n"));
        sheet.addCell(new Label(0, 7, "2.1.1"));
        sheet.addCell(new Label(1, 7, BOX + "\r\n"));

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(1, boxes.size());
            AbstractHolder box = boxes.iterator().next();
            assertEquals(BOX, box.getName());
            assertEquals(new Integer(2), box.getRowPosition());
        } finally {
            version.abort();
        }
    }

    public void test1_2_1() throws IOException, RowsExceededException, WriteException, ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        sheet.addCell(new Label(0, 1, "Row 1"));
        sheet.addCell(new Label(0, 2, "Position\r\n"));
        sheet.addCell(new Label(1, 2, "Box description\r\n"));
        sheet.addCell(new Label(0, 3, "1.1.1"));
        sheet.addCell(new Label(1, 3, ""));
        sheet.addCell(new Label(2, 3, "1.2.1"));
        sheet.addCell(new Label(3, 3, BOX + "\r\n"));

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(1, boxes.size());
            AbstractHolder box = boxes.iterator().next();
            assertEquals(BOX, box.getName());
            assertEquals(new Integer(2), box.getColPosition());
        } finally {
            version.abort();
        }
    }

    public void test1_1_2() throws IOException, RowsExceededException, WriteException, ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        assertNotNull(book);
        WritableSheet sheet = book.createSheet("sheet", 0);
        assertNotNull(sheet);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        sheet.addCell(new Label(0, 1, "Row 1"));
        sheet.addCell(new Label(0, 2, "Position\r\n"));
        sheet.addCell(new Label(1, 2, "Box description\r\n"));
        sheet.addCell(new Label(0, 3, "1.1.1"));
        sheet.addCell(new Label(1, 3, ""));
        sheet.addCell(new Label(0, 4, "1.1.2"));
        sheet.addCell(new Label(1, 4, BOX + "\r\n"));

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(1, boxes.size());
            AbstractHolder box = boxes.iterator().next();
            assertEquals(BOX, box.getName());
            assertEquals(new Integer(2), box.getSubPosition());
        } finally {
            version.abort();
        }
    }

    public void testThreeBoxes() throws IOException, RowsExceededException, WriteException,
        ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        WritableSheet sheet = getSheet(book, 0);

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Holder freezer = Loader.load(sheet, version);
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(3, boxes.size());
        } finally {
            version.abort();
        }
    }

    public void testLoadStream() throws IOException, RowsExceededException, WriteException,
        ConstraintException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        WritableSheet sheet = getSheet(book, 0);
        book.write();
        book.close();
        os.close();
        byte[] buffer = os.toByteArray();

        InputStream is = new ByteArrayInputStream(buffer);

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Workbook myBook = Workbook.getWorkbook(is);
            new Holder(version, FREEZER_NAME);
            Collection<Holder> freezers = Loader.load(myBook, version);
            Holder freezer = freezers.iterator().next();
            assertNotNull(freezer);
            AbstractHolder shelf = freezer.getSubHolders().iterator().next();
            assertEquals(SHELF, shelf.getName());
            Set<AbstractHolder> boxes = shelf.getSubHolders();
            assertEquals(3, boxes.size());
        } catch (BiffException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            version.abort();
        }
    }

    private WritableSheet getSheet(jxl.write.WritableWorkbook book, int sheetNumber) throws WriteException,
        RowsExceededException {
        WritableSheet sheet = book.createSheet("sheet", sheetNumber);

        sheet.addCell(new Label(0, 0, FREEZER_NAME + "\r\n"));
        sheet.addCell(new Label(1, 0, SHELF));
        sheet.addCell(new Label(0, 1, "Row 1"));
        sheet.addCell(new Label(0, 2, "Position\r\n"));
        sheet.addCell(new Label(1, 2, "Box description\r\n"));
        sheet.addCell(new Label(0, 3, "1.1.1"));
        sheet.addCell(new Label(2, 3, "1.2.1"));
        sheet.addCell(new Label(0, 4, "1.1.2"));
        sheet.addCell(new Label(2, 4, "1.2.2"));
        sheet.addCell(new Label(3, 4, BOX + "1.2.2"));
        sheet.addCell(new Label(0, 5, ""));
        sheet.addCell(new Label(0, 6, "Row 2"));
        sheet.addCell(new Label(0, 7, "Position\r\n"));
        sheet.addCell(new Label(1, 7, "Box description\r\n"));
        sheet.addCell(new Label(0, 8, "2.1.1"));
        sheet.addCell(new Label(2, 8, "2.2.1"));
        sheet.addCell(new Label(3, 8, BOX + "2.2.1"));
        sheet.addCell(new Label(0, 9, "2.1.2"));
        sheet.addCell(new Label(1, 9, BOX + "2.1.2"));
        return sheet;
    }

    //TODO test for duplicate box names
    public void testDuplicateBoxes() throws IOException, RowsExceededException, WriteException,
        ConstraintException {
        OutputStream os = new ByteArrayOutputStream();
        jxl.write.WritableWorkbook book = jxl.Workbook.createWorkbook(os);
        WritableSheet sheet = getSheet(book, 0);
        sheet.addCell(new Label(1, 8, BOX + "2.1.2"));

        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new Holder(version, FREEZER_NAME);
            Loader.load(sheet, version);
            AbstractModelObject box = version.findFirst(Holder.class, Holder.PROP_NAME, BOX + "2.1.2_01");
            assertNotNull(box);

        } finally {
            version.abort();
        }
    }

    //TODO test for access rights

}
