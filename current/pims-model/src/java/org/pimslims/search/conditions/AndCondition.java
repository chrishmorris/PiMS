/**
 * pims-model org.pimslims.search.conditions Or.java
 * 
 * @author bl67
 * @date 10 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.search.conditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.pimslims.persistence.JpqlQuery;
import org.pimslims.search.AbstractCondition;
import org.pimslims.search.Condition;
import org.pimslims.search.Serial;

/**
 * Or condition
 * 
 */
public class AndCondition extends AbstractCondition {

    private Collection<Condition> conditions;

    /**
     * Constructor for Or
     * 
     * @param conditions
     */
    public AndCondition(Collection<Condition> conditions) {
        super(null, "and");
        this.conditions = conditions;
    }

    /**
     * Or.bindingParameter
     * 
     * @see org.pimslims.search.Condition#bindingParameter(org.hibernate.Query, int)
     */
    public void bindingParameter(JpqlQuery hqlQuery) {
        for (Condition condition : conditions) {
            condition.bindingParameter(hqlQuery);
        }

    }

    /**
     * Or.getHQLString
     * 
     * @see org.pimslims.search.Condition#getHQLString(java.lang.String, java.lang.String, int)
     */
    @Override
    public String getHQLString(String hqlName, String alias, Serial serial) {
        String hqlString = "";
        for (Condition condition : conditions) {
            hqlString += condition.getHQLString(hqlName, alias, serial) + " and ";
        }
        hqlString = hqlString.substring(0, hqlString.lastIndexOf(" and "));
        return "(" + hqlString + ")";
    }

    @Override
    public AndCondition clone() {
        Collection<Condition> newConditions = new LinkedList<Condition>();
        for (Condition condition : conditions) {
            newConditions.add(condition.clone());
        }
        return new AndCondition(newConditions);
    }

    /**
     * AndCondition.matches
     * 
     * @see org.pimslims.search.Condition#matches(java.lang.Object)
     */
    public boolean matches(Object value) {
        Collection<Condition> conditions2 = this.conditions;
        for (Iterator iterator = conditions2.iterator(); iterator.hasNext();) {
            Condition condition = (Condition) iterator.next();
            if (!condition.matches(value)) {
                return false;
            }
        }
        return true;
    }

}
