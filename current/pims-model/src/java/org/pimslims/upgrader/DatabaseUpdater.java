/**
 * created on 14/02/2006
 */
package org.pimslims.upgrader;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.util.MessageHandler;

/**
 * Database implementation, such as: create/load a table/column using provided connection This class is
 * provided for ModelUpdateVersion and testing only!!!
 * 
 * @author bl67
 * 
 */
public class DatabaseUpdater {
    /**
     * PRIMARY_KEY String
     */
    private static final String PRIMARY_KEY = "_pkey";

    // DB criterias:
    private static final String catalog = null;// "pims";

    // could use this.doWork()
    private java.sql.Connection connection;

    protected Statement stmt = null;

    boolean resetStmt = true;

    private Session session;

    static public DatabaseUpdater getUpdater(final java.sql.Connection connection) {
        if (HibernateUtil.isOracleDB()) {
            return new OracleDatabaseUpdater(connection);
        }
        return new DatabaseUpdater(connection);

    }

    static public DatabaseUpdater getUpdater(final ModelUpdateVersionImpl muv) {
        if (HibernateUtil.isOracleDB()) {
            return new OracleDatabaseUpdater(muv);
        }
        // else
        return new DatabaseUpdater(muv);

    }

    protected DatabaseUpdater(final java.sql.Connection connection) {
        this.connection = connection;
    }

    /**
     * @param muv
     */
    protected DatabaseUpdater(final ModelUpdateVersionImpl muv) {
        this.connection = muv.getSession().connection();
        this.session = muv.getSession();
    }

    /**
     * .DatabaseUpdater.doWork
     * 
     * @param work
     * @throws HibernateException
     * @see org.hibernate.Session#doWork(org.hibernate.jdbc.Work)
     */
    private void doWork(Work work) throws HibernateException {
        this.session.doWork(work);
    }

    public void abort() throws SQLException {
        this.connection.rollback();
    }

    public void changeColumnLength(final String tableName, final String fieldName, String type,
        final int length) throws SQLException {
        if (length > 0) {
            type = type + " (" + length + ")";
        }
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ALTER COLUMN " + fieldName + " type " + type);
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void createColumn(final String tableName, final String columnName, String columnType)
        throws SQLException {
        if (columnType == null) {
            columnType = "text";
        }
        final String statement =
            "ALTER TABLE " + tableName + " ADD COLUMN " + columnName.toLowerCase() + " "
                + columnType.toLowerCase();
        sqlExecuteUpdate(statement);
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, statement);
    }

    public final void createSequence(final String sequenceName) throws SQLException {
        sqlExecuteUpdate("CREATE SEQUENCE " + sequenceName + " START 1");
        createRightOnTable(sequenceName);
    }

    /**
     * grant right to table
     * 
     * @param tableName
     * @throws SQLException
     */
    protected void createRightOnTable(final String tableName) throws SQLException {
        //sqlExecuteUpdate("ALTER TABLE " + tableName + " OWNER TO postgres");
        //sqlExecuteUpdate("GRANT ALL ON TABLE " + tableName + " TO postgres");
        sqlExecuteUpdate("GRANT SELECT ON TABLE " + tableName + " TO pimsview");
        sqlExecuteUpdate("GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE " + tableName + " TO pimsupdate");
        sqlExecuteUpdate("GRANT ALL ON TABLE " + tableName + " TO pimsadmin");
    }

    public void createTable(final String tableName) throws SQLException {
        sqlExecuteUpdate("CREATE TABLE " + tableName + " ()");
        createRightOnTable(tableName);
    }

    public final void dropColumn(final String tableName, final String columnName) throws SQLException {
        if (isColumnExist(tableName, columnName)) {
            dropConstraintInTable(tableName);
            DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "Dropping column: " + tableName + "."
                + columnName);
            sqlExecuteUpdate("ALTER TABLE " + tableName + " drop COLUMN " + columnName);
        } else {
            DebugMessageHandler.LOG(MessageHandler.Level.WARN, "Column does not exist: " + tableName + "."
                + columnName);

        }
    }

    public void dropConstraint(final String tableName, final String constraintName) throws SQLException {
        sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraintName + " CASCADE");
    }

    /**
     * drop all constraints in this table (may point to other tables)
     * 
     * @param tableName
     * @throws SQLException
     */
    public void dropConstraintInTable(final String tableName) throws SQLException {
        final List<String> constrains = new LinkedList<String>();
        final ResultSet rs =
            sqlExecuteQuery("SELECT constraint_name FROM information_schema.table_constraints where table_name='"
                + tableName + "'");
        while (rs.next()) {
            constrains.add(rs.getString("constraint_name"));
        }
        for (final String constraint : constrains) {
            if (constraint.toLowerCase().endsWith(PRIMARY_KEY)) {
                continue;
            } else if (constraint.toLowerCase().endsWith("null")) {
                continue;
            } else if (isConstraintExist(tableName, constraint)) {
                sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraint + " cascade");
                // note this removes uniqueness constraints
                // they are restored in ModelUpdateVersionImpl.upgradeAttribute
            }

        }
    }

    /**
     * drop all constraints on(point to) this table
     * 
     * @param tableName
     * @throws SQLException
     */
    public void dropConstraintOnTable(final String tableName) throws SQLException {
        final Map<String, String> constrains = new HashMap<String, String>();
        final ResultSet rs =
            sqlExecuteQuery("SELECT a.constraint_name, a.table_name, b.table_name FROM information_schema.table_constraints a, information_schema.constraint_table_usage b WHERE b.constraint_name=a.constraint_name AND b.table_name='"
                + tableName + "'");
        while (rs.next()) {
            constrains.put(rs.getString("constraint_name"), rs.getString(2));
        }
        for (final String constraint : constrains.keySet()) {
            if (constraint.toLowerCase().endsWith(PRIMARY_KEY)) {
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
    }

    public final void dropTable(final String tableName) throws SQLException {
        if (isTableExist(tableName)) {
            // drop all constraints on this table
            dropConstraintOnTable(tableName);

            sqlExecuteUpdate("Drop TABLE " + tableName);
        }
    }

    public Set findAll(final String table, final String Attribute) throws SQLException {
        final Set results = new HashSet();
        final ResultSet rs = sqlExecuteQuery("select " + Attribute + " from " + table);
        while (rs.next()) {
            results.add(rs.getObject(1));
        }
        return results;
    }

    public String getColumnType(final String tableName, final String columnName) throws SQLException {
        // if(connection==null)
        // getConnection();
        final DatabaseMetaData metadata = connection.getMetaData();

        final ResultSet columnNames =
            metadata.getColumns(catalog, "public", tableName.toLowerCase(), columnName.toLowerCase());
        while (columnNames.next()) {
            /*
             * FIELD NAME - 4 DATA TYPE(short) - 5 DATA TYPE(String, depend on db) - 6
             */
            String columnType = columnNames.getString(6);
            if (columnType.equalsIgnoreCase("text")) {
                columnType = "varchar";
            }
            return columnType;
        }
        throw new RuntimeException("No such column: " + tableName + ":" + columnName);

    }

    /**
     * check Version Number of postgres DB >=8.1
     */
    public boolean checkPostgresDBVersionNumber() throws SQLException {
        final ResultSet rs = sqlExecuteQuery("SELECT version()");
        rs.next();
        final String versionString = rs.getString("version").toLowerCase(); // should
        // get like  PostgreSQL 8.1.3 on....
        if (versionString.startsWith("postgresql 8.0") || versionString.startsWith("postgresql 7")) {
            return false;
        }
        return true;
    }

    /**
     * check Version Number of postgres driver >=8.1
     */
    public boolean checkPostgresJDBCVersionNumber() {
        final String versionString = org.postgresql.Driver.getVersion().toLowerCase();
        if (versionString.startsWith("postgresql 8.0") || versionString.startsWith("postgresql 7")) {
            return false;
        }
        return true;
    }

    // get revision number from DB
    // return is 1 for pims0.3
    // return 16 for pims0.4 without revision Number table
    public int getDBRevisionNumber() throws SQLException {
        int rn;

        if (!isTableExist("revisionnumber")) {
            if (!isTableExist("hold_holder")) {
                rn = 1;
            } else {
                rn = 10;
            }
        } else {
            final ResultSet rs =
                sqlExecuteQuery("select revision from revisionnumber ORDER BY revision DESC");
            if (rs.next()) {
                rn = rs.getInt("revision");
            } else {
                rn = -2;
            }
        }

        return rn;
    }

    public void setDBRevisionNumber() throws SQLException {
        final int oldReversionNumber = getDBRevisionNumber();
        final int newReveisionNumber = ModelUpdateVersionImpl.getJavaRevisionNumber();
        if (!isTableExist("revisionnumber")) {
            createTable("revisionnumber");
            createColumn("revisionnumber", "revision", "int4");
            setColumnNotNull("revisionnumber", "revision");
            createColumn("revisionnumber", "name", "varchar(254)");
            setColumnNotNull("revisionnumber", "name");
            createColumn("revisionnumber", "release", "varchar(254)");
            setColumnNotNull("revisionnumber", "release");
            createColumn("revisionnumber", "tag", "varchar(254)");
            setColumnNotNull("revisionnumber", "tag");
            createColumn("revisionnumber", "author", "varchar(254)");
            setColumnNotNull("revisionnumber", "author");
            createColumn("revisionnumber", "date_", "varchar(254)");
            setColumnNotNull("revisionnumber", "date_");

            setPrimaryKey("revisionnumber", "revision");
        }
        // "date" is a reserved word in Oracle
        if (isColumnExist("revisionnumber", "date")) {
            renameColumn("revisionnumber", "date", "date_");
        }

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

    public final boolean isColumnEmpty(final String tableName, final String colName) throws SQLException {

        final ResultSet rs = sqlExecuteQuery("SELECT " + colName + " FROM " + tableName);
        while (rs.next()) {
            if (rs.getObject(1) != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isColumnExist(final String tableName, final String columnName) throws SQLException {
        // if(connection==null)
        // getConnection();
        final DatabaseMetaData metadata = connection.getMetaData();

        final ResultSet columnNames =
            metadata.getColumns(catalog, "public", tableName.toLowerCase(), columnName.toLowerCase());
        while (columnNames.next()) {
            columnNames.close();
            return true;
        }
        columnNames.close();
        return false;

    }

    public boolean isConstraintExist(final String tableName, final String constraintName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select constraint_type from information_schema.table_constraints where constraint_name='"
                + constraintName + "' and table_name='" + tableName + "'");
        while (rs.next()) {
            // index already existed
            return true;
        }
        return false;

    }

    public boolean isIndexExist(final String tableName, final String indexName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select * from pg_indexes where indexname='" + indexName + "' and tablename='"
                + tableName + "'");
        while (rs.next()) {
            return true;
        }
        return false;
    }

    public final boolean isTableEmpty(final String tableName) throws SQLException {

        final ResultSet rs = sqlExecuteQuery("SELECT * FROM " + tableName);
        while (rs.next()) {
            return false;
        }
        return true;
    }

    public boolean isTableExist(final String tableName) throws SQLException {
        // if(connection==null)
        // getConnection();
        final DatabaseMetaData metadata = connection.getMetaData();

        final ResultSet tableNames =
            metadata.getTables(catalog, "public", tableName.toLowerCase(), new String[] { "TABLE" });
        while (tableNames.next()) {
            return true;
        }
        return false;

    }

    public List<String> getAllTableNames() throws SQLException {
        // if(connection==null)
        // getConnection();
        final DatabaseMetaData metadata = connection.getMetaData();

        final ResultSet tableNames = metadata.getTables(catalog, "public", "%", new String[] { "TABLE" });
        final List<String> results = new LinkedList<String>();
        while (tableNames.next()) {
            results.add(tableNames.getString(3));
        }
        return results;

    }

    public final boolean isSequenceExist(final String sequenceName) throws SQLException {
        final ResultSet rs =
            sqlExecuteQuery("select * from pg_class  where relname = '" + sequenceName + "'");
        if (rs.next()) {
            // index already existed
            return true;
        }
        return false;

    }

    public final void dropSequence(final String sequenceName) throws SQLException {
        if (isSequenceExist(sequenceName)) {
            sqlExecuteUpdate("DROP SEQUENCE " + sequenceName);
        } else {
            System.out.println(sequenceName + " does not exist!");
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameColumn(java.lang.String,
     *      java.lang.String,java.lang.String)
     */
    public void renameColumn(final String tableName, final String oldColumnName, final String newColumnName)
        throws SQLException {

        sqlExecuteUpdate("ALTER TABLE " + tableName + " RENAME COLUMN " + oldColumnName + " TO "
            + newColumnName);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameConstraint(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void renameConstraint(final String tableName, final String oldConstraintName,
        final String newConstraintName, final String restSQL) throws SQLException {
        if (isConstraintExist(tableName, oldConstraintName)) {
            sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP CONSTRAINT " + oldConstraintName
                + " cascade");
        }
        if (!isConstraintExist(tableName, newConstraintName)) {
            sqlExecuteUpdate("ALTER TABLE " + tableName + " ADD CONSTRAINT " + newConstraintName + " "
                + restSQL);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameTable(java.lang.String, java.lang.String)
     */
    public void renameTable(final String oldTableName, final String newTableName) throws SQLException {
        // drop all constraints of this table
        dropConstraintInTable(oldTableName);
        // drop all constraints on this table
        dropConstraintOnTable(oldTableName);

        sqlExecuteUpdate("ALTER TABLE " + oldTableName + " RENAME TO " + newTableName);

    }

    public void setColumnNotNull(final String tableName, final String columnName) throws SQLException {
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ALTER COLUMN " + columnName.toLowerCase()
            + " Set Not Null");
    }

    public void setColumnUnique(final String tableName, final String columnName) throws SQLException {
        String constraint = makeConstraintName(tableName, columnName);
        if (isConstraintExist(tableName, constraint)) {
            sqlExecuteUpdate("ALTER TABLE " + tableName + " DROP CONSTRAINT " + constraint + " cascade");
        }
        sqlExecuteUpdate("ALTER TABLE " + tableName + "  ADD CONSTRAINT " + constraint + " UNIQUE("
            + columnName + ")");
    }

    private int count = 0;

    /**
     * DatabaseUpdater.makeKeyName
     * 
     * @param tableName
     * @param columnName
     * @return a key name, less that 30 chars for Oracle
     */
    private String makeConstraintName(final String tableName, final String columnName) {
        String key = tableName + "_" + columnName + "_key";
        if (30 < key.length()) {
            return tableName + (count++);
        }
        return key;
    }

    /**
     * Drop a not null constraint
     * 
     * The SQL syntax is different than for other constraints, not null constraints don't have names.
     * 
     * @param tableName the table to alter
     * @param columnName the column that can now be null
     * @throws SQLException
     */
    public void setColumnCanBeNull(final String tableName, final String columnName) throws SQLException {
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ALTER COLUMN " + columnName.toLowerCase()
            + " drop Not Null");
    }

    public void setConnection(final java.sql.Connection connection) {
        this.connection = connection;
    }

    public void setDefaultValue(final String tableName, final String columnName, final Object defaultValue)
        throws SQLException {
        sqlExecuteUpdate("update " + tableName + " set " + columnName + "=" + defaultValue);
    }

    /**
     * 
     * @param tableName the table needs to create constraint
     * @param constraintName the name of this constraint
     * @param foreignTableName the foreign table referenced(without "_fk")
     * @param foreignColumnName
     * @param keyId the column referenced
     * @param onDelete "Set Null" or "CASCADE"
     * @throws SQLException
     */
    public void setForeignKey(final String tableName, String constraintName, final String foreignTableName,
        final String foreignColumnName, final String keyId, String onDelete) throws SQLException {
        constraintName = getShortKeyName(constraintName + "_fk");
        if (isConstraintExist(tableName, constraintName)) {
            return;
        }
        if (onDelete == null) {
            onDelete = "SET NULL";
        }
        // ALTER TABLE mole_molecule_commna ADD CONSTRAINT
        // mole_molecule_commna_fk FOREIGN KEY (moleculeid) REFERENCES
        // mole_molecule (memopsbaseclassid) MATCH SIMPLE ON UPDATE NO ACTION ON
        // DELETE SET NULL;
        // sqlExecuter("ALTER TABLE "+ tableName+" ADD CONSTRAINT "+
        // constraintName+"_fk FOREIGN KEY ("+keyId+") REFERENCES
        // "+foreignTableName+" ("+foreignKeyId+") MATCH SIMPLE ON UPDATE NO
        // ACTION ON DELETE "+onDelete);
        final String statement =
            "ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " FOREIGN KEY (" + keyId
                + ") REFERENCES " + foreignTableName + " (" + foreignColumnName
                + ") MATCH SIMPLE ON UPDATE NO ACTION ON DELETE " + onDelete;
        try {
            sqlExecuteUpdate(statement);
        } catch (final SQLException e) {
            System.err.println(statement);
            throw e;
        }
    }

    /**
     * @param tableName the table needs to create constraint
     * @param foreignTableName the foreign table referenced
     * @param keyId the column referenced
     * @throws SQLException
     */
    public final void setForeignKeyOfMultiAttributeTable(final String tableName,
        final String foreignTableName, final String keyId) throws SQLException {
        // ALTER TABLE mole_molecule_commna ADD CONSTRAINT
        // mole_molecule_commna_fk FOREIGN KEY (moleculeid) REFERENCES
        // mole_molecule (memopsbaseclassid) MATCH SIMPLE ON UPDATE NO ACTION ON
        // DELETE SET NULL;
        final String constraintName = getShortKeyName(tableName + "_fk");
        if (isConstraintExist(tableName, constraintName)) {
            return;
        }
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " FOREIGN KEY ("
            + keyId + ") REFERENCES " + foreignTableName
            + (HibernateUtil.isOracleDB() ? "" : " ON UPDATE NO ACTION") + " ON DELETE CASCADE");

    }

    public void setIndex(final String tableName, String roleIndexName, final String colName)
        throws SQLException {
        roleIndexName = getShortKeyName(roleIndexName + "_inx");
        if (isIndexExist(tableName, roleIndexName)) {
            return;
        }
        sqlExecuteUpdate("CREATE INDEX " + roleIndexName + " ON " + tableName + " USING btree (" + colName
            + ")");
        return;

    }

    public final void setPrimaryKey(final String tableName, final String keyId) throws SQLException {
        // ALTER TABLE mole_molecule ADD CONSTRAINT mole_molecule_pkey PRIMARY
        // KEY(memopsbaseclassid);
        //rename existing priary key
        final String existingPK = getPrimaryColumn(tableName);
        if (existingPK != null && existingPK.length() > 0 && (!existingPK.equalsIgnoreCase(keyId))) {
            renameColumn(tableName, existingPK, keyId);
            // could return;  
        }
        final String constraintName = getShortKeyName(tableName + PRIMARY_KEY);
        if (isConstraintExist(tableName, constraintName)) {
            return;
        }
        String primaryConstraintNam = getPrimaryConstraintName(tableName);
        if (primaryConstraintNam != null && (!primaryConstraintNam.equalsIgnoreCase(constraintName))) {
            dropConstraint(tableName, primaryConstraintNam);
        }
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " PRIMARY KEY ("
            + keyId + ")");
    }

    /**
     * create a name which length <30 DatabaseUpdater.getShortKeyName
     * 
     * TODO no, other code assumes that primer key names end _pkey
     * 
     * @param string
     * @return
     */
    protected String getShortKeyName(String oldName) {
        if (oldName.length() < 30) {
            return oldName;
        }
        String newName = oldName.toUpperCase();
        if (newName.contains("RESEARCHOBJECTIVE")) {
            newName = newName.replace("RESEARCHOBJECTIVE", "RO");
        } else if (newName.contains("PARAMETERDEFINITION")) {
            newName = newName.replace("PARAMETERDEFINITION", "PD");
        } else if (newName.contains("MOLECULE")) {
            newName = newName.replace("MOLECULE", "MOLE");
        } else if (newName.contains("SCHEDULEPLANOFFSET")) {
            newName = newName.replace("SCHEDULEPLANOFFSET", "SPOFFSET");
        } else if (newName.contains("SCHEDULEDTASK")) {
            newName = newName.replace("SCHEDULEDTASK", "SCHTASK");
        } else if (newName.contains("ABSTRACT")) {
            newName = newName.replace("ABSTRACT", "ABS");
        } else if (newName.contains("REFHOLDERSOURCE")) {
            newName = newName.replace("REFHOLDERSOURCE", "RHSource");
        } else if (newName.contains("REFSAMPLESOURCE")) {
            newName = newName.replace("REFSAMPLESOURCE", "RSSource");
        } else if (newName.contains("PERMISSION")) {
            newName = newName.replace("PERMISSION", "PERM");
        } else if (newName.contains("INSTRUMENT")) {
            newName = newName.replace("INSTRUMENT", "INST");
        } else if (newName.contains("OTHERROLE")) {
            newName = newName.replace("OTHERROLE", "OR");
        } else if (newName.contains("POSITION")) {
            newName = newName.replace("POSITION", "POS");
        } else if (newName.contains("REFHOLDEROFFSET")) {
            newName = newName.replace("REFHOLDEROFFSET", "RHOFFSET");
        } else if (newName.contains("HOLDERTYPESOURCE")) {
            newName = newName.replace("HOLDERTYPESOURCE", "HTSource");
        } else if (newName.contains("HAZARDPHRASE")) {
            newName = newName.replace("HAZARDPHRASE", "HZPHRASE");
        }
        if (newName.length() >= 30) {
            String[] names = newName.split("_");
            newName = names[1] + "_" + names[2] + "_" + names[names.length - 1];
        }
        if (newName.length() >= 30) {
            throw new RuntimeException(newName + " is too long");
        }
        return newName;

    }

    public final void setPrimaryKeysMtoM(final String tableName, final String keyId1, final String keyId2)
        throws SQLException {
        final String constraintName = getShortKeyName(tableName + PRIMARY_KEY);
        if (isConstraintExist(tableName, constraintName)) {
            return;
        }
        // ALTER TABLE mole_molecule_commna ADD CONSTRAINT
        // mole_molecule_commna_pkey PRIMARY KEY(moleculeid, order_);
        sqlExecuteUpdate("ALTER TABLE " + tableName + " ADD CONSTRAINT " + constraintName + " PRIMARY KEY ("
            + keyId1 + "," + keyId2 + ")");
    }

    /**
     * set uniquekey of a table if these keys are not unique for existing data, try to fix it if there are two
     * keys and one of them is serial
     * 
     * @param tableName
     * @param keys
     * @throws SQLException
     */
    public void setUniqueKey(final String tableName, final List<String> keys) throws SQLException {
        // TODO this is a temp solution for unique check, it works in this
        // version,
        // but may fail in the future
        if (keys.contains("serial") && keys.size() == 2) {
            uniqueCheck(tableName, keys);
        }
        String keyName = tableName;
        for (final String key : keys) {
            keyName += "_" + key;
        }
        keyName += "_uq";
        keyName = getShortKeyName(keyName);
        // check index is exist or not
        final ResultSet rs =
            sqlExecuteQueryIgnorCase("select constraint_type,constraint_name from information_schema.table_constraints where constraint_type='UNIQUE' and  table_name='"
                + tableName.toLowerCase() + "'");
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

    public final ResultSet sqlExecuteQuery(String sql) throws SQLException {
        if (HibernateUtil.isOracleDB())
            sql = sql.toUpperCase();
        else
            sql = sql.toLowerCase();
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --sql-- " + sql);

        try {
            resetStatement();
            final ResultSet rs = stmt.executeQuery(sql);

            return rs;
        } catch (SQLException e) {
            System.err.println(sql);
            throw e;
        }

    }

    protected final ResultSet sqlExecuteQueryIgnorCase(final String sql) throws SQLException {

        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --sql-- " + sql);

        resetStatement();

        final ResultSet rs = stmt.executeQuery(sql);

        return rs;

    }

    public void resetStatement() throws SQLException {
        if (resetStmt && stmt != null)
            stmt.close();
        stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
    }

    public final PreparedStatement sqlPrepareUpdate(final String sql) throws SQLException {
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --sql-- " + sql);
        return connection.prepareStatement(sql);
    }

    public final void sqlExecuteUpdate(String sql) throws SQLException {
        if (HibernateUtil.isOracleDB())
            sql = sql.toUpperCase();
        else
            sql = sql.toLowerCase();
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --sql-- " + sql);

        // this sql is set to be lower case!!
        try {
            final Statement _stmt = connection.createStatement();
            _stmt.executeUpdate(sql);
            _stmt.close();
        } catch (final SQLException e) {
            System.err.println(sql);
            throw e;
        }
    }

    // public final void createConstraint(String tableName, String
    // constraintName, String type,)

    final void sqlExecuteUpdateIgnorCase(final String sql) throws SQLException {

        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  --sql-- " + sql);
        // this sql is NOT set to be lower case!!
        final Statement _stmt = connection.createStatement();
        _stmt.executeUpdate(sql);
        _stmt.close();
    }

    /**
     * get primary column
     * 
     * Note that this may fail, since the older version of the code to make a key name sometimes truncates it.
     */
    String getPrimaryColumn(final String tableName) throws SQLException {
        final ResultSet rs =
        //   sqlExecuteQueryIgnorCase("SELECT a.table_name, b.column_name FROM information_schema.table_constraints a, information_schema.constraint_column_usage b WHERE  a.constraint_type='PRIMARY KEY' AND b.constraint_name=a.constraint_name and a.table_name='"
        //      + tableName.toLowerCase() + "'");
            sqlExecuteQueryIgnorCase("SELECT table_name, column_name FROM information_schema.constraint_column_usage WHERE  constraint_name like '%_pk%'"
                + "and table_name='" + tableName.toLowerCase() + "'");

        while (rs.next()) {
            //System.out.println(rs.getString("table_name") + ":" + rs.getString("column_name"));
            return (rs.getString("column_name"));
        }
        return null;
    }

    /**
     * get primary Constraint name Note that this may fail, since the older version of the code to make a key
     * name sometimes truncates it.
     */
    String getPrimaryConstraintName(final String tableName) throws SQLException {
        final ResultSet rs =
        //   sqlExecuteQueryIgnorCase("SELECT a.table_name, b.column_name FROM information_schema.table_constraints a, information_schema.constraint_column_usage b WHERE  a.constraint_type='PRIMARY KEY' AND b.constraint_name=a.constraint_name and a.table_name='"
        //      + tableName.toLowerCase() + "'");
            sqlExecuteQueryIgnorCase("SELECT constraint_name FROM information_schema.constraint_column_usage WHERE  constraint_name like '%_pk%'"
                + "and table_name='" + tableName.toLowerCase() + "'");

        while (rs.next()) {
            //System.out.println(rs.getString("table_name") + ":" + rs.getString("column_name"));
            return (rs.getString("constraint_name"));
        }
        return null;
    }

    /**
     * cuurently only check/fix unqiue keys which has serial and size=2
     * 
     * @param tableName
     * @param keys
     * @throws SQLException
     */
    private void uniqueCheck(final String tableName, final List<String> keys) throws SQLException {

        if (true/* postgresBefore8_1() */) {
            return;
        }/*
         String anotherKeyName;
         final String primaryColumnName = getPrimaryColumn(tableName);

         if (keys.get(0).equalsIgnoreCase("serial")) {
            anotherKeyName = keys.get(1);
         } else {
            anotherKeyName = keys.get(0);
         }

         DebugMessageHandler.LOG(MessageHandler.INFO, "  --Unique Check-- serial+" + anotherKeyName
            + "  in table " + tableName + "! ");

         // get a list of another key
         final Set<Long> anotherKeys = findAll(tableName, anotherKeyName);

         // unique check for each of another key
         boolean foundDuplicate = false;
         for (final Long anotherKey : anotherKeys) {
            final ResultSet rs =
                sqlExecuteQueryIgnorCase("select " + primaryColumnName + ", serial from " + tableName
                    + " where " + anotherKeyName + "='" + anotherKey + "' ORDER BY serial");
            int number = 0;
            while (rs.next()) {
                number++;// this should be the correct serial(start from 1)
                if (rs.getInt("serial") != number) {
                    rs.updateInt("serial", number);
                    foundDuplicate = true;
                    rs.updateRow();
                }
            }
         }
         if (foundDuplicate) {
            DebugMessageHandler.LOG(DebugMessageHandler.WARN, "  --Not Unique-- serial+" + anotherKeyName
                + " is not Unique in table " + tableName + ", it is fixed by regenerate the serial number! ");
         } */

    }

    /**
     * @param dbSequenceName
     * @throws SQLException
     */
    public long getNextSeqVal(final String dbSequenceName) throws SQLException {
        // create the sequence if it is not there
        if (!HibernateUtil.isOracleDB()) {
            if (!isSequenceExist(dbSequenceName)) {
                createSequence(dbSequenceName);
            }
        }

        int next = 0;
        //for postgres
        String sqlString = "SELECT nextval('" + dbSequenceName + "');";
        //for oracle
        if (HibernateUtil.isOracleDB()) {
            sqlString = "select " + dbSequenceName + ".NEXTVAL from dual";
        }

        final ResultSet rs = sqlExecuteQueryIgnorCase(sqlString);
        rs.next();
        next = rs.getInt(1);

        return next;

    }

    /**
     * @param dbSequenceName
     * @param value
     * @throws SQLException
     * 
     */
    public void setSequenceStart(final String dbSequenceName, final long value) throws SQLException {
        // create the sequence if it is not there
        if (!HibernateUtil.isOracleDB()) {
            //for postgres
            if (!isSequenceExist(dbSequenceName)) {
                createSequence(dbSequenceName);
            }

            this.sqlExecuteQueryIgnorCase("SELECT setval('" + dbSequenceName + "', " + value + ");");
        } else {
            //for oracle
            //WARNING: on oracle following code will cause data saved to DB without commit!!
            final long currentValue = getNextSeqVal(dbSequenceName);
            final long increment = value - currentValue;
            this.sqlExecuteQueryIgnorCase("alter sequence " + dbSequenceName + " increment by " + increment);
            sqlExecuteQueryIgnorCase("select " + dbSequenceName + ".NEXTVAL from dual");
            this.sqlExecuteQueryIgnorCase("alter sequence " + dbSequenceName + " increment by 1");
        }
    }

    /**
     * 
     * @param primaryIDFrom
     * @param primaryIDTo
     * @param columnNames the map of columns (columnFrom, columnTo)
     * @throws SQLException
     */
    public void copyColumnData(final String fromTable, final String toTable, final String primaryIDFrom,
        final String primaryIDTo, Map<String, String> columnNames, final boolean checkIDExist)
        throws SQLException {
        if (columnNames == null) {
            columnNames = Collections.EMPTY_MAP;
        }
        //get string of columnsFrom and to
        String columnsFrom = " " + primaryIDFrom + " ";
        for (final String columnFrom : columnNames.keySet()) {
            columnsFrom += ", " + columnFrom + " ";
        }

        if (checkIDExist) {
            //insert primaryIDFrom if not exist
            final ResultSet rsFrom = sqlExecuteQuery("select " + columnsFrom + " from " + fromTable);
            resetStatement();
            final PreparedStatement psp_insert =
                sqlPrepareUpdate("insert into " + toTable + " (" + primaryIDTo + ") values (?)");
            while (rsFrom.next()) {
                final Long ID = rsFrom.getLong(primaryIDFrom);
                if (ID <= 0) {
                    continue;
                }
                final ResultSet rsTo =
                    sqlExecuteQuery("select * from " + toTable + " where " + primaryIDTo + " = " + ID);
                if (!rsTo.next()) //if id not found insert id
                {
                    psp_insert.setLong(1, ID);
                    psp_insert.executeUpdate();
                }
            }
        }

        //copy values
        for (final String columnFrom : columnNames.keySet()) {
            final String columnsTo = columnNames.get(columnFrom);
            final String sqlCopy =
                "update " + toTable + " set " + columnsTo + "=" + fromTable + "." + columnFrom + " from "
                    + fromTable + " where " + toTable + "." + primaryIDTo + "=" + fromTable + "."
                    + primaryIDFrom;
            sqlExecuteUpdate(sqlCopy);
        }
    }

    /**
     * DatabaseUpdater.setColumnNullable
     * 
     * @param datatype
     * @param classTableName
     * @param roleColName
     * 
     * @throws SQLException
     */
    public void setColumnNullable(String tableName, String columnName, String datatype) throws SQLException {
        String sql;
        if (HibernateUtil.isOracleDB()) {
            sql =
                "ALTER TABLE " + tableName + " MODIFY " + columnName.toLowerCase() + " " + datatype
                    + "  Null";
        } else {
            sql = "ALTER TABLE " + tableName + " ALTER COLUMN " + columnName.toLowerCase() + " Drop Not Null";
        }
        sqlExecuteUpdate(sql);
        //System.out.println(sql);
    }

}
