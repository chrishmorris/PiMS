/**
 * pims-web org.pimslims.servlet.complex ViewComplexServlet.java
 * 
 * @author Marc Savitsky
 * @date 17 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.complex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.ComplexBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.ResearchObjectiveElementBeanI;
import org.pimslims.presentation.mru.MRU;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.target.ResearchObjectiveElementBean;
import org.pimslims.servlet.PIMSServlet;

/**
 * ViewComplexServlet
 * 
 */
public class ViewComplexServlet extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "Custom view of a complex";
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

        //System.out.println("org.pimslims.servlet.ViewComplexServlet.doGet");
        //final long begin = System.currentTimeMillis();

        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion version = super.getReadableVersion(request, response);

        try {
            final String pathInfo = request.getPathInfo();
            if (null == pathInfo || 2 > pathInfo.length()) {
                throw new ServletException("No complex specified");
            }
            final String hook = pathInfo.substring(1);

            final ComplexBean bean = ResearchObjectiveElementBean.readComplexHook(version, hook);
            if (null == bean) {
                this.writeErrorHead(request, response, "Complex not found: " + hook,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            //System.out.println("Complex [" + bean.toString() + "]");

            final String userName = PIMSServlet.getUsername(request);

            final List<MRU> MRUs =
                new ArrayList<MRU>(MRUController.getMRUs(userName, "org.pimslims.model.target.Target"));

            final Collection<Target> mruTargets = new HashSet<Target>();
            for (final ModelObjectShortBean mru : MRUs) {
                final Target target = (Target) version.get(mru.getHook());
                //was if (!bean.containsTarget(target)) but could have two domains from one target
                mruTargets.add(target);
                //}
            }

            request.setAttribute(
                "mrus",
                new ArrayList<ResearchObjectiveElementBeanI>(NewComplexServlet
                    .getResearchObjectiveElementsList(mruTargets, "target")));

            request.setAttribute("complexbean", bean);
            request.setAttribute("mayUpdate", true);
            version.commit();

            //System.out.println("ViewComplexServlet [" + (System.currentTimeMillis() - begin) + "]");
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/JSP/complex/ViewComplex.jsp");
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
     *      it seems like this does nothing
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.ViewComplexServlet.doPost");
        for (final Iterator iter = request.getParameterMap().entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final String key = (String) entry.getKey();
            final String[] values = (String[]) entry.getValue();
            for (int i = 0; i < values.length; i++) {
                System.out.println("Parameter [" + key + "," + values[i] + "]");
            }
        }

    }

    /**
     * get expb candidates from target & expb MRU, but exclude the current expb used in experiment
     * 
     * @param version
     * @param userName
     * @param experiment
     * @return
     */
    static Map<String, String> getExpbFromMru(final ReadableVersion version, final String userName,
        final Experiment experiment) {
        if (experiment != null && experiment.getProject() != null) {
            return MRUController.getPossibleMRUItems(version, ResearchObjective.class, experiment
                .getResearchObjective().get_Hook(), false);
        }
        return MRUController.getPossibleMRUItems(version, ResearchObjective.class, null, false);

    }

}
