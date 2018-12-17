/**
 * pims-web org.pimslims.command.newtarget EMBLNucleotideParser.java
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

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.Location;
import org.biojava.bio.symbol.SymbolList;
import org.biojavax.RankedCrossRef;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.bio.taxa.NCBITaxon;
import org.pimslims.bioinf.CdsList;
import org.pimslims.bioinf.targets.ServletTargetCreator;
import org.pimslims.lab.Util;

/**
 * EMBLNucleotideParser parser for EMBL nucleotide records. Please note that EMBL protein sequence is similar
 * to Swissprot/Uniprot Thus UniProt parser should be used
 */
public class EMBLNucleotideParser implements RecordParser {

    RichSequence completeDnaSeq;

    RichFeature feature;

    Annotation recordAnnotation;

    String record;

    // TODO add , int featureNumber
    public EMBLNucleotideParser(final String record, final String cdsName) {
        // create a buffered reader to read the sequence file
        this.record = record;
        final BufferedReader br = new BufferedReader(new StringReader(record));
        try {
            final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readEMBL(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            this.completeDnaSeq = seqI.nextRichSequence();
            final CdsList list = new CdsList(this.completeDnaSeq);
            if (cdsName != null) {
                this.feature = ParserUtility.getCDSByName(cdsName, list);
            } else {
                this.feature = ParserUtility.getCDS(list, 0);
            }
            this.recordAnnotation = this.completeDnaSeq.getAnnotation();
        } catch (final BioException ex) {
            // not in uniprot format
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
    }

    // TODO add , int featureNumber
    public EMBLNucleotideParser(final String record, final int cdsNumber) {
        // create a buffered reader to read the sequence file
        this.record = record;
        final BufferedReader br = new BufferedReader(new StringReader(record));
        try {
            final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readEMBL(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            this.completeDnaSeq = seqI.nextRichSequence();
            this.feature = ParserUtility.getCDS(new CdsList(this.completeDnaSeq), cdsNumber);
            this.recordAnnotation = this.completeDnaSeq.getAnnotation();

        } catch (final BioException ex) {
            // not in uniprot format
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
    }

/*
    void setFeature(final int cdsNumber) {
        this.feature = ParserUtility.getCDS(this.completeDnaSeq, cdsNumber);
    }

    void setFeature(final String cdsName) {
        this.feature = ParserUtility.getCDSByName(cdsName, this.completeDnaSeq);
    } */

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getAccession()
     */
    public String getAccession() {
        return this.completeDnaSeq.getAccession();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getComments()
     */
    public String getComments() {
        return ServletTargetCreator.makeString(this.completeDnaSeq.getComments(), "\n"); //TODO check if this is still valid for multiple CDS sequences 
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getRecordFormat()
     */
    public String getDatabaseFormat() {
        return EMBLDbs.EMBL.toString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSequenceRelease()
     */
    public String getSequenceRelease() {
        String dbr = null;
        if (this.recordAnnotation.containsProperty("SV")) {
            dbr = (String) this.recordAnnotation.getProperty("SV");
        } else if (this.recordAnnotation.containsProperty("udat")
            && this.recordAnnotation.containsProperty("urel")) {
            dbr =
                (String) this.recordAnnotation.getProperty("udat") + "/rev "
                    + this.recordAnnotation.getProperty("urel");
        }
        return dbr;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getDnaSequence()
     */
    public String getDnaSequence() {
        final SymbolList sl = this.completeDnaSeq.getInternalSymbolList();
        // Some EMBL sequences has features defined outside the sequence
        // e.g. embl_cds:U22530 (ID=AAA64218)
        // In this case SymbolList cannot meaningfully return sub sequence
        // it returns hash code instead
        // EMBL CDS database have locations outside given sub sequence
        // this not need to be truncated 
        final Location loc = this.feature.getLocation();
        int length = 0;
        if (loc.getMin() >= this.completeDnaSeq.length() || loc.getMax() > this.completeDnaSeq.length()) {
            return this.getFullDnaSequence();
        }
        if (loc.isContiguous()) {
            length = loc.getMax() - loc.getMin();
            if (length > this.completeDnaSeq.length() || loc.getMax() > this.completeDnaSeq.length()) {
                return this.getFullDnaSequence();
            }
        }

        // This may be more reliable way of fixing problem described above
        try {
            final String sequence = this.feature.getLocation().symbols(sl).seqString();
            if (sequence.contains("@")) {
                return this.getFullDnaSequence();
            }
            return sequence;
        } catch (final OutOfMemoryError e) {
            // fix sometimes fails, for unknown reason
            return this.getFullDnaSequence();
        }
    }

    /**
     * This is to use with records from embl_cds database which contains inappropriate feature coordinates
     * (related to the whole genome sequence perhaps, not to the piece of it)
     * 
     * @return complete sequence of the record.
     */
    public String getFullDnaSequence() {
        //SymbolList sl = completeDnaSeq.getInternalSymbolList();
        // Some EMBL sequences has features defined outside the sequence
        // e.g. embl_cds:U22530 (ID=AAA64218)
        // In this case SymbolList cannot meaningfully return sub sequence
        // it returns hash code instead
        // This may be more reliable way of fixing problem described above
        return this.completeDnaSeq.seqString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSource()
     */
    public String getSource() {
        return this.record;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getFuncDescription()
     */
    public String getFuncDescription() {
        String func = ParserUtility.getProperty(this.feature.getAnnotation(), "function");
        if (Util.isEmpty(func)) {
            func = this.completeDnaSeq.getDescription();
        }
        return func;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getGeneName() Not mandatory in uniprot need an
     *      alternative
     */
    public String getGeneName() {
        return ParserUtility.getProperty(this.feature.getAnnotation(), "gene");
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinName()
     */
    public String getProteinName() {
        final String protName = ParserUtility.getProteinName(this.feature.getAnnotation());
        return Util.isEmpty(protName) ? this.completeDnaSeq.getName() : protName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinSeqeunce()
     */
    public String getProteinSequence() {
        return ParserUtility.getProperty(this.feature.getAnnotation(), "translation");
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSpeciesName() TODO getTaxtId by organism name
     */
    public String getSpeciesName() {
        final NCBITaxon tax = this.completeDnaSeq.getTaxon();
        String spName = null;
        if (tax == null) {
            final TaxonomyRecordExtractor trex = new TaxonomyRecordExtractor(this.record);
            if (trex.containsOS()) {
                spName = trex.getSpeciesName();
            }
        } else {
            spName = tax.getDisplayName();
        }
        return spName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTargetName()
     */
    public String getTargetName() {
        return this.getProteinName();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTaxId() It appears that biojava cannot extract OX
     *      line with NCBI tax ID
     */
    public int getTaxId() {
        int id = -1;
        final NCBITaxon tax = this.completeDnaSeq.getTaxon();
        // Attemp to extract taxid without biojava help 
        if (tax == null) {
            final TaxonomyRecordExtractor ex = new TaxonomyRecordExtractor(this.record);
            if (ex.containsOX()) {
                id = ex.getTaxId();
            }
        } else {
            id = tax.getNCBITaxID();
        }
        return id;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getXaccessions()
     */
    public BioDBReferences getXaccessions() {
        final Set<RankedCrossRef> crf = this.feature.getRankedCrossRefs();
        if (crf != null && !crf.isEmpty()) {
            return new BioDBReferences(crf);
        }
        return null;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSourceType()
     */
    public Type getSourceType() {

        if (this.feature.getAnnotation().containsProperty("translation")) {
            final String translation = (String) this.feature.getAnnotation().getProperty("translation");
            if (!Util.isEmpty(translation)) {
                return Type.complete;
            }
        }
        return Type.DNA;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getLookedUpSourceURL()
     */
    public String getLookedUpSourceURL() {
        return null;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSourceURL()
     */
    public String getSourceURL() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getBioEntry()
     */
    public CdsList getBioEntry() {
        return new CdsList(this.completeDnaSeq);
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getOtherRecordParser()
     */
    public RecordParser getOtherRecordParser() {
        return null;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getLookedUpSourceDatabase()
     */
    public String getLookedUpSourceDatabase() {
        return null;
    }

}
