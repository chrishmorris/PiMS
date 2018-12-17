/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.exception;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;

/**
 * Reports that a transaction has been cancelled.
 * 
 * This will be thrown if the user attempts to update a version after calling version.abort(). It may also be
 * thrown as a result of problems internal to the transaction engine. Applications can retry the operation by
 * getting a new transaction.
 * 
 * @see WritableVersion
 * @version 0.1
 */
public class AbortedException extends ModelException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Comment for <code>version</code>
     * 
     * the transaction that was cancelled
     */
    public final ReadableVersion version;

    /**
     * Indicates that a transaction has been cancelled. The application may retry.
     * 
     * @param version object for the transaction that cannot complete
     */
    public AbortedException(final ReadableVersion version) {
        super();
        this.version = version;
    }

    /**
     * Indicates that a transaction has been cancelled. The application may retry.
     * 
     * @param version object for the transaction that cannot complete
     */
    public AbortedException(final ReadableVersion version, String message) {
        super(message);
        this.version = version;
    }

    /**
     * Indicates that a transaction has been cancelled. The application may retry.
     * 
     * @param version object for the transaction that cannot complete
     */
    public AbortedException(final ReadableVersion version, String message, Throwable cause) {
        super(message, cause);
        this.version = version;
    }
}
