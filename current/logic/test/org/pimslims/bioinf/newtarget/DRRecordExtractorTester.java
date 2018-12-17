/**
 * pims-web org.pimslims.bioinf.newtarget DRRecordExtractorTester.java
 * 
 * @author pvt43
 * @date 6 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 pvt43 
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import junit.framework.TestCase;

/**
 * DRRecordExtractorTester
 * 
 */
public class DRRecordExtractorTester extends TestCase {

    String record =
        "ID   FTSE_ECOLI     STANDARD;      PRT;   222 AA.\r\n" + "AC   P10115;\r\n"
            + "DT   01-MAR-1989 (Rel. 10, Created)\r\n"
            + "DT   01-MAR-1989 (Rel. 10, Last sequence update)\r\n"
            + "DT   01-MAY-2005 (Rel. 47, Last annotation update)\r\n"
            + "DE   Cell division ATP-binding protein ftsE.\r\n" + "GN   Name=ftsE;\r\n"
            + "GN   OrderedLocusNames=b3463, c4256, z4837, ECs4312, SF3481, S4282;\r\n"
            + "OS   Escherichia coli,\r\n" + "OS   Escherichia coli O6,\r\n"
            + "OS   Escherichia coli O157:H7, and\r\n" + "OS   Shigella flexneri.\r\n"
            + "OC   Bacteria; Proteobacteria; Gammaproteobacteria; Enterobacteriales;\r\n"
            + "OC   Enterobacteriaceae; Escherichia.\r\n" + "OX   NCBI_TaxID=562, 217992, 83334, 623;\r\n"
            + "RN   [1]\r\n" + "RP   NUCLEOTIDE SEQUENCE.\r\n" + "RC   SPECIES=E.coli; STRAIN=K12;\r\n"
            + "RX   MEDLINE=87089083; PubMed=3025556;\r\n"
            + "RA   Gill D.R., Hatfull G.F., Salmond G.P.C.;\r\n"
            + "RT   \"A new cell division operon in Escherichia coli.\";\r\n"
            + "RL   Mol. Gen. Genet. 205:134-145(1986).\r\n" + "RN   [2]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE.\r\n" + "RC   SPECIES=E.coli; STRAIN=K12 / MG1655;\r\n"
            + "RX   MEDLINE=94316500; PubMed=8041620;\r\n"
            + "RA   Sofia H.J., Burland V., Daniels D.L., Plunkett G. III, Blattner F.R.;\r\n"
            + "RT   \"Analysis of the Escherichia coli genome. V. DNA sequence of the\r\n"
            + "RT   region from 76.0 to 81.5 minutes.\";\r\n"
            + "RL   Nucleic Acids Res. 22:2576-2586(1994).\r\n" + "RN   [3]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   SPECIES=E.coli; STRAIN=O6:H1 / CFT073 / ATCC 700928 / UPEC;\r\n"
            + "RX   MEDLINE=22388234; PubMed=12471157; DOI=10.1073/pnas.252529799;\r\n"
            + "RA   Welch R.A., Burland V., Plunkett G. III, Redford P., Roesch P.,\r\n"
            + "RA   Rasko D., Buckles E.L., Liou S.-R., Boutin A., Hackett J., Stroud D.,\r\n"
            + "RA   Mayhew G.F., Rose D.J., Zhou S., Schwartz D.C., Perna N.T.,\r\n"
            + "RA   Mobley H.L.T., Donnenberg M.S., Blattner F.R.;\r\n"
            + "RT   \"Extensive mosaic structure revealed by the complete genome sequence\r\n"
            + "RT   of uropathogenic Escherichia coli.\";\r\n"
            + "RL   Proc. Natl. Acad. Sci. U.S.A. 99:17020-17024(2002).\r\n" + "RN   [4]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   SPECIES=E.coli; STRAIN=O157:H7 / EDL933 / ATCC 700927 / EHEC;\r\n"
            + "RX   MEDLINE=21074935; PubMed=11206551; DOI=10.1038/35054089;\r\n"
            + "RA   Perna N.T., Plunkett G. III, Burland V., Mau B., Glasner J.D.,\r\n"
            + "RA   Rose D.J., Mayhew G.F., Evans P.S., Gregor J., Kirkpatrick H.A.,\r\n"
            + "RA   Posfai G., Hackett J., Klink S., Boutin A., Shao Y., Miller L.,\r\n"
            + "RA   Grotbeck E.J., Davis N.W., Lim A., Dimalanta E.T., Potamousis K.,\r\n"
            + "RA   Apodaca J., Anantharaman T.S., Lin J., Yen G., Schwartz D.C.,\r\n"
            + "RA   Welch R.A., Blattner F.R.;\r\n"
            + "RT   \"Genome sequence of enterohaemorrhagic Escherichia coli O157:H7.\";\r\n"
            + "RL   Nature 409:529-533(2001).\r\n" + "RN   [5]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   SPECIES=E.coli; STRAIN=O157:H7 / Sakai / RIMD 0509952 / EHEC;\r\n"
            + "RX   MEDLINE=21156231; PubMed=11258796;\r\n"
            + "RA   Hayashi T., Makino K., Ohnishi M., Kurokawa K., Ishii K., Yokoyama K.,\r\n"
            + "RA   Han C.-G., Ohtsubo E., Nakayama K., Murata T., Tanaka M., Tobe T.,\r\n"
            + "RA   Iida T., Takami H., Honda T., Sasakawa C., Ogasawara N., Yasunaga T.,\r\n"
            + "RA   Kuhara S., Shiba T., Hattori M., Shinagawa H.;\r\n"
            + "RT   \"Complete genome sequence of enterohemorrhagic Escherichia coli\r\n"
            + "RT   O157:H7 and genomic comparison with a laboratory strain K-12.\";\r\n"
            + "RL   DNA Res. 8:11-22(2001).\r\n" + "RN   [6]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   SPECIES=S.flexneri; STRAIN=301 / Serotype 2a;\r\n"
            + "RX   MEDLINE=22272406; PubMed=12384590; DOI=10.1093/nar/gkf566;\r\n"
            + "RA   Jin Q., Yuan Z., Xu J., Wang Y., Shen Y., Lu W., Wang J., Liu H.,\r\n"
            + "RA   Yang J., Yang F., Zhang X., Zhang J., Yang G., Wu H., Qu D., Dong J.,\r\n"
            + "RA   Sun L., Xue Y., Zhao A., Gao Y., Zhu J., Kan B., Ding K., Chen S.,\r\n"
            + "RA   Cheng H., Yao Z., He B., Chen R., Ma D., Qiang B., Wen Y., Hou Y.,\r\n"
            + "RA   Yu J.;\r\n"
            + "RT   \"Genome sequence of Shigella flexneri 2a: insights into pathogenicity\r\n"
            + "RT   through comparison with genomes of Escherichia coli K12 and O157.\";\r\n"
            + "RL   Nucleic Acids Res. 30:4432-4441(2002).\r\n" + "RN   [7]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   SPECIES=S.flexneri; STRAIN=2457T / ATCC 700930 / Serotype 2a;\r\n"
            + "RX   MEDLINE=22590274; PubMed=12704152;\r\n" + "RX   DOI=10.1128/IAI.71.5.2775-2786.2003;\r\n"
            + "RA   Wei J., Goldberg M.B., Burland V., Venkatesan M.M., Deng W.,\r\n"
            + "RA   Fournier G., Mayhew G.F., Plunkett G. III, Rose D.J., Darling A.,\r\n"
            + "RA   Mau B., Perna N.T., Payne S.M., Runyen-Janecky L.J., Zhou S.,\r\n"
            + "RA   Schwartz D.C., Blattner F.R.;\r\n"
            + "RT   \"Complete genome sequence and comparative genomics of Shigella\r\n"
            + "RT   flexneri serotype 2a strain 2457T.\";\r\n"
            + "RL   Infect. Immun. 71:2775-2786(2003).\r\n"
            + "CC   -!- FUNCTION: Not known. Is coded in an operon essential for cell\r\n"
            + "CC       division.\r\n" + "CC   -!- SIMILARITY: Belongs to the ABC transporter family.\r\n"
            + "CC   --------------------------------------------------------------------------\r\n"
            + "CC   This Swiss-Prot entry is copyright. It is produced through a collaboration\r\n"
            + "CC   between  the Swiss Institute of Bioinformatics  and the  EMBL outstation -\r\n"
            + "CC   the European Bioinformatics Institute.  There are no  restrictions on  its\r\n"
            + "CC   use as long as its content is in no way modified and this statement is not\r\n"
            + "CC   removed.\r\n"
            + "CC   --------------------------------------------------------------------------\r\n"
            + "DR   EMBL; X04398; CAA27985.1; -.\r\n" + "DR   EMBL; U00039; AAB18438.1; -.\r\n"
            + "DR   EMBL; U00096; -.\r\n" + "DR   EMBL; AE016768; AAN82692.1; -.\r\n"
            + "DR   EMBL; AE005569; AAG58572; -.\r\n" + "DR   EMBL; AP002565 \r\n"
            + "DR   EMBL; AE015356; AAN44940.1; ALT_INIT.\r\n" + "DR   EMBL; AE016992; AAP19242.1; -.\r\n"
            + "DR   PIR; H86013; H86013.\r\n" + "DR   PIR; H91167; H91167.\r\n"
            + "DR   PIR; S03131; CEECFE.\r\n" + "DR   HSSP; Q58206; 1L2T.\r\n"
            + "DR   EchoBASE; EB0336; -.\r\n" + "DR   EcoGene; EG10340; ftsE.\r\n"
            + "DR   InterPro; IPR003593; AAA_ATPase.\r\n" + "DR   InterPro; IPR003439; ABC_transporter.\r\n"
            + "DR   InterPro; IPR005286; IISP.\r\n" + "DR   Pfam; PF00005; ABC_tran; 1.\r\n"
            + "DR   ProDom; PD000006; ABC_transporter; 1.\r\n" + "DR   SMART; SM00382; AAA; 1.\r\n"
            + "DR   TIGRFAMs; TIGR00960; 3a0501s02; 1.\r\n"
            + "DR   PROSITE; PS00211; ABC_TRANSPORTER_1; 1.\r\n"
            + "DR   PROSITE; PS50893; ABC_TRANSPORTER_2; 1.\r\n"
            + "KW   ATP-binding; Cell cycle; Cell division; Complete proteome.\r\n"
            + "FT   NP_BIND      35     42       ATP (By similarity).\r\n"
            + "SQ   SEQUENCE   222 AA;  24439 MW;  13CFCDECD8FE7590 CRC64;\r\n"
            + "     MIRFEHVSKA YLGGRQALQG VTFHMQPGEM AFLTGHSGAG KSTLLKLICG IERPSAGKIW\r\n"
            + "     FSGHDITRLK NREVPFLRRQ IGMIFQDHHL LMDRTVYDNV AIPLIIAGAS GDDIRRRVSA\r\n"
            + "     ALDKVGLLDK AKNFPIQLSG GEQQRVGIAR AVVNKPAVLL ADEPTGNLDD ALSEGILRLF\r\n"
            + "     EEFNRVGVTV LMATHDINLI SRRSYRMLTL SDGHLHGGVG HE\r\n" + "//\r\n";

    public void testGetSecondaryIdList() {
        DRRecordExtractor drex = new DRRecordExtractor(record);
        assertEquals(6, drex.countSecondaryIds());
        String[] ids = drex.getSecondaryIdList();
        assertEquals("CAA27985", ids[0]);
        assertEquals("AAB18438", ids[1]);
        assertEquals("AAN82692", ids[2]);
        assertEquals("AAG58572", ids[3]);
        assertEquals("AAN44940", ids[4]);
        assertEquals("AAP19242", ids[5]);
        /*
         for (int i = 0; i < ids.length; i++) {
             System.out.println(ids[i]);
         }
         */
    }
}
