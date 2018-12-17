/**
 * pims-model org.pimslims.model.core Bookmark.java
 * 
 * @author cm65
 * @date 20 Sep 2013
 * 
 *       Protein Information Management System
 * @version: 5.0
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.core;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

/**
 * Bookmark
 * 
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "CORE_BOOKMARK")
@org.pimslims.annotation.MetaClass(helpText = "A page in PiMS", keyNames = {}, subclasses = {})
public class Bookmark extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    /**
     * PROP_URL String Relative URL, excluding http://localhost:8080/pims/
     */
    public static final String PROP_URL = "url";

    @Basic(optional = false)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "A unique name to identify the bookmark", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = false)
    @Column(name = "URL", columnDefinition = "TEXT", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "The relative URL of the bookmarked page", constraints = { "contains_no_linebreak" })
    private String url;

    /**
     * Empty constructor required by Hibernate
     */
    protected Bookmark() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Bookmark(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Bookmark(WritableVersion version, String name, String url)
        throws org.pimslims.exception.ConstraintException {
        this(version);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);
        attributes.put(PROP_URL, url);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Bookmark(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
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

    public void setName(final String name) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_NAME, name);
    }

    public String getUrl() {
        return (String) get_prop(PROP_URL);
    }

    public void setUrl(String url) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_URL, url);
    }

}
