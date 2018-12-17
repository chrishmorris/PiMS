/**
 * V4_3-web org.pimslims.bioinf CDS.java
 * 
 * @author cm65
 * @date 13 Oct 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.bioinf;

import org.pimslims.bioinf.newtarget.GenBankParserTools;
import org.pimslims.lab.Util;

/**
 * CDS represents the location of a coding sequence within an operon or gene
 * 
 * TODO support non-contiguous coding sequences. Will biojava do this parsing for us?
 */
public class CDS {

    private final boolean isReverseStrand;

    private final String accession;

    private int to;

    private int from;

    /**
     * Constructor for CDS
     * 
     * see getAccessionFromLocation help for examples of input
     * 
     * @param codedBy
     */
    public CDS(String codedBy) {

        if (Util.isEmpty(codedBy)) {
            throw new IllegalArgumentException("CodedBy is required");
        }
        System.out.println(codedBy); //TODO remove
        if (!GenBankParserTools.canDetailedQuerybeMade(codedBy)) {
            throw new IllegalArgumentException("Sorry, PiMS does not yet support: " + codedBy);
        }
        this.isReverseStrand = codedBy.startsWith("complement");
        this.accession = GenBankParserTools.getAccessionFromLocation(codedBy);
        final int cidx = codedBy.indexOf(':');
        if (cidx < 0) {
            throw new IllegalArgumentException("No accession number in: " + codedBy);
        }
        codedBy = codedBy.substring(cidx + 1);
        // something like this should remain 60017..61213)
        final int ldelimiterIdx = codedBy.indexOf("..");
        if (ldelimiterIdx < 0) {
            throw new IllegalArgumentException("No end location found in: " + codedBy);
        }
        String fromLoc = codedBy.substring(0, ldelimiterIdx);
        String toLoc = codedBy.substring(ldelimiterIdx + 2);
        final int rightBracketIdx = toLoc.indexOf(')');
        if (rightBracketIdx > 0) {
            toLoc = toLoc.substring(0, rightBracketIdx);
        }
        // Each location can be ambiguous e.g. <344..>3434
        // at this point we can remove this
        fromLoc = fromLoc.replace(">", "").replace("<", "");
        toLoc = toLoc.replace(">", "").replace("<", "");
        // Now make sure that only digits remains
        this.from = -1;
        this.to = -1;

        this.from = Integer.parseInt(fromLoc);
        this.to = Integer.parseInt(toLoc);
    }

    /**
     * CDS.isReverseStrand
     * 
     * @return
     */
    public boolean isReverseStrand() {
        return this.isReverseStrand;
    }

    /**
     * CDS.getAccession
     * 
     * @return
     */
    public String getAccession() {
        return this.accession;
    }

    /**
     * CDS.getTo
     * 
     * @return
     */
    public int getTo() {
        return this.to;
    }

    /**
     * CDS.getFrom
     * 
     * @return
     */
    public int getFrom() {
        return this.from;
    }

}
