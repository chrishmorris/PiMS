/**
 * current-pims-web org.pimslims.utils.sequenator SequenceAnalyser.java
 * 
 * @author pvt43
 * @date 16 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pvt43 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.utils.sequenator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.lab.Util;

/**
 * SequenceAnalyser
 * 
 * Utilities for defining non-ambiguous part of sequence in the assumption that both ends of the sequence
 * contain reading errors defined as N. Please note that other DNA ambiguous letters are not supported!
 * 
 * 
 */
public class DnaSequenceAnalyser {

    public static String cleanSequence(final String sequence) {
        final Pattern WHITE_SPACE = Pattern.compile("\\s");
        final Matcher m = WHITE_SPACE.matcher(sequence);
        final String s = m.replaceAll("").toUpperCase();
        return s.replaceAll("\n", "");
    }

    // Return a part of the ambiguous sequence which is flanked by four ambiguous DNA letters (NNNN) 
    public static String getWellReadFragment(String asequence) {

        if (Util.isEmpty(asequence)) {
            return "";
        }

        asequence = DnaSequenceAnalyser.cleanSequence(asequence);
        if (!asequence.contains("N")) {
            // Sequence is well defined no ambiguous characters found!
            return asequence;
        }
        final StringBuffer sb = new StringBuffer(asequence);
        // Sequence is more likely to contain errors only
        //System.out.println("AAA:" + (double) DnaSequenceAnalyser.getAmbNucNumber(sb) / asequence.length());
        if (asequence.length() < 60 && (DnaSequenceAnalyser.getAmbNucNumber(sb) / asequence.length()) > 0.5) {
            return "";
        }
        // Sequence is more likely to contain too many errors 
        if (((double) DnaSequenceAnalyser.getAmbNucNumber(sb) / asequence.length()) > 0.7) {
            return "";
        }
        // Consider ends are well defined in this case and nothing to do here 
        if (!asequence.startsWith("NNNN") && !asequence.endsWith("NNNN")) {
            return asequence;
        }
        final int[] score = DnaSequenceAnalyser.calculateScore(sb, true);
        final int[] backwardScore = DnaSequenceAnalyser.calculateScore(sb, false);

        final int start = DnaSequenceAnalyser.getClearSeqStart(score) + 1;
        final int end = DnaSequenceAnalyser.getClearSeqEnd(backwardScore);

        if (start < end && start > 0 && end < sb.length()) {
            //System.out.println("Start" + start + " end " + end);
            //System.out.println("seq:" + asequence);
            asequence = sb.substring(start, end);
        } else {
            System.out.println("Wrong start & end sequence position calculation for seq:");
            System.out.println(asequence);
        }
        return asequence;
    }

    static int getAmbNucNumber(final StringBuffer sb) {
        int ns = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == 'N') {
                ns++;
            }
        }
        return ns;
    }

    private static int getClearSeqStart(final int[] score) {
        int maxLeft = 0;
        int maxLeftIdx = 0;
        boolean startNotReached = true;
        int goodDNACounter = 0;
        for (int i = 0; i < score.length; i++) {
            // Allow for some errors up to 4 nucleotides within the sequence (i.e. score > 40 )
            if ((maxLeft < score[i] || score[i] > 40) && startNotReached) {
                maxLeft = score[i];
                maxLeftIdx = i;
                goodDNACounter = 0;
            } else {
                goodDNACounter++;
                if (goodDNACounter > 50) {
                    startNotReached = false;
                }
            }
        }
        return maxLeftIdx;
    }

    private static int getClearSeqEnd(final int[] backwardScore) {
        int maxRight = 0;
        int maxRightIdx = 0;
        int goodDNACounter = 0;
        boolean endNotReached = true;

        for (int i = backwardScore.length - 1; i >= 0; i--) {
            // Allow for some errors up to 4 nucleotides within the sequence (i.e. score > 40 )
            if ((maxRight < backwardScore[i] || backwardScore[i] > 40) && endNotReached) {
                maxRight = backwardScore[i];
                maxRightIdx = i;
                goodDNACounter = 0;
            } else {
                goodDNACounter++;
                if (goodDNACounter > 50) {
                    endNotReached = false;
                }
            }
        }
        return maxRightIdx;
    }

    /**
     * 
     * SequencingOrder.calculateScore
     * 
     * @param seq
     * @param direction
     * @return
     */
    static int[] calculateScore(final StringBuffer sequence, final boolean direction) {

        final int[] score = new int[sequence.length()];

        final int length = sequence.length();
        int ambCounter = 1;
        if (direction) {
            for (int i = 0; i < length; i++) {
                final char schar = sequence.charAt(i);
                if (schar == 'N') {
                    ambCounter++;
                    score[i] = ambCounter * 10;
                } else {
                    ambCounter = 1;
                    score[i] = 1;
                }
            }
        } else {
            for (int i = length - 1; i >= 0; i--) {
                final char schar = sequence.charAt(i);
                if (schar == 'N') {
                    ambCounter++;
                    score[i] = ambCounter * 10;
                } else {
                    ambCounter = 1;
                    score[i] = 1;
                }
            }
        }
        return score;
    }

}
