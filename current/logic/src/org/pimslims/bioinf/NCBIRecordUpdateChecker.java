/**
 * current-pims-web org.pimslims.bioinf NCBIRecordUpdateChecker.java
 * 
 * @author Petr
 * @date 12 Mar 2008
 * 
 * Protein Information Management System
 * @version: 2.1
 * 
 * Copyright (c) 2008 Petr 
 * 
 * 
 */
package org.pimslims.bioinf;

/**
 * NCBIRecordUpdateChecker This class checks the date of modification of genbank record Please note that this
 * date will be changed in the event of any modification of genbank record. This may be some changes in the
 * annotation, like gene name, thus, this is not necessarily to do with sequence modifications. If sequence is
 * modified genbank a new genbank id (gi number) will be issued. Gb record part example
 * 
 * LOCUS HSU07343 2484bp mRNA linear PRI 29-NOV-1995
 */
public class NCBIRecordUpdateChecker {
    /*
     * Tasks load all gb sources from current PIMS target record.  
     * Extract date of modification and gi number
     * Download new record using extracted accession number
     * Extract the last modified date (+ may be some other info) 
     * Compare the dates. 
     * If downloaded record date is more recent, then report an update
     * TODO also distinguish between sequence and other updates, indicate them using different color
     * 
     * Design intention
     * class to load gb data from PIMS
     * class to hold the data
     * existed class to DBFetch to get new records.
     * .jsp & servlet for displaying results
     */
}
