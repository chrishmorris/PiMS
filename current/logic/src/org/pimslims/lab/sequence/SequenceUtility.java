/**
 * current-pims-web org.pimslims.lab.sequence SequenceUtility.java
 * 
 * @author Susy Griffiths YSBL
 * @date 02-Oct-2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 susy The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pimslims.bioinf.local.PimsAlignment;
import org.pimslims.bioinf.local.SWSearch;
import org.pimslims.dao.ReadableVersion;

/**
 * SequenceUtility methods for use with both DNA and Protein sequences
 * 
 */
public class SequenceUtility {

    /**
     * Constructor for SequenceUtility Zero-argument Constructor
     */
    public SequenceUtility() {
        super();
        /* empty */
    }

    /**
     * SequenceUtility.chunkSeq to create a List of strings formatted in chunks of up to 10 blocks of 10
     * characters
     * 
     * @param seq java.lang.String representing the sequence to be processed
     * @return java.util.List of strings of sequence in chunks of
     */
    public static List<String> chunkSeq(final String seq) {
        if (null == seq) {
            return Collections.EMPTY_LIST;
        }
        final List<String> seqInChunks = new ArrayList<String>();
        String sequence = seq;
        //240610 version4.1 fix for sequences with <=3 terminal stop codons
        //probably a better way to do this but OK for now
        if (sequence.endsWith("***")) {
            sequence = sequence.substring(0, sequence.lastIndexOf("***"));
        } else if (sequence.endsWith("**")) {
            sequence = sequence.substring(0, sequence.lastIndexOf("**"));
        } else if (sequence.endsWith("*")) {
            sequence = sequence.substring(0, sequence.lastIndexOf("*"));
        }
        int cols = 0;
        //int cntr = 0;
        String chunk = "";
        String lastChunk = "";
        final int TEN = 10;
        final int HUNDRED = 100;
        for (int i = 0; i < sequence.length(); i++) {
            chunk = chunk + sequence.substring(i, i + 1);
            cols++;
            if (cols % TEN == 0) {
                chunk = chunk + " ";
            }
            if (cols == HUNDRED) {
                //cntr++;
                seqInChunks.add(chunk);
                cols = 0;
                chunk = "";
            }
        }
        lastChunk = chunk;
        seqInChunks.add(lastChunk);
        return seqInChunks;
    }

    /*
     * SequenceUtility.compProtSeqs to create a Formatted PiMSAlignment between 2 protein sequences
     * The Target Protein sequence and the translated DNA sequence
     */
    public static String[] compProtSeqs(final String protSeqName, final String protSeq, final String dnaSeq,
        final ReadableVersion version) {
        String tpSeq = "";
        String transSeq = "";
        String[] aliSeqs = null;
        if (null != protSeq && protSeq != "") {
            tpSeq = protSeq;
        }
        if (null != dnaSeq && dnaSeq != "") {
            transSeq = ThreeLetterProteinSeq.translate(dnaSeq);
        }
        SWSearch search = null;
        if (tpSeq != "" && transSeq != "") {
            search = new SWSearch(protSeqName, transSeq, "BLOSUM62", 10, 1);
            final PimsAlignment alignment = search.align("Translation", tpSeq, false, version, null, false);
            aliSeqs = alignment.getFormatted();
        }
        return aliSeqs;
    }
}
