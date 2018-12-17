package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.TargetBeanForLists;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.presentation.worklist.SampleBean;
import org.pimslims.presentation.worklist.SampleCriteria;
import org.pimslims.presentation.worklist.SampleDAO;
import org.pimslims.servlet.PIMSServlet;

public class SampleProgress extends PIMSServlet {

    /**
     * MAX_TYPES int
     */
    private static final int MAX_TYPES = 200;

    public static final String READY = "ready";

    public static final String DAYS_OF_NO_PROGRESS = "days_of_no_progress";

    public static final String EXPERIMENT_IN_USE = "experiment_in_use";

    public static final String ACTIVE = "active";

    public static final String NEXTEXPTYPE = "next_exp_type";

    public static final String ANY = "any";

    public static final String USER_HOOK = "assigned_to";

    public static final String PERSONHOOKNEWASSIGNEDTO = "new_assigned_to";

    public static final String COMMENTS = "assignment_comments";

    /**
     * 
     */
    public SampleProgress() {
        super();
    }

    @Override
    public final String getServletInfo() {
        return "Show sample progress list";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // long start = System.currentTimeMillis();

        final SampleCriteria criteria = new SampleCriteria();
        // get parameters
        final String ready = request.getParameter(SampleProgress.READY);
        final String inUsed = request.getParameter(SampleProgress.EXPERIMENT_IN_USE);
        final String days = request.getParameter(SampleProgress.DAYS_OF_NO_PROGRESS);
        final String activeString = request.getParameter(SampleProgress.ACTIVE);
        final String nextExpType = request.getParameter(SampleProgress.NEXTEXPTYPE);
        final String userHook = request.getParameter(SampleProgress.USER_HOOK);

        int noProgressDays = 0;
        if (null != days) {
            try {
                noProgressDays = Integer.parseInt(days);
            } catch (final NumberFormatException e) {
                this.writeErrorHead(request, response, "Not a number: " + days,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final List<SampleBean> sampleBeans =
                SampleProgress.getSampleBeans(version, criteria, ready, inUsed, noProgressDays, activeString,
                    nextExpType, userHook);
            request.setAttribute("sampleBeans", sampleBeans);
            request.setAttribute("criteria", criteria);
            request.setAttribute("typeNames", SampleProgress.getAllExpTypeNames(version));
            /* this involves disclosure: 
            request.setAttribute("userPersons",
                PersonUtility.getHookAndName(PersonUtility.getUserPersons(version))); */
            request.setAttribute("personHookAssignedTo", criteria.getUserHookAssignedTo());
            request.setAttribute("personNameAssignedTo", criteria.getUserNameAssignedTo(version));
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/sample/SampleProgress.jsp");
            rd.forward(request, response);
        } finally {
            version.abort();
        }
        //  System.out.println("sampleprogress get:" + (System.currentTimeMillis() - start) + " ms");

    }

    public static List<String> getAllExpTypeNames(final ReadableVersion version) {
        final List<String> typeNames = new ArrayList<String>();
        final Collection<ExperimentType> types =
            version.getAll(ExperimentType.class, 0, SampleProgress.MAX_TYPES);
        if (SampleProgress.MAX_TYPES == types.size()) {
            throw new IllegalStateException("Sorry, you have too many experiment types");
        }
        for (final ExperimentType et : types) {
            typeNames.add(et.getName());
        }
        Collections.sort(typeNames);
        return typeNames;
    }

    /**
     * @param version org.pimslims.dao.ReadableVersion
     * @return List of Status names
     */
    public static List<String> getAllStatusNames(final ReadableVersion version) {
        final List<String> tsNames = new ArrayList<String>();
        final Collection<TargetStatus> statuses =
            version.getAll(TargetStatus.class, 0, TargetBeanForLists.MAX_STATUSES);
        if (TargetBeanForLists.MAX_STATUSES == statuses.size()) {
            throw new IllegalStateException("Sorry, you have too many target statuses");
        }
        for (final TargetStatus ts : statuses) {
            tsNames.add(ts.get_Name());
        }
        Collections.sort(tsNames);
        return tsNames;
    }

    /**
     * @param version
     * @param criteria
     * @param ready select ready samples only
     * @param inUsed select samples that have been used
     * @param noProgressDays days since last activity
     * @param activeString sample is flagged active
     * @param nextExpType type of experiment the sample can be used in
     * @param personHook
     * @return
     */
    static List<SampleBean> getSampleBeans(final ReadableVersion version, final SampleCriteria criteria,
        final String ready, final String inUsed, final int noProgressDays, final String activeString,
        final String nextExpType, String userHook) {

        assert (criteria != null);
        // readyForNext
        final Boolean readyForNext = SampleProgress.getBooleanFromString(ready);
        criteria.setReadyForNext(readyForNext);

        // EXPERIMENT_IN_USE
        final Boolean alreadyInUse = SampleProgress.getBooleanFromString(inUsed);
        criteria.setAlreadyInUse(alreadyInUse);

        // DAYS_OF_NO_PROGRESS
        criteria.setDaysNoProgress(noProgressDays);

        // ACTIVE
        final Boolean active = SampleProgress.getBooleanFromString(activeString);
        criteria.setActive(active);

        // next exp type
        if (nextExpType == null || nextExpType.equalsIgnoreCase(SampleProgress.ANY)
            || nextExpType.length() == 0) {
            criteria.setExpTypeNameReadyFor(null);
        } else {
            criteria.setExpTypeNameReadyFor(nextExpType);
        }

        // person assigned
        if (userHook == null || userHook.equalsIgnoreCase(MRUController.NONE) || userHook.length() == 0) {
            userHook = null;
        }
        criteria.setUserHookAssignedTo(userHook);

        return SampleDAO.getSampleBeanList(version, criteria);
    }

    static Boolean getBooleanFromString(final String stringValue) {
        Boolean value;
        if (null != stringValue) {
            if (stringValue.equalsIgnoreCase("yes") || stringValue.equalsIgnoreCase("true")) {
                value = true;
            } else if (stringValue.equalsIgnoreCase("no") || stringValue.equalsIgnoreCase("false")) {
                value = false;
            } else {
                value = null;
            }
        } else {
            value = null;
        }
        return value;
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
        // long start = System.currentTimeMillis();
        if (!this.checkStarted(request, response)) {
            return;
        }
        final String personHookAssignedTo = request.getParameter(SampleProgress.PERSONHOOKNEWASSIGNEDTO);
        final String comments = request.getParameter(SampleProgress.COMMENTS);

        final Collection<String> sampleHooks =
            SampleProgress.getSelectedSampleHooks(request.getParameterMap());
        final WritableVersion wv = super.getWritableVersion(request, response);
        try {
            SampleProgress.processNewAssignment(wv, sampleHooks, personHookAssignedTo, comments);
            wv.commit();

        } catch (final ConstraintException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (final AbortedException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // System.out.println("sampleprogress post:" + (System.currentTimeMillis() - start) + " ms");
        this.doGet(request, response);
    }

    /**
     * get call selected sample hooks from parameterMap
     * 
     * @param parameterMap
     * @return
     */
    static Collection<String> getSelectedSampleHooks(final Map parameterMap) {
        final Collection<String> selectedSampleHooks = new HashSet<String>();
        for (final Object key : parameterMap.keySet()) {
            if (key instanceof String) {
                final String stringKey = (String) key;
                if (stringKey.startsWith(Sample.class.getName() + ':')) {
                    final String hook = stringKey;
                    if (parameterMap.get(key) instanceof String[]) {
                        final String value = ((String[]) parameterMap.get(key))[0];
                        if (value.equalsIgnoreCase("on")) {
                            selectedSampleHooks.add(hook);
                        }
                    }

                }

            }
        }
        return selectedSampleHooks;
    }

    static void processNewAssignment(final WritableVersion wv, final Collection<String> sampleHooks,
        final String userHookAssignedTo, final String comments) throws ConstraintException {
        User p = null;
        if (!userHookAssignedTo.equalsIgnoreCase(MRUController.NONE)) {
            p = wv.get(userHookAssignedTo);
        }
        for (final String sampleHook : sampleHooks) {
            final Sample sample = wv.get(sampleHook);
            assert sample != null;
            sample.setAssignTo(p);
            if (sample.getDetails() == null || sample.getDetails().trim().length() < 1) {
                sample.setDetails(comments);
            } else if (comments != null && comments.trim().length() > 1) {
                sample.setDetails(comments + "; " + sample.getDetails());
            }

        }

    }

}
