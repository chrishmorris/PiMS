/**
 *  ConnectionImpl.java
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
 * ConnectionImpl
 * 
 */
public class RockImagerConnection implements ManufacturerConnection {

    private final Connection connection;

    private final boolean readOnly;

    /**
     * Constructor for ConnectionImpl
     * 
     * @param connection
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public RockImagerConnection(boolean readOnly) {
        super();
        File file = new File("conf/Properties");
        try {
            this.readOnly = readOnly;
            // this simple implementation does not include access rights
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            if (this.readOnly) {
                Properties properties = new Properties();
                InputStream in = new FileInputStream(file);
                properties.load(in);
                String url = properties.getProperty("fx.RIurl");
                String username = properties.getProperty("fx.username");
                String password = properties.getProperty("fx.password");
                System.out.println("Connecting to: " + url);
                this.connection = DriverManager.getConnection(url, username, password);
            } else {
                // TODO 00 writable connection for testing
                this.connection =
                    DriverManager.getConnection("jdbc:jtds:sqlserver://SQLPLAYGROUND/RockMaker", "xtalpims", "xt4lp1ms!");
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
     * .ConnectionImpl.clearWarnings
     * 
     * @throws SQLException
     * @see java.sql.Connection#clearWarnings()
     */
    private void clearWarnings() throws SQLException {
        this.connection.clearWarnings();
    }

    /**
     * .ConnectionImpl.close
     * 
     * @throws SQLException
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        this.connection.close();
    }

    /**
     * .ConnectionImpl.commit
     * 
     * @throws SQLException
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        checkCanwrite();
        this.connection.commit();
    }

    /**
     * ConnectionImpl.checkCanwrite
     */
    private void checkCanwrite() {
        if (this.readOnly) {
            throw new IllegalStateException("Don't write to Rhombix");
        }
    }

    /**
     * .ConnectionImpl.createBlob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createBlob()
     */
    private Blob createBlob() throws SQLException {
        return this.connection.createBlob();
    }

    /**
     * .ConnectionImpl.createClob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createClob()
     */
    private Clob createClob() throws SQLException {
        return this.connection.createClob();
    }

    /**
     * .ConnectionImpl.createNClob
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createNClob()
     */
    private NClob createNClob() throws SQLException {
        return this.connection.createNClob();
    }

    /**
     * .ConnectionImpl.createStatement
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    /**
     * .ConnectionImpl.createStatement
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
     * .ConnectionImpl.createStatement
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
     * .ConnectionImpl.getAutoCommit
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    /**
     * .ConnectionImpl.getClientInfo
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getClientInfo()
     */
    public Properties getClientInfo() throws SQLException {
        return this.connection.getClientInfo();
    }

    /**
     * .ConnectionImpl.getClientInfo
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
     * .ConnectionImpl.getMetaData
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.connection.getMetaData();
    }

    /**
     * .ConnectionImpl.getTransactionIsolation
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        return this.connection.getTransactionIsolation();
    }

    /**
     * .ConnectionImpl.getTypeMap
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getTypeMap()
     */
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.connection.getTypeMap();
    }

    /**
     * .ConnectionImpl.getWarnings
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return this.connection.getWarnings();
    }

    /**
     * .ConnectionImpl.isClosed
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }

    /**
     * .ConnectionImpl.isReadOnly
     * 
     * @return
     * @throws SQLException
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    /**
     * .ConnectionImpl.prepareStatement
     * 
     * @param arg0
     * @return
     * @throws SQLException
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public ManufacturerPreparedStatement prepareStatement(String sql) throws SQLException {
        return new ManufacturerPreparedStatementImpl(this.connection.prepareStatement(sql), sql, this.readOnly);
    }

    /**
     * .ConnectionImpl.rollback
     * 
     * @throws SQLException
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    /**
     * .ConnectionImpl.setAutoCommit
     * 
     * @param arg0
     * @throws SQLException
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean arg0) throws SQLException {
        this.connection.setAutoCommit(arg0);
    }

    /**
     * .ConnectionImpl.setReadOnly
     * 
     * @param arg0
     * @throws SQLException
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean arg0) throws SQLException {
        this.connection.setReadOnly(arg0);
    }
}
