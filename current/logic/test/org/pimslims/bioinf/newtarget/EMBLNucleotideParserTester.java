/**
 * current-pims-web org.pimslims.command.newtarget EMBLParserTester.java
 * 
 * @author Petr
 * @date 4 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.naming.ServiceUnavailableException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.dao.ModelImpl;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.properties.PropertyGetter;

/**
 * EMBLParserTester
 * 
 */
public class EMBLNucleotideParserTester extends TestCase {

    /**
     * 
     */
    private static final String COMMENTS =
        "Draft entry and computer-readable sequence for [1] kindly provided\r\n"
            + "CC   by R.J.Kadner, 22-NOV-1988.";

    /*
     * EMBL 2CDS example record
     */
    public static final String emblPrimer = "ID   X57471; SV 1; linear; genomic DNA; STD; PRO; 2961 BP.\r\n"
        + "XX\r\n" + "AC   X57471;\r\n" + "XX\r\n" + "DT   14-JUN-1991 (Rel. 28, Created)\r\n"
        + "DT   18-APR-2005 (Rel. 83, Last updated, Version 3)\r\n" + "XX\r\n"
        + "DE   E.coli fepC, fepD and fepG genes for ferric enterobactin transport proteins\r\n" + "XX\r\n"
        + "KW   fepC gene; fepD gene; fepG gene; ferric enterobactin transport protein.\r\n" + "XX\r\n"
        + "OS   Escherichia coli\r\n"
        + "OC   Bacteria; Proteobacteria; Gammaproteobacteria; Enterobacteriales;\r\n"
        + "OC   Enterobacteriaceae; Escherichia.\r\n" + "XX\r\n" + "RN   [1]\r\n" + "RP   1-2961\r\n"
        + "RA   Shea C.M.;\r\n" + "RT   ;\r\n"
        + "RL   Submitted (13-MAR-1991) to the EMBL/GenBank/DDBJ databases.\r\n"
        + "RL   C.M. Shea, Univ of Missouri-Columbia, School of Medicine, Dept of Mol\r\n"
        + "RL   Microbiology and Immunology, Columbia MO 65212, USA\r\n" + "XX\r\n" + "RN   [2]\r\n"
        + "RX   PUBMED; 1838574.\r\n" + "RA   Shea C.M., McIntosh M.A.;\r\n"
        + "RT   \"Nucleotide sequence and genetic organization of the ferric enterobactin\r\n"
        + "RT   transport system: homology to other periplasmic-binding-protein-dependent\r\n"
        + "RT   systems in Escherichia coli\";\r\n" + "RL   Mol. Microbiol. 5(6):1415-1428(1991).\r\n"
        + "XX\r\n" + "RN   [3]\r\n" + "RA   Chenault S., Earhart C.F.;\r\n"
        + "RT   \"Organization of genes encoding membrane proteins of the  Escherichia coli\r\n"
        + "RT   ferrienterobactin permease\";\r\n" + "RL   Mol. Microbiol. 0:0-0(0).\r\n" + "XX\r\n"
        + "FH   Key             Location/Qualifiers\r\n" + "FH\r\n" + "FT   source          1..2961\r\n"
        + "FT                   /organism=\"Escherichia coli\"\r\n"
        + "FT                   /map=\"13 minutes\"\r\n" + "FT                   /strain=\"K12\"\r\n"
        + "FT                   /mol_type=\"genomic DNA\"\r\n" + "FT                   /clone=\"pITS24\"\r\n"
        + "FT                   /db_xref=\"taxon:562\"\r\n" + "FT   -35_signal      25..30\r\n"
        + "FT   promoter        25..54\r\n"
        + "FT                   /note=\"bi-directional promoter for fep operon and  p43\"\r\n"
        + "FT   -10_signal      49..54\r\n" + "FT   mRNA            61..2950\r\n"
        + "FT                   /note=\"for fepD, fepG, fepC\"\r\n" + "FT   misc_signal     75..93\r\n"
        + "FT                   /note=\"FURbox\"\r\n" + "FT   RBS             96..100\r\n"
        + "FT   CDS             106..1110\r\n" + "FT                   /transl_table=11\r\n"
        + "FT                   /gene=\"fepD\"\r\n"
        + "FT                   /function=\"ferric enterobactin transport protein\"\r\n"
        + "FT                   /db_xref=\"GOA:P23876\"\r\n"
        + "FT                   /db_xref=\"InterPro:IPR000522\"\r\n"
        + "FT                   /db_xref=\"UniProtKB/Swiss-Prot:P23876\"\r\n"
        + "FT                   /protein_id=\"CAA40707.1\"\r\n"
        + "FT                   /translation=\"MSGSVAVTRAIAVPGLLLLLIIATALSLLIGAKSLPASVVLEAFS\r\n"
        + "FT                   GTCQSADCTIVLDARLPRTLAGLLAGGALGLAGALMQTLTRNPLADPGLLGVNAGASFA\r\n"
        + "FT                   IVLGAALFGYSSAQEQLAMAFAGALVASLIVAFTGSQGGGQLSPVRLTLAGVALAAVLE\r\n"
        + "FT                   GLTSGIALLNPDVYDQLRFWQAGSLDIRNLHTLKVVLIPVLIAGATALLLSRALNSLSL\r\n"
        + "FT                   GSDTATALGSRVARTQLIGLLAITVLCGSATAIVGPIAFIGLMMPHMARWLVGADHRWS\r\n"
        + "FT                   LPVTLLATPALLLFADIIGRVIVPGELRVSVVSAFIGAPVLIFLVRRKTRGGA\"\r\n"
        + "FT   RBS             1098..1103\r\n" + "FT   CDS             1107..2099\r\n"
        + "FT                   /transl_table=11\r\n" + "FT                   /gene=\"fepG\"\r\n"
        + "FT                   /function=\"ferric enterobactin  transport protein\"\r\n"
        + "FT                   /db_xref=\"GOA:P23877\"\r\n"
        + "FT                   /db_xref=\"UniProtKB/Swiss-Prot:P23877\"\r\n"
        + "FT                   /protein_id=\"CAA40708.1\"\r\n"
        + "FT                   /translation=\"MIYVSRRLLITCLLLVSACVVAGIWGLRSGAVTLETSQVFAALMG\r\n"
        + "FT                   DAPRSMTMVVTEWRLPRVLMALLIGAALGVSGAIFQSLMRNPLGSPDVMGFNTGAWSGV\r\n"
        + "FT                   LVAMVLFGQDLTAIALSAMVGGIVTSLLVWLLAWRNGIDTFRLIIIGIGVRAMLVAFNT\r\n"
        + "FT                   WLLLKASLETALTAGLWNAGSLNGLTWAKTSPSAPIIILMLIAAALLVRRMRLLEMGDD\r\n"
        + "FT                   TACALGVRLERSRLLMMLVAVVLTAAATALAGPISFIALVAPHIARRISGTARWGLTQA\r\n"
        + "FT                   ALCGALLLLAADLCAQQLFMPYQLPVGVVTVSLGGIYLIVLLIQESRKK\"\r\n"
        + "FT   RBS             2080..2084\r\n" + "FT   CDS             2096..2911\r\n"
        + "FT                   /transl_table=11\r\n" + "FT                   /gene=\"fepC\"\r\n"
        + "FT                   /function=\"ferric enterobactin  transport protein,\r\n"
        + "FT                   ATP-binding protein\"\r\n"
        + "FT                   /db_xref=\"GOA:P23878\"\r\n"
        + "FT                   /db_xref=\"UniProtKB/Swiss-Prot:P23878\"\r\n"
        + "FT                   /protein_id=\"CAA40709.1\"\r\n"
        + "FT                   /translation=\"MTESVARLRGEQLTLGYGKYTVAENLTVEIPDGHFTAIIGPNGCG\r\n"
        + "FT                   KSTLLRTLSRLMTPAHGHVWLDGEHIQHYASKEVARRIGLLAQNATTPGDITVQELVAR\r\n"
        + "FT                   GRYPHQPLFTRWRKEDEEAVTKAMQATGITHLADQSVDTLSGGQRQRAWIAMVLAQETA\r\n"
        + "FT                   IMLLDEPTTWLDISHQIDLLELLSELNREKGYTLAAVLHDLNQACRYASHLIALREGKI\r\n"
        + "FT                   VAQGRPKEIVTAELIERIYGLRCMIIDDPVAGTPLVVPLGRTAPSTANS\"\r\n" + "XX\r\n"
        + "SQ   Sequence 2961 BP; 547 A; 802 C; 885 G; 727 T; 0 other;\r\n"
        + "     tgccttgcca tcaaatattt taaataccta ttcttaggct gcacatgcta acatatccaa        60\r\n"
        + "     ataagatcga taacgataat taatttcatt atcatggaag ttcgtatgtc tggttctgtt       120\r\n"
        + "     gccgtgacac gcgccattgc cgtgcccgga ttgctgttat tactgattat cgcgacggca       180\r\n"
        + "     ttaagcctgc tcattggggc aaaatcactc cccgcttccg tagtgctgga ggccttctcc       240\r\n"
        + "     ggcacctgcc agagcgccga ctgcaccatc gtgctcgacg cgcggctgcc gcgtaccctt       300\r\n"
        + "     gccggtttac tggcaggcgg cgcgcttggc cttgccgggg cgttaatgca aaccctcacc       360\r\n"
        + "     cgaaacccac ttgccgaccc cggcttgctt ggcgtgaacg ccggagccag ctttgccatt       420\r\n"
        + "     gtgctgggtg cggcgctgtt tggttactct tccgcgcagg aacaactggc gatggccttc       480\r\n"
        + "     gccggggcgc tggtggcctc attgattgtt gcctttaccg gcagtcaggg cggcgggcag       540\r\n"
        + "     ttaagtccgg tgcgtttaac cctggcgggc gtggcgctgg cggcggtgct ggaaggactg       600\r\n"
        + "     accagcggca tcgccctgct taatcctgac gtctacgatc agttgcgttt ctggcaagcc       660\r\n"
        + "     ggttcgctgg atattcgcaa tctacatacc ttaaaagtgg tgctgatccc ggtgctgatc       720\r\n"
        + "     gccggagcaa ctgcgctatt actgagtcgc gcgctgaaca gtttgagcct cggcagcgac       780\r\n"
        + "     accgcgacgg cgctgggcag tcgcgtggcg cgcacacagt tgattggtct gctggcgatt       840\r\n"
        + "     accgtgcttt gtggtagtgc gacggcaata gttggcccga ttgcctttat tggcctgatg       900\r\n"
        + "     atgccgcata tggcgcgttg gctggtgggt gccgatcatc gctggtcgct gcccgtcacg       960\r\n"
        + "     ctacttgcta cccctgccct gctgctgttt gccgatatca tcgggcgggt gattgtgccc      1020\r\n"
        + "     ggcgaactgc gcgtttctgt ggtcagtgcg tttattggtg caccggtgct gatcttcctc      1080\r\n"
        + "     gtgcgacgta aaacgcgagg tggtgcatga tttacgtctc tcgccgatta ctcatcacct      1140\r\n"
        + "     gtttgctgct ggtttccgcc tgtgtggttg caggtatctg gggattacgc agcggtgccg      1200\r\n"
        + "     tcacgctgga aacctcgcag gtattcgccg cgctgatggg cgatgcgccg cgcagtatga      1260\r\n"
        + "     cgatggtggt caccgaatgg cgtttaccac gcgtgctgat ggcgctgttg attggcgcag      1320\r\n"
        + "     cactgggcgt cagtggcgcg atttttcagt cgctgatgcg taacccgctc ggcagccctg      1380\r\n"
        + "     acgtaatggg ctttaacacc ggggcgtgga gcggcgtgct ggtggcgatg gtgctgtttg      1440\r\n"
        + "     gtcaggacct gacggctatc gcgctgtcag caatggtggg cggcattgtc acttcgctgc      1500\r\n"
        + "     tggtctggct gctcgcctgg cgcaacggca tcgacacctt tcggttgatt attatcggta      1560\r\n"
        + "     tcggcgttcg cgccatgctg gtggccttta atacctggct gttgctgaaa gcgtctttag      1620\r\n"
        + "     aaacggcgct aacagcaggt ttgtggaatg ccggatcgct caacggcctg acgtgggcaa      1680\r\n"
        + "     aaacctcgcc ttccgcaccc atcattatat tgatgctcat tgccgccgcc ttactggtac      1740\r\n"
        + "     gccggatgcg cttgctggaa atgggcgatg ataccgcgtg tgcgctgggc gtcaggctcg      1800\r\n"
        + "     aacgttcgcg tctgttaatg atgctggttg cagtggtgct taccgctgcg gcaacagcgc      1860\r\n"
        + "     ttgccgggcc gatttccttt attgctttag tcgcaccgca cattgcccga cgcattagcg      1920\r\n"
        + "     gcaccgctcg ctgggggcta acccaggcgg cgctatgcgg ggcgctgtta ctgctggcgg      1980\r\n"
        + "     ccgatctctg cgcccaacaa ctgtttatgc cgtatcaact tccggttggc gtcgttaccg      2040\r\n"
        + "     tcagcctcgg cggtatttac cttatcgtct tgttaattca ggagtctcgc aaaaaatgac      2100\r\n"
        + "     cgaatcagta gcccgtttgc gcggcgaaca gttaaccctg ggatatggca aatataccgt      2160\r\n"
        + "     tgcggaaaat ctgactgtag aaatacctga tggtcacttc acggcaatta tcgggccaaa      2220\r\n"
        + "     tggctgcggt aaatccacgt tactgcgtac cttaagccgc ctgatgacgc ctgctcatgg      2280\r\n"
        + "     gcatgtctgg ctggatggcg agcacattca acattacgcc agtaaagagg ttgcacgccg      2340\r\n"
        + "     gattggtctg ttggcgcaaa atgctaccac gccgggcgat atcaccgtgc aggagctggt      2400\r\n"
        + "     ggcgcgtgga cgttatccgc atcaaccgct gtttacccgc tggcgcaaag aggatgaaga      2460\r\n"
        + "     agcggtaacg aaagcaatgc aggccacggg aataactcat ctggcagatc aaagcgtgga      2520\r\n"
        + "     taccctttct ggcggacaac gccagcgagc gtggatcgcg atggtgctgg cccaggaaac      2580\r\n"
        + "     ggcaattatg ctgctcgacg aacccacgac ctggctggat atcagtcatc agattgattt      2640\r\n"
        + "     gctggagttg ttaagcgaac tgaaccgcga aaaaggctat accctggcgg cggtgctgca      2700\r\n"
        + "     cgatcttaat caggcctgtc gttacgccag ccatttgatt gcattgcggg aagggaaaat      2760\r\n"
        + "     tgttgctcag ggacggccga aggagattgt cactgctgaa ctgattgagc gcatttatgg      2820\r\n"
        + "     tctgcgctgc atgatcattg acgatccagt ggccggaacg ccgcttgtgg tgccgctcgg      2880\r\n"
        + "     acgaacggca ccttcaaccg caaatagtta aactaagtgg tctgccatca tggcatcctg      2940\r\n"
        + "     ttttctggat gccatcgcat a                                                2961\r\n" + "//\r\n";

    String EMBLNucSingleCDS = "ID   M13748; SV 1; linear; genomic DNA; STD; PRO; 2624 BP.\r\n" + "XX\r\n"
        + "AC   M13748;\r\n" + "XX\r\n" + "DT   16-JUL-1988 (Rel. 16, Created)\r\n"
        + "DT   04-MAR-2000 (Rel. 63, Last updated, Version 7)\r\n" + "XX\r\n"
        + "DE   Escherichia coli ferrienterochelin receptor (fepA) gene, complete cds.\r\n" + "XX\r\n"
        + "KW   fepA gene; ferrienterochelin receptor; membrane protein;\r\n"
        + "KW   outer membrane protein.\r\n" + "XX\r\n" + "OS   Escherichia coli\r\n"
        + "OC   Bacteria; Proteobacteria; Gammaproteobacteria; Enterobacteriales;\r\n"
        + "OC   Enterobacteriaceae; Escherichia.\r\n" + "XX\r\n" + "RN   [1]\r\n" + "RP   1-2624\r\n"
        + "RX   PUBMED; 3015941.\r\n" + "RA   Lundrigan M.D., Kadner R.J.;\r\n"
        + "RT   \"Nucleotide sequence of the gene for the ferrienterochelin receptor FepA in\r\n"
        + "RT   Escherichia coli. Homology among outer membrane receptors that interact\r\n"
        + "RT   with TonB\";\r\n" + "RL   J. Biol. Chem. 261(23):10797-10801(1986).\r\n" + "XX\r\n" + "CC   "
        + EMBLNucleotideParserTester.COMMENTS
        + "\r\n"
        + "XX\r\n"
        + "FH   Key             Location/Qualifiers\r\n"
        + "FH\r\n"
        + "FT   source          1..2624\r\n"
        + "FT                   /organism=\"Escherichia coli\"\r\n"
        + "FT                   /strain=\"K-12\"\r\n"
        + "FT                   /mol_type=\"genomic DNA\"\r\n"
        + "FT                   /clone=\"pPC104.\"\r\n"
        + "FT                   /db_xref=\"taxon:562\"\r\n"
        + "FT   sig_peptide     271..336\r\n"
        + "FT                   /gene=\"fepA\"\r\n"
        + "FT                   /note=\"ferrienterochelin receptor signal peptide\"\r\n"
        + "FT   CDS             271..2508\r\n"
        + "FT                   /codon_start=1\r\n"
        + "FT                   /transl_table=11\r\n"
        + "FT                   /gene=\"fepA\"\r\n"
        + "FT                   /product=\"ferrienterochelin receptor protein\"\r\n"
        + "FT                   /db_xref=\"GOA:P05825\"\r\n"
        + "FT                   /db_xref=\"PDB:1FEP\"\r\n"
        + "FT                   /db_xref=\"UniProtKB/Swiss-Prot:P05825\"\r\n"
        + "FT                   /protein_id=\"AAA65994.1\"\r\n"
        + "FT                   /translation=\"MNKKIHSLALLVNLGIYGVAQAQEPTDTPVSHDDTIVVTAAEQNL\r\n"
        + "FT                   QAPGVSTITADEIRKNPVARDVSKIIRTMPGVNLTGNSTSGQRGNNRQIDIRGMGPENT\r\n"
        + "FT                   LILIDGKPVSSRNSVRQGWRGERDTRGDTSWVPPEMIERIEVLRGPARARYGNGAAGGV\r\n"
        + "FT                   VNIITKKGSGEWHGSWDAYFNAPEHKEEGATKRTNFSLTGPLGDEFSFRLYGNLDKTQA\r\n"
        + "FT                   DAWDINQGHQSARAGTYATTLPAGREGVINKDINGVVRWDFAPLQSLELEAGYSRQGNL\r\n"
        + "FT                   YAGDTQNTNSDSYTRSKYGDETNRLYRQNYALTWNGGWDNGVTTSNWVQYEHTRNSRIP\r\n"
        + "FT                   EGLAGGTEGKFNEKATQDFVDIDLDDVMLHSEVNLPIDFLVNQTLTLGTEWNQQRMKDL\r\n"
        + "FT                   SSNQALTGTNTGGAIDGVSTTDRSPYSKAEIFSLFAENNMELTDSTIVTPGLRFDHHSI\r\n"
        + "FT                   VGNNWSPALNISQGLGDDFTLKMGIARAYKAPSLYQTNPNYILYSKGQGCYASAGGCYL\r\n"
        + "FT                   QGNDDLKAETSINKEIGLEFKRDGWLAGVTWFRNDYRNKIEAGYVAVGQNAVGTDLYQW\r\n"
        + "FT                   DNVPKAVVEGLEGSLNVPVSETVMWTNNITYMLKSENKTTGDRLSIIPEYTLNSTLSWQ\r\n"
        + "FT                   AREDLSMQTTFTWYGKQQPKKYNYKGQPAVGPETKEISPYSIVGLSATWDVTKNVSLTG\r\n"
        + "FT                   GVDNLFDKRLWRAGNAQTTGDLAGANYIAGAGAYTYNEPGRTWYMSVNTHF\"\r\n"
        + "FT   mat_peptide     337..2505\r\n"
        + "FT                   /gene=\"fepA\"\r\n"
        + "FT                   /note=\"ferrienterochelin receptor\"\r\n"
        + "XX\r\n"
        + "SQ   Sequence 2624 BP; 686 A; 653 C; 733 G; 552 T; 0 other;\r\n"
        + "     gactcgacag ctctcactcc tcatttaacg ccgtcacacc ataaccatgt tactgtgcaa        60\r\n"
        + "     ttttcatgat gcagaaatat attagtaata ttatgataac tatttgcatt tgcaatagcg       120\r\n"
        + "     taatgcgcgc cgtggaagcg cggacattaa ttaaccaact gactgcgtgt ctttcaggga       180\r\n"
        + "     tcaaaggttt tcgcggtagc gggatgcgtc gtgttgatga cgaccatgcc cgacagttgc       240\r\n"
        + "     aattcgtggc aaaaatgcag gaataaaaca atgaacaaga agattcattc cctggccttg       300\r\n"
        + "     ttggtcaatc tggggattta tggggtagcg caggcacaag agccgaccga tactcctgtt       360\r\n"
        + "     tcacatgacg atactattgt cgttaccgcc gccgagcaga acttacaggc gcctggcgtt       420\r\n"
        + "     tcgaccatca ccgcagatga aatccgcaaa aacccggttg cccgcgatgt gtcgaagatc       480\r\n"
        + "     atccgtacca tgccaggcgt taacctgacc ggtaactcca ccagtggtca gcgtgggaat       540\r\n"
        + "     aaccgacaga ttgatattcg cggtatgggt ccggaaaaca cgctgatttt gattgacggc       600\r\n"
        + "     aagccggtaa gcagccgtaa ctcggtgcgt cagggctggc gtggcgagcg cgatacccgt       660\r\n"
        + "     ggtgatactt cctgggtgcc acctgaaatg attgaacgta ttgaagttct gcgtggtccg       720\r\n"
        + "     gcacgtgcgc gttatggcaa cggcgcggcg ggcggcgtgg ttaacatcat taccaaaaaa       780\r\n"
        + "     ggcagcggcg agtggcacgg ctcctgggac gcatatttca atgcgccaga acataaagag       840\r\n"
        + "     gaaggtgcca ccaaacgcac taactttagc ctgaccggtc cgctgggcga cgaattcagc       900\r\n"
        + "     ttccgtttgt atggcaacct cgacaaaacc caggctgacg cgtgggatat caaccagggc       960\r\n"
        + "     catcagtccg cgcgtgccgg aacgtatgcc acgacgttac cagccgggcg cgaaggggta      1020\r\n"
        + "     atcaacaaag atattaatgg cgtggtgcgc tgggatttcg cgccattgca atcgctggaa      1080\r\n"
        + "     ctggaagcag gttacagccg ccagggtaac ctgtatgcgg gcgacaccca gaataccaac      1140\r\n"
        + "     tccgattcct atacccgctc gaaatatggc gatgaaacca accgtctgta tcgccagaac      1200\r\n"
        + "     tacgcgctga cctggaacgg tggctgggat aacggcgtga ccaccagcaa ctgggtgcag      1260\r\n"
        + "     tacgaacaca cccgtaactc gcgtattccg gaaggtctgg cgggcggtac cgaagggaaa      1320\r\n"
        + "     tttaacgaaa aagcgacaca ggatttcgtc gatatcgatc ttgatgacgt gatgctgcac      1380\r\n"
        + "     agcgaagtta acctgccgat tgatttcctc gttaaccaga cgctgacgct gggtacggag      1440\r\n"
        + "     tggaatcagc aacggatgaa ggacttaagt tccaaccagg cactgaccgg aacgaatacc      1500\r\n"
        + "     ggtggcgcta ttgatggcgt gagtaccacc gaccgtagcc cgtattcaaa agcagaaatt      1560\r\n"
        + "     ttctcgctgt ttgccgaaaa caacatggag ctgactgaca gcaccatcgt aacgccgggg      1620\r\n"
        + "     ctgcgtttcg atcatcacag tattgtcggc aataactgga gcccggcgct gaacatatcg      1680\r\n"
        + "     caaggtttag gcgatgactt cacgctgaaa atgggcatcg cccgtgctta taaagcgccg      1740\r\n"
        + "     agcctgtacc agactaaccc gaactacatt ctctacagta aaggtcaggg ttgctatgcc      1800\r\n"
        + "     agcgcgggcg gctgctatct gcaaggtaac gatgacctga aagcagaaac cagcatcaac      1860\r\n"
        + "     aaagagattg gtctggagtt caaacgcgac gggtggctgg cgggcgtcac ctggttccgt      1920\r\n"
        + "     aacgattatc gcaataagat tgaagcgggc tatgtggctg tagggcaaaa cgcagtcggc      1980\r\n"
        + "     accgatctct atcagtggga taacgtgccg aaagcggtgg ttgaaggtct ggaaggatcg      2040\r\n"
        + "     ttaaacgtac cggttagcga aacggtgatg tggaccaata acatcactta tatgctgaag      2100\r\n"
        + "     agtgaaaaca aaaccacggg cgaccgtttg tcgatcatcc cggagtatac gttgaactca      2160\r\n"
        + "     acgctgagct ggcaggcacg ggaagatttg tcgatgcaaa cgaccttcac ctggtacggc      2220\r\n"
        + "     aagcagcagc cgaagaagta caactataaa ggtcagccag cggttggacc ggaaaccaaa      2280\r\n"
        + "     gaaattagtc cttacagcat tgttggcctg agcgcgacct gggatgtgac gaagaatgtc      2340\r\n"
        + "     agtctgaccg gcggcgtgga caatctgttc gacaaacgtt tgtggcgtgc gggtaatgcc      2400\r\n"
        + "     cagaccacgg gcgatttggc aggggccaac tatatcgccg gtgccggggc gtatacctat      2460\r\n"
        + "     aacgagccgg gacgtacgtg gtatatgagc gtaaacaccc acttctgatg ctaacgtcag      2520\r\n"
        + "     attgttgaca aagtgcgcgt cgttcatgcc ggatgcgcgg tgaacgcctt atccggccta      2580\r\n"
        + "     caaaaatctt gccaattcaa tatattgcag gaccatgtag gcct                       2624\r\n" + "//\r\n";

    String EMBLNucSCDSProtSeq = "MNKKIHSLALLVNLGIYGVAQAQEPTDTPVSHDDTIVVTAAEQNL\r\n"
        + "QAPGVSTITADEIRKNPVARDVSKIIRTMPGVNLTGNSTSGQRGNNRQIDIRGMGPENT\r\n"
        + "LILIDGKPVSSRNSVRQGWRGERDTRGDTSWVPPEMIERIEVLRGPARARYGNGAAGGV\r\n"
        + "VNIITKKGSGEWHGSWDAYFNAPEHKEEGATKRTNFSLTGPLGDEFSFRLYGNLDKTQA\r\n"
        + "DAWDINQGHQSARAGTYATTLPAGREGVINKDINGVVRWDFAPLQSLELEAGYSRQGNL\r\n"
        + "YAGDTQNTNSDSYTRSKYGDETNRLYRQNYALTWNGGWDNGVTTSNWVQYEHTRNSRIP\r\n"
        + "EGLAGGTEGKFNEKATQDFVDIDLDDVMLHSEVNLPIDFLVNQTLTLGTEWNQQRMKDL\r\n"
        + "SSNQALTGTNTGGAIDGVSTTDRSPYSKAEIFSLFAENNMELTDSTIVTPGLRFDHHSI\r\n"
        + "VGNNWSPALNISQGLGDDFTLKMGIARAYKAPSLYQTNPNYILYSKGQGCYASAGGCYL\r\n"
        + "QGNDDLKAETSINKEIGLEFKRDGWLAGVTWFRNDYRNKIEAGYVAVGQNAVGTDLYQW\r\n"
        + "DNVPKAVVEGLEGSLNVPVSETVMWTNNITYMLKSENKTTGDRLSIIPEYTLNSTLSWQ\r\n"
        + "AREDLSMQTTFTWYGKQQPKKYNYKGQPAVGPETKEISPYSIVGLSATWDVTKNVSLTG\r\n"
        + "GVDNLFDKRLWRAGNAQTTGDLAGANYIAGAGAYTYNEPGRTWYMSVNTHF";

    String EMBLNucSCDSCompleteNucSeq =
        "     gactcgacag ctctcactcc tcatttaacg ccgtcacacc ataaccatgt tactgtgcaa  \r\n"
            + "     ttttcatgat gcagaaatat attagtaata ttatgataac tatttgcatt tgcaatagcg  \r\n"
            + "     taatgcgcgc cgtggaagcg cggacattaa ttaaccaact gactgcgtgt ctttcaggga  \r\n"
            + "     tcaaaggttt tcgcggtagc gggatgcgtc gtgttgatga cgaccatgcc cgacagttgc  \r\n"
            + "     aattcgtggc aaaaatgcag gaataaaaca atgaacaaga agattcattc cctggccttg  \r\n"
            + "     ttggtcaatc tggggattta tggggtagcg caggcacaag agccgaccga tactcctgtt  \r\n"
            + "     tcacatgacg atactattgt cgttaccgcc gccgagcaga acttacaggc gcctggcgtt  \r\n"
            + "     tcgaccatca ccgcagatga aatccgcaaa aacccggttg cccgcgatgt gtcgaagatc  \r\n"
            + "     atccgtacca tgccaggcgt taacctgacc ggtaactcca ccagtggtca gcgtgggaat  \r\n"
            + "     aaccgacaga ttgatattcg cggtatgggt ccggaaaaca cgctgatttt gattgacggc  \r\n"
            + "     aagccggtaa gcagccgtaa ctcggtgcgt cagggctggc gtggcgagcg cgatacccgt  \r\n"
            + "     ggtgatactt cctgggtgcc acctgaaatg attgaacgta ttgaagttct gcgtggtccg  \r\n"
            + "     gcacgtgcgc gttatggcaa cggcgcggcg ggcggcgtgg ttaacatcat taccaaaaaa  \r\n"
            + "     ggcagcggcg agtggcacgg ctcctgggac gcatatttca atgcgccaga acataaagag  \r\n"
            + "     gaaggtgcca ccaaacgcac taactttagc ctgaccggtc cgctgggcga cgaattcagc  \r\n"
            + "     ttccgtttgt atggcaacct cgacaaaacc caggctgacg cgtgggatat caaccagggc  \r\n"
            + "     catcagtccg cgcgtgccgg aacgtatgcc acgacgttac cagccgggcg cgaaggggta  \r\n"
            + "     atcaacaaag atattaatgg cgtggtgcgc tgggatttcg cgccattgca atcgctggaa  \r\n"
            + "     ctggaagcag gttacagccg ccagggtaac ctgtatgcgg gcgacaccca gaataccaac  \r\n"
            + "     tccgattcct atacccgctc gaaatatggc gatgaaacca accgtctgta tcgccagaac  \r\n"
            + "     tacgcgctga cctggaacgg tggctgggat aacggcgtga ccaccagcaa ctgggtgcag  \r\n"
            + "     tacgaacaca cccgtaactc gcgtattccg gaaggtctgg cgggcggtac cgaagggaaa  \r\n"
            + "     tttaacgaaa aagcgacaca ggatttcgtc gatatcgatc ttgatgacgt gatgctgcac  \r\n"
            + "     agcgaagtta acctgccgat tgatttcctc gttaaccaga cgctgacgct gggtacggag  \r\n"
            + "     tggaatcagc aacggatgaa ggacttaagt tccaaccagg cactgaccgg aacgaatacc  \r\n"
            + "     ggtggcgcta ttgatggcgt gagtaccacc gaccgtagcc cgtattcaaa agcagaaatt  \r\n"
            + "     ttctcgctgt ttgccgaaaa caacatggag ctgactgaca gcaccatcgt aacgccgggg  \r\n"
            + "     ctgcgtttcg atcatcacag tattgtcggc aataactgga gcccggcgct gaacatatcg  \r\n"
            + "     caaggtttag gcgatgactt cacgctgaaa atgggcatcg cccgtgctta taaagcgccg  \r\n"
            + "     agcctgtacc agactaaccc gaactacatt ctctacagta aaggtcaggg ttgctatgcc  \r\n"
            + "     agcgcgggcg gctgctatct gcaaggtaac gatgacctga aagcagaaac cagcatcaac  \r\n"
            + "     aaagagattg gtctggagtt caaacgcgac gggtggctgg cgggcgtcac ctggttccgt  \r\n"
            + "     aacgattatc gcaataagat tgaagcgggc tatgtggctg tagggcaaaa cgcagtcggc  \r\n"
            + "     accgatctct atcagtggga taacgtgccg aaagcggtgg ttgaaggtct ggaaggatcg  \r\n"
            + "     ttaaacgtac cggttagcga aacggtgatg tggaccaata acatcactta tatgctgaag  \r\n"
            + "     agtgaaaaca aaaccacggg cgaccgtttg tcgatcatcc cggagtatac gttgaactca  \r\n"
            + "     acgctgagct ggcaggcacg ggaagatttg tcgatgcaaa cgaccttcac ctggtacggc  \r\n"
            + "     aagcagcagc cgaagaagta caactataaa ggtcagccag cggttggacc ggaaaccaaa  \r\n"
            + "     gaaattagtc cttacagcat tgttggcctg agcgcgacct gggatgtgac gaagaatgtc  \r\n"
            + "     agtctgaccg gcggcgtgga caatctgttc gacaaacgtt tgtggcgtgc gggtaatgcc  \r\n"
            + "     cagaccacgg gcgatttggc aggggccaac tatatcgccg gtgccggggc gtatacctat  \r\n"
            + "     aacgagccgg gacgtacgtg gtatatgagc gtaaacaccc acttctgatg ctaacgtcag  \r\n"
            + "     attgttgaca aagtgcgcgt cgttcatgcc ggatgcgcgg tgaacgcctt atccggccta  \r\n"
            + "     caaaaatctt gccaattcaa tatattgcag gaccatgtag gcct ";

    String emblcds = "ID   BAB72911; SV 1; linear; genomic DNA; STD; PRO; 912 BP.\r\n" + "XX\r\n"
        + "PA   BA000019.2\r\n" + "XX\r\n" + "DE   Nostoc sp. PCC 7120 hypothetical protein\r\n" + "XX\r\n"
        + "OS   Nostoc sp. PCC 7120\r\n"
        + "OC   Bacteria; Cyanobacteria; Nostocales; Nostocaceae; Nostoc.\r\n"
        + "OX   NCBI_TaxID=103690 {evidence};\r\n" + "XX\r\n" + "FH   Key             Location/Qualifiers\r\n"
        + "FH\r\n" + "FT   source          1..912\r\n"
        + "FT                   /organism=\"Nostoc sp. PCC 7120\"\r\n"
        + "FT                   /strain=\"PCC7120\"\r\n"
        + "FT                   /mol_type=\"genomic DNA\"\r\n"
        + "FT   CDS             complement(BA000019.2:1108682..1109593)\r\n"
        + "FT                   /codon_start=1\r\n" + "FT                   /transl_table=11\r\n"
        + "FT                   /gene=\"all0954\"\r\n" + "FT                   /note=\"ORF_ID:all0954\"\r\n"
        + "FT                   /note=\"similar to potassium channel protein\"\r\n"
        + "FT                   /db_xref=\"GOA:Q8YY97\"\r\n"
        + "FT                   /db_xref=\"HSSP:P63250\"\r\n"
        + "FT                   /db_xref=\"InterPro:IPR001838\"\r\n"
        + "FT                   /db_xref=\"InterPro:IPR013099\"\r\n"
        + "FT                   /db_xref=\"InterPro:IPR013518\"\r\n"
        + "FT                   /db_xref=\"InterPro:IPR013521\"\r\n"
        + "FT                   /db_xref=\"UniProtKB/TrEMBL:Q8YY97\"\r\n"
        + "FT                   /protein_id=\"BAB72911.1\"\r\n"
        + "FT                   /translation=\"MKFQLKRLAKKRPDRVQIKIQDGQFEIIGMGVWHSYWRDPYHLLL\r\n"
        + "FT                   TIPWTGFLLLICLSYLAINLIFALAYWLGGDCIANAKPGNFLDLFFFSVQTLASIGYGA\r\n"
        + "FT                   MYPKTLYANTVVTIEAMIGLVGIAVMTGLAFARFSRPSARVIFSRVAVITPYNDVPTLM\r\n"
        + "FT                   FRTANQRRNLILEAQMRVYLMRDEITAEGGFIRRFHDLKLLRNQTPSFTLSWLALHPID\r\n"
        + "FT                   EFSPLYGMSAESLIQTNTNIIISVSGIDETVAQVVHARYTYTASNILWNSRFVDIFHHT\r\n"
        + "FT                   SDGHRYIDYKYFHDVLPLDKIG\"\r\n" + "XX\r\n"
        + "SQ   Sequence 912 BP; 261 A; 179 C; 176 G; 296 T; 0 other; 3556440386 CRC32;\r\n"
        + "     atgaaatttc aactgaagag acttgcaaag aaacgaccag atagagtcca aattaaaatt        60\r\n"
        + "     caggatggac aatttgagat tattggcatg ggtgtatggc attcttactg gcgagatcct       120\r\n"
        + "     tatcatttac tgctaactat tccttggact ggctttttat tactaatttg tctttcttat       180\r\n"
        + "     ctagcaatta atctcatatt tgccctagct tattggctgg gaggagattg tatagccaat       240\r\n"
        + "     gctaaacctg gtaatttttt ggatctattc ttctttagtg tacagacact tgcatctatt       300\r\n"
        + "     ggctatggtg cgatgtatcc taaaacacta tatgccaata ctgttgttac catcgaagca       360\r\n"
        + "     atgattggtt tggtaggaat tgctgtgatg acaggactgg catttgctcg cttttcccgc       420\r\n"
        + "     ccttctgccc gtgttatatt tagccgtgtt gcagtcatca caccgtacaa tgatgtacca       480\r\n"
        + "     acgctgatgt ttcgcactgc taaccagcga cgcaatttaa ttctagaagc ccagatgcgc       540\r\n"
        + "     gtctatttaa tgcgtgacga aatcacagca gaaggaggat ttattcggcg tttccacgat       600\r\n"
        + "     ttaaaactat taagaaatca gacacctagt tttacattaa gctggctagc actgcatcct       660\r\n"
        + "     atagatgaat tcagcccttt atatggaatg tcagcagagt cgctaataca aacaaacaca       720\r\n"
        + "     aatataatta tctcggtcag tggcattgat gaaacagtag cccaagtagt tcatgcccgt       780\r\n"
        + "     tatacttata cggctagtaa tattttatgg aacagtcgct ttgttgatat ttttcaccat       840\r\n"
        + "     acatctgatg gacatcgcta catcgactat aaatattttc atgatgtctt gcctttagat       900\r\n"
        + "     aaaataggct aa                                                           912\r\n" + "//";

    RichSequence protSeq;

    RichFeature feature;

    EMBLNucleotideParser emparser = null;

    EMBLNucleotideParser emCDSparser = null;

    EMBLNucleotideParser emMultipleCDSparser = null;

    //private String brecord;

    @Override
    protected void setUp() {
        final BufferedReader br = new BufferedReader(new StringReader(this.EMBLNucSingleCDS));
        //SymbolTokenization rParser;
        try {
            DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
            //    RichSequence.IOTools.readEMBL(br, rParser, RichSequenceBuilderFactory.FACTORY,
            //        RichObjectFactory.getDefaultNamespace());
                RichSequence.IOTools.readEMBLDNA(br, null);
            this.protSeq = seqI.nextRichSequence();
            this.feature = (RichFeature) this.protSeq.getFeatureSet().iterator().next();

        } catch (final BioException e) {
            e.printStackTrace();
            Assert.fail(e.getLocalizedMessage());
        }
        this.emparser = new EMBLNucleotideParser(this.EMBLNucSingleCDS, null);
        this.emCDSparser = new EMBLNucleotideParser(this.emblcds, null);
        this.emMultipleCDSparser = new EMBLNucleotideParser(EMBLNucleotideParserTester.emblPrimer, 2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // fails now, but seems like defect in Uniprot
    public void testLinkToBadDnaSeq() throws ServiceUnavailableException, IOException, BioInfException {
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
        final String record = DBFetch.getDBrecord("Uniprot", "A3YJK9");
        System.out.println(record);
        new UniProtParser(record);
    }

    public void testQ6GS18() throws ServiceUnavailableException, IOException, BioInfException, BioException {
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
        new ProteinSequence("MTSFSTSAQC STSDSACRIS PGQINQVRPK LPLLKILHAA GAQGEMFTVK EVMHYLGQYI\r\n"
            + "     MVKQLYDQQE QHMVYCGGDL LGELLGRQSF SVKDPSPLYD MLRKNLVTLA TATTDAAQTL\r\n"
            + "     ALAQDHSMDI PSQDQLKQSA EESSTSRKRT TEDDIPTLPT SEHKCIHSRE DEDLIENLAQ\r\n"
            + "     DETSRLDLGF EEWDVAGLPW WFLGNLRSNY TPRSNGSTDL QTNQDVGTAI VSDTTDDLWF\r\n"
            + "     LNESVSEQLG VGIKVEAADT EQTSEEVGKV SDKKVIEVGK NDDLEDSKSL SDDTDVEVTS\r\n"
            + "     EDEWQCTECK KFNSPSKRYC FRCWALRKDW YSDCSKLTHS LSTSDITAIP EKENEGNDVP\r\n"
            + "     DCRRTISAPV VRPKDAYIKK ENSKLFDPCN SVEFLDLAHS SESQETISSM GEQLDNLSEQ\r\n"
            + "     RTDTENMEDC QNLLKPCSLC EKRPRDGNII HGRTGHLVTC FHCARRLKKA GASCPICKKE\r\n"
            + "     IQLVIKVFIA");
        final String record = DBFetch.getDBrecord("Uniprot", "Q6GS18");
        System.out.println(record);
        new UniProtParser(record);
    }

    
    private static final String Q5ZT67 = "ID   Q5ZT67_LEGPH            Unreviewed;       425 AA.\n" + 
    		"AC   Q5ZT67;\n" + 
    		"DT   23-NOV-2004, integrated into UniProtKB/TrEMBL.\n" + 
    		"DT   23-NOV-2004, sequence version 1.\n" + 
    		"DT   29-MAY-2013, entry version 37.\n" + 
    		"DE   SubName: Full=Inclusion membrane protein A;\n" + 
    		"GN   Name=legC7; OrderedLocusNames=lpg2298;\n" + 
    		"OS   Legionella pneumophila subsp. pneumophila (strain Philadelphia 1 /\n" + 
    		"OS   ATCC 33152 / DSM 7513).\n" + 
    		"OC   Bacteria; Proteobacteria; Gammaproteobacteria; Legionellales;\n" + 
    		"OC   Legionellaceae; Legionella.\n" + 
    		"OX   NCBI_TaxID=272624;\n" + 
    		"RN   [1]\n" + 
    		"RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\n" + 
    		"RC   STRAIN=Philadelphia 1 / ATCC 33152 / DSM 7513;\n" + 
    		"RX   PubMed=15448271; DOI=10.1126/science.1099776;\n" + 
    		"RA   Chien M., Morozova I., Shi S., Sheng H., Chen J., Gomez S.M.,\n" + 
    		"RA   Asamani G., Hill K., Nuara J., Feder M., Rineer J., Greenberg J.J.,\n" + 
    		"RA   Steshenko V., Park S.H., Zhao B., Teplitskaya E., Edwards J.R.,\n" + 
    		"RA   Pampou S., Georghiou A., Chou I.-C., Iannuccilli W., Ulz M.E.,\n" + 
    		"RA   Kim D.H., Geringer-Sameth A., Goldsberry C., Morozov P., Fischer S.G.,\n" + 
    		"RA   Segal G., Qu X., Rzhetsky A., Zhang P., Cayanis E., De Jong P.J.,\n" + 
    		"RA   Ju J., Kalachikov S., Shuman H.A., Russo J.J.;\n" + 
    		"RT   \"The genomic sequence of the accidental pathogen Legionella\n" + 
    		"RT   pneumophila.\";\n" + 
    		"RL   Science 305:1966-1968(2004).\n" + 
    		"CC   -----------------------------------------------------------------------\n" + 
    		"CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\n" + 
    		"CC   Distributed under the Creative Commons Attribution-NoDerivs License\n" + 
    		"CC   -----------------------------------------------------------------------\n" + 
    		"DR   EMBL; AE017354; AAU28360.1; -; Genomic_DNA.\n" + 
    		"DR   RefSeq; YP_007567339.1; NC_020521.1.\n" + 
    		"DR   RefSeq; YP_096307.1; NC_002942.5.\n" + 
    		"DR   ProteinModelPortal; Q5ZT67; -.\n" + 
    		"DR   STRING; 272624.lpg2298; -.\n" + 
    		"DR   EnsemblBacteria; AAU28360; AAU28360; lpg2298.\n" + 
    		"DR   GeneID; 14799488; -.\n" + 
    		"DR   GeneID; 3079356; -.\n" + 
    		"DR   KEGG; lpn:lpg2298; -.\n" + 
    		"DR   PATRIC; 22331815; VBILegPne29832_2411.\n" + 
    		"DR   eggNOG; NOG254686; -.\n" + 
    		"DR   HOGENOM; HOG000278340; -.\n" + 
    		"DR   OMA; ERICKAE; -.\n" + 
    		"DR   ProtClustDB; CLSK833083; -.\n" + 
    		"DR   BioCyc; LPNE272624:GHDI-2297-MONOMER; -.\n" + 
    		"PE   4: Predicted;\n" + 
    		"KW   Complete proteome.\n" + 
    		"SQ   SEQUENCE   425 AA;  47486 MW;  0BADE6831E42AF0A CRC64;\n" + 
    		"     MATNETELQV LIQHDSKSTI TTSSLDSTDK DPKASTGTPE IESASLAQIV DTQKQLSQVK\n" + 
    		"     ESLESIVDSI AENPSLITRA ASAWGELPMW QKVTGGLVLT APTLAVGLFA HIGVLLVIGG\n" + 
    		"     VTGLTYTAGA IVLDDHHTCN VNIAKRLKEG LFGLADLLQI TIEALDAIRK KFAEEIEKFK\n" + 
    		"     NENLRLTDNI DRLGNEVESL SAQVELYMEI EKMLRKDTNE MEQTVKKLQE STTKQTDLLE\n" + 
    		"     KNQKELSKIR KEYEKSQLQL AEKIAELHEV RLSLGLEVQK AKTVAKTLEG TVQTLTGTVI\n" + 
    		"     ADEEQRVSFQ KKLDGFLNDK QLSFDQVAER ICKAEEELKK VKEELRQSND RYSELLKRQE\n" + 
    		"     QQVERLEKLG LHKLERIVDK ENVKPSNDPV HSGLLSHGIY STPKGKVTQP KVEVVEDRQT\n" + 
    		"     IALVN\n" + 
    		"//";

    public void testQ5ZT67() throws ServiceUnavailableException, IOException, BioInfException, BioException {
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
        
       // final String record = DBFetch.getDBrecord("Uniprot", "Q5ZT67");
       // assertEquals(Q5ZT67, record);
       String cleaned = Q5ZT67.replaceFirst("OS.*\nOS", "OS");
        new UniProtParser(Q5ZT67);
        //new UniProtParser(record);
    }

    
    public void testNonesuch() throws ServiceUnavailableException, IOException, BioInfException, BioException {
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();

        try {
            final String record = DBFetch.getDBrecord("Uniprot", "nonesuch");
            Assert.assertEquals("", record);
        } catch (final IOException e) {
            // this might also be OK
            Assert.assertTrue(e.getMessage().contains("500"));
        }
    }

    public void testGetDNASequence() {
        final String nucSeq = this.emparser.getDnaSequence();
        Assert.assertNotNull(nucSeq);
        Assert.assertEquals(2238, nucSeq.length());
        final String clearCompNucSeq = this.EMBLNucSCDSCompleteNucSeq.replace("\r\n", "").replace(" ", "");
        Assert.assertNotSame(clearCompNucSeq, nucSeq);
        //System.out.println(clearCompNucSeq.length() + " AA " + nucSeq.length());
        Assert.assertTrue(clearCompNucSeq.length() > nucSeq.length());
        //final String protseq = this.emparser.getProteinSequence().replace("\r\n", "").replace(" ", "");

        Assert.assertTrue(nucSeq, nucSeq.startsWith("atgaacaagaagattcattc")); // start of coding sequence
    }

    public void testGetProteinSequence() {
        final String seq = this.emparser.getProteinSequence();
        Assert.assertNotNull(seq);
        Assert.assertEquals(this.EMBLNucSCDSProtSeq.replace("\r\n", "").replace(" ", ""), seq);
    }

    public void testGetAccession() {
        final String acc = this.emparser.getAccession();
        Assert.assertNotNull(acc);
        Assert.assertEquals("M13748", acc);
    }

    public void testGetDatabaseRelease() {
        /*
          Annotation seqAn = protSeq.getAnnotation();
            for (Iterator iterator = protSeq.getAnnotation().keys().iterator(); iterator.hasNext();) {
                SimpleComparableTerm name = (SimpleComparableTerm) iterator.next();
                System.out.println(name.getName());
                System.out.println(protSeq.getAnnotation().getProperty(name));
            }
        */
        final String r = this.emparser.getSequenceRelease();
        Assert.assertNotNull(r);
        Assert.assertEquals("04-MAR-2000/rev 63", r);
    }

    public void testXReferences() {
        this.emparser.getXaccessions();
        //System.out.println(br);
    }

    public void testGetTaxID() {
    	assertTrue(TaxonomyRecordExtractor.OX.matcher("OX NCBI_TaxID=103690;\r").matches());
    	assertTrue(TaxonomyRecordExtractor.OX.matcher("OX NCBI_TaxID=103690 {evidence};").matches());
        Assert.assertNotNull(this.emCDSparser.getTaxId());
        Assert.assertEquals(103690, this.emCDSparser.getTaxId());
    }

    public void testGetFunctionalDescription() {
        Assert.assertNotNull(this.emparser.getFuncDescription());
        Assert.assertEquals("Escherichia coli ferrienterochelin receptor (fepA) gene, complete cds.",
            this.emparser.getFuncDescription());
    }

    public void testGetProteinSeq() {
        final String fepDPSeq =
            "MTESVARLRGEQLTLGYGKYTVAENLTVEIPDGHFTAIIGPNGCG"
                + "KSTLLRTLSRLMTPAHGHVWLDGEHIQHYASKEVARRIGLLAQNATTPGDITVQELVAR"
                + "GRYPHQPLFTRWRKEDEEAVTKAMQATGITHLADQSVDTLSGGQRQRAWIAMVLAQETA"
                + "IMLLDEPTTWLDISHQIDLLELLSELNREKGYTLAAVLHDLNQACRYASHLIALREGKI"
                + "VAQGRPKEIVTAELIERIYGLRCMIIDDPVAGTPLVVPLGRTAPSTANS".replace(" ", "").replace("\n", "");

        Assert.assertNotNull(this.emMultipleCDSparser.getProteinSequence());
        Assert.assertEquals(fepDPSeq, this.emMultipleCDSparser.getProteinSequence());
        Assert.assertEquals("fepC", this.emMultipleCDSparser.getGeneName());

    }

    public void testGetComments() {
        Assert.assertEquals(EMBLNucleotideParserTester.COMMENTS.replace("\r\nCC   ", "\n"),
            this.emparser.getComments());
    }

    public void testGetTargetName() {
        Assert.assertEquals("FepA", this.emparser.getTargetName());
    }

    public void testGetSpeciesName() {
        Assert.assertEquals("Escherichia coli", this.emparser.getSpeciesName());
    }

    public void testGetSourceType() {
        Assert.assertEquals("FepA", this.emparser.getProteinName());
    }

}
