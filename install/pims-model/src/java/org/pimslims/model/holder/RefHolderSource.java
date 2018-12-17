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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "HOLD_REFHOLDERSOURCE", uniqueConstraints = { @UniqueConstraint(columnNames = { "CATALOGNUM",
    "REFHOLDERID" }) })
@org.pimslims.annotation.MetaClass(helpText = "Defines examples of supplier details for this particular reagent to allow a link to be created to the product data page.", keyNames = {
    "CATALOGNUM", "REFHOLDER" }, subclasses = {}, parent = org.pimslims.model.holder.RefHolder.class, parentRoleName = "refHolder")
public class RefHolderSource extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_CATALOGNUM = "catalogNum";

    public static final String PROP_PRODUCTNAME = "productName";

    public static final String PROP_PRODUCTGROUPNAME = "productGroupName";

    public static final String PROP_DATAPAGEURL = "dataPageUrl";

    public static final String PROP_REFHOLDER = "refHolder";

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
    @Column(name = "PRODUCTGROUPNAME", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "eg: condition's manufacturer screen Name.", constraints = { "contains_no_linebreak" })
    private String productGroupName;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "DATAPAGEURL", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The url for the product data page")
    private String dataPageUrl;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REFHOLDERID", unique = false, nullable = true)
    @org.pimslims.annotation.Role(helpText = "N/A", low = 1, high = 1, isChangeable = true, reverseRoleName = "refHolderSources")
    private RefHolder refHolder;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUPPLIERID", unique = false, nullable = false)
    @org.pimslims.annotation.Role(helpText = "The name of the laboratory if it is 'Home made' or the name of the company who provides the component.", low = 1, high = 1, isChangeable = true, reverseRoleName = "Unresolved")
    private Organisation supplier;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected RefHolderSource() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected RefHolderSource(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public RefHolderSource(WritableVersion wVersion, java.lang.String catalogNum, RefHolder refHolder,
        org.pimslims.model.people.Organisation supplier) throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_REFHOLDER, refHolder);
        attributes.put(PROP_CATALOGNUM, catalogNum);
        attributes.put(PROP_SUPPLIER, supplier);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public RefHolderSource(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: productGroupName
     */
    public String getProductGroupName() {
        return (String) get_prop(PROP_PRODUCTGROUPNAME);
    }

    public void setProductGroupName(String productGroupName)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PRODUCTGROUPNAME, productGroupName);
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
     * Property: refHolder
     */
    public RefHolder getRefHolder() {
        return (RefHolder) get_prop(PROP_REFHOLDER);
    }

    public void setRefHolder(RefHolder refHolder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFHOLDER, refHolder);
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
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return supplier.getName() + ": " + this.catalogNum;
    }

}
