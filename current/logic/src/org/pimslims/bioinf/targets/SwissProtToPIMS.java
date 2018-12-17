/**
 * 
 */
package org.pimslims.bioinf.targets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.pimslims.lab.Util;

/**
 * @author Petr Troshin
 */
@Deprecated
// no longer supported
public class SwissProtToPIMS extends TargetImporter {

    /**
     * Class designed to upload the SwissProt entries from files to the PIMS Targets database. Class reads
     * appropriate fields from SwissProt file and puts is to the PIMS database.
     * 
     * @author Petr Troshin <br>
     *         Created on 28.09.2005
     */

    /**
     * <p>
     * Fields mapping:
     * 
     * <PRE>
     * 	 EBI TT vs SwissProt mappings 
     * 	 additionalRemarks = DE -Description. 
     * 	 annotationUrl = link to SwissProt file
     * 	 database = &quot;SWISSPROT&quot;;
     * 	 databaseID = AC- Accession number(s) 
     * 	 proteinName = GN- This line contains the name(s) of the gene(s) that encode for the stored protein sequence.
     * 	 species = OS - Organism species
     * 	 tagretID = next from 1 
     * 	 workpackage = 3A see KW field - Keywords.
     * 	 DR - Database cross-references.
     * 
     * </PRE>
     */

    /**
     * Constructor for SwissProtToPIMS
     * 
     * @param dataOwner
     */
    public SwissProtToPIMS(final String dataOwner) {
        super(dataOwner);
    }

    /**
     * Path to the directory contains swissProt files
     */
    static final String dirPath = "d:/temp/MAN_Targets/old";

    /**
     * Targets counter
     */
    private static int count;

    /**
     * Total number of objects counter
     */
    public static int objCounter;

    /**
     * If true - write statistics or log about the progress to out.
     */
    public static final int LogInfo = 0;

    public static final int LogDebug = 1;

    public static final int collectStatistic = 2;

    public static LogWriter log = null;

    /**
     * Method performs all the work Load SwissProt file Gets appropriate fields from the annotation Clean this
     * fields from redundant and unsuitable information
     * 
     * @see UMTargetsRetrieval
     * @see UMTargetsRetrieval#getWorkPackage(String)
     * 
     *      public void parse(final File swissProtfile) { BufferedReader br = null; try { // create a buffered
     *      reader to read the sequence file br = new BufferedReader(new FileReader(swissProtfile)); } catch
     *      (final FileNotFoundException ex) { // can't find specified file ex.printStackTrace();
     *      System.exit(-1); }
     * 
     *      // read the GenBank File final SequenceIterator sequences = SeqIOTools.readSwissprot(br);
     * 
     *      // iterate through the sequences while (sequences.hasNext()) { try {
     * 
     *      final Sequence seq = sequences.nextSequence();
     * 
     *      final Annotation seqAn = seq.getAnnotation();
     * 
     * 
     *      // System.out.println("Start parsing Citation: " + // System.currentTimeMillis());
     *      this.writeCitation(seqAn.getProperty(ReferenceAnnotation.class)); //
     *      System.out.println("Stop parsing Citation: " + // System.currentTimeMillis());
     * 
     *      // The name of the file is the DB name of the protein. String fname = swissProtfile.getName();
     *      fname = fname.substring(0, fname.indexOf("."));
     * 
     *      super.updateUMTarget(fname);
     * 
     *      this.dbNamePrms.put(DbNameAttr.name, "SWISSPROT"); this.dbRefPrms.put(DbRefAttr.code, ((ArrayList)
     *      seqAn.getProperty("swissprot.accessions")).get(0)); // getAccession( //
     *      (String)seqAn.getProperty("AC") // ));
     * 
     *      this.naturalSrcPrms.put(NaturalSourceAttr.ncbiTaxonomyId, SwissProtToPIMS.getTaxId((String)
     *      seqAn.getProperty("OX"))); // * this.naturalSrcPrms.put(NaturalSourceAttr.organismName,
     *      ServletTargetCreator.makeString(seqAn.getProperty("OS"), " ")); // *
     * 
     *      // TODO Change this! final String sp = ServletTargetCreator.makeString(seqAn.getProperty("OS"),
     *      " "); this.naturalSrcPrms.put(NaturalSourceAttr.species, sp.length() > 80 ? sp.substring(0, 79) :
     *      sp);
     * 
     *      this.moleculePrms.put(MoleculeAttr.molType, "protein");
     *      this.moleculePrms.put(MoleculeAttr.seqString, seq.seqString()); // System.out.println("Mol name: "
     *      + getProtName( // makeString(seqAn.getProperty("GN")) ) ) ;
     * 
     *      this.moleculePrms.put(MoleculeAttr.name, PIMSTargetWriter.getUniqueMolComponentName(
     *      SwissProtToPIMS.getProtName(ServletTargetCreator.makeString(seqAn.getProperty("GN"), " ")), 0,
     *      TargetImporter.molNames));
     * 
     *      if (seqAn.containsProperty("CC")) { this.moleculePrms .put(MoleculeAttr.details,
     *      ServletTargetCreator.makeString(seqAn.getProperty("CC"), " ")); }
     *      this.moleculePrms.put(MoleculeAttr.keywords, Util.makeCollection(seqAn.getProperty("KW")));
     * 
     *      this.targetPrms.put(TargetAttr.functionDescription, seqAn.getProperty("DE"));
     *      SwissProtToPIMS.count = SwissProtToPIMS.count++; this.targetPrms.put(TargetAttr.NAME,
     *      SwissProtToPIMS.getUMTargetID(SwissProtToPIMS.count)); this.targetPrms.put(TargetAttr.localName,
     *      SwissProtToPIMS.getSwissProtID((String) seqAn.getProperty("ID")));
     *      this.targetPrms.put(TargetAttr.whyChosen, "Its one of MPSi selected targets");
     *      this.targetPrms.put(TargetAttr.functionDescription,
     *      ServletTargetCreator.makeString(seqAn.getProperty("DE"), " ")); // was
     *      targetPrms.put(TargetAttr.proteinName, getProtName( // makeString(seqAn.getProperty("GN")))); //
     *      duplicate call to // GN !
     * 
     *      System.out .println(UMTargetsRetrieval.getWorkPackage(swissProtfile.getName()).substring(0, 4));
     *      this.targetGroupPrms.put(TargetGroupAttr.shortName,
     *      (UMTargetsRetrieval.getWorkPackage(swissProtfile.getName()).substring(0, 4)).trim());
     *      this.targetGroupPrms.put(TargetGroupAttr.completeName,
     *      UMTargetsRetrieval.getWorkPackage(swissProtfile.getName()));
     *      this.targetGroupPrms.put(TargetGroupAttr.groupingType, "TCDB category");
     * 
     *      // Annotation final FileInfoHolder fHolder = new FileInfoHolder(swissProtfile.getName(),
     *      swissProtfile.getAbsolutePath(), TargetAttr.class.getName()); this.filePrms.add(fHolder);
     *      this.putToDb();
     * 
     *      } catch (final BioException ex) { // not in SwissProt format this.ex.printStackTrace(); } catch
     *      (final NoSuchElementException ex) { // request for more sequence when there isn't any
     *      this.ex.printStackTrace(); } } }
     */

    /**
     * Make a Target ID, in general format is: "UM" + 4 digits
     * 
     * @param targetNumber
     * @return target ID
     */
    @Deprecated
    // obsolete
    public static String getUMTargetID(final int targetNumber) {
        String id = "UM";
        if (targetNumber < 10) {
            id += "000" + targetNumber;
        }
        if (targetNumber >= 10 && targetNumber < 100) {
            id += "00" + targetNumber;
        }
        if (targetNumber >= 100 && targetNumber < 1000) {
            id += "0" + targetNumber;
        }
        if (targetNumber >= 1000 && targetNumber < 10000) {
            id += targetNumber;
        }
        if (targetNumber > 10000) {
            id = Integer.toString(targetNumber);
        }
        return id;
    }

    public static String getTaxId(String OX) {
        OX = OX.trim();
        final int indx = OX.toLowerCase().indexOf("ncbi_taxid");
        if (indx >= 0) {
            // TODO check that OX contains only digits
            return OX.substring(11, OX.length() - 1);
        }
        return OX;
    }

    /**
     * Remove any additional DB references.
     * 
     * @param accession
     * @return the first DB reference
     */
    public static String getAccession(final String accession) {
        final int ind = accession.indexOf(";");
        if (ind < 0) {
            return accession;
        }
        return accession.substring(0, ind).trim();
    }

    /**
     * Example data NRTA_ANASP STANDARD; PRT; 440 AA.
     * 
     * @param ID - full ID string
     * @return first part of ID - NRTA_ANASP
     */
    public static String getSwissProtID(final String ID) {
        return ID.trim().substring(0, ID.indexOf(";"));
    }

    /**
     * Remove all additional information from "GN" field
     * 
     * @param gnline "GN" lines
     * @return protein name
     */
    public static String getProtName(final String gnline) {
        final int nidx = gnline.toLowerCase().indexOf("name");
        if (nidx < 0) {
            return gnline;
        }
        final int eq = gnline.indexOf("=");
        final int end = gnline.indexOf(";");
        return gnline.substring(eq + 1, end);
    }

    /**
     * Make a Target ID, in general format is: "UM" + 4 digits
     * 
     * @param targetNumber
     * @return target ID
     * 
     *         private static String getTargetID(int targetNumber) { String id = "UM"; if(targetNumber < 10) {
     *         id += "000" + targetNumber; } if(targetNumber >= 10 && targetNumber < 100) { id += "00" +
     *         targetNumber; } if(targetNumber >= 100 && targetNumber < 1000) { id += "0" + targetNumber; }
     *         if(targetNumber >= 1000 && targetNumber < 10000) { id += targetNumber; } if(targetNumber >
     *         10000) { id = Integer.toString(targetNumber); } return id; }
     */

    /**
     * Data examples: Galinier A., Bleicher F.;
     * 
     * @param RA - could be ArrayList in case of multiple lines RA or single String String usually contains
     *            the list of authors which must be separated by comma.
     * @return Collection of HashMaps of person parameters (!) There is only the first author created
     *         temporary see the code for a change
     */
    static void parseRA(final Object RA, final Citation citationParam) {
        final Collection list = Util.makeCollection(RA);
        for (final Iterator iter = list.iterator(); iter.hasNext();) {
            final String elem = (String) iter.next();
            final StringTokenizer st = new StringTokenizer(elem, ",");
            if (st.hasMoreTokens()) { // @TODO note: while - to import all
                // authors change here!
                final String author = st.nextToken().trim();
                final HashMap person = new HashMap();
                final int spaceIndx = SwissProtToPIMS.findSpace(author, author.indexOf("."), false);
                person.put(PersonAttr.familyName, author.substring(0, spaceIndx));
                // System.out.println("AUTHOR: " + author);
                final ArrayList initials = SwissProtToPIMS.getInitials(author.substring(spaceIndx));
                person.put(PersonAttr.middleInitials, initials);
                citationParam.personsPrms.add(person);
            }
        }
    }

    /**
     * Determine the type of Citation and parse the parameters of it
     * 
     * @param RLine
     * @param citationParam
     */
    static void parseRL(final String RLine, final Citation citationParam) {
        // Try do determine Citation type
        final String rl = RLine.trim();

        if (rl.startsWith("Submitted")) {
            citationParam.setCitationType(Citation.THESIS_CITATION);
            SwissProtToPIMS.parseSubmittedCitation(rl, citationParam);

        } else if (rl.startsWith("(In)")) {
            citationParam.setCitationType(Citation.BOOK_CITATION);
            /* TODO code this later */
            // parseBookCitation(rl, citationParam);
        } else if (rl.startsWith("Thesis")) {
            citationParam.setCitationType(Citation.THESIS_CITATION);
            SwissProtToPIMS.parseThesisCitation(rl, citationParam);

        } else if (rl.startsWith("Unpublished")) {
            throw new UnsupportedOperationException(
                "The 'unpublished' citation type has not been implemented yet. ");

        } else if (rl.startsWith("(er)")) {
            throw new UnsupportedOperationException("The 'er' (Electronic publications) "
                + "citation type has not been implemented yet. ");

        } else if (rl.startsWith("Patent number")) {
            throw new UnsupportedOperationException("The 'Parent citation' "
                + "citation type has not been implemented yet. ");
        }
        // Lets think that this is JournalCitation
        else {
            citationParam.setCitationType(Citation.JOURNAL_CITATION);
            if (!SwissProtToPIMS.parseJournalCitation(rl, citationParam)) {
                System.out.println("Could not make out a data type for the Citation \n" + rl
                    + "\n possible cause - improper formatted citation ");
            }
        }

    }

    /**
     * Parse Journal Citation and set it's parameters Data format: J. Bacteriol. 173:2665-2672(1991).
     * 
     * @param rl - rl line
     * @param citationParam - TargetImporter.Citation
     * @return true if citation type was correct and citation was parsed successfully false otherwise
     */
    static boolean parseJournalCitation(final String rl, final Citation citationParam) {
        final int colonInd = rl.indexOf(":");

        if (colonInd != -1 && Character.isDigit(rl.charAt(colonInd - 1)) && colonInd >= 0
            && rl.indexOf("-") >= 0) {

            final int leftIndx = SwissProtToPIMS.findIndex(rl, colonInd, false);
            final String volume = rl.substring(leftIndx + 1, colonInd);
            final String rightpart = rl.substring(colonInd);

            final int dashIndx = rightpart.indexOf("-");
            final String fromPage = rightpart.substring(1, dashIndx).trim();

            final int lbracketIndx = rightpart.indexOf("(");
            final String toPage = rightpart.substring(rightpart.indexOf("-") + 1, lbracketIndx).trim();
            final String yearString = rightpart.substring(lbracketIndx + 1, rightpart.indexOf(")"));
            final String jTitle = rl.substring(0, leftIndx).trim();
            /*
             * System.out.println("TITLE : " + jTitle); System.out.println("VOLUM : " + volume);
             * System.out.println("FROM P: " + fromPage); System.out.println("TO P: " + toPage);
             * System.out.println("YEAR : " + yearString);
             */
            citationParam.citationPrms.put(JournalCitationAttr.title, jTitle);
            citationParam.citationPrms.put(JournalCitationAttr.volume, volume);
            citationParam.citationPrms.put(JournalCitationAttr.firstPage, fromPage);
            citationParam.citationPrms.put(JournalCitationAttr.lastPage, toPage);
            final Integer year = SwissProtToPIMS.getInteger(yearString);
            if (year != null) {
                citationParam.citationPrms.put(JournalCitationAttr.year, year);
            }
            return true;
        }
        return false;
    }

    /**
     * Method to parse "Submitted" type of citation treat it as Thesis citation. Data example: Submitted
     * (OCT-1995) to the EMBL/GenBank/DDBJ databases
     * 
     * @param rl
     * @param citationParam
     * @return
     */
    static void parseSubmittedCitation(final String rl, final Citation citationParam) {
        String yearString = rl.substring(rl.indexOf("("), rl.indexOf(")")).trim();
        final int lyearIndx = SwissProtToPIMS.findIndex(yearString, yearString.length() - 1, false);
        yearString = yearString.substring(lyearIndx + 1, yearString.length());
        // System.out.println("YEAR: " + yearString);
        citationParam.citationPrms.put(ThesisCitationAttr.details, rl);
        final Integer year = SwissProtToPIMS.getInteger(yearString);
        if (year != null) {
            citationParam.citationPrms.put(ThesisCitationAttr.year, year);
        }
    }

    static Integer getInteger(final String intString) {
        Integer year = null;
        try {
            year = Integer.decode(intString);
        } catch (final NumberFormatException e) {
            // Ok could not parse year. Carry on
            e.printStackTrace();
        }
        return year;
    }

    /**
     * Parse thesis citation to ThesisCitation class. Data example: Thesis (Year), Institution_name, Country.
     * 
     * @param rl
     * @param citationParam
     * @return
     */
    static boolean parseThesisCitation(final String rl, final Citation citationParam) {
        final StringTokenizer st = new StringTokenizer(rl, ",");
        if (st.hasMoreTokens()) {
            final String thName_year = st.nextToken();
            final int lbrindx = thName_year.indexOf("(");
            final int rbrindx = thName_year.indexOf(")");
            Integer year = null;
            String title = "";
            if (lbrindx != 0 && rbrindx != 0 && lbrindx > rbrindx) {
                title = thName_year.substring(0, lbrindx - 1);
                citationParam.citationPrms.put(ThesisCitationAttr.title, title);
                final String yearStr = thName_year.substring(lbrindx, rbrindx);
                year = SwissProtToPIMS.getInteger(yearStr);
                if (year != null) {
                    citationParam.citationPrms.put(ThesisCitationAttr.year, year);
                } else {
                    citationParam.citationPrms.put(ThesisCitationAttr.details, yearStr);
                }
            } else {
                return false;
            }
        }
        if (st.hasMoreTokens()) {
            final String institutionName = st.nextToken();
            citationParam.citationPrms.put(ThesisCitationAttr.institution, institutionName);
        }
        if (st.hasMoreTokens()) {
            final String country = st.nextToken();
            citationParam.citationPrms.put(ThesisCitationAttr.country, country);
        }
        return true;
    }

    /**
     * Find the first not digit in a String starting from "from" position to the right(true) or to the left
     * (false)
     * 
     * @param rl
     * @param from
     * @param toRight
     * @return index in a String
     */
    static int findIndex(final String rl, int from, final boolean toRight) {
        char c;
        do {
            if (toRight) {
                from = from + 1;
            } else {
                from = from - 1;
            }
            c = rl.charAt(from);
            // System.out.println("CHAR: " + c);
        } while (Character.isDigit(c));

        return from;
    }

    /**
     * Find the first not digit in a String starting from "from" position to the right(true) or to the left
     * (false)
     * 
     * @param rl
     * @param from
     * @param toRight
     * @return index in a String
     */
    static int findSpace(final String rl, int from, final boolean toRight) {
        char c;
        do {
            if (toRight) {
                from = from + 1;
            } else {
                from = from - 1;
            }
            c = rl.charAt(from);
            // System.out.println("CHAR: " + c);
        } while (Character.isWhitespace(c));

        return from;
    }

    static ArrayList getInitials(String author) {
        final ArrayList initials = new ArrayList();
        // Last author names may have ";" - it should be deleted
        final int indx = author.indexOf(";");
        if (indx >= 0) {
            final StringBuffer auth = new StringBuffer(author);
            auth.deleteCharAt(indx);
            author = auth.toString();
        }
        final StringTokenizer st = new StringTokenizer(author, ".");
        // Put initials into collection
        while (st.hasMoreTokens()) {
            final String init = st.nextToken().trim();
            initials.add(init);
            // System.out.println("Initials: " + init);
        }
        return initials;
    }

    /**
     * Upload Manchester targets with changed state (all new) These targets was selected during the data
     * analysis.
     * 
     * private static void getAddedTargets() { // Upload new Man targets File f = new
     * File("D:/MPSI_web/added_manchester_targets.txt"); try { BufferedReader bf = new BufferedReader(new
     * FileReader(f)); String line; while((line = bf.readLine()) != null) { String data =
     * UMTargetsRetrieval.getSwissProtEntry(line.trim()); System.out.println("SEQ: " + data );
     * Util.writeToFile(data, "D:/temp/mpsi/added/" + line.trim()); } } catch (FileNotFoundException e) {
     * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } }
     */

    /**
     * Method iterate other all files in the directory and construct SwissProtToEBITartes entities. After put
     * them to the database. For database connection details and etc..
     * 
     * @see EBITarget args[] - is not used
     * 
     *      public static void main(final String[] args) { final File[] files =
     *      Util.getAllFiles(SwissProtToPIMS.dirPath); SwissProtToPIMS.log = new
     *      LogWriter(SwissProtToPIMS.collectStatistic);
     *      SwissProtToPIMS.log.printLog("TOTAL number of SWISSPROT files: " + files.length,
     *      SwissProtToPIMS.LogInfo);
     * 
     *      for (int i = 0; i < files.length; i++) { SwissProtToPIMS.log.printLog("",
     *      SwissProtToPIMS.LogInfo); SwissProtToPIMS.log.printExact("Current file: " + files[i],
     *      SwissProtToPIMS.collectStatistic); final Date sd = new Date(System.currentTimeMillis());
     *      SwissProtToPIMS.log.printExact("Start processing time: " + sd, SwissProtToPIMS.collectStatistic);
     *      SwissProtToPIMS.log.printExact("Objects created so far: " + SwissProtToPIMS.objCounter,
     *      SwissProtToPIMS.collectStatistic);
     * 
     *      SwissProtToPIMS.count = i; new SwissProtToPIMS(files[i]);
     * 
     *      final Date ed = new Date(System.currentTimeMillis());
     *      SwissProtToPIMS.log.printExact("Full parsing took: " + (ed.getTime() - sd.getTime()) + " ms",
     *      SwissProtToPIMS.collectStatistic); } }
     */

}
