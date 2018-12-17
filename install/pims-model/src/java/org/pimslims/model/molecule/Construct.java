package org.pimslims.model.molecule;

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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "MOLECULEID")
@Table(name = "MOLE_CONSTRUCT", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "This is a subtype of MolComponent. It is used to summarise information about a construct such as an expression vector plasmid.", keyNames = {}, subclasses = {})
public class Construct extends org.pimslims.model.molecule.Molecule implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CONSTRUCTSTATUS = "constructStatus";

    public static final String PROP_SEQUENCETYPE = "sequenceType";

    public static final String PROP_FUNCTION = "function";

    public static final String PROP_RESISTANCEDETAILS = "resistanceDetails";

    public static final String PROP_PROMOTERDETAILS = "promoterDetails";

    public static final String PROP_MARKERDETAILS = "markerDetails";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONSTRUCTSTATUS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The status of the construct e.g. empty, with target, ...", constraints = { "contains_no_linebreak" })
    private String constructStatus;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SEQUENCETYPE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The type of nucleic acid sequence in the construct. Note that to find all the DNA molecules it is necessary to search for DNA + cDNA and for RNA, RNA + mRNA + tRNA.", constraints = { "contains_no_linebreak" })
    private String sequenceType;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FUNCTION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The Construct class is also used to describe vectors and so the function attribute can be used to indicate the function of the vector e.g. expression.")
    private String function;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "RESISTANCEDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Detail field for resistance information. Precise information should go to a MolCompFeature.")
    private String resistanceDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PROMOTERDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Detail field for promoter information. Precise information should go to a MolCompFeature.")
    private String promoterDetails;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "MARKERDETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Detail field for marker information. Precise information should go to a MolCompFeature.")
    private String markerDetails;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Construct() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Construct(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    @Deprecated
    public Construct(WritableVersion wVersion, java.lang.String molType, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_MOLTYPE, molType);
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor for required arguments
     */
    public Construct(WritableVersion wVersion, java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_MOLTYPE, "DNA");
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Construct(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: constructStatus
     */
    public String getConstructStatus() {
        return (String) get_prop(PROP_CONSTRUCTSTATUS);
    }

    public void setConstructStatus(String constructStatus) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONSTRUCTSTATUS, constructStatus);
    }

    /**
     * Property: sequenceType
     */
    public String getSequenceType() {
        return (String) get_prop(PROP_SEQUENCETYPE);
    }

    public void setSequenceType(String sequenceType) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SEQUENCETYPE, sequenceType);
    }

    /**
     * Property: function
     */
    public String getFunction() {
        return (String) get_prop(PROP_FUNCTION);
    }

    public void setFunction(String function) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FUNCTION, function);
    }

    /**
     * Property: resistanceDetails
     */
    public String getResistanceDetails() {
        return (String) get_prop(PROP_RESISTANCEDETAILS);
    }

    public void setResistanceDetails(String resistanceDetails)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_RESISTANCEDETAILS, resistanceDetails);
    }

    /**
     * Property: promoterDetails
     */
    public String getPromoterDetails() {
        return (String) get_prop(PROP_PROMOTERDETAILS);
    }

    public void setPromoterDetails(String promoterDetails) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROMOTERDETAILS, promoterDetails);
    }

    /**
     * Property: markerDetails
     */
    public String getMarkerDetails() {
        return (String) get_prop(PROP_MARKERDETAILS);
    }

    public void setMarkerDetails(String markerDetails) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_MARKERDETAILS, markerDetails);
    }

}
