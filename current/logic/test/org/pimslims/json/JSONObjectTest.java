package org.pimslims.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

public class JSONObjectTest extends TestCase {

    public void testJSONObject() {
        JSONObject json = new JSONObject();
        assertEquals("{}", json.toString());
        assertFalse(json.keys().hasNext());
    }

    public void testPut() {
        JSONObject json = new JSONObject();
        json.put("a", new Integer(1));
        assertEquals(new Integer(1), json.get("a"));
        assertEquals("{\"a\":1}", json.toString());
        Iterator<String> keys = json.keys();
        assertTrue(keys.hasNext());
        assertEquals("a", keys.next());
        assertFalse(keys.hasNext());
    }

    public void testBoolean() {
        JSONObject json = new JSONObject();
        json.put("a", Boolean.TRUE);
        assertEquals(Boolean.TRUE, json.get("a"));
        assertEquals("{\"a\":true}", json.toString());
        Iterator<String> keys = json.keys();
        assertTrue(keys.hasNext());
        assertEquals("a", keys.next());
        assertFalse(keys.hasNext());
    }

    public void testPutAll() {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap();
        map.put("b", Boolean.FALSE);
        json.put("a", new Integer(1));
        json.putAll(map);

        assertEquals(new Integer(1), json.get("a"));
        assertEquals(Boolean.FALSE, json.get("b"));
        Iterator<String> keys = json.keys();
        assertTrue(keys.hasNext());
        keys.next();
        assertTrue(keys.hasNext());
        keys.next();
        assertFalse(keys.hasNext());
        assertEquals("{\"a\":1,\"b\":false}", json.toString());
    }

    public void testQuotesInKey() {
        JSONObject json = new JSONObject();
        json.put("q\"qe\\eeq\\\"eq", Boolean.TRUE);
        assertEquals(Boolean.TRUE, json.get("q\"qe\\eeq\\\"eq"));
        Iterator<String> keys = json.keys();
        assertTrue(keys.hasNext());
        assertEquals("q\"qe\\eeq\\\"eq", keys.next());
        assertEquals("{\"q\\\"qe\\\\eeq\\\\\\\"eq\":true}", json.toString());
    }

    public void testQuotesInValue() {
        JSONObject json = new JSONObject();
        json.put("a", "q\"qe\\eeq\\\"eq");
        assertEquals("q\"qe\\eeq\\\"eq", json.get("a"));
        Iterator<String> keys = json.keys();
        assertTrue(keys.hasNext());
        assertEquals("a", keys.next());
        assertEquals("{\"a\":\"q\\\"qe\\\\eeq\\\\\\\"eq\"}", json.toString());
    }

}
