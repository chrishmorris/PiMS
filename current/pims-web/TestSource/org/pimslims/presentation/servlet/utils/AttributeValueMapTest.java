/**
 * V2_2-pims-web org.pimslims.presentation.servlet.utils AttributeValueMapTest.java
 * 
 * @author cm65
 * @date 25 Jul 2008
 * 
 * Protein Information Management System
 * @version: 2.2
 * 
 * Copyright (c) 2008 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.servlet.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.model.people.Person;
import org.pimslims.presentation.create.AttributeValueMap;

/**
 * AttributeValueMapTest
 * 
 */
public class AttributeValueMapTest extends TestCase {

    /**
     * Test method for
     * {@link org.pimslims.presentation.servlet.utils.AttributeValueMap#toDecodedParamString()}.
     */
    public void testToDecodedNoParamString() {
        final AttributeValueMap avm = new AttributeValueMap(Person.class.getName(), "");
        final String decoded = avm.toDecodedParamString();
        Assert.assertEquals("", decoded);
    }

    public void testToDecodedOneParam() {
        final AttributeValueMap avm =
            new AttributeValueMap(Person.class.getName(), Person.PROP_FAMILYNAME + "=Morris");
        final String decoded = avm.toDecodedParamString();
        Assert.assertEquals(Person.PROP_FAMILYNAME + "=Morris", decoded);
    }

    public void testUrlEncoded() {
        final AttributeValueMap avm =
            new AttributeValueMap(Person.class.getName(), Person.PROP_FAMILYNAME + "=<Morris");
        final String decoded = avm.toDecodedParamString();
        Assert.assertEquals(Person.PROP_FAMILYNAME + "=%3CMorris", decoded);
    }

}
