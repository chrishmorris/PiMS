/**
 * pims-web org.pimslims.command BlastSearch.java
 * 
 * @author Peter Troshin
 * @date 28 Sep 2007 Organisation STFC Daresbury Laboratory Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Peter Troshin * *
 * 
 */
package org.pimslims.bioinf.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.model.molecule.Molecule;

/**
 * Smith-Waterman Search
 * 
 */
public class SWSearch {

    private float cutoff = 0;

    //was private final Sequence templateSeq;

    private final String templateSeqString;

    final String templateName;

    final AlignmentParameters searchParam;

    /**
     * Constructor for test cases
     * 
     * @param name
     * @param templateSeq
     */
    public SWSearch(final String name, final String templateSeq) {
        this(name, templateSeq, null, 20, 1);
    }

    /**
     * @param name
     * @param templateSeq
     * @param matrixName
     * @param openingPenalty
     * @param extend
     */
    public SWSearch(final String name, final String templateSeq, final String matrixName,
        final int openingPenalty, final int extend) {
        this.templateSeqString = templateSeq;
        this.templateName = name;
        this.searchParam = new AlignmentParameters(matrixName, (short) openingPenalty, (short) extend);
    }

/*
    @Deprecated
    // only tested, not used
    public void setParameters(final String matrixName, final float openingPenalty, final float extend) {
        this.setParameters(matrixName, openingPenalty, extend, 2 * extend);
    }

    private void setParameters(final String matrixName, final float openingPenalty, final float extend,
        final float open) {
        this.searchParam.setMatrix(matrixName);
        this.searchParam.setOpeningPenalty(openingPenalty);
        this.searchParam.setExtend(extend);
        // was this.searchParam.open = open;
    } */

    /**
     * if you already know MolComponent, set it to molecule if you can not provide it, just set it to null
     * 
     * @return
     */
    public PimsAlignment align(final String name, final String seq, final boolean revComplement,
        final ReadableVersion rv, final Molecule molecule, final boolean isDNA) {
        return BiojavaAlignment.getSmithWatermanAlignment(name, seq, revComplement, molecule,
            this.searchParam.extend, this.searchParam.openingPenalty, this.searchParam.getMatrix(),
            this.templateName, this.templateSeqString, isDNA);
    }

    /**
     * SWSearch.align
     * 
     * JMD Convenience method to avoid creating unnecessary Molecules
     * 
     * @param namedSequences
     * @param isDNA
     * @return
     */
    public ArrayList<PimsAlignment> align(final Map<String, String> namedSequences, final boolean isDNA) {
        return this.align(namedSequences, null, null, isDNA);
    }

    private ArrayList<PimsAlignment> align(final Map<String, String> namedSequences,
        final ReadableVersion rv, final Map<String, Molecule> molecomponents, final boolean isDNA) {
        final ArrayList<PimsAlignment> alignments = new ArrayList<PimsAlignment>();
        for (final String name : namedSequences.keySet()) {
            Molecule molComponent = null;
            if (molecomponents != null) {
                molComponent = molecomponents.get(name);
            }
            PimsAlignment aw;
            try {
                aw = this.align(name, namedSequences.get(name), false, rv, molComponent, isDNA);
            } catch (final IllegalArgumentException e1) {
                continue; // not a valid DNA sequence
            }
            // Now reverse the chain if it is DNA
            if (isDNA) {

                final String revComplement =
                    new DnaSequence(namedSequences.get(name)).getReverseComplement().getSequence();
                final PimsAlignment revAw = this.align(name, revComplement, true, rv, molComponent, true);
                this.cutByScore(alignments, revAw);

            }
            this.cutByScore(alignments, aw);
        }

        Collections.sort(alignments);
        return alignments;
    }

    /**
     * @param alignments
     * @param aw
     */
    private void cutByScore(final ArrayList<PimsAlignment> alignments, final PimsAlignment aw) {
        if (this.cutoff != 0) {
            if (aw.getScore() > this.cutoff) {
                alignments.add(aw);
            }
        } else {
            alignments.add(aw);
        }
    }

    public ArrayList<PimsAlignment> align(final Collection<Molecule> components, final boolean isDNA,
        final ReadableVersion version) {
        if (components == null || components.size() == 0) {
            return new ArrayList();
        }
        final Map<String, String> namedSequences = new TreeMap<String, String>();
        final Map<String, Molecule> molComponents = new HashMap<String, Molecule>();
        for (final Molecule comp : components) {
            final String seq = comp.getSequence();
            if (Util.isEmpty(seq)) {
                continue;
            }
            namedSequences.put(comp.getName(), SWSearch.cleanSequence(seq));
            molComponents.put(comp.getName(), comp);
        }
        return this.align(namedSequences, version, molComponents, isDNA);
    }

    public void setCutoff(final float score) {
        this.cutoff = score;
    }

    public static String cleanSequence(final String sequence) {
        return sequence.replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "");
    }
}
