/**
 * 
 */
package org.pimslims.util;

import java.io.IOException;
import java.util.Calendar;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;

/**
 * Represents a stored file, e.g. a gel image. Provides the methods that the UI will need to provide
 * facilities to view and upload files.
 * 
 * This has been designed for use with http://jakarta.apache.org/commons/fileupload/using.html
 * 
 * @author cm65
 * 
 */
public interface File {

    /**
     * @return Returns the extension.
     * @throws ApiException
     */
    public String getExtension();

    /**
     * @param extension The extension to set.
     * @throws ApiException
     * @throws ConstraintException
     */
    public void setExtension(String extension) throws ConstraintException;

    /**
     * @return Returns the mimeType.
     * @throws ApiException
     */
    public String getMimeType();

    /**
     * @param mimeType The mimeType to set.
     * @throws ApiException
     * @throws ConstraintException
     */
    public void setMimeType(String mimeType) throws ConstraintException;

    /**
     * Associate this file with a recorded page.
     * 
     * @param associate page this file refers to
     * @return boolean
     * @throws AccessException
     * @throws ConstraintException
     */
    @Deprecated
    // better to start with page
    public boolean add(LabBookEntry associate) throws AccessException, ConstraintException;

    public String getLegend();

    public void setLegend(String legend) throws ConstraintException;

    public String getTitle();

    public void setTitle(String title) throws ConstraintException;

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.model.annotation.Annotation#getDescription()
     */
    public String getDescription();

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.model.annotation.Annotation#setDescription(java.lang.String)
     */
    public void setDescription(String arg0) throws ConstraintException;

    /**
     * Make the file ready for reading. The caller is responsible for later calling close()
     * 
     * @throws IOException if there is an I/O error
     * @throws ApiException
     */
    public void open() throws IOException;

    /**
     * Get the next bytes from the file
     * 
     * @see java.io.InputStream#read(byte[])
     */
    public int read(byte[] arg0) throws IOException;

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#close()
     */
    public void close() throws IOException;

    /**
     * Delete this file
     * 
     * @throws ApiException
     */
    public void delete();

    /**
     * @return the length of this file
     * @throws ApiException
     */
    public long getLength();

    /**
     * @return the hook of the related data model record
     */
    public String getHook();

    /**
     * @return the name of the file
     * @throws ApiException
     */
    public String getName();

    /**
     * @return a java.io.File representing where the file is stored
     * @throws ApiException
     */
    public java.io.File getFile();

    public Calendar getDate();

}
