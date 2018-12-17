/**
 * utils AbstractTestCaseTest.java
 * 
 * @date 2 Apr 2007 08:55:45
 * 
 * @author Anne Pajon, pajon@ebi.ac.uk EMBL - European Bioinformatics Institute - MSD group Wellcome Trust
 *         Genome Campus, Hinxton, Cambridge CB10 1SD, UK
 * 
 *         Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 0.5
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
package org.pimslims.test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;

/**
 * AbstractTestCaseTest
 * 
 */
public class AbstractTestCaseTest extends AbstractTestCase {

    public void testCreateMinimum() {
        List<String> failedClassNames = new LinkedList<String>();
        Collection<String> metaClassNames = model.getClassNames();
        assertTrue("only" + metaClassNames.size(), metaClassNames.size() > 50);
        for (String metaClassName : metaClassNames) {
            if (metaClassName.toLowerCase().contains("org.pimslims.model.core"))
                // may not creatable
                continue;
            wv = getWV();
            Class javaClass = model.getMetaClass(metaClassName).getJavaClass();
            try {
                create(javaClass);
            } catch (AccessException e) {

                e.printStackTrace();
                System.err.println("Failed to create " + javaClass.getName());
                failedClassNames.add(javaClass.getName());
            } catch (Exception e) {
                if (e.toString().contains("ContentStorage is not supported in pims yet!")) {
                    // it is fine

                } else {
                    e.printStackTrace();
                    System.err.println("Failed to create " + javaClass.getName());
                    failedClassNames.add(javaClass.getName());
                }
            } finally {
                wv.abort();
            }
        }
        assertTrue("failed to create following classes: " + failedClassNames.toString(),
            failedClassNames.size() == 0);
    }

    public void testCreateMax() {
        List<String> failedClassNames = new LinkedList<String>();
        Collection<String> metaClassNames = model.getClassNames();
        for (String metaClassName : metaClassNames) {
            if (metaClassName.startsWith("org.pimslims.model.core"))// may
                // not
                // creatable
                continue;
            wv = getWV();
            Class javaClass = model.getMetaClass(metaClassName).getJavaClass();
            try {
                createFull(javaClass);
            } catch (AccessException e) {

                e.printStackTrace();
                System.err.println("Failed to create " + javaClass.getName());
                failedClassNames.add(javaClass.getName());
            } catch (ConstraintException e) {

                if (e.toString().contains("ContentStorage is not supported in pims yet!")) {
                    // it is fine
                } else {
                    e.printStackTrace();
                    System.err.println("Failed to create " + javaClass.getName());
                    failedClassNames.add(javaClass.getName());
                }
            } finally {
                wv.abort();
            }
        }
        assertTrue("failed to create following classes: " + failedClassNames.toString(),
            failedClassNames.size() == 0);
    }
}
