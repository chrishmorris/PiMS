/**
 * DM org.pimslims.metamodel Paging.java
 * 
 * @author bl67
 * @date 21 Feb 2008
 * 
 * Protein Information Management System
 * @version: 2.1
 * 
 * Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 * 
 * A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.search;

import java.util.LinkedHashMap;

/**
 * Paging
 * 
 */
public class Paging {
    private static String DBID = "dbId";

    /**
     * search options: start and limit of results to show
     */
    public final static int UNLIMITED = Integer.MAX_VALUE;

    public enum Order {
        DESC, ASC
    }

    /**
     * index in list of first object to return
     */
    private int start = 0;

    /**
     * maximum number of objects to return
     */
    private int limit = 40;

    /**
     * names of attribute to sort
     */
    private LinkedHashMap<String, Order> orderBy = new LinkedHashMap<String, Order>();

    public Paging(int start, int limit) {
        this.start = start;
        this.limit = limit;
        //by default, sorted by dbid
        orderBy.put(DBID, Order.DESC);
    }

    public Paging(int start, int limit, LinkedHashMap<String, Order> orderBy) {
        this.start = start;
        this.limit = limit;
        setOrderBy(orderBy);
    }

    public Paging(int start, int limit, String orderByAttribute, Order order) {
        this.start = start;
        this.limit = limit;
        LinkedHashMap<String, Order> orderByToSet = new LinkedHashMap<String, Order>();
        orderByToSet.put(orderByAttribute, order);
        setOrderBy(orderByToSet);
    }

    /**
     * @return Returns the start.
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start The start to set.
     * @return
     */
    public Paging setStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * @return Returns the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit The limit to set.
     * @return
     */
    public Paging setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * @return Returns the orderBy.
     */
    public LinkedHashMap<String, Order> getOrderBy() {
        return new LinkedHashMap<String, Order>(orderBy);
    }

    /**
     * @param orderBy The orderBy to set.
     * @return
     */
    public Paging setOrderBy(LinkedHashMap<String, Order> orderBy) {
        if (orderBy == null)
            this.orderBy = new LinkedHashMap<String, Order>();
        else
            this.orderBy = new LinkedHashMap<String, Order>(orderBy);
        return this;
    }

    /**
     * add an orderBy
     * 
     * @param order
     */
    public Paging addOrderBy(String orderBy, Order order) {

        if (this.orderBy.size() == 1 && this.orderBy.containsKey(DBID)) {
            this.orderBy.remove(DBID);
            this.orderBy.put(orderBy, order);
        }
        this.orderBy.put(orderBy, order);
        return this;
    }

}
