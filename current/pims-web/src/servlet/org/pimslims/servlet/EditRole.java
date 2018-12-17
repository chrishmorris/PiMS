/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.search.Paging;

/**
 * Edit a many-many association e.g. EditRole/org.pimslims.model.target.Target:2345/molecule
 * 
 * @author cm65
 * 
 */
public class EditRole extends PIMSServlet {

    /**
     * IS_IN_MODAL_WINDOW String
     */
    @Deprecated
    // version that is not in modal window is no longer used, and does not work
    private static final String IS_IN_MODAL_WINDOW = "isInModalWindow";

    /**
     * DISABLED String
     */
    private static final String DISABLED = "disabled=\"disabled\"";

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Manage an association";
    }

    protected static final java.util.regex.Pattern PATH_INFO = java.util.regex.Pattern
        .compile("^/(.*?:\\d+)/([a-zA-Z0-9]+)$");

    protected String getRequestJSP() {
        return "/JSP/EditRole.jsp";
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

        final java.io.PrintWriter writer = response.getWriter();
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
            final String displayName = ServletUtil.getDisplayName(metaClass);
            MetaRole metaRole = null;
            try {
                metaRole = metaClass.getMetaRole(metaRoleName);
            } catch (final IllegalArgumentException e) {
                version.abort();
                PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(displayName + "s do not have an association: " + metaRoleName);
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
            final int hi = metaRole.getHigh();
            String otherRoleName = null; // reverse role
            int otherLow = 0; // max number of associates for other
            int otherHi = -1; // max number of associates for other
            final MetaRole otherRole = metaRole.getOtherRole();
            if (null != otherRole) {
                // this role is navigable both ways
                otherRoleName = metaRole.getOtherRole().getRoleName(); // reverse
                // role
                otherLow = metaRole.getOtherRole().getLow(); // max number of
                // associates for
                // other
                otherHi = otherRole.getHigh(); // max number of associates for
                // other
            }
            final MetaClass otherMetaClass = metaRole.getOtherMetaClass();
            boolean editingChildRole = false;
            if (metaRole instanceof MetaRoleImpl && ((MetaRoleImpl) metaRole).isChildRole()) {
                editingChildRole = true;
                // can't remove children, have to delete them.
            }

            final java.util.Collection associates = object.get(metaRoleName);

            // prepare the controls for the current associates
            //final boolean mayUpdate = metaRole.isChangeable() && version.mayUpdate(object);
            final ModelObjectBean bean = BeanFactory.newBean(object);
            final boolean mayUpdate = metaRole.isChangeable() && bean.getMayUpdate();

            final Map<String, String> removeControl = new java.util.HashMap();

            //TODO compare this with EditRole.jsp. whjich seems to override this
            if (editingChildRole && version.mayUpdate(object)) {
                request.setAttribute("controlHeader", "Delete?");
            } else if (mayUpdate) {
                request.setAttribute("controlHeader", "Remove?");
            } else {
                request.setAttribute("controlHeader", ""); // no controls column
            }
            for (final Iterator iter = associates.iterator(); iter.hasNext();) {
                final ModelObject associate = (ModelObject) iter.next();
                final String associateHook = associate.get_Hook();
                if (editingChildRole && mayUpdate) {
                    removeControl.put(associateHook,
                        EditRole.getDeleteControl(associateHook, "title=\"Delete?\"", ""));
                } else if (otherLow > 0 && otherLow == associate.get(otherRoleName).size()) {
                    removeControl.put(associateHook,
                        this.getRemoveControl(associateHook, "title=\"can not remove\"", EditRole.DISABLED));
                } else {
                    removeControl.put(associateHook,
                        this.getRemoveControl(associateHook, "title=\"Remove?\"", ""));
                }
            }
            request.setAttribute("removeControl", removeControl); // I think no longer used __Chris

            // search and show search results
            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), otherMetaClass);
            //final MetaClass searchType = metaRole.getOtherMetaClass();
            final Map<String, Object> criteria =
                QuickSearch.getOnlyCriteria(version, metaClass /*TOTO should that be otherMetaClass? */,
                    request.getParameterMap());
            final String search_all = request.getParameter(QuickSearch.SEARCH_ALL);
            final Collection<ModelObjectBean> results =
                QuickSearch.search(version, metaRole.getOtherMetaClass(), paging, criteria, search_all);

            // prepare the controls for the potential associates
            String type = "checkbox";
            if (1 == hi) {
                type = "radio";
            }

            //final Collection disabledResult = new HashSet();
            //final Collection enabledResult = new HashSet();
            final Map addResult = new LinkedHashMap();
            final Map addControl = new LinkedHashMap(); // this is passed to list jsp as "actions"
            request.setAttribute("addControl", addControl);
            request.setAttribute("addResult", addResult);
            for (final Iterator iter = results.iterator(); iter.hasNext();) {
                final ModelObjectBean result = (ModelObjectBean) iter.next();
                final String foundHook = result.getHook();
                if (associates.size() == hi) {
                    if (mayUpdate && 1 == hi
                        && "true".equals(request.getParameter(EditRole.IS_IN_MODAL_WINDOW))) {
                        // PIMS-3527 See listItemControl.tag.
                        addControl.put(foundHook, "replace");
                        addResult.put(foundHook, true);
                        //enabledResult.add(result);
                    } else {
                        addControl.put(foundHook, EditRole.getAddControl(foundHook,
                            "title=\"you must remove before adding\"", EditRole.DISABLED, type));
                        addResult.put(foundHook, false);
                        //disabledResult.add(result);
                    }
                } else if (foundHook.equals(object.get_Hook())) {
                    // PiMS 3044 UI allows adding a location to itself
                    addControl.put(foundHook, EditRole.getAddControl(foundHook,
                        "title=\"Cannot add to oneself\"", EditRole.DISABLED, type));
                    addResult.put(foundHook, false);
                    //disabledResult.add(result);

                } else if (removeControl.containsKey(foundHook)) {
                    addControl
                        .put(foundHook, EditRole.getAddControl(foundHook, "title=\"Already added\"",
                            EditRole.DISABLED, type));
                    addResult.put(foundHook, false);
                    //disabledResult.add(result);
                } else if (otherHi != -1 && this.isFullUp(otherRoleName, otherHi, result)) {
                    // can't add this one
                    addControl.put(foundHook,
                        EditRole.getAddControl(foundHook, "title=\"In Use\"", EditRole.DISABLED, type));
                    addResult.put(foundHook, false);
                    //disabledResult.add(result);
                } else {
                    // this can be added, make enabled check box
                    if (mayUpdate) {
                        if ("true".equals(request.getParameter(EditRole.IS_IN_MODAL_WINDOW))) {
                            // See listItemControl.tag.
                            if (1 == hi) {
                                addControl.put(foundHook, "modalSingleAdd");
                            } else {
                                addControl.put(foundHook, "modalMultiAdd");
                            }
                        } else {
                            addControl.put(foundHook,
                                EditRole.getAddControl(foundHook, "title=\"Add?\"", "", type));
                        }
                        addResult.put(foundHook, true);
                    } else {
                        addControl.put(foundHook, EditRole.getAddControl(foundHook,
                            "title=\"You don't have permission to update this\"", EditRole.DISABLED, type));
                        addResult.put(foundHook, false);
                    }
                    //enabledResult.add(result);
                }
            }

            //} else { // role is not changeable
            //    request.setAttribute("addControl", java.util.Collections.EMPTY_MAP);
            //}
            // call JSP to show search form and results
            QuickSearch.prepareSearchForm(request, version, metaRole.getOtherMetaClass(), criteria);
            request.setAttribute(
                "resultSize",
                QuickSearch.getCountOfResults(version, metaRole.getOtherMetaClass(), criteria,
                    request.getParameter(QuickSearch.SEARCH_ALL)));

            //request.setAttribute("modelObject", ModelObjectView.getModelObjectView(object));
            request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));
            request.setAttribute("searchMetaClass", ServletUtil.getPIMSMetaClass(otherMetaClass));
            request.setAttribute("metaRole", ServletUtil.getPIMSMetaRole(metaRole));
            request.setAttribute("displayName", displayName);

            //request                .setAttribute("removeResults", ModelObjectView.getModelObjectViews(associates, true, null));
            QuickSearch.passDisplayAttributes(request, metaClass, results);
            request.setAttribute("addResults", results);
            //final Collection attributes = new HashSet(ServletUtil.getFilledAttributeNames(results));
            //attributes.addAll(ServletUtil.getFilledAttributeNames(associates));

            ListRole.passAttributeNames(request, metaClass, otherRoleName, results);

            request.setAttribute("bean", BeanFactory.newBean(object));
            QuickSearch.setPagingAttributes(request, paging);

            // show header
            this.writeHead(request, response, "Add " + ServletUtil.getPIMSMetaRole(metaRole).getAlias()
                + " for " + displayName + ": " + object.get_Name());

            final RequestDispatcher dispatcher = request.getRequestDispatcher(this.getRequestJSP());
            dispatcher.include(request, response);
            version.commit();
        } catch (final AbortedException e1) {
            this.log("list aborted", e1);
            writer.print("Sorry, there has been a problem, please try again.");
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        PIMSServlet.writeFoot(writer, request);
    }

    private boolean isFullUp(final String otherRoleName, final int otherHi, final ModelObjectBean result) {
        final Object associates = result.getValues().get(otherRoleName);
        if (null == associates) {
            return false;
        }
        if (associates instanceof ModelObjectShortBean) {
            return 1 == otherHi;
        }
        return otherHi == ((Collection) associates).size();
    }

    /*
     * (non-Javadoc)
     *
     * @see http://jakarta.apache.org/commons/fileupload/apidocs/index.html
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
        }

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
        final org.pimslims.dao.WritableVersion version = this.getWritableVersion(request, response);

        final org.pimslims.metamodel.ModelObject object =
            this.getRequiredObject(version, request, response, hook);
        if (object == null) {
            return; // error mesasge was provided by getRequiredObject
        }
        if (!version.mayUpdate(object)) {
            this.writeErrorHead(request, response, "You are not allowed to do that",
                HttpServletResponse.SC_FORBIDDEN);
            final PrintWriter writer = response.getWriter();
            writer.print("User: " + version.getUsername() + " is not allowed to update record: "
                + object.get_Name());
            PIMSServlet.writeFoot(writer, request);
            return;
        }
        final org.pimslims.metamodel.MetaClass metaClass = object.get_MetaClass();
        final String displayName = ServletUtil.getDisplayName(metaClass);
        final MetaRole metaRole = metaClass.getMetaRole(metaRoleName);
        if (null == metaRole) {
            version.abort();
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(displayName + "s do not have an association: " + metaRoleName);
            return;
        }

        try {
            // add, remove, and delete
            final Collection remove =
                ServletUtil.getModelObjectsByHooks(version, request.getParameterValues("remove"));
            object.remove(metaRoleName, remove);
            MRUController.addObjects(version.getUsername(), remove);
            final Collection delete =
                ServletUtil.getModelObjectsByHooks(version, request.getParameterValues("delete"));
            version.delete(delete);
            final Collection add =
                ServletUtil.getModelObjectsByHooks(version, request.getParameterValues("add"));
            object.add(metaRoleName, add);
            MRUController.addObjects(version.getUsername(), add);

            /*
             * PiMS 1538 Construct Primers should be set automatically in PCR Experiment
             *           when Construct is selected
             */
            if (object instanceof org.pimslims.model.experiment.Experiment
                && metaRoleName.equals("researchObjective") && !add.isEmpty()) {

                for (final Object mob : add) {
                    if (mob instanceof org.pimslims.model.target.ResearchObjective) {
                        final ResearchObjective exp = (ResearchObjective) mob;
                        ExperimentUtility.setExpBlueprintSamples((Experiment) object, exp);
                    }
                }
            }

            /*
             * PiMS 2043 change to natural source of a target doesn't filter down 
             *           to abstractcomponents
             */
            if (object instanceof org.pimslims.model.target.Target
                && metaRoleName.equals(Target.PROP_SPECIES)) {

                System.out.println("org.pimslims.servlet.EditRole PiMS 2043 [" + object.getClass().getName()
                    + ":" + metaRoleName + "]");

                final Target target = (Target) object;
                final Molecule protein = target.getProtein();
                protein.remove(Substance.PROP_NATURALSOURCE, remove);
                protein.add(Substance.PROP_NATURALSOURCE, add);
            }

            version.commit();
            // show results
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + object.get_Hook());
        } catch (final AccessException e) {
            throw new ServletException(e);
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

    /**
     * @param hook represents the object to be controlled
     * @param title e.g. title=\"\"
     * @param disabled "" or "disabled"
     * @return
     */
    protected final String getRemoveControl(final String hook, final String title, final String disabled) {
        return "<input " + disabled + " " + title + " type=\"checkbox\" name=\"remove\" value=\"" + hook
            + "\" />";
    }

    /**
     * @param hook represents the object to be controlled
     * @param title e.g. title=\"\"
     * @param disabled "" or "disabled"
     * @return
     */
    @Deprecated
    // moved to ListRole
    protected static final String getDeleteControl(final String hook, final String title,
        final String disabled) {
        return "<input " + disabled + " " + title + " type=\"checkbox\" name=\"delete\" value=\"" + hook
            + "\" />";
    }

    /**
     * @param hook represents the object to be controlled
     * @param title e.g. title=\"\"
     * @param disabled "" or "disabled"
     * @return
     */
    public static String getAddControl(final String hook, final String title, final String disabled,
        final String type) {
        if ("".equals(disabled)) {

            return "<span " + title + "><input type=\"" + type + "\" name=\"add\" value=\"" + hook
                + "\" /></span>";
        } else {

            return "<span " + title + "><input " + disabled + " type=\"" + type + "\" name=\"add\" value=\""
                + hook + "\" /></span>";
        }
    }
}
