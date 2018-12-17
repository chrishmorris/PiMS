/**
 * pims-web org.pimslims.servlet.complex EditComplexRole.java
 * 
 * @author Marc Savitsky
 * @date 2 Oct 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.ComplexBeanWriter;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.search.Paging;
import org.pimslims.servlet.EditRole;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

/**
 * EditComplexRole this is wierd - it gives you a choice of ROEs, not of Targets
 */
public class EditComplexRole extends EditRole {

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("EditComplexRole.doGet");
        final java.io.PrintWriter writer = response.getWriter();
        final String pathInfo = request.getPathInfo();
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

            //final ComplexBean complex = ResearchObjectiveElementBean.readComplexHook(version, hook);

            final org.pimslims.metamodel.ModelObject object =
                this.getRequiredObject(version, request, response, hook);
            if (object == null) {
                return; // error mesasge was provided by getRequiredObject
            }
            final org.pimslims.metamodel.MetaClass roMetaClass = object.get_MetaClass();
            //final String displayName = ServletUtil.getDisplayName(metaClass);

            final MetaClass targetMetaClass = this.getModel().getMetaClass(Target.class.getName());

            // show header, since search may take some time
            this.writeHead(request, response, "Add Targets to Complex: " + object.get_Name());

            //final java.util.Collection associates = object.get(metaRoleName);

            // prepare the controls for the current associates
            final boolean mayUpdate = version.mayUpdate(object);

            // search and show search results
            final Paging paging = QuickSearch.getPaging(request.getParameterMap(), targetMetaClass);
            final Map<String, Object> criteria =
                QuickSearch.getOnlyCriteria(version, targetMetaClass, request.getParameterMap());
            //criteria.put("componentType", "target");
            final String search_all = request.getParameter(QuickSearch.SEARCH_ALL);
            final Collection<ModelObjectBean> results =
                QuickSearch.search(version, targetMetaClass, paging, criteria, search_all);

            // prepare the controls for the potential associates
            final String type = "checkbox";
            /* if (1 == hi) {
                 type = "radio";
             } */

            //final Collection disabledResult = new LinkedHashSet();
            final Collection enabledResult = new LinkedHashSet();

            final Map addControl = new LinkedHashMap();
            request.setAttribute("addControl", addControl);

            for (final ModelObjectBean result : results) {

                /* ignore some unattached components seen on mpsi
                if (!result.getValues().containsKey("target")) {
                    continue;
                } */

                //final ModelObjectShortBean target = (ModelObjectShortBean) result.getValues().get("target");

                // this can be added, make enabled check box
                if (mayUpdate) {
                    addControl.put(result.getHook(),
                        EditRole.getAddControl(result.getHook(), "title=\"Add?\"", "", type));
                } else {
                    addControl.put(result.getHook(),
                        EditRole.getAddControl(result.getHook(), "title=\"Add?\"", "disabled", type));
                }
                enabledResult.add(result);
                // }
            }
            //results = new LinkedHashSet();
            final List<ResearchObjectiveElementBeanI> myResults =
                new ArrayList<ResearchObjectiveElementBeanI>();
            // put the Enabled results first and the disabled
            myResults.addAll(enabledResult);
            //myResults.addAll(disabledResult);
            // was Collections.sort(myResults);

            // call JSP to show search form and results
            QuickSearch.prepareSearchForm(request, version, targetMetaClass, criteria);
            request.setAttribute(
                "resultSize",
                QuickSearch.getCountOfResults(version, targetMetaClass, criteria,
                    request.getParameter(QuickSearch.SEARCH_ALL)));

            //request.setAttribute("metaClass", ServletUtil.getPIMSMetaClass(metaClass));
            request.setAttribute("searchMetaClass", ServletUtil.getPIMSMetaClass(targetMetaClass));
            //request.setAttribute("metaRole", ServletUtil.getPIMSMetaRole(metaRole)); 
            //request.setAttribute("displayName", displayName);

            request.setAttribute("results", myResults);
            request.setAttribute("bean", BeanFactory.newBean(object));
            QuickSearch.setPagingAttributes(request, paging);

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/complex/EditComplexRole.jsp");
            dispatcher.include(request, response);

            version.commit();
        } catch (final AbortedException e1) {
            throw new ServletException(e1);
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
     * @see http://jakarta.apache.org/commons/fileupload/apidocs/index.html
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        //System.out.println("org.pimslims.servlet.EditComplexRole.doPost()");

        final Map parms = request.getParameterMap();
        for (final Iterator it = parms.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            /*System.out.println("org.pimslims.servlet.EditRole request parameter [" + e.getKey() + ":"
                + s.toString() + "]"); */
        }

        final String pathInfo = request.getPathInfo();
        final Matcher matcher = EditRole.PATH_INFO.matcher(pathInfo);
        if (!matcher.matches()) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("Object and role not specified");
            return;
        }
        final String hook = matcher.group(1);
        //final String metaRoleName = matcher.group(2);
        final WritableVersion version = this.getWritableVersion(request, response);

        final ComplexBean complex = ResearchObjectiveElementBean.readComplexHook(version, hook);
        final ResearchObjective blueprint = version.get(complex.getBlueprintHook());

        try {
            // add 

            final Collection<ModelObject> add =
                ServletUtil.getModelObjectsByHooks(version, request.getParameterValues("add"));

            for (final ModelObject modelobject : add) {

                final Map<String, Object> m = new HashMap<String, Object>();
                m.put(ResearchObjectiveElement.PROP_EXPRESSIONOBJECTIVE, blueprint);
                m.put(ResearchObjectiveElement.PROP_COMPONENTTYPE, ComplexBeanWriter.COMPLEXTYPE);
                m.put(ResearchObjectiveElement.PROP_APPROXBEGINSEQID, 0);
                Target target = null;
                if (modelobject instanceof Target) {
                    target = (Target) modelobject;
                } else if (modelobject instanceof ResearchObjectiveElement) {
                    // obsolete
                    final ResearchObjectiveElement component = (ResearchObjectiveElement) modelobject;
                    target = component.getTarget();
                }
                if (target != null) {
                    m.put(ResearchObjectiveElement.PROP_TARGET, target);
                    String seq = target.getSeqString();
                    if (null == seq) {
                        seq = "";
                    }
                    m.put(ResearchObjectiveElement.PROP_APPROXENDSEQID, seq.length());
                }
                if (blueprint.getAccess() != null) {
                    m.put(LabBookEntry.PROP_ACCESS, blueprint.getAccess());
                }
                //m.put(ResearchObjectiveElement.PROP_MOLCOMPONENT, createMolComp(version, OpticPIMSMapping.template,
                //    construct.getDnaSeq(), "DNA"));
                version.create(ResearchObjectiveElement.class, m);
            }
            version.commit();
            // show results
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);
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
}
