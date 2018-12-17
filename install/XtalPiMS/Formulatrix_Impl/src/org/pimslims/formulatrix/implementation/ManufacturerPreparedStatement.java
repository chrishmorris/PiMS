/**
 *  MaufacturerPreparedStatement.java
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * MaufacturerPreparedStatement
 * 
 */
public interface ManufacturerPreparedStatement {

    /**
     * MaufacturerPreparedStatement.setString
     * 
     * @param i
     * @param value
     * @throws SQLException
     */
    void setString(int i, String value) throws SQLException;

    /**
     * MaufacturerPreparedStatement.setTimestamp
     * 
     * @param i
     * @param timestamp
     * @throws SQLException
     */
    void setTimestamp(int i, Timestamp timestamp) throws SQLException;

    /**
     * MaufacturerPreparedStatement.executeQuery
     * 
     * @return
     * @throws SQLException
     */
    ResultSet executeQuery() throws SQLException;

    /**
     * MaufacturerPreparedStatement.setLong
     * 
     * @param i
     * @param id
     */
    void setLong(int i, long id) throws SQLException;

    /**
     * MaufacturerPreparedStatement.executeUpdate
     */
    int executeUpdate() throws SQLException;

    /**
     * MaufacturerPreparedStatement.close
     */
    void close() throws SQLException;

    /**
     * MaufacturerPreparedStatement.setInt
     * 
     * @param i
     * @param j
     */
    void setInt(int i, int j) throws SQLException;

    /**
     * MaufacturerPreparedStatement.setNull
     * 
     * @param i
     * @param bigint
     */
    void setNull(int i, int bigint) throws SQLException;

    /**
     * MaufacturerPreparedStatement.setDouble
     * 
     * @param i
     * @param quantity
     * @throws SQLException
     */
    void setDouble(int i, double quantity) throws SQLException;

    /**
     * MaufacturerPreparedStatement.setBoolean
     * 
     * @param i
     * @param equals
     * @throws SQLException
     */
    void setBoolean(int i, boolean b) throws SQLException;

}
