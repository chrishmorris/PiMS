/*
 * Created on 04-Aug-2004 @author: Chris Morris
 */
package org.pimslims.upgrader;

import java.sql.SQLException;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;

/**
 * Provides methods to make permitted updates to the data model. The changes supported are ones that do not
 * invalidate existing databases.
 * 
 * Note on implementation: for thread safety, the implementation should get a clone of the current model and
 * modify that.
 * 
 * @version 0.1
 */
public interface ModelUpdateVersion
// extends WritableVersion
{

    /**
     * Update a model class and its attributes but without roles
     * 
     * @param metaClass the model class to be updated,
     * @throws ConstraintException
     */
    public void updateMetaClassAttributes(MetaClass metaClass) throws ConstraintException, SQLException;

    /**
     * Update a model class' roles
     * 
     * @param metaClass the model class to be updated
     * @throws ConstraintException
     */
    public void updateMetaRoleOfClass(MetaClass metaClass) throws ConstraintException, SQLException;

    public void commit() throws AbortedException, ConstraintException /*
                                                                               * throws SQLException
                                                                               */;

    public void abort() throws AbortedException /* throws SQLException */;

    /**
     * rename a table in db
     * 
     * @throws SQLException
     */
    public void renameTable(String oldTableName, String newTableName) throws SQLException;

    /**
     * rename a constraint in db
     * 
     * @param tableName eg: sam_crystalsample
     * @param oldConstraintName eg: sam_cryssa_memobacl_fk
     * @param newConstraintName eg: sam_cryssa_aaaaaaaa_fk
     * @param restSQL FOREIGN KEY (memopsbaseclassid)REFERENCES impl_memopsbaseclass (dbid) MATCH SIMPLE ON
     *            UPDATE NO ACTION ON DELETE CASCADE
     * @throws SQLException
     */
    public void renameConstraint(String tableName, String oldConstraintName, String newConstraintName,
        String restSQL) throws SQLException;

    /**
     * rename a column in db
     * 
     * @param tableName
     * @param oldColumnName
     * @param newColumnName
     * @throws SQLException
     */
    public void renameColumn(String tableName, String oldColumnName, String newColumnName)
        throws SQLException;

    /**
     * Indicates that the changes are complete.
     * 
     * @return the revised model
     */
    // AbstractModel commitModel()throws AbortedException, ConstraintException;
}
