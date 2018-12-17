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
import org.pimslims.metamodel.ModelObject;
import org.pimslims.metamodel.TestAMetaClass;
import org.pimslims.model.target.Project;

/**
 * @author cm65
 * 
 */
public class ProjectTester extends TestAMetaClass {

    private static final Map ATTRIBUTES = new java.util.HashMap();
    static {
        ATTRIBUTES.put(Project.PROP_SHORTNAME, "test" + System.currentTimeMillis());
        ATTRIBUTES.put(Project.PROP_COMPLETENAME, "test" + System.currentTimeMillis());
    }

    private static final Map ATTRIBUTES2 = new java.util.HashMap();
    static {
        ATTRIBUTES2.put(Project.PROP_SHORTNAME, "testTwo" + System.currentTimeMillis());
        ATTRIBUTES2.put(Project.PROP_COMPLETENAME, "testTwo" + System.currentTimeMillis());
    }

    private static final Map ATTRIBUTES3 = new java.util.HashMap();
    static {
        ATTRIBUTES3.put(Project.PROP_SHORTNAME, "testThree" + System.currentTimeMillis());
        ATTRIBUTES3.put(Project.PROP_COMPLETENAME, "testThree" + System.currentTimeMillis());
    }

    /**
     * 
     * @param methodName
     */
    public ProjectTester(String methodName) {
        super(org.pimslims.model.target.Project.class.getName(), ModelImpl.getModel(), methodName, ATTRIBUTES);
    }

    public void testAddOthers() {
        WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);

        try {
            ModelObject testObject = doTestVersion(wv);
            Project project = (Project) testObject;
            ModelObject testObject2 = doTestVersion(wv);
            Project project2 = (Project) testObject2;
            ModelObject testObject3 = doTestVersion(wv);
            //Project project3 = (Project) testObject3;

            // test association
            assertEquals("no subprojects", 0, project.getSubProjects().size());
            assertNull("no superproject", project2.getProject());

            project.addSubProject(project2);

            assertEquals("1 subproject", 1, project.getSubProjects().size());
            assertNotNull("superproject", project2.getProject());

            wv.delete(project2);
            wv.delete(testObject3);
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
            Project project = (Project) testObject;

            // test association
            assertEquals("subprojects", 0, project.getSubProjects().size());
            project.addSubProject(project);
            // subProjects.add(project);
            // project.setSubProjects(subProjects);
            assertEquals("subprojects", 1, project.getSubProjects().size());
            assertNotNull("superproject", project.getProject());

            // now test creating with association,  API alone
            ATTRIBUTES2.put(Project.PROP_PROJECT, project);
            ATTRIBUTES2.put(Project.PROP_SUBPROJECTS,
                new HashSet<Project>(java.util.Collections.singletonList(project)));
            Project project2 = new Project(wv, ATTRIBUTES2);
            assertNotNull("superproject", project2.getProject());

            // now test creating with association, PIMS API
            ATTRIBUTES3.put(Project.PROP_PROJECT, testObject);
            ATTRIBUTES3.put(Project.PROP_SUBPROJECTS,
                new HashSet<Project>(java.util.Collections.singletonList((Project) testObject)));
            Project testObject3 = new Project(wv, ATTRIBUTES3);
            assertEquals("subprojects", 1, testObject3.get(Project.PROP_SUBPROJECTS).size());
            try {
                assertEquals("superproject", 1, testObject3.get(Project.PROP_PROJECT).size());
            } catch (RuntimeException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }

            // also test creating with subprojects
            // LATER assertEquals("subprojects", 1,
            // project2.getSubProjects().size());

            wv.delete(project2);
            wv.delete(testObject3);
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

}
