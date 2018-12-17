/**
 * @author Anne Pajon
 * @date Jun 11, 2009
 * 
 *       Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 3.2
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.api;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ModelException;
import org.pimslims.model.molecule.Host;
import org.pimslims.model.people.Organisation;
import org.pimslims.test.POJOFactory;

/**
 * HostTester
 * 
 */
public class HostTester extends org.pimslims.test.AbstractTestCase {

    public void testSimpleCreate() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Host host = POJOFactory.create(version, Host.class);
            assertNotNull(host);
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

    public void testCreateWithSuppliers() {
        WritableVersion version = model.getWritableVersion(AbstractModel.SUPERUSER);
        try {
            Host host = POJOFactory.create(version, Host.class);
            Organisation supplier = POJOFactory.create(version, Organisation.class);
            assertNotNull(host);
            assertNotNull(supplier);
            host.addSupplier(supplier);
            assertEquals(1, host.getSuppliers().size());
            assertEquals(supplier.getName(), host.getSuppliers().iterator().next().getName());
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
