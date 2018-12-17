/**
 * pims-web org.pimslims.lab SupplierFactoryTest.java
 * 
 * @author cm65
 * @date 15 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.lab;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.people.Organisation;

/**
 * SupplierFactoryTest
 * 
 */
public class SupplierFactoryTest extends TestCase {

    private static final String UNIQUE = "sf" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param arg0
     */
    public SupplierFactoryTest(final String arg0) {
        super(arg0);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.SupplierFactory#createOrganisation(org.pimslims.dao.WritableVersion, java.lang.String, java.util.Map)}
     * .
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testCreateOrganisation() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Organisation organisation =
                SupplierFactory
                    .createOrganisation(version, SupplierFactoryTest.UNIQUE, Collections.EMPTY_MAP);
            Assert.assertEquals(SupplierFactoryTest.UNIQUE, organisation.getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.SupplierFactory#getOrganisation(org.pimslims.dao.WritableVersion, java.lang.String)}
     * .
     * 
     * @throws AccessException
     * @throws ConstraintException
     */
    public final void testGetOrganisation() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            SupplierFactory.createOrganisation(version, SupplierFactoryTest.UNIQUE, Collections.EMPTY_MAP);
            final Organisation organisation =
                SupplierFactory.getOrganisation(version, SupplierFactoryTest.UNIQUE);
            Assert.assertNotNull(organisation);
            Assert.assertEquals(SupplierFactoryTest.UNIQUE, organisation.getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
