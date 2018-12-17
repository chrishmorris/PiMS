/**
 * 
 */
package org.pimslims.lab.primer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pimslims.lab.primer.PrimerDesigner.PrimerTm;
import org.pimslims.lab.sequence.DnaSequence;

/**
 * @author Marc Savitsky OPPF 03-02-07
 * 
 */
public class PimsSDMPrimerDesigner {
    public static PimsSDMPrimerDesigner getSDMPrimerDesigner() {
        return new PimsSDMPrimerDesigner();
    }

    private PimsSDMPrimerDesigner() {// empty
    }

    /**
     * PimsSDMPrimerDesigner.makePrimers
     * 
     * @see org.pimslims.lab.primer.DesignPrimers#makePrimers(java.lang.String[], float)
     * 
     *      parameters are String[] {newSequence, wildSequence}
     */
    public List<String> makePrimers(final String[] seq, final float desiredTm) {
/*
        for (int i = 0; i < seq.length; i++) {
            if (i == 0) {
                System.out.println("PimsSDMPrimerDesigner mutated sequence [" + seq[i] + "]");
            }
            if (i == 1) {
                System.out.println("PimsSDMPrimerDesigner    wild sequence [" + seq[i] + "]");
            }
        } */

        assert seq.length == 2;

        final DnaSequence wild = new DnaSequence(seq[1]);
        final DnaSequence sequence = new DnaSequence(seq[0]);
        final List<PrimerTm> primers = new ArrayList<PrimerTm>();

        if (PrimerDesigner.MAX_LENGTH > sequence.getLength()) {
            throw new IllegalArgumentException("DNA sequence must be at least " + PrimerDesigner.MAX_LENGTH
                + " nucleotides");
        }

        int firstMutation = 0;
        int lastMutation = 0;
        int mutationCount = 0;
        /*
        for (int i = 0; i < wild.getLength(); i++) {
            if (wild.getSequence().charAt(i) != sequence.getSequence().charAt(i)) {
                System.out.println("mutation at [" + i + ":" + wild.getSequence().charAt(i) + ":"
                    + sequence.getSequence().charAt(i) + "]");
                if (firstMutation == 0) {
                    firstMutation = i;
                }
                lastMutation = i;
                mutationCount++;
            }
        }
        */

        firstMutation = PimsSDMPrimerDesigner.firstMutation(seq);
        lastMutation = PimsSDMPrimerDesigner.lastMutation(seq);

        if (wild.getLength() == sequence.getLength()) {
            mutationCount = lastMutation - firstMutation + 1;
        }
        if (wild.getLength() > sequence.getLength()) {
            mutationCount = wild.getLength() - sequence.getLength();
        }
        if (sequence.getLength() > wild.getLength()) {
            mutationCount = sequence.getLength() - wild.getLength();
        }

        if (mutationCount == 0) {
            throw new IllegalArgumentException("No Mutations have been made");
        }

        int start = firstMutation - 35;
        if (start < 0) {
            start = 0;
        }

        int end = lastMutation + 35;
        if (end > sequence.getLength()) {
            end = sequence.getLength();
        }

        //final PrimerDesigner pd = new PrimerDesigner(desiredTm);
        for (int z = start; z < (firstMutation - 10); z++) {
            for (int y = (lastMutation + 10); y < end; y++) {
                final String primerStr = sequence.getSequence().substring(z, y);
                PimsSDMPrimerDesigner.calcSDMTms(primerStr, primers, mutationCount, desiredTm);
            }
        }

        // Primer with Tm closest to required will be at the top
        Collections.sort(primers);

        // Now find other primers
        final List<PrimerTm> filteredList = new ArrayList<PrimerTm>();
        for (final PrimerTm ptm : primers) {
            //System.out.println("PimsSDMPrimerDesigner filter [" + ptm.toString() + ":" + ptm.hasProperEnd()
            //    + ":" + ptm.hasTmWithinSDMBoundaries() + "]");
            if (ptm.hasProperEnd() && ptm.hasTmWithinSDMBoundaries()) {
                filteredList.add(ptm);
            }
        }
        Collections.sort(filteredList);
        final List<String> sequences = new ArrayList<String>(filteredList.size() + 1);
        for (final PrimerTm ptm : filteredList) {
            sequences.add(ptm.seq);
        }

        return sequences;
    }

    static void calcSDMTms(final String primer, final List<PrimerDesigner.PrimerTm> primers, final int mutationCount,
        final float desiredTm) {
        // This temperature calculation method is going to be more precise,
        // but this is up to Susy which method to use.
        // float pTm = new DnaSequence(primer).getPrimerTm();
        final float pTm = new DnaSequence(primer).getSDMTm(mutationCount);
        //final DecimalFormat twoDForm = new DecimalFormat("#.##");
        //System.out.println("PrimerDesigner add [" + Float.valueOf(twoDForm.format(pTm)) + ":" + primer + "]");
        primers.add(new PrimerDesigner.PrimerTm(primer, pTm, desiredTm));
    }

    public static String getMutation(final String[] seq) {

        return new DnaSequence(seq[0]).getSequence().substring(PimsSDMPrimerDesigner.firstMutation(seq),
            PimsSDMPrimerDesigner.lastMutation(seq));
    }

    public static float calcTm(final String primer, final int mutationCount) {
        if (primer == null) {
            return 0;
        }
        final DnaSequence dna = new DnaSequence(primer);
        return dna.getSDMTm(mutationCount);
    }

    /**
     * 
     * PimsSDMPrimerDesigner.countMutations
     * 
     * @param seq
     * @return
     * @throws IllegalAlphabetException
     * @throws IllegalSymbolException
     * 
     *             parameters are String[] {newSequence, wildSequence}
     */
    public static int countMutations(final String[] seq) {
        int mutationCount = 0;
        final CodonBean[] wildCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[1])).toArray(new CodonBean[0]);
        final CodonBean[] newCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[0])).toArray(new CodonBean[0]);

        for (int i = PimsSDMPrimerDesigner.firstMutation(seq) / 3; i <= +PimsSDMPrimerDesigner
            .lastMutation(seq) / 3; i++) {
            mutationCount += wildCodons[i].compareTriplet(newCodons[i]);
        }
        return mutationCount;
    }

    /**
     * 
     * PimsSDMPrimerDesigner.firstMutation
     * 
     * @param seq
     * @return
     * @throws IllegalAlphabetException
     * @throws IllegalSymbolException
     * 
     *             parameters are String[] {newSequence, wildSequence}
     */
    public static int firstMutation(final String[] seq) {

        final CodonBean[] wildCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[1])).toArray(new CodonBean[0]);
        final CodonBean[] newCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[0])).toArray(new CodonBean[0]);

        for (int i = 0; i < wildCodons.length; i++) {
            if (!wildCodons[i].equals(newCodons[i])) {
                if (wildCodons.length == newCodons.length) {
                    //System.out.println("first [" + (i * 3) + ":" + wildCodons[i].firstDiff(newCodons[i])
                    //    + "]");
                    return i * 3 + (wildCodons[i].firstDiff(newCodons[i]) - 1);
                } else {
                    return i * 3;
                }
            }
        }

        return 0;
    }

    /**
     * 
     * PimsSDMPrimerDesigner.lastMutation
     * 
     * @param seq
     * @return
     * @throws IllegalAlphabetException
     * @throws IllegalSymbolException
     * 
     *             parameters are String[] {newSequence, wildSequence}
     */
    public static int lastMutation(final String[] seq) {

        final CodonBean[] wildCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[1])).toArray(new CodonBean[0]);
        final CodonBean[] newCodons =
            CodonBean.getCodonBeans(new DnaSequence(seq[0])).toArray(new CodonBean[0]);

        for (int i = 0; i < wildCodons.length; i++) {

            if (newCodons.length - i - 1 < 0) {
                break;
            }

            //System.out.println("last [" + i + ":" + (wildCodons.length - i - 1) + ","
            //    + wildCodons[wildCodons.length - i - 1] + ":" + (newCodons.length - i - 1) + ","
            //    + newCodons[newCodons.length - i - 1] + "]");

            if (!wildCodons[wildCodons.length - i - 1].equals(newCodons[newCodons.length - i - 1])) {

                if (wildCodons.length == newCodons.length) {

                    //System.out.println("last [" + (newCodons.length - i - 1) * 3 + ":"
                    //    + wildCodons[wildCodons.length - i - 1].lastDiff(newCodons[newCodons.length - i - 1])
                    //    + "]");

                    return (newCodons.length - i - 1)
                        * 3
                        + (wildCodons[wildCodons.length - i - 1]
                            .lastDiff(newCodons[newCodons.length - i - 1]) - 1);
                } else {
                    return (newCodons.length - i) * 3;
                }
            }
        }
        return 0;
    }

}
