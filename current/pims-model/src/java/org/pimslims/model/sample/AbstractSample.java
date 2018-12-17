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
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.pimslims.annotation.Role;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.reference.HazardPhrase;
import org.pimslims.model.reference.SampleCategory;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "SAM_ABSTRACTSAMPLE", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(helpText = "General information on the sample or reagent.", keyNames = { "NAME" }, subclasses = {
    org.pimslims.model.sample.Sample.class, org.pimslims.model.sample.RefSample.class })
public abstract class AbstractSample extends org.pimslims.model.core.LabBookEntry implements
    java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_PH = "ph";

    public static final String PROP_IONICSTRENGTH = "ionicStrength";

    public static final String PROP_ISHAZARD = "isHazard";

    public static final String PROP_ISACTIVE = "isActive";

    public static final String PROP_SAMPLECOMPONENTS = "sampleComponents";

    public static final String PROP_HAZARDPHRASES = "hazardPhrases";

    public static final String PROP_SAMPLECATEGORIES = "sampleCategories";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The name of the sample or the code to identify it. It is the unique identifier.", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The pH.")
    private Float ph;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "IONICSTRENGTH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The ionic strength (dimensionless quantity).")
    private Float ionicStrength;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISHAZARD", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Is the sample hazard?")
    private Boolean isHazard;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISACTIVE", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "True if the sample is active.")
    private Boolean isActive;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "abstractSample")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "abstractSample")
    private final Set<SampleComponent> sampleComponents = new HashSet<SampleComponent>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "SAM_ABSTSA2HAZAPH", joinColumns = { @JoinColumn(name = "OTHERROLE", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "HAZARDPHRASEID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of all hazard pharses associated to a particular sample.", low = 0, high = -1, isChangeable = true, reverseRoleName = "Unresolved")
    private final Set<HazardPhrase> hazardPhrases = new HashSet<HazardPhrase>(0);

    /* ------------------------------------------------------------ */
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "sam_sampca2abstsa", joinColumns = { @JoinColumn(name = "ABSTRACTSAMPLEID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "SAMPLECATEGORYID", nullable = false, updatable = false) })
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @org.pimslims.annotation.Role(helpText = "List of sample category associated to a sample.", low = 0, high = -1, isChangeable = true)
    private final Set<SampleCategory> sampleCategories = new HashSet<SampleCategory>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected AbstractSample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected AbstractSample(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public AbstractSample(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public AbstractSample(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: ph
     */
    public Float getPh() {
        return (Float) get_prop(PROP_PH);
    }

    public void setPh(Float ph) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PH, ph);
    }

    /**
     * Property: ionicStrength
     */
    public Float getIonicStrength() {
        return (Float) get_prop(PROP_IONICSTRENGTH);
    }

    public void setIonicStrength(Float ionicStrength) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IONICSTRENGTH, ionicStrength);
    }

    /**
     * Property: isHazard
     */
    public Boolean getIsHazard() {
        return (Boolean) get_prop(PROP_ISHAZARD);
    }

    public void setIsHazard(Boolean isHazard) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISHAZARD, isHazard);
    }

    /**
     * Property: isActive
     */
    public Boolean getIsActive() {
        return (Boolean) get_prop(PROP_ISACTIVE);
    }

    public void setIsActive(Boolean isActive) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISACTIVE, isActive);
    }

    /**
     * Property: sampleComponents
     */
    public Set<SampleComponent> getSampleComponents() {
        return (Set<SampleComponent>) get_prop(PROP_SAMPLECOMPONENTS);
    }

    public void setSampleComponents(java.util.Collection<SampleComponent> sampleComponents)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLECOMPONENTS, sampleComponents);
    }

    public void addSampleComponent(SampleComponent sampleComponent)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SAMPLECOMPONENTS, sampleComponent);
    }

    public void removeSampleComponent(SampleComponent sampleComponent)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SAMPLECOMPONENTS, sampleComponent);
    }

    /**
     * Property: hazardPhrases
     */
    public Set<HazardPhrase> getHazardPhrases() {
        return (Set<HazardPhrase>) get_prop(PROP_HAZARDPHRASES);
    }

    public void setHazardPhrases(java.util.Collection<HazardPhrase> hazardPhrases)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HAZARDPHRASES, hazardPhrases);
    }

    /**
     * Property: sampleCategories
     */
    public Set<SampleCategory> getSampleCategories() {
        return (Set<SampleCategory>) get_prop(PROP_SAMPLECATEGORIES);
    }

    public void setSampleCategories(java.util.Collection<SampleCategory> sampleCategories)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SAMPLECATEGORIES, sampleCategories);
    }

    public void addSampleCategory(org.pimslims.model.reference.SampleCategory sampleCategory)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_SAMPLECATEGORIES, sampleCategory);
    }

    public void removeSampleCategory(org.pimslims.model.reference.SampleCategory sampleCategory)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_SAMPLECATEGORIES, sampleCategory);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getLocalRiskPhrases()
     */
    @Role(helpText = "Local risk phrases that are specific to a certain laboratory.", isDerived = true)
    public java.util.Set<org.pimslims.model.reference.HazardPhrase> getLocalRiskPhrases() {
        java.util.Set<org.pimslims.model.reference.HazardPhrase> list =
            new java.util.HashSet<org.pimslims.model.reference.HazardPhrase>();
        java.util.Iterator i = getHazardPhrases().iterator();
        while (i.hasNext()) {
            org.pimslims.model.reference.HazardPhrase hazardPhrase =
                (org.pimslims.model.reference.HazardPhrase) i.next();
            if (hazardPhrase.getClassification().equals("local")) {
                list.add(hazardPhrase);
            }
        }
        return list;
    }

    /**
     * Derived method: getRPhrases()
     */
    @Role(helpText = "Risk Phrases (R-phrases) that are associated to a sample.", isDerived = true)
    public java.util.Set<org.pimslims.model.reference.HazardPhrase> getRPhrases() {
        java.util.Set<org.pimslims.model.reference.HazardPhrase> list =
            new java.util.HashSet<org.pimslims.model.reference.HazardPhrase>();
        java.util.Iterator i = getHazardPhrases().iterator();
        while (i.hasNext()) {
            org.pimslims.model.reference.HazardPhrase hazardPhrase =
                (org.pimslims.model.reference.HazardPhrase) i.next();
            if ((hazardPhrase.getClassification().equals("MSDS")) & (hazardPhrase.getCode().charAt(0) == 'R')) {
                list.add(hazardPhrase);
            }
        }
        return list;
    }

    /**
     * Derived method: getSPhrases()
     */
    @Role(helpText = "Safety Phrases (S-phrases) that are associated to a sample.", isDerived = true)
    public java.util.Set<org.pimslims.model.reference.HazardPhrase> getSPhrases() {
        java.util.Set<org.pimslims.model.reference.HazardPhrase> list =
            new java.util.HashSet<org.pimslims.model.reference.HazardPhrase>();
        java.util.Iterator i = getHazardPhrases().iterator();
        while (i.hasNext()) {
            org.pimslims.model.reference.HazardPhrase hazardPhrase =
                (org.pimslims.model.reference.HazardPhrase) i.next();
            if ((hazardPhrase.getClassification().equals("MSDS")) & (hazardPhrase.getCode().charAt(0) == 'S')) {
                list.add(hazardPhrase);
            }
        }
        return list;
    }

}
