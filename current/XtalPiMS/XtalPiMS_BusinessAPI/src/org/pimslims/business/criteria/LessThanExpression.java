/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.criteria;

import java.util.Calendar;

/**
 * 
 * @author ian
 */
public class LessThanExpression extends BusinessCriterion {
    private final Object value;

    private final boolean eq;

    public LessThanExpression(String property, Object value, boolean eq) {
        super(property);
        this.value = value;
        this.eq = eq;
    }

    public Object getValue() {
        return value;
    }

    public boolean isEq() {
        return eq;
    }

    @Override
    public String formatClause() {
        String retVal = " " + getProperty() + " <";
        if (eq) {
            retVal += "=";
        }
        if ((value instanceof String) || (value instanceof Calendar)) {
            //TODO this is not right for Calendar, see GreaterThanExpression
            retVal += " '" + value.toString().replaceAll("'", "''") + "'";
        } else {
            retVal += value.toString().replaceAll("'", "''");
        }

        return retVal;
    }

}
