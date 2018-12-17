package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;
import org.pimslims.presentation.construct.SyntheticGeneBean;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.servlet.construct.SyntheticGene;

/**
 * Servlet implementation class for Servlet: DNAandProtSeqPopup
 * 
 */
public class DNAandProtSeqPopup extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {
    /* (non-Javadoc)
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display DNA and protein sequences in a popup";
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public DNAandProtSeqPopup() {
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
            String seq = null;
            String protId = "";
            //TODO for Synthetic gene need to use synthetic gene sample sequence
            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no DNA sequence specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            final String hook = pathInfo.substring(1);
            if (hook.contains("target.Target")) {
                final Target t = version.get(hook);
                if (null == t) {
                    this.writeErrorHead(request, response, "Target not found: " + hook,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                // Get a SPOTTarget
                final TargetBean tb = new TargetBean(t);
                protId = tb.getName();

                //Get the sequence
                if (tb != null && tb.getDnaSeq() != null) {
                    seq = tb.getDnaSeq();
                } else {
                    seq = "";
                }
            }
            //For processing SyntheticGene sequence
            if (hook.contains("sample.Sample")) {
                final Sample sgSample = version.get(hook);
                if (null == sgSample || !(SyntheticGeneManager.isSynthGene(sgSample))) {
                    this.writeErrorHead(request, response, "Synthetic gene Sample not found",
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                //Make a SyntheticGene bean
                final SyntheticGeneBean sgb = new SyntheticGeneBean(sgSample);
                protId = sgb.getName();

                //Get the sequence
                if (sgb != null && sgb.getDnaSeq() != null) {
                    seq = SyntheticGene.cleanSequence(sgb.getDnaSeq());
                } else {
                    seq = "";
                }
            }
            //If this is the construct sequence, need to use start and end to select region of sequence
            String queryString = request.getQueryString();
            if (null != queryString) {
                queryString =
                    queryString.replaceAll("start=", "").replaceAll("end=", "").replaceAll("protId=", "");
                final String[] startAndEnd = queryString.split("&");

                final int start = Integer.parseInt(startAndEnd[0]);
                final int end = Integer.parseInt(startAndEnd[1]);
                seq = seq.substring((start * 3) - 3, end * 3);
                protId = startAndEnd[2];
            }

            request.setAttribute("protId", protId);
            final int seqLen = seq.length() / 3;
            request.setAttribute("seqLen", seqLen);

            //Make the array to pass to the jsp
            List<String> dnaAndProtSeq2 = new ArrayList<String>();

            dnaAndProtSeq2 = ThreeLetterProteinSeq.dnaAndProtArray(seq);
            request.setAttribute("dnaAndProtSeq2", dnaAndProtSeq2);

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/DNAandProtSeqPopup2.jsp");
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
        // TODO Auto-generated method stub
    }

}