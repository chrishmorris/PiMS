/**
 * 
 */
package org.pimslims.lab.sequence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.BioException;
import org.biojava.bio.proteomics.IsoelectricPointCalc;
import org.biojava.bio.proteomics.MassCalc;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;

/**
 * @author cm65
 * 
 */
public class ProteinSequence {

    /**
     * [CATG]*
     */
    private String sequence;

    private final SymbolList protein;

    private static final Pattern WHITE_SPACE = Pattern.compile("\\s");

    /**
     * @param sequence
     * @throws BioException
     * @throws IllegalAlphabetException
     */
    public ProteinSequence(final String sequence) {
        // TODO also accept 3 letter code
        final Matcher m = ProteinSequence.WHITE_SPACE.matcher(sequence);
        this.sequence = m.replaceAll("").toUpperCase();
        if (this.sequence.endsWith("*")) {
            // strip stop codon
            this.sequence = this.sequence.substring(0, this.sequence.length() - 1);
        }
        try {
            this.protein = ProteinTools.createProtein(this.sequence);
            if (!this.sequence.contains("*")) {
                MassCalc.getMolecularWeight(this.protein);
                // provoke exceptions now, if can't calculate results
                final IsoelectricPointCalc ic = new IsoelectricPointCalc();
                ic.getPI(this.protein, true, true);
            }
        } catch (final IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        } catch (final BioException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns a string consisting of the protein one letter code
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

    public float getExtinctionCoefficient() {
        final int nW = AmbiguousDnaSequence.countRegexpInString(this.sequence, "W");
        final int nY = AmbiguousDnaSequence.countRegexpInString(this.sequence, "Y");
        final int nC = AmbiguousDnaSequence.countRegexpInString(this.sequence, "C");
        // construct.setExtinction(((nW*5690)+(nY*1280))/construct.getWeight());
        return (nY * 1490.0f) + (nW * 5500.0f) + (nC * 125.0f);
        // This calculation can be found at
        // http://www.expasy.ch/tools/protparam-doc.html
    }

    public float getAbsPt1percent() {
        if (0 == this.getLength()) {
            return 0f;
        }
        final float absPt1pc = this.getExtinctionCoefficient() / this.getMass();
        return absPt1pc;
    }

    public float getMass() {

        if (0 == this.getLength()) {
            return 0f;
        }

        // TODO when biojava1.5 is released, use MassCalc.getMolecularWeight
        // instead of MassCalc.getMass
        // to resolve problems with ambiguous symbols
        try {
            // double mass = MassCalc.getMass(protein,
            // SymbolPropertyTable.AVG_MASS, true);
            final double mass = MassCalc.getMolecularWeight(this.protein);
            return (float) mass;
        } catch (final IllegalSymbolException e) {
            if (e.getMessage().contains("Symbol TER not found in alphabet PROTEIN")) {
                throw new IllegalArgumentException("Sequence contains a Stop Codon: " + this.sequence);
            }
            throw new RuntimeException(e); // should have been thrown in
            // constructor
        }

    }

    public float getPI() {

        final IsoelectricPointCalc ic = new IsoelectricPointCalc();
        try {
            return (float) ic.getPI(this.protein, true, true);
        } catch (final IllegalAlphabetException e) {
            throw new RuntimeException(e); // should have been thrown in
            // constructor
        } catch (final BioException e) {
            return -1;
        }

    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof ProteinSequence)) {
            return false;
        }
        final ProteinSequence other = (ProteinSequence) obj;
        return this.sequence.equals(other.sequence);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.sequence.hashCode() + 54379;
    }

    /**
     * @param string the name to put in the returned object
     * @return a biojava Sequence object
     */
    public Sequence getBiojavaSequence(final String name) {
        try {
            return ProteinTools.createProteinSequence(this.sequence, name);
        } catch (final IllegalSymbolException e) {
            // should have been checked in constructor
            throw new RuntimeException(e);
        }
    }

}
