/**
 * current-pims-web org.pimslims.lab.sequence PositionAwareSequenceTester.java
 * 
 * @author Petr Troshin
 * @date 21 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr Troshin
 * 
 * 
 */
package org.pimslims.lab.sequence;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * PositionAwareSequenceTester
 * 
 */
public class PositionAwareSequenceTester extends TestCase {
    static final String sequences =
        ">2055804_pIC3.3_alphaleadf_C02.seq\r\n"
            + "NNNNNNNNNNNNANNNNNNNNNNNNCTCTCGANNNNAGAGGCTGAGCTATGGCGAGTAAAGAGATGTTTGGGATTTAAGTAAAATTGGAGAAGAACAATCAGCAGAAGATGCAGAAGATGGGCCTCCAGAGCTCCTGTTTATTCATGGAGGGCACACTGCCAAGATTTCTGACTTCAGCTGGAATCCCAATGAACCTTGGGTCATTTGCTCTGTGTCTGAAGATAACATCATGCAGATATGGCAGATGGCTGAAAATATTTACAATGATGAAGAGTCAGATGTCACGGCATCGGAACTGGAGGGACAAGGATCTAAACATCACCATCACCATCACTAAGTTTTAGCCTTAGACATGACTGTTCCTCAGTTCAAGTTGGGCACTTACGAGAAGACCGGTCTTGCTAGATTCTAATCAAGAGGATGTCAGAATGCCATTTGCCTGAGAGATGCAGGCTTCATTTTTGATACTTTTTTATTTGTAACCTATATAGTATAGGATTTTTTTTGTCATTTTGTTTCTTCTCGTACGAGCTTGCTCCTGATCAGCCTATCTCGCAGCTGATGAATATCTTGTGGTAGGGGTTTGGGAAAATCATTCGAGTTTGATGTTTTTCTTGGTATTTCCCACTCCTCTTCAGAGTACAGAAGATTAAGTGAGACCTTCGTTTGTGCGGATCCCCCACACACCATAGCTTCAAAATGTTTCTACTCCTTTTTTACTCTTCCAGATTTTCTCGGACTCCGCGCATCGCCGTACCACTTCAAAACACCCAAGCACAGCATACTAAATTTCCCCTCTTTCTTCCTCTAGGGTGTCGTTAATTACCCGTACTAAAGGTTTGGAAAAGAAAAAAGAGANCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCNTGTNCTATNCACTTTTTTTACTTCTTGCTCATTAGAAGAAGCNTAGCATCTANCTANNNNNNGTTGANANTNNNATCGGCATANNNNNNCGGCNNANNATANNNGACAAGGTGANGANNNNNATGNNNNTGANCAGNNCNNNCNNNNNNNCNNNNNNNGNNNNNNNNGNNNNNTNNGACNACNNNNNNNNNNNGNNNTNNNNNNNANTNCNNNNNGNNNGNNNNNNNNNCNNTTNNNNNNNNNNNNNNNNNANNNNNNNNNN\r\n"
            + ">2055804_pIC3.4_alphaleadf_D02.seq\r\n"
            + "NNNNNNNNNNNTTCTCTCGANAANAGAGGCTGAGCTATGGCGAGTAAAGAGATGTTTGAAGATACTGTGGAGGAGCGTGTCATCAACGAAGAGTATAAAATCTGGAAGAAGAATACACCGTTTCTGTATGACCTGGTTATGACCCATGCTCTTCAGTGGCCCAGTCTTACCGTTCAGTGGCTTCCTGAAGTGACTAAACCAGAAGGAAAGGATTATGCCCTTCATTGGCTAGTGCTGGGCACTCATACATCTGATGAGCAGAACCATCTGGTGGTTGCTCGAGTTCATATTCCCAATGATGATGCACAGTTTGATGCTTCCCACTGTGACAGTGACAAGGGAGAATTCGGTGGCTTTGGTTCTGTAACAGGGAAAATTGAATGTGAAATTAAAATTAACCATGAAGGAGAAGTGAATCGTGCTCGTTATATGCCACAGAATCCTCACATCATTGCCACAAAAACACCATCTTCTGATGTTTTGGTTTTTGACTATACAAAACACCCTGCAAAACCAGATCCAAGTGGAGAATGTAATCCTGATCTTAGATTAAGAGGTCACCAAAAGGAAGGCTATGGTCTTTCCTGGAATTCTAATCTGAGTGGGCATCTCCTGAGTGCATCTGATGACCATACTGTCTGCCTGTGGGATATAAATGCAGGACCAAAGGAAGGCAAAATTGTGGATGCTAAAGCAATCTTTACTGGCCACTCAGCTGTTGTAGAGGATGTGGCCTGGCATCTGCTGCATGAGTCCTTGTTTGGATCTGTTGCTGATGATCAGAAACTTATGATATGGGACACCAGATCCAATACCACTTCTAAGCCGAGCCATTTGGTGGATGCACACACCGCTGAGGTCAACTGCCTCTCATTCAATCCCTACAGCGAGTTCATTCTGGCAACTGGCTCTGCAGATAAGACTGTAGCTTTATGGGACCTGCGTAATCTGAAACTAAAACTCCACACCTTTGAATCGCATANGGATGAAATTTTCCAGGTCCACTGGNCTCACATNATGAAACTNNTNCTGGGCCTCAAGNNGNACTGATCNCNCNGAANNNNTGGGNNTTAGTAAATTGGANAAGAANATCANNNGAANATGCNNAANAANGGGNNTNCNNNNNNCNNNTNNTCATGGNNGNNNNCNNNNNNATTTCTGNANNTNNNNGANNCNNNANNNGNNATTTNNNNNNNNNNNANNNNNNNNNGGNNAATNGNNNNGNNNNNNNNNNNNNNANNNNNNNNNTNNNNNNNNNNNNCNNNNGGNNNNNNN\r\n"
            + ">2055804_pIC4.1_alphaleadf_E02.seq\r\n"
            + "NNNNNNNNNANNNNNNCTCTCGANNNNAGAGGCTGAAGAGAAGAGGAGTGGAGGTCCTGCGTTTGCAACGGTCTGCTGCTAGTGTATCCCCTCCTGTTGCGTTTGGCACTTATGTGTGAGAATGGACCTGTGGATGTCGGATGGCAAAAAGGTTTCATTCAACCTTTCGTCTTTGGATGTTAGATCTGATCTCATGACCAAAATCCCTTAACGTGAGTTTTCGTTCCACTGAGCGTCAGACCCCGTAGAAAAGATCAAAGGATCTTCTTGAGATCCTTTTTTTCTGCGCGTAATCTGCTGCTTGCAAACAAAAAAACCACCGCTACCAGCGGTGGTTTGTTTGCCGGATCAAGAGCTACCAACTCTTTTTCCGAAGGTAACTGGCTTCAGCAGAGCGCAGATACCAAATACTGTTCTTCTAGTGTAGCCGTAGTTAGGCCACCACTTCAAGAACTCTGTAGCACCGCCTACATACCTCGCTCTGCTAATCCTGTTACCAGTGGCTGCTGCCAGTGGCGATAAGTCGTGTCTTACCGGGTTGGACTCAAGACGATAGTTACCGGATAAGGCGCAGCGGTCGGGCTGAACGGGGGGTTCGTGCACACAGCCCAGCTTGGAGCGAACGACCTACACCGAACTGAGATACCTACAGCGTGAGCTATGAGAAAGCGCCACGCTTCCCGAAGGGAGAAAGGCGGACAGGTATCCGGTAAGCGGCAGGGTCGGAACAGGAGAGCGCACGAGGGAGCTTCCAGGGGGAAACGCCTGGTATCTTTATAGTCCTGTCGGGNTTCGCCACCTCTGACTTGAGCGTCGATTTTTGTGATGCTCGTCAGGGGGGCGGAGCCTATGGAAAAACGCCAGCACGCGGCCTTTTNACGGTTCCTGGCCTTTTGCTGGCCTTTTGCTCACATGTTGGTCTCANCTTGCAAATNAAAGCCNTNCGAGCGTCCAAANCTTCTCAAGCAAGGNTTTCAGTATAATGNTACATGCGTACACNCNTCTGTACAGAAAAAAANAAAAATTTGAAATATNAATAACGNTNCNNATACNNACATNACNNTANANAANTAAATANGNACCNANANTNNNGNTNNNNAACNCCNTNCTTTTCGNNANNGCGAANNGGGGGGNNNGGNNTGNANGNNNNNNGNACNTNANNNATNNNNNANNNNNNNNGNNAAAGGGGAACNNNNNCNNNNNNNNNNNNNNTNNNGNNNNCTNNNANNNNNNTNNNNNNCNNNNNNNN\r\n"
            + ">2055804_pIC4.2_alphaleadf_F02.seq\r\n"
            + "NNNNNNNNNNNNGTTCTCTCGNNANNAGAGGCCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCTTGTTCTATTACAACTTTTTTTACTTCTTGCTCATTAGAAAGAAAGCATAGCAATCTAATCTAAGGGCGGTGTTGACAATTAATCATCGGCATAGTATATCGGCATAGTATAATACGACAAGGTGAGGAACTAAACCATGGCCAAGTTGACCAGTGCCGTTCCGGTGCTCACCGCGCGCGACGTCGCCGGAGCGGTCGAGTTCTGGACCGACCGGCTCGGGTTCTCCCGGGAATTCGTGGAGGACGACTTCGCCGGTGTGGTCCGGGACGACGTGACCCTGTTCATCAGCGCGGTCCAGGACCAGGTGGTGCCGGACAACACCCTGGCCTGGGTGTGGGTGCGCGGCCTGGACGAGCTGTACGCCGAGTGGTCGGAGGTCGTGTCCACGAACTTCCGGGACGCCTCCGGGCCGGCCATGACCGAGATCGGCGAGCAGCCGTGGGGGCGGGAGTTCGCCCTGCGCGACCCGGCCGGCAACTGCGTGCACTTCGTGGCCGAGGAGCAGGACTGACACGTCCGACGCGGCCCGACGGGTCCGAGGCCTCGGAGATCCGTCCCCCTTTTCCTTTGTCGATATCATGTAATTAGTTATGTCACGCTTACATTCACGCCCTCCCCCCACATCCGCTCTAACCGAAAAGGAAGGAGTTAGACAACCTGAAGTCTAGGTCCCTATTTATTTTTTTATAGTTATGTTAGTATTAAGAACGTTATTTATATTTCAAATTTTTCTTTTTTTTCTGTACAGACGCGTGTACGCATGTAACATTATACTGAAAACCTTGCTTGANAANNTTTTGGGACGCTCGAAGGCTTTANTTGCAGCTGGANANNNATGTGAGCAAAGGCCAGCAANNNNGNANNTAAAAGGCNCNTTGCTGGCNTTTTCNTNGNNNCNNCCCCCNNANNNNATNNNNAANCNANNNNNAGTNNNNGNNGNNNANNNNNNNNNNNNANNNCNGNNNTNCNNNNNNNCNNNNGNNNNNNNCNANCNNNNNNNCNNNNNNNNNNCNNCCNNNGNNNNGNGN\r\n"
            + ">2055804_pIC4.3_alphaleadf_G02.seq\r\n"
            + "NNNNNNNNNNNNNNNNNNNNNNCTTNNNNNANNNNNGCTGCTGATGCTGNNNNACCGGTGTNTTATTNCTANNACCCNNNTNTNGNNGNNTTCTNTGNGNGCTGGACCCCTATTACNCCNCATCTGCTGNNGTTGCCCCATNTGCTGNTCCTGCATTACGGTCTCTATAAGAAGATGATCGTCTTCCAGCCACACCAGGCCTCCCATCATGAGATGTGCTGCTTCCACTCGGANGACTACNTTGACTTCCTGAACAGAGTCAGCCCCACCTNTATATGCTNNNNTCAGAGNAGTCTTAATGCCNTCTACNNGNNNNATGACTGCCCNNTGTTTCCCGGGCTCTTTNANTTCNGCTCGCN\r\n"
            + ">2055804_pIC5.2_alphaleadf_B03.seq\r\n"
            + "TCTCTCGGAGGCTGAAGCTAAAGTACCAAGAATCAAAAATAAAGTGTGAACCTAGACCATGACAAACTATTGTGGGAACCTATTATAAAACATCACCATCACCATCACTAAGTTTTAGCCTTAGACATGACTGTTCCTCAGTTCAAGTTGGGCACTTACGAGAAGACCGGTCTTGCTAGATTCTAATCAAGAGGATGTCAGAATGCCATTTGCCTGAGAGATGCAGGCTTCATTTTTGATACTTTTTTATTTGTAACCTATATAGTATAGGATTTTTTTTGTCATTTTGTTTCTTCTCGTACGAGCTTGCTCCTGATCAGCCTATCTCGCAGCTGATGAATATCTTGTGGTAGGGGTTTGGGAAAATCATTCGAGTTTGATGTTTTTCTTGGTATTTCCCACTCCTCTTCAGAGTACAGAAGATTAAGTGAGACCTTCGTTTGTGCGGATCCCCCACACACCATAGCTTCAAAATGTTTCTACTCCTTTTTTACTCTTCCAGATTTTCTCGGACTCCGCGCATCGCCGTACCACTTCAAAACACCCAAGCACAGCATACTAAATTTCCCCTCTTTCTTCCTCTAGGGTGTCGTTAATTACCCGTACTAAAGGTTTGGAAAAGAAAAAAGAGACCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCTTGTTCTATTACAACTTTTTTTACTTCTTGCTCATTAGAAAGAAAGCATAGCAATCTAATCTAAGGGCGGTGTTGACAATTAATCATCGGCATAGTATATCGGCATAGTATAATACGACAAGGTGAGGAACTAAACCATGGNCAAGTTGACCAGTGCCGTTCCGNTGCTCACCGCGCGCGACGT\r\n";

    public void testLoadFasta() {
        ArrayList<PositionAwareSequence> pss = null;
        try {
            pss = PositionAwareSequence.loadFastaFromString(PositionAwareSequenceTester.sequences);
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertFalse(pss.isEmpty());
        Assert.assertEquals(6, pss.size());
    }

    public void testRemoveAmbiguity() {
        ArrayList<PositionAwareSequence> pss = null;
        try {
            pss = PositionAwareSequence.loadFastaFromString(PositionAwareSequenceTester.sequences);
        } catch (final IOException e) {
            Assert.fail(e.getMessage());
        }
        PositionAwareSequence ps = pss.get(0);
        Assert
            .assertEquals(
                "NNNNNNNNNNNNANNNNNNNNNNNNCTCTCGANNNNAGAGGCTGAGCTATGGCGAGTAAAGAGATGTTTGGGATTTAAGTAAAATTGGAGAAGAACAATCAGCAGAAGATGCAGAAGATGGGCCTCCAGAGCTCCTGTTTATTCATGGAGGGCACACTGCCAAGATTTCTGACTTCAGCTGGAATCCCAATGAACCTTGGGTCATTTGCTCTGTGTCTGAAGATAACATCATGCAGATATGGCAGATGGCTGAAAATATTTACAATGATGAAGAGTCAGATGTCACGGCATCGGAACTGGAGGGACAAGGATCTAAACATCACCATCACCATCACTAAGTTTTAGCCTTAGACATGACTGTTCCTCAGTTCAAGTTGGGCACTTACGAGAAGACCGGTCTTGCTAGATTCTAATCAAGAGGATGTCAGAATGCCATTTGCCTGAGAGATGCAGGCTTCATTTTTGATACTTTTTTATTTGTAACCTATATAGTATAGGATTTTTTTTGTCATTTTGTTTCTTCTCGTACGAGCTTGCTCCTGATCAGCCTATCTCGCAGCTGATGAATATCTTGTGGTAGGGGTTTGGGAAAATCATTCGAGTTTGATGTTTTTCTTGGTATTTCCCACTCCTCTTCAGAGTACAGAAGATTAAGTGAGACCTTCGTTTGTGCGGATCCCCCACACACCATAGCTTCAAAATGTTTCTACTCCTTTTTTACTCTTCCAGATTTTCTCGGACTCCGCGCATCGCCGTACCACTTCAAAACACCCAAGCACAGCATACTAAATTTCCCCTCTTTCTTCCTCTAGGGTGTCGTTAATTACCCGTACTAAAGGTTTGGAAAAGAAAAAAGAGANCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCNTGTNCTATNCACTTTTTTTACTTCTTGCTCATTAGAAGAAGCNTAGCATCTANCTANNNNNNGTTGANANTNNNATCGGCATANNNNNNCGGCNNANNATANNNGACAAGGTGANGANNNNNATGNNNNTGANCAGNNCNNNCNNNNNNNCNNNNNNNGNNNNNNNNGNNNNNTNNGACNACNNNNNNNNNNNGNNNTNNNNNNNANTNCNNNNNGNNNGNNNNNNNNNCNNTTNNNNNNNNNNNNNNNNNANNNNNNNNNN",
                ps.sequence);
        Assert
            .assertEquals(
                "AGAGGCTGAGCTATGGCGAGTAAAGAGATGTTTGGGATTTAAGTAAAATTGGAGAAGAACAATCAGCAGAAGATGCAGAAGATGGGCCTCCAGAGCTCCTGTTTATTCATGGAGGGCACACTGCCAAGATTTCTGACTTCAGCTGGAATCCCAATGAACCTTGGGTCATTTGCTCTGTGTCTGAAGATAACATCATGCAGATATGGCAGATGGCTGAAAATATTTACAATGATGAAGAGTCAGATGTCACGGCATCGGAACTGGAGGGACAAGGATCTAAACATCACCATCACCATCACTAAGTTTTAGCCTTAGACATGACTGTTCCTCAGTTCAAGTTGGGCACTTACGAGAAGACCGGTCTTGCTAGATTCTAATCAAGAGGATGTCAGAATGCCATTTGCCTGAGAGATGCAGGCTTCATTTTTGATACTTTTTTATTTGTAACCTATATAGTATAGGATTTTTTTTGTCATTTTGTTTCTTCTCGTACGAGCTTGCTCCTGATCAGCCTATCTCGCAGCTGATGAATATCTTGTGGTAGGGGTTTGGGAAAATCATTCGAGTTTGATGTTTTTCTTGGTATTTCCCACTCCTCTTCAGAGTACAGAAGATTAAGTGAGACCTTCGTTTGTGCGGATCCCCCACACACCATAGCTTCAAAATGTTTCTACTCCTTTTTTACTCTTCCAGATTTTCTCGGACTCCGCGCATCGCCGTACCACTTCAAAACACCCAAGCACAGCATACTAAATTTCCCCTCTTTCTTCCTCTAGGGTGTCGTTAATTACCCGTACTAAAGGTTTGGAAAAGAAAAAAGAGANCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCNTGTNCTATNCACTTTTTTTACTTCTTGCTCATTAGAAGAAGCNTAGCATCTANCTA",
                ps.removeAmbiguity());
        ps = pss.get(5);
        // No ambiguity
        Assert
            .assertEquals(
                "TCTCTCGGAGGCTGAAGCTAAAGTACCAAGAATCAAAAATAAAGTGTGAACCTAGACCATGACAAACTATTGTGGGAACCTATTATAAAACATCACCATCACCATCACTAAGTTTTAGCCTTAGACATGACTGTTCCTCAGTTCAAGTTGGGCACTTACGAGAAGACCGGTCTTGCTAGATTCTAATCAAGAGGATGTCAGAATGCCATTTGCCTGAGAGATGCAGGCTTCATTTTTGATACTTTTTTATTTGTAACCTATATAGTATAGGATTTTTTTTGTCATTTTGTTTCTTCTCGTACGAGCTTGCTCCTGATCAGCCTATCTCGCAGCTGATGAATATCTTGTGGTAGGGGTTTGGGAAAATCATTCGAGTTTGATGTTTTTCTTGGTATTTCCCACTCCTCTTCAGAGTACAGAAGATTAAGTGAGACCTTCGTTTGTGCGGATCCCCCACACACCATAGCTTCAAAATGTTTCTACTCCTTTTTTACTCTTCCAGATTTTCTCGGACTCCGCGCATCGCCGTACCACTTCAAAACACCCAAGCACAGCATACTAAATTTCCCCTCTTTCTTCCTCTAGGGTGTCGTTAATTACCCGTACTAAAGGTTTGGAAAAGAAAAAAGAGACCGCCTCGTTTCTTTTTCTTCGTCGAAAAAGGCAATAAAAATTTTTATCACGTTTCTTTTTCTTGAAAATTTTTTTTTTTGATTTTTTTCTCTTTCGATGACCTCCCATTGATATTTAAGTTAATAAACGGTCTTCAATTTCTCAAGTTTCAGTTTCATTTTTCTTGTTCTATTACAACTTTTTTTACTTCTTGCTCATTAGAAAGAAAGCATAGCAATCTAATCTAAGGGCGGTGTTGACAATTAATCATCGGCATAGTATATCGGCATAGTATAATACGACAAGGTGAGGAACTAAACCATGGNCAAGTTGACCAGTGCCGTTCCGNTGCTCACCGCGCGCGACGT",
                ps.removeAmbiguity());
    }
}
