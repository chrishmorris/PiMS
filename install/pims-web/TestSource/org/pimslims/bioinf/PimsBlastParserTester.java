package org.pimslims.bioinf;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.xml.sax.SAXException;

@Deprecated
// the code this tests is not in use
public class PimsBlastParserTester extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PimsBlastParserTester.class);
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastParser.PimsBlastParser()'
     */
    public void testPimsBlastParser() {

        final PimsBlastParser pbp = new PimsBlastParser();
        Assert.assertNotNull(pbp);

    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastParser.parseBlastString(String)' output from Blast which
     * returns no hits
     */
    public void testParseBlastBadString() throws IOException, SAXException {
        final List noHits = PimsBlastParser.parseBlastString(PimsBlastParserTester.badString); // output
        // with no
        // results
        Assert.assertNotNull(noHits);
        //System.out.println("The noHits list contains " + noHits.size() + " sss result\n");
        Assert.assertTrue(noHits.size() == 1);

    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastParser.parseBlastString(String)' How the parser handles
     * an empty string. Shouldn't happen
     */
    public void testParseEmptyString() throws IOException, SAXException {
        final List emptyHits = PimsBlastParser.parseBlastString(PimsBlastParserTester.emptyString);
        Assert.assertNotNull(emptyHits);
        //System.out.println("The list contains " + emptyHits.size() + " hit");
        Assert.assertTrue(emptyHits.size() == 0);
    }

    static final String TEST_STRING = BioJavaBlastParser.TEST_STRING;

    static String badString =
        "BLASTP 2.0MP-WashU [04-May-2006] [linux26-x64-I32LPF64 2006-05-10T17:22:28]\r\n" + "\r\n"
            + "Copyright (C) 1996-2006 Washington University, Saint Louis, Missouri USA.\r\n"
            + "All Rights Reserved.\r\n" + "\r\n"
            + "Reference:  Gish, W. (1996-2006) http://blast.wustl.edu\r\n" + "\r\n" + "Query=  Sequence\r\n"
            + "        (6 letters)\r\n" + ">Filtered+0\r\n" + "QWERTY\r\n" + "\r\n" + "Database:  pdb\r\n"
            + "           96,352 sequences; 23,073,089 total letters.\r\n"
            + "Searching....10....20....30....40....50....60....70....80....90....100% done\r\n" + "\r\n"
            + "                                                                     Smallest\r\n"
            + "                                                                       Sum\r\n"
            + "                                                              High  Probability\r\n"
            + "Sequences producing High-scoring Segment Pairs:              Score  P(N)      N\r\n" + "\r\n"
            + "\r\n" + "      *** NONE ***\r\n" + "\r\n" + "\r\n" + "\r\n" + "Parameters:\r\n" + "  E=10\r\n"
            + "  B=3\r\n" + "  V=3\r\n"
            + "  mformat=\"7,/ebi/extserv/blast-work/interactive/blast-20070612-11590895_app.xml\"\r\n"
            + "  mformat=1\r\n" + "  matrix=BLOSUM62\r\n" + "  sump\r\n" + "  echofilter\r\n"
            + "  filter=seg\r\n" + "  cpus=8\r\n" + "  sort_by_pvalue\r\n"
            + "  putenv=\"WUBLASTMAT=/ebi/extserv/bin/wu-blast/matrix\"\r\n"
            + "  putenv=\"WUBLASTDB=/ebi/services/idata/v1869/blastdb\"\r\n"
            + "  putenv=\"WUBLASTFILTER=/ebi/extserv/bin/wu-blast/filter\"\r\n" + "\r\n"
            + "  ctxfactor=1.00\r\n" + "\r\n"
            + "  Query                        -----  As Used  -----    -----  Computed  ----\r\n"
            + "  Frame  MatID Matrix name     Lambda    K       H      Lambda    K       H\r\n"
            + "   +0      0   BLOSUM62        0.319   0.128   0.524    same    same    same\r\n"
            + "               Q=9,R=2         0.244   0.0300  0.180     n/a     n/a     n/a\r\n" + "\r\n"
            + "  Query\r\n" + "  Frame  MatID  Length  Eff.Length     E    S W   T  X   E2     S2\r\n"
            + "   +0      0        6         6       9.3  56 3  12 22  0.48    19\r\n"
            + "                                                    29  0.44    22\r\n" + "\r\n" + "\r\n"
            + "Statistics:\r\n" + "\r\n" + "  Database:  /ebi/services/idata/v1869/blastdb/pdb\r\n"
            + "   Title:  pdb\r\n" + "   Posted:  11:00:21 PM BST Jun 6, 2007\r\n"
            + "   Created:  11:00:21 PM BST Jun 6, 2007\r\n" + "   Format:  XDF-1\r\n"
            + "   # of letters in database:  23,073,089\r\n" + "   # of sequences in database:  96,352\r\n"
            + "   # of database sequences satisfying E:  0\r\n" + "  No. of states in DFA:  95 (21 KB)\r\n"
            + "  Total size of DFA:  33 KB (2054 KB)\r\n"
            + "  Time to generate neighborhood:  0.00u 0.00s 0.00t   Elapsed:  00:00:00\r\n"
            + "  No. of threads or processors used:  8\r\n"
            + "  Search cpu time:  0.23u 0.50s 0.73t   Elapsed:  00:00:02\r\n"
            + "  Total cpu time:  0.23u 0.51s 0.74t   Elapsed:  00:00:02\r\n"
            + "  Start:  Tue Jun 12 11:59:10 2007   End:  Tue Jun 12 11:59:12 2007";

    static String emptyString = "";

    static String sgtString =
        "BLASTP 2.0MP-WashU [04-May-2006] [linux26-x64-I32LPF64 2006-05-10T17:22:28]\r\n" + "\r\n"
            + "Copyright (C) 1996-2006 Washington University, Saint Louis, Missouri USA.\r\n"
            + "All Rights Reserved.\r\n" + "\r\n"
            + "Reference:  Gish, W. (1996-2006) http://blast.wustl.edu\r\n" + "\r\n" + "Query=  Sequence\r\n"
            + "        (132 letters)\r\n" + ">Filtered+0\r\n"
            + "MKSSGLFPFLVLLALGTLAPWAVEGSGKSFKAGVCPPKKSAQCLRYKKPECQSDWQCPGK\r\n"
            + "KRCCPDTCGIKCLDPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMG\r\n" + "MCGKSCVSPVKA\r\n"
            + "\r\n" + "Database:  sgt\r\n" + "           129,521 sequences; 40,945,614 total letters.\r\n"
            + "Searching....10....20....30....40....50....60....70....80....90....100% done\r\n" + "\r\n"
            + "                                                                     Smallest\r\n"
            + "                                                                       Sum\r\n"
            + "                                                              High  Probability\r\n"
            + "Sequences producing High-scoring Segment Pairs:              Score  P(N)      N\r\n" + "\r\n"
            + "SGT:K03D10.1________ SECSG###### Expressed                     101  8.7e-05   1\r\n"
            + "SGT:VpR63___________ NESGC###### Expressed                      83  0.00041   1\r\n"
            + "SGT:APC26454________ Midwest#### Cloned                         82  0.00053   1\r\n" + "\r\n"
            + "WARNING:  Descriptions of 52 database sequences were not reported due to the\r\n"
            + "          limiting value of parameter V = 3.\r\n" + "\r\n" + "\r\n"
            + ">SGT:K03D10.1________ SECSG###### Expressed\r\n" + "        Length = 700\r\n" + "\r\n"
            + " Score = 101 (40.6 bits), Expect = 8.7e-05, P = 8.7e-05\r\n"
            + " Identities = 28/97 (28%), Positives = 36/97 (37%)\r\n" + "\r\n"
            + "Query:    38 KKSAQCLRYKKPECQSDWQ----CPGKKRCCPDTCGIKCLDPVD-TPNPTRRKPGKCPVT 92\r\n"
            + "             K+ A C     P C+  +     C  KK C        C D         + KPG CP  \r\n"
            + "Sbjct:    54 KEDATCSACSAP-CREQFDDIKAC--KKSCVASDDRETCEDSCHYLQRIYQEKPGACPSV 110\r\n" + "\r\n"
            + "Query:    93 YGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP 129\r\n"
            + "               Q         C+MDG+C    KCC   C + C+ P\r\n"
            + "Sbjct:   111 SNQS-NYECSALCQMDGECPETQKCCSSGCSRQCLKP 146\r\n" + "\r\n" + "\r\n"
            + ">SGT:VpR63___________ NESGC###### Expressed\r\n" + "        Length = 82\r\n" + "\r\n"
            + " Score = 83 (34.3 bits), Expect = 0.00041, P = 0.00041\r\n"
            + " Identities = 24/68 (35%), Positives = 35/68 (51%)\r\n" + "\r\n"
            + "Query:    64 CPDTCGIKCLDP-VDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKR-----DLKC 117\r\n"
            + "             C   CG  C+ P + +P P   K GK P    +C+ LN  N C++ G+ +R     D K \r\n"
            + "Sbjct:     3 CRLGCGACCIAPSISSPIPGMPK-GK-PAGV-RCVQLNDDNLCKLFGKPERPKVCHDFKP 59\r\n" + "\r\n"
            + "Query:   118 CMGMCGKS 125\r\n" + "             C  +CGK+\r\n" + "Sbjct:    60 CPVVCGKT 67\r\n"
            + "\r\n" + "\r\n" + ">SGT:APC26454________ Midwest#### Cloned\r\n" + "        Length = 83\r\n"
            + "\r\n" + " Score = 82 (33.9 bits), Expect = 0.00053, P = 0.00053\r\n"
            + " Identities = 23/68 (33%), Positives = 34/68 (50%)\r\n" + "\r\n"
            + "Query:    64 CPDTCGIKCLDP-VDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKR-----DLKC 117\r\n"
            + "             C   CG  C+ P + +P P     GK P    +C+ LN  N C++ G+ +R     D K \r\n"
            + "Sbjct:     4 CRLGCGACCIAPSISSPIPGMPN-GK-PAGV-RCVQLNEDNLCQLFGRPERPKVCHDFKA 60\r\n" + "\r\n"
            + "Query:   118 CMGMCGKS 125\r\n" + "             C  +CGK+\r\n" + "Sbjct:    61 CPVVCGKT 68\r\n"
            + "\r\n" + "WARNING:  HSPs involving 52 database sequences were not reported due to the\r\n"
            + "          limiting value of parameter B = 3.\r\n" + "\r\n" + "\r\n" + "Parameters:\r\n"
            + "  E=10\r\n" + "  B=3\r\n" + "  V=3\r\n"
            + "  mformat=\"7,/ebi/extserv/blast-work/interactive/blast-20070612-12381487_app.xml\"\r\n"
            + "  mformat=1\r\n" + "  matrix=BLOSUM62\r\n" + "  sump\r\n" + "  echofilter\r\n"
            + "  filter=seg\r\n" + "  cpus=8\r\n" + "  sort_by_pvalue\r\n"
            + "  putenv=\"WUBLASTMAT=/ebi/extserv/bin/wu-blast/matrix\"\r\n"
            + "  putenv=\"WUBLASTDB=/ebi/services/idata/v1869/blastdb\"\r\n"
            + "  putenv=\"WUBLASTFILTER=/ebi/extserv/bin/wu-blast/filter\"\r\n" + "\r\n"
            + "  ctxfactor=1.00\r\n" + "\r\n"
            + "  Query                        -----  As Used  -----    -----  Computed  ----\r\n"
            + "  Frame  MatID Matrix name     Lambda    K       H      Lambda    K       H\r\n"
            + "   +0      0   BLOSUM62        0.323   0.140   0.497    same    same    same\r\n"
            + "               Q=9,R=2         0.244   0.0300  0.180     n/a     n/a     n/a\r\n" + "\r\n"
            + "  Query\r\n" + "  Frame  MatID  Length  Eff.Length     E    S W   T  X   E2     S2\r\n"
            + "   +0      0      132       132       8.0  67 3  12 22  0.39    31\r\n"
            + "                                                    30  0.43    33\r\n" + "\r\n" + "\r\n"
            + "Statistics:\r\n" + "\r\n" + "  Database:  /ebi/services/idata/v1869/blastdb/sgt\r\n"
            + "   Title:  sgt\r\n" + "   Posted:  11:00:59 PM BST Jun 6, 2007\r\n"
            + "   Created:  11:00:59 PM BST Jun 6, 2007\r\n" + "   Format:  XDF-1\r\n"
            + "   # of letters in database:  40,945,614\r\n" + "   # of sequences in database:  129,521\r\n"
            + "   # of database sequences satisfying E:  55\r\n"
            + "  No. of states in DFA:  532 (113 KB)\r\n" + "  Total size of DFA:  231 KB (2097 KB)\r\n"
            + "  Time to generate neighborhood:  0.00u 0.00s 0.00t   Elapsed:  00:00:00\r\n"
            + "  No. of threads or processors used:  8\r\n"
            + "  Search cpu time:  1.69u 0.26s 1.95t   Elapsed:  00:00:01\r\n"
            + "  Total cpu time:  1.69u 0.26s 1.95t   Elapsed:  00:00:02\r\n"
            + "  Start:  Tue Jun 12 12:38:17 2007   End:  Tue Jun 12 12:38:19 2007\r\n"
            + "WARNINGS ISSUED:  2";
}
