/**
 * pims-web org.pimslims.lab.primer SDMPrimerDesignerTest.java
 * 
 * @author Marc Savitsky
 * @date 17 Jun 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab;

import java.util.List;

import junit.framework.Assert;

import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.pimslims.lab.primer.PimsSDMPrimerDesigner;
import org.pimslims.test.AbstractTestCase;

/**
 * SDMPrimerDesignerTest
 * 
 */
public class SDMPrimerDesignerTest extends AbstractTestCase {

    //gatgaaattaatgagccaccgcccaatatatgtgagcagtgcttaggtgatgaggctaatatacgaatgactaaaattccacaaggctctgaatgtaagatttgtaccttaccgttcaccttataccattttaagacgtctaaacgaagcaataatatcattaaaacattaatatgtgtacggtgcgccactcagagaaacatttgtcaatgctgtatgcttgattcaagatggcacatccctatacaactgcgggatcatttaatatcccttgtgaacgaggaaaacgttatgacggaggaagcaaaaaacgacatgatgaaaaggtttctgtcactaaaaaacgtgaaattaggaggagctcagattacgagtgatccgtcagaagcagataatatcgttgataaattgaaaaatatacttctgcgagcaacatcagatggcccaagtactccgttaataaaaaacacaactgcattgtataaaaatgaaaaaggtgcaaatgaggtcaaaaacttggaaaagtacgcgtccgtggacatttctcatattttgaagaaactacccttgaatgaatcgtttttaaaaaacccctccaccaaatcattttttctgtataatattgatgcctcgatccctgaatggaaaataactgatacagtttcgcaattattgggtataaagaaatggaaggatggaaattcattgtcattaatagtcaaccacaaggcgaagtgtggtggcctaagatttcaatctagtgaattgggggagcggtttgtcagtaaaataagtgagacacttgtcacaccaaagggcctaaagagaggagttttacttattgaccgttttagaatctttatcattccgtggtcgtctggcttttcagctgcgtcattcggaaccaacactgcagaaaatataaaacttagtttaagtttgaataagcttattcagttggagctagggctttcttttcctacaaaaagcacagataacgcaaagaatgacaagaaaaagacatcaaaaaaagttcataaggacagatcaaagaaatcgaaa

    private final String wildDNASeq = "tctgaaagaaaagctattaacaagtactatccaccggattacaacccactggaagctgaaaagct"
        + "atccaggaaaatggctgaataactgaaaaccatgaataagagccacgcatcgattagattaatga"
        + "ccccatttagtatgaggtgtttggaatgtaacgaatatattcccaaaagcagaaaattcaatggt"
        + "aagaaagagctactgaaggagaagtacctggactccattaagatatatagactaaccatttcatg"
        + "tccacgttgtgccaattccattgcatttagaacagaccccggtaattcagattatgtaatggaag"
        + "tggggggcgtaagaaactatgtccctcagaaaccaaatgatgacctgaacgctaaaacagctgtc"
        + "gaaagcatagacgagacactgcaacgcttagtgagagagaaggaaatggagcagaacgagaagat"
        + "gggtataaaggagcaagcagatgacaaaatggatttgctggaaaaaagactagccaaaattcaac"
        + "aagagcaagaggatgatgaggaacttgaaaatttaaggaaaaaaaaccttgaaatgagccagaga"
        + "gctgaaatgataaaccgcagcaaacacgcacaacaagaaaaagcagtaacgactgatgatctgga"
        + "caacctcgtagatcaggtatttgacaatcacaggcagcgtaccaataaacctggcaataacaacg"
        + "atgagaagagaactccgttgtttaatcctacatccactaagggaaaaatacagaagaaaagctct"
        + "gtgcgtacgaatccgttagggatagttataaaacgaggaaagtctctcaaa";

    /*
        "gatgaaattaatgagccaccgcccaatatatgtgagcagtgcttaggtgatgaggctaatata"
            + "cgaatgactaaaattccacaaggctctgaatgtaagatttgtaccttaccgttcaccttatac"
            + "cattttaagacgtctaaacgaagcaataatatcattaaaacattaatatgtgtacggtgcgcc"
            + "actcagagaaacatttgtcaatgctgtatgcttgattcaagatggcacatccctatacaactg"
            + "cgggatcatttaatatcccttgtgaacgaggaaaacgttatgacggaggaagcaaaaaacgac"
            + "atgatgaaaaggtttctgtcactaaaaaacgtgaaattaggaggagctcagattacgagtgat"
            + "ccgtcagaagcagataatatcgttgataaattgaaaaatatacttctgcgagcaacatcagat"
            + "ggcccaagtactccgttaataaaaaacacaactgcattgtataaaaatgaaaaaggtgcaaat"
            + "gaggtcaaaaacttggaaaagtacgcgtccgtggacatttctcatattttgaagaaactaccc"
            + "ttgaatgaatcgtttttaaaaaacccctccaccaaatcattttttctgtataatattgatgcc"
            + "tcgatccctgaatggaaaataactgatacagtttcgcaattattgggtataaagaaatggaag"
            + "gatggaaattcattgtcattaatagtcaaccacaaggcgaagtgtggtggcctaagatttcaa"
            + "tctagtgaattgggggagcggtttgtcagtaaaataagtgagacacttgtcacaccaaagggc"
            + "ctaaagagaggagttttacttattgaccgttttagaatctttatcattccgtggtcgtctggc"
            + "ttttcagctgcgtcattcggaaccaacactgcagaaaatataaaacttagtttaagtttgaat"
            + "aagcttattcagttggagctagggctttcttttcctacaaaaagcacagataacgcaaagaat"
            + "gacaagaaaaagacatcaaaaaaagttcataaggacagatcaaagaaatcgaaa";
    */
    private final String mutatedDNASeq = "tctgaaagaaaagctattaacaagtactatccaccggattacaacccactggaagctgaaaagct"
        + "atccaggaaaatggctaaaaaaGtgaaaaccatgaataagagccacgcatcgattagattaatga"
        + "ccccatttagtatgaggtgtttggaatgtaacgaatatattcccaaaagcagaaaattcaatggt"
        + "aagaaagagctactgaaggagaagtacctggactccattaagatatatagactaaccatttcatg"
        + "tccacgttgtgccaattccattgcatttagaacagaccccggtaattcagattatgtaatggaag"
        + "tggggggcgtaagaaactatgtccctcagaaaccaaatgatgacctgaacgctaaaacagctgtc"
        + "gaaagcatagacgagacactgcaacgcttagtgagagagaaggaaatggagcagaacgagaagat"
        + "gggtataaaggagcaagcagatgacaaaatggatttgctggaaaaaagactagccaaaattcaac"
        + "aagagcaagaggatgatgaggaacttgaaaatttaaggaaaaaaaaccttgaaatgagccagaga"
        + "gctgaaatgataaaccgcagcaaacacgcacaacaagaaaaagcagtaacgactgatgatctgga"
        + "caacctcgtagatcaggtatttgacaatcacaggcagcgtaccaataaacctggcaataacaacg"
        + "atgagaagagaactccgttgtttaatcctacatccactaagggaaaaatacagaagaaaagctct"
        + "gtgcgtacgaatccgttagggatagttataaaacgaggaaagtctctcaaa";

    /*   
       "gatgaaattaatgagccaccgcccaatatatgtgagcagtgcttaggtgatgaggctaatata"
     + "cgaatgactaaaattccacaaggcgctgaatgtaagatttgtaccttaccgttcaccttatac"
     + "cattttaagacgtctaaacgaagcaataatatcattaaaacattaatatgtgtacggtgcgcc"
     + "actcagagaaacatttgtcaatgctgtatgcttgattcaagatggcacatccctatacaactg"
     + "cgggatcatttaatatcccttgtgaacgaggaaaacgttatgacggaggaagcaaaaaacgac"
     + "atgatgaaaaggtttctgtcactaaaaaacgtgaaattaggaggagctcagattacgagtgat"
     + "ccgtcagaagcagataatatcgttgataaattgaaaaatatacttctgcgagcaacatcagat"
     + "ggcccaagtactccgttaataaaaaacacaactgcattgtataaaaatgaaaaaggtgcaaat"
     + "gaggtcaaaaacttggaaaagtacgcgtccgtggacatttctcatattttgaagaaactaccc"
     + "ttgaatgaatcgtttttaaaaaacccctccaccaaatcattttttctgtataatattgatgcc"
     + "tcgatccctgaatggaaaataactgatacagtttcgcaattattgggtataaagaaatggaag"
     + "gatggaaattcattgtcattaatagtcaaccacaaggcgaagtgtggtggcctaagatttcaa"
     + "tctagtgaattgggggagcggtttgtcagtaaaataagtgagacacttgtcacaccaaagggc"
     + "ctaaagagaggagttttacttattgaccgttttagaatctttatcattccgtggtcgtctggc"
     + "ttttcagctgcgtcattcggaaccaacactgcagaaaatataaaacttagtttaagtttgaat"
     + "aagcttattcagttggagctagggctttcttttcctacaaaaagcacagataacgcaaagaat"
     + "gacaagaaaaagacatcaaaaaaagttcataaggacagatcaaagaaatcgaaa";
    */
    /**
     * Constructor for SDMPrimerDesignerTest
     * 
     * @param name
     */
    public SDMPrimerDesignerTest(final String name) {
        super(name);
    }

    /**
     * SDMPrimerDesignerTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * SDMPrimerDesignerTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // contains no assertions!
    public void testSDMPrimerDesign() {
        final PimsSDMPrimerDesigner dp = PimsSDMPrimerDesigner.getSDMPrimerDesigner();
        //final List<String> primers =
        dp.makePrimers(new String[] { this.wildDNASeq, this.mutatedDNASeq }, 78f);
/*
        for (final String primer : primers) {
            System.out.println("primer [" + primer + "]");
        } */
    }

    public void testCountNoMutations() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final String news = new String(this.wildDNASeq);
        final int count = PimsSDMPrimerDesigner.countMutations(new String[] { news, wild });

        Assert.assertEquals(0, count);
    }

    public void testCountMutations() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.setCharAt(87, 'G');
        final String news = new String(dna.toString());
        final int count = PimsSDMPrimerDesigner.countMutations(new String[] { news, wild });

        Assert.assertEquals(1, count);
    }

    public void testFirstMutation() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.setCharAt(87, 'G');
        final String news = new String(dna.toString());
        final int count = PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);
    }

    public void testLastMutation() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.setCharAt(87, 'G');
        final String news = new String(dna.toString());
        final int count = PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);
    }

    public void testLastMutationAgain() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild =
            new String("GAATGTGTGACTCAGTTGTTGAAGGACACCTGCTTTGAAGGAGGGGA"
                + "CATTACTACGGTCTTCACACCAAGCGCCAAGTACTGCCAGGTAGTCTGCACTTACCACCCAAGATGTTTACTCT"
                + "TCACTTTCACGGCGGAATCACCATCTGAGGATCCCACCCGATGGTTTACTTGTGTCCTGAAAGACAGTGTTACA"
                + "GAAACACTGCCAAGAGTGAATAGGACAGCAGCGATTTCTGGGTATTCTTTCAAGCAATGCTCACACCAAATAAG"
                + "CGCTTGCAACAAAGACATTTATGTGGACCTAGACATGAAGGGCATAAACTATAACAGCTCAGTTGCCAAGAGTG"
                + "CTCAAGAATGCCAAGAAAGATGCACGGATGACGTCCACTGCCACTTTTTCACGTACGCCACAAGGCAGTTTCCC"
                + "AGCCTGGAGCATCGTAACATTTGTCTACTGAAGCACACCCAAACAGGGACACCAACCAGAATAACGAAGCTCGA"
                + "TAAAGTGGTGTCTGGATTTTCACTGAAATCCTGTGCACTTTCTAATCTG");

        final String news =
            new String("GAATGTGTGACTCAGTTGTTGAAGGACACCTGCTTTGAAGGAGGGGA"
                + "CATTACTACGGTCTTCACACCAAGCGCCAAGTACTGCCAGGTAGTCTGCACTTACCACCCAAGATGTTTACTCT"
                + "TCACTTTCACGGCGGAATCACCATCTGAGGATCCCACCCGATGGTTTACTTGTGTCCTGAAAGACAGTGTTACA"
                + "GAAACACTGCCAAGAGTGGAAAGGACAGCAGCGATTTCTGGGTATTCTTTCAAGCAATGCTCACACCAAATAAG"
                + "CGCTTGCAACAAAGACATTTATGTGGACCTAGACATGAAGGGCATAAACTATAACAGCTCAGTTGCCAAGAGTG"
                + "CTCAAGAATGCCAAGAAAGATGCACGGATGACGTCCACTGCCACTTTTTCACGTACGCCACAAGGCAGTTTCCC"
                + "AGCCTGGAGCATCGTAACATTTGTCTACTGAAGCACACCCAAACAGGGACACCAACCAGAATAACGAAGCTCGA"
                + "TAAAGTGGTGTCTGGATTTTCACTGAAATCCTGTGCACTTTCTAATCTG");

        Assert.assertEquals(213, PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild }));
        Assert.assertEquals(215, PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild }));
    }

    public void testMultipleMutations() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.setCharAt(87, 'G');
        dna.setCharAt(93, 'G');
        final String news = new String(dna.toString());
        int count = PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);

        count = PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild });

        Assert.assertEquals(93, count);

        count = PimsSDMPrimerDesigner.countMutations(new String[] { news, wild });

        Assert.assertEquals(2, count);
    }

    public void testDeletions() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.delete(87, 93);
        final String news = new String(dna.toString());
        int count = PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);

        count = PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);
    }

    public void testMyDeletions() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild =
            new String("GGCGGTGGACCACACATAGGCAGTGAAAGGCTTCAGGAGTTGCGGGC"
                + "AAGGACACAAACTCTTTTTTGCGACGTGCCGTCGGCGCCTTTTGGCATCCATGCCGTAACATCCGAAGGAACG"
                + "AAACATGGTGTTAACTGCGGTGAGTGGTTTGGGCCAACGCCGATAGCGAAAACGCTGAGTGCG");

        final String news =
            new String("GGCGGTGGACCACACATAGGCAGTGAAAGGCTTCAGGAGTTGCGGGC"
                + "AAGGACACAAACTCTTTTTTGCGACGTGCCGTCGGCGCCTATCCATGCCGTAACATCCGAAGGAACGAAACAT"
                + "GGTGTTAACTGCGGTGAGTGGTTTGGGCCAACGCCGATAGCGAAAACGCTGAGTGCG");

        int count = PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);

        count = PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);
/*
        System.out.println("firstmutation ["
            + PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild }) + "]");
        System.out.println("lastmutation [" + PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild })
            + "]"); */
        final PimsSDMPrimerDesigner design = PimsSDMPrimerDesigner.getSDMPrimerDesigner();
        design.makePrimers(new String[] { news, wild }, 78.0f);
    }

    public void testInsertions() throws IllegalAlphabetException, IllegalSymbolException {
        final String wild = new String(this.wildDNASeq);
        final StringBuffer dna = new StringBuffer(this.wildDNASeq);
        dna.insert(87, "CATCAT");
        final String news = new String(dna.toString());
        int count = PimsSDMPrimerDesigner.firstMutation(new String[] { news, wild });

        Assert.assertEquals(87, count);

        count = PimsSDMPrimerDesigner.lastMutation(new String[] { news, wild });

        Assert.assertEquals(93, count);
    }

    // contains no assertions!
    public void xtestMySDMPrimerDesign() {

        final String wild =
            new String("GGCGGTGGACCACACATAGGCAGTGAAAGGCTTCAGGAGTTGCGGGCAAGGACACAAACTCTTTTTT"
                + "GCGACGTGCCGTCGGCGCCTAGCGGCATCCATGCCGTAACATCCGAAGGAACGAAACATGGTGTTAACTGCGG"
                + "TGAGTGGTTTGGGCCAACGCCGATAGCGAAAACGCTGAGTGCG");

        final String mutated =
            new String("GGCGGTGGACCACACATAGGCAGTGAAAGGCTTCAGGAGTTGCGGGCAAGGACACAAACTCTTTTTT"
                + "GCGACGTGCCGTCGGCGCCTTTTGGCATCCATGCCGTAACATCCGAAGGAACGAAACATGGTGTTAACTGCGG"
                + "TGAGTGGTTTGGGCCAACGCCGATAGCGAAAACGCTGAGTGCG");

        final PimsSDMPrimerDesigner dp = PimsSDMPrimerDesigner.getSDMPrimerDesigner();
        final List<String> primers = dp.makePrimers(new String[] { wild, mutated }, 78f);

        for (final String primer : primers) {
            System.out.println("primer [" + primer + "]");
        }
    }
}
