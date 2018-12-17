/**
 * current-pims-web org.pimslims.lab.primer PrimerDesigner.java
 * 
 * @author Petr Troshin
 * @date 12 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
 * 
 *           Copyright (c) 2008 pvt43
 * 
 * 
 */
package org.pimslims.lab.primer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pimslims.lab.sequence.DnaSequence;

/*
 * Specs: (thanks Susy) The functionality I was asked to implement finds the longest primer which is as close
 * as possible to the user-specified Tm and is 50 nucleotides or less in length. Matching the Tm is more
 * important than size but 50 nucleotides is the maximum length. It then finds shorter and longer primers
 * (within 5oC of the Tm) which end in no more than 2 Gs or Cs. All primers should be 50 nucleotides or less.
 */

public class PrimerDesigner {

    public static final int MAX_LENGTH = 50;

    public static final int MIN_LENGTH = 10;

    final float desiredTm;

    // Change this to get primers within smaller temperature range (closer to desired Tm)
    public static float tmFork = 5f;

    public PrimerDesigner(final float desiredTm) {
        this.desiredTm = desiredTm;
    }

    static class PrimerTm implements Comparable<PrimerTm> {

        final String seq;

        final float tm;

        final float desiredTm;

        public PrimerTm(final String seq, final float tm, final float desiredTm) {
            this.seq = seq;
            this.tm = tm;
            this.desiredTm = desiredTm;
        }

        /**
         * @see java.lang.Comparable#compareTo(java.lang.Object) The primers with closest to desirable Tm will
         *      be on top of the collection
         */
        public int compareTo(final PrimerTm o) {
            return new Float(Math.abs(this.desiredTm - this.tm)).compareTo(Math.abs(this.desiredTm - o.tm));
        }

        boolean endsWithGC() {
            return this.seq.endsWith("C") || this.seq.endsWith("G");
        }

        boolean endsWithDoubleGC() {
            final char penultimate = this.seq.charAt(this.seq.length() - 2);
            return penultimate == 'C' || penultimate == 'G';
        }

        boolean hasProperEnd() {
            return this.endsWithGC() && !this.endsWithDoubleGC();
        }

        boolean hasTmWithinBoundaries() {
            if (this.desiredTm - PrimerDesigner.tmFork <= this.tm
                && this.tm <= this.desiredTm + PrimerDesigner.tmFork) {
                return true;
            }
            return false;
        }

        boolean hasTmWithinSDMBoundaries() {
            if (this.desiredTm <= this.tm && this.tm <= this.desiredTm + PrimerDesigner.tmFork) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return this.seq + ":" + this.tm;
        }
    }

    /**
     * @see org.pimslims.lab.primer.DesignPrimers#makePrimers(java.lang.String, float) Method must return the
     *      list of shortest possible primers with Tm +-5C from user specified Tm. In addition primers must
     *      end with G or C but NOT end with more than 2 GC repeats.
     * 
     *      Method return one primer only designed for the left part of sequence e.g. 5' It must be called
     *      twice with reverse sequence to obtain primers for 3' end. This seems to comply with original
     *      intention.
     * 
     */

    //TODO is this used for SDM primers?
    public static List<String> makePrimers(final String[] seq, final float desiredTm) {

        final PrimerDesigner pd = new PrimerDesigner(desiredTm);
        final DnaSequence sequence = new DnaSequence(seq[0]);
        final List<PrimerTm> primers = new ArrayList<PrimerTm>();
        if (PrimerDesigner.MAX_LENGTH > sequence.getLength()) {
            throw new IllegalArgumentException("DNA sequence must be at least " + PrimerDesigner.MAX_LENGTH
                + " nucleotides");
        }

        final String primerMax = sequence.getSequence().substring(0, PrimerDesigner.MAX_LENGTH);

        // make a list of forward primers
        for (int i = PrimerDesigner.MAX_LENGTH; i > PrimerDesigner.MIN_LENGTH; i--) {
            pd.calcTms(primerMax.substring(0, i), primers);
        }
        // Primer with Tm closest to required will be at the top
        Collections.sort(primers);

        // Now find other primers
        final List<PrimerTm> filteredList = new ArrayList<PrimerTm>();
        for (final PrimerTm ptm : primers) {
            if (ptm.hasProperEnd() && ptm.hasTmWithinBoundaries()) {
                filteredList.add(ptm);
            }
        }
        Collections.sort(filteredList);

        final List<String> sequences = new ArrayList<String>(filteredList.size() + 1);
        for (final PrimerTm ptm : filteredList) {
            sequences.add(ptm.seq);
        }
        sequences.add(0, primers.get(0).seq);

        return sequences;
    }

    void calcTms(final String primer, final List<PrimerTm> primers) {
        // This temperature calculation method is going to be more precise,
        // but this is up to Susy which method to use.
        // float pTm = new DnaSequence(primer).getPrimerTm();
        final float pTm = new DnaSequence(primer).getTm();
        primers.add(new PrimerTm(primer, pTm, this.desiredTm));
    }
}
