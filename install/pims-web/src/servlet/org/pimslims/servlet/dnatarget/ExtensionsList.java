package org.pimslims.servlet.dnatarget;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.presentation.construct.ExtensionBean;
import org.pimslims.presentation.construct.Extensions;
import org.pimslims.servlet.PIMSServlet;

/**
 * Servlet implementation class for Servlet: ExtensionsList
 * 
 */
public class ExtensionsList extends PIMSServlet {
    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public ExtensionsList() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Prepare the list of Extensions and html header, then forward to the presentation page";
    }

    /* (non-Java-doc)
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!this.checkStarted(request, response)) {
            return;
        }

        final PrintWriter writer = response.getWriter();
        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }

        try {
            final ComponentCategory forwardExtension =
                rv.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, "Forward Extension");
            final ComponentCategory reverseExtension =
                rv.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME, "Reverse Extension");
            if (null == forwardExtension || null == reverseExtension) {
                throw new ServletException(
                    "Please load reference data: sample categories for extensions not found.");
            }
            request.setAttribute("forwardExtension", forwardExtension.getDbId());
            request.setAttribute("reverseExtension", reverseExtension.getDbId());

            final Collection<ExtensionBean> fexs = Extensions.makeExtensionBeans(rv, "Forward");
            final Collection<ExtensionBean> rexs = Extensions.makeExtensionBeans(rv, "Reverse");

            request.setAttribute("fexs", fexs);
            request.setAttribute("rexs", rexs);

            final ServletContext sCont = this.getServletContext();
            final RequestDispatcher dispatcher =
                sCont.getRequestDispatcher("/JSP/dnaTarget/ListExtensions.jsp");
            dispatcher.forward(request, response);
            rv.commit();
        } catch (final AbortedException e1) {
            this.writeHead(request, response, "Extensions");
            writer.print(" Sorry, there has been a problem, please try again.");
            PIMSServlet.writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
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
}
