package org.pimslims.lab.sequence;

import java.util.Collections;
import java.util.HashMap;

import org.pimslims.constraint.Constraint;
import org.pimslims.metamodel.ModelObject;

/**
 * A constraint for the seqString of a moleculte
 * 
 * @author cm65
 * 
 */
public class SequenceConstraint implements Constraint {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public SequenceConstraint() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.constraint.Constraint#verify(java.lang.String, java.lang.Object,
     *      org.pimslims.metamodel.ModelObject)
     */
    public boolean verify(String name, Object value, ModelObject object) {
        if (null == value) {
            return true;
        }
        String sequence = ((String) value).toUpperCase();

        return verifyAminoAcidSequence(sequence) || verifyNucleicAcidSequence(sequence);
        /*
         * if (object==null || null==object.getValue("molType") //LATER remove this - at present molType is
         * not in use ) { // can't tell MolType return verifyAminoAcidSequence( sequence ) ||
         * verifyNucleicAcidSequence( sequence ); } String molType = (String)object.getValue("molType"); if
         * (null==molType) {return false;} if ("protein".equals(molType)) { return verifyAminoAcidSequence(
         * sequence ); } else if (-1!=molType.indexOf("NA")) { // DNA or RNA return verifyNucleicAcidSequence(
         * sequence ); } // "otherpolymer" or "nonpolymer" type return false; //
         */
    }

    public static boolean verifyAminoAcidSequence(String sequence) {
        for (int i = 0; i < sequence.length(); i++) {
            String code1Letter = sequence.substring(i, i + 1);
            if (!THREE_LETTER_CODE.containsKey(code1Letter)) {
                return false;
            }
        }
        return true; // that's fine
    }

    private static final String NUCLEIC_ACID_CODE = "CATG";

    public static boolean verifyNucleicAcidSequence(String sequence) {
        for (int i = 0; i < sequence.length(); i++) {
            String code1Letter = sequence.substring(i, i + 1);
            if (-1 == NUCLEIC_ACID_CODE.indexOf(code1Letter)) {
                return false;
            }
        }
        return true; // that's fine
    }

    private static java.util.Map<String, String> code = new HashMap<String, String>();
    static {
        code.put("A", "ALA");
        code.put("C", "CYS");
        code.put("D", "ASP");
        code.put("E", "GLU");
        code.put("F", "PHE");
        code.put("G", "GLY");
        code.put("H", "HIS");
        code.put("I", "ILE");
        code.put("K", "LYS");
        code.put("L", "LEU");
        code.put("M", "MET");
        code.put("N", "ASN");
        code.put("P", "PRO");
        code.put("Q", "GLN");
        code.put("R", "ARG");
        code.put("S", "SER");
        code.put("T", "THR");
        code.put("V", "VAL");
        code.put("W", "TRP");
        code.put("Y", "TYR");
    }

    /**
     * Map single letter code to three letter code for amino acids
     */
    public static final java.util.Map<String, String> THREE_LETTER_CODE = Collections.unmodifiableMap(code);

    static {
        code = new HashMap<String, String>();
        code.put("A", "A");
        code.put("C", "C");
        code.put("G", "G");
        code.put("T", "T");
    }

    /**
     * Code for DNA
     */
    public static final java.util.Map<String, String> DNA_CODE = Collections.unmodifiableMap(code);

}
