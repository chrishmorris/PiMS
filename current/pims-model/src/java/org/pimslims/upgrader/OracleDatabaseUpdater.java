/**
 * pims-model org.pimslims.upgrader OracleDatabaseUpdater.java
 * 
 * @author bl67
 * @date 14 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pimslims.util.MessageHandler;

/**
 * OracleDatabaseUpdater
 * 
 */
@Deprecated
// support for Oracle is not currently maintained.
public class OracleDatabaseUpdater extends DatabaseUpdater {

    /**
     * TEMPORARY_COLUMN A name for a temporary column. Deliberately contains a special character, so clashes
     * are unlikely. Also unique - so it cannot clash with an old temporary column from a failed update.
     * Oracle does not roll back DDL statements well.
     */
    private static final String TEMPORARY_COLUMN = "\"pims-temp" + System.currentTimeMillis() + "\"";

    /**
     * Constructor for OracleDatabaseUpdater
     * 
     * @param connection
     */
    public OracleDatabaseUpdater(Connection connection) {
        super(connection);

    }

    /**
     * Constructor for OracleDatabaseUpdater
     * 
     * @param muv
     */
    public OracleDatabaseUpdater(ModelUpdateVersionImpl muv) {
        super(muv.getSession().connection());
    }

    /**
     * OracleDatabaseUpdater.dropConstraintInTable
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#dropConstraintInTable(java.lang.String)
     */
    @Override
    public void dropConstraintInTable(String tableName) throws SQLException {
        final Map<String, String> constrains = new HashMap<String, String>();
        final ResultSet rs =
            sqlExecuteQuery("SELECT constraint_name,constraint_type from user_constraints where  table_name='"
                + tableName + "'");
        while (rs.next()) {
            constrains.put(rs.getString("constraint_name"), rs.getString("constraint_type"));
        }
        for (final String constraint : constrains.keySet()) {
            if (constrains.get(constraint).equalsIgnoreCase("p")) {
                continue;
            } else if (constrains.get(constraint).equalsIgnoreCase("c")) {
                continue;
            } else {
                sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraint + " cascade");
            }

        }
        rs.close();
    }

    /**
     * OracleDatabaseUpdater.getAllTableNames
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#getAllTableNames()
     */
    @Override
    public List<String> getAllTableNames() throws SQLException {
        List<String> tableNames = new LinkedList<String>();
        final ResultSet rs = sqlExecuteQuery("select table_name from user_tables");
        try {
            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }
        } finally {
            rs.close();
        }
        return tableNames;
    }

    /**
     * OracleDatabaseUpdater.isTableExist
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#isTableExist(java.lang.String)
     */
    @Override
    public boolean isTableExist(String tableName) throws SQLException {
        ResultSet rs =
            sqlExecuteQuery("select table_name from user_tables where table_name='" + tableName + "'");
        while (rs.next()) {
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

    /**
     * OracleDatabaseUpdater.isConstraintExist
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#isConstraintExist(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isConstraintExist(String tableName, String constraintName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select constraint_type from user_constraints where constraint_name='"
                + constraintName + "' and table_name='" + tableName + "'");
        while (rs.next()) {
            // index already existed
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

    /**
     * OracleDatabaseUpdater.getColumnType
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#getColumnType(java.lang.String, java.lang.String)
     */
    @Override
    public String getColumnType(String tableName, String columnName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select data_type from user_tab_columns where table_name='" + tableName
                + "' and column_name='" + columnName + "'");
        try {
            while (rs.next()) {
                return rs.getString("data_type");
            }
        } finally {
            rs.close();
        }

        return "";

    }

    /**
     * OracleDatabaseUpdater.isColumnExist
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#isColumnExist(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isColumnExist(String tableName, String columnName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select table_name from all_tab_columns where table_name='" + tableName
                + "' and column_name='" + columnName + "'");
        while (rs.next()) {
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

    /**
     * OracleDatabaseUpdater.dropConstraintOnTable
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#dropConstraintOnTable(java.lang.String)
     */
    @Override
    public void dropConstraintOnTable(String tableName) throws SQLException {
        final Map<String, String> constrains = new HashMap<String, String>();
        final ResultSet rs =
            sqlExecuteQuery("SELECT a.constraint_name, a.table_name, b.table_name from user_constraints a join user_constraints b on (a.r_CONSTRAINT_NAME=b.CONSTRAINT_NAME) where b.table_name='"
                + tableName + "'");
        while (rs.next()) {
            constrains.put(rs.getString("constraint_name"), rs.getString(2));
        }
        for (final String constraint : constrains.keySet()) {
            if (constraint.toLowerCase().endsWith("_pkey") || constraint.toLowerCase().endsWith("_pk")) {
                continue;
            } else if (constraint.toLowerCase().endsWith("null")) {
                continue;
            } else {
                if (isConstraintExist(constrains.get(constraint), constraint)) {
                    sqlExecuteUpdate("ALTER TABLE " + constrains.get(constraint) + " DROP CONSTRAINT "
                        + constraint + " cascade");
                }
            }
        }
        rs.close();
    }

    /**
     * OracleDatabaseUpdater.createColumn
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#createColumn(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void createColumn(String tableName, String columnName, String columnType) throws SQLException {
        if (!isColumnExist(tableName, columnName)) {
            if (columnType == null) {
                columnType = "VARCHAR2";
            } else if (columnType.equalsIgnoreCase("int8")) {
                columnType = "NUMBER";
            } else if (columnType.equalsIgnoreCase("text")) {
                columnType = "VARCHAR2(4000)";
            } else if (columnType.equalsIgnoreCase("INT4")) {
                columnType = "INTEGER";
            } else if (columnType.toLowerCase().startsWith("varchar(")) {
                columnType = columnType.toLowerCase().replace("varchar(", "varchar2(");
            } else if (columnType.toLowerCase().startsWith("float")) {
                columnType = "FLOAT";
            } else if (columnType.toLowerCase().startsWith("boolean")) {
                columnType = "NUMERIC(1,0)";
            } else if (columnType.toLowerCase().startsWith("timestamptz")) {
                columnType = "TIMESTAMP WITH TIME ZONE";
            }
            final String statement = "ALTER TABLE " + tableName + " ADD " + columnName + " " + columnType;
            sqlExecuteUpdate(statement);
            DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, statement);
        }

        if (isColumnExist(tableName, "id_")) {
            dropColumn(tableName, "id_");
        }
    }

    /**
     * OracleDatabaseUpdater.createRightOnTable
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#createRightOnTable(java.lang.String)
     */
    @Override
    protected void createRightOnTable(String tableName) throws SQLException {
        sqlExecuteUpdate("GRANT SELECT ON  " + tableName + " TO pimsview");
        sqlExecuteUpdate("GRANT SELECT, UPDATE, INSERT, DELETE ON  " + tableName + " TO pimsupdate");
    }

    /**
     * OracleDatabaseUpdater.createTable
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#createTable(java.lang.String)
     */
    @Override
    public void createTable(String tableName) throws SQLException {
        sqlExecuteUpdate("CREATE TABLE " + tableName + " (id_ long)");
        createRightOnTable(tableName);
    }

    /**
     * OracleDatabaseUpdater.getPrimaryColumn
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#getPrimaryColumn(java.lang.String)
     */
    @Override
    String getPrimaryColumn(String tableName) throws SQLException {
        //final Map<String, String> constrains = new HashMap<String, String>();
        final ResultSet rs =
            sqlExecuteQuery("SELECT a.constraint_name,b.column_name from user_constraints a join user_cons_columns b on (a.constraint_name=b.constraint_name) where a.table_name='"
                + tableName + "' and a.constraint_type='P'");
        try {
            while (rs.next()) {
                return rs.getString(2);
            }
        } finally {
            rs.close();
        }

        return null;
    }

    /**
     * OracleDatabaseUpdater.getPrimaryConstraintName
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#getPrimaryConstraintName(java.lang.String)
     */
    @Override
    String getPrimaryConstraintName(String tableName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("SELECT constraint_name,constraint_type from user_constraints where  table_name='"
                + tableName + "' and constraint_type='p'");
        try {
            while (rs.next()) {
                return rs.getString("constraint_name");
            }
        } finally {
            rs.close();
        }

        return null;
    }

    /**
     * OracleDatabaseUpdater.setUniqueKey
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setUniqueKey(java.lang.String, java.util.List)
     */
    @Override
    public void setUniqueKey(String tableName, List<String> keys) throws SQLException {
        String keyName = tableName;
        for (final String key : keys) {
            keyName += "_" + key;
        }
        keyName += "_uq";
        keyName = getShortKeyName(keyName);
        // check index is exist or not
        final ResultSet rs =
            sqlExecuteQueryIgnorCase("SELECT constraint_name,constraint_type from user_constraints where  table_name='"
                + tableName + "' and constraint_type='U'");
        final List<String> oldUniqueKeys = new LinkedList<String>();
        while (rs.next()) {
            //   if (rs.getString("constraint_name").equalsIgnoreCase(keyName))
            //       return;
            oldUniqueKeys.add(rs.getString("constraint_name"));
        }
        // drop old one
        for (final String oldConstraintName : oldUniqueKeys) {
            this.dropConstraint(tableName, oldConstraintName);
        }
        // create new one
        String keyString = keys.get(0);
        for (final String key : keys) {
            if (!key.equalsIgnoreCase(keys.get(0))) {
                keyString = keyString + "," + key;
            }
        }
        // not exist
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG,
            tableName + " is empty? " + this.isTableEmpty(tableName));
        final String statement =
            "ALTER TABLE " + tableName + " ADD CONSTRAINT " + keyName + " UNIQUE (" + keyString + ")";
        sqlExecuteUpdate(statement);

        return;
    }

    /**
     * OracleDatabaseUpdater.setColumnNotNull
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setColumnNotNull(java.lang.String, java.lang.String)
     */
    @Override
    public void setColumnNotNull(String tableName, String columnName) throws SQLException {
        if (isColumnNullable(tableName, columnName))
            sqlExecuteUpdate("ALTER TABLE " + tableName + " modify " + columnName + " Not Null");
    }

    /**
     * OracleDatabaseUpdater.setColumnCanBeNull
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setColumnCanBeNull(java.lang.String, java.lang.String)
     */
    @Override
    public void setColumnCanBeNull(String tableName, String columnName) throws SQLException {
        if (!isColumnNullable(tableName, columnName))
            sqlExecuteUpdate("ALTER TABLE " + tableName + " modify " + columnName + " Null");
    }

    /**
     * OracleDatabaseUpdater.setForeignKey
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setForeignKey(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setForeignKey(String tableName, String constraintName, String foreignTableName,
        String foreignColumnName, String keyId, String onDelete) throws SQLException {
        if (!constraintName.toLowerCase().endsWith("_fk"))
            constraintName = constraintName + "_fk";
        constraintName = getShortKeyName(constraintName);
        if (isConstraintExist(tableName, constraintName)) {
            return;
        }
        if (onDelete == null) {
            onDelete = "SET NULL";
        }
        final String statement =
            "ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " FOREIGN KEY (" + keyId
                + ") REFERENCES " + foreignTableName;
        try {
            sqlExecuteUpdate(statement);
        } catch (final SQLException e) {
            System.err.println(statement);
            throw e;
        }
    }

    /**
     * OracleDatabaseUpdater.isIndexExist
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#isIndexExist(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isIndexExist(String tableName, String columnName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select * from user_ind_columns where column_name='" + columnName
                + "' and table_name='" + tableName + "'");
        while (rs.next()) {
            rs.close();
            return true;
        }
        rs.close();
        return false;
    }

    /**
     * OracleDatabaseUpdater.setIndex
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setIndex(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setIndex(String tableName, String roleIndexName, String colName) throws SQLException {

        if (isIndexExist(tableName, colName)) {
            return;
        }
        roleIndexName = getShortKeyName(roleIndexName + "_inx");

        sqlExecuteUpdate("CREATE INDEX " + roleIndexName + " ON " + tableName + " (" + colName + ")");
        return;
    }

    /**
     * OracleDatabaseUpdater.setDBRevisionNumber
     * 
     * @see org.pimslims.upgrader.DatabaseUpdater#setDBRevisionNumber()
     */
    @Override
    public void setDBRevisionNumber() throws SQLException {
        final int oldReversionNumber = getDBRevisionNumber();
        final int newReveisionNumber = ModelUpdateVersionImpl.getJavaRevisionNumber();

        // when dbversion is newer than JAR version, update will be skiped!
        if (oldReversionNumber < newReveisionNumber) {
            DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --Change revision number from "
                + oldReversionNumber + " to " + newReveisionNumber);
            sqlExecuteUpdate("INSERT into revisionnumber (revision,name,release,tag,author,date_) values ("
                + newReveisionNumber + ",'" + RevisionNumber.getName() + "','" + RevisionNumber.getRelease()
                + "','" + RevisionNumber.getTag() + "','" + RevisionNumber.getAuthor() + "_v_Upgrader','"
                + RevisionNumber.getDate() + "')");
        }
    }

    public boolean isColumnNullable(String tableName, String columnName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select NULLABLE from USER_TAB_COLS where table_name='" + tableName
                + "' and column_name='" + columnName + "'");
        try {
            while (rs.next()) {
                if (rs.getString("NULLABLE").equalsIgnoreCase("N"))
                    return false;
                return true;
            }
        } finally {
            rs.close();
        }
        throw new RuntimeException("Can not find column " + columnName + " in table " + tableName);

    }

    @Override
    //TODO fix this
    public final void changeColumnLength(final String tableName, final String fieldName, String type,
        int length) throws SQLException {
        if ("TEXT".equalsIgnoreCase(type)) {
            type = "VARCHAR2";
            if (0 == length) {
                length = 2000;
            }
        }
        if (length > 0) {
            type = type + " (" + length + ")";
        }
        // a simple ALTER table MODIFY may result in ORA-22858 error
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ADD  " + TEMPORARY_COLUMN + " " + type + " ");
        sqlExecuteUpdate("UPDATE " + tableName + " SET " + TEMPORARY_COLUMN + " = " + fieldName);
        sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP COLUMN " + fieldName);
        sqlExecuteUpdate("ALTER TABLE " + tableName + " RENAME COLUMN " + TEMPORARY_COLUMN + " TO "
            + fieldName);

    }
}
