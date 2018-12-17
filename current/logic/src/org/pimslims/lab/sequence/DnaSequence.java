/**
 * 
 */
package org.pimslims.lab.sequence;

import java.util.List;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;

/**
 * @author cm65
 * 
 */
public class DnaSequence extends AmbiguousDnaSequence {

    private final SymbolList symbolList;

    /**
     * @param sequence
     */
    public DnaSequence(final String sequence) {
        super(sequence);

        try {
            this.symbolList = DNATools.createDNA(this.sequence);
        } catch (final IllegalSymbolException e) {
            throw new IllegalArgumentException("Not a DNA sequence: " + sequence, e);
        }
    }

    /**
     * Constructor for DnaSequence
     * 
     * @param sl org.biojava.bio.symbol.SymbolList
     */
    public DnaSequence(final SymbolList sl) {
        super(sl.seqString());
        this.symbolList = sl;
    }

    /**
     * e.g.http://www.ambion.com/techlib/append/na_mw_tables.html M.W. = (An x 313.2) + (Tn x 304.2) + (Cn x
     * 289.2) + (Gn x 329.2) + 79.0
     * 
     * DnaSequence.getMass
     * 
     * @return
     */
    public float getMass() {
        //throw new UnsupportedOperationException("Not implemented yet");

        return (float) ((AmbiguousDnaSequence.countRegexpInString(this.sequence, "A") * 313.2)
            + (AmbiguousDnaSequence.countRegexpInString(this.sequence, "T") * 304.2)
            + (AmbiguousDnaSequence.countRegexpInString(this.sequence, "C") * 289.2) + (AmbiguousDnaSequence
            .countRegexpInString(this.sequence, "G") * 329.2));
    }

    /**
     * @return the number of bases which are G or C
     */
    public float getNGC() {
        final float nGC = AmbiguousDnaSequence.countRegexpInString(this.sequence, "G|C");
        return nGC;
    }

    /**
     * @return GC percentage
     */
    public float getGCContent() {
        if (0 == this.sequence.length()) {
            return 0f;
        }
        return this.getNGC() * 100 / this.sequence.length(); // Tm calculation uses nGC not

    }

    public float getTm() {
        final int length = this.sequence.length();
        if (0 == length) {
            return 0.0f;
        }

        final float nGC = this.getNGC();
        // return new Float((64.9+(41*(nGC-16.4)/theSeq.length())));
        // Float tm=new Float(69.3 + 41 * (nGC)/theSeq.length() -
        // 650/theSeq.length()); //Susy's
        return 69.3f + 41f * (nGC) / length - 650f / length; // Susy's

        /*
         * JvN 25 November 2005: Up to version 3.9 of SPoT had a bug in the primer Tm calculation procedure.
         * The following formula was used: ----- return new Float((64.9+((41*nGC)-16.4)/theSeq.length()));
         * ----- According to the website http://www.promega.com/biomath/calc11.htm#melt_results, the way to
         * do it is: Theoretical Tm of Oligos There are several formulas for calculating melting temperatures
         * (Tm). In all cases these calculations will give you a good starting point for determining
         * appropriate annealing temperatures for PCR, RT-PCR, hybridization and primer extension procedures.
         * However, a precise optimum annealing temperature must be determined empirically. Basic Tm
         * Calculations The simplest formula is as follows (1): Tm = 4°C x (number of Gs and Cs in the primer) +
         * 2°C x (number of As and Ts in the primer) This formula is valid for oligos <14 bases and assumes
         * that the reaction is carried out in the presence of 50mM monovalent cations. For longer oligos, the
         * formula below is used: Tm = 64.9°C + 41°C x (number of Gs and Cs in the primer - 16.4)/N Where N is
         * the length of the primer. For example, Promega�s T7 Promoter Primer (TAATACGACTCACTATAGGG) is a
         * 20mer composed of 5 Ts, 7 As, 4CsC�s, and 4 Gs. Thus, its melting temperature is calculated: 64.9°C +
         * 41°C x (8 - 16.4)/20 = 47.7°C
         */

    }

    public float getSDMTm(final int mutationCount) {
        final int length = this.sequence.length();
        if (0 == length) {
            return 0.0f;
        }
        final float nGC = this.getNGC();
        final float misMatch = mutationCount * 100 / length;
        final float f = nGC * 100 / length;
        final int percentGC = (int) (f + 0.5f);

        //System.out.println("misMatch = [" + mutationCount + " * 100 / " + length + "]");
        //System.out.println("Tm = [81.5 - " + misMatch + " + " + (0.41f * percentGC) + " - " + 675f / length
        //    + "]");

        return 81.5f - misMatch + (0.41f * percentGC) - 675f / length;
    }

    public boolean getIsStopCodon() {
        if (this.sequence == null) {
            return false;
        }
        // when this sequence end with any stop code, return true
        for (final String stopCode : AmbiguousDnaSequence.stopCodes) {
            if (this.sequence.endsWith(stopCode)) {
                return true;
            }
        }
        // else
        return false;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DnaSequence)) {
            return false;
        }
        final DnaSequence other = (DnaSequence) obj;
        return this.sequence.equals(other.sequence);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return this.sequence.hashCode() + 127;
    }

    /**
     * DnaSequence.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DNA Sequence: " + this.sequence;
    }

    /**
     * @return java.util.List of strings of formatted sequence
     */
    public List<String> chunkSeq() {
        final List<String> seqInChunks = SequenceUtility.chunkSeq(this.sequence);
        return seqInChunks;
    }

    int calcBasesNumber(final NucType nuc) {
        return AmbiguousDnaSequence.countRegexpInString(this.sequence, nuc.toString());
    }

    /**
     * The two standard approximation calculations are used. For sequences less than 14 nucleotides the
     * formula is
     * 
     * Tm= (wA+xT) * 2 + (yG+zC) * 4
     * 
     * where w,x,y,z are the number of the bases A,T,G,C in the sequence, respectively (from Marmur,J., and
     * Doty,P. (1962) J Mol Biol 5:109-118 [PubMed]).
     * 
     * For sequences longer than 13 nucleotides, the equation used is
     * 
     * Tm= 64.9 +41*(yG+zC-16.4)/(wA+xT+yG+zC)
     * 
     * See Wallace,R.B., Shaffer,J., Murphy,R.F., Bonner,J., Hirose,T., and Itakura,K. (1979) Nucleic Acids
     * Res 6:3543-3557 (Abstract) and Sambrook,J., and Russell,D.W. (2001) Molecular Cloning: A Laboratory
     * Manual. Cold Spring Harbor Laboratory Press; Cold Spring Harbor, NY. (CHSL Press) ASSUMPTIONS: Both
     * equations assume that the annealing occurs under the standard conditions of 50 nM primer, 50 mM Na+,
     * and pH 7.0.
     */
    /**
     * Method for Tm calculation for primers less than 14 nucleotides
     * 
     * Tm= (wA+xT) * 2 + (yG+zC) * 4
     * 
     * @return double Primer Tm
     */
    float calculateMarmurTm() {
        final int nA = this.calcBasesNumber(NucType.A);
        final int nT = this.calcBasesNumber(NucType.T);
        return (nA + nT) * 2 + this.getNGC() * 4;
    }

    /**
     * Method for Tm calculation for primers longer than 13 nucleotides
     * 
     * Tm= 64.9 +41*(yG+zC-16.4)/(wA+xT+yG+zC)
     * 
     * @return double Primer Tm
     */
    float calculateWallaceTm() {
        return 64.9f + 41 * (this.getNGC() - 16.4f) / this.sequence.length();
    }

    public float getPrimerTm() {
        float tm = 0;
        if (this.getSequence().length() > 13) {
            tm = this.calculateWallaceTm();
        } else {
            tm = this.calculateMarmurTm();
        }
        return tm;
    }

    /**
     * DnaSequence.getReverseComplement
     * 
     * @return
     */
    public DnaSequence getReverseComplement() {
        try {
            return new DnaSequence(DNATools.reverseComplement(this.symbolList));
        } catch (final IllegalAlphabetException e) {
            throw new RuntimeException(e);
        }
    }

}
