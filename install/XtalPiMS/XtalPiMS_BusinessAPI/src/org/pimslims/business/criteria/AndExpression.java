/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class AndExpression extends BusinessCriterion {
    private final BusinessCriterion criteria1;
    private final BusinessCriterion criteria2;

    public AndExpression(String property, BusinessCriterion criteria1, BusinessCriterion criteria2) {
        super(property);
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
    }

    public BusinessCriterion getCriteria1() {
        return criteria1;
    }

    public BusinessCriterion getCriteria2() {
        return criteria2;
    }

    @Override
    public String formatClause() {
        return "(" + criteria1.formatClause() + " AND " + criteria2.formatClause() + ")";
    }
    
}
