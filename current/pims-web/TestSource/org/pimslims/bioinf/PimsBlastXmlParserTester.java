package org.pimslims.bioinf;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.presentation.bioinf.PimsBlastXmlParser;

public class PimsBlastXmlParserTester extends TestCase {

    /**
     * 
     */
    private static final String FOOTER = "</SequenceSimilaritySearchResult>\r\n" + "</EBIApplicationResult>";

    /**
     * 
     */
    private static final String HEADER =
        "<?xml version=\"1.0\"?>\r\n"
            + "<EBIApplicationResult xmlns=\"http://www.ebi.ac.uk/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.ebi.ac.uk/schema/ApplicationResult.xsd\">\r\n"
            + "<Header>\r\n"
            + "	<program name=\"WU-blastp\" version=\"2.0MP-WashU [04-May-2006]\" citation=\"PMID:12824421\"/>\r\n"
            + "	<commandLine command=\"/ebi/extserv/bin/wu-blast/blastp &quot;pdb&quot; /ebi/extserv/blast-work/interactive/blast-20070608-12561956.input E=1.0 B=3 V=3 -mformat=7,/ebi/extserv/blast-work/interactive/blast-20070608-12561956_app.xml -mformat=1 -matrix BLOSUM62 -sump  -echofilter -filter none -cpus 8 -sort_by_pvalue -putenv=&apos;WUBLASTMAT=/ebi/extserv/bin/wu-blast/matrix&apos; -putenv=&quot;WUBLASTDB=$IDATA_CURRENT/blastdb&quot; -putenv=&apos;WUBLASTFILTER=/ebi/extserv/bin/wu-blast/filter&apos; \"/>\r\n"
            + "	<parameters>\r\n"
            + "		<sequences total=\"1\">\r\n"
            + "			<sequence number=\"1\" name=\"Sequence\" type=\"p\" length=\"132\"/>\r\n"
            + "		</sequences>\r\n"
            + "		<databases total=\"1\" sequences=\"96352\" letters=\"23073089\">\r\n"
            + "			<database number=\"1\" name=\"pdb\" type=\"p\" created=\"2007-06-06T23:00:21+01:00\"/>\r\n"
            + "		</databases>\r\n"
            + "		<scores>2</scores>\r\n"
            + "		<alignments>2</alignments>\r\n"
            + "		<matrix>BLOSUM62</matrix>\r\n"
            + "		<expectationUpper>1</expectationUpper>\r\n"
            + "		<statistics>sump</statistics>\r\n"
            + "		<filter>none</filter>\r\n"
            + "	</parameters>\r\n"
            + "	<timeInfo start=\"2007-06-08T12:56:22+01:00\" end=\"2007-06-08T12:56:22+01:00\" search=\"PT\"/>\r\n"
            + "</Header>\r\n" + "<SequenceSimilaritySearchResult>\r\n";

    /**
     * TODO the format has changed
     */
    private static final String THREE_HITS =
        "	<hits total=\"2\">\r\n"
            + "		<hit number=\"1\" database=\"pdb\" id=\"1FLE_I\" length=\"57\" description=\"mol:protein length:57  ELAFIN\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>143</score>\r\n"
            + "					<bits>55.4</bits>\r\n"
            + "					<expectation>1.4e-10</expectation>\r\n"
            + "					<probability>1.4e-10</probability>\r\n"
            + "					<identity>48</identity>\r\n"
            + "					<positives>55</positives>\r\n"
            + "					<querySeq start=\"74\" end=\"129\">DPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP</querySeq>\r\n"
            + "					<pattern>+PV  P  T  KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P</pattern>\r\n"
            + "					<matchSeq start=\"3\" end=\"56\">EPVKGPVST--KPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "				<alignment number=\"2\">\r\n"
            + "					<score>94</score>\r\n"
            + "					<bits>38.1</bits>\r\n"
            + "					<expectation>2.1e-05</expectation>\r\n"
            + "					<probability>2.1e-05</probability>\r\n"
            + "					<identity>39</identity>\r\n"
            + "					<positives>47</positives>\r\n"
            + "					<querySeq start=\"29\" end=\"75\">SFKAGVCPPKKSAQCLRYKKPE-CQSDWQCPGKKRCCPDTCGIKCLDP</querySeq>\r\n"
            + "					<pattern>S K G CP     +C     P  C  D  CPG K+CC  +CG+ C  P</pattern>\r\n"
            + "					<matchSeq start=\"10\" end=\"56\">STKPGSCPIIL-IRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "			</alignments>\r\n"
            + "		</hit>\r\n"
            + "		<hit number=\"2\" database=\"pdb\" id=\"1UDK_A\" length=\"51\" description=\"mol:protein length:51  Nawaprin\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>79</score>\r\n"
            + "					<bits>32.9</bits>\r\n"
            + "					<expectation>0.00082</expectation>\r\n"
            + "					<probability>0.00082</probability>\r\n"
            + "					<identity>37</identity>\r\n"
            + "					<positives>47</positives>\r\n"
            + "					<querySeq start=\"31\" end=\"76\">KAGVCPPKKSA-QCLRYKKPECQSDWQCPGKKRCCPDTCG-IKCLDPV</querySeq>\r\n"
            + "					<pattern>K+G CP        L   K  C SD  CP  ++CC + CG + C  PV</pattern>\r\n"
            + "					<matchSeq start=\"3\" end=\"50\">KSGSCPDMSMPIPPLGICKTLCNSDSGCPNVQKCCKNGCGFMTCTTPV</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "				<alignment number=\"2\">\r\n"
            + "					<score>65</score>\r\n"
            + "					<bits>27.9</bits>\r\n"
            + "					<expectation>0.032</expectation>\r\n"
            + "					<probability>0.032</probability>\r\n"
            + "					<identity>34</identity>\r\n"
            + "					<positives>38</positives>\r\n"
            + "					<querySeq start=\"85\" end=\"130\">KPGKCPVTYGQCLMLNPP-----NFCEMDGQCKRDLKCCMGMCG-KSCVSPV</querySeq>\r\n"
            + "					<pattern>K G CP       M  PP       C  D  C    KCC   CG  +C +PV</pattern>\r\n"
            + "					<matchSeq start=\"3\" end=\"50\">KSGSCP----DMSMPIPPLGICKTLCNSDSGCPNVQKCCKNGCGFMTCTTPV</matchSeq>\r\n"
            + "				</alignment>\r\n" + "			</alignments>\r\n" + "		</hit>\r\n" + "	</hits>\r\n";

    private static final String QUERY1 = "DPVDTPNPTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP";

    private static final String PATTERN1 = "+PV  P  T  KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P";

    private static final String MATCH1 = "EPVKGPVST--KPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP";

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PimsBlastXmlParserTester.class);

    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser.parseXmlString()' using output from WSWUBlast
     */
    public final void testParseWUBlastXmlString() {
        final List testHits = PimsBlastXmlParser.parseXmlString(PimsBlastXmlParserTester.TEST_XML);
        Assert.assertNotNull(testHits);
        Assert.assertEquals(testHits.size(), 3);
        testHits.get(0);
        /*
         * System.out.println("---Data from testParseWUBlastXmlString"); System.out.println("The header for
         * testWUBlastXml Parser contains: "); System.out.println("Database searched for testWUBlastXml Parser
         * is " +bhb.getDatabaseSearched()); System.out.println("Search date for testWUBlastXml Parser is "
         * +bhb.getSearchDate()); System.out.println("Target for testWUBlastXml Parser is "
         * +bhb.getTargetId()); System.out.println("Target length for testWUBlastXml Parser is "
         * +bhb.getTargetLen());
         */// HitBean details
        final BlastHitBean bHitb = (BlastHitBean) testHits.get(1);
        // System.out.println("The contents of the First BlastHitBean:");
        // System.out.println("The first hit is number " + bHitb.getHitNum());
        Assert.assertEquals(bHitb.getHitNum(), 1);
        // System.out.println("The first hit ID is " + bHitb.getHitDbId());
        Assert.assertEquals(bHitb.getHitDbId(), "1FLE_I");
        // System.out.println("The database for the first hit is " +
        // bHitb.getDatabase());
        Assert.assertEquals(bHitb.getDatabase(), "pdb");
        // System.out.println("The pdb url is "+
        // bHitb.getDbBaseURL()+bHitb.getHitURLid());
        Assert.assertEquals("http://www.ebi.ac.uk/pdbe-srv/view/entry/1FLE", bHitb.getDbBaseURL()
            + bHitb.getHitURLid());
        // System.out.println("The length of the first hit is " +
        // bHitb.getHitLength());
        Assert.assertEquals(bHitb.getHitLength(), 57);
        // System.out.println("The description for the first hit is " +
        // bHitb.getDescription());
        Assert.assertEquals(bHitb.getDescription(), "mol:protein length:57  ELAFIN");
        // System.out.println("The number of alignments for this hit is "+
        // bHitb.getNoOfAlignments());
        Assert.assertEquals(bHitb.getNoOfAlignments(), 2);
        Assert.assertEquals(bHitb.getNoOfAlignments(), bHitb.getAlignmentBeans().size());
        // System.out.println("Number of alignment beans is " +
        // bHitb.getAlignmentBeans().size());

        // First AlignmentBean details
        // System.out.println("Details about the First alignment:");
        assert bHitb.getAlignmentBeans().get(0).getHitNum() == 1;
        assert bHitb.getAlignmentBeans().get(0).getAlignmentNo() == 1;
        assert bHitb.getAlignmentBeans().get(0).getScore() == 143;
        assert bHitb.getAlignmentBeans().get(0).getBits() == 55.4;
        assert bHitb.getAlignmentBeans().get(0).getExpect() == 1.4e-10;
        assert bHitb.getAlignmentBeans().get(0).getProbability() == 1.4e-10;
        assert bHitb.getAlignmentBeans().get(0).getIdentity() == 48;
        assert bHitb.getAlignmentBeans().get(0).getPositives() == 55;
        Assert.assertEquals(bHitb.getAlignmentBeans().get(0).getQuerySeq(), PimsBlastXmlParserTester.QUERY1);
        assert bHitb.getAlignmentBeans().get(0).getQuerySeqStart() == 74;
        assert bHitb.getAlignmentBeans().get(0).getQuerySeqEnd() == 129;
        Assert.assertEquals(bHitb.getAlignmentBeans().get(0).getPattern(), PimsBlastXmlParserTester.PATTERN1);
        Assert.assertEquals(bHitb.getAlignmentBeans().get(0).getMatchSeq(), PimsBlastXmlParserTester.MATCH1);
        assert bHitb.getAlignmentBeans().get(0).getMatchSeqStart() == 3;
        assert bHitb.getAlignmentBeans().get(0).getMatchSeqEnd() == 56;
        assert bHitb.getAlignmentBeans().get(0).getAlignmentLen() == 56;
        assert bHitb.getAlignmentBeans().get(0).getGaps() == 2;
        assert bHitb.getAlignmentBeans().get(0).getNumIdentities() == 27;
        assert bHitb.getAlignmentBeans().get(0).getNumPositives() == 31;
        /*
         * System.out.println("Hit number: "+ bHitb.getAlignmentBeans().get(0).getHitNum());
         * System.out.println("Alignment number: "+ bHitb.getAlignmentBeans().get(0).getAlignmentNo());
         * System.out.println("Score: "+ bHitb.getAlignmentBeans().get(0).getScore());
         * System.out.println("Bits: "+ bHitb.getAlignmentBeans().get(0).getBits());
         * System.out.println("Expect: "+ bHitb.getAlignmentBeans().get(0).getExpect());
         * System.out.println("Probability: "+ bHitb.getAlignmentBeans().get(0).getProbability());
         * System.out.println("Identities: "+ bHitb.getAlignmentBeans().get(0).getIdentity());
         * System.out.println("Positives: "+ bHitb.getAlignmentBeans().get(0).getPositives());
         * System.out.println("The query sequence is:
         * "+bHitb.getAlignmentBeans().get(0).getQuerySeqStart()+SPACE
         * +bHitb.getAlignmentBeans().get(0).getQuerySeq()+SPACE+bHitb.getAlignmentBeans().get(0).getQuerySeqEnd());
         * System.out.println("The pattern is: "+bHitb.getAlignmentBeans().get(0).getPattern());
         * System.out.println("The match sequence is: "+bHitb.getAlignmentBeans().get(0).getMatchSeqStart()+" "
         * +bHitb.getAlignmentBeans().get(0).getMatchSeq()+SPACE+bHitb.getAlignmentBeans().get(0).getMatchSeqEnd());
         * System.out.println("The alignment length is "+ bHitb.getAlignmentBeans().get(0).getAlignmentLen());
         * System.out.println("The number of identities is " +
         * bHitb.getAlignmentBeans().get(0).getNumIdentities()); System.out.println("The number of positives
         * is " + bHitb.getAlignmentBeans().get(0).getNumPositives()); System.out.println("");
         */
    }

    public final void testParseSgtHit() {
        final List testHits = PimsBlastXmlParser.parseXmlString(PimsBlastXmlParserTester.SGT_HIT);
        Assert.assertNotNull(testHits);
        Assert.assertEquals(2, testHits.size());
        testHits.get(0);
        // HitBean details
        final BlastHitBean bHitb = (BlastHitBean) testHits.get(1);
        Assert.assertEquals(bHitb.getHitNum(), 1);
        Assert.assertEquals("SGT", bHitb.getHitDbId()); // should this be InputParams.TARGETDB
        Assert.assertEquals(bHitb.getDatabase(), "pdb");
        Assert.assertEquals("http://www.ebi.ac.uk/pdbe-srv/view/entry/SGT", bHitb.getDbBaseURL()
            + bHitb.getHitURLid());
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser.parseXmlString()' using output from WSNCBIBlast
     */
    public final void testParseNCBIBlastXmlString() {
        final List testHits = PimsBlastXmlParser.parseXmlString(PimsBlastXmlParserTester.NCBI_XML);
        Assert.assertNotNull(testHits);
        Assert.assertEquals(testHits.size(), 4);
        testHits.get(0);
        /*
         * System.out.println("---Data from testParseNCBIBlastXmlString"); System.out.println("The NCBI header
         * contains: "); System.out.println("NCBI Database searched is " +bhb.getDatabaseSearched());
         * System.out.println("NCBI Search date is " +bhb.getSearchDate()); System.out.println("NCBI Target is "
         * +bhb.getTargetId()); System.out.println("NCBI Target length is " +bhb.getTargetLen());
         * System.out.println("");
         */
        // HitBean details
        final BlastHitBean bHitb = (BlastHitBean) testHits.get(1);
        Assert.assertEquals(bHitb.getAlignmentBeans().size(), 2);
        assert bHitb.getAlignmentBeans().get(0).getAlignmentLen() == 49;
        Assert.assertEquals(bHitb.getAlignmentBeans().get(1).getAlignmentRows().size(), 3);
        Assert.assertEquals(bHitb.getAlignmentBeans().get(0).getStrand(), "plus");

        final String testPattern = bHitb.getAlignmentBeans().get(1).getAlignmentRows().get(1);
        // System.out.println("First alignment pattern is: " +testPattern);
        assert testPattern.equals("              + K G CP        +       C  D  CP G K+CC  +C G+ C  P");
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser.parseXmlString()' using output from WSNCBIBlast
     */
    public final void testParseNoHitsXmlString() {
        final List testHits = PimsBlastXmlParser.parseXmlString(PimsBlastXmlParserTester.NO_HITS);
        Assert.assertNotNull(testHits);
        Assert.assertEquals(testHits.size(), 1);
        testHits.get(0);
        /*
         * System.out.println("---Data from testNoHitsXmlString"); System.out.println("The header contains:
         * "); System.out.println("Database searched is " +bhb.getDatabaseSearched());
         * System.out.println("Search date is " +bhb.getSearchDate()); System.out.println("Target is "
         * +bhb.getTargetId()); System.out.println("Target length is " +bhb.getTargetLen());
         * System.out.println("");
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessAlignment() {
        // List<String> seqArray = new java.util.ArrayList<String>();
        // System.out.println("---Data from testProcessAlignment");
        final String query =
            "VGVIMGSTSDW---ETMKYACDILDELNIPYEKKVVSAHRTPDYMFEYAETARERGL-KV"
                + "IIAGAGGAAHLPGMVAAKTNLPVIGVPVQSKALNGLDSLLSIVQMPGGVPVATVAIGKAG" + "STNAGLLAAQILG";
        final String pattern =
            "V V+MGSTSD    E +K AC       IP E +V SAH+ PD            G+  V"
                + " +A AG +  L  +++  T  PVI  P  +    G+  + S +++P G+  +TV +   G" + "S      AAQI G";
        final String match =
            "VVVLMGSTSDLGHCEKIKKACG---NFGIPCELRVTSAHKGPDETLRIKAEYEGDGIPTV"
                + "FVAVAGRSNGLGPVMSGNTAYPVISCPPLTPDW-GVQDVWSSLRLPSGLGCSTV-LSPEG" + "SAQ---FAAQIFG";
        final int quStart = 5;
        final int maStart = 268;
        final int quEnd = 133;
        final int maEnd = 392;
        Assert.assertEquals(query.length(), 133);
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals(
            "target:    5  VGVIMGSTSD W---ETMKYA CDILDELNIP YEKKVVSAHR TPDYMFEYAE TARERGL-KV    60", seqArray
                .get(0));
        // System.out.println("The long alignment has "+seqArray.size()+ "
        // rows");
        // System.out.println("The long alignment looks like this:");
        /*
         * for(final Iterator i = seqArray.iterator(); i.hasNext();){ final String row = (String)i.next();
         * System.out.println(row); }
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessAlignment2() {
        // List<String> seqArray = new java.util.ArrayList<String>();
        // System.out.println("---Data from CAB64235");
        final String query =
            "PECQSDWQCPGKKRCCPDTCGIKCLDPVDT------------------------------"
                + "--PNP-TRRKPG-----KCP----VTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSC";
        final String pattern =
            "P+C      P   +CC D C   C  P DT                              "
                + "  PNP T+ + G      CP    V    C+   PP+  E+D    +  + C G+C K+C";
        final String match =
            "PQCNGHCFGPNPNQCCHDECAGGCSGPQDTDCFACRHFNDSGACVPRCPQPLVYNKLTFQ"
                + "LEPNPHTKYQYGGVCVASCPHNFVVDQTSCVRACPPDKMEVDKNGLKMCEPCGGLCPKAC";
        final int quStart = 49;
        final int maStart = 193;
        final int quEnd = 126;
        final int maEnd = 312;
        Assert.assertEquals(query.length(), 120);
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals(
            "target:   49  PECQSDWQCP GKKRCCPDTC GIKCLDPVDT ---------- ---------- ----------    78", seqArray
                .get(0));
        /*
         * System.out.println("The long alignment has "+seqArray.size()+ " rows"); System.out.println("The
         * long alignment looks like this:"); for(final Iterator i = seqArray.iterator(); i.hasNext();){ final
         * String row = (String)i.next(); System.out.println(row); }
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessShortAlignment() {
        //System.out.println("---Data from testProcessShortAlignment");
        final String query = PimsBlastXmlParserTester.QUERY1;
        final String pattern = PimsBlastXmlParserTester.PATTERN1;
        final String match = PimsBlastXmlParserTester.MATCH1;
        final int quStart = 74;
        final int maStart = 3;
        final int quEnd = 129;
        final int maEnd = 56;
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals(
            "target:   74  DPVDTPNPTR RKPGKCPVTY GQCLMLNPPN FCEMDGQCKR DLKCCMGMCG KSCVSP   129", seqArray
                .get(0));
        // System.out.println("The short alignment has "+seqArray.size()+ " rows
        // ");
        // System.out.println("The short alignment looks like this:");
        /*
         * for(final Iterator i = seqArray.iterator(); i.hasNext();){ final String row = (String)i.next();
         * System.out.println(row); }
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testShortPatternAlignment() {
        // System.out.println("---Data from testShortPatternAlignment");
        final String query = "SFKAGVCPPKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGIKCLDP";
        final String pattern = " K G CP       +      C  D  CPG K+CC  +CG+ C  P";
        final String match = "STKPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP";
        final int quStart = 29;
        final int maStart = 10;
        final int quEnd = 75;
        final int maEnd = 56;
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals("target:   29  SFKAGVCPPK KSAQCLRYKK PECQSDWQCP GKKRCCPDTC GIKCLDP   75",
            seqArray.get(0));
        // assertEquals("target: 29 SFKAGVCPP KKSAQCLRYK KPECQSDWQC PGKKRCCPDT
        // CGIKCLDP 75", seqArray.get(0));
        /*
         * System.out.println("The alignment has "+seqArray.size()+ " rows"); System.out.println("The
         * alignment looks like this:"); for(final Iterator i = seqArray.iterator(); i.hasNext();){ final
         * String row = (String)i.next(); System.out.println(row); }
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment' This is to test errors in
     * TargetDB Blast which cause PIMS-1159 The xml contsins errors such as missing residues in the pattern
     * string
     */
    public final void testBadPatternAlignment() {
        // System.out.println("---Data from testBadPatternAlignment");
        final String query =
            "SSITIYMFVRNTIQYPYTGMASNLTSTYGQYDNGENVFLYYNVNPTSTSAWT------ISGSAGLTTSAP--SGSYFSSPNAYYAN-SAVGDYMYTQIPKLNTNVIITFD--VYTTGLGDLFFLVNSSG";
        final String pattern =
            "+ TI+ +   +I Y    + S+ T   G Y N +     Y +NP  T  WT      ISSA + T      GSY    N Y  N      + Y     ++ + +I  D  +Y     D + +N  G";
        final String match =
            "TNTTIWNYTTGSINYASPVIGSDGTIYIGSYGNSK----LYAINPDGTLKWTYNTGGKISGSAAIGTDGTIYIGSY--DKNIYAINPDGTLKWAYNTGGSISGSAVIGSDGTIYIGSSDDKVYAINPDG";
        final int quStart = 337;
        final int maStart = 318;
        final int quEnd = 454;
        final int maEnd = 440;
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        assert seqArray.size() == 0;
        /*
         * System.out.println("The alignment has "+seqArray.size()+ " rows"); System.out.println("The
         * alignment looks like this:"); for(final Iterator i = seqArray.iterator(); i.hasNext();){ final
         * String row = (String)i.next(); System.out.println(row); }
         */
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessDNASeqAlignment() {
        final String query = "atccctagacatagccctgacaagctcggaccaaga";
        final String pattern = "||||| || |||||||||||||| ||| | ||| ||";
        final String match = "atccccagccatagccctgacaacctcaggccacga";
        final int quStart = 16;
        final int maStart = 74014;
        final int quEnd = 51;
        final int maEnd = 73979;
        Assert.assertEquals(query.length(), 36);
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals("hit:   74014  atccccagcc atagccctga caacctcagg ccacga   73979", seqArray.get(2));
        //assertEquals("hit:   73944  atccccagcc atagccctga caacctcagg ccacga   73979", seqArray.get(2));
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessDNASeqAlignment2() {
        final String query = "agatagacagatccctagacatagc";
        final String pattern = "|||||||||||||||||| || |||";
        final String match = "agatagacagatccctagccaaagc";
        final int quStart = 6;
        final int maStart = 22666;
        final int quEnd = 30;
        final int maEnd = 22690;
        Assert.assertEquals(query.length(), 25);
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals("target:    6  agatagacag atccctagac atagc   30", seqArray.get(0));
    }

    /*
     * Test method for 'org.pimslims.bioinf.PimsBlastXmlParser().processAlignment'
     */
    public final void testProcessDNASeqAlignment3() {
        final String query =
            "atcatatacaaattacttgttttcaaaaggagaagataaaattttaaagaaaattggtgaggaatgcacggaagtaattatcgcatctaaaaataatgataaagaagagctggtgaaagaaatggttgatgtattgtatcactgcttcgtgttattagccgaaaaaaatataccattagaagatattatggaggaa";
        final String pattern =
            "||| ||||| |||| | | | ||||||| |||||||||| |||| ||| | || || ||| |||||||||||||| ||| | ||||||| |||| |||||||| | | |||| ||| |||||| ||| | | || ||||||| ||||| | | ||||| ||||||| || ||| | ||||| ||||||| || |||";
        final String match =
            "atcctatactgattatctatatcaaaaaggattagataaaattctaaaaaaagtgggagaagaaagcacggaagtaattctcggcgccaaaaata---ataatgaagagctaatttatgaaacttcagatttattgtttcatttaatggtcttattagttgaaaagggtgtcagtttagatgatattaaagatgaa";
        final int quStart = 66;
        final int maStart = 1549449;
        final int quEnd = 261;
        final int maEnd = 1549257;
        Assert.assertEquals(query.length(), 196);
        final List seqArray =
            BioinfUtility.processAlignment(query, pattern, match, quStart, maStart, quEnd, maEnd);
        Assert.assertNotNull(seqArray);
        Assert.assertEquals(
            "target:   66  atcatataca aattacttgt tttcaaaagg agaagataaa attttaaaga aaattggtga    125",
            seqArray.get(0));
        Assert.assertEquals(
            "hit: 1549449  atcctatact gattatctat atcaaaaagg attagataaa attctaaaaa aagtgggaga    1549389",
            seqArray.get(2));
        Assert.assertEquals(
            "hit: 1549388  agaaagcacg gaagtaattc tcggcgccaa aaata---at aatgaagagc taatttatga    1549326",
            seqArray.get(5));
    }

    private static final String TEST_XML =
        PimsBlastXmlParserTester.HEADER + PimsBlastXmlParserTester.THREE_HITS
            + PimsBlastXmlParserTester.FOOTER;

    private static final String NCBI_XML =
        "<?xml version=\"1.0\"?>\r\n"
            + "<EBIApplicationResult xmlns=\"http://www.ebi.ac.uk/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.ebi.ac.uk/schema/ApplicationResult.xsd\">\r\n"
            + "<Header>\r\n"
            + "	<program name=\"NCBI-blastp\" version=\"2.2.15 [Oct-15-2006]\" citation=\"PMID:9254694\"/>\r\n"
            + "	<commandLine command=\"/ebi/extserv/bin/ncbi-blast/blastall -p blastp -d $IDATA_CURRENT/blastdb/pdb -i /ebi/extserv/blast-work/interactive/blast-20070524-14405797.input -M BLOSUM62 -b 5 -v 5 -e 1.0 -X 0 -G 11 -E 1 -a 8 -L 1,132 -m 0 -gt -F F \"/>\r\n"
            + "	<parameters>\r\n"
            + "		<sequences total=\"1\">\r\n"
            + "			<sequence number=\"1\" name=\"Sequence\" type=\"p\" length=\"132\"/>\r\n"
            + "		</sequences>\r\n"
            + "		<databases total=\"1\" sequences=\"95624\" letters=\"22913533\">\r\n"
            + "			<database number=\"1\" name=\"pdb\" type=\"p\" created=\"2007-05-23T23:00:00+01:00\"/>\r\n"
            + "		</databases>\r\n"
            + "		<gapOpen>11</gapOpen>\r\n"
            + "		<gapExtension>1</gapExtension>\r\n"
            + "	</parameters>\r\n"
            + "</Header>\r\n"
            + "<SequenceSimilaritySearchResult>\r\n"
            + "	<hits total=\"3\">\r\n"
            + "		<hit number=\"1\" database=\"pdb\" id=\"2REL_\" length=\"57\" description=\"mol:protein length:57  R-ELAFIN\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>141</score>\r\n"
            + "					<bits>58.9</bits>\r\n"
            + "					<expectation>1e-09</expectation>\r\n"
            + "					<identity>48</identity>\r\n"
            + "					<positives>55</positives>\r\n"
            + "                 <strand>plus/plus</strand>\r\n"
            + "					<querySeq start=\"81\" end=\"129\">PTRRKPGKCPVTYGQCLMLNPPNFCEMDGQCKRDLKCCMGMCGKSCVSP</querySeq>\r\n"
            + "					<pattern>P   KPG CP+   +C MLNPPN C  D  C    KCC G CG +C  P</pattern>\r\n"
            + "					<matchSeq start=\"8\" end=\"56\">PVSTKPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "				<alignment number=\"2\">\r\n"
            + "					<score>88</score>\r\n"
            + "					<bits>38.5</bits>\r\n"
            + "					<expectation>0.002</expectation>\r\n"
            + "					<identity>36</identity>\r\n"
            + "					<positives>44</positives>\r\n"
            + "					<querySeq start=\"29\" end=\"75\">SFKAGVCPPKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGIKCLDP</querySeq>\r\n"
            + "					<pattern> K G CP       +      C  D  CPG K+CC  +CG+ C  P</pattern>\r\n"
            + "					<matchSeq start=\"10\" end=\"56\">STKPGSCPIILIRCAMLNPPNRCLKDTDCPGIKKCCEGSCGMACFVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "			</alignments>\r\n"
            + "		</hit>\r\n"
            + "		<hit number=\"2\" database=\"pdb\" id=\"1ZLG_A\" length=\"680\" description=\"mol:protein length:680  Anosmin 1\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>84</score>\r\n"
            + "					<bits>37.0</bits>\r\n"
            + "					<expectation>0.004</expectation>\r\n"
            + "					<identity>33</identity>\r\n"
            + "					<positives>45</positives>\r\n"
            + "					<querySeq start=\"31\" end=\"88\">KAGVCP-PKKSAQCLRYKKPECQSDWQCPGKKRCCPDTCGIKCLDPVDTPNPTRRKPGK</querySeq>\r\n"
            + "					<pattern>K G CP P+K++         C+ D +C G K+CC + CG  C  P         KP K</pattern>\r\n"
            + "					<matchSeq start=\"107\" end=\"165\">KQGDCPAPEKASGFAAACVESCEVDNECSGVKKCCSNGCGHTCQVPKTLYKGVPLKPRK</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "				<alignment number=\"2\">\r\n"
            + "					<score>71</score>\r\n"
            + "					<bits>32.0</bits>\r\n"
            + "					<expectation>0.14</expectation>\r\n"
            + "					<identity>44</identity>\r\n"
            + "					<positives>56</positives>\r\n"
            + "					<querySeq start=\"105\" end=\"129\">CEMDGQCKRDLKCCMGMCGKSCVSP</querySeq>\r\n"
            + "					<pattern>CE+D +C    KCC   CG +C  P</pattern>\r\n"
            + "					<matchSeq start=\"128\" end=\"152\">CEVDNECSGVKKCCSNGCGHTCQVP</matchSeq>\r\n"
            + "				</alignment>\r\n"
            + "			</alignments>\r\n"
            + "		</hit>\r\n"
            + "		<hit number=\"3\" database=\"pdb\" id=\"1UDK_A\" length=\"51\" description=\"mol:protein length:51  Nawaprin\">\r\n"
            + "			<alignments total=\"1\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>73</score>\r\n"
            + "					<bits>32.7</bits>\r\n"
            + "					<expectation>0.084</expectation>\r\n"
            + "					<identity>37</identity>\r\n"
            + "					<positives>47</positives>\r\n"
            + "					<querySeq start=\"31\" end=\"76\">KAGVCPPKKSA-QCLRYKKPECQSDWQCPGKKRCCPDTCG-IKCLDPV</querySeq>\r\n"
            + "					<pattern>+G CP        L   K  C SD  CP  ++CC + CG + C  PV</pattern>\r\n"
            + "					<matchSeq start=\"3\" end=\"50\">KSGSCPDMSMPIPPLGICKTLCNSDSGCPNVQKCCKNGCGFMTCTTPV</matchSeq>\r\n"
            + "				</alignment>\r\n" + "			</alignments>\r\n" + "		</hit>\r\n" + "	</hits>\r\n"
            + PimsBlastXmlParserTester.FOOTER;

    private static final String SGT_HIT =
        PimsBlastXmlParserTester.HEADER
            + "    <hits total=\"1\">\r\n"
            + "<hit number=\"1\" database=\"pdb\" id=\"SGT\" length=\"576\" \r\n"
            + "description=\"RSGI####### Soluble\">\r\n"
            + "            <alignments total=\"1\">\r\n"
            + "                <alignment number=\"1\">\r\n"
            + "                    <score>933</score>\r\n"
            + "                    <bits>363</bits>\r\n"
            + "                    <expectation>1e-100</expectation>\r\n"
            + "                    <identity>36</identity>\r\n"
            + "                    <positives>51</positives>\r\n"
            + "                    <querySeq start=\"147\" \r\n"
            + "end=\"817\">EDLRSHAWYHGRIPRQVSENLVQRDGDFLVRDSLSSPGNFVLTCQWKNLAQHFKINRTVLRLSEAYSRVQYQFEMESFDSIPGLVRCYVGNRRPISQQSGAIIFQPINRTVPLRCLEEHYGTSPGQAREGSLTKGRPDVAKRLSLTMGGVQAREQNLPRGNLLRNKEKSGSQPACLDHMQDRRALSLKAHQSESYLPIGCKLPPQSSGVDTSPCPNSPVFRTGSEPAL--SPAVVRRVSSDARAGEALRGSDSQLCPKPPPKPCKVPFLKVPSSPSAWLNSEANYCELNPAFATGCGRGAKLPSCAQGSHTELLTAKQNEAPGPRNSGVNYLILDDDDRERPWEPAAAQMEKGQWDKGEFVTPLLETVSSFRPNEFESKFLPPENKPLETAMLKRAKELFTNNDPKVIAQHVLSMDCRVARILGVSEEMRRNMGVSSGLELITLPHGHQLRLDIIERHNTMAIGIAVDILGCTGTLEDRAATLSKIIQVAVELK-DSMGDLYSFSALMKALEMPQITRLEKTWTALRHQYTQTAILYEKQLKPFSKLLHEGRESTCVPPNNVSVPLLMPLVTLMERQAVTFEGTDMWEKNDQSCEIMLNHLATARFMAEAADSYRMNAERILAGFQPDEEMNEICKTEFQMRLLWGSKGAQVNQTERYEKFNQILTALSRKLEP</querySeq>\r\n"
            + "                    <pattern>EDL    WYHG + RQ +E L+Q++GDFLVR S S  GN \r\n"
            + "V++C+W+  A HF++ R  LR         +Q E E F SIP LV  Y+  RRP+SQ +GA++ +P+    \r\n"
            + "PLR              E +L  G   +                       LR ++ S SQPA L HM   \r\n"
            + "R                       +G++ S  P S + RT S+P L  +PA +  V+      ++LR SD \r\n"
            + "QL  K P KP + P  ++P +          YCEL P          ++PS  QG      T+     P \r\n"
            + "P               E PW  A    E+   +   F  P  E   SF P++  S  L P+N+PLE  +L   \r\n"
            + "+ LF  + P   A H+L +DC+   +LGV+ + R NMGVSSGLEL+TLPHGH LRL+++ERH T+A+  A+ \r\n"
            + "+LGC+G LE+RAA L  ++++A+ L+  + GDL   +A+M AL MPQ++RLE TW  LR  +T+ A+ \r\n"
            + "+E++LKP  + L EG    C  P  V++P + P+V L+       EG ++    D+SCE +L  L  AR \r\n"
            + "M   A  +R  A + L GF+P+ E+ E   T F  RLLWGS+GA   + ER+EKF ++L  \r\n"
            + "LS++LEP</pattern>\r\n"
            + "                    <matchSeq start=\"8\" \r\n"
            + "end=\"574\">EDLAGQPWYHGLLSRQKAEALLQQNGDFLVRASGSRGGNPVISCRWRGSALHFEVFRVALRPRPGRPTALFQLEDEQFPSIPALVHSYMTGRRPLSQATGAVVSRPVTWQGPLR----------RSFSEDTLMDGPARIEP---------------------LRARKWSNSQPADLAHMGRSRE--------------------DPAGMEASTMPISALPRTSSDPVLLKAPAPLGTVA------DSLRASDGQLQAKAPTKPPRTPSFELPDASER----PPTYCELVP----------RVPS-VQG------TSPSQSCPEP---------------EAPWWEAEEDEEE---ENRCFTRPQAEI--SFCPHDAPSCLLGPQNRPLEPQVLHTLRGLFLEHHPGSTALHLLLVDCQATGLLGVTRDQRGNMGVSSGLELLTLPHGHHLRLELLERHQTLALAGALAVLGCSGPLEERAAALRGLVELALALRPGAAGDLPGLAAVMGALLMPQVSRLEHTWRQLRRSHTEAALAFEQELKPLMRALDEG-AGPC-DPGEVALPHVAPMVRLL-------EGEEVAGPLDESCERLLRTLHGARHMVRDAPKFRKVAAQRLRGFRPNPELREALTTGFVRRLLWGSRGAGAPRAERFEKFQRVLGVLSQRLEP</matchSeq>\r\n"
            + "                </alignment>\r\n" + "            </alignments>\r\n" + "        </hit>"
            + " </hits>\r\n" + PimsBlastXmlParserTester.FOOTER;

    private static final String NO_HITS =
        "<?xml version=\"1.0\"?>\r\n"
            + "<EBIApplicationResult xmlns=\"http://www.ebi.ac.uk/schema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.ebi.ac.uk/schema/ApplicationResult.xsd\">\r\n"
            + "<Header>\r\n"
            + "	<program name=\"NCBI-blastp\" version=\"2.2.15 [Oct-15-2006]\" citation=\"PMID:9254694\"/>\r\n"
            + "	<commandLine command=\"/ebi/extserv/bin/ncbi-blast/blastall -p blastp -d $IDATA_CURRENT/blastdb/pdb -i /ebi/extserv/blast-work/interactive/blast-20070613-13524815.input -M BLOSUM62 -b 5 -v 5 -e 1.0 -X 0 -G 11 -E 1 -a 8 -L 1,6 -m 0 -gt -F F \"/>\r\n"
            + "	<parameters>\r\n" + "		<sequences total=\"1\">\r\n"
            + "			<sequence number=\"1\" name=\"Sequence\" type=\"p\" length=\"6\"/>\r\n"
            + "		</sequences>\r\n" + "		<databases total=\"1\" sequences=\"96352\" letters=\"23073089\">\r\n"
            + "			<database number=\"1\" name=\"pdb\" type=\"p\" created=\"2007-06-06T23:00:00+01:00\"/>\r\n"
            + "		</databases>\r\n" + "		<gapOpen>11</gapOpen>\r\n" + "		<gapExtension>1</gapExtension>\r\n"
            + "	</parameters>\r\n" + "</Header>\r\n" + "<SequenceSimilaritySearchResult>\r\n"
            + "	<hits total=\"0\">\r\n" + "	</hits>\r\n" + PimsBlastXmlParserTester.FOOTER;
}
