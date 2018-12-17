/**
 * pims-web org.pimslims.servlet.importer OpticImporterTest.java
 * 
 * @author Marc Savitsky
 * @date 9 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky
 * 
 */
package org.pimslims.servlet.importer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.IllegalSymbolException;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.model.target.TargetGroup;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.construct.ConstructBean;
import org.pimslims.presentation.construct.ConstructBeanReader;
import org.pimslims.properties.PropertyGetter;

import uk.ac.ox.oppf.pims.inserter.Inserter;

/**
 * OpticImporterTest
 * 
 */
public class OpticImporterTest extends TestCase {

    /**
     * The database being tested
     */
    private final AbstractModel model;

    /**
     * The transaction to use for testing
     */
    private WritableVersion version = null;

    private final String dnaSeq = "tctgaaagaaaagctattaacaagtactatccaccggattacaacccactggaagctgaaaagct"
        + "atccaggaaaatggctgaataactgaaaaccatgaataagagccacgcatcgattagattaatga"
        + "ccccatttagtatgaggtgtttggaatgtaacgaatatattcccaaaagcagaaaattcaatggt"
        + "aagaaagagctactgaaggagaagtacctggactccattaagatatatagactaaccatttcatg"
        + "tccacgttgtgccaattccattgcatttagaacagaccccggtaattcagattatgtaatggaag"
        + "tggggggcgtaagaaactatgtccctcagaaaccaaatgatgacctgaacgctaaaacagctgtc"
        + "gaaagcatagacgagacactgcaacgcttagtgagagagaaggaaatggagcagaacgagaagat"
        + "gggtataaaggagcaagcagatgacaaaatggatttgctggaaaaaagactagccaaaattcaac"
        + "aagagcaagaggatgatgaggaacttgaaaatttaaggaaaaaaaaccttgaaatgagccagaga"
        + "gctgaaatgataaaccgcagcaaacacgcacaacaagaaaaagcagtaacgactgatgatctgga"
        + "caacctcgtagatcaggtatttgacaatcacaggcagcgtaccaataaacctggcaataacaacg"
        + "atgagaagagaactccgttgtttaatcctacatccactaagggaaaaatacagaagaaaagctct"
        + "gtgcgtacgaatccgttagggatagttataaaacgaggaaagtctctcaaa";

    private final String protSeq = "tctgaaagaaaagctattaacaagtactatccaccggattacaacccactggaagctgaaaagct"
        + "atccaggaaaatggctgaataactgaaaaccatgaataagagccacgcatcgattagattaatga"
        + "ccccatttagtatgaggtgtttggaatgtaacgaatatattcccaaaagcagaaaattcaatggt"
        + "aagaaagagctactgaaggagaagtacctggactccattaagatatatagactaaccatttcatg"
        + "tccacgttgtgccaattccattgcatttagaacagaccccggtaattcagattatgtaatggaag"
        + "tggggggcgtaagaaactatgtccctcagaaaccaaatgatgacctgaacgctaaaacagctgtc"
        + "gaaagcatagacgagacactgcaacgcttagtgagagagaaggaaatggagcagaacgagaagat"
        + "gggtataaaggagcaagcagatgacaaaatggatttgctggaaaaaagactagccaaaattcaac"
        + "aagagcaagaggatgatgaggaacttgaaaatttaaggaaaaaaaaccttgaaatgagccagaga"
        + "gctgaaatgataaaccgcagcaaacacgcacaacaagaaaaagcagtaacgactgatgatctgga"
        + "caacctcgtagatcaggtatttgacaatcacaggcagcgtaccaataaacctggcaataacaacg"
        + "atgagaagagaactccgttgtttaatcctacatccactaagggaaaaatacagaagaaaagctct"
        + "gtgcgtacgaatccgttagggatagttataaaacgaggaaagtctctcaaa";

    private final String localName = "OPTIC9292"; /* "OPTIC11889"; */

    private final String comments = "The quick brown fox jumped over the lazy dog";

    private Person person = null;

    private User user = null;

    private TargetGroup targetGroup = null;

    private final String description = "Oppf Target";

    //private final String optic = "12220";
    //private final String oppf = "OPPF6633";

    private final String optic = "12298";

    private final String oppf = "OPPF6593";

    /**
     * @param name
     */
    public OpticImporterTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
        PropertyGetter.setWorkingDirectory("WebContent/WEB-INF/");
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        this.version = this.model.getTestVersion();
        Assert.assertNotNull(this.version);

        // These objects would be expected to exist
        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(Person.PROP_GIVENNAME, "Louise");
        attr.put(Person.PROP_FAMILYNAME, "Bird");
        attr.put(Person.PROP_TITLE, "Dr");
        this.person = this.version.create(Person.class, attr);
        Assert.assertNotNull(this.person);

        attr.clear();
        attr.put(Person.PROP_GIVENNAME, "Joanne");
        attr.put(Person.PROP_FAMILYNAME, "Nettleship");
        attr.put(Person.PROP_TITLE, "Dr");
        final Person jo = this.version.create(Person.class, attr);

        attr.clear();
        attr.put(User.PROP_NAME, jo.getFamilyName());
        attr.put(User.PROP_PERSON, jo);
        this.version.create(User.class, attr);

        attr.clear();
        attr.put(Person.PROP_GIVENNAME, "Ralf");
        attr.put(Person.PROP_FAMILYNAME, "Flaig");
        final Person ralf = this.version.create(Person.class, attr);

        attr.clear();
        attr.put(User.PROP_NAME, ralf.getFamilyName());
        attr.put(User.PROP_PERSON, ralf);
        this.version.create(User.class, attr);

        attr.clear();
        attr.put(Person.PROP_GIVENNAME, "Ray");
        attr.put(Person.PROP_FAMILYNAME, "Owens");
        attr.put(Person.PROP_TITLE, "Dr");
        final Person ray = this.version.create(Person.class, attr);

        attr.clear();
        attr.put(User.PROP_NAME, ray.getFamilyName());
        attr.put(User.PROP_PERSON, ray);
        this.version.create(User.class, attr);

        attr.clear();
        attr.put(User.PROP_PERSON, this.person);
        attr.put(User.PROP_NAME, this.person.getFamilyName());
        this.user = this.version.create(User.class, attr);
        Assert.assertNotNull(this.user);

        //attr.clear();
        //attr.put(Project.PROP_SHORTNAME, "OPPF");
        //attr.put(Project.PROP_COMPLETENAME, "OPPF");
        //this.project = this.version.create(Project.class, attr);
        //Assert.assertNotNull(this.project);

        attr.clear();
        attr.put(TargetGroup.PROP_NAME, "OPPF Targets");
        this.targetGroup = this.version.create(TargetGroup.class, attr);
        Assert.assertNotNull(this.targetGroup);

        attr.clear();
        attr.put(ExperimentType.PROP_NAME, "Optic Construct Design");
        ExperimentType experimentType = this.version.findFirst(ExperimentType.class, attr);

        if (null == experimentType) {
            experimentType = this.version.create(ExperimentType.class, attr);
        }

        attr.clear();
        attr.put(Protocol.PROP_NAME, "OPPF Construct Primer Design");
        attr.put(Protocol.PROP_EXPERIMENTTYPE, experimentType);
        Protocol primerDesignProtocol = this.version.findFirst(Protocol.class, attr);

        if (null == primerDesignProtocol) {
            primerDesignProtocol = this.version.create(Protocol.class, attr);
        }

        //findOrCreatePrimerDesignParameterDefinitions(version);

    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        Assert.assertFalse(this.version.isCompleted());
        if (this.version.isCompleted()) {
            return; // can't tidy up
        }

        if (null != this.version) {
            this.version.abort(); // not testing persistence
        }

    }

/*
    public void testDuplicateImportTarget() throws AccessException, IllegalAlphabetException,
        IllegalSymbolException, SQLException, ClassNotFoundException {

        final Collection<String> targetList = new ArrayList<String>();
        targetList.add("9336");

        //for testing
        for (final Iterator<String> iter = targetList.iterator(); iter.hasNext();) {
            System.out.println("targetList [" + iter.next() + "]");
        }

        final Inserter inserter = new Inserter();

        if (!targetList.isEmpty()) {
            inserter.insertTargets(this.version, targetList);
        }

    }
*/
    /**
     * Note This test should be run on an empty database
     */
    public void testImportTarget() throws AccessException, IllegalAlphabetException, IllegalSymbolException,
        SQLException, ClassNotFoundException, ConstraintException {

        final Collection<String> targetList = new ArrayList<String>();
        targetList.add(this.optic);

        //for testing
        for (final Iterator<String> iter = targetList.iterator(); iter.hasNext();) {
            System.out.println("targetList [" + iter.next() + "]");
        }

        final Inserter inserter = OpticImporter.getInserter();

        if (!targetList.isEmpty()) {
            inserter.insertTargets(this.version, targetList);
        }

        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(Target.PROP_NAME, "OPTIC" + this.optic);
        final Target target = this.version.findFirst(Target.class, attr);
        Assert.assertNotNull(target);

        attr.clear();
        attr.put(ResearchObjective.PROP_LOCALNAME, this.oppf);
        final ResearchObjective objective = this.version.findFirst(ResearchObjective.class, attr);
        Assert.assertNotNull(objective);

        Assert.assertEquals(1, objective.getResearchObjectiveElements().size());
        for (final ResearchObjectiveElement element : objective.getResearchObjectiveElements()) {
            Assert.assertEquals(target, element.getTarget());
        }

        final ConstructBean bean = ConstructBeanReader.readConstruct(objective);
        Assert.assertNotNull("Expressed protein is null", bean.getExpressedProt());
        Assert.assertNotNull("Final protein is null", bean.getFinalProt());

        for (final PrimerBean primer : bean.getPrimers()) {
            Assert.assertNotNull("Primer " + bean.getName() + " has null sequence", primer.getSequence());
            Assert.assertFalse(0 == primer.getSequence().length());
            Assert.assertNotNull("Primer " + bean.getName() + " has null overlap", primer.getOverlap());
            Assert.assertNotNull("Primer " + bean.getName() + " has null GCGene", primer.getGCGene());
            Assert.assertNotNull("Primer " + bean.getName() + " has null GCFull", primer.getGCFull());
            Assert.assertNotNull("Primer " + bean.getName() + " has null TmGene", primer.getTmGene());
        }
    }

    public void testCreateTarget() throws AccessException, IllegalAlphabetException, IllegalSymbolException,
        SQLException, ClassNotFoundException, ConstraintException {

        final Target target = this.create(this.version);
        Assert.assertNotNull(target);
    }

    public void testCreateProtein() throws AccessException, IllegalAlphabetException, IllegalSymbolException,
        SQLException, ClassNotFoundException, ConstraintException {

        final Molecule molecule = this.createProtein(this.version);
        Assert.assertNotNull(molecule);
        Assert.assertEquals(molecule.getSequence(), this.protSeq);
    }

    public void testCreateDnas() throws AccessException, IllegalAlphabetException, IllegalSymbolException,
        SQLException, ClassNotFoundException, ConstraintException {

        final Molecule molecule = this.createDna(this.version);
        Assert.assertNotNull(molecule);
    }

    public void testCreateMolecules() throws AccessException, IllegalAlphabetException,
        IllegalSymbolException, SQLException, ClassNotFoundException, ConstraintException {

        Molecule molecule = this.createProtein(this.version);
        Assert.assertNotNull(molecule);

        molecule = this.createDna(this.version);
        Assert.assertNotNull(molecule);
    }

    public void testFindPerson() throws AccessException, IllegalAlphabetException, IllegalSymbolException,
        SQLException, ClassNotFoundException, ConstraintException {

        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(Person.PROP_GIVENNAME, "Joanne");
        attr.put(Person.PROP_FAMILYNAME, "Nettleship");
        attr.put(Person.PROP_TITLE, "Dr");
        final Person jo = this.version.findFirst(Person.class, attr);
        Assert.assertNotNull(jo);

        attr.clear();
        attr.put(Person.PROP_GIVENNAME, "Ralf");
        attr.put(Person.PROP_FAMILYNAME, "Flaig");
        attr.put(Person.PROP_TITLE, "");
        final Person ralf = this.version.findFirst(Person.class, attr);
        Assert.assertNotNull(ralf);
    }

    public Molecule createProtein(final WritableVersion version) throws AccessException,
        IllegalAlphabetException, IllegalSymbolException, SQLException, ClassNotFoundException,
        ConstraintException {

        // Find the organism
        final ModelObject organism = this.findOrganism(this.version, "Zea mays");

        // Find the component category
        final ComponentCategory category = this.findComponentCategory(this.version, "Protein");

        // Get a HashMap to hold the attributes
        final HashMap<String, Object> attr = new HashMap<String, Object>();

        // Set the attributes
        attr.put(Molecule.PROP_MOLTYPE, "protein");
        attr.put(Molecule.PROP_SEQUENCE, this.protSeq);
        attr.put(Substance.PROP_NAME, this.localName + " Protein");
        attr.put(Substance.PROP_NATURALSOURCE, organism);

        // Create and return the Molecule
        //return new MolComponent(getWritableVersion(), attr);
        //return (MolComponent) version.create(MolComponent.class, attr);
        final Molecule molComponent = this.version.create(Molecule.class, attr);
        if (null != category) {
            molComponent.addCategory(category);
        }
        return molComponent;
    }

    private Molecule createDna(final WritableVersion version) throws AccessException, ConstraintException {

        // Find the organism
        final Organism organism = this.findOrganism(this.version, "Zea mays");

        // Find the component category
        final ComponentCategory category = this.findComponentCategory(this.version, "Nucleic acid");
        System.out.println("findComponentCategory X [" + category + "]");

        // Get a HashMap to hold the attributes
        final HashMap<String, Object> attr = new HashMap<String, Object>();

        // Set the attributes
        attr.put(Molecule.PROP_MOLTYPE, "DNA");
        attr.put(Molecule.PROP_SEQUENCE, this.dnaSeq);
        attr.put(Substance.PROP_NAME, this.localName + " DNA");
        attr.put(Substance.PROP_NATURALSOURCE, organism);

        //attr.put(Molecule.PROP_CATEGORIES, Collections.singleton(category));

        // Create and return the Molecule
        final Molecule molComponent = this.version.create(Molecule.class, attr);
        if (null != category) {
            molComponent.addCategory(category);
        }
        return molComponent;
    }

    private Organism findOrganism(final WritableVersion version, final String name) {
        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(Organism.PROP_NAME, name);
        final Collection<Organism> c = version.findAll(Organism.class, attr);

        if (c.isEmpty()) {
            return null;
        }
        return c.iterator().next();
    }

    private ComponentCategory findComponentCategory(final WritableVersion version, final String name) {
        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(ComponentCategory.PROP_NAME, name);
        final Collection<ComponentCategory> c = version.findAll(ComponentCategory.class, attr);

        if (c.isEmpty()) {
            return null;
        }
        return c.iterator().next();
    }

    public synchronized Target create(final WritableVersion version) throws AccessException,
        ConstraintException, IllegalAlphabetException, IllegalSymbolException, SQLException,
        ClassNotFoundException {

        //if (targetBean == null) 
        //    throw new IllegalStateException("targetBean not set yet!");

        System.out.println("TargetInserter.create [" + this.localName + ":" + this.comments + "]");
        Target target = null;

        // Find the project
        //final Collection<Project> projects = Collections.singletonList(this.project);

        // Create the protein MolComponent
        final Molecule protein = this.createProtein(version);

        // Create the db refs
        final Collection<Molecule> dnas = Collections.singletonList(this.createDna(version));

        //Create the targetGroups
        final Collection<TargetGroup> groups = Collections.singletonList(this.targetGroup);

        // Find the organism
        final ModelObject organism = this.findOrganism(this.version, "Zea mays");

        // Define the attributes
        final HashMap<String, Object> attr = new HashMap<String, Object>();
        attr.put(Target.PROP_NAME, this.localName);
        attr.put(LabBookEntry.PROP_CREATOR, this.user);
        attr.put(Target.PROP_FUNCTIONDESCRIPTION, this.description);
        attr.put(Target.PROP_PROTEIN, protein);
        attr.put(Target.PROP_WHYCHOSEN, this.comments);
        //attr.put(Target.PROP_ATTACHMENTS, dbRefs);
        attr.put(Target.PROP_NUCLEICACIDS, dnas);
        //attr.put(Target.PROP_PROJECTS, projects);
        attr.put(Target.PROP_SPECIES, organism);
        attr.put(Target.PROP_TARGETGROUPS, groups);

        // version 27- of the database
        // attr.put(Target.PROP_COMMONNAME, targetBean.getCommonName());
        // attr.put(Target.PROP_LOCALNAME, targetBean.getLocalName());
        // version 27+ of the database
        //attr.put(Target.PROP_ALIASES, Collections.singleton(this.localName));
        //target.setAliasNames(Collections.singleton(this.localName));

        // Create the target
        //Target target = new Target(getWritableVersion(), attr);
        target = version.create(Target.class, attr);
        target.setAliasNames(Collections.singleton(this.localName));

        return target;

    }
}
