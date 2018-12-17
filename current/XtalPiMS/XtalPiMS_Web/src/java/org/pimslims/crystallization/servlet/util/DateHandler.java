/**
 * 
 */
package org.pimslims.crystallization.servlet.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.exception.BusinessException;

/**
 * Date request param handling functions.
 * 
 * NB DateHandler is NOT thread-safe. Instance per request, please!
 * 
 * @author Jon Diprose
 *
 */
public class DateHandler {

	/**
	 * Parser for xtalPiMS standard stringified date format (dd/MM/yyyy)
	 */
	private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Calendar handling is broken in GreaterThan and LessThan, so
	 * need so send as pre-formatted string.
	 */
	private final DateFormat sqldf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Parse a date request param in xtalPiMS standard format (dd/MM/yyyy)
	 * into a Calendar.
	 * 
	 * @param date - the date request param
	 * @return The Calendar representation of date
	 * @throws ParseException If date does not conform to dd/MM/yyyy
	 */
	public Calendar parseDate(String date) throws ParseException {
		Calendar cal = null;
		if ((null != date) && (!"".equals(date))) {
			final Date tmp = df.parse(date);
			cal = Calendar.getInstance();
			cal.setTime(tmp);
			// TODO If Calendar handling is fixed in LessThan/GreaterThan,
			// then may need to ensure that cal represents midnight on the
			// specified date.
		}
		return cal;
	}

	/**
	 * Add the appropriate BusinessCriterions to the specified BusinessCriteria
	 * to restrict to start <= dateProperty < end. Note that the implementation
	 * gives the effect of allowing dateProperty when dateProperty >= midnight in
	 * the morning of start and < midnight in the evening of end, ie start is the
	 * earliest date that can be returned and end is the latest.
	 * 
	 * @param criteria - the criteria to modify
	 * @param dateProperty - the property on which to restrict
	 * @param start - the earliest date that can be returned
	 * @param end - the latest date that can be returned
	 * @throws BusinessException If something goes wrong
	 */
	public void addCriterion(BusinessCriteria criteria, String dateProperty, Calendar start, Calendar end) throws BusinessException {

		/*
		 * NB The between implementation is deficient for dates because
		 * between is exclusive. This means that on the dot of midnight
		 * at the start of the range will be incorrectly omitted, and the
		 * end of the range appears one day too early. Fix is to use
		 * d >= start AND d < end+1day.
		 */
		Calendar adjEnd = null;
		if (null != end) {
			adjEnd = (Calendar) end.clone();
			adjEnd.add(Calendar.DATE, 1);
		}

		/*
		 * Note that the two ifs below would need to be else ifs and the
		 * between-generating clause reintroduced were the between bug to
		 * be fixed.
		 */
		//if ((null != start) && (null != adjEnd)) {
		//	criteria.add(BusinessExpression.Between(dateProperty,
		//		start, adjEnd));
		//}
		if (null != start) {
			// TODO Fix broken Calendar handling in GreaterThan
			// Eq is true to catch on the dot of midnight
			criteria.add(BusinessExpression.GreaterThan(dateProperty,
					sqldf.format(start.getTime()), true));
		}
		if (null != adjEnd) {
			// TODO Fix broken Calendar handling in LessThan
			// Eq is false because we have already incremented the end date
			criteria.add(BusinessExpression.LessThan(dateProperty,
					sqldf.format(adjEnd.getTime()), false));
		}

	}
}
