/**
 * V4_3-web org.pimslims.csv MultiPlateScreenTest.java
 * 
 * @author cm65
 * @date 24 Nov 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.spreadsheet;

import org.pimslims.spreadsheet.MultiPlateScreen;

import junit.framework.Assert;
import junit.framework.TestCase;

/*
 * MultiPlateScreenTest Some multiplate experiments are best reported by a sheet like:
 */
// Plate,Well,Input,...
// BC12323,A01,BC5463:A01,...
// BC12323,A02,BC5463:A02,...
// ...
// BC12324,A01,BC5464:A01,...
// BC12324,A02,BC5464:A02,...
/*
 * In other cases, most of the properties of well A01 are the same each each of the plates; and some other
 * properties are the same for all wells of each plate. This is a "multi plate screening". This class tests
 * the support for a different format of spreadsheet that will be more convenient in this case:
 */
// Plate,,BC12323,BC12324
// Temperature,,35C,21C
// Well,Input,Result,Result
// A01,BC5463:A01,OK,Failed
// A02,BC5463:A02,OK,OK ...
/* 
 */
public class MultiPlateScreenTest extends TestCase {

    /**
     * Constructor for MultiPlateScreenTest
     * 
     * @param name
     */
    public MultiPlateScreenTest(final String name) {
        super(name);
    }

    /**
     * MultiPlateScreenTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * MultiPlateScreenTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSettersGetters() {
        final String value = "value";
        final MultiPlateScreen bean = this.makeBean(value);
        Assert.assertEquals(value, bean.getValue("barcode", "C04", "keyword"));
        Assert.assertEquals(1, bean.getPlates().size());
        Assert.assertEquals("barcode", bean.getPlates().iterator().next());
        Assert.assertEquals(1, bean.getWells().size());
        Assert.assertEquals("C04", bean.getWells().iterator().next());
        Assert.assertEquals(1, bean.getKeywords().size());
        Assert.assertEquals("keyword", bean.getKeywords().iterator().next());

        final MultiPlateScreen bean2 = this.makeBean(value);

        Assert.assertEquals(1, bean.getWellProperties().size());
        Assert.assertTrue(bean.getWellProperties().containsKey("keyword"));

        Assert.assertEquals(1, bean.getPlateProperties().size());
        Assert.assertTrue(bean.getPlateProperties().containsKey("keyword"));

    }

    public void testEquals() {
        Assert.assertEquals(this.makeBean("value"), this.makeBean("value"));
        Assert.assertFalse(this.makeBean("value").equals(this.makeBean("value2")));
        Assert.assertFalse(this.makeBean("value").equals(this.makeBean(null)));
    }

    public void testRepeatedProperty() {
        final MultiPlateScreen bean = new MultiPlateScreen();
        bean.add("barcode1", "C04", "plateProp", "one");
        bean.add("barcode1", "H03", "plateProp", "one");
        bean.add("barcode2", "C04", "plateProp", "two");
        bean.add("barcode2", "H03", "plateProp", "two");
        bean.add("barcode1", "C04", "wellProp", "c4");
        bean.add("barcode1", "H03", "wellProp", "h3");
        bean.add("barcode2", "C04", "wellProp", "c4");
        bean.add("barcode2", "H03", "wellProp", "h3");

        Assert.assertFalse(bean.getWellProperties().containsKey("plateProp"));
        Assert.assertTrue(bean.getWellProperties().containsKey("wellProp"));
        Assert.assertEquals("c4", bean.getWellProperties().get("wellProp").get("C04"));
        Assert.assertEquals("h3", bean.getWellProperties().get("wellProp").get("H03"));

        Assert.assertFalse(bean.getPlateProperties().containsKey("wellProp"));
        Assert.assertTrue(bean.getPlateProperties().containsKey("plateProp"));
        Assert.assertEquals("one", bean.getPlateProperties().get("plateProp").get("barcode1"));
        Assert.assertEquals("two", bean.getPlateProperties().get("plateProp").get("barcode2"));

    }

    public void testAllNull() {
        final MultiPlateScreen bean = this.makeBean(null);
        Assert.assertEquals("", bean.getValue("barcode", "C04", "keyword"));
        Assert.assertEquals(1, bean.getPlates().size());
        Assert.assertEquals("barcode", bean.getPlates().iterator().next());
        Assert.assertEquals(1, bean.getWells().size());
        Assert.assertEquals("C04", bean.getWells().iterator().next());
        Assert.assertEquals(1, bean.getKeywords().size());
        Assert.assertEquals("keyword", bean.getKeywords().iterator().next());

        final MultiPlateScreen bean2 = new MultiPlateScreen();
        bean2.add("barcode", "C04", "keyword", null);
        Assert.assertEquals(bean, bean2);
        Assert.assertEquals(1, bean.getWellProperties().size());
        Assert.assertTrue(bean.getWellProperties().containsKey("keyword"));

        Assert.assertEquals(1, bean.getPlateProperties().size());
        Assert.assertTrue(bean.getPlateProperties().containsKey("keyword"));
        this.assertRoundTrip(bean);

    }

    public void testPartNull() {
        final MultiPlateScreen bean = new MultiPlateScreen();
        bean.add("barcode1", "C04", "plateProp", "one");
        bean.add("barcode1", "H03", "plateProp", null);
        bean.add("barcode2", "C04", "plateProp", "two");
        bean.add("barcode2", "H03", "plateProp", "two");
        bean.add("barcode1", "C04", "wellProp", "c4");
        bean.add("barcode1", "H03", "wellProp", "h3");
        bean.add("barcode2", "C04", "wellProp", null);
        bean.add("barcode2", "H03", "wellProp", "h3");

        Assert.assertFalse(bean.getWellProperties().containsKey("wellProp"));
        Assert.assertFalse(bean.getWellProperties().containsKey("plateProp"));
        Assert.assertFalse(bean.getPlateProperties().containsKey("plateProp"));
        Assert.assertFalse(bean.getPlateProperties().containsKey("wellProp"));

        this.assertRoundTrip(bean);
    }

    private MultiPlateScreen makeBean(final String value) {
        final MultiPlateScreen bean = new MultiPlateScreen();
        bean.add("barcode", "C04", "keyword", value);
        return bean;
    }

    public void testToStringArray() {
        final String[][] array = this.makeBean("value").toStringArray();
        Assert.assertEquals(3, array.length);
        Assert.assertEquals(2, array[0].length);
        Assert.assertEquals("Plate", array[0][0]);
        Assert.assertEquals("barcode", array[0][1]);
        Assert.assertEquals(2, array[1].length);
        Assert.assertEquals("Well", array[1][0]);
        Assert.assertEquals("keyword", array[1][1]);
        Assert.assertEquals(2, array[2].length);
        Assert.assertEquals("C04", array[2][0]);
        Assert.assertEquals("value", array[2][1]);
    }

    public void testWellParm() {
        final MultiPlateScreen bean = new MultiPlateScreen();
        // make a well property
        bean.add("barcode2", "C04", "target", "value");
        bean.add("barcode1", "C04", "target", "value");
        // and a result property
        bean.add("barcode1", "C04", "status", "OK");
        bean.add("barcode2", "C04", "status", "failed");
        bean.add("barcode1", "D01", "status", "TBD");
        bean.add("barcode2", "D01", "status", "TBD");

        final String[][] array = bean.toStringArray();
        Assert.assertEquals(4, array.length);
        Assert.assertEquals(4, array[0].length);
        Assert.assertEquals("", array[0][0]);
        Assert.assertEquals("Plate", array[0][1]);
        Assert.assertEquals("barcode1", array[0][2]);
        Assert.assertEquals("barcode2", array[0][3]);
        Assert.assertEquals(4, array[1].length);
        Assert.assertEquals("Well", array[1][0]);
        Assert.assertEquals("target", array[1][1]);
        Assert.assertEquals("status", array[1][2]);
        Assert.assertEquals("status", array[1][3]);
        Assert.assertEquals(4, array[2].length);
        Assert.assertEquals("C04", array[2][0]);
        Assert.assertEquals("value", array[2][1]);
        Assert.assertEquals("OK", array[2][2]);
        Assert.assertEquals("failed", array[2][3]);

        this.assertRoundTrip(bean);
    }

    public void testPlateParm() {
        final MultiPlateScreen bean = new MultiPlateScreen();
        // make a plate parameter
        bean.add("barcode2", "C04", "target", "value2");
        bean.add("barcode2", "A01", "target", "value2");
        bean.add("barcode1", "C04", "target", "value1");
        bean.add("barcode1", "A01", "target", "value1");
        // make a result parameter
        bean.add("barcode1", "A01", "status", "OK");
        bean.add("barcode1", "C04", "status", "failed");
        bean.add("barcode2", "A01", "status", "TBD");
        bean.add("barcode2", "C04", "status", "TBD");
        final String[][] array = bean.toStringArray();

        Assert.assertEquals(5, array.length);
        Assert.assertEquals(3, array[0].length);
        Assert.assertEquals("Plate", array[0][0]);
        Assert.assertEquals("barcode1", array[0][1]);
        Assert.assertEquals("barcode2", array[0][2]);

        Assert.assertEquals(3, array[1].length);
        Assert.assertEquals("target", array[1][0]);
        Assert.assertEquals("value1", array[1][1]);
        Assert.assertEquals("value2", array[1][2]);

        Assert.assertEquals(3, array[2].length);
        Assert.assertEquals("Well", array[2][0]);
        Assert.assertEquals("status", array[2][1]);

        Assert.assertEquals(3, array[3].length);
        Assert.assertEquals("A01", array[3][0]);
        Assert.assertEquals("OK", array[3][1]);

        Assert.assertEquals(3, array[4].length);
        Assert.assertEquals("C04", array[4][0]);
        Assert.assertEquals("failed", array[4][1]);

        this.assertRoundTrip(bean);
    }

    public void testTwoResults() {
        final MultiPlateScreen bean = new MultiPlateScreen();
        // make a result parameter
        bean.add("barcode1", "A01", "status", "OK");
        bean.add("barcode1", "C04", "status", "failed");
        bean.add("barcode2", "A01", "status", "TBD");
        bean.add("barcode2", "C04", "status", "TBD");
        // make another
        bean.add("barcode2", "C04", "od", "2c");
        bean.add("barcode2", "A01", "od", "2a");
        bean.add("barcode1", "C04", "od", "1c");
        bean.add("barcode1", "A01", "od", "1a");

        final String[][] array = bean.toStringArray();
        Assert.assertEquals(4, array.length);
        Assert.assertEquals(5, array[0].length);
        Assert.assertEquals("Plate", array[0][0]);
        Assert.assertEquals("barcode1", array[0][1]);
        Assert.assertEquals("", array[0][2]);
        Assert.assertEquals("barcode2", array[0][3]);
        Assert.assertEquals("", array[0][4]);

        Assert.assertEquals(5, array[1].length);
        Assert.assertEquals("Well", array[1][0]);
        Assert.assertEquals("od", array[1][1]);
        Assert.assertEquals("status", array[1][2]);
        Assert.assertEquals("od", array[1][3]);
        Assert.assertEquals("status", array[1][4]);

        Assert.assertEquals(5, array[2].length);
        Assert.assertEquals("A01", array[2][0]);
        Assert.assertEquals("1a", array[2][1]);
        Assert.assertEquals("OK", array[2][2]);
        Assert.assertEquals("2a", array[2][3]);
        Assert.assertEquals("TBD", array[2][4]);

        Assert.assertEquals(5, array[3].length);
        Assert.assertEquals("C04", array[3][0]);
        Assert.assertEquals("1c", array[3][1]);
        Assert.assertEquals("failed", array[3][2]);
        Assert.assertEquals("2c", array[3][3]);
        Assert.assertEquals("TBD", array[3][4]);

        this.assertRoundTrip(bean);
    }

    private void assertRoundTrip(final MultiPlateScreen bean) {
        Assert.assertEquals(bean, new MultiPlateScreen(bean.toStringArray()));
    }
}
