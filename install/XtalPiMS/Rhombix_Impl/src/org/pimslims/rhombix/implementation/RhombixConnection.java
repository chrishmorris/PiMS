/**
 * Rhombix_Impl org.pimslims.rhombix RhombixConnection.java
 * 
 * @author cm65
 * @date 6 Apr 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.SQLException;

/**
 * RhombixConnection
 * 
 * Represents a database connection. Used so we can guarantee not to update the database.
 */
public interface RhombixConnection {

    /**
     * RhombixConnection.rollback
     */
    void rollback() throws SQLException;

    /**
     * RhombixConnection.setAutoCommit
     * 
     * @param b
     */
    void setAutoCommit(boolean b) throws SQLException;

    /**
     * RhombixConnection.isClosed
     * 
     * @return
     * @throws SQLException
     */
    boolean isClosed() throws SQLException;

    /**
     * RhombixConnection.commit
     */
    void commit() throws SQLException;

    /**
     * RhombixConnection.close
     */
    void close() throws SQLException;

    /**
     * RhombixConnection.prepareStatement
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    RhombixPreparedStatement prepareStatement(String sql) throws SQLException;

}
