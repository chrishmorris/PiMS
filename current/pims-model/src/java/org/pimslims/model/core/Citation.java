package org.pimslims.model.core;

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

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ATTACHMENTID")
@Table(name = "CORE_CITATION")
@org.pimslims.annotation.EquivalentClass("http://purl.org/dc/terms/BibliographicResource")
@org.pimslims.annotation.MetaClass(helpText = "Citation (litterature reference).", keyNames = {}, subclasses = {
    org.pimslims.model.core.JournalCitation.class, org.pimslims.model.core.BookCitation.class,
    org.pimslims.model.core.ConferenceCitation.class, org.pimslims.model.core.ThesisCitation.class })
public abstract class Citation extends org.pimslims.model.core.Attachment implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_TITLE = "title";

    public static final String PROP_STATUS = "status";

    public static final String PROP_FIRSTPAGE = "firstPage";

    public static final String PROP_LASTPAGE = "lastPage";

    public static final String PROP_YEAR = "year";

    public static final String PROP_AUTHORS = "authors";

    public static final String PROP_EDITORS = "editors";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://purl.uniprot.org/core/title")
    @Column(name = "TITLE", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Citation (article) Title", constraints = { "contains_no_linebreak" })
    private String title;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "STATUS", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "E.g. 'published', 'in press','to be published' ?", constraints = { "contains_no_linebreak" })
    private String status;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "FIRSTPAGE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "First page number (not an integer, as it might be e.g. '235A')", constraints = { "contains_no_linebreak" })
    private String firstPage;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LASTPAGE", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Last page number (not an integer, as it might be e.g. '238A')", constraints = { "contains_no_linebreak" })
    private String lastPage;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "YEAR", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Year of publication")
    private Integer year;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AUTHORS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Authors of Citation")
    private String authors;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "EDITORS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "People who are editors of Citation")
    private String editors;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Citation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Citation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Citation(WritableVersion wVersion, org.pimslims.model.core.LabBookEntry parent)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_PARENTENTRY, parent);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Citation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: title
     */
    public String getTitle() {
        return (String) get_prop(PROP_TITLE);
    }

    public void setTitle(String title) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TITLE, title);
    }

    /**
     * Property: status
     */
    public String getStatus() {
        return (String) get_prop(PROP_STATUS);
    }

    public void setStatus(String status) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_STATUS, status);
    }

    /**
     * Property: firstPage
     */
    public String getFirstPage() {
        return (String) get_prop(PROP_FIRSTPAGE);
    }

    public void setFirstPage(String firstPage) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_FIRSTPAGE, firstPage);
    }

    /**
     * Property: lastPage
     */
    public String getLastPage() {
        return (String) get_prop(PROP_LASTPAGE);
    }

    public void setLastPage(String lastPage) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_LASTPAGE, lastPage);
    }

    /**
     * Property: year
     */
    public Integer getYear() {
        return (Integer) get_prop(PROP_YEAR);
    }

    public void setYear(Integer year) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_YEAR, year);
    }

    /**
     * Property: authors
     */
    public String getAuthors() {
        return (String) get_prop(PROP_AUTHORS);
    }

    public void setAuthors(String authors) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AUTHORS, authors);
    }

    /**
     * Property: editors
     */
    public String getEditors() {
        return (String) get_prop(PROP_EDITORS);
    }

    public void setEditors(String editors) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_EDITORS, editors);
    }

    /* ************************************************************ */
    /* CLASS CONSTRAINTS                                            */
    /* ************************************************************ */
    @Override
    protected void checkConstraints() throws ConstraintException {

        if (isEmpty(this.getTitle())) {
            if ((isEmpty(getAuthors()) && isEmpty(this.getYear()))
                || (isEmpty(this.getAuthors()) && isEmpty(this.getYear()))
                || (isEmpty(this.getAuthors()) && isEmpty(this.getYear()))) {
                throw new ConstraintException(this, "title,authors,year", this.getTitle() + ","
                    + this.getAuthors() + this.getYear(),
                    "A citation must have a title or a year with an author.");
            }
        }

        if ((isEmpty(this.getAuthors()) && isEmpty(this.getYear()))
            || (isEmpty(this.getAuthors()) && isEmpty(this.getYear()))
            || (isEmpty(this.getAuthors()) && isEmpty(this.getYear()))) {
            if (isEmpty(this.getTitle())) {
                throw new ConstraintException(this, "title,authors,year", this.getTitle() + ","
                    + this.getAuthors() + this.getYear(),
                    "A citation must have a title or a year with an author.");
            }
        }

    }

    /**
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        String year = "";
        if (!isEmpty(this.year))
            year = " " + Integer.toString(this.year);
        if (!isEmpty(this.getAuthors()))
            return this.authors + year;
        else if (!isEmpty(this.getTitle()))
            return this.title + year;
        else
            return this.get_Hook();

    }
}
