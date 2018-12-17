/*
 * Created on 09-Dec-2004 @author: Chris Morris
 */
package org.pimslims.model.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaElement;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * Tests generated class for reagent source
 */
public class TargetMetaClassTester extends org.pimslims.metamodel.TestAMetaClass {

    /**
     * Values for creating the object target
     */
    public static final HashMap<String, Object> ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrTarget());

    /**
     * Values for creating a protein
     */
    public static final HashMap<String, Object> PROTEIN_ATTRIBUTES = new HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrProtein());

    /**
     * Identifies the protein - this is a required association
     */
    private String proteinHook = null;

    /**
     * 
     */
    public TargetMetaClassTester(final String methodName) {
        super(org.pimslims.model.target.Target.class.getName(), ModelImpl.getModel(), methodName, ATTRIBUTES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        proteinHook = setUpAssociate(Molecule.class, PROTEIN_ATTRIBUTES);
    }

    @Override
    protected void tearDown() {
        super.tearDown();
        org.pimslims.test.POJOFactory.removeTestingRecords(this.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ModelObject doTestVersion(WritableVersion wv) throws ConstraintException, AccessException {
        attributes.put(org.pimslims.model.target.Target.PROP_PROTEIN, wv.get(proteinHook));

        // create
        Target target = new Target(wv, attributes);
        ResearchObjectiveElement blueprintComponent =
            org.pimslims.test.POJOFactory.createBlueprintComponent(wv);
        blueprintComponent.setTarget(target);
        org.pimslims.test.POJOFactory.createMilestone(wv, target);

        // isInstance
        assertTrue("isInstance", metaClass.isInstance(target));
        assertTrue("isInstance", model.getMetaClass(org.pimslims.model.target.Target.class.getName())
            .isInstance(target));
        assertFalse("isInstance", model.getMetaClass(org.pimslims.model.molecule.Molecule.class.getName())
            .isInstance(target));

        //doTestTargetDbRef(wv, target);
        doTestBlueprintComponent(wv, target);
        java.util.Collection statuses = target.getMilestones();
        assertTrue("has status", 1 == statuses.size());
        java.util.Collection blueprintComponents = target.getResearchObjectiveElements();
        assertTrue("has blueprint", 1 == blueprintComponents.size());
        return target;
    }

    /**
     * @param wv
     * @param testObject
     */
    private void doTestBlueprintComponent(WritableVersion wv, ModelObject testObject) {
        Target target = (Target) testObject;
        assertFalse("some blueprint components", target.getResearchObjectiveElements().isEmpty());
        try {
            ResearchObjectiveElement bc = target.getResearchObjectiveElements().iterator().next();
            assertNotNull("has blueprint", bc);
            bc.setApproxBeginSeqId(1);
            assertEquals("begin", 1, bc.getApproxBeginSeqId().intValue());

            ModelObject component = bc;
            component.set_Value("approxBeginSeqId", new Integer(2));
            assertEquals("begin again", new Integer(2), component.get_Value("approxBeginSeqId"));

        } catch (AccessException e) {
            fail(e.getLocalizedMessage());
        } catch (ConstraintException e) {
            fail(e.getLocalizedMessage());
        }

    }

    /**
     * @param wv
     * @param testObject
     */
    public void testStatus() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            String statusName = "Selected" + System.currentTimeMillis();
            attributes.put(org.pimslims.model.target.Target.PROP_PROTEIN, wv.get(proteinHook));
            Target target = new Target(wv, attributes);
            org.pimslims.test.POJOFactory.createMilestone(wv, target,
                org.pimslims.test.POJOFactory.createStatus(wv, statusName));
            Set<Milestone> statusSet = target.getMilestones();
            assertTrue("should not be empty", statusSet.size() > 0);
            assertTrue("should have default status", statusSet.iterator().hasNext());
            Milestone status = statusSet.iterator().next();
            assertEquals(statusName, status.getStatus().getName());
            /*
             * assertEquals("getCurrentTargetStatusCode","Selected",TargetUtility.getCurrentTargetStatusCode(target));
             * TargetUtility.newTargetStatus(wv, target, "testTargetStatus");
             * assertEquals("getCurrentTargetStatusCode","testTargetStatus",TargetUtility.getCurrentTargetStatusCode(target));
             */
            System.out.println("TargetTester.testStatus: " + target.getName());
            assertEquals(target.getName() + "-" + status.getStatus().getName(), status.get_Name());
            target.delete();
        } catch (ConstraintException e) {
            e.printStackTrace();
            fail(e.getLocalizedMessage());
        } catch (AccessException e) {
            fail(e.getLocalizedMessage());
        } finally {
            wv.abort();
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            attributes.put(org.pimslims.model.target.Target.PROP_PROTEIN, wv.get(proteinHook));
            // create again, with no status
            attributes.put(org.pimslims.model.target.Target.PROP_MILESTONES, Collections.EMPTY_SET);
            Target target2 = new Target(wv, attributes);
            assertTrue("created with no status", target2.getMilestones().size() == 0);

        } catch (ConstraintException e) {
            fail(e.getLocalizedMessage());
        } finally {
            wv.abort();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doTestFindAll(WritableVersion wv) throws ConstraintException, AccessException {
        ATTRIBUTES.put(org.pimslims.model.target.Target.PROP_PROTEIN, wv.get(proteinHook));
        // create
        Target target = new Target(wv, ATTRIBUTES);
        String hook = target.get_Hook();
        assertNotNull("created", wv.get(hook));

        try {
            // get all
            assertTrue("getAll", wv.getAll(metaClass.getJavaClass(), 0, 10).contains(target));
        } catch (final UnsupportedOperationException ex1) {
            fail(ex1.getMessage());
        }
        assertTrue("created and found", wv.findAll(metaClass.getJavaClass(), ATTRIBUTES).contains(target));

        wv.delete(target);
    }

    public void testMetaData() {
        MetaClass milestone = model.getMetaClass(org.pimslims.model.target.Milestone.class.getName());
        assertFalse("class help", milestone.getHelpText().equals(""));
        MetaElement status = milestone.getProperty(Milestone.PROP_STATUS);
        assertFalse("code help", status.getHelpText().equals(""));

        MetaRole access = milestone.getMetaRole("access");
        assertTrue("hidden", access.isHidden());
        assertNotNull("attachments", metaClass.getMetaRole("attachments"));
        assertNotNull("attachments", metaClass.getMetaRoles().get("attachments"));

        // LATER assertNotNull("blueprint",
        // metaClass.getMetaRole("blueprintComponents") );
    }

    /**
     * test the methods that need access to a model object
     */
    @Override
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject testObject = doTestVersion(wv);
            wv.delete(testObject);
        } catch (ModelException ex) {
            ex.printStackTrace();
            fail(ex.getClass().getName() + ": " + ex.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testComponent() {
        MetaClass bc = model.getMetaClass(ResearchObjectiveElement.class.getName());
        MetaRole bp = bc.getMetaRole(ResearchObjectiveElement.PROP_RESEARCHOBJECTIVE);
        assertTrue("child", ((MetaRoleImpl) bp).isParentRole());
        assertFalse("immutable", bp.isChangeable());
    }
}
