/**
 * 
 */
package uk.ac.sspf.spot.pims;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.ConstructUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.presentation.servlet.utils.ProgressListener;

import uk.ac.sspf.spot.beans.ConstructFastaBean;
import uk.ac.sspf.spot.beans.ConstructFilterBean;

/**
 * <p>
 * Class to populate the ConstructFastaBean beans, taking into account any filter supplied via a
 * ConstructFilterBean.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * 
 * <pre>
 * // Get a ConstructProgress
 * ConstructFasta cf = new ConstructProgress(readableVersion);
 * 
 * // Warning - potentially slow
 * ArrayList fastaList = cf.getFastaList();
 * </pre>
 * 
 * @author Jon Diprose
 */
@Deprecated
public class ConstructFasta extends ConstructReport {

    public static final String SPOTCONSTRUCT_COMPONENT_TYPE = ConstructUtility.SPOTCONSTRUCT;

    /**
     * Constructor specifying the ReadbleVersion and the filter
     */
    public ConstructFasta(final ReadableVersion readableVersion, final ConstructFilterBean filter) {
        this.setReadableVersion(readableVersion);
        this.setFilter(filter);
    }

    /**
     * Get the filtered progress list. Warning - this goes away and queries the database so may be very slow!
     * Returns an ArrayList of SPOTConstructProgressBeans
     * 
     * @param targets
     * @param progressBar
     * 
     * @return An ArrayList containing the ConstructProgressBean elements
     * 
     * @throws IOException
     */

    public Collection<ConstructFastaBean> getProgressList(final Collection<Target> targets,
        final ProgressListener progressBar) throws IOException {
        // Declare the ArrayList
        final ArrayList<ConstructFastaBean> fastaList = new ArrayList<ConstructFastaBean>();
        // Loop over all the targets
        int count = 0;
        final Iterator iter = targets.iterator();
        while (iter.hasNext()) {
            // Get this target
            final Target target = (Target) iter.next();

            final boolean isWanted = this.filter.isWanted(target);
            if (isWanted) {

                // Get the ExpBlueprints for this target
                this.findConstructs(fastaList, target);
            }
            count = count + 1;
            if (count % 20 == 0) {
                progressBar.setProgress(count);
            }
        } // end while// Return the ArrayList
        return fastaList;
    }

    /**
     * Find the progress of all the constructs for the current target
     * 
     * @param fastaList
     * @param target
     * @param spotTarget
     * 
     */
    private void findConstructs(final Collection<ConstructFastaBean> fastaList, final Target target) {

        // Check we have a current target
        if (target == null) {
            return;
        }

        // Get the ResearchObjectiveElements for this Target
        final Collection<ModelObject> blueprintComponents =
            target.findAll(Target.PROP_RESEARCHOBJECTIVEELEMENTS,
                ResearchObjectiveElement.PROP_COMPONENTTYPE, ConstructFasta.SPOTCONSTRUCT_COMPONENT_TYPE);

        // Loop over all the ResearchObjectiveElements
        for (final ModelObject mo : blueprintComponents) {

            // Get this ResearchObjectiveElement
            final ResearchObjectiveElement blueprintComponent = (ResearchObjectiveElement) mo;

            // Get the ExpBlueprint
            final ResearchObjective expBlueprint = blueprintComponent.getResearchObjective();

            // Get the full ConstructBean
            final ConstructBean spotConstruct = ConstructBeanReader.readConstruct(expBlueprint);

            // Check we have a sequence
            if (spotConstruct.getFinalProt() != null) {

                // Add a fasta element
                String organism = "";
                if (null != target.getSpecies()) {
                    organism = target.getSpecies().getName();
                }
                //final String project = "";
                /* if (!target.getProjects().isEmpty()) {
                    project = target.getProjects().iterator().next().getCompleteName();
                } */

                this.addFasta(fastaList, spotConstruct.getName(), spotConstruct.getFinalProt(), organism, "");

            }

        }

    }

    /**
     * Add a progress element to the list for the current target/construct/milestone
     * 
     * @param fastaList
     * @param constructID
     * @param finalProt
     * @param organism
     * @param project
     */
    private void addFasta(final Collection<ConstructFastaBean> fastaList, final String constructID,
        final String finalProt, final String organism, final String project) {

        // Create a new ConstructProgressBean
        final ConstructFastaBean fasta = new ConstructFastaBean();

        // Populate the ConstructProgressBean
        fasta.setDescription(constructID + " - " + organism);
        fasta.setSequence(finalProt);

        // Add this progress point
        fastaList.add(fasta);

    }

}
