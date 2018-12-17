/**
 * org.pimslims.upgrader DateFixerTest.java
 * 
 * @date 21-Dec-2006 10:59:11
 * 
 * @author Bill
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 *           Copyright (c) 2006
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
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;

public class DBDataTest extends org.pimslims.test.AbstractTestCase {

    // this test is too slow
    public void testRemoveDuplicatedWorkflowItem() {
        if (!DataFixer.RemoveDuplicatedWorkflowItem())
            log("Invalid WorkflowItem found and fixed!");
        else
            log("All WorkflowItem are correct!");
        assertTrue("Current DB's WorkflowItem data still has problem",
            DataFixer.RemoveDuplicatedWorkflowItem());
    }

/* may exhaust memory
    public void testCheckAndFixMolTypeForSequence() {
        if (!DataFixer.checkAndFixMolTypeForSequence())
            log("Invalid molType found and fixed!");
        else
            log("All molType are correct!");
        assertTrue("Current DB's molType data still has problem", DataFixer.checkAndFixMolTypeForSequence());
    } */

    public void SLOWtestDetectInvalidMemopsBaseClass() throws SQLException, AbortedException,
        ConstraintException {
        // get all dbid
        List<Long> ids = new ArrayList<Long>();
        ModelUpdateVersionImpl muv = (ModelUpdateVersionImpl) model.getModelUpdateVersion();
        try {
            DatabaseUpdater dbu = muv.getDbUpdater();
            ResultSet rs = dbu.sqlExecuteQuery("select dbid from core_labbookentry");
            while (rs.next()) {
                Long dbid = rs.getLong("dbid");
                ids.add(dbid);
            }
            rs.close();
        } finally {
            muv.abort();
        }
        // find invalid id and delete the record
        List<Long> invalidIDs;
        do {
            invalidIDs = new ArrayList<Long>();
            rv = getRV();
            try {
                for (Long id : ids) {
                    try {
                        rv.get(id);
                    } catch (org.hibernate.InstantiationException ex) {
                        if (ex.getMessage().startsWith("Cannot instantiate abstract class or interface:")) {
                            invalidIDs.add(id);
                            System.out.println("invalid ID: " + id);
                            rv.abort();
                            rv = getRV();
                        }
                    }
                    if (invalidIDs.size() > 2000)
                        break;
                }
            } finally {
                rv.abort();
            }
            // System.out.println(invalidIDs);
            System.out.println("found " + invalidIDs.size() + " invalid records");
            muv = (ModelUpdateVersionImpl) model.getModelUpdateVersion();
            try {
                DatabaseUpdater dbu = muv.getDbUpdater();

                for (Long id : invalidIDs)
                    dbu.sqlExecuteUpdate("delete from impl_memopsbaseclass where dbid=" + id);

                muv.commit();
            } finally {
                if (!muv.isCompleted())
                    muv.abort();
            }
        } while (invalidIDs.size() > 2000);
    }

    // this one takes ages
    public void slowtestDetectEmptyRequiredAttribute() throws AbortedException, ConstraintException,
        AccessException {

        System.out
            .println("\nThis test will scan the data to find potential problem like: required attribute with null value");

        for (String className : model.getClassNames())
            if (!className.endsWith("MemopsBaseClass") && !className.endsWith("ContentStored")
                && !className.endsWith("ExperimentalData")) {
                System.out.println("Checking " + className);
                WritableVersion wv = model.getWritableVersion(AbstractModel.SUPERUSER);
                try {
                    MetaClass mc = model.getMetaClass(className);
                    Collection<ModelObject> mos = wv.getAll(mc.getJavaClass());
                    for (MetaAttribute ma : mc.getAttributes().values())
                        if (ma.isRequired() && !ma.isDerived() && !ma.isHidden()) {
                            for (ModelObject mo : mos) {
                                Object attributeValue = mo.get_Value(ma.getName());
                                if (!ValueisValid(attributeValue)) {
                                    reportInvalid(ma, mo);
                                    if (className.endsWith("AppDataString"))
                                        mo.delete();
                                    else
                                        mo.set_Value(ma.getName(), ma.getName());
                                }
                            }
                        }
                    /*
                     * wv.commit(); } catch (ModelException ex) { fail(ex.toString());
                     */
                    wv.commit();
                } finally {
                    if (!wv.isCompleted()) {
                        wv.abort();
                    }
                }
            }

    }

    /**
     * @param ma
     * @param mo
     */
    static MetaAttribute lastMA = null;

    String errorInfo = null;

    private void reportInvalid(MetaAttribute ma, ModelObject mo) {
        if (ma != lastMA) {
            lastMA = ma;
            System.out.println("Invalid value found in " + ma.getMetaClass().getName() + "." + ma.getName()
                + ":");
        }
        System.out.println(mo.get_Hook() + errorInfo);

    }

    /**
     * @param attributeValue
     * @return
     */
    private boolean ValueisValid(Object attributeValue) {
        if (attributeValue == null) {
            errorInfo = " is null";
            return false;
        } else if (attributeValue instanceof String) {
            String av = (String) attributeValue;
            if (av.length() == 0) {
                errorInfo = " is not null but a empty string";
                return false;
            }
            if (av.trim().length() == 0) {
                errorInfo = " is Empty after trim";
                return false;
            }
        }
        return true;
    }
}
