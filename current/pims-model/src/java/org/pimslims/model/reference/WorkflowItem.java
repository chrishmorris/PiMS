package org.pimslims.model.reference;

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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_WORKFLOWITEM")
@org.pimslims.annotation.MetaClass(helpText = "N/A", keyNames = {}, subclasses = {})
public class WorkflowItem extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_EXPERIMENTTYPE = "experimentType";

    public static final String PROP_STATUS = "status";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EXPERIMENTTYPEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "ref_workflowitem_et_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "workflowItems")
    private ExperimentType experimentType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "STATUSID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "ref_workflowitem_status_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "workflowItems")
    private TargetStatus status;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected WorkflowItem() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected WorkflowItem(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public WorkflowItem(WritableVersion wVersion, org.pimslims.model.reference.ExperimentType experimentType)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_EXPERIMENTTYPE, experimentType);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public WorkflowItem(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: experimentType
     */
    public ExperimentType getExperimentType() {
        return (ExperimentType) get_prop(PROP_EXPERIMENTTYPE);
    }

    public void setExperimentType(ExperimentType experimentType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXPERIMENTTYPE, experimentType);
    }

    /**
     * Property: status
     */
    public TargetStatus getStatus() {
        return (TargetStatus) get_prop(PROP_STATUS);
    }

    public void setStatus(TargetStatus status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.get_Hook();
    }

}
