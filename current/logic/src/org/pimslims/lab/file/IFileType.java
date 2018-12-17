/**
 * pims-web org.pimslims.lab.file IFileType.java
 * 
 * @author Marc Savitsky
 * @date 25 Jun 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.jdom.JDOMException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;

/**
 * IFileType
 * 
 */
public interface IFileType {

    public String getTypeName();

    public boolean isOfThisType(ReadableVersion version, String fileName) throws IFileException;

    public IFileType getInstance(final String name, final InputStream inputStream) throws JDOMException,
        IOException;

    public void processAsAttachment(ModelObject object);

    //public ModelObject process() throws IFileException;

    public ModelObject process(WritableVersion version) throws IFileException, ConstraintException,
        UnsupportedEncodingException, IOException, AccessException;

}
