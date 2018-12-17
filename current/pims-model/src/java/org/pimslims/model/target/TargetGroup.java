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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_TARGETGROUP", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "General grouping system for Target. It can be used to represent a protein family.", keyNames = { "NAME" }, subclasses = {}, parentRoleName = "project")
public class TargetGroup extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_GROUPINGTYPE = "groupingType";

    public static final String PROP_TARGETGROUP = "targetGroup";

    public static final String PROP_SUBTARGETGROUPS = "subTargetGroups";

    public static final String PROP_TARGETS = "targets";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the target group.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GROUPINGTYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "it is the name of the grouping type, it can be a protein family group a gene family,... The difference between these is then entirely up to the understanding of the user.", constraints = { "contains_no_linebreak" })
    private String groupingType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "TARGETGROUPID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "targ_targetgroup_tg_inx")
    @org.pimslims.annotation.Role(helpText = "The target group to which the sub target groups belong.", low = 0, high = 1, isChangeable = true, reverseRoleName = "subTargetGroups")
    private TargetGroup targetGroup;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "targetGroup")
    @org.pimslims.annotation.Role(helpText = "List of sub target group contained in a target group.", low = 0, high = -1, isChangeable = true, reverseRoleName = "targetGroup")
    private Set<TargetGroup> subTargetGroups = new HashSet<TargetGroup>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "TARG_TARGGR2TARGETS", joinColumns = { @JoinColumn(name = "TARGETGROUPID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "TARGETID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "The list of target that belongs to a particular target group.", low = 0, high = -1, isChangeable = true, reverseRoleName = "targetGroups")
    private Set<Target> targets = new HashSet<Target>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected TargetGroup() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected TargetGroup(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    @Deprecated
    public TargetGroup(WritableVersion wVersion, java.lang.String namingSystem, java.lang.String shortName)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, shortName);
        init(attributes);
    }

    /**
     * Constructor for required arguments
     */
    public TargetGroup(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public TargetGroup(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: groupingType
     */
    public String getGroupingType() {
        return (String) get_prop(PROP_GROUPINGTYPE);
    }

    public void setGroupingType(String groupingType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GROUPINGTYPE, groupingType);
    }

    /**
     * Property: targetGroup
     */
    public TargetGroup getTargetGroup() {
        return (TargetGroup) get_prop(PROP_TARGETGROUP);
    }

    public void setTargetGroup(TargetGroup targetGroup) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TARGETGROUP, targetGroup);
    }

    /**
     * Property: subTargetGroups
     */
    public Set<TargetGroup> getSubTargetGroups() {
        return (Set<TargetGroup>) get_prop(PROP_SUBTARGETGROUPS);
    }

    public void setSubTargetGroups(java.util.Collection<TargetGroup> subTargetGroups)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBTARGETGROUPS, subTargetGroups);
    }

    public void addSubTargetGroup(TargetGroup subTargetGroup)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SUBTARGETGROUPS, subTargetGroup);
    }

    public void removeSubTargetGroup(TargetGroup subTargetGroup)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SUBTARGETGROUPS, subTargetGroup);
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

}
