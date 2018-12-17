/**
 * pims-web org.pimslims.command.newtarget PIMSTargetWriter.java
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
package org.pimslims.bioinf.targets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.bioinf.newtarget.BioDBReferences;
import org.pimslims.bioinf.newtarget.BioDBReferences.BioDBReference;
import org.pimslims.bioinf.newtarget.BioFormatGuesser;
import org.pimslims.bioinf.newtarget.PIMSTarget;
import org.pimslims.bioinf.newtarget.ParserUtility;
import org.pimslims.bioinf.newtarget.RecordParser;
import org.pimslims.bioinf.newtarget.RecordParser.DATABASES;
import org.pimslims.bioinf.newtarget.RecordParser.Type;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.Attachment;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * PIMSTargetWriter
 * 
 */
public class PIMSTargetWriter {

    final WritableVersion rw;

    final PIMSTarget ptarget;

    public PIMSTargetWriter(final WritableVersion version, final PIMSTarget pimsTarget) {
        this.rw = version;
        this.ptarget = pimsTarget;
    }

    /**
     * TODO Improve error reporting
     */
    public Target record() {
        Target target = null;
        try {

            LabNotebook access = null;
            final String accessHook = this.ptarget.getAccess();
            if (null != accessHook) {
                access = (LabNotebook) this.rw.get(accessHook);
                this.rw.setDefaultOwner(access);
            }

            final Organism naturalSource = this.recordNaturalSource();

            final Molecule protein =
                this.recordProtein(this.ptarget.getProteinName(), this.ptarget.getProteinSequence());

            final Molecule dna =
                this.recordDNA(this.ptarget.getProteinName() + "_seq", this.ptarget.getDnaSequence());

            // Target initialization
            if (protein == null) {
                throw new AssertionError("No protein for new target");
            }
            final String nextTargetName = MPSITargetName.getTargetCommonName(this.rw, this.rw.getUsername());

            target = this.recordTarget(nextTargetName, protein, this.ptarget.getComments());
            target.setNucleicAcids(Collections.singletonList(dna));
            target.setSpecies(naturalSource);
            target.setFunctionDescription(this.ptarget.getFuncDescription());
            target.setGeneName(this.ptarget.getGeneName());

            final BioDBReferences bioRfes = this.ptarget.getXaccessions();
            if (bioRfes != null) {
                for (final BioDBReference bioref : bioRfes.dbreferences) {
                    if (bioref.accession != null && bioref.accession.length() < 32) {
                        this.recordXDBRefs(bioref, target);
                    }
                }
            }

            final String databaseName = this.getDatabaseName(this.ptarget);
            final Database dbname = this.recordDatabase(databaseName);

            // DbRef

            this.recordDBRefs(dbname, this.ptarget.getAccession(), this.ptarget.getSequenceRelease(),
                this.ptarget.getSourceType().toString(), target);
            //TODO refactor!
            if (this.ptarget.getOtherRecordParser() != null) {
                final RecordParser otherParser = this.ptarget.getOtherRecordParser();
                final Database otherDbname = this.recordDatabase(this.ptarget.getLookedUpSourceDatabase());
                final String otherSourceType =
                    (this.ptarget.getSourceType() == RecordParser.Type.DNA ? RecordParser.Type.protein
                        .toString() : RecordParser.Type.DNA.toString());

                // Other source DbRef
                this.recordDBRefs(otherDbname, otherParser.getAccession(), otherParser.getSequenceRelease(),
                    otherSourceType, target);

            }

            org.pimslims.util.File file = null;
            String fileName = nextTargetName + "_" + this.ptarget.getSourceType() + "_record";
            file = this.rw.createFile(this.ptarget.getSource().getBytes(), fileName, target);
            file.setExtension(".txt");
            file.setDescription("The entry from " + PIMSTargetWriter.getDbName(databaseName) + " database");
            file.add(target);
            // Looked up source file
            if (this.ptarget.getLookedUpSource() != null) {
                fileName =
                    nextTargetName + "_" + ParserUtility.getOtherSourceType(this.ptarget.getSourceType())
                        + "_record";
                final org.pimslims.util.File file2 =
                    this.rw.createFile(this.ptarget.getLookedUpSource().getBytes(), fileName, target); //TODO make better name
                file2.setDescription("The entry from "
                    + PIMSTargetWriter.getDbName(this.ptarget.getLookedUpSourceDatabase()) + " database");
                file2.setExtension(".txt");
            }

        } catch (final ConstraintException e) {
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return target;
    }

    /**
     * This method decides what to record as the database for a record.
     * 
     * @param ptarget
     * @return
     */
    private String getDatabaseName(final PIMSTarget ptarget) {
        final String dbname = ptarget.getDatabaseName();
        if (Util.isEmpty(dbname)) {
            return ptarget.getDatabaseFormat(); // Record format rather then the database name
        }
        if (dbname.equalsIgnoreCase(DATABASES.GenBank.toString())) {
            return ptarget.getDatabaseFormat(); // The format more precisely defined during processing e.g. GenBank -> GenBankNucleotide
        }
        return dbname; // This is giving an exact database name of the record e.g. epo_prt, though it will not match reference data
    }

    /**
     * @param target
     * @throws AccessException
     * @throws ConstraintException
     */
    private ExternalDbLink recordDBRefs(final Database dbname, final String accession,
        final String dbrelease, final String dbdetails, final Target target) throws AccessException,
        ConstraintException {
        final Map<String, Object> prop = Util.getNewMap();
        prop.put(ExternalDbLink.PROP_DATABASE, dbname);
        prop.put(ExternalDbLink.PROP_ACCESSION_NUMBER, accession);
        prop.put(Attachment.PROP_PARENTENTRY, target);
        // The data supplied here by parsers is not actually a database release,
        // it is rather record version
        if (!Util.isEmpty(dbdetails)) {
            prop.put(ExternalDbLink.PROP_RELEASE, dbrelease);
            prop.put(LabBookEntry.PROP_DETAILS, dbdetails);
        }
        return Util.getOrCreate(this.rw, ExternalDbLink.class, prop);
    }

    /**
     * @param target
     * @throws AccessException
     * @throws ConstraintException This is going to record all cross database references which were associated
     *             with the record. The database names are not going to match PIMS reference data in some
     *             cases, rather they are a short name for the database.
     */
    private ExternalDbLink recordXDBRefs(final BioDBReference bioref, final Target target)
        throws AccessException, ConstraintException {
        return this.recordDBRefs(this.recordDatabase(bioref.databaseName), bioref.accession, null, null,
            target);
        // Dbrelease is always null here to show that this is not main datadase reference to a record!
    }

    /**
     * @throws AccessException
     * @throws ConstraintException This is likely to be in reference data
     */
    private Database recordDatabase(String dbname) throws AccessException, ConstraintException {
        final Map<String, Object> prop = Util.getNewMap();
        dbname = BioFormatGuesser.getDBReferenceName(dbname);
        prop.put(Database.PROP_NAME, dbname);
        return Util.getOrCreate(this.rw, Database.class, prop);
    }

    private Target recordTarget(final String name, final Molecule protein, String whyChosen)
        throws AccessException, ConstraintException {

        final Map<String, Object> prop = Util.getNewMap();
        prop.put(Target.PROP_NAME, name);
        prop.put(Target.PROP_PROTEIN, protein);
        if (Util.isEmpty(whyChosen)) {
            whyChosen = " ";
        }
        prop.put(Target.PROP_WHYCHOSEN, whyChosen);

        final Target target = this.rw.create(Target.class, prop);
        return target;
    }

    /*
     * TODO remove similar method from ServletTargetCreator
     */
    boolean isMolNameUnique(final String molName) throws SQLException, AccessException {
        final Map param = new HashMap();
        param.put(Substance.PROP_NAME, molName);
        return PIMSTargetWriter.isUnique(this.rw, param,
            this.rw.getModel().getMetaClass(Molecule.class.getName()));
    }

    private Molecule recordDNA(final String name, final String sequence) throws AccessException,
        ConstraintException, SQLException {
        return this.recordMolComponent(Type.DNA, name, sequence);
    }

    private Molecule recordProtein(final String name, final String sequence) throws AccessException,
        ConstraintException, SQLException {
        return this.recordMolComponent(Type.protein, name, sequence);
    }

    private Molecule recordMolComponent(final Type type, String name, final String sequence)
        throws AccessException, ConstraintException, SQLException {
        final Map<String, Object> prop = Util.getNewMap();
        if (!this.isMolNameUnique(name)) {
            name =
                PIMSTargetWriter.getUniqueMolComponentName(name, 0,
                    PIMSTargetWriter.getMolcomponentNames(this.rw));
        }
        prop.put(Substance.PROP_NAME, name);
        prop.put(Molecule.PROP_MOLTYPE, type.toString());
        prop.put(Molecule.PROP_SEQUENCE, sequence);
        return Util.getOrCreate(this.rw, Molecule.class, prop);
    }

    /**
     * Species means which genomic DNA this target were cloned from.
     * 
     * @param rw
     * @throws AccessException
     * @throws ConstraintException
     */
    private Organism recordNaturalSource() throws AccessException, ConstraintException {
        final Map<String, Object> prop = Util.getNewMap();
        // Look up species by taxid first
        Organism source = null;
        String ncbiId = null;
        if (this.ptarget.getIntTaxId() != -1) {
            ncbiId = this.ptarget.getTaxId();
            prop.put(Organism.PROP_NCBITAXONOMYID, ncbiId);
            source = this.rw.findFirst(Organism.class, prop);
            if (source != null) {
                return source; // found by tax id
            }
        }
        prop.clear(); // There may be NCBITaxID 
        prop.put(Organism.PROP_NAME, this.ptarget.getSpeciesName());
        source = this.rw.findFirst(Organism.class, prop);
        return source; // found by name
    }

    /**
     * Add number to the name which have been assigned already to make it unique. Be careful when call this
     * method twice since use the name gets not unique
     * 
     * @param protName
     * @param i - next number
     * @return protName^i
     */
    // there is now a method in DM: version.getUniqueName
    public static String getUniqueMolComponentName(String name, int i, final HashSet<String> names) {
        if (name == null) {
            return System.currentTimeMillis() + "_molecule";
        }
        if (names.contains(name)) {
            final int del = name.indexOf("^");
            name = del >= 0 ? name.substring(0, del) : name;
            name = name + "^" + i;
            // System.out.println("Exist !!!!!! " + name);
            return PIMSTargetWriter.getUniqueMolComponentName(name, ++i, names);
        }
        return name;
    }

    /**
     * Was the similar object recorded?
     * 
     * @param criteria
     * @param metaClassName
     * @return true if object(s) which meets criteria was found otherwise false
     * @throws SQLException
     */
    @Deprecated
    // now in data model
    public static boolean isUnique(final ReadableVersion rv, final Map criteria, final MetaClass metaClass) {

        //may not be accessable by current user
        final ReadableVersion version = rv.getModel().getReadableVersion(Access.ADMINISTRATOR);
        try {
            final Collection<ModelObject> results = version.findAll(metaClass.getJavaClass(), criteria);
            if (results != null && results.size() > 0) {
                return false;
            }
        } finally {
            version.abort();
        }
        return true;
    }

    public static HashSet getMolcomponentNames(final ReadableVersion rv) throws SQLException, AccessException {
        //may not be accessable by current user
        final ReadableVersion version = rv.getModel().getReadableVersion(Access.ADMINISTRATOR);
        try {
            final Collection<Molecule> mols = version.findAll(Molecule.class, Collections.EMPTY_MAP);
            final HashSet<String> molNames = new HashSet<String>();
            for (final Iterator iter = mols.iterator(); iter.hasNext();) {
                final ModelObject mObj = (ModelObject) iter.next();
                molNames.add((String) mObj.get_Value(Substance.PROP_NAME));
            }
            return molNames;
        } finally {
            version.abort();
        }
    }

    public static String getDbName(final String dbname) {
        String database = dbname;
        String subDB = null;
        if (dbname.equals(DATABASES.GenbankNucleotide.toString())) {
            database = DATABASES.GenBank.toString();
            subDB = "Nucleotide";
        }
        if (dbname.equals(DATABASES.GenbankProtein.toString())) {
            database = DATABASES.GenBank.toString();
            subDB = "Protein";
        }
        if (dbname.equals("GeneID")) {
            database = DATABASES.GenBank.toString();
            subDB = "Gene";
        }
        if (null == subDB) {
            return database;
        } else {
            return database + "/" + subDB;
        }
    }
}
