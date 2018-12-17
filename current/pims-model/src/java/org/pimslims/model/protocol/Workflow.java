package org.pimslims.model.protocol;

/**
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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.target.ResearchObjective;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_WORKFLOW", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "Groups protocols into a plan of work.", keyNames = { "NAME" }, subclasses = {})
public class Workflow extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_PROTOCOLS = "protocols";

    public static final String PROP_PROJECTS = "projects";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the workflow.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "EXPE_WORKFLOW2PROTOCOLS", joinColumns = { @JoinColumn(name = "WORKFLOWID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "PROTOCOLID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The list of Protocol that belongs to a particular workflow.", low = 0, high = -1, isChangeable = true, reverseRoleName = "workflows")
    private Set<Protocol> protocols = new HashSet<Protocol>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "EXPE_WORKFLOW2RESEARCHOBJECTIVE", joinColumns = { @JoinColumn(name = "WORKFLOWID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "PROJECTID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The list of Protocol that belongs to a particular workflow.", low = 0, high = -1, isChangeable = true, reverseRoleName = "workflows")
    private Set<ResearchObjective> projects = new HashSet<ResearchObjective>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Workflow() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Workflow(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Workflow(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Workflow(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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

    public void setName(String shortName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, shortName);
    }

    /**
     * Property: targets
     */
    public Set<Protocol> getProtocols() {
        return (Set<Protocol>) get_prop(PROP_PROTOCOLS);
    }

    public void setProtocols(java.util.Collection<Protocol> protocols)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROTOCOLS, protocols);
    }

    public void addProtocol(org.pimslims.model.protocol.Protocol protocol)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_PROTOCOLS, protocol);
    }

    public void removeProtocol(org.pimslims.model.protocol.Protocol protocol)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_PROTOCOLS, protocol);
    }

    public Set<Project> getProjects() {
        return (Set<Project>) get_prop(PROP_PROJECTS);
    }

    public void setProjects(final java.util.Collection<Project> projects)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROJECTS, projects);
    }

    public void addProject(final Project project) throws org.pimslims.exception.ConstraintException {
        add(PROP_PROJECTS, (ModelObject) project);
    }

    public void removeProject(final Project project) throws org.pimslims.exception.ConstraintException {
        remove(PROP_PROJECTS, (ModelObject) project);
    }

}
