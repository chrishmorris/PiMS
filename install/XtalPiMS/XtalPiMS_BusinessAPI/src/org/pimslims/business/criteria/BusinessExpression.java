/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class BusinessExpression {
    /**
     * Private constructor.
     */
    private BusinessExpression() {
    }
    /**
     * And.
     * @param property
     * @param criteria1
     * @param criteria2
     * @return
     */
    public static final BusinessCriterion And(String property, BusinessCriterion criteria1, BusinessCriterion criteria2) {
        return new AndExpression(property, criteria1, criteria2);
    }
    public static final BusinessCriterion Or(String property, BusinessCriterion criteria1, BusinessCriterion criteria2) {
        return new OrExpression(property, criteria1, criteria2);
    }
    public static final BusinessCriterion Not(String property, BusinessCriterion criteria) {
        return new NotExpression(property, criteria);
    }
    public static final BusinessCriterion Null(String property, boolean not) {
        return new NullExpression(property, not);
    }
    @SuppressWarnings("unchecked")
    public static final BusinessCriterion Equals(String property, Object obj, boolean not) {
        return new EqualsExpression(property, obj, not);
    }
    @SuppressWarnings("unchecked")
    public static final BusinessCriterion GreaterThan(String property, Object obj, boolean eq) {
        return new GreaterThanExpression(property, obj, eq);
    }
    @SuppressWarnings("unchecked")
    public static final BusinessCriterion LessThan(String property, Object obj, boolean eq) {
        return new LessThanExpression(property, obj, eq);
    }
    @SuppressWarnings("unchecked")
    public static final BusinessCriterion Like(String property, String obj, boolean not, boolean caseInsensitive) {
        return new LikeExpression(property, obj, not, caseInsensitive);
    }
    
    @SuppressWarnings("unchecked")
    public static final BusinessCriterion Between(String property, Object criteria1, Object criteria2) {
        return new BetweenExpression(property, criteria1, criteria2);
    }
    
    public static final BusinessCriterion In(String property, Object[] values) {
        return new InExpression(property, values);
    }
     
    public static final BusinessCriterion In(String property, Object value) {
        return new InExpression (property, value);
    }
}
