/**
 * pims-model org.pimslims.search Condition.java
 * 
 * @author bl67
 * @date 26 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
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

import org.pimslims.persistence.JpqlQuery;

/*
 * Condition
 */
public interface Condition {

    /**
     * Condition.getHQLString
     * 
     * @param hqlName column name
     * @param alias prefix of variable name
     * @param serial provides suffix of variable name
     * @return
     */
    public String getHQLString(String hqlName, String alias, Serial serial);

    /**
     * @param hqlQuery
     * @param level
     * @param name
     */
    public void bindingParameter(JpqlQuery hqlQuery);

    public Condition clone();

    /**
     * Condition.matches
     * 
     * @param get_Value
     * @return
     */
    public boolean matches(Object value);

    //TODO public String getAttributeName

}