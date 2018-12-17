/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class NotExpression extends BusinessCriterion {
    private final BusinessCriterion criterion;

    public NotExpression(String property, BusinessCriterion criterion) {
        super(property);
        this.criterion = criterion;
    }

    public BusinessCriterion getCriterion() {
        return criterion;
    }

    @Override
    public String formatClause() {
        return " NOT (" + criterion.formatClause() + ")";
    }
}
