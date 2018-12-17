package org.pimslims.model.reference;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.model.people.Organisation;

/**
 * @author Anne Pajon
 * @date Jun 12, 2009
 * 
 *       Protein Information Management System - PiMS project
 * @see http://www.pims-lims.org
 * @version: 3.2
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
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "PublicEntryID")
@Table(name = "REF_HOLDERTYPESOURCE", uniqueConstraints = { @UniqueConstraint(columnNames = { "CATALOGNUM",
    "HOLDERTYPEID" }) })
@org.pimslims.annotation.MetaClass(helpText = "Defines examples of supplier details for this particular holder type to allow a link to be created to the product data page.", keyNames = {
    "CATALOGNUM", "HOLDERTYPE" }, subclasses = {}, parent = org.pimslims.model.reference.HolderType.class, parentRoleName = "holderType")
public class HolderTypeSource extends org.pimslims.model.reference.PublicEntry {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CATALOGNUM = "catalogNum";

    public static final String PROP_PRODUCTNAME = "productName";

    public static final String PROP_DATAPAGEURL = "dataPageUrl";

    public static final String PROP_HOLDERTYPE = "holderType";

    public static final String PROP_SUPPLIER = "supplier";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "CATALOGNUM", length = 80, unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The catalog number.", constraints = { "contains_no_linebreak" })
    private String catalogNum;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PRODUCTNAME", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "eg: condition's manufacturer code.", constraints = { "contains_no_linebreak" })
    private String productName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DATAPAGEURL", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The url for the product data page")
    private String dataPageUrl;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HOLDERTYPEID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "ref_hts_holdertype_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "holderTypeSources")
    private AbstractHolderType holderType;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUPPLIERID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "ref_hts_supplier_inx")
    @org.pimslims.annotation.Role(helpText = "The name of the laboratory if it is 'Home made' or the name of the company who provides this holder type.", low = 1, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Organisation supplier;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected HolderTypeSource() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected HolderTypeSource(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public HolderTypeSource(WritableVersion wVersion, java.lang.String catalogNum,
        org.pimslims.model.reference.AbstractHolderType holderType,
        org.pimslims.model.people.Organisation supplier) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_CATALOGNUM, catalogNum);
        attributes.put(PROP_HOLDERTYPE, holderType);
        attributes.put(PROP_SUPPLIER, supplier);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public HolderTypeSource(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: catalogNum
     */
    public String getCatalogNum() {
        return (String) get_prop(PROP_CATALOGNUM);
    }

    public void setCatalogNum(String catalogNum) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CATALOGNUM, catalogNum);
    }

    /**
     * Property: productName
     */
    public String getProductName() {
        return (String) get_prop(PROP_PRODUCTNAME);
    }

    public void setProductName(String productName) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PRODUCTNAME, productName);
    }

    /**
     * Property: dataPageUrl
     */
    public String getDataPageUrl() {
        return (String) get_prop(PROP_DATAPAGEURL);
    }

    public void setDataPageUrl(String dataPageUrl) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DATAPAGEURL, dataPageUrl);
    }

    /**
     * Property: holderType
     */
    public AbstractHolderType getHolderType() {
        return (AbstractHolderType) get_prop(PROP_HOLDERTYPE);
    }

    public void setHolderType(AbstractHolderType holderType)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDERTYPE, holderType);
    }

    /**
     * Property: supplier
     */
    public Organisation getSupplier() {
        return (Organisation) get_prop(PROP_SUPPLIER);
    }

    public void setSupplier(Organisation supplier) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SUPPLIER, supplier);
    }

    /**
     * AbstractModelObject.getName
     * 
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.supplier.getName() + ": " + this.catalogNum;
    }

}
