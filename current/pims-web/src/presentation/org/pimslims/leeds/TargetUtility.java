/**
 * 
 */
package org.pimslims.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.create.TargetFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.TargetBeanForLists;

/**
 * @author cm65
 * 
 *         Some methods by Peter Troshin
 * 
 */
public class TargetUtility {
    

    /**
     * search target name in name and alias
     * 
     * @param version
     * @param targetName
     * @return
     * 
     *         public static Target searchForTargetByNames(final ReadableVersion version, final String
     *         targetName) {
     * 
     *         // At this point F1 (Fn) and R1 (Rn) is already cut // This expects MPSIL0334 // Returns UL0334
     *         // or PAC0877 // return PAC0877 //System.out.println("Tname was " + targetName); final String
     *         name = FindLeedsTarget.getTargetCommonName(targetName); String fullname = null; if
     *         (FindLeedsTarget.isMPSITarget(targetName)) { fullname = "UL" + name; } else { fullname = name;
     *         } //System.out.println("post name " + name); final Target target =
     *         FindLeedsTarget.findTarget(version, fullname);
     * 
     *         if (target == null) { System.out.println("Not found: " + targetName + " as: " + fullname); }
     * 
     *         return target;
     * 
     *         }
     */

    /**
     * @param model the database in use
     * @return a list of target statuses, in order
     * 
     */
    public static List<org.pimslims.model.reference.TargetStatus> getTargetStatuses(
        final ReadableVersion version) {
        final Collection statuses =
            version.getAll(org.pimslims.model.reference.TargetStatus.class, 0, TargetBeanForLists.MAX_STATUSES);
        if (TargetBeanForLists.MAX_STATUSES == statuses.size()) {
            throw new IllegalStateException("Sorry, you have too many target statuses");
        }
        return (List<TargetStatus>) statuses;
    }

    /**
     * @param target
     * @return its current status code
     * 
     */
    @SuppressWarnings("unchecked")
    public static String getCurrentTargetStatusCode(final Target target) {
        final Milestone milestone = TargetBeanForLists.getCurrentTargetMilestone(target);
        if (null == milestone) {
            return "";
        }
        return milestone.getStatus().getName();
    }

    /**
     * Filters a collection of targets by their status. This can be either those targets that currently have
     * the desired status, or those that have ever had it (including those that have it now).
     * 
     * @param targets A collection of ModelObjects representing Targets.
     * @param status The status on which to filter the targets.
     * @param timeframe Either "current" or "historical". "current" restricts the filter to return only
     *            targets that currently have this status. "historical" will return all targets that have ever
     *            had this status, including those that currently have it.
     * @return a HashSet containing ModelObject representations of Targets meeting the filter criteria
     * 
     */
    public static HashSet filterTargetsByTargetStatus(final Collection<Target> targets, final String status,
        final String timeframe) {

        System.out.println("In TargetUtility");

        if (!timeframe.equals("current") && !timeframe.equals("historical")) {
            return null;
        }
        final HashSet<Target> filteredTargets = new HashSet<Target>();

        if (timeframe.equals("current")) {
            for (final Target t : targets) {
                final String lastMilestoneCode = TargetUtility.getCurrentTargetStatusCode(t);
                if (lastMilestoneCode.equals(status)) {
                    filteredTargets.add(t);
                }
            }
        } else { //"historical"

            for (final Target t : targets) {
                final Set<Milestone> statushistory = t.getMilestones();
                for (final Milestone st : statushistory) {
                    if (st.getStatus().getName().equals(status)) {
                        filteredTargets.add(t);
                        continue;
                    }
                }
            }
        }
        return filteredTargets;
    }

    /**
     * @param target
     * @param status
     * 
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Milestone newTargetStatus(final WritableVersion version, final Target target,
        final String status) throws AccessException, ConstraintException {
        final TargetStatus ts = TargetFactory.getStatus(version, status);
        return new Milestone(version, java.util.Calendar.getInstance(), ts, target);
    }

    /**
     * @param sample the sample
     * @return the ResearchObjective associated with it, if any
     */
    public static Project getTargetOrConstruct(final Sample sample) {
        final OutputSample os = sample.getOutputSample();
        if (null == os) {
            return null;
        }
        final Experiment experiment = os.getExperiment();
        return experiment.getProject();
    }

    /**
     * Associate target with experiment Method assumes that all target has at least one BluePrintComponents &
     * ExpBluePrint
     */
    @Deprecated
    public static void setTarget(final Target target, final Experiment experiment) throws ConstraintException {
        assert target != null && experiment != null : "Target or experiment is null! Cannot proceed";
        final ResearchObjectiveElement bcomp = target.getResearchObjectiveElements().iterator().next();
        final ResearchObjective expBlue = bcomp.getResearchObjective();
        expBlue.addExperiment(experiment);
    }

    /**
     * Get target associated with experiment Method assumes that all target has at least one
     * BluePrintComponents & ExpBluePrint
     */
    @Deprecated
    public static Target getFirstTarget(final Experiment experiment) {
        assert experiment != null : "Target or experiment is null! Cannot proceed";
        final ResearchObjective expBlue = (ResearchObjective) experiment.getProject();
        if (expBlue == null) {
            return null;
        }
        for (final ResearchObjectiveElement bcomp : expBlue.getResearchObjectiveElements()) {
            final Target target = bcomp.getTarget();
            if (target != null) {
                return target;
            }
        }
        return null;
    }

    /**
     * @param target
     */
    public static Collection<ResearchObjective> getTargetExpBlueprint(final Target target) {
        final Collection<ResearchObjective> expbs = new ArrayList<ResearchObjective>();
        for (final ResearchObjectiveElement bc : target.getResearchObjectiveElements()) {
            if (bc.getResearchObjective() != null) {
                expbs.add(bc.getResearchObjective());
            }
        }
        return expbs;

    }

    /**
     * @param target
     */
    @Deprecated
    // unused
    public static ResearchObjective getLastTargetExpBlueprint(final Target target) {
        final Collection<ResearchObjective> expBlues = TargetUtility.getTargetExpBlueprint(target);
        if (expBlues != null && !expBlues.isEmpty()) {
            return expBlues.iterator().next();
        }
        return null;
    }

    public static Collection<Protocol> getTargetProtocols(final Collection<String> targetHooks,
        final ReadableVersion version) {
        final Collection<Protocol> usedProtocols = new HashSet<Protocol>();
        for (final String targetHook : targetHooks) {
            final Target target = version.get(targetHook);
            assert null != target;
            for (final Experiment exp : TargetUtility.getExperiments(target)) {
                if (exp.getProtocol() != null) {
                    usedProtocols.add(exp.getProtocol());
                }
            }
        }
        return usedProtocols;
    }

    public static Collection<Experiment> getExperiments(final Target target) {
        final Collection<Experiment> exps = new HashSet<Experiment>();
        for (final ResearchObjective expb : TargetUtility.getTargetExpBlueprint(target)) {
            exps.addAll(expb.getExperiments());
        }
        return exps;
    }

    public static Experiment getMostRecentExperiment(final Target target) {
        final Collection<Experiment> exps = TargetUtility.getExperiments(target);
        Experiment recentExp = null;
        for (final Experiment exp : exps) {
            if (recentExp != null) {
                if (recentExp.getStartDate().before(exp.getStartDate())) {
                    recentExp = exp;
                }
            } else {
                recentExp = exp;
            }
        }
        return recentExp;
    }

}
