/**
 * 
 */
package org.pimslims.upgrader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaAttributeImpl;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;
import org.pimslims.metamodel.MetaUtils;
import org.pimslims.persistence.HibernateUtil;
import org.pimslims.util.MessageHandler;

/**
 * @author bl67
 * 
 */

public class ModelUpdateVersionImpl extends WritableVersionImpl implements ModelUpdateVersion {
    // lastUpgratedVersion limit the latest version(of ccp.jar) can be upgraded
    static int lastUpgradedVersion = new Integer(Messages.getString("lastUpgratedVersionNumber"));//$NON-NLS-1$

    static boolean DEBUG = true;

    private DatabaseUpdater dbUpdater = null;

    private final boolean createConstrains = true;

    /**
     * @param session
     */
    public ModelUpdateVersionImpl(final String username, final ModelImpl model, final Timestamp date,
        EntityManager entityManager) {
        super(username, model, date, entityManager);
        this.dbUpdater = DatabaseUpdater.getUpdater(this);

        // check the postgres DB version number and JDBC version Number >=8.1
        try {
            if (!HibernateUtil.isOracleDB()) {
                if (dbUpdater.checkPostgresDBVersionNumber() == false) {
                    DebugMessageHandler
                        .LOG(MessageHandler.Level.WARN,
                            "The version of postgres DB is below 8.1! Please upgrade your postgres to 8.1 or later"); //$NON-NLS-1$
                    Thread.sleep(3000);
                }
            }

        } catch (final InterruptedException e) {
            if (!this.isCompleted()) {
                this.abort();
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final SQLException e) {
            if (!this.isCompleted()) {
                this.abort();
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * @return Returns the dbUpdater.
     */
    public DatabaseUpdater getDbUpdater() {
        return dbUpdater;
    }

    /**
     * Update a model class and its attributes but without roles
     * 
     * @param metaClass the model class to be updated
     * @throws ConstraintException
     */

    public final void updateMetaClassAttributes(final MetaClass metaClass) throws ConstraintException,
        SQLException {
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG,
            " --Updating Class without role:" + metaClass.getName()); //$NON-NLS-1$

        String tableName = MetaUtils.getClassTableName(metaClass);
        final boolean isTableExist = dbUpdater.isTableExist(tableName);
        if (!isTableExist) {
            dbUpdater.createTable(tableName);
            dbUpdater.createColumn(tableName, MetaUtils.getKeyName(metaClass), MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
        }
        // TODO was dbUpdater.dropConstraintInTable(tableName);

        /*
         * create attributes for class
         */
        final java.util.Map metaAttributes = ((MetaClassImpl) metaClass).getAllAttributes();
        for (final Iterator iter = metaAttributes.values().iterator(); iter.hasNext();) {
            final MetaAttribute attribute = (MetaAttribute) iter.next();
            this.upgradeAttribute(attribute);
        }
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "  ==Updated!"); //$NON-NLS-1$

    }

    /**
     * ModelUpdateVersionImpl.setKey
     * 
     * // each class has a primary key which link with its super class,
     * 
     * @param metaClass
     * @throws SQLException
     * @throws ConstraintException
     */
    public void setKey(final MetaClass metaClass) throws SQLException, ConstraintException {
        final String tableName = MetaUtils.getClassTableName(metaClass);
        final String keyName = MetaUtils.getKeyName(metaClass);

        dbUpdater.setPrimaryKey(tableName, keyName);
        dbUpdater.setColumnNotNull(tableName, keyName);

        /*
         * create constrain for super class, exception top class: MemopsBaseClass
         */
        if (metaClass.getSupertype() instanceof MetaClassImpl) {
            try {
                DebugMessageHandler.LOG(MessageHandler.Level.DEBUG,
                    " --Create Constraint/ForeignKey for super class:" + metaClass.getSupertype()); //$NON-NLS-1$

                final String supClassTableName = MetaUtils.getSupperClassTableName(metaClass);
                final String supClassKeyName = MetaUtils.getKeyName(metaClass.getSupertype());
                final String subClassFKName = MetaUtils.getSubClassFkName(metaClass);
                // if supper class is not created yet, creat it first
                if (!dbUpdater.isTableExist(supClassTableName)) {
                    updateMetaClassAttributes(metaClass.getSupertype());
                }
                dbUpdater.setForeignKey(tableName, subClassFKName, supClassTableName, supClassKeyName,
                    keyName, "cascade" //$NON-NLS-1$
                );
            } catch (final RuntimeException e) {
                System.out.println("Cant create superclass constraint for: " + metaClass.getMetaClassName() //$NON-NLS-1$
                    + "\n" + e.getLocalizedMessage() //$NON-NLS-1$
                );
                throw e;
            }
        }
    }

    /**
     * Update a model class' roles
     * 
     * @param metaClass the model class to be updated
     * @throws ConstraintException
     */
    public final void updateMetaRoleOfClass(final MetaClass metaClass) throws ConstraintException,
        SQLException {
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, " Update Class " //$NON-NLS-1$
            + metaClass.getName() + "'s role:"); //$NON-NLS-1$
        /*
        * Create rolse for class
        */

        final java.util.Map metaRoles = ((MetaClassImpl) metaClass).getDeclaredMetaRoles();
        for (final Iterator iter = metaRoles.values().iterator(); iter.hasNext();) {
            final MetaRole role = (MetaRole) iter.next();
            this.upgradeAssociation(role);
        }

        /*
         * Create uniqueKeys
         */
        // the uniqueKeys will be selected from keyNames and put into uniqueKeys
        final List<String> keyNames = ((MetaClassImpl) metaClass).getKeyNames();
        if (keyNames.size() > 0) {
            try {
                dbUpdater.setUniqueKey(MetaUtils.getClassTableName(metaClass),
                    getUniqueKeys(metaClass, keyNames));
            } catch (final org.postgresql.util.PSQLException e) {
                final String tableName = MetaUtils.getClassTableName(metaClass);
                final List<String> UniqueKeysNames = getUniqueKeys(metaClass, keyNames);
                // the existed data may have null value
                throw new RuntimeException(
                    metaClass.getName()
                        + "'s role "
                        + keyNames.toString().replaceAll(", ", "+")
                        + " contains duplicated value which is not allowed in new version!\n To fix this: remove duplicated value in DB's table "
                        + tableName + "." + UniqueKeysNames.toString().replaceAll(", ", "+"), e);
            }
        }

        //TODO there is a problem with this - if there are duplicate rows, the whole upgrade fails

        /*
         * List <String >uniqueKeys = new LinkedList<String>(); List <String>
         * keyNames=((CCPNMetaClass)metaClass).getKeyNames(); if(keyNames.contains("dbId"))
         * keyNames.remove("dbId"); // MetaClass superClass= metaClass.getSupertype(); ///List <String>
         * superKeyNames=((CCPNMetaClass)superClass).getKeyNames(); //Python: if clazz.keyNames and
         * clazz.superclass and not clazz.superclass.keyNames:
         * //if(keyNames!=null&&keyNames.size()>0)//&&((CCPNMetaClass)superClass).isCCPNMetaClassNull())
         * //??&&superClass's keyNames==null?? { //unique key from parent role
         * if(parentRole!=null&&!CCPNMetaUtils.isIgnoredRole(parentRole)
         * &&parentRole.getOtherRole().getHigh()!=1) { String
         * colName=CCPNMetaUtils.getRoleColName(parentRole); uniqueKeys.add(colName); } for(String
         * uniquekey:keyNames) { //unique key from attribute MetaAttribute
         * attribute=metaClass.getAttribute(uniquekey); if(attribute!=null)
         * if(!CCPNMetaUtils.isSuperAttribute(attribute)) {
         * if(!CCPNMetaUtils.isMultiAttribute(attribute.getType())) { uniqueKeys.add(uniquekey); continue; }
         * else { //when one of the element cardinality is upper than one (n..n with n>1). //do not generate
         * unique keys for these cases. uniqueKeys=new LinkedList<String>(); break; } } //unique key from
         * role Map<String, MetaRole> roles=((CCPNMetaClass)metaClass).getAllMetaRoles();
         * if(roles.containsKey(uniquekey)) if(roles.get(uniquekey).getHigh()==1)
         * uniqueKeys.add(CCPNMetaUtils.getRoleColName(roles.get(uniquekey))); else //when one of the element
         * cardinality is upper than one (n..n with n>1). //do not generate unique keys for these cases. {
         * uniqueKeys=new LinkedList<String>(); break; } } } if(uniqueKeys.size()>0)
         * dbUpdater.setUniqueKey(CCPNMetaUtils.getClassTableName(metaClass),uniqueKeys);
         */

    }

    /**
     * @param metaClass
     * @param keyNames
     * @return
     */
    private List<String> getUniqueKeys(final MetaClass metaClass, final List<String> keyNames) {
        final List<String> UniqueKeys = new LinkedList<String>();
        for (final String uk : keyNames) {
            String keyname = null;
            for (final String attributeName : ((MetaClassImpl) metaClass).getAllAttributes().keySet()) {
                if (attributeName.equalsIgnoreCase(uk)) {
                    keyname = uk;
                    break;
                }

            }
            if (keyname == null) {
                for (final String roleName : ((MetaClassImpl) metaClass).getDeclaredMetaRoles().keySet()) {
                    if (roleName.equalsIgnoreCase(uk)) {
                        keyname = uk + "id";
                        break;
                    }
                }
            }
            if (keyname != null) {
                UniqueKeys.add(keyname); //$NON-NLS-1$
            } else {
                throw new RuntimeException(metaClass + " has unknown key name:" + uk); //$NON-NLS-1$
            }
        }
        return UniqueKeys;
    }

    public MetaAttribute upgradeAttribute(final MetaAttribute attribute) throws SQLException {
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, " Update attribute: " //$NON-NLS-1$
            + attribute);

        // if it is a derived attribute, just ignore it, except serial
        if (attribute.isDerived()) {
            return attribute;
        }
        // if it is a attribute belong to its abstract class, then ingore it
        if (MetaUtils.isSuperAttribute(attribute)) {
            return attribute;
        }

        // if it is a simple attribute, check the column exists, or create it
        if (!MetaUtils.isMultiAttribute(attribute.getType()) && MetaUtils.getColumnName(attribute) != null) {
            final String attributeColumnName = MetaUtils.getColumnName(attribute);
            final String classTableName = MetaUtils.getClassTableName(attribute.getMetaClass());
            /*
             * TODO if (dbUpdater.isColumnEmpty(classTableName, attributeColumnName)) { // make sure the type
             * is right dbUpdater.dropColumn(classTableName, attributeColumnName); }
             */
            if (!dbUpdater.isColumnExist(classTableName, attributeColumnName)) {
                // if it is a required attribute, and the table is not empty,
                // throw a constraint exception
                if (attribute.isRequired() && !dbUpdater.isTableEmpty(classTableName)) {
                    throw new RuntimeException("Can not create " + attribute.getMetaClass() + "'s column  "
                        + attribute.getName() + " which is required but this table is not empty!");
                }

                this.createColumn(classTableName, attributeColumnName, MetaUtils.getSqlType(attribute));

            } else { // check the data type is same with column type in db
                final String columnType = dbUpdater.getColumnType(classTableName, attributeColumnName);
                if (columnType.equalsIgnoreCase("timestamp")) {
                    dbUpdater.sqlExecuteUpdate("ALTER TABLE " + classTableName + " ALTER "
                        + attributeColumnName + " TYPE timestamptz");
                } else if (!MetaUtils.columnTypeCheck(attribute, columnType.toUpperCase())) {
                    if (isColumnEmpty(classTableName, attributeColumnName)) {
                        // the column is unused - this doesn't need a special upgrader
                        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, "Changing empty column: "
                            + classTableName + "." + attributeColumnName);

                        this.dropColumn(classTableName, attributeColumnName);
                        this.createColumn(classTableName, attributeColumnName, columnType);
                    } else {
                        throw new RuntimeException(attribute.getMetaClass() + "'s attribute: "
                            + attribute.getName() + " column type should be changed from "
                            + columnType.toUpperCase() + " to " + MetaUtils.getSqlType(attribute));
                    }
                }
            }
            if (attribute.isRequired()) {
                try {
                    dbUpdater.setColumnNotNull(classTableName, attributeColumnName);
                } catch (final org.postgresql.util.PSQLException e) {
                    // the existed data may have null value
                    throw new RuntimeException(
                        attribute.getMetaClass().getName()
                            + "'s attribute "
                            + attribute.getName()
                            + " contains NULL value which is not allowed in new version!\n To fix this: remove null value in DB "
                            + classTableName + "." + attributeColumnName, e);
                }
            }
            // add uniqueness constraint if required and can PIMS-3624
            final Column dbColumn = ((MetaAttributeImpl) attribute).getDBColumn();
            if (dbColumn.unique() && this.containsNoDuplicates(classTableName, attributeColumnName)) {
                dbUpdater.setColumnUnique(classTableName, attributeColumnName);
            }
        }
        // if it is a multi-valued attributes, check the table exists, or create
        // it
        // the table contains: order_ int8 ,(classname+id)int8,attributname (on
        // its own type and length
        /*
         * eg: a molecule has many commonName, another table mole_molecule_commna is needed
         */
        else {
            // table name
            final String multiAttributeTableName =
                MetaUtils.getMultiAttributeTableName(attribute.getMetaClass(), attribute.getName());
            // related meta class table name
            final String classTableName = MetaUtils.getClassTableName(attribute.getMetaClass());
            // column related with attribute
            final String attributeColumnName = MetaUtils.getColumnName(attribute);
            // the primary column name which defined in JoinTable
            JoinTable joinTable =
                ((MetaAttributeImpl) attribute).getAnnotation(javax.persistence.JoinTable.class);
            final String primaryColumnName = joinTable.joinColumns()[0].name();

            /*
             * TODO if (dbUpdater.isTableEmpty(multiAttributeTableName)) { // easy way to make sure it is
             * right dbUpdater.dropTable(multiAttributeTableName); }
             */
            if (!this.dbUpdater.isTableExist(multiAttributeTableName)) {
                // if it is a required attribute, and the table is not empty,
                // throw a constraint exception
                if (attribute.isRequired() && !this.dbUpdater.isTableEmpty(classTableName)) {
                    throw new RuntimeException("Can not create " + attribute.getMetaClass() + "'s column"
                        + attribute.getName() + " which is required but this table is not empty!");
                }

                dbUpdater.createTable(multiAttributeTableName);
                dbUpdater.createColumn(multiAttributeTableName, primaryColumnName,
                    MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
                dbUpdater.createColumn(multiAttributeTableName, "order_", //$NON-NLS-1$
                    MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
                dbUpdater.createColumn(multiAttributeTableName, attributeColumnName,
                    MetaUtils.getSqlType(attribute));
                dbUpdater.setColumnNotNull(multiAttributeTableName, "order_"); //$NON-NLS-1$
                dbUpdater.setColumnNotNull(multiAttributeTableName, primaryColumnName);
                dbUpdater.setColumnNotNull(multiAttributeTableName, attributeColumnName);
                dbUpdater.setPrimaryKeysMtoM(multiAttributeTableName, primaryColumnName, "order_"); //$NON-NLS-1$
                dbUpdater.setForeignKeyOfMultiAttributeTable(multiAttributeTableName, classTableName,
                    primaryColumnName);
            } else {
                // check the data type is same with column type in db
                final String columnType =
                    dbUpdater.getColumnType(multiAttributeTableName, attributeColumnName);
                MetaUtils.columnTypeCheck(attribute, columnType.toUpperCase());
            }

        }

        return attribute;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#addAssociation(java.lang.String, java.lang.String, int,
     *      int, int, int, java.lang.String, java.lang.String)
     */
    public MetaRole upgradeAssociation(final MetaRole role) throws SQLException {
        DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, " Update role: " //$NON-NLS-1$
            + role);

        // Reference: Roles and Many-Many Roles in SqlSchema.py
        // if it is a required association, and the table is not empty, throw a
        // constraint exception
        /*
         * because required attribute can not be empty for exist data(row)
         */
        if (MetaUtils.isIgnoredRole(role)) {
            // continue;
        } else if (MetaUtils.isManyToOne_or_OneToOneRole(role)) {
            // This is a many-to-one link or a one-to-one link.
            createManyToOne_or_OneToOneRole(role);
        } else if (MetaUtils.isOneToManyRole(role)) {
            // This is a one-to-many link.
            // continue;
        } else {
            // This is a many-to-many link. Done after. Require new table.
            createManyToManyRole(role);
        }

        return role;
    }

    /**
     * Function that create column and foreign key constraint for many-one or one-one
     * 
     * @param role
     * @throws SQLException
     */
    public final void createManyToOne_or_OneToOneRole(final MetaRole role) throws SQLException {

        // check the table exist
        final String roleColName = MetaUtils.getRoleColName(role);
        final String classTableName = MetaUtils.getClassTableName(role.getOwnMetaClass());
        if (classTableName == null || roleColName == null) {
            //table/column is not created from this side
            return;
        }
        if (!dbUpdater.isTableExist(classTableName)) {
            DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, role.getOwnMetaClass().getMetaClassName()
                + "'s role " //$NON-NLS-1$
                + role.getRoleName() + "is related with a table does not exist:" //$NON-NLS-1$
                + classTableName);
            return;

        }
        boolean isColumnExist = dbUpdater.isColumnExist(classTableName, roleColName);
        /*if (isColumnExist && dbUpdater.isColumnEmpty(classTableName, roleColName)) {
            // drop and recreate the column, then we are sure it is right
            // this may save us from writing a specialUpgrader later
            DebugMessageHandler.LOG(MessageHandler.DEBUG, "Dropping empty column: " + classTableName + "."
                + roleColName);
            dbUpdater.dropColumn(classTableName, roleColName);
            isColumnExist = false;
        }*/
        // LATER if the column exists, check the type it is associated with is
        // the right one
        // to see is this column required
        boolean notNull = false;
        if (role.getLow() > 0) {

            if (role.isOneWay()) {
                notNull = true;
            } else if (role.getOwnMetaClass().getShortName().equalsIgnoreCase("AbstractStorage") //$NON-NLS-1$
                && role.getOtherMetaClass().getShortName().equalsIgnoreCase("Url")) {
                notNull = false;
            } else {
                notNull = true;
            }
        }

        // if it is required column and this table is not empty, throw error
        if (notNull && !dbUpdater.isTableEmpty(classTableName) && !isColumnExist) {
            throw new RuntimeException("Can not create " + role.getOwnMetaClass() + "'s column"
                + role.getRoleName() + " which is required but this table is not empty!");
        }

        if (!isColumnExist) {
            dbUpdater.createColumn(classTableName, roleColName, MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
        }
        // set constraint for this column when it is required
        if (notNull) {
            dbUpdater.setColumnNotNull(classTableName, roleColName);
        } else {
            dbUpdater.setColumnNullable(classTableName, roleColName, MetaUtils.getDBType("BIGINT"));
        }

        //check ordered
        if (!role.isOneWay()) {
            MetaRoleImpl otherRose = (MetaRoleImpl) role.getOtherRole();
            // If other role is ordered, create "order_" column in this table
            org.hibernate.annotations.IndexColumn orderIndexColum =
                (otherRose).getAnnotation(org.hibernate.annotations.IndexColumn.class);
            if (orderIndexColum != null) {
                //get order column name, eg: order_
                String orderColumnName = orderIndexColum.name();
                if (!dbUpdater.isColumnExist(classTableName, orderColumnName)) {
                    dbUpdater.createColumn(classTableName, orderColumnName, MetaUtils.getDBType("BIGINT"));
                }
            }
        }

        // create constraint
        if (createConstrains) {
            final String roleFKName = MetaUtils.getRoleFkName(role);
            final String roleTableName = MetaUtils.getRoleTableName(role);
            if (!dbUpdater.isTableExist(roleTableName)) {
                DebugMessageHandler.LOG(MessageHandler.Level.ERROR, role.getOwnMetaClass().getMetaClassName()
                    + "'s role " //$NON-NLS-1$
                    + role.getRoleName() + "is related with a table does not exist:" //$NON-NLS-1$
                    + roleTableName);

            } else {
                String onDelAction = "set null"; //$NON-NLS-1$
                ManyToOne annotation = ((MetaRoleImpl) role).getAnnotation(ManyToOne.class);
                if (null != annotation
                    && Arrays.asList(annotation.cascade()).contains(javax.persistence.CascadeType.REMOVE)) {
                    onDelAction = "cascade";
                }
                // # special case MemopsBaseClass/AccessObject: ON DELETE NO
                // ACTION
                if (role.getName().endsWith("access")) {
                    onDelAction = "no action"; //$NON-NLS-1$
                }
                // # many-to-one parent/child: ON DELETE CASCADE
                final MetaRole parentRole = ((MetaClassImpl) (role.getOwnMetaClass())).getParentRole();
                if (parentRole != null) {
                    if (role.getName().equals(parentRole.getName()) && role.getLow() == 1) {
                        onDelAction = "cascade"; //$NON-NLS-1$
                    }
                }
                final String foreignColumnName = MetaUtils.getKeyName(role.getOtherMetaClass());
                dbUpdater.setForeignKey(classTableName, roleFKName, roleTableName, foreignColumnName,
                    roleColName, onDelAction);
            }

        }

        /*
         * Create index for this role (many-one or one)
         */
        // the base name of FKName is same as base name of index
        final String roleIndexName = MetaUtils.getRoleFkName(role);
        // index will be created if not exist
        dbUpdater.setIndex(classTableName, roleIndexName, roleColName);

    }

    /**
     * Function that writes new table for many-to-many relationship
     * 
     * @param role
     */
    public void createManyToManyRole(final MetaRole role) throws SQLException {
        final String MtoMTableName = MetaUtils.getMToMTableName(role);
        if (MtoMTableName == null) {
            //table is not created from this side
            return;
            /*
             * TODO if (dbUpdater.isTableEmpty(MtoMTableName)) { // easy way to make sure it is right
             * dbUpdater.dropTable(MtoMTableName); }
             */
        }

        // create this table when it is not exist
        final boolean isTableExist = dbUpdater.isTableExist(MtoMTableName);
        final String thisRoleColName = MetaUtils.getThisRoleColName(role);
        final String OtherRoleColName = MetaUtils.getOtherRoleColName(role);
        if (!isTableExist) {
            dbUpdater.createTable(MtoMTableName);
            // create columns of this table
            dbUpdater.createColumn(MtoMTableName, thisRoleColName, MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
            dbUpdater.createColumn(MtoMTableName, OtherRoleColName, MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$
        }
        // create constraints for this table
        // primary key
        final String prePKeyName = dbUpdater.getPrimaryConstraintName(MtoMTableName);
        if (prePKeyName != null) {
            dbUpdater.dropConstraint(MtoMTableName, prePKeyName);
        }
        if (containsNoDuplicates(MtoMTableName, role)) {
            dbUpdater.setPrimaryKeysMtoM(MtoMTableName, thisRoleColName, OtherRoleColName);
        } /* TODO else            
                                                            alter table MtoMTableName rename to old;
                                                            create table MtoMTableName as select * from  old group by  1,2;
                                                            drop table old;
                                                                */

        String onDelete;
        if (role.getLow() == 1) {
            onDelete = "set default"; //$NON-NLS-1$
        } else {
            onDelete = "CASCADE"; //$NON-NLS-1$
        }

        // drop all constraints in table before adding them
        dbUpdater.dropConstraintInTable(MtoMTableName);
        if (createConstrains) {
            String roleFKName = MetaUtils.getOtherRoleFkName(MtoMTableName, role);
            String roleTableName = MetaUtils.getOtherRoleTableName(role);
            if (!dbUpdater.isTableExist(roleTableName)) {
                DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, role.getOwnMetaClass().getMetaClassName()
                    + "'s role " //$NON-NLS-1$
                    + role.getRoleName() + "is related with a table does not exist:" //$NON-NLS-1$
                    + roleTableName);

            } else {
                final String foreignColumnName = MetaUtils.getKeyName(role.getOwnMetaClass());
                dbUpdater.setForeignKey(MtoMTableName, roleFKName, roleTableName, foreignColumnName,
                    OtherRoleColName, onDelete);
            }
            // foreign key2
            roleFKName = MetaUtils.getThisRoleFkName(role);
            roleTableName = MetaUtils.getThisRoleTableName(role);
            if (!dbUpdater.isTableExist(roleTableName)) {
                DebugMessageHandler.LOG(MessageHandler.Level.DEBUG, role.getOwnMetaClass().getMetaClassName()
                    + "'s role " //$NON-NLS-1$
                    + role.getRoleName() + "is related with a table does not exist:" //$NON-NLS-1$
                    + roleTableName);

            } else {
                final String foreignColumnName = MetaUtils.getKeyName(role.getOtherMetaClass()); //TODO is thos getOwnMetaClass?
                dbUpdater.setForeignKey(MtoMTableName, roleFKName, roleTableName, foreignColumnName,
                    thisRoleColName, onDelete);
            }
        }
    }

    /**
     * ModelUpdateVersionImpl.containsNoDuplicates
     * 
     * @see http://cselnx4.dl.ac.uk:8080/jira/browse/PIMS-3594
     * 
     * @param role
     * 
     * @param mtoMTableName
     * @return
     * @throws SQLException
     */
    private boolean containsNoDuplicates(String mToMTableName, MetaRole role) throws SQLException {
        String select =
            "select * from (select " + (HibernateUtil.isOracleDB() ? "" : "*,") + " count(*) as c from "
                + mToMTableName + " group by " + MetaUtils.getThisRoleColName(role) + ", "
                + MetaUtils.getOtherRoleColName(role) + ")" + (HibernateUtil.isOracleDB() ? "" : " as")
                + " sub where c>1 ";
        final ResultSet rs = dbUpdater.sqlExecuteQuery(select);
        boolean ret = true;
        while (rs.next()) {
            ret = false;
            DebugMessageHandler.LOG(MessageHandler.Level.WARN, "Duplicate row in: " + mToMTableName + " "
                + rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3));
        }
        rs.close();
        return ret;
    }

    private boolean containsNoDuplicates(String table, String column) throws SQLException {
        String select =
            "select * from (select " + column + ", count(" + column + ") as c from " + table + " group by "
                + column + ") " + (HibernateUtil.isOracleDB() ? "" : "as ") + "sub where c>1 ";
        final ResultSet rs = dbUpdater.sqlExecuteQuery(select);
        boolean ret = true;
        while (rs.next()) {
            ret = false;
            DebugMessageHandler.LOG(MessageHandler.Level.WARN, "Duplicate value in: " + table + "." + column
                + ": " + rs.getObject(1));
        }
        rs.close();
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameConstraint(java.lang.String, java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void renameConstraint(final String tableName, final String oldConstraintName,
        final String newConstraintName, final String restSQL) throws SQLException {

        dbUpdater.renameConstraint(tableName, oldConstraintName, newConstraintName, restSQL);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameTable(java.lang.String, java.lang.String)
     */
    public void renameTable(final String oldTableName, final String newTableName) throws SQLException {

        dbUpdater.renameTable(oldTableName, newTableName);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.ModelUpdateVersion#renameColumn(java.lang.String,
     *      java.lang.String,java.lang.String)
     */
    public void renameColumn(final String tableName, final String oldColumnName, final String newColumnName)
        throws SQLException {

        dbUpdater.renameColumn(tableName, oldColumnName, newColumnName);

    }

    public void createColumn(final String tableName, final String columnName, final String type)
        throws SQLException {
        dbUpdater.createColumn(tableName, columnName, type);
    }

    public void dropColumn(final String tableName, final String columnName) throws SQLException {
        dbUpdater.dropColumn(tableName, columnName);

    }

    public void setColumnNotNull(final String tableName, final String columnName) throws SQLException {
        dbUpdater.setColumnNotNull(tableName, columnName);

    }

    public void dropTable(final String tableName) throws SQLException {
        dbUpdater.dropTable(tableName);

    }

    public void dropConstraint(final String tableName, final String constraintName) throws SQLException {
        dbUpdater.dropConstraint(tableName, constraintName);
    }

    /* *//**
     * Upgrade attribute (oldAttributeName) of a metaClass (newMetaRole's onw metaclass) to a metaclass
     * (newMetaRole's other metaclass) which contain old value of this attribute
     */
    /*
        public void upgrade_From_Attribute_to_class(MetaRole newMetaRole, String oldAttributeName)
            throws SQLException, ConstraintException {

            MetaClass ownMetaClass = newMetaRole.getOwnMetaClass();
            MetaClass targetMetaClass = newMetaRole.getOtherMetaClass();
            DebugMessageHandler
                .LOG(
                    MessageHandler.DEBUG,
                    "--upgrade the attribute " + oldAttributeName + " of " + ownMetaClass.getName() + " into a new class " + targetMetaClass.getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            // create column for ownMetaClass
            String ownTableName = MetaUtils.getClassTableName(ownMetaClass);
            String roleColName = MetaUtils.getRoleColName(newMetaRole);
            dbUpdater.createColumn(ownTableName, roleColName, MetaUtils.getDBType("BIGINT")); //$NON-NLS-1$

            // create table for otherMetaClass
            updateMetaClassWithoutRole(targetMetaClass);
            // create project role for targetMetaClass
            Map targetAllRoles = ((MetaClassImpl) targetMetaClass).getDeclaredMetaRoles();
            if (targetAllRoles.containsKey("project")) //$NON-NLS-1$
                createManyToOne_or_OneToOneRole((MetaRole) targetAllRoles.get("project")); //$NON-NLS-1$

            // get list of oldAttributeName from ownTable
            Set results = dbUpdater.findAll(ownTableName, oldAttributeName);

            // get related id for this list
            Map IDMap = new HashMap();
            for (Iterator iter = results.iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                HashMap attributes = new HashMap();
                attributes.put(oldAttributeName, name);
                try {
                    ModelObject TargetObject = this.create(DbName.class, attributes);
                    // why the following cast has error in runtime??
                    // Long ID=((MappedModelObject)TargetObject).getID();
                    Long ID = getIDFromHook(TargetObject.get_Hook());
                    IDMap.put(name, ID);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            // update the value in roleCol(link to the targetTable) of ownTable
            // using IDMap
            String PKName = MetaUtils.getSubClassId(ownMetaClass);
            dbUpdater.updateAll(ownTableName, PKName, roleColName, oldAttributeName, IDMap);

            // set not null
            if (newMetaRole.getLow() > 0)
                dbUpdater.setColumnNotNull(ownTableName, roleColName);

            // remove old column
            dbUpdater.dropColumn(ownTableName, MetaUtils.getColumnName(oldAttributeName));
        }*/

    public boolean isColumnEmpty(final String table, final String column) throws SQLException {
        return dbUpdater.isColumnEmpty(table, column);
    }

    public int getDBRevisionNumber() {
        try {
            return dbUpdater.getDBRevisionNumber();
        } catch (final Exception e) {
            DebugMessageHandler.LOG(MessageHandler.Level.FATAL, "Can not get revision Number from Database!"); //$NON-NLS-1$
            throw new RuntimeException(e);
        }
    }

    public static int getJavaRevisionNumber() {

        int jarRN = RevisionNumber.getRevision();

        return jarRN;
    }

    public void setDBRevisionNumber() throws SQLException {
        dbUpdater.setDBRevisionNumber();
    }

    public boolean isColumnExist(final String tableName, final String columnName) throws SQLException {
        return (dbUpdater.isColumnExist(tableName, columnName));
    }

    public boolean isRevisionNumberSame() {
        final int dbrn = getDBRevisionNumber();
        final int jarRN = getJavaRevisionNumber();
        System.out.println("Database version=" + dbrn + "; Jar version=" + jarRN); //$NON-NLS-1$ //$NON-NLS-2$

        if (dbrn == jarRN) {
            return true;
        } else if (jarRN > dbrn) {
            if (dbrn >= lastUpgradedVersion)// DB is the latest version which
            // can be upgrated
            {
                DebugMessageHandler
                    .LOG(
                        MessageHandler.Level.WARN,
                        "pims-model.jar(" + jarRN + ") is newer than database(" + dbrn + "). However, there is no upgrader available/necessary!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                return true;
            }
            DebugMessageHandler
                .LOG(
                    MessageHandler.Level.WARN,
                    "pims-model.jar(" + jarRN + ") is newer than database(" + dbrn + ") and this database should be upgraded to version " + lastUpgradedVersion); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return false;
        } else // CCPNRN<DBRN
        {
            DebugMessageHandler.LOG(MessageHandler.Level.WARN,
                "pims-model.jar(" + jarRN + ") is older than database(" + dbrn + ")!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return true;
        }

    }

    /**
     * @TODO this function should not be here
     * @param hook
     * @return
     */
    private Long getIDFromHook(final String hook) {
        int i;
        for (i = hook.length() - 1; i > 0; i--) {
            if (!Character.isDigit(hook.charAt(i))) {
                break;
            }
        }
        return new Long(hook.substring(i + 1, hook.length()));
    }

    public void setIndex(final String tableName, final String roleIndexName, final String colName)
        throws SQLException {
        dbUpdater.setIndex(tableName, roleIndexName, colName);
    }

    public void setUniqueKey(final String tableName, final List<String> keys) throws SQLException {

        dbUpdater.setUniqueKey(tableName, keys);
    }

    public void changeColumnLength(final String tableName, final String fieldName, final String type,
        final int length) throws SQLException {
        dbUpdater.changeColumnLength(tableName, fieldName, type, length);
    }

}
