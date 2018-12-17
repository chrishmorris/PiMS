/*
 * Created on 18-Feb-2005 @author: Chris Morris
 */
package org.pimslims.util;

import java.util.logging.Level;

/**
 * Provides methods for displaying messages to users, and logging them.
 * 
 * The methods in the lab API should use a MessageHandler to report problems. They should not write to STDOUT,
 * since in an applet or server this is not very accessible.
 * 
 * The method signatures are compatible with log4j.
 * 
 * @version 0.1
 */
public interface MessageHandler {

    // priority levels for messages
    /**
     * Priority level for a message of value only to developers.
     */
    final Level DEBUG = Level.FINEST;

    /**
     * Priority level for an information message. The user should normally be informed of the message, but it
     * does not indicate a problem.
     */
    final Level INFO = Level.INFO;

    /**
     * Priority level for a warning message. This message indicates a condition that may be an error.
     */
    final Level WARN = Level.WARNING;

    /**
     * Priority level for an error message. These messages must be reported to the user.
     */
    final Level ERROR = Level.SEVERE;

    /**
     * Priority level for a message indicating a fatal error. The messages must be recorded if at all
     * possible.
     */
    final Level FATAL = Level.SEVERE;

    /**
     * Log an error message, and display it to the user if the priority level is at least INFO.
     * 
     * @param priority one of the constants defined above
     * @param message the message
     */
    void log(Level priority, String message);

    /**
     * Log an exception, and display a message to the user if the priority level is at least INFO.
     * 
     * @param priority one of the constants defined above
     * @param message an error message explaining the exception
     * @param ex an exception to record
     */
    void log(Level priority, String message, Throwable ex);

}
