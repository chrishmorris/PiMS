/**
 * V2_3-pims-web org.pimslims.lab.sequence AmbiguousDnaSequence.java
 * 
 * @author cm65
 * @date 11 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab.sequence;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.IllegalSymbolException;

/**
 * AmbiguousDnaSequence
 * 
 */
public class AmbiguousDnaSequence {
    //http://www.mun.ca/biochem/courses/3107/symbols.html
    //allow ACGT plus URYMKSWBDHVN
    private static final Pattern AMDNA_CODE = Pattern.compile("^[CATGURYMKSWBDHVN]*$");

    protected enum NucType {
        G, C, A, T, U, R, Y, M, K, S, W, B, D, H, V, N
    }

    public static boolean isDnaSequence(final String sequence) {

        final Matcher m2 = AmbiguousDnaSequence.AMDNA_CODE.matcher(sequence.toUpperCase());
        return m2.matches();
    }

    protected static final Collection<String> stopCodes = new HashSet<String>();
    static {
        AmbiguousDnaSequence.stopCodes.add("TAA");
        AmbiguousDnaSequence.stopCodes.add("TAG");
        AmbiguousDnaSequence.stopCodes.add("TGA");
    }

    private static final Pattern WHITE_SPACE = Pattern.compile("\\s");

    /**
     * [CATG]*
     */
    protected String sequence;

    protected AmbiguousDnaSequence() {
        // for subclasses only
    }

    public AmbiguousDnaSequence(final String sequence) {
        this.sequence = AmbiguousDnaSequence.cleanSequence(sequence);
        if (!AmbiguousDnaSequence.isDnaSequence(this.sequence)) {
            System.err.println("sequence is not DNA sequence:" + sequence);
            throw new IllegalArgumentException("Not a DNA sequence: " + sequence);
        }
    }

    protected static String cleanSequence(final String sequence) {
        final Matcher m = AmbiguousDnaSequence.WHITE_SPACE.matcher(sequence);
        final String s = m.replaceAll("").toUpperCase();
        return s;
    }

    public static int countRegexpInString(final String theString, final String theRegExp) {
        final Pattern p = Pattern.compile(theRegExp);
        final Matcher m = p.matcher(theString);
        int cnt = 0;
        while (m.find()) {
            cnt++;
        }
        return cnt;
    }

    /**
     * Returns a string consisting of C, A, T, G.
     * 
     * @return Returns the sequence.
     */
    public String getSequence() {
        return this.sequence;
    }

    public String getFastA(final String description) {
        String ret = ">" + description + "\n";
        for (int i = 0; i < this.sequence.length(); i = i + 79) {
            final int end = Math.min(i + 79, this.sequence.length());
            ret = ret + this.sequence.substring(i, end) + "\n";
        }
        return ret;
    }

    public int getLength() {
        return this.sequence.length();
    }

    /**
     * @param string the name to put in the returned object
     * @return a biojava Sequence object
     */
    public Sequence getBiojavaSequence(final String name) {
        try {
            return DNATools.createDNASequence(this.sequence, name);
        } catch (final IllegalSymbolException e) {
            // should have been checked in constructor
            throw new RuntimeException(e);
        }
    }

    public String getSequenceforHTML() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.sequence.length(); i += 60) {
            int j = i + 60;
            if (j > this.sequence.length()) {
                j = this.sequence.length();
            }
            sb.append(this.sequence.substring(i, j) + "<br />");
        }
        return sb.toString();
    }

}