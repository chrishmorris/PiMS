/**
 * 
 */
package org.pimslims.lab.primer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.pimslims.lab.primer.PrimerDesigner.PrimerTm;
import org.pimslims.lab.sequence.DnaSequence;

/**
 * @author Susy Griffiths YSBL 03-02-07
 * 
 */
public class PimsStandardPrimerDesigner implements DesignPrimers {
    public static DesignPrimers getStandardPrimerDesigner() {
        return new PimsStandardPrimerDesigner();
    }

    private PimsStandardPrimerDesigner() {// empty
    }

    @Override
    public List<String> makePrimers(final String[] sequence, final float desiredTm) {

        return PimsStandardPrimerDesigner.doMakePrimers(sequence, desiredTm);
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
    public static List<String> doMakePrimers(final String[] seq, final float desiredTm) {

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

    /**
     * Method to create an array list of YorkPrimerBeans
     * 
     * @param primers List<String> of primers
     * @param type String F= forward R= reverse
     * @return List of org.pimslims.lab.YorkPrimerBean
     */
    public static List<YorkPrimerBean> makeYPBs(final List<String> primers, final String type) {
        final List<YorkPrimerBean> ypbs = new ArrayList<YorkPrimerBean>();
        final Iterator pit = primers.iterator();
        while (pit.hasNext()) {
            // create primerBean for each primer
            final YorkPrimerBean pb = new YorkPrimerBean();
            // setconstructId, primerSeq,F or R, Tm and length
            final String primer = pit.next().toString();
            pb.setPrimerSeq(primer);
            pb.setPrimerType(type);
            final float primTm = PimsStandardPrimerDesigner.calcTm(primer);
            pb.setPrimerTm(primTm);
            pb.setPrimerLength(Integer.valueOf(primer.length()));
            ypbs.add(pb);
        }
        return ypbs;
    }

    /**
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @param dP an instance of org.pimslims.lab.DesignPrimers
     * @param desTm String value for primer design Tm
     * @param ptype String to define primer type- forward or reverse
     * @return List of org.pimslims.lab.YorkPrimerBean
     * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
     * @throws IllegalAlphabetException if BioJavav alphabet not specified
     */
    public static List<YorkPrimerBean> makePrimerBeanList(final DesignPrimers dP, final float desTm,
        final String ptype, final String dnaSeq) {
        String seq = "";
        if ("R".equals(ptype)) {
            final DnaSequence dna = new DnaSequence(dnaSeq);
            final DnaSequence rev = dna.getReverseComplement();
            seq = rev.getSequence();
        } else {
            seq = dnaSeq;
        }
        assert null != seq;

        final List<String> primers = dP.makePrimers(new String[] { seq }, desTm);
        return PimsStandardPrimerDesigner.makeYPBs(primers, ptype);
    }

    /**
     * @param cb an instance of org.pimslims.presentation.construct.ConstructBean
     * @param primerBeans
     * @param rprimerBeans
     * @param dP
     * @throws IllegalSymbolException if Non-standard letters in DNA Sequence
     * @throws IllegalAlphabetException if BioJavav alphabet not specified
     */
    public static void makeFandRPrimerBeanLists(final List<YorkPrimerBean> primerBeans,
        final List<YorkPrimerBean> rprimerBeans, final Float desiredTm, final String dnaSeq) {
        final float desTm = desiredTm.floatValue();
        final String fType = "F";
        primerBeans.addAll(PimsStandardPrimerDesigner.makePrimerBeanList(
            PimsStandardPrimerDesigner.getStandardPrimerDesigner(), desTm, fType, dnaSeq));

        // Need the reverse complement sequence to calculate rev primer
        final String rType = "R";
        rprimerBeans.addAll(PimsStandardPrimerDesigner.makePrimerBeanList(
            PimsStandardPrimerDesigner.getStandardPrimerDesigner(), desTm, rType, dnaSeq));
    }

    public static float calcTm(final String primer) {
        if (primer == null) {
            return 0;
        }
        final DnaSequence dna = new DnaSequence(primer);
        return dna.getTm();

    }

}
