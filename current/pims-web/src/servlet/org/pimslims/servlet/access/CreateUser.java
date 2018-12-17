/*
 * Created on 18.07.2005 TODO Error Messages passing
 */
package org.pimslims.servlet.access;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.people.Person;
import org.pimslims.sec.License;
import org.pimslims.servlet.Create;
import org.pimslims.servlet.PIMSServlet;

/**
 * Create a PiMS user, and an associated Person record.
 * 
 * @author Chris Morris
 */
@javax.servlet.annotation.WebServlet("/Create/org.pimslims.model.accessControl.User")
public class CreateUser extends PIMSServlet {

    /**
     * LICENSE2 String
     */
    static final String LICENSE = "license";

    public static final long serialVersionUID = 1;

    private License license;

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
    public CreateUser() {
        super();
    }

    /**
     * CreateUser.init
     * 
     * @see org.pimslims.servlet.PIMSServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        this.getLicense(config);
    }

    private void getLicense(final ServletConfig config) throws ServletException {
        try {
            this.license = (License) config.getServletContext().getAttribute(CreateUser.LICENSE);
            if (null == this.license) {
                this.license = License.getLicense();
                config.getServletContext().setAttribute(CreateUser.LICENSE, this.license);
            }
        } catch (final IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        PIMSServlet.validatePost(request);

        final java.io.PrintWriter writer = response.getWriter();
        final MetaClass userMetaClass = this.getModel().getMetaClass(User.class.getName());
        final MetaClass personMetaClass = this.getModel().getMetaClass(Person.class.getName());

        final WritableVersion rw = this.getWritableVersion(request, response);
        final int maxUsers = this.license.getMaxUsers();
        if (rw == null) {
            return;
        }
        try {
            final int count = rw.getCountOfAll(this.getModel().getMetaClass(User.class.getName()));
            if (count >= maxUsers && -1 != maxUsers
            /* TODO remove this last bit && 20 != maxUsers */
            ) {
                throw new ServletException("Maximum permitted users: " + maxUsers);
            }
            final User user = (User) CreateUser.create(userMetaClass, request.getParameterMap(), rw);
            final Person person = (Person) CreateUser.create(personMetaClass, request.getParameterMap(), rw);
            user.setPerson(person);
            rw.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + user.get_Hook());
        } catch (final AccessException aex) {
            rw.abort();
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_FORBIDDEN);
            writer.print("You are not allowed to make these changes");
            return;
        } catch (final ConstraintException cex) {
            rw.abort();
            // TODO could use ConstraintException.attributeName to show the
            // error message by the input field
            request.setAttribute("javax.servlet.error.exception", cex);
            request.getRequestDispatcher("/update/ConstraintErrorPage").forward(request, response);
            return;
        } catch (final AbortedException abx) {
            throw new ServletException(abx);
        } finally {
            if (rw != null) {
                if (!rw.isCompleted()) {
                    rw.abort();
                }
            }
        }

    }

    static ModelObject create(final MetaClass metaClass, final Map<String, String[]> parms,
        final WritableVersion rw) throws AccessException, ConstraintException, ServletException {
        String username = null;
        if (null != parms.get(User.class.getName() + ":" + User.PROP_USERGROUPS)) {
            final String[] hooks = parms.get(User.class.getName() + ":" + User.PROP_USERGROUPS);
            if (1 == hooks.length) {
                final UserGroup group = rw.get(hooks[0]);
                if (null != group && null != group.getHeader()) {
                    // group leader can add userids
                    username = group.getHeader().getName();
                }
                if (null != group && null != group.getMaxSize()) {
                    // check there is room for more
                    final Collection<ModelObject> activeMembers =
                        group.findAll(UserGroup.PROP_MEMBERUSERS, User.PROP_ISACTIVE, Boolean.TRUE);
                    if (activeMembers.size() >= group.getMaxSize()) {
                        throw new AssertionError("Group is full");
                    }
                }
            }
        }
        if (!rw.isAdmin() && !rw.getUsername().equals(username)) {
            throw new AssertionError("Creating this new userid is not permitted to user:" + rw.getUsername());
        }
        final Map<String, String[]> myParms = CreateUser.getParms(metaClass.getMetaClassName(), parms);
        final Map<String, Object> params = Create.parseValues(rw, myParms, metaClass, Collections.emptyMap());

        return rw.create(metaClass.getJavaClass(), params);
    }

    /**
     * @param metaClassName the name of the type being edited or created
     * @param parameterMap the HTTP parameters
     * @return the parameters for this metaclass
     */
    private static Map<String, String[]> getParms(final String metaClassName, final Map parameterMap) {
        final Map<String, String[]> ret = new HashMap<String, String[]>(parameterMap.size());
        for (final Iterator iter = parameterMap.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String name = (String) entry.getKey();
            if (name.startsWith(metaClassName)) {
                ret.put(name, (String[]) entry.getValue());
            }
        }
        return ret;
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
            request.getRequestDispatcher("/JSP/create/" + User.class.getName() + ".jsp");
        dispatcher.forward(request, response);

    }

}
