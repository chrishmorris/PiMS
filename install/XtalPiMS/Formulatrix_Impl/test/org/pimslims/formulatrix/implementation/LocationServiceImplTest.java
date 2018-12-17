package org.pimslims.formulatrix.implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.business.core.model.Location;
import org.pimslims.business.core.service.LocationService;
import org.pimslims.crystallization.business.LocationServiceTest;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Instrument;

public class LocationServiceImplTest extends LocationServiceTest {

    public static Test suite() {
        final TestSuite suite = new TestSuite(LocationServiceImplTest.class);
        return suite;
    }

    public LocationServiceImplTest(final String methodName) {
        super(methodName, new ManufacturerDataStorageImpl(false, ManufacturerVersion.OULU));
    }

  

}
