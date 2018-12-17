package org.pimslims.embl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.biojava.bio.BioException;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.Alias;
import org.pimslims.model.target.Project;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructResultBean;
import org.pimslims.servlet.spot.SpotTarget;

public class HamburgResearchObjectiveBeanTest extends TestCase {

    private static final String OWNER = "Non-MPI-IB targets" + System.currentTimeMillis();

    private static final String CONDITION =
        "NiNTA : 50mM Tris (pH 8.0), 200mM NaCl, 5% (v/v) glycerol (+ 500mM imidazole), TEV cleavage";

    private static final String UNIQUE = "htb" + System.currentTimeMillis();

    private final AbstractModel model;

    public HamburgResearchObjectiveBeanTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testSaveTarget() throws ConstraintException, BioException {
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean(UNIQUE, "Rv0066c", Collections.EMPTY_SET, Collections.EMPTY_SET,
                "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            assertEquals("MTB-" + bean.getRvNumber(), ro.get_Name());
            assertEquals(bean.getName(), ro.getWhyChosen());
            assertEquals("EMBL Hamburg", ro.get_Owner());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSaveOrf() throws ConstraintException, BioException {
        HamburgOrfBean orf =
            new HamburgOrfBean("orf" + UNIQUE, "KKK", "Mycobacterium tuberculosis", null, null);
        orf.setDnaSequence("CATG");
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean("t" + UNIQUE, "Rv0066c", Collections.singleton(orf),
                Collections.EMPTY_SET, "EMBL2 non MPI-IB targets");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            new org.pimslims.model.core.LabNotebook(version, OWNER);
            new Project(version, OWNER, OWNER);
            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);

            assertEquals(1, ro.getResearchObjectiveElements().size());
            Target target = getTarget(ro);
            assertEquals("MTB-" + bean.getRvNumber(), target.getName());
            assertEquals(orf.getName(), target.getFunctionDescription());
            Set<Alias> aliases = target.getAliases();
            assertEquals(2, aliases.size());
            // was assertEquals(orf.getName(), aliases.iterator().next());
            Molecule protein = target.getProtein();
            assertEquals(orf.getProteinSequence(), protein.getSeqString().toString());
            assertEquals(orf.getOrganism(), target.getSpecies().getName());
            // TODO needs fix in DM assertEquals(bean.getWorkpackage(),
            // target.get_Owner());
            assertEquals(1, target.getProjects().size());
            // assertEquals(OWNER, target.getProjects()  .iterator().next().getName());
            Set<Molecule> nucleicAcids = target.getNucleicAcids();
            assertEquals(1, nucleicAcids.size());
            assertEquals("CATG", nucleicAcids.iterator().next().getSeqString());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSave2Orfs() throws ConstraintException, BioException {
        HamburgOrfBean orf1 =
            new HamburgOrfBean("hypothetical protein", "KKK", "Mycobacterium tuberculosis", null, null);
        orf1.setDnaSequence("CATG");
        HamburgOrfBean orf2 =
            new HamburgOrfBean("hypothetical protein" + UNIQUE + "two", "KKK", "Mycobacterium tuberculosis",
                null, null);
        orf2.setDnaSequence("CATG");
        Collection<HamburgOrfBean> orfs = new HashSet<HamburgOrfBean>();
        orfs.add(orf1);
        orfs.add(orf2);
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean("t" + UNIQUE, "Rv0066c", orfs, Collections.EMPTY_SET,
                "EMBL2 non MPI-IB targets");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {

            new org.pimslims.model.core.LabNotebook(version, OWNER);
            new Project(version, OWNER, OWNER);
            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);

            assertEquals(2, ro.getResearchObjectiveElements().size());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    private Target getTarget(ResearchObjective ro) {
        ResearchObjectiveElement element = ro.getResearchObjectiveElements().iterator().next();
        assertEquals("target", element.getComponentType());
        Target target = element.getTarget();
        return target;
    }

    public void testSaveAccession() throws ConstraintException, BioException {
        HamburgOrfBean orf = new HamburgOrfBean("orf" + UNIQUE, null, null, "Swissprot", "Q07702");
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean("t" + UNIQUE, "description", Collections.singleton(orf),
                Collections.EMPTY_SET, "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            new Project(version, "EMBL Hamburg", "EMBL Hamburg");
            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            assertEquals(1, ro.getResearchObjectiveElements().size());
            Target target = getTarget(ro);
            assertEquals(1, target.getExternalDbLinks().size());
            ExternalDbLink link = target.getExternalDbLinks().iterator().next();
            assertEquals("Swiss-Prot", link.getDbName().getName());
            assertEquals(orf.getAccessId(), link.getCode());
            assertNotNull(link.getUrl());
            assertFalse("".equals(link.getUrl()));
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSaveProject() throws ConstraintException, BioException {
        HamburgExperimentBean experiment =
            new HamburgExperimentBean("OK", 20f, "Rosetta(DE3)pLysS", "pETM-11", "constructDescription", "",
                "", "", "", "");
        HamburgProjectBean project =
            new HamburgProjectBean("1", "Cloned", null, null, "Matthias Wilmanns", "Public", "? (41-296)",
                null, "remarks", experiment, Collections.singleton(new DbLinkBean("Pubmed",
                    "TUBERCULOSIS.ACT.")));
        HamburgOrfBean orf =
            new HamburgOrfBean("orf" + UNIQUE, "KKK", "Mycobacterium tuberculosis", null, null);
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean(UNIQUE, "Rv" + UNIQUE, Collections.singleton(orf), Collections
                .singleton(project), "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Construct Design");
            Protocol protocol = new Protocol(version, "Hamburg Construct Details", type);
            new ParameterDefinition(version, HamburgResearchObjectiveBeanWriter.EXPRESSION_QUALITY, "String",
                protocol);
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            new Project(version, "EMBL Hamburg", "EMBL Hamburg");
            Person person = new Person(version, "Wilmanns");
            person.setGivenName("Matthias");
            person.addUser(new User(version, UNIQUE));

            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            Collection<Experiment> constructs = writer.getConstructs(ro);
            assertEquals(1, constructs.size());
            Experiment construct = constructs.iterator().next();
            assertEquals("MTB-Rv" + UNIQUE + "." + project.getId(), construct.getName());
            assertEquals(project.getVector(), HamburgResearchObjectiveBeanWriter
                .getExpressionVector(construct));
            assertEquals(project.getStrain(), HamburgResearchObjectiveBeanWriter
                .getExpressionStrain(construct));
            assertEquals(construct.getCreator().getPerson().getFamilyName(), "Wilmanns");
            assertEquals(project.getDescription(), construct.getDetails());
            assertEquals(project.getExpressionQuality(), HamburgResearchObjectiveBeanWriter
                .getExpressionQuality(construct));
            assertEquals(project.getExpressionLevel(), HamburgResearchObjectiveBeanWriter
                .getExpressionLevel(construct));
            assertNotNull(construct.getProtocol());
            Parameter parm =
                construct.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME,
                    HamburgResearchObjectiveBeanWriter.EXPRESSION_QUALITY);
            assertNotNull(parm.getParameterDefinition());
            assertEquals("remarks\nconstructDescription", HamburgResearchObjectiveBeanWriter
                .getLastExperiment(construct).getDetails());
            assertEquals(1, construct.getExternalDbLinks().size());
            ExternalDbLink link = construct.getExternalDbLinks().iterator().next();
            assertEquals("PubMed", link.getDbName().getName());
            assertEquals("TUBERCULOSIS.ACT.", link.getCode());

            Collection<ConstructResultBean> constructBeans = SpotTarget.getMilestones(version, getTarget(ro));
            assertEquals(1, constructBeans.size());

        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSaveCrystallography() throws ConstraintException, BioException {
        /*
         * HamburgExperimentBean(String expressionQuality, Float
         * expressionLevel, String strain, String vector, String
         * constructDescription, String crystalSize, String resolution, String
         * ligand, String condition, String crystalForm)
         */
        HamburgExperimentBean experiment =
            new HamburgExperimentBean("OK", 20f, "Rosetta(DE3)pLysS", "pETM-11", "", "100 x 100", "2.15 Å",
                "no ligand", CONDITION, "needles");
        HamburgProjectBean project =
            new HamburgProjectBean("1", "Cloned", null, null, "Matthias Wilmanns", "Public", "? (41-296)",
                null, "remarks", experiment, Collections.singleton(new DbLinkBean("Pubmed",
                    "TUBERCULOSIS.ACT.")));
        HamburgOrfBean orf =
            new HamburgOrfBean("orf" + UNIQUE, "KKK", "Mycobacterium tuberculosis", null, null);
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean(UNIQUE, "description", Collections.singleton(orf), Collections
                .singleton(project), "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ExperimentType type =
                version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, "Construct Design");
            Protocol protocol = new Protocol(version, "Hamburg Construct Details", type);
            new ParameterDefinition(version, HamburgResearchObjectiveBeanWriter.EXPRESSION_QUALITY, "String",
                protocol);
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            new Project(version, "EMBL Hamburg", "EMBL Hamburg");
            Person person = new Person(version, "Wilmanns");
            person.setGivenName("Matthias");
            person.addUser(new User(version, UNIQUE));

            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            Collection<Experiment> constructs = writer.getConstructs(ro);
            assertEquals(1, constructs.size());
            Experiment construct = constructs.iterator().next();
            Experiment last = HamburgResearchObjectiveBeanWriter.getLastExperiment(construct);
            assertEquals("100 x 100", HamburgResearchObjectiveBeanWriter.getParameterValue("Crystal Size",
                last));
            assertEquals("2.15 Å", HamburgResearchObjectiveBeanWriter.getParameterValue("Resolution", last));
            assertEquals("no ligand", HamburgResearchObjectiveBeanWriter.getParameterValue("Ligand", last));
            assertEquals(CONDITION, HamburgResearchObjectiveBeanWriter.getParameterValue(
                "Crystallization condition", last));
            assertEquals("needles", HamburgResearchObjectiveBeanWriter
                .getParameterValue("Crystal Form", last));
            assertNotNull(last.getProtocol());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSaveMilestone() throws ConstraintException, BioException {
        HamburgExperimentBean experiment =
            new HamburgExperimentBean("OK", 20f, "Rosetta(DE3)pLysS", "pETM-11", "", "", "", "", "", "");
        HamburgProjectBean project =
            new HamburgProjectBean("1", "Cloned", null, null, "Matthias Wilmanns", "Public", "? (41-296)",
                "", "", experiment, Collections.EMPTY_SET);
        HamburgOrfBean orf =
            new HamburgOrfBean("orf" + UNIQUE, "KKK", "Mycobacterium tuberculosis", null, null);
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean(UNIQUE, "description", Collections.singleton(orf), Collections
                .singleton(project), "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            new Project(version, "EMBL Hamburg", "EMBL Hamburg");
            Person person = new Person(version, "Wilmanns");
            person.setGivenName("Matthias");
            person.addUser(new User(version, UNIQUE));

            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            Collection<Experiment> constructs = writer.getConstructs(ro);
            assertEquals(1, constructs.size());
            Experiment construct = constructs.iterator().next();
            assertNotNull(HamburgResearchObjectiveBeanWriter.getMilestone(construct));
            assertEquals(project.getTask(), HamburgResearchObjectiveBeanWriter.getMilestone(construct)
                .getStatus().getName());
            Experiment last = HamburgResearchObjectiveBeanWriter.getLastExperiment(construct);
            assertNotNull(last.getProtocol());

        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSaveLocation() throws ConstraintException, BioException {
        HamburgExperimentBean experiment =
            new HamburgExperimentBean("OK", 20f, "Rosetta(DE3)pLysS", "pETM-11", "", "", "", "", "", "");
        HamburgProjectBean project =
            new HamburgProjectBean("1", "Cloned", "Box 3, E9", null, "Matthias Wilmanns", "Public",
                "? (41-296)", "Linda Schuldt", "", experiment, Collections.EMPTY_SET);
        HamburgOrfBean orf =
            new HamburgOrfBean("orf" + UNIQUE, "KKK", "Mycobacterium tuberculosis", null, null);
        HamburgResearchObjectiveBean bean =
            new HamburgResearchObjectiveBean(UNIQUE, "description", Collections.singleton(orf), Collections
                .singleton(project), "");
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            new org.pimslims.model.core.LabNotebook(version, "EMBL Hamburg");
            new Project(version, "EMBL Hamburg", "EMBL Hamburg");
            Person person = new Person(version, "Wilmanns");
            person.setGivenName("Matthias");
            person.addUser(new User(version, UNIQUE));
            person = new Person(version, "Schuldt");
            person.setGivenName("Linda");
            User contact = new User(version, "Linda Schuldt");
            person.addUser(contact);

            HamburgResearchObjectiveBeanWriter writer = new HamburgResearchObjectiveBeanWriter(version);
            ResearchObjective ro = writer.save(bean);
            Collection<Experiment> constructs = writer.getConstructs(ro);
            assertEquals(1, constructs.size());
            Experiment construct = constructs.iterator().next();
            assertNotNull(HamburgResearchObjectiveBeanWriter.getLocation(construct));
            assertEquals(project.getLabID(), HamburgResearchObjectiveBeanWriter.getLocation(construct)
                .getName());
            assertEquals(contact, HamburgResearchObjectiveBeanWriter.getContact(construct));

        } finally {
            version.abort(); // not testing persistence here
        }
    }

    // TODO test contact

}
