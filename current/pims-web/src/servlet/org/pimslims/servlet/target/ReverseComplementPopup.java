package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.SequenceUtility;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;

/**
 * Servlet implementation class for Servlet: ReverseComplementPopup
 * 
 */
public class ReverseComplementPopup extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {

    /**
     * ReverseComplementPopup.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return null;
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
            String targName = "";
            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no object specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final String hook = pathInfo.substring(1);
            DnaSequence seq = null;

            if (hook.contains("target.Target")) {
                final Target t = version.get(hook);
                if (null == t) {
                    this.writeErrorHead(request, response, "Target not found: " + hook,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                // Get a SPOTTarget
                final TargetBean tb = new TargetBean(t);
                targName = tb.getName();

                //Get the sequence and make the reverse complement
                if (tb != null && tb.getDnaSeq() != null) {
                    seq = new DnaSequence(tb.getDnaSeq());
                } else if (PIMSTarget.isDNATarget(t)) {
                    seq = new DnaSequence(tb.getProtSeq());
                } else {
                    seq = new DnaSequence("");
                }
            } //For Synthetic gene hook is  a molecule
            else if (hook.contains("molecule.Molecule")) {
                final Molecule molecule = version.get(hook);
                if (null == molecule) {
                    this.writeErrorHead(request, response, "Molecule not found: " + hook,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                targName = molecule.getName();
                //Get the sequence
                if (molecule.getSequence() != null) {
                    seq = new DnaSequence(molecule.getSequence());
                } else {
                    seq = new DnaSequence("");
                }
            }

            request.setAttribute("targName", targName);

            final String rcSeq = seq.getReverseComplement().getSequence();
            final List<String> rcChunks = SequenceUtility.chunkSeq(rcSeq);
            request.setAttribute("rcChunks", rcChunks);

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/target/ReverseComplementPopup.jsp");
            dispatcher.forward(request, response);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }
}