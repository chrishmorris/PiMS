/**
 * pims-model org.pimslims.dao TestVersionImpl.java
 * 
 * @author cm65
 * @date 18 Jun 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import java.sql.Timestamp;

import javax.persistence.EntityManager;

import org.pimslims.access.Access;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;

/**
 * TestVersionImpl
 * 
 */
public class TestVersionImpl extends WritableVersionImpl {

    /**
     * Constructor for TestVersionImpl
     * 
     * @param username
     * @param model
     * @param date
     * @param session
     */
    public TestVersionImpl(ModelImpl model, Timestamp date, EntityManager entityManager) {
        super(Access.ADMINISTRATOR, model, date, entityManager);
    }

    @Override
    public void commit() throws AbortedException, ConstraintException {
        throw new UnsupportedOperationException("You cannot commit a test version");
    }

}
