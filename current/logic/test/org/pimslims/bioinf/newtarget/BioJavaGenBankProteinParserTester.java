/**
 * pims-web org.pimslims.command.newtarget GenBankToPIMSTester.java
 * 
 * @author pvt43
 * @date 15 Nov 2007
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
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleRankedCrossRef;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.ontology.SimpleComparableTerm;
import org.pimslims.dao.ModelImpl;
import org.pimslims.properties.PropertyGetter;

/**
 * This is a Tester for BioJava genbank protein sequence parser. It is useful as reference on how to get
 * information out of the parsed file. BioJavaGenBankProteinParserTester
 * 
 */
public class BioJavaGenBankProteinParserTester extends TestCase {
    RichSequence bio = null;

    protected static final String genBankProtein =
        "LOCUS       YP_006528                398 aa            linear   PHG 08-DEC-2008\r\n"
            + "DEFINITION  ParA [Enterobacteria phage P1].\r\n" + "ACCESSION   YP_006528\r\n"
            + "VERSION     YP_006528.1  GI:46401682\r\n" + "DBLINK      Project: 14493\r\n"
            + "DBSOURCE    REFSEQ: accession NC_005856.1\r\n" + "KEYWORDS    .\r\n"
            + "SOURCE      Enterobacteria phage P1\r\n" + "  ORGANISM  Enterobacteria phage P1\r\n"
            + "            Viruses; dsDNA viruses, no RNA stage; Caudovirales; Myoviridae;\r\n"
            + "            P1-like viruses.\r\n" + "REFERENCE   1  (residues 1 to 398)\r\n"
            + "  AUTHORS   Lobocka,M.B., Rose,D.J., Plunkett,G. III, Rusin,M., Samojedny,A.,\r\n"
            + "            Lehnherr,H., Yarmolinsky,M.B. and Blattner,F.R.\r\n"
            + "  TITLE     Genome of bacteriophage P1\r\n"
            + "  JOURNAL   J. Bacteriol. 186 (21), 7032-7068 (2004)\r\n" + "   PUBMED   15489417\r\n"
            + "REFERENCE   2  (residues 1 to 398)\r\n" + "  CONSRTM   NCBI Genome Project\r\n"
            + "  TITLE     Direct Submission\r\n"
            + "  JOURNAL   Submitted (06-APR-2006) National Center for Biotechnology\r\n"
            + "            Information, NIH, Bethesda, MD 20894, USA\r\n"
            + "REFERENCE   3  (residues 1 to 398)\r\n" + "  AUTHORS   Lobocka,M.B.\r\n"
            + "  TITLE     Direct Submission\r\n"
            + "  JOURNAL   Submitted (14-FEB-2000) Department of Microbial Biochemistry,\r\n"
            + "            Institute of Biochemistry and Biophysics of the Polish Academy of\r\n"
            + "            Sciences, Ul. Pawinskiego 5A, Warsaw 02-106, Poland\r\n"
            + "REFERENCE   4  (residues 1 to 398)\r\n" + "  AUTHORS   Rusin,M. and Samojedny,A.\r\n"
            + "  TITLE     Direct Submission\r\n"
            + "  JOURNAL   Submitted (14-FEB-2000) Department of Tumor Biology, Centre of\r\n"
            + "            Oncology, M. Sklodowska-Curie Memorial Institute, Ul. Wybrzeze AK\r\n"
            + "            15, Gliwice 44-101, Poland\r\n"
            + "COMMENT     PROVISIONAL REFSEQ: This record has not yet been subject to final\r\n"
            + "            NCBI review. The reference sequence was derived from AAQ14032.\r\n"
            + "            Method: conceptual translation.\r\n"
            + "FEATURES             Location/Qualifiers\r\n" + "     source          1..398\r\n"
            + "                     /organism=\"Enterobacteria phage P1\"\r\n"
            + "                     /isolate=\"mod749::IS5 c1.100 mutant\"\r\n"
            + "                     /host=\"Escherichia coli\"\r\n"
            + "                     /db_xref=\"taxon:10678\"\r\n" + "     Protein         1..398\r\n"
            + "                     /product=\"ParA\"\r\n"
            + "                     /name=\"encodes ParA/SopA family protein involved in active\r\n"
            + "                     partitioning of P1 plasmid prophage at cell division\"\r\n"
            + "                     /calculated_mol_wt=44139\r\n" + "     Region          108..393\r\n"
            + "                     /region_name=\"Soj\"\r\n"
            + "                     /note=\"ATPases involved in chromosome partitioning [Cell\r\n"
            + "                     division and chromosome partitioning]; COG1192\"\r\n"
            + "                     /db_xref=\"CDD:31385\"\r\n" + "     Region          110..>154\r\n"
            + "                     /region_name=\"ParA\"\r\n"
            + "                     /note=\"ParA and ParB of Caulobacter crescentus belong to a\r\n"
            + "                     conserved family of bacterial proteins implicated in\r\n"
            + "                     chromosome segregation. ParB binds to DNA sequences\r\n"
            + "                     adjacent to the origin of replication and localizes to\r\n"
            + "                     opposite cell poles shortly following...; cd02042\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Site            117..123\r\n"
            + "                     /site_type=\"other\"\r\n" + "                     /note=\"P-loop\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Site            123\r\n"
            + "                     /site_type=\"other\"\r\n"
            + "                     /note=\"Magnesium ion binding site\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Region          <237..300\r\n"
            + "                     /region_name=\"ParA\"\r\n"
            + "                     /note=\"ParA and ParB of Caulobacter crescentus belong to a\r\n"
            + "                     conserved family of bacterial proteins implicated in\r\n"
            + "                     chromosome segregation. ParB binds to DNA sequences\r\n"
            + "                     adjacent to the origin of replication and localizes to\r\n"
            + "                     opposite cell poles shortly following...; cd02042\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Site            251\r\n"
            + "                     /site_type=\"other\"\r\n"
            + "                     /note=\"Magnesium ion binding site\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     CDS             1..398\r\n"
            + "                     /gene=\"parA\"\r\n" + "                     /locus_tag=\"P1_gp060\"\r\n"
            + "                     /coded_by=\"complement(NC_005856.1:60017..61213)\"\r\n"
            + "                     /note=\"weak ATPase, binds to par operator site to repress\r\n"
            + "                     transcription; 100 pct identical to previously predicted\r\n"
            + "                     product of parA of P1 Swissprot:PARA_ECOLI; similar to\r\n"
            + "                     partition ATPases of ParA/SopA family of many low copy\r\n"
            + "                     number plasmids and bacteria\"\r\n"
            + "                     /transl_table=11\r\n"
            + "                     /db_xref=\"GeneID:2777494\"\r\n" + "ORIGIN      \r\n"
            + "        1 msdssqlhkv aqranrmlnv lteqvqlqkd elhanefyqv yakaalaklp lltranvdya\r\n"
            + "       61 vsemeekgyv fdkrpagssm kyamsiqnii diyehrgvpk yrdryseayv ifisnlkggv\r\n"
            + "      121 sktvstvsla hamrahphll medlrilvid ldpqssatmf lshkhsigiv natsaqamlq\r\n"
            + "      181 nvsreellee fivpsvvpgv dvmpasidda fiasdwrelc nehlpgqnih avlkenvidk\r\n"
            + "      241 lksdydfilv dsgphldafl knalasanil ftplppatvd fhsslkyvar lpelvklisd\r\n"
            + "      301 egcecqlatn igfmsklsnk adhkychsla kevfggdmld vflprldgfe rcgesfdtvi\r\n"
            + "      361 sanpatyvgs adalknaria aedfakavfd riefirsn\r\n" + "//\r\n" + "\r\n" + "";

    String protSequence =
        "msdssqlhkv aqranrmlnv lteqvqlqkd elhanefyqv yakaalaklp lltranvdya\r\n"
            + "       vsemeekgyv fdkrpagssm kyamsiqnii diyehrgvpk yrdryseayv ifisnlkggv\r\n"
            + "      sktvstvsla hamrahphll medlrilvid ldpqssatmf lshkhsigiv natsaqamlq\r\n"
            + "      nvsreellee fivpsvvpgv dvmpasidda fiasdwrelc nehlpgqnih avlkenvidk\r\n"
            + "      lksdydfilv dsgphldafl knalasanil ftplppatvd fhsslkyvar lpelvklisd\r\n"
            + "      egcecqlatn igfmsklsnk adhkychsla kevfggdmld vflprldgfe rcgesfdtvi\r\n"
            + "      sanpatyvgs adalknaria aedfakavfd riefirsn";

    /**
     * @param name
     */
    public BioJavaGenBankProteinParserTester(final String name) {
        super(name);
        // set up the HTTP proxy
        ModelImpl.getModel();
        PropertyGetter.setProxySetting();
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final String genbankEntry = BioJavaGenBankProteinParserTester.genBankProtein;
        final RichSequenceIterator seqI =
            BioJavaGenBankProteinParserTester.parseGenbankProteinEntry(genbankEntry);
        this.bio = seqI.nextRichSequence();

        //Alternatively 
        //RichSequenceIterator seqI =
        //   RichSequence.IOTools.readGenbankProtein(br, RichObjectFactory.getDefaultNamespace());
        //  RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
    }

    public static RichSequenceIterator parseGenbankProteinEntry(final String genbankEntry)
        throws BioException {
        final BufferedReader br = new BufferedReader(new StringReader(genbankEntry));
        // For dna DNATools.getDNA().getTokinezation() // for DNA
        final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
        final RichSequenceIterator seqI =
            RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                RichObjectFactory.getDefaultNamespace());
        return seqI;
    }

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
    }

    public void testMisc() {
        // System.out.println(bio.getComments());
        // was Assert.assertNull(this.bio.getDescription());
        Assert.assertEquals("PHG", this.bio.getDivision());
        Assert.assertEquals("YP_006528", this.bio.getURN());
    }

    public void testAccession() {
        Assert.assertEquals("YP_006528", this.bio.getAccession()); // Nice to have this: 46401682
    }

    public void testName() {
        Assert.assertEquals("YP_006528", this.bio.getName()); // get it from the product
    }

    public void testTaxId() {
        Assert.assertEquals(10678, this.bio.getTaxon().getNCBITaxID()); // see taxon on the source feature
    }

    public void testOrganismFromTaxId() {
        Assert.assertEquals("Enterobacteria phage P1", this.bio.getTaxon().getDisplayName()); // see taxon on the source feature
    }

    public void testFeatureList() {
        final FeatureHolder cds = this.bio.filter(new FeatureFilter.ByType("source"));
        for (final Iterator iterator = cds.features(); iterator.hasNext();) {
            final Feature f = (Feature) iterator.next();
            Assert.assertEquals("GenBank", f.getSource());
            Assert.assertEquals("source", f.getType());

            Assert.assertEquals(1, f.getLocation().getMin());
            Assert.assertEquals(398, f.getLocation().getMax());
            Assert.assertEquals(true, f.getLocation().isContiguous());
            // Look at LocationTools. methods may be useful 

            //            assertEquals("", f.getSequence()); //accession
            //System.out.println(f.getAnnotation());
            final Annotation annot = f.getAnnotation();
            final boolean[] test = new boolean[5];
            Assert.assertFalse(test[0]); // Make sure initialisation is with false 
            Assert.assertFalse(test[3]);
            for (final Iterator iterator2 = annot.keys().iterator(); iterator2.hasNext();) {
                final SimpleComparableTerm st = (SimpleComparableTerm) iterator2.next();
                if (st.getName().equals("isolate")) {
                    test[0] = true;
                    Assert.assertEquals("mod749::IS5 c1.100 mutant", annot.getProperty(st));
                }
                if (st.getName().equals("specific_host")) {
                    test[1] = true;
                    Assert.assertEquals("Escherichia coli", annot.getProperty(st));
                }
                if (st.getName().equals("virion")) {
                    test[2] = true;
                    Assert.assertEquals("", annot.getProperty(st));
                }
                if (st.getName().equals("organism")) {
                    test[3] = true;
                }
                // This is always out of the properties 
                if (st.getName().equals("db_xref")) {
                    test[4] = true;
                }

                // This is BioJava internals. These methods are not used in PIMS
                // These asserts are here to document this tool behavour 
                // Some of them if return something meaningful can be very useful
                // lets wait till later versions of biojava
                Assert.assertEquals("auto-generated by biojavax", st.getDescription());
                Assert.assertNull(st.getIdentifier());
                Assert.assertEquals("biojavax", st.getOntology().getName());
                Assert.assertTrue(st.getRankedCrossRefs().isEmpty());
                Assert.assertTrue(st.getSynonyms().length == 0);

            }
            Assert.assertTrue(test[0]);
            //was Assert.assertTrue(test[1]);
            //was Assert.assertTrue(test[2]);
            // These is fine because these information is merged together in a Taxon! 
            Assert.assertFalse(test[3]);
            Assert.assertFalse(test[4]);
            Assert.assertTrue(f.getAnnotation().containsProperty("isolate"));
            // This is available as Taxon not as a Feature as one might expect. 
            Assert.assertFalse(f.getAnnotation().containsProperty("organism"));
            Assert.assertFalse(f.getAnnotation().containsProperty("db_xref"));

        }
    }

    public void testFeatureList2() {
        final FeatureHolder cds = this.bio.filter(new FeatureFilter.ByType("CDS"));
        // This is true, as cross references stored for each feature
        Assert.assertTrue(this.bio.getRankedCrossRefs().isEmpty());
        // RichSequence rs = RichSequence.Tools.enrich(s);
        for (final Iterator iterator = cds.features(); iterator.hasNext();) {
            final RichFeature f = (RichFeature) iterator.next();
            Assert.assertEquals("GenBank", f.getSource());
            Assert.assertEquals("CDS", f.getType());
            Assert.assertFalse(f.getRankedCrossRefs().isEmpty());

            for (final Iterator iterator2 = f.getRankedCrossRefs().iterator(); iterator2.hasNext();) {
                final SimpleRankedCrossRef cr = (SimpleRankedCrossRef) iterator2.next();
                Assert.assertEquals("GeneID", cr.getCrossRef().getDbname());
                Assert.assertEquals("2777494", cr.getCrossRef().getAccession());
            }

            final Annotation annot = f.getAnnotation();
            Assert.assertTrue(annot.containsProperty("transl_table"));
            Assert.assertTrue(annot.containsProperty("note"));
            Assert.assertTrue(annot.containsProperty("coded_by"));
            Assert.assertTrue(annot.containsProperty("gene"));
            Assert.assertFalse(annot.containsProperty("db_xref"));
        }
    }

    public void testLocation() {
        final FeatureHolder cds = this.bio.filter(new FeatureFilter.ByType("CDS"));
        final RichFeature rf = (RichFeature) cds.features().next();
        // System.out.println(rf.getLocation().getMax());
        Assert.assertTrue(rf.getAnnotation().containsProperty("coded_by"));
        Assert.assertEquals("complement(NC_005856.1:60017..61213)", rf.getAnnotation()
            .getProperty("coded_by"));
        Assert.assertEquals("NC_005856.1", GenBankParserTools
            .getAccessionFromLocation("complement(NC_005856.1:60017..61213)"));
        Assert.assertEquals("NC_005856.1", GenBankParserTools
            .getAccessionFromLocation("NC_005856.1:60017..61213"));
        Assert.assertEquals("NC_005856.1", GenBankParserTools
            .getAccessionFromLocation("NC_005856.1:60017..61213,398434..234344"));
    }

    public void testCanDetailedQuerybeMade() {
        Assert.assertTrue(GenBankParserTools.canDetailedQuerybeMade("complement(NC_005856.1:60017..61213)"));
        Assert.assertTrue(GenBankParserTools.canDetailedQuerybeMade("NC_005856.1:60017..61213"));
        Assert
            .assertFalse(GenBankParserTools.canDetailedQuerybeMade("NC_005856.1:60017..61213,39483..45455"));
        Assert.assertFalse(GenBankParserTools
            .canDetailedQuerybeMade("join(complement NC_005856.1:60017..61213,39483..45455)"));
        Assert.assertFalse(GenBankParserTools
            .canDetailedQuerybeMade("join(NC_005856.1:60017..61213,NC_005856.1:39483..45455)"));
    }

    /*
     * This testcase if making requests to external web site
     * The internet connection & proxy settings mast be set as appropriate
     */
    public void xtestGetGenBankNucleotideSubEntry() {
        String subEntry = null;
        String revsubEntry = null;
        String revsubEntryAmb = null;
        try {
            subEntry = GenBankParserTools.getGenBankNucleotideSubEntry("NC_005856.1:60017..61213");
            Assert.assertNull(GenBankParserTools.getGenBankNucleotideSubEntry("NC_005856.160017..61213"));
            revsubEntry =
                GenBankParserTools.getGenBankNucleotideSubEntry("complement(NC_005856.1:60017..61213)");
            revsubEntryAmb =
                GenBankParserTools.getGenBankNucleotideSubEntry("complement(NC_005856.1:>60017..<61213)");
            Assert.assertNull(GenBankParserTools
                .getGenBankNucleotideSubEntry("NC_005856.1:160017..61213,34344..22434"));
            Assert.assertNull(GenBankParserTools
                .getGenBankNucleotideSubEntry("join(complement(NC_005856.1:160017..61213))"));
            Assert.assertNull(GenBankParserTools.getGenBankNucleotideSubEntry("60017..61213"));
        } catch (final IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Assert.assertNotNull(subEntry);
        Assert.assertNotNull(revsubEntry);
        Assert.assertNotNull(revsubEntryAmb);
        Assert.assertNotSame(subEntry, revsubEntry);
        Assert.assertEquals(revsubEntry, revsubEntryAmb);
    }

}
