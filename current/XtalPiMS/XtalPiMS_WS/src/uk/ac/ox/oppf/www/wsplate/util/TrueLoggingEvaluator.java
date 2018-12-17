package uk.ac.ox.oppf.www.wsplate.util;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * TriggeringEventEvaluator that always returns true, for
 * use with SMTPAppender
 * 
 * @author Jon Diprose
 */
public class TrueLoggingEvaluator implements TriggeringEventEvaluator {

	/**
	 * Implementation of isTriggeringEvent that always returns true
	 * 
	 * @return true
	 * @see org.apache.log4j.spi.TriggeringEventEvaluator#isTriggeringEvent(org.apache.log4j.spi.LoggingEvent)
	 */
	public boolean isTriggeringEvent(LoggingEvent event) {
		return true;
	}

}
