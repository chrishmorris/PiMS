package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.pimslims.bioinf.DBFetch;
import org.pimslims.lab.Util;

import org.pimslims.model.target.Target;

/**
 * This class is designed to read the proteins from the tab delimited file (ProteinList) with protein names.
 * THe class tried to find and to download the appropriate SwissProt entries into the local folder (filePath)
 * In order to classify the proteins, class reads 4 files each of which contains the list of the proteins with
 * the same function.
 * 
 * <PRE>
 * The example files is shown in the repository under data directory
 * subBindProt = Substrate binding proteins List
 * aTPBindProt = ATP binding proteins
 * membraneProt = Membrane Proteins
 * regulProt = regulatory proteins
 * </PRE>
 * 
 * All other proteins are fall to the Substrate binding proteins (SUB_SPEC_PROT) category. This class
 * dependents on some methods in uk.ac.Util.class
 * 
 * @see uk.ac.mpsi.Util
 * @author Petr Troshin <br>
 *         Created on 11-Apr-2005
 * @deprecated This class is no longer supported and does not reflect the changes regarding target recording
 *             in PIMS
 */
@Deprecated
public class UMTargetsRetrieval {
    /**
     * This ArrayList contains all proteins name extracted from the file
     */
    ArrayList genesName;

    /**
     * This ArrayList contains all not found proteins
     */
    ArrayList notFound;

    /**
     * This ArrayList contains all proteins with not direct match of its identifier to the database
     */
    ArrayList notExactMatchFound; // 

    /**
     * This ArrayList contains all proteins which has a properly identifier and can be directly found in the
     * database
     */
    ArrayList found;

    /**
     * HashMap used to get the classification of the proteins, each map represent the list of the proteins
     * fall into one of the classification group. The classification is uploaded from the corresponding files
     * contains the list of the protein from one classification group. (Except Substrate specific proteins)
     * All protein which were not found into any other group fall into the category - Substrate specific
     * proteins. These HashMap are only populated and used when #getWorkPackage() method is called. It happens
     * when SwissProtTarges are uploading to the EBI Targets Tracker database.
     * 
     * @see #getWorkPackage(String)
     */

    /**
     * This HashMap contains Substrate Binding Proteins
     */
    static final HashMap SUB_BIND_PROT = new HashMap();

    /**
     * This HashMap contains ATP - binding proteins
     */
    static final HashMap ATP_BIND_PROT = new HashMap();

    /**
     * This HashMap contains membrane proteins (3.A group in TCDB classification)
     */
    static final HashMap MEMBRANE_PROT = new HashMap();

    /**
     * This HashMap contains Regulator proteins
     */
    static final HashMap REGUL_PROT = new HashMap();

    /**
     * This HashMap contains Substrate specific proteins
     */
    static final HashMap SUB_SPEC_PROT = new HashMap();

    /**
     * Where to get the information on the proteins types These files are only the sources for workpackage
     * classification and are only populated and used when #getWorkPackage() method is called.
     * 
     * @see #ATP_BIND_PROT etc
     */
    static final String subBindProt =
        "C:/Documents and Settings/pvt43/Desktop/targets_tracking/Excel_extracts/targets_Sub_build_prot.txt";

    static final String aTPBindProt =
        "C:/Documents and Settings/pvt43/Desktop/targets_tracking/Excel_extracts/targets_ATP_bind_prot.txt";

    static final String membraneProt =
        "C:/Documents and Settings/pvt43/Desktop/targets_tracking/Excel_extracts/targets_membr_prot.txt";

    static final String regulProt =
        "C:/Documents and Settings/pvt43/Desktop/targets_tracking/Excel_extracts/targets_regul_prot.txt";

    /**
     * Path to write the obtained SwissProt files
     */
    static String filePath = "c:/temp/mpsi/";

    /**
     * The path to the protein list
     */
    static final String proteinList =
        "C:/Documents and Settings/pvt43/Desktop/targets_tracking/Excel_extracts/Targets_Database_clear1.txt";

    /**
     * Load predefined types
     */
    static {
        loadTypes();
    }

    /**
     * Methods loads the list of proteins from files to the HashMap
     * 
     * @param bf BufferReader
     * @param hm HashMap
     */
    private static void loadType(BufferedReader bf, HashMap hm) {
        String line;
        try {
            while ((line = bf.readLine()) != null) {
                line = line.trim();
                if (!Util.isEmpty(line)) {
                    hm.put(line, "");
                }
            }
            bf.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Method loads the list of proteins from the files statically defined above to the defined above named
     * HashMaps. The hashmaps are the following:
     * 
     * @see #SUB_BIND_PROT
     * @see #ATP_BIND_PROT
     * @see #MEMBRANE_PROT
     * @see #SUB_SPEC_PROT
     * @see #REGUL_PROT The files are the following:
     * @see #aTPBindProt
     * @see #membraneProt
     * @see #subBindProt
     * @see #regulProt
     */
    private static void loadTypes() {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(subBindProt));
            BufferedReader bfATP = new BufferedReader(new FileReader(aTPBindProt));
            BufferedReader bfmem = new BufferedReader(new FileReader(membraneProt));
            BufferedReader bfregul = new BufferedReader(new FileReader(regulProt));
            loadType(bf, SUB_BIND_PROT);
            loadType(bfATP, ATP_BIND_PROT);
            loadType(bfmem, MEMBRANE_PROT);
            loadType(bfregul, REGUL_PROT);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * In time of construction of UMTargetsRetrieval object the Target list passes to it. UMTargetsRetrieval
     * parses this list and downloads the proteins from it to the #filePath folder.
     * 
     * @param targetsList
     */
    public UMTargetsRetrieval(String targetsList) {
        try {
            parse(targetsList);
            getSummary();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Parse the targetsList to the individual proteins, download appropriate SwissProt entries, write
     * statistics
     * 
     * @param targetsList
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void parse(String targetsList) throws IOException {
        String line = "";
        genesName = new ArrayList();
        notFound = new ArrayList();
        notExactMatchFound = new ArrayList();
        found = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(targetsList));
        while ((line = br.readLine()) != null) {
            if (Util.isEmpty(line.trim())) {
                continue;
            }
            StringTokenizer st = new StringTokenizer(line);
            String orgShortName = (String) st.nextElement();
            while (st.hasMoreElements()) {
                String protName = (String) st.nextElement();
                setOrganism(protName, orgShortName);
                String accession = getAccession(extractDesc(protName));
                if (!Util.isEmpty(accession)) {
                    String swissProtEntry = getSwissProtEntry(accession);
                    if (!Util.isEmpty(swissProtEntry)) {
                        Util.writeToFile(swissProtEntry, filePath + protName);
                    } else {
                        System.out.println("For Entry number: " + accession);
                        System.out
                            .println("Accession number has been found, but the sequence downloading has failed! "
                                + "/n");
                    }
                }
            }
        }
        System.out.println("Total not direct matches: " + notExactMatchFound.size());
        System.out.println("Total direct matches: " + found.size());
        System.out.println("Total not found: " + notFound.size());
    }

    /**
     * The method gets the SwissProt database accession number for the particular protein.
     * 
     * @param geneLocNames response from the SwissProt search server. May contain the right entry, or search
     *            results page.
     * @return accession number or null if nothing found.
     */
    private String getAccession(String[] geneLocNames) throws IOException {
        int idxpa = -1;
        String data = DBFetch.getResponse("http://www.expasy.org/cgi-bin/sprot-search-de?" + geneLocNames[0]);
        if (data.indexOf("Search in Swiss-Prot and TrEMBL for:") >= 0
            && data.indexOf("There are matches to") >= 0 && data.indexOf("There are matches to 0 ") < 0) {
            System.out.println("Not direct match. Look at it carefully! ");
            System.out.println("LocusName is: " + geneLocNames[0]);
            notExactMatchFound.add(geneLocNames[0]);
            return getAccessionFromSearchPage(data);

        } else if ((idxpa = data.indexOf("\"black\">Primary accession number")) >= 0) {
            System.out.println("Direct match! ");
            System.out.println("LocusName is: " + geneLocNames[0]);
            found.add(geneLocNames[0]);
            return getAccessionFromData(data.substring(idxpa));

        } else {
            System.out.println("The protein could not be found on the basis of the provided data!");
            System.out.println("The protein name is: " + geneLocNames[0] + "\n");
            notFound.add(geneLocNames[0]);
            return "";
        }
    }

    /**
     * The method gets the WorkPackage for EBI Targets tracker
     * 
     * @param protName Name of the protein
     * @return The Workpackage for the EBI targets tracker
     */
    public static String getWorkPackage(String protName) {
        if (ATP_BIND_PROT.containsKey(protName)) {
            return EBITarget.classWpackage.get("ABC.ATP");
        } else if (MEMBRANE_PROT.containsKey(protName)) {
            return EBITarget.classWpackage.get("3.A.");
        } else if (REGUL_PROT.containsKey(protName)) {
            return EBITarget.classWpackage.get("ABC.R");
        } else if (SUB_BIND_PROT.containsKey(protName)) {
            return EBITarget.classWpackage.get("ABC.S");
        } else {
            return EBITarget.classWpackage.get("ABC.SS");
        }
    }

    /**
     * Set the short name of the protein source organism. Can be used for verification purpose
     * 
     * @param protName
     * @param orgShortName
     */
    private void setOrganism(String protName, String orgShortName) {
        if (ATP_BIND_PROT.containsKey(protName)) {
            ATP_BIND_PROT.put(protName, orgShortName);
        } else if (MEMBRANE_PROT.containsKey(protName)) {
            MEMBRANE_PROT.put(protName, orgShortName);
        } else if (REGUL_PROT.containsKey(protName)) {
            REGUL_PROT.put(protName, orgShortName);
        } else if (SUB_BIND_PROT.containsKey(protName)) {
            SUB_BIND_PROT.put(protName, orgShortName);
        } else {
            SUB_SPEC_PROT.put(protName, orgShortName);
        }
    }

    /**
     * Print the summary of all downloaded entries to the stndout.
     */
    private void getSummary() {
        System.out.println("Direct match! ");
        System.out.println("LocusName is: ");
        for (int i = 0; i < found.size(); i++) {
            System.out.println(found.get(i));
        }

        System.out.println("The protein could not be found on the basis of the provided data!");
        System.out.println("The protein name is: ");
        for (int i = 0; i < notFound.size(); i++) {
            System.out.println(notFound.get(i));
        }

        System.out.println("Not direct match. Look at it carefully! ");
        System.out.println("LocusName is: ");
        for (int i = 0; i < notExactMatchFound.size(); i++) {
            System.out.println(notExactMatchFound.get(i));
        }
    }

    /**
     * Retrieve the database accession number from the search page.
     * 
     * @param rawResponce from SwissProt search system
     * @return protein accession number
     */
    private static String getAccessionFromSearchPage(String rawResponce) {
        int ind = rawResponce.indexOf("niceprot.pl?");
        rawResponce = rawResponce.toUpperCase();
        rawResponce = rawResponce.substring(ind);
        ind = rawResponce.indexOf("<HR>");
        rawResponce = rawResponce.substring(0, ind);
        rawResponce = rawResponce.substring(rawResponce.indexOf("<B>"), rawResponce.indexOf("</B>"));
        String protName = rawResponce.substring(3);
        System.out.println("Found protein accession is: " + protName + "\n");
        return protName;
    }

    /**
     * Retrieve the accession number from SwissProt server response contained particular protein
     * 
     * @param rawData protein information page rawData
     * @return protein database accession number
     */
    private static String getAccessionFromData(String rawData) {
        // System.out.println(rawData);
        rawData = rawData.substring(rawData.indexOf("\"black\">Primary accession number"));
        // rawData = rawData.toLowerCase();
        rawData = rawData.substring(0, rawData.indexOf("</tr>"));
        // System.out.println("after " + rawData);
        rawData = rawData.substring(rawData.indexOf("<b>"), rawData.indexOf("</b>"));
        String protName = rawData.substring(3);
        System.out.println("Found protein accession is: " + protName + "\n");
        return protName;
    }

    /**
     * Method retrieve the SwissProt database entry by accession number
     * 
     * @param accession SwissProt database accession
     * @return SwissProt entry
     */
    public static String getSwissProtEntry(String accession) throws IOException {
        return DBFetch.getResponse("http://www.expasy.org/cgi-bin/get-sprot-raw.pl?" + accession);
    }

    /**
     * The method separate gene name and locusName record format is: src4563(pheA), src4563 - locus name pheA -
     * gene name
     * 
     * @return String[0] - locus Name; String[1] - gene Name
     */
    private String[] extractDesc(String geneLocName) {
        String[] geneLocNames = new String[2];
        int startGeneName = geneLocName.indexOf("(");
        int endGeneName = geneLocName.indexOf(")");
        if (startGeneName < 0 && endGeneName < 0) {
            geneLocNames[0] = geneLocName;
            return geneLocNames;
        }
        geneLocNames[0] = geneLocName.substring(0, startGeneName);
        geneLocNames[1] = geneLocName.substring(startGeneName, endGeneName);
        return geneLocNames;
    }

    /**
     * Start to retrieve the information on targets
     * 
     * @param args - not in use
     */
    public static void main(String[] args) throws Exception {
        // UMTargetsRetrieval ut = new UMTargetsRetrieval(proteinList);
        /* HashMap prop = (HashMap) */Target.class.getDeclaredField("initClassDict")
            .get("java.lang.HashMap");
        /*
         * for (int i = 0; i < prop.getType().getDeclaredFields().length; i++) {
         * System.out.println(prop.getType().getDeclaredFields()[i]); } System.out.println();
         */
    }
}
