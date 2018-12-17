package org.pimslims.presentation.construct;

import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;

/**
 * This class annotates SPOT constructs It has a constructor that accepts a SPOT construct By Johan?
 * 
 */
public class ConstructAnnotator { // this class contains static methods only

    /**
     * @param cb org.pimslims.presentation.construct.ConstructBean
     */
    public static void annotateFinalProt(final ConstructBean cb) {

        try {
            if (cb.getFinalProt() == null) {
                throw new IllegalStateException(
                    "Please only run constructAnnotator() on the constructBean after a value has for the final protein sequence");
            }

            try {
                final ProteinSequence finalProt = new ProteinSequence(cb.getFinalProt());
                cb.setWeight(new Double(finalProt.getMass()));
                cb.setExtinction(new Double(finalProt.getExtinctionCoefficient()));
                cb.setAbs01pc(new Double(finalProt.getAbsPt1percent()));
                cb.setProtPi(new Double(finalProt.getPI()));

            } catch (final IllegalArgumentException e) {
                throw new IllegalStateException("Bad protein sequence: " + cb.getFinalProt());
            }
            try {
                final ProteinSequence exp = new ProteinSequence(cb.getExpressedProt());
                cb.setExpProtWeight(new Double(exp.getMass()));
                cb.setExpProtExtinction(new Double(exp.getExtinctionCoefficient()));
                cb.setExpProtabs01pc(new Double(exp.getAbsPt1percent()));
                cb.setExpProtLen((long) exp.getLength());
                cb.setExpProtPi(new Double(exp.getPI()));

            } catch (final IllegalArgumentException e) {
                // bad sequence - don't annotate
            }

            // temporary fix until biojava
            // sorts molecular weight and pI calculation of polypeptides
            // containing ambiguity symbols
            // expected in version 1.5
        } catch (final NullPointerException ex) {
            cb.setWeight(new Double(-1));
            cb.setProtPi(new Double(-1));
            cb.setExpProtWeight(new Double(-1));
            cb.setExpProtPi(new Double(-1));
        }
    }

    /**
     * @param bean org.pimslims.presentation.construct.ConstructBean
     * @return String representing PCR product
     */
    public static String getPCRProductSequence(final ConstructBean bean) {

        final String targetDnaSeq = bean.getDnaSeq();
        if (bean.getSdmConstruct()) {
            return targetDnaSeq;
        }

        final String revPrimer = bean.getRevPrimer();
        if (null == bean.getFwdPrimer() || null == revPrimer || null == targetDnaSeq) {
            return "";
        }

        final StringBuffer sb = new StringBuffer();
        sb.append(bean.getFwdPrimer());
        final String targetDnaWithoutOverlaps = ConstructAnnotator.getTargetDnaWithoutOverlaps(bean);
        sb.append(targetDnaWithoutOverlaps);
        final String rc = ConstructAnnotator.getReverseComplement(revPrimer);
        sb.append(rc);

        final String ret = sb.toString().toUpperCase();
        return ret;
    }

    /**
     * ConstructAnnotator.getReverseComplement
     * 
     * @param revPrimer
     * @return
     */
    private static String getReverseComplement(final String revPrimer) {
        final DnaSequence reverseComplement = new DnaSequence(revPrimer).getReverseComplement();
        final String rc = reverseComplement.getSequence();
        return rc;
    }

    /**
     * ConstructAnnotator.getTargetDnaWithoutOverlaps
     * 
     * @param bean
     * @param targetDnaSeq
     * @return
     */
    private static String getTargetDnaWithoutOverlaps(final ConstructBean bean) {
        if (null == bean.getDnaSeq()) {
            return null;
        }
        return bean.getDnaSeq().substring(bean.getFwdOverlapLen(),
            bean.getDnaSeq().length() - bean.getRevOverlapLen());
    }

    /**
     * @param cb org.pimslims.presentation.construct.ConstructBean
     */
    public static void annotatePCRProductGC(final ConstructBean cb) {
        if (cb.getPcrProductSeq() != null) {
            final DnaSequence pProd = new DnaSequence(cb.getPcrProductSeq());
            cb.setPcrProductGC((double) pProd.getGCContent());
        }
    }

    /**
     * Method to ensure primer sequence contains no non-sequence characters
     * 
     * @param primer String sequence of primer to clean
     * @return cleanPrimer String sequence of cleaned primer
     */
    public static String cleanPrimer(final String primer) {
        String cleanPrimer = "";
        final String dirtyPrimer = primer;
        cleanPrimer = dirtyPrimer.replaceAll("[^A-Z]", "");
        return cleanPrimer;

    }

    // primers may make a mutation to target, e.g. replace GTG with ATG
    public static String getDnaSeqTranslated(final ConstructBean cb) {
        final String dnaSeq =
            cb.getFwdOverlap() + ConstructAnnotator.getTargetDnaWithoutOverlaps(cb)
                + ConstructAnnotator.getReverseComplement(cb.getRevOverlap());
        return ThreeLetterProteinSeq.translate(dnaSeq);
    }

    public static void calcFinalProt(final ConstructBean cb) {
        if (null == cb.getDnaSeq()) {
            return;
        }
        try {
            final String protein =
                (null == cb.getFinalProtN() ? "" : cb.getFinalProtN())
                    + ConstructAnnotator.getDnaSeqTranslated(cb)
                    + (null == cb.getFinalProtC() ? "" : cb.getFinalProtC());
            cb.setFinalProt(protein);
        } catch (final IllegalArgumentException e) {
            // bad sequence, cannot annotate but must still show construct
        }
    }

    public static void calcExpressedProt(final ConstructBean cb) {
        if (null == cb.getDnaSeq()) {
            return;
        }
        try {
            cb.setExpressedProt(cb.getForwardTag() + ConstructAnnotator.getDnaSeqTranslated(cb)
                + cb.getReverseTag());
        } catch (final IllegalArgumentException e) {
            // bad sequence, cannot annotate but must still show construct
            e.printStackTrace();
        }
    }

}
