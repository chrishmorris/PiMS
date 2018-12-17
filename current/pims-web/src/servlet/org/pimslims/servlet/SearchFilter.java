/**
 * V5_0-web org.pimslims.servlet Filter.java
 * 
 * @author cm65
 * @date 24 Jul 2013
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents search terms. Used in search pages and reports.
 */
public class SearchFilter {

    final Map<String, Object> criteria;

    /**
     * Constructor for Filter
     * 
     * @param criteria2
     */
    public SearchFilter(final SearchFilter filter) {
        this(filter.criteria);
    }

    /**
     * Constructor for Filter
     */
    public SearchFilter() {
        this(Collections.EMPTY_MAP);
    }

    /**
     * Constructor for Filter
     * 
     * @param criteria
     */
    SearchFilter(final Map<String, Object> criteria) {
        super();
        this.criteria = new HashMap(criteria);
    }

    /**
     * .Filter.put
     * 
     * @param arg0
     * @param arg1
     * @return
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Object put(final String arg0, final Object arg1) {
        return this.criteria.put(arg0, arg1);
    }

    /**
     * TODO make this private .Filter.remove
     * 
     * @param key
     * @return
     * @see java.util.Map#remove(java.lang.Object)
     */
    public void remove(final String key) {
        this.criteria.remove(key);
    }

    /**
     * .Filter.putAll
     * 
     * @param m
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map<? extends String, ? extends Object> m) {
        this.criteria.putAll(m);
    }

    /**
     * TODO probably just need getSearchAll .Filter.get
     * 
     * @param key
     * @return
     * @see java.util.Map#get(java.lang.Object)
     */
    public Object get(final String key) {
        return this.criteria.get(key);
    }

    /**
     * TODO review whether a more specific method is sufficient .Filter.containsKey
     * 
     * @param key
     * @return
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final String key) {
        return this.criteria.containsKey(key);
    }

    /**
     * .Filter.isEmpty
     * 
     * @return
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return this.criteria.isEmpty();
    }

    /**
     * Filter.getMap
     * 
     * @return
     */
    @Deprecated
    // improve the API wherever this is used
    public Map<String, Object> getMap() {
        return this.criteria;
    }

}
