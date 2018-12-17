package org.pimslims.crystallization.business;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.exception.BusinessException;

public abstract class PersonServiceTest extends TestCase {

    private static final String BARCODE = "bar" + System.currentTimeMillis();

    // unique string to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    protected final DataStorage dataStorage;

    public PersonServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
    }

    public void testGetService() throws Exception {
        //XtalSession session = this.dataStorage.getSession("administrator");
        this.dataStorage.openResources("administrator");
        try {
            PersonService service = this.dataStorage.getPersonService();
            assertNotNull(service);
            assertEquals(this.dataStorage, service.getDataStorage());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            PersonService service = this.dataStorage.getPersonService();
            Person toMake = makePerson();

            service.create(toMake);

            org.pimslims.business.core.model.Person result = service.findByUsername(toMake.getUsername());
            assertNotNull(result);
            assertEquals(toMake.getUsername(), result.getUsername());
            assertEquals(toMake.getFamilyName(), result.getFamilyName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    private Person makePerson() {
        Person toMake = new Person();
        toMake.setFamilyName("family" + UNIQUE);
        toMake.setUsername("user" + UNIQUE);
        return toMake;
    }

    public void testUpdate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            PersonService service = this.dataStorage.getPersonService();
            Person toMake = makePerson();

            service.create(toMake);
            Group xGroup = new Group("group1" + UNIQUE, createOrganisation());
            GroupService groupService = this.dataStorage.getGroupService();
            groupService.create(xGroup);

            toMake.setGivenName("givenName" + UNIQUE);
            toMake.addGroup(xGroup);
            service.update(toMake);

            org.pimslims.business.core.model.Person result = service.findByUsername(toMake.getUsername());
            assertNotNull(result);
            assertEquals(toMake.getUsername(), result.getUsername());
            assertEquals(toMake.getFamilyName(), result.getFamilyName());
            assertEquals(1, result.getGroups().size());
            assertEquals(xGroup, result.getGroups().iterator().next());
            assertEquals(toMake.getGivenName(), result.getGivenName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    private Organisation createOrganisation() throws BusinessException {
        Organisation organisation = new Organisation();
        organisation.setName("organisation" + UNIQUE);
        OrganisationService organisationService = this.dataStorage.getOrganisationService();
        assertNotNull(organisationService);
        organisationService.create(organisation);
        return organisation;
    }
}
