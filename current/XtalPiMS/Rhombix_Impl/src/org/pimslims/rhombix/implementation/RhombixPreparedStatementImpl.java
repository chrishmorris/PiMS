/**
 * Rhombix_Impl org.pimslims.rhombix RhombixPreparedStatementImpl.java
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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * RhombixPreparedStatementImpl Used to Guarantee that the application is read only.
 */
public class RhombixPreparedStatementImpl implements RhombixPreparedStatement {

    private final PreparedStatement statement;

    private final boolean readOnly;

    private static int numStatements;

    /**
     * .RhombixPreparedStatementImpl.cancel
     * 
     * @throws SQLException
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        this.statement.cancel();
    }

    /**
     * .RhombixPreparedStatementImpl.close
     * 
     * @throws SQLException
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        numStatements--;
        this.statement.close();
    }

    /**
     * .RhombixPreparedStatementImpl.executeQuery
     * 
     * @return
     * @throws SQLException
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return this.statement.executeQuery();
    }

    /**
     * .RhombixPreparedStatementImpl.executeUpdate
     * 
     * @return
     * @throws SQLException
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        if (this.readOnly) {
            throw new IllegalStateException("Dont update the Rhombix database");
        }
        return this.statement.executeUpdate();
    }

    /**
     * .RhombixPreparedStatementImpl.setArray
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(int parameterIndex, Array x) throws SQLException {
        this.statement.setArray(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setAsciiStream
     * 
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.statement.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setAsciiStream
     * 
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
     */
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.statement.setAsciiStream(parameterIndex, x, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setAsciiStream
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
     */
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.statement.setAsciiStream(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setBigDecimal
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.statement.setBigDecimal(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setBinaryStream
     * 
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.statement.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setBinaryStream
     * 
     * @param parameterIndex
     * @param x
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
     */
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.statement.setBinaryStream(parameterIndex, x, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setBinaryStream
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
     */
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.statement.setBinaryStream(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setBlob
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        this.statement.setBlob(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setBlob
     * 
     * @param parameterIndex
     * @param inputStream
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
     */
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        this.statement.setBlob(parameterIndex, inputStream, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setBlob
     * 
     * @param parameterIndex
     * @param inputStream
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
     */
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        this.statement.setBlob(parameterIndex, inputStream);
    }

    /**
     * .RhombixPreparedStatementImpl.setBoolean
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.statement.setBoolean(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setByte
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.statement.setByte(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setBytes
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.statement.setBytes(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setCharacterStream
     * 
     * @param parameterIndex
     * @param reader
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        this.statement.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setCharacterStream
     * 
     * @param parameterIndex
     * @param reader
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
     */
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        this.statement.setCharacterStream(parameterIndex, reader, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setCharacterStream
     * 
     * @param parameterIndex
     * @param reader
     * @throws SQLException
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
     */
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.statement.setCharacterStream(parameterIndex, reader);
    }

    /**
     * .RhombixPreparedStatementImpl.setClob
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        this.statement.setClob(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setClob
     * 
     * @param parameterIndex
     * @param reader
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
     */
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.statement.setClob(parameterIndex, reader, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setClob
     * 
     * @param parameterIndex
     * @param reader
     * @throws SQLException
     * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
     */
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        this.statement.setClob(parameterIndex, reader);
    }

    /**
     * .RhombixPreparedStatementImpl.setDate
     * 
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        this.statement.setDate(parameterIndex, x, cal);
    }

    /**
     * .RhombixPreparedStatementImpl.setDate
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.statement.setDate(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setDouble
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.statement.setDouble(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setEscapeProcessing
     * 
     * @param enable
     * @throws SQLException
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.statement.setEscapeProcessing(enable);
    }

    /**
     * .RhombixPreparedStatementImpl.setFloat
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.statement.setFloat(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setInt
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.statement.setInt(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setLong
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.statement.setLong(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setMaxRows
     * 
     * @param max
     * @throws SQLException
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        this.statement.setMaxRows(max);
    }

    /**
     * .RhombixPreparedStatementImpl.setNCharacterStream
     * 
     * @param parameterIndex
     * @param value
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
     */
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        this.statement.setNCharacterStream(parameterIndex, value, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setNCharacterStream
     * 
     * @param parameterIndex
     * @param value
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
     */
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        this.statement.setNCharacterStream(parameterIndex, value);
    }

    /**
     * .RhombixPreparedStatementImpl.setNClob
     * 
     * @param parameterIndex
     * @param value
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
     */
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        this.statement.setNClob(parameterIndex, value);
    }

    /**
     * .RhombixPreparedStatementImpl.setNClob
     * 
     * @param parameterIndex
     * @param reader
     * @param length
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
     */
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.statement.setNClob(parameterIndex, reader, length);
    }

    /**
     * .RhombixPreparedStatementImpl.setNClob
     * 
     * @param parameterIndex
     * @param reader
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
     */
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        this.statement.setNClob(parameterIndex, reader);
    }

    /**
     * .RhombixPreparedStatementImpl.setNString
     * 
     * @param parameterIndex
     * @param value
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
     */
    public void setNString(int parameterIndex, String value) throws SQLException {
        this.statement.setNString(parameterIndex, value);
    }

    /**
     * .RhombixPreparedStatementImpl.setNull
     * 
     * @param parameterIndex
     * @param sqlType
     * @param typeName
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        this.statement.setNull(parameterIndex, sqlType, typeName);
    }

    /**
     * .RhombixPreparedStatementImpl.setNull
     * 
     * @param parameterIndex
     * @param sqlType
     * @throws SQLException
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.statement.setNull(parameterIndex, sqlType);
    }

    /**
     * .RhombixPreparedStatementImpl.setObject
     * 
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @param scaleOrLength
     * @throws SQLException
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
        throws SQLException {
        this.statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    /**
     * .RhombixPreparedStatementImpl.setObject
     * 
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @throws SQLException
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        this.statement.setObject(parameterIndex, x, targetSqlType);
    }

    /**
     * .RhombixPreparedStatementImpl.setObject
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.statement.setObject(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setQueryTimeout
     * 
     * @param seconds
     * @throws SQLException
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        this.statement.setQueryTimeout(seconds);
    }

    /**
     * .RhombixPreparedStatementImpl.setString
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        this.statement.setString(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setTime
     * 
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        this.statement.setTime(parameterIndex, x, cal);
    }

    /**
     * .RhombixPreparedStatementImpl.setTime
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.statement.setTime(parameterIndex, x);
    }

    /**
     * .RhombixPreparedStatementImpl.setTimestamp
     * 
     * @param parameterIndex
     * @param x
     * @param cal
     * @throws SQLException
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        this.statement.setTimestamp(parameterIndex, x, cal);
    }

    /**
     * .RhombixPreparedStatementImpl.setTimestamp
     * 
     * @param parameterIndex
     * @param x
     * @throws SQLException
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.statement.setTimestamp(parameterIndex, x);
    }

    /**
     * Constructor for RhombixPreparedStatementImpl
     * 
     * @param sql
     * 
     * @param prepareStatement
     */
    public RhombixPreparedStatementImpl(PreparedStatement preparedStatement, String sql, boolean readOnly) {
        numStatements++;
        if (100 < numStatements) {
            Exception e = new Exception("Too many open statements");
            e.fillInStackTrace();
            e.printStackTrace();
            System.exit(3);
        }
        // could log like this: System.out.println("" + numStatements + " " + sql);
        this.statement = preparedStatement;
        this.readOnly = readOnly;
    }

}
