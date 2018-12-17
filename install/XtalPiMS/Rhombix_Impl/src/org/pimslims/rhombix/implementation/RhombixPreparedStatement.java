/**
 * Rhombix_Impl org.pimslims.rhombix RhombixPreparedStatement.java
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * RhombixPreparedStatement
 * 
 */
public interface RhombixPreparedStatement {

    /**
     * RhombixPreparedStatement.setString
     * 
     * @param i
     * @param value
     * @throws SQLException
     */
    void setString(int i, String value) throws SQLException;

    /**
     * RhombixPreparedStatement.setTimestamp
     * 
     * @param i
     * @param timestamp
     * @throws SQLException
     */
    void setTimestamp(int i, Timestamp timestamp) throws SQLException;

    /**
     * RhombixPreparedStatement.executeQuery
     * 
     * @return
     * @throws SQLException
     */
    ResultSet executeQuery() throws SQLException;

    /**
     * RhombixPreparedStatement.setLong
     * 
     * @param i
     * @param id
     */
    void setLong(int i, long id) throws SQLException;

    /**
     * RhombixPreparedStatement.executeUpdate
     */
    int executeUpdate() throws SQLException;

    /**
     * RhombixPreparedStatement.close
     */
    void close() throws SQLException;

    /**
     * RhombixPreparedStatement.setInt
     * 
     * @param i
     * @param j
     */
    void setInt(int i, int j) throws SQLException;

    /**
     * RhombixPreparedStatement.setNull
     * 
     * @param i
     * @param bigint
     */
    void setNull(int i, int bigint) throws SQLException;

    /**
     * RhombixPreparedStatement.setDouble
     * 
     * @param i
     * @param quantity
     * @throws SQLException
     */
    void setDouble(int i, double quantity) throws SQLException;

    /**
     * RhombixPreparedStatement.setBoolean
     * 
     * @param i
     * @param equals
     * @throws SQLException
     */
    void setBoolean(int i, boolean b) throws SQLException;

}
