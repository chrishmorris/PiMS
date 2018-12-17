/*
 * Created on 18-Feb-2005 @author: Chris Morris
 */
package org.pimslims.upgrader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.pimslims.util.MessageHandler;

/**
 * An implementation of MessageHandler that is convenient for developement.
 * 
 * It puts user messages to STDOUT, and developer messages to STDERR
 * 
 * @version 0.1
 */
public class DebugMessageHandler implements MessageHandler {

    private static Logger log = Logger.getLogger(org.pimslims.upgrader.Upgrader.class.getName());

    /**
     * Set to false to suppress messages with priority=DEBUG
     */

    private static MessageHandler singleton = null;

    public static void LOG(final Level priority, final String message, final Throwable ex) {
        if (singleton == null) {
            singleton = new DebugMessageHandler();
        }
        singleton.log(priority, message, ex);
    }

    public static void LOG(final Level level, final String message) {
        if (singleton == null) {
            singleton = new DebugMessageHandler();
        }
        singleton.log(level, message);
    }

    public DebugMessageHandler() {
        super();
        // System.err.println("PIMS DebugMessageHandler started!\n");
    }

    /**
     * {@inheritDoc}
     */
    public void log(final Level priority, final String message, final Throwable ex) {
        log.log(priority, message, ex);
    }

    /**
     * {@inheritDoc}
     */
    public void log(final Level priority, final String message) {
        log.log(priority, message);

    }
}
