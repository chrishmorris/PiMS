/*
 * Created on 13 Sept 2004 @author: Chris Morris
 */
package org.pimslims.metamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.access.AccessImpl;
import org.pimslims.access.PIMSAccess;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.accessControl.Permission;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;
import org.pimslims.model.core.LabNotebook;

/**
 * Test cases for access rights. For use cases, see
 * http://www.mole.ac.uk/lims/project/use-case-suite.html#access
 * 
 * These tests depend on MetaRole. You should fix errors found by TestMetaRole first. These tests use
 * WritableVersion throughout, to avoid a dependency on ReadableVersion.
 * 
 * Note that there are no test cases for changing the owner of an object. It is not clear that this is
 * required. If it is needed, there seem to be two options: (1) administrator only (2) anyone with delete
 * permission for old owner and create permissions for new owner.
 * 
 * @version 0.1
 */
public abstract class AbstractTestAccess extends junit.framework.TestCase {

    /**
     * e.g. "name", the attribute which has a uniqueness constraint, or null if none does.
     */
    protected final String key;

    protected static final String OWNER = "testOwner" + System.currentTimeMillis();

    /**
     * The access strategy being tested
     */
    protected AccessImpl access;

    /**
     * The model in use
     */
    protected final AbstractModel model;

    /**
     * Model type to create when testing
     */
    protected final MetaClass metaClass;

    /**
     * name => value for attributes when creating
     */
    protected final Map<String, Object> attributes;

    /**
     * base model type
     */
    protected final MetaClass modelObject;

    /**
     * model type for data ownership
     */
    protected final MetaClass owner;

    /**
     * model type for users
     */
    protected final MetaClass user;

    /**
     * Role for adding users to groups
     */
    protected static final String MEMBER_ROLE = UserGroup.PROP_MEMBERUSERS;

    /**
     * User name for testing with
     */
    public static final String USERNAME = "TestUser" + System.currentTimeMillis();

    /**
     * For creating test owner
     */
    public static final Map<String, Object> OWNER_MAP = new java.util.HashMap<String, Object>();
    static {
        OWNER_MAP.put("name", OWNER);
        OWNER_MAP.put("details", "just for testing");
    }

    /**
     * For creating test user, reused in other test classes
     */
    public static final Map<String, Object> USER_MAP = new java.util.HashMap<String, Object>();
    static {
        USER_MAP.put("name", USERNAME);
    }

    /**
     * model type for user groups
     */
    protected final MetaClass userGroup;

    public static final String GROUP_NAME = "testGroup" + System.currentTimeMillis();

    /**
     * For creating test group, reused in other test cases
     */
    public static final Map<String, Object> GROUP_MAP = new java.util.HashMap<String, Object>();
    static {
        GROUP_MAP.put("name", GROUP_NAME);
    }

    /**
     * For creating a group leader, reused in other test cases
     */
    public static final Map<String, Object> LEADER_MAP = new java.util.HashMap<String, Object>();
    static {
        LEADER_MAP.put("leader", Boolean.TRUE);
    }

    /**
     * Model type for access rights
     */
    protected final MetaClass permission;

    /**
     * hook for test model object, owne by test user
     */
    protected String objectHook;

    /**
     * hook for test reference data
     */
    protected String refHook;

    /**
     * hook for test user identity
     */
    protected static String userHook;

    /**
     * hook for test user group
     */
    protected static String groupHook;

    /**
     * hook for test data owner
     */
    protected static String ownerHook;

    /**
     * Subclasses must create a model and call this constructor.
     * 
     * @param model model to test
     * @param methodName name of test
     * @param metaClassName an isntance of this type will be created
     * @param attributes names and values of attributes when creating an instance
     */
    protected AbstractTestAccess(final AbstractModel model, final String methodName,
        final String metaClassName, final Map<String, Object> attributes, final String key) {
        super(methodName);
        this.model = model;
        modelObject = model.getRootMetaClass();
        access = (AccessImpl) model.getAccess();
        // owner =
        // model.getMetaClass(org.pimslims.access.Owner.class.getName());
        // assertNotNull("owner", owner);
        user = access.getUser();
        assertNotNull("metaclass for user", user);
        owner = access.getOwner();
        userGroup = access.getUserGroup();
        permission = access.getPermission();
        this.key = key;
        metaClass = model.getMetaClass(metaClassName);
        this.attributes = attributes;
    }

    public static void suiteSetUp(AbstractModel model) {
        AccessImpl access = (AccessImpl) model.getAccess();
        MetaClass user = access.getUser();
        assertNotNull("metaclass for user", user);
        MetaClass owner = access.getOwner();
        MetaClass userGroup = access.getUserGroup();
        //MetaClass permission = access.getPermission();

        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            // create the test owner if necessary
            // UC-new-access
            LabNotebook testOwner = null;

            testOwner = wv.create(Access.REFERENCE, owner.getJavaClass(), OWNER_MAP);
            System.out.println("Created owner: " + OWNER_MAP.get("name"));

            ownerHook = testOwner.get_Hook();
            assertNotNull("testOwner", testOwner);
            assertEquals("owner name", OWNER, testOwner.get_Value("name"));
            assertNotNull("owner", wv.getOwner(OWNER));
            ModelObject ownerObject = wv.getOwner(OWNER);
            assertNotNull("owner", ownerObject);

            // UC-new-user
            User testUser = null;
            testUser = wv.create(user.getJavaClass(), USER_MAP);
            userHook = testUser.get_Hook();

            // UC-new-group
            UserGroup testGroup = null;

            testGroup = wv.create(userGroup.getJavaClass(), GROUP_MAP);

            groupHook = testGroup.get_Hook();

            // UC-add-user
            testGroup.add(MEMBER_ROLE, testUser);
            assertTrue("in group", testUser.isAssociated("userGroups", testGroup));
            assertTrue("in group", testGroup.isAssociated(MEMBER_ROLE, testUser));

            // UC-add-permission
            //int numPermissions = wv.getAll(permission.getJavaClass()).size();
            addPermission(wv, testGroup, PIMSAccess.CREATE);
            // TODO assertEquals("one more permission", numPermissions+1,
            // wv.getAll(permission).size());
            addPermission(wv, testGroup, PIMSAccess.READ);
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
                System.out.println("suite set up failed");
            }
        }
    }

    protected static void addPermission(WritableVersion wv, ModelObject group, String opType)
        throws AccessException, ConstraintException {
        ModelObject owner = wv.getOwner(OWNER);
        Map<String, Object> a = new HashMap<String, Object>();
        a.put("accessObject", owner);
        a.put("userGroup", group);
        a.put("opType", opType);

        wv.create(Permission.class, a);

    }

    public static void suiteTearDown(AbstractModel model) {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("cant get transaction", wv);
        try {
            ModelObject testUser = null;
            if (null != userHook) {
                testUser = wv.get(userHook);
            }
            ModelObject testGroup = null;
            if (null != groupHook) {
                testGroup = wv.get(groupHook);
            }
            /*
             * ModelObject testOwner = null; if (null!=ownerHook) { testOwner = wv.get(ownerHook); }
             */

            // remove the permission objects
            Map<String, Object> map = new java.util.HashMap<String, Object>();
            map.put(Permission.PROP_USERGROUP, testGroup);
            java.util.Collection<Permission> ps = wv.findAll(Permission.class, map);
            wv.delete(ps);

            // clean up
            // note that there is no UI for deleting these objects
            // - in normal use deleting them can cause inconsistencies.
            if (null != testUser) {
                wv.delete(testUser);
            }
            if (null != testGroup) {
                wv.delete(testGroup);
            }
            // TODO if (null!=testOwner) {wv.delete(testOwner);}
            wv.commit();
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (ConstraintException e) {
            throw new RuntimeException(e);
        } catch (AbortedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("cant get transaction", wv);
        try {

            // create the objects we need
            attributes.put(key, "reference" + new java.util.Date().getTime());
            ModelObject refObject = wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);
            assertEquals("reference", Access.REFERENCE, refObject.get_Owner());
            refHook = refObject.get_Hook();

            assertNotNull("owner", wv.getOwner(OWNER));
            attributes.put(key, "owned" + new java.util.Date().getTime());
            ModelObject testObject = wv.create(OWNER, metaClass.getJavaClass(), attributes);
            assertEquals("owner", OWNER, testObject.get_Owner());
            objectHook = testObject.get_Hook();

            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("cant get transaction", wv);
        try {
            ModelObject testObject = wv.get(objectHook);
            if (testObject != null) {
                wv.delete(testObject);
            }
            ModelObject refObject = wv.get(refHook);
            if (refObject != null) {
                wv.delete(refObject);
            }

            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * Test features required for version 0.3 With these features we have an API and can develop support for
     * laboratory operations
     */
    public void testV0_3() {

        WritableVersion wv = model.getWritableVersion(USERNAME);
        assertNotNull(wv);
        try {
            assertNotNull("read", wv.get(this.objectHook)); // check testUser
            // can now read
        } finally {
            wv.abort();
        }

        wv = model.getWritableVersion(USERNAME);
        assertNotNull(wv);
        try {
            attributes.put(key, System.currentTimeMillis() + "test");
            ModelObject object2 = wv.create( // check testUser can now create
                metaClass.getJavaClass(), attributes);
            assertEquals("owner", OWNER, object2.get_Owner());

            assertNotNull("owner", wv.getOwner(OWNER));
            assertTrue("may create: " + USERNAME + " " + OWNER,
                wv.mayCreate(OWNER, metaClass, new java.util.HashMap()));
            assertEquals("default", OWNER, wv.getDefaultOwner(metaClass, null));

            attributes.put(key, "testV0_3_" + System.currentTimeMillis());
            ModelObject object = wv.create( // check testUser can now create
                OWNER, metaClass.getJavaClass(), attributes);
            assertEquals("owner", OWNER, object.get_Owner());

        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (final Exception ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }

            cause.printStackTrace();
            fail(cause.getMessage());
        } finally {
            wv.abort();
        }

        // test administrator
        wv = this.model.getTestVersion();
        try {
            wv.get(objectHook);
            assertNotNull("read", objectHook);
            assertNotNull("owner", wv.getOwner(OWNER));
            assertTrue("may create: " + Access.ADMINISTRATOR + " " + OWNER,
                wv.mayCreate(OWNER, metaClass, new java.util.HashMap()));
            attributes.put(key, "testAdmin" + System.currentTimeMillis());
            ModelObject mo = wv.create( // check admin can now create
                metaClass.getJavaClass(), attributes);
            mo.delete();
            attributes.put(key, "testAdmin1" + System.currentTimeMillis());
            mo = wv.create( // check admin can now create
                Access.REFERENCE, metaClass.getJavaClass(), attributes);
            mo.delete();
            attributes.put(key, "testAdmin2" + System.currentTimeMillis());
            ModelObject object = wv.create( // check admin can now create
                OWNER, metaClass.getJavaClass(), attributes);
            assertEquals("admin can create anything", OWNER, object.get_Owner());
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    }

    /**
     * Test can't update or delete With these features we have functioning access control
     * 
     * @throws AccessException
     */
    public void testV0_2() throws AccessException {
        doTestDenied(refHook);
        doTestDenied(objectHook);

        // check can't create object with nonexistent owner
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        assertNotNull("cant get transaction", wv);
        try {
            ModelObject bad = wv.create("nonesuch", metaClass.getJavaClass(), attributes);
            assertNull("no such owner", bad);
        } catch (AssertionError e) {
            // that's as it should be
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } finally {
            wv.abort();
        }

        // check ordinary user can't create reference data
        wv = model.getWritableVersion(USERNAME);
        try {
            wv.create(Access.REFERENCE, metaClass.getJavaClass(), attributes);
            fail("Ordinary user created reference data");
        } catch (AccessException e) {
            // that's as it should be;
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } finally {
            wv.abort();
        }
    }

    /**
     * Test denial
     */
    public void doTestDenied(String hook) {
        WritableVersion wv;
        // TODO test create
        // TODO test changing an association

        // check testUser cant delete
        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject testObject = wv.get(hook);
            assertNotNull(testObject.get_Version());
            assertFalse("may not delete", wv.mayDelete(testObject));
            wv.delete(testObject);
            wv.commit();
            fail("Shouldn't be able to delete");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject testObject = wv.get(hook);
            assertNotNull(USERNAME + " can read " + hook, testObject);
            assertFalse("may not update", wv.mayUpdate(testObject));
            testObject.set_Value("name", "changed");
            wv.commit();
            fail("Shouldn't be able to update");
        } catch (final AccessException ex) {
            // that's fine
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } catch (AbortedException e) {
            fail(e.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * Features required later: ordinary user cant create rights
     */
    public void testLaterCantExtend() {

        WritableVersion wv = model.getWritableVersion(USERNAME);
        try {
            // UC-new-user.A1 only admin or group leader
            USER_MAP.put("name", "bad" + System.currentTimeMillis());
            Map<String, Object> a = new java.util.HashMap<String, Object>(USER_MAP);
            a.put("name", "notByAdmin" + new java.util.Date().getTime());
            wv.create(user.getJavaClass(), a);
            fail("Only admin can create a new user");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            wv.abort();
        }

        // UC-add-permission.A1 only admin
        wv = model.getWritableVersion(USERNAME);
        try {
            wv.create(permission.getJavaClass(), new java.util.HashMap<String, Object>());
            wv.commit();
            fail("Only admin can create a new permission");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // UC-new-group.A1 request not from admin
        wv = model.getWritableVersion(USERNAME);
        try {
            wv.create(userGroup.getJavaClass(), GROUP_MAP);
            wv.commit();
            fail("Only admin can create a new group");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // UC-new-owner.A1 request not from admin
        wv = model.getWritableVersion(USERNAME);
        try {
            wv.create(owner.getJavaClass(), OWNER_MAP);
            wv.commit();
            fail("Only admin can create new owner");
        } catch (final AccessException ex) {
            // that's fine
            wv.abort();
        } catch (final ModelException ex) {
            fail(ex.toString());
            wv.abort();
        } // */

        // UC-add-user.A1 only admin or group leader can add
        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject testUser = wv.get(userHook);
            ModelObject testGroup = wv.get(groupHook);
            testUser.add(MEMBER_ROLE, testGroup);

            wv.commit();
            fail("Only admin or group leader can add a user to a group");
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    /**
     * also check can't make a user etc that is not owned by admin
     */
    public void testLaterCantExtend2() {

        WritableVersion wv = model.getWritableVersion(USERNAME);
        try {
            // UC-new-user.A1 only admin or group leader
            USER_MAP.put("name", "bad" + System.currentTimeMillis());
            Map<String, Object> a = new java.util.HashMap<String, Object>(USER_MAP);
            a.put("name", "notByAdmin2" + new java.util.Date().getTime());
            wv.create(OWNER, user.getJavaClass(), a);
            fail("Only admin can create a new user");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            wv.abort();
        }
    }

    /**
     * Desired features, to make administration easier.
     */
    public void testDesired() {
        WritableVersion wv;
        // UC-remove-permission
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            assertNotNull(wv.get(objectHook));
            UserGroup testGroup = (UserGroup) wv.get(groupHook);

            // remove the permission objects
            Map<String, Object> map = new java.util.HashMap<String, Object>();
            map.put(Permission.PROP_USERGROUP, testGroup);
            Collection<ModelObject> ps = wv.findAll(permission.getJavaClass(), map);
            assertTrue("found", 0 < ps.size());
            // can't testGroup.remove(UserGroup.PROP_PERMISSIONS, ps);
            wv.delete(ps);
            assertEquals("delete removes", 0, testGroup.getPermissions().size());

            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        /* now confirm that test user cant read
         * This now fails because the user has no read permission
         * TODO add another lab notebook to revive this test
        wv = model.getWritableVersion(USERNAME);
        try {
            assertNull("cant read", wv.get(objectHook));
        } finally {
            wv.abort();
        }
        wv = model.getWritableVersion(USERNAME + "");
        try {
            Collection<ModelObject> objects = wv.getAll(metaClass.getJavaClass(), 0, 10);
            for (Iterator iter = objects.iterator(); iter.hasNext();) {
                ModelObject element = (ModelObject) iter.next();
                assertFalse("found unreadable", objectHook.equals(element.get_Hook()));
            }
        } finally {
            wv.abort();
        } */

        // UC-remove-create
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject testGroup = wv.get(groupHook);
            // restore permissions
            addPermission(wv, testGroup, PIMSAccess.CREATE);
            addPermission(wv, testGroup, PIMSAccess.READ);
            wv.delete(testGroup);
            wv.commit();
            //can delete a group which has permissions as group is the parent of permission and delete parent will delete children
        } catch (final ConstraintException ex) {
            // that's OK
        } catch (AccessException e) {
            fail(e.getLocalizedMessage());
        } catch (AbortedException e) {
            fail(e.getLocalizedMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            GROUP_MAP.put("name", "desired" + System.currentTimeMillis());
            ModelObject testGroup = wv.create(userGroup.getJavaClass(), GROUP_MAP);
            // restore permissions
            addPermission(wv, testGroup, PIMSAccess.CREATE);
            addPermission(wv, testGroup, PIMSAccess.READ);

            wv.commit();
            groupHook = testGroup.get_Hook();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // LATER UC-add-user group leader can add
        // LATER UC-new-access.A2, UC-new-group.A2 name already exists
        // LATER UC-remove-create alternative scenarios
    }

    /**
     * Test removing a user from a group
     */
    public void testDesiredRemove() {
        WritableVersion wv;
        // remove user from group
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject testUser = wv.get(userHook);
            ModelObject testGroup = wv.get(groupHook);

            // UC-remove-user
            testGroup.remove(MEMBER_ROLE, testUser);

            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        // now confirm that rights have been lost
        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject object = wv.get(objectHook); // check testUser can
            // now read
            assertNull("Shouldn't be able to read after removal", object);
        } finally {
            wv.abort();
        }

        // put user back in group for later tests
        wv = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ModelObject testUser = wv.get(userHook);
            ModelObject testGroup = wv.get(groupHook);
            testGroup.add(MEMBER_ROLE, testUser);
            wv.commit();
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /**
     * Desired feature: ordinary user can't remove rights
     */
    public void testDesiredDelete() {
        WritableVersion wv;

        wv = model.getWritableVersion(USERNAME);
        try {
            assertNotNull(groupHook);
            ModelObject testGroup = wv.get(groupHook);
            testGroup.add(MEMBER_ROLE, ModelObject.EMPTY_SET);
            wv.commit();
            fail("Shouldn't be able to remove test user from group");
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        // */
        // LATER must not be able to delete owners

        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject testUser = wv.get(userHook);
            wv.delete(testUser);

            wv.commit();
            fail("Shouldn't be able to delete test user");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        } // */

        wv = model.getWritableVersion(USERNAME);
        try {
            ModelObject testGroup = wv.get(groupHook);

            wv.delete(testGroup);
            wv.commit();
            fail("Shouldn't be able to delete testGroup");
        } catch (final AccessException ex) {
            // that's fine
        } catch (final ModelException ex) {
            fail(ex.toString());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        } // */

    }

    /**
     * Optional features, give the ability to extend the access control model.
     */
    public void testOptional() {
        // LATER UC-new-condition Installs code on the server to implement a
        // complex data access rule, called a Condition.
        // LATER UC-set-condition Set a Condition restricting access to data for
        // a particular Data Owner.

    }

}
