package org.pimslims.bioinf.targets;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.pimslims.bioinf.targets.EBITarget.EBITargetStatus;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.util.File;

// TODO Do not forgot to create a Location!
/**
 * @author Petr Troshin aka pvt43
 */
@Deprecated
// no longer in use
public class TargetImporter extends AbstractTargetImporter implements PIMSClassesDescription {

    // could define the creator of the Target
    // could to remove initials from familyName of Person

    // HashMap to keep track of Molecular names which must be unique.
    static HashSet<String> molNames = null;

    static HashMap<String, String> molNamesIds = null;
    static {
        TargetImporter.molNamesIds = new HashMap<String, String>();
        TargetImporter.molNames = new HashSet<String>();
        TargetImporter.molNamesIds.put("nextLeedsID", "500");
    }

    class FileInfoHolder {
        protected String path;

        protected String name;

        public String extension;

        // private String content;

        private ModelObject modelobject;

        private String assotiateName;

        public String getName() {
            return this.name;
        }

        public String getPath() {
            return this.path;
        }

        String cleanName(String name) {
            if (name.startsWith(".")) {
                name = name.substring(1);
            } else if (name.endsWith(".")) {
                name = name.substring(0, name.length() - 1);
            }
            return name;
        }

        public ModelObject getAssotiatedObject() {
            if (this.modelobject != null) {
                return this.modelobject;
            }
            if (this.assotiateName.indexOf("Molecule") >= 0) {
                return TargetImporter.this.protein;
            }
            if (this.assotiateName.indexOf("Target") >= 0) {
                return TargetImporter.this.target;
            }
            if (this.assotiateName.indexOf("Project") >= 0) {
                return TargetImporter.this.project;
            }
            if (this.assotiateName.indexOf("Citation") >= 0) {
                if (TargetImporter.this.citations.size() > 0) {
                    return TargetImporter.this.citations.get(0);
                }
                throw new AssertionError("You are trying to associate file with non existed citation! ");
            }
            throw new AssertionError("Please define appropriate associated object " + " or add a new type ");
        }

    } // FileInfoHolder class end

    /**
     * Class to represent the statuses of work on the target corresponds to Target.Status and
     * Target.TargetStatus info
     * 
     * @author pvt43
     */
    static class Statuses {

        Status[] statuses;

        public void addStatus(final String statusCode, final Timestamp date) {
            ArrayList st = null;
            final Status newstatus = new Status(statusCode, date);
            if (this.isUnique(newstatus)) {
                if (this.statuses != null) {
                    st = new ArrayList(Arrays.asList(this.statuses));
                } else {
                    st = new ArrayList();
                }
                st.add(newstatus);
            }
            this.statuses = (Status[]) st.toArray(new Status[st.size()]);
        }

        public static HashMap getTargetStatusPrms(final Status status) {
            final HashMap tsprm = new HashMap();
            tsprm.put(TargetStatusAttr.name, status.statusCode);
            return tsprm;
        }

        public static HashMap getStatusPrms(final Status status) {
            final HashMap sprm = new HashMap();
            sprm.put(StatusAttr.date, status.date);
            return sprm;
        }

        boolean isUnique(final Status status) {
            if (this.statuses != null) {
                for (int i = 0; i < this.statuses.length; i++) {
                    if (this.statuses[i].equalsStatus(status)) {
                        return false;
                    }
                }
            }
            return true;
        }

        static class Status {
            String statusCode;

            Timestamp date;

            public Status(final String statusCode, final Timestamp date) {
                this.statusCode = statusCode;
                this.date = date;
            }

            public boolean equalsStatus(final Status status) {
                if (this.statusCode.equalsIgnoreCase(status.statusCode) && this.date.equals(status.date)) {
                    return true;
                }
                return false;
            }

        } // end Status class

    } // end Statuses class

    /**
     * Default Date
     */
    public static Timestamp defaultStatusDate = null;
    static {
        try {
            TargetImporter.defaultStatusDate =
                new Timestamp(((new SimpleDateFormat("mm-dd-yyyy")).parse("01-01-2005")).getTime());
        } catch (final ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * The holder for pubmed, medline and DOI codes of created Citations
     * 
     * static HashMap citationIds = new HashMap();
     */
    // One to many objects e.g. Citations(many)-to-Target(one)
    // Holder for FileInfoHolder instances
    ArrayList filePrms = null;

    protected final AbstractModel model;

    WritableVersion rw = null;

    // Objects parameters

    // Instance of Statuses class
    Statuses statuses = null;

    private final int counter;

    /**
     * Get changed old MPSI targets create Map TargetID->EBITarget instance
     */
    public static Map eBITargets = new HashMap();

    /*
     * Needed forimporter static { Connection conn = EBITarget.getMPSIDBConnection(); ArrayList chTargets =
     * EBITarget.getChangedTargets(conn); for (Iterator iter = chTargets.iterator(); iter.hasNext();) {
     * EBITarget target = (EBITarget) iter.next(); eBITargets.put(target.targetID, target); } try {
     * conn.rollback(); conn.close(); } catch (SQLException e) { e.printStackTrace(); } eBITargets =
     * Collections.unmodifiableMap(eBITargets); }
     */

    public TargetImporter(final String dataOwner) {
        super(dataOwner);
        this.model = org.pimslims.dao.ModelImpl.getModel();
        this.counter = 0;
        this.filePrms = new ArrayList();
        this.statuses = new Statuses();
    }

    @Override
    public void cleanAllValues() {
        super.cleanAllValues(); // Clear one-to-many object holders
        this.filePrms.clear();
        this.statuses = null;
    }

    /**
     * Method to record the information on the families parsed earlier to the PIMS
     * 
     * @param families
     * @param familiesDesc
     * 
     *            void recordULFamilies(final TreeMap families, final TreeMap familiesDesc) { try { final
     *            TreeMap targetGroups = new TreeMap(); this.rw =
     *            this.model.getWritableVersion(AbstractModel.SUPERUSER); //
     * 
     *            // Create root TCDB groups final HashMap rootGroups = new HashMap(); rootGroups.put("2.A.",
     *            "Porters (uniporters, symporters, antiporters)"); rootGroups.put("1.A.", "α-Type channels");
     *            rootGroups.put("9.B.", "Putative uncharacterized transport proteins");
     * 
     *            for (final Iterator iter = rootGroups.keySet().iterator(); iter.hasNext();) { final String
     *            key = (String) iter.next(); this.targetGroupPrms.put(TargetGroupAttr.shortName, key);
     *            this.targetGroupPrms.put(TargetGroupAttr.completeName, rootGroups.get(key));
     *            this.targetGroupPrms.put(TargetGroupAttr.groupingType, "TCDB category");
     * 
     *            final ModelObject tg = Util.getOrCreate(this.rw, TargetGroup.class.getName(),
     *            this.dataOwner, this.targetGroupPrms); targetGroups.put(key, tg);
     *            this.targetGroupPrms.clear(); }
     * 
     *            for (final Iterator iter = families.entrySet().iterator(); iter.hasNext();) { final
     *            Map.Entry element = (Map.Entry) iter.next(); String key = (String) element.getKey();
     * 
     *            this.targetGroupPrms.put(TargetGroupAttr.shortName, key);
     *            this.targetGroupPrms.put(TargetGroupAttr.completeName, element.getValue());
     *            this.targetGroupPrms.put(TargetGroupAttr.groupingType, "TCDB category");
     *            this.targetGroupPrms.put(TargetGroupAttr.details, familiesDesc.get(key)); this.targetGroup =
     *            Util.getOrCreate(this.rw, TargetGroup.class.getName(), this.dataOwner,
     *            this.targetGroupPrms);
     * 
     *            targetGroups.put(key, this.targetGroup);
     * 
     *            // Create files associated with TargetGroup // 1.A.1_alignment_percent_identity.htm //
     *            1.A.1_alignment_residue_type.htm // 1.A.1_phylogeny.mht
     * 
     *            key = key.substring(0, key.length() - 1) + "_"; final String[] fileNames = {
     *            "alignment_percent_identity.htm", "alignment_residue_type.htm", "phylogeny.mht" }; final
     *            String[] fileLabels = { "Alignment illustrating percent identities",
     *            "Alignment showing residue types", "Phylogenetic Tree" }; for (int i = 0; i <
     *            fileNames.length; i++) { if (key.startsWith("2.A.1") && !key.startsWith("2.A.1.") ||
     *            key.startsWith("2.A.1.10") || key.startsWith("2.A.3") && !key.startsWith("2.A.3.") ||
     *            key.startsWith("2.A.66") && !key.startsWith("2.A.66.") || key.startsWith("2.A.7") &&
     *            !key.startsWith("2.A.7.") || key.startsWith("2.A.7.3")) { continue; } final String filename
     *            = key + fileNames[i]; final FileInfoHolder f = new FileInfoHolder(fileLabels[i],
     *            filename.indexOf("phylogeny") >= 0 ? ".mht" : ".htm", LeedsHTMLTargetsParser.sourceDirectory
     *            + filename, this.targetGroup); this.filePrms.add(f); } }
     * 
     *            final String rootGroupKey = (String) targetGroups.firstKey();
     *            this.createHierarchy(targetGroups, rootGroupKey); System.out.println("Hierarchy created");
     *            this.recordFiles(); System.out.println("Files recorded");
     * 
     *            this.rw.commit(); } catch (final AbortedException e) {
     *            System.out.println("Could not write new Target, abort"); e.printStackTrace(); } catch (final
     *            ConstraintException e) { System.out.println("Could not write new Target, abort");
     *            e.printStackTrace(); } catch (final AccessException e) {
     *            System.out.println("Could not write new Target, abort"); throw new RuntimeException(e); }
     *            catch (final IOException e) { throw new RuntimeException(e); } finally { if
     *            (!this.rw.isCompleted()) { this.rw.abort(); } } }
     */

    /**
     * Create a group hierarchy. Some redundant iteration has happened here, but it does not influence the
     * result. To be sorted out later.
     * 
     * @param targetGroups
     * @param rootGroupKey
     * @throws ConstraintException
     */
    void createHierarchy(final TreeMap targetGroups, String rootGroupKey) throws ConstraintException {
        final SortedMap subMap = targetGroups.tailMap(rootGroupKey + "\0");
        for (final Iterator iter = subMap.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            final TargetGroup tg = (TargetGroup) ((ModelObject) elem.getValue());
            String curGroupname = (String) elem.getKey();
            if (curGroupname.equals(rootGroupKey)) {
                continue;
            }
            if (curGroupname.startsWith(rootGroupKey)) {

                if ((curGroupname.split("\\.")).length - 1 == rootGroupKey.split("\\.").length) {

                    final TargetGroup rtg = (TargetGroup) ((ModelObject) targetGroups.get(rootGroupKey));
                    if (!rtg.getSubTargetGroups().contains(tg)) {
                        rtg.addSubTargetGroup(tg);
                    }
                } else {

                    // Drop possible last dot
                    curGroupname = curGroupname.substring(0, curGroupname.length() - 1);
                    // Drop last letter
                    curGroupname = curGroupname.substring(0, curGroupname.lastIndexOf(".") + 1);
                    this.createHierarchy(targetGroups, curGroupname);
                }
            } else {
                if (rootGroupKey.startsWith(curGroupname.substring(0, 1))) {
                    curGroupname = curGroupname.substring(0, curGroupname.length() - 1);
                    rootGroupKey = curGroupname.substring(0, curGroupname.lastIndexOf(".") + 1);

                    final TargetGroup rtg = (TargetGroup) ((ModelObject) targetGroups.get(rootGroupKey));
                    if (!rtg.getSubTargetGroups().contains(tg)) {
                        rtg.addSubTargetGroup(tg);
                    }
                } else {
                    this.createHierarchy(targetGroups, curGroupname);
                }
            }
        }
    }

    void recordFiles() throws AccessException, IOException, ConstraintException {
        for (final Iterator iter = this.filePrms.iterator(); iter.hasNext();) {
            final FileInfoHolder fInfo = (FileInfoHolder) iter.next();
            File mfile = null;
            mfile =
                this.rw.createFile(new FileInputStream(new java.io.File(fInfo.path)),
                    Util.substring(fInfo.name, 26), (LabBookEntry) fInfo.getAssotiatedObject());
            System.out.println(fInfo.extension);
            mfile.setExtension(fInfo.extension);
            mfile.setDescription(fInfo.name);
        }
    }

    /**
     * Record the information to the database Split an information in to several piece, create the number of
     * modelObject to hold the information, create the associations between different model object Note the
     * information were put in the Map somethere outside this method!
     */
    @Override
    protected String putToDb() {
        final WritableVersion rw = this.model.getWritableVersion(AbstractModel.SUPERUSER);
        final Date ds = new Date(System.currentTimeMillis());
        // System.out.println("Start creating an objects: " + ds);

        try {

            for (final Iterator iter = this.citationsPrms.iterator(); iter.hasNext();) {
                final Citation citationParam = (Citation) iter.next();
                final ArrayList authors = new ArrayList();
                for (final Iterator it = citationParam.personsPrms.iterator(); it.hasNext();) {
                    final HashMap personPrms = (HashMap) it.next();
                    final ModelObject author =
                        Util.getOrCreate(rw, org.pimslims.model.people.Person.class.getName(),
                            this.dataOwner, personPrms);
                    // ModelObject author =
                    // create(org.pimslims.model.people.Person.class.getName(),
                    // personPrms);
                    authors.add(author);
                }
                citationParam.citationPrms.put(CitationRoles.authors, authors);
                final ModelObject citation =
                    Util.getOrCreate(rw, citationParam.citationClassName, this.dataOwner,
                        citationParam.citationPrms);
                this.citations.add(citation);
            }

            // targetGroup =
            // targetGroups.get(targetGroupPrms.get(TargetGroupAttr.name);
            // targetGroup = getOrCreate(rw, TargetGroup.class.getName(),
            // targetGroupPrms);
            // assert targetGroup != null : "Group of the Target is undefined!";
            // ArrayList groups = new ArrayList();
            // groups.add(targetGroup);
            // targetPrms.put(TargetRoles.targetGroups, groups);

            this.naturalSource =
                Util.getOrCreate(rw, Organism.class.getName(), this.dataOwner, this.naturalSrcPrms);

            // Protein
            if (this.naturalSource != null) {
                this.moleculePrms.put(MoleculeRoles.naturalSource, this.naturalSource);
            }
            if (this.citations.size() > 0) {
                this.moleculePrms.put(MoleculeRoles.citations, this.citations);
            }
            this.protein = Util.create(rw, Molecule.class.getName(), this.dataOwner, this.moleculePrms);

            // Nucleotide seq of the protein
            this.nucleotides =
                Collections.singleton(Util.create(rw, Molecule.class.getName(), this.dataOwner,
                    this.nucleotidesPrms));

            // Project
            // Try to retrieve if there is one
            // project = getOrCreate(rw, "org.pimslims.model.target.Project", prjPrms);
            // targetPrms.put(TargetRoles.projects, project);

            // Target initialization
            if (this.protein == null) {
                throw new AssertionError("Mandatory relation molecule missed. Stop.");
            }
            this.targetPrms.put(TargetRoles.protein, this.protein);
            if (this.nucleotides != null) {
                this.targetPrms.put(TargetRoles.nucleotides, this.nucleotides);
            }
            if (this.annotation != null) {
                this.targetPrms.put(LabBookEntry.PROP_ATTACHMENTS, this.annotation);
            }
            if (this.naturalSource != null) {
                this.targetPrms.put(TargetRoles.species, this.naturalSource);
            }
            this.targetPrms.put(Target.PROP_MILESTONES, Collections.EMPTY_LIST);

            // DbRef
            this.dbRef =
                Util.create(rw, org.pimslims.model.core.ExternalDbLink.class.getName(), this.dataOwner,
                    this.dbRefPrms);
            if (this.dbRef != null) {
                this.targetPrms.put(TargetRoles.dbRefs, this.dbRef);
            }

            this.target =
                Util.create(rw, org.pimslims.model.target.Target.class.getName(), this.dataOwner,
                    this.targetPrms);

            // Status
            if (this.target == null) {
                throw new AssertionError("Mandatory relation target missed. Stop.");
            }

            this.targetGroup =
                Util.getOrCreate(rw, TargetGroup.class.getName(), this.dataOwner, this.targetGroupPrms);
            assert this.targetGroup != null : "Group of the Target is undefined!";
            ((TargetGroup) this.targetGroup).addTarget((Target) this.target);

            // TargetStatus
            // Try to retrieve if there is one
            if (this.statuses.statuses != null) {
                for (int i = 0; i < this.statuses.statuses.length; i++) {
                    final Statuses.Status stat = this.statuses.statuses[i];
                    this.targetStatus =
                        Util.getOrCreate(rw, "org.pimslims.model.target.TargetStatus", this.dataOwner,
                            Statuses.getTargetStatusPrms(stat));
                    final HashMap statusPrms = new HashMap();
                    statusPrms.put(StatusAttr.date, stat.date);
                    statusPrms.put(StatusRoles.code, this.targetStatus);
                    statusPrms.put(StatusRoles.target, this.target);

                    Util.create(rw, org.pimslims.model.reference.TargetStatus.class.getName(),
                        this.dataOwner, statusPrms);
                }
            } else {

                final HashMap targetStatusPrms = new HashMap();
                targetStatusPrms.put(TargetStatusAttr.name, "Selected");
                this.targetStatus =
                    Util.getOrCreate(rw, "org.pimslims.model.target.TargetStatus", this.dataOwner,
                        targetStatusPrms);

                final HashMap statusPrms = new HashMap();
                statusPrms.put(StatusAttr.date, TargetImporter.defaultStatusDate);
                statusPrms.put(StatusRoles.code, this.targetStatus);
                statusPrms.put(StatusRoles.target, this.target);
                Util.create(rw, org.pimslims.model.reference.TargetStatus.class.getName(), this.dataOwner,
                    statusPrms);
            }

            this.recordFiles();

            rw.commit();
        } catch (final AbortedException e) {
            System.out.println("Could not write new Target, abort");
            e.printStackTrace();
        } catch (final ConstraintException e) {
            System.out.println("Could not write new Target, abort");
            e.printStackTrace();
        } catch (final AccessException e) {
            System.out.println("Could not write new Target, abort");
            throw new RuntimeException(e);
        } catch (final FileNotFoundException e) {
            System.out.println("Could not write new Target, abort");
            throw new RuntimeException(e);
        } catch (final IOException e) {
            System.out.println("Could not write new Target, abort");
            throw new RuntimeException(e);
        } finally {
            if (!rw.isCompleted()) {
                rw.abort();
            }
        }

        // Collect statistics
        if (SwissProtToPIMS.log != null
            && SwissProtToPIMS.log.getLogLevel() == SwissProtToPIMS.collectStatistic) {
            final Date de = new Date(System.currentTimeMillis());
            final long l = (de.getTime() - ds.getTime());
            SwissProtToPIMS.log
                .printExact("Transaction took: " + l + " ms", SwissProtToPIMS.collectStatistic);
            SwissProtToPIMS.log.printExact("Number of Objects created for the file: " + this.counter,
                SwissProtToPIMS.collectStatistic);
            SwissProtToPIMS.log.printExact("Creation time per object: " + l / this.counter + " ms/obj",
                SwissProtToPIMS.collectStatistic);
        }
        final String hook = this.target.get_Hook();
        // Clean all values
        this.cleanAllValues();
        return hook;
    }

    /**
     * Update UL targets based on the state defined in the EBI target tracker database
     * 
     * @param ULTargetName String name of the target
     */
    public void updateULTarget(final String ULTargetName) {
        int c = 0;
        for (final Iterator iter = TargetImporter.eBITargets.keySet().iterator(); iter.hasNext();) {
            final String targetID = (String) iter.next();
            if (targetID.indexOf("UL") >= 0) {
                if (ULTargetName.indexOf(targetID.trim().substring(2)) >= 0) {
                    final EBITarget target = (EBITarget) TargetImporter.eBITargets.get(targetID);
                    System.out.println("DB: " + target.targetID);
                    assert ++c == 1 : "Only one match expected!";
                    for (int i = 0; i < target.eBITargetStatus.length; i++) {
                        final EBITargetStatus targetStatus = target.eBITargetStatus[i];
                        // targetStatus.additionalRemarks;

                        // Correct a grammatical mistake
                        if (targetStatus.progressionStage.equals("In crystalization")) {
                            targetStatus.progressionStage = "In crystallization";
                        }

                        final SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy"); // 24-06-2005
                        Date d = null;
                        try {
                            d = sd.parse(targetStatus.preferredDate);
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }

                        this.statuses.addStatus(targetStatus.progressionStage, new Timestamp(d.getTime()));
                    }
                }
            }
        }
    }

    /**
     * Update UM targets based on the state defined in the EBI target tracker database
     * 
     * @param dbID String database id of the Target
     */
    public void updateUMTarget(final String dbID) {
        int c = 0;
        for (final Iterator iter = TargetImporter.eBITargets.keySet().iterator(); iter.hasNext();) {
            final String targetID = (String) iter.next();
            if (targetID.indexOf("UM") >= 0) {
                final EBITarget target = (EBITarget) TargetImporter.eBITargets.get(targetID);
                if (dbID.equalsIgnoreCase(target.databaseID.trim())) {
                    assert ++c == 1 : "Only one match expected!";
                    System.out.println("Status updated for the Target DBID: " + target.targetID);
                    for (int i = 0; i < target.eBITargetStatus.length; i++) {
                        final EBITargetStatus targetStatus = target.eBITargetStatus[i];
                        // targetStatus.additionalRemarks;

                        // Correct a grammatical mistake
                        if (targetStatus.progressionStage.equals("In crystalization")) {
                            targetStatus.progressionStage = "In crystallization";
                        }

                        final SimpleDateFormat sd = new SimpleDateFormat("dd-M-yy");
                        Date d = null;
                        try {
                            d = sd.parse(targetStatus.preferredDate);
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }
                        this.statuses.addStatus(targetStatus.progressionStage, new Timestamp(d.getTime()));
                    }
                }
            }
        }
    }

    public static void main(final String[] args) {
        // System.out.println(defaultStatusDate);
        // new TargetImporter().updateUMTarget("UM");

    }
} // class end

