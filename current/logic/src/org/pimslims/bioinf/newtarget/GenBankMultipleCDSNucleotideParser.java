/**
 * pims-web org.pimslims.command.newtarget GenBankMuptipleCDSNucleotideParser.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.Location;
import org.biojavax.RankedCrossRef;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.ontology.SimpleComparableTerm;
import org.pimslims.bioinf.BioInfException;
import org.pimslims.bioinf.CdsList;
import org.pimslims.bioinf.targets.ServletTargetCreator;
import org.pimslims.lab.Util;

/**
 * GenBankMuptipleCDSNucleotideParser
 */
public class GenBankMultipleCDSNucleotideParser implements RecordParser {

    RichSequence seq = null;

    private final String record;

    final RichFeature feature;

    public GenBankMultipleCDSNucleotideParser(final String genBankNucleotideEntry, final String featureName,
        final String proteinName) throws IOException, BioInfException {
        this.record = genBankNucleotideEntry;
        final BufferedReader br = new BufferedReader(new StringReader(genBankNucleotideEntry));
        // read the GenBank File
        try {
            final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());

            this.seq = seqI.nextRichSequence();
            final CdsList list = new CdsList(this.seq);
            assert seqI.hasNext() == false;
            if (list.isSingleCDS()) {
                this.feature = list.getCDS(0);
            } else if (featureName != null) {
                this.feature = list.getCDSByName(featureName);
            } else {
                this.feature = list.getCDSByProtein(proteinName);
            }
        } catch (final BioException ex) {
            if ("Could not read sequence".equals(ex.getMessage())) {
                Throwable cause = ex;
                while (null != cause.getCause()) {
                    cause = cause.getCause();
                }
                cause.printStackTrace();
                if (cause instanceof java.lang.StringIndexOutOfBoundsException) {
                    // unable to parse results - currently GenBank seems to return HTML not a GenBank file
                    throw new BioInfException("No nucleotide sequence available");
                }
                throw new IOException("Could not read nucleotide sequence: " + cause.getMessage());
            }
            // not in genbank format
            ex.printStackTrace();
            throw new IllegalArgumentException("Sorry, there has been a error while creating the target.", ex);
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getComments()
     */
    public String getComments() {
        return ServletTargetCreator.makeString(this.seq.getComments(), "\n");
    }

    /**
     * @see oorg.pimslims.bioinf.newtargetecordParser#getSequenceRelease()
     */
    public String getSequenceRelease() {
        return Integer.toString(this.seq.getVersion());
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getTaxId()
     */
    public int getTaxId() {
        return this.seq.getTaxon().getNCBITaxID();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getSource()
     */
    public String getSource() {
        return this.record;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getAccession()
     */
    public String getAccession() {
        return this.seq.getAccession();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getFuncDescription()
     */
    public String getFuncDescription() {
        return this.seq.getDescription();
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
     * 
     * @param seq
     * @return
     */
    public String getProteinSequence() {
        String translation = null;
        if (this.feature.getAnnotation().containsProperty("translation")) {
            translation = (String) this.feature.getAnnotation().getProperty("translation");
        }
        return translation;
    }

    /**
     * @see org.pimsorg.pimslims.bioinf.newtargetrser#getRecordFormat()
     */
    public String getDatabaseFormat() {
        return DATABASES.GenbankNucleotide.toString();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getDnaSequence()
     */
    public String getDnaSequence() {
        if (null == this.feature) {
            return "";
        }
        final Location location = this.feature.getLocation();
        assert null != location;
        return location.symbols(this.seq.getInternalSymbolList()).seqString().toUpperCase();
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getGeneName()
     */
    public String getGeneName() {
        String geneName = null;
        final Annotation annot = this.feature.getAnnotation();
        for (final Iterator iterator2 = annot.keys().iterator(); iterator2.hasNext();) {
            final SimpleComparableTerm st = (SimpleComparableTerm) iterator2.next();
            if (st.getName().equals("gene")) {
                geneName = (String) annot.getProperty(st);
            }
        }

        return geneName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getProteinName()
     */
    public String getProteinName() {
        final String protName = ParserUtility.getProteinName(this.feature.getAnnotation());
        return Util.isEmpty(protName) ? this.seq.getName() : protName;
    }

    /**
     * @see org.pimslims.bioinf.newtarget.RecordParser#getXaccessions() This may not be useful for this format
     *      !
     * @see org.pimslimsorg.pimslims.bioinf.newtarget#getXaccessions() This may not be useful for this format
     *      !
     */
    public BioDBReferences getXaccessions() {
        final Set<RankedCrossRef> refs = this.feature.getRankedCrossRefs();
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
     * @see org.pimslims.bioinf.newtarget.RecordParser#getBioEntry()
     */
    public CdsList getBioEntry() {
        return new CdsList(this.seq);
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
        return DATABASES.GenbankNucleotide.toString();
    }

}
