package org.pimslims.json;

import java.util.ArrayList;
import java.util.Iterator;

public class JSONArray {

    final private ArrayList<Object> array = new ArrayList();

    public JSONArray() {
        super();
    }

    /**
     * @param value String, Number, Boolean, null, JSONArray, JSONObject
     */
    public void add(Object value) {
        this.array.add(value);
    }

    public Object[] toArray() {
        return this.array.toArray();
    }

    @Override
    public String toString() {
        if (this.array.isEmpty()) {
            return "[]";
        }
        String ret = "";
        for (Iterator iterator = this.array.iterator(); iterator.hasNext();) {
            ret += ",";
            Object object = iterator.next();
            ret += toJsonString(object);
        }
        return "[" + ret.substring(1) + "]";
    }

    /**
     * JSONArray.toJsonString
     * 
     * @param ret
     * @param object
     * @return
     */
    static String toJsonString(Object object) {
        String ret = "";
        if (object instanceof String) {
            ret += "\"";
            ret += ((String) object).replace("\\", "\\\\").replace("\"", "\\\"");
            ret += "\"";
        } else {
            ret += object;
        }
        return ret;
    }

}
