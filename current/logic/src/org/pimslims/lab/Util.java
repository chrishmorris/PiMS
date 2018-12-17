package org.pimslims.lab;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetamodelUtil;
import org.pimslims.metamodel.ModelObject;

/**
 * This is an utility class, it contains various often used static methods.
 * 
 * @author Petr Troshin <br>
 *         Created on 11-Apr-2005
 */
public final class Util {

    private Util() {/* empty */
    }

    @Deprecated
    // now in DM
    private static final int MAX_NAME_LENGTH = 80;

    /**
     * Obtain the object from the database if there is one otherwise create the object using parameters Can
     * only be used to obtain one instance of object from the database will throw an AssertionError otherwise
     * 
     * @param rw WritableVersion
     * @param metaClassName String the name of a metaclass
     * @param params Map contains parameters
     * @param dataOwner String the name of the LabNotebook if null use default one
     * @return ModelObject
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    public static <T extends ModelObject> T getOrCreate(final WritableVersion rw, final String metaClassName,
        final String dataOwner, final Map params) throws AccessException, ConstraintException {

        assert rw != null : "WritableVersion is null";
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("Parameters are empty! ");
        }

        final MetaClass metaclass = rw.getModel().getMetaClass(metaClassName);

        T mo = (T) rw.findFirst(metaclass.getJavaClass(), params);

        if (mo == null) {

            mo = (T) Util.create(rw, metaClassName, dataOwner, params);
        }
        //System.out.println("getOrCreate [" + mo.get_Hook() + ":" + mo.get_Name() + "]");
        return mo;
    }

    public static <T extends ModelObject> T getOrCreate(final WritableVersion rw, final Class<?> metaClass,
        final String dataOwner, final Map params) throws AccessException, ConstraintException,
        ClassNotFoundException {
        return (T) Util.getOrCreate(rw, metaClass.getName(), dataOwner, params);
    }

    /*
     * Use always default dataOwner
     */

    @Deprecated
    //TODO this may fail if user is not admin
    public static <T extends ModelObject> T getOrCreate(final WritableVersion rw, final Class<?> metaClass,
        final Map params) throws AccessException, ConstraintException {
        return (T) Util.getOrCreate(rw, metaClass.getName(), null, params);
    }

    /**
     * Create ModelObjects using the Map holding parameters
     * 
     * @param mObjname String name of the ModelObject
     * @param dataOwner LabNotebook as String If null use default one.
     * @param parameters - Map paraneterName->value
     * @return ModelObject
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    public static <T extends ModelObject> T create(final WritableVersion rw, final String className,
        final String dataOwner, final Map parameters) throws AccessException, ConstraintException {
        T mObj = null;
        Class javaClass;
        try {
            javaClass = Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("Can not create " + className, e);
        }
        if (!parameters.isEmpty() && Util.isParametersComplete(className, parameters, rw)) {
            assert rw != null : "Problems with the Writable version! ";
            if (dataOwner != null) {
                mObj = (T) rw.create(dataOwner, javaClass, parameters);
            } else {
                mObj = (T) rw.create(javaClass, parameters);
            }
        } else {
            throw new IllegalArgumentException("Could not create ModelObject " + className
                + "\n Parameters: " + parameters);
        }
        return mObj;
    }

    public static <T extends ModelObject> T create(final WritableVersion rw, final Class<?> mObjClass,
        final String dataOwner, final Map parameters) throws AccessException, ConstraintException,
        ClassNotFoundException {
        return (T) Util.create(rw, mObjClass.getName(), dataOwner, parameters);
    }

    /*
     * Always use default dataOwner
     */
    public static <T extends ModelObject> T create(final WritableVersion rw, final Class<?> mObjClass,
        final Map parameters) throws AccessException, ConstraintException {
        return (T) Util.create(rw, mObjClass.getName(), null, parameters);
    }

    /**
     * Extract sequence from FASTA format.
     * 
     * @param fasta
     * @return Protein sequence
     */
    public static String parseFasta(final String fasta) {
        String line = "", sequence = "";
        final StringTokenizer strt = new StringTokenizer(fasta, "\n");
        while (strt.hasMoreElements()) {
            line = (String) strt.nextElement();
            if (line.trim().startsWith(">")) {
                continue;
            }
            sequence += line;
        }
        return sequence;
    }

    public static String makeKey(final String id, String fullName) {
        fullName = fullName == null ? "" : " " + fullName;
        fullName = id + fullName;
        fullName = fullName.substring(0, fullName.length() > 254 ? 253 : fullName.length());
        return fullName;
    }

    /**
     * Check whether or not all the parameters are in place to create a ModelObject
     * 
     * @param className
     * @param parameters
     * @return
     */
    public static boolean isParametersComplete(final String className, final Map parameters,
        final WritableVersion rw) {
        final MetaClass meta = rw.getModel().getMetaClass(className);
        final HashMap attributes = new HashMap(meta.getAttributes());
        final ArrayList mattr = MetamodelUtil.getMandatoryAttr(MetamodelUtil.getSortedAttributes(attributes));

        for (final Iterator iter = mattr.iterator(); iter.hasNext();) {
            final MetaAttribute metaAttr = (MetaAttribute) iter.next();
            final Object o = parameters.get(metaAttr.getName());
            if (MetamodelUtil.isEmpty(o)) {
                // System.out.println("Mandatory attribute " +
                // metaAttr.getName() + " is empty");
                return false;
            }
        }
        final ArrayList mroles = MetamodelUtil.getMandatoryRoles(meta.getMetaRoles());
        for (final Iterator iterator = mroles.iterator(); iterator.hasNext();) {
            final String mrole = (String) iterator.next();
            final Object o = parameters.get(mrole);
            if (MetamodelUtil.isEmpty(o)) {
                // System.out.println("Mandatory role " + mrole + " is empty");
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether or not object is an instance of collection if yes return object, if not put it into the
     * ArrayList and return
     * 
     * @param o (Object, instance of a Collection or not )
     * @return Collection with object in it, or object itself if it is a collection.
     */
    public static Collection makeCollection(final Object o) {
        if (o == null) {
            throw new AssertionError("Please filter out null values prior using this method! ");
        }
        if (o instanceof Collection) {
            return (Collection) o;
        }
        final ArrayList ar = new ArrayList();
        ar.add(o);
        return ar;
    }

    /**
     * Check is the String field empty or null
     * 
     * @param value any String
     * @return true if the string is empty, false otherwise.
     */
    public static boolean isEmpty(final String value) {
        if (value != null && value.trim().length() != 0) {
            return false;
        }
        return true;

    }

    /**
     * FilenameFilter implementation Allow filter the directory contents by file extension.
     */
    public static class ExtensionFilter implements FilenameFilter {
        String extension = "";

        public ExtensionFilter(final String extension) {
            this.extension = extension;
        }

        public boolean accept(final File dir, final String name) {
            return (name.endsWith(this.extension));
        }
    }

    /**
     * Write any String data to file.
     * 
     * @param data
     * @param filePathandName File name with full path
     */
    public static void writeToFile(final String data, final String filePathandName) {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(filePathandName));
            bw.write(data);
            bw.flush();
            bw.close();
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Write any String data to file.
     * 
     * @param data
     * @param filePathandName File name with full path
     */
    public static void writeToFile(final byte[] data, final String filePathandName) {
        try {
            final BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(filePathandName));
            bw.write(data);
            bw.flush();
            bw.close();
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Get list of all files from the particular directory
     * 
     * @param directory name
     * @return Array of the files in the directory
     */
    public static File[] getAllFiles(final String directory) {
        final File ff = new File(directory);
        return ff.listFiles();
    }

    /**
     * Get list of files from the particular directory passed through FileFilter
     * 
     * @param directory name
     * @param fileFilter
     * @return Array of the files in the directory which satisfy the FileFilter criteria
     */
    public static File[] getFiles(final String directory, final FileFilter fileFilter) {
        final File ff = new File(directory);
        return ff.listFiles(fileFilter);
    }

    /**
     * Format sequence per 80 letter in one string. Without spaces.
     * 
     * @param longSequence
     * @return multiple line formated sequence, one line 80 letters length
     */
    public static String getFormatedSequence(final String longSequence) {
        final StringBuffer sb = new StringBuffer(longSequence);
        final String linesp = System.getProperty("line.separator");
        for (int i = 0; i < sb.length(); i = i + 82) {
            sb.insert(i, linesp);
        }
        return sb.toString();
    }

    /**
     * 
     * @param fileName
     * @return file extension
     */
    public static String getFileExtension(final String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).trim();
    }

    /**
     * Substring string to given length
     * 
     * @param str
     * @param length
     * @return
     */
    public static String substring(final String str, final int length) {
        if (str == null) {
            return null;
        }
        if (str.length() < length) {
            return str;
        }
        return str.substring(0, length - 1);
    }

    public static String readFileToString(final File file) throws IOException {
        String fileStr = "";
        String line = null;
        final BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            fileStr += line + "\n";
        }
        br.close();
        return fileStr;
    }

    public static String readFileToString(final org.pimslims.util.File file) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[256];
        int length;
        while (true) {
            length = file.read(buffer);
            if (0 >= length) {
                break;
            }
            out.write(buffer, 0, length);
        }
        return out.toString();
    }

    public static String readFileToString(final InputStream inStream) throws IOException {
        final byte[] bytes = new byte[inStream.available()];
        inStream.read(bytes);
        final ByteArrayOutputStream contentStr = new ByteArrayOutputStream();
        contentStr.write(bytes);
        return contentStr.toString();
    }

    /**
     * Unused
     * 
     * @param args
     */
    public static void main(final String[] args) {
        /* empty */
    }

    /**
     * This method to check whether the hook is valid It assumes that the hook structure is the following
     * ccp.api.Location.Location:2323 This would not work for packages less then 4 levels deep! TODO do we
     * really need this? If so, use a regexp.
     */
    public static boolean isHookValid(String hook) {
        if (hook == null) {
            return false;
        }
        if (hook.split("\\.").length < 4) {
            return false;
        }
        if (hook.trim().indexOf(':') < 0) {
            return false;
        }
        hook = hook.trim();
        final String id = hook.substring(hook.indexOf(':') + 1).trim();
        if (id.length() == 0) {
            return false;
        }
        for (final char c : id.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

/*
    public static List join(final ModelObject sample1, final ModelObject sample2) {
        final List samples = Util.makeList(sample1);
        samples.add(sample2);
        return samples;
    } */

    public static List<ModelObject> makeList(final ModelObject value) {
        final ArrayList<ModelObject> list = new ArrayList<ModelObject>(5);
        list.add(value);
        return list;
    }

    @Deprecated
    public static Map<String, Object> getNewMap() {
        final Map<String, Object> param = new HashMap<String, Object>();
        return param;
    }

    public static Calendar getCalendar(final Date date) {
        final Calendar ret = Calendar.getInstance();
        if (date != null) {
            ret.setTimeInMillis(date.getTime());
        }
        return ret;
    }

/* caused compilation error due to circular dependency - but unused anyway
    public static Calendar parseDate(final String date) {
        final Calendar ret = Calendar.getInstance();
        if (date != null) {
            ret.setTimeInMillis(AttributeToHTML.parseDate(date).getTime());
        }
        return ret;
    } */

    /**
     * @param oldName the name of the object being copied
     * @return
     */
    @Deprecated
    // use method in DM
    public static String nextName(final String tag, final String oldName) {
        String suffix = " " + tag + " 1";
        String prefix = oldName.trim();
        final Matcher m = Util.getCopiedPattern(tag).matcher(oldName);
        if (m.matches()) {
            // it's e.g. "T0017.FL-pcr copy 3"
            final String number = m.group(2);
            final int copy = Integer.parseInt(number) + 1;
            prefix = m.group(1);
            suffix = " " + tag + " " + copy;
        }
        if (prefix.length() + suffix.length() > Util.MAX_NAME_LENGTH) {
            prefix = prefix.substring(0, Util.MAX_NAME_LENGTH - suffix.length());
        }
        return prefix + suffix;
    }

    /**
     * @param pname
     * @return
     */
    public static String nextNumericName(final String pname) {

        final Pattern duplicated = Pattern.compile("(^.*?)_(\\d{1,9})$");

        final Matcher m = duplicated.matcher(pname);
        if (m.matches()) {
            final String number = m.group(2);
            final int copy = Integer.parseInt(number) + 1;
            return m.group(1) + "_" + copy;
        }
        return pname.trim() + "_1";
    }

    /**
     * Must be used in synchronized context is in multithreaded environment
     * 
     * @param dupl
     * @param attributes
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void duplicate(final ModelObject dupl, final Map<String, Object> attributes)
        throws AccessException, ConstraintException {
        dupl.set_Values(attributes);
    }

    /**
     * Make sure name is unique
     * 
     * @param protocols
     * @param pname
     * @param clazz
     * @return
     */
    public static String makeName(final WritableVersion version, final String pname, final Class clazz) {
        return version.getUniqueName(clazz, pname);
    }

    /**
     * @param tag a string to use making new names - must avoid clashes with records that cant be viewed, e.g.
     *            username
     * @return a pattern matching any name made by makeName
     */
    @Deprecated
    // now in DM
    public static Pattern getCopiedPattern(final String tag) {
        return Pattern.compile("(^.*?) " + tag + " (\\d{1,9})$");
    }
}
