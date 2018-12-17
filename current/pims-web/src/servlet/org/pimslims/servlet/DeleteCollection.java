/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.WritableVersion;
import org.pimslims.presentation.ServletUtil;

/**
 * @author Petr Troshin
 * 
 */
public class DeleteCollection extends PIMSServlet {

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Delete a collection of records specified by hooks";
    }

    /**
     * One type of record only can be deleted at a time, this type is specified in the query string Please use
     * this only if you need to delete one type of object at a time, as related object will not be deleted. It
     * is therefore recommended to use specifically developed delete for plate experiments.
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        if (!checkStarted(request, response)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        String type = null;
        if (null != pathInfo && 0 < pathInfo.length()) {
            type = pathInfo.substring(1); // e.g.
            // Search/org.pimslims.model.experiment.Experiment
        }
        WritableVersion version = getWritableVersion(request, response);
        if (version == null) {
            return;
        }

        MetaClass meta = version.getModel().getMetaClass(type);

        PrintWriter writer = response.getWriter();
        if (type == null || meta == null) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("No type or wrong type specified in request ");
            return;
        }

        // get selected hooks
        Collection<String> hooks =
            ServletUtil.getSelectedHooks(request.getParameterMap(), meta.getMetaClassName());

        // return when not exp selected
        if ((hooks == null || hooks.size() == 0)) {
            writeHead(request, response, "Nothing Selected");
            writer.print("Please select items to delete first!");
            writeFoot(writer, request);
            return;
        }

        try {
            Collection<ModelObject> mObjs = new ArrayList<ModelObject>();
            for (String hook : hooks) {
                ModelObject obj = version.get(hook);
                // Ignore if any of selected records for deletion are not of a type specified
                if (!meta.getJavaClass().isInstance(obj)) {
                    continue;
                }
                mObjs.add(obj);
            }
            version.delete(mObjs);
            version.commit();
        } catch (final AbortedException e1) {
            writeHead(request, response, "Oops...");
            writer.print(" Sorry, there has been a problem, please try again.");
            writeFoot(writer, request);
        } catch (final ConstraintException e1) {
            throw new RuntimeException(e1);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        redirect(response, getReferer(request));
    }
}
