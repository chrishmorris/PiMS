/**
 * datamodel org.pimslims.dao MockTransaction.java
 * 
 * @author cm65
 * @date 22 Mar 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.dao;

import javax.transaction.Synchronization;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

/**
 * MockTransaction obsolete
 */
public class MockTransaction implements Transaction {

    /**
     * MockTransaction.begin
     * 
     * @see org.hibernate.Transaction#begin()
     */
    public void begin() throws HibernateException {
        // TODO Auto-generated method stub

    }

    /**
     * MockTransaction.commit
     * 
     * @see org.hibernate.Transaction#commit()
     */
    public void commit() throws HibernateException {
        // TODO Auto-generated method stub

    }

    /**
     * MockTransaction.isActive
     * 
     * @see org.hibernate.Transaction#isActive()
     */
    public boolean isActive() throws HibernateException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * MockTransaction.registerSynchronization
     * 
     * @see org.hibernate.Transaction#registerSynchronization(javax.transaction.Synchronization)
     */
    public void registerSynchronization(Synchronization arg0) throws HibernateException {
        // TODO Auto-generated method stub

    }

    /**
     * MockTransaction.rollback
     * 
     * @see org.hibernate.Transaction#rollback()
     */
    public void rollback() throws HibernateException {
        // TODO Auto-generated method stub

    }

    /**
     * MockTransaction.setTimeout
     * 
     * @see org.hibernate.Transaction#setTimeout(int)
     */
    public void setTimeout(int arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * MockTransaction.wasCommitted
     * 
     * @see org.hibernate.Transaction#wasCommitted()
     */
    public boolean wasCommitted() throws HibernateException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * MockTransaction.wasRolledBack
     * 
     * @see org.hibernate.Transaction#wasRolledBack()
     */
    public boolean wasRolledBack() throws HibernateException {
        // TODO Auto-generated method stub
        return false;
    }

}
