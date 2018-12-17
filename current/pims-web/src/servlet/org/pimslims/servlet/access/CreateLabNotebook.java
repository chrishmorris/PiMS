/*
 * Created on 18.07.2005 TODO Error Messages passing
 */
package org.pimslims.servlet.access;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.servlet.PIMSServlet;

/**
 * 
 * @author Chris Morris
 */
@javax.servlet.annotation.WebServlet("/Create/org.pimslims.model.core.LabNotebook")
public class CreateLabNotebook extends PIMSServlet {

    public static final long serialVersionUID = 1;

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Create a user and person record";
    }

    /**
     * 
     */
    public CreateLabNotebook() {
        super();
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);

        final WritableVersion rw = this.getWritableVersion(request, response);
        if (rw == null) {
            return;
        }
        try {
            final Map<String, String[]> parms = request.getParameterMap();
            final String name = request.getParameter(LabNotebook.class.getName() + ":name");
            final boolean mustCreateDataOwner = true;
            final String[] createUserGroup = parms.get("UserGroup");
            final boolean mustCreateUserGroup = null != createUserGroup && 0 < createUserGroup.length;
            final String[] createLeader = parms.get("Leader");
            final boolean mustCreateLeader = null != createLeader && 0 < createLeader.length;

            Integer maxSize = null;
            final String parameter = request.getParameter("maxSize");
            if (null != parameter && !"".equals(parameter)) {
                maxSize = Integer.decode(parameter);
            }
            CreateLabNotebook.create(rw, name, mustCreateDataOwner, mustCreateUserGroup, mustCreateLeader,
                request.getParameter("password"), maxSize);

            final ModelObject owner = rw.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, name);
            final ModelObject group = rw.findFirst(UserGroup.class, UserGroup.PROP_NAME, name);
            rw.commit();

            if (null == group) {
                PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + owner.get_Hook());
                return;
            }
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + group.get_Hook());
        } catch (final ConstraintException cex) {
            rw.abort();
            cex.printStackTrace();
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (rw != null) {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
            }
        }

    }

    public static void create(final WritableVersion rw, final String name, final boolean mustCreateDataOwner,
        final boolean mustCreateMemberGroup, final boolean mustCreateLeader, final String password,
        final Integer maxSize) throws ConstraintException, AccessException {
        final LabNotebook owner = new LabNotebook(rw, name);
        UserGroup group = null;
        if (mustCreateMemberGroup) {
            group = new UserGroup(rw, name);
            group.setMaxSize(maxSize);
            new Permission(rw, "create", owner, group);
            new Permission(rw, "read", owner, group);
            new Permission(rw, "update", owner, group);
            new Permission(rw, "delete", owner, group);
            if (!mustCreateLeader) {
                new Permission(rw, "unlock", owner, group);
            }
        }
        if (mustCreateLeader) {
            final User leader = new User(rw, name);
            final UserGroup leaderGroup = new UserGroup(rw, name + " Leaders");
            new Permission(rw, "create", owner, leaderGroup);
            new Permission(rw, "read", owner, leaderGroup);
            new Permission(rw, "update", owner, leaderGroup);
            new Permission(rw, "delete", owner, leaderGroup);
            new Permission(rw, "unlock", owner, leaderGroup);
            group.setHeader(leader);
            leaderGroup.addMemberUser(leader);
            if (null != password) {
                leader.setDigestedPassword(password);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @baseURL org.pimslims.model.target.Target:molecule?molecule=hook,hook,hook&OtherMolecules=hook,hook
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        final RequestDispatcher dispatcher =
            request.getRequestDispatcher("/JSP/create/" + LabNotebook.class.getName() + ".jsp");
        dispatcher.forward(request, response);

    }

}
