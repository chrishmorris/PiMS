/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ServletUtil;

/**
 * Display associates e.g. read/ListRole/org.pimslims.model.target.Target:2345/molecule
 * 
 * This is designed to be used as the src= in a delayed get box
 * 
 * @author cm65
 * 
 */
public class ListRole extends PIMSServlet { //

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show associated records";
    }

    protected static final java.util.regex.Pattern PATH_INFO = java.util.regex.Pattern
        .compile("^/(.*?:\\d+)/([a-zA-Z0-9]+)$");

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String pathInfo = request.getPathInfo();
        if (null == pathInfo) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Object and role not specified");
            return;
        }
        final Matcher matcher = EditRole.PATH_INFO.matcher(pathInfo);
        if (!matcher.matches()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Object and role not specified");
            return;
        }
        final String hook = matcher.group(1);
        final String metaRoleName = matcher.group(2);
        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            final org.pimslims.metamodel.ModelObject object =
                this.getRequiredObject(version, request, response, hook);
            if (object == null) {
                return; // error message was provided by getRequiredObject
            }
            final org.pimslims.metamodel.MetaClass metaClass = object.get_MetaClass();
            MetaRole metaRole = null;
            try {
                metaRole = metaClass.getMetaRole(metaRoleName);
            } catch (final IllegalArgumentException e) {
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(
                    metaClass.getMetaClassName() + "s do not have an association: " + metaRoleName);
                return;
            }

            /*
             * could support custom handlers like this: if
             * (this.getClass().getName().equals("org.pimslims.servlet.ListFiles")) { try {
             * Class.forName("org.pimslims.servlet.editRole."+metaClass.getShortName()); RequestDispatcher
             * dispatcher = request.getRequestDispatcher(
             * "/Search."+metaClass.getShortName()+"/"+metaClass.getMetaClassName() ); dispatcher.forward(request,
             * response); return; } catch (final ClassNotFoundException ex) { // no custom handler, continue } }
             */

            // get the metadata
            String otherRoleName = null; // reverse role
            int otherLow = 0; // max number of associates for other
            /* final int hi = metaRole.getHigh();
            int otherHi = -1; // max number of associates for other*/
            final MetaRole otherRole = metaRole.getOtherRole();
            if (null != otherRole) {
                // this role is navigable both ways
                otherRoleName = metaRole.getOtherRole().getRoleName();
                otherLow = metaRole.getOtherRole().getLow();
                //otherHi = otherRole.getHigh();                 
            }
            //final MetaClass otherMetaClass = metaRole.getOtherMetaClass();
            boolean editingChildRole = false;
            if (metaRole instanceof MetaRoleImpl) {
                if (((MetaRoleImpl) metaRole).isChildRole()) {
                    editingChildRole = true;
                    // can't remove children, have to delete them.
                } else if (otherLow == 1) {
                    editingChildRole = true;
                }
            }

            final java.util.Collection associates = object.get(metaRoleName);

            // prepare the controls for the current associates
            final boolean mayUpdate = metaRole.isChangeable() && version.mayUpdate(object);
            Map<String, String> unlinkActions = new java.util.HashMap();

            /* Do we write a column at all? If so, what header does it have? */
            if (editingChildRole && mayUpdate) {
                request.setAttribute("controlHeader", "Delete?");
            } else if (mayUpdate) {
                request.setAttribute("controlHeader", "Remove?");
            } else {
                request.setAttribute("controlHeader", ""); // no controls column
                unlinkActions = null;
            }

            /* If we're having a column, what icons do we put in it? 
             * 
             * NB this appears to assume that the answer is the same for all associates,
             * i.e., if I can delete one, I can delete them all.
             * 
             * */
            if (null != unlinkActions) {
                for (final Iterator iter = associates.iterator(); iter.hasNext();) {
                    final ModelObject associate = (ModelObject) iter.next();
                    if (null == associate) {
                        continue; // cant view this one
                    }
                    final String associateHook = associate.get_Hook();
                    if (editingChildRole && mayUpdate) {
                        unlinkActions.put(associateHook, "delete");
                    } else if (otherLow > 0 && otherLow == associate.get(otherRoleName).size()) {
                        unlinkActions.put(associateHook, "cant_remove");
                    } else {
                        unlinkActions.put(associateHook, "remove");
                    }
                }
            }
            request.setAttribute("actions", unlinkActions);

            final List<ModelObjectBean> beans = ModelObjectBean.getModelObjectBeans(associates);
            request.setAttribute("beans", beans);

            ListRole.passAttributeNames(request, metaClass, otherRoleName, beans);

            // Pass some extra values that might be useful for some custom lists
            request.setAttribute("pageing", false);
            request.setAttribute("size", beans.size());
            request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));
            request.setAttribute("metaRole", ServletUtil.getPIMSMetaRole(metaRole));
            request.setAttribute("modelObject", BeanFactory.newBean(object));
            version.commit();

            response.setStatus(HttpServletResponse.SC_OK);
            PIMSServlet.dispatchCustomJsp(request, response, metaRole.getOtherMetaClass().getMetaClassName(),
                "list", this.getServletContext());

        } catch (final AbortedException e1) {
            throw new ServletException("Sorry, there has been a problem, please try again.", e1);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static void passAttributeNames(final HttpServletRequest request,
        final org.pimslims.metamodel.MetaClass metaClass, final String otherRoleName,
        final Collection<ModelObjectBean> beans) {
        final List<String> attributeNames = ListRole.getAttributeNames(beans);
        if (null != otherRoleName) {
            // no point listing association with this object
            attributeNames.remove(otherRoleName);
        }
        final Map<String, MetaAttribute> attributes = new TreeMap<String, MetaAttribute>();
        for (final String name : attributeNames) {
            attributes.put(name, metaClass.getAttribute(name)); // getAttribute(name)
        }
        request.setAttribute("attributes", QuickSearch.sortAttributes(attributes));
    }

    /**
     * ListRole.getAttributeNames
     * 
     * @param beans ModelObjectBeans representing the members of the list
     * @return attributes to display
     */
    public static List<String> getAttributeNames(final Collection<ModelObjectBean> beans) {
        final Set<String> attributes = new HashSet();
        for (final Iterator iterator = beans.iterator(); iterator.hasNext();) {
            final ModelObjectBean bean = (ModelObjectBean) iterator.next();
            final Map<String, Object> values = bean.getValues();
            for (final Iterator iterator2 = values.keySet().iterator(); iterator2.hasNext();) {
                final String name = (String) iterator2.next();
                if (!attributes.contains(name) && null != values.get(name) && !"".equals(values.get(name))) {
                    if (bean.getMetaClass().getMetaRoles().containsKey(name)) {
                        continue; // pass role names separately
                    }
                    final MetaAttribute ma = bean.getMetaClass().getAttribute(name);
                    if (null != ma) {
                        final Class type = ma.getClass();
                        if (type == String.class && 0 == ma.getLength()) {
                            continue; // indefinite string, e.g. sequence
                        }
                    }
                    attributes.add(name);
                }
            }
        }

        // don't want to see these standard attributes in a list
        attributes.remove(LabBookEntry.PROP_ACCESS);
        attributes.remove(LabBookEntry.PROP_CREATIONDATE);
        attributes.remove(LabBookEntry.PROP_CREATOR);
        attributes.remove(LabBookEntry.PROP_LASTEDITEDDATE);
        attributes.remove(LabBookEntry.PROP_LASTEDITOR);
        attributes.remove("seqString"); // for TargetBeans
        attributes.remove("name"); // will show pimsWidget:link
        final ArrayList ret = new ArrayList(attributes);
        Collections.sort(ret);
        return ret;
    }

    /**
     * ListRole.doPost
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Not implemented yet");
    }

    /**
     * @param hook represents the object to be controlled
     * @param title e.g. title=\"\"
     * @param disabled "" or "disabled"
     * @return
     */
    protected final String getRemoveControl(final String hook, final String title, final String disabled) {
        return "remove";
    }

}
