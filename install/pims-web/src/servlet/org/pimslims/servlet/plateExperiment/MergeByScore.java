package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.presentation.PlateExperimentUpdateBean;
import org.pimslims.presentation.plateExperiment.PlateExperimentUtility;

public class MergeByScore extends org.pimslims.servlet.PIMSServlet implements javax.servlet.Servlet {

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.plateExperiment.MergeByScore doGet");
        /*
        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.plateexperiment.MergeByScore request parameter ["
                + e.getKey() + ":" + s.toString() + "]");
        }
        */
        if (!this.checkStarted(request, response)) {
            return;
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            RefInputSample refInputSample = null;
            if (null != request.getParameter("refInputSample")) {
                refInputSample =
                    (RefInputSample) this.getRequiredObject(rv, request, response,
                        request.getParameter("refInputSample"));
            }
            if (null == refInputSample) {
                return;
            }
            request.setAttribute("refInputSample", refInputSample);

            final Map values = new HashMap();
            if (null == refInputSample.getAmount()) {
                values.put(refInputSample.getName(), new Float("0"));
            } else {
                values.put(refInputSample.getName(), refInputSample.getAmount());
            }
            values.put(refInputSample.getName() + "Unit", refInputSample.getUnit());
            values.put(refInputSample.getName() + "DisplayUnit", refInputSample.getDisplayUnit());
            final Measurement measurement = Measurement.getMeasurement(values, refInputSample.getName());
            System.out.println("Measurement [" + measurement.getValue() + ":" + measurement.getDisplayValue()
                + "]");
            request.setAttribute("measurement", measurement);

            if (null != request.getParameter("outputGroup")) {
                final ExperimentGroup group =
                    (ExperimentGroup) this.getRequiredObject(rv, request, response,
                        request.getParameter("outputGroup"));
                final PlateExperimentUpdateBean outputGroupBean =
                    new PlateExperimentUpdateBean(group, null);

                request.setAttribute("outputGroupBean", outputGroupBean);
            }

            Collection<ExperimentGroup> inputGroups = null;
            final String relationship = "parentPlate"; //request.getParameter("relationship");

            /*
            if ("grandparentPlate".equals(relationship)) {
                final ExperimentGroup group =
                    (ExperimentGroup) this.getRequiredObject(rv, request, response,
                        request.getParameter("grandparentGroup"));
                inputGroups = PlateExperimentUtility.getChildGroups(group, true);
            }
            */

            if ("parentPlate".equals(relationship)) {
                final ExperimentGroup group =
                    (ExperimentGroup) this.getRequiredObject(rv, request, response,
                        request.getParameter("parentGroup"));
                inputGroups = PlateExperimentUtility.getSiblingGroups(group);
            }

            final List<PlateExperimentUpdateBean> inputGroupBeans =
                new ArrayList<PlateExperimentUpdateBean>();
            for (final ExperimentGroup group : inputGroups) {
                inputGroupBeans.add(new PlateExperimentUpdateBean(group, null));
            }

            Collections.sort(inputGroupBeans);
            request.setAttribute("inputGroupBeans", inputGroupBeans);

            final RequestDispatcher rd =
                request.getRequestDispatcher("/JSP/plateExperiment/mergeByScore.jsp");
            rd.forward(request, response);

            rv.commit();
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /*
     * (non-Java-doc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Not yet implemented");
    }

    @Override
    public String getServletInfo() {
        return "For copying samples between wells.";
    }

}
