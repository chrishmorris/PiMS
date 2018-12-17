/**
 * V2_0-pims-web org.pimslims.bioinf.newtarget BioDBFormatMapper.java
 * 
 * @author Petr Troshin
 * @date 10 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr Troshin
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.IOException;

import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.newtarget.RecordParser.DATABASES;
import org.pimslims.bioinf.newtarget.RecordParser.EMBLDbs;
import org.pimslims.lab.Util;

/**
 * BioDBFormatMapper
 * 
 */
public class BioFormatGuesser {

    String database;

    public BioFormatGuesser(final String database) throws IOException {
        this.database = database;
    }

    // Formats supported by PIMS 2.0
    private enum BIOFORMATS {
        UniProt, GenbankNucleotide, GenbankProtein, EMBL
    }

    public static RecordParser getParser(final String recordToParse, final String cdsName)
        throws IOException, BioInfException {
        final BIOFORMATS format = BioFormatGuesser.guessFormat(recordToParse);
        if (format == null) {
            return null;
        }
        switch (format) {
            case UniProt:
                return new UniProtParser(recordToParse);
            case GenbankNucleotide:
                return new GenBankMultipleCDSNucleotideParser(recordToParse, cdsName, null);
            case GenbankProtein:
                return new GenBankProteinParser(recordToParse);
            case EMBL:
                return new EMBLNucleotideParser(recordToParse, cdsName);
            default:
                throw new RuntimeException("Sorry, but the sequence type has not been recognized");
        }
    }

    public static BIOFORMATS guessFormat(final String record) {

        if (ParserUtility.isEMBL(record)) {
            return BIOFORMATS.EMBL;
        }
        if (ParserUtility.isUniprot(record)) {
            return BIOFORMATS.UniProt;
        }
        if (ParserUtility.isGenBankDNA(record)) {
            return BIOFORMATS.GenbankNucleotide;
        }
        if (ParserUtility.isGenBankProtein(record)) {
            return BIOFORMATS.GenbankProtein;
        }

        return null;
    }

    public static boolean canParse(final String record) {
        if (record == null) {
            return false;
        }
        // record.length() should between 100 and 10240000
        if (record.length() < 100 || record.length() > 10024000) {
            return false;
        }
        if (BioFormatGuesser.guessFormat(record) == null) {
            return false;
        }
        return true;
    }

    /**
     * Unify the database names, remove ambiguity
     * 
     * @param dbname
     * @return
     */
    public static String getDBReferenceName(String dbname) {
        dbname = dbname.trim();
        if (dbname.equalsIgnoreCase("GI")) {
            return DATABASES.GenBank.toString();
        }
        // Known double referencing cases
        if (dbname.equalsIgnoreCase("UniProtKB/TrEMBL")) {
            return EMBLDbs.UniProtKB.toString();
        }
        if (dbname.equalsIgnoreCase("UniProtKB/Swiss-Prot")) {
            return DATABASES.SwissProt.toString();
        }

        // Unknown double referencing cases
        if (dbname.split("/").length > 1) {
            final String[] tok = dbname.split("/");
            for (final String dbn : tok) {
                final String dname = BioFormatGuesser.getStandardDBName(dbn);
                if (!Util.isEmpty(dname)) {
                    return dname; // good enough to find one database name only, do not care about loosing alias
                }
            }
        }
        final String dname = BioFormatGuesser.getStandardDBName(dbname);
        if (!Util.isEmpty(dname)) {
            return dname;
        }

        // Unrecognised database name
        return dbname;
    }

    private static String getStandardDBName(final String dbname) {
        for (final DATABASES recognized : DATABASES.values()) {
            if (dbname.equalsIgnoreCase(recognized.toString())) {
                return recognized.toString();
            }
        }
        for (final EMBLDbs recognized : EMBLDbs.values()) {
            if (dbname.equalsIgnoreCase(recognized.toString())) {
                return recognized.toString();
            }
        }
        return null;
    }

}
