/**
 * pims-model org.pimslims.model.reference DbNameA.java
 * 
 * @author cm65
 * @date 20 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.reference;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

/**
 * DbNameA
 * 
 */
@Deprecated
// renamed as "Database"
public abstract class DbName extends org.pimslims.model.reference.PublicEntry {

    /**
     * Constructor for DbNameA
     * 
     * @param wVersion
     * @throws ConstraintException
     */
    protected DbName(WritableVersion wVersion) throws ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for DbNameA
     */
    protected DbName() {
        super();
    }

}
