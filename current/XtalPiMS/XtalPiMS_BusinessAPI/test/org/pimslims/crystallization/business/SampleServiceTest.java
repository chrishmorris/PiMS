package org.pimslims.crystallization.business;

import junit.framework.TestCase;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;

public abstract class SampleServiceTest extends TestCase {

    private static final String BARCODE = "bar" + System.currentTimeMillis();

    // unique string to avoid name clashes
    private static final String UNIQUE = "test" + System.currentTimeMillis();

    protected final DataStorage dataStorage;

    public SampleServiceTest(String methodName, DataStorage dataStorage) {
        super(methodName);
        this.dataStorage = dataStorage;
    }

    public void testGetService() throws Exception {
        //XtalSession session = this.dataStorage.getSession("administrator");
        this.dataStorage.openResources("administrator");
        try {
            SampleService service = this.dataStorage.getSampleService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testCreate() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            SampleService service = this.dataStorage.getSampleService();
            Sample toMake = new Sample();
            toMake.setName("sample" + UNIQUE);
            toMake.setDescription("desc" + UNIQUE);

            service.create(toMake);

            Sample made = service.findByName(toMake.getName());
            assertNotNull(made);
            assertEquals(toMake.getName(), made.getName());
            assertEquals(toMake.getDescription(), made.getDescription());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

    public void testScientist() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            SampleService sampleService = this.dataStorage.getSampleService();
            Person scientist = new Person();
            scientist.setUsername("user" + UNIQUE);
            scientist.setFamilyName("familyName" + UNIQUE);
            this.dataStorage.getPersonService().create(scientist);
            // have to have construct to set scientist
            Construct construct = new Construct("construct" + UNIQUE, null);
            this.dataStorage.getConstructService().create(construct);
            Sample sample = new Sample("sample" + UNIQUE);
            sample.setConstruct(construct);
            sampleService.create(sample);
            // have to make a sample production to save construct
            //SampleProduction sp = new SampleProduction(sample, SampleProductionServiceTest.NOW );
            //this.dataStorage.getSampleProductionService().create(sp);

            sampleService.setOwnerForSample(sample, scientist);

            // test getScientist
            Sample made = sampleService.findByName(sample.getName());
            assertNotNull(made);
            Person madeScientist = sampleService.getOwner(made);
            assertNotNull(madeScientist);
            assertEquals(scientist.getUsername(), madeScientist.getUsername());
            assertEquals(construct.getName(), sample.getConstruct().getName());

/*            // test find by user
            Collection<Sample> samples = sampleService.findByUser(scientist, null);
            assertEquals(1, samples.size());
            made = samples.iterator().next();
            assertEquals(sample.getName(), made.getName());*/
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
