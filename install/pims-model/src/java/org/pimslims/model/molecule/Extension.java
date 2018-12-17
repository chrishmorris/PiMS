package org.pimslims.model.molecule;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

/**
 * @author Anne Pajon
 * @date Jun 12, 2009
 *
 * Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 3.2
 *
 * Copyright (c) 2007
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * A copy of the license is in dist/docs/LGPL.txt.
 * It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.txt
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "MOLECULEID")
@Table(name = "MOLE_EXTENSION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "This is a subtype of Molecule. They are 5'-extensions for Primers to include optional values for 'Encoded protein tag' and 'Restriction site'", keyNames = {}, subclasses = {})
public class Extension extends Molecule implements java.lang.Comparable, java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_EXTENSIONTYPE = "extensionType";
    public static final String PROP_RESTRICTIONENZYME = "restrictionEnzyme";
    public static final String PROP_RELATEDPROTEINTAGSEQ = "relatedProteinTagSeq";
    public static final String PROP_ISFORUSE = "isForUse";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "EXTENSIONTYPE", length=32, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "forward or reverse", constraints = { "value_must_be_one_of('forward', 'reverse')" })
    private String extensionType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "RESTRICTIONENZYME", length=254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The name of the restriction enzyme.")
    private String restrictionEnzyme;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "RELATEDPROTEINTAGSEQ", columnDefinition="TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sequence of the related protein Tag.")
    private String relatedProteinTagSeq;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISFORUSE", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "To prevent incorrect extension from being used. It's a way of marking them not to be used.")
    private Boolean isForUse;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Extension() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Extension(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Extension(WritableVersion wVersion, java.lang.String name, java.lang.String extensionType) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_MOLTYPE, "DNA");
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_EXTENSIONTYPE, extensionType);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Extension(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: extensionType
     */
    public String getExtensionType() {
        return (String) get_prop(PROP_EXTENSIONTYPE);
    }

    public void setExtensionType(String extensionType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EXTENSIONTYPE, extensionType);
    }

    /**
     * Property: restrictionEnzyme
     */
    public String getRestrictionEnzyme() {
        return (String) get_prop(PROP_RESTRICTIONENZYME);
    }

    public void setRestrictionEnzyme(String restrictionEnzyme) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESTRICTIONENZYME, restrictionEnzyme);
    }

    /**
     * Property: relatedProteinTagSeq
     */
    public String getRelatedProteinTagSeq() {
        return (String) get_prop(PROP_RELATEDPROTEINTAGSEQ);
    }

    public void setRelatedProteinTagSeq(String relatedProteinTagSeq) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RELATEDPROTEINTAGSEQ, relatedProteinTagSeq);
    }

    /**
     * Property: isForUse
     */
    public Boolean getIsForUse() {
        return (Boolean) get_prop(PROP_ISFORUSE);
    }

    public void setIsForUse(final Boolean isForUse) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISFORUSE, isForUse);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */

}
