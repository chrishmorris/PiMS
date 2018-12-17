/**
 * V4_3-web org.pimslims.presentation.create ValueConverterTest.java
 * 
 * @author cm65
 * @date 10 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.create;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.HolderType;
import org.pimslims.presentation.servlet.utils.ValueFormatter;

/**
 * ValueConverterTest
 * 
 */
public class ValueConverterTest extends TestCase {

    private final AbstractModel model;

    public ValueConverterTest(final String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void test() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Experiment.class.getName());
        try {
            new ValueConverter(metaClass, map).getConvertedValue("nonesuch", null);
            Assert.fail();
        } catch (final IllegalArgumentException e) {
            // that's right
        }
    }

    public void testBadDate() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Experiment.class.getName());
        new ValueConverter(metaClass, map).getConvertedValue(Experiment.PROP_ENDDATE, "xxxx");
        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(Experiment.PROP_ENDDATE));
        Assert.assertEquals("The date is incorrect format", map.get(Experiment.PROP_ENDDATE));
    }

    public void testOldDate() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Experiment.class.getName());
        final String date = ValueFormatter.formatDate(Calendar.getInstance());
        final Object value =
            new ValueConverter(metaClass, map).getConvertedValue(Experiment.PROP_ENDDATE, date);
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(value instanceof Calendar);
        Assert.assertEquals(date, ValueFormatter.formatDate((Calendar) value));
    }

    public void testTimestamp() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Experiment.class.getName());
        final Calendar date = Calendar.getInstance();
        final Object value =
            new ValueConverter(metaClass, map).getConvertedValue(Experiment.PROP_ENDDATE,
                Long.toString(date.getTimeInMillis()));
        Assert.assertEquals(0, map.size());
        Assert.assertTrue(value instanceof Calendar);
        Assert.assertEquals(date, value);
    }

    public void testName() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Experiment.class.getName());
        final Object convertedValue =
            new ValueConverter(metaClass, map).getConvertedValue(Experiment.PROP_NAME, "myname");
        Assert.assertEquals("myname", convertedValue);

    }

    public void testNoInt() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(HolderType.class.getName());
        Assert.assertNull(new ValueConverter(metaClass, map).getConvertedValue(HolderType.PROP_MAXROW, ""));

    }

    public void testNoIntTrimmed() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(HolderType.class.getName());
        Assert
            .assertNull(new ValueConverter(metaClass, map).getConvertedValue(HolderType.PROP_MAXROW, "   "));

    }

    public void testInt() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(HolderType.class.getName());
        final Object convertedValue =
            new ValueConverter(metaClass, map).getConvertedValue(HolderType.PROP_MAXROW, "12");
        Assert.assertEquals(new Integer(12), convertedValue);

    }

    public void testBadInt() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(HolderType.class.getName());
        final Object convertedValue =
            new ValueConverter(metaClass, map).getConvertedValue(HolderType.PROP_MAXROW, "twelve");

        Assert.assertEquals(1, map.size());
        Assert.assertTrue(map.containsKey(HolderType.PROP_MAXROW));
        Assert.assertEquals("Please enter a number instead of: twelve", map.get(HolderType.PROP_MAXROW));
    }

    public void testStringList() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final Object convertedValue =
            new ValueConverter(metaClass, map).getConvertedValue(Organisation.PROP_ADDRESSES, "STFC\nDL");
        Assert.assertTrue(convertedValue instanceof List);
        Assert.assertEquals(2, ((Collection) convertedValue).size());
    }

    public void testNullList() {
        final Map map = new HashMap();
        final MetaClass metaClass = this.model.getMetaClass(Organisation.class.getName());
        final Object convertedValue =
            new ValueConverter(metaClass, map).getConvertedValue(Organisation.PROP_ADDRESSES, null);
        Assert.assertTrue(convertedValue instanceof List);
        Assert.assertEquals(0, ((Collection) convertedValue).size());

    }

}
