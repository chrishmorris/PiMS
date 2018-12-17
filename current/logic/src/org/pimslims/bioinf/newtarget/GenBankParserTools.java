/**
 * pims-web org.pimslims.command.newtarget GenBankParserTools.java
 * 
 * @author pvt43
 * @date 3 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.IOException;

import org.pimslims.bioinf.CDS;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.lab.Util;

/**
 * GenBankParserTools
 * 
 */
public class GenBankParserTools {

    /**
     * ENTREZ String - this URL used to return GenBank Files, but now returns HTML
     */
    private static final String ENTREZ = "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?";

    private GenBankParserTools() {
        // no instances
    }

    public static String getGenBankNucleotideSubEntryURL(final CDS cds) {

        if (Util.isEmpty(cds.getAccession())) {
            return null; // Cannot find accession 
        }

        return DBFetch.getNucleotideEntryURL(cds.getAccession(), cds.getFrom(), cds.getTo(),
            cds.isReverseStrand());
    }

    /**
     * GenBankParserTools.getGenBankNucleotideSubEntry
     * 
     * @param URL e.g.
     *            http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?val=nc_005856.1&from=60017&to=61213&strand
     *            =on&view=gbwithparts
     * @return TODO: change the URL to
     * 
     * 
     * 
     * 
     * 
     *         http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=nc_005856.1&from=
     *         60017 &rettype=gbwithparts&strand=on&to=61213
     * 
     * @throws IOException
     */
    public static String getGenBankNucleotideSubEntry(String url) throws IOException {
        if (url.startsWith(GenBankParserTools.ENTREZ)) {
            // this URL now gets an HTML format output, must replace
            String queryString = url.substring(GenBankParserTools.ENTREZ.length());
            queryString = queryString.replace("val=", "id=");
            queryString = queryString.replace("view=", "rettype=");
            queryString = queryString.replace("sendto=t", "");
            // from=, to=, strand= are OK
            url = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&" + queryString;
        }
        return DBFetch.getResponse(url);
    }

    /*
     * see getAccessionFromLocation help for examples of input
     */
    public static boolean canDetailedQuerybeMade(final String location) {
        if (location.toLowerCase().contains("join")) {
            return false;
        }
        // Commas are only used to separate non-contiguous locations
        if (location.toLowerCase().contains(",")) {
            return false;
        }
        return true;
    }

    /*
    * @return
    * complement(NC_005856.1:60017..61213) -> NC_005856.1 
    * NC_005856.1:60017..61213 -> NC_005856.1
    * 60017..61213 -> null
    * NC_005856.1:60017..61213,64112-65999 -> NC_005856.1
    * /coded_by="AJ626792.1:<1..>238"
    * join(DQ885807.1:<268..573,
                   DQ885807.1:3271..3367,DQ885807.1:3756..3812,
                   DQ885807.1:4015..4564,DQ885807.1:4735..5162,
                   DQ885807.1:5829..6296)"
    */
    public static String getAccessionFromLocation(String clocation) {
        if (clocation == null) {
            return null;
        }
        final int cidx = clocation.indexOf(':');
        if (cidx < 0) {
            return null; // There is no reference to accession
        }
        clocation = clocation.substring(0, cidx);
        final int leftbracketidx = clocation.indexOf('(');
        if (leftbracketidx < 0) {
            return clocation;
        } else {
            return clocation.substring(leftbracketidx + 1);
        }
    }

}
