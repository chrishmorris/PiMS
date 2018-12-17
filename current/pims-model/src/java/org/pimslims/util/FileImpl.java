/**
 * 
 */
package org.pimslims.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Annotation;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.LabBookEntry;

import sun.net.www.MimeEntry;

/**
 * Represents a file of data, e.g. a gel image.
 * 
 * @author cm65
 * 
 */
/**
 * FileImpl
 * 
 */
public class FileImpl implements File, Comparable {

    /**
	 * 
	 */
    private static final String ANNOTATIONS = LabBookEntry.PROP_ATTACHMENTS;

    /**
	 * 
	 */
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /**
     * the file name
     */
    private String name;

    /**
     * The data model Annotation that this wraps - null if a LabBookEntry has not yet been assigned
     */
    private org.pimslims.model.core.Annotation annotation = null;

    /**
     * For reading the file. Note that this is not thread safe - the file should be associated with a
     * particular transaction and therefore only with one thread
     */
    private java.io.InputStream in = null;

    private String mimeType = null;

    /*
     * Mime types table. At present, it's easiest to use the standard table. We
     * need a few extra types, so we add them at startup. If we need to make
     * this locally configurable, then we'll have to do something smarter. See
     * http://msdlocal.ebi.ac.uk/docs/mimetype.html and
     * http://www.ch.ic.ac.uk/chemime/
     * 
     * Note that Safari misbehaves for mime types that start with "text/"
     */
    private static final sun.net.www.MimeTable MIME_TABLE = sun.net.www.MimeTable.getDefaultTable();
    static {
        String mimetype = "message/rfc822";
        String extension = ".mht";
        addMimeTableEntry(mimetype, extension);
        MimeEntry entry = new sun.net.www.MimeEntry("chemical/seq-na-genbank");
        entry.setExtensions(".gb");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("chemical/x-swissprot");
        entry.setExtensions(".sw");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("application/x-chimerax");
        entry.setExtensions(".chimerax");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("application/msword");
        entry.setExtensions(".doc");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("application/x-align");
        entry.setExtensions(".aln");
        MIME_TABLE.add(entry);

        addMimeTableEntry("application/x-graphpad-prism-pzf", "pzf");
        addMimeTableEntry("application/x-graphpad-prism-pzfx", "pzfx");
        addMimeTableEntry("application/x-graphpad-prism-pzt", "pzt");
        addMimeTableEntry("application/x-graphpad-prism-pzb", "pzb");
        addMimeTableEntry("application/x-graphpad-prism-pzm", "pzm");
        addMimeTableEntry("application/x-graphpad-prism-pzc", "pzc");

        entry = new sun.net.www.MimeEntry("chemical/x-pdb");
        entry.setExtensions(".pdb");
        MIME_TABLE.add(entry);

        entry = new sun.net.www.MimeEntry("chemical/x-mol2");
        entry.setExtensions(".mol2");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("chemical/x-ent");
        entry.setExtensions(".ent");
        MIME_TABLE.add(entry);

        entry = new sun.net.www.MimeEntry("model/vrml");
        entry.setExtensions(".vrml");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("model/vrml");
        entry.setExtensions(".wrl");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("application/sbml+xml");
        entry.setExtensions(".sbml");
        MIME_TABLE.add(entry);
        entry = new sun.net.www.MimeEntry("application/zip");
        entry.setExtensions(".zip");
        MIME_TABLE.add(entry);

    }

    private static void addMimeTableEntry(String mimetype, String extension) {
        sun.net.www.MimeEntry entry = new sun.net.www.MimeEntry(mimetype);
        entry.setExtensions(extension);
        MIME_TABLE.add(entry);
    }

    // see
    // http://tomcat.apache.org/tomcat-5.0-doc/catalina/docs/api/org/apache/tomcat/util/http/MimeMap.html

    /**
     * @param annotation
     * @return the file the annotation represents
     */
    public static FileImpl getFile(final Annotation annotation) {
        return new FileImpl(annotation);
    }

    /**
     * Remove metacharacters from suggested name, to prevent file path attacks
     */
    private static final Pattern FORBIDDEN = Pattern.compile("[^A-Za-z0-9.]");

    /**
     * Maximum length. Annotation limits it to 32 characters. Java adds five characters to provide a unique
     * name.
     */
    private static final int NAME_LENGTH = 27;

    /**
     * Save a file. Before committing the transaction, the caller must do: file.add(labBookEntry);
     * 
     * @param wv
     * @param in
     * @param _name
     * @return
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public static File createFile(final WritableVersion wv, final InputStream in, final String _name)
        throws AccessException, ConstraintException, IOException {
        final String name = getShortName(_name);
        final java.io.File outFile = save(wv, in, name);
        final FileImpl fileImpl = new FileImpl(wv, outFile.getName());
        return fileImpl;
    }

    /**
     * @param wv writable version
     * @param in data to copy to file
     * @param _name suggested name: this class will ensure the actual name is unique
     * @return file created
     * @throws ConstraintException
     * @throws AccessException
     * @throws IOException
     */
    public static File createFile(final WritableVersion wv, final InputStream in, final String _name,
        final LabBookEntry parent) throws AccessException, ConstraintException, IOException {
        assert null != parent;
        final String name = getShortName(_name);
        final java.io.File outFile = save(wv, in, name);
        final FileImpl fileImpl = new FileImpl(wv, outFile.getName());
        final Annotation annotation =
            createAnnotation(wv, outFile.getName(), parent, getDefaultMimeType(name));
        annotation.setDetails(name);
        fileImpl.setAnnotation(annotation);
        // could do fileImpl.setDetails(name);
        return fileImpl;
    }

    private static java.io.File save(final WritableVersion wv, final InputStream in, final String name)
        throws IOException, FileNotFoundException {
        java.io.File outFile;
        final String dir = getDirectory((ModelImpl) wv.getModel());
        final java.io.File directory = new java.io.File(dir);
        if (!directory.exists()) {
            throw new RuntimeException("Directory not found: " + dir);
        }

        // prevent attacks
        Matcher matcher = FORBIDDEN.matcher(name);
        String safe_name = matcher.replaceAll("");
        // find the extension
        matcher = EXTENSION.matcher(safe_name);
        String extension = "";
        if (matcher.matches()) {
            extension = matcher.group(2);
            safe_name = matcher.group(1);
        }

        // trim to max length permitted by Annotation
        if (NAME_LENGTH < safe_name.length() + extension.length()) {
            safe_name = safe_name.substring(0, NAME_LENGTH - extension.length());
        }
        if (safe_name.length() < 3) {
            safe_name += "Uploaded";
        }
        // make the disk file
        outFile = java.io.File.createTempFile(safe_name, extension, directory);
        final java.io.OutputStream out = new java.io.FileOutputStream(outFile);
        final byte[] data = new byte[256];
        while (true) {
            final int length = in.read(data);
            if (0 >= length) {
                break;
            }
            out.write(data, 0, length);
        }
        out.close();
        return outFile;
    }

    /**
     * @param _name
     * @return
     */
    private static String getShortName(final String name) {
        final char[] nameChars = name.toCharArray();

        for (int i = name.length() - 1; i >= 0; i--) {
            if (nameChars[i] == '\\' || nameChars[i] == '/') {
                return name.substring(i + 1);
            }
        }
        return name;
    }

    /**
     * @param annotation2
     */
    private void setAnnotation(final ModelObject annotation) {
        this.annotation = (Annotation) annotation;
    }

    /**
     * @param name
     * @param mimeType
     * @return
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Annotation createAnnotation(final WritableVersion wv, final String name,
        final LabBookEntry parent, final String mimeType) throws AccessException, ConstraintException {
        final java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(Annotation.PROP_FILENAME, "/uploadedFiles/");
        attributes.put(Annotation.PROP_NAME, name);
        attributes.put(Attachment.PROP_PARENTENTRY, parent);
        attributes.put(Annotation.PROP_MIMETYPE, mimeType);
        return wv.create(Annotation.class, attributes);
    }

    /**
     * @param wv
     * @param data
     * @param name
     * @return the new file
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public static File createFile(final WritableVersion wv, final byte[] data, final String name,
        final LabBookEntry parent) throws AccessException, ConstraintException, IOException {
        final InputStream in = new java.io.ByteArrayInputStream(data);
        return createFile(wv, in, name, parent);
    }

    /**
     * The location of the PIMS upload directory
     */
    private final String directory;

    /**
     * @param annotation a model object representing the annotation in use
     */
    private FileImpl(final Annotation annotation) {
        this(annotation.get_Version(), annotation.getName());
        setAnnotation(annotation);
    }

    /**
     * @param
     */
    private FileImpl(final ReadableVersion version, final String name) {
        final ModelImpl model = (ModelImpl) version.getModel();
        this.name = name;
        this.directory = getDirectory(model);
        assert null != this.directory : "Property not found: uploadDirectory";
        // annotation is null until parent is set
    }

    private static String getDirectory(final ModelImpl model) {
        return model.getProperty("uploadDirectory");
    }

    private static final java.util.regex.Pattern EXTENSION = java.util.regex.Pattern
        .compile("^(.*)(\\.[^.]+)$");

    /**
     * @see org.pimslims.util.File#getExtension()
     */
    public String getExtension() {
        final Matcher matcher = EXTENSION.matcher(name);
        String extension = "";
        if (matcher.matches()) {
            extension = matcher.group(2);
        }
        return extension;
    }

    /**
     * @see org.pimslims.util.File#setExtension(java.lang.String)
     */
    public void setExtension(final String extension) throws ConstraintException {
        assert null != extension && (extension.startsWith(".") || extension.length() == 0);
        final java.io.File file = getFile();
        final Matcher matcher = EXTENSION.matcher(name);
        String prefix = name;
        if (matcher.matches()) {
            prefix = matcher.group(1);
        }
        final boolean ok = file.renameTo(new java.io.File(directory, prefix + extension));
        if (!ok) {
            throw new RuntimeException("Rename failed, e.g. name already exists: " + prefix + extension);
        }
        name = prefix + extension;
        if (annotation != null) {
            annotation.setName(name);
        }

        this.mimeType = getDefaultMimeType(name);
        if (this.annotation != null) {
            this.annotation.setMimeType(getDefaultMimeType(name));
        }
    }

    /**
     * @see org.pimslims.util.File#getMimeType()
     */
    public String getMimeType() {
        if (this.annotation != null && this.annotation.getMimeType() != null
            && this.annotation.getMimeType().length() > 1) {
            return this.annotation.getMimeType();
        }
        if (this.mimeType != null && this.mimeType.length() > 1) {
            return this.mimeType;
        }
        return getDefaultMimeType(name);
    }

    static String getDefaultMimeType(final String name) {
        String ret = MIME_TABLE.getContentTypeFor(name);
        if (null == ret) {
            ret = DEFAULT_MIME_TYPE;
        }
        return ret;
    }

    /**
     * @see org.pimslims.util.File#setMimeType(java.lang.String)
     */
    public void setMimeType(final String mimeType) throws ConstraintException {
        this.mimeType = mimeType;
        if (this.annotation != null) {
            this.annotation.setMimeType(mimeType);
        }

        final String extension = getExtension(mimeType);
        if (null == extension || extension.length() == 0) {
            return;
        } // unknown mime type
        if (name.toLowerCase().endsWith(extension.toLowerCase())) {
            return; //do not need to change the extenstion
        }
        if (DEFAULT_MIME_TYPE.equals(mimeType)) {
            return;
        }
        setExtension(extension);
    }

    /**
     * @param mimeType
     * @return the extension to use
     */
    private static String getExtension(final String mimeType) {
        final MimeEntry entry = MIME_TABLE.find(mimeType);
        if (null == entry) {
            return null;
        }
        final String[] extensions = entry.getExtensions();
        return extensions[extensions.length - 1];
    }

    /**
     * @see org.pimslims.util.File#add(org.pimslims.metamodel.ModelObject)
     */
    public boolean add(final LabBookEntry associate) throws AccessException, ConstraintException {
        if (null == this.annotation) {
            this.setAnnotation(createAnnotation((WritableVersion) associate.get_Version(), this.getName(),
                associate, getDefaultMimeType(this.getName())));
        }
        return associate.add(ANNOTATIONS, this.annotation);
    }

    public String getDescription() {
        return this.annotation.getDetails();
    }

    /**
     * @see org.pimslims.util.File#setDescription(java.lang.String)
     */
    public void setDescription(final String arg0) throws ConstraintException {
        this.annotation.setDetails(arg0);
    }

    /**
     * @return return the path for this file
     */
    protected String getPath() {
        return this.annotation.getFilename();
    }

    /**
     * @param arg0
     * @throws ConstraintException
     */
    protected void setPath(final String arg0) throws ConstraintException {
        this.annotation.setFilename(arg0);
    }

    /**
     * @return Returns the annotation.
     */
    @Deprecated
    public Annotation getAnnotation() {
        return this.annotation;
    }

    /**
     * @see org.pimslims.util.File#open()
     */
    public void open() throws IOException {
        in = new java.io.FileInputStream(getFile());
    }

    /**
     * @see org.pimslims.util.File#read(byte[])
     */
    public int read(final byte[] arg0) throws IOException {
        return this.in.read(arg0);
    }

    /**
     * @see org.pimslims.util.File#close()
     */
    public void close() throws IOException {
        this.in.close();
    }

    /**
     * Note that this deletes the disk file, as well as the database record. Note that it is not
     * transactionally correct - if the transaction is aborted, the disk file is lost. LATER fix this by
     * actually doing the delete in the commit method.
     * 
     * @see org.pimslims.util.implementation.File#delete()
     */
    public void delete() {
        try {
            if (null != annotation) {
                annotation.delete();
            }
            deleteWithoutAnnotation();
        } catch (final AccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteWithoutAnnotation() {
        // try {
        final String fileName = this.name;
        final java.io.File diskFile = new java.io.File(directory, fileName);
        diskFile.delete();

        /*
         * } catch (AccessException e) { e.printStackTrace(); throw new
         * RuntimeException(e); } catch (ConstraintException e) {
         * e.printStackTrace(); throw new RuntimeException(e); }
         */
    }

    /**
     * @see org.pimslims.util.File#getHook()
     */
    public String getHook() {
        if (null == this.annotation) {
            throw new IllegalStateException("This file has no hook until the parent LabBookEntry is set: "
                + this.name);
        }
        return this.annotation.get_Hook();
    }

    /**
     * @see org.pimslims.util.File#getLength()
     */
    public long getLength() {
        final java.io.File file = getFile();
        return file.length();
    }

    /**
     * @see org.pimslims.util.File#getName()
     */
    public String getName() {
        return this.name;
    }

    /**
     * @see org.pimslims.util.File#getFile()
     */
    public java.io.File getFile() {
        return new java.io.File(directory, name);
    }

    /**
     * @see org.pimslims.model.core.Annotation#getDetails()
     * @return the original name of the file
     */
    public String getDetails() {
        return this.annotation.getDetails();
    }

    /**
     * @see java.lang.Comparable#compareTo(T)
     */
    public int compareTo(final Object o) {
        if (o instanceof FileImpl) {
            final FileImpl fo = (FileImpl) o;
            return this.getName().compareTo(fo.getName());
        }
        return -1;

    }

    /**
     * @see org.pimslims.util.File#getLegend()
     */
    public String getLegend() {
        return this.annotation.getLegend();
    }

    /**
     * @see org.pimslims.util.File#getTitle()
     */
    public String getTitle() {
        return this.annotation.getTitle();
    }

    /**
     * @throws ConstraintException
     * @see org.pimslims.util.File#setLegend(java.lang.String)
     */
    public void setLegend(final String legend) throws ConstraintException {
        this.annotation.setLegend(legend);

    }

    /**
     * @throws ConstraintException
     * @see org.pimslims.util.File#setTitle(java.lang.String)
     */
    public void setTitle(final String title) throws ConstraintException {
        this.annotation.setTitle(title);

    }

    /**
     * FileImpl.getDate
     * 
     * @see org.pimslims.util.File#getDate()
     */
    @Override
    public Calendar getDate() {
        return this.annotation.getDate();
    }

}
