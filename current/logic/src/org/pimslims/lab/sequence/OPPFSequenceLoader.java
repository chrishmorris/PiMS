/**
 * current-pims-web org.pimslims.lab.sequence OPPFSequenceLoader.java
 * 
 * @author Petr Troshin
 * @date 20 Nov 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 Petr Troshin * *
 * 
 */
package org.pimslims.lab.sequence;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;

/**
 * OPPFSequenceLoader
 * 
 */
public class OPPFSequenceLoader {

    private int loadedFilesCounter;

    String collatedFileHook;

    String originalFileHook;

    public ArrayList<String> rejectedFiles;

    /**
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    public OPPFSequenceLoader(final WritableVersion wv, final InputStream inputStream)
        throws AccessException, ConstraintException, IOException {
        // save into a file on server
        this.rejectedFiles = new ArrayList<String>();
        final org.pimslims.util.File uploadedFile = wv.createFile(inputStream, "uploadedSequenceData", null); //FILE
        final org.pimslims.util.File cfile = this.extractFiles(wv, uploadedFile);
        this.collatedFileHook = cfile.getHook();
        this.originalFileHook = uploadedFile.getHook();
    }

    public int getNumberOfLoadedFiles() {
        return this.loadedFilesCounter;
    }

    public String getCollatedFileHook() {
        return this.collatedFileHook;
    }

    public String getOriginalFileHook() {
        return this.originalFileHook;
    }

    public ArrayList<String> getRejectedFiles() {
        return this.rejectedFiles;
    }

    /**
     * Extract files from zip archive and collate the content of .seq files in one file.
     * 
     * @param wv
     * @throws ZipException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     */
    public org.pimslims.util.File extractFiles(final WritableVersion wv,
        final org.pimslims.util.File uploadedFile) throws IOException, AccessException, ConstraintException {

        org.pimslims.util.File collatedFile = null;
        // unzip
        synchronized (uploadedFile) {

            final ZipFile zippedFile = new ZipFile(uploadedFile.getFile());
            final Enumeration<? extends ZipEntry> entries = zippedFile.entries();
            // Collect data in one place
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            // save each
            while (entries.hasMoreElements()) {
                final ZipEntry currentZipEntry = entries.nextElement();
                if (currentZipEntry.isDirectory()) { // ignore directory
                    continue;
                }
                final String entryName = currentZipEntry.getName();
                if (!entryName.toUpperCase().endsWith(".SEQ")) { // Process only files with seq extension
                    continue;
                }
                this.loadedFilesCounter++;
                final InputStream inputStreamOfEntry = zippedFile.getInputStream(currentZipEntry);
                this.process(entryName, inputStreamOfEntry, output);
                inputStreamOfEntry.close();
            }
            //  output.close(); ByteOutputStream will be closed automatically
            zippedFile.close();

            collatedFile = wv.createFile(output.toByteArray(), zippedFile.getName() + "processed", null); //FILE
            collatedFile.setExtension(".zip");
            collatedFile.setDescription("Uploaded sequence archive");

        }
        // ((WritableVersionImpl) wv).setFlushSessionAfterCreate(true);
        System.out.println(collatedFile.getHook());
        return collatedFile;
    }

    private void process(final String name, final InputStream is, final OutputStream output)
        throws IOException {
        final String file = Util.readFileToString(is);
        if (Util.isEmpty(file)) {
            return;
        }

        final PositionAwareSequence ps =
            new PositionAwareSequence(name, file, OPPFSequenceLoader.getPosition(name));
        if (ps.isValid()) {
            output.write(ps.toFile().getBytes());
        } else {
            this.rejectedFiles.add(name);
        }

    }

    /**
     * This method is in use for local directory processing
     * 
     * @param args
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        String dir = null;
        String outFile = null;
        if (args.length >= 2) {
            dir = args[0];
            outFile = args[1];
        } else {
            System.out
                .println("Usage: OPPFSequenceLoader <path_to_the_directory_with_sequences> <output_file> <style>");
            System.out.println("Where style is optional can be TXT or XML");
            return;
        }

        if (dir == null || outFile == null) {
            System.out
                .println("Usage: OPPFSequenceLoader <path_to_the_directory_with_sequences> <output_file> <style>"
                    + "/n style is optional can be TXT or XML");
            return;
        }

        File dirf = new File(dir);
        if (!dirf.exists() || !dirf.canRead() || !dirf.isDirectory()) {
            System.out.println("Directory provided does not exist or not readable or not a directory!");
            return;
        }

        dirf = null;
        final File[] files = Util.getFiles(dir, new FileFilter() {
            public boolean accept(final File file) {
                return file.getName().endsWith(".seq");
            }
        });
        final BufferedWriter bf = new BufferedWriter(new FileWriter(outFile));
        boolean style = false; //defaults to txt  
        if (args.length >= 3 && args[2] != null && args[2].equalsIgnoreCase("xml")) {
            bf.write("<PositionAwareSequences>\n");
            style = true; // defaults to xml
        }
        for (final File f : files) {
            final String fname = f.getName();
            final PositionAwareSequence ps =
                new PositionAwareSequence(fname, Util.readFileToString(f), OPPFSequenceLoader
                    .getPosition(fname));

            if (style) {
                bf.write(ps.toXML());
            } else {
                bf.write(ps.toFile());
            }
        }
        if (style) {
            bf.write("</PositionAwareSequences>\n");
        }
        bf.close();
        System.out.println("Done");
    }

/*
 * Name example: 2055804_pIC1.4_alphaleadf_D01.seq
 * @return "D01" 
 */
    public static String getPosition(String fname) {
        if (fname == null || fname.length() == 0) {
            return null;
        }
        assert fname.endsWith(".seq");
        final int dotpos = fname.lastIndexOf('.');
        assert dotpos > 0 && dotpos - 3 > 0;
        fname = fname.substring(dotpos - 3, dotpos);
        return fname;
    }
}
