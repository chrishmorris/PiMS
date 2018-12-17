/**
 * pims-web org.pimslims.lab.file FileFactory.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.fileupload.FileItem;
import org.jdom.JDOMException;
import org.pimslims.dao.ReadableVersion;

/**
 * FileFactory
 * 
 */
public class FileFactory extends java.util.HashSet {

    private static Collection<IFileType> iFileTypes = new HashSet<IFileType>();

    public static void register(final IFileType iFileType) {
        System.out.println("org.pimslims.lab.file.FileFactory register [" + iFileType.getTypeName() + "]");
        FileFactory.iFileTypes.add(iFileType);
    }

    public static IFileType newFile(final ReadableVersion version, final FileItem item) throws JDOMException,
        IOException, IFileException {
        return FileFactory.getIFile(version, item.getName(), item.getInputStream());
    }

    /*  @Deprecated
     public static IFileType newFile(final File item) throws JDOMException, IOException {

         IFileType iFile = null;
         final WritableVersion version = AbstractTestCase.model.getWritableVersion(Access.ADMINISTRATOR);
         try {
             final FileInputStream inputStream = new FileInputStream(item);
             iFile = FileFactory.getIFile(version, item.getName(), inputStream);
             inputStream.close();
             version.close();

         } catch (final Exception e) {
             e.printStackTrace();

         } finally {
             if (!version.isCompleted()) {
                 version.abort();
             }
         }
         return iFile;
     } */

    public static IFileType getIFile(final ReadableVersion version, final String name,
        final InputStream inputStream) throws JDOMException, IOException, IFileException {

        System.out.println("org.pimslims.lab.file.FileFactory.getIFile [" + name + "]");

        for (final IFileType iFileType : FileFactory.iFileTypes) {
            if (iFileType.isOfThisType(version, name)) {
                return iFileType.getInstance(name, inputStream);
            }
        }
        return null;
    }
}
