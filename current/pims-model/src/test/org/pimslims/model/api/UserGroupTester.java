/**
 * @author cm65
 * @version: 0.2
 */
package org.pimslims.model.api;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.metamodel.TestAMetaClass;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.accessControl.UserGroup;

/**
 * 
 * @author Chris Morris
 * @date May 2006
 */
public class UserGroupTester extends TestAMetaClass {

    /**
     * Value for creating a UserGroup
     */
    private static final Map ATTRIBUTES = new HashMap();
    static {
        ATTRIBUTES.put("name", "test" + System.currentTimeMillis());
    }

    /**
     * Constructor
     */
    public UserGroupTester(String methodName) {
        super(UserGroup.class.getName(), ModelImpl.getModel(), methodName, ATTRIBUTES);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#testHelpText()
     */
    @Override
    public void testHelpText() {
        super.testHelpText();
    }

    /**
     * LATER fix this test case - it leaves the DB connection bad
     */
    public void xtestNameClash() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            doTestVersion(wv);
            doTestVersion(wv);
            fail("created two groups with same name");
        } catch (ModelException ex) {
            wv.abort();
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            } // not testing persistence here
        }
    }

    /**
     * Normal test, plus add/remove
     */
    @Override
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject testObject = doTestVersion(wv);
            doTestAddRemove(testObject);
            wv.delete(testObject);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    /**
     * Test for defect237 when one object is removed, others may be removed too
     * 
     * @param testObject
     * @throws AccessException
     * @throws ConstraintException
     */
    private void doTestAddRemove(ModelObject testObject) throws AccessException, ConstraintException {
        WritableVersion version = (WritableVersion) testObject.get_Version();
        Map userAttr = new HashMap();
        userAttr.put("name", "one" + System.currentTimeMillis());
        ModelObject user1 = version.create(User.class, userAttr);
        userAttr.put("name", "two" + System.currentTimeMillis());
        ModelObject user2 = version.create(User.class, userAttr);

        testObject.add("memberUsers", user1);
        assertEquals("added 1", 1, testObject.get("memberUsers").size());
        testObject.add("memberUsers", user2);
        assertEquals("added 2", 2, testObject.get("memberUsers").size());
        testObject.remove("memberUsers", user1);
        assertEquals("removed 1", 1, testObject.get("memberUsers").size()); // defect237
        testObject.remove("memberUsers", user2);
        assertEquals("removed 2", 0, testObject.get("memberUsers").size());

        // tidy up
        version.delete(user1);
        version.delete(user2);
    }

}
