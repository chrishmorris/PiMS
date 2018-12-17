/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.criteria;

import java.sql.Date;
import java.util.Calendar;

/**
 * 
 * @author ian
 */
public class GreaterThanExpression extends BusinessCriterion {
    private final Object value;

    private final boolean eq;

    public GreaterThanExpression(String property, Object value, boolean eq) {
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
        String retVal = " " + getProperty() + " >";
        if (eq) {
            retVal += "=";
        }
        if ((value instanceof String)) {
            retVal += " '" + value.toString().replaceAll("'", "''") + "'";
        } else if ((value instanceof Calendar)) {
            retVal += new Date(((Calendar) value).getTimeInMillis()).toString();
            /* TODO no, for Oracle this is wrong. See Rhombix PlateInspectionServiceImpl */
        } else {
            retVal += value.toString().replaceAll("'", "''");
        }

        return retVal;
    }
}
