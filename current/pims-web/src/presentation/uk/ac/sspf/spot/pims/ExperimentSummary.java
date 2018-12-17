/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.presentation.worklist.ConstructProgressBean;

import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * <p>
 * Class to populate the ConstructProgressBean beans, taking into account any filter supplied via a
 * ConstructFilterBean.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * // Get a ConstructProgress
 * ConstructProgess cp = new ConstructProgress(readableVersion);
 * 
 * // Warning - potentially slow
 * ArrayList progressList = cp.getProgressList();
 * </pre>
 * 
 * @author Jon Diprose
 */
@Deprecated
public class ExperimentSummary extends ConstructReport {

    /**
     * Experiment date order comparator
     */
    private static final ProgressDateComparator DATE_ORDER = new ProgressDateComparator();

    /**
     * The progress list
     */
    private ArrayList<ConstructProgressBean> progressList;

    /**
     * Constructor specifying the ReadbleVersion and the filter
     */
    public ExperimentSummary(final ReadableVersion readableVersion, final ConstructFilterBean filter) {
        this.setReadableVersion(readableVersion);
        this.setFilter(filter);
    }

    /**
     * Get the filtered progress list. Warning - this goes away and queries the database so may be very slow!
     * Returns an ArrayList of SPOTConstructProgressBeans
     * 
     * @param targets
     * 
     * @return An ArrayList containing the ConstructProgressBean elements
     * 
     */
    @SuppressWarnings("unchecked")
    public List<ConstructProgressBean> getProgressList(final Collection<Target> targets) {

        this.progressList = new ArrayList<ConstructProgressBean>();
        for (final Target target : targets) {
            if (this.filter.isWanted(target)) {
                this.findConstructProgress(target);
            }
        }
        // LATER How can I sort this without the @SuppressWarnings?
        Collections.sort(this.progressList, ExperimentSummary.DATE_ORDER);
        return this.progressList;
    }

    /**
     * Find the progress of all the constructs for the current target
     * 
     * @param spotTarget
     * 
     */
    private void findConstructProgress(final Target target) {

        // Get the real Target
        final Target t = target;
        final Collection<Milestone> statuses = t.getMilestones();

        // add milestones for blueprints this is in
        for (final ResearchObjectiveElement mo : t.getResearchObjectiveElements()) {
            if (mo.getComponentType().equals(ConstructUtility.SPOTCONSTRUCT)) {
                final ResearchObjective expBlueprint = mo.getResearchObjective();
                statuses.addAll(expBlueprint.getMilestones());
            }
        }

        for (final Milestone status : statuses) {
            final ConstructResultBean spotMilestone =
                new ConstructResultBean(this.getReadableVersion(), status);
            // Filter on milestone
            if (this.milestoneFilter(spotMilestone)) {
                // Add a progress element
                this.addProgress(spotMilestone, ConstructFilterBean.getOrganism(target), ConstructFilterBean
                    .getOldProject(target), target);
            }
        }
    }

    /**
     * Add a progress element to the list for the current target/construct/milestone
     * 
     * @param spotTarget
     * @param spotConstruct
     * @param spotMilestone
     * @param organism
     * @param project
     * @param target
     */
    private void addProgress(final ConstructResultBean spotMilestone, final String organism,
        final String project, final Target target) {

        // Create a new ConstructProgressBean
        final ConstructProgressBean progress = new ConstructProgressBean();

        // Populate the ConstructProgressBean
        progress.setConstructId(spotMilestone.getName());
        progress.setDateOfExperiment(spotMilestone.getDateOfExperiment());
        progress.setMilestone(spotMilestone.getMilestoneName());
        progress.setOrganism(organism);
        //progress.setProject(project);
        progress.setTargetName(spotMilestone.getProteinName());
        progress.setResult(spotMilestone.getResult());
        progress.setTargetId(target.get_Hook());
        progress.setPersonId(spotMilestone.getPerson_login_name());

        // Add this progress point
        this.progressList.add(progress);

    }

    /**
     * Simple filter on milestone
     * 
     * @return True if the filter term is found in the milestone
     * @see ExperimentSummary#containsIgnoreCase(String, String)
     */
    boolean milestoneFilter(final ConstructResultBean spotMilestone) {

        // Apply our filter
        return ConstructFilterBean.containsIgnoreCase(spotMilestone.getMilestoneName(), this.filter
            .getMilestone());

    }

}
