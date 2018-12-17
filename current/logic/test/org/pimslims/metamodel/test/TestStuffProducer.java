/*
 * Created on 24.06.2005
 */
package org.pimslims.metamodel.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.lab.StuffProducer;

/**
 * @author Petr Troshin
 */
public class TestStuffProducer extends TestCase {

    /**
     * Test cases for StuffProducer utility class
     */
    public TestStuffProducer() {
        super("Testing StuffProducer");
    }

    /**
     * static link to the class just to shortening its name
     */
    protected static StuffProducer S;

    /**
     * Ensure all random values are random enough
     * 
     */
    public void testgetRandomNumbers() {
        boolean b = StuffProducer.getboolean();
        Assert.assertTrue("This is not a boolean", (b || !b));
        Assert.assertTrue("This is not a Boolean", "java.lang.Boolean".equals(StuffProducer.getBoolean()
            .getClass().getName()));
        Assert.assertFalse("Two randomly generated ints are equals", (StuffProducer.getint() == StuffProducer
            .getint()));
        Assert.assertFalse("Two randomly generated doubles are equals", ((Double.compare(StuffProducer
            .getdouble(), StuffProducer.getdouble())) == 0));
        Assert.assertFalse("Two randomly generated longs are equals",
            (StuffProducer.getlong() == StuffProducer.getlong()));
        Assert.assertFalse("Two randomly generated floats are equals", ((Float.compare(StuffProducer
            .getfloat(), StuffProducer.getfloat())) == 0));
        Assert.assertFalse("Two randomly generated Longs are equals", StuffProducer.getLong().compareTo(
            StuffProducer.getLong()) == 0);
        Assert.assertFalse("Two randomly generated Doubles are equals", (StuffProducer.getDouble()
            .compareTo(StuffProducer.getDouble())) == 0);
        Assert.assertFalse("Two randomly generated Floats are equals", (StuffProducer.getFloat()
            .compareTo(StuffProducer.getFloat())) == 0);
        Assert.assertFalse("Two randomly generated Ints are equals", StuffProducer.getInt().compareTo(
            StuffProducer.getInt()) == 0);
    }

    /**
     * Test returned string length
     * 
     */
    public void testgetString() {
        Assert.assertTrue("String length is more than it is allowed to be", StuffProducer.getString(10)
            .length() <= 10
            && StuffProducer.getString(10).length() >= 0);
        Assert.assertTrue(StuffProducer.getString(-10).length() == 0);
        // Get string formed from typed characters and digits
        final String typedAndDigitscharacters =
            StuffProducer.getString(StuffProducer.typeAndDigitCharacters, 2000);
        final String nontypedcharacters = "!£$%^&*()+=-><:;@~#|\\?¬`\"'";
        final String digits = "1234567890";
        // Make sure that tested string does not contain nontyped characters
        this.testCharacters(typedAndDigitscharacters, nontypedcharacters);
        // Get string formed from typed characters only
        final String typedcharacters = StuffProducer.getString(StuffProducer.typeCharacters, 2000);
        // Make sure that tested string does not contain nontyped characters and
        // digits
        this.testCharacters(typedcharacters, nontypedcharacters.concat(digits));
    }

    void testCharacters(final String testedStr, final String denyCharacters) {
        for (int i = 0; i < denyCharacters.length(); i++) {
            Assert.assertTrue(testedStr.indexOf(denyCharacters.charAt(i)) < 0);
        }
    }

    /**
     * Test the length of the string in the List
     * 
     */
    public void testList() {
        List l = StuffProducer.getList(10);
        Assert.assertTrue(l.size() >= 0);
        for (final Iterator iter = l.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            Assert.assertTrue(element.length() <= 10);
        }
        l = StuffProducer.getList(-10);
        Assert.assertTrue(l.size() >= 0);
        for (final Iterator iter = l.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            Assert.assertTrue(element.length() == 0);
        }
    }

    public void testMap() {
        final ArrayList ar = new ArrayList();
        ar.add("1212");
        ar.add("aaaa");
        Map m = StuffProducer.getMap(ar);
        Assert.assertTrue(m.size() == 2);
        Assert.assertNotNull(m.get("1212"));
        Assert.assertNotNull(m.get("aaaa"));

        final String[] str = new String[] { "sss", "zzzz", "qq" };
        m = StuffProducer.getMap(str);
        Assert.assertTrue(m.size() == 3);
        Assert.assertNotNull(m.get("sss"));
        Assert.assertNotNull(m.get("zzzz"));
        Assert.assertNotNull(m.get("qq"));

        m = StuffProducer.getMap();
        for (final Iterator iter = m.keySet().iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            final String value = (String) m.get(element);
            Assert.assertNotNull(element);
            Assert.assertNotNull(value);
        }
    }

    public void testCollection() {
        final Collection c = StuffProducer.getCollection(10);
        Assert.assertNotNull(c);
        Assert.assertTrue(c.size() >= 0);
        for (final Iterator iter = c.iterator(); iter.hasNext();) {
            final String element = (String) iter.next();
            Assert.assertTrue(element.length() <= 10);
        }
    }

    public void testArray() {
        final String[] sar = StuffProducer.getStringArray(20);
        Assert.assertNotNull(sar);
        Assert.assertTrue(sar.length != 0);
        for (int i = 0; i < sar.length; i++) {
            final String s = sar[i];
            Assert.assertTrue(s.length() <= 20);
        }
    }

}
