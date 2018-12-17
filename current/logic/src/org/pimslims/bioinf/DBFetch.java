package org.pimslims.bioinf;

/**
 * 
 * @author Peter Troshin
 * @date December 2007
 * 
 *       Protein Information Management System
 * @version: 2.0
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.naming.ServiceUnavailableException;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.bioinf.newtarget.BioFormatGuesser;
import org.pimslims.bioinf.newtarget.RecordParser;
import org.pimslims.bioinf.newtarget.RecordParser.DATABASES;
import org.pimslims.bioinf.newtarget.RecordParser.EMBLDbs;
import org.pimslims.lab.Util;

public class DBFetch {

    /**
     * ENTREZ String
     */
    private static final String ENTREZ = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?";

    public static enum Viewtype {
        full, text
    }

    public static String getDBrecord(final String database, final String accession) throws IOException,
        ServiceUnavailableException {
        String record = null;
        if (Util.isEmpty(accession)) {
            return null;
        }
        if (database.equalsIgnoreCase(EMBLDbs.EMBLCDS.toString())) {
            record = DBFetch.getEmblCdsEntry(accession);
        } else if (database.equalsIgnoreCase(DATABASES.SwissProt.toString())) {
            record = DBFetch.getUniprotEntry(accession);
        } else if (database.equalsIgnoreCase(DATABASES.GenBank.toString())) {
            record = DBFetch.getGenBankEntry(accession);
        } else if (database.equalsIgnoreCase(DATABASES.JCVI.toString())) {
            record = DBFetch.getGenBankEntry(DBFetch.getGenBankIdFromJCVI(accession));
        } else {
            // note that http://www.ebi.ac.uk/Tools/dbfetch/dbfetch/Uniprot/Q762B6 redirects to http://www.ebi.ac.uk/Tools/dbfetch/dbfetch/Uniprot/Q762B6
            record =
                NcbiBlastImpl.get("http://www.ebi.ac.uk/Tools/webservices/rest/dbfetch/" + database + "/"
                    + accession);
            if ("ERROR 12 No entries found.".equals(record)) {
                record = "";
            }
        }
        return record;
    }

    /**
     * Cut accession from this in context of a larger document
     * 
     * Method is highly volatile as it depends on the output of external web site!
     * DBFetch.getGenBankIdFromJCVI
     * 
     * @param JCVI_accession
     * @return
     * @throws IOException
     */
    public static String getGenBankIdFromJCVI(final String JCVI_accession) throws IOException {
        String resp = DBFetch.getResponse(DBFetch.getJCVIURL(JCVI_accession));

        if (Util.isEmpty(resp)) {
            return null;
        }
        final String searchKey = "&genbank=";
        final int genIdIdx = resp.indexOf(searchKey);
        if (genIdIdx == -1) {
            //Cannot find "GenBank ID:"
            // Later could fall back to uniprot
            return null;
        }
        resp = resp.substring(genIdIdx + searchKey.length());

        final int endIdx = resp.indexOf("&");
        if (endIdx == -1) {
            return null;
        }
        resp = resp.substring(0, endIdx);
        System.out.println("GENBANK ID:" + resp.trim());
        return resp.trim();
    }

    public static RecordParser getParser(final String record, final String cdsName) throws IOException,
        BioInfException {
        return BioFormatGuesser.getParser(record, cdsName);
    }

    public static String getEmblCdsEntry(final String accession) throws IOException {
        final URL url = new URL(DBFetch.getEMBLDBsURL(EMBLDbs.EMBLCDS.toString(), accession));
        return DBFetch.getWithoutBadPrefix(url);
    }

    /**
     * Method retrieve the uniprot database entry by accession number
     * 
     * @param accession uniprot database accession
     * @return uniprot entry
     */
    public static String getUniprotEntry(final String accession) throws IOException {
        final URL url = new URL(DBFetch.getUniprotEntryURL(accession, Viewtype.text));
        return DBFetch.getWithoutBadPrefix(url);
    }

    private static String getWithoutBadPrefix(final URL url) throws IOException {
        final BufferedReader in =
            new BufferedReader(new InputStreamReader(url.openStream()), DBFetch.BUFFER_SIZE);
        final StringBuffer sb = new StringBuffer();
        String line;
        boolean beforeHeader = true;
        while (null != (line = in.readLine())) {
            if (line.startsWith("ID ")) {
                beforeHeader = false;
            } else if (beforeHeader) {
                // this should not happen, see http://www.ebi.ac.uk/2can/tutorials/formats.html#swiss
                // but actually it sometimes does
                continue;
            }
            sb.append(line + "\r\n");
        }
        in.close();
        return sb.toString();
    }

    public static String getUniprotEntryURL(final String accession, final Viewtype type) {
        if (type == Viewtype.text) {
            return "http://www.uniprot.org/uniprot/" + accession + ".txt";
        } else {
            return "http://www.uniprot.org/uniprot/" + accession;
        }
    }

    /**
     * DBFetch.getGenBankEntry
     * 
     * @param accession
     * @return a GenBank file
     * @throws IOException
     */
    public static String getGenBankEntry(final String accession) throws IOException {
        // If numeric accession is requested the database in request seems to be ignored 
        // Thus additional check on sequence type is required
        String result = DBFetch.getGenbankNucleotideEntry(accession);
        if (result == null) {
            result = DBFetch.getGenbankProteinEntry(accession);
        }
        return result;
    }

    /**
     * Retrieve genbank nucleotide sequence from core database (does not include EST & GSS) by id
     * 
     * @param gbId
     * @return a GenBank file TODO check it is not HTML
     * @throws IOException
     */
    public static String getGenbankNucleotideEntry(final String gbId) throws IOException {
        final String url =
            "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?rettype=gbwithparts&strand=on&db=nucleotide&id="
                + StringEscapeUtils.escapeXml(gbId);
        // &from=60017&to=61213 
        System.out.println(url);
        return DBFetch.getResponse(url);
    }

    /**
     * Retrieve Genbank protein by id
     * 
     * @param gbId
     * @return GenBank file
     * @throws IOException
     */
    public static String getGenbankProteinEntry(final String gbId) throws IOException {
        return DBFetch.getResponse(DBFetch.getGenbankProteinEntryURL(gbId));
    }

    /**
     * Retrieve Genbank protein by id
     * 
     * @param gbId
     * @return
     * @throws IOException
     */
    private static String getGenbankProteinEntryURL(final String gbId) {
        //TODO fix this, the URL to get Genbank file format has changed
        //TODO I don't think we want the sendto=t, it redirects
        return DBFetch.ENTREZ + "db=protein&id=" + StringEscapeUtils.escapeXml(gbId) + "&sendto=t";
    }

    public static String getEntrezEntryURL(final String gbId, final Viewtype type) {
        if (type == Viewtype.text) {
            return DBFetch.ENTREZ + "id=" + StringEscapeUtils.escapeXml(gbId) + "&sendto=t";
        } else {
            return DBFetch.ENTREZ + "id=" + StringEscapeUtils.escapeXml(gbId);
        }
    }

    /**
     * Retrieve sub sequence 'from' to 'to' form the sequence (protein or nucleotide)
     * 
     * @param gbId
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    public static String getNucleotideEntry(final String gbId, final int from, final int to,
        final boolean reverseStrand) throws IOException {
        return DBFetch.getResponse(DBFetch.getNucleotideEntryURL(gbId, from, to, reverseStrand));
    }

    public static String getNucleotideEntryURL(final String gbId, final int from, final int to,
        final boolean reverseStrand) {
        String revStrand = "";
        if (reverseStrand) {
            revStrand = "&strand=on";
        }
        //TODO fix this, the URL to get Genbank file format has changed
        return DBFetch.ENTREZ + "val=" + StringEscapeUtils.escapeXml(gbId) + "&from=" + from + "&to=" + to
            + revStrand + "&sendto=t" + "&view=" + "gbwithparts";
    }

    // This return whole web page, there is no option for a clear text representation
    public static String getNCBITaxonomyURL(final int taxId) {
        return "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id=" + taxId; // &lvl=0
    }

    // This return whole web page, there is no option for a clear text representation
    public static String getNCBITaxonomyURL(final String speciesName) {
        return "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?name="
            + DBFetch.encodeName(speciesName);
    }

    private static String encodeName(final String str) {
        if (Util.isEmpty(str)) {
            return null;
        }
        return str.replace(" ", "+");
    }

    public static String getEMBLDBsURL(final String database, final String accession) {
        for (final RecordParser.EMBLDbs supportedDbs : RecordParser.EMBLDbs.values()) {
            if (database.trim().equalsIgnoreCase(supportedDbs.toString())) {
                return DBFetch.getURL(database.toLowerCase(), accession);
            }
        }
        return null;
    }

    private static String getURL(final String database, final String accession) {
        if (Util.isEmpty(accession)) {
            return null;
        }
        return "http://www.ebi.ac.uk/cgi-bin/dbfetch?db=" + database + "&style=raw&id=" + accession;
    }

    public static String getGOURL(final String accession) {
        return "http://amigo.geneontology.org/cgi-bin/amigo/go.cgi?view=details&search_constraint=terms&depth=0&query="
            + accession;
    }

    public static String getPrositeURL(final String accession) {
        return "http://www.expasy.ch/cgi-bin/prosite-search-ac?" + accession;
    }

    public static String getPfamURL(final String accession) {
        return "http://pfam.sanger.ac.uk/family?entry=" + accession;
    }

    public static String getSMRURL(final String accession) {
        return "http://swissmodel.expasy.org/repository/smr.php?sptr_ac=" + accession;
    }

    public static String getTIGRFAMsURL(final String accession) {
        return "http://cmr.tigr.org/tigr-scripts/CMR/HmmReport.cgi?hmm_acc=" + accession;
    }

    public static String getJCVIURL(final String accession) {
        // return "http://cmr.jcvi.org/cgi-bin/CMR/shared/AnnotationSearch.cgi?search_type=nt_locus&match_type=exact&search_string="
        //    + accession;
        return "http://cmr.jcvi.org/cgi-bin/CMR/shared/GenePage.cgi?locus=" + accession;
    }

    public static String getRefSeqURL(final String accession) {
        return DBFetch.getEntrezEntryURL(accession, Viewtype.full);
    }

    public static String getPIRURL(final String accession) {
        return "http://pir.georgetown.edu/cgi-bin/ipcEntry?id=" + accession;
    }

    public static String getKEGGURL(final String accession) {
        return "http://www.genome.ad.jp/dbget-bin/www_bget?" + accession;
    }

    public static String getSMARTURL(final String accession) {
        return "http://smart.embl-heidelberg.de/smart/do_annotation.pl?DOMAIN=" + accession;
    }

    public static String getProDomURL(final String accession) {
        return "http://prodom.prabi.fr/prodom/current/cgi-bin/request.pl?question=DBEN&query=" + accession;
    }

    public static String getHAMAPURL(final String accession) {
        return "http://www.expasy.org/unirule/" + accession;
    }

    public static String getPantherURL(final String accession) {
        return "http://www.pantherdb.org/panther/family.do?clsAccession=" + accession;
    }

    //TODO check this, the NCBI has changed some database URLs
    public static String getUniGeneURL(final String accession) {
        return "http://www.ncbi.nlm.nih.gov/sites/entrez?db=unigene&cmd=search&term=" + accession;
    }

    public static String getXtalPimsURL(final String accession, final String dbUrl) {
        return dbUrl + "/ViewPlate.jsp?barcode=" + accession;
    }

    public static String getISPyBDiamondURL(final String accession) {
        return "https://ispyb.diamond.ac.uk/ispyb/ispyb/user/viewResults.do?reqCode=display&dataCollectionId="
            + accession;
    }

    public static String getISPyBESRFURL(final String accession) {
        return "https://wwws.esrf.fr/ispyb/ispyb/user/viewResults.do?reqCode=display&dataCollectionId="
            + accession;
    }

    /**
     * Connects to the server using given URL and return the server response. TODO fix this, it does not
     * follow a 301 response
     * 
     * @param request URL request
     * @return server response
     */
    public static String getResponse(final String request) throws IOException {
        final URL url = new URL(request);
        final String response;
        try {
            response = DBFetch.downloadContent(url.openStream());
        } catch (final IOException e) {
            if (e.getMessage().startsWith("Server returned HTTP response code: 400")) {
                return null; // accession code not found
            }
            throw e;
        }
        if ("\nNothing has been found\n".equals(response)) {
            return null;
        }
        return response;
    }

    /**
     * Collect contents from InputStream to one string
     * 
     * @param inputStream
     * @return String representation of all data contained in InputStream
     * @throws IOException
     */
    public static String downloadContent(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream), DBFetch.BUFFER_SIZE);
        int charsRead;
        final char[] copyBuffer = new char[DBFetch.BUFFER_SIZE];
        final StringBuffer sb = new StringBuffer();
        while ((charsRead = in.read(copyBuffer, 0, DBFetch.BUFFER_SIZE)) != -1) {
            sb.append(copyBuffer, 0, charsRead);
        }
        in.close();
        return sb.toString();
    }

    /**
     * Size of the Stream copy receive buffer
     */
    public static final int BUFFER_SIZE = 1024;
}
