package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.Annotation;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.ontology.SimpleComparableTerm;

@Deprecated
// not used, and should be in test, not production code.
public class BioJavaGenBankProteinParserForDev extends TestCase {
    RichSequence bio = null;

    static final String genBankProtein =
        " LOCUS       YP_006528                398 aa            linear   PHG 25-JUL-2007\r\n"
            + "DEFINITION  ParA [Enterobacteria phage P1].\r\n" + "ACCESSION   YP_006528\r\n"
            + "VERSION     YP_006528.1  GI:46401682\r\n" + "DBSOURCE    REFSEQ: accession NC_005856.1\r\n"
            + "KEYWORDS    .\r\n" + "SOURCE      Enterobacteria phage P1\r\n"
            + "  ORGANISM  Enterobacteria phage P1\r\n"
            + "            Viruses; dsDNA viruses, no RNA stage; Caudovirales; Myoviridae;\r\n"
            + "            P1-like viruses.\r\n" + "REFERENCE   1  (residues 1 to 398)\r\n"
            + "  AUTHORS   Lobocka,M.B., Rose,D.J., Plunkett,G., Rusin,M., Samojedny,A.,\r\n"
            + "            Lehnherr,H., Yarmolinsky,M.B. and Blattner,F.R.\r\n"
            + "  TITLE     Genome of bacteriophage p1\r\n"
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
            + "                     /virion\r\n"
            + "                     /isolate=\"mod749::IS5 c1.100 mutant\"\r\n"
            + "                     /specific_host=\"Escherichia coli\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n"
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
            + "                     chromosome segregation; cd02042\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Site            117..123\r\n"
            + "                     /site_type=\"other\"\r\n" + "                     /note=\"P-loop\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Site            123\r\n"
            + "                     /site_type=\"other\"\r\n"
            + "                     /note=\"Magnesium ion binding site\"\r\n"
            + "                     /db_xref=\"CDD:73302\"\r\n" + "     Region          <237..300\r\n"
            + "                     /region_name=\"ParA\"\r\n"
            + "                     /note=\"ParA and ParB of Caulobacter crescentus belong to a\r\n"
            + "                     conserved family of bacterial proteins implicated in\r\n"
            + "                     chromosome segregation; cd02042\"\r\n"
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
            + "      361 sanpatyvgs adalknaria aedfakavfd riefirsn\r\n" + "//\r\n" + "\r\n";

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        final BufferedReader br =
            new BufferedReader(new StringReader(BioJavaGenBankProteinParserForDev.genBankProtein));
        final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
        final RichSequenceIterator seqI =
            RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                RichObjectFactory.getDefaultNamespace());
        this.bio = seqI.nextRichSequence();

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        this.bio = null;
    }

    /*
     * This is a failing test case. It is OK. We cannot fix this
     */
    public void testFeatureList() {

        final FeatureHolder cds = this.bio.filter(new FeatureFilter.ByType("source"));
        // assertFalse(bio.getRankedCrossRefs().isEmpty());

        for (final Iterator iterator = cds.features(); iterator.hasNext();) {
            final Feature f = (Feature) iterator.next();
            Assert.assertEquals("GenBank", f.getSource());
            Assert.assertEquals("source", f.getType());

            final Annotation annot = f.getAnnotation();

            Assert.assertTrue(annot.containsProperty("virion"));
            Assert.assertTrue(annot.containsProperty("specific_host"));
            Assert.assertTrue(annot.containsProperty("isolate"));
            // Those 2 are combined and stored in Taxon 
            //assertTrue(annot.containsProperty("organism"));
            //assertTrue(annot.containsProperty("db_xref"));

        }
    }

    public void testFeatureList2() {
        final FeatureHolder cds = this.bio.filter(new FeatureFilter.ByType("CDS"));
        // assertFalse(bio.getRankedCrossRefs().isEmpty());

        for (final Iterator iterator = cds.features(); iterator.hasNext();) {
            final RichFeature f = (RichFeature) iterator.next();
            Assert.assertEquals("GenBank", f.getSource());
            Assert.assertEquals("CDS", f.getType());
            System.out.println(f.getRankedCrossRefs());
            for (final Iterator iterator2 = f.getAnnotation().asMap().keySet().iterator(); iterator2
                .hasNext();) {
                final SimpleComparableTerm name = (SimpleComparableTerm) iterator2.next();
                System.out.println(name.getName());
            }
            //CrossRef.ANNOTATION.;
            //Annotation annot = f.getAnnotation();

            // assertTrue(annot.containsProperty("virion"));
            //assertTrue(annot.containsProperty("specific_host"));
            //assertTrue(annot.containsProperty("isolate"));
            //assertTrue(annot.containsProperty("organism"));
            //  assertTrue(annot.containsProperty("db_xref"));

        }
    }
}
