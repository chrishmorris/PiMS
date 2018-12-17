package org.pimslims.lab.sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SymbolListViews;
import org.biojava.bio.symbol.TranslationTable;

/**
 * Utility bean to provide protein sequence in 3-letter code BioJava does not appear to provide this
 * functionality
 * 
 * @author Susy Griffiths YSBL
 */
public class ThreeLetterProteinSeq {

    private int resCount;

    /**
     * Zero-argument Constructor
     */
    public ThreeLetterProteinSeq() { /* empty */
    }

    /**
     * Code to convert a string a one-letter protein sequence symbols to three-letter symbols
     * 
     * @param sequence - the sequence in 1-letter codes
     * @return the sequence in 3-letter codes
     */
    public static String createThreeLetterSeq(final String sequence) {
        String tlcSequence = "";

        // Check input
        if ((sequence == null) || ("".equals(sequence))) {
            return "";
        }

        for (int i = 0; i < sequence.length(); i++) {
            final String slc = sequence.substring(i, i + 1);
            // System.out.println("Character at position " +i+ slc);
            final String tlc = ThreeLetterProteinSeq.code.get(slc);
            assert null != tlc : "Invalid protein single letter code: " + slc;
            tlcSequence = tlcSequence + tlc;
        }
        // Return the sequence
        return tlcSequence;
    }

    /**
     * Method to convert a sequence to codons
     */
    public static String convertToTriplets(final String sequence) {
        String tripletString = "";
        for (int i = 0; i < sequence.length(); i += 3) {
            final String triplet = sequence.substring(i, i + 3);
            // System.out.println("The triplet is " + triplet);
            tripletString = tripletString + " " + triplet;
        }
        //System.out.println("The new string is " + tripletString);
        return tripletString;
    }

    /**
     * Method to create a triplet sequence creates an array of strings of length 80 If its protein sequence,
     * it adds a counter in 20's If its DNA it adds a counter in 60's
     */
    public ArrayList createTripletSeq(String sequence, String molType) {
        final int seqLength = sequence.length();
        molType = molType.toLowerCase();
        if ("protein".equals(molType)) {
            this.resCount = 20;
            sequence = ThreeLetterProteinSeq.createThreeLetterSeq(sequence);
        }
        if ("dna".equals(molType)) {
            this.resCount = 60;
        }
        final String tripletString = ThreeLetterProteinSeq.convertToTriplets(sequence);
        String newString = "";
        final ArrayList<String> seqArray = new ArrayList<String>();
        String seqRow = "";
        String lastRow = "";
        int rowNum = 0;
        int resNum = 0;

        for (int i = 0; i < tripletString.length(); i++) {
            newString = newString + tripletString.substring(i, i + 1);
            resNum++;
            if (resNum % 80 == 0) {
                seqRow = newString + "  " + (this.resCount * (rowNum + 1));
                //System.out.println("The row number " + rowNum + " is " + seqRow);
                seqArray.add(seqRow);
                rowNum++;
            }
        }
        final int rem = tripletString.length() % 80;
        lastRow = tripletString.substring(tripletString.length() - rem, tripletString.length());
        lastRow = lastRow + "  " + (seqLength);
        //System.out.println("LastRow is : " + lastRow);
        seqArray.add(lastRow);
        return seqArray;
    }

    public static String translate(final String theDNA) {
        if (null == theDNA) {
            return null;
        }
        if (0 != theDNA.length() % 3) {
            throw new IllegalArgumentException("Sequence is not a whole number of codons: <" + theDNA + ">");
        }
        String theProtein = null;
        try {
            // JvN Problem: The DNA sequence might contain stop codons which
            // will not be in Protein for biological reason
            // Solution - Don't use the protein sequence of the target for
            // primer design
            // Rather translate the DNA sequence when giving user choice of
            // protein start and stop positions
            // Translate the DNA sequence into protein
            final TranslationTable eup = RNATools.getGeneticCode(TranslationTable.UNIVERSAL);
            assert null != theDNA;
            SymbolList seq = DNATools.createDNA(theDNA);
            // SymbolList seq = DNATools.createDNA("agtgtgactggttatcta");

            // transcribe to RNA
            seq = DNATools.toRNA(seq);

            // veiw the RNA sequence as codons, this is done internally by
            // RNATool.translate()
            seq = SymbolListViews.windowedSymbolList(seq, 3);

            // translate
            final SymbolList protein = SymbolListViews.translate(seq, eup);

            // TargetProtSeq=spotTarget.getProtSeq();
            theProtein = protein.seqString();
        } catch (final IllegalSymbolException ex) {
            throw new IllegalArgumentException("Protein sequence is not all IUB symbols", ex);
        } catch (final IllegalAlphabetException ex) {
            throw new RuntimeException("Invalid DNA sequence", ex);
        }
        return theProtein;
    }

    /**
     * Method to create an array of alternating strings of DNA and protein sequence 'triplets' Creates an
     * array of strings of length 80
     */
    public static ArrayList make2SeqArray(final String dnaSeq, final String protSeq) {
        final String codonString = ThreeLetterProteinSeq.convertToTriplets(dnaSeq);
        final String tlcString =
            ThreeLetterProteinSeq.convertToTriplets(ThreeLetterProteinSeq.createThreeLetterSeq(protSeq));
        String newString = "";
        String newString2 = "";
        final ArrayList<String> seqArray = new ArrayList<String>();
        String lastDNARow = "";
        String lastProtRow = "";
        int resNum = 0;

        for (int i = 0; i < codonString.length(); i++) {
            newString = newString + codonString.substring(i, i + 1);
            newString2 = newString2 + tlcString.substring(i, i + 1);
            resNum++;
            if (resNum % 100 == 0) {
                seqArray.add(newString);
                seqArray.add(newString2);
                resNum = 0;
                newString = "";
                newString2 = "";
            }
        }
        final int rem = codonString.length() % 100;
        final int remProt = tlcString.length() % 100;
        lastDNARow = codonString.substring(codonString.length() - rem, codonString.length());
        lastProtRow = tlcString.substring(tlcString.length() - remProt, tlcString.length());
        //System.out.println("Last DNA Row is : " + lastDNARow);
        seqArray.add(lastDNARow);
        //System.out.println("Last Protein Row is : " + lastProtRow);
        seqArray.add(lastProtRow);
        return seqArray;
    }

    /**
     * Method to create an array of alternating strings of DNA and its tarnslated protein sequence 'triplets'
     * Creates an array of strings of length 80 = 25 aa or triplets per row
     */
    public static ArrayList dnaAndProtArray(final String dnaSeq) {
        final String protSeq = ThreeLetterProteinSeq.translate(dnaSeq);
        return ThreeLetterProteinSeq.make2SeqArray(dnaSeq, protSeq);
    }

    // modified from MoleculeFactory.java
    public static java.util.Map<String, String> code = new HashMap<String, String>();
    static {
        ThreeLetterProteinSeq.code.put("A", "Ala");
        ThreeLetterProteinSeq.code.put("C", "Cys");
        ThreeLetterProteinSeq.code.put("D", "Asp");
        ThreeLetterProteinSeq.code.put("E", "Glu");
        ThreeLetterProteinSeq.code.put("F", "Phe");
        ThreeLetterProteinSeq.code.put("G", "Gly");
        ThreeLetterProteinSeq.code.put("H", "His");
        ThreeLetterProteinSeq.code.put("I", "Ile");
        ThreeLetterProteinSeq.code.put("K", "Lys");
        ThreeLetterProteinSeq.code.put("L", "Leu");
        ThreeLetterProteinSeq.code.put("M", "Met");
        ThreeLetterProteinSeq.code.put("N", "Asn");
        ThreeLetterProteinSeq.code.put("P", "Pro");
        ThreeLetterProteinSeq.code.put("Q", "Gln");
        ThreeLetterProteinSeq.code.put("R", "Arg");
        ThreeLetterProteinSeq.code.put("S", "Ser");
        ThreeLetterProteinSeq.code.put("T", "Thr");
        ThreeLetterProteinSeq.code.put("V", "Val");
        ThreeLetterProteinSeq.code.put("W", "Trp");
        ThreeLetterProteinSeq.code.put("Y", "Tyr");
        // these may not be used with BioJava 1.4
        ThreeLetterProteinSeq.code.put("*", "Ter");
        ThreeLetterProteinSeq.code.put("U", "Sec"); // selenocys
        ThreeLetterProteinSeq.code.put("X", "Xaa");
        ThreeLetterProteinSeq.code.put("B", "Asx"); // Asn or Asp
        ThreeLetterProteinSeq.code.put("Z", "Glx"); // Glu or Gln
        ThreeLetterProteinSeq.code.put("J", "Xle"); // Leu or Ile
    }

    public static Boolean compareSeqs(final String protSeq, final String dnaSeq) {
        if (null == protSeq || null == dnaSeq) {
            return true;
        }
        if ("" == protSeq || "" == dnaSeq) {
            return true;
        }
        if (0 != dnaSeq.length() % 3) {
            return false; // not a whole number of codons
        }
        Boolean matchingSeqs = false;
        String protein = protSeq;
        //System.out.println("Protein:" + protSeq);
        String translatedSeq = ThreeLetterProteinSeq.translate(dnaSeq);
        //System.out.println("Translation:" + translatedSeq);
        //Need to remove stop codons at end of sequence from the comparison
        final Pattern stopCodes = Pattern.compile("\\*{1,}$");
        final Matcher matchStop = stopCodes.matcher(translatedSeq);
        final Matcher matchStop2 = stopCodes.matcher(protein);
        if (matchStop.find()) {
            //System.out.println("Translated sequence Ends with stop codons");
            translatedSeq = matchStop.replaceFirst("");
            //System.out.println("Replaced Translation:" + translatedSeq);

        }
        if (matchStop2.find()) {
            //System.out.println("Protein sequence Ends with stop codons");
            protein = matchStop2.replaceFirst("");
            //System.out.println("Replaced Protein:" + protein);
        }

        if (translatedSeq.equals(protein)) {
            matchingSeqs = true;
        } /* else {
                                                                                                              for (int i = 0; i < protein.length(); i++) {
                                                                                                                  if (protein.charAt(i) == translatedSeq.charAt(i)) {
                                                                                                                      continue;
                                                                                                                  }
                                                                                                                  System.out.println("Residue: " + (i + 1) + " Protein: " + protein.charAt(i) + " translated: "
                                                                                                                      + translatedSeq.charAt(i) + " DNA: " + dnaSeq.substring(i * 3, i * 3 + 3));
                                                                                                                  break;
                                                                                                              }
                                                                                                          } */
        return matchingSeqs;
    }

    public static int lengthWithoutTer(final String dnaSeq) {
        int seqLen = 0;
        //final String seq = dnaSeq;
        String transSeq = ThreeLetterProteinSeq.translate(dnaSeq);
        final Pattern stopCodes = Pattern.compile("\\*{1,}$");
        final Matcher matchStop = stopCodes.matcher(transSeq);
        if (matchStop.find()) {
            //System.out.println("Translated sequence Ends with stop codons, Length is " + transSeq.length());
            transSeq = matchStop.replaceFirst("");
            seqLen = transSeq.length();
            //System.out.println("Replaced Translation:" + transSeq.length());
        }
        seqLen = transSeq.length();
        //System.out.println("Final sequence length:" + seqLen);

        return seqLen;
    }
}
