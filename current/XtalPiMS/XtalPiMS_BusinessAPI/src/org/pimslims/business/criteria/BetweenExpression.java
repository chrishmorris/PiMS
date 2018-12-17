/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author ian
 */
public class BetweenExpression extends BusinessCriterion {

    private final Object start;
    private final Object end;

    @SuppressWarnings("unchecked")
    public BetweenExpression(String property, Object start, Object end) {
        super(property);
        this.start = start;
        this.end = end;
    }

    @Override
    public String formatClause() {
        String retVal = " " + getProperty() + " BETWEEN ";
        //Might have to enclose strings in quotes...
        if (getStart() instanceof String) {
            retVal += "'" + getStart().toString().replaceAll("'", "''") + "' AND '" + getEnd().toString().replaceAll("'", "''") + "'";
        } else if (getStart() instanceof Calendar) {
            Date startDate = new Date(((Calendar)start).getTimeInMillis());
            Date endDate = new Date(((Calendar)end).getTimeInMillis());
            retVal += "'" + startDate.toString() + "' AND '" + endDate.toString() + "'";
        } else {
            retVal += getStart().toString().replaceAll("'", "''") + " AND " + getEnd().toString().replaceAll("'", "''");
        }
        
        return retVal;
    }

    public Object getStart() {
        return start;
    }

    public Object getEnd() {
        return end;
    }

}
