package org.pimslims.utils.primer3;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.pimslims.lab.primer.YorkPrimerBean;

public class Primer3Results {

    private YorkPrimerBean forwardPrimer;

    private YorkPrimerBean reversePrimer;

    /*
     * Constants used in cutting values out of results file
     */
    private final int LENGTH_START = 24;

    private final int LENGTH_END = 27;

    private final int TM_START = 29;

    private final int TM_END = 35;

    private final int SEQUENCE_START = 56;

    public Primer3Results(final byte[] bytes) {

        System.out.println("org.pimslims.utils.primer3.Primer3Results [" + new String(bytes) + "]");

        final BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

        String line = null; // not declared within while loop
        /*
         * readLine is a bit quirky : it returns the content of a line MINUS the newline. it returns null only
         * for the END of the stream. it returns an empty String if two newlines appear in a row.
         */
        try {
            while ((line = in.readLine()) != null) {

                System.out.println("line [" + line + "]");

                if (line.startsWith("LEFT PRIMER")) {
                    this.forwardPrimer = this.makePrimer(line, 'F');
                }
                if (line.startsWith("RIGHT PRIMER")) {
                    this.reversePrimer = this.makePrimer(line, 'R');
                }
            }
        } catch (final IOException e) {
            System.out.println("IOException caught [" + e.getLocalizedMessage() + "]");
        }

    }

    private YorkPrimerBean makePrimer(final String s, final char c) {
        final YorkPrimerBean p = new YorkPrimerBean();
        p.setPrimerType(new Character(c).toString());
        p.setPrimerLength(Integer.parseInt(s.substring(this.LENGTH_START, this.LENGTH_END).trim()));
        p.setPrimerTm(new Float(s.substring(this.TM_START, this.TM_END).trim()).floatValue());
        p.setPrimerSeq(s.substring(this.SEQUENCE_START));
        return p;
    }

    public final YorkPrimerBean getForwardPrimer() {
        return this.forwardPrimer;
    }

    public final YorkPrimerBean getReversePrimer() {
        return this.reversePrimer;
    }

    /*
     * PRIMER PICKING RESULTS FOR AB004788_46_705 No mispriming library specified Using 1-based sequence
     * positions OLIGO start len tm gc% any 3' seq LEFT PRIMER 46 23 67.86 60.87 4.00 2.00
     * ATGTCGTCCCACCTAGTCGAGCC RIGHT PRIMER 705 23 68.05 60.87 5.00 0.00 TCAGTAGGTGCTGGCAGAGGGTG SEQUENCE
     * SIZE: 1265 INCLUDED REGION SIZE: 660 PRODUCT SIZE: 660, PAIR ANY COMPL: 6.00, PAIR 3' COMPL: 2.00 1
     * CGGAGACGGTCCTGCTGCTGCCGCAGTCCTGCCAGCTGTCCGACAATGTCGTCCCACCTA
     * .............................................>>>>>>>>>>>>>>> 61
     * GTCGAGCCGCCGCCGCCCCTGCACAACAACAACAACAACTGCGAGGAAAATGAGCAGTCT >>>>>>>> 121
     * CTGCCCCCGCCGGCCGGCCTCAACAGTTCCTGGGTGGAGCTACCCATGAACAGCAGCAAT 181
     * GGCAATGATAATGGCAATGGGAAAAATGGGGGGCTGGAACACGTACCATCCTCATCCTCC 241
     * ATCCACAATGGAGACATGGAGAAGATTCTTTTGGATGCACAACATGAATCAGGACAGAGT 301
     * AGTTCCAGAGGCAGTTCTCACTGTGACAGCCCTTCGCCACAAGAAGATGGGCAGATCATG 361
     * TTTGATGTGGAAATGCACACCAGCAGGGACCATAGCTCTCAGTCAGAAGAAGAAGTTGTA 421
     * GAAGGAGAGAAGGAAGTCGAGGCTTTGAAGAAAAGTGCGGACTGGGTATCAGACTGGTCC 481
     * AGTAGACCCGAAAACATTCCACCCAAGGAGTTCCACTTCAGACACCCTAAACGTTCTGTG 541
     * TCTTTAAGCATGAGGAAAAGTGGAGCCATGAAGAAAGGGGGTATTTTCTCCGCAGAATTT 601
     * CTGAAGGTGTTCATTCCATCTCTCTTCCTTTCTCATGTTTTGGCTTTGGGGCTAGGCATC 661
     * TATATTGGAAAGCGACTGAGCACACCCTCTGCCAGCACCTACTGAGGGAAAGGAAAAGCC <<<<<<<<<<<<<<<<<<<<<<<...............
     * 721 CCTGGAAATGCGTGTGACCTGTGAAGTGGTGTATTGTCACAGTAGCTTATTTGAACTTGA
     * ............................................................ 781
     * GACCATTGTAAGCATGACCCAACCTACCACCCTGTTTTTACATATCCAATTCCAGTAACT
     * ............................................................ 841
     * CTCAAATTCAATATTTTATTCAAACTCTGTTGAGGCATTTTACTAACCTTATACCCTTTT
     * ............................................................ 901
     * TGGCCTGAAGACATTTTAGAATTTCCTAACAGAGTTTACTGTTGTTTAGAAATTTGCAAG
     * ............................................................ 961
     * GGCTTCTTTTCCGCAAATGCCACCAGCAGATTATAATTTTGTCAGCAATGCTATTATCTC
     * ............................................................ 1021
     * TAATTAGTGCCACCAGACTAGACCTGTATCATTCATGGTATAAATTTTACTCTTGCAACA
     * ............................................................ 1081
     * TAACTACCATCTCTCTCTTAAAACGAGATCAGGTTAGCAAATGATGTAAAAGAAGCTTTA
     * ............................................................ 1141
     * TTGTCTAGTTGTTTTTTTTCCCCCAAGACAAAGGCAAGTTTCCCTAAGTTTGAGTTGATA
     * ............................................................ 1201
     * GTTATTAAAAAGAAAACAAAACAAAAAAAAAAGGCAAGGCACAACAAAAAAATATCCTGG
     * ............................................................ 1261 GCAAT ..... KEYS (in order of
     * precedence): ...... vector sequence >>>>>> left primer <<<<<< right primer Statistics con too in
     * in no tm tm high high high sid many tar excl bad GC too too any 3' poly end ered Ns get reg GC% clamp
     * low high compl compl X stab ok Left 22 0 0 0 0 4 5 10 0 0 0 0 3 Right 22 0 0 0 0 9 4 4 0 0 0 0 5 Pair
     * Stats: considered 1, ok 1 primer3 release 1.1.0
     */

    /*
     * PRIMER PICKING RESULTS FOR example No mispriming library specified Using 1-based sequence positions NO
     * PRIMERS FOUND SEQUENCE SIZE: 108 INCLUDED REGION SIZE: 90 1
     * GTAGTCAGTAGACNATGACNACTGACGATGCAGACNACACACACACACACAGCACACAGG .... 61
     * TATTAGTGGGCCATTCGATCCCGACCCAAATCGATAGCTACGATGACG .............. KEYS (in order of precedence): ......
     * vector sequence Statistics con too in in no tm tm high high high sid many tar excl bad GC too too any
     * 3' poly end ered Ns get reg GC% clamp low high compl compl X stab ok Left 48 0 0 0 0 8 40 0 0 0 0 0 0
     * Right 53 0 0 0 0 8 23 0 0 0 0 0 22 Pair Stats: considered 0, ok 0 primer3 release 1.1.0
     */

}
