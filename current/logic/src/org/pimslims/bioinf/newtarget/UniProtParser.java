/**
 * pims-web org.pimslims.command.newtarget UniProtParser.java
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
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleComment;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.pimslims.bioinf.CdsList;
import org.pimslims.bioinf.DBFetch;
import org.pimslims.bioinf.targets.ServletTargetCreator;
import org.pimslims.lab.Util;

/**
 * Uniprot format and its subset formats like SwissProt parser
 * 
 */
public class UniProtParser implements RecordParser {

    private RichSequence protSeq;

    private Annotation seqAn;

    private final String record;

    private String nucleotideURL;

    private RecordParser dnaRecordParser;

    private String nucSourceDatabase; // only one database for now 

    public UniProtParser(final String record1) throws IOException {

        /* clean the record.
         * A few Uniprot records have two lines for the taxon id
         * On 1 Oct 2014 Uniprot added evidence codes
         * BioJava 1.8.2 can't handle them 
        */
        this.record = record1.replaceFirst("OS.*\nOS", "OS")
        		.replaceAll(" \\{[^}]*\\}", "");
        		;

        final BufferedReader br = new BufferedReader(new StringReader(record));
        try {
            final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readUniProt(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            this.protSeq = seqI.nextRichSequence();
            this.seqAn = this.protSeq.getAnnotation();
            this.setOtherRecordParser(); // this is to obtain details of nucleotide record 
        } catch (final BioException ex) {
            // not in uniprot format
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.");
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.");
        }
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getAccession()
     */
    @Override
    public String getAccession() {
        return this.protSeq.getAccession();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getComments()
     */
    @Override
    public String getComments() {
        final Set comments = new HashSet<SimpleComment>();
        String function = "";
        for (final Object object : this.protSeq.getComments()) {
            final SimpleComment comment = (SimpleComment) object;
            System.out.println("UniProtParser comment [" + comment.getComment() + "]");
            final int i = comment.getComment().indexOf("-!- FUNCTION:");
            if (i < 0) {
                comments.add(comment);
            }
            if (i >= 0) {
                function = comment.getComment();
            }
        }
        System.out.println("UniProtParser function [" + function + "]");
        return ServletTargetCreator.makeString(comments, "\n");
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getRecordFormat()
     */
    @Override
    public String getDatabaseFormat() {
        return EMBLDbs.UniProt.toString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSequenceRelease()
     */
    @Override
    public String getSequenceRelease() {
        return (String) this.seqAn.getProperty("adat") + "/rev " + this.seqAn.getProperty("arel");
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getDnaSequence()
     */
    @Override
    public String getDnaSequence() {
        String dna = "";
        if (this.dnaRecordParser != null) {
            dna = this.dnaRecordParser.getDnaSequence();
        }
        // TODO If previous attempt failed lookup a nuc record by RefSeq cross references
        return dna;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSource()
     */
    @Override
    public String getSource() {
        return this.record;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getFuncDescription()
     */
    @Override
    public String getFuncDescription() {
        if (!"".equals(this.getFunction())) {
            return this.getFunction();
        }
        String desc = null;
        if (this.seqAn.containsProperty("DE")) {
            desc = ServletTargetCreator.makeString(this.seqAn.getProperty("DE"), " ");
        } else {
            desc = this.protSeq.getDescription();
        }

        return desc;
    }

    public String getFunction() {
        String function = "";
        for (final Object object : this.protSeq.getComments()) {
            final SimpleComment comment = (SimpleComment) object;
            final int i = comment.getComment().indexOf("-!- FUNCTION:");
            if (i >= 0) {
                function = comment.getComment();
            }
        }
        return function;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getGeneName() Not mandatory in uniprot need an
     *      alternative
     */
    @Override
    public String getGeneName() {
        String prop = "GN";
        return getProperty(prop);
    }

    /**
     * UniProtParser.getProperty
     * 
     * @param prop
     * @return
     */
    public String getProperty(String prop) {
        String ret = null;
        if (this.seqAn.containsProperty(prop)) {
            ret = ServletTargetCreator.makeString(this.seqAn.getProperty(prop), " ");
        }
        return ret;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinName()
     */
    @Override
    public String getProteinName() {
        final String protName = this.protSeq.getName();
        return protName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinSeqeunce()
     */
    @Override
    public String getProteinSequence() {
        return this.protSeq.seqString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSpeciesName() TODO getTaxtId by organism name
     */
    @Override
    public String getSpeciesName() {
        return this.protSeq.getTaxon().getDisplayName();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTargetName()
     */
    @Override
    public String getTargetName() {
        return this.getProteinName();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTaxId()
     */
    @Override
    public int getTaxId() {
        return this.protSeq.getTaxon().getNCBITaxID();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getXaccessions()
     */
    @Override
    public BioDBReferences getXaccessions() {
        return new BioDBReferences(this.protSeq.getRankedCrossRefs());
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSourceType()
     */
    @Override
    public Type getSourceType() {
        return Type.protein;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getLookedUpSourceURL()
     */
    @Override
    public String getLookedUpSourceURL() {
        return this.nucleotideURL;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getBioEntry()
     */
    @Override
    public CdsList getBioEntry() {
        return new CdsList(this.protSeq);
    }

    //TODO if this fails, continue without DNA details
    private void setOtherRecordParser() {
        // Attemp to lookup a nuc record by EMBL cross references
        this.nucSourceDatabase = EMBLDbs.EMBLCDS.toString().toLowerCase();
        EMBLNucleotideParser emparser = null;
        final DRRecordExtractor drr = new DRRecordExtractor(this.record);
        final String[] ids = drr.getSecondaryIdList();
        String nucEntry = null;
        String nucAccession = null;
        for (int i = 0; i < ids.length; i++) {
            try {
                nucEntry = DBFetch.getDBrecord(this.nucSourceDatabase, ids[i]);
                if (!Util.isEmpty(nucEntry)) {
                    // TODO add check for number of CDS
                    nucAccession = ids[i];
                    emparser = new EMBLNucleotideParser(nucEntry, null);
                    emparser.getFullDnaSequence();
                    this.nucleotideURL = this.nucSourceDatabase + ":" + nucAccession;
                    this.dnaRecordParser = emparser;
                    break;
                }
            } catch (final Exception e) {
                /* It is very unusual to catch all exceptions
                 * And even more unusual to ignore them. 
                 * However, these services have proved to be so fragile
                 * that in this case, that is all we can do.
                 * */
                e.printStackTrace();
            }
        }
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getOtherRecordParser()
     */
    @Override
    public RecordParser getOtherRecordParser() {
        return this.dnaRecordParser;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getLookedUpSourceDatabase()
     */
    @Override
    public String getLookedUpSourceDatabase() {
        return this.nucSourceDatabase;
    }

}
