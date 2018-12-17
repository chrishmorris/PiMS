package org.pimslims.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.access.Access;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.presentation.CsrfDefence;

/**
 * @author Chris Morris
 * 
 * 
 */
public class PasswordChange extends PIMSServlet {

    /**
     * PasswordChange.doGet
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ReadableVersion version = this.getModel().getReadableVersion(Access.ADMINISTRATOR);
        if (null == version) {
            this.writeErrorHead(request, response, "Sorry, PIMS is busy, please retry",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            return;
        }

        try {
            request.setAttribute(
                "isLeader",
                PIMSServlet.isAdmin(request)
                    || PasswordChange.isLeader(request.getParameter("username"),
                        PIMSServlet.getUsername(request), version));
        } finally {
            if (version != null) {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }

        }
        final RequestDispatcher rd = request.getRequestDispatcher("/JSP/core/PasswordChange.jsp");
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "allows a user to change her or his own password, or the adminstrator";
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
            throw new ServletException("attempt to change password with method: " + request.getMethod());
        }
        if (null == request.getParameter(CsrfDefence.PARAMETER)) {
            throw new ServletException("attempt to change password with bad form: " + request.getMethod());
        } else {
            CsrfDefence.validate(request);
        }
        if (!this.checkStarted(request, response)) {
            return;
        }

        String username = PIMSServlet.getUsername(request);
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
            final boolean leader =
                PIMSServlet.isAdmin(request)
                    || PasswordChange.isLeader(request.getParameter("username"),
                        PIMSServlet.getUsername(request), version);
            if (leader) {
                username = request.getParameter("username");
            }
            final User user = version.findFirst(User.class, User.PROP_NAME, username);
            if (null == user) {
                this.writeErrorHead(request, response, "No such user: " + username,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (!leader && !user.isPassword(request.getParameter("oldPassword"))) {
                System.out.println("Password change attempted for user: " + username);
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("message", "Please enter your old password");
                final RequestDispatcher rd = request.getRequestDispatcher("/JSP/core/PasswordChange.jsp");
                rd.forward(request, response);
                return;
            }
            user.setDigestedPassword(request.getParameter("newPassword"));
            version.commit();
            this.writeHead(request, response, "Password changed for user: " + username);
        } catch (final AbortedException abe) {
            throw new ServletException(abe);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
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
