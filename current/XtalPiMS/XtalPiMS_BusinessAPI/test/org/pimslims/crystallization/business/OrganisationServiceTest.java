/**
 * OrganisationService.java
 * 
 * Created on 5 Feb 2008
 *
 * Author seroul
 */
package org.pimslims.crystallization.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.service.OrganisationService;

import junit.framework.TestCase;

/**
 * @author seroul
 *
 */
public class OrganisationServiceTest extends TestCase {
	
	// unique string to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();
	
	protected final DataStorage dataStorage;

	/**
	 * @param name
	 */
	public OrganisationServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
	}
	
	
	public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            Organisation toMake = new Organisation();
            toMake.setName("organisation" + UNIQUE);
            toMake.setContactName("cn"+UNIQUE);
            
            OrganisationService service = this.dataStorage.getOrganisationService();
        	assertNotNull(service);
            service.create(toMake);

            Organisation made = service.findByName(toMake.getName());
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            //TODO assertEquals(toMake.getContactName(), made.getContactName());
            
            Organisation madeID = service.find(toMake.getId());
            assertNotNull(madeID);
            assertEquals(toMake.getId(), madeID.getId());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
	
	public void testGroup() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            Group group = new Group();
            group.setName("group" + UNIQUE);
            
            Organisation organisation = new Organisation();
            organisation.setName("organisation" + UNIQUE);
            organisation.addGroup(group);
            OrganisationService organisationService = this.dataStorage.getOrganisationService();
            organisationService.create(organisation);
            
            // test getGroups
            Organisation madeOrganisation = organisationService.findByName(organisation.getName());
            assertNotNull(madeOrganisation);
            Collection<Group> madeGroups = organisation.getGroups();
            assertNotNull(madeGroups);
            Iterator<Group> iterator = madeGroups.iterator();
            Group madeGroup = iterator.next();
            assertNotNull(madeGroup);
            assertEquals(madeGroup.getName(), group.getName());
            
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
	
	public void testGroups() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            Group group1 = new Group();
            group1.setName("group1" + UNIQUE);
            Group group2 = new Group();
            group2.setName("group2" + UNIQUE);
            Collection<Group> groups = new ArrayList<Group>();
            groups.add(group1);
            groups.add(group2);
            
            Organisation organisation = new Organisation();
            organisation.setName("organisation" + UNIQUE);
            organisation.setGroups(groups);
            OrganisationService organisationService = this.dataStorage.getOrganisationService();
            organisationService.create(organisation);
            
            // test getGroups
            Organisation madeOrganisation = organisationService.findByName(organisation.getName());
            assertNotNull(madeOrganisation);
            Collection<Group> madeGroups = organisation.getGroups();
            assertNotNull(madeGroups);
            assertEquals(madeGroups.size(), 2);
            
            assertTrue(madeGroups.contains(group1));
            assertTrue(madeGroups.contains(group2));
            
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }
}
