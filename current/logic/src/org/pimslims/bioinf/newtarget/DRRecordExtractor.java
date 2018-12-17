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

import java.util.ArrayList;

import org.apache.commons.lang.NotImplementedException;
import org.pimslims.lab.Util;

/**
 * DRRecordExtractor This is an utility class to extract complete cross database reference line (DR) from EMBL
 * protein and Uniprot/Swissprot formats As unfortunately BioJava cannot extract requred reference For more
 * information on the problem please see {@link UniProtParserTester.testXReferences()}
 */
public class DRRecordExtractor {

    /*  Complex example of extracted DR list
     *  more often it will be one line only
     * DR   EMBL; X04398; CAA27985.1; -.
     * DR   EMBL; U00039; AAB18438.1; -.
     * DR   EMBL; U00096; AAC76488.1; -.
     * DR   EMBL; AE016768; AAN82692.1; -.
     * DR   EMBL; AE005569; AAG58572.1; -.
     * DR   EMBL; AP002565; BAB37735.1; -.
     * DR   EMBL; AE015356; AAN44940.1; ALT_INIT.
     */
    String record;

    String[] lines;

    public DRRecordExtractor(final String record) {
        this.record = record;
        this.lines = this.getLines();
    }

    String[] getLines() {
        final String[] lines = this.record.split("\n");
        final ArrayList<String> drlines = new ArrayList<String>();
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            if (Util.isEmpty(line)) {
                continue;
            }
            if (line.startsWith("DR") && line.contains("EMBL")) {
                drlines.add(line);
            }
        }
        return drlines.toArray(new String[drlines.size()]);
    }

    /**
     * 
     * @return all Ids found
     */
    public String[] getFullIdList() {
        throw new NotImplementedException("getFullIdList is not implemented yet");
    }

    /**
     * @return The list of ids from the second column, or second position after EMBL
     */
    public String[] getSecondaryIdList() {
        final ArrayList<String> sIds = new ArrayList<String>(this.lines.length);
        for (int i = 0; i < this.lines.length; i++) {
            String sid = this.getSecondaryIds(this.lines[i]);
            if (Util.isEmpty(sid)) {
                continue;
            }
            sid = sid.trim();
            if (this.isId(sid)) {
                sIds.add(this.clean(sid));
            }
        }
        return sIds.toArray(new String[sIds.size()]);
    }

    // valid id must be more than 5 characters and all letters must be in upper case and id must contains digits and letters.  
    boolean isId(final String id) {
        int charsCount = 0;
        int digitsCount = 0;
        if (id.length() > 5) {
            for (int i = 0; i < id.length(); i++) {
                if (Character.isLetter(id.charAt(i)) && Character.isUpperCase(id.charAt(i))) {
                    charsCount++;
                }
                if (Character.isDigit(id.charAt(i))) {
                    digitsCount++;
                }
            }
        }
        if (charsCount > 0 && digitsCount > 0) {
            return true;
        }

        return false;
    }

    /*
     * Remove version information from Id 
     * @return AAG58572.1 -> AAG58572 
     */
    String clean(String id) {
        final int dotIdx = id.indexOf('.');
        if (dotIdx > 0) {
            id = id.substring(0, dotIdx);
        }
        return id;
    }

    String getSecondaryIds(final String line) {
        final String[] id = line.split(";");
        if (id.length >= 3) {
            return id[2];
        }
        return null;
    }

    public int countIds() {
        throw new NotImplementedException("is not implemented yet");
    }

    public int countSecondaryIds() {
        return this.getSecondaryIdList().length;
    }

}
