package org.pimslims.crystallization.implementation;

import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.crystallization.business.PersonServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;

public class PersonTest extends PersonServiceTest {

    /**
     * unique string for avoiding name clashes in tests
     */
    static final String UNIQUE = "test" + System.currentTimeMillis();

    public PersonTest(String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public void testFindByUsername() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            WritableVersion version = (WritableVersion) ((DataStorageImpl) this.dataStorage).getVersion();
            org.pimslims.model.accessControl.User pimsUser =
                new org.pimslims.model.accessControl.User(version, "user" + UNIQUE);
            org.pimslims.model.people.Person pimsPerson =
                new org.pimslims.model.people.Person(version, "family" + UNIQUE);
            pimsUser.setPerson(pimsPerson);

            PersonService service = this.dataStorage.getPersonService();
            Person result = service.findByUsername("nonesuch");
            assertNull(result);
            result = service.findByUsername(pimsUser.getName());
            assertNotNull(result);
            assertEquals(pimsUser.getName(), result.getUsername());
            assertEquals(pimsPerson.getFamilyName(), result.getFamilyName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
