package org.pimslims.model.target;

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

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.ExternalDbLink;
import org.pimslims.model.core.LabBookEntry;

/**
 * @author Anne Pajon
 * @date Jun 16, 2009
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
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "TARG_SIMILARITYHIT")
@org.pimslims.annotation.MetaClass(helpText = "The list of similarity hits between a given target and an external database link.", keyNames = {}, subclasses = {}, parent = org.pimslims.model.target.Target.class, parentRoleName = "target")
public class SimilarityHit extends LabBookEntry {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_TARGET = "target";

    public static final String PROP_EXTERNALDBLINK = "externalDbLink";

    public static final String PROP_EVALUE = "eValue";

    public static final String PROP_SIMILARITYPERCENTAGE = "similarityPercentage";

    public static final String PROP_DESCRIPTION = "description";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TARGETID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "targ_similarityhit_target_inx")
    @org.pimslims.annotation.Role(helpText = "The given target.", low = 1, high = 1, isChangeable = true, reverseRoleName = "similarityHits")
    private Target target;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ATTACHMENTID", unique = false, nullable = false)
    @org.hibernate.annotations.Index(name = "targ_similarityhit_edbl_inx")
    @org.pimslims.annotation.Role(helpText = "The external database link.", low = 1, high = 1, isChangeable = true, reverseRoleName = "similarityHits")
    private ExternalDbLink externalDbLink;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "EVALUE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The eValue.")
    private Float eValue;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SIMILARITYPERCENTAGE", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The percentage of similarity.")
    private Float similarityPercentage;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The description is an human-readable description for the similarity hit.")
    private String description;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected SimilarityHit() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected SimilarityHit(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public SimilarityHit(WritableVersion wVersion, Target target, ExternalDbLink externalDbLink)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(PROP_TARGET, target);
        attributes.put(PROP_EXTERNALDBLINK, externalDbLink);
        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public SimilarityHit(WritableVersion wVersion, java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */

    /**
     * @return Returns the target.
     */
    public Target getTarget() {
        return (Target) get_prop(PROP_TARGET);
    }

    /**
     * @param target The target to set.
     * @throws ConstraintException
     */
    public void setTarget(Target target) throws ConstraintException {
        set_prop(PROP_TARGET, target);
    }

    /**
     * @return Returns the externalDbLink.
     */
    public ExternalDbLink getExternalDbLink() {
        return (ExternalDbLink) get_prop(PROP_EXTERNALDBLINK);
    }

    /**
     * @param externalDbLink The externalDbLink to set.
     * @throws ConstraintException
     */
    public void setExternalDbLink(ExternalDbLink externalDbLink) throws ConstraintException {
        set_prop(PROP_EXTERNALDBLINK, externalDbLink);
    }

    /**
     * @return Returns the eValue.
     */
    public Float getEValue() {
        return (Float) get_prop(PROP_EVALUE);
    }

    /**
     * @param value The eValue to set.
     * @throws ConstraintException
     */
    public void setEValue(Float value) throws ConstraintException {
        set_prop(PROP_EVALUE, value);
    }

    /**
     * @return Returns the similarityPercentage.
     */
    public Float getSimilarityPercentage() {
        return (Float) get_prop(PROP_SIMILARITYPERCENTAGE);
    }

    /**
     * @param similarityPercentage The similarityPercentage to set.
     * @throws ConstraintException
     */
    public void setSimilarityPercentage(Float similarityPercentage) throws ConstraintException {
        set_prop(PROP_SIMILARITYPERCENTAGE, similarityPercentage);
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return (String) get_prop(PROP_DESCRIPTION);
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */

    /**
     * SimilarityHit.getName
     * 
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        return this.target.getName() + ": " + this.similarityPercentage + "with "
            + this.externalDbLink.getName();
    }

}
