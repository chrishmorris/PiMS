package org.pimslims.model.target;

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

import java.util.Calendar;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_PROJECT")
@org.pimslims.annotation.MetaClass(orderBy = "shortName", helpText = "The project groups a list of targets together. It may be used to represent a different kind of grouping for targets than TargetGroup. It could groups targets that are not related by their properties.", keyNames = {}, subclasses = {}, parentRoleName = "rootProject")
public class Project extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_SHORTNAME = "shortName";

    public static final String PROP_COMPLETENAME = "completeName";

    public static final String PROP_STARTDATE = "startDate";

    public static final String PROP_PROJECT = "project";

    public static final String PROP_SUBPROJECTS = "subProjects";

    public static final String PROP_TARGETS = "targets";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "SHORTNAME", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The short name of the project used for display.", constraints = { "contains_no_linebreak" })
    private String shortName;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "COMPLETENAME", columnDefinition = "TEXT", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The complete name of the project.")
    private String completeName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STARTDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "Starting date of the project.")
    private Calendar startDate;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PROJECTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_project_project_inx")
    @org.pimslims.annotation.Role(helpText = "The project to which the sub projects belong.", low = 0, high = 1, isChangeable = true, reverseRoleName = "subProjects")
    private Project project;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @org.pimslims.annotation.Role(helpText = "List of sub project contained in a project.", low = 0, high = -1, isChangeable = true, reverseRoleName = "project")
    private final Set<Project> subProjects = new HashSet<Project>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "projects")
    @org.pimslims.annotation.Role(helpText = "The list of target that belongs to a particular project.", low = 0, high = -1, isChangeable = true, reverseRoleName = "projects")
    private final Set<Target> targets = new HashSet<Target>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Project() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Project(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Project(WritableVersion wVersion, java.lang.String shortName, java.lang.String completeName)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_SHORTNAME, shortName);
        attributes.put(PROP_COMPLETENAME, completeName);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Project(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: shortName
     */
    public String getShortName() {
        return (String) get_prop(PROP_SHORTNAME);
    }

    public void setShortName(String shortName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SHORTNAME, shortName);
    }

    /**
     * Property: completeName
     */
    public String getCompleteName() {
        return (String) get_prop(PROP_COMPLETENAME);
    }

    public void setCompleteName(String completeName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_COMPLETENAME, completeName);
    }

    /**
     * Property: startDate
     */
    public Calendar getStartDate() {
        return (Calendar) get_prop(PROP_STARTDATE);
    }

    public void setStartDate(Calendar startDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STARTDATE, startDate);
    }

    /**
     * Property: project
     */
    public Project getProject() {
        return (Project) get_prop(PROP_PROJECT);
    }

    public void setProject(Project project) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROJECT, project);
    }

    /**
     * Property: subProjects
     */
    public Set<Project> getSubProjects() {
        return (Set<Project>) get_prop(PROP_SUBPROJECTS);
    }

    public void setSubProjects(java.util.Collection<Project> subProjects)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBPROJECTS, subProjects);
    }

    public void addSubProject(Project subProject) throws org.pimslims.exception.ConstraintException {
        add(PROP_SUBPROJECTS, subProject);
    }

    public void removeSubProject(Project subProject) throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUBPROJECTS, subProject);
    }

    /**
     * Property: targets
     */
    public Set<Target> getTargets() {
        return (Set<Target>) get_prop(PROP_TARGETS);
    }

    public void setTargets(java.util.Collection<Target> targets)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TARGETS, targets);
    }

    public void addTarget(org.pimslims.model.target.Target target)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_TARGETS, target);
    }

    public void removeTarget(org.pimslims.model.target.Target target)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_TARGETS, target);
    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.shortName;
    }

}
