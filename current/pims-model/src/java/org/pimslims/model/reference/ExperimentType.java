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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.Protocol;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_EXPERIMENTTYPE", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "The type of experiment.", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class ExperimentType extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_WORKFLOWITEMS = "workflowItems";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The unique name.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "experimentType")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "experimentType")
    private Set<WorkflowItem> workflowItems = new HashSet<WorkflowItem>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected ExperimentType() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected ExperimentType(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public ExperimentType(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public ExperimentType(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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

    public void setName(String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    /**
     * Property: workflowItems
     */
    public Set<WorkflowItem> getWorkflowItems() {
        return (Set<WorkflowItem>) get_prop(PROP_WORKFLOWITEMS);
    }

    public void setWorkflowItems(java.util.Collection<WorkflowItem> workflowItems)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_WORKFLOWITEMS, workflowItems);
    }

    public void addWorkflowItem(WorkflowItem workflowItem) throws org.pimslims.exception.ConstraintException {
        add(PROP_WORKFLOWITEMS, workflowItem);
    }

    public void removeWorkflowItem(WorkflowItem workflowItem)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_WORKFLOWITEMS, workflowItem);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Property: experiments
     */
    public Collection<Experiment> getExperiments() {
        return this.get_Version().findAll(Experiment.class, Experiment.PROP_EXPERIMENTTYPE, this);
    }

    /**
     * Property: protocols
     */
    public Collection<Protocol> getProtocols() {
        return this.get_Version().findAll(Protocol.class, Protocol.PROP_EXPERIMENTTYPE, this);
    }

}
