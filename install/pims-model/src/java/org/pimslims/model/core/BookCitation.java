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
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "CITATIONID")
@Table(name = "CORE_BOOKCITATION", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Citation (litterature reference) for book or book chapter.", keyNames = {}, subclasses = {})
@Deprecated
// It's not in use, and it is ill thought out
public class BookCitation extends org.pimslims.model.core.Citation implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_BOOKTITLE = "bookTitle";

    public static final String PROP_VOLUME = "volume";

    public static final String PROP_BOOKSERIES = "bookSeries";

    public static final String PROP_PUBLISHER = "publisher";

    public static final String PROP_PUBLISHERCITY = "publisherCity";

    public static final String PROP_ISBN = "isbn";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "BOOKTITLE", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Title of book")
    private String bookTitle;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "VOLUME", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Volume 'number' (not an integer)", constraints = { "contains_no_linebreak" })
    private String volume;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "BOOKSERIES", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Series of which book forms part")
    private String bookSeries;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PUBLISHER", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Book publisher")
    private String publisher;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PUBLISHERCITY", length = 254, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "City of book publisher")
    private String publisherCity;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ISBN", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "ISBN code for book", constraints = { "contains_no_linebreak" })
    private String isbn;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected BookCitation() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected BookCitation(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public BookCitation(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: bookTitle
     */
    public String getBookTitle() {
        return (String) get_prop(PROP_BOOKTITLE);
    }

    public void setBookTitle(String bookTitle) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_BOOKTITLE, bookTitle);
    }

    /**
     * Property: volume
     */
    public String getVolume() {
        return (String) get_prop(PROP_VOLUME);
    }

    public void setVolume(String volume) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_VOLUME, volume);
    }

    /**
     * Property: bookSeries
     */
    public String getBookSeries() {
        return (String) get_prop(PROP_BOOKSERIES);
    }

    public void setBookSeries(String bookSeries) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_BOOKSERIES, bookSeries);
    }

    /**
     * Property: publisher
     */
    public String getPublisher() {
        return (String) get_prop(PROP_PUBLISHER);
    }

    public void setPublisher(String publisher) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PUBLISHER, publisher);
    }

    /**
     * Property: publisherCity
     */
    public String getPublisherCity() {
        return (String) get_prop(PROP_PUBLISHERCITY);
    }

    public void setPublisherCity(String publisherCity) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PUBLISHERCITY, publisherCity);
    }

    /**
     * Property: isbn
     */
    public String getIsbn() {
        return (String) get_prop(PROP_ISBN);
    }

    public void setIsbn(String isbn) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ISBN, isbn);
    }

}
