package org.pimslims.embl;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.BioException;
import org.pimslims.access.Access;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.StatusUtility;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.Database;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Project;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.bioinf.DbRefBean;

/**
 * Save a bean representing the legacy data about a complex (or single target).
 * 
 * @author cm65
 * 
 */
public class HamburgResearchObjectiveBeanWriter {
    /**
     * parameter names
     */
    private static final String LIGAND = "Ligand";

    private static final String CONDITION = "Crystallization condition";

    private static final String CRYSTAL_FORM = "Crystal Form";

    private static final String CRYSTAL_SIZE = "Crystal Size";

    private static final String RESOLUTION = "Resolution";

    private static final String EXPRESSION_STRAIN = "Expression Strain";

    private static final String EXPRESSION_VECTOR = "Expresssion Vector";

    static final String EXPRESSION_QUALITY = "Expression Quality";

    private static final String EXPRESSION_LEVEL = "Expression Level";

    private static final Map<String, String> WORKPACKAGES = new HashMap();
    static {
        WORKPACKAGES.put("EMBL1 MPI Infectious Biology Berlin", "MPI Infectious Biology Berlin targets");
        WORKPACKAGES.put("EMBL2 non MPI-IB targets", "Non-MPI-IB targets");
        WORKPACKAGES.put("", "EMBL Hamburg");
    }

    private final WritableVersion version;

    private final ExperimentType constructDesign;

    private final Calendar date = Calendar.getInstance();

    private final Protocol protocol;

    private final ParameterDefinition strainDef;

    private final ParameterDefinition vectorDef;

    private final ParameterDefinition qualityDef;

    private final ParameterDefinition levelDef;

    private final Protocol crystallography;

    private final ParameterDefinition crystalSize;

    private final ParameterDefinition resolution;

    private final ParameterDefinition ligand;

    private final ParameterDefinition condition;

    private final ParameterDefinition crystalForm;

    public HamburgResearchObjectiveBeanWriter(WritableVersion version) {
        super();

        // set up some constants that can be used each time we save
        this.version = version;
        this.constructDesign =
            version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Construct Design");
        assert null != this.constructDesign : "Please load Hamburg reference data";

        this.protocol = this.version.findFirst(Protocol.class, Protocol.PROP_NAME, "PLUMS Molecular Biology");
        assert null != this.protocol : "Please load Hamburg reference data";
        this.strainDef =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, EXPRESSION_STRAIN);
        this.vectorDef =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, EXPRESSION_VECTOR);
        this.qualityDef =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, EXPRESSION_QUALITY);
        this.levelDef =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, EXPRESSION_LEVEL);

        this.crystallography =
            this.version.findFirst(Protocol.class, Protocol.PROP_NAME, "PLUMS Crystallography");
        this.crystalSize =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, CRYSTAL_SIZE);
        this.resolution =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, RESOLUTION);
        this.ligand = version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, LIGAND);
        this.condition =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, CONDITION);
        this.crystalForm =
            version.findFirst(ParameterDefinition.class, ParameterDefinition.PROP_NAME, CRYSTAL_FORM);
    }

    public ResearchObjective save(HamburgResearchObjectiveBean bean) throws ConstraintException, BioException {
        String workpackage = bean.getWorkpackage();
        if (WORKPACKAGES.containsKey(workpackage)) {
            workpackage = WORKPACKAGES.get(workpackage);
        }
        version.setDefaultOwner(workpackage);
        String name = bean.getName();
        if (bean.getRvNumber().startsWith("Rv")) {
            name = "MTB-" + bean.getRvNumber();
        }
        ResearchObjective ro = new ResearchObjective(version, name, bean.getName());

        // process all the open reading frames
        Collection<HamburgOrfBean> orfs = bean.getOrfs();
        for (Iterator<HamburgOrfBean> iterator = orfs.iterator(); iterator.hasNext();) {
            HamburgOrfBean orf = iterator.next();
            Target target = saveORF(bean, ro, orf);
            if (1 == orfs.size()) {
                target.setName(name);
                new Alias(version, bean.getRvNumber(), target);
                new Alias(version, bean.getName(), target);
/*
                Set<Alias> aliases = target.getAliases();
                aliases.add(bean.getRvNumber());
                aliases.add(bean.getName());
				target.setAliases(aliases ); */
            }
        }

        // process all the "projects" (constructs)
        Collection<HamburgProjectBean> projects = bean.getProjects();
        for (Iterator<HamburgProjectBean> iterator = projects.iterator(); iterator.hasNext();) {
            HamburgProjectBean project = iterator.next();
            saveConstruct(bean, ro, project);
        }
        return ro;
    }

    private void saveConstruct(HamburgResearchObjectiveBean bean, ResearchObjective ro,
        HamburgProjectBean project) throws ConstraintException {
        if (!HamburgProjectBean.PUBLIC.equals(project.getAccess())) {
            throw new AssertionError("Only public projects are supported so far");
        }

        Experiment construct =
            new Experiment(this.version, ro.getName() + "." + project.getId(), this.date, this.date,
                this.constructDesign);
        construct.setProtocol(this.protocol);
        construct.setResearchObjective(ro);
        // no, can no longer do this construct.setCreator(getScientist(project.getPi()));
        construct.setDetails(project.getDescription());

        Parameter strain = new Parameter(version, construct);
        strain.setName(EXPRESSION_STRAIN);
        strain.setValue(project.getStrain());
        strain.setParameterDefinition(this.strainDef);
        strain.setParamType("String");
        Parameter vector = new Parameter(version, construct);
        vector.setName(EXPRESSION_VECTOR);
        vector.setValue(project.getVector());
        vector.setParameterDefinition(this.vectorDef);
        vector.setParamType("String");
        Parameter quality = new Parameter(version, construct);
        quality.setName(EXPRESSION_QUALITY);
        quality.setValue(project.getExpressionQuality());
        quality.setParameterDefinition(this.qualityDef);
        quality.setParamType("String");
        Parameter level = new Parameter(version, construct);
        level.setName(EXPRESSION_LEVEL);
        if (null != project.getExpressionLevel()) {
            level.setValue(project.getExpressionLevel().toString());
        }
        level.setParameterDefinition(this.levelDef);
        level.setParamType("Float");

        // process the "Task"
        String task = project.getTask();
        Protocol protocol = getProtocol(task);
        Experiment last =
            new Experiment(this.version, bean.getName() + "." + project.getId() + "." + task, this.date,
                this.date, protocol.getExperimentType());
        last.setProtocol(protocol);
        last.setResearchObjective(ro);
        last.setDetails(project.getRemarks() + "\n" + project.getConstructDescription());
        if (null != project.getContact()) {
            last.setCreator(getScientist(project.getContact()));
        }
        if (!"Selected".equals(task)) {
            StatusUtility.createMilestone(last);
        }

        // save crystallography parameters
        if (project.hasCrystallography()) {
            last.setProtocol(this.crystallography);
            last.setExperimentType(this.crystallography.getExperimentType());
            Parameter parm = new Parameter(version, last);
            parm.setParameterDefinition(this.crystalSize);
            parm.setName(CRYSTAL_SIZE);
            parm.setValue(project.getCrystalSize());
            parm.setParamType("String");
            parm = new Parameter(version, last);
            parm.setParameterDefinition(this.resolution);
            parm.setName(RESOLUTION);
            parm.setValue(project.getResolution());
            parm.setParamType("String");
            parm = new Parameter(version, last);
            parm.setParameterDefinition(this.ligand);
            parm.setName(LIGAND);
            parm.setValue(project.getLigand());
            parm.setParamType("String");
            parm = new Parameter(version, last);
            parm.setParameterDefinition(this.condition);
            parm.setName(CONDITION);
            parm.setValue(project.getCondition());
            parm.setParamType("String");
            parm = new Parameter(version, last);
            parm.setParameterDefinition(this.crystalForm);
            parm.setName(CRYSTAL_FORM);
            parm.setValue(project.getCrystalForm());
            parm.setParamType("String");
        }

        // create output sample
        Sample sample = new Sample(version, last.getName());
        OutputSample os = new OutputSample(version, last);
        os.setName("Product");
        os.setSample(sample);
        if (null != project.getLabID()) {
            Location location = getLocation(project.getLabID());
            Holder holder = new Holder(version, last.getName());
            new HolderLocation(version, Calendar.getInstance(), location, holder);
            sample.setHolder(holder);
        }

        // save the db links
        for (Iterator iterator = project.getDbLinks().iterator(); iterator.hasNext();) {
            DbLinkBean link = (DbLinkBean) iterator.next();
            Database dbName = getDatabase(link.getDbName());
            new ExternalDbLink(version, dbName, construct).setCode(link.getAccession());
        }
    }

    private Location getLocation(String labID) throws ConstraintException {
        Location ret = version.findFirst(Location.class, Location.PROP_NAME, labID);
        if (null == ret) {
            assert Access.ADMINISTRATOR.equals(version.getUsername());
            ret = new Location(version, labID);

        }
        return ret;
    }

    /**
     * Convert EMBL milestone names to the PiMS experiment type
     */
    private static final Map<String, String> TYPES = new HashMap();
    static {
        TYPES.put("Selected", "Unspecified");
        TYPES.put("Cloned", "Ligation");
        TYPES.put("Expressed", "Prokaryotic Expression");
        TYPES.put("Soluble", "Prokaryotic Expression Trial");
        TYPES.put("Purified", "Chromatography");
        TYPES.put("Crystallized", "PLUMS Crystallography");
        TYPES.put("Diffraction-quality Crystals", "PLUMS Crystallography");
        TYPES.put("Crystal Structure", "PLUMS Crystallography");
        TYPES.put("In PDB", "PLUMS Crystallography");
    }

    private Protocol getProtocol(String task) {

        Protocol ret = version.findFirst(Protocol.class, Protocol.PROP_NAME, TYPES.get(task.trim()));
        if (null == ret) {
            throw new java.lang.IllegalArgumentException("No protocol for: " + task);
            /*
             * Could: assert Access.ADMINISTRATOR.equals(version.getUsername());
             * return new DbName(version, name);
             * 
             * This is fine as long as your spellings are consistent. If your
             * database has not been curated, this will cause problems.
             */
        }
        return ret;
    }

    private static final Pattern NAME = Pattern.compile("^([a-zA-Z\\-]+)\\s+([a-zA-Z\\\\-]+)(,.*)?$");

    /**
     * find the PiMS record of the PI
     * 
     * @param name
     * @return
     */
    private User getScientist(String name) {
        if (name.contains(",")) {
            System.out.println("Ignoring second name: " + name);
        }
        String name2 = name.replace("Muller", "Mueller"); // correct spelling
        Matcher m = NAME.matcher(name2);
        if (!m.matches()) {
            System.err.println("Unknown name: " + name);
            return null;
        }
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(Person.PROP_GIVENNAME, m.group(1));
        criteria.put(Person.PROP_FAMILYNAME, m.group(2));
        Person scientist = this.version.findFirst(Person.class, criteria);
        if (null == scientist || 1 != scientist.getUsers().size()) {
            System.err.println("Unknown name: " + name);
            return null;
        }
        return scientist.getUsers().iterator().next();
    }

    // should really serialise accesses to this
    private static int protein = 0;

    private Target saveORF(HamburgResearchObjectiveBean bean, ResearchObjective ro, HamburgOrfBean orf)
        throws ConstraintException, BioException {
        String proteinName = orf.getName();
        if ("?".equals(proteinName) || "hypothetical protein".equals(proteinName)
            || proteinName.contains("robabl") // probably, Probable
            || proteinName.contains("like")) {
            proteinName += " " + (protein++);
        }
        Molecule protein = new Molecule(version, "protein", proteinName + ".p");
        if (null != orf.getProteinSequence()) {
            protein.setSeqString(new ProteinSequence(orf.getProteinSequence()).getSequence());
        }
        // A PiMS target represents an ORF
        Target target = new Target(version, proteinName, protein);
        target.addProject(getProject(bean.getWorkpackage()));
        target.setFunctionDescription(orf.getName());
        if (null != orf.getOrganism()) {
            Organism species = getSpecies(orf.getOrganism());
            target.setSpecies(species);
        }
        if (null != orf.getAccessId()) {
            Database database = getDatabase(orf.getDatabase());
            ExternalDbLink link = new ExternalDbLink(version, database, target);
            link.setCode(orf.getAccessId());
            String url = new DbRefBean(database, link).getLink();
            link.setUrl(url);
        }
        if (null != orf.getDnaSequence()) {
            Molecule dna = new Molecule(version, "DNA", proteinName + ".dna");
            dna.setSeqString(new DnaSequence(orf.getDnaSequence()).getSequence());
            target.addNucleicAcid(dna);
        }
        // link the orf to the research objective (complex)
        new ResearchObjectiveElement(version, "target", bean.getRvNumber(), ro).setTarget(target);
        return target;
    }

    private Project getProject(String workpackage) {
        if (WORKPACKAGES.containsKey(workpackage)) {
            workpackage = WORKPACKAGES.get(workpackage);
        }
        Project ret = version.findFirst(Project.class, Project.PROP_COMPLETENAME, workpackage);
        if (null == ret) {
            throw new java.lang.IllegalArgumentException("Unknown project: " + workpackage);
            /*
             * Could: assert Access.ADMINISTRATOR.equals(version.getUsername());
             * return new Project(version, name);
             * 
             * This is fine as long as your spellings are consistent. If your
             * database has not been curated, this will cause problems.
             */
        }
        return ret;
    }

    /**
     * Convert EMBL database names to the PiMS name
     */
    private static final Map<String, String> DBNAMES = new HashMap();
    static {
        DBNAMES.put("Swissprot", "Swiss-Prot");
        DBNAMES.put("Pubmed", "PubMed");
        DBNAMES.put("PDB", "PDB");
    }

    /**
     * 
     * @param name database name
     * @return the PiMS record representing it
     */
    private Database getDatabase(String name) {
        Database ret = version.findFirst(Database.class, Database.PROP_NAME, DBNAMES.get(name));
        if (null == ret) {
            throw new java.lang.IllegalArgumentException("Unknown database: " + name);
            /*
             * Could: assert Access.ADMINISTRATOR.equals(version.getUsername());
             * return new DbName(version, name);
             * 
             * This is fine as long as your spellings are consistent. If your
             * database has not been curated, this will cause problems.
             */
        }
        return ret;
    }

    /**
     * 
     * @param name the name of the organism
     * @return the PiMS record representing it
     */
    private Organism getSpecies(String name) {
        if ("".equals(name)) {
            return null;
        }
        Organism ret = version.findFirst(Organism.class, Organism.PROP_NAME, name);
        if (null == ret) {
            throw new java.lang.IllegalArgumentException("Unknown organism: " + name);
            /*
             * Could: assert Access.ADMINISTRATOR.equals(version.getUsername());
             * return new Organism(version, name);
             * 
             * This is fine as long as your spellings are consistent. If your
             * database has not been curated, this will cause problems.
             */
        }
        return ret;
    }

    public Collection<Experiment> getConstructs(ResearchObjective ro) {
        return ro.findAll(ResearchObjective.PROP_EXPERIMENTS, Experiment.PROP_EXPERIMENTTYPE,
            this.constructDesign);
    }

    public static String getExpressionVector(Experiment construct) {
        Parameter parm =
            construct.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, EXPRESSION_VECTOR);
        return parm.getValue();
    }

    public static String getExpressionStrain(Experiment construct) {
        Parameter parm =
            construct.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, EXPRESSION_STRAIN);
        return parm.getValue();
    }

    public static String getExpressionQuality(Experiment construct) {
        Parameter parm =
            construct.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, EXPRESSION_QUALITY);
        return parm.getValue();
    }

    public static Float getExpressionLevel(Experiment construct) {
        Parameter parm =
            construct.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, EXPRESSION_LEVEL);
        return Float.parseFloat(parm.getValue());
    }

    public static Milestone getMilestone(Experiment construct) {
        Experiment experiment = getLastExperiment(construct);
        Set<Milestone> milestones = experiment.getMilestones();
        if (1 != milestones.size()) {
            return null;
        }
        return milestones.iterator().next();
    }

    static Experiment getLastExperiment(Experiment construct) {
        Collection<Experiment> experiments = construct.getResearchObjective().getExperiments();
        experiments.remove(construct);
        if (1 != experiments.size()) {
            // we can only record the construct design and the last experiment
            return null;
        }
        Experiment experiment = experiments.iterator().next();
        return experiment;
    }

    public static Location getLocation(Experiment construct) {
        Experiment experiment = getLastExperiment(construct);
        if (experiment.getOutputSamples().isEmpty()) {
            return null;
        }
        Sample sample = experiment.getOutputSamples().iterator().next().getSample();
        if (null == sample.getHolder()) {
            return null;
        }
        Location location = sample.getHolder().getHolderLocations().iterator().next().getLocation();
        return location;
    }

    public static User getContact(Experiment construct) {
        Experiment experiment = getLastExperiment(construct);
        return experiment.getCreator();
    }

    public static String getParameterValue(String name, Experiment last) {
        Parameter parameter = last.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, name);
        if (null == parameter) {
            return null;
        }
        return parameter.getValue();
    }
}
