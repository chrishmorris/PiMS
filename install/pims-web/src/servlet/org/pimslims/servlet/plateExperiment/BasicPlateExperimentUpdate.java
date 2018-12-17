/**
 * current-pims-web org.pimslims.servlet.plateExperiment UpdatePlateExperiment.java
 * 
 * @author cm65
 * @date 4 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.servlet.plateExperiment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.servlet.PIMSServlet;

/**
 * UpdatePlateExperiment
 * 
 * Used for editing basic details only Implements some special edits that change all experiments
 */
public class BasicPlateExperimentUpdate extends PIMSServlet {

    /**
     * 
     */
    public BasicPlateExperimentUpdate() {
        super();
    }

    /**
     * @see org.pimslims.servlet.Update#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Writes editted values to database";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        throw new UnsupportedOperationException(this.getClass().getName() + " does not accept GET");

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
            System.out
                .println("org.pimslims.servlet.plateexperiment.PlateExperimentUpdate request parameter ["
                    + e.getKey() + ":" + s.toString() + "]");
        }
        */
        //final String UserName = PIMSServlet.getUsername(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            BasicPlateExperimentUpdate.processRequest(version, request.getParameterMap());
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final ParseException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

    }

    private static final String NAME = "name";

    public static final String START_DATE = "startDate";

    private static final String END_DATE = "endDate";

    private static final String ISACTIVE = "isActive";

    private static final Pattern EXPERIMENTHOOK = Pattern.compile("^(([a-zA-Z.]+:\\d+):*(.*))$");

    public static void processRequest(final WritableVersion version, final Map<String, String[]> parms)
        throws ServletException, AccessException, ConstraintException, ParseException {

        for (final Iterator it = parms.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String key = (String) e.getKey();
            if (key.startsWith("_")) {
                continue;
            }
            final String[] value = (String[]) e.getValue();

            //System.out.println("processRequest [" + key + "|" + value[0] + "]");

            // decode keys type of org.pimslims.model.experiment.Experiment:123456:status
            String hook = null;
            final Matcher m = BasicPlateExperimentUpdate.EXPERIMENTHOOK.matcher(key);
            assert m.matches() : "Key not recognised: " + key;
            hook = m.group(2);

            final ExperimentGroup group = version.get(hook);

            if (null != group) {
                if (key.endsWith(BasicPlateExperimentUpdate.NAME)) {
                    BasicPlateExperimentUpdate.updateName(group, value[0]);
                }

                if (key.endsWith(BasicPlateExperimentUpdate.START_DATE)) {
                    BasicPlateExperimentUpdate.updateStartDate(group, value[0]);
                }
                if (key.endsWith(LabBookEntry.PROP_PAGE_NUMBER)) {
                    BasicPlateExperimentUpdate.updatePageNumber(group, value[0]);
                }

                if (key.endsWith(BasicPlateExperimentUpdate.END_DATE)) {
                    BasicPlateExperimentUpdate.updateEndDate(group, value[0]);
                }

                if (key.endsWith(BasicPlateExperimentUpdate.ISACTIVE)) {
                    BasicPlateExperimentUpdate.updateSamplesIsActive(group, value[0]);
                }
            }
        }

    }

    /**
     * PlateExperimentUpdate.updatePageNumber
     * 
     * @param group
     * @param string
     */
    private static Collection<ModelObject> updatePageNumber(final ExperimentGroup group, final String value)
        throws ConstraintException, ParseException {

        final Collection<ModelObject> updates = new HashSet<ModelObject>();

        if (ServletUtil.validString(value)) {
            group.setPageNumber(value);
        }

        return updates;
    }

    private static Collection<ModelObject> updateName(final ExperimentGroup group, final String value)
        throws ConstraintException, ParseException {

        final Collection<ModelObject> updates = new HashSet<ModelObject>();

        if (ServletUtil.validString(value)) {
            group.setName(value);
        }

        return updates;
    }

    //TODO use ValueFormatter
    private static Collection<ModelObject> updateStartDate(final ExperimentGroup group, final String value)
        throws ConstraintException, NumberFormatException {

        final Collection<ModelObject> updates = new HashSet<ModelObject>();
        final SimpleDateFormat df = Utils.getDateFormat();
        final Calendar date = Calendar.getInstance();

        if (ServletUtil.validString(value)) {
            date.setTimeInMillis(Long.parseLong(value));

            final Collection<Experiment> experiments = group.getExperiments();
            for (final Experiment experiment : experiments) {
                experiment.setStartDate(date);
                updates.add(experiment);
            }
        }
        group.setStartDate(date);
        return updates;
    }

    //TODO no, use ValueFormatter
    private static Collection<ModelObject> updateEndDate(final ExperimentGroup group, final String value)
        throws ConstraintException, NumberFormatException {

        final Collection<ModelObject> updates = new HashSet<ModelObject>();
        final Calendar date = Calendar.getInstance();

        if (ServletUtil.validString(value)) {
            date.setTimeInMillis(Long.parseLong(value));

            final Collection<Experiment> experiments = group.getExperiments();
            for (final Experiment experiment : experiments) {
                experiment.setEndDate(date);
                updates.add(experiment);
            }
        }
        group.setEndDate(date);
        return updates;
    }

    private static Collection<ModelObject> updateSamplesIsActive(final ExperimentGroup group,
        final String value) throws ConstraintException, ParseException {

        final Collection<ModelObject> updates = new HashSet<ModelObject>();
        final boolean isActive = new Boolean(value);

        final Collection<Experiment> experiments = group.getExperiments();
        for (final Experiment experiment : experiments) {
            for (final OutputSample outputSample : experiment.getOutputSamples()) {
                final Sample sample = outputSample.getSample();
                if (null != sample) {
                    sample.setIsActive(isActive);
                }
                updates.add(outputSample);
            }
        }

        return updates;
    }
}
