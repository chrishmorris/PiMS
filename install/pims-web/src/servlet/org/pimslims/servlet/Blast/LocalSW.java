package org.pimslims.servlet.Blast;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.local.AlignmentParameters;
import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.bioinf.local.SequenceGetter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Peter Troshin STFC Daresbory Laboratory
 * @date September 2007 TODO fix http://pimstrak1.dl.ac.uk:8080/jira/browse/PRIV-71
 */
public class LocalSW extends PIMSServlet {

    static final int GAPS = 20; // defaults

    static final int GPEN = 1; // defaults

    static final int SCORE = 500; // default

    static final int NUM_HITS = 20; // default

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     *      Set parameters for search
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        request.setAttribute("score", LocalSW.SCORE);
        request.setAttribute("gaps", LocalSW.GAPS);
        request.setAttribute("gpen", LocalSW.GPEN);
        request.setAttribute("hitsNum", LocalSW.NUM_HITS);
        request.setAttribute("matrixes", AlignmentParameters.getMatrixList());
        request.setAttribute("defaultMatrix", "BLOSUM62");
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/bioinf/LoadSequence.jsp");
        rd.forward(request, response);
    }

    /**
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Only runs if not file
        final String score = request.getParameter("score");
        final String matrix = request.getParameter("matrix");
        // This is a new request pass defaults
        final String gaps = request.getParameter("gaps");
        final String gpen = request.getParameter("gpen");
        final String hitsNum = request.getParameter("hitsNum");
        int gapsFl = LocalSW.GAPS;
        int gpenFl = LocalSW.GPEN;
        int scoreInt = LocalSW.SCORE;
        int hitsNumInt = LocalSW.NUM_HITS;
        try {
            gapsFl = Integer.parseInt(gaps);
            gpenFl = Integer.parseInt(gpen);
            scoreInt = Integer.parseInt(score);
            hitsNumInt = Integer.parseInt(hitsNum);
        } catch (final NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Fail to parse SWSearch parameters, falling back to defaults");
        }

        String sequence = request.getParameter("record");
        //System.out.println("USING SC" + scoreInt + " GA " + gapsFl + " gP " + gpenFl + " Matr " + matrix);
        String queryName = null;
        queryName = this.getName(sequence);
        if (queryName != null) {
            sequence = this.removeName(sequence);
        } else {
            queryName = "Query";
        }
        SWSearch searcher = null;
        sequence = SWSearch.cleanSequence(sequence);
        searcher = new SWSearch(queryName, sequence, matrix, gapsFl, gpenFl);
        ReadableVersion rv = null;
        try {
            rv = this.getReadableVersion(request, response);
            List<PimsAlignment> alingments = null;
            searcher.setCutoff(scoreInt);
            if ("true".equalsIgnoreCase(request.getParameter("isDNA"))) {
                alingments = searcher.align(SequenceGetter.getDNAs(rv), true, rv);
            } else {
                alingments = searcher.align(SequenceGetter.getProteins(rv), false, rv);
            }

            if (alingments.isEmpty()) {
                final PrintWriter writer = response.getWriter();
                this.writeHead(request, response, "No similar sequences found");
                writer.print("No significant hits found. Try adjusting a cutoff score. ");
                PIMSServlet.writeFoot(writer, request);
                rv.abort();
                // Nothing to display
                return;
            }
            // Chop the result list to the requested size
            if (hitsNumInt != 0 && alingments.size() > hitsNumInt) {
                alingments = new ArrayList<PimsAlignment>(alingments).subList(0, hitsNumInt);
            }
            request.setAttribute("alignments", alingments);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/bioinf/LocalSWResult.jsp");
            rd.forward(request, response);

            rv.commit();
        } catch (final AbortedException e) {
            throw new RuntimeException(e);
        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /**
     * @param sequence
     * @return
     */
    private String removeName(String sequence) {
        sequence = sequence.trim();
        final int nlinepos = sequence.indexOf('\n');
        sequence = sequence.substring(nlinepos + 1);
        return sequence;
    }

    /**
     * @param sequence
     * @return
     */
    private String getName(String sequence) {
        if (sequence == null) {
            return null;
        }
        sequence = sequence.trim();
        String fline = null;
        if (sequence.startsWith(">")) {
            final int nlinepos = sequence.indexOf('\n');
            if (nlinepos < 0) {
                return null;
            }
            fline = sequence.substring(1, nlinepos - 1);
            // trim to 13 characters
            if (fline.length() > 13) {
                fline = fline.substring(0, 13);
            }
        }
        return fline;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Run local Smith-Waterman search agains protein or nucleotide sequences";
    }

}
