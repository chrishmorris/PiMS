/**
* V4_4-web org.pimslims.presentation.pdf DocumentI.java
 * 
 * @author cm65
 * @date 28 Sep 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.pdf;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * DocumentI
 * 
 */
public interface DocumentI {

    public static final String MIME_IMAGE_TIFF = "image/tiff";

    public static final String MIME_IMAGE_PNG = "image/png";

    public static final String MIME_IMAGE_JPEG = "image/jpeg";

    public abstract void addTable(final TableI table) throws IOException;

    public abstract TableI createTable(final String title) throws IOException;

    public abstract void addAuthor(final String arg0);

    public abstract void addCreationDate();

    public abstract void addTitle(final String arg0);

    public abstract void close() throws IOException;

    public abstract void addImage(final byte[] graph) throws MalformedURLException, IOException;

}