/**
 * pims-model org.pimslims.search ListContains.java
 * 
 * @author cm65
 * @date 12 Aug 2011
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
 * ListContains
 * 
 */
public class ListContains extends AbstractCondition {

    /**
     * Constructor for ListContains
     * 
     * @param string
     */
    public ListContains(String value) {
        super(value, "listContains");
    }

    /**
     * @see org.pimslims.search.Condition#getHQLString(java.lang.String)
     * @return e.g. :value in (synonyms)
     */
    @Override
    public String getHQLString(String hqlName, String aliasName, Serial serial) {
        this.hqlName = hqlName.substring(hqlName.indexOf('.') + 1);
        this.alias = aliasName;
        this.serialNumber = serial.getNextValue();
        return " :" + aliasName + this.serialNumber + " in (" + this.hqlName + ") ";
    }

    /**
     * @see org.pimslims.search.Condition#bindingParameter(org.hibernate.Query, java.lang.String)
     */
    public void bindingParameter(JpqlQuery hqlQuery) {
        hqlQuery.setString(this.alias + this.serialNumber, (String) value);
    }

    /**
     * ListContains.clone
     * 
     * @see org.pimslims.search.AbstractCondition#clone()
     */
    @Override
    public Condition clone() {
        return new ListContains((String) this.value);
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
