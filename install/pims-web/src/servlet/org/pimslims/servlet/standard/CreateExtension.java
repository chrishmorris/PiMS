package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.construct.ExtensionBean;
import org.pimslims.presentation.construct.Extensions;
import org.pimslims.servlet.PIMSServlet;

/**
 * Servlet to handle the creation of a new extension
 * 
 */
public class CreateExtension extends PIMSServlet {

    /**
     * Code to satisy Serializable Interface
     */
    private static final long serialVersionUID = -5851882064372940058L;

    /**
     * @return Servlet descriptor string
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "PiMS new extension page";
    }

    //private static final Pattern JUST_ID = Pattern.compile("^\\d+$");

    /**
     * Show the form
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            request.setAttribute("directions", CreateExtension.directions);
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/standard/CreateExtension.jsp");
            dispatcher.forward(request, response);
            version.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    /**
     * Post causes an extension to be created
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // Get a WritableVersion

        final ExtensionBean bean = new ExtensionBean();

        bean.setExName(request.getParameter("name"));
        bean.setExSeq(request.getParameter("sequence"));
        bean.setDirection(request.getParameter("direction"));
        bean.setEncodedTag(request.getParameter("relatedProteinTagSeq"));
        bean.setRestrictionSite(request.getParameter("restrictionSite"));

        String hook = null;

        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final LabNotebook project = version.get(request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID));
            assert project != null : "project should not be null";
            bean.setAccess(new ModelObjectBean(project));

            //TODO hook = ExtensionUtility.save(version, bean);
            hook = Extensions.save(version, bean);
            version.commit();
            // now show the new extension
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    // Mapping for direction to use in create jsp
    public static final java.util.Map<String, String> directions = new HashMap<String, String>();
    static {
        CreateExtension.directions.put("reverse", "Reverse");
        CreateExtension.directions.put("forward", "Forward");
    }

}
