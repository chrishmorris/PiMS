/**
 * pims-web org.pimslims.command.newtarget GenBankFetch.java
 * 
 * @author pvt43
 * @date 12 Nov 2007
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojavax.RankedCrossRef;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.CDS;
import org.pimslims.bioinf.CdsList;
import org.pimslims.bioinf.targets.ServletTargetCreator;
import org.pimslims.lab.Util;

/**
 * GenBankFetch
 * 
 */
public class GenBankProteinParser implements RecordParser {

    private RichSequence seq;

    private final String nucleotideEntryURL;

    private final String record;

    private RecordParser dnaRecordParser;

    public GenBankProteinParser(final String genBankProtein) throws IOException {
        this.record = genBankProtein;

        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(genBankProtein));
        // GenbankRichSequenceDB gbr = new GenbankRichSequenceDB();
        //    RichSequenceBuilderFactory seqFactory = this.getFactory(); // - Alternative factory

        // read the GenBank File
        try {
            // DNATools.getDNA().getTokinezation() // for DNA
            final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            // Better
            //RichSequenceIterator seqI =
            //   RichSequence.IOTools.readGenbankProtein(br, RichObjectFactory.getDefaultNamespace());
            //  RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
            // This does not work for protein sequences !! 
            // SequenceIterator sequences = SeqIOTools.readGenbank(br);
            // iterate through the sequences

            assert seqI.hasNext();

            this.seq = seqI.nextRichSequence();
            this.seq.getComments();
            final CDS cds = new CDS(this.getCodedBy());
            this.nucleotideEntryURL = GenBankParserTools.getGenBankNucleotideSubEntryURL(cds);
            this.setOtherRecordParser(this.seq.getName());
        } catch (final BioException ex) {
            // not in uniprot format
            ex.printStackTrace();
            throw new IllegalArgumentException("Sorry, there has been a error while creating the target.", ex);
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTaxId()
     */
    public int getTaxId() {
        return this.seq.getTaxon().getNCBITaxID();
    }

    /**
     * This is work in case of gen bank DNA sequnce!
     * 
     * @param seq
     * @return private String getProteinSequence() { FeatureHolder cds = seq.filter(new
     *         FeatureFilter.ByType("CDS"), true); String translation = null; for (Iterator<Feature> iter =
     *         cds.features(); iter.hasNext();) { Feature f = iter.next(); if
     *         (f.getAnnotation().containsProperty("translation")) { translation = (String)
     *         f.getAnnotation().getProperty("translation"); } } return translation; }
     */

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getAccession()
     */
    public String getAccession() {
        return this.seq.getAccession();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getComments()
     */
    public String getComments() {
        return ServletTargetCreator.makeString(this.seq.getComments(), "\n");
    }

    /**
     * @see orgorg.pimslims.bioinf.newtargetordParser#getRecordFormat()
     */
    public String getDatabaseFormat() {
        return DATABASES.GenbankProtein.toString();
    }

    /**
     * @see org.org.pimslims.bioinf.newtargetrdParser#getSequenceRelease()
     */
    public String getSequenceRelease() {
        return Integer.toString(this.seq.getVersion());
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getDnaSequence()
     */
    public String getDnaSequence() {
        if (this.dnaRecordParser != null) {
            return this.dnaRecordParser.getDnaSequence();
        }

        return "";
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
        return this.seq.getDescription();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getGeneName()
     */
    public String getGeneName() {
        String geneName = null;
        final FeatureHolder cds = this.getCDSFeature();
        if (cds.countFeatures() == 0) {
            return geneName;
        }

        for (final Iterator iterator = cds.features(); iterator.hasNext();) {
            final Feature f = (Feature) iterator.next();
            final Annotation annot = f.getAnnotation();
            geneName = ParserUtility.getProperty(annot, "gene");
            if (!Util.isEmpty(geneName)) {
                break;
            }
        }
        return geneName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinName()
     */
    public String getProteinName() {
        return this.seq.getName();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinSequence()
     */
    public String getProteinSequence() {
        return this.seq.seqString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSpeciesName()
     */
    public String getSpeciesName() {
        return this.seq.getTaxon().getDisplayName();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTargetName()
     */
    public String getTargetName() {
        return this.seq.getName();
    }

    /**
     * @see org.pimslims.org.pimslims.bioinf.newtargetgetXaccessions() This may not be useful for this format
     *      !
     */
    public BioDBReferences getXaccessions() {
        final RichFeature cds = this.getCDSFeature();
        if (cds.countFeatures() == 0) {
            return null;
        }

        for (final Iterator<RichFeature> iterator = cds.features(); iterator.hasNext();) {
            final RichFeature f = iterator.next();
            final Set<RankedCrossRef> refs = f.getRankedCrossRefs();
            if (refs != null && !refs.isEmpty()) {
                return new BioDBReferences(refs);
            }
            /*
            for (Iterator iterator2 = f.getRankedCrossRefs().iterator(); iterator2.hasNext();) {
                SimpleRankedCrossRef cr = (SimpleRankedCrossRef) iterator2.next();
                assertEquals("GeneID", cr.getCrossRef().getDbname());
                assertEquals("2777494", cr.getCrossRef().getAccession());
            }
            */
        }
        return null;
    }

    private RichFeature getCDSFeature() {
        final FeatureHolder cds = this.seq.filter(new FeatureFilter.ByType("CDS"));
        final RichFeature rf = (RichFeature) cds.features().next();
        return rf;
    }

    /*
     * Look in CDS of the record for annotation like 
     * /coded_by=complement(NC_005856.1:60017..61213)
     * 
     */
    private String getCodedBy() {
        final RichFeature rf = this.getCDSFeature();
        if (!rf.getAnnotation().containsProperty("coded_by")) {
            return null; // cannot find nucleotide sequence
        }
        return (String) rf.getAnnotation().getProperty("coded_by");
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSourceType()
     */
    public Type getSourceType() {
        return Type.protein;
    }

    /**
     * @see org.pimslims.biorg.pimslims.bioinf.newtargettLookedUpSourceURL() TODO FIX THIS: This is set only
     *      after calling getDNASequence at the moment
     */
    public String getLookedUpSourceURL() {
        return this.nucleotideEntryURL;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getBioEntry()
     */
    public CdsList getBioEntry() {
        return new CdsList(this.seq);
    }

    private void setOtherRecordParser(final String proteinName) throws IOException {

        String nucleotideEntry;
        try {
            nucleotideEntry = GenBankParserTools.getGenBankNucleotideSubEntry(this.nucleotideEntryURL);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("Nucleotide details not found at: " + this.nucleotideEntryURL, e);
        } catch (final IOException e) {
            throw new RuntimeException("Error getting: " + this.nucleotideEntryURL, e);
        }
        try {
            if (!Util.isEmpty(nucleotideEntry)) {
                //TODO probably new GenBankMultipeNucleotideParser(nucleotideEntry, proteinName);
                this.dnaRecordParser =
                    new GenBankMultipleCDSNucleotideParser(nucleotideEntry, null, proteinName);
            }
        } catch (final BioInfException e) {
            /* ignroe this
             * It is used for unrecoverable errors
             * But at least we can get the protein sequence
             * */
        }

    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getOtherRecordParser()
     */
    public RecordParser getOtherRecordParser() {
        return this.dnaRecordParser;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getLookedUpSourceDatabase()
     */
    public String getLookedUpSourceDatabase() {
        return DATABASES.GenbankNucleotide.toString(); //only one database for now
    }

}
