/**
 * current-pims-web org.pimslims.servlet.spot SpotNewTargetTest.java
 * 
 * @author cm65
 * @date 28 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.servlet.spot;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Person;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.mock.MockHttpServletRequest;
import org.pimslims.presentation.mock.MockHttpServletResponse;
import org.pimslims.presentation.mock.MockServletConfig;

/**
 * SpotNewTargetTest
 * 
 */
public class SpotNewTargetTest extends TestCase {

    private static final String UNIQUE = "nt" + System.currentTimeMillis();

    /**
     * 
     */
    private static final String COMMENTS = "comments" + SpotNewTargetTest.UNIQUE;

    /**
     * 
     */
    private static final String TARGET_NAME = "target" + SpotNewTargetTest.UNIQUE;

    /**
     * 
     */
    private static final String PROTEIN_NAME = "prot" + SpotNewTargetTest.UNIQUE;

    private static final String PROJECT_NAME = "proj" + SpotNewTargetTest.UNIQUE;

    private final AbstractModel model;

    private String personHook;

    //private String projectHook;

    private String organismHook;

    private MockHttpServletResponse response;

    private SpotNewTarget servlet;

    private MockHttpServletRequest request;

    private HashMap<String, String[]> parms;

    /**
     * @param name
     */
    public SpotNewTargetTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    @Override
    public void setUp() throws ServletException, AbortedException, ConstraintException {
        // create the objects we need
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            final Organism organism = new Organism(version, SpotNewTargetTest.UNIQUE);
            this.organismHook = organism.get_Hook();
            final Person person = new Person(version, SpotNewTargetTest.UNIQUE);
            this.personHook = person.get_Hook();
            /*final Project project =
                new Project(version, SpotNewTargetTest.PROJECT_NAME, SpotNewTargetTest.PROJECT_NAME); */
            new User(version, SpotNewTargetTest.UNIQUE).setPerson(person);
            //this.projectHook = project.get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        this.servlet = new SpotNewTarget();
        this.servlet.init(new MockServletConfig(this.model));
        this.response = new MockHttpServletResponse();
        this.parms = new HashMap<String, String[]>();
        this.parms.put("dnaSeq", new String[] { "atgaaa" });
        this.parms.put("organismId", new String[] { this.organismHook });
        //this.parms.put(PIMSServlet.LAB_NOTEBOOK_ID, new String[] { this.projectHook });
        this.parms.put("personHook", new String[] { this.personHook });

        this.request = new MockHttpServletRequest("post", this.parms);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            /* final Project project = version.get(this.projectHook);
            project.delete(); */
            final Organism organism = version.get(this.organismHook);
            version.delete(organism.getTargets());
            organism.delete();
            final Person person = version.get(this.personHook);
            version.delete(person.getUsers());
            person.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.servlet.spot.SpotNewTarget#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * .
     * 
     * @throws ServletException
     * @throws IOException
     * @throws ConstraintException
     * @throws AbortedException
     * @throws AccessException
     * @throws ServletException
     */
    public final void testDoPostNoProteinName() throws IOException, AbortedException, ConstraintException,
        AccessException, ServletException {
        try {
            this.servlet.doPost(this.request, this.response);
            Assert.fail("completed without require name");
        } catch (final ServletException e) {
            Assert.assertTrue(e.getMessage().contains("name - should not be null"));
        }
    }

    public final void testDoPost() throws IOException, AbortedException, ConstraintException,
        AccessException, ServletException {
        this.setParameter("proteinName", SpotNewTargetTest.PROTEIN_NAME);
        this.setParameter("targetId", SpotNewTargetTest.TARGET_NAME);
        this.setParameter("comments", SpotNewTargetTest.COMMENTS);
        this.servlet.doPost(this.request, this.response);

        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            // test it was done OK
            //final Project project = version.get(this.projectHook);
            //Assert.assertEquals(1, project.getTargets().size());
            final Target target = this.getTarget(version);
            Assert.assertEquals(SpotNewTargetTest.TARGET_NAME, target.getName());
            Assert.assertEquals(SpotNewTargetTest.COMMENTS, target.getWhyChosen());
            final Molecule protein = target.getProtein();
            Assert.assertEquals(SpotNewTargetTest.PROTEIN_NAME, protein.getName());
            //Assert.assertNotNull(target.getCreatorPerson());
            Assert.assertNotNull(target.getSpecies());

            // tidy up
            version.delete(target.getNucleicAcids());
            version.delete(target.getExternalDbLinks());
            target.delete();
            protein.delete();
            version.flush();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    private Target getTarget(final WritableVersion version) {
        final Organism species = version.get(this.organismHook);
        return version.findFirst(Target.class, Target.PROP_SPECIES, species);
    }

    public final void testOwner() throws IOException, AbortedException, ConstraintException, AccessException,
        ServletException {

        String ownerHook = null;
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            ownerHook = new LabNotebook(version, SpotNewTargetTest.PROJECT_NAME).get_Hook();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        this.setParameter("proteinName", SpotNewTargetTest.PROTEIN_NAME + "own");
        this.setParameter("targetId", SpotNewTargetTest.TARGET_NAME);
        this.setParameter("comments", SpotNewTargetTest.COMMENTS);
        this.setParameter("accessId", ownerHook);
        this.servlet.doPost(this.request, this.response);

        version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            //  test it was done OK
            //final Project project = version.get(this.projectHook);
            //Assert.assertEquals(1, project.getTargets().size());
            final Target target = this.getTarget(version);
            Assert.assertNotNull(target.getAccess());
            //Assert.assertEquals(project.getShortName(), target.getAccess().getName());

            // tidy up
            version.delete(target.getNucleicAcids());
            version.delete(target.getExternalDbLinks());
            final Molecule protein = target.getProtein();
            target.delete();
            protein.delete();
            version.flush();
            final LabNotebook owner = version.get(ownerHook);
            final Collection<LabBookEntry> owned = owner.getLabBookEntries();
            if (!owned.isEmpty()) {
                final LabBookEntry entry = owned.iterator().next();
                Assert.fail("must delete: " + entry.toString());
            }
            owner.delete();
            version.commit();
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * @param string
     * @param string2
     */
    private void setParameter(final String name, final String value) {
        this.parms.put(name, new String[] { value });
    }

}
