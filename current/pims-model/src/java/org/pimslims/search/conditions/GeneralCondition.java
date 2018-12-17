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

public abstract class GeneralCondition extends AbstractCondition {

    /**
     * @param value
     * @param operator
     */
    public GeneralCondition(Object value, String operator) {
        super(value, operator);
        assert value != null;
    }

    /**
     * @see org.pimslims.search.Condition#bindingParameter(org.hibernate.Query, java.lang.String)
     */
    public void bindingParameter(JpqlQuery hqlQuery) {
        assert null != this.alias;
        if ("".equals(value)) {
            hqlQuery.setParameter(this.alias + this.serialNumber, null);
        } else {
            hqlQuery.setParameter(this.alias + this.serialNumber, value);
        }

    }

    @Override
    public GeneralCondition clone() {
        return this; // immutable
    }

}
