/**
 * pims-web org.pimslims.servlet.importer CheckSequence.java
 * 
 * @author Marc Savitsky
 * @date 29 Jul 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.oppf;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sequence.ASequence;
import org.pimslims.lab.sequence.PcrDnaSequence;
import org.pimslims.lab.sequence.PearsonDnaSequence;
import org.pimslims.lab.sequence.SequenceException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.WellExperimentBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;
import org.pimslims.servlet.DiagramServlet;
import org.pimslims.util.File;

public class AlignSequenceServlet extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    private static final String IS_IN_MODAL_WINDOW = QuickSearch.IS_IN_MODAL_WINDOW;

    public static final String MIME_ZIP = "application/zip";

    public static final String MIME_COMPRESS = "application/x-zip-compressed";

    @Override
    public String getServletInfo() {
        return "Import Sequence data and Compare Sequence results";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("AlignSequence.doGet");

        if (!this.checkStarted(request, response)) {
            return;
        }

        //final ServletContext scontext = this.getServletContext();
        final PrintWriter writer = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash
        System.out.println("AlignSequence.doGet [" + pathInfo + "]");

        ReadableVersion version = null;
        try {
            // get a read transaction
            version = this.getReadableVersion(request, response);

            final ModelObject object = version.get(pathInfo);

            if (object == null) { // http 404 error
                final Object message =
                    org.pimslims.properties.PropertyGetter.getStringProperty("error.graph.no_hook",
                        "Not Found: " + pathInfo);
                response.sendError(404,
                    (message != null && message.toString().length() > 0) ? message.toString()
                        : DiagramServlet.ErrorMessage_NoHook);
                return;
            }

            final Experiment experiment = (Experiment) object;
            final PearsonDnaSequence zipSequence = AlignSequenceServlet.getZipSequence(version, experiment);
            System.out.println("AlignSequenceServlet zip sequence [" + zipSequence.getSequence() + "]");
            final PcrDnaSequence pcrSequence = AlignSequenceServlet.getConstructSequence(version, experiment);
            System.out.println("AlignSequenceServlet construct PCR sequence [" + pcrSequence.getSequence()
                + "]");
            //final PimsAlignment alignment = AlignSequence.runAlign(version, pcrSequence, zipSequence);

            final PimsAlignment alignment =
                AlignSequenceServlet.getAlignment(pcrSequence, zipSequence, version);

            request.setAttribute("alignment", alignment);
            request.setAttribute("clustalw", AlignSequenceServlet.format(alignment, zipSequence));
            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(experiment));
            request.setAttribute("zipsequence", zipSequence);
            request.setAttribute("pcrsequence", pcrSequence);

            request.setAttribute(AlignSequenceServlet.IS_IN_MODAL_WINDOW,
                request.getParameter(AlignSequenceServlet.IS_IN_MODAL_WINDOW));

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/oppf/AlignSequence.jsp");
            dispatcher.forward(request, response);

            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final SequenceException e) {
            //throw new ServletException(e);
            this.writeErrorHead(request, response, "No Sequence Align", HttpServletResponse.SC_BAD_REQUEST);
            writer.print(e.getLocalizedMessage());
            PIMSServlet.writeFoot(writer, request);
            return;
        } catch (final AccessException e) {
            throw new ServletException(e);

        } finally {
            if (version != null && !version.isCompleted()) {
                version.abort();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("AlignSequence.doPost");

    }

/*
    private void showBadPathPage(final HttpServletRequest request, final HttpServletResponse response,
        final String dotPath) throws IOException {
        this.writeErrorHead(request, response, "Installation problem", HttpServletResponse.SC_BAD_REQUEST);
        final PrintWriter writer = response.getWriter();
        writer.print("<div style=\"border:1px solid blue;padding:15px;background-color:#ccf\">");
        writer.print("<p>PIMS is unable to show your graph. Please show this page to your adminstrator.</p>");
        writer.print("<p>The GraphViz executable dot was not found at: " + dotPath + "</p>");
        writer.print("</div><br/>");
        writer.print("<a href=\"../Installation\">View installation details</a>");
    } */
/*
    private boolean validString(final String s) {
        if (null != s && s.trim().length() > 0) {
            return true;
        }
        return false;
    } */

    /**
     * 
     * CheckSequenceServlet.getZipSequence
     * 
     * @param version
     * @param experiment
     * @return
     */
    public static PearsonDnaSequence getZipSequence(final ReadableVersion version, final Experiment experiment)
        throws SequenceException {

        final ExperimentGroup group = experiment.getExperimentGroup();
        final Holder holder = HolderFactory.getPlate(group);

        try {
            final WellExperimentBean bean =
                new WellExperimentBean(experiment, holder != null ? holder.getName() : null, holder != null
                    ? holder.get_Hook() : null, group != null ? group.getName() : null, group != null
                    ? group.get_Hook() : null, HolderFactory.getRow(experiment),
                    HolderFactory.getColumn(experiment), Collections.EMPTY_MAP);

            final String position =
                bean.getRow() + new DecimalFormat("00").format(new Integer(bean.getColumn()));

            System.out.println("AlignSequenceServlet.getZipEntry position [" + position + "]");

            if (null != group) {

                for (final File file : group.get_Files()) {
                    System.out.println("AlignSequenceServlet.AlignSequence file [" + file.getDescription()
                        + ":" + file.getName() + ":" + file.getMimeType() + "]");

                    if (AlignSequenceServlet.isZipFile(file)) {
                        file.open();
                        System.out.println("AlignSequenceServlet.getZipEntry file [" + file.getName() + "]");
                        final ZipFile zipFile = new ZipFile(file.getFile());

                        final Collection<ZipEntry> entries =
                            Collections.list((Enumeration<ZipEntry>) zipFile.entries());

                        for (final ZipEntry entry : entries) {
                            final String name = entry.getName();
                            System.out.println("Zip entry [" + name + "]");

                            if (name.endsWith(".seq")) {
                                final Project researchObjective = experiment.getProject();
                                if (null != researchObjective) {
                                    if (researchObjective.getName().equals(
                                        SequenceResultServlet.getOPPFNumber(name))) {
                                        return new PearsonDnaSequence(name,
                                            AlignSequenceServlet.readInputStream(zipFile, entry));
                                    }
                                }
                                if (entry.getName().contains(position)) {
                                    return new PearsonDnaSequence(name, AlignSequenceServlet.readInputStream(
                                        zipFile, entry));
                                }
                            }
                        }
                        file.close();
                    }
                }
            }

        } catch (final AccessException e) {
            throw new SequenceException(e.getLocalizedMessage());
        } catch (final IOException e) {
            throw new SequenceException(e.getLocalizedMessage());
        }

        throw new SequenceException("No zip file containing a sequence for this experiment found");
    }

    public static boolean isZipSequence(final ReadableVersion version, final Experiment experiment) {
        try {
            AlignSequenceServlet.getZipSequence(version, experiment);
        } catch (final SequenceException e) {
            return false;
        }
        return true;

    }

    protected static String readInputStream(final ZipFile zipFile, final ZipEntry entry) throws IOException {
        final StringBuffer sb = new StringBuffer();
        final InputStream zis = zipFile.getInputStream(entry);
        int len;
        /* Too slow!
        while ((len = zis.read()) >= 0) {
            sb.append((char) len);
        }
        zis.close();
        return sb.toString();
         */
        final byte[] b = new byte[4096];
        while ((len = zis.read(b, 0, 4096)) >= 0) {
            sb.append(new String(b, 0, len));
        }
        zis.close();
        return sb.toString();

    }

    /**
     * 
     * CheckSequenceServlet.getConstructSequence
     * 
     * @param version
     * @param experiment
     * @return
     */
    public static PcrDnaSequence getConstructSequence(final ReadableVersion version,
        final Experiment experiment) throws SequenceException {

        final Project expBlueprint = experiment.getProject();
        if (null == expBlueprint) {
            throw new SequenceException("No ResearchObjective found for this experiment");
        }
        final ConstructBean constructBean =
            ConstructBeanReader.readConstruct((ResearchObjective) expBlueprint);
        final PcrDnaSequence pcrSequence = new PcrDnaSequence(constructBean.getPcrProductSeq());
        pcrSequence.setName(constructBean.getName());
        pcrSequence.setTargetName(constructBean.getTargetName());

        return pcrSequence;
    }

    private static String getPaddedString(final String name, final int i) {
        final StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append("                  ");
        return sb.toString().substring(0, i);
    }

    public static String format(final PimsAlignment alignment, final ASequence zipSequence) {

        final StringBuffer sb = new StringBuffer();

        if (zipSequence instanceof org.pimslims.lab.sequence.PearsonDnaSequence) {
            sb.append("Sequence format is Pearson\n");
        }

        if (null != alignment) {
            sb.append("Sequence 1: " + AlignSequenceServlet.getPaddedString(alignment.getQueryName(), 17)
                + alignment.getQuerySeqLength() + " bp\n");
            sb.append("Sequence 2: " + AlignSequenceServlet.getPaddedString(alignment.getHitName(), 17)
                + zipSequence.getLength() + " bp\n");

            sb.append("Alignment Score " + alignment.getScore() + "\n");
            sb.append("Alignment Identity " + alignment.getIdentity() + " (" + alignment.getPercentIdentity()
                + "%)\n");
            sb.append("Biojava multiple sequence alignment\n");
            sb.append("\n");
            final String[] formats = alignment.getFormatted();

            for (int i = 0; i < formats.length; i++) {
                if (formats[i].startsWith("Query:")) {
                    sb.append(AlignSequenceServlet.getPaddedString(alignment.getQueryName(), 15) + " "
                        + formats[i].substring(7) + "\n");
                } else if (formats[i].startsWith("Target:")) {
                    sb.append(AlignSequenceServlet.getPaddedString(alignment.getHitName(), 15)
                        + formats[i].substring(7) + "\n");
                } else {
                    sb.append("        " + formats[i] + "\n");
                }
            }
        }

        return sb.toString();
    }

    /**
     * AlignSequenceServlet.getAlignment
     * 
     * @param pcrSequence
     * @param zipSequence
     * @param version
     * @return
     * @throws AccessException
     * @throws ConstraintException
     * @deprecated Use {@link #getAlignment(PcrDnaSequence, PearsonDnaSequence)} instead as version is no
     *             longer needed.
     */
    @Deprecated
    protected static PimsAlignment getAlignment(final PcrDnaSequence pcrSequence,
        final PearsonDnaSequence zipSequence, final ReadableVersion version) throws AccessException,
        ConstraintException {
        return AlignSequenceServlet.getAlignment(pcrSequence, zipSequence);
    }

    /**
     * AlignSequenceServlet.getAlignment
     * 
     * @param pcrSequence
     * @param zipSequence
     * @param version
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    protected static PimsAlignment getAlignment(final PcrDnaSequence pcrSequence,
        final PearsonDnaSequence zipSequence) throws AccessException, ConstraintException {
        return AlignSequenceServlet.getAlignment(pcrSequence.getName(), pcrSequence.getSequence(),
            zipSequence.getName(), zipSequence.getSequence());
    }

    /**
     * AlignSequenceServlet.getAlignment
     * 
     * @param pcrSequence
     * @param zipSequence
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    protected static PimsAlignment getAlignment(final String expectedName, final String expectedSequence,
        final String measuredName, final String measuredSequence) throws AccessException, ConstraintException {

        SWSearch search = null;
        search = new SWSearch(expectedName, expectedSequence);
        //final Collection<PimsAlignment> alignments =
        //    search.align(Collections.singleton(zipSequence.getMolComponent(version)), true, version);
        final Map<String, String> namedSequences = new HashMap<String, String>();
        namedSequences.put(measuredName, SWSearch.cleanSequence(measuredSequence));
        final Collection<PimsAlignment> alignments = search.align(namedSequences, true);

        PimsAlignment alignment = null;
        if (alignments.iterator().hasNext()) {
            alignment = alignments.iterator().next();
        }

        return alignment;
    }

    // TODO use SequencingResult.isZipFile
    private static boolean isZipFile(final File file) {
        if (file.getMimeType().equals(AlignSequenceServlet.MIME_ZIP)) {
            return true;
        }
        if (file.getMimeType().equals(AlignSequenceServlet.MIME_COMPRESS)) {
            return true;
        }
        return false;
    }
}
