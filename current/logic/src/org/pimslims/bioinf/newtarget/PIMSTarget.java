/**
 * pims-web org.pimslims.command.newtarget PIMSTarget.java
 * 
 * @author Peter Troshin (aka pvt43)
 * @date 14 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
package org.pimslims.bioinf.newtarget;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.pimslims.bioinf.newtarget.BioDBReferences.BioDBReference;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Target;

/**
 * PIMSTarget
 * 
 */
public class PIMSTarget {

    /**
     * DNA_TARGET String
     */
    public static final String DNA_TARGET = "*DNA Target*";

    private final RecordParser parser;

    private String accessHook;

    //private String creatorHook;

    private String databaseName;

    public PIMSTarget(final RecordParser parser) {
        super();
        this.parser = parser;
    }

    /**
     * String type Setter for parser
     * 
     * @param parser public void setParser(String parser) { try { Object p =
     *            Class.forName(parser).newInstance(); if (p instanceof RecordParser) { this.parser =
     *            (RecordParser) p; } else { throw new RuntimeException( "Incorrect parser provided. Parser
     *            must implement RecordParser interface"); } } catch (ClassNotFoundException e) { throw new
     *            RuntimeException(e); } catch (InstantiationException e) { throw new RuntimeException(e); }
     *            catch (IllegalAccessException e) { throw new RuntimeException(e); } }
     */

    /*
     * @return hook of the lab note book
     */
    public String getAccess() {
        return this.accessHook;
    }

    /*
     *   hook of the lab note book
     */
    public void setAccess(final String accessHook) {
        if (Util.isHookValid(accessHook)) {
            this.accessHook = accessHook;
        } else {
            System.out.println("Hook " + accessHook + " is not valid - cannot set lab notebook!");
        }
    }

    /**
     * @return Returns the databaseRelease. This is treated as a key to main reference for the record. So it
     *         is never empty
     */
    public final String getSequenceRelease() {
        return this.parser.getSequenceRelease();
    }

    /**
     * @return Returns the targetName.
     */
    public final String getTargetName() {
        return this.parser.getTargetName();
    }

    /**
     * @return Returns the database.
     */
    public String getDatabaseFormat() {
        return this.parser.getDatabaseFormat();
    }

    /**
     * The source of main record
     * 
     * @return Returns the database.
     */
    public String getDatabaseName() {
        return this.databaseName;
    }

    /**
     * @return Returns the database.
     */
    public void setDatabaseName(final String database) {
        this.databaseName = database;
    }

    /**
     * @return Returns the accession.
     */
    public final String getAccession() {
        return this.parser.getAccession();
    }

    /**
     * @return Returns the naturalSource.
     */
    public final String getSpeciesName() {
        String orgName = this.parser.getSpeciesName();
        /* Make sure Organism does not end with ; */
        if (!Util.isEmpty(orgName)) {
            final int cIdx = orgName.lastIndexOf(";");
            if (cIdx > 0) {
                orgName = orgName.substring(0, cIdx).trim();
            }
        }
        return orgName;
    }

    /**
     * @return Returns the taxId.
     */
    public final String getTaxId() {
        return Integer.toString(this.parser.getTaxId());
    }

    /**
     * @return Returns the taxId.
     */
    public final int getIntTaxId() {
        return this.parser.getTaxId();
    }

    /**
     * @return Returns the dnaSeq.
     */
    public final String getDnaSequence() {
        String dseq = this.parser.getDnaSequence();
        if (null == dseq) {
            return ""; // requested in PIMS-2548
        }
        return dseq = dseq.toUpperCase(); // This is required for unification with ex-SPOT functionality
    }

    /**
     * @return Returns the proteinSeq.
     */
    public final String getProteinSequence() {
        String pseq = this.parser.getProteinSequence();
        if (!Util.isEmpty(pseq)) {
            pseq = pseq.toUpperCase(); // This is required for unification with ex-SPOT functionality
        }
        return pseq;
    }

    /**
     * @return Returns the geneName.
     */
    public final String getGeneName() {
        return this.parser.getGeneName();
    }

    /**
     * @return Returns the proteinName.
     */
    public final String getProteinName() {
        return this.parser.getProteinName();
    }

    /**
     * @return Returns the comments.
     */
    public final String getComments() {
        return this.parser.getComments();
    }

    /**
     * @return Returns the funcDescription.
     */
    public final String getFuncDescription() {
        return this.parser.getFuncDescription();
    }

    /**
     * @return Returns the file.
     */
    public final String getSource() {
        return this.parser.getSource();
    }

    public final String getLookedUpSource() {
        if (this.getOtherRecordParser() != null) {
            return this.getOtherRecordParser().getSource();
        }
        return null;
    }

    public final RecordParser getOtherRecordParser() {
        if (this.parser.getOtherRecordParser() != null) {
            return this.parser.getOtherRecordParser();
        }
        return null;
    }

    public String getLookedUpSourceDatabase() {
        return this.parser.getLookedUpSourceDatabase();
    }

    /* Holder for manually added database references */
    private BioDBReferences refs;

    /**
     * @see org.pimslims.bioinf.newtargetRecordParser#getXaccessions() There is no constructor in this class
     *      so need to make sure that parser.getXaccessions() is called
     */
    public BioDBReferences getXaccessions() {
        return this.refs != null ? this.refs : this.parser.getXaccessions();
    }

    public void addXaccessions(final String database, final String accession) {
        this.refs = this.parser.getXaccessions();
        if (this.refs == null) {
            this.refs = new BioDBReferences(new BioDBReference(database, accession));

        } else {
            this.refs.addReference(database, accession);
        }
    }

    public RecordParser.Type getSourceType() {
        return this.parser.getSourceType();
    }

    public String getLookedUpSourceURL() {
        return this.parser.getLookedUpSourceURL();
    }

    /**
     * Test is a DNA Target, should contain *DNA Target* and MolType for 'protein is DNA
     * 
     * @param t Target
     * @return
     */
    public static Boolean isDNATarget(final Target t) {
        if (null == t) {
            return false;
        }
        //TODO check MolType for 'protein'
        for (final Alias alias : t.getAliases()) {
            if (alias.getName().equals(PIMSTarget.DNA_TARGET) && t.getProtein().getMolType().equals("DNA")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test is a Natural Source Target, should contain *Natural Source Target* and MolType for 'protein is
     * SOURCE
     * 
     * @param t Target
     * @return
     */
    public static Boolean isNaturalSourceTarget(final Target t) {
        //TODO check MolType for 'protein'
        if ((!t.getAliases().contains("*Natural Source Target*"))) {
            return false;
        }
        return true;
    }

    @Deprecated
    //TODO speed this up, by searching Molecule instead
    public static Target isTargetDNAUnique(final String dnaSequence, final ReadableVersion rv) {
        for (final Target t : PIMSTarget.getAllTargets(rv)) {
            final Set<Molecule> dnas = t.getNucleicAcids();
            if (dnas.isEmpty()) {
                continue;
            }
            if (!PIMSTarget.isMolComponentSequenceUnique(dnas, dnaSequence)) {
                return t;
            }
        }
        return null;
    }

    @Deprecated
    // this could be thousands
    private static Collection<Target> getAllTargets(final ReadableVersion rv) {
        return rv.getAll(Target.class);
    }

    @Deprecated
    // TODO speed this up by searching MolComponents instead
    public static Target isTargetProteinUnique(final String protSequence, final ReadableVersion rv) {
        for (final Target t : PIMSTarget.getAllTargets(rv)) {
            final Molecule prot = t.getProtein();
            if (prot == null) {
                continue;
            }
            if (!PIMSTarget.isMolComponentSequenceUnique(Collections.singleton(prot), protSequence)) {
                return t;
            }
        }
        return null;
    }

    @Deprecated
    // slow
    static boolean isMolComponentSequenceUnique(final Set<Molecule> mols, final String sequence) {
        for (final Molecule mol : mols) {
            final String msequence = mol.getSequence();
            if (Util.isEmpty(msequence)) {
                continue;
            }
            if (msequence.equalsIgnoreCase(sequence)) {
                return false;
            }
        }
        return true;
    }
}
