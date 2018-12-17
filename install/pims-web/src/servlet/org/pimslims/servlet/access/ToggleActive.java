package org.pimslims.servlet.access;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.presentation.CsrfDefence;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.PasswordChange;

/**
 * @author Chris Morris
 * 
 *         Change the active/inactive status of a user Security issue: This functionality is only available to
 *         the administrator, and to a group leader. This restriction is enforced in the code below, changes
 *         should be reviewed.
 */
public class ToggleActive extends PIMSServlet {

    /**
     * PasswordChange.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Get is not supported");
    }

    @Override
    public String getServletInfo() {
        return "allows a the adminstrator or a group leader to make a userid active or inactive";
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

        // This method has to use an administrator version, so do security checks
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            throw new ServletException("attempt to change active status with method: " + request.getMethod());
        }
        if (null == request.getParameter(CsrfDefence.PARAMETER)) {
            throw new ServletException("attempt to change active status with bad form: "
                + request.getMethod());
        } else {
            CsrfDefence.validate(request);
        }
        if (!this.checkStarted(request, response)) {
            return;
        }

        final String username = PIMSServlet.getUsername(request);
        if (null == username) {
            this.writeErrorHead(request, response, "You are not logged in to PiMS",
                HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        final WritableVersion version = this.getModel().getWritableVersion(Access.ADMINISTRATOR);
        if (null == version) {
            this.writeErrorHead(request, response, "Sorry, PIMS is busy, please retry",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return;
        }

        try {
            final String hook = request.getPathInfo().substring(1);
            final User user = (User) this.getRequiredObject(version, request, response, hook);
            if (null == user) {
                // error message already sent
                return;
            }
            final boolean leader =
                PIMSServlet.isAdmin(request)
                    || PasswordChange.isLeader(user.getName(), PIMSServlet.getUsername(request), version);
            if (!leader) {
                throw new ServletException("You are not allowed to change the active status for user: "
                    + user.getName());
            }
            user.setIsActive(Boolean.FALSE.equals(user.getIsActive()));
            // could if (!user.getIsActive()) {user.setDigestedPassword(null)};
            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + user.get_Hook());
        } catch (final AbortedException abe) {
            throw new ServletException(abe);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } finally {
            if (version != null) {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }

        }
    }

    /**
     * PIMSServlet.isLeader
     * 
     * @param request
     * @return
     */
    public static boolean isLeader(final String username, final String sessionUser,
        final ReadableVersion version) {
        if (null == username) {
            return false;
        }
        final User user = version.findFirst(User.class, User.PROP_NAME, username);
        if (null == user) {
            return false;
        }
        final Collection<UserGroup> groups = user.getUserGroups();
        for (final Iterator iterator = groups.iterator(); iterator.hasNext();) {
            final UserGroup group = (UserGroup) iterator.next();
            if (null != group.getHeader() && sessionUser.equals(group.getHeader().getName())) {
                return true;
            }
        }
        return false;
    }
}
