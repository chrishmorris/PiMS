package org.pimslims.json;

import junit.framework.TestCase;

public class JSONArrayTest extends TestCase {

    public void testEmpty() {
        JSONArray json = new JSONArray();
        assertEquals("[]", json.toString());
        Object[] array = json.toArray();
        assertEquals(0, array.length);
    }

    public void testNull() {
        JSONArray json = new JSONArray();
        json.add(null);
        assertEquals("[null]", json.toString());
        Object[] array = json.toArray();
        assertEquals(1, array.length);
        //TODO assertNull(array[0]);
    }

    public void testString() {
        JSONArray json = new JSONArray();
        json.add("a b");
        assertEquals("[\"a b\"]", json.toString());
        Object[] array = json.toArray();
        assertEquals(1, array.length);
        assertEquals("a b", array[0]);
    }

    public void testQuotes() {
        JSONArray json = new JSONArray();
        json.add("q\"qe\\eeq\\\"eq");
        assertEquals("[\"q\\\"qe\\\\eeq\\\\\\\"eq\"]", json.toString());
        Object[] array = json.toArray();
        assertEquals(1, array.length);
        assertEquals("q\"qe\\eeq\\\"eq", array[0]);
    }

    public void testNumber() {
        JSONArray json = new JSONArray();
        json.add(new Integer(1));
        assertEquals("[1]", json.toString());
        Object[] array = json.toArray();
        assertEquals(1, array.length);
        assertEquals(new Integer(1), array[0]);
    }

    public void testTwo() {
        JSONArray json = new JSONArray();
        json.add(new Integer(1));
        json.add(new Integer(2));
        assertEquals("[1,2]", json.toString());
        Object[] array = json.toArray();
        assertEquals(2, array.length);
        assertEquals(new Integer(1), array[0]);
        assertEquals(new Integer(2), array[1]);
    }

}
