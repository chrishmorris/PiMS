/**
 * current-pims-web org.pimslims.command.newtarget SwissProtParserTester.java
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

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;

import junit.framework.Assert;

import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleComment;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;

/**
 * SwissProtParserTester
 * 
 */
public class UniProtParserTester extends junit.framework.TestCase {
    /**
     * ORGANISM String
     */
    static final String ORGANISM = "Mycoplasma pneumoniae";

    RichSequence bio = null;

    // This has the same format Uniprot entries
    protected static final String swissprotEntry =
        "ID   3MGH_MYCPA              Reviewed;         205 AA.\r\n" + "AC   Q740F6;\r\n"
            + "DT   12-DEC-2006, integrated into UniProtKB/Swiss-Prot.\r\n"
            + "DT   05-JUL-2004, sequence version 1.\r\n" + "DT   02-OCT-2007, entry version 21.\r\n"
            + "DE   Putative 3-methyladenine DNA glycosylase (EC 3.2.2.-).\r\n"
            + "GN   OrderedLocusNames=MAP_1395;\r\n" + "OS   "
            + UniProtParserTester.ORGANISM
            + ".\r\n"
            + "OC   Bacteria; Actinobacteria; Actinobacteridae; Actinomycetales;\r\n"
            + "OC   Corynebacterineae; Mycobacteriaceae; Mycobacterium;\r\n"
            + "OC   Mycobacterium avium complex (MAC).\r\n"
            + "OX   NCBI_TaxID=1770;\r\n"
            + "RN   [1]\r\n"
            + "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].\r\n"
            + "RC   STRAIN=ATCC BAA-968 / K-10;\r\n"
            + "RX   PubMed=16116077; DOI=10.1073/pnas.0505662102;\r\n"
            + "RA   Li L., Bannantine J.P., Zhang Q., Amonsin A., May B.J., Alt D.,\r\n"
            + "RA   Banerji N., Kanjilal S., Kapur V.;\r\n"
            + "RT   \"The complete genome sequence of Mycobacterium avium subspecies\r\n"
            + "RT   paratuberculosis.\";\r\n"
            + "RL   Proc. Natl. Acad. Sci. U.S.A. 102:12344-12349(2005).\r\n"
            + "CC   -!- SIMILARITY: Belongs to the DNA glycosylase MPG family.\r\n"
            + "CC   -----------------------------------------------------------------------\r\n"
            + "CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\r\n"
            + "CC   Distributed under the Creative Commons Attribution-NoDerivs License\r\n"
            + "CC   -----------------------------------------------------------------------\r\n"
            + "DR   EMBL; AE016958; AAS03712.1; -; Genomic_DNA.\r\n"
            + "DR   RefSeq; NP_960329.1; -.\r\n"
            + "DR   GeneID; 2719480; -.\r\n"
            + "DR   GenomeReviews; AE016958_GR; MAP_1395.\r\n"
            + "DR   KEGG; mpa:MAP1395; -.\r\n"
            + "DR   HAMAP; MF_00527; -; 1.\r\n"
            + "DR   InterPro; IPR003180; PurDNA_glycsylse.\r\n"
            + "DR   Gene3D; G3DSA:3.10.300.10; PurDNA_glycsylse; 1.\r\n"
            + "DR   PANTHER; PTHR10429; PurDNA_glycsylse; 1.\r\n"
            + "DR   Pfam; PF02245; Pur_DNA_glyco; 1.\r\n"
            + "DR   ProDom; PD009649; PurDNA_glycsylse; 1.\r\n"
            + "DR   TIGRFAMs; TIGR00567; 3mg; 1.\r\n"
            + "PE   3: Inferred from homology;\r\n"
            + "KW   Complete proteome; DNA damage; DNA repair; Hydrolase.\r\n"
            + "FT   CHAIN         1    205       Putative 3-methyladenine DNA glycosylase.\r\n"
            + "FT                                /FTId=PRO_0000265035.\r\n"
            + "SQ   SEQUENCE   205 AA;  21538 MW;  5E66BDFC6C6A1FC7 CRC64;\r\n"
            + "     MRDAAEQLLV DPVEAARRLL GATLTGRGVS GVIVEVEAYG GVPDGPWPDA AAHSYKGLRA\r\n"
            + "     RNFVMFGPPG RLYTYRSHGI HVCANVSCGP DGTAAAVLLR AAALEDGTDV ARGRRGELVH\r\n"
            + "     TAALARGPGN LCAAMGITMA DNGIDLFDPD SPVTLRLHEP LTAVCGPRVG VSQAADRPWR\r\n"
            + "     LWLPGRPEVS AYRRSPRAPA PGTSD\r\n" + "//";

    String protSequence = "     MRDAAEQLLV DPVEAARRLL GATLTGRGVS GVIVEVEAYG GVPDGPWPDA AAHSYKGLRA\r\n"
        + "     RNFVMFGPPG RLYTYRSHGI HVCANVSCGP DGTAAAVLLR AAALEDGTDV ARGRRGELVH\r\n"
        + "     TAALARGPGN LCAAMGITMA DNGIDLFDPD SPVTLRLHEP LTAVCGPRVG VSQAADRPWR\r\n"
        + "     LWLPGRPEVS AYRRSPRAPA PGTSD\r\n";

    UniProtParser uparser = null;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final BufferedReader br = new BufferedReader(new StringReader(UniProtParserTester.swissprotEntry));
        // For dna DNATools.getDNA().getTokinezation() // for DNA
        final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
        final RichSequenceIterator seqI =
            RichSequence.IOTools.readUniProt(br, rParser, RichSequenceBuilderFactory.FACTORY,
                RichObjectFactory.getDefaultNamespace());
        this.bio = seqI.nextRichSequence();

        this.uparser = new UniProtParser(UniProtParserTester.swissprotEntry);
        //Alternatively 
        //RichSequenceIterator seqI =
        //   RichSequence.IOTools.readGenbankProtein(br, RichObjectFactory.getDefaultNamespace());
        //  RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
    }

    // DT
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.bio = null;
    }

    public void testGetProteinSequence() {
        final String seq = this.protSequence.replaceAll(" ", "").replaceAll("\r\n", "").trim();
        Assert.assertTrue("Protein seqs are not identical! ", seq.equalsIgnoreCase(this.bio.seqString()));
        Assert.assertEquals("Protein seqs are not identical! ", seq, this.uparser.getProteinSequence());
    }

    public void testAccession() {
        Assert.assertEquals("Q740F6", this.bio.getAccession());
        Assert.assertEquals("Q740F6", this.uparser.getAccession());
    }

    public void testName() {
        Assert.assertEquals("3MGH", this.bio.getName()); // get it from the product
        Assert.assertEquals("3MGH", this.uparser.getProteinName());
    }

    // 
    public void testTaxId() {
        Assert.assertEquals(1770, this.bio.getTaxon().getNCBITaxID()); // see taxon on the source feature
        Assert.assertEquals(1770, this.uparser.getTaxId());
    }

    public void testOrganismFromTaxId() {
        Assert.assertEquals(UniProtParserTester.ORGANISM, this.bio.getTaxon().getDisplayName()); // see taxon on the source feature
        Assert.assertEquals(UniProtParserTester.ORGANISM, this.uparser.getSpeciesName());
    }

    public void testDate() {
        /*
         for (Iterator iterator = bio.getAnnotation().keys().iterator(); iterator.hasNext();) {
             SimpleComparableTerm name = (SimpleComparableTerm) iterator.next();
             System.out.println(name.getName());
             System.out.println(bio.getAnnotation().getProperty(name));
         }
         */
        Assert.assertEquals("02-OCT-2007", this.bio.getAnnotation().getProperty("adat"));
        Assert.assertEquals("12-DEC-2006", this.bio.getAnnotation().getProperty("cdat")); // integrated into UniProtKB/Swiss-Prot date
        Assert.assertEquals("21", this.bio.getAnnotation().getProperty("arel"));
        Assert.assertEquals("02-OCT-2007/rev 21", this.uparser.getSequenceRelease());
    }

    public void testComments() {
        Assert.assertEquals(1, this.bio.getComments().size());
        final String val = ((SimpleComment) this.bio.getComments().iterator().next()).getComment();
        Assert.assertEquals("-!- SIMILARITY: Belongs to the DNA glycosylase MPG family.", val);
    }

    public void getStrain() {
        Assert.assertEquals("ATCC BAA-968 / K-10", this.bio.getAnnotation().getProperty("STRAIN")); // integrated into UniProtKB/Swiss-Prot date
    }

    public void getDatadaseName() {
        Assert.assertEquals("Swiss-Prot", this.bio.getAnnotation().getProperty("UniProt database name")); // integrated into UniProtKB/Swiss-Prot date
    }

    /**
     * The number of cross references seem to be related to the number of DR lines However, on some lines e.g.
     * EMBL database there is more than one database reference At the moment BioJava seems do not store them.
     * There is no way to get the second accession in the line EMBL; AE016958; AAS03712.1; Unfortunately this
     * is what is neded, as the first accession refers to whole genome sequence, the second refers to a
     * particular gene/protein.
     */
    public void testXReferences() {

        int referenceCounter = 0;
        for (final Iterator iterator2 = this.bio.getRankedCrossRefs().iterator(); iterator2.hasNext();) {
            referenceCounter++;
            final SimpleRankedCrossRef cr = (SimpleRankedCrossRef) iterator2.next();
            //System.out.println("r1 " + cr.getRank());
            //System.out.println("DB: " + cr.getCrossRef().getDbname());
            //System.out.println("A: " + cr.getCrossRef().getAccession());
            //System.out.println("V: " + cr.getCrossRef().getVersion());
            // @see comment above the method! 
            Assert.assertFalse(cr.getCrossRef().getAccession().equals("AAS03712.1"));
            if (cr.getCrossRef().getDbname().equals("EMBL")) {
                Assert.assertEquals("EMBL", cr.getCrossRef().getDbname());
                Assert.assertEquals("AE016958", cr.getCrossRef().getAccession());
            }
            if (cr.getCrossRef().getDbname().equals("RefSeq")) {
                Assert.assertEquals("RefSeq", cr.getCrossRef().getDbname());
                Assert.assertEquals("NP_960329.1", cr.getCrossRef().getAccession());
            }
            if (cr.getCrossRef().getDbname().equals("GeneID")) {
                Assert.assertEquals("GeneID", cr.getCrossRef().getDbname());
                Assert.assertEquals("2719480", cr.getCrossRef().getAccession());
            }
            if (cr.getCrossRef().getDbname().equals("InterPro")) {
                Assert.assertEquals("InterPro", cr.getCrossRef().getDbname());
                Assert.assertEquals("IPR003180", cr.getCrossRef().getAccession());
            }
            if (cr.getCrossRef().getDbname().equals("Pfam")) {
                Assert.assertEquals("Pfam", cr.getCrossRef().getDbname());
                Assert.assertEquals("PF02245", cr.getCrossRef().getAccession());
            }
            final BioDBReferences br = this.uparser.getXaccessions();
            Assert.assertEquals("AE016958", br.getAccession("EMBL"));
            Assert.assertEquals("NP_960329.1", br.getAccession("RefSeq"));

            // Three are others..
            // System.out.println(cr.getCrossRef().getDbname());
            // System.out.println(cr.getCrossRef().getAccession());
        }
        // There is 12 DR lines in the source. @see comment above the method
        Assert.assertEquals(12, referenceCounter);
    }

    public void testBioXReferences() {
        final BioDBReferences bioxfers = new BioDBReferences(this.bio.getRankedCrossRefs());
        Assert.assertNotNull(bioxfers);
        Assert.assertEquals("AE016958", bioxfers.getAccession("EMBL"));
        bioxfers.addReference("TEST", "AAA222");
        Assert.assertEquals("AAA222", bioxfers.getAccession("TEST"));
        // This is the default behaviour 
        bioxfers.addReference("TEST", "AAA333");
        // assertEquals("AAA333", bioxfers.getAccession("TEST"));
    }

    public void testEMBLBioXReferences() {
        final BioDBReferences bioxfers = new BioDBReferences(this.bio.getRankedCrossRefs());
        Assert.assertNotNull(bioxfers);
        Assert.assertEquals("AE016958", bioxfers.getAccession("EMBL"));
        bioxfers.addReference("TEST", "AAA222");
        Assert.assertEquals("AAA222", bioxfers.getAccession("TEST"));
        // This is the default behaviour 
        bioxfers.addReference("TEST", "AAA333");
        // assertEquals("AAA333", bioxfers.getAccession("TEST"));
    }
}
