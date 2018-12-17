package org.pimslims.servlet.target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.leeds.TargetProgressSearcher;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.presentation.target.TargetExperimentBean;
import org.pimslims.search.Conditions;
import org.pimslims.servlet.PIMSServlet;

/**
 * Display target progress according to experiments associated with the target
 * 
 * @author Petr Troshin
 */
public class TargetProgress extends PIMSServlet {

    final int defaultDaysback = 30;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "Target progress calculated ";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final String isShort = request.getParameter("isShort");
        String view = "/JSP/report/TargetProgress.jsp";

        if (!Util.isEmpty(isShort) && isShort.equalsIgnoreCase("Yes")) {
            view = "/JSP/report/TargetProgressShortList.jsp";
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            //final Collection<Person> users = PersonUtility.getUserPersons(rv);
            //final ArrayList<Person> sortedusers = new ArrayList<Person>(users);
            //Collections.sort(sortedusers, new PersonUtility.GivenNameComparator());
            // no, disclosure  request.setAttribute("users", sortedusers); 

            final String days = request.getParameter("daysback");
            int daysback = this.defaultDaysback;
            if (!Util.isEmpty(days)) {
                daysback = Integer.parseInt(days);
            }
            final long daystime = (long) 1000 * 60 * 60 * 24 * daysback;

            String dataOwnerHook = request.getParameter("dataOwner");

            if (Util.isEmpty(dataOwnerHook)) {
                dataOwnerHook = "anywhere";
            }

            String creatorHook = "";
            if (rv.getCurrentUser() != null) {
                creatorHook = rv.getCurrentUser().get_Hook();
            }

            final String allExperiments = request.getParameter("allExperiments");
            boolean showAllExps = false;
            if (!Util.isEmpty(allExperiments)) {
                showAllExps = true;
            }
            //System.out.println(dataOwnerHook + " " + creatorHook + " " + daysback);

            request.setAttribute("activeTargets",
                TargetProgress.search(rv, creatorHook, dataOwnerHook, daystime, showAllExps));
            request.setAttribute("chosenPersonHook", creatorHook); // probably obsolete
            request.setAttribute("checked", showAllExps);
            final Collection<LabNotebook> accs = PIMSServlet.getAll(rv, LabNotebook.class);
            // no, disclosure request.setAttribute("dataOwners", accs);
            request.setAttribute("rdataOwner", dataOwnerHook);
            request.setAttribute("daysback", daysback);
            final RequestDispatcher rd = request.getRequestDispatcher(view);

            //TODO check if this should be forward
            rd.include(request, response);

            rv.commit();
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final IllegalStateException e) {
            throw new ServletException(e.getMessage());
        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }

    }

    /**
     * @param rv teh current transaction
     * @param creatorUserHook the hook of the person who owns the target
     * @param dataOwnerHook the hook of the data owner for the project
     * @param daystime milliseconds since the last experiment
     * @param allExperiments
     * @return
     */
    public static Collection<TargetExperimentBean> search(final ReadableVersion rv,
        final String creatorUserHook, final String dataOwnerHook, final long daystime,
        final boolean allExperiments) {
    
        final TargetProgressSearcher ts = new TargetProgressSearcher(rv);
        if (Util.isHookValid(creatorUserHook)) {
            ts.addCriteria(LabBookEntry.PROP_CREATOR, rv.get(creatorUserHook));
        }
        if (Util.isHookValid(dataOwnerHook)) {
            ts.addCriteria(LabBookEntry.PROP_ACCESS, rv.get(dataOwnerHook));
        }
        final long time = System.currentTimeMillis() - daystime;
        final Calendar start = java.util.Calendar.getInstance();
        start.setTimeInMillis(time);
        ts.addCriteria(Experiment.PROP_STARTDATE, Conditions.greaterThan(start));
    
        final java.util.List<TargetExperimentBean> atargets = ts.search();
    
        List<TargetExperimentBean> cleanTargets = null;
        if (allExperiments) {
            // This preserve all duplicated target and experiments
            cleanTargets = new ArrayList<TargetExperimentBean>(atargets);
        } else {
            // This remove all duplicated target from the collection, thus remove some experiments
            cleanTargets = new ArrayList<TargetExperimentBean>(new HashSet<TargetExperimentBean>(atargets));
        }
    
        // This is required as I am looking for experiment with the Creator equal to creatorHook.
        // However, the search above will guarantee that at least one experiment with this creator is associated
        // to one target, but this may not be necessarily most recent experiment.
        if (Util.isHookValid(creatorUserHook)) {
            for (final TargetExperimentBean t : atargets) {
                final Experiment exp = TargetUtility.getMostRecentExperiment(t.getTarget());
                final User expCreator = exp.getCreator();
                if (expCreator == null || !creatorUserHook.equals(expCreator.get_Hook())) {
                    cleanTargets.remove(t);
                }
            }
        }
        Collections.sort(cleanTargets, new TargetProgressSearcher.ExperimentStartDateComporator());
        return cleanTargets;
    }

}
