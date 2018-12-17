package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.presentation.worklist.ConstructProgressBean;
import org.pimslims.presentation.worklist.ExpBlueprintDao;
import org.pimslims.servlet.PIMSServlet;

@Deprecated
// obsolete
public class WeekOldConstructs extends PIMSServlet {

    /**
     * @baseURL /read/WeekOldSamples
     */
    public WeekOldConstructs() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public final String getServletInfo() {
        return "construct with no progress brick";
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
            final List<ConstructProgressBean> expbs =
                ExpBlueprintDao.getNoProgressExpBlueprint(version, 7, null, null, 10);

            request.setAttribute("ConstructProgressBeans", expbs);
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/construct/constructList.jsp");
            // headers already sent
            rd.include(request, response);
        } finally {
            version.abort();
        }
    }
}
