/*
 * Created on 15-Oct-2004
 */
package org.pimslims.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.FlushMode;
import org.pimslims.access.PIMSAccess;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.DuplicateKeyConstraintException;
import org.pimslims.metamodel.IDGenerator;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.util.File;
import org.pimslims.util.FileImpl;

/**
 * @author bl67
 * 
 */

public class WritableVersionImpl extends ReadableVersionImpl implements WritableVersion {

    /**
     * @param username the user this version is for
     * @param model the model this is a version of
     * @param date the time data was last committed
     * @param session
     */
    public WritableVersionImpl(final String username, final ModelImpl model, final java.sql.Timestamp date,
        EntityManager entityManager) {

        super(username, model, date, entityManager);
        this.model = model;
        forceDataOwnerCheck = model.forceDataOwnerCheck;
        // set this session as writable
        this.entityManager.setFlushMode(this.pimsFlushMode.getJPAFlushMode());

    }

    /**
     * {@inheritDoc}
     */
    public <T extends ModelObject> void delete(final T object) throws AccessException, ConstraintException {
        object.delete();
    }

    /**
     * {@inheritDoc}
     */
    public <T extends ModelObject> void delete(final Collection<T> objects) throws AccessException,
        ConstraintException {
        assert !isCompleted() : "this has already been committed or aborted";
        for (final Object element : objects) {
            delete((ModelObject) element);

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws AbortedException, ConstraintException {
        assert !isCompleted() : "this has already been committed or aborted";
        // LATER if (!verify()) {throw new ConstraintException(null, null,
        // null);}
        super.commit();
        // set current time as last update time
        this.model.setLastUpdateTime(new java.sql.Timestamp(new Date().getTime()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void abort() {
        super.abort();
    }

    /**
     * Search criteria to find create permissions
     */
    private static final Map<String, Object> CREATE_CRITERIA = new java.util.HashMap<String, Object>();
    static {
        CREATE_CRITERIA.put("opType", PIMSAccess.CREATE);
    }

    /**
     * {@inheritDoc}
     */
    public <T extends ModelObject> T create(final Class<T> javaClass, final Map<String, Object> attributes)
        throws AccessException, ConstraintException {
        assert !isCompleted() : "this has already been committed or aborted";
        //final MetaClass metaClass = model.getMetaClass(javaClass.getName());
        if (attributes.keySet().contains(LabBookEntry.PROP_ACCESS)) {
            setDefaultOwner(((LabNotebook) attributes.get(LabBookEntry.PROP_ACCESS)));
        }

        //else
        try {
            return this.<T> create(this.accessController.getCurrentDefaultOwnerName(javaClass), javaClass,
                attributes);
        } catch (AccessException e) {
            //e.printStackTrace();
            throw new AccessException("Can not create " + javaClass + " : " + e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    public <T extends ModelObject> T create(final String owner, final Class<T> javaClass,
        final Map<String, Object> attributes) throws AccessException, ConstraintException {
        assert !isCompleted() : "this has already been committed or aborted";
        if (LabBookEntry.class.isAssignableFrom(javaClass)) {
            this.setDefaultOwner(owner);
        }

        // no specific factory method has been written */
        // create a new MetaClass to avoid ConcurrentModificationException
        final MetaClass metaClass = MetaClassImpl.getMetaClass(javaClass);
        return metaClass.<T> create(this, owner, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        if (!isCompleted()) {
            abort();
        }
        super.finalize();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.WritableVersion#createFile(byte[], java.lang.String, java.lang.String)
     */
    public File createFile(final byte[] data, final String name, final LabBookEntry parent)
        throws AccessException, ConstraintException, IOException {
        return FileImpl.createFile(this, data, name, parent);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.metamodel.WritableVersion#createFile(java.io.InputStream, java.lang.String,
     *      java.lang.String)
     */
    public File createFile(final InputStream inputStream, final String name, final LabBookEntry parent)
        throws AccessException, ConstraintException, IOException {
        return FileImpl.createFile(this, inputStream, name, parent);
    }

    /**
     * insert or update object, a select sql will be done before action
     * 
     * WritableVersionImpl.save
     * 
     * @see org.pimslims.dao.WritableVersion#save(org.pimslims.metamodel.ModelObject)
     */
    public <T extends ModelObject> T save(final T object) {
        assert !isCompleted();
        // TODO should the next line be .merge(object)? See
        // http://saloon.javaranch.com/cgi-bin/ubb/ultimatebb.cgi?ubb=get_topic&f=78&t=001363
        getSession().saveOrUpdate(object);
        // was ((AbstractModelObject) object).setReflectiveModelObject(this);
        return object;
    }

    /**
     * insert new object without select sql
     * 
     * WritableVersionImpl.saveNew
     * 
     * @param <T>
     * @param object
     * @return
     */
    public <T extends ModelObject> T saveNew(final T object) {
        assert !isCompleted();
        getSession().save(object);
        // was ((AbstractModelObject) object).setReflectiveModelObject(this);
        return object;
    }

    public Long getUniqueID() throws ConstraintException, AccessException {
        final Long id = IDGenerator.getNextID(this);
        return id;
    }

    public void flush() throws ConstraintException {
        flush(null);
    }

    /**
     * @param abstractModelObject
     * @throws ConstraintException
     */
    public void flush(final ModelObject modelObject) throws ConstraintException {
        try {
            this.entityManager.flush();
        } /* catch (final org.hibernate.exception.ConstraintViolationException cve) {
            Throwable cause = cve;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            if (cause instanceof java.sql.BatchUpdateException) {
                cause = ((java.sql.BatchUpdateException) cause).getNextException();
                throw new RuntimeException(cause);
            }
            if (cause.getMessage().toLowerCase().contains("unique")) {
                throw getDuplicateException(modelObject, cause);
            }
            // else
            throw new org.pimslims.exception.ConstraintException("Data model constraint violated: "
                + cve.getMessage(), cve);
          }  */catch (final Exception ex) {
            throw processFlushException(modelObject, ex);
        }

    }

    /**
     * WritableVersionImpl.processFlushException
     * 
     * @param modelObject context, if known
     * @param ex
     * @return DuplicateKeyConstraintException or other ConstraintException
     * @throws RuntimeException, if the error is not a constraint violation
     */
    public static ConstraintException processFlushException(final ModelObject modelObject, final Exception ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof java.sql.BatchUpdateException) {
            cause = ((java.sql.BatchUpdateException) cause).getNextException();
            if (cause instanceof org.postgresql.util.PSQLException
                && cause.getMessage().contains(" does not exist")) {
                throw new RuntimeException("Please upgrade your PiMS database ", cause);
            }
            throw new RuntimeException(cause);
        } else if (null != cause.getMessage() && cause.getMessage().toLowerCase().contains("unique")) {
            return getDuplicateException(modelObject, cause);
        } else if (cause instanceof SQLException) {
            if (cause.getMessage().toLowerCase().contains("syntax")
                || cause.getMessage().toLowerCase().contains("foreign key")
                || cause.getMessage().toLowerCase().contains("must appear")) {
                throw new RuntimeException(cause);
            }
            throw new RuntimeException("Database error, please run upgrader", cause);
        } else if (ex instanceof org.hibernate.exception.ConstraintViolationException) {
            return new org.pimslims.exception.ConstraintException("Data model constraint violated: "
                + cause.getMessage(), cause);
        }
        // else
        throw new RuntimeException(cause);
    }

    /**
     * @param modelObject
     * @param cause
     * @return
     */
    private static DuplicateKeyConstraintException getDuplicateException(final ModelObject modelObject,
        final Throwable cause) {
        List<String> attributeNames = null;
        if (modelObject != null) {
            attributeNames = getAttributesFromException(modelObject, cause);
        }
        if (attributeNames == null || attributeNames.size() == 0) {
            return new DuplicateKeyConstraintException(modelObject, "duplicate key is unknown", "UNKNOWN",
                cause.getLocalizedMessage(), cause);
        }
        String attributReport;
        if (attributeNames != null && attributeNames.size() > 0) {
            attributReport = attributeNames.toString();
        } else {
            attributReport = null;
        }

        Object value = null;
        if (modelObject != null) {
            final List<Object> values = new LinkedList<Object>();
            for (final String attributeName : attributeNames) {
                values.add(modelObject.get_Value(attributeName));
            }
            value = values.toString();
        }
        final String msg = cause.getMessage();
        return new org.pimslims.exception.DuplicateKeyConstraintException(modelObject, attributReport, value,
            msg, cause);
    }

    /**
     * @param modelObject
     * @param cause eg: duplicate key violates unique constraint
     *            "memops_accesscontrol_user__projectid_name__must_be_unique"
     * @return
     */
    private static List<String> getAttributesFromException(final ModelObject modelObject,
        final Throwable cause) {
        assert cause.getMessage().contains("unique");
        String message = cause.getMessage().replace("_must_be_unique", "");
        message = message.replace("_projectid", "");
        if (!message.contains('"' + "")) {
            return null;
        }
        final String uniqueName =
            message.subSequence(message.indexOf('"') + 1, message.lastIndexOf('"')).toString();
        final String[] uniqueNames = uniqueName.split("_");
        final List<String> uniqueNameList = Arrays.asList(uniqueNames);

        final List<String> attributeNameList = new LinkedList<String>();
        final MetaClass metaclass = modelObject.get_MetaClass();
        for (String name : uniqueNameList) {
            if (name.endsWith("id")) {
                name = name.substring(0, name.lastIndexOf("id"));
            }
            if (metaclass.getProperty(name) != null) {
                attributeNameList.add(name);
            }
        }
        return attributeNameList;

    }

    //was private User defaultCreator = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.Set<ModelObject> getPermittedOwners(final MetaClass metaClass) {
        return model.getAccess().getPermittedOwners(metaClass);
    }

    /**
     * {@inheritDoc}
     */
    public void setDefaultOwner(final String owner) {
        this.accessController.setDefaultOwner(owner);
    }

    public void setDefaultOwner(final LabNotebook owner) {
        this.accessController.setDefaultOwner(owner);
    }

    /**
     * @throws AccessException
     * @throws AccessException
     * @see org.pimslims.dao.ReadableVersion#getCurrentUserAccess()
     */
    public LabNotebook getCurrentDefaultOwner() {
        return this.accessController.getCurrentDefaultOwner();

    }

    boolean forceDataOwnerCheck;

    /**
     * setForceDataOwnerCheck
     * 
     * @param b
     */
    public void setForceDataOwnerCheck(boolean b) {
        forceDataOwnerCheck = b;

    }

    public boolean getForceDataOwnerCheck() {
        return forceDataOwnerCheck;

    }

    /**
     * WritableVersion.clear
     * 
     * @see org.pimslims.dao.WritableVersion#clear()
     */
    @Override
    public void clear() {
        this.getSession().clear();

    }

    /**
     * WritableVersion.setFlushModeCommit
     * 
     * @see org.pimslims.dao.WritableVersion#setFlushModeCommit()
     */
    @Override
    public void setFlushModeCommit() {
        this.getSession().setFlushMode(FlushMode.COMMIT);
    }

}
