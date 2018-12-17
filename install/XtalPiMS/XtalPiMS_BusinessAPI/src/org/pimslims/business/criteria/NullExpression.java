/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class NullExpression extends BusinessCriterion {
    private final boolean not;

    public NullExpression(String property, boolean not) {
        super(property);
        this.not = not;
    }

    public boolean isNot() {
        return not;
    }

    @Override
    public String formatClause() {
        String retVal = " " + getProperty() + " IS";
        if (not) {
            retVal += " NOT";
        } 
        retVal += " NULL";
        
        return retVal;
    }

}
