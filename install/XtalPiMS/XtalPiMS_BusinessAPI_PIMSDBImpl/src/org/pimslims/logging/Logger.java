package org.pimslims.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.pimslims.business.criteria.BusinessCriterion;

/**
 * Logger
 * 
 */
public class Logger {

	private static final LogManager logManager = java.util.logging.LogManager
			.getLogManager();
	static {
		InputStream in = Logger.class
				.getResourceAsStream("/java.util.logging.config.properties");
		if (null == in) {
			System.err
					.println("File not found: java.util.logging.config.properties");
		} else {
			try {
				logManager.readConfiguration(in);
			} catch (SecurityException e) {
				// we can't recover from this, but start up anyway
				e.printStackTrace();
			} catch (IOException e) {
				// we can't recover from this, but start up anyway
				e.printStackTrace();
			}
		}
	}

	private final java.util.logging.Logger logger;

	public Logger(String string) {
		this.logger = java.util.logging.Logger.getLogger(string);
	}

	public static Logger getLogger(Class class1) {
		return new Logger(class1.getCanonicalName());
	}

	public static Logger getLogger(String string) {
		return new Logger(string);
	}

	public void info(String message) {
		this.logger.finest(message);
	}

	public void debug(String message) {
		this.logger.fine(message);
	}

	public void error(String message, Throwable t) {
		this.logger.throwing(message, "", t);
	}

	public void error(String message) {
		this.logger.severe(message);
	}

	public final Object getLevel() {
		return this.logger.getLevel();
	}

	public void setLevel(Object level) {
		this.logger.setLevel((Level) level);
	}

	public void debug(BusinessCriterion businessCriterion) {
		this.debug(businessCriterion.toString());
	}

}
