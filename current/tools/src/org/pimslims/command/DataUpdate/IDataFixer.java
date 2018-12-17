/**
 * current-pims-web org.pimslims.command.DataUpdate IDataFixer.java
 * 
 * @author bl67
 * @date 17 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 bl67 
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.dao.WritableVersion;

/**
 * IDataFixer
 * 
 */
public interface IDataFixer {
    /**
     * 
     * @return description of this fix
     */
    String getDescription();

    /**
     * @param wv
     * @return true when found problem and fixed
     * @throws ConstraintException
     * @throws AccessException
     */
    Boolean fixData(WritableVersion wv) throws ConstraintException, AccessException;

}
