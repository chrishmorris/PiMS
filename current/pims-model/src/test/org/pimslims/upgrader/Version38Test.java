/**
 * current-datamodel org.pimslims.upgrader Version34Test.java
 * 
 * @author cm65
 * @date 22 May 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.upgrader;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;

/**
 * Version34Test
 * 
 * A collection of test cases to verify the changes from DM 33 to DM 34
 * 
 */
public class Version38Test extends TestCase {

    //private static final String UNIQUE = "v38_" + System.currentTimeMillis();

    //private static final Calendar NOW = Calendar.getInstance();

    /* string length 80 for testing
    private static final String LONG =
        "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    */
    private final AbstractModel model;

    /**
     * @param name
     */
    public Version38Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testAttributes() throws SQLException {

        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) this.model.getModelUpdateVersion();
        try {
            final DatabaseUpdater dbUpgrader = muv.getDbUpdater();
            assertFalse(dbUpgrader.isColumnExist("targ_targetgroup", "namingsystem"));
            assertTrue(dbUpgrader.isColumnExist("mole_primer", "lengthongene"));
        } finally {
            muv.abort();
        }
    }

    /* old
    public void testRoles() throws  SQLException {
            
            ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) this.model.getModelUpdateVersion();
            try {
            final DatabaseUpdater dbUpgrader = muv.getDbUpdater();
            

            assertTrue(dbUpgrader.isConstraintExist("acco_usergroup", "acco_usergroup_header"));
            
            } finally {
                muv.abort();
            }
    } */

    public void testTables() throws SQLException {

        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) this.model.getModelUpdateVersion();
        try {
            final DatabaseUpdater dbUpgrader = muv.getDbUpdater();

            assertFalse(dbUpgrader.isTableExist("acco_usergroup2members"));
            assertTrue(dbUpgrader.isTableExist("acco_usergroup2user"));

        } finally {
            muv.abort();
        }
    }

}
