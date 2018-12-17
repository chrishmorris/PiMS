/**
 * pims-web org.pimslims.bioinf.newtarget DRRecordExtractor.java
 * 
 * @author pvt43
 * @date 6 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.lab.Util;

/**
 * ORRecordExtractor This is an utility class to extract NCBI taxonomy id line (OX) from EMBL protein and
 * Uniprot/Swissprot formats As unfortunately BioJava cannot extract required reference
 */
public class TaxonomyRecordExtractor {

    /*  Complex example of extracted DR list
     *  more often it will be one line only
     *  OS   Anabaena sp. (strain PCC 7120).
     *  OC   Bacteria; Cyanobacteria; Nostocales; Nostocaceae; Nostoc.
     *  OX   NCBI_TaxID=103690 {evidence};
     *   
     */
    String record;

    String OXline;

    //TODO support for multiple OS lines
    // Consider getting the name using NCBI taxonomy query by taxId
    String OSline;

    public TaxonomyRecordExtractor(final String record) {
        this.record = record;
        this.extractLines();
    }

    void extractLines() {
        final String[] lines = this.record.split("\n");
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            if (Util.isEmpty(line)) {
                continue;
            }
            if (line.startsWith("OX") && line.contains("NCBI_TaxID=")) {
                this.OXline = line;
            }
            if (line.startsWith("OS")) {
                this.OSline = line;
            }
        }
    }

    /**
     * @return The list of ids from the second column, or second position after EMBL
     */
    public int getTaxId() {
        if (!Util.isEmpty(this.OXline)) {
            Matcher m = OX.matcher(this.OXline);
			if (!m.matches()) {
				return -1;
			}
			return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    public String getSpeciesName() {
        String spname = null;
        if (!Util.isEmpty(this.OSline)) {
            spname = this.OSline.substring(2).trim(); // cut OS 
        }
        return spname;
    }

    static final Pattern OX = Pattern.compile("OX\\s+NCBI_TaxID=(\\d+)(?: \\{.*\\})?+;\\s*");

    public boolean containsOX() {
        return !Util.isEmpty(this.OXline);
    }

    public boolean containsOS() {
        return !Util.isEmpty(this.OSline);
    }

}
