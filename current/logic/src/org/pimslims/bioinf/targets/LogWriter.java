package org.pimslims.bioinf.targets;

import java.io.PrintStream;

/**
 * @author Petr Troshin aka pvt43
 */

public class LogWriter {

    /*
     * Requested log level
     */
    private int logLevel = 0;

    public LogWriter(final int logLevel) {
        this.logLevel = logLevel;
    }

    public void setLogLevel(final int logLevel) {
        this.logLevel = logLevel;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    /**
     * Print the messages to the standard output depending on the current logLevel
     * 
     * @param message message to print
     * @param messagelogLevel log level
     */
    public void printLog(final String message, final int messagelogLevel, final PrintStream out) {
        if (messagelogLevel <= this.logLevel) {
            out.println(message);
        }
    }

    /**
     * Print the messages to the standard output depending on the current logLevel
     * 
     * @param message message to print
     * @param messagelogLevel log level
     */
    public void printLog(final String message, final int messagelogLevel) {
        if (messagelogLevel <= this.logLevel) {
            this.printLog(message, messagelogLevel, System.out);
        }
    }

    /**
     * Print the messages to the standard output only if current log level the same as a message logLevel
     * 
     * @param message message to print
     * @param messagelogLevel log level of the message
     */
    public void printExact(final String message, final int messagelogLevel, final PrintStream out) {
        if (messagelogLevel == this.logLevel) {
            out.println(message);
        }
    }

    /**
     * Print the messages to the standard output only if current log level the same as a message logLevel
     * 
     * @param message message to print
     * @param messagelogLevel log level of the message
     */
    public void printExact(final String message, final int messagelogLevel) {
        if (messagelogLevel == this.logLevel) {
            this.printLog(message, messagelogLevel, System.out);
        }
    }

}
