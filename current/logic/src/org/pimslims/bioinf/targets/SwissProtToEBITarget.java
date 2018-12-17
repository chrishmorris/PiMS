package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;
import org.pimslims.lab.Util;

/**
 * Class designed to upload the SwissProt entries from files to the EBI TT database. Class reads appropriate
 * fields from SwissProt file and puts is to the TT database.
 * 
 * @author Petr Troshin <br>
 *         Created on 18.02.2005
 */

/**
 * <p>
 * Fields mapping:
 * 
 * <PRE>
 *  EBI TT vs SwissProt mappings 
 *  additionalRemarks = DE -Description. 
 *  annotationUrl = link to SwissProt file
 *  database = &quot;SWISSPROT&quot;;
 *  databaseID = AC- Accession number(s) 
 *  proteinName = GN- This line contains the name(s) of the gene(s) that encode for the stored protein sequence.
 *  species = OS - Organism species
 *  tagretID = next from 1 
 *  workpackage = 3A see KW field - Keywords.
 *  DR - Database cross-references.
 * </PRE>
 * 
 * @deprecated This class is no longer supported
 */
@Deprecated
public class SwissProtToEBITarget extends EBITarget {

    /**
     * Path to the directory contains swissProt files
     */
    static final String dirPath = "d:/temp/mpsi/";

    /**
     * Class perform all task in time of construction the SwissProtToEBITarget object.
     * 
     * @param swissProtfile
     */
    public SwissProtToEBITarget(File swissProtfile) {
        super();
        parse(swissProtfile);
    }

    /**
     * Method performs all the work Load SwissProt file Gets appropriate fields from the annotation Clean this
     * fields from redundant and unsuitable information
     * 
     * @see UMTargetsRetrieval
     * @see UMTargetsRetrieval#getWorkPackage(String)
     */
    public void parse(File swissProtfile) {
        BufferedReader br = null;
        try {

            // create a buffered reader to read the sequence file
            br = new BufferedReader(new FileReader(swissProtfile));

        } catch (FileNotFoundException ex) {
            // can't find specified file
            ex.printStackTrace();
            System.exit(-1);
        }

        // read the GenBank File
        SequenceIterator sequences = SeqIOTools.readSwissprot(br);

        // iterate through the sequences
        while (sequences.hasNext()) {
            try {
                Sequence seq = sequences.nextSequence();
                this.sequence = seq.seqString();

                Annotation seqAn = seq.getAnnotation();
                for (Iterator i = seqAn.keys().iterator(); i.hasNext();) {
                    i.next();
                    this.database = "SWISSPROT";
                    this.databaseID = getAccession((String) seqAn.getProperty("AC"));
                    this.eBITargetStatus[0].additionalRemarks = makeString(seqAn.getProperty("DE"));
                    this.proteinName = getProtName(makeString(seqAn.getProperty("GN")));
                    this.species = makeString(seqAn.getProperty("OS"));
                    this.workpackage = UMTargetsRetrieval.getWorkPackage(swissProtfile.getName());
                }
            } catch (BioException ex) {
                // not in SwissProt format
                ex.printStackTrace();
            } catch (NoSuchElementException ex) {
                // request for more sequence when there isn't any
                ex.printStackTrace();
            }
        }
    }

    /**
     * Remove any additional DB references.
     * 
     * @param accession
     * @return the first DB reference
     */
    private static String getAccession(String accession) {
        int ind = accession.indexOf(";");
        if (ind < 0) {
            return accession;
        }
        return accession.substring(0, ind).trim();
    }

    /**
     * Remove all additional information from "GN" field
     * 
     * @param gnline "GN" lines
     * @return protein name
     */
    private String getProtName(String gnline) {
        int nidx = gnline.toLowerCase().indexOf("name");
        if (nidx < 0) {
            return gnline;
        }
        int eq = gnline.indexOf("=");
        int end = gnline.indexOf(";");
        return gnline.substring(eq + 1, end);
    }

    /**
     * Method receives ArrayList or String and return only strings by concatenating different values from
     * ArrayList to one String
     * 
     * @param untype ArrayList or String
     * @return String
     */
    private String makeString(Object untype) {
        String str = "";
        if (untype instanceof ArrayList) {
            for (Iterator i = ((ArrayList) untype).iterator(); i.hasNext();) {
                str += " " + (String) i.next();
            }
            return str.substring(1);
        }
        return (String) untype;
    }

    /**
     * Method iterate other all files in the directory and construct SwissProtToEBITartes entities. After put
     * them to the database. For database connection details and etc..
     * 
     * @see EBITarget args[] - is not used
     */
    public static void main(String[] args) {
        File[] files = Util.getAllFiles(dirPath);
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
            SwissProtToEBITarget sw = new SwissProtToEBITarget(files[i]);
            sw.targetID = SwissProtToPIMS.getUMTargetID(i + 1);
            sw.annotationUrl = serverPath + "umtargets/" + files[i].getName();
            try {
                sw.writeToDb();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            System.out.println(sw);
        }
    }

}
