/*
 * Created on 18-Feb-2005 @author: Chris Morris
 */
package org.pimslims.upgrader;

import org.pimslims.util.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * An implementation of MessageHandler that is convenient for developement.
 * 
 * It puts user messages to STDOUT, and developer messages to STDERR
 * 
 * @version 0.1
 */
public class DebugMessageHandler implements MessageHandler {

    private static Logger log = LoggerFactory.getLogger(org.pimslims.upgrader.Upgrader.class.getName());

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
    @Override
    public void log(final Level priority, final String message, final Throwable ex) {
        switch (priority) {
            case FATAL:
                log.error(MarkerFactory.getMarker("FATAL"), message, ex);
                break;
            case ERROR:
                log.error(message, ex);
                break;
            case WARN:
                log.warn(message, ex);
                break;
            case INFO:
                log.info(message, ex);
                break;
            default:
                log.debug(message, ex);
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(final Level priority, final String message) {
        switch (priority) {
            case FATAL:
                log.error(MarkerFactory.getMarker("FATAL"), message);
                break;
            case ERROR:
                log.error(message);
                break;
            case WARN:
                log.warn(message);
                break;
            case INFO:
                log.info(message);
                break;
            default:
                log.debug(message);
                break;
        }
    }
}
