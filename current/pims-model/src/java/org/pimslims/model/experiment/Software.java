package org.pimslims.model.experiment;

/**
 * Generated 05-Aug-2008 09:22:37 by Hibernate Tools 3.2.0.b9
 * 
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.0
 * 
 *           Copyright (c) 2007
 * 
 *           This library is free software; you can redistribute it and/or modify it under the terms of the
 *           GNU Lesser General Public License as published by the Free Software Foundation; either version
 *           2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_SOFTWARE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "VERSION" }) })
@org.pimslims.annotation.MetaClass(helpText = "Program used for some task or tasks.", keyNames = { "NAME",
    "VERSION" }, subclasses = {})
public class Software extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_VERSION = "version";

    public static final String PROP_VENDORNAME = "vendorName";

    public static final String PROP_VENDORADDRESS = "vendorAddress";

    public static final String PROP_VENDORWEBADDRESS = "vendorWebAddress";

    public static final String PROP_TASKS = "tasks";

    public static final String PROP_METHODS = "methods";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "name of Software.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "VERSION", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "Software version used.", constraints = { "contains_no_linebreak" })
    private String version;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VENDORNAME", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Name of vendor or software producer.")
    private String vendorName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VENDORADDRESS", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Address of vendor or software producer.")
    private String vendorAddress;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VENDORWEBADDRESS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Web address (home page) of vendor or software producer.")
    private String vendorWebAddress;

    /* ------------------------------------------------------------ */
    @org.hibernate.annotations.CollectionOfElements
    @JoinTable(name = "EXPE_SOFTWARE_TASKS", joinColumns = @JoinColumn(name = "SOFTWAREID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "TASK")
    @org.pimslims.annotation.Attribute(helpText = "Tasks carried out with the software.")
    private final List<String> tasks = new ArrayList<String>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "software")
    @org.pimslims.annotation.Role(helpText = "Methods carried out using the Software.", low = 0, high = -1, isChangeable = true, reverseRoleName = "software")
    private final Set<Method> methods = new HashSet<Method>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Software() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Software(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Software(final WritableVersion wVersion, final java.lang.String name,
        final java.lang.String version) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_VERSION, version);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Software(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: name
     */
    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: version
     */
    public String getVersion() {
        return (String) get_prop(PROP_VERSION);
    }

    public void setVersion(final String version) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VERSION, version);
    }

    /**
     * Property: vendorName
     */
    public String getVendorName() {
        return (String) get_prop(PROP_VENDORNAME);
    }

    public void setVendorName(final String vendorName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VENDORNAME, vendorName);
    }

    /**
     * Property: vendorAddress
     */
    public String getVendorAddress() {
        return (String) get_prop(PROP_VENDORADDRESS);
    }

    public void setVendorAddress(final String vendorAddress)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VENDORADDRESS, vendorAddress);
    }

    /**
     * Property: vendorWebAddress
     */
    public String getVendorWebAddress() {
        return (String) get_prop(PROP_VENDORWEBADDRESS);
    }

    public void setVendorWebAddress(final String vendorWebAddress)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VENDORWEBADDRESS, vendorWebAddress);
    }

    /**
     * Property: tasks
     */
    public List<String> getTasks() {
        return (List<String>) get_prop(PROP_TASKS);
    }

    public void setTasks(final List<String> tasks) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TASKS, tasks);
    }

    /**
     * Property: methods
     */
    public Set<Method> getMethods() {
        return (Set<Method>) get_prop(PROP_METHODS);
    }

    public void setMethods(final java.util.Collection<Method> methods)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_METHODS, methods);
    }

    public void addMethod(final Method method) throws org.pimslims.exception.ConstraintException {
        add(PROP_METHODS, method);
    }

    public void removeMethod(final Method method) throws org.pimslims.exception.ConstraintException {
        remove(PROP_METHODS, method);
    }

}
