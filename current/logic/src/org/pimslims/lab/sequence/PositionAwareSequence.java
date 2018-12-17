/**
 * current-pims-web org.pimslims.lab.sequence PlateSequence.java
 * 
 * @author Petr Troshin aka pvt43
 * @date 20 Nov 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 Petr Troshin
 * 
 * 
 */
package org.pimslims.lab.sequence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.pimslims.lab.Util;

/**
 * PlateSequence
 * 
 */
public class PositionAwareSequence {

    String fileName;

    /*
     * Please check that the sequence is not empty prior to making this object
     */
    String sequence;

    String position;

    public PositionAwareSequence(final String fname, final String sequence, final String position) {
        this.fileName = fname;
        assert !Util.isEmpty(sequence);
        this.sequence = sequence.trim();
        if (this.isSequenceinFasta()) {
            this.fileName = fname + " / " + this.getNameFromFasta();
            this.sequence = this.getSequenceFromFasta();
        }
        this.position = position;
        //this.sequence = removeAmbiguty();
    }

    public PositionAwareSequence(final String sequence) {
        assert !Util.isEmpty(sequence);
        this.sequence = sequence;
        //removeAmbiguty();
    }

    public boolean isValid() {
        return !Util.isEmpty(this.sequence) && !Util.isEmpty(this.fileName);
    }

    boolean isSequenceinFasta() {
        return this.sequence.replaceAll("\n", "").startsWith(">");
    }

    int getNewLinePosition() {
        return this.sequence.indexOf("\n");
    }

    String getNameFromFasta() {
        if (this.getNewLinePosition() < 0) {
            return null;
        }
        return this.sequence.substring(1, this.getNewLinePosition()).trim();
    }

    String getSequenceFromFasta() {
        if (this.getNewLinePosition() < 0) {
            return null;
        }
        return this.sequence.substring(this.getNewLinePosition()).trim();
    }

    @Override
    public String toString() {
        String seq = "File: " + this.fileName;
        seq += "\nSequence: " + this.sequence;
        seq += "\nPosition: " + this.position;
        seq += "\n";
        return seq;
    }

    public String toFile() {
        String seq = "";
        if (this.isRequiredValuesDefined()) {
            seq += ">" + this.fileName;
            seq += "\n" + this.sequence;
            seq += "\n";
        } else {
            System.err.println("File name or sequence is empty, will not output this record");
            System.err.println("File name: " + this.fileName);
            System.err.println("Sequence: " + this.sequence);
        }
        return seq;
    }

    boolean isRequiredValuesDefined() {
        return !Util.isEmpty(this.fileName) && !Util.isEmpty(this.sequence);
    }

    public String toXML() {
        String seq = "";
        if (this.isRequiredValuesDefined()) {
            seq = "<File>" + this.fileName + "</File>\n";
            seq += "<Sequence>" + this.sequence + "</Sequence>\n";
        } else {
            System.err.println("File name or sequence is empty, will not output this record");
            System.err.println("File name: " + this.fileName);
            System.err.println("Sequence: " + this.sequence);
        }
        return seq;
    }

    /* Find last NN combination at the start and end of the sequence
    * Example: given NNNANAAANNNNAAAAANNMMAMATTATTATWTTATTWTTATTWTTATTWTATTWTTATTWNTTATTWTATTWTANNATTATTANNNNN
    * after method return               MMAMATTATTATWTTATTWTTATTWTTATTWTATTWTTATTWNTTATTWTATTWTA
    * Solution is to return the longest matched subsequence.
    */
    protected String removeAmbiguity() {
        // Walk forward the sequnce
        int longestMatchIdx = 0;
        final String[] subseq = this.sequence.split("NN");
        for (int i = 1; i < subseq.length; i++) {
            if (subseq[i].length() > subseq[longestMatchIdx].length()) {
                longestMatchIdx = i;
            }
        }
        return subseq[longestMatchIdx];
    }

    /**
     * This method assumes that position of a well is encoded in file name!!!
     * 
     * @see OPPFSequenceLoader.getPosition for details.
     * @param file
     * @return OPPF specific at the moment
     * @throws IOException
     */
    public static ArrayList<PositionAwareSequence> loadFasta(final BufferedReader bfr) throws IOException {
        ArrayList<PositionAwareSequence> pss = null;
        pss = new ArrayList<PositionAwareSequence>();
        String line = null;
        String name = null;
        String sequence = null;
        while ((line = bfr.readLine()) != null) {
            if (line.length() == 0) {
                continue; // delimiter or empty string
            }
            if (line.startsWith(">")) {
                name = line.substring(1);
            } else {
                sequence = line;
                if (name != null) {
                    pss.add(new PositionAwareSequence(name, sequence, OPPFSequenceLoader.getPosition(name)));
                }
                name = null;
            }
        }
        return pss;
    }

    public static ArrayList<PositionAwareSequence> loadFastaFromFile(final File file) throws IOException {
        final BufferedReader bfr = new BufferedReader(new FileReader(file));
        return PositionAwareSequence.loadFasta(bfr);
    }

    public static ArrayList<PositionAwareSequence> loadFastaFromString(final String sequences)
        throws IOException {
        final BufferedReader bfr = new BufferedReader(new StringReader(sequences));
        return PositionAwareSequence.loadFasta(bfr);
    }
}
