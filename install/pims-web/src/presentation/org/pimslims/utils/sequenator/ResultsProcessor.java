/**
 * current-pims-web org.pimslims.utils.sequenator ResultsProcessor.java
 * 
 * @author pvt43
 * @date 16 Feb 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;
import org.pimslims.utils.experiment.Utils;

/**
 * ResultsProcessor
 * 
 */
public class ResultsProcessor {

    // Map<String, File> annotations;

    Set<Experiment> exps;

    /**
     * Constructor for ResultsProcessor
     * 
     * @throws IOException
     * @throws ConstraintException
     * @throws AccessException
     */
    public ResultsProcessor(final WritableVersion rw, final java.io.File file,
        final ExperimentGroup expGroup, final boolean matchWell, final boolean matchTemplatePrimer)
        throws AccessException, ConstraintException, IOException {
        this(rw, new FileInputStream(file), expGroup, matchWell, matchTemplatePrimer);
    }

    public ResultsProcessor(final WritableVersion rw, final InputStream iostream,
        final ExperimentGroup expGroup, final boolean matchWell, final boolean matchTemplatePrimer)
        throws AccessException, ConstraintException, IOException {
        this.exps = expGroup.getExperiments();
        final FileImpl uploadedZippedFile = (FileImpl) FileImpl.createFile(rw, iostream, "uploadedZipFile");
        // extract files on server
        // this.annotations =
        this.extractFiles(rw, uploadedZippedFile, expGroup, matchWell, matchTemplatePrimer);
    }

    /*
    public Map<String, File> getAnnotations() {
        return this.annotations;
    }
    */

    /**
     * extract files on server And link them to the experiment concerned could return Map<String, File>
     * 
     * @param wv
     * @throws ZipException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     */
    private void extractFiles(final WritableVersion wv, final FileImpl uploadedFile,
        LabBookEntry linkingObject, final boolean matchWell, final boolean matchTemplatePrimer)
        throws IOException, AccessException, ConstraintException {
        //final Map<String, File> unzippedFiles = new HashMap<String, File>();

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
            String entryName = currentZipEntry.getName();
            // Strip the archive name first 
            entryName = entryName.substring(entryName.indexOf("/") + 1);
            System.out.println("ArcFileName: " + entryName);
            final Experiment exp = this.match(entryName, matchWell, matchTemplatePrimer);
            if (exp != null) {
                linkingObject = exp;
                System.out.println("Match found for:" + exp);

                final InputStream inputStreamOfEntry = zippedFile.getInputStream(currentZipEntry);
                if (!ResultsProcessor.isFileAlreadyAttached(entryName, exp)) {
                    final FileImpl unzippedFile =
                        (FileImpl) FileImpl.createFile(wv, inputStreamOfEntry, entryName, linkingObject);
                    //System.out.println("SU:" + entryName);
                    if (!Util.isEmpty(entryName)) {
                        unzippedFile.setTitle(entryName);
                    }
                    // Determine & set the status of experiments for which results has been uploaded
                    // Rate only experiments for which new files was found
                    SequencingOrder.rateResult(exp);
                }
            }
            // unzippedFiles.put(entryName, unzippedFile);
        }
        zippedFile.close();
        ((WritableVersionImpl) wv).getFlushMode().setFlushSessionAfterCreate(true);
        //System.out.println(unzippedFiles.size() + " files are extracted!");
        // return unzippedFiles;
    }

    static boolean isFileAlreadyAttached(final String fileName, final Experiment exp) {
        final File f = ServletUtil.getFileByTitle(exp, fileName);
        if (f != null) {
            return true;
        }
        return false;
    }

    /* Utility method to help with testing
     */
    protected static Set<String> getFileNames(final String fileName) throws ZipException, IOException {
        final HashSet<String> unzippedFiles = new HashSet<String>();

        // unzip
        final ZipFile zippedFile = new ZipFile(fileName);
        final Enumeration<? extends ZipEntry> entries = zippedFile.entries();
        // save each
        while (entries.hasMoreElements()) {
            final ZipEntry currentZipEntry = entries.nextElement();
            if (currentZipEntry.isDirectory()) {
                continue;
            }
            String entryName = currentZipEntry.getName();
            // Strip the archive name first 
            entryName = entryName.substring(entryName.indexOf("/") + 1);

            unzippedFiles.add(entryName);
        }
        zippedFile.close();
        return unzippedFiles;
    }

    protected Experiment match(final String fileName, final boolean wellOnly, final boolean templatePrimerOnly) {
        for (final Experiment exp : this.exps) {
            if (wellOnly) {
                if (new NamesHolder(exp).matchWell(fileName)) {
                    return exp;
                }
                continue;
            }
            if (templatePrimerOnly) {
                if (new NamesHolder(exp).matchTemplateAndPrimer(fileName)) {
                    return exp;
                }
                continue;
            }

            if (new NamesHolder(exp).match(fileName)) {
                return exp;
            }

        }
        return null;
    }

    /*
     * Helper class to help to apply complex rules of name matching between various experiment properties and a file name
     */
    static class NamesHolder {
        String pName;

        String tName;

        String well;

        String sName;

        /**
         * Constructor for ResultsProcessor.NamesHolder
         */
        public NamesHolder(final Experiment exp) {
            this.pName = Utils.getParameterValue(exp, SequencingOrder.primerName);
            this.tName = Utils.getParameterValue(exp, SequencingOrder.TemplateName);
            final Sample sample = new SequencingFileSet(exp).sample;
            this.sName = new SequencingFileSet(exp).sample.getName();
            this.well = HolderFactory.getPositionInHolder(sample);

        }

        boolean matchWell(final String fileName) {
            if (fileName.toLowerCase().startsWith(this.well.toLowerCase())) {
                return true;
            }
            return false;
        }

        boolean matchTemplateAndPrimer(String fileName) {
            assert !Util.isEmpty(fileName);
            assert fileName.indexOf(".") > 0;
            // trim extension
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            final String[] parts = fileName.split("_");
            if (parts.length != 4) {
                return false;
            }
            final String templatePrimerName = parts[2] + "_" + parts[3];
            if (templatePrimerName.equalsIgnoreCase(this.getTemplateAndPrimerNames())) {
                return true;
            }
            return false;
        }

        boolean match(String fileName) {
            assert !Util.isEmpty(fileName);
            // file does not have extension
            if (fileName.indexOf(".") <= 0) {
                return false;
            }
            // trim extension
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            final String[] parts = fileName.split("_");
            if (parts.length != 4) {
                return false;
            }
            final String fileNameNoCapilary = parts[0] + "_" + parts[2] + "_" + parts[3];
            //  System.out
            //      .println("MAtching: " + fileNameNoCapilary + "=" + this.getExpectedFileNameNoCapilary());
            if (fileNameNoCapilary.equalsIgnoreCase(this.getExpectedFileNameNoCapilary())) {
                return true;
            }
            return false;
        }

        String getExpectedFileNameNoCapilary() {
            // well_capilary_template_primer
            // A01_001_PUC18_acmd.
            // full name: this.well + "_" + "001" + "_" + this.tName + "_" + this.pName;
            return this.well + "_" + this.tName + "_" + this.pName;
        }

        String getTemplateAndPrimerNames() {
            // well_capilary_template_primer
            // A01_001_PUC18_acmd.
            // full name: this.well + "_" + "001" + "_" + this.tName + "_" + this.pName;
            return this.tName + "_" + this.pName;
        }

    }

    public static Set<String> getExpectedFileNames(final Set<NamesHolder> nameHolders) {
        final Set<String> fnames = new HashSet<String>();
        for (final NamesHolder nm : nameHolders) {
            fnames.add(nm.getExpectedFileNameNoCapilary());
        }
        return fnames;
    }
}
