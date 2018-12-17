/**
 * ProjectTest.java
 * 
 * Created on 19 Feb 2008
 *
 * Author seroul
 */
package org.pimslims.crystallization.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Project;
import org.pimslims.crystallization.business.ProjectServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

/**
 * @author seroul
 *
 */
public class ProjectServiceImplTest extends ProjectServiceTest {

    /**
     * unique string for avoiding name clashes in tests
     */
    static final String UNIQUE = "test" + System.currentTimeMillis();
    Project project;
    private static final String description = "description" + UNIQUE;
    private static final String name = "name" + UNIQUE;

    /**
     * @param name
     */
    public ProjectServiceImplTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        project = new Project();
        project.setDescription(description);
        project.setName(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ProjectServiceImplTest.class);

        return suite;
    }
}
