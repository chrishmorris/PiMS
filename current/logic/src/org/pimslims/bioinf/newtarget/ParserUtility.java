/**
 * current-pims-web org.pimslims.command.newtarget GenBankMultipleCDSParser.java
 * 
 * @author Petr Troshin aka pvt43
 * @date 3 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Petr Troshin
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.SimpleFeatureHolder;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.Location;
import org.biojavax.RichObjectFactory;
import org.biojavax.bio.seq.RichFeature;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.io.RichSequenceBuilderFactory;
import org.biojavax.ontology.SimpleComparableTerm;
import org.pimslims.bioinf.CdsList;
import org.pimslims.lab.Util;

/**
 * GenBankMultipleCDSParser
 * 
 */
public class ParserUtility {

    RichSequence seq = null;

    /**
     * @param genBankNucleotideEntry
     */
    public ParserUtility(final String genBankMultipleCDSEntry) {
        this.parse(genBankMultipleCDSEntry);
    }

    public PIMSTarget parse(final String genBankNucleotideEntry) {
        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(genBankNucleotideEntry));
        // read the GenBank File
        try {
            final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());

            if (seqI.hasNext()) {
                this.seq = seqI.nextRichSequence();
                assert seqI.hasNext() == false;
            } else {
                throw new RuntimeException("No sequence data available!");
            }
        } catch (final BioException ex) {
            // not in Genbank nucleotide format
            ex.printStackTrace();
            throw new IllegalArgumentException("Sorry, there has been a error while creating the target.", ex);
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
        return null;
    }

    public static boolean isGenBankDNA(final String genBankEntry) {
        try {
            return ParserUtility.isGenBank(genBankEntry, DNATools.getDNA().getTokenization("token"));
        } catch (final BioException e) {
            // Should never happens except is something wrong with biojava jar
            throw new RuntimeException(e);
        }
    }

    public static boolean isGenBankProtein(final String genBankEntry) {
        try {
            return ParserUtility.isGenBank(genBankEntry, ProteinTools.getAlphabet().getTokenization("token"));
        } catch (final BioException e) {
            // Should never happens except is something wrong with biojava jar
            throw new RuntimeException(e);
        }
    }

    public static boolean isUniprot(final String record) {
        if (!ParserUtility.startsWithId(record)) {
            return false;
        }
        final BufferedReader br = new BufferedReader(new StringReader(record));
        try {
            final SymbolTokenization rParser = ProteinTools.getAlphabet().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readUniProt(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            final RichSequence protSeq = seqI.nextRichSequence();
            if (Util.isEmpty(protSeq.getAccession())) {
                return false;
            }
            protSeq.getAnnotation();
        } catch (final BioException ex) {
            // not in UniProt format
            return false;
        }
        return true;
    }

    public static boolean isEMBL(final String record) {
        if (!ParserUtility.startsWithId(record)) {
            return false;
        }
        final BufferedReader br = new BufferedReader(new StringReader(record));
        try {
            final SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readEMBL(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());
            final RichSequence protSeq = seqI.nextRichSequence();
            if (Util.isEmpty(protSeq.getAccession())) {
                return false;
            }
            protSeq.getAnnotation();
        } catch (final BioException ex) {
            // not in EMBL format
            return false;
        }
        return true;
    }

    /**
     * @param record
     */
    private static boolean startsWithId(final String record) {
        if (record.trim().startsWith("ID")) {
            return true;
        }
        return false;
    }

    private static boolean isGenBank(final String genBankEntry, final SymbolTokenization rParser) {
        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(genBankEntry));
        // read the GenBank File
        try {
            final RichSequenceIterator seqI =
                RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                    RichObjectFactory.getDefaultNamespace());

            assert seqI.hasNext();
            final RichSequence seq = seqI.nextRichSequence(); // This will throw BioException if the record is not DNA
            if (!ParserUtility.hasCDS(new CdsList(seq))) {
                throw new BioException("No CDS in genbank file!");
            }
        } catch (final BioException ex) {
            // This is OK not in Genbank nucleotide format
            return false;
        } catch (final NoSuchElementException ex) {
            // request for more sequence when there isn't any
            ex.printStackTrace();
            throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
        }
        return true;
    }

    SimpleFeatureHolder getCDSList() {
        final SimpleFeatureHolder fh =
            (SimpleFeatureHolder) this.seq.filter(new FeatureFilter.ByType("CDS"), true);
        assert fh.countFeatures() > 1 : "There must be more that one CDS in this sequence!";
        return fh;
    }

    // This is fragile! Better to rely on feature properties, but this is
    // require implementation. For now feature number should be fine, as feature
    // storage backed up by ArrayList
    RichFeature getCDS(final int featureNumber) {
        final SimpleFeatureHolder fholder = this.getCDSList();
        int counter = 0;
        for (final Iterator iterator = fholder.features(); iterator.hasNext();) {
            final RichFeature feature = (RichFeature) iterator.next();
            if (counter == featureNumber) {
                return feature;
            }
            counter++;
        }
        return null;
    }

    //This method only works if the annotation value is a simple string
    //Values are case sensitive!
    RichFeature getCDSByAnnotation(final String akey, final String avalue) {
        final SimpleFeatureHolder fholder = this.getCDSList();
        for (final Iterator iterator = fholder.features(); iterator.hasNext();) {
            final RichFeature feature = (RichFeature) iterator.next();
            final Annotation annot = feature.getAnnotation();
            for (final Iterator iterator2 = annot.keys().iterator(); iterator2.hasNext();) {
                final SimpleComparableTerm st = (SimpleComparableTerm) iterator2.next();
                if (st.getName().equals(akey) && ((String) annot.getProperty(st)).equals(avalue)) {
                    return feature;
                }
            }

        }
        return null;
    }

    /**
     * 
     * @param featureNumber
     * @return String representing GenBank sub nucleotide entry for chosen CDS
     * 
     *         public String getCDSNucleotideRecord(final int featureNumber) { String nucEntry = null; // If
     *         cannot make URL to request the resource if (!this.canLinkbeMade(featureNumber)) { return null;
     *         }
     * 
     *         final RichFeature rf = this.getCDS(featureNumber); final RichLocation rloc =
     *         RichLocation.Tools.enrich(rf.getLocation()); // -1 complement, 1 - direct boolean reverseStrand
     *         = false; if (rloc.getStrand().intValue() == -1) { reverseStrand = true; } try { nucEntry =
     *         DBFetch.getNucleotideEntry(this.seq.getAccession(), rf.getLocation().getMin(), rf
     *         .getLocation().getMax(), reverseStrand);
     * 
     *         } catch (final IOException e) {
     *         System.out.println("cannot get nucleotide sequence for a feature" + rf); e.printStackTrace(); }
     *         // This can return null if no nucleotide sub entry was found return nucEntry; }
     */

    protected boolean canLinkbeMade(final int featureNumber) {
        final RichFeature rf = this.getCDS(featureNumber);
        final Location loc = rf.getLocation();
        if (!loc.isContiguous()) {
            return false;
        }
        return true;
    }

    /**
     * @return null If the location is not contiguous, string representation of location otherwise. If the
     *         location is not contiguous it is not possible to get presice CDS record from genbank
     * 
     *         protected String getCodedBy(int featureNumber) { RichFeature rf = getCDS(featureNumber);
     *         Location loc = rf.getLocation(); if (!loc.isContiguous()) return null; // location does not
     *         recognise strand (add complement) in its toString method // we need fix for this RichLocation
     *         rl = RichLocation.Tools.enrich(rf.getLocation()); String location = loc.toString(); // -1
     *         complement, 1 - direct if (rl.getStrand().intValue() == -1) { location = addStrand(location); }
     *         // LocationTools. return location; }
     * 
     *         private static String addStrand(String location) { return "complement(" + location + ")"; }
     */

    /**
     * Can get protein record if one of the following is true: 1) There is a "protein_id" 2) There is a GI
     * DbXreference
     * 
     * @return true if there is a gen bank identifier for a protein for a given feature
     */
    protected boolean canGetProtein(final int featureNumber) {
        final RichFeature rf = this.getCDS(featureNumber);
        final Annotation ann = rf.getAnnotation();
        if (ann.containsProperty("protein_id") && !Util.isEmpty((String) ann.getProperty("protein_id"))) {
            return true;
        }
        final BioDBReferences bdb = new BioDBReferences(rf.getRankedCrossRefs());
        if (!Util.isEmpty(bdb.getAccession("GI"))) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @return false if there is no "translation" annotation for given CDS, true otherwise
     */
    protected boolean containsTranslation(final int featureNumber) {
        final RichFeature rf = this.getCDS(featureNumber);
        final Annotation ann = rf.getAnnotation();
        if (ann.containsProperty("translation") && !Util.isEmpty((String) ann.getProperty("translation"))) {
            return true;
        }
        return false;
    }

    public static String getProperty(final Annotation annotation, final String propName) {
        if (annotation.containsProperty(propName) && !Util.isEmpty((String) annotation.getProperty(propName))) {
            return (String) annotation.getProperty(propName);
        }
        return null;
    }

    /**
     * 
     * @return complete genbank protein sequence of chosen CDS
     * 
     * 
     *         public String getCDSProteinRecord(final int featureNumber) {
     * 
     *         if (!this.canGetProtein(featureNumber)) { return null; }
     * 
     *         final RichFeature rf = this.getCDS(featureNumber); String gi = null; final BioDBReferences bdb
     *         = new BioDBReferences(rf.getRankedCrossRefs()); if (!Util.isEmpty(bdb.getAccession("GI"))) { gi
     *         = bdb.getAccession("GI"); } else { final Annotation ann = rf.getAnnotation(); gi =
     *         ParserUtility.getProperty(ann, "protein_id"); } String protein = null; try { protein =
     *         DBFetch.getGenbankProteinEntry(gi); } catch (final IOException e) {
     *         System.out.println("Cannot download GenBank protein record with ID " + gi);
     *         e.printStackTrace(); }
     * 
     *         return protein; }
     */

    @Deprecated
    // just use the method of CdsList
    public static RichFeature getCDS(final CdsList seq, final int featureNumber) {
        return seq.getCDS(featureNumber);
    }

    @Deprecated
    // just use the method of CdsList
    public static boolean hasCDS(final CdsList list) {
        return list.hasCDS();
    }

    /**
     * 
     * @return true if there is only one CDS feature in the sequence annotation
     */
    @Deprecated
    // just use the method of CdsList
    public static boolean isSingleCDS(final CdsList list) {
        return list.isSingleCDS();
    }

    @Deprecated
    // just use the method of CdsList
    public static RichFeature getCDSByName(final String cdsName, final CdsList rseq) {
        return rseq.getCDSByName(cdsName);
    }

    @Deprecated
    // just use the method of CdsList
    public static RichFeature getCDSByLocation(final CdsList list, final int start, final int end) {
        return list.getCDSByLocation(start, end);
    }

    public static String getProteinName(final Annotation annotation) {
        String protName = ParserUtility.getProperty(annotation, "gene");
        // Make first letter capital
        if (!Util.isEmpty(protName) && protName.length() > 2) {
            protName = protName.substring(0, 1).toUpperCase() + protName.substring(1);
            // The product name tends to be longer
        } else {
            protName = ParserUtility.getProperty(annotation, "product");
        }
        return protName;
    }

    public static String getOtherSourceType(final RecordParser.Type sourceType) {
        if (sourceType == RecordParser.Type.complete) {
            return "";
        }
        return sourceType == RecordParser.Type.DNA ? RecordParser.Type.protein.toString()
            : RecordParser.Type.DNA.toString();
    }
}
