package org.pimslims.model.sample;

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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.holder.RefSamplePosition;

// TODO rename this to "Recipe"
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ABSTRACTSAMPLEID")
@Table(name = "SAM_REFSAMPLE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Recipe to which a 'real' sample conforms. "
    + "This is a means to store details which are shared by instances of the same reagent but are supplier and batch independant.", keyNames = {}, subclasses = {})
public class RefSample extends org.pimslims.model.sample.AbstractSample implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_REFSAMPLEPOSITIONS = "refSamplePositions";

    public static final String PROP_REFSAMPLESOURCES = "refSampleSources";

    public static final String PROP_ISSALTCRYSTAL = "isSaltCrystal";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refSample")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "refSample")
    private Set<RefSamplePosition> refSamplePositions = new HashSet<RefSamplePosition>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refSample")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "refSample")
    private Set<ReagentCatalogueEntry> refSampleSources = new HashSet<ReagentCatalogueEntry>(0);

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "isSaltCrystal", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "May cause salt crystal?")
    private Boolean isSaltCrystal;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefSample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefSample(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefSample(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefSample(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */

    /**
     * Property: refSamplePositions
     */
    public Set<RefSamplePosition> getRefSamplePositions() {
        return (Set<RefSamplePosition>) get_prop(PROP_REFSAMPLEPOSITIONS);
    }

    public void setRefSamplePositions(java.util.Collection<RefSamplePosition> refSamplePositions)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFSAMPLEPOSITIONS, refSamplePositions);
    }

    public void addRefSamplePosition(RefSamplePosition refSamplePosition)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFSAMPLEPOSITIONS, refSamplePosition);
    }

    public void removeRefSamplePosition(RefSamplePosition refSamplePosition)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFSAMPLEPOSITIONS, refSamplePosition);
    }

    /**
     * Property: refSampleSources
     */
    public Set<ReagentCatalogueEntry> getRefSampleSources() {
        return (Set<ReagentCatalogueEntry>) get_prop(PROP_REFSAMPLESOURCES);
    }

    public void setRefSampleSources(java.util.Collection<ReagentCatalogueEntry> refSampleSources)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFSAMPLESOURCES, refSampleSources);
    }

    public void addRefSampleSource(ReagentCatalogueEntry refSampleSource)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFSAMPLESOURCES, refSampleSource);
    }

    public void removeRefSampleSource(ReagentCatalogueEntry refSampleSource)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFSAMPLESOURCES, refSampleSource);
    }

    /**
     * Property: isLocked
     */
    public Boolean getIsSaltCrystal() {
        return (Boolean) get_prop(PROP_ISSALTCRYSTAL);
    }

    public void setIsSaltCrystal(Boolean IsSaltCrystal) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISSALTCRYSTAL, IsSaltCrystal);
    }

    public Collection<Sample> getConformings() {
        return this.get_Version().findAll(Sample.class, Sample.PROP_REFSAMPLE, this);
    }

}
