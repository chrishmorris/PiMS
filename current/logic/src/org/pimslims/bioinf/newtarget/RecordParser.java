/**
 * pims-web org.pimslims.command.newtarget RecordParser.java
 * 
 * @author pvt43
 * @date 28 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import org.pimslims.bioinf.CdsList;

/**
 * RecordParser Interface define methods that is required for recording the target in PIMS
 */
public interface RecordParser {

    public static enum Type {
        protein, DNA, complete
    }

    /**
     * This is a enumeration of biological record formats which are supported by PIMS
     * 
     * Only 4 formats are recognised by PIMS 2.0 - GenbankNucleotide, GenbankProtein, UniProt, EMBL
     */
    public static enum DATABASES {
        SwissProt, // now the same as Uniprot 
        GenBank, GenbankProtein, GenbankNucleotide, GO, Prosite, Pfam, SMR, TIGRFAMs, JCVI, RefSeq, PIR, KEGG, SMART, ProDom, HAMAP, PANTHER, UniGene, XtalPims, ISPyBDiamond, ISPyBESRF, unspecified
    }

    /**
     * List of databases retrieval from which supported by EBI web services
     * 
     */
    public static enum EMBLDbs {
        IPI, HGVBase, InterPro, PDB, EMBL, EMBLSVA, EMBLCDS, EmsembleGene, UniProt, UniParc, JPO_PRT, EPO_PRT, USPTO_PRT, UniRef50, UniRef100, UniRef90, UniProtKB, GenomeReviews
    }

    public CdsList getBioEntry();

    /**
     * 
     * @return Looked up source BioEntry
     */
    public RecordParser getOtherRecordParser();

    /**
     * The name of the database of the record
     * 
     * @return
     */
    public String getDatabaseFormat();

    /**
     * The accession of the record in the database returned by getDatabase
     * 
     * @return
     */
    public String getAccession();

    /**
     * Accessions to other databases
     * 
     * @return
     */
    public BioDBReferences getXaccessions();

    public String getSpeciesName();

    public int getTaxId();

    public String getDnaSequence();

    public String getProteinSequence();

    public String getGeneName();

    public String getProteinName();

    public String getComments();

    public String getFuncDescription();

    public String getSource();

    public Type getSourceType();

    public String getLookedUpSourceURL();

    public String getLookedUpSourceDatabase();

    public String getTargetName();

    public String getSequenceRelease();
}
