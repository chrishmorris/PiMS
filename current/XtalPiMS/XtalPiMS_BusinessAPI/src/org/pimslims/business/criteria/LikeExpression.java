/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.business.criteria;

/**
 * 
 * @author ian
 */
public class LikeExpression extends BusinessCriterion {

    private final String value;

    private final boolean like;

    private final boolean caseInsensitive;

    public LikeExpression(String property, String value, boolean like, boolean caseInsensitive) {
        super(property);
        this.value = value;
        this.like = like;
        this.caseInsensitive = caseInsensitive;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        String valueString = " '%" + value.replaceAll("'", "''") + "%' ";
        String keyword;
        if (!isLike()) {
            keyword = " NOT LIKE ";
        } else {
            keyword = " LIKE ";
        }
        String output;
        if (caseInsensitive) {
            output = " lower(" + getProperty() + ")" + keyword + " lower(" + valueString + ") ";
        } else {
            output = getProperty() + keyword + valueString;
        }
        return output;
    }

    public boolean isLike() {
        return like;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    @Override
    public String formatClause() {
        return toString();
    }
}
