/*
 * Created on 18-Feb-2005 @author: Chris Morris
 */
package org.pimslims.util;

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

    /**
     * <p>
     * Message Levels
     * </p>
     * <p>
     * <li>{@link #DEBUG}</li>
     * <li>{@link #INFO}</li>
     * <li>{@link #WARN}</li>
     * <li>{@link #ERROR}</li>
     * </p>
     */
    public enum Level {
        /**
         * Priority level for a message of value only to developers.
         */
        DEBUG,

        /**
         * Priority level for an information message. The user should normally be informed of the message, but
         * it does not indicate a problem.
         */
        INFO,

        /**
         * Priority level for a warning message. This message indicates a condition that may be an error.
         */
        WARN,

        /**
         * Priority level for an error message. These messages must be reported to the user.
         */
        ERROR,

        /**
         * Priority level for a message indicating a fatal error. The messages must be recorded if at all
         * possible.
         */
        FATAL

    }

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
