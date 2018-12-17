package org.pimslims.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONObject {

    private final Map<String, Object> map = new HashMap();

    public JSONObject(JSONObject json) {
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            this.put(key, json.get(key));
        }
    }

    public JSONObject() {
    }

    /**
     * @param key
     * @param value String, Number, Boolean, null, JSONArray, JSONObject
     */
    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public boolean getBoolean(String key) {
        return (Boolean) this.map.get(key);
    }

    public Iterator<String> keys() {
        return this.map.keySet().iterator();
    }

    public void putAll(Map<String, ? extends Object> map) {
        this.map.putAll(map);
    }

    @Override
    public String toString() {
        if (this.map.isEmpty()) {
            return "{}";
        }
        String ret = "";
        List<String> keySet = new ArrayList(this.map.keySet());
        Collections.sort(keySet);
        for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            ret += ",\"";
            ret += key.replace("\\", "\\\\").replace("\"", "\\\"");
            ret += "\":";
            ret += JSONArray.toJsonString(map.get(key));
        }
        return "{" + ret.substring(1) + "}";
    }
}
