package org.pimslims.crystallization.business;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.ConstructService;

public abstract class ConstructServiceTest extends AbstractXtalTest {

    public ConstructServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName, dataStorage);

    }

    public void testGetService() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            ConstructService service = this.dataStorage.getConstructService();
            assertNotNull(service);
            //was assertEquals(this.dataStorage, service.getDataStorage());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            ConstructService service = this.dataStorage.getConstructService();
            Construct toMake = new Construct(UNIQUE, null);
            Person xPerson = createXPerson();
            //could Group xgroup = createXGroup();

            toMake.setDescription("desc");
            toMake.setOwner(xPerson);
            //could toMake.setGroup(xgroup);
            service.create(toMake);

            Construct result = service.findByName(toMake.getName());
            assertNotNull(result);
            assertEquals(toMake.getDescription(), result.getDescription());
            assertEquals(toMake.getName(), result.getName());
            //could assertEquals(toMake.getGroup().getName(), result.getGroup().getName());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testUpdate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            ConstructService service = this.dataStorage.getConstructService();
            Construct toMake = createXConstruct();

            //update some details
            toMake.setDescription("desc changed" + UNIQUE);
            Person xPerson = createXPerson();
            toMake.setOwner(xPerson);

            //update
            service.update(toMake);
            Construct result = service.findByName(toMake.getName());
            assertNotNull(result);
            assertEquals(toMake.getDescription(), result.getDescription());

        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testFindByUsername() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            ConstructService service = this.dataStorage.getConstructService();
            Construct toMake = new Construct(UNIQUE, null);
            toMake.setDescription("desc");
            Person person = new Person();
            person.setUsername("u " + UNIQUE);
            person.setFamilyName("F" + UNIQUE);
            this.dataStorage.getPersonService().create(person);
            toMake.setOwner(person);

            service.create(toMake);

            // test getScientist
            Construct result = service.findByName(toMake.getName());
            assertNotNull(result);
            assertEquals(toMake.getDescription(), result.getDescription());
            assertEquals(toMake.getName(), result.getName());
            assertNotNull(result.getOwner());
            assertEquals(person.getUsername(), result.getOwner().getUsername());

            Collection<Construct> constructs = service.findByUser(person);
            assertEquals(1, constructs.size());
            result = constructs.iterator().next();
            assertEquals(toMake.getName(), result.getName());
            assertEquals(toMake.getOwner().getUsername(), result.getOwner().getUsername());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
