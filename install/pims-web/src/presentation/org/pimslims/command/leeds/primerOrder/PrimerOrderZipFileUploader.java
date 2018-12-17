package org.pimslims.command.leeds.primerOrder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

public class PrimerOrderZipFileUploader {
    // name & hook of annocation for the extracted files
    Map<String, File> uploadedUnzippedAnnotations;

    String xlsFileLocalPath = null;

    String xlsFileName = null;

    File xlsFile = null;

    /**
     * @param wv
     * @param inputStream
     * @param name
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public PrimerOrderZipFileUploader(final WritableVersion wv, final InputStream inputStream)
        throws AccessException, ConstraintException, IOException {
        super();
        // save into a file on server
        final FileImpl uploadedZippedFile =
            (FileImpl) FileImpl.createFile(wv, inputStream, "uploadedZipFile");
        // extract files on server
        this.uploadedUnzippedAnnotations = this.extractFiles(wv, uploadedZippedFile);
        // set information about xls file
        this.setXlsFileInfo(wv);
        // delete
        // TODO maybe add this zipfile into order
        // uploadedZippedFile.delete();
    }

    private void setXlsFileInfo(final WritableVersion wv) {
        final List<String> xlsFileNames = new LinkedList<String>();
        for (final String name : this.uploadedUnzippedAnnotations.keySet()) {
            if (name.toLowerCase().endsWith(".xls")) {
                xlsFileNames.add(name);
            }
        }
        if (xlsFileNames.size() == 0) {
            throw new java.lang.IllegalArgumentException("No .xls file found in zip");
        } else if (xlsFileNames.size() > 1) {
            throw new java.lang.IllegalArgumentException("Too many .xls files found in zip");
        }
        this.xlsFileName = xlsFileNames.get(0);
        this.xlsFile = (this.uploadedUnzippedAnnotations.get(this.xlsFileName));
        this.xlsFileLocalPath = this.xlsFile.getName();

    }

    /**
     * extract files on server
     * 
     * @param wv
     * @throws ZipException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     */
    private Map<String, File> extractFiles(final WritableVersion wv, final FileImpl uploadedFile)
        throws IOException, AccessException, ConstraintException {
        final Map<String, File> unzippedFiles = new HashMap<String, File>();

        ((WritableVersionImpl) wv).getFlushMode().setFlushSessionAfterCreate(false);
        // unzip
        final ZipFile zippedFile = new ZipFile(uploadedFile.getFile());
        final Enumeration<? extends ZipEntry> entries = zippedFile.entries();
        // save each
        while (entries.hasMoreElements()) {
            final ZipEntry currentZipEntry = entries.nextElement();
            if (currentZipEntry.isDirectory()) {
                continue;
            }
            final String entryName = currentZipEntry.getName();
            final InputStream inputStreamOfEntry = zippedFile.getInputStream(currentZipEntry);
            final FileImpl unzippedFile = (FileImpl) FileImpl.createFile(wv, inputStreamOfEntry, entryName);
            // annotation.setDetails(annotation.getDetails().replace("/", " "));
            unzippedFiles.put(entryName, unzippedFile);
        }
        zippedFile.close();
        ((WritableVersionImpl) wv).getFlushMode().setFlushSessionAfterCreate(true);
        //System.out.println(unzippedFiles.size() + " files are extracted!");
        return unzippedFiles;
    }

    /**
     * get name of the first xls file found
     * 
     * @return
     */
    public String getXLSFileName() {
        return this.xlsFileName;
    }

    /**
     * get hook of annotation which contrains the first xls file found
     * 
     * @return
     */
    public org.pimslims.util.File getXLSFile() {
        return this.xlsFile;
    }

    /**
     * get local path of file
     * 
     * @return
     */
    public String getXLSFileServerName() {
        return this.xlsFileLocalPath;
    }

    /**
     * find all target related files by target name
     * 
     * @param targetName
     * @return
     */
    public Map<String, org.pimslims.util.File> findTargetRelatedFiles(final String targetName) {
        final Map<String, org.pimslims.util.File> targetRelatedFiles =
            new HashMap<String, org.pimslims.util.File>();
        for (final String name : this.uploadedUnzippedAnnotations.keySet()) {
            if (name.toLowerCase().contains(targetName.toLowerCase())) {
                targetRelatedFiles.put(name, (this.uploadedUnzippedAnnotations.get(name)));
            }
        }
        return targetRelatedFiles;
    }
}
