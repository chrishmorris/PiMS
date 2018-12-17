/*
 * Created on 22-Sep-2004 @author: Chris Morris
 */
package org.pimslims.access;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;

/**
 * This abstract class represents the strategy for access control.
 * 
 * @version 0.1
 */
public abstract class Access {

    /**
     * Owner name for reference data. This informationis readbale by all, and writable only by the
     * administrator.
     */
    public static final String REFERENCE = "reference";

    /**
     * The data model these access rules apply to
     */
    protected AbstractModel model;

    /**
     * User instance to represent the PIMS administrator
     */
    public static final String ADMINISTRATOR = "administrator";

    /**
     * 
     */
    public Access() {
        // no action currently required
    }

    /**
     * Note that this can't be set in the constructor, because the access strategy is passed to the
     * constructor of the model.
     * 
     * @param model The data model these access rules apply to
     */
    public void setModel(final AbstractModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model already set");
        }
        this.model = model;
    }

    /**
     * Check if an object is readable
     * 
     * @param object the object to read
     * @return true if the owner of the current version can view this object
     */
    public abstract boolean mayRead(final ModelObject object);

    /**
     * Check if an object is writable
     * 
     * @param object the object to update
     * @return true if the owner of the current version can change this object
     * @throws UnsupportedOperationException if the object is not supplied by a WritableVersion
     */
    public abstract boolean mayUpdate(final ModelObject object);

    /**
     * Check if an object can be deleted
     * 
     * @param object the object to delete
     * @return true if the owner of the current version can delete this object
     * @throws UnsupportedOperationException if the object is not supplied by a WritableVersion
     */
    public abstract boolean mayDelete(final ModelObject object);

    /**
     * Report whether the current user has creation rights with a specific Access object in a specific model
     * type.
     * 
     * @param version the transaction this creation would be part of
     * @param ownerName represents the intended ownership of the new object
     * @param metaClass the model type of the intended object
     * @param attributes the intended attributes for the new object, may be examined by a Condition
     * @return true if the current transaction can create object of this type
     * @throws UnsupportedOperationException if the object is not part of a WritableVersion
     */
    @Deprecated
    // seems not to be so useful
    public abstract boolean mayCreate(final ReadableVersion version, final String ownerName,
        final MetaClass metaClass, final java.util.Map attributes);

    /**
     * Get a list of owners which the current user can use to create an object of a specified type.
     * 
     * @param metaClass the model type of the object to be created
     * @return list of model objects representing data owners for which the creation is permitted
     */
    public abstract java.util.Set<ModelObject> getPermittedOwners(final MetaClass metaClass);

    /**
     * Access.mayUnlock
     * 
     * @param object LabBookEntry
     * @return
     */
    public abstract boolean mayUnlock(ModelObject object);

}
