/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class InExpression extends BusinessCriterion {

    private final Object[] values;

    public InExpression(String property, Object[] values) {
        super(property);
        this.values = values;
    }
    
    public InExpression(String property, Object object) {
        super(property);
        this.values = new Object[1];
        this.values[0] = object;
    }

    @Override
    public String formatClause() {
        String retVal = "";
        if ((values != null) && (values.length > 0)) {
            retVal = " " + getProperty() + " IN (";

            for (int i = 0; i < values.length - 1; i++) {
                retVal += values[i].toString() + ", ";
            }
            retVal += values[values.length - 1].toString().replaceAll("'", "''");
            retVal += ")";
        }
        return retVal;
    }
}
