package org.pimslims.servlet.access;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PermissionReader;
import org.pimslims.presentation.PermissionWriter;
import org.pimslims.servlet.PIMSServlet;

public class Permission extends PIMSServlet {

    public Permission() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Edit permissions";
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

        if (!this.checkStarted(request, response)) {
            return;
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final PermissionReader reader = new PermissionReader(version);

            final List<ModelObjectShortBean> notebooks = reader.getOwners();
            request.setAttribute("notebooks", notebooks);

            // get groups and owners from request
            final Map<String, Boolean> toView = new HashMap();
            String[] groupHooks = request.getParameterValues("userGroup");
            String[] ownerHooks = request.getParameterValues("owner");
            if (null == ownerHooks && null != groupHooks) {
                ownerHooks = reader.getOwnersForGroups(groupHooks);
            }
            if (null == groupHooks && null != ownerHooks) {
                groupHooks = reader.getGroupsForOwners(ownerHooks);
            }

            // tell the JSP what to display
            if (null != groupHooks) {
                for (int i = 0; i < groupHooks.length; i++) {
                    toView.put(groupHooks[i], Boolean.TRUE);
                }
            }
            if (null != ownerHooks) {
                for (int i = 0; i < ownerHooks.length; i++) {
                    toView.put(ownerHooks[i], Boolean.TRUE);
                }
            }

            final List<org.pimslims.presentation.UserGroupBean> groups = reader.getGroups();
            request.setAttribute("groups", groups);
            request.setAttribute("toView", toView);
            request.setAttribute("hasSomeEntries", ownerHooks != null && groupHooks != null);
            version.commit();

            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/access/Permission.jsp");
            dispatcher.forward(request, response);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final Collection<UserGroup> groups = new HashSet();
            String[] hooks = request.getParameterValues("group");
            for (int i = 0; i < hooks.length; i++) {
                groups.add((UserGroup) version.get(hooks[i]));
            }
            final Collection<LabNotebook> owners = new HashSet();
            hooks = request.getParameterValues("notebook");
            for (int i = 0; i < hooks.length; i++) {
                owners.add((LabNotebook) version.get(hooks[i]));
            }
            final PermissionWriter writer = new PermissionWriter(version, owners, groups);
            // the form is all check boxes, so we don't need the parameter
            // values, just the names
            final Collection<String> parameters = request.getParameterMap().keySet();
            writer.save(parameters);
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

}
