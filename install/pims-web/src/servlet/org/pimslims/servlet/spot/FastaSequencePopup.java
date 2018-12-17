package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBean;

/**
 * Servlet implementation class for Servlet: SequencePopup
 * 
 */
public class FastaSequencePopup extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public FastaSequencePopup() {
        super();
    }

    /**
     * SequencePopup.getServletInfo
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display sequence in Fasat fotmat";
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
            String name = "";
            String fastaHeader = "";
            List<String> fastaSequence = new ArrayList<String>();

            String queryString = request.getQueryString();
            if (null != queryString) {
                queryString = queryString.replaceAll("type=", "").replaceAll(":seqString", "");
                fastaHeader = FastaSequencePopup.headerMap.get(queryString);
                if (null == fastaHeader) {
                    fastaHeader = "";
                }
            }

            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                this.writeErrorHead(request, response, "no bean specified ",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String hook = pathInfo.substring(1);

            //For a Target
            if (hook.contains("target.Target")) {
                final Target t = version.get(hook);
                if (null == t) {
                    this.writeErrorHead(request, response, "Target not found: " + hook,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                // Get a SPOTTarget
                final TargetBean tb = new TargetBean(t);

                //Get the sequence
                if (queryString.contains("DNA")) {
                    if (tb != null && tb.getDnaSeq() != null) {
                        seq = tb.getDnaSeq();
                    }
                } else if (queryString.contains("Protein")) {
                    if (tb != null && tb.getProtSeq() != null) {
                        seq = tb.getProtSeq();
                    }
                } else {
                    seq = "";
                }
                //Get the name
                if (tb != null && tb.getName() != null) {
                    name = tb.getName();
                } else {
                    name = "";
                }
            }
            //If it is a Construct or Synthetic gene
            else if (hook.contains("molecule.Molecule")) {
                hook = hook.replaceAll(":seqString", "");

                final Molecule m = version.get(hook);
                if (null == m) {
                    this.writeErrorHead(request, response, "Sequence not found: " + hook,
                        HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                //Get the sequence
                final String PCRseq = m.getSequence();
                seq = PCRseq.replaceAll(" ", "");

                //Get construct name
                name = m.getName();
            }
            //process the sequence
            fastaSequence = FastaSequencePopup.fastaSeq(seq);
            //request.setAttribute("fastaSequence", fastaSequence);

            //Need details from the Query string to add to header
            fastaHeader = name + " " + fastaHeader;
            request.setAttribute("fastaHeader", fastaHeader);

            final int seqLen = seq.length();
            request.setAttribute("seqLen", seqLen);
            request.setAttribute("sequence", seq);
            request.setAttribute("fastaSequence", fastaSequence);

            // Dispatch to the JSP
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/spot/FastaSequencePopup.jsp");
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
        throw new UnsupportedOperationException();
    }

    // Mapping for queryString values for header
    private static java.util.Map<String, String> headerMap = new HashMap<String, String>();
    static {
        FastaSequencePopup.headerMap.put("targetDNA", "DNA");
        FastaSequencePopup.headerMap.put("targetProtein", "protein");
        //FastaSequencePopup.headerMap.put("pcrProduct", "PCR product");
        FastaSequencePopup.headerMap.put("expProtein", "Expressed protein");
        FastaSequencePopup.headerMap.put("finalProtein", "Final protein");
        FastaSequencePopup.headerMap.put("sGeneDNA", "Synthetic gene DNA");
        FastaSequencePopup.headerMap.put("sGeneProtein", "Synthetic gene protein");
    }

    /**
     * FastaSequencePopup.fastaSeq to create a List of strings of 100 characters for FASTA formatted sequences
     * characters
     * 
     * @param seq java.lang.String representing the sequence to be processed
     * @return java.util.List of strings of sequence
     */
    public static List<String> fastaSeq(final String seq) {
        if (null == seq || "" == seq) {
            return Collections.EMPTY_LIST;
        }
        final List<String> fastaSeq = new ArrayList<String>();
        final String sequence = seq;
        int cols = 0;
        //int cntr = 0;
        String chunk = "";
        String lastChunk = "";
        final int HUNDRED = 100;
        for (int i = 0; i < sequence.length(); i++) {
            chunk = chunk + sequence.substring(i, i + 1);
            cols++;
            if (cols == HUNDRED) {
                //cntr++;
                fastaSeq.add(chunk);
                cols = 0;
                chunk = "";
            }
        }
        lastChunk = chunk;
        fastaSeq.add(lastChunk);
        return fastaSeq;
    }
}