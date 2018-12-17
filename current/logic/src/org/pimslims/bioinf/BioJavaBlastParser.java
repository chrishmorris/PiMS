package org.pimslims.bioinf;

import java.io.*;
import java.util.*;

import org.biojava.bio.program.sax.*;
import org.biojava.bio.program.ssbind.*;
import org.biojava.bio.search.*;
import org.biojava.bio.seq.db.*;
import org.xml.sax.*;
import org.biojava.bio.*;

public class BioJavaBlastParser {

    /**
     * Class based on BioJava cookbook example for parsing blast output BUT, it uses the raw output, NOT the
     * xml output Also doesn't recognise the 'BLASTP 2.0MP-WashU [04-May-2006]' from ebi web service had to
     * change it to BLASTP 2.0.11 [Oct-15-2006]
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            // get the Blast input as a Stream
            // InputStream is = new FileInputStream(args[0]);
            StringReader str = new StringReader(TEST_STRING);

            // make a BlastLikeSAXParser
            BlastLikeSAXParser parser = new BlastLikeSAXParser();
            parser.setModeLazy();

            doParse(str, parser);

        } catch (SAXException ex) {
            // XML problem
            ex.printStackTrace();
        } catch (IOException ex) {
            // IO problem, possibly file not found
            ex.printStackTrace();
        }
    }

    /**
     * @param str
     * @param parser
     * @throws IOException
     * @throws SAXException
     * @throws NoSuchElementException
     */
    static void doParse(StringReader str, XMLReader parser) throws IOException, SAXException {
        // make the SAX event adapter that will pass events to a Handler.
        SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();

        // set the parsers SAX event adapter
        parser.setContentHandler(adapter);

        // The list to hold the SeqSimilaritySearchResults
        List results = new ArrayList();

        // create the SearchContentHandler that will build
        // SeqSimilaritySearchResults
        // in the results List
        SearchContentHandler builder =
            new BlastLikeSearchBuilder(results, new DummySequenceDB("queries"),
                new DummySequenceDBInstallation());

        // register builder with adapter
        adapter.setSearchContentHandler(builder);

        // parse the file, after this the result List will be populated with
        // SeqSimilaritySearchResults
        // parser.parse(new InputSource(is));
        parser.parse(new InputSource(str));

        // output some blast details
        for (Iterator i = results.iterator(); i.hasNext();) {
            SeqSimilaritySearchResult result = (SeqSimilaritySearchResult) i.next();

            Annotation anno = result.getAnnotation();

            for (Iterator j = anno.keys().iterator(); j.hasNext();) {
                Object key = j.next();
                Object property = anno.getProperty(key);
                System.out.println(key + " : " + property);
            }
            System.out.println("Hits: ");

            // list the hits
            for (Iterator k = result.getHits().iterator(); k.hasNext();) {
                SeqSimilaritySearchHit hit = (SeqSimilaritySearchHit) k.next();
                System.out.print("\tmatch: " + hit.getSubjectID());
                System.out.println("\te score: " + hit.getEValue());
            }

            System.out.println("\n");
        }
    }

    

    public static final  String TEST_STRING =
        "BLASTP 2.0MP-WashU [04-May-2006] [linux26-x64-I32LPF64 2006-05-10T17:22:28]\r\n" + "\r\n"
            + "Copyright (C) 1996-2006 Washington University, Saint Louis, Missouri USA.\r\n"
            + "All Rights Reserved.\r\n" + "\r\n"
            + "Reference:  Gish, W. (1996-2006) http://blast.wustl.edu\r\n" + "\r\n" + "Query=  Sequence\r\n"
            + "        (132 letters)\r\n" + ">Filtered+0\r\n"
            + "MKSSGLFPFLVLLALGTLAPWAVEGSGKSFKAGVCPPKKSAQCLRYKKPECQSDWQCPGK\r\n"
            + "KRCCPDTCGIKCLDPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMG\r\n" + "MCGKSCVSPVKA\r\n"
            + "\r\n" + "Database:  pdb\r\n" + "           94,877 sequences; 22,737,184 total letters.\r\n"
            + "Searching....10....20....30....40....50....60....70....80....90....100% done\r\n" + "\r\n"
            + "                                                                     Smallest\r\n"
            + "                                                                       Sum\r\n"
            + "                                                              High  Probability\r\n"
            + "Sequences producing High-scoring Segment Pairs:              Score  P(N)      N\r\n" + "\r\n"
            + "PDB:1FLE_I mol:protein length:57  ELAFIN                       143  1.3e-10   1\r\n"
            + "PDB:2REL_  mol:protein length:57  R-ELAFIN                     143  1.3e-10   1\r\n"
            + "PDB:1UDK_A mol:protein length:51  Nawaprin                      79  0.00081   1\r\n" + "\r\n"
            + "WARNING:  Descriptions of 118 database sequences were not reported due to the\r\n"
            + "          limiting value of parameter V = 3.\r\n" + "\r\n" + "\r\n"
            + ">PDB:1FLE_I mol:protein length:57  ELAFIN\r\n" + "        Length = 57\r\n" + "\r\n"
            + " Score = 143 (55.4 bits), Expect = 1.3e-10, P = 1.3e-10\r\n"
            + " Identities = 27/56 (48%), Positives = 31/56 (55%)\r\n" + "\r\n"
            + "Query:    74 DPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP 129\r\n"
            + "             +PV  P  T  KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P\r\n"
            + "Sbjct:     3 EPVKGPVST--KPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP 56\r\n" + "\r\n"
            + " Score = 94 (38.1 bits), Expect = 2.1e-05, P = 2.1e-05\r\n"
            + " Identities = 19/48 (39%), Positives = 23/48 (47%)\r\n" + "\r\n"
            + "Query:    29 SFKAGVCPPKKSAQCLRYKKPE-CQSDWQCPGKKRCCPDTCGIKCLDP 75\r\n"
            + "             S K G CP     +C     P  C  D  CPG K+CC  +CG+ C  P\r\n"
            + "Sbjct:    10 STKPGSCPIIL-IRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP 56\r\n" + "\r\n" + "\r\n"
            + ">PDB:2REL_  mol:protein length:57  R-ELAFIN\r\n" + "        Length = 57\r\n" + "\r\n"
            + " Score = 143 (55.4 bits), Expect = 1.3e-10, P = 1.3e-10\r\n"
            + " Identities = 27/56 (48%), Positives = 31/56 (55%)\r\n" + "\r\n"
            + "Query:    74 DPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP 129\r\n"
            + "             +PV  P  T  KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P\r\n"
            + "Sbjct:     3 EPVKGPVST--KPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP 56\r\n" + "\r\n"
            + " Score = 94 (38.1 bits), Expect = 2.1e-05, P = 2.1e-05\r\n"
            + " Identities = 19/48 (39%), Positives = 23/48 (47%)\r\n" + "\r\n"
            + "Query:    29 SFKAGVCPPKKSAQCLRYKKPE-CQSDWQCPGKKRCCPDTCGIKCLDP 75\r\n"
            + "             S K G CP     +C     P  C  D  CPG K+CC  +CG+ C  P\r\n"
            + "Sbjct:    10 STKPGSCPIIL-IRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP 56\r\n" + "\r\n" + "\r\n"
            + ">PDB:1UDK_A mol:protein length:51  Nawaprin\r\n" + "        Length = 51\r\n" + "\r\n"
            + " Score = 79 (32.9 bits), Expect = 0.00081, P = 0.00081\r\n"
            + " Identities = 18/48 (37%), Positives = 23/48 (47%)\r\n" + "\r\n"
            + "Query:    31 KAGVCPPKKSA-QCLRYKKPECQSDWQCPGKKRCCPDTCG-IKCLDPV 76\r\n"
            + "             K+G CP        L   K  C SD  CP  ++CC + CG + C  PV\r\n"
            + "Sbjct:     3 KSGSCPDMSMPIPPLGICKTLCNSDSGCPNVQKCCKNGCGFMTCTTPV 50\r\n" + "\r\n"
            + " Score = 65 (27.9 bits), Expect = 0.032, P = 0.031\r\n"
            + " Identities = 18/52 (34%), Positives = 20/52 (38%)\r\n" + "\r\n"
            + "Query:    85 KPGKCPVTYGQCLMLNPP-----NFCEMDGQCKRDLKCCMGMCG-KSCVSPV 130\r\n"
            + "             K G CP       M  PP       C  D  C    KCC   CG  +C +PV\r\n"
            + "Sbjct:     3 KSGSCP----DMSMPIPPLGICKTLCNSDSGCPNVQKCCKNGCGFMTCTTPV 50\r\n" + "\r\n"
            + "WARNING:  HSPs involving 118 database sequences were not reported due to the\r\n"
            + "          limiting value of parameter B = 3.\r\n" + "\r\n" + "\r\n" + "Parameters:\r\n"
            + "  E=10\r\n" + "  B=3\r\n" + "  V=3\r\n"
            + "  mformat=\"7,/ebi/extserv/blast-work/interactive/blast-20070522-09554183_app.xml\"\r\n"
            + "  mformat=1\r\n" + "  matrix=BLOSUM62\r\n" + "  sump\r\n" + "  echofilter\r\n"
            + "  filter=seg\r\n" + "  cpus=8\r\n" + "  sort_by_pvalue\r\n"
            + "  putenv=\"WUBLASTMAT=/ebi/extserv/bin/wu-blast/matrix\"\r\n"
            + "  putenv=\"WUBLASTDB=/ebi/services/idata/v1848/blastdb\"\r\n"
            + "  putenv=\"WUBLASTFILTER=/ebi/extserv/bin/wu-blast/filter\"\r\n" + "\r\n"
            + "  ctxfactor=1.00\r\n" + "\r\n"
            + "  Query                        -----  As Used  -----    -----  Computed  ----\r\n"
            + "  Frame  MatID Matrix name     Lambda    K       H      Lambda    K       H\r\n"
            + "   +0      0   BLOSUM62        0.323   0.140   0.497    same    same    same\r\n"
            + "               Q=9,R=2         0.244   0.0300  0.180     n/a     n/a     n/a\r\n" + "\r\n"
            + "  Query\r\n" + "  Frame  MatID  Length  Eff.Length     E    S W   T  X   E2     S2\r\n"
            + "   +0      0      132       132       7.8  65 3  12 22  0.39    31\r\n"
            + "                                                    30  0.43    33\r\n" + "\r\n" + "\r\n"
            + "Statistics:\r\n" + "\r\n" + "  Database:  /ebi/services/idata/v1848/blastdb/pdb\r\n"
            + "   Title:  pdb\r\n" + "   Posted:  11:00:12 PM BST May 9, 2007\r\n"
            + "   Created:  11:00:11 PM BST May 9, 2007\r\n" + "   Format:  XDF-1\r\n"
            + "   # of letters in database:  22,737,184\r\n" + "   # of sequences in database:  94,877\r\n"
            + "   # of database sequences satisfying E:  121\r\n"
            + "  No. of states in DFA:  532 (113 KB)\r\n" + "  Total size of DFA:  231 KB (2097 KB)\r\n"
            + "  Time to generate neighborhood:  0.00u 0.00s 0.00t   Elapsed:  00:00:00\r\n"
            + "  No. of threads or processors used:  8\r\n"
            + "  Search cpu time:  0.95u 0.13s 1.08t   Elapsed:  00:00:00\r\n"
            + "  Total cpu time:  0.95u 0.13s 1.08t   Elapsed:  00:00:00\r\n"
            + "  Start:  Tue May 22 09:55:46 2007   End:  Tue May 22 09:55:46 2007\r\n"
            + "WARNINGS ISSUED:  2";

}
