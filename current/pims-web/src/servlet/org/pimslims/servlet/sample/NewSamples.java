/**
 * 
 */
package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.presentation.worklist.SampleBean;
import org.pimslims.presentation.worklist.SampleCriteria;
import org.pimslims.presentation.worklist.SampleDAO;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author cm65
 * 
 */
public class NewSamples extends PIMSServlet {

    /**
     * @baseURL /read/WeekOldSamples
     */
    public NewSamples() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public final String getServletInfo() {
        return "new samples brick";
    }

    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String username = PIMSServlet.getUsername(request);
        if (null == username) {
            return; // not logged on yet
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final List<SampleBean> sampleBeans =
                SampleDAO.getSampleBeanList(version, SampleCriteria.NEW_SAMPLE_SEARCH);
            request.setAttribute("sampleBeans", sampleBeans);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/sample/sampleList.jsp");
            rd.forward(request, response);
        } finally {
            version.abort();
        }
    }

}
