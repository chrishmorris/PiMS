package org.pimslims.servlet.target;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.sequence.SequenceUtility;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * To create an alignment between the Target protein sequence and translated DNA sequence. PIMS-876 DNA
 * sequence and protein sequence should match. CompareTargetSequences
 * 
 */
public class CompareTargetSequences extends PIMSServlet {

    /**
     * CompareTargetSequences.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return null;
    }

    /* 
     * Create the alignment and display in a popup window
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
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

            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no target specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final String hook = pathInfo.substring(1);
            final Target t = version.get(hook);
            if (null == t) {
                this.writeErrorHead(request, response, "Target not found: " + hook,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Get a SPOTTarget
            final TargetBean tb = new TargetBean(t);

            //Make the alignment and put it into the request
            CompareTargetSequences.targetSeqAlignment(request, version, tb);

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/CompareTargetSequences.jsp");
            dispatcher.forward(request, response);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * SpotTarget.targetSeqAlignment
     * 
     * @param request
     * @param version
     * @param tb
     */
    public static void targetSeqAlignment(final HttpServletRequest request, final ReadableVersion version,
        final TargetBean tb) {
        final String[] seqAli =
            SequenceUtility.compProtSeqs(tb.getProtein_name(), tb.getProtSeq(), tb.getDnaSeq(), version);
        final Integer lengthTransSeq = ThreeLetterProteinSeq.lengthWithoutTer(tb.getDnaSeq());
        final Integer protSeqLen = tb.getProtSeq().length();
        request.setAttribute("seqAli", seqAli);
        request.setAttribute("lengthTransSeq", lengthTransSeq);
        request.setAttribute("protSeqLen", protSeqLen);
    }

}