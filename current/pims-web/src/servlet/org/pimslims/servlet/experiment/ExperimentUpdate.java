package org.pimslims.servlet.experiment;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ConstructMileStoneUtil;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.Update;

public class ExperimentUpdate extends PIMSServlet {

    static String MILESTONEWEBID = "_milestoneAchieved";

    static String ACHIEVED = "on";

    @Override
    public String getServletInfo() {
        return "Update experiment in database and refresh";
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        try {
            final HttpSession session = request.getSession();
            ExperimentUpdate.processRequest(version, parms, session);
            version.commit();
            PIMSServlet.redirectPostToReferer(request, response, request.getParameter(Update.BOX_TO_OPEN));

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ParseException e) {
            this.writeErrorHead(request, response, "Invalid date", HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    public static Set<ModelObject> processRequest(final WritableVersion version,
        final Map<String, String[]> parms, final HttpSession session) throws ServletException,
        AccessException, ConstraintException, ParseException {
        // get experiment hook, expblueprint hook and milestone achieved
        String expHook = null;
        String expbHook = null;
        boolean milestoneAchieved = false;
        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();

            if (key.equalsIgnoreCase(ExperimentUpdate.MILESTONEWEBID)) {// milestone achieved
                final String val = (parms.get(key))[0];
                if (ExperimentUpdate.ACHIEVED.equals(val)) {
                    milestoneAchieved = true;
                }
            } else if (key.endsWith(Experiment.PROP_PROJECT)) {

                expbHook = (parms.get(key))[0]; // expb hook
                if (MRUController.NONE.equalsIgnoreCase(expbHook)) {
                    expbHook = null;
                }

                final Matcher m = Update.KEY.matcher(key);
                if (!m.matches()) {
                    throw new ServletException("Invalid parameter name: " + key);
                }
                expHook = m.group(1);// experiment hook

                if (null != expHook && null != expbHook) {
                    ExperimentUpdate.addExpBlueprint(version, expHook, expbHook);
                }
            }
        }
        if (expbHook != null && milestoneAchieved) {
            ExperimentUpdate.addMilestone(version, expHook, expbHook);
        } else if (expbHook != null) {
            ExperimentUpdate.removeMilestone(version, expHook);
        }
        return Update.processRequest(version, parms, session).keySet();
    }

    // delete all milestones of the experiment
    static void removeMilestone(final WritableVersion version, final String expHook) throws AccessException,
        ConstraintException {
        final Experiment exp = version.get(expHook);
        final Set<Milestone> milstones = exp.getMilestones();
        for (final Milestone milestone : milstones) {
            milestone.delete();
        }

    }

    /**
     * Add a milestone which link beween experiment and expBlueprint if no such milestone existed
     * 
     * @param version
     * @param experimentHook
     * @param expBlueprintHook
     * @throws ConstraintException
     * @throws AccessException
     */
    static void addMilestone(final WritableVersion version, final String experimentHook,
        final String projectHook) throws AccessException, ConstraintException {
        final Experiment exp = version.get(experimentHook);
        final ResearchObjective expb = version.get(projectHook);
        String targetHook = null;
        if (expb.getResearchObjectiveElements() != null && expb.getResearchObjectiveElements().size() > 0) {
            targetHook = expb.getResearchObjectiveElements().iterator().next().getTarget().get_Hook();
        }

        /*String creatorHook = null;
        if (exp.getCreator() != null) {
            creatorHook = exp.getCreator().get_Hook();
        } */

        // check existed milestone
        final Set<Milestone> milestones = exp.getMilestones();
        boolean milestoneFound = false;
        for (final Milestone milestone : milestones) {
            if (expb.equals(milestone.getResearchObjective())) {
                milestoneFound = true;
            }
        }
        if (!milestoneFound) {
            ConstructMileStoneUtil.createMilestone(version, expb, targetHook, exp.getExperimentType(), exp,
                new java.util.Date(), "");
        }

    }

    static void addExpBlueprint(final WritableVersion version, final String experimentHook,
        final String expBlueprintHook) throws AccessException, ConstraintException {

        if (null != expBlueprintHook) {
            System.out.println("addExpBlueprint [" + expBlueprintHook + "]");
            final Experiment exp = version.get(experimentHook);
            final ResearchObjective expb = version.get(expBlueprintHook);
            ExperimentUtility.setExpBlueprintSamples(exp, expb);
        }
    }
}
