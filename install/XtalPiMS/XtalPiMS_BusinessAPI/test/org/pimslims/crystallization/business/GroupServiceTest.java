/**
 * GroupServiceTest.java
 * 
 * Created on 6 Feb 2008
 * 
 * Author seroul
 */
package org.pimslims.crystallization.business;

import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.exception.BusinessException;

/**
 * @author seroul
 * 
 */
public class GroupServiceTest extends TestCase {

    // unique string to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    protected final DataStorage dataStorage;

    /**
     * @param methodName
     * @param dataStorage
     */
    public GroupServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
    }

    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final Organisation organisation = createOrfanisation();

            final Group toMake = new Group("group" + UNIQUE, organisation);

            final GroupService groupService = this.dataStorage.getGroupService();
            assertNotNull(groupService);
            groupService.create(toMake);

            final Group made = groupService.findByName(toMake.getName());
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            assertEquals(organisation.getName(), made.getOrganisation().getName());

            final Group madeID = groupService.find(toMake.getId());
            assertNotNull(madeID);
            assertEquals(toMake.getId(), madeID.getId());
            assertEquals(organisation.getName(), madeID.getOrganisation().getName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testFindByPersonAndUser() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            //preapare 2 group each have a user/person
            final Organisation organisation = createOrfanisation();
            final Group xGroup1 = new Group("group1" + UNIQUE, organisation);
            final Group xGroup2 = new Group("group2" + UNIQUE, organisation);
            final Person scientist1 = createPerson("u1");
            final Person scientist2 = createPerson("u2");
            xGroup1.addUser(scientist1);
            xGroup2.addUser(scientist2);

            final GroupService groupService = this.dataStorage.getGroupService();
            groupService.create(xGroup1);
            groupService.create(xGroup2);
            //do search by name
            Group found = groupService.findByName(xGroup1.getName());
            assertEquals(xGroup1, found);
            //do search by person
            final Collection<Group> xGroupsFound1 = groupService.findByPerson(scientist1.getUsername());
            assertEquals(1, xGroupsFound1.size());
            assertEquals(xGroup1, xGroupsFound1.iterator().next());
            //do search by username
            final Collection<Group> xGroupsFound2 = groupService.findByPerson(scientist2.getUsername());
            assertEquals(1, xGroupsFound2.size());
            assertEquals(xGroup2, xGroupsFound2.iterator().next());
            

            PersonService service = this.dataStorage.getPersonService();
            org.pimslims.business.core.model.Person user = service.findByUsername(scientist1.getUsername());
            assertNotNull(user);            
            assertEquals(1, user.getGroups().size());
            Group group = user.getGroups().iterator().next();
            assertEquals("group1" + UNIQUE, group.getName());
            Organisation foundOrg = group.getOrganisation();
            assertNotNull(foundOrg);

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testUsers() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final Person scientist1 = createPerson("u1");

            final Person scientist2 = createPerson("u2");

            final Organisation organisation = createOrfanisation();

            final Group toMake = new Group("group" + UNIQUE, organisation);
            toMake.addUser(scientist1);
            toMake.addUser(scientist2);

            final GroupService groupService = this.dataStorage.getGroupService();
            assertNotNull(groupService);
            groupService.create(toMake);

            final Group made = groupService.findByName(toMake.getName());
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            final List<Person> madeList = made.getUsers();
            assertNotNull(madeList);
            assertEquals(2, madeList.size());
            assertTrue(madeList.contains(scientist1));
            assertTrue(madeList.contains(scientist2));
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    private Person createPerson(final String prefix) throws BusinessException {
        final Person scientist = new Person();
        scientist.setUsername("user" + prefix + UNIQUE);
        scientist.setFamilyName("familyName" + prefix + UNIQUE);

        return scientist;
    }

    private Organisation createOrfanisation() throws BusinessException {
        final Organisation organisation = new Organisation();
        organisation.setName("organisation" + UNIQUE);
        final OrganisationService organisationService = this.dataStorage.getOrganisationService();
        assertNotNull(organisationService);
        organisationService.create(organisation);
        return organisation;
    }

}
