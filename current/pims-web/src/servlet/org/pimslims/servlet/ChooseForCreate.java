/**
 * 
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.presentation.AttributeToHTML;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.create.AttributeValueMap;
import org.pimslims.presentation.create.RoleHooksHolder;
import org.pimslims.search.Paging;

/**
 * Edit a role for an object that is under creation e.g.
 * /ChooseForCreate/org.pimslims.model.sample.Sample/refSample
 * 
 * @author cm65
 * 
 */
public class ChooseForCreate extends PIMSServlet {

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Manage an association";
    }

    private static final java.util.regex.Pattern PATH_INFO = java.util.regex.Pattern
        .compile("^/(.*?)/([a-zA-Z0-9]+)$");

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
        final String pathInfo = this.getPathInfo(request);
        if (null == pathInfo) {
            throw new ServletException("No class to create");
        }
        final Matcher matcher = ChooseForCreate.PATH_INFO.matcher(pathInfo);

        if (!matcher.matches()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Object and role not specified");
            return;
        }
        final String metaClassName = matcher.group(1);
        final org.pimslims.metamodel.MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        final String metaRoleName = matcher.group(2);
        MetaRole metaRole = null;
        try {
            metaRole = metaClass.getMetaRole(metaRoleName);
        } catch (final IllegalArgumentException e) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print(
                metaClass.getMetaClassName() + "s do not have an association: " + metaRoleName);
            return;
        }

        //TODO no need for this, send header from JSP as normal
        final String otherName = ServletUtil.getDisplayName(metaRole.getOtherMetaClass());
        this.writeHead(request, response, "Choose " + otherName + " for new " + metaClass.getAlias());

        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }
        try {

            // new style search
            final Paging paging =
                QuickSearch.getPaging(request.getParameterMap(), metaRole.getOtherMetaClass());
            final Map criteria = QuickSearch.getOnlyCriteria(version, metaClass, request.getParameterMap()); // is this used?
            final String search_all = request.getParameter(QuickSearch.SEARCH_ALL);
            final List<ModelObjectBean> beans =
                QuickSearch.search(version, metaRole.getOtherMetaClass(), paging,
                    QuickSearch.getOnlyCriteria(version, metaClass, request.getParameterMap()), search_all);

            // prepare the controls for the potential associates
            final int hi = metaRole.getHigh();
            String otherRoleName = null; // reverse role
            // int otherLow = 0; // min number of associates for other
            int otherHi = -1; // max number of associates for other
            final MetaRole otherRole = metaRole.getOtherRole();
            if (null != otherRole) {
                // this role is navigable both ways
                otherRoleName = metaRole.getOtherRole().getRoleName(); // reverse
                // role
                // otherLow = metaRole.getOtherRole().getLow(); // max number of
                // associates for other
                otherHi = otherRole.getHigh(); // max number of associates for
                // other
            }
            final Map<String, String> addControl = new java.util.HashMap();
            String type = "checkbox";
            if (1 == hi) {
                type = "radio";
            }
            for (final Iterator iter = beans.iterator(); iter.hasNext();) {
                final ModelObjectBean result = (ModelObjectBean) iter.next();

                if (otherHi == 1 && null != result.getValues().get(otherRoleName)) {
                    // can't add this one
                    addControl.put(result.getHook(),
                        EditRole.getAddControl(result.getHook(), "title=\"In Use\"", "disabled", type));
                } else {
                    // this can be added, make enabled check box
                    addControl.put(result.getHook(),
                        EditRole.getAddControl(result.getHook(), "title=\"Add?\"", "", type));
                }

            }

            // call JSP to show search form and results
            QuickSearch.prepareSearchForm(request, version, metaRole.getOtherMetaClass(), criteria);
            request.setAttribute(
                "resultSize",
                QuickSearch.getCountOfResults(version, metaRole.getOtherMetaClass(), criteria,
                    request.getParameter(QuickSearch.SEARCH_ALL)));

            request.setAttribute("metaClass", metaClass);
            request.setAttribute("displayName", ServletUtil.getDisplayName(metaClass));
            request.setAttribute("otherName", otherName);
            request.setAttribute("metaRole", ServletUtil.getPIMSMetaRole(metaRole));
            request.setAttribute("searchMetaClass",
                ServletUtil.getPIMSMetaClass(metaRole.getOtherMetaClass()));
            request.setAttribute("actions", addControl);
            request.setAttribute("parametersString", request.getParameter(AttributeToHTML.ACTION));
            if (!metaRole.getOtherMetaClass().isAbstract() && // dont drive
                // the user
                // round in
                // circles:
                !ServletUtil.containsRequiredRoles(metaRole.getOtherMetaClass())) {
                request.setAttribute("createNewLink", "ok");
            }

            QuickSearch.setPagingAttributes(request, paging);

            request.setAttribute("beans", beans);
            final int totalRecords = QuickSearch.getCountOfRecords(version, metaRole.getOtherMetaClass());
            request.setAttribute("totalRecords", totalRecords);
            QuickSearch.passDisplayAttributes(request, metaClass, beans);
            QuickSearch.passSearchAttributes(request, metaClass, version, totalRecords);

            //TODO add header to JSP and use forward instead of include
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/ChooseForCreate.jsp");
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

        // /org.pimslims.model.target.Target/genomicProjects
        // String pathInfo = request.getPathInfo(); Alternative way of getting
        // Metaclass & role info
        // System.out.println(this.getClass().getName()+".doPost()");
        final String metaClassName = request.getParameter(Create.PARM_METACLASSNAME);
        final String metaRoleName = request.getParameter("METAROLENAME");

        String parameters = request.getParameter("parametersString");

        try {
            parameters = java.net.URLDecoder.decode(parameters, "UTF-8");

            final RoleHooksHolder rl = new RoleHooksHolder(metaClassName);
            if (parameters != null && parameters.length() != 0) {
                rl.parse(parameters);
            }
            final AttributeValueMap av = new AttributeValueMap(metaClassName, parameters);

            final String[] hooks = request.getParameterMap().get("add");
            if (hooks != null && hooks.length > 0) {
                rl.addHooks(metaRoleName, hooks);
            }

            PIMSServlet.redirectPost(response, request.getContextPath() + "/Create/" + metaClassName + "?"
                + rl.toDecodedParamString() + "&" + av.toDecodedParamString());

        } catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new ServletException(e);
        }
    }

    protected String getPathInfo(final HttpServletRequest request) {
        return request.getPathInfo();
    }

}
