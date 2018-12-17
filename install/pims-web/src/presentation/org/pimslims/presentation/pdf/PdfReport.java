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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.lab.Utils;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.experiment.ExperimentBean;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.report.Filtered;
import org.pimslims.util.File;

/**
 * Pdf TODO use filter
 */
public class PdfReport {

    private final DocumentI document;

    public PdfReport(final OutputStream outputStream, final String title, final String footer)
        throws IOException {
        super();
        this.document = new PdfDocument(outputStream, title, footer);
    }

    public void showSample(final SampleBean bean, final Sample sample, final String location)
        throws IOException {

        final String title = "Sample : " + sample.getName();
        final TableI table = this.document.createTable(title);

        final String value = location;
        final String key = "Location";
        this.addRow(table, key, value);

        if (null != sample.getOutputSample() && null != sample.getOutputSample().getRefOutputSample()) {
            table.addKeyAndValue("Role", sample.getOutputSample().getRefOutputSample().getName());
        }

        for (final SampleCategory sampleCategory : sample.getSampleCategories()) {
            table.addKeyAndValue("SampleCategory", sampleCategory.getName());
        }

        this.document.addTable(table);
    }

    /**
     * PdfReport.addRow
     * 
     * @param table
     * @param value
     * @param key
     * @throws IOException
     */
    private void addRow(final TableI table, final String key, final String value) throws IOException {
        if (null != value && !"".equals(value)) {
            table.addKeyAndValue(key, value);
        }
    }

    public void showComplex(final ComplexBean bean) throws IOException {

        final String title = "Complex : " + bean.getName();
        final TableI table = this.document.createTable(title);

        final String key = "Why Chosen";
        table.addKeyAndValue(key, bean.getWhyChosen());

        table.addKeyAndValue("Description", bean.getDetails());

        this.document.addTable(table);
    }

    public TableI showTarget(final TargetBean bean) throws IOException {

        final String title = "Target : " + bean.getName();
        final TableI table = this.document.createTable(title);

        this.addRow(table, "Organism", bean.getOrganismName());
        if (null != bean.getCreator()) {
            table.addKeyAndValue("Scientist", bean.getCreator().getName());
        }

        this.addRow(table, "Why Chosen", bean.getWhyChosen());

        this.addRow(table, "Description", bean.getFunc_desc());

        this.addRow(table, "Comments", bean.getComments());

        this.addRow(table, "DNA sequence", this.formatSequence(bean.getProtSeq()));
        this.addRow(table, "Protein sequence", this.formatSequence(bean.getDnaSeq()));

        this.document.addTable(table);
        return table;
    }

    /**
     * PdfReport.formatSequence
     * 
     * @param protSeq
     * @return
     */
    private String formatSequence(final String sequence) {
        if (null == sequence || "".equals(sequence)) {
            return "";
        }
        return Utils.StringFormatter.getFormatedSequence(10, 5, sequence, Utils.StringFormatter.DEFAULT,
            Utils.StringFormatter.GENBANK);
    }

    public void showConstruct(final ConstructBean bean, final Map<String, Filtered> keywordFilter)
        throws IOException {

        final String title = "Construct : " + bean.getName();
        final TableI table = this.document.createTable(title);

        // no, it's in target table.addKeyAndValue("Organism", bean.getOrganismName());

        this.addRow(table, "Why Chosen", bean.getWhyChosen());

        this.addRow(table, "Description", bean.getDescription());

        this.addRow(table, "Comments", bean.getComments());

        this.addFilteredRow(keywordFilter, table, "Forward Primer",
            bean.getFwdPrimerName() + " - " + bean.getFwdPrimer());

        this.addFilteredRow(keywordFilter, table, "Reverse Primer",
            bean.getRevPrimerName() + " - " + bean.getRevPrimer());

        this.addFilteredRow(keywordFilter, table, "Insert", this.formatSequence(bean.getPcrProductSeq()));
        this.addFilteredRow(keywordFilter, table, "Translated sequence",
            this.formatSequence(bean.getProtSeq()));
        this.addFilteredRow(keywordFilter, table, "Expressed protein",
            this.formatSequence(bean.getExpressedProt()));
        this.addFilteredRow(keywordFilter, table, "Final protein", this.formatSequence(bean.getFinalProt()));

        this.document.addTable(table);
    }

    public TableI showExperiment(final ExperimentBean bean, final Experiment experiment,
        final Map<String, Filtered> keywordFilter) throws IOException {

        final String title =
            "Experiment : " + bean.getExperimentName() + "-" + bean.getExperimentProtocolName();
        final TableI table = this.document.createTable(title);

        table.addKeyAndValue("Protocol", bean.getExperimentProtocolName());

        final String key = "Method";
        final String value = bean.getMethod();
        this.addFilteredRow(keywordFilter, table, key, value);

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
                //TODO if not filtered
                sb.append(refInputSampleName + ": " + sampleName + "\n");
            }
            final String name = sb.toString();
            if (null == keywordFilter.get(name) || !keywordFilter.get(name).getFilteredOut()) {
                table.addKeyAndValue("Inputs", name);
            }
        }

        if (null != experiment.getProtocol()
            && (null == keywordFilter.get("Reagents") || !keywordFilter.get("Reagents").getFilteredOut())

        ) {
            final StringBuffer sb = new StringBuffer();
            final List<RefInputSample> riss = experiment.getProtocol().getRefInputSamples();
            for (final Iterator iterator = riss.iterator(); iterator.hasNext();) {
                final RefInputSample ris = (RefInputSample) iterator.next();
                if (null == ris.getRecipe()) {
                    continue; // not a reagent
                }
                sb.append(ris.getName() + ": " + ris.getRecipe().getName() + "\n");
            }
            table.addKeyAndValue("Reagents", sb.toString());

        }

        if (null == keywordFilter.get("Product") || !keywordFilter.get("Product").getFilteredOut()) {
            final Set<OutputSample> oss = experiment.getOutputSamples();
            for (final Iterator iterator = oss.iterator(); iterator.hasNext();) {
                final OutputSample os = (OutputSample) iterator.next();
                String name = os.getName();
                if (null != os.getRefOutputSample()) {
                    name += ": " + os.getRefOutputSample().getSampleCategory().getName();
                } else if (null != os.getSample()) {
                    name += ": ";
                    final Set<SampleCategory> categories = os.getSample().getSampleCategories();
                    for (final Iterator iterator2 = categories.iterator(); iterator2.hasNext();) {
                        final SampleCategory sampleCategory = (SampleCategory) iterator2.next();
                        name += sampleCategory.getName() + " ";
                    }
                }
                table.addKeyAndValue("Product", name);
            }
        }

        final Collection<Parameter> parameters = experiment.getParameters();

        if (null != parameters && parameters.size() > 0) {

            for (final Parameter parameter : parameters) {
                if (null == parameter.getValue() || "".equals(parameter.getValue())) {
                    continue;
                }
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

                final String name = sb.toString();
                if (null == keywordFilter.get(name) || !keywordFilter.get(name).getFilteredOut()) {
                    table.addKeyAndValue(sb.toString(), parameter.getValue());
                }
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
     * PdfReport.addFilteredRow
     * 
     * @param keywordFilter
     * @param table
     * @param key
     * @param value
     * @throws IOException
     */
    private void addFilteredRow(final Map<String, Filtered> keywordFilter, final TableI table,
        final String key, final String value) throws IOException {
        if (null == keywordFilter.get(key) || !keywordFilter.get(key).getFilteredOut()) {
            this.addRow(table, key, value);
        }
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
