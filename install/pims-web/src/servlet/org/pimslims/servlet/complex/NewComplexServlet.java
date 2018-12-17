/**
 * pims-web org.pimslims.servlet.complex NewComplexServlet.java
 * 
 * @author Marc Savitsky
 * @date 11 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.complex;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * NewComplexServlet
 * 
 */

public class NewComplexServlet extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Custom create of a complex";
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

        //System.out.println("org.pimslims.servlet.NewComplexServlet.doGet");
        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = super.getReadableVersion(request, response);

        try {
            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));
            version.commit();
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/complex/NewComplex.jsp");
            dispatcher.forward(request, response);
        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new ServletException(e);
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

        System.out.println("org.pimslims.servlet.NewComplexServlet.doPost");
        for (final Iterator iter = request.getParameterMap().entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String key = (String) entry.getKey();
            final String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                System.out.println("Parameter [" + key + "," + values[i] + "]");
            }
        }

        final WritableVersion version = this.getWritableVersion(request, response);
        if (version == null) {
            return;
        }

        ComplexBean newComplex;

        try {

            final ComplexBean complex = new ComplexBean();
            complex.setName(request.getParameter("complexname"));
            complex.setWhyChosen(request.getParameter("complexwhychosen"));
            complex.setDetails(request.getParameter("complexdetails"));
            complex.setlabNotebookHook(request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID));
            // was complex.addComponent(new BlueprintComponentBean());
/*
            final String[] components = request.getParameterValues("newcomplexcomponent");

            if (null != components) {
                for (int i = 0; i < components.length; i++) {
                    final ResearchObjectiveElement component = version.get(components[i]);
                    final BlueprintComponentBean componentBean = new BlueprintComponentBean(component);
                    complex.addComponent(componentBean);
                }
            } */

            //System.out.println(complex.toString());
            newComplex = ResearchObjectiveElementBean.createNewComplex(version, complex);
            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final AccessException e) {
            throw new ServletException(e.getLocalizedMessage());

        } catch (final ConstraintException e) {
            throw new ServletException(e.getLocalizedMessage());

        } finally {
            if (version != null) {
                if (!version.isCompleted()) {
                    version.abort();
                }
            }
        }

        // now show the new target
        final String hook = newComplex.getBlueprintHook();
        PIMSServlet.redirectPost(response, request.getContextPath() + "/ViewComplex/" + hook);
    }

    /**
     * get all blueprintComponents from the list of targets which component type is 'type'
     * 
     * @param targets
     * @param type
     * @return Collection<ResearchObjectiveElement> which is ordered
     */
    static Set<ResearchObjectiveElementBeanI> getResearchObjectiveElementsList(final Collection<Target> targets,
        final String type) {

        final Set<ResearchObjectiveElementBeanI> collection = new HashSet<ResearchObjectiveElementBeanI>();

        for (final Target target : targets) {
            final Collection<ResearchObjectiveElement> components = target.getResearchObjectiveElements();
            for (final ResearchObjectiveElement component : components) {
                if (component.getComponentType().equals(type)) {
                    collection.add(new ResearchObjectiveElementBean(component));
                }
            }
        }
        return collection;
    }
}
