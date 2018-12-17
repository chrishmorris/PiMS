/** 
 * xtalpims-ws-stub uk.ac.ox.oppf.www.wsplate.impl TestPlateInfoProviderImpl.java
 * @author Jon
 * @date 26 Aug 2010
 *
 * Protein Information Management System
 * @version: 4.1
 *
 * Copyright (c) 2010 Jon 
 * The copyright holder has licenced the STFC to redistribute this software
 */
package uk.ac.ox.oppf.www.wsplate.impl;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
// FIXME VMXi Bodge - no UserDB
//import org.pimslims.user.jndi.JNDIHelper;
//import org.pimslims.user.userdb.util.UserInfoProvider;

import uk.ac.ox.oppf.www.wsplate.GetPlateID;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDError;
import uk.ac.ox.oppf.www.wsplate.GetPlateIDResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfo;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoError;
import uk.ac.ox.oppf.www.wsplate.GetPlateInfoResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateType;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypeResponse;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypes;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesError;
import uk.ac.ox.oppf.www.wsplate.GetPlateTypesResponse;
import uk.ac.ox.oppf.www.wsplate.PlateInfo;
import uk.ac.ox.oppf.www.wsplate.PlateType;
import uk.ac.ox.oppf.www.wsplate.Robot;

/**
 * TestPlateInfoProviderImpl
 *
 */
public class TestPlateInfoProviderImpl extends TestCase {

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
    public TestPlateInfoProviderImpl(String name) throws Exception {
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
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateID(uk.ac.ox.oppf.www.wsplate.GetPlateID)}.
     * @throws GetPlateIDError 
     */
    public void testGetPlateID() throws GetPlateIDError {
        PlateInfoProvider pip = new PlateInfoProviderImpl(new MockDataStorageFactoryProvider());
        Robot robot = new Robot();
        robot.setID("1");
        robot.setName("test");
        GetPlateID request = new GetPlateID();
        String barcode = "331300123456";
        request.setBarcode(barcode);
        request.setRobot(robot);
        GetPlateIDResponse response = pip.getPlateID(request);
        assertEquals(barcode, response.getGetPlateIDReturn());
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateInfo(uk.ac.ox.oppf.www.wsplate.GetPlateInfo)}.
     * @throws GetPlateInfoError 
     */
    public void testGetPlateInfo() throws GetPlateInfoError {
        //UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
        PlateInfoProvider pip = new PlateInfoProviderImpl(new MockDataStorageFactoryProvider());
        Robot robot = new Robot();
        robot.setID("1");
        robot.setName("test");
        GetPlateInfo request = new GetPlateInfo();
        request.setRobot(robot);
        request.setPlateID("331300123456");
        GetPlateInfoResponse response = pip.getPlateInfo(request);
        PlateInfo plateInfo = response.getGetPlateInfoReturn();
        assertNotNull(plateInfo);
        assertEquals("331300123456 in Silver Bullets HT", plateInfo.getExperimentName());
        assertEquals(123456, plateInfo.getPlateNumber());
        assertEquals("8586", plateInfo.getPlateTypeID());
        assertEquals("Christian Sielbold Group", plateInfo.getProjectName());
        assertEquals("jon@strubi.ox.ac.uk", plateInfo.getUserEmail());
        assertEquals("jon", plateInfo.getUserName());
        assertNotNull(plateInfo.getDateDispensed());
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateInfo(uk.ac.ox.oppf.www.wsplate.GetPlateInfo)}.
     * @throws GetPlateInfoError 
     */
    public void testGetPlateInfo2() throws GetPlateInfoError {
        //UserInfoProvider.setEmf(Persistence.createEntityManagerFactory("UserDB"));
        PlateInfoProvider pip = new PlateInfoProviderImpl(new MockDataStorageFactoryProvider());
        Robot robot = new Robot();
        robot.setID("1");
        robot.setName("test");
        GetPlateInfo request = new GetPlateInfo();
        request.setRobot(robot);
        request.setPlateID("331300123456");
        GetPlateInfoResponse response = pip.getPlateInfo(request);
        PlateInfo plateInfo = response.getGetPlateInfoReturn();
        assertNotNull(plateInfo);
        assertEquals("331300123456 in Silver Bullets HT", plateInfo.getExperimentName());
        assertEquals(123456, plateInfo.getPlateNumber());
        assertEquals("8586", plateInfo.getPlateTypeID());
        assertEquals("Christian Sielbold Group", plateInfo.getProjectName());
        assertEquals("jon@strubi.ox.ac.uk", plateInfo.getUserEmail());
        assertEquals("jon", plateInfo.getUserName());
        assertNotNull(plateInfo.getDateDispensed());
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
        response.getGetPlateInfoReturn();
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateType(uk.ac.ox.oppf.www.wsplate.GetPlateType)}.
     * @throws GetPlateTypeError 
     */
    public void testGetPlateType() throws GetPlateTypeError {
        PlateInfoProvider pip = new PlateInfoProviderImpl(new MockDataStorageFactoryProvider());
        Robot robot = new Robot();
        robot.setID("1");
        robot.setName("test");
        GetPlateType request = new GetPlateType();
        String plateTypeID = "8586";
        request.setPlateTypeID(plateTypeID);
        request.setRobot(robot);
        GetPlateTypeResponse response = pip.getPlateType(request);
        PlateType plateType = response.getGetPlateTypeReturn();
        assertNotNull(plateType);
        assertEquals(plateTypeID, plateType.getID());
        assertEquals("CrystalQuick plate, square wells", plateType.getName());
        assertEquals(12, plateType.getNumColumns());
        assertEquals(2, plateType.getNumDrops());
        assertEquals(8, plateType.getNumRows());
    }

    /**
     * Test method for {@link uk.ac.ox.oppf.www.wsplate.impl.PlateInfoProviderImpl#getPlateTypes(uk.ac.ox.oppf.www.wsplate.GetPlateTypes)}.
     * @throws GetPlateTypesError 
     */
    public void testGetPlateTypes() throws GetPlateTypesError {
        PlateInfoProvider pip = new PlateInfoProviderImpl(new MockDataStorageFactoryProvider());
        Robot robot = new Robot();
        robot.setID("1");
        robot.setName("test");
        GetPlateTypes request = new GetPlateTypes();
        request.setRobot(robot);
        GetPlateTypesResponse response = pip.getPlateTypes(request);
        PlateType[] plateTypes = response.getWrapper().getItem();
        assertTrue(plateTypes.length > 0);
        for (int i = 0; i < plateTypes.length; i++) {
            System.out.println(plateTypes[i].getID() + ": " + plateTypes[i].getName());
        }
    }

}
