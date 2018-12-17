/**
 * pims-web org.pimslims.presentation CodonBean.java
 * 
 * @author Marc Savitsky
 * @date 25 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.primer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.biojava.bio.dist.Distribution;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.CodonPrefTools;
import org.biojava.bio.symbol.FiniteAlphabet;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.biojava.bio.symbol.ManyToOneTranslationTable;
import org.biojava.bio.symbol.ReversibleTranslationTable;
import org.biojava.bio.symbol.SimpleAtomicSymbol;
import org.biojava.bio.symbol.SimpleCodonPref;
import org.biojava.bio.symbol.Symbol;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SymbolListViews;
import org.biojava.bio.symbol.TranslationTable;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ThreeLetterProteinSeq;

/**
 * CodonBean
 */
public class CodonBean {

    private final SymbolList seq;

    private final SymbolList aminoAcid;

    private final String translationTable = TranslationTable.UNIVERSAL;

    private static final String allCodons = "AAAAACAAGAAT" + "ACAACCACGACT" + "AGAAGCAGGAGT" + "ATAATCATGATT"
        + "CAACACCAGCAT" + "CCACCCCCGCCT" + "CGACGCCGGCGT" + "CTACTCCTGCTT" + "GAAGACGAGGAT" + "GCAGCCGCGGCT"
        + "GGAGGCGGGGGT" + "GTAGTCGTGGTT" + "TAATACTAGTAT" + "TCATCCTCGTCT" + "TGATGCTGGTGT" + "TTATTCTTGTTT";

    private static java.util.Map<String, String> AAAbbreviations = new HashMap<String, String>();
    static {
        CodonBean.AAAbbreviations.put("F", "Phe"); //Phenylalanine
        CodonBean.AAAbbreviations.put("G", "Gly"); //Glycine
        CodonBean.AAAbbreviations.put("A", "Ala"); //Alanine
        CodonBean.AAAbbreviations.put("V", "Val"); //Valine
        CodonBean.AAAbbreviations.put("L", "Leu"); //Leucine
        CodonBean.AAAbbreviations.put("I", "Ile"); //Isoleucine
        CodonBean.AAAbbreviations.put("M", "Met"); //Methionine
        CodonBean.AAAbbreviations.put("W", "Trp"); //Trptophane
        CodonBean.AAAbbreviations.put("P", "Pro"); //Proline
        CodonBean.AAAbbreviations.put("S", "Ser"); //Serine
        CodonBean.AAAbbreviations.put("T", "Thr"); //Threonine
        CodonBean.AAAbbreviations.put("C", "Cys"); //Cysteine
        CodonBean.AAAbbreviations.put("Y", "Tyr"); //Tyrosine
        CodonBean.AAAbbreviations.put("N", "Asn"); //Asparagine
        CodonBean.AAAbbreviations.put("Q", "Gln"); //Glutamine
        CodonBean.AAAbbreviations.put("D", "Asp"); //Aspartic Acid
        CodonBean.AAAbbreviations.put("E", "Glu"); //Glutamic Acid
        CodonBean.AAAbbreviations.put("K", "Lys"); //Lysine
        CodonBean.AAAbbreviations.put("R", "Arg"); //Arginine
        CodonBean.AAAbbreviations.put("H", "His"); //Histidine
        CodonBean.AAAbbreviations.put("*", "Ter"); // stop codon
    }

/*
    private static java.util.Map<String, String> AACodons = new HashMap<String, String>();
    static {
        CodonBean.AACodons.put("Phe", "TTT"); //Phenylalanine
        CodonBean.AACodons.put("Gly", "GGC"); //Glycine
        CodonBean.AACodons.put("Ala", "GCG"); //Alanine
        CodonBean.AACodons.put("Val", "GTG"); //Valine
        CodonBean.AACodons.put("Leu", "CTG"); //Leucine
        CodonBean.AACodons.put("Ile", "ATT"); //Isoleucine
        CodonBean.AACodons.put("Met", "ATG"); //Methionine
        CodonBean.AACodons.put("Trp", "TGG"); //Trptophane
        CodonBean.AACodons.put("Pro", "CCG"); //Proline
        CodonBean.AACodons.put("Ser", "AGC"); //Serine
        CodonBean.AACodons.put("Thr", "ACC"); //Threonine
        CodonBean.AACodons.put("Cys", "TGC"); //Cysteine
        CodonBean.AACodons.put("Tyr", "TAT"); //Tyrosine
        CodonBean.AACodons.put("Asn", "AAC"); //Asparagine
        CodonBean.AACodons.put("Gln", "CAA"); //Glutamine
        CodonBean.AACodons.put("Asp", "GAT"); //Aspartic Acid
        CodonBean.AACodons.put("Glu", "GAA"); //Glutamic Acid
        CodonBean.AACodons.put("Lys", "AAA"); //Lysine
        CodonBean.AACodons.put("Arg", "CGC"); //Arginine
        CodonBean.AACodons.put("His", "CAT"); //Histidine
    } */

    private static java.util.Map<String, String> Nucleotides = new HashMap<String, String>();
    static {
        CodonBean.Nucleotides.put("uracil", "T");
        CodonBean.Nucleotides.put("thymine", "T");
        CodonBean.Nucleotides.put("adenine", "A");
        CodonBean.Nucleotides.put("cytosine", "C");
        CodonBean.Nucleotides.put("guanine", "G");
    }

    public CodonBean(final String triplet) {
        try {
            this.seq = DNATools.createDNA(triplet);
            final TranslationTable eup = RNATools.getGeneticCode(this.translationTable);
            SymbolList sym = DNATools.toRNA(this.seq);
            sym = SymbolListViews.windowedSymbolList(sym, 3);
            this.aminoAcid = SymbolListViews.translate(sym, eup);
        } catch (final IllegalSymbolException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAlphabetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean equals(final Object object) {
        return (object instanceof CodonBean)
            && this.seq.seqString().equals(((CodonBean) object).seq.seqString());
    }

    @Override
    public int hashCode() {
        return this.seq.hashCode();
    }

    public int compareTriplet(final CodonBean bean) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (this.getTriplet().charAt(i) != bean.getTriplet().charAt(i)) {
                count++;
            }
        }
        return count;
    }

    static final Comparator<CodonBean> THREE_LETTER_SEQUENCE = new Comparator<CodonBean>() {
        public int compare(final CodonBean c1, final CodonBean c2) {

            return c1.getAminoAcid().compareTo(c2.getAminoAcid());

        }
    };

    public int firstDiff(final CodonBean bean) {
        //System.out.println("diff [" + this.getTriplet() + ":" + bean.getTriplet() + "]");
        for (int i = 0; i < 3; i++) {
            if (this.getTriplet().charAt(i) != bean.getTriplet().charAt(i)) {
                return i + 1;
            }
        }
        return 0;
    }

    public int lastDiff(final CodonBean bean) {
        //System.out.println("diff [" + this.getTriplet() + ":" + bean.getTriplet() + "]");
        for (int i = 2; i >= 0; i--) {
            if (this.getTriplet().charAt(i) != bean.getTriplet().charAt(i)) {
                return i + 1;
            }
        }
        return 0;
    }

    public String getTriplet() {
        return this.seq.seqString().toUpperCase();
    }

    public String getAminoAcid() {
        return ThreeLetterProteinSeq.code.get(this.aminoAcid.seqString());
    }

    public String getAminoAcid1Letter() throws IllegalAlphabetException {
        return this.aminoAcid.seqString();
    }

    /**
     * 
     * CodonBean.getTranslationTable
     * 
     * @return
     */
    public static Collection<CodonBean> getTranslationTable() {

        final List<CodonBean> codonList = new ArrayList<CodonBean>();

        for (int i = 0; i < CodonBean.allCodons.length(); i += 3) {
            final String triplet = CodonBean.allCodons.substring(i, i + 3);
            final CodonBean bean = new CodonBean(triplet);
            codonList.add(bean);
        }

        return codonList;
    }

    private static final Collection<String> EXPRESSION_ORGANISMS = new ArrayList<String>();

    public static final String DEFAULT_EXPRESSION_ORGANISM = CodonPrefTools.ECOLI;
    static {
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.CEREVISIAE_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.DROSOPHILA_MELANOGASTER_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.ECOLI);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.FUGU_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.MAN_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.MOUSE_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.POMBE_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.RAT_NUCLEAR);
        CodonBean.EXPRESSION_ORGANISMS.add(CodonPrefTools.WORM_NUCLEAR);
    }

    public static final Collection<String> getExpressionOrganisms() {
        return Collections.unmodifiableCollection(CodonBean.EXPRESSION_ORGANISMS);
    }

    /**
     * 
     * CodonBean.getBestPreferrerCodonTable
     * 
     * @return
     * @throws IllegalAlphabetException
     */
    public static Collection<CodonBean> getPreferredCodonTable(final String organism) {

        final List<CodonBean> codonList = new ArrayList<CodonBean>();
        final SimpleCodonPref codonPref = (SimpleCodonPref) CodonPrefTools.getCodonPreference(organism);

        final Distribution distribution = codonPref.getFrequency();
        final ManyToOneTranslationTable table = codonPref.getGeneticCode();
        final ReversibleTranslationTable rTable = RNATools.transcriptionTable();

        final FiniteAlphabet alphabet = ProteinTools.getAlphabet();
        final Iterator iter = alphabet.iterator();
        while (iter.hasNext()) {
            final Symbol aa = (Symbol) iter.next();

            Symbol symbol = null;
            double d = 0;
            try {
                final Collection<Symbol> set = table.untranslate(aa);
                for (final Symbol sym : set) {
                    if (distribution.getWeight(sym) > d) {
                        d = distribution.getWeight(sym);
                        symbol = sym;
                    }
                }

                final SimpleAtomicSymbol saSymbol = (SimpleAtomicSymbol) symbol;
                final StringBuffer triplet = new StringBuffer();
                for (final Object object : saSymbol.getSymbols()) {
                    final Symbol dna = rTable.untranslate((Symbol) object);
                    triplet.append(CodonBean.Nucleotides.get(dna.getName()));

                }

                codonList.add(new CodonBean(triplet.toString()));

            } catch (final IllegalSymbolException e) {
                System.out.println("IllegalSymbolException caught amino acid [" + aa.getName() + "]");
            }

        }

        return codonList;
    }

    public static CodonBean getCodonBean(final String aa, final Collection<CodonBean> list)
        throws IllegalAlphabetException {
        for (final CodonBean bean : list) {
            if (bean.getAminoAcid().equals(aa)) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 
     * CodonBean.getCodonBeans
     * 
     * @param sequence
     * @return
     * @throws IllegalAlphabetException
     * @throws IllegalSymbolException
     */
    public static Collection<CodonBean> getCodonBeans(final DnaSequence sequence) {
        assert 0 == sequence.getLength() % 3 : "Sequence must be a whole number of codons";
        final List<CodonBean> codonList = new ArrayList<CodonBean>();
        for (int i = 0; i < sequence.getSequence().length(); i += 3) {
            final String triplet = sequence.getSequence().substring(i, i + 3);
            final CodonBean bean = new CodonBean(triplet);
            codonList.add(bean);
        }
        return codonList;
    }

    @Override
    public String toString() {

        return this.getTriplet() + " " + this.getAminoAcid();

    }
}
