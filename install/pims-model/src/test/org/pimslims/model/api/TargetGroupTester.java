/**
 * 
 */
package org.pimslims.model.api;

import java.util.HashSet;
import java.util.Map;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.metamodel.TestAMetaClass;
import org.pimslims.model.target.TargetGroup;

/**
 * @author cm65
 * 
 */
public class TargetGroupTester extends TestAMetaClass {

    private static final Map<String, Object> ATTRIBUTES = new java.util.HashMap<String, Object>();
    static {
        ATTRIBUTES.put(org.pimslims.model.target.TargetGroup.PROP_NAME, "test"
            + System.currentTimeMillis());
    }

    private static final Map<String, Object> ATTRIBUTES2 = new java.util.HashMap<String, Object>();
    static {
        ATTRIBUTES2.put(org.pimslims.model.target.TargetGroup.PROP_NAME, "testTwo"
            + System.currentTimeMillis());
    }

    private static final Map<String, Object> ATTRIBUTES3 = new java.util.HashMap<String, Object>();
    static {
        ATTRIBUTES3.put(org.pimslims.model.target.TargetGroup.PROP_NAME, "testThree"
            + System.currentTimeMillis());
    }

    /**
     * 
     * @param methodName
     */
    public TargetGroupTester(String methodName) {
        super(org.pimslims.model.target.TargetGroup.class.getName(), ModelImpl.getModel(), methodName,
            ATTRIBUTES);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.TestAMetaClass#testVersion()
     */
    @Override
    public void testVersion() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject testObject = doTestVersion(wv);
            TargetGroup group = (TargetGroup) testObject;

            // test association
            group.addSubTargetGroup(group);
            assertEquals("sub", 1, group.getSubTargetGroups().size());
            assertNotNull("reverse association", group.getTargetGroup());

            // now test creating with association,  API alone
            ATTRIBUTES2.put("subTargetGroups", new HashSet<TargetGroup>(java.util.Collections
                .singletonList(group)));
            TargetGroup group2 = new TargetGroup(wv, ATTRIBUTES2);
            // LATER assertNull("super", group2.getTargetGroup()); // throws
            // exception

            // now test creating with association, PIMS API
            ATTRIBUTES3.put("targetGroup", testObject);
            TargetGroup testObject3 = (TargetGroup) wv.create(javaClass, ATTRIBUTES3);
            assertNotNull("super", testObject3.getTargetGroup());
            assertTrue("sub", testObject3.getTargetGroup().getSubTargetGroups().contains(testObject3));

            // also test creating with subprojects
            // LATER assertEquals("subprojects", 1,
            // project2.getSubProjects().size());

            group2.delete();
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

    public void testMetaData() {
        MetaAttribute details = metaClass.getAttribute("details");
        assertEquals("long: " + details.getLength(), -1, details.getLength());
    }

}
