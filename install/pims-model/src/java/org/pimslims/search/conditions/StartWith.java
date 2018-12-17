/**
 * pims-model org.pimslims.search.conditions GreaterThan.java
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
package org.pimslims.search.conditions;

import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.AbstractCondition;
import org.pimslims.search.Serial;

/**
 * GreaterThan
 * 
 */
public class StartWith extends AbstractCondition {

    /**
     * @param value
     * @param operator
     */
    public StartWith(String value) {
        super(value, "like");

    }

    /**
     * @see org.pimslims.search.Condition#getHQLString(java.lang.String)
     */
    @Override
    public String getHQLString(String hqlName, String aliasName, Serial serial) {
        super.getHQLString(hqlName, aliasName, serial);
        return " lower( " + this.hqlName + ") " + operator + " :" + this.alias + this.serialNumber;
    }

    /**
     * @see org.pimslims.search.Condition#bindingParameter(org.hibernate.Query, java.lang.String)
     */
    public void bindingParameter(JpqlQuery hqlQuery) {
        hqlQuery.setParameter(this.alias + this.serialNumber, value.toString().toLowerCase() + "%");

    }

    @Override
    public StartWith clone() {
        return new StartWith((String) value);
    }

    /**
     * StartWith.matches
     * 
     * @see org.pimslims.search.Condition#matches(java.lang.Object)
     */
    public boolean matches(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

}
