package org.pimslims.dao;

import org.pimslims.access.Access;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.AbstractTestWritableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.people.Group;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.Target;

public class WritableVersionImplTester extends AbstractTestWritableVersion {

    public WritableVersionImplTester(String methodName) {
        super(ModelImpl.getModel(), methodName, Organisation.class.getName(), OrganisationTester.ATTRIBUTES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestWritableVersion#testAbort()
     */
    @Override
    public void testAbort() {
        super.testAbort();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.metamodel.AbstractTestWritableVersion#testFlush()
     */
    @Override
    public void testFlush() {
        super.testFlush();
    }

    public void testTwoSessions() {
        ReadableVersion version1 = (ReadableVersion) getWritableVersion(Access.ADMINISTRATOR);
        ReadableVersion version2 = (ReadableVersion) getWritableVersion(Access.ADMINISTRATOR);
        try {
            assertFalse(version1.getSession() == version2.getSession());
        } finally {
            version1.abort();
            version2.abort();
        }
    }

    public void testTwoAssociates() throws ConstraintException, AbortedException {
        WritableVersionImpl version1 = (WritableVersionImpl) getWritableVersion(Access.ADMINISTRATOR);
        WritableVersionImpl version2 = (WritableVersionImpl) getWritableVersion(Access.ADMINISTRATOR);
        try {
            Organisation org1 = version1.get(this.hook);
            Group g1 = new Group(version1, org1);
            assert (org1.getGroups().contains(g1));

            Organisation org2 = version2.get(this.hook);
            Group g2 = new Group(version2, org2);
            assert (org2.getGroups().contains(g2));

            g1.flush();
            version2.commit();

        } finally {
            if (!version1.isCompleted()) {
                version1.abort();
            }
            if (!version1.isCompleted()) {
                version2.abort();
            }
        }
    }

    public void testGetUniqueID() {
        wv = getWV();
        try {
            Organisation org1 = create(Organisation.class);
            Long ID = wv.getUniqueID();
            Long ID2 = wv.getUniqueID();
            assertTrue(ID2 > ID);
            assertTrue(ID2 > org1.getDbId());
            Organisation org2 = create(Organisation.class);
            assertTrue(ID2 < org2.getDbId());
            wv.commit();
        } catch (ConstraintException e) {
            fail(e.getMessage());
        } catch (AbortedException e) {
            fail(e.getMessage());
        } catch (AccessException e) {

            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    public void test2OwnerWritable() throws AccessException, ConstraintException, AbortedException {
        wv = getWV();
        String userName;
        String owner1;
        //String owner2;
        try {
            User user1 = create(User.class);
            owner1 =
                user1.getUserGroups().iterator().next().getPermissions().iterator().next().getLabNotebook()
                    .getName();
            User user2 = create(User.class);
            /* owner2 =
                user2.getUserGroups().iterator().next().getPermissions().iterator().next().getLabNotebook()
                    .getName(); */
            user2.getUserGroups().iterator().next().addMemberUser(user1);
            //user1 belongs to 2 usergroups which have 2 sets of permissions
            userName = user1.getName();
            wv.commit();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

        wv = getWV(userName);
        try {
            LabNotebook owner = wv.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, owner1);
            assertNotNull(owner);
            //try create
            Target t1 = create(Target.class, LabBookEntry.PROP_ACCESS, owner);
            assertNotNull(t1);
            assertEquals(owner, t1.getAccess());
            String hook = t1.get_Hook();
            //try read
            assertNotNull(wv.get(hook));
            //try update
            t1.setDetails("details" + System.currentTimeMillis());
            //try delete
            t1.delete();
            assertNull(wv.get(hook));
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public void testDataOwnerCheckProperty() throws ConstraintException, AccessException {
        super.model.setForceDataOwnerCheck(true);
        wv = getWV();
        try {
            ModelObject object = create(ExperimentType.class);
            assertNotNull(object);
            object = create(Target.class);
            fail("data owner not set");
        } catch (RuntimeException e) {
            //this is correct
        } finally {
            super.model.setForceDataOwnerCheck(false);
            wv.abort();
        }

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(WritableVersionImplTester.class);
    }

}
