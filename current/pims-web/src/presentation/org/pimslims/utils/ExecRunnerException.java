package org.pimslims.utils;

/**
 * Exception thrown when running ExecRunner - when running an external executable file withing java code with
 * control over input stream, output stream and error stream
 * 
 * @author Ekaterina Pilicheva
 * @since 31.03.2006
 */
public class ExecRunnerException extends Exception {

    public ExecRunnerException(String message) {
        super(message);
    }

    public ExecRunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @author cm65
     * 
     */
    public static class BadPathException extends ExecRunnerException {

        public BadPathException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}
