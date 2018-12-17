/**
 * V4_3-web org.pimslims.presentation.pdf Pdf.java
 * 
 * @author cm65
 * @date Sep 24, 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.util.File;

// import com.lowagie.text.pdf.PdfPTable;

/**
 * Pdf
 * 
 */
public class PdfReport {

    private final DocumentI document;

    public PdfReport(final OutputStream outputStream, final String title, final String footer)
        throws IOException {
        super();
        this.document = new PdfDocument(outputStream, title, footer);
    }

    public void showSample(final SampleBean bean, final Sample sample) throws IOException {

        final String title = "Sample : " + sample.getName();
        final TableI table = this.document.createTable(title);

        if (null != sample.getOutputSample() && null != sample.getOutputSample().getRefOutputSample()) {
            table.addKeyAndValue("Role", sample.getOutputSample().getRefOutputSample().getName());
        }

        for (final SampleCategory sampleCategory : sample.getSampleCategories()) {
            table.addKeyAndValue("SampleCategory", sampleCategory.getName());
        }

        this.document.addTable(table);
    }

    public void showComplex(final ComplexBean bean) throws IOException {

        final String title = "Complex : " + bean.getName();
        final TableI table = this.document.createTable(title);

        final String key = "Why Chosen";
        table.addKeyAndValue(key, bean.getWhyChosen());

        table.addKeyAndValue("Description", bean.getDetails());

        this.document.addTable(table);
    }

    public void showTarget(final TargetBean bean) throws IOException {

        final String title = "Target : " + bean.getName();
        final TableI table = this.document.createTable(title);

        table.addKeyAndValue("Organism", bean.getOrganismName());
        if (null != bean.getCreator()) {
            table.addKeyAndValue("Scientist", bean.getCreator().getName());
        }

        table.addKeyAndValue("Why Chosen", bean.getWhyChosen());

        table.addKeyAndValue("Description", bean.getFunc_desc());

        table.addKeyAndValue("Comments", bean.getComments());

        this.document.addTable(table);
    }

    public void showExpBlueprint(final ConstructBean bean) throws IOException {

        final String title = "Construct : " + bean.getName();
        final TableI table = this.document.createTable(title);

        table.addKeyAndValue("Organism", bean.getOrganismName());

        table.addKeyAndValue("Why Chosen", bean.getWhyChosen());

        table.addKeyAndValue("Description", bean.getDescription());

        table.addKeyAndValue("Comments", bean.getComments());

        table.addKeyAndValue("Forward Primer", bean.getFwdPrimerName() + " - " + bean.getFwdPrimer());

        table.addKeyAndValue("Reverse Primer", bean.getRevPrimerName() + " - " + bean.getRevPrimer());

        this.document.addTable(table);
    }

    public TableI showExperiment(final ExperimentBean bean, final Experiment experiment) throws IOException {

        final String title =
            "Experiment : " + bean.getExperimentName() + "-" + bean.getExperimentProtocolName();
        final TableI table = this.document.createTable(title);

        table.addKeyAndValue("Protocol", bean.getExperimentProtocolName());

        if (null != bean.getCreator()) {
            table.addKeyAndValue("Scientist", bean.getCreator().getName());
        }

        if (null != bean.getDetails() && bean.getDetails().length() > 0) {
            table.addKeyAndValue("Details", bean.getDetails());
        }

        table.addKeyAndValue("Start date", bean.getStartDateOfExperimentString());

        table.addKeyAndValue("End date", bean.getEndDateOfExperimentString());

        final Collection<InputSample> inputSamples = experiment.getInputSamples();

        if (null != inputSamples && inputSamples.size() > 0) {

            final StringBuffer sb = new StringBuffer();
            for (final InputSample inputSample : inputSamples) {
                String refInputSampleName = "";
                if (null != inputSample.getRefInputSample()) {
                    refInputSampleName = inputSample.getRefInputSample().getName();
                }
                String sampleName = "";
                if (null != inputSample.getSample()) {
                    sampleName = inputSample.getSample().getName();
                }
                sb.append(refInputSampleName + ": " + sampleName + "\n");
            }
            table.addKeyAndValue("Inputs", sb.toString());
        }

        final Collection<Parameter> parameters = experiment.getParameters();

        if (null != parameters && parameters.size() > 0) {

            for (final Parameter parameter : parameters) {
                final StringBuffer sb = new StringBuffer();
                final ParameterDefinition pd = parameter.getParameterDefinition();
                if (null == pd) {
                    sb.append(parameter.getName());
                } else {
                    if (pd.getIsResult()) {
                        sb.append("Observation: ");
                    } else if (pd.getIsGroupLevel()) {
                        sb.append("Setup: ");
                    }
                    if (null != pd.getName() && pd.getName().startsWith("__")) {
                        sb.append(pd.getLabel());
                    } else {
                        sb.append(pd.getName());
                    }
                }
                table.addKeyAndValue(sb.toString(), parameter.getValue());
            }
        }

        final Collection<File> files = PdfReport.getImages(experiment);
        for (final File file : files) {

            //System.out.println("T2CReport show file [" + file.getName() + ":" + file.getExtension() + ":"
            //    + file.getMimeType() + "]");

            final byte[] bytes = PdfReport.getBytesFromFile(file.getFile());
            final String mimeType = file.getMimeType();
            final String title2 = file.getTitle();
            table.addImage(bytes, mimeType, title2);

        }

        this.document.addTable(table);
        return table;
    }

    /**
     * PdfReport.close
     */
    public void close() throws IOException {
        this.document.close();
    }

    /**
     * PdfReport.addImage
     * 
     * @param graph
     */
    public void addImage(final byte[] graph) throws MalformedURLException, IOException {
        this.document.addImage(graph);
    }

    public static byte[] getBytesFromFile(final java.io.File file) throws IOException {

        final InputStream is = new java.io.FileInputStream(file);

        // Get the size of the file
        final long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        final byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static Collection<File> getImages(final Experiment experiment) {
        final Collection<File> files = experiment.get_Files();
        final ExperimentGroup group = experiment.getExperimentGroup();
        if (null != group) {
            files.addAll(group.get_Files());
        }
        final Collection<File> ret = new ArrayList(files.size());
        for (final Iterator iterator = files.iterator(); iterator.hasNext();) {
            final File file = (File) iterator.next();
            if (null != file.getMimeType() && file.getMimeType().startsWith("image")) {
                ret.add(file);
            }
        }
        return ret;
    }

}
