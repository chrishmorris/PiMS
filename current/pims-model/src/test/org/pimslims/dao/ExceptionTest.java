/**
 * DM org.pimslims.hibernate ExceptionTest.java
 * 
 * @author bl67
 * @date 14 Apr 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
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
package org.pimslims.dao;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DuplicateKeyConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.test.AbstractTestCase;

/**
 * ExceptionTest this test not availabe for oracle
 */

public class ExceptionTest extends AbstractTestCase {

    public void testDuplicationException() throws AccessException, ConstraintException, AbortedException {
        if (HibernateUtil.isOracleDB())
            return;
        wv = getWV();
        try {
            create(Sample.class, AbstractSample.PROP_NAME, "sameName");
            create(Sample.class, AbstractSample.PROP_NAME, "sameName");
            fail();
        } catch (DuplicateKeyConstraintException e) {
            //e.printStackTrace();
            //System.out.println(e.toString());
            assertEquals("[sameName]", e.value);
            assertEquals("[" + AbstractSample.PROP_NAME + "]", e.attributeName);
        } finally {
            wv.abort();
        }

        wv = getWV();
        try {
            Sample s1 = create(Sample.class);
            Sample s2 = create(Sample.class);
            s1.setName("sameName2");
            s2.setName("sameName2");
            wv.commit();
            fail();
        } catch (DuplicateKeyConstraintException e) {
            System.out.println(e.toString());
            //assertEquals(Sample.PROP_NAME, e.attributeName);
        } finally {
            wv.abort();
        }
    }

    public void testTargetDuplicationException() throws AccessException, ConstraintException {
        if (HibernateUtil.isOracleDB())
            return;
        wv = getWV();
        try {
            Target t = create(Target.class, Target.PROP_NAME, "sameName");
            final MetaClass metaClass = wv.getModel().getMetaClass(Target.class.getName());
            Map<String, Object> attributes = new HashMap<String, Object>();
            attributes.put(Target.PROP_NAME, "sameName");
            attributes.put(Target.PROP_PROTEIN, t.getProtein());
            // create the new one in the standard way
            metaClass.create(wv, "reference", attributes);
            fail();
        } catch (DuplicateKeyConstraintException e) {
            assertEquals("[sameName]", e.value);
            assertEquals("[" + Target.PROP_NAME + "]", e.attributeName);
        } finally {
            wv.abort();
        }

    }

}
