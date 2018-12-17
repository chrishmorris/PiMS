/**
 * pims-web org.pimslims.bioinf.local SequenceGetter.java
 * 
 * @author Peter Troshin (aka pvt43)
 * @date 28 Sep 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.local;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * SequenceGetter
 * 
 */
public class SequenceGetter {

    /**
     * PROTEIN String
     */
    public static final String PROTEIN = "Protein";

    /**
     * PCR_PRODUCT String
     */
    public static final String PCR_PRODUCT = "PCR Product";

    public static Collection<Molecule> getDNAs(final ReadableVersion rv) {
        final Collection<Molecule> dnas =
            rv.findAll(org.pimslims.model.molecule.Molecule.class, Molecule.PROP_MOLTYPE, "DNA");
        return dnas;
    }

    public static Collection<Molecule> getProteins(final ReadableVersion rv) {
        final Collection<Molecule> proteins =
            rv.findAll(org.pimslims.model.molecule.Molecule.class, Molecule.PROP_MOLTYPE, "protein");
        return proteins;
    }

    private static final Set<Character> ALLOWED = new HashSet<Character>();
    static {
        SequenceGetter.ALLOWED.add('A');
        SequenceGetter.ALLOWED.add('C');
        SequenceGetter.ALLOWED.add('G');
        SequenceGetter.ALLOWED.add('T');
        SequenceGetter.ALLOWED.add('\n');
        SequenceGetter.ALLOWED.add(' ');
        SequenceGetter.ALLOWED.add('-'); // sequence alignment may contain gaps
        SequenceGetter.ALLOWED.add('N'); // most commonly used ambiguity symbol
    }

    public static boolean isDNA(String sequence) {
        if (Util.isEmpty(sequence)) {
            throw new AssertionError("Please provide the sequence");
        }
        float counter = 0;

        // DNA - ACGTRYMKSWBDHVN

        // TODO Make it better
        sequence = sequence.trim();
        for (int i = 0; i < sequence.length(); i++) {
            final char a = sequence.toUpperCase().charAt(i);
            if (SequenceGetter.ALLOWED.contains(a)
                && (a != 'E' && a != 'F' && a != 'I' && a != 'L' && a != 'P' && a != 'Q' && a != 'R'
                    && a != 'X' && a != 'Z')) {
                counter++;
            }
        }
        // allow the rest be 10% of sequence
        if (counter / sequence.length() > 0.9) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param ExperimentGroup plate
     * @return Map Position in the plate -> PCR product sequence
     */
    public static Map<String, String> getPCRProductsFromPlate(final ExperimentGroup plate) {
        final Map<String, String> pcrp = new HashMap<String, String>();
        for (final Experiment exp : plate.getExperiments()) {
            final ResearchObjective expb = (ResearchObjective) exp.getProject();
            if (expb == null) {
                continue;
            }
            final Set<ResearchObjectiveElement> bls = expb.getResearchObjectiveElements();
            if (bls == null || bls.isEmpty()) {
                continue;
            }
            for (final ResearchObjectiveElement b : bls) {
                final Set<Molecule> trialMols = b.getTrialMolComponents();
                if (trialMols == null || trialMols.isEmpty()) {
                    continue;
                }
                pcrp.put(HolderFactory.getPositionInHolder(exp), SequenceGetter.getPCRProduct(trialMols));
            }

        }
        return pcrp;
    }

    /**
     * 
     * @param experiment
     * @return PCRProductLength if there is a TrialMolecomponent associated with experiment through
     *         researchObjective->blueprintComponent->TrialMolComponent
     */
    public static int getPCRProductLength(final Collection<Molecule> trialMols) {
        final String sequence = SequenceGetter.getPCRProduct(trialMols);
        if (sequence != null) {
            return sequence.trim().length();
            // TODO get this under test
        }
        return -1;
    }

    /**
     * TODO get this under test
     * 
     * @param blueComp a set of molecules
     * @return PCR Product sequence, if any
     */
    public static String getPCRProduct(final Collection<Molecule> molComps) {
        String pcrProdSeq = null;
        for (final Molecule mol : molComps) {
            //System.out.println("SequenceGetter.getPCRProduct mol [" + mol.getName() + "]");
            if (mol.getName().endsWith(SequenceGetter.PCR_PRODUCT)) {
                pcrProdSeq = mol.getSequence();
                /* TODO using following code when ComponentCategory fixed in Construct Bean Writer:createMolComp
                for (final ComponentCategory cc : mol.getCategories()) {
                    System.out.println("SequenceGetter.getPCRProduct cc [" + cc.getName() + "]");
                    if (cc.getName().equals("PCR Product")) {
                        pcrProdSeq = mol.getSeqString();
                    }
                }
                */
            }
        }
        return pcrProdSeq;
    }

    /**
     * 
     * @param experiment
     * @return ProteinMW if there is a TrialMolecomponent associated with experiment through
     *         researchObjective->blueprintComponent->TrialMolComponent
     */
    public static float getProteinMW(final Collection<Molecule> trialMols) {
        final String sequence = SequenceGetter.getProtein(trialMols);

        try {
            if (sequence != null) {
                final ProteinSequence protein = new ProteinSequence(sequence);
                return protein.getMass();
            }
        } catch (final IllegalArgumentException e) { // Do Nothing
        }
        return -1;
    }

    /**
     * 
     * @param blueComp
     * @return PCR Product sequence
     */
    public static String getProtein(final Collection<Molecule> molComps) {
        String proteinSeq = null;
        for (final Molecule mol : molComps) {
            if (mol.getName().endsWith(SequenceGetter.PROTEIN)) {
                proteinSeq = mol.getSequence();
                /* TODO using following code when ComponentCategory fixed in Construct Bean Writer:createMolComp
                for (final ComponentCategory cc : mol.getCategories()) {
                    if (cc.getName().equals("Protein")) {
                        proteinSeq = mol.getSeqString();
                    }
                }
                */
            }
        }
        return proteinSeq;
    }
}
