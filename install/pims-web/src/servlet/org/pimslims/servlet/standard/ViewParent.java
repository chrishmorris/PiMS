package org.pimslims.servlet.standard;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.servlet.PIMSServlet;

public class ViewParent extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "redirects to parent object";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {

        // ensure that PIMS was able to connect to the database
        if (!checkStarted(request, response)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            writeErrorHead(request, response, "Sample must be specified", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        ReadableVersion version = getReadableVersion(request, response);
        try {
            MetaClass metaClass = null; // type to show, if any
            ModelObject object = version.get(pathInfo); // e.g.
            // ViewParent/org.pimslims.model.experiment.Experiment:42355
            if (null == object) {
                writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            metaClass = object.get_MetaClass();
            MetaRole parentRole = ((MetaClassImpl) metaClass).getParentRole();
            if ("project".equals(parentRole.getRoleName())) {
                parentRole = null; // ignore memops project
            }
            if (null == parentRole) {
                throw new ServletException("No parent role for: " + metaClass.getMetaClassName());
            }

            Collection<ModelObject> parents = parentRole.get(object);
            if (1 != parents.size()) {
                throw new ServletException("No parent for: " + pathInfo);
            }
            ModelObject parent = parents.iterator().next();
            String parentHook = parent.get_Hook();
            version.commit();
            redirect(response, request.getContextPath() + "/View/" + parentHook);
            //RequestDispatcher dispatcher = request.getRequestDispatcher("/View/" + parentHook);
            //dispatcher.forward(request, response);
        } catch (final AbortedException e1) {
            throw new ServletException("Please try again", e1);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            } // tidy up the transaction
        }
    }

}
