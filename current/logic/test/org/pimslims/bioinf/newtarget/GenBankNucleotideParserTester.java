/**
 * current-pims-web org.pimslims.command.newtarget GenBankNucleotideParserTester.java
 * 
 * @author Petr
 * @date 29 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * GenBankNucleotideParserTester
 * 
 */
public class GenBankNucleotideParserTester extends TestCase {
    /**
     * 
     */
    static final String DNA_SEQUENCE =
        "        1 ccatggcgga tcgtcgtcgt tgtcgatgat catcgaagga agctagagga tatggctcaa\r\n"
            + "       61 tactttgata atatatatac tgatctctcc gtacaacaaa aatataaaaa ttctagctag\r\n"
            + "      121 tatcgaatga gacatatgct atgctagtac tacgaatcta aaaagatgta catattttga\r\n"
            + "      181 ttcgtattat taggatatat cacgagtttt tatattttga gacggatgta ataattctga\r\n"
            + "      241 atttagttgt gatcgcatgg catgcaggag ctcgctggtg tggggggagg ggcactacaa\r\n"
            + "      301 cggcgccgtc aagacgcgga agtcgacggt gatgcagccg ccgccggcgg aggaggagga\r\n"
            + "      361 cgacgccgac cacgcggcgc gccaccggag ccggcagctg agggagctct acgactggct\r\n"
            + "      421 gcagcaggcc ggggagaact ccagcggcgg cgtgcagacg tcgtcgacga cggcgagccg\r\n"
            + "      481 gcggccgggg gcggctctgt cgccggagga cctgacggag acggagtggt tcttcctcat\r\n"
            + "      541 gtcggcatcc tactccttcc ctcccggcat cgggtatata ataaaaaata tagatataaa\r\n"
            + "      601 tatttaagca tgcatgcata aattaaacca cacttcttgt tacgtgttct tggcaaaatg\r\n"
            + "      661 atgaacaatt accactaatt aattggagcc agaaacccta aagatttacc cacctggtta\r\n"
            + "      721 attaatcggt gtgttgatcc acgcatgcat gcatgcagaa aatcaagatc aggatagctc\r\n"
            + "      781 cttttctttt gcaggttaat tagctagatc ttcacgtata attagctagc tagattttaa\r\n"
            + "      841 aatataattt attcaatttg atttatgatt tttatttttt atttcaaata gatacaactg\r\n"
            + "      901 tatacaaaat tttattttgg tacatacctc cgatccaact acatcagagg taaaaaaaaa\r\n"
            + "      961 attaaaccgt tggaattgat tagaacaaga tcgtgcggtc aaattatatc ataactaact\r\n"
            + "     1021 tttctgattc tctaaagcat agagatgtat atatacatcg tattattagg ctctatattt\r\n"
            + "     1081 cctgattaac actagatgca tatataattt tgatagtcaa aatatacttt tgataggctc\r\n"
            + "     1141 taaagaaaaa cttaataaca tgtactccct ccatatactt ttgatagtca tatttcatct\r\n"
            + "     1201 tgacacacag atcaagtata agtaattcta cttatcatcc atttaaacac gctactagtt\r\n"
            + "     1261 attcctcata aacaagcgat tcattaatat ttacatttct cgatgcttgt gtagccaata\r\n"
            + "     1321 ttgtgtggaa gaatggaatg tcattaagag gataggttgt tggattaaaa tatgcctatc\r\n"
            + "     1381 aaaaataaat ttttagattt gaaaatatgc ctatcaaaag tagatggagg gagtattaat\r\n"
            + "     1441 taatgtgaat ttccaatcct actgttgtga tattaggctt tgtaccttct tgtccaggag\r\n"
            + "     1501 gtatatatat ggctctttta aggatgggag aaaatatcat ctttaataca actatatatg\r\n"
            + "     1561 gcttttgttt gataaataca acttttattt tgtatgaata caaatatatt gataaatatc\r\n"
            + "     1621 caccattata atcctaaccc attaggatca tatggtgtat atttttttaa ctatttgttt\r\n"
            + "     1681 tttataaatt aatattaaga gatcacaata aaaatatagt attatgaaag tactcttaac\r\n"
            + "     1741 aacatatcca atgataaaat tattattatt acaaaatata gtggtcaaat tgtatagaat\r\n"
            + "     1801 tcaatagcct gattttatga cgtcaagtaa attaaataaa gaatgaaggt agtgctagag\r\n"
            + "     1861 tgatcaaaca atatctctcc taaaatatgt cctataagtt ttactccata aatccaaggg\r\n"
            + "     1921 tcaaaagttg ttgggttatt tttttagata ataacatact accccttttc aaaatgtatg\r\n"
            + "     1981 attctattga ctttttgcac aacatttaac catttgtcat attaaaaatt agtataaaca\r\n"
            + "     2041 tctaanaata taagttacaa ttatatttta tttgatgata aaacaactca caacaaaata\r\n"
            + "     2101 aataatattt atataatctt tttggaataa aacgaatgat caaacattat tcaaaaagtc\r\n"
            + "     2161 aatggtatag tacgttttga aattgataga ctatgagagc aaaattttga gataacatgg\r\n"
            + "     2221 aaaattatcc tcttagacat tgcactgtgt aataattaat aataatgaat gaaaggctaa\r\n"
            + "     2281 gacttttctt ccaccttata taagtggttg aatatatagc aatcacatca ttacatgatt\r\n"
            + "     2341 ttgtaaccaa ccgtctctat agctccgata cagtgctagt ttcacatcgt aataattaaa\r\n"
            + "     2401 gagtataata ataaatcgag gtgtacttct catcgatgaa gtgatgtgcc gcttagctaa\r\n"
            + "     2461 attaaactcg tatgcgaaaa atcagtatat gtccggttaa tttctaagag agagattgag\r\n"
            + "     2521 agagaataat tgcgcccctc caaatccccc tcttggacgt tagggagcta tatagacggt\r\n"
            + "     2581 attgctaagt gcgatgtgta cataacgtac ctgtcgtagg aacatttctc atccaaatta\r\n"
            + "     2641 agtagtaatg catggcatga aatccatttt tgtattttgc atggcaaaga atgacaacaa\r\n"
            + "     2701 ggaatacact agctagccct gccctttttc aatttaattt aacatcaaac ttagttattg\r\n"
            + "     2761 tatttctttt gtcagaatag catgcattgc atactcttta aaaataatta attagtgtat\r\n"
            + "     2821 tttactagtc ttacaaaagt atcaagagag acaactaatt atagttggga gacaccaaac\r\n"
            + "     2881 ttgtttttaa taatgacaat taaaacccta cctctacatc caacatagac gtacatagtc\r\n"
            + "     2941 cgaaggcgcc aaatatttgt acatttagct accagatttc agtacgagtt ctcacattat\r\n"
            + "     3001 aattttgatt tttttatttt ttttataaac aatctggtac ccttttatgt ctggaaggaa\r\n"
            + "     3061 aaaaaaaatc taaattgcaa cattttagtc ggtgagaatg gtactctgtc ctagctactt\r\n"
            + "     3121 tctacacatg agagagagag agagagagag agagagagcc tttaattgcc cttgcccatg\r\n"
            + "     3181 catctttctt tgcacacatg tatgcttttc acattgtcat gaggagagaa cttgttaagt\r\n"
            + "     3241 tgcacacatg tgtgctttgc atgtcttcag gttacctgga agggcatttg caaggagagg\r\n"
            + "     3301 ccatgtatgg ctcactggag caaatgaagt tgacagcaaa gtattcctaa gagcaattct\r\n"
            + "     3361 tgccaaggtt cagccatcac cttctcttac ctatttttca ctctgaatgc caacagtgct\r\n"
            + "     3421 ttgcacattg tagtctgttt gcagactgca aatgatgacc ataatcagat cagaaaataa\r\n"
            + "     3481 aataatatta tatacttttt gagccagcta gcaagaatat gtaacaataa ttctcctttt\r\n"
            + "     3541 tttttcttgt tcttttccct gatgtggtgc ataacaaata accaaactga tgaatggcag\r\n"
            + "     3601 agtgctggta tccaggtatt tgcctctaaa agtagctaca cgtttactat gaaattttgt\r\n"
            + "     3661 ggcttttgtt catctttgga tgcagtggcc attatctaaa aactatgaat ttccagactg\r\n"
            + "     3721 cagtttttat ctaattttgt gactttgtac atcagacagt tgtgtgcatt cctgttgtcg\r\n"
            + "     3781 atggcgtcct ggaaattgga actacggaaa aggtgatttc gtatattatc agctgacaat\r\n"
            + "     3841 ctaattatat gggccatata attaagtata aatcaaaata cctcataata tattataaag\r\n"
            + "     3901 tatctaatgt gattatgtga atattggcta tttcaatgta atttgatata tgaaactgat\r\n"
            + "     3961 aatcctctga aactccgtaa ggatcaaact aatcaaaatg tatatatttt caaggtggag\r\n"
            + "     4021 gaagatatgg gcctgattca gtatgcaagg ggcatcttca tggatcaaca tggcatccac\r\n"
            + "     4081 atgaagccta ccctctcaca gcactcaaca tccaacccag tcacccactg tactcatcag\r\n"
            + "     4141 catccaatcc aggttcagat gcaactaggt atcaccagcc aaacaaagtt tgattattca\r\n"
            + "     4201 gatgagctca atgcagatga ggagaatgat gacacagaag aagagggcat gtcaggttca\r\n"
            + "     4261 gacactaaca acactgacac tgaaaggaat tcaggccagc tgcaacttca aatgcaagac\r\n"
            + "     4321 caactgaaca tggtgagcaa tgaccaccag acaataccaa ataatgcagt ttccagtgag\r\n"
            + "     4381 ctaatgcagt gtgagatgtc agaagtggta agagatggct gctcaaataa tattttagag\r\n"
            + "     4441 gatgaaatcc aaatgctgat ggattgccaa aacagtaatt gtcagttaaa tttgcaaggg\r\n"
            + "     4501 ccagatgagc cttgtcactc ttggcatttt ctctgcgagg agttacaaaa tgattaccag\r\n"
            + "     4561 ccaggtatta catttgagaa gataatcctt caaaagcacc cttgttccaa aaatatatat\r\n"
            + "     4621 ttgtactctt cacacaagca ctgccatttt ttttcttttt tgcatacatc ctcaattctt\r\n"
            + "     4681 gcatttcttt tccatatatt tgatacaact gtctccattt cccttctgtc acagctactg\r\n"
            + "     4741 aagatcaagt ggcatcacct gaaaataccc attacccaaa aacactcatg acaatcctac\r\n"
            + "     4801 attacaacac gctgcgacag caagagatga acatcaagaa ctacttgcca gtttcagaga\r\n"
            + "     4861 aatcatcatt ctccagatgg actactcctg aaggaagtga tgacaacaag accatgatca\r\n"
            + "     4921 gtccaggcac cacacagaga atgctcaaga gcatcctgat gattgttccc agtagtcact\r\n"
            + "     4981 gcagttacag gggagcagaa acacctgaat caaggggcgg gaaaggcgca agtggaacgc\r\n"
            + "     5041 gaaaagtcgg tgccatccaa ggtgatttca gtgccaacca tgtgctgaaa gagaggagaa\r\n"
            + "     5101 gaagagagaa gctcaatgag aagttcataa ttctgcgatc tttggtacct ttcatgacaa\r\n"
            + "     5161 aggtaattaa gtactccctc tatttctata aagccgtatt tgactagtta tcttatttag\r\n"
            + "     5221 aaagtatgtg caaatatgta aaatataagt catacttaaa gaacttttaa tgttattaaa\r\n"
            + "     5281 taataagtca caccaaaaat aaaacatata tatttttaat aagataaatg attaaatgta\r\n"
            + "     5341 tatataaaaa ttaatagcgt cacatatttt aaaatagagg ggtatttaag tacccacagg\r\n"
            + "     5401 atcatcaaaa ttcagttatc ttttcttaag cctctaacga acattggaag atcctcacta\r\n"
            + "     5461 atggcagcat gaatctaggg ttcactattt cggaatgcaa aatatgtttt accgggcatc\r\n"
            + "     5521 cgatttttaa aaaattcaga atgaagaaaa ttgaatcttt tttatggatt tgaataaatc\r\n"
            + "     5581 ttgataaatt cgaaaaaatt tccgaacttt tggccagaag tgaatcctac ccgtatccac\r\n"
            + "     5641 cggtaataaa cctaaatttt tgggagtaat gaattaatgt tatatataat ccatgaatta\r\n"
            + "     5701 tatagttcca aactactccg taacaaattt tcaggagtag tgaaattaat attattacaa\r\n"
            + "     5761 tctcagaaaa aaatggcaga aacaattaat ctgttttcaa ttattaatta atttgttttt\r\n"
            + "     5821 gtgtccagat ggacaaggcg tcgatactag gcgacacgat cgagtacgtg aagcagctaa\r\n"
            + "     5881 ggaaccgcat acaagagctc gagtcgtcgt cgtcgtcgtc acgagcagcc gcccgggcgc\r\n"
            + "     5941 catcggcggc ggccgccggg aggcggagga agagatccgc cgccgccgcc actgccacgg\r\n"
            + "     6001 cggcggaagg gatgagcagc agcaatggcc gcaatggcgg cgaggcggcg gaggtggtgc\r\n"
            + "     6061 aggtgtccat catcgagagc gacgcgctgc tggagctccg gtgcggttgc ggcggcggcg\r\n"
            + "     6121 gcggcggtgt ggtgctgctc cgggtgatgc aggcgatgca ggagctccag ctggaggtca\r\n"
            + "     6181 ccgccgtcca ggcctcgtgc gccggtggcg agctgctcgc cgagctgcgc gccaaggtcg\r\n"
            + "     6241 tcgttatgat cctgatctgc atgaaaatgc aaatgcaaat gcaaatgcag aattaagctt\r\n"
            + "     6301 tcattcttgc tcctctgaat tctgaattta tatattcacc cttctttcga tctgctcgta\r\n"
            + "     6361 cgttcgtttc gcctaaatta tgtacaaatt aactgaatct ttgaactgaa aataactgaa\r\n"
            + "     6421 tcttttttgt gtgtttttgt gtgggtgaat tggttggcgc aggtgaaggg gaggaggagg\r\n"
            + "     6481 agcagcatcg ctcaggtgaa gagggccatc catctcgtcc tctcctcctc atcgatatca\r\n"
            + "     6541 ccctgaatta attaataatt aatctagctt cgtgcatgaa tgcatgccac aaatatatac\r\n"
            + "     6601 aaatttacca tatcaatatg tgagagagta ataatcatat aattgcaatc aagcacctgt\r\n"
            + "     6661 gctgcatgca tatatatatt ctgattgcaa ttcatttgca aatgttaaaa ctagatatgt\r\n"
            + "     6721 atgtacatat atcatatatg tggagtacat taacattaga ttaattagaa ccatctatat\r\n"
            + "     6781 atctaaccat cgtggcaaat tggttagatc agggaagtga aaaaactcta gtaataataa\r\n"
            + "     6841 tagtaatgta atgccatttt atcaagcttg aggtaattat cgtgatttag gaaaatgatg\r\n"
            + "     6901 catagataga actatatata tcgattcttt gtttgaattt tctaaaaggt aat\r\n";

    /**
     * 
     */
    static final String PROTEIN_SEQUENCE = "SSLVWGEGHYNGAVKTRKSTVMQPPPAEEEDDADHAARHRSRQL\r\n"
        + "                     RELYDWLQQAGENSSGGVQTSSTTASRRPGAALSPEDLTETEWFFLMSASYSFPPGIG\r\n"
        + "                     LPGRAFARRGHVWLTGANEVDSKVFLRAILAKTVVCIPVVDGVLEIGTTEKVEEDMGL\r\n"
        + "                     IQYARGIFMDQHGIHMKPTLSQHSTSNPVTHCTHQHPIQVQMQLGITSQTKFDYSDEL\r\n"
        + "                     NADEENDDTEEEGMSGSDTNNTDTERNSGQLQLQMQDQLNMVSNDHQTIPNNAVSSEL\r\n"
        + "                     MQCEMSEVVRDGCSNNILEDEIQMLMDCQNSNCQLNLQGPDEPCHSWHFLCEELQNDY\r\n"
        + "                     QPATEDQVASPENTHYPKTLMTILHYNTLRQQEMNIKNYLPVSEKSSFSRWTTPEGSD\r\n"
        + "                     DNKTMISPGTTQRMLKSILMIVPSSHCSYRGAETPESRGGKGASGTRKVGAIQGDFSA\r\n"
        + "                     NHVLKERRRREKLNEKFIILRSLVPFMTKMDKASILGDTIEYVKQLRNRIQELESSSS\r\n"
        + "                     SSRAAARAPSAAAAGRRRKRSAAAATATAAEGMSSSNGRNGGEAAEVVQVSIIESDAL\r\n"
        + "                     LELRCGCGGGGGGVVLLRVMQAMQELQLEVTAVQASCAGGELLAELRAKVVVMILICM\r\n"
        + "                     KMQMQMQMQN";

    GenBankNucleotideParser pnuc = null;

    public static final String record =
        "LOCUS       DQ885807                6953 bp    DNA     linear   PLN 19-SEP-2007\r\n"
            + "DEFINITION  Oryza sativa (japonica cultivar-group) cultivar Huma gadog Rc\r\n"
            + "            protein (Rc) gene, partial cds.\r\n" + "ACCESSION   DQ885807\r\n"
            + "VERSION     DQ885807.1  GI:122934770\r\n" + "KEYWORDS    .\r\n"
            + "SOURCE      Oryza sativa Japonica Group\r\n" + "  ORGANISM  Oryza sativa Japonica Group\r\n"
            + "            Eukaryota; Viridiplantae; Streptophyta; Embryophyta; Tracheophyta;\r\n"
            + "            Spermatophyta; Magnoliophyta; Liliopsida; Poales; Poaceae; BEP\r\n"
            + "            clade; Ehrhartoideae; Oryzeae; Oryza.\r\n"
            + "REFERENCE   1  (bases 1 to 6953)\r\n"
            + "  AUTHORS   Sweeney,M.T., Thomson,M.J., Cho,Y.G., Park,Y.J., Williamson,S.H.,\r\n"
            + "            Bustamante,C.D. and McCouch,S.R.\r\n"
            + "  TITLE     Global dissemination of a single mutation conferring white pericarp\r\n"
            + "            in rice\r\n" + "  JOURNAL   PLoS Genet. 3 (8), E133 (2007)\r\n"
            + "   PUBMED   17696613\r\n" + "REFERENCE   2  (bases 1 to 6953)\r\n"
            + "  AUTHORS   McCouch,S.R., Sweeney,M.T., Bustamante,C.D., Thomson,M.J.,\r\n"
            + "            Cho,Y.G., Park,Y.J. and Williamson,S.H.\r\n" + "  TITLE     Direct Submission\r\n"
            + "  JOURNAL   Submitted (27-JUL-2006) Plant Breeding and Genetics, Cornell\r\n"
            + "            University, 161 Emerson Hall, Ithaca, NY 14853, USA\r\n"
            + "FEATURES             Location/Qualifiers\r\n" + "     source          1..6953\r\n"
            + "                     /organism=\"Oryza sativa Japonica Group\"\r\n"
            + "                     /mol_type=\"genomic DNA\"\r\n"
            + "                     /cultivar=\"Huma gadog\"\r\n"
            + "                     /db_xref=\"taxon:39947\"\r\n" + "                     /note=\"red\r\n"
            + "                     genotype: tropical japonica\"\r\n" + "     gene            <1..6953\r\n"
            + "                     /gene=\"Rc\"\r\n"
            + "     mRNA            join(<268..573,3271..3367,3756..3812,4015..4564,\r\n"
            + "                     4735..5162,5829..>6296)\r\n" + "                     /gene=\"Rc\"\r\n"
            + "                     /product=\"Rc protein\"\r\n"
            + "     CDS             join(<268..573,3271..3367,3756..3812,4015..4564,\r\n"
            + "                     4735..5162,5829..6296)\r\n" + "                     /gene=\"Rc\"\r\n"
            + "                     /codon_start=2\r\n" + "                     /product=\"Rc protein\"\r\n"
            + "                     /protein_id=\"ABM68349.1\"\r\n"
            + "                     /db_xref=\"GI:122934771\"\r\n" + "                     /translation=\""
            + GenBankNucleotideParserTester.PROTEIN_SEQUENCE + "\"\r\n" + "ORIGIN      \r\n"
            + GenBankNucleotideParserTester.DNA_SEQUENCE + "//";

    // Sequence from NCBI server processed by location
    String rcseq = "gagctcgctg gtgtgggggg aggggcacta caacggcgcc gtcaagacgc ggaagtcgac\r\n"
        + "ggtgatgcag ccgccgccgg cggaggagga ggacgacgcc gaccacgcgg cgcgccaccg\r\n"
        + "gagccggcag ctgagggagc tctacgactg gctgcagcag gccggggaga actccagcgg\r\n"
        + "cggcgtgcag acgtcgtcga cgacggcgag ccggcggccg ggggcggctc tgtcgccgga\r\n"
        + "ggacctgacg gagacggagt ggttcttcct catgtcggca tcctactcct tccctcccgg\r\n"
        + "catcgggtta cctggaaggg catttgcaag gagaggccat gtatggctca ctggagcaaa\r\n"
        + "tgaagttgac agcaaagtat tcctaagagc aattcttgcc aagacagttg tgtgcattcc\r\n"
        + "tgttgtcgat ggcgtcctgg aaattggaac tacggaaaag gtggaggaag atatgggcct\r\n"
        + "gattcagtat gcaaggggca tcttcatgga tcaacatggc atccacatga agcctaccct\r\n"
        + "ctcacagcac tcaacatcca acccagtcac ccactgtact catcagcatc caatccaggt\r\n"
        + "tcagatgcaa ctaggtatca ccagccaaac aaagtttgat tattcagatg agctcaatgc\r\n"
        + "agatgaggag aatgatgaca cagaagaaga gggcatgtca ggttcagaca ctaacaacac\r\n"
        + "tgacactgaa aggaattcag gccagctgca acttcaaatg caagaccaac tgaacatggt\r\n"
        + "gagcaatgac caccagacaa taccaaataa tgcagtttcc agtgagctaa tgcagtgtga\r\n"
        + "gatgtcagaa gtggtaagag atggctgctc aaataatatt ttagaggatg aaatccaaat\r\n"
        + "gctgatggat tgccaaaaca gtaattgtca gttaaatttg caagggccag atgagccttg\r\n"
        + "tcactcttgg cattttctct gcgaggagtt acaaaatgat taccagccag ctactgaaga\r\n"
        + "tcaagtggca tcacctgaaa atacccatta cccaaaaaca ctcatgacaa tcctacatta\r\n"
        + "caacacgctg cgacagcaag agatgaacat caagaactac ttgccagttt cagagaaatc\r\n"
        + "atcattctcc agatggacta ctcctgaagg aagtgatgac aacaagacca tgatcagtcc\r\n"
        + "aggcaccaca cagagaatgc tcaagagcat cctgatgatt gttcccagta gtcactgcag\r\n"
        + "ttacagggga gcagaaacac ctgaatcaag gggcgggaaa ggcgcaagtg gaacgcgaaa\r\n"
        + "agtcggtgcc atccaaggtg atttcagtgc caaccatgtg ctgaaagaga ggagaagaag\r\n"
        + "agagaagctc aatgagaagt tcataattct gcgatctttg gtacctttca tgacaaagat\r\n"
        + "ggacaaggcg tcgatactag gcgacacgat cgagtacgtg aagcagctaa ggaaccgcat\r\n"
        + "acaagagctc gagtcgtcgt cgtcgtcgtc acgagcagcc gcccgggcgc catcggcggc\r\n"
        + "ggccgccggg aggcggagga agagatccgc cgccgccgcc actgccacgg cggcggaagg\r\n"
        + "gatgagcagc agcaatggcc gcaatggcgg cgaggcggcg gaggtggtgc aggtgtccat\r\n"
        + "catcgagagc gacgcgctgc tggagctccg gtgcggttgc ggcggcggcg gcggcggtgt\r\n"
        + "ggtgctgctc cgggtgatgc aggcgatgca ggagctccag ctggaggtca ccgccgtcca\r\n"
        + "ggcctcgtgc gccggtggcg agctgctcgc cgagctgcgc gccaaggtcg tcgttatgat\r\n"
        + "cctgatctgc atgaaaatgc aaatgcaaat gcaaatgcag aattaa";

    String completeSeq = "ccatggcgga tcgtcgtcgt tgtcgatgat catcgaagga agctagagga tatggctcaa\r\n"
        + "tactttgata atatatatac tgatctctcc gtacaacaaa aatataaaaa ttctagctag\r\n"
        + "tatcgaatga gacatatgct atgctagtac tacgaatcta aaaagatgta catattttga\r\n"
        + "ttcgtattat taggatatat cacgagtttt tatattttga gacggatgta ataattctga\r\n"
        + "atttagttgt gatcgcatgg catgcaggag ctcgctggtg tggggggagg ggcactacaa\r\n"
        + "cggcgccgtc aagacgcgga agtcgacggt gatgcagccg ccgccggcgg aggaggagga\r\n"
        + "cgacgccgac cacgcggcgc gccaccggag ccggcagctg agggagctct acgactggct\r\n"
        + "gcagcaggcc ggggagaact ccagcggcgg cgtgcagacg tcgtcgacga cggcgagccg\r\n"
        + "gcggccgggg gcggctctgt cgccggagga cctgacggag acggagtggt tcttcctcat\r\n"
        + "gtcggcatcc tactccttcc ctcccggcat cgggtatata ataaaaaata tagatataaa\r\n"
        + "tatttaagca tgcatgcata aattaaacca cacttcttgt tacgtgttct tggcaaaatg\r\n"
        + "atgaacaatt accactaatt aattggagcc agaaacccta aagatttacc cacctggtta\r\n"
        + "attaatcggt gtgttgatcc acgcatgcat gcatgcagaa aatcaagatc aggatagctc\r\n"
        + "cttttctttt gcaggttaat tagctagatc ttcacgtata attagctagc tagattttaa\r\n"
        + "aatataattt attcaatttg atttatgatt tttatttttt atttcaaata gatacaactg\r\n"
        + "tatacaaaat tttattttgg tacatacctc cgatccaact acatcagagg taaaaaaaaa\r\n"
        + "attaaaccgt tggaattgat tagaacaaga tcgtgcggtc aaattatatc ataactaact\r\n"
        + "tttctgattc tctaaagcat agagatgtat atatacatcg tattattagg ctctatattt\r\n"
        + "cctgattaac actagatgca tatataattt tgatagtcaa aatatacttt tgataggctc\r\n"
        + "taaagaaaaa cttaataaca tgtactccct ccatatactt ttgatagtca tatttcatct\r\n"
        + "tgacacacag atcaagtata agtaattcta cttatcatcc atttaaacac gctactagtt\r\n"
        + "attcctcata aacaagcgat tcattaatat ttacatttct cgatgcttgt gtagccaata\r\n"
        + "ttgtgtggaa gaatggaatg tcattaagag gataggttgt tggattaaaa tatgcctatc\r\n"
        + "aaaaataaat ttttagattt gaaaatatgc ctatcaaaag tagatggagg gagtattaat\r\n"
        + "taatgtgaat ttccaatcct actgttgtga tattaggctt tgtaccttct tgtccaggag\r\n"
        + "gtatatatat ggctctttta aggatgggag aaaatatcat ctttaataca actatatatg\r\n"
        + "gcttttgttt gataaataca acttttattt tgtatgaata caaatatatt gataaatatc\r\n"
        + "caccattata atcctaaccc attaggatca tatggtgtat atttttttaa ctatttgttt\r\n"
        + "tttataaatt aatattaaga gatcacaata aaaatatagt attatgaaag tactcttaac\r\n"
        + "aacatatcca atgataaaat tattattatt acaaaatata gtggtcaaat tgtatagaat\r\n"
        + "tcaatagcct gattttatga cgtcaagtaa attaaataaa gaatgaaggt agtgctagag\r\n"
        + "tgatcaaaca atatctctcc taaaatatgt cctataagtt ttactccata aatccaaggg\r\n"
        + "tcaaaagttg ttgggttatt tttttagata ataacatact accccttttc aaaatgtatg\r\n"
        + "attctattga ctttttgcac aacatttaac catttgtcat attaaaaatt agtataaaca\r\n"
        + "tctaanaata taagttacaa ttatatttta tttgatgata aaacaactca caacaaaata\r\n"
        + "aataatattt atataatctt tttggaataa aacgaatgat caaacattat tcaaaaagtc\r\n"
        + "aatggtatag tacgttttga aattgataga ctatgagagc aaaattttga gataacatgg\r\n"
        + "aaaattatcc tcttagacat tgcactgtgt aataattaat aataatgaat gaaaggctaa\r\n"
        + "gacttttctt ccaccttata taagtggttg aatatatagc aatcacatca ttacatgatt\r\n"
        + "ttgtaaccaa ccgtctctat agctccgata cagtgctagt ttcacatcgt aataattaaa\r\n"
        + "gagtataata ataaatcgag gtgtacttct catcgatgaa gtgatgtgcc gcttagctaa\r\n"
        + "attaaactcg tatgcgaaaa atcagtatat gtccggttaa tttctaagag agagattgag\r\n"
        + "agagaataat tgcgcccctc caaatccccc tcttggacgt tagggagcta tatagacggt\r\n"
        + "attgctaagt gcgatgtgta cataacgtac ctgtcgtagg aacatttctc atccaaatta\r\n"
        + "agtagtaatg catggcatga aatccatttt tgtattttgc atggcaaaga atgacaacaa\r\n"
        + "ggaatacact agctagccct gccctttttc aatttaattt aacatcaaac ttagttattg\r\n"
        + "tatttctttt gtcagaatag catgcattgc atactcttta aaaataatta attagtgtat\r\n"
        + "tttactagtc ttacaaaagt atcaagagag acaactaatt atagttggga gacaccaaac\r\n"
        + "ttgtttttaa taatgacaat taaaacccta cctctacatc caacatagac gtacatagtc\r\n"
        + "cgaaggcgcc aaatatttgt acatttagct accagatttc agtacgagtt ctcacattat\r\n"
        + "aattttgatt tttttatttt ttttataaac aatctggtac ccttttatgt ctggaaggaa\r\n"
        + "aaaaaaaatc taaattgcaa cattttagtc ggtgagaatg gtactctgtc ctagctactt\r\n"
        + "tctacacatg agagagagag agagagagag agagagagcc tttaattgcc cttgcccatg\r\n"
        + "catctttctt tgcacacatg tatgcttttc acattgtcat gaggagagaa cttgttaagt\r\n"
        + "tgcacacatg tgtgctttgc atgtcttcag gttacctgga agggcatttg caaggagagg\r\n"
        + "ccatgtatgg ctcactggag caaatgaagt tgacagcaaa gtattcctaa gagcaattct\r\n"
        + "tgccaaggtt cagccatcac cttctcttac ctatttttca ctctgaatgc caacagtgct\r\n"
        + "ttgcacattg tagtctgttt gcagactgca aatgatgacc ataatcagat cagaaaataa\r\n"
        + "aataatatta tatacttttt gagccagcta gcaagaatat gtaacaataa ttctcctttt\r\n"
        + "tttttcttgt tcttttccct gatgtggtgc ataacaaata accaaactga tgaatggcag\r\n"
        + "agtgctggta tccaggtatt tgcctctaaa agtagctaca cgtttactat gaaattttgt\r\n"
        + "ggcttttgtt catctttgga tgcagtggcc attatctaaa aactatgaat ttccagactg\r\n"
        + "cagtttttat ctaattttgt gactttgtac atcagacagt tgtgtgcatt cctgttgtcg\r\n"
        + "atggcgtcct ggaaattgga actacggaaa aggtgatttc gtatattatc agctgacaat\r\n"
        + "ctaattatat gggccatata attaagtata aatcaaaata cctcataata tattataaag\r\n"
        + "tatctaatgt gattatgtga atattggcta tttcaatgta atttgatata tgaaactgat\r\n"
        + "aatcctctga aactccgtaa ggatcaaact aatcaaaatg tatatatttt caaggtggag\r\n"
        + "gaagatatgg gcctgattca gtatgcaagg ggcatcttca tggatcaaca tggcatccac\r\n"
        + "atgaagccta ccctctcaca gcactcaaca tccaacccag tcacccactg tactcatcag\r\n"
        + "catccaatcc aggttcagat gcaactaggt atcaccagcc aaacaaagtt tgattattca\r\n"
        + "gatgagctca atgcagatga ggagaatgat gacacagaag aagagggcat gtcaggttca\r\n"
        + "gacactaaca acactgacac tgaaaggaat tcaggccagc tgcaacttca aatgcaagac\r\n"
        + "caactgaaca tggtgagcaa tgaccaccag acaataccaa ataatgcagt ttccagtgag\r\n"
        + "ctaatgcagt gtgagatgtc agaagtggta agagatggct gctcaaataa tattttagag\r\n"
        + "gatgaaatcc aaatgctgat ggattgccaa aacagtaatt gtcagttaaa tttgcaaggg\r\n"
        + "ccagatgagc cttgtcactc ttggcatttt ctctgcgagg agttacaaaa tgattaccag\r\n"
        + "ccaggtatta catttgagaa gataatcctt caaaagcacc cttgttccaa aaatatatat\r\n"
        + "ttgtactctt cacacaagca ctgccatttt ttttcttttt tgcatacatc ctcaattctt\r\n"
        + "gcatttcttt tccatatatt tgatacaact gtctccattt cccttctgtc acagctactg\r\n"
        + "aagatcaagt ggcatcacct gaaaataccc attacccaaa aacactcatg acaatcctac\r\n"
        + "attacaacac gctgcgacag caagagatga acatcaagaa ctacttgcca gtttcagaga\r\n"
        + "aatcatcatt ctccagatgg actactcctg aaggaagtga tgacaacaag accatgatca\r\n"
        + "gtccaggcac cacacagaga atgctcaaga gcatcctgat gattgttccc agtagtcact\r\n"
        + "gcagttacag gggagcagaa acacctgaat caaggggcgg gaaaggcgca agtggaacgc\r\n"
        + "gaaaagtcgg tgccatccaa ggtgatttca gtgccaacca tgtgctgaaa gagaggagaa\r\n"
        + "gaagagagaa gctcaatgag aagttcataa ttctgcgatc tttggtacct ttcatgacaa\r\n"
        + "aggtaattaa gtactccctc tatttctata aagccgtatt tgactagtta tcttatttag\r\n"
        + "aaagtatgtg caaatatgta aaatataagt catacttaaa gaacttttaa tgttattaaa\r\n"
        + "taataagtca caccaaaaat aaaacatata tatttttaat aagataaatg attaaatgta\r\n"
        + "tatataaaaa ttaatagcgt cacatatttt aaaatagagg ggtatttaag tacccacagg\r\n"
        + "atcatcaaaa ttcagttatc ttttcttaag cctctaacga acattggaag atcctcacta\r\n"
        + "atggcagcat gaatctaggg ttcactattt cggaatgcaa aatatgtttt accgggcatc\r\n"
        + "cgatttttaa aaaattcaga atgaagaaaa ttgaatcttt tttatggatt tgaataaatc\r\n"
        + "ttgataaatt cgaaaaaatt tccgaacttt tggccagaag tgaatcctac ccgtatccac\r\n"
        + "cggtaataaa cctaaatttt tgggagtaat gaattaatgt tatatataat ccatgaatta\r\n"
        + "tatagttcca aactactccg taacaaattt tcaggagtag tgaaattaat attattacaa\r\n"
        + "tctcagaaaa aaatggcaga aacaattaat ctgttttcaa ttattaatta atttgttttt\r\n"
        + "gtgtccagat ggacaaggcg tcgatactag gcgacacgat cgagtacgtg aagcagctaa\r\n"
        + "ggaaccgcat acaagagctc gagtcgtcgt cgtcgtcgtc acgagcagcc gcccgggcgc\r\n"
        + "catcggcggc ggccgccggg aggcggagga agagatccgc cgccgccgcc actgccacgg\r\n"
        + "cggcggaagg gatgagcagc agcaatggcc gcaatggcgg cgaggcggcg gaggtggtgc\r\n"
        + "aggtgtccat catcgagagc gacgcgctgc tggagctccg gtgcggttgc ggcggcggcg\r\n"
        + "gcggcggtgt ggtgctgctc cgggtgatgc aggcgatgca ggagctccag ctggaggtca\r\n"
        + "ccgccgtcca ggcctcgtgc gccggtggcg agctgctcgc cgagctgcgc gccaaggtcg\r\n"
        + "tcgttatgat cctgatctgc atgaaaatgc aaatgcaaat gcaaatgcag aattaagctt\r\n"
        + "tcattcttgc tcctctgaat tctgaattta tatattcacc cttctttcga tctgctcgta\r\n"
        + "cgttcgtttc gcctaaatta tgtacaaatt aactgaatct ttgaactgaa aataactgaa\r\n"
        + "tcttttttgt gtgtttttgt gtgggtgaat tggttggcgc aggtgaaggg gaggaggagg\r\n"
        + "agcagcatcg ctcaggtgaa gagggccatc catctcgtcc tctcctcctc atcgatatca\r\n"
        + "ccctgaatta attaataatt aatctagctt cgtgcatgaa tgcatgccac aaatatatac\r\n"
        + "aaatttacca tatcaatatg tgagagagta ataatcatat aattgcaatc aagcacctgt\r\n"
        + "gctgcatgca tatatatatt ctgattgcaa ttcatttgca aatgttaaaa ctagatatgt\r\n"
        + "atgtacatat atcatatatg tggagtacat taacattaga ttaattagaa ccatctatat\r\n"
        + "atctaaccat cgtggcaaat tggttagatc agggaagtga aaaaactcta gtaataataa\r\n"
        + "tagtaatgta atgccatttt atcaagcttg aggtaattat cgtgatttag gaaaatgatg\r\n"
        + "catagataga actatatata tcgattcttt gtttgaattt tctaaaaggt aat\r\n";

    String proteinSeq = GenBankNucleotideParserTester.PROTEIN_SEQUENCE;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        this.pnuc = new GenBankNucleotideParser(GenBankNucleotideParserTester.record);

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.pnuc = null;
    }

    public void testGetSequenceInLocation() {
        Assert.assertEquals(this.rcseq.replace(" ", "").replace("\r\n", ""), this.pnuc.getDnaSequence());
    }

    public void testGetProteinSequence() {
        Assert.assertEquals(this.proteinSeq.replace(" ", "").replace("\r\n", ""),
            this.pnuc.getProteinSequence());
    }

    public void testBioXReferences() {
        final BioDBReferences bioxfers = this.pnuc.getXaccessions();
        //System.out.println(bioxfers);
        Assert.assertNotNull(bioxfers);
        Assert.assertEquals("122934771", bioxfers.getAccession("GI"));
    }

    public void testAccession() {
        Assert.assertEquals("DQ885807", this.pnuc.getAccession()); // Nice to have this: 46401682
    }

    public void testName() {
        Assert.assertEquals("Rc protein", this.pnuc.getProteinName()); // get it from the product
        Assert.assertEquals("DQ885807", this.pnuc.getTargetName()); // get it from the product
    }

    public void testTaxId() {
        Assert.assertEquals(39947, this.pnuc.getTaxId()); // see taxon on the source feature
    }

    public void testOrganismFromTaxId() {
        Assert.assertEquals("Oryza sativa Japonica Group", this.pnuc.getSpeciesName());
    }

}
