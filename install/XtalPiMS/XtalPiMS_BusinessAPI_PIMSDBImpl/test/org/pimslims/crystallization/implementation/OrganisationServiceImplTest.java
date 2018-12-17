/**
 * OrganisationServiceImplTest.java
 * 
 * Created on 5 Feb 2008
 * 
 * Author seroul
 */
package org.pimslims.crystallization.implementation;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Organisation;
import org.pimslims.crystallization.business.OrganisationServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;

/**
 * @author seroul
 * 
 */
public class OrganisationServiceImplTest extends OrganisationServiceTest {

    // unique string used to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    private static final String fax = "fax" + UNIQUE;

    private static final String name = "name" + UNIQUE;

    private static final String building = "building" + UNIQUE;

    private static final String contactName = "contactName" + UNIQUE;

    private static final String country = "country" + UNIQUE;

    private static final String email = "email" + UNIQUE;

    private static final String phone = "phone" + UNIQUE;

    private static final String postalCode = "postalCode" + UNIQUE;

    private static final String street = "street" + UNIQUE;

    private static final String town = "town" + UNIQUE;

    private static final String urlAddress = "http://www.xURL.address/" + UNIQUE;

    URL url;

    Organisation organisation;

    /**
     * @param name
     */
    public OrganisationServiceImplTest(String testName) {
        super(testName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        url = new URL(urlAddress);
        organisation = new Organisation();
        organisation.setName(name);
        organisation.setFax(fax);
        organisation.setBuilding(building);
        organisation.setContactName(contactName);
        organisation.setCountry(country);
        organisation.setEmail(email);
        organisation.setPhone(phone);
        organisation.setPostalCode(postalCode);
        organisation.setStreet(street);
        organisation.setTown(town);
        organisation.setURL(url);

    }

    public static Test suite() {
        TestSuite suite = new TestSuite(OrganisationServiceImplTest.class);

        return suite;
    }

}
