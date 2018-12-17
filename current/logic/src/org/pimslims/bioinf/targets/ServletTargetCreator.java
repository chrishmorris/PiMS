package org.pimslims.bioinf.targets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.FeatureHolder;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojavax.RichObjectFactory;
import org.biojavax.SimpleComment;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin
 */
@Deprecated
public class ServletTargetCreator extends AbstractTargetImporter {

    /**
     * Class designed to upload the SwissProt entries from files to the PIMS Targets database. Class reads
     * appropriate fields from SwissProt file and puts is to the PIMS database.
     * 
     * @author Petr Troshin <br>
     *         Created on 28.09.2005
     */
    /**
     * @warning this object is not reusable: After putToDb() is called, the object can not be used!
     */
    final WritableVersion rw;

    final String databaseName;

    /**
     * Class perform all task in time of construction the SwissProtToEBITarget object. TODO test this
     * periodically, EBI keeps changing the services
     * 
     * @param swissProtfile
     */
    public ServletTargetCreator(final String protEntry, final String database, final WritableVersion rw,
        final Person user) throws AccessException {

        super(null);

        this.rw = rw;
        this.person = user;
        int databaseId = -1;
        this.databaseName = database;
        if (database.equalsIgnoreCase("swissprot") || database.endsWith("_prt")
            || database.equalsIgnoreCase("uniprot")) {
            databaseId = 0;
        }
        if (database.startsWith("embl")) { // should this be InputParams.EMBL
            databaseId = 1;
        }
        if (database.startsWith("genbank")) {
            databaseId = 2;
        }

        switch (databaseId) {
            case 0:
                this.parseSwissProt(protEntry);
                break;
            case 1:
                this.parseEMBL(protEntry);
                break;
            case 2:
                this.genBankToPIMS(protEntry);
                break;
            default:
                throw new AssertionError("Undefined database");
        }

    }

    /**
     * This method puts a limitation on the data from which the target could be created In particular it
     * mandates the size of the file to be less that 1 Mb and more than 100 letters The file needs to start
     * from ID and contain at least 3 annotation features.
     * 
     * @param record String which represents a target in either EMBL or Swissprot format
     * @return false if the record does not satisfy the conditions stated
     */
    public static boolean isFormatSupported(final String record) {
        if (record == null) {
            return false;
        }
        // record.length() should between 100 and 1024000
        if (record.length() < 100 || record.length() > 1024000) {
            return false;
        }

        if (!(record.trim().startsWith("ID"))) {
            return false;
        }
        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(record));
        // read the GenBank File
        SequenceIterator sequences = SeqIOTools.readSwissprot(br);
        if (!sequences.hasNext()) {
            sequences = SeqIOTools.readEmbl(br);
        }
        if (sequences.hasNext()) {
            Sequence seq = null;
            try {
                seq = sequences.nextSequence();
            } catch (final NoSuchElementException e) {
                // This is OK, will not do anything -> sequence supplied is in
                // the wrong format
                return false;
            } catch (final BioException e) {
                return false;
            }
            final Annotation seqAn = seq.getAnnotation();
            if (seqAn.keys().size() < 4) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method performs all the work Load SwissProt file Gets appropriate fields from the annotation Clean this
     * fields from redundant and unsuitable information
     * 
     * @see UMTargetsRetrieval
     * @see UMTargetsRetrieval#getWorkPackage(String)
     */
    private void parseSwissProt(final String swissProtEntry) {

        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(swissProtEntry));

        // read the GenBank File
        final SequenceIterator sequences = SeqIOTools.readSwissprot(br);
        // iterate through the sequences
        if (sequences.hasNext()) {
            try {

                final Sequence seq = sequences.nextSequence();

                final Annotation seqAn = seq.getAnnotation();
                /*
                 * Print all the keys //System.out.println("ALL " + seqAn); for (Iterator i =
                 * seqAn.keys().iterator(); i.hasNext();) { Object key = i.next(); System.out.println("key " +
                 * key); System.out.println("value " + seqAn.getProperty(key)); }
                 */

                this.dbNamePrms.put(Database.PROP_NAME, this.databaseName);

                if (seqAn.containsProperty("swissprot.accessions")) {
                    this.dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER,
                        ((ArrayList) seqAn.getProperty("swissprot.accessions")).get(0));
                } else if (seqAn.containsProperty("AC")) {
                    this.dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER,
                        SwissProtToPIMS.getAccession((String) seqAn.getProperty("AC")));
                }

                if (seqAn.containsProperty("OX")) {
                    this.naturalSrcPrms.put(Organism.PROP_NCBITAXONOMYID,
                        SwissProtToPIMS.getTaxId((String) seqAn.getProperty("OX")));
                } else {
                    this.naturalSrcPrms.put(Organism.PROP_NCBITAXONOMYID, "unknown");
                }
                String os = null;
                if (seqAn.containsProperty("OS")) {
                    os = ServletTargetCreator.makeString(seqAn.getProperty("OS"), " ");
                    this.naturalSrcPrms.put(Organism.PROP_NAME, os);
                }

                // TODO Change this!
                this.naturalSrcPrms.put(Organism.PROP_SPECIES, os.length() > 80 ? os.substring(0, 79) : os);

                this.molComponentPrms.put(Molecule.PROP_MOLTYPE, "protein");
                this.molComponentPrms.put(Molecule.PROP_SEQUENCE, seq.seqString());

                // Not mandatory in swissprot need an alternative
                String gn = null;
                if (seqAn.containsProperty("GN")) {
                    gn = ServletTargetCreator.makeString(seqAn.getProperty("GN"), " ");
                }
                String id = null;
                if (seqAn.containsProperty("ID")) {
                    id = SwissProtToPIMS.getSwissProtID((String) seqAn.getProperty("ID"));
                }
                String protName = null;
                if (gn != null) {
                    protName = SwissProtToPIMS.getProtName(gn);
                } else {
                    protName = SwissProtToPIMS.getProtName(id);
                }
                this.molComponentPrms.put(Substance.PROP_NAME, protName);

                if (seqAn.containsProperty("CC")) {
                    this.molComponentPrms.put(LabBookEntry.PROP_DETAILS,
                        ServletTargetCreator.makeString(seqAn.getProperty("CC"), " "));
                }
                if (seqAn.containsProperty("KW")) {
                    this.molComponentPrms.put(Substance.PROP_KEYWORDS,
                        Util.makeCollection(seqAn.getProperty("KW")));
                }

                String de = null;
                if (seqAn.containsProperty("DE")) {
                    de = ServletTargetCreator.makeString(seqAn.getProperty("DE"), " ");
                }
                this.targetPrms.put(Target.PROP_FUNCTIONDESCRIPTION, de);
                this.targetPrms
                    .put(
                        Target.PROP_WHYCHOSEN,
                        "Because this target was created automatically, it was impossible to complete this field. Please update it as appropriate.");
                // was targetPrms.put(TargetAttr.proteinName, protName); //
                // duplicate call to GN !

                // Annotation
                this.annotationPrms.put("entry", swissProtEntry);

            } catch (final BioException ex) {
                // not in SwissProt format
                ex.printStackTrace();
                throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
            } catch (final NoSuchElementException ex) {
                // request for more sequence when there isn't any
                ex.printStackTrace();
                throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
            }
        }
    }

    /**
     * Method performs all the work Load SwissProt file Gets appropriate fields from the annotation Clean this
     * fields from redundant and unsuitable information
     * 
     * @see UMTargetsRetrieval
     * @see UMTargetsRetrieval#getWorkPackage(String)
     */
    private void parseEMBL(final String protEntry) {

        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(protEntry));

        // read the GenBank File
        final SequenceIterator sequences = SeqIOTools.readEmbl(br);

        // iterate through the sequences
        if (sequences.hasNext()) {
            try {

                final Sequence seq = sequences.nextSequence();

                final Annotation seqAn = seq.getAnnotation();

                final FeatureHolder cds = seq.filter(new FeatureFilter.ByType("CDS"), true);
                String translation = null;
                for (final Iterator iter = cds.features(); iter.hasNext();) {
                    final Feature f = (Feature) iter.next();
                    if (f.getAnnotation().containsProperty("translation")) {
                        translation = (String) f.getAnnotation().getProperty("translation");
                    }
                }

                // Print all the keys
                // System.out.println("ALL " + seqAn);
                /*
                 * for (Iterator i = seqAn.keys().iterator(); i.hasNext();) { Object key = i.next();
                 * System.out.println("key " + key); System.out.println("value " + seqAn.getProperty(key)); }
                 */

                this.annotationPrms.put("entry", this.prepareParameters(protEntry, seq, seqAn, translation));

            } catch (final BioException ex) {
                // not in SwissProt format
                // ex.printStackTrace();
                throw new IllegalArgumentException(
                    "Sorry, there has been a error while creating the target.", ex);
            } catch (final NoSuchElementException ex) {
                // request for more sequence when there isn't any
                ex.printStackTrace();
                throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
            }
        }
    }

    /**
     * @param protEntry
     * @param seq
     * @param seqAn
     * @param translation
     * @return
     */
    private String prepareParameters(final String protEntry, final Sequence seq, final Annotation seqAn,
        final String translation) {
        final String entryName = SwissProtToPIMS.getSwissProtID((String) seqAn.getProperty("ID"));

        String id = null;
        if (seqAn.containsProperty("ID")) {
            id = SwissProtToPIMS.getSwissProtID((String) seqAn.getProperty("ID"));
        }

        this.dbNamePrms.put(Database.PROP_NAME, this.databaseName);
        if (seqAn.containsProperty("embl_accessions")) {
            this.dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER,
                ((ArrayList) seqAn.getProperty("embl_accessions")).get(0));
        } else if (seqAn.containsProperty("AC")) {
            this.dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER, seqAn.getProperty("AC"));
        } else if (seqAn.containsProperty("ID")) {
            this.dbRefPrms.put(ExternalDbLink.PROP_ACCESSION_NUMBER, id);
        }

        if (seqAn.containsProperty("SV")) {
            this.dbRefPrms.put(ExternalDbLink.PROP_RELEASE, seqAn.getProperty("SV"));
        }

        // TODO TaxID
        String os = null;
        if (seqAn.containsProperty("OS")) {
            os = ServletTargetCreator.makeString(seqAn.getProperty("OS"), " ");
            this.naturalSrcPrms.put(Organism.PROP_NAME, os);
        }
        if (seqAn.containsProperty("OX")) {
            this.naturalSrcPrms.put(Organism.PROP_NCBITAXONOMYID, seqAn.getProperty("OX"));
        }
        this.naturalSrcPrms.put(Organism.PROP_SPECIES, os.length() > 80 ? os.substring(0, 79) : os);

        this.molComponentPrms.put(Molecule.PROP_MOLTYPE, "protein");
        assert translation != null : "Cannot create molecule. The sequence is empty.";
        this.molComponentPrms.put(Molecule.PROP_SEQUENCE, translation);

        final String protName = SwissProtToPIMS.getProtName(entryName);
        this.molComponentPrms.put(Substance.PROP_NAME, protName);

        this.nucleotidesPrms.put(Molecule.PROP_MOLTYPE, "DNA");
        this.nucleotidesPrms.put(Molecule.PROP_SEQUENCE, seq.seqString().toUpperCase());
        this.nucleotidesPrms.put(Substance.PROP_NAME, protName + "_seq");

        if (seqAn.containsProperty("CC")) {
            this.molComponentPrms.put(LabBookEntry.PROP_DETAILS,
                ServletTargetCreator.makeString(seqAn.getProperty("CC"), " "));
        }
        if (seqAn.containsProperty("KW")) {
            this.molComponentPrms.put(Substance.PROP_KEYWORDS, Util.makeCollection(seqAn.getProperty("KW")));
        }

        if (seqAn.containsProperty("DE")) {
            this.targetPrms.put(Target.PROP_FUNCTIONDESCRIPTION,
                ServletTargetCreator.makeString(seqAn.getProperty("DE"), " "));
        }
        this.targetPrms
            .put(
                Target.PROP_WHYCHOSEN,
                "Because this target was created automatically, it was impossible to complete this field. Please update it as appropriate.");
        // was targetPrms.put(TargetAttr.proteinName,
        // SwissProtToPIMS.getProtName(entryName)); // duplicate call to
        // GN !

        // Annotation
        return protEntry;
    }

    private void genBankToPIMS(final String genBankEntry) {

        // create a buffered reader to read the sequence file
        final BufferedReader br = new BufferedReader(new StringReader(genBankEntry));
        // This loads nucleotide sequences only!
        // GenbankRichSequenceDB gbr = new GenbankRichSequenceDB();

        //    RichSequenceBuilderFactory seqFactory = this.getFactory(); // - Alternative factory

        // read the GenBank File
        /*
        SymbolTokenization rParser = DNATools.getDNA().getTokenization("token");
           RichSequenceIterator seqI =
               RichSequence.IOTools.readGenbank(br, rParser, RichSequenceBuilderFactory.FACTORY,
                   RichObjectFactory.getDefaultNamespace());
        */
        // Better
        final RichSequenceIterator seqI =
            RichSequence.IOTools.readGenbankProtein(br, RichObjectFactory.getDefaultNamespace());
        //  RichSequence.IOTools.readGenbankDNA(br, RichObjectFactory.getDefaultNamespace());
        //SequenceIterator sequences = SeqIOTools.readGenbank(br);

        // iterate through the sequences
        //final PIMSTarget ptarget = new PIMSTarget();

        if (seqI.hasNext()) {
            try {
                final RichSequence seq = seqI.nextRichSequence();
                final Annotation seqAn = seq.getAnnotation();
                final FeatureHolder cds = seq.filter(new FeatureFilter.ByType("CDS"), true);
                String translation = null;
                for (final Iterator<Feature> iter = cds.features(); iter.hasNext();) {
                    final Feature f = iter.next();
                    if (f.getAnnotation().containsProperty("translation")) {
                        translation = (String) f.getAnnotation().getProperty("translation");
                    }
                }

                // Print all the keys
                // System.out.println("ALL " + seqAn);
                /*
                 * for (Iterator i = seqAn.keys().iterator(); i.hasNext();) { Object key = i.next();
                 * System.out.println("key " + key); System.out.println("value " + seqAn.getProperty(key)); }
                 */

                this.annotationPrms.put("entry",
                    this.prepareParameters(genBankEntry, seq, seqAn, translation));

            } catch (final BioException ex) {
                // not in SwissProt format
                // ex.printStackTrace();
                throw new IllegalArgumentException(
                    "Sorry, there has been a error while creating the target.", ex);
            } catch (final NoSuchElementException ex) {
                // request for more sequence when there isn't any
                ex.printStackTrace();
                throw new RuntimeException("Sorry, there has been a error while creating the target.", ex);
            }
        }
    }

    @Deprecated
    // not in use
    private boolean isMolNameUnique(final String molName) throws SQLException, AccessException {
        final HashMap param = new HashMap();
        param.put(Substance.PROP_NAME, molName);
        return PIMSTargetWriter.isUnique(this.rw, param, this.rw.getModel().getMetaClass(Molecule.class.getName()));
    }

    /**
     * Record the information to the database Split an information in to several piece, create the number of
     * modelObject to hold the information, create the associations between different model object Note the
     * information were put in the Map somethere outside this method!
     */
    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.command.targets.AbstractTargetImporter#putToDb()
     */
    @Override
    @Deprecated
    protected// not in use
    String putToDb() {
        try {

            this.naturalSource = Util.getOrCreate(this.rw, Organism.class, this.naturalSrcPrms);

            // Protein
            if (this.naturalSource != null) {
                this.molComponentPrms.put(Substance.PROP_NATURALSOURCE, this.naturalSource);
            }

            final String molName = (String) this.molComponentPrms.get(Substance.PROP_NAME);
            if (!this.isMolNameUnique(molName)) {
                this.molComponentPrms.put(Substance.PROP_NAME,
                    PIMSTargetWriter.getUniqueMolComponentName(molName, 0, PIMSTargetWriter.getMolcomponentNames(this.rw)));
            }

            this.molComponent = Util.getOrCreate(this.rw, Molecule.class, this.molComponentPrms);

            if (this.nucleotidesPrms.size() > 0) {
                final String nucMolName = (String) this.nucleotidesPrms.get(Substance.PROP_NAME);
                if (!this.isMolNameUnique(nucMolName)) {
                    this.nucleotidesPrms.put(Substance.PROP_NAME,
                        PIMSTargetWriter.getUniqueMolComponentName(nucMolName, 0, PIMSTargetWriter.getMolcomponentNames(this.rw)));

                }
                // Nucleotide seq of the protein
                this.nucleotides =
                    Collections.singleton(Util.getOrCreate(this.rw, Molecule.class, this.nucleotidesPrms));
            }

            // Target initialization
            if (this.molComponent == null) {
                throw new AssertionError("Mandatory relation molComponent missed. Stop.");
            }
            this.targetPrms.put(Target.PROP_PROTEIN, this.molComponent);
            if (this.nucleotides != null) {
                this.targetPrms.put(Target.PROP_NUCLEICACIDS, this.nucleotides);
            }
            if (this.annotation != null) {
                // TODO CHECK MODEL CHANGE: Annotation must have a parent
                this.targetPrms.put(LabBookEntry.PROP_ATTACHMENTS, this.annotation);
            }
            if (this.naturalSource != null) {
                this.targetPrms.put(Target.PROP_SPECIES, this.naturalSource);
            }
            if (this.person != null) {
                this.targetPrms.put(LabBookEntry.PROP_CREATOR, this.person);
            }

            // DbName
            this.dbName =
                Util.getOrCreate(this.rw, org.pimslims.model.reference.Database.class, this.dbNamePrms);
            if (this.dbName != null) {
                this.dbRefPrms.put(ExternalDbLink.PROP_DATABASE, this.dbName);
            }

            // DbRef
            this.dbRef =
                Util.getOrCreate(this.rw, org.pimslims.model.core.ExternalDbLink.class, this.dbRefPrms);
            if (this.dbRef != null) {
                final ArrayList dbrefsa = new ArrayList();
                dbrefsa.add(this.dbRef);
                // TODO CHECK MODEL CHANGE: attachment like ExternalDbLink must have parent to be created
                this.targetPrms.put(LabBookEntry.PROP_ATTACHMENTS, dbrefsa);
            }

            MPSITargetName.getInstance();
            this.targetPrms.put(Target.PROP_NAME,
                MPSITargetName.getTargetCommonName(this.rw, this.rw.getUsername()));
            this.target = this.rw.create(org.pimslims.model.target.Target.class, this.targetPrms);

            // Status
            if (this.target == null) {
                throw new AssertionError("Mandatory relation target missed. Stop.");
            }

            final String entry = (String) this.annotationPrms.get("entry");
            final org.pimslims.util.File file =
                this.rw.createFile(entry.getBytes(), (String) this.targetPrms.get(Target.PROP_NAME),
                    (LabBookEntry) this.target);

            if (this.databaseName != null && this.databaseName.trim().length() != 0) {
                file.setDescription("The entry from " + this.databaseName.toUpperCase() + " database");
            }
            file.setExtension(".txt");

        } catch (final ConstraintException e) { // ?? dup
            throw new RuntimeException(e);
        } catch (final AccessException e) {
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final String hook = this.target.get_Hook();
        // Clean all values
        this.cleanAllValues();
        return hook;
    }

    /**
     * Method receives ArrayList or String and return only strings by concatenating different values from
     * ArrayList to one String
     * 
     * @param untype ArrayList or String
     * @return String
     */
    public static String makeString(final Object untype, String delim) {
        String str = "";
        if (untype == null) {
            return "";
        }
        if (delim == null) {
            delim = "";
        }
        if (untype instanceof Collection) {
            for (final Iterator i = ((Collection) untype).iterator(); i.hasNext();) {
                final Object elem = i.next();
                if (elem instanceof SimpleComment) {
                    str += ((SimpleComment) elem).getComment() + delim;
                } else {
                    str += (String) elem + delim;
                }
            }
            return (str.length() > 0 ? str.substring(0, str.length() - delim.length()) : "");
        }
        return (String) untype;
    }

    /**
     * Method iterate other all files in the directory and construct SwissProtToEBITartes entities. After put
     * them to the database. For database connection details and etc..
     * 
     * @throws IOException
     * @see EBITarget args[] - is not used
     */

}
