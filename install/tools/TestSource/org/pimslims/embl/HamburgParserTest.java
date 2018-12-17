package org.pimslims.embl;

import java.io.IOException;
import java.util.Collection;

import junit.framework.TestCase;

public class HamburgParserTest extends TestCase {

	private static final String CONSTRUCT_DESCRIPTION = "additional N-terminal GA";

	private static final String DNA_SEQUENCE = "CATG";

	private static final String CONTACT = "Young-Hwa Song";

	public HamburgParserTest(String name) {
		super(name);
	}

	public void testNothingToParse() throws IOException {

		Collection<HamburgResearchObjectiveBean> beans = HamburgParser
				.parse("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
						+ "<!DOCTYPE target SYSTEM \"target.dtd\"><targets></targets>");
		assertEquals(0, beans.size());
	}

	public void testParse() throws IOException {
		Collection<HamburgResearchObjectiveBean> beans = HamburgParser
				.parse(HEADER + TAIL);
		assertEquals(1, beans.size());
		HamburgResearchObjectiveBean bean = beans.iterator().next();
		assertEquals(ID, bean.getName());
        assertEquals(DESCRIPTION, bean.getRvNumber());
        assertEquals(WORKPACKAGE, bean.getWorkpackage());
        assertEquals(0, bean.getProjects().size());
        assertEquals(0, bean.getOrfs().size());
    }

    public void testOrf() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + ORF + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getOrfs().size());
        HamburgOrfBean bean = ro.getOrfs().iterator().next();
        assertEquals(ORF_NAME, bean.getName());
        assertEquals(ORGANISM, bean.getOrganism());
        assertEquals(SEQUENCE, bean.getProteinSequence());
        assertEquals(DBNAME, bean.getDatabase());
        assertEquals(ACCESSION, bean.getAccessId());
        assertEquals(DNA_SEQUENCE, bean.getDnaSequence());
    }

    public void testProject() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + OK_PROJECT_HEAD + "?" + PROJECT_TAIL
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("Public", bean.getAccess());
        assertEquals("1", bean.getId());
        assertEquals(PROJECT_DESCRIPTION, bean.getDescription());
        assertEquals("", bean.getRemarks());
    }

    private static final String DB_LINK = "\r\n" + "  <dblink id=\'1\'>\r\n"
            + "    <dbaccnum>VYAS</dbaccnum>\r\n"
            + "    <dbname>Pubmed</dbname>\r\n" + "  </dblink>";

    public void testDbLink() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + OK_PROJECT_HEAD + "?" + PROJECT_TAIL + DB_LINK
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean project = ro.getProjects().iterator().next();
        assertEquals(1, project.getDbLinks().size());
        DbLinkBean bean = project.getDbLinks().iterator().next();
        assertEquals("Pubmed", bean.getDbName());
        assertEquals("VYAS", bean.getAccession());
    }

    public void testParseStatus() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + OK_PROJECT_HEAD + "?" + PROJECT_TAIL
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("Public", bean.getAccess());
        assertEquals("1", bean.getId());
        assertEquals(PROJECT_DESCRIPTION, bean.getDescription());
        assertEquals(PI, bean.getPi());
    }

    public void testParseContact() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + OK_PROJECT_HEAD + CONTACT + PROJECT_TAIL
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("Public", bean.getAccess());
        assertEquals("1", bean.getId());
        assertEquals(PROJECT_DESCRIPTION, bean.getDescription());
        assertEquals(CONTACT, bean.getContact());
    }

    /*
     * could public void testParse2Contacts() throws IOException { Collection<HamburgResearchObjectiveBean>
     * beans = HamburgParser .parse(HEADER + OK_PROJECT_HEAD + CONTACT +",
     * Jack-King Scott" + PROJECT_TAIL + " </project>\r\n" + TAIL);
     * assertEquals(1, beans.size()); HamburgResearchObjectiveBean ro =
     * beans.iterator().next(); assertEquals(1, ro.getProjects().size());
     * HamburgProjectBean bean = ro.getProjects().iterator().next();
     * assertEquals("Public", bean.getAccess()); assertEquals("1",
     * bean.getId()); assertEquals(PROJECT_DESCRIPTION, bean.getDescription());
     * assertEquals(CONTACT, bean.getContact()); }
     */

    public void testParseExperiment() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + OK_PROJECT_HEAD + "?" + PROJECT_TAIL
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("OK", bean.getExpressionQuality());
        assertEquals(10f, bean.getExpressionLevel());
        assertEquals(VECTOR, bean.getVector());
        assertEquals(STRAIN, bean.getStrain());
        assertEquals(CONSTRUCT_DESCRIPTION, bean.getConstructDescription());
    }

    public void testParseCrystallography() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + X_PROJECT + "?" + PROJECT_TAIL
                        + " </project>\r\n" + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("100 x 100", bean.getCrystalSize());
        assertEquals("cubic", bean.getCrystalForm());
        assertEquals("no ligand", bean.getLigand());
        //assertEquals("2.15 Å", bean.getResolution());
        assertEquals(
                "NiNTA : 50mM Tris (pH 8.0), 200mM NaCl, 5% (v/v) glycerol (+ 500mM imidazole), TEV cleavage",
                bean.getCondition());
    }

    public void testParseNoExpression() throws IOException {
        Collection<HamburgResearchObjectiveBean> beans = HamburgParser
                .parse(HEADER + NO_EXPRESSION_PROJECT + TAIL);
        assertEquals(1, beans.size());
        HamburgResearchObjectiveBean ro = beans.iterator().next();
        assertEquals(1, ro.getProjects().size());
        HamburgProjectBean bean = ro.getProjects().iterator().next();
        assertEquals("no expression", bean.getExpressionQuality());
        assertEquals(null, bean.getExpressionLevel());
    }

    private static final String ID = "XMTB00003";

    private static final String DESCRIPTION = "Rv0066c";

    private static final String PROJECT_DESCRIPTION = "full length (1-838)";

    private static final String TASK = "Purified";

    private static final String LAB_ID = "Box 3, F6";

    private static final String PI = "Matthias Wilmanns";

    private static final String LAB = "EMBL Hamburg";

    private static final String VECTOR = "pETM-11";

    private static final String STRAIN = "Rosetta(DE3)pLysS";

    private static final String OK_PROJECT_HEAD = " <project id=\'1\'>\r\n"
            + "  <experiment>\r\n" + "    <clond></clond>\r\n"
            + "    <exprc></exprc>\r\n" + "    <exprv>" + VECTOR
            + "</exprv>\r\n" + "    <exprs>" + STRAIN + "</exprs>\r\n"
            + "    <xtalc></xtalc>\r\n" + "<clond>" + CONSTRUCT_DESCRIPTION
            + "</clond>" + "    <expry>10mg/ml</expry>\r\n" // expression
            // quality
            // OK,
            + "  </experiment>\r\n" + "  <status>\r\n" + "    <contact>";

    // construct that has reached crystallography
    private static final String X_PROJECT = " <project id=\'1\'>\r\n"
            + "  <experiment>\r\n"
            + "    <xtldm>100 x 100</xtldm>\r\n"
            + "    <xtlrs>2.15 Å</xtlrs>\r\n"
            + "    <exprc>NiNTA : 50mM Tris (pH 8.0), 200mM NaCl, 5% (v/v) glycerol (+ 500mM imidazole), TEV cleavage</exprc>\r\n"
            + "    <exprv>pET22b</exprv>\r\n"
            + "    <xtalc></xtalc>\r\n"
            + "    <expry>20mg/ml</expry>\r\n"
            + "    <lignd>no ligand</lignd>\r\n"
            + "    <clond>C-terminal thrombin cleavage site and His6-Tag</clond>\r\n"
            + "    <exprs>BL21(DE3)pRARE</exprs>\r\n"
            + "    <xtlsh>cubic</xtlsh>\r\n" + "  </experiment>"
            + "  <status>\r\n" + "    <contact>";

    private static final String PROJECT_TAIL = "</contact>\r\n" + "    <task>"
            + TASK + "</task>\r\n" + "    <labID>" + LAB_ID + "</labID>\r\n"
            + "    <lab>" + LAB + "</lab>\r\n"
            + "    <funding-project></funding-project>\r\n"
            + "    <url></url>\r\n" + "    <PI>" + PI + "</PI>\r\n"
            + "    <remarks></remarks>\r\n" + "  </status>\r\n"
            + "  <access>Public</access>\r\n" + "  <description>"
            + PROJECT_DESCRIPTION + "</description>\r\n";

    private static final String NO_EXPRESSION_PROJECT = "<project id=\'1\'>\r\n"
            + "  <experiment>\r\n"
            + "    <clond></clond>\r\n"
            + "    <exprc></exprc>\r\n"
            + "    <exprv>pETM-11</exprv>\r\n"
            + "    <exprs>Rosetta(DE3)pLysS</exprs>\r\n"
            + "    <xtalc></xtalc>\r\n"
            + "    <expry>no expression</expry>\r\n"
            + "  </experiment>\r\n"
            + "  <status>\r\n"
            + "    <contact>?</contact>\r\n"
            + "    <task>Cloned</task>\r\n"
            + "    <labID>Box 3, E7</labID>\r\n"
            + "    <lab>EMBL Hamburg</lab>\r\n"
            + "    <funding-project></funding-project>\r\n"
            + "    <url></url>\r\n"
            + "    <PI>Matthias Wilmanns</PI>\r\n"
            + "    <remarks></remarks>\r\n"
            + "  </status>\r\n"
            + "  <access>Public</access>\r\n"
            + "  <description>full length (1-507)</description>\r\n"
            + " </project>";

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
            + "<!DOCTYPE target SYSTEM \"target.dtd\">\r\n"
            + "<targets>\r\n"
            + "<target>\r\n"
            + " <ID>"
            + ID
            + "</ID>\r\n"
            + " <exprv>HASH</exprv>\r\n" + " <exprc>HASH</exprc>\r\n";

    private static final String ORGANISM = "Mycobacterium tuberculosis";

    private static final String SEQUENCE = "MTDTTLPPDDSLDRIEPV"; // real data
    // can be
    // very long

    private static final String ORF_NAME = "DNA gyrase subunit A";

    private static final String DBNAME = "Swissprot";

    private static final String ACCESSION = "Q07702";

    private static final String ORF = " <orf id=\'1\'>\r\n" + "  <specie>"
            + ORGANISM + "</specie>\r\n" + "  <taxoid></taxoid>\r\n"
            + "  <sequence>" + SEQUENCE + "</sequence>\r\n" + "  <name>"
            + ORF_NAME + "</name>\r\n" + "  <dbaccnum>" + ACCESSION
            + "</dbaccnum>\r\n" + "  <dbname>" + DBNAME + "</dbname>\r\n"
            + " </orf>\r\n"
            // note this next element is not in the most sensible place, you'd
            // expect it to be in the ORF
            + "<genesequence>" + DNA_SEQUENCE + "</genesequence>";

    private static final String BAD_ORF = " <orf id=\'1\'>\r\n" + "  <specie>"
            + ORGANISM + "</specie>\r\n" + "  <taxoid></taxoid>\r\n"
            + "  <sequence>" + "JOU" + "</sequence>\r\n" + "  <name>"
            + ORF_NAME + "</name>\r\n" + "  <dbaccnum>" + ACCESSION
            + "</dbaccnum>\r\n" + "  <dbname>" + DBNAME + "</dbname>\r\n"
            + " </orf>\r\n";

    private static final String WORKPACKAGE = "EMBL2 non MPI-IB targets";

    private static final String TAIL = " <expry>HASH</expry>\r\n"
            + " <xtalc>HASH</xtalc>\r\n" + " <description>" + DESCRIPTION
            + "</description>\r\n" + " <clond>HASH</clond>\r\n"
            + " <exprs>HASH</exprs>\r\n"
            + " <annotationUrl>(gene sequence)</annotationUrl>\r\n"
            + " <workpackage>" + WORKPACKAGE + "</workpackage>\r\n"
            + "</target>\r\n" + "</targets>";

}
