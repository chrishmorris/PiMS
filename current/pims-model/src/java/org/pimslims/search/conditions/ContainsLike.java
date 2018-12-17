/**
 * pims-model org.pimslims.search.conditions ContainsLike.java
 * 
 * @author cm65
 * @date 15 Aug 2011
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search.conditions;

import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.AbstractCondition;
import org.pimslims.search.Condition;
import org.pimslims.search.Serial;

/**
 * ContainsLike
 * 
 */
public class ContainsLike extends AbstractCondition {

    /**
     * @param value
     * @param operator
     */
    public ContainsLike(String value) {
        super(value, "like");

    }

    public void bindingParameter(JpqlQuery hqlQuery) {
        hqlQuery.setParameter(this.alias + this.serialNumber, "%" + value.toString().toLowerCase() + "%");
    }

    @Override
    public String getHQLString(String hqlName, String aliasName, Serial serial) {
        this.hqlName = hqlName.substring(hqlName.indexOf('.') + 1);
        this.alias = aliasName;
        this.serialNumber = serial.getNextValue();
        return " lower( " + this.hqlName + ") " + operator + " :" + this.alias + this.serialNumber;
    }

    @Override
    public Condition clone() {
        return new ContainsLike((String) value);
    }

    /**
     * Between.matches
     * 
     * @see org.pimslims.search.Condition#matches(java.lang.Object)
     */
    public boolean matches(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

}
