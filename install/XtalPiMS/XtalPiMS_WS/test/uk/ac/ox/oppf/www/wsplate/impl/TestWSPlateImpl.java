package uk.ac.ox.oppf.www.wsplate.impl;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.pimslims.user.jndi.JNDIHelper;

import uk.ac.ox.oppf.www.wsplate.ListProjectsElement;
import uk.ac.ox.oppf.www.wsplate.ListProjectsError;
import uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement;
import uk.ac.ox.oppf.www.wsplate.Project;
import uk.ac.ox.oppf.www.wsplate.userdb.UserInfoProvider;

public class TestWSPlateImpl extends TestCase {

    /**
     * The name of the DataSource as stated in persistence.xml.
     * Currently java:comp/env/jdbc/ISGO2008DB
     */
    public static final String DATA_SOURCE_NAME = "java:comp/env/jdbc/UserDB";
    
    /**
     * The DataSource to use
     */
    private static DataSource dataSource = null;
    
    /**
     * Initialize dataSource
     */
    private static void initializeDataSource() {

        BasicDataSource ds = new BasicDataSource();
        
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/userdb");
        ds.setUsername("userdb-user");
        ds.setPassword("changeMe");
        ds.setDefaultAutoCommit(false);
        ds.setInitialSize(1);

        dataSource = ds;

    }
    
    /**
     * Constructor for TestPlateInfoProviderImpl 
     * @param name
     * @throws Exception 
     */
    public TestWSPlateImpl(String name) throws Exception {
        super(name);
        if (null == dataSource) {
            initializeDataSource();
        }
        JNDIHelper.initialize(DATA_SOURCE_NAME, dataSource);
    }

    /**
     * TestPlateInfoProviderImpl.setUp
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
    }

    /**
     * TestPlateInfoProviderImpl.tearDown
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        UserInfoProvider.getEmf().close();
        UserInfoProvider.setEmf(null);
    }
    
    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#listProjects(uk.ac.ox.oppf.www.wsplate.ListProjectsElement)}.
     * @throws ListProjectsError 
     */
    public void testListProjectsAdmin() throws ListProjectsError {
        WSPlateImpl wsPlate = new WSPlateImpl(new MockDataStorageFactoryProvider());
        ListProjectsElement request = new ListProjectsElement();
        ListProjectsResponseElement response = wsPlate.listProjects(request);
        assertNotNull(response);
        assertNotNull(response.getListProjectsResponseElement());
        assertNotNull(response.getListProjectsResponseElement().getWrapper());
        assertNotNull(response.getListProjectsResponseElement().getWrapper().getItem());
        Project[] projects = response.getListProjectsResponseElement().getWrapper().getItem();
        assertTrue("Some projects should be returned", 0 < projects.length);
        for (int i = 0 ; i < projects.length; i++) {
            assertNotNull(projects[i].getProjectName());
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#listProjects(uk.ac.ox.oppf.www.wsplate.ListProjectsElement)}.
     * @throws ListProjectsError 
     */
    public void testListProjectsUser() throws ListProjectsError {
        // TODO Use a standard username for testing
        WSPlateImpl wsPlate = new WSPlateImpl(new MockDataStorageFactoryProvider("jon"));
        ListProjectsElement request = new ListProjectsElement();
        ListProjectsResponseElement response = wsPlate.listProjects(request);
        assertNotNull(response);
        assertNotNull(response.getListProjectsResponseElement());
        assertNotNull(response.getListProjectsResponseElement().getWrapper());
        assertNotNull(response.getListProjectsResponseElement().getWrapper().getItem());
        Project[] projects = response.getListProjectsResponseElement().getWrapper().getItem();
        assertTrue("Some projects should be returned", 0 < projects.length);
        for (int i = 0 ; i < projects.length; i++) {
            assertNotNull(projects[i].getProjectName());
        }
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#listProjects(uk.ac.ox.oppf.www.wsplate.ListProjectsElement)}.
     */
    public void testListProjectsNullUser() {
        WSPlateImpl wsPlate = new WSPlateImpl(new MockDataStorageFactoryProvider(null));
        ListProjectsElement request = new ListProjectsElement();
        try {
            wsPlate.listProjects(request);
            fail("Should have thrown an exception");
        }
        catch (ListProjectsError e) {
            // This is expected
        }
    }

}
