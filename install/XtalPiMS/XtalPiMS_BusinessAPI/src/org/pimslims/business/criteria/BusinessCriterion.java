/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.business.criteria;

/**
 * 
 * @author ian
 */
public abstract class BusinessCriterion {
    private String property;

    public BusinessCriterion(final String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public abstract String formatClause();

    public void setProperty(final String property) {
        this.property = property;
    }

    /**
     * BusinessCriterion.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return formatClause();
    }
}
