/**
 * org.pimslims.hibernate ExperimentTypeTester.java
 * 
 * @date 05-Oct-2006 17:23:06
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
 * 
 * Copyright (c) 2006
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */
package org.pimslims.model.api;

import java.util.HashMap;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.ExperimentType;

/**
 * ExperimentTypeTester
 * 
 */
public class ExperimentTypeTester extends TestCase {

    /**
     * Values for creating a objects of type ExperimentType
     */
    public static final HashMap<String, Object> ATTRS1 =
        new HashMap<String, Object>(org.pimslims.test.POJOFactory.getAttrExperimentType());

    public static final HashMap<String, Object> ATTRS2 =
        new HashMap<String, Object>(org.pimslims.test.POJOFactory.getAttrExperimentType());

    public void testCreate() {
        WritableVersion wv = ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ExperimentType expType = new ExperimentType(wv, ATTRS1);
            assertNotNull("created", expType.get_Hook());
        } catch (ConstraintException e) {
            e.printStackTrace();
            fail(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testFindAllEmpty() {
        WritableVersion wv = ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ExperimentType expType1 = new ExperimentType(wv, ATTRS1);
            assertNotNull("created", expType1.get_Hook());

            ExperimentType expType2 = null;
            java.util.Collection expTyeList = wv.findAll(ExperimentType.class, ATTRS2);
            if (0 == expTyeList.size()) {
                expType2 = new ExperimentType(wv, ATTRS2);
                assertNotNull("created", expType2.get_Hook());
            } else if (1 == expTyeList.size()) {
                java.util.Iterator i = expTyeList.iterator();
                expType2 = (ExperimentType) ((ModelObject) i.next());
                assertNotNull("already exists", expType2.get_Hook());
            }

        } catch (ConstraintException e) {
            e.printStackTrace();
            fail(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

    public void testFindAllTwice() {
        WritableVersion wv = ModelImpl.getModel().getWritableVersion(AbstractModel.SUPERUSER);
        try {
            ExperimentType expType1 = new ExperimentType(wv, ATTRS1);
            assertNotNull("created", expType1.get_Hook());

            ExperimentType expType2;
            java.util.Collection expTypeList = wv.findAll(ExperimentType.class, ATTRS2);
            if (0 == expTypeList.size()) {
                expType2 = new ExperimentType(wv, ATTRS2);
                assertNotNull("created", expType2.get_Hook());
            } else if (1 == expTypeList.size()) {
                java.util.Iterator i = expTypeList.iterator();
                expType2 = (ExperimentType) ((ModelObject) i.next());
                assertNotNull("already exists", expType2.get_Hook());
            }

            expTypeList = wv.findAll(ExperimentType.class, ATTRS2);
            if (0 == expTypeList.size()) {
                expType2 = new ExperimentType(wv, ATTRS2);
                assertNotNull("created", expType2.get_Hook());
            } else if (1 == expTypeList.size()) {
                java.util.Iterator i = expTypeList.iterator();
                expType2 = (ExperimentType) ((ModelObject) i.next());
                assertNotNull("already exists", expType2.get_Hook());
            }

        } catch (ConstraintException e) {
            e.printStackTrace();
            fail(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            wv.abort(); // not testing persistence here
        }
    }

}
