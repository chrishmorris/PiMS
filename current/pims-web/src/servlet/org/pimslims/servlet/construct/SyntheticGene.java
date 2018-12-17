package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.spot.SpotTarget;

/**
 * Servlet implementation class for Servlet: SyntheticGene
 * 
 * @author susy
 * 
 */
public class SyntheticGene extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {
    /* (non-Javadoc)
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display Synthetic gene sample";
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */

    public SyntheticGene() {
        super();
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        // Get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }

        try {

            //final SyntheticGeneBean psgb = new SyntheticGeneBean(null);

            final String pathInfo = request.getPathInfo();
            //COMMENT OUT FOR DEVELOPMENT
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no Sample specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final String hook = pathInfo.substring(1);
            //final Sample sample = (Sample) version.get(pathInfo); // e.g.
            final Sample sample = version.get(hook);
            if (null == sample) {
                this.writeErrorHead(request, response, "Sample not found: " + hook,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            //make sure sample is a synthetic gene sample
            final SampleCategory samCat =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Synthetic gene");
            final Set<SampleCategory> sCats = sample.getSampleCategories();
            if (!sCats.contains(samCat)) {
                this.writeErrorHead(request, response, "Not a Synthetic gene Sample: " + hook,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final SyntheticGeneBean psgb = new SyntheticGeneBean(sample);

            //Name of the synthetic gene sample
            final String sgName = sample.get_Name();
            psgb.setSgeneName(sgName);
            psgb.setName(sgName); //modelObject

            //hook
            psgb.setSgeneHook(sample.get_Hook());

            final String UserName = PIMSServlet.getUsername(request);
            psgb.setUserName(UserName);

            request.setAttribute("writers", PIMSServlet.findWriters(sample.getAccess()));

            request.setAttribute("mayUpdate", new Boolean(sample.get_MayUpdate()));

            //Get a DNA and protein sample components and calculate parameters
            Molecule dnaMol = null;
            Molecule protMol = null;
            Float gcCont = 0.0f;
            Float protPI = 0.0f;
            Float protEX = 0.0f;
            Float protMass = 0.0f;
            Float abs01pc = 0.0f;
            final Set<SampleComponent> sampleComps = sample.getSampleComponents();
            for (final SampleComponent sampleComp : sampleComps) {
                final Molecule mol = (Molecule) sampleComp.getRefComponent();
                if (mol.getMolType().equals("DNA")) {
                    dnaMol = mol;
                    psgb.setSgDnaSeqHook(dnaMol.get_Hook());
                    psgb.setDnaSeq(SyntheticGene.cleanSequence(dnaMol.getSequence()));
                    gcCont = SyntheticGene.calcGCContent(dnaMol.getSequence());
                }
                if (mol.getMolType().equals("protein")) {
                    protMol = mol;
                    psgb.setSgProteinSeqHook(protMol.get_Hook());
                    psgb.setProteinSeq(SyntheticGene.cleanSequence(protMol.getSequence()));
                    if (psgb.getProteinSeq() != null) {
                        final ProteinSequence pseq = new ProteinSequence(psgb.getProteinSeq());
                        protPI = pseq.getPI();
                        protEX = pseq.getExtinctionCoefficient();
                        protMass = pseq.getMass();
                        abs01pc = pseq.getAbsPt1percent();
                    }

                }
            }
            request.setAttribute("gcCont", gcCont);
            request.setAttribute("protPI", protPI);
            request.setAttribute("protEX", protEX);
            request.setAttribute("protMass", protMass);
            request.setAttribute("abs01pc", abs01pc);

            //final Molecule dnaMol = sample.
            // psgb.setSgDnaSeqHook(dnaMol.get_Hook());

            request.setAttribute("psgb", psgb);

            //Milestones to display Constructs
            if (null != psgb.getTargetBean().getHook()) {
                final String targHook = psgb.getTargetBean().getHook();
                final Target sgTarget = version.get(targHook);
                final Collection<ConstructResultBean> milestones =
                    SpotTarget.getMilestones(version, sgTarget);

                request.setAttribute("milestones", milestones);
            }

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/construct/SyntheticGene.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("post is not supported");
    }

    /**
     * @param sequence String
     * @return gc Float
     */
    public static Float calcGCContent(final String sequence) {
        try {
            DnaSequence seq = null;
            Float gc;
            if (sequence != null) {
                seq = new DnaSequence(sequence);
            } else {
                seq = new DnaSequence("");
            }
            gc = seq.getGCContent();
            return gc;
        } catch (final IllegalArgumentException ex) {
            // bad sequence
            return 0f;
        }
    }

    public static final Pattern WHITE_SPACE = Pattern.compile("\\s");

    public static String cleanSequence(final String sequence) {
        final Matcher m = SyntheticGene.WHITE_SPACE.matcher(sequence);
        final String s = m.replaceAll("").toUpperCase();
        return s;
    }

}