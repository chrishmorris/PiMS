package uk.ac.ox.oppf.www.wsplate.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.dao.ReadableVersion;
//import org.pimslims.persistence.HQLBuilder;
//FIXME VMXi Bodge - no UserDB
//import org.pimslims.user.jndi.JNDIHelper;
//import org.pimslims.user.userdb.util.UserInfoProvider;

import uk.ac.ox.oppf.www.wsplate.ArrayUploadImage;
import uk.ac.ox.oppf.www.wsplate.ImageType;
import uk.ac.ox.oppf.www.wsplate.ListProjectsElement;
import uk.ac.ox.oppf.www.wsplate.ListProjectsError;
import uk.ac.ox.oppf.www.wsplate.ListProjectsResponseElement;
import uk.ac.ox.oppf.www.wsplate.ListSamples;
import uk.ac.ox.oppf.www.wsplate.ListSamplesElement;
import uk.ac.ox.oppf.www.wsplate.ListSamplesResponseElement;
import uk.ac.ox.oppf.www.wsplate.Project;
import uk.ac.ox.oppf.www.wsplate.Robot;
import uk.ac.ox.oppf.www.wsplate.Size;
import uk.ac.ox.oppf.www.wsplate.UploadImage;
import uk.ac.ox.oppf.www.wsplate.UploadImages;
import uk.ac.ox.oppf.www.wsplate.UploadImagesElement;
import uk.ac.ox.oppf.www.wsplate.UploadImagesError;
import uk.ac.ox.oppf.www.wsplate.UploadImagesResponseElement;

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
        // FIXME VMXi Bodge - no UserDB
        //if (null == dataSource) {
        //    initializeDataSource();
        //}
        //JNDIHelper.initialize(DATA_SOURCE_NAME, dataSource);
    }

    /**
     * TestPlateInfoProviderImpl.setUp
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // FIXME VMXi Bodge - no UserDB
        //UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
    }

    /**
     * TestPlateInfoProviderImpl.tearDown
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        // FIXME VMXi Bodge - no UserDB
        //UserInfoProvider.getEmf().close();
        //UserInfoProvider.setEmf(null);
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

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#uploadImages(uk.ac.ox.oppf.www.wsplate.UploadImagesElement)}.
     */
    public void testUploadImages() {
        WSPlateImpl wsPlate = new WSPlateImpl(new MockDataStorageFactoryProvider());
        
        Calendar now = Calendar.getInstance();
        
        Size microns = new Size();
        microns.setHeight(1280f);
        microns.setWidth(960f);
        Size pixels = new Size();
        pixels.setHeight(1.9959819f);
        pixels.setWidth(1.9959819f);
        
        Robot robot = new Robot();
        robot.setID("foo");
        robot.setName("RI1000-0082");
        
        UploadImage[] uploadImageArray = new UploadImage[1];
        uploadImageArray[0] = new UploadImage();
        uploadImageArray[0].setColourDepth(24);
        uploadImageArray[0].setDateImaged(now);
        uploadImageArray[0].setImage(microns);
        uploadImageArray[0].setImagingID("441305100338-20120522-113225");
        uploadImageArray[0].setPixel(pixels);
        uploadImageArray[0].setPlateID("441305100338");
        uploadImageArray[0].setRobot(robot);
        uploadImageArray[0].setType(ImageType.composite);
        uploadImageArray[0].setUrl("http://www.oppf.rc-harwell.ac.uk/images/lowres/441305100338/441305100338-A01-test.jpg");
        uploadImageArray[0].setWell("A01");
        
        ArrayUploadImage arrayUploadImage = new ArrayUploadImage();
        arrayUploadImage.setItem(uploadImageArray);
        
        UploadImages uploadImages = new UploadImages();
        uploadImages.setWrapper(arrayUploadImage);
        
        UploadImagesElement request = new UploadImagesElement();
        request.setUploadImagesElement(uploadImages);
        try {
            UploadImagesResponseElement response = wsPlate.uploadImages(request);
            assertTrue(response.getUploadImagesResponseElement().getWrapper().getItem()[0].getOk());
        } catch (UploadImagesError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.WSPlateImpl#ListSamples(uk.ac.ox.oppf.www.wsplate.ListSamplesElement)}.
     */
    public void testListSamples() throws Exception {
        WSPlateImpl wsPlate = new WSPlateImpl(new MockDataStorageFactoryProvider("jon"));
        
        Robot robot = new Robot();
        robot.setID("foo");
        robot.setName("Cartesian #4");
        
        ListSamples listSamples = new ListSamples();
        listSamples.setRobot(robot);
        
        ListSamplesElement request = new ListSamplesElement();
        request.setListSamplesElement(listSamples);
        
        ListSamplesResponseElement response = wsPlate.listSamples(request);
        
        assertTrue(response.getListSamplesResponseElement().getWrapper().getItem().length > 0);
        
    }
    
}
