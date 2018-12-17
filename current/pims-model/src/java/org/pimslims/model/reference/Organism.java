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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Target;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_ORGANISM", uniqueConstraints = {})
@org.pimslims.annotation.MetaClass(orderBy = "name", helpText = "Natural source of molecule.", keyNames = { "NAME" }, subclasses = {})
public class Organism extends org.pimslims.model.reference.PublicEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_NCBITAXONOMYID = "ncbiTaxonomyId";

    public static final String PROP_SCIENTIFICNAME = "scientificName";

    public static final String PROP_GENUS = "genus";

    public static final String PROP_SPECIES = "species";

    public static final String PROP_ORGANISMACRONYM = "organismAcronym";

    public static final String PROP_ATCCNUMBER = "atccNumber";

    public static final String PROP_ICTVCODE = "ictvCode";

    public static final String PROP_STRAIN = "strain";

    public static final String PROP_FRACTION = "fraction";

    public static final String PROP_CELLLINE = "cellLine";

    public static final String PROP_CELLLOCATION = "cellLocation";

    public static final String PROP_CELLTYPE = "cellType";

    public static final String PROP_GENEMNEMONIC = "geneMnemonic";

    public static final String PROP_ORGAN = "organ";

    public static final String PROP_ORGANELLE = "organelle";

    public static final String PROP_TISSUE = "tissue";

    public static final String PROP_VARIANT = "variant";

    public static final String PROP_SUBVARIANT = "subVariant";

    public static final String PROP_PLASMID = "plasmid";

    public static final String PROP_PLASMIDDETAILS = "plasmidDetails";

    public static final String PROP_SECRETION = "secretion";

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
    @Basic(optional = true)
    @Column(name = "NCBITAXONOMYID", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The ID for the NCBI taxonomy node.")
    private String ncbiTaxonomyId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SCIENTIFICNAME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The NCBI scientific name. (= genius + species). This should really be a derived attribute!", constraints = { "contains_no_linebreak" })
    private String scientificName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GENUS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organism genus.", constraints = { "contains_no_linebreak" })
    private String genus;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SPECIES", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organism species.", constraints = { "contains_no_linebreak" })
    private String species;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORGANISMACRONYM", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The organism acronym.")
    private String organismAcronym;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ATCCNUMBER", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "ATCC number of cell line. Should be removed?", constraints = { "contains_no_linebreak" })
    private String atccNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ICTVCODE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The code for the International Comitee on Taxonomy of Viruses.")
    private String ictvCode;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STRAIN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organism strain.", constraints = { "contains_no_linebreak" })
    private String strain;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FRACTION", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Is this the same as tissueFraction (which would then be a better name)?")
    private String fraction;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CELLLINE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Source cell line.", constraints = { "contains_no_linebreak" })
    private String cellLine;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CELLLOCATION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The source cell location.")
    private String cellLocation;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CELLTYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Source cell type.", constraints = { "contains_no_linebreak" })
    private String cellType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "GENEMNEMONIC", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Gene Mnemonic.")
    private String geneMnemonic;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORGAN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Source Organ.", constraints = { "contains_no_linebreak" })
    private String organ;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ORGANELLE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organelle from which molecule is derived.")
    private String organelle;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TISSUE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Source tissue.", constraints = { "contains_no_linebreak" })
    private String tissue;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VARIANT", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organism variant.", constraints = { "contains_no_linebreak" })
    private String variant;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBVARIANT", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Organism sub-variant.")
    private String subVariant;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PLASMID", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Plasmid name, if molecule is (part of) a plasmid.")
    private String plasmid;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PLASMIDDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "N/A")
    private String plasmidDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SECRETION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Secretion from which molecule is derived.")
    private String secretion;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Organism() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Organism(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Organism(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_NAME, name);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Organism(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: ncbiTaxonomyId
     */
    public String getNcbiTaxonomyId() {
        return (String) get_prop(PROP_NCBITAXONOMYID);
    }

    public void setNcbiTaxonomyId(String ncbiTaxonomyId) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NCBITAXONOMYID, ncbiTaxonomyId);
    }

    /**
     * Property: scientificName
     */
    public String getScientificName() {
        return (String) get_prop(PROP_SCIENTIFICNAME);
    }

    public void setScientificName(String scientificName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SCIENTIFICNAME, scientificName);
    }

    /**
     * Property: genus
     */
    public String getGenus() {
        return (String) get_prop(PROP_GENUS);
    }

    public void setGenus(String genus) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GENUS, genus);
    }

    /**
     * Property: species
     */
    public String getSpecies() {
        return (String) get_prop(PROP_SPECIES);
    }

    public void setSpecies(String species) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SPECIES, species);
    }

    /**
     * Property: organismAcronym
     */
    public String getOrganismAcronym() {
        return (String) get_prop(PROP_ORGANISMACRONYM);
    }

    public void setOrganismAcronym(String organismAcronym) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGANISMACRONYM, organismAcronym);
    }

    /**
     * Property: atccNumber
     */
    public String getAtccNumber() {
        return (String) get_prop(PROP_ATCCNUMBER);
    }

    public void setAtccNumber(String atccNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ATCCNUMBER, atccNumber);
    }

    /**
     * Property: ictvCode
     */
    public String getIctvCode() {
        return (String) get_prop(PROP_ICTVCODE);
    }

    public void setIctvCode(String ictvCode) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ICTVCODE, ictvCode);
    }

    /**
     * Property: strain
     */
    public String getStrain() {
        return (String) get_prop(PROP_STRAIN);
    }

    public void setStrain(String strain) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STRAIN, strain);
    }

    /**
     * Property: fraction
     */
    public String getFraction() {
        return (String) get_prop(PROP_FRACTION);
    }

    public void setFraction(String fraction) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FRACTION, fraction);
    }

    /**
     * Property: cellLine
     */
    public String getCellLine() {
        return (String) get_prop(PROP_CELLLINE);
    }

    public void setCellLine(String cellLine) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CELLLINE, cellLine);
    }

    /**
     * Property: cellLocation
     */
    public String getCellLocation() {
        return (String) get_prop(PROP_CELLLOCATION);
    }

    public void setCellLocation(String cellLocation) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CELLLOCATION, cellLocation);
    }

    /**
     * Property: cellType
     */
    public String getCellType() {
        return (String) get_prop(PROP_CELLTYPE);
    }

    public void setCellType(String cellType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CELLTYPE, cellType);
    }

    /**
     * Property: geneMnemonic
     */
    public String getGeneMnemonic() {
        return (String) get_prop(PROP_GENEMNEMONIC);
    }

    public void setGeneMnemonic(String geneMnemonic) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_GENEMNEMONIC, geneMnemonic);
    }

    /**
     * Property: organ
     */
    public String getOrgan() {
        return (String) get_prop(PROP_ORGAN);
    }

    public void setOrgan(String organ) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGAN, organ);
    }

    /**
     * Property: organelle
     */
    public String getOrganelle() {
        return (String) get_prop(PROP_ORGANELLE);
    }

    public void setOrganelle(String organelle) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ORGANELLE, organelle);
    }

    /**
     * Property: tissue
     */
    public String getTissue() {
        return (String) get_prop(PROP_TISSUE);
    }

    public void setTissue(String tissue) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TISSUE, tissue);
    }

    /**
     * Property: variant
     */
    public String getVariant() {
        return (String) get_prop(PROP_VARIANT);
    }

    public void setVariant(String variant) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VARIANT, variant);
    }

    /**
     * Property: subVariant
     */
    public String getSubVariant() {
        return (String) get_prop(PROP_SUBVARIANT);
    }

    public void setSubVariant(String subVariant) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUBVARIANT, subVariant);
    }

    /**
     * Property: plasmid
     */
    public String getPlasmid() {
        return (String) get_prop(PROP_PLASMID);
    }

    public void setPlasmid(String plasmid) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PLASMID, plasmid);
    }

    /**
     * Property: plasmidDetails
     */
    public String getPlasmidDetails() {
        return (String) get_prop(PROP_PLASMIDDETAILS);
    }

    public void setPlasmidDetails(String plasmidDetails) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PLASMIDDETAILS, plasmidDetails);
    }

    /**
     * Property: secretion
     */
    public String getSecretion() {
        return (String) get_prop(PROP_SECRETION);
    }

    public void setSecretion(String secretion) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SECRETION, secretion);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Property: targets
     */
    public Collection<Target> getTargets() {
        return this.get_Version().findAll(Target.class, Target.PROP_SPECIES, this);
    }

    public void addTarget(Target target) throws ConstraintException {
        target.setSpecies(this);
    }

}
