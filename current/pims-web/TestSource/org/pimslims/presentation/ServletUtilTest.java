/**
 * V2_0-pims-web org.pimslims.presentation ServletUtilTest.java
 * 
 * @author cm65
 * @date 18 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetamodelUtil;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.presentation.create.RoleHooksHolder;

/**
 * ServletUtilTest
 * 
 */
public class ServletUtilTest extends TestCase {

    //private static final String UNIQUE = "test" + System.currentTimeMillis();

    //private static final Calendar NOW = java.util.Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public ServletUtilTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testGetMandatoryAttr() {
        final String metaClassName = org.pimslims.model.people.Person.class.getName();
        final org.pimslims.metamodel.MetaClass metaClass = this.model.getMetaClass(metaClassName);
        final Map attributes = new HashMap(metaClass.getAttributes());
        final ArrayList sortedAttributes = new ArrayList();
        final Map.Entry[] sortedmap = MetamodelUtil.sortMap(attributes);
        for (int i = 0; i < sortedmap.length; i++) {
            final Map.Entry elem = sortedmap[i];
            sortedAttributes.add(elem.getValue());
        }
        ServletUtil.getMandatoryAttr(sortedAttributes);
        ServletUtil.getMandatoryAttr(sortedAttributes);

    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.ServletUtil#isMetaClassForAttribute(org.pimslims.metamodel.MetaClass, java.lang.String)}
     * .
     */
    public final void testIsMetaClassForAttributeFalse() {

        final MetaClass metaClass = this.model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        Assert.assertFalse(ServletUtil.isMetaClassForAttribute(metaClass, "nonesuch"));
    }

    public final void testgetPimsMetaClass() {

        final MetaClass metaClass = this.model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        final MetaClass pmeta = ServletUtil.getPIMSMetaClass(metaClass);
        Assert.assertNotNull(pmeta);
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.ServletUtil#isMetaClassForAttribute(org.pimslims.metamodel.MetaClass, java.lang.String)}
     * .
     */
    public final void testIsMetaClassForAttributeTrue() {

        final MetaClass metaClass = this.model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        Assert.assertTrue(ServletUtil.isMetaClassForAttribute(metaClass, LabBookEntry.PROP_DETAILS));
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.ServletUtil#isMetaClassForRole(org.pimslims.metamodel.MetaClass, java.lang.String)}
     * .
     */
    public final void testIsMetaClassForRoleFalse() {

        final MetaClass metaClass = this.model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        Assert.assertFalse(RoleHooksHolder.roleExists(metaClass, "nonesuch"));
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.ServletUtil#isMetaClassForRole(org.pimslims.metamodel.MetaClass, java.lang.String)}
     * .
     */
    public final void testIsMetaClassForRoleTrue() {

        final MetaClass metaClass = this.model.getMetaClass(org.pimslims.model.target.Target.class.getName());
        Assert.assertTrue(RoleHooksHolder.roleExists(metaClass, LabBookEntry.PROP_ATTACHMENTS));
    }

}
