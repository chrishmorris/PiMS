package org.pimslims.bioinf;

import java.io.IOException;
import java.io.StringReader;

import org.biojava.bio.BioException;
import org.biojava.bio.program.sax.blastxml.BlastXMLParserFacade;
import org.xml.sax.SAXException;

public class BioJavaBlastXmlParser {

    /**
     * Class based on BioJava cookbook example for parsing blast output Modified to attempt to use the xml
     * output
     * 
     * @param args
     */
    public static void main(final String[] args) {
        try {
            // get the Blast input as a Stream
            // InputStream is = new FileInputStream(args[0]);
            final StringReader str = new StringReader(BioJavaBlastXmlParser.testXml);

            // make a BlastXmlParser
            final BlastXMLParserFacade parser = new BlastXMLParserFacade();
            // parser.setModeLazy();

            BioJavaBlastParser.doParse(str, parser);

        } catch (final SAXException ex) {
            // XML problem
            ex.printStackTrace();
        } catch (final IOException ex) {
            // IO problem, possibly file not found
            ex.printStackTrace();
        } catch (final BioException e) {
            e.printStackTrace();
        }
    }

    //CHECKSTYLE:OFF
    static String testXml =
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
            + "	<hits total=\"4\">\r\n"
            + "		<hit number=\"1\" database=\"pdb\" id=\"2REL_\" length=\"57\" description=\"mol:protein length:57  R-ELAFIN\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>141</score>\r\n"
            + "					<bits>58.9</bits>\r\n"
            + "					<expectation>1e-09</expectation>\r\n"
            + "					<identity>48</identity>\r\n"
            + "					<positives>55</positives>\r\n"
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
            + "		<hit number=\"2\" database=\"pdb\" id=\"1FLE_I\" length=\"57\" description=\"mol:protein length:57  ELAFIN\">\r\n"
            + "			<alignments total=\"2\">\r\n"
            + "				<alignment number=\"1\">\r\n"
            + "					<score>141</score>\r\n"
            + "					<bits>58.9</bits>\r\n"
            + "					<expectation>1e-09</expectation>\r\n"
            + "					<identity>48</identity>\r\n"
            + "					<positives>55</positives>\r\n"
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
            + "		<hit number=\"3\" database=\"pdb\" id=\"1ZLG_A\" length=\"680\" description=\"mol:protein length:680  Anosmin 1\">\r\n"
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
            + "		<hit number=\"4\" database=\"pdb\" id=\"1UDK_A\" length=\"51\" description=\"mol:protein length:51  Nawaprin\">\r\n"
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
            + "</SequenceSimilaritySearchResult>\r\n" + "</EBIApplicationResult>";
}
