/*
 * Created on 26-Jul-2004 @author: Chris Morris
 */
package org.pimslims.dao;

import java.util.Collection;
import java.util.Map;

import org.pimslims.access.Access;
import org.pimslims.constraint.Constraint;
import org.pimslims.metamodel.Invariant;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.upgrader.ModelUpdateVersion;

/**
 * Represents a data model, proving metadata and implementation. All application access to the values in a
 * data model begins with getting a version from a model. An application should only have one instance of a
 * data model, unless it is e.g. copying from one repository to another.
 * 
 */
public abstract class AbstractModel {

    /**
     * Comment for <code>SUPERUSER</code>
     * 
     * Name of a special user with the right to update the User table and other access control tables.
     * Required to bootstrap the access control system.
     */
    public static final String SUPERUSER = Access.ADMINISTRATOR;

    /**
     * Comment for <code>metaClasses</code>
     */
    protected java.util.Map metaClasses = new java.util.HashMap(); // name =>

    // MetaClass

    /**
     * @return a set of strings, the names of the model types
     * 
     *         Often better to use the method visit(ModelVisitor)
     */
    public abstract Collection<String> getClassNames();

    public void visit(ModelVisitor visitor) {
        final Collection<String> classNames = this.getClassNames();
        for (final String metaClassName : classNames) {
            final MetaClassImpl metaClass = (MetaClassImpl) this.getMetaClass(metaClassName);
            visitor.openClass(metaClass);
            final Map<String, MetaAttribute> attributes = metaClass.getDeclaredMetaAttributes();
            for (final MetaAttribute attribute : attributes.values()) {
                visitor.visitDeclaredAttribute(attribute);
            }
            Map<String, MetaRole> roles = metaClass.getDeclaredMetaRoles();
            for (MetaRole role : roles.values()) {
                visitor.visitDeclaredRole(role);
            }
        }
    }

    /**
     * @param metaClassName - name of model type that is wanted
     * @return MetaClass object for the model type
     */
    public abstract MetaClass getMetaClass(final String metaClassName);

    /**
     * Starts a read transaction, for viewing the recorded information. Implementations should be
     * transactionally correct, i.e. all the values read should be from the same version of the database, just
     * after a specific write transaction has committed.
     * 
     * In an initial implementation, it will be acceptable if this blocks until any write transaction is
     * complete.
     * 
     * @param username - the name of the user the operation is for.
     * @return a readable version of the model, including the results of all complete wrire transactions.
     */
    public abstract ReadableVersion getReadableVersion(final String username);

    /**
     * Starts a long read operation. An initial implementation may be the same as getReadableVersion(). Later
     * an implementation will be needed that guarantees not to block writes. Implementations need not be
     * transactionally correct.
     * 
     * @param username - the name of the user the operation is for
     * @return a readable version of the model
     */
    @Deprecated
    // not used
    public ReadableVersion getReportVersion(final String username) {
        return getReadableVersion(username);
    }

    /**
     * Starts a read operation of a previous version of the model.
     * 
     * @param username the name of the user the operation is for
     * @param date - the version returned will show the data as it was then
     * @return a readable version of the model
     */
    public ReadableVersion getReadableVersion(final String username, final java.util.Date date) {
        throw new UnsupportedOperationException(); // later
    }

    /**
     * Starts a read operation of a previous version of the model.
     * 
     * @param username the user the request is for
     * @param hook the identifier of a persistent object
     * @return the version of the model in which the object was last updated
     */
    @Deprecated
    // not used
    public ReadableVersion getRecentVersion(final String username, final String hook) {
        throw new java.lang.UnsupportedOperationException(); // later
    }

    /**
     * Starts a write transaction, for updating the recorded information. Implementations should be
     * transactionally correct. i.e. changes made should be visible to other transactions only after this
     * transaction has been committed, and the database shold be exactly as if all write transactions were
     * performed in some specific order, with no overlaps.
     * 
     * It will probably be acceptable if only one write transaction can be open at a time. However, this
     * implementation is also designed to support optimistic algorithms.
     * 
     * @param username - the name of the user the operation is for
     * @return a writeable version of the model
     */
    public abstract WritableVersion getWritableVersion(final String username);

    /**
     * AbstractModel.getTestVersion
     * 
     * @return a WritableVersion that cannot be committed.
     */
    public abstract WritableVersion getTestVersion();

    /**
     * Starts a model update transaction.
     * 
     * This will be used occasionally, when an upgraded version of the model is shipped. Usually new code will
     * be loaded after the upgrade. Read and write transactions should be rejected if an update transaction is
     * planned.
     * 
     * This method should only be called on behalf of the super user. It is the caller's responsibility to
     * ensure this.
     * 
     * @return a version offering metadata update facilities
     */
    public abstract ModelUpdateVersion getModelUpdateVersion();

    /**
     * Finds the data base version for an instance of a model object
     * 
     * @param object in-memory representation of a model object
     * @return transaction the object is part of
     */
    public abstract ReadableVersion getCurrentVersion(final ModelObject object);

    /**
     * Create an association between two model types.
     * 
     * The MetaClass instances representing the types are modified to add the association. The database or
     * other backing store is queried to see if the association exists If not a new column will be added.
     * 
     * An example is for the association: CompositeElement <-2---------*-> CompositeInteraction the call
     * should be: new Association("CompositeElement",CompositeElement,2,2,
     * 0,-1,CompositeInteraction,"CompositeInteraction" );
     * 
     * @param role1 the name of the role the first type plays
     * @param metaClass1 first participating type
     * @param low1 minimum number of type1 for a type2
     * @param high1 maximum number of type1 for a type2 (-1 or any)
     * @param low2 minimum number of type2 for a type1
     * @param high2 maximum number of type2 for a type1 (-1 or any)
     * @param metaClass2 second participating type
     * @param role2 the name of the role the second type plays
     */
    @Deprecated
    // believed unused
    protected abstract void addAssociation(final String role1, final MetaClass metaClass1, final int low1,
        final int high1, final int low2, final int high2, final MetaClass metaClass2, final String role2);

    /**
     * Adds a type to the model.
     * 
     * The database or other backing store is queried to see if the type exists. If not a new table will be
     * created.
     * 
     * 
     * @param javaClass java.lang.Class of instances of the new type
     * @param name name of new type in model
     * @param supertype the model type the new type extends
     * @param invariant consistency rule for objects of this type
     * @return a MetaClass instance representing the new type
     */
    @Deprecated
    // believed unused
    protected abstract MetaClass addMetaClass(final Class javaClass, final String name,
        final MetaClass supertype, final Invariant invariant);

    /**
     * Adds a new field to a model type.
     * 
     * The MetaClass instance representing the type is modified to add the attribute. The database or other
     * backing store is queried to see if the attribute exists, and if not it is altered as necessary.
     * 
     * @param metaClass to add the attribute to
     * @param attributeName name of new attribute
     * @param javaClass java.lang.Class of values for this attribute
     * @param constraint rule limiting the allowed values
     * @return a representation of the new attribute
     */
    @Deprecated
    // believed unused
    protected abstract MetaAttribute addAttribute(final MetaClass metaClass, final String attributeName,
        final Class javaClass, final Constraint constraint);

    // basic types, required for any data model
    // it is permissible for a particular model to extend these types

    /**
     * @return Root model type.
     */
    public abstract MetaClass getRootMetaClass();

    /**
     * @return implementation of access control
     */
    public Access getAccess() {
        return access;
    }

    protected Access access = null;

    /**
     * @param access the access rights strategy in use
     */
    protected AbstractModel(final Access access) {

        access.setModel(this);
        this.access = access;
    }

    /**
     * provided only to allow serializable implemention
     */
    protected AbstractModel() {
        // no action needed
    }

    /**
     * Copy constructor.
     * 
     * Does a deep clone. Helps to update the model in a threadsafe way.
     * 
     * @param old Model to copy
     * 
     *            protected AbstractModel(final AbstractModel old) { java.util.List ts = new
     *            java.util.ArrayList(); // types to add ts.add( old.metaClasses.get("ModelObject") ); //
     *            start at root java.util.List roles = new java.util.ArrayList(); // Associations to add
     *            java.util.Map map = new java.util.HashMap(); // translation old type => new type // make the
     *            type instances while ( !ts.isEmpty() ) { MetaClass t = (MetaClass)ts.remove(0); // type to
     *            process ts.add(t.getSubtypes()); // more types to add to new model // process roles
     *            java.util.Map r = t.getMetaRoles(); for (java.util.Iterator i=r.values().iterator();
     *            i.hasNext();) { MetaRole role = (MetaRole)i.next(); if (roles.contains(role.getOtherRole()))
     *            { // this association is already known } else { roles.add(role); } }
     * 
     * 
     *            MetaClass newt = addMetaClass( ModelObject.class, t.getMetaClassName(), t.getSupertype(),
     *            t.getInvariant() ); map.put(t, newt); // record translation } // now make the associations
     *            for (java.util.Iterator i = roles.iterator(); i.hasNext();) { MetaRole role =
     *            (MetaRole)i.next(); MetaRole other = role.getOtherRole(); MetaClass newt1 =
     *            (MetaClass)map.get(role.getOwnMetaClass()); MetaClass newt2 =
     *            (MetaClass)map.get(other.getOwnMetaClass()); addAssociation( newt1.getMetaClassName(),
     *            newt1, role.getLow(),role.getHigh(), other.getLow(),other.getHigh(), newt2,
     *            newt2.getMetaClassName() ); } }
     */

    /**
     * Close the connection to the database or other repository
     */
    public void disconnect() {
        // subclasses may need to implement this
    }

    boolean forceDataOwnerCheck = false;

    /**
     * AbstractModel.setForceDataOwnerCheck
     * 
     * @param b
     */
    public void setForceDataOwnerCheck(boolean b) {
        forceDataOwnerCheck = b;

    }

    /**
     * AbstractModel.isAllSessionsClosed
     * 
     * @return
     */
    public abstract boolean isAllSessionsClosed();

    /**
     * AbstractModel.getExportRules
     * 
     * @return a string representation of RDF rules mapping PiMS data to public ontologies
     */
    public abstract String getExportRules();

    /**
     * AbstractModel.getExportRules
     * 
     * @return a string representation of RDF rules public ontologies to the PiMS schema
     */
    public abstract String getImportRules();

}
