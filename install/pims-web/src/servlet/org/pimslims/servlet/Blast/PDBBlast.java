package org.pimslims.servlet.Blast;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.bioinf.BlastHeaderBean;
import org.pimslims.bioinf.NCBIBlast;
import org.pimslims.presentation.bioinf.BlastMultipleUtility;
import org.pimslims.presentation.bioinf.PimsBlastXmlParser;
import org.pimslims.servlet.PIMSServlet;

/**
 * Create single target call to EBI Web Sevices NCBI Blast against PDB or TargetDB Retrieve results as XML and
 * process. Can be extended for other databases but would need to handle new 'dbToSearch' and add to dbCodes
 * map in BlastMultipleUtility. Create header, hit and alignment beans and forward to BlastResult.jsp The
 * first element in the 'beans' array is a BlastHeaderBean, this is created even when there are no hits.
 * 
 * @author Susy Griffiths YSBL
 * @date May 2007
 * 
 */
public class PDBBlast extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
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

        // Only runs if not file
        final String target_protSeq = request.getParameter("target_protSeq");
        final String target_protName = request.getParameter("target_protName");
        final String target_hook = request.getParameter("target_hook");
        final String dbToSearch = request.getParameter("blast");

        String result = null;
        try {
            result = NCBIBlast.getBlastResult(target_protSeq, dbToSearch);

        } catch (final ConnectException cex) {
            final String message =
                "Cannot connect to the server. This might be because of your proxy settings. Please contact your system administrator.";
            this.showError(request, response, message);
            return;
        } catch (final IOException ioe) {
            this.showError(request, response, "Sorry cannot find target " + target_protName + " in database ");
            return;
        }

        if (result == null || result.equals("")) {
            this.showError(request, response, "Sorry no BLAST results for " + target_protName);
            return;
        }
        // Now parse the result and make the beans
        // List blastBeans = new java.util.ArrayList();
        BlastHeaderBean bhb = new BlastHeaderBean();
        // List<BlastHitBean> bHitBeans = new
        // java.util.ArrayList<BlastHitBean>();
        List blastBeans = new java.util.ArrayList();
        blastBeans = PimsBlastXmlParser.parseXmlString(result);
        bhb = (BlastHeaderBean) blastBeans.get(0);
        bhb.setTargetId(target_protName);
        bhb.setDatabaseSearched(BlastMultipleUtility.dbCodes.get(bhb.getDatabaseSearched()));

        // succeeded
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/bioinf/BlastResult.jsp");
        request.setAttribute("result", result);
        request.setAttribute("target_protName", target_protName);
        request.setAttribute("target_hook", target_hook);
        request.setAttribute("bhb", bhb);
        request.setAttribute("blastBeans", blastBeans);
        request.setAttribute("dbToSearch", dbToSearch);
        rd.forward(request, response);

    }

    /**
     * TODO pass message as attribute not parameter, this is a security defect
     * 
     * @param request
     * @param response
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    private void showError(final HttpServletRequest request, final HttpServletResponse response,
        final String message) throws ServletException, IOException {
        this.log("##########" + message);
        final RequestDispatcher rd = request.getRequestDispatcher("/BlastResult?message=" + message);
        rd.forward(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Run WUBlast for Target protein sequence against PDB";
    }

}
