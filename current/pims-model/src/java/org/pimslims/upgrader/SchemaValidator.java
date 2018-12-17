/**
 * pims-model org.pimslims.upgrader SchemaVerifier.java
 * 
 * @author bl67
 * @date 02/09 2008
 * 
 *       Protein Information Management System
 * @version: 2.3 Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.upgrader;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinTable;

import org.pimslims.dao.AbstractModel;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaAttributeImpl;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetaRoleImpl;

/**
 * SchemaVerifier
 * 
 */
public class SchemaValidator {

    static public boolean verifySchema(final ModelUpdateVersionImpl muv, final AbstractModel model)
        throws SQLException {
        Boolean isCorrect = true;
        final DatabaseUpdater dup = muv.getDbUpdater();
        //TODO use visit(ModelVisitor)
        final Collection<String> classNames = model.getClassNames();
        for (final String metaClassName : classNames) {
            final MetaClassImpl metaClass = (MetaClassImpl) model.getMetaClass(metaClassName);

            //verify table name
            final String tableName = metaClass.getDBTableName();
            if (!dup.isTableExist(tableName)) {
                System.err.println("Can not find table: " + tableName + " for " + metaClassName);
                isCorrect = false;
                continue;
            }

            //verify column name of attribute
            final Map<String, MetaAttribute> attributes = metaClass.getDeclaredMetaAttributes();
            for (final MetaAttribute attribute : attributes.values()) {
                final JoinTable jointable = ((MetaAttributeImpl) attribute).getDBJoinTable();
                if (jointable != null) {
                    final String jointTableName = jointable.name();
                    if (!dup.isTableExist(jointTableName)) {
                        System.err.println("Can not find join table " + jointTableName + " for " + attribute);
                        isCorrect = false;
                    }
                }
                final Column column = ((MetaAttributeImpl) attribute).getDBColumn();
                if (column != null) {
                    final String columnName = column.name();
                    String tableNameForColumn = tableName;
                    if (jointable != null) {
                        tableNameForColumn = jointable.name();
                    }
                    if (!dup.isColumnExist(tableNameForColumn, columnName)) {
                        System.err.println("Can not find column " + columnName + " in table "
                            + tableNameForColumn + " for " + attribute);
                        isCorrect = false;
                    }
                }
            }

            final Map<String, MetaRole> roles = metaClass.getDeclaredMetaRoles();
            for (final MetaRole role : roles.values()) {

                //verfiy column name of roles' join table
                final JoinTable jointable = ((MetaRoleImpl) role).getDBJoinTable();
                if (jointable != null) {
                    final String jointTableName = jointable.name();
                    if (!dup.isTableExist(jointTableName)) {
                        System.err.println("Can not find join table " + jointTableName + " for " + role);
                        isCorrect = false;
                    }
                }
            }

        }
        return isCorrect;

    }

}
