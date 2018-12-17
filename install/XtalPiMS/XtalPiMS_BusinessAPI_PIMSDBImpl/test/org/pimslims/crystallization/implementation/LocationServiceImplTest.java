package org.pimslims.crystallization.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.crystallization.business.LocationServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Instrument;

public class LocationServiceImplTest extends LocationServiceTest {

    public static Test suite() {
        final TestSuite suite = new TestSuite(LocationServiceImplTest.class);
        return suite;
    }

    public LocationServiceImplTest(final String methodName) {
        super(methodName, DataStorageFactory.getDataStorageFactory().getDataStorage());
    }

    public void testFind() throws Exception {
        this.dataStorage.openResources("administrator");
        try {
            final WritableVersion version =
                (WritableVersion) (((DataStorageImpl) this.dataStorage).getVersion());
            final Instrument pimsLocation =
                new Instrument(version, UNIQUE);
            final LocationService service = this.dataStorage.getLocationService();

            Location result = service.findByName("nonesuch");
            assertNull(result);
            result = service.findByName(pimsLocation.getName());

            assertNotNull(result);
            assertEquals(pimsLocation.getName(), result.getName());
        } finally {
            this.dataStorage.abort(); // not testing persistence
        }
    }

}
