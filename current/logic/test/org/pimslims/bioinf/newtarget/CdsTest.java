/**
 * pims-web org.pimslims.command.newtarget GenBankToPIMSTester.java
 * 
 * @author pvt43
 * @date 17 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.StringReader;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.pimslims.bioinf.CdsList;
import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;

/**
 * BioJavaGenBankNucleotideParserTester. This is a Tester for BioJava genbank nucleotide sequence parser. It
 * is useful as reference on how to get information out of the parsed file.
 */
public class CdsTest extends TestCase {

    RichSequence bio = null;

    ParserUtility gbm = null;

    static final String genBankNucleotide =
        "LOCUS       SCU49845     5028 bp    DNA             PLN       21-JUN-1999\r\n"
            + "DEFINITION  Saccharomyces cerevisiae TCP1-beta gene, partial cds, and Axl2p\r\n"
            + "            (AXL2) and Rev7p (REV7) genes, complete cds.\r\n" + "ACCESSION   U49845\r\n"
            + "VERSION     U49845.1  GI:1293613\r\n" + "KEYWORDS    .\r\n"
            + "SOURCE      Saccharomyces cerevisiae (baker\'s yeast)\r\n"
            + "  ORGANISM  Saccharomyces cerevisiae\r\n"
            + "            Eukaryota; Fungi; Ascomycota; Saccharomycotina; Saccharomycetes;\r\n"
            + "            Saccharomycetales; Saccharomycetaceae; Saccharomyces.\r\n"
            + "REFERENCE   1  (bases 1 to 5028)\r\n"
            + "  AUTHORS   Torpey,L.E., Gibbs,P.E., Nelson,J. and Lawrence,C.W.\r\n"
            + "  TITLE     Cloning and sequence of REV7, a gene whose function is required for\r\n"
            + "            DNA damage-induced mutagenesis in Saccharomyces cerevisiae\r\n"
            + "  JOURNAL   Yeast 10 (11), 1503-1509 (1994)\r\n" + "  PUBMED    7871890\r\n"
            + "REFERENCE   2  (bases 1 to 5028)\r\n"
            + "  AUTHORS   Roemer,T., Madden,K., Chang,J. and Snyder,M.\r\n"
            + "  TITLE     Selection of axial growth sites in yeast requires Axl2p, a novel\r\n"
            + "            plasma membrane glycoprotein\r\n"
            + "  JOURNAL   Genes Dev. 10 (7), 777-793 (1996)\r\n" + "  PUBMED    8846915\r\n"
            + "REFERENCE   3  (bases 1 to 5028)\r\n" + "  AUTHORS   Roemer,T.\r\n"
            + "  TITLE     Direct Submission\r\n"
            + "  JOURNAL   Submitted (22-FEB-1996) Terry Roemer, Biology, Yale University, New\r\n"
            + "            Haven, CT, USA\r\n" + "FEATURES             Location/Qualifiers\r\n"
            + "     source          1..5028\r\n"
            + "                     /organism=\"Saccharomyces cerevisiae\"\r\n"
            + "                     /db_xref=\"taxon:4932\"\r\n"
            + "                     /chromosome=\"IX\"\r\n" + "                     /map=\"9\"\r\n"
            + "     CDS             <1..206\r\n" + "                     /codon_start=3\r\n"
            + "                     /product=\"TCP1-beta\"\r\n"
            + "                     /protein_id=\"AAA98665.1\"\r\n"
            + "                     /db_xref=\"GI:1293614\"\r\n"
            + "                     /translation=\"SSIYNGISTSGLDLNNGTIADMRQLGIVESYKLKRAVVSSASEA\r\n"
            + "                     AEVLLRVDNIIRARPRTANRQHM\"\r\n" + "     gene            687..3158\r\n"
            + "                     /gene=\"AXL2\"\r\n" + "     CDS             687..3158\r\n"
            + "                     /gene=\"AXL2\"\r\n"
            + "                     /note=\"plasma membrane glycoprotein\"\r\n"
            + "                     /codon_start=1\r\n"
            + "                     /function=\"required for axial budding pattern of S.\r\n"
            + "                     cerevisiae\"\r\n" + "                     /product=\"Axl2p\"\r\n"
            + "                     /protein_id=\"AAA98666.1\"\r\n"
            + "                     /db_xref=\"GI:1293615\"\r\n"
            + "                     /translation=\"MTQLQISLLLTATISLLHLVVATPYEAYPIGKQYPPVARVNESF\r\n"
            + "                     TFQISNDTYKSSVDKTAQITYNCFDLPSWLSFDSSSRTFSGEPSSDLLSDANTTLYFN\r\n"
            + "                     VILEGTDSADSTSLNNTYQFVVTNRPSISLSSDFNLLALLKNYGYTNGKNALKLDPNE\r\n"
            + "                     VFNVTFDRSMFTNEESIVSYYGRSQLYNAPLPNWLFFDSGELKFTGTAPVINSAIAPE\r\n"
            + "                     TSYSFVIIATDIEGFSAVEVEFELVIGAHQLTTSIQNSLIINVTDTGNVSYDLPLNYV\r\n"
            + "                     YLDDDPISSDKLGSINLLDAPDWVALDNATISGSVPDELLGKNSNPANFSVSIYDTYG\r\n"
            + "                     DVIYFNFEVVSTTDLFAISSLPNINATRGEWFSYYFLPSQFTDYVNTNVSLEFTNSSQ\r\n"
            + "                     DHDWVKFQSSNLTLAGEVPKNFDKLSLGLKANQGSQSQELYFNIIGMDSKITHSNHSA\r\n"
            + "                     NATSTRSSHHSTSTSSYTSSTYTAKISSTSAAATSSAPAALPAANKTSSHNKKAVAIA\r\n"
            + "                     CGVAIPLGVILVALICFLIFWRRRRENPDDENLPHAISGPDLNNPANKPNQENATPLN\r\n"
            + "                     NPFDDDASSYDDTSIARRLAALNTLKLDNHSATESDISSVDEKRDSLSGMNTYNDQFQ\r\n"
            + "                     SQSKEELLAKPPVQPPESPFFDPQNRSSSVYMDSEPAVNKSWRYTGNLSPVSDIVRDS\r\n"
            + "                     YGSQKTVDTEKLFDLEAPEKEKRTSRDVTMSSLDPWNSNISPSPVRKSVTPSPYNVTK\r\n"
            + "                     HRNRHLQNIQDSQSGKNGITPTTMSTSSSDDFVPVKDGENFCWVHSMEPDRRPSKKRL\r\n"
            + "                     VDFSNKSNVNVGQVKDIHGRIPEML\"\r\n"
            + "     gene            complement(3300..4037)\r\n" + "                     /gene=\"REV7\"\r\n"
            + "     CDS             complement(3300..4037)\r\n" + "                     /gene=\"REV7\"\r\n"
            + "                     /codon_start=1\r\n" + "                     /product=\"Rev7p\"\r\n"
            + "                     /protein_id=\"AAA98667.1\"\r\n"
            + "                     /db_xref=\"GI:1293616\"\r\n"
            + "                     /translation=\"MNRWVEKWLRVYLKCYINLILFYRNVYPPQSFDYTTYQSFNLPQ\r\n"
            + "                     FVPINRHPALIDYIEELILDVLSKLTHVYRFSICIINKKNDLCIEKYVLDFSELQHVD\r\n"
            + "                     KDDQIITETEVFDEFRSSLNSLIMHLEKLPKVNDDTITFEAVINAIELELGHKLDRNR\r\n"
            + "                     RVDSLEEKAEIERDSNWVKCQEDENLPDNNGFQPPKIKLTSLVGSDVGPLIIHQFSEK\r\n"
            + "                     LISGDDKILNGVYSQYEEGESIFGSLF\"\r\n" + "ORIGIN\r\n"
            + "        1 gatcctccat atacaacggt atctccacct caggtttaga tctcaacaac ggaaccattg\r\n"
            + "       61 ccgacatgag acagttaggt atcgtcgaga gttacaagct aaaacgagca gtagtcagct\r\n"
            + "      121 ctgcatctga agccgctgaa gttctactaa gggtggataa catcatccgt gcaagaccaa\r\n"
            + "      181 gaaccgccaa tagacaacat atgtaacata tttaggatat acctcgaaaa taataaaccg\r\n"
            + "      241 ccacactgtc attattataa ttagaaacag aacgcaaaaa ttatccacta tataattcaa\r\n"
            + "      301 agacgcgaaa aaaaaagaac aacgcgtcat agaacttttg gcaattcgcg tcacaaataa\r\n"
            + "      361 attttggcaa cttatgtttc ctcttcgagc agtactcgag ccctgtctca agaatgtaat\r\n"
            + "      421 aatacccatc gtaggtatgg ttaaagatag catctccaca acctcaaagc tccttgccga\r\n"
            + "      481 gagtcgccct cctttgtcga gtaattttca cttttcatat gagaacttat tttcttattc\r\n"
            + "      541 tttactctca catcctgtag tgattgacac tgcaacagcc accatcacta gaagaacaga\r\n"
            + "      601 acaattactt aatagaaaaa ttatatcttc ctcgaaacga tttcctgctt ccaacatcta\r\n"
            + "      661 cgtatatcaa gaagcattca cttaccatga cacagcttca gatttcatta ttgctgacag\r\n"
            + "      721 ctactatatc actactccat ctagtagtgg ccacgcccta tgaggcatat cctatcggaa\r\n"
            + "      781 aacaataccc cccagtggca agagtcaatg aatcgtttac atttcaaatt tccaatgata\r\n"
            + "      841 cctataaatc gtctgtagac aagacagctc aaataacata caattgcttc gacttaccga\r\n"
            + "      901 gctggctttc gtttgactct agttctagaa cgttctcagg tgaaccttct tctgacttac\r\n"
            + "      961 tatctgatgc gaacaccacg ttgtatttca atgtaatact cgagggtacg gactctgccg\r\n"
            + "     1021 acagcacgtc tttgaacaat acataccaat ttgttgttac aaaccgtcca tccatctcgc\r\n"
            + "     1081 tatcgtcaga tttcaatcta ttggcgttgt taaaaaacta tggttatact aacggcaaaa\r\n"
            + "     1141 acgctctgaa actagatcct aatgaagtct tcaacgtgac ttttgaccgt tcaatgttca\r\n"
            + "     1201 ctaacgaaga atccattgtg tcgtattacg gacgttctca gttgtataat gcgccgttac\r\n"
            + "     1261 ccaattggct gttcttcgat tctggcgagt tgaagtttac tgggacggca ccggtgataa\r\n"
            + "     1321 actcggcgat tgctccagaa acaagctaca gttttgtcat catcgctaca gacattgaag\r\n"
            + "     1381 gattttctgc cgttgaggta gaattcgaat tagtcatcgg ggctcaccag ttaactacct\r\n"
            + "     1441 ctattcaaaa tagtttgata atcaacgtta ctgacacagg taacgtttca tatgacttac\r\n"
            + "     1501 ctctaaacta tgtttatctc gatgacgatc ctatttcttc tgataaattg ggttctataa\r\n"
            + "     1561 acttattgga tgctccagac tgggtggcat tagataatgc taccatttcc gggtctgtcc\r\n"
            + "     1621 cagatgaatt actcggtaag aactccaatc ctgccaattt ttctgtgtcc atttatgata\r\n"
            + "     1681 cttatggtga tgtgatttat ttcaacttcg aagttgtctc cacaacggat ttgtttgcca\r\n"
            + "     1741 ttagttctct tcccaatatt aacgctacaa ggggtgaatg gttctcctac tattttttgc\r\n"
            + "     1801 cttctcagtt tacagactac gtgaatacaa acgtttcatt agagtttact aattcaagcc\r\n"
            + "     1861 aagaccatga ctgggtgaaa ttccaatcat ctaatttaac attagctgga gaagtgccca\r\n"
            + "     1921 agaatttcga caagctttca ttaggtttga aagcgaacca aggttcacaa tctcaagagc\r\n"
            + "     1981 tatattttaa catcattggc atggattcaa agataactca ctcaaaccac agtgcgaatg\r\n"
            + "     2041 caacgtccac aagaagttct caccactcca cctcaacaag ttcttacaca tcttctactt\r\n"
            + "     2101 acactgcaaa aatttcttct acctccgctg ctgctacttc ttctgctcca gcagcgctgc\r\n"
            + "     2161 cagcagccaa taaaacttca tctcacaata aaaaagcagt agcaattgcg tgcggtgttg\r\n"
            + "     2221 ctatcccatt aggcgttatc ctagtagctc tcatttgctt cctaatattc tggagacgca\r\n"
            + "     2281 gaagggaaaa tccagacgat gaaaacttac cgcatgctat tagtggacct gatttgaata\r\n"
            + "     2341 atcctgcaaa taaaccaaat caagaaaacg ctacaccttt gaacaacccc tttgatgatg\r\n"
            + "     2401 atgcttcctc gtacgatgat acttcaatag caagaagatt ggctgctttg aacactttga\r\n"
            + "     2461 aattggataa ccactctgcc actgaatctg atatttccag cgtggatgaa aagagagatt\r\n"
            + "     2521 ctctatcagg tatgaataca tacaatgatc agttccaatc ccaaagtaaa gaagaattat\r\n"
            + "     2581 tagcaaaacc cccagtacag cctccagaga gcccgttctt tgacccacag aataggtctt\r\n"
            + "     2641 cttctgtgta tatggatagt gaaccagcag taaataaatc ctggcgatat actggcaacc\r\n"
            + "     2701 tgtcaccagt ctctgatatt gtcagagaca gttacggatc acaaaaaact gttgatacag\r\n"
            + "     2761 aaaaactttt cgatttagaa gcaccagaga aggaaaaacg tacgtcaagg gatgtcacta\r\n"
            + "     2821 tgtcttcact ggacccttgg aacagcaata ttagcccttc tcccgtaaga aaatcagtaa\r\n"
            + "     2881 caccatcacc atataacgta acgaagcatc gtaaccgcca cttacaaaat attcaagact\r\n"
            + "     2941 ctcaaagcgg taaaaacgga atcactccca caacaatgtc aacttcatct tctgacgatt\r\n"
            + "     3001 ttgttccggt taaagatggt gaaaattttt gctgggtcca tagcatggaa ccagacagaa\r\n"
            + "     3061 gaccaagtaa gaaaaggtta gtagattttt caaataagag taatgtcaat gttggtcaag\r\n"
            + "     3121 ttaaggacat tcacggacgc atcccagaaa tgctgtgatt atacgcaacg atattttgct\r\n"
            + "     3181 taattttatt ttcctgtttt attttttatt agtggtttac agatacccta tattttattt\r\n"
            + "     3241 agtttttata cttagagaca tttaatttta attccattct tcaaatttca tttttgcact\r\n"
            + "     3301 taaaacaaag atccaaaaat gctctcgccc tcttcatatt gagaatacac tccattcaaa\r\n"
            + "     3361 attttgtcgt caccgctgat taatttttca ctaaactgat gaataatcaa aggccccacg\r\n"
            + "     3421 tcagaaccga ctaaagaagt gagttttatt ttaggaggtt gaaaaccatt attgtctggt\r\n"
            + "     3481 aaattttcat cttcttgaca tttaacccag tttgaatccc tttcaatttc tgctttttcc\r\n"
            + "     3541 tccaaactat cgaccctcct gtttctgtcc aacttatgtc ctagttccaa ttcgatcgca\r\n"
            + "     3601 ttaataactg cttcaaatgt tattgtgtca tcgttgactt taggtaattt ctccaaatgc\r\n"
            + "     3661 ataatcaaac tatttaagga agatcggaat tcgtcgaaca cttcagtttc cgtaatgatc\r\n"
            + "     3721 tgatcgtctt tatccacatg ttgtaattca ctaaaatcta aaacgtattt ttcaatgcat\r\n"
            + "     3781 aaatcgttct ttttattaat aatgcagatg gaaaatctgt aaacgtgcgt taatttagaa\r\n"
            + "     3841 agaacatcca gtataagttc ttctatatag tcaattaaag caggatgcct attaatggga\r\n"
            + "     3901 acgaactgcg gcaagttgaa tgactggtaa gtagtgtagt cgaatgactg aggtgggtat\r\n"
            + "     3961 acatttctat aaaataaaat caaattaatg tagcatttta agtataccct cagccacttc\r\n"
            + "     4021 tctacccatc tattcataaa gctgacgcaa cgattactat tttttttttc ttcttggatc\r\n"
            + "     4081 tcagtcgtcg caaaaacgta taccttcttt ttccgacctt ttttttagct ttctggaaaa\r\n"
            + "     4141 gtttatatta gttaaacagg gtctagtctt agtgtgaaag ctagtggttt cgattgactg\r\n"
            + "     4201 atattaagaa agtggaaatt aaattagtag tgtagacgta tatgcatatg tatttctcgc\r\n"
            + "     4261 ctgtttatgt ttctacgtac ttttgattta tagcaagggg aaaagaaata catactattt\r\n"
            + "     4321 tttggtaaag gtgaaagcat aatgtaaaag ctagaataaa atggacgaaa taaagagagg\r\n"
            + "     4381 cttagttcat cttttttcca aaaagcaccc aatgataata actaaaatga aaaggatttg\r\n"
            + "     4441 ccatctgtca gcaacatcag ttgtgtgagc aataataaaa tcatcacctc cgttgccttt\r\n"
            + "     4501 agcgcgtttg tcgtttgtat cttccgtaat tttagtctta tcaatgggaa tcataaattt\r\n"
            + "     4561 tccaatgaat tagcaatttc gtccaattct ttttgagctt cttcatattt gctttggaat\r\n"
            + "     4621 tcttcgcact tcttttccca ttcatctctt tcttcttcca aagcaacgat ccttctaccc\r\n"
            + "     4681 atttgctcag agttcaaatc ggcctctttc agtttatcca ttgcttcctt cagtttggct\r\n"
            + "     4741 tcactgtctt ctagctgttg ttctagatcc tggtttttct tggtgtagtt ctcattatta\r\n"
            + "     4801 gatctcaagt tattggagtc ttcagccaat tgctttgtat cagacaattg actctctaac\r\n"
            + "     4861 ttctccactt cactgtcgag ttgctcgttt ttagcggaca aagatttaat ctcgttttct\r\n"
            + "     4921 ttttcagtgt tagattgctc taattctttg agctgttctc tcagctcctc atatttttct\r\n"
            + "     4981 tgccatgact cagattctaa ttttaagcta ttcaatttct ctttgatc\r\n" + "//";

    String nucSequence = "gatcctccat atacaacggt atctccacct caggtttaga tctcaacaac ggaaccattg\r\n"
        + "ccgacatgag acagttaggt atcgtcgaga gttacaagct aaaacgagca gtagtcagct\r\n"
        + "ctgcatctga agccgctgaa gttctactaa gggtggataa catcatccgt gcaagaccaa\r\n"
        + "gaaccgccaa tagacaacat atgtaacata tttaggatat acctcgaaaa taataaaccg\r\n"
        + "ccacactgtc attattataa ttagaaacag aacgcaaaaa ttatccacta tataattcaa\r\n"
        + "agacgcgaaa aaaaaagaac aacgcgtcat agaacttttg gcaattcgcg tcacaaataa\r\n"
        + "attttggcaa cttatgtttc ctcttcgagc agtactcgag ccctgtctca agaatgtaat\r\n"
        + "aatacccatc gtaggtatgg ttaaagatag catctccaca acctcaaagc tccttgccga\r\n"
        + "gagtcgccct cctttgtcga gtaattttca cttttcatat gagaacttat tttcttattc\r\n"
        + "tttactctca catcctgtag tgattgacac tgcaacagcc accatcacta gaagaacaga\r\n"
        + "acaattactt aatagaaaaa ttatatcttc ctcgaaacga tttcctgctt ccaacatcta\r\n"
        + "cgtatatcaa gaagcattca cttaccatga cacagcttca gatttcatta ttgctgacag\r\n"
        + "ctactatatc actactccat ctagtagtgg ccacgcccta tgaggcatat cctatcggaa\r\n"
        + "aacaataccc cccagtggca agagtcaatg aatcgtttac atttcaaatt tccaatgata\r\n"
        + "cctataaatc gtctgtagac aagacagctc aaataacata caattgcttc gacttaccga\r\n"
        + "gctggctttc gtttgactct agttctagaa cgttctcagg tgaaccttct tctgacttac\r\n"
        + "tatctgatgc gaacaccacg ttgtatttca atgtaatact cgagggtacg gactctgccg\r\n"
        + "acagcacgtc tttgaacaat acataccaat ttgttgttac aaaccgtcca tccatctcgc\r\n"
        + "tatcgtcaga tttcaatcta ttggcgttgt taaaaaacta tggttatact aacggcaaaa\r\n"
        + "acgctctgaa actagatcct aatgaagtct tcaacgtgac ttttgaccgt tcaatgttca\r\n"
        + "ctaacgaaga atccattgtg tcgtattacg gacgttctca gttgtataat gcgccgttac\r\n"
        + "ccaattggct gttcttcgat tctggcgagt tgaagtttac tgggacggca ccggtgataa\r\n"
        + "actcggcgat tgctccagaa acaagctaca gttttgtcat catcgctaca gacattgaag\r\n"
        + "gattttctgc cgttgaggta gaattcgaat tagtcatcgg ggctcaccag ttaactacct\r\n"
        + "ctattcaaaa tagtttgata atcaacgtta ctgacacagg taacgtttca tatgacttac\r\n"
        + "ctctaaacta tgtttatctc gatgacgatc ctatttcttc tgataaattg ggttctataa\r\n"
        + "acttattgga tgctccagac tgggtggcat tagataatgc taccatttcc gggtctgtcc\r\n"
        + "cagatgaatt actcggtaag aactccaatc ctgccaattt ttctgtgtcc atttatgata\r\n"
        + "cttatggtga tgtgatttat ttcaacttcg aagttgtctc cacaacggat ttgtttgcca\r\n"
        + "ttagttctct tcccaatatt aacgctacaa ggggtgaatg gttctcctac tattttttgc\r\n"
        + "cttctcagtt tacagactac gtgaatacaa acgtttcatt agagtttact aattcaagcc\r\n"
        + "aagaccatga ctgggtgaaa ttccaatcat ctaatttaac attagctgga gaagtgccca\r\n"
        + "agaatttcga caagctttca ttaggtttga aagcgaacca aggttcacaa tctcaagagc\r\n"
        + "tatattttaa catcattggc atggattcaa agataactca ctcaaaccac agtgcgaatg\r\n"
        + "caacgtccac aagaagttct caccactcca cctcaacaag ttcttacaca tcttctactt\r\n"
        + "acactgcaaa aatttcttct acctccgctg ctgctacttc ttctgctcca gcagcgctgc\r\n"
        + "cagcagccaa taaaacttca tctcacaata aaaaagcagt agcaattgcg tgcggtgttg\r\n"
        + "ctatcccatt aggcgttatc ctagtagctc tcatttgctt cctaatattc tggagacgca\r\n"
        + "gaagggaaaa tccagacgat gaaaacttac cgcatgctat tagtggacct gatttgaata\r\n"
        + "atcctgcaaa taaaccaaat caagaaaacg ctacaccttt gaacaacccc tttgatgatg\r\n"
        + "atgcttcctc gtacgatgat acttcaatag caagaagatt ggctgctttg aacactttga\r\n"
        + "aattggataa ccactctgcc actgaatctg atatttccag cgtggatgaa aagagagatt\r\n"
        + "ctctatcagg tatgaataca tacaatgatc agttccaatc ccaaagtaaa gaagaattat\r\n"
        + "tagcaaaacc cccagtacag cctccagaga gcccgttctt tgacccacag aataggtctt\r\n"
        + "cttctgtgta tatggatagt gaaccagcag taaataaatc ctggcgatat actggcaacc\r\n"
        + "tgtcaccagt ctctgatatt gtcagagaca gttacggatc acaaaaaact gttgatacag\r\n"
        + "aaaaactttt cgatttagaa gcaccagaga aggaaaaacg tacgtcaagg gatgtcacta\r\n"
        + "tgtcttcact ggacccttgg aacagcaata ttagcccttc tcccgtaaga aaatcagtaa\r\n"
        + "caccatcacc atataacgta acgaagcatc gtaaccgcca cttacaaaat attcaagact\r\n"
        + "ctcaaagcgg taaaaacgga atcactccca caacaatgtc aacttcatct tctgacgatt\r\n"
        + "ttgttccggt taaagatggt gaaaattttt gctgggtcca tagcatggaa ccagacagaa\r\n"
        + "gaccaagtaa gaaaaggtta gtagattttt caaataagag taatgtcaat gttggtcaag\r\n"
        + "ttaaggacat tcacggacgc atcccagaaa tgctgtgatt atacgcaacg atattttgct\r\n"
        + "taattttatt ttcctgtttt attttttatt agtggtttac agatacccta tattttattt\r\n"
        + "agtttttata cttagagaca tttaatttta attccattct tcaaatttca tttttgcact\r\n"
        + "taaaacaaag atccaaaaat gctctcgccc tcttcatatt gagaatacac tccattcaaa\r\n"
        + "attttgtcgt caccgctgat taatttttca ctaaactgat gaataatcaa aggccccacg\r\n"
        + "tcagaaccga ctaaagaagt gagttttatt ttaggaggtt gaaaaccatt attgtctggt\r\n"
        + "aaattttcat cttcttgaca tttaacccag tttgaatccc tttcaatttc tgctttttcc\r\n"
        + "tccaaactat cgaccctcct gtttctgtcc aacttatgtc ctagttccaa ttcgatcgca\r\n"
        + "ttaataactg cttcaaatgt tattgtgtca tcgttgactt taggtaattt ctccaaatgc\r\n"
        + "ataatcaaac tatttaagga agatcggaat tcgtcgaaca cttcagtttc cgtaatgatc\r\n"
        + "tgatcgtctt tatccacatg ttgtaattca ctaaaatcta aaacgtattt ttcaatgcat\r\n"
        + "aaatcgttct ttttattaat aatgcagatg gaaaatctgt aaacgtgcgt taatttagaa\r\n"
        + "agaacatcca gtataagttc ttctatatag tcaattaaag caggatgcct attaatggga\r\n"
        + "acgaactgcg gcaagttgaa tgactggtaa gtagtgtagt cgaatgactg aggtgggtat\r\n"
        + "acatttctat aaaataaaat caaattaatg tagcatttta agtataccct cagccacttc\r\n"
        + "tctacccatc tattcataaa gctgacgcaa cgattactat tttttttttc ttcttggatc\r\n"
        + "tcagtcgtcg caaaaacgta taccttcttt ttccgacctt ttttttagct ttctggaaaa\r\n"
        + "gtttatatta gttaaacagg gtctagtctt agtgtgaaag ctagtggttt cgattgactg\r\n"
        + "atattaagaa agtggaaatt aaattagtag tgtagacgta tatgcatatg tatttctcgc\r\n"
        + "ctgtttatgt ttctacgtac ttttgattta tagcaagggg aaaagaaata catactattt\r\n"
        + "tttggtaaag gtgaaagcat aatgtaaaag ctagaataaa atggacgaaa taaagagagg\r\n"
        + "cttagttcat cttttttcca aaaagcaccc aatgataata actaaaatga aaaggatttg\r\n"
        + "ccatctgtca gcaacatcag ttgtgtgagc aataataaaa tcatcacctc cgttgccttt\r\n"
        + "agcgcgtttg tcgtttgtat cttccgtaat tttagtctta tcaatgggaa tcataaattt\r\n"
        + "tccaatgaat tagcaatttc gtccaattct ttttgagctt cttcatattt gctttggaat\r\n"
        + "tcttcgcact tcttttccca ttcatctctt tcttcttcca aagcaacgat ccttctaccc\r\n"
        + "atttgctcag agttcaaatc ggcctctttc agtttatcca ttgcttcctt cagtttggct\r\n"
        + "tcactgtctt ctagctgttg ttctagatcc tggtttttct tggtgtagtt ctcattatta\r\n"
        + "gatctcaagt tattggagtc ttcagccaat tgctttgtat cagacaattg actctctaac\r\n"
        + "ttctccactt cactgtcgag ttgctcgttt ttagcggaca aagatttaat ctcgttttct\r\n"
        + "ttttcagtgt tagattgctc taattctttg agctgttctc tcagctcctc atatttttct\r\n"
        + "tgccatgact cagattctaa ttttaagcta ttcaatttct ctttgatc";

    /**
     * @param name
     */
    public CdsTest(final String name) {
        super(name);
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final BufferedReader br =
            new BufferedReader(new StringReader(CdsTest.genBankNucleotide));
        final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
        final RichSequenceIterator seqI =
            RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                RichObjectFactory.getDefaultNamespace());
        this.bio = seqI.nextRichSequence();

        this.gbm = new ParserUtility(CdsTest.genBankNucleotide);
        //Alternatively 
        //RichSequenceIterator seqI =
        //   RichSequence.IOTools.readGenbankProtein(br, RichObjectFactory.getDefaultNamespace());
        //  RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.bio = null;
    }

    public void testGetNucleotideSequence() {
        final String seq = this.nucSequence.replaceAll(" ", "").replaceAll("\r\n", "").trim();
        Assert.assertTrue("Sequences are not identical! ", seq.equalsIgnoreCase(this.bio.seqString()));
    }

    public void testAccession() {
        Assert.assertEquals("U49845", this.bio.getAccession()); // Nice to have this: 46401682
    }

    public void testName() {
        Assert.assertEquals("SCU49845", this.bio.getName()); // get it from the product
    }

    public void testTaxId() {
        Assert.assertEquals(4932, this.bio.getTaxon().getNCBITaxID()); // see taxon on the source feature
    }

    public void testOrganismFromTaxId() {
        Assert.assertEquals("Saccharomyces cerevisiae", this.bio.getTaxon().getDisplayName()); // see taxon on the source feature
    }

    public void testCDSExtract() {
        final FeatureHolder cdslist = this.gbm.getCDSList();
        Assert.assertNotNull(cdslist);
        Assert.assertEquals(3, cdslist.countFeatures());
        org.biojavax.bio.seq.RichFeature rf = this.gbm.getCDS(0);
        Assert.assertNotNull(rf);
        //System.out.println(rf.getName());
        //
        final RichFeature f2 = this.gbm.getCDSByAnnotation("protein_id", "AAA98665.1");
        final RichFeature f3 = this.gbm.getCDSByAnnotation("protein_id", "AAA98666.1");
        Assert.assertNotNull(f2);
        Assert.assertNotNull(f3);
        Assert.assertEquals(rf, f2);
        Assert.assertNotSame(rf, f3);
        Assert.assertNotSame(f2, f3);
        rf = this.gbm.getCDS(1);
        Assert.assertNotNull(rf);
        Assert.assertEquals(rf, f3);
        Assert.assertNotSame(rf, f2);
    }

    // This will test whether result do not differ from one execution to another
    public void testCDSExtractReliability() {
        Assert.assertFalse(ParserUtility.isSingleCDS(new CdsList(this.bio)));
        final FeatureHolder cdslist = this.gbm.getCDSList();
        Assert.assertNotNull(cdslist);
        Assert.assertEquals(3, cdslist.countFeatures());
        org.biojavax.bio.seq.RichFeature rf = this.gbm.getCDS(0);
        Assert.assertNotNull(rf);
        //System.out.println(rf.getName());
        //
        final RichFeature f2 = this.gbm.getCDSByAnnotation("protein_id", "AAA98665.1");
        final RichFeature f3 = this.gbm.getCDSByAnnotation("protein_id", "AAA98666.1");
        Assert.assertNotNull(f2);
        Assert.assertNotNull(f3);
        Assert.assertEquals(rf, f2);
        Assert.assertNotSame(rf, f3);
        Assert.assertNotSame(f2, f3);
        rf = this.gbm.getCDS(1);
        Assert.assertNotNull(rf);
        Assert.assertEquals(rf, f3);
        Assert.assertNotSame(rf, f2);
    }

    public void testGetCDSProteinRecord() {
        this.gbm.getCDSList();
        final RichFeature rf = this.gbm.getCDS(0);
        Assert.assertNotNull(rf);
        Assert.assertTrue(this.gbm.canGetProtein(0));
        // was Assert.assertNotNull(this.gbm.getCDSProteinRecord(0));
        Assert.assertTrue(this.gbm.containsTranslation(0));
        //assertNotNull(gbm.getCodedBy(0));
        //assertEquals("<1..206", gbm.getCodedBy(0));
        //assertEquals("687..3158", gbm.getCodedBy(1));
        //assertEquals("complement(3300..4037)", gbm.getCodedBy(2));
        // was Assert.assertNotNull(this.gbm.getCDSNucleotideRecord(0));
        //was Assert.assertNotNull(this.gbm.getCDSProteinRecord(0));
    }

/*
    public void testGetSubNucleotideRecord() {
        this.gbm.getCDSList();
        this.gbm.getCDS(0);
        Assert.assertNotNull(this.gbm.getCDSNucleotideRecord(0));
    } */

    public void testGetCDSByName() {
        this.gbm.getCDSList();
        RichFeature rf = this.gbm.getCDS(0);
        // Assert.assertNotNull(this.gbm.getCDSNucleotideRecord(0));
        final CdsList list = new CdsList(this.bio);
        RichFeature rfn = ParserUtility.getCDSByName(rf.getName(), list);
        Assert.assertEquals(rf, rfn);

        rf = this.gbm.getCDS(2);
        rfn = ParserUtility.getCDSByName(rf.getName(), list);
        Assert.assertEquals(rf, rfn);
        Assert.assertEquals(rf.getLocation(), rfn.getLocation());
    }

    public void testGetCDSByLocation() {
        this.gbm.getCDSList();
        final RichFeature rf = this.gbm.getCDS(1);
        final RichFeature rfn = ParserUtility.getCDSByLocation(new CdsList(this.bio), 687, 3158);
        Assert.assertEquals(rf, rfn);

    }

}
