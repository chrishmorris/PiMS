/**
 * current-pims-web org.pimslims.lab.sequence PlatePCRProductSequenceComporator.java
 * 
 * @author Petr Troshin
 * @date 21 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.lab.sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.bioinf.local.SequenceGetter;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * This class is for comparing PCRProduct sequences of the constructs associated with Plate experiments to
 * actual sequences which come from sequenator
 * 
 * PlatePCRProductSequenceComporator
 * 
 */
public class PlatePCRProductSequenceComparator {
    /*
     * Plate
     */
    final ExperimentGroup plate;

    /*
     * Actual sequences loaded by PositionAwareSequence.loadFasta method
     */
    final ArrayList<PositionAwareSequence> actualSequences;

    /**
     * @param plate
     * @param actualSequences
     */
    public PlatePCRProductSequenceComparator(final ExperimentGroup plate,
        final ArrayList<PositionAwareSequence> actualSequences) {
        this.plate = plate;
        this.actualSequences = actualSequences;
    }

    public ArrayList<PimsAlignment> compare() {
        // Position -> pcr product sequence
        final Map<String, String> pcrprds = SequenceGetter.getPCRProductsFromPlate(this.plate);
        final ArrayList<PimsAlignment> misalls = new ArrayList<PimsAlignment>();
        final ReadableVersion rv = this.plate.get_Version();
        for (final Iterator iterator = pcrprds.keySet().iterator(); iterator.hasNext();) {
            final String position = (String) iterator.next();
            final String sequence = pcrprds.get(position);

            SWSearch searcher;
            searcher = new SWSearch(position, sequence, "IDENTITY", 10, 1);
            for (final PositionAwareSequence ps : this.actualSequences) {
                final PimsAlignment alignment =
                    searcher.align(ps.fileName, ps.sequence, false, rv, null, true);
                if (alignment.getGaps() != 0 && alignment.getIdentity() != 1) {
                    misalls.add(alignment);
                }
            }
        }
        return misalls;
    }

    /**
     * 
     * @param rv
     * @return A list of plates that have more then 10% of wells with sequences in a TrialMolcomponent role
     *         thus making them worth sequence comparison excersise
     */
    public static ArrayList<ExperimentGroup> getEligiblePlates(final ReadableVersion rv) {

        final ArrayList<ExperimentGroup> eligiblegroups = new ArrayList<ExperimentGroup>();
        final Collection<ExperimentGroup> expgs = rv.getAll(ExperimentGroup.class);

        for (final ExperimentGroup expg : expgs) {
            // TODO filter by the protocol and/or experiment type
            // Filter by holder
            final Holder plate = HolderFactory.getPlate(expg);
            if (plate == null) {
                continue;
            }
            final List<String> columns = HolderFactory.getColumns(plate);
            final List<String> rows = HolderFactory.getRows(plate);
            /*
            TODO If information of the number of sequence is available we can limit this further
             */
            int sequenceCounter = 0;
            for (final Experiment exp : expg.getExperiments()) {
                final Project expb = exp.getProject();
                if (expb == null) {
                    continue;
                }
                // Filter by the bluePrintComponent with PCR product sequence
                final Set<ResearchObjectiveElement> bls =
                    ((ResearchObjective) expb).getResearchObjectiveElements();
                if (bls == null || bls.isEmpty()) {
                    continue;
                }
                if (PlatePCRProductSequenceComparator.filterByPCRSequence(expg, bls)) {
                    sequenceCounter++;
                }
            }
            if (sequenceCounter != 0 && ((columns.size() * rows.size()) / sequenceCounter) < 10) {
                eligiblegroups.add(expg);
            }
        }
        return eligiblegroups;
    }

    /**
     * This method return true if there is a sequence in TrialMolComponent role
     * 
     * @param ExperimentGroup expg
     * @param Set<BlueprintComponet>bls
     * @return true if at least one of blueprintcomponents has a sequence in TrialMolcomponent role
     */
    private static boolean filterByPCRSequence(final ExperimentGroup expg,
        final Set<ResearchObjectiveElement> bls) {
        for (final ResearchObjectiveElement b : bls) {
            final Set<Molecule> trialMols = b.getTrialMolComponents();
            if (trialMols == null || trialMols.isEmpty()) {
                continue;
            }
            final String seq = SequenceGetter.getPCRProduct(trialMols);
            if (!Util.isEmpty(seq)) {
                return true;
            }
        }
        return false;
    }

}
