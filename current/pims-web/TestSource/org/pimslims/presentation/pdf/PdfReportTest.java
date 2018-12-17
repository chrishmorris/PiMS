/**
 * pims-web org.pimslims.servlet.report T2CReportTest.java
 * 
 * @author cm65
 * @date 9 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.presentation.experiment.ExperimentBean;

/**
 * T2CReportTest
 * 
 */
public class PdfReportTest extends TestCase {

    private static final String UNIQUE = "t2cr" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    AbstractModel model = ModelImpl.getModel();

    public void testExperiment() throws ConstraintException, IOException {
        /* */
        final File file = new File("test.pdf");
        final FileOutputStream os = new FileOutputStream(file);
        // or os = new ByteArrayOutputStream() */
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, PdfReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PdfReportTest.UNIQUE, PdfReportTest.NOW, PdfReportTest.NOW, type);
            final ExperimentBean bean = new ExperimentBean(experiment);
            final PdfReport document = new PdfReport(os, "title", "footer");
            final TableI table = document.showExperiment(bean, experiment);
            Assert.assertEquals(3, table.getRows());
            document.close();
            os.close();
        } finally {
            version.abort();
        }
    }

    public void testParameters() throws ConstraintException, IOException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, PdfReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PdfReportTest.UNIQUE, PdfReportTest.NOW, PdfReportTest.NOW, type);
            new Parameter(version, experiment).setValue("pvalue1");
            new Parameter(version, experiment).setValue("pvalue2");
            final ExperimentBean bean = new ExperimentBean(experiment);
            final PdfReport document = new PdfReport(new ByteArrayOutputStream(), "title", "footer");
            final TableI table = document.showExperiment(bean, experiment);
            Assert.assertEquals(5, table.getRows());
        } finally {
            version.abort();
        }
    }

    public void testFile() throws ConstraintException, IOException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, PdfReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PdfReportTest.UNIQUE, PdfReportTest.NOW, PdfReportTest.NOW, type);
            final InputStream stream = new ByteArrayInputStream("test".getBytes("UTF8"));
            version.createFile(stream, "file", experiment);
            final ExperimentBean bean = new ExperimentBean(experiment);
            final PdfReport document = new PdfReport(new ByteArrayOutputStream(), "title", "footer");
            document.showExperiment(bean, experiment);
        } finally {
            version.abort();
        }
    }
/*
    public void testBadTiff() throws ConstraintException, IOException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ExperimentType type = new ExperimentType(version, PdfReportTest.UNIQUE);
            final Experiment experiment =
                new Experiment(version, PdfReportTest.UNIQUE, PdfReportTest.NOW, PdfReportTest.NOW, type);
            final InputStream stream = new ByteArrayInputStream("test".getBytes("UTF8"));
            version.createFile(stream, "file", experiment).setMimeType(DocumentI.MIME_IMAGE_TIFF);
            final ExperimentBean bean = new ExperimentBean(experiment);
            final PdfReport document = new PdfReport(new ByteArrayOutputStream(), "title", "footer");
            document.showExperiment(bean);
            Assert.fail("Exception not reported");
        } catch (final IllegalArgumentException e) {
            // that is as it should be
        } finally {
            version.abort();
        }
    } */

}
