/*
 * Created on 22-Sep-2004 @author: Chris Morris
 */
package org.pimslims.access;


/**
 * Implements the access rights model proposed for PIMS
 * 
 * @version 0.1
 */
public abstract class PIMSAccess extends Access {

    // operation types for permissions
    // The data model is designed to allow more fine grained control,
    // but PIMS only uses these four permissions.
    /**
     * Operation type for delete requests
     */
    public static final String CREATE = "create";

    /**
     * Operation type for read requests
     */
    public static final String READ = "read";

    /**
     * Operation type for update requests
     */
    public static final String UPDATE = "update";

    /**
     * Operation type for delete requests
     */
    public static final String DELETE = "delete";

    /**
     * Operation type to unlock a page (currently only for experiments)
     */
    public static final String UNLOCK = "unlock";

    /**
     * 
     */
    public PIMSAccess() {
        super();
    }

}
