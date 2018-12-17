/**
 * pims-web org.pimslims.lab.file CaliperResultFile.java
 * 
 * @author Marc Savitsky
 * @date 12 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.JDOMException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ModelObject;

/**
 * CartesianFile
 * 
 */
public class CartesianFile implements IFileType {

    public static Map<String, String> inputMap;

    public static String fileName;

    public CartesianFile() {
        // Empty constructor
    }

    /**
     * IFileType.getTypeName
     * 
     * @see org.pimslims.lab.file.IFileType#getTypeName()
     */
    public String getTypeName() {
        return "Cartesian";
    }

    /**
     * 
     * Constructor for CartesianFile
     * 
     * @param name
     * @param inputStream
     * @throws JDOMException
     * @throws IOException
     */
    public CartesianFile(final String name, final InputStream inputStream) throws JDOMException, IOException {

        CartesianFile.fileName = name;
        CartesianFile.inputMap = new HashMap<String, String>();

        final BufferedReader d = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = d.readLine()) != null) {
            if (line.indexOf(':') > 0) {
                final String key = line.substring(0, line.indexOf(':')).trim();
                final String value = line.substring(line.indexOf(':') + 1).trim();
                CartesianFile.inputMap.put(key, value);
            }
        }
    }

    private static final Pattern FILE_CARTESIAN = Pattern.compile("^([0-9]+)-([0-9]+)-([0-9]+)_cart.txt$");

    /**
     * 
     * CaliperResultFile.isCaliperResultFile
     * 
     * @param name
     * @return
     */
    public boolean isOfThisType(final ReadableVersion version, final String name) {

        final Matcher m = CartesianFile.FILE_CARTESIAN.matcher(name);
        if (m.matches()) {
            return true;
        }
        return false;
    }

/*
    public ModelObject process() {

        System.out.println("CartesianFile.process [" + CartesianFile.fileName + "]");

        ModelObject group = null;
        final AbstractModel model = ModelImpl.getModel();
        final WritableVersion version = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        try {
            group = this.process(version);
            version.commit();

        } catch (final Exception e) {
            System.out.println("Exception caught [" + e.getLocalizedMessage() + "]");
            e.printStackTrace();

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        return group;
    } */

    public ModelObject process(final WritableVersion version) throws IFileException {
        return null;
    }

    public String getBarcode() {
        return CartesianFile.inputMap.get("Plate");
    }

    public String getProtein() {
        return CartesianFile.inputMap.get("Protein");
    }

    public Calendar getDate() {
        final Calendar calendar = new GregorianCalendar();
        final SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        Date date;
        try {
            date = format.parse(CartesianFile.inputMap.get("Date"));
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.setTime(date);
        return calendar;

    }

    public String getUser() {
        return CartesianFile.inputMap.get("Username");
    }

    public String getFilename() {
        return CartesianFile.inputMap.get("Filename");
    }

    public int getProtVol() {
        return new Integer(CartesianFile.inputMap.get("ProtVol")).intValue();
    }

    public int getScreenVol() {
        return new Integer(CartesianFile.inputMap.get("ScreenVol")).intValue();
    }

    /**
     * IFileType.getInstance
     * 
     * @see org.pimslims.lab.file.IFileType#getInstance(java.lang.String, java.io.InputStream)
     */
    public IFileType getInstance(final String name, final InputStream inputStream) throws JDOMException,
        IOException {
        System.out.println("CartesianFile.getInstance [" + name + "]");
        return new CartesianFile(name, inputStream);
    }

    /**
     * IFileType.processAsAttachment
     * 
     * @see org.pimslims.lab.file.IFileType#processAsAttachment(org.pimslims.metamodel.ModelObject)
     */
    public void processAsAttachment(final ModelObject object) {
        // TODO Auto-generated method stub

    }

}
