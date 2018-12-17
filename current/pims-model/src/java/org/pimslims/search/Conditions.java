/**
 * DM org.pimslims.hibernate Restriction.java
 * 
 * @author bl67
 * @date 8 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 2.1
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.conditions.AndCondition;
import org.pimslims.search.conditions.Between;
import org.pimslims.search.conditions.Contain;
import org.pimslims.search.conditions.GeneralCondition;
import org.pimslims.search.conditions.ListContains;
import org.pimslims.search.conditions.NullCondition;
import org.pimslims.search.conditions.OrCondition;
import org.pimslims.search.conditions.StartWith;

/**
 * Restriction to compare date and number , like a simplified org.hibernate.criterion.Restrictions This class
 * is also a factory
 * 
 */
public abstract class Conditions {

    public static final String OR = "OR";

    public static final String AND = "AND";

    public static final String NOTEXISTS = "Not Exists";

    protected static final String NOT_EQUALS = "!=";

    protected static final String EQUALS = "=";

    protected static final String LESSTHAN = "<";

    protected static final String GREATERTHAN = ">";

    protected static final String ISNULL = "is null";

    protected static final String NOTNULL = "is not null";

    /**
     * Apply a "= value"
     * 
     * @param value
     * @return
     */
    public static Condition eq(Object value) {
        if (value == null || "".equals(value) // Oracle treats empty string as null, so Pims does too 
        )
            return isNull();
        //else
        return new GeneralCondition(value, EQUALS) {
            public boolean matches(Object value) {
                return this.value.equals(value);
            }
        };
    }

    /**
     * Apply a "!= value"
     * 
     * @param value
     * @return
     */
    public static Condition notEquals(Object value) {
        return new GeneralCondition(value, NOT_EQUALS) {
            public boolean matches(Object value) {
                return !this.value.equals(value);
            }
        };
    }

    /**
     * Apply a ">value"
     * 
     * @param value
     * @return
     */
    public static Condition greaterThan(Comparable value) {
        return new GeneralCondition(value, GREATERTHAN) {
            public boolean matches(Object value) {
                return -1 == ((Comparable) this.value).compareTo(value);
            }
        };
    }

    /**
     * Apply a ">=value"
     * 
     * @param value
     * @return
     */
    public static Condition greaterOrEquals(Object value) {
        return new GeneralCondition(value, GREATERTHAN + EQUALS) {
            public boolean matches(Object value) {
                return 0 >= ((Comparable) this.value).compareTo(value);
            }
        };
    }

    /**
     * Apply a "<value"
     * 
     * @param value
     * @return
     */
    public static Condition lessThan(Object value) {
        return new GeneralCondition(value, LESSTHAN) {
            public boolean matches(Object value) {
                return 1 == ((Comparable) this.value).compareTo(value);
            }
        };
    }

    /**
     * Apply a "<=value"
     * 
     * @param value
     * @return
     */
    public static Condition lessOrEquals(Object value) {
        return new GeneralCondition(value, LESSTHAN + EQUALS) {
            public boolean matches(Object value) {
                return 0 <= ((Comparable) this.value).compareTo(value);
            }
        };
    }

    /**
     * @return
     */
    public static Condition notNull() {
        return new NullCondition(NOTNULL) {
            public boolean matches(Object value) {
                return null != value;
            }
        };
    }

    /**
     * @return
     */
    public static Condition isNull() {
        return new NullCondition(ISNULL) {

            public boolean matches(Object value) {
                return null == value;
            }
        };
    }

    public static Condition contains(String value) {
        return new Contain(value);
    }

    public static Condition startWith(String value) {
        return new StartWith(value);
    }

    /**
     * @param dateToCompare
     * @param futureDate1
     * @return
     */
    public static Condition between(Object value1, Object value2) {
        return new Between(value1, value2);
    }

    public static Condition or(Collection<Condition> conditions) {
        return new OrCondition(conditions);
    }

    public static Condition or(Condition condition1, Condition condition2) {
        Collection<Condition> conditions = new LinkedList<Condition>();
        conditions.add(condition1);
        conditions.add(condition2);
        return or(conditions);
    }

    public static Condition and(Collection<Condition> conditions) {
        return new AndCondition(conditions);
    }

    public static Condition and(Condition condition1, Condition condition2) {
        Collection<Condition> conditions = new LinkedList<Condition>();
        conditions.add(condition1);
        conditions.add(condition2);
        return and(conditions);
    }

    public static Map<String, Object> orMap(Map<String, Object> criteria) {
        Map<String, Object> orMap = new HashMap<String, Object>();
        orMap.put(OR, criteria);
        return orMap;
    }

    /**
     * Conditions.andMap
     * 
     * @param criteria - must be non-empty
     * @return map AND -> criteria
     */
    public static Map<String, Object> andMap(Map<String, Object> criteria) {
        Map<String, Object> andMap = new HashMap<String, Object>();
        andMap.put(AND, criteria);
        return andMap;
    }

    /**
     * Conditions.newMap
     * 
     * @param key
     * @param value
     * @return Map key -> value
     */
    public static Map<String, Object> newMap(String key, Object value) {
        Map newMap = new HashMap<String, Object>();
        newMap.put(key, value);
        return newMap;
    }

    /**
     * Conditions.notExistsMap
     * 
     * @param propExperiments
     * @param expCriteria
     * @return
     */
    public static Map<String, Object> notExistsMap(Map<String, Object> criteria) {
        Map<String, Object> notExistsMap = new HashMap<String, Object>();
        notExistsMap.put(NOTEXISTS, criteria);
        return notExistsMap;
    }

    /**
     * Conditions.listContains, e.g. for synonyms of a molecule
     * 
     * @param string
     */
    public static AbstractCondition listContains(String string) {
        return new ListContains(string);
    }

    /**
     * Conditions.isEmpty for an association
     * 
     * @param string
     */
    public static Condition isEmpty() {
        return new Condition() {

            @Override
            public String getHQLString(String hqlName, String alias, Serial serial) {
                return " " + hqlName + " is empty ";
            }

            @Override
            public void bindingParameter(JpqlQuery hqlQuery) {
            }

            @Override
            public boolean matches(Object value) {
                // TODO Auto-generated method stub
                return false;
            }

            /**
             * .clone
             * 
             * @see java.lang.Object#clone()
             */
            @Override
            public Condition clone() {
                return this; // immutable
            }
        };
    }

    /**
     * Conditions.isNotEmpty for an association
     * 
     * @param string
     */
    public static Condition isNotEmpty() {
        return new Condition() {

            @Override
            public String getHQLString(String hqlName, String alias, Serial serial) {
                return " " + hqlName + " is not empty ";
            }

            @Override
            public void bindingParameter(JpqlQuery hqlQuery) {
            }

            @Override
            public boolean matches(Object value) {
                // TODO Auto-generated method stub
                return false;
            }

            /**
             * .clone
             * 
             * @see java.lang.Object#clone()
             */
            @Override
            public Condition clone() {
                return this; // immutable
            }
        };
    }
}
