/*
 * Created on Jan-2007 @author: Peter Troshin (mostly copied form delete servlet)
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AccessException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;

/**
 * Deletes a model object, if permitted See dummy_deleteform Not appropriate on the view page of the object to
 * be deleted
 */
public class DeleteAndBackToReferer extends PIMSServlet {

    /**
     * 
     */
    public DeleteAndBackToReferer() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "Deletes an object from the PIMS database";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    //TODO this code is very similar with Delete.java
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);

        String hook = request.getQueryString();
        if (null == hook) {
            hook = request.getParameter("hook");
        }
        hook = URLDecoder.decode(hook, "UTF-8");
        final org.pimslims.dao.WritableVersion version = this.getWritableVersion(request, response);

        final org.pimslims.metamodel.ModelObject object =
            this.getRequiredObject(version, request, response, hook);
        if (object == null) {
            return;
        }
        final MetaClass metaClass = object.get_MetaClass();
        final String displayName = ServletUtil.getDisplayName(metaClass);
        final String name = object.get_Name();
        final String pathName = request.getContextPath(); // usuallly /pims
        try {
            // special delete for experimentgroup

            if (metaClass.getJavaClass().getSimpleName().equals(ExperimentGroup.class.getSimpleName())) {
                PlateExperimentUtility.deleteExpGroup(version, object.get_Hook());
            }
            version.delete(object);
            MRUController.deleteObject(object.get_Hook());
        } catch (final AccessException ex) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(
                PIMSServlet.getUsername(request) + ", you are not allowed to delete: " + object.get_Name());
            version.abort();
            return;
        } catch (final org.pimslims.exception.ConstraintException ex) {
            this.writeErrorHead(request, response, "Error deleting " + name, HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print(
                "<p class=error>The record of <a href=\"View/" + hook + "\">" + name
                    + "</a> cannot currently be deleted, because it is associated with other records</p>"
                    + "<a href=\"" + pathName + "/Search/" + metaClass.getMetaClassName() + "\">View "
                    + displayName + "s</a>");
            version.abort();
            return;
        }

        if (this.commit(request, response, version)) {
            System.out.println("commit successed!");

            PIMSServlet.redirectPostToReferer(request, response);
        }

    }
}
