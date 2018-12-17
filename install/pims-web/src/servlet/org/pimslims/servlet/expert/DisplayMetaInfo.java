/*
 * Created on 24.08.2005 - Code Style - Code Templates
 */
package org.pimslims.servlet.expert;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Petr Troshin
 */

@Deprecated
// obsolete
public class DisplayMetaInfo extends PIMSServlet {

    public static final long serialVersionUID = 982372807;

    /**
     * 
     */
    public DisplayMetaInfo() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Display metainformation in a nice manner";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        final java.io.PrintWriter writer = response.getWriter();

        // get the class we are creating,
        // Path is e.g. /Create/org.pimslims.model.people.organisation
        final String pathInfo = request.getPathInfo();
        final HttpSession session = request.getSession();
        if (pathInfo == null || pathInfo.length() < 1) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No type specified");
            return;
        }
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        final MetaClass metaClass = this.getModel().getMetaClass(pathInfo.substring(1));
        if (metaClass == null) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("Unknown type: " + pathInfo.substring(1));
            return;
        }

        try {
            rv.commit();
        } catch (final ConstraintException cex) {
            throw new RuntimeException(cex);
        } catch (final AbortedException abx) {
            throw new RuntimeException(abx);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
        final ServletContext sCont = this.getServletContext();
        final RequestDispatcher dispatcher = sCont.getRequestDispatcher("/JSP/displayMetaInfo.jsp");
        session.setAttribute("_class_", metaClass);
        dispatcher.forward(request, response);

    } // doGet() end

}
