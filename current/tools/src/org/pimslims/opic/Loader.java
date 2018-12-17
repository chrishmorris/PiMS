/**
 * tools org.pimslims.opic Loader.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.HolderType;

/**
 * Loader
 * 
 */
public class Loader {

    private static String HOLDERTYPE_FREEZER = "New Brunswick Innova Freezer";

    private static String HOLDERTYPE_SHELF = "New Brunswick Innova Freezer Shelf";

    private static String HOLDERTYPE_BOX = "81-place storage box";

    /**
     * Loader.load
     * 
     * @param book
     * @param version
     * @return
     * @throws ConstraintException
     * @throws IOException
     * @throws BiffException
     */
    public static Collection<Holder> load(Workbook book, WritableVersion version) throws ConstraintException,
        IOException {

        System.out.println("Loader.load(Workbook)");
        Collection<Holder> freezers = new HashSet<Holder>();

        Sheet[] sheets = book.getSheets();
        for (int i = 0; i < sheets.length; i++) {
            Sheet sheet = book.getSheet(i);
            freezers.add(load(sheet, version));
        }

        return freezers;
    }

    public static Collection<Holder> load(WritableWorkbook book, WritableVersion version)
        throws ConstraintException, IOException {

        Collection<Holder> freezers = new HashSet<Holder>();

        Sheet[] sheets = book.getSheets();
        for (int i = 0; i < sheets.length; i++) {
            Sheet sheet = book.getSheet(i);
            freezers.add(load(sheet, version));
        }

        return freezers;
    }

    /**
     * Loader.load
     * 
     * @param sheet
     * @param version
     * @return
     * @throws ConstraintException
     */
    public static Holder load(Sheet sheet, WritableVersion version) throws ConstraintException {

        System.out.println("Loader.load(Sheet)");
        String freezerName = sheet.getCell(0, 0).getContents().trim();
        Holder freezer = version.findFirst(Holder.class, Holder.PROP_NAME, freezerName);
        if (null == freezer) {
            System.out.println("Loader new freezer [" + freezerName + "]");
            HolderType holderType =
                version.findFirst(HolderType.class, HolderType.PROP_NAME, Loader.HOLDERTYPE_FREEZER);
            freezer = new Holder(version, freezerName);
            freezer.setHolderType(holderType);
        }

        String shelfName = sheet.getCell(2, 0).getContents().trim();
        if (null != shelfName && !"".equals(shelfName)) {
            processShelf(version, freezer, shelfName, sheet);
        }
        return freezer;
    }

    private static void processShelf(WritableVersion version, Holder freezer, String shelfName, Sheet sheet)
        throws ConstraintException {

        System.out.println("Loader new shelf [" + shelfName + "]");
        HolderType holderType =
            version.findFirst(HolderType.class, HolderType.PROP_NAME, Loader.HOLDERTYPE_SHELF);
        Holder shelf = new Holder(version, shelfName);
        shelf.setHolderType(holderType);
        shelf.setParentHolder(freezer);
        int sheetRow = 1;
        int rows = sheet.getRows();
        while (sheetRow < rows) {
            String contents = sheet.getCell(0, sheetRow).getContents().trim();
            if ("".equals(contents)) {
                sheetRow = sheetRow + 1; // skip empty row
            } else if (contents.startsWith("Row ")) {
                sheetRow = processShelfRow(version, shelf, sheet, sheetRow);
            } else {
                throw new IllegalArgumentException("Unexpected contents in left hand column: " + contents
                    + toString(sheetRow, 1));
            }
        }

    }

    private static final Pattern POSITION = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)$"); // e.g. 1.2.3 

    /**
     * Loader.processShelfRow
     * 
     * @param version
     * @param shelf
     * @param sheet
     * @param sheetRow the row with e.g. "Row 2" in it
     * @throws ConstraintException
     * @return the row number at which to resume processing
     */
    private static int processShelfRow(WritableVersion version, Holder shelf, Sheet sheet, int rowRow)
        throws ConstraintException {
        int sheetRow = rowRow + 1;
        if ("Position".equals(sheet.getCell(0, sheetRow).getContents().trim())) {
            sheetRow = sheetRow + 1; // Skip Position | Box description
        }
        int rows = sheet.getRows();
        while (sheetRow < rows) {
            // process a row consisting of position, box description, position, box description, ...
            int sheetColumn = 0;
            while (sheetColumn < sheet.getColumns()) {
                String position = sheet.getCell(sheetColumn, sheetRow).getContents().trim(); // e.g. 1.1.1
                String boxName = sheet.getCell(sheetColumn + 1, sheetRow).getContents().trim(); // e.g. Fab 159

                if (!"".equals(boxName)) {
                    Matcher m = POSITION.matcher(position);
                    if (!m.matches()) {
                        String location = toString(sheetRow, sheetColumn);
                        throw new IllegalArgumentException("Unexpected position: " + position + location);
                    }
                    HolderType holderType =
                        version.findFirst(HolderType.class, HolderType.PROP_NAME, Loader.HOLDERTYPE_BOX);
                    Holder box = new Holder(version, getUniqueName(version, boxName));
                    System.out.println("Loader new box [" + box.get_Name() + "]");
                    shelf.addSubHolder(box);
                    box.setHolderType(holderType);
                    box.setRowPosition(Integer.parseInt(m.group(1)));
                    box.setColPosition(Integer.parseInt(m.group(2)));
                    box.setSubPosition(Integer.parseInt(m.group(3)));
                }
                sheetColumn = sheetColumn + 2;
            }
            sheetRow = sheetRow + 1;
            String contents = "";
            if (sheetRow < rows) {
                contents = sheet.getCell(0, sheetRow).getContents().trim();
            }
            if ("".equals(contents)) {
                break; // blank row in spreadsheet, prepare for details of next shelf row
            }
        }
        return sheetRow;
    }

    private static String toString(int sheetRow, int sheetColumn) {
        String location = " in row: " + (sheetRow - 1) + " column:" + (sheetColumn - 1);
        return location;
    }

    private static String getUniqueName(ReadableVersion version, String holderName) {
        String pname = holderName;
        int i = 0;
        while (alreadyExists(version, pname, Holder.class)) {
            pname = nextName(holderName, ++i);
        }
        return pname;
    }

    public static <T extends LabBookEntry> boolean alreadyExists(final ReadableVersion version,
        final String name, final Class<T> clazz) {

        String hql = "select count(A) from " + clazz.getName() + " as A where name=:name";
        final org.hibernate.Query query = version.getSession().createQuery(hql);
        query.setString("name", name);
        List list = query.list();
        assert 1 == list.size();
        Object count = list.get(0);
        if (new Long(1L).equals(count)) {
            return true;
        }
        if (new Long(0L).equals(count)) {
            return false;
        }
        throw new AssertionError("Wrong number of: " + clazz.getName() + " called: " + name + ", found: "
            + count);

    }

    private static String nextName(String name, int i) {
        StringBuffer newName = new StringBuffer(name);
        newName.append("_");
        newName.append(new DecimalFormat("00").format(i));
        return newName.toString();
    }

    /**
     * Loader.load
     * 
     * @param is
     * @param version
     * @return
     * @return
     * @throws ConstraintException
     * @throws IOException
     * @throws BiffException
     */
    public static AbstractModelObject load(InputStream is, WritableVersion version) throws ConstraintException,
        IOException {
        try {
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            return load(sheet, version);

        } catch (BiffException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loader.load
     * 
     * @param is
     * @param version
     * @return
     * @throws ConstraintException
     * @throws IOException
     * @throws BiffException
     */
    public static Collection<Holder> loadAll(InputStream is, WritableVersion version)
        throws ConstraintException, IOException {

        System.out.println("Loader.loadAll");
        try {
            Workbook book = Workbook.getWorkbook(is);
            return load(book, version);

        } catch (BiffException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<String> spreadsheets = new ArrayList<String>();
        String notebook = null;

        for (int i = 0; i < args.length; i++) {
            System.out.println("arg[" + i + "]: " + args[i] + " " + args[i + 1]);

            if (args[i].charAt(0) == '-') {
                char c = args[i].charAt(1);

                switch (c) {

                    case 'l':
                        i++;
                        notebook = new String(args[i]);
                        break;

                    case 's':
                        i++;
                        spreadsheets.add(args[i]);
                        break;

                    default:
                        break;

                }
            }
        }

        if (spreadsheets.isEmpty() || null == notebook) {
            System.err.println("use is Loader -l notebook -s spreadsheet");
            System.exit(1);
        }

        final AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        version.setDefaultOwner(notebook);

        try {
            for (String spreadsheet : spreadsheets) {
                InputStream is = new FileInputStream(new File(spreadsheet));
                Loader.loadAll(is, version);
                version.commit();
                System.out.println("Loaded: " + spreadsheet);
            }

        } catch (ConstraintException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } catch (AbortedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (!version.isCompleted())
                version.abort();
        }

        System.out.println("Loader completed");
    }

}
