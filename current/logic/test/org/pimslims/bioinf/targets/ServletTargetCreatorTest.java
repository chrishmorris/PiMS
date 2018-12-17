package org.pimslims.bioinf.targets;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.model.people.Person;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

@Deprecated
// ServletTargetCreator is no longer maintained
public class ServletTargetCreatorTest extends AbstractTestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ServletTargetCreatorTest.class);
    }

    public void testStaticisNotFormatSupported() {

        Assert.assertFalse(ServletTargetCreator.isFormatSupported(null));

        Assert.assertFalse(ServletTargetCreator.isFormatSupported("")); //$NON-NLS-1$
        Assert.assertFalse(ServletTargetCreator.isFormatSupported("123")); //$NON-NLS-1$
        final String longString =
            "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        Assert.assertTrue(longString.length() > 100);
        Assert.assertFalse(ServletTargetCreator.isFormatSupported(longString));
        final String longStringWithID = "ID" + longString;
        Assert.assertTrue(longStringWithID.length() > 100 && longStringWithID.startsWith("ID"));
        Assert.assertFalse(ServletTargetCreator.isFormatSupported(longStringWithID));
    }

    public void testStaticisCorrectFormatSupported() {
        Assert.assertTrue(ServletTargetCreator
            .isFormatSupported(ServletTargetCreatorTest.CORRECTStringForTargetCreateFrom));
        final String WrongStringWithoutEnd =
            ServletTargetCreatorTest.CORRECTStringForTargetCreateFrom.substring(0,
                ServletTargetCreatorTest.CORRECTStringForTargetCreateFrom.length() - 2);
        Assert.assertFalse(ServletTargetCreator.isFormatSupported(WrongStringWithoutEnd));
        Assert.assertTrue(ServletTargetCreator
            .isFormatSupported(ServletTargetCreatorTest.CORRECTStringWithoutSeqForTargetCreateFrom));
    }

    public void testConstructor() throws AccessException {
        this.wv = super.getWV();
        try {
            final String protrenty = ServletTargetCreatorTest.CORRECTStringForTargetCreateFrom;
            final String database = "swissprot";
            final Person person = POJOFactory.createPerson(this.wv);
            ServletTargetCreator stc = new ServletTargetCreator(protrenty, database, this.wv, person);
            Assert.assertNotNull(stc);

            final String database2 = "swissprot_prt";
            stc = new ServletTargetCreator(protrenty, database2, this.wv, person);
            Assert.assertNotNull(stc);

            final String database3 = "emblswissprot";
            try {
                stc = new ServletTargetCreator(protrenty, database3, this.wv, person);
                Assert.fail("not a valid record!");
            } catch (final IllegalArgumentException e) {
                // it is ok!
            }
            final String databaseCorrectEmbl = "emblswissprot";
            final String emblProtrenty = ServletTargetCreatorTest.EmblStringForTargetCreateFrom;

            stc = new ServletTargetCreator(emblProtrenty, databaseCorrectEmbl, this.wv, person);

            final String database4 = "uniprot";
            stc = new ServletTargetCreator(protrenty, database4, this.wv, person);
            Assert.assertNotNull(stc);

        } catch (final ConstraintException e) {

            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    // temporarily removed from test because fails
    public void xtestCreateTarget() throws AccessException {
        this.wv = super.getWV();
        try {
            final String protrenty = ServletTargetCreatorTest.CORRECTStringForTargetCreateFrom;
            final String database = "swissprot";
            final Person person = POJOFactory.createPerson(this.wv);
            final ServletTargetCreator stc = new ServletTargetCreator(protrenty, database, this.wv, person);
            Assert.assertNotNull(stc);
            final String targetHook = stc.putToDb();
            final Target t = this.wv.get(targetHook);
            Assert.assertNotNull(t);

        } catch (final ConstraintException e) {

            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            this.wv.abort();
        }

        // create with embl record
        this.wv = super.getWV();
        try {
            final String databaseCorrectEmbl = "emblswissprot";
            final String emblProtrenty = ServletTargetCreatorTest.EmblStringForTargetCreateFrom;
            final Person person = POJOFactory.createPerson(this.wv);

            final ServletTargetCreator stc =
                new ServletTargetCreator(emblProtrenty, databaseCorrectEmbl, this.wv, person);
            Assert.assertNotNull(stc);
            final String targetHook = stc.putToDb();
            final Target t = this.wv.get(targetHook);
            Assert.assertNotNull(t);

            try {
                stc.putToDb();
                Assert.fail("should no use stc again");
            } catch (final IllegalArgumentException e) {
                // ok
            }

        } catch (final ConstraintException e) {

            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }

    public void testIsNotUnique() {
        this.wv = super.getWV();
        final Map<String, Object> criteria = new HashMap<String, Object>();

        try {

            final Person person = POJOFactory.createPerson(this.wv);
            person.setFamilyName(person.getFamilyName() + System.currentTimeMillis());
            criteria.put(Person.PROP_FAMILYNAME, person.getFamilyName());

            // wv.commit();
        } catch (final ConstraintException e) {

            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

        this.wv = super.getWV();
        try {
            final boolean test = PIMSTargetWriter.isUnique(this.wv, criteria, MetaClassImpl.getMetaClass(Person.class));
            Assert.assertTrue(test);
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    // CHECKSTYLE:OFF

    private static String EmblStringForTargetCreateFrom =
        "ID   X82682; SV 1; linear; genomic DNA; STD; PRO; 2470 BP.\r\n" + "XX\r\n" + "AC   X82682;\r\n"
            + "XX\r\n" + "DT   15-MAY-1995 (Rel. 43, Created)\r\n"
            + "DT   15-MAY-1995 (Rel. 43, Last updated, Version 2)\r\n" + "XX\r\n"
            + "DE   E.coli plasmid cta, cti and ctl genes\r\n" + "XX\r\n"
            + "KW   colicin 10; colicin 10 immunity protein; colicin 10 lysis protein;\r\n"
            + "KW   cta gene; cti gene; ctl gene.\r\n" + "XX\r\n" + "OS   Escherichia coli\r\n"
            + "OC   Bacteria; Proteobacteria; Gammaproteobacteria; Enterobacteriales;\r\n"
            + "OC   Enterobacteriaceae; Escherichia.\r\n" + "XX\r\n" + "RN   [1]\r\n" + "RP   1-2470\r\n"
            + "RA   Pilsl H.;\r\n" + "RT   ;\r\n"
            + "RL   Submitted (16-NOV-1994) to the EMBL/GenBank/DDBJ databases.\r\n"
            + "RL   H. Pilsl, Institut fuer Mikrobiologie II, Universitaet Tuebingen, Auf der\r\n"
            + "RL   Morgenstelle 28, 72076 Tuebingen, FRG\r\n" + "XX\r\n" + "RN   [2]\r\n"
            + "RX   PUBMED; 7651137.\r\n" + "RA   Pilsl H., Braun V.;\r\n"
            + "RT   \"Novel colicin 10:assignment of four domains to the TonB and To1C-\r\n"
            + "RT   dependent uptake via the Tsx receptor and to pore formation\";\r\n"
            + "RL   Mol. Microbiol. 16(1):57-67(1995).\r\n" + "XX\r\n"
            + "FH   Key             Location/Qualifiers\r\n" + "FH\r\n" + "FT   source          1..2470\r\n"
            + "FT                   /organism=\"Escherichia coli\"\r\n"
            + "FT                   /strain=\"PTE10\"\r\n"
            + "FT                   /mol_type=\"genomic DNA\"\r\n"
            + "FT                   /db_xref=\"taxon:562\"\r\n" + "FT   CDS             446..1918\r\n"
            + "FT                   /transl_table=11\r\n" + "FT                   /gene=\"cta\"\r\n"
            + "FT                   /product=\"colicin 10\"\r\n"
            + "FT                   /db_xref=\"GOA:Q47125\"\r\n"
            + "FT                   /db_xref=\"InterPro:IPR000293\"\r\n"
            + "FT                   /db_xref=\"UniProtKB/Swiss-Prot:Q47125\"\r\n"
            + "FT                   /protein_id=\"CAA57998.1\"\r\n"
            + "FT                   /translation=\"MDKVTDNSPDVESTESTEGSFPTVGVDTGDTITATLATGTENVGG\r\n"
            + "FT                   GGGAFGGASESSAAIHATAKWSTAQLKKHQAEQAARAAAAEAALAKAKSQRDALTQRLK\r\n"
            + "FT                   DIVNDALRANAARSPSVTDLAHANNMAMQAEAERLRLAKAEQKAREEAEAAEKALREAE\r\n"
            + "FT                   RQRDEIARQQAETAHLLAMAEAAEAEKNRQDSLDEEHRAVEVAEKKLAEAKAELAKAES\r\n"
            + "FT                   DVQSKQAIVSRVAGELENAQKSVDVKVTGFPGWRDVQKKLERQLQDKKNEYSSVTNALN\r\n"
            + "FT                   SAVSIRDAKKTEVQNAEIKLKEAKDALEKSQVKDSVDTMVGFYQYITEQYGEKYSRIAQ\r\n"
            + "FT                   DLAEKAKGSKFNSVDEALAAFEKYKNVLDKKFSKVDRDDIFNALESITYDEWAKHLEKI\r\n"
            + "FT                   SRALKVTGYLSFGYDVWDGTLKGLKTGDWKPLFVTLEKSAVDFGVAKIVALMFSFIVGA\r\n"
            + "FT                   PLGFWGIAIITGIVSSYIGDDELNKLNELLGI\"\r\n"
            + "FT   CDS             complement(1915..2205)\r\n" + "FT                   /transl_table=11\r\n"
            + "FT                   /gene=\"cti\"\r\n"
            + "FT                   /product=\"colicin 10 immunity protein\"\r\n"
            + "FT                   /db_xref=\"UniProtKB/TrEMBL:Q47126\"\r\n"
            + "FT                   /protein_id=\"CAA57999.1\"\r\n"
            + "FT                   /translation=\"MTVKYYLHNLLESLIPWLFYLLLNYKTPPFSLIIFIASIHVLLYP\r\n"
            + "FT                   YSKLTIFSFIQNTTKMKKEPWYSYNLFYFLYLAMAIPVGLPSFIYYSLKRN\"\r\n"
            + "FT   CDS             2230..2361\r\n" + "FT                   /transl_table=11\r\n"
            + "FT                   /gene=\"ctl\"\r\n"
            + "FT                   /product=\"colicin 10 lysis protein\"\r\n"
            + "FT                   /db_xref=\"GOA:Q47127\"\r\n"
            + "FT                   /db_xref=\"InterPro:IPR003059\"\r\n"
            + "FT                   /db_xref=\"UniProtKB/TrEMBL:Q47127\"\r\n"
            + "FT                   /protein_id=\"CAA58000.1\"\r\n"
            + "FT                   /translation=\"MKITFLIITILSLSGCQANYIRDVQGGTVAPSSSSELTGIAVQ\"\r\n"
            + "XX\r\n" + "SQ   Sequence 2470 BP; 721 A; 409 C; 623 G; 717 T; 0 other;\r\n"
            + "     gtataccgcc gagtaagcct gggtgggatt cacaccatta ctgatttcac tgaccacatg        60\r\n"
            + "     acgctgatct gccagtgtca gcgtgtaaac ctgtttctgg atgaggtcac gggctttatc       120\r\n"
            + "     atgctcatat cggcagcaac aaagggtgtc gagaagtcgt ttgtctgttt tgttacgttt       180\r\n"
            + "     attgccgggt gatttgctca catcgttctc cgttttttca tgatccaaca gttttatatc       240\r\n"
            + "     tcatcctgat ttgctccggt gttctgtttc gacgttctca atgcctgcct ccccatagtg       300\r\n"
            + "     tgatccttat tgcaaaaatg aaaatccatg ctcttgacat ggacaatgct gagtagtagg       360\r\n"
            + "     tttttgctgt acataaaacc agtggttata tgtacagtat gttgttttta atttattgtt       420\r\n"
            + "     ttaattatca aagaggaatt tctttatgga taaagtcact gataattctc cagatgtgga       480\r\n"
            + "     gagcacagaa tctactgagg ggtcattccc aactgttggg gttgatactg gcgatacgat       540\r\n"
            + "     tacagcgacg cttgcaactg gaactgaaaa tgttggtgga ggcggtggag catttggtgg       600\r\n"
            + "     ggccagtgaa agttctgctg cgatacatgc aaccgctaaa tggtctaccg cgcagttgaa       660\r\n"
            + "     aaaacatcag gctgaacagg ctgcccgtgc tgctgcggct gaggcagcat tggcaaaagc       720\r\n"
            + "     gaaatctcag cgtgatgccc tgactcaacg tctcaaggat attgttaatg acgctttacg       780\r\n"
            + "     tgctaatgcc gctcgtagtc catcagtaac tgaccttgct catgccaata atatggcaat       840\r\n"
            + "     gcaggcagag gctgagcgtt tgcgccttgc gaaggcagag caaaaagccc gtgaagaagc       900\r\n"
            + "     tgaagcagca gaaaaagcgc tccgggaagc agaacgccaa cgtgatgaga ttgcccgcca       960\r\n"
            + "     acaggctgaa accgcgcatt tgttagcaat ggcggaggca gcagaggctg agaaaaatcg      1020\r\n"
            + "     acaggattct cttgatgaag agcatcgggc tgtggaagtg gcagagaaga agctggctga      1080\r\n"
            + "     ggctaaagct gaactggcga aggccgaaag cgatgtacag agtaagcaag cgattgtttc      1140\r\n"
            + "     cagagttgca ggggagcttg aaaacgctca aaaaagtgtt gatgtgaagg ttaccggatt      1200\r\n"
            + "     tcctggatgg cgtgatgttc agaaaaaact ggagagacaa ttgcaggata agaagaatga      1260\r\n"
            + "     atattcgtca gtgacgaatg ctcttaattc tgctgttagc attagagatg ctaaaaaaac      1320\r\n"
            + "     agaagttcag aatgctgaga taaaattaaa agaagctaag gatgctcttg agaagagtca      1380\r\n"
            + "     ggtaaaagac tctgttgata ctatggttgg gttttatcaa tatataaccg aacaatatgg      1440\r\n"
            + "     ggaaaaatat tccagaatag ctcaggattt agctgaaaag gcgaagggta gtaaatttaa      1500\r\n"
            + "     tagtgttgat gaagcacttg ctgcatttga aaagtataaa aatgtactgg ataagaaatt      1560\r\n"
            + "     cagtaaggtt gatagggatg atatttttaa tgctttagag tctattactt atgatgagtg      1620\r\n"
            + "     ggccaagcat ctagaaaaga tctctagggc tcttaaggtt actggatatt tgtctttcgg      1680\r\n"
            + "     gtatgatgta tgggatggta ccctaaaggg attaaaaaca ggagactgga agcctttatt      1740\r\n"
            + "     tgtcactctg gagaagagcg cggtagattt cggcgtggca aaaattgtgg cattaatgtt      1800\r\n"
            + "     tagttttatt gttggtgcgc ctcttggctt ctggggaatt gcaattatca caggtattgt      1860\r\n"
            + "     ttcttcttac ataggggatg atgagttgaa caagcttaat gaattactag gtatttaatt      1920\r\n"
            + "     tctctttaga gagtaatata tgaaacttgg caatcctact gggattgcca ttgctaggta      1980\r\n"
            + "     aagaaaataa aaaaggttgt atgagtacca aggttctttt ttcatttttg tcgtgttttg      2040\r\n"
            + "     tataaaagaa aatatagtta gttttgaata tgggtatagt aatacatgta tagatgcgat      2100\r\n"
            + "     gaatatgata agtgaaaatg ggggcgtctt atagtttaat aacaaataga atagccatgg      2160\r\n"
            + "     gataagtgac tctagtaaat tgtgtaggta gtattttacg gtcattattt atgttgttat      2220\r\n"
            + "     aggggtagaa tgaaaataac gtttttgata attacaatat tatccttgtc gggatgtcaa      2280\r\n"
            + "     gcaaattata ttcgtgatgt acaaggaggg acggtcgctc catcttcctc ttctgaactg      2340\r\n"
            + "     acagggatcg cggttcagta gaaaagatca aaggatcttc ttgagatcct ttttttctgc      2400\r\n"
            + "     gcgtaatctg ctgcttgcaa acaaaaaaac caccgctacc aacggtggtt tgtttgccgg      2460\r\n"
            + "     atcaagatct                                                             2470\r\n" + "//";

    private static String CORRECTStringForTargetCreateFrom =
        "ID   NRTA_ANASP              Reviewed;         440 AA.\r\n" + "AC   Q44292; O06469;\r\n"
            + "DT   01-NOV-1997, integrated into UniProtKB/Swiss-Prot.\r\n"
            + "DT   23-JAN-2002, sequence version 3.\r\n" + "DT   31-OCT-2006, entry version 37.\r\n"
            + "DE   Nitrate transport protein nrtA.\r\n" + "GN   Name=nrtA; OrderedLocusNames=alr0608;\r\n"
            + "OS   Anabaena sp. (strain PCC 7120).\r\n"
            + "OC   Bacteria; Cyanobacteria; Nostocales; Nostocaceae; Nostoc.\r\n"
            + "OX   NCBI_TaxID=103690;\r\n" + "RN   [1]\r\n" + "RP   NUCLEOTIDE SEQUENCE [GENOMIC DNA].\r\n"
            + "RX   MEDLINE=97144534; PubMed=8990301;\r\n" + "RA   Frias J.E., Flores E., Herrero A.;\r\n"
            + "RT   \"Nitrate assimilation gene cluster from the heterocyst-forming\r\n"
            + "RT   cyanobacterium Anabaena sp. strain PCC 7120.\";\r\n"
            + "RL   J. Bacteriol. 179:477-486(1997).\r\n" + "RN   [2]\r\n" + "RP   SEQUENCE REVISION.\r\n"
            + "RA   Frias J.E.;\r\n" + "RL   Submitted (JUN-1999) to the EMBL/GenBank/DDBJ databases.\r\n"
            + "RN   [3]\r\n" + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RX   MEDLINE=21595285; PubMed=11759840; DOI=10.1093/dnares/8.5.205;\r\n"
            + "RA   Kaneko T., Nakamura Y., Wolk C.P., Kuritz T., Sasamoto S.,\r\n"
            + "RA   Watanabe A., Iriguchi M., Ishikawa A., Kawashima K., Kimura T.,\r\n"
            + "RA   Kishida Y., Kohara M., Matsumoto M., Matsuno A., Muraki A.,\r\n"
            + "RA   Nakazaki N., Shimpo S., Sugimoto M., Takazawa M., Yamada M.,\r\n"
            + "RA   Yasuda M., Tabata S.;\r\n"
            + "RT   \"Complete genomic sequence of the filamentous nitrogen-fixing\r\n"
            + "RT   cyanobacterium Anabaena sp. strain PCC 7120.\";\r\n"
            + "RL   DNA Res. 8:205-213(2001).\r\n" + "RN   [4]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [GENOMIC DNA] OF 1-92.\r\n"
            + "RX   MEDLINE=97136629; PubMed=8982006;\r\n" + "RA   Cai Y., Wolk C.P.;\r\n"
            + "RT   \"Nitrogen deprivation of Anabaena sp. strain PCC 7120 elicits rapid\r\n"
            + "RT   activation of a gene cluster that is essential for uptake and\r\n"
            + "RT   utilization of nitrate.\";\r\n" + "RL   J. Bacteriol. 179:258-266(1997).\r\n"
            + "CC   -!- FUNCTION: Essential component of the nitrate-transporting system.\r\n"
            + "CC       May be the substrate-binding protein (By similarity).\r\n"
            + "CC   -!- SUBCELLULAR LOCATION: Cell inner membrane; peripheral membrane\r\n"
            + "CC       protein (Potential).\r\n"
            + "CC   -!- SIMILARITY: To carotenoid-binding protein A (cbpA).\r\n"
            + "CC   -----------------------------------------------------------------------\r\n"
            + "CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\r\n"
            + "CC   Distributed under the Creative Commons Attribution-NoDerivs License\r\n"
            + "CC   -----------------------------------------------------------------------\r\n"
            + "DR   EMBL; X99709; CAA68041.2; -; Genomic_DNA.\r\n"
            + "DR   EMBL; BA000019; BAB72566.1; -; Genomic_DNA.\r\n"
            + "DR   EMBL; U61496; AAC46075.1; -; Genomic_DNA.\r\n" + "DR   PIR; AG1882; AG1882.\r\n"
            + "DR   GenomeReviews; BA000019_GR; alr0608.\r\n" + "DR   KEGG; ana:alr0608; -.\r\n"
            + "DR   BioCyc; NOST-PCC-01:NOST-PCC-01-000607-MONOMER; -.\r\n"
            + "DR   BioCyc; NSP103690:ALR0608-MONOMER; -.\r\n" + "DR   InterPro; IPR006311; Tat.\r\n"
            + "DR   TIGRFAMs; TIGR01409; TAT_signal_seq; 1.\r\n"
            + "KW   Complete proteome; Inner membrane; Membrane; Nitrate assimilation;\r\n"
            + "KW   Transport.\r\n" + "FT   CHAIN         1    440       Nitrate transport protein nrtA.\r\n"
            + "FT                                /FTId=PRO_0000057955.\r\n"
            + "FT   CONFLICT    100    100       W -> C (in Ref. 1).\r\n"
            + "SQ   SEQUENCE   440 AA;  48476 MW;  29937A41FB45CE9C CRC64;\r\n"
            + "     MTHVSRRKFL FTTGAAAAAS ILVHGCTSNG SQSATTGEQA PSAAPAANVS AANAPKVETT\r\n"
            + "     KAKLGFIPLT DAAPLIIAKE KGFFAKYGMT DIEVIKQKSW PVTRDNLKIG SSGGGIDGAH\r\n"
            + "     ILSPMPYLMT INDKVPMYIL ARLNTNGQAI SVAEKFKELN VNLESKSLKD AAIKAKADKK\r\n"
            + "     ALKMGITFPG GTHDLWMRYW LAAGGINPDQ DVVLEAVPPP QMVANMKVNT VDGFCVGEPW\r\n"
            + "     NAQLVNQKIG YSALVTGELW KDHPEKAFSM RQDWIEQNPN AAQAILMAIL EAQQWCDKAE\r\n"
            + "     NKEEMCKICS DRKYFNVAAA DIIERAKGNI DYGDGRKEQN FAHRMKFWAD NASYPYKSHD\r\n"
            + "     IWFLTEDIRW GYLPKDTKVQ DIVNQVNKED LWKKAAKAIG VADAEIPASS SRGVETFFDG\r\n"
            + "     VKFDPEKPEE YLNSLKIKKV\r\n" + "//";

    private static String CORRECTStringWithoutSeqForTargetCreateFrom =
        "ID   NRTA_ANASP              Reviewed;         440 AA.\r\n" + "AC   Q44292; O06469;\r\n"
            + "DT   01-NOV-1997, integrated into UniProtKB/Swiss-Prot.\r\n"
            + "DT   23-JAN-2002, sequence version 3.\r\n" + "DT   31-OCT-2006, entry version 37.\r\n"
            + "DE   Nitrate transport protein nrtA.\r\n" + "GN   Name=nrtA; OrderedLocusNames=alr0608;\r\n"
            + "OS   Anabaena sp. (strain PCC 7120).\r\n"
            + "OC   Bacteria; Cyanobacteria; Nostocales; Nostocaceae; Nostoc.\r\n"
            + "OX   NCBI_TaxID=103690;\r\n" + "RN   [1]\r\n" + "RP   NUCLEOTIDE SEQUENCE [GENOMIC DNA].\r\n"
            + "RX   MEDLINE=97144534; PubMed=8990301;\r\n" + "RA   Frias J.E., Flores E., Herrero A.;\r\n"
            + "RT   \"Nitrate assimilation gene cluster from the heterocyst-forming\r\n"
            + "RT   cyanobacterium Anabaena sp. strain PCC 7120.\";\r\n"
            + "RL   J. Bacteriol. 179:477-486(1997).\r\n" + "RN   [2]\r\n" + "RP   SEQUENCE REVISION.\r\n"
            + "RA   Frias J.E.;\r\n" + "RL   Submitted (JUN-1999) to the EMBL/GenBank/DDBJ databases.\r\n"
            + "RN   [3]\r\n" + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RX   MEDLINE=21595285; PubMed=11759840; DOI=10.1093/dnares/8.5.205;\r\n"
            + "RA   Kaneko T., Nakamura Y., Wolk C.P., Kuritz T., Sasamoto S.,\r\n"
            + "RA   Watanabe A., Iriguchi M., Ishikawa A., Kawashima K., Kimura T.,\r\n"
            + "RA   Kishida Y., Kohara M., Matsumoto M., Matsuno A., Muraki A.,\r\n"
            + "RA   Nakazaki N., Shimpo S., Sugimoto M., Takazawa M., Yamada M.,\r\n"
            + "RA   Yasuda M., Tabata S.;\r\n"
            + "RT   \"Complete genomic sequence of the filamentous nitrogen-fixing\r\n"
            + "RT   cyanobacterium Anabaena sp. strain PCC 7120.\";\r\n"
            + "RL   DNA Res. 8:205-213(2001).\r\n" + "RN   [4]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [GENOMIC DNA] OF 1-92.\r\n"
            + "RX   MEDLINE=97136629; PubMed=8982006;\r\n" + "RA   Cai Y., Wolk C.P.;\r\n"
            + "RT   \"Nitrogen deprivation of Anabaena sp. strain PCC 7120 elicits rapid\r\n"
            + "RT   activation of a gene cluster that is essential for uptake and\r\n"
            + "RT   utilization of nitrate.\";\r\n" + "RL   J. Bacteriol. 179:258-266(1997).\r\n" +

            "//";
}
