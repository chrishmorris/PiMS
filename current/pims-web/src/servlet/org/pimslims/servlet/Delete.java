/*
 * Created on 28-Jan-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.CsrfDefence;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.construct.ConstructBeanWriter;
import org.pimslims.presentation.construct.SyntheticGeneManager;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;
import org.pimslims.servlet.spot.SpotTarget;

/**
 * Deletes a model object, if permitted
 */
public class Delete extends PIMSServlet {

    /**
     * 
     */
    public Delete() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "Deletes an object from the PIMS database";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String[] hooks = new String[1];
        if (null != request.getParameter("hook") && !"".equals(request.getParameter("hook"))) {
            hooks = request.getParameterValues("hook");
        } else {
            if (null == request.getPathInfo()) {
                this.writeErrorHead(request, response, "Please specify page to delete",
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            hooks[0] = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        }
        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Collection<ModelObjectShortBean> canBeans = new ArrayList(hooks.length);
            final Collection<ModelObjectShortBean> cantBeans = new ArrayList(hooks.length);
            for (int i = 0; i < hooks.length; i++) {
                final org.pimslims.metamodel.ModelObject object =
                    this.getRequiredObject(version, request, response, hooks[i]);
                if (object == null) {
                    return;
                }
                final Collection<ModelObjectShortBean> beans = this.getBeans(object);
                for (final Iterator iterator = beans.iterator(); iterator.hasNext();) {
                    final ModelObjectShortBean bean = (ModelObjectShortBean) iterator.next();
                    if (bean.getMayDelete()) {
                        canBeans.add(bean);
                    } else {
                        cantBeans.add(bean);
                    }

                }
            }
            request.setAttribute("modelObjects", canBeans);
            request.setAttribute("cantDelete", cantBeans);
            version.commit();
            response.setStatus(HttpServletResponse.SC_OK);
            final RequestDispatcher dispatcher =
                this.getServletContext().getRequestDispatcher("/JSP/Delete.jsp");
            dispatcher.forward(request, response);
            PIMSServlet.writeFoot(response.getWriter(), request);
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
     * Delete.getBeans
     * 
     * @param object
     * @return
     */
    private Collection<ModelObjectShortBean> getBeans(final org.pimslims.metamodel.ModelObject object) {
        if (object instanceof Target) {
            return SpotTarget.getLinkedRecords((Target) object);
        } else if (object instanceof ResearchObjective) {
            return ModelObjectShortBean.getBeansInOriginalOrder(ConstructBeanWriter
                .getConstructParts((ResearchObjective) object));
        } else if (object instanceof Sample && SyntheticGeneManager.isSynthGene((Sample) object) == true) {
            return ModelObjectShortBean.getBeansInOriginalOrder(SyntheticGeneManager
                .getSyntheticGeneParts((Sample) object));

        }
        final Collection<ModelObjectShortBean> beans =
            Collections.singleton(new ModelObjectShortBean(object));
        return beans;
    }

    /**
     * Deletes one or more records from PiMS
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);

        String[] hooks = new String[1];
        if (null != request.getParameter("hook") && !"".equals(request.getParameter("hook"))) {
            hooks = request.getParameterValues("hook");
        } else {
            if (null == request.getQueryString()) {
                throw new ServletException("Nothing specified to delete");
            }
            hooks[0] = URLDecoder.decode(request.getQueryString(), "UTF-8");
        }

        CsrfDefence.validate(request);

        final String pathName = request.getContextPath(); // e.g. /pims
        org.pimslims.metamodel.ModelObject object = null;

        final org.pimslims.dao.WritableVersion version = this.getWritableVersion(request, response);
        try {
            final MetaClass[] metaClass = new MetaClass[hooks.length];
            final ModelObject[] parent = new ModelObject[hooks.length];
            final String[] name = new String[hooks.length];
            for (int i = 0; i < hooks.length; i++) {
                object = this.getRequiredObject(version, request, response, hooks[i]);
                if (object == null) {
                    return;
                }
                metaClass[i] = object.get_MetaClass();
                name[i] = StringEscapeUtils.escapeHtml(object.get_Name());
                final MetaRole parentRole = ((MetaClassImpl) metaClass[i]).getParentRole();
                /* if (parentRole != null && "project".equals(parentRole.getRoleName())) {
                     parentRole = null; // ignore memops project
                 } */
                if (null != parentRole) {
                    final Collection<ModelObject> parents = parentRole.get(object);
                    if (1 == parents.size()) {
                        parent[i] = parents.iterator().next();
                    }
                }
                //TODO no, do this like the other special deletes, .e.g. target
                // special delete for experimentgroup
                if (metaClass[i].getJavaClass().getSimpleName().equals(ExperimentGroup.class.getSimpleName())) {
                    PlateExperimentUtility.deleteExpGroup(version, object.get_Hook());
                }

                // special delete for experiment outputSamplegroup
                if (metaClass[i].getJavaClass().getSimpleName().equals(OutputSample.class.getSimpleName())) {
                    Delete.deleteExpOutputSample(version, object.get_Hook());
                }
                version.delete(object);
                MRUController.deleteObject(object.get_Hook());

            }
            version.commit();

            response.setStatus(HttpServletResponse.SC_OK);
            final PrintWriter writer = response.getWriter();
            if (null != request.getParameter("isAJAX")
                && "true".equals(request.getParameter("isAJAX").toString())) {
                response.setContentType("text/xml");
                final XMLOutputter xo = new XMLOutputter();
                xo.output(this.makeSuccessXML(hooks), writer);
            } else {
                // normal non-AJAX response
                response.setContentType("text/html");
                this.writeHead(request, response, "PiMS records deleted");
                for (int i = 0; i < hooks.length; i++) {
                    if (null == parent[i]) {
                        final String displayName = ServletUtil.getDisplayName(metaClass[i]);
                        writer.print("<p>The record of " + name[i] + " has been deleted from PIMS.</p>"
                            + "<a href=\"" + pathName + "/Search/" + metaClass[i].getMetaClassName()
                            + "\">View " + displayName + "s</a>");
                    } else {
                        writer.print("<p>The record of " + name[i] + " has been deleted from "
                            + parent[i].get_Name() + "</p>" + "<a href=\"" + pathName + "/View/"
                            + parent[i].get_Hook() + "\">View " + parent[i].get_Name() + "</a>");
                    }
                }
            }

        } catch (final AccessException ex) {
            throw new ServletException(ex);
        } catch (final org.pimslims.exception.ConstraintException ex) {
            throw new ServletException(ex);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    private Document makeSuccessXML(final String[] hooks) { // XML document
        final Element rootElement = new Element("deleted");
        for (int i = 0; i < hooks.length; i++) {

            final Element elem = new Element("object");
            elem.setAttribute("hook", hooks[i]);
            rootElement.addContent(elem);
        }
        final Document xml = new Document(rootElement);
        return xml;
    }

    public static void deleteExpOutputSample(final WritableVersion wv, final String outputSampleHook)
        throws AccessException, ConstraintException {
        final OutputSample outputSample = wv.get(outputSampleHook);
        if (outputSample == null) {
            return;
        }
        // delete exp's OutputSample and sample
        if (outputSample.getSample() != null) {
            MRUController.deleteObject(outputSample.getSample().get_Hook());
            outputSample.getSample().delete();
        }
        wv.delete(outputSample);
    }

}
