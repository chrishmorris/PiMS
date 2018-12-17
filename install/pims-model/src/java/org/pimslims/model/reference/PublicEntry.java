/**
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.3
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
package org.pimslims.model.reference;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.core.LabNotebook;

@Entity
//TODO consider @Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "REF_PUBLICENTRY", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Base class for all kind of reference data.", keyNames = {}, subclasses = {
    org.pimslims.model.reference.Database.class, org.pimslims.model.reference.ExperimentType.class,
    org.pimslims.model.reference.AbstractHolderType.class,
    org.pimslims.model.reference.HolderTypeSource.class, org.pimslims.model.reference.HazardPhrase.class,
    org.pimslims.model.reference.InstrumentType.class, org.pimslims.model.reference.TargetStatus.class,
    org.pimslims.model.reference.ComponentCategory.class, org.pimslims.model.reference.HolderCategory.class,
    org.pimslims.model.reference.WorkflowItem.class, org.pimslims.model.reference.SampleCategory.class,
    org.pimslims.model.reference.ImageType.class, org.pimslims.model.reference.Organism.class })
public abstract class PublicEntry extends org.pimslims.metamodel.AbstractModelObject implements
    java.lang.Comparable, java.io.Serializable, org.pimslims.metamodel.PublicAccess {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DBID = "dbId";

    public static final String PROP_DETAILS = "details";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Id
    @Basic(optional = false)
    @Column(name = "DBID", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "")
    private Long dbId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details field for comments.")
    private String details;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected PublicEntry() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected PublicEntry(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: dbId
     */
    @Override
    public Long getDbId() {
        return this.dbId;
    }

    @Override
    protected void setDbId(final Long dbId) {
        this.dbId = dbId;
    }

    /**
     * Property: details
     */
    public String getDetails() {
        return (String) get_prop(PROP_DETAILS);
    }

    public void setDetails(final String details) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DETAILS, details);
    }

    @Override
    public LabNotebook getAccess() {
        return null;
    }
}
