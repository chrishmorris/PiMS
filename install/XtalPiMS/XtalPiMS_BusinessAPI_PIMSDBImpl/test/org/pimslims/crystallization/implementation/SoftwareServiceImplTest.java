/*
 * SampleServiceImplTest.java
 * JUnit based test
 *
 * Created on 09 August 2007, 16:56
 */

package org.pimslims.crystallization.implementation;

import java.net.URL;
import java.sql.Timestamp;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.business.crystallization.model.Software;
import org.pimslims.business.crystallization.service.SoftwareService;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;

/**
 * 
 * @author ian
 */
public class SoftwareServiceImplTest extends TestCase {

    // unique string used to avoid name clashes
    private static final String UNIQUE = "_test" + System.currentTimeMillis();

    private static final Timestamp NOW = new Timestamp(System.currentTimeMillis());

    private static final String description = "xSoftware description" + UNIQUE;

    private static final String name = "xSoftware name" + UNIQUE;

    private static final String vendorAddress = "xSoftware vendorAddress" + UNIQUE;

    private static final String vendorName = "xSoftware vendorName" + UNIQUE;

    private static final String sofwareversion = "xSoftware version" + UNIQUE;

    private static final String urlAddress = "http://www.xURL.address/" + UNIQUE;

    protected final DataStorageImpl dataStorage;

    Software xSoftware;

    private URL vendorURL;

    public SoftwareServiceImplTest(String testName) {
        super(testName);
        dataStorage = DataStorageFactory.getDataStorageFactory().getDataStorage();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SoftwareServiceImplTest.class);

        return suite;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //create a xURL
        vendorURL = new URL(urlAddress);

        //create a xSoftware
        xSoftware = new Software();
        xSoftware.setDescription(description);
        xSoftware.setName(name);
        xSoftware.setVendorAddress(vendorAddress);
        xSoftware.setVendorName(vendorName);
        xSoftware.setVendorURL(vendorURL);
        xSoftware.setVersion(sofwareversion);
    }

    /**
     * Test of create software
     */
    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = this.dataStorage.getWritableVersion();

            //create pims software
            SoftwareService service = this.dataStorage.getSoftwareService();
            service.create(xSoftware);
            Long pimsID = xSoftware.getId();

            //verify pims software
            assertNotNull(pimsID);
            org.pimslims.model.experiment.Software pimsObject = version.get(pimsID);
            assertNotNull(pimsObject);
            assertEquals(description, pimsObject.getDetails());
            assertEquals(name, pimsObject.getName());
            assertEquals(vendorAddress, pimsObject.getVendorAddress());
            assertEquals(vendorName, pimsObject.getVendorName());
            assertEquals(sofwareversion, pimsObject.getVersion());
            assertEquals(urlAddress, pimsObject.getVendorWebAddress());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /**
     * Test of load software
     */
    public void testLoad() throws Exception {

        this.dataStorage.openResources("administrator");
        try {
            //create pims software
            SoftwareService service = this.dataStorage.getSoftwareService();
            service.create(xSoftware);

            //load xtal software
            Software xtalObject = service.findByName(name);

            //verify xtal software
            assertNotNull(xtalObject);
            assertEquals(description, xtalObject.getDescription());
            assertEquals(name, xtalObject.getName());
            assertEquals(vendorAddress, xtalObject.getVendorAddress());
            assertEquals(vendorName, xtalObject.getVendorName());
            assertEquals(sofwareversion, xtalObject.getVersion());
            assertEquals(urlAddress, xtalObject.getVendorURL().toString());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    /**
     * Test of update software
     */
    public void testUpdate() throws Exception {

        this.dataStorage.openResources("administrator");
        try {
            //create pims software
            SoftwareService service = this.dataStorage.getSoftwareService();
            service.create(xSoftware);

            String updated = "Updated";
            //update xtal software
            xSoftware.setDescription(description + updated);
            xSoftware.setVendorAddress(vendorAddress + updated);
            xSoftware.setVendorName(vendorName + updated);
            xSoftware.setVendorURL(new URL(urlAddress + updated));
            xSoftware.setVersion(sofwareversion + updated);
            service.update(xSoftware);

            //reload xtal software
            Software xtalObject = service.findByName(name);

            //verify xtal software
            assertNotNull(xtalObject);
            assertEquals(description + updated, xtalObject.getDescription());
            assertEquals(vendorAddress + updated, xtalObject.getVendorAddress());
            assertEquals(vendorName + updated, xtalObject.getVendorName());
            assertEquals(sofwareversion + updated, xtalObject.getVersion());
            assertEquals(urlAddress + updated, xtalObject.getVendorURL().toString());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
}
