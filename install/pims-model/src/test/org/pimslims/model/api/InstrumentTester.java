/**
 * pims-model org.pimslims.model.api InstrumentTester.java
 * 
 * @author pajanne
 * @date Jul 3, 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.model.api;

import java.util.Collections;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.reference.InstrumentType;
import org.pimslims.test.POJOFactory;

/**
 * InstrumentTester
 * 
 */
public class InstrumentTester extends org.pimslims.test.AbstractTestCase {
    public void testSimpleCreate() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Instrument instrument = new Instrument(version, "TestName");
            assertNotNull(instrument);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testCreateWithInstType() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Instrument instrument = new Instrument(version, "TestName");
            InstrumentType type = new InstrumentType(version, "TestName");
            instrument.setInstrumentTypes(Collections.singleton(type));
            assertNotNull(instrument);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }

    public void testSimpleCreateWithPOJOFactory() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Instrument instrument = POJOFactory.createWithFullAttributes(version, Instrument.class);
            assertNotNull(instrument);
        } catch (ModelException ex) {
            Throwable cause = ex;
            while (null != cause.getCause()) {
                cause = cause.getCause();
            }
            cause.printStackTrace();
            fail(cause.getClass().getName() + ": " + cause.getMessage());
        } finally {
            version.abort(); // not testing persistence here
        }
    }
}
