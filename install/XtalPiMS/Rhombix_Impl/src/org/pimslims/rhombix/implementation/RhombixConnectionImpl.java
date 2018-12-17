/**
 * Rhombix_Impl org.pimslims.rhombix RhombixConnectionImpl.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * RhombixConnectionImpl
 * 
 */
public class RhombixConnectionImpl implements RhombixConnection {

    private final Connection connection;

    private final boolean readOnly;

    /**
     * Constructor for RhombixConnectionImpl
     * 
     * @param connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    RhombixConnectionImpl(boolean readOnly) {
        super();
        File file = new File("conf/Properties");
        try {
            this.readOnly = readOnly;
            // this simple implementation does not include access rights
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if (this.readOnly) {
                Properties properties = new Properties();
                InputStream in = new FileInputStream(file);
                properties.load(in);
                String url = properties.getProperty("rh.url");
                String username = properties.getProperty("rh.username");
                String password = properties.getProperty("rh.password");
                System.out.print("Connecting to: " + url);
                this.connection = DriverManager.getConnection(url, username, password);
            } else {
                // writable connection for testing
                this.connection =
                    DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "rhombix", "rhombix");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("Could not find connection details at: " + file.getAbsolutePath());
            }
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * .RhombixConnectionImpl.clearWarnings
     * 
     * @throws SQLException
     * @see java.sql.Connection#clearWarnings()
     */
    private void clearWarnings() throws SQLException {
        this.connection.clearWarnings();
    }

    /**
     * .RhombixConnectionImpl.close
     * 
     * @throws SQLException
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        this.connection.close();
    }

    /**
     * .RhombixConnectionImpl.commit
     * 
     * @throws SQLException
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        checkCanwrite();
        this.connection.commit();
    }

    /**
     * RhombixConnectionImpl.checkCanwrite
     */
    private void checkCanwrite() {
        if (this.readOnly) {
            throw new IllegalStateException("Don't write to Rhombix");
        }
    }

    /**
     * .RhombixConnectionImpl.createBlob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createBlob()
     */
    private Blob createBlob() throws SQLException {
        return this.connection.createBlob();
    }

    /**
     * .RhombixConnectionImpl.createClob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createClob()
     */
    private Clob createClob() throws SQLException {
        return this.connection.createClob();
    }

    /**
     * .RhombixConnectionImpl.createNClob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createNClob()
     */
    private NClob createNClob() throws SQLException {
        return this.connection.createNClob();
    }

    /**
     * .RhombixConnectionImpl.createStatement
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    /**
     * .RhombixConnectionImpl.createStatement
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
        return this.connection.createStatement(arg0, arg1, arg2);
    }

    /**
     * .RhombixConnectionImpl.createStatement
     * 
     * @param arg0
     * @param arg1
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int arg0, int arg1) throws SQLException {
        return this.connection.createStatement(arg0, arg1);
    }

    /**
     * .RhombixConnectionImpl.getAutoCommit
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    /**
     * .RhombixConnectionImpl.getClientInfo
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getClientInfo()
     */
    public Properties getClientInfo() throws SQLException {
        return this.connection.getClientInfo();
    }

    /**
     * .RhombixConnectionImpl.getClientInfo
     * 
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getClientInfo(java.lang.String)
     */
    public String getClientInfo(String arg0) throws SQLException {
        return this.connection.getClientInfo(arg0);
    }

    /**
     * .RhombixConnectionImpl.getMetaData
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.connection.getMetaData();
    }

    /**
     * .RhombixConnectionImpl.getTransactionIsolation
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        return this.connection.getTransactionIsolation();
    }

    /**
     * .RhombixConnectionImpl.getTypeMap
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTypeMap()
     */
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.connection.getTypeMap();
    }

    /**
     * .RhombixConnectionImpl.getWarnings
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return this.connection.getWarnings();
    }

    /**
     * .RhombixConnectionImpl.isClosed
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }

    /**
     * .RhombixConnectionImpl.isReadOnly
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    /**
     * .RhombixConnectionImpl.prepareStatement
     * 
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public RhombixPreparedStatement prepareStatement(String sql) throws SQLException {
        return new RhombixPreparedStatementImpl(this.connection.prepareStatement(sql), sql, this.readOnly);
    }

    /**
     * .RhombixConnectionImpl.rollback
     * 
     * @throws SQLException
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    /**
     * .RhombixConnectionImpl.setAutoCommit
     * 
     * @param arg0
     * @throws SQLException
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean arg0) throws SQLException {
        this.connection.setAutoCommit(arg0);
    }

    /**
     * .RhombixConnectionImpl.setReadOnly
     * 
     * @param arg0
     * @throws SQLException
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean arg0) throws SQLException {
        this.connection.setReadOnly(arg0);
    }
}
