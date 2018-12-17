/**
 * V5_0-web org.pimslims.servlet.access ViewUser.java
 * 
 * @author Edward Danael
 * @date June 2014
 * 
 *       Copyright (c) 2013 Edward Daniel The copyright holder has licenced the STFC to redistribute this
 *       software
 */
package org.pimslims.servlet.access;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.json.JSONObject;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.PermissionReader;
import org.pimslims.servlet.PIMSServlet;

/**
 * ViewUser
 * 
 */
public class UserGroups extends PIMSServlet {

    /**
     * ViewUser.doDoGet
     * 
     * @see org.pimslims.servlet.expert.View#doDoGet(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.metamodel.ModelObject, javax.servlet.http.HttpServletRequest)
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

        final PermissionReader reader = new PermissionReader(version);

        //TODO No, this should be the list of LNs that I own, or that a group I am in has admin permission on.
        final List<ModelObjectShortBean> notebooks = reader.getOwners();
        request.setAttribute("notebooks", notebooks);

        //TODO No, this should be a list of all groups I am in plus all public groups, unless I'm an administrator.
        final List<org.pimslims.presentation.UserGroupBean> groups = reader.getGroups();
        request.setAttribute("groups", groups);

        final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/access/Groups.jsp");
        dispatcher.forward(request, response);

        version.abort();

    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final JSONObject jo = new JSONObject();
        final PrintWriter writer = response.getWriter();
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            jo.put("hello", "test");
            //            if (null != request.getParameter("setPermissions")) {
            //                final List<String> toEnable = new LinkedList<String>();
            //                final List<String> toDisable = new LinkedList<String>();
            //                final List<String> pnames = (List<String>) request.getParameterMap().keySet();
            //                final Iterator i = pnames.iterator();
            //                while (i.hasNext()) {
            //                    final String pname = (String) i.next();
            //                    if (pname.matches("^.+:[a-z]+:.+$")) {
            //                        //groupName:permission:labnotebookName
            //                        final String action = request.getParameter(pname);
            //                        if ("enable".equals(action)) {
            //                            toEnable.add(pname);
            //                        } else {
            //                            toDisable.add(pname);
            //                        }
            //                    }
            //                }
            //                this.setPermissions(version, toEnable, toDisable);
            //            }
            //
            //            version.commit();
            //        } catch (final AbortedException e) {
            //            jo = this.JSONError(e.getMessage());
            //            jo.put("trace", e.getStackTrace());
            //        } catch (final ConstraintException e) {
            //            jo = this.JSONError(e.getMessage());
            //            jo.put("trace", e.getStackTrace());
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
            response.setContentType("application/json");
            writer.write(jo.toString());
        }
    }

    /**
     * ViewUser.getServletInfo
     * 
     * @see org.pimslims.servlet.expert.View#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show all Groups of which a user is a member, along with their permissions on his LNs or those he can administer";
    }

    private JSONObject setPermissions(final WritableVersion version, final List<String> toEnable,
        final List<String> toDisable) {

        final JSONObject jo = new JSONObject();

        final Iterator i = toEnable.iterator();
        while (i.hasNext()) {
            final String pname = (String) i.next();
            if (pname.matches("^.+:[a-z]+:.+$")) {
                final String[] parts = pname.split(":");
                final String groupName = parts[0];
                final String permission = parts[1];
                final String labNotebookName = parts[2];
                //TODO Now create the permission
                //If exception, throw it - catch in doPost, see catch blocks for JSON handling 
                final JSONObject changedPermission = new JSONObject();
                changedPermission.put("name", pname);
                changedPermission.put("isEnabled", true);
            }
        }

        final Iterator j = toDisable.iterator();
        while (j.hasNext()) {
            final String pname = (String) j.next();
            if (pname.matches("^.+:[a-z]+:.+$")) {
                final String[] parts = pname.split(":");
                final String groupName = parts[0];
                final String permission = parts[1];
                final String labNotebookName = parts[2];
                //TODO Now remove the permission
                //If exception, throw it - catch in doPost, see catch blocks for JSON handling 
                final JSONObject changedPermission = new JSONObject();
                changedPermission.put("name", pname);
                changedPermission.put("isEnabled", false);
            }
        }

        jo.put("serverTime", System.currentTimeMillis());
        return jo;
    }

    private JSONObject JSONError(final String error) {
        final JSONObject jo = new JSONObject();
        jo.put("error", error);
        return jo;
    }

}
