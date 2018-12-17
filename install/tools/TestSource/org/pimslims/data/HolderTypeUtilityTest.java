/**
 * pims-tools org.pimslims.data HolderTypeUtilityTest.java
 * 
 * @author cm65
 * @date 19 Sep 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.data;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import junit.framework.TestCase;

import org.jdom.JDOMException;
import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.CrystalType;
import org.pimslims.model.reference.HolderType;

/**
 * HolderTypeUtilityTest
 * 
 */
public class HolderTypeUtilityTest extends TestCase {

    /**
     * HEADERS String
     */
    private static final String HEADERS =
        "holderCategory,name,maxRow,maxColumn,maxSubPosition,maxVolume(uL),maxVolumeDisplayUnit,HTPmaxVolume,details,catNum,supplier,pack size,units,,role/category,experiment types\r\n";

    private static final String UNIQUE = "htu" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param name
     */
    public HolderTypeUtilityTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public final void test() throws JDOMException, IOException, AccessException, ConstraintException {
        final Reader reader = new StringReader(HEADERS);
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            HolderTypeUtility.load(version, reader);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testNoCategory() throws JDOMException, IOException, AccessException,
        ConstraintException {
        final Reader reader = new StringReader(HEADERS + "nonesuch," + UNIQUE + "8,12,3,,,,,,,,,,");
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            HolderTypeUtility.load(version, reader);
            fail("no error message for missing category");
        } catch (IllegalArgumentException e) {
            // that's fine
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public final void testXtal() throws JDOMException, IOException, AccessException, ConstraintException {
        final Reader reader =
            new StringReader(HEADERS + "96 well plate," + UNIQUE + ",8,12,3,,,,,,,,,,,Crystallogenesis");
        final WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            HolderTypeUtility.load(version, reader);
            HolderType found = version.findFirst(HolderType.class, HolderType.PROP_NAME, UNIQUE);
            version.flush();
            assertNotNull(found);
            assertTrue(found instanceof CrystalType);
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
