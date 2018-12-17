/*
 * Created on 02.09.2005 - Code Style - Code Templates
 */
package org.pimslims.servlet.utils.decorators;

import java.text.SimpleDateFormat;

import org.displaytag.decorator.ColumnDecorator;
import org.pimslims.lab.Utils;

/**
 * @author Petr Troshin TODO implement org.displaytag.decorator.DisplaytagColumnDecorator To find uses of
 *         this, search *.jsp for 'decorator=' Still in use for serach targets
 */
@Deprecated
// no longer used, and does not work after time zone fix
public class Date implements ColumnDecorator {

    /**
     * 
     */
    public Date() {
        super();
    }

    public String decorate(final Object date) {

        if (date == null) {
            return "";
        }
        final SimpleDateFormat sdf = Utils.getDateFormat();

        if (date instanceof java.util.Date) {
            return sdf.format((java.util.Date) date);

        } else if (date instanceof java.sql.Date) {
            return sdf.format((java.sql.Date) date);

        } else if (date instanceof java.util.Calendar) {
            return sdf.format(date);

        } else if (date instanceof String) {
            return (String) date;

        } else {
            throw new UnsupportedOperationException("The supplied data is not a Date but a "
                + date.getClass().getName());
        }
    }

}
