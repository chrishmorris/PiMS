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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.annotation.Attribute;
import org.pimslims.dao.WritableVersion;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.target.ResearchObjectiveElement;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "SAM_SAMPLECOMPONENT")
@org.pimslims.annotation.MetaClass(helpText = "The information on the sample component. One component is one element of the contents of the sample.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.sample.AbstractSample.class, parentRoleName = "abstractSample")
public class SampleComponent extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CONCENTRATION = "concentration";

    @Deprecated
    // unused
    public static final String PROP_CONCENTRATIONERROR = "concentrationError";

    public static final String PROP_CONCENTRATIONUNIT = "concentrationUnit";

    public static final String PROP_CONCDISPLAYUNIT = "concDisplayUnit";

    public static final String PROP_PH = "ph";

    public static final String PROP_PURITY = "purity";

    public static final String PROP_ABSTRACTSAMPLE = "abstractSample";

    public static final String PROP_RESEARCHOBJECTIVEELEMENT = "researchObjectiveElement";

    public static final String PROP_CONTAINER = "container";

    public static final String PROP_REFCOMPONENT = "refComponent";

    public static final String PROP_CONTENTS = "contents";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCENTRATION", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Concentration of the component in the sample.")
    private Float concentration;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCENTRATIONERROR", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The concentration error.")
    private Float concentrationError;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCENTRATIONUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The concentration unit that should be one of these units: kg/m3 for mass density, mol/m3 for amount-of-substance concentration, m3/m3 for volume fraction, mol/mol for amount of substance fraction and kg/kg for mass fraction.", constraints = { "value_must_be_one_of('kg/m3', 'M', 'm3/m3', 'mol/mol', 'kg/kg')" })
    private String concentrationUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String concDisplayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PH", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The pH.")
    private Float ph;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PURITY", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The value of the sample component purity.")
    private Float purity;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ABSTRACTSAMPLEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "sam_samplecomponent_as_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "sampleComponents")
    private AbstractSample abstractSample;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "RESEARCHOBJECTIVELEMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sam_samplecomponent_roe_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "sampleComponents")
    private ResearchObjectiveElement researchObjectiveElement;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CONTAINERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sam_samplecomponent_c_inx")
    @org.pimslims.annotation.Role(helpText = "The container component to which the sub components belong.", low = 0, high = 1, isChangeable = true, reverseRoleName = "contents")
    private SampleComponent container;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REFCOMPONENTID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "sam_samplecomponent_rc_inx")
    @org.pimslims.annotation.Role(helpText = "The component used as a reference associated to a SampleComponent.", low = 1, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Substance refComponent;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "container")
    @org.pimslims.annotation.Role(helpText = "List of sub component contained in another component. This is the contents of a component.", low = 0, high = -1, isChangeable = true, reverseRoleName = "container")
    private final Set<SampleComponent> contents = new HashSet<SampleComponent>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SampleComponent() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SampleComponent(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public SampleComponent(WritableVersion wVersion, org.pimslims.model.molecule.Substance refComponent,
        org.pimslims.model.sample.AbstractSample abstractSample)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_ABSTRACTSAMPLE, abstractSample);
        attributes.put(PROP_REFCOMPONENT, refComponent);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SampleComponent(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: concentration
     */
    public Float getConcentration() {
        return (Float) get_prop(PROP_CONCENTRATION);
    }

    public void setConcentration(Float concentration) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCENTRATION, concentration);
    }

    /**
     * Property: concentrationError
     */
    @Deprecated
    // unused
    public Float getConcentrationError() {
        return (Float) get_prop(PROP_CONCENTRATIONERROR);
    }

    public void setConcentrationError(Float concentrationError)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCENTRATIONERROR, concentrationError);
    }

    /**
     * Property: concentrationUnit
     */
    public String getConcentrationUnit() {
        return (String) get_prop(PROP_CONCENTRATIONUNIT);
    }

    public void setConcentrationUnit(String concentrationUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCENTRATIONUNIT, concentrationUnit);
    }

    /**
     * Property: concDisplayUnit
     */
    public String getConcDisplayUnit() {
        return (String) get_prop(PROP_CONCDISPLAYUNIT);
    }

    public void setConcDisplayUnit(String concDisplayUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCDISPLAYUNIT, concDisplayUnit);
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
     * Property: purity
     */
    public Float getPurity() {
        return (Float) get_prop(PROP_PURITY);
    }

    public void setPurity(Float purity) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PURITY, purity);
    }

    /**
     * Property: abstractSample
     */
    public AbstractSample getAbstractSample() {
        return (AbstractSample) get_prop(PROP_ABSTRACTSAMPLE);
    }

    public void setAbstractSample(AbstractSample abstractSample)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ABSTRACTSAMPLE, abstractSample);
    }

    /**
     * Property: researchObjectiveElement
     */
    public ResearchObjectiveElement getResearchObjectiveElement() {
        return (ResearchObjectiveElement) get_prop(PROP_RESEARCHOBJECTIVEELEMENT);
    }

    public void setResearchObjectiveElement(ResearchObjectiveElement researchObjectiveElement)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESEARCHOBJECTIVEELEMENT, researchObjectiveElement);
    }

    /**
     * Property: container
     */
    public SampleComponent getContainer() {
        return (SampleComponent) get_prop(PROP_CONTAINER);
    }

    public void setContainer(SampleComponent container) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONTAINER, container);
    }

    /**
     * Property: refComponent
     */
    public Substance getRefComponent() {
        return (Substance) get_prop(PROP_REFCOMPONENT);
    }

    public void setRefComponent(Substance refComponent) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFCOMPONENT, refComponent);
    }

    /**
     * Property: contents
     */
    public Set<SampleComponent> getContents() {
        return (Set<SampleComponent>) get_prop(PROP_CONTENTS);
    }

    public void setContents(java.util.Collection<SampleComponent> contents)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONTENTS, contents);
    }

    public void addContent(SampleComponent content) throws org.pimslims.exception.ConstraintException {
        add(PROP_CONTENTS, content);
    }

    public void removeContent(SampleComponent content) throws org.pimslims.exception.ConstraintException {
        remove(PROP_CONTENTS, content);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getName()
     */
    @Override
    @Attribute(helpText = "The derived name of this SampleComponent.")
    public String getName() {
        if (this.refComponent == null || this.abstractSample == null)
            return this.get_Hook();
        return "'" + getRefComponent().get_Name() + "' in '" + getAbstractSample().get_Name() + "'";
    }

}
