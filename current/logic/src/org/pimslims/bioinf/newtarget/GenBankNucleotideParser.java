/**
 * pims-web org.pimslims.command.newtarget GenBankNucleotideParser.java
 * 
 * @author Peter Troshin aka pvt43
 * @date 30 Nov 2007
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

import org.pimslims.bioinf.BioInfException;

/**
 * GenBankNucleotideParser This will only work for genbank single CDS entries. Please use
 * GenBankMultipleCDSEntryParser
 */
public class GenBankNucleotideParser extends GenBankMultipleCDSNucleotideParser {

    public GenBankNucleotideParser(final String genBankNucleotideEntry) throws IOException, BioInfException {
        super(genBankNucleotideEntry, null, null);
    }

    /**
     * 
     * @param seq
     * @return
     */
    @Override
    public String getProteinSequence() {
        String translation = null;
        if (this.feature.getAnnotation().containsProperty("translation")) {
            translation = (String) this.feature.getAnnotation().getProperty("translation");
        }
        return translation;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getDnaSequence()
     */
    @Override
    public String getDnaSequence() {
        //TODO feature may be misidentified, if there were overlapping CDSes.
        return this.feature.getLocation().symbols(this.seq.getInternalSymbolList()).seqString();
    }

}
