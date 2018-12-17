/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.presentation.servlet.utils.ProgressListener;
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
public class ConstructProgress extends ConstructReport {

    /**
     * Constructor specifying the ReadbleVersion
     */
    public ConstructProgress(final ReadableVersion readableVersion) {
        this(readableVersion, new ConstructFilterBean());
    }

    /**
     * Constructor specifying the ReadbleVersion and the filter
     */
    public ConstructProgress(final ReadableVersion readableVersion, final ConstructFilterBean filter) {
        this.setReadableVersion(readableVersion);
        this.setFilter(filter);

    }

    public static Collection<Target> getAll(final ReadableVersion version) {
        // speedupQuery(version);
        // add roles needed in TargetBean into joinlist
        final java.util.List<String> rolesToJoin = new ArrayList<String>();
        rolesToJoin.add(Target.PROP_RESEARCHOBJECTIVEELEMENTS);
        rolesToJoin.add(LabBookEntry.PROP_ATTACHMENTS);
        rolesToJoin.add(Target.PROP_SPECIES);
        rolesToJoin.add(Target.PROP_PROTEIN);
        rolesToJoin.add(Target.PROP_NUCLEICACIDS);
        return version.getAll(org.pimslims.model.target.Target.class, rolesToJoin);
    }

    /**
     * Get the filtered progress list. Warning - this goes away and queries the database so may be very slow!
     * Returns an ArrayList of SPOTConstructProgressBeans
     * 
     * @param targets
     * @param progressBar
     * 
     * @return An ArrayList containing the ConstructProgressBean elements
     * @throws IOException
     * 
     */
    public List<ConstructProgressBean> getProgressList(final Collection<Target> targets,
        final ProgressListener listener) throws IOException {

        // Declare the ArrayList
        final ArrayList<ConstructProgressBean> progressList = new ArrayList<ConstructProgressBean>();

        // Loop over all the targets
        final Iterator<Target> iter = targets.iterator();
        int count = 0;
        final int total = targets.size();
        while (iter.hasNext()) {
            count = count + 1;
            if (listener != null) {
                listener.setProgress((100 * count) / total);
            }

            // Get this target
            final Target target = iter.next();
            if (this.filter.isWanted(target)) {

                // Get the ExpBlueprints for this target
                final Collection<ResearchObjectiveElement> blueprintComponents =
                    target.getResearchObjectiveElements();
                for (final ResearchObjectiveElement blueprintComponent : blueprintComponents) {
                    if (blueprintComponent.getComponentType()
                        .equalsIgnoreCase(ConstructUtility.SPOTCONSTRUCT)) {
                        // Get the ExpBlueprint
                        final ResearchObjective expBlueprint = blueprintComponent.getResearchObjective();
                        final Collection<Milestone> statuses = expBlueprint.getMilestones();
                        // Get the Experiments for this target
                        final ConstructProgressBean progress = this.findLatestMilestone(target, statuses);
                        if (null != progress && this.filter.isWanted(progress)) {
                            progressList.add(progress);
                        }

                    }
                }
            }
        }
        if (null != listener) {
            listener.setProgressHidden(true);
        }
        Collections.sort(progressList, new ProgressDateComparator());
        return progressList;

    }

    /**
     * Find the milestones for the current construct
     * 
     * @param progressList
     * @param target
     * @param statuses
     * @return
     */
    private ConstructProgressBean findLatestMilestone(final Target target,
        final Collection<Milestone> statuses) {
        ConstructResultBean latest = null;

        for (final Milestone status : statuses) {
            final ConstructResultBean spotMilestone =
                new ConstructResultBean(this.getReadableVersion(), status);

            /*
             * TODO In this loop we should only keep track of the furthest progress point and not call
             * addProgress()
             */

            if (latest == null) {
                latest = spotMilestone;
            } else {

                // Get the experiment dates
                final Calendar ld = latest.getDateOfExperiment();
                final Calendar td = spotMilestone.getDateOfExperiment();

                // If the latest has a date
                if (ld != null) {
                    // If this one has a date
                    if (td != null) {
                        // If this one is more recent
                        if (td.compareTo(ld) > 0) {
                            latest = spotMilestone;
                        }
                    }
                }
            }
        }
        if (latest == null) {
            return null;
        }

        final ConstructProgressBean progress =
            this.getProgress(ConstructFilterBean.getOrganism(target),
                ConstructFilterBean.getOldProject(target), latest);
        progress.setTargetId(target.get_Hook());
        return progress;

    }

    /**
     * Add a progress element to the list for the current target/construct/milestone
     * 
     * @param organism
     * @param project
     * @return
     */
    private ConstructProgressBean getProgress(final String organism, final String project,
        final ConstructResultBean spotMilestone) {

        // Check we have a SPOTConstructMilestone
        if (spotMilestone == null) {
            return null;
        }

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

        return progress;

    }

}
