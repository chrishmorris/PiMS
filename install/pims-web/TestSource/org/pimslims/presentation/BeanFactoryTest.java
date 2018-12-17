/**
 * pims-web org.pimslims.presentation BeanFactoryTest.java
 * 
 * @author cm65
 * @date 1 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.Organism;

/**
 * BeanFactoryTest
 * 
 */
public class BeanFactoryTest extends TestCase {

    private final AbstractModel model;

    public BeanFactoryTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testOrganism() {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final ModelObject object = new Organism(version, "test" + System.currentTimeMillis());
            final ModelObjectShortBean bean = BeanFactory.newBean(object);
            Assert.assertEquals("Natural source", bean.getClassDisplayName());
            Assert.assertEquals(Organism.class.getName(), bean.getClassName());
            Assert.assertEquals(object.get_Hook(), bean.getHook());
            Assert.assertEquals(object.get_Name(), bean.getName());

            Assert.assertEquals("Natural source: " + object.get_Name(), bean.toString());
            Assert.assertTrue(bean.getMayDelete());
            Assert.assertTrue(bean.getMayUpdate());
            Assert.assertTrue(bean.getMenu(), bean.getMenu().contains(
                "url:'/View/" + object.get_Hook() + "' } "));
            Assert.assertTrue(bean.getMenu(), bean.getMenu().contains(
                "url:'/Delete/" + object.get_Hook() + "' }"));
            Assert.assertTrue(bean.getMenu().endsWith("]"));
        } catch (final ConstraintException e) {
            Assert.fail(e.getMessage());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
