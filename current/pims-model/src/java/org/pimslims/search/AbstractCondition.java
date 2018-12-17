/**
 * pims-model org.pimslims.search AbstractCondition.java
 * 
 * @author bl67
 * @date 11 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search;

import org.pimslims.metamodel.ModelObject;

/**
 * AbstractCondition
 * 
 */
public abstract class AbstractCondition implements Condition, Cloneable {
    protected final Object value;

    protected final String operator;

    protected final String attributeName;

    protected String alias;

    protected String hqlName;

    protected int serialNumber;

    //can not be directly instantiated
    public AbstractCondition(Object value, String operator) {
        super();
        this.value = value;
        this.operator = operator;
        this.attributeName = null; // TODO start using it, allows better API
    }

    /**
     * @see org.pimslims.search.Condition#getHQLString(java.lang.String)
     */
    public String getHQLString(String hqlName, String aliasName, Serial serial) {
        if (value != null && value instanceof ModelObject) {
            this.hqlName = hqlName.substring(hqlName.indexOf('.') + 1);
        } else
            this.hqlName = hqlName;

        this.alias = aliasName;
        this.serialNumber = serial.getNextValue();
        return " " + this.hqlName + " " + operator + " :" + alias + this.serialNumber + " ";

    }

    @Override
    public abstract Condition clone();
}
