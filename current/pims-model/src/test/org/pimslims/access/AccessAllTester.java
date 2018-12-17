/**
 * 
 */
package org.pimslims.access;

import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.AbstractTestAccess;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.people.Organisation;

/**
 * @author cm65
 * 
 */
public class AccessAllTester extends junit.framework.TestCase {

    private static final String USERNAME = org.pimslims.test.POJOFactory.USERNAME;

    /**
     * For creating test user, reused in other test classes
     */
    public static final Map<String, Object> USER_MAP = new java.util.HashMap<String, Object>(
        org.pimslims.test.POJOFactory.getAttrUser());

    private String userHook = null;

    AbstractModel model;

    @Override
    protected void setUp() {
        this.model = ModelImpl.getModel();
        AccessImpl access = (AccessImpl) this.model.getAccess();
        // owner =
        // model.getMetaClass(org.pimslims.access.Owner.class.getName());
        // assertNotNull("owner", owner);
        MetaClass user = access.getUser();
        assertNotNull("metaclass for user", user);

        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            // UC-new-user
            ModelObject u = wv.create(user.getJavaClass(), USER_MAP);
            this.userHook = u.get_Hook();

            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (final RuntimeException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        WritableVersion wv = this.model.getWritableVersion(AbstractModel.SUPERUSER);
        try {

            if (null != this.userHook) {
                ModelObject u = wv.get(this.userHook);
                if (null != u) {
                    u.delete();
                }
            }
            wv.commit();
        } catch (final ModelException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } catch (final RuntimeException ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }
        super.tearDown();
        //System.gc();
        //System.gc();

    }

    public void testWorldWritable() throws AccessException, ConstraintException, AbortedException {
        WritableVersion wv = null;
        try {
            wv = this.model.getWritableVersion("nonesuch");
            fail("Writable version with invalid user");
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            if (null != wv) {
                wv.abort();
            }
        }

        wv = this.model.getWritableVersion(USERNAME);
        assertEquals("transaction username", USERNAME, wv.getUsername());
        //MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        try {
            wv.create(Organisation.class, OrganisationTester.ATTRIBUTES);
            fail("User without permission should not able to create! ");

        } catch (AccessException ex) {
            //this is correct
        } catch (ConstraintException e) {
            throw new RuntimeException(e);
        } finally {
            wv.abort();
        }

        AbstractTestAccess.suiteTearDown(this.model);

    }

}
