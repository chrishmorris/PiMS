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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.annotation.Uri;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.core.LabNotebook;

@Entity
//
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

    public static final String PROP_SAME_AS = "sameAs";

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
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "DETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details field for comments.")
    private String details;

    @org.hibernate.annotations.CollectionOfElements
    // TODO ElementCollection
    @JoinTable(name = "REF_PublEn_sameAs", joinColumns = @JoinColumn(name = "DBID"))
    @org.hibernate.annotations.IndexColumn(name = "ORDER_", base = 0)
    @Column(name = "SAME_AS")
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2002/07/owl#sameAs")
    @org.pimslims.annotation.Attribute(helpText = "Resources in other ontologies that have the same meaning")
    private final List<String> sameAs = new ArrayList<String>(0);

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

    /**
     * Property: sameAs
     * 
     * Resources in other ontology that have the same meanining. Note that an owl:sameAs annotation has strong
     * implications. In particular, it is transitive. So often we will report this with the weaker
     * rdfs:seeAlso.
     */
    public Collection<String> getSameAs() {
        return (List<String>) get_prop(PROP_SAME_AS);
    }

    public void setSameAs(Collection<String> sameAs) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAME_AS, new ArrayList(sameAs));
    }

    @Uri
    public void addSameAs(String sameAs) {
        this.sameAs.add(sameAs);
    }

}
