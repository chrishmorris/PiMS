/**
 *  Connection.java
 * 
 * @author cm65
 * @date 6 Apr 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

import java.sql.SQLException;

/**
 * Connection
 * 
 * Represents a database connection. Used so we can guarantee not to update the database.
 */
public interface ManufacturerConnection {

    /**
     * Connection.rollback
     */
    void rollback() throws SQLException;

    /**
     * Connection.setAutoCommit
     * 
     * @param b
     */
    void setAutoCommit(boolean b) throws SQLException;

    /**
     * Connection.isClosed
     * 
     * @return
     * @throws SQLException
     */
    boolean isClosed() throws SQLException;

    /**
     * Connection.commit
     */
    void commit() throws SQLException;

    /**
     * Connection.close
     */
    void close() throws SQLException;

    /**
     * Connection.prepareStatement
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    ManufacturerPreparedStatement prepareStatement(String sql) throws SQLException;

}
