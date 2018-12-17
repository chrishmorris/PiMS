/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 *
 * @author ian
 */
public class BusinessOrder extends BusinessCriterion {
    private final boolean ascending;

    public BusinessOrder(String property, boolean ascending) {
        super(property);
        this.ascending = ascending;
    }

    public static final BusinessOrder ASC(String property) {
        return new BusinessOrder(property, true);
    }
    public static final BusinessOrder DESC(String property) {
        return new BusinessOrder(property, false);
    }
        
    public boolean isAscending() {
        return ascending;
    }
    
    @Override
    public String formatClause() {
        return getProperty() + (ascending ? " ASC " : " DESC ");
    }    
}
