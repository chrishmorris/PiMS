package org.pimslims.model.holder;

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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.sample.ReagentCatalogueEntry;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ABSTRACTHOLDERID")
@Table(name = "HOLD_REFHOLDER", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "The purpose of RefHolder is to allow to describe the layout of RefSamples, which may be independent of who actually supplied the holder.", keyNames = {}, subclasses = {})
public class RefHolder extends org.pimslims.model.holder.AbstractHolder implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_REFSAMPLEPOSITIONS = "refSamplePositions";

    public static final String PROP_REFHOLDERSOURCES = "refHolderSources";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refHolder")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "refHolder")
    private final Set<RefSamplePosition> refSamplePositions = new HashSet<RefSamplePosition>(0);

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "refHolder")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "refHolder")
    private final Set<RefHolderSource> refHolderSources = new HashSet<RefHolderSource>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefHolder() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefHolder(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefHolder(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefHolder(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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

    public void setRefSamplePositions(final java.util.Collection<RefSamplePosition> refSamplePositions)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFSAMPLEPOSITIONS, refSamplePositions);
    }

    public void addRefSamplePosition(final RefSamplePosition refSamplePosition)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFSAMPLEPOSITIONS, refSamplePosition);
    }

    public void removeRefSamplePosition(final RefSamplePosition refSamplePosition)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFSAMPLEPOSITIONS, refSamplePosition);
    }

    /**
     * Property: refSampleSources
     */
    public Set<RefHolderSource> getRefHolderSources() {
        return (Set<RefHolderSource>) get_prop(PROP_REFHOLDERSOURCES);
    }

    public void setRefHolderSources(final java.util.Collection<ReagentCatalogueEntry> refSampleSources)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFHOLDERSOURCES, refSampleSources);
    }

    public void addRefHolderSource(final RefHolderSource refHolderSource)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_REFHOLDERSOURCES, refHolderSource);
    }

    public void removeRefHolderSource(final RefHolderSource refHolderSource)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_REFHOLDERSOURCES, refHolderSource);
    }

}
