/**
 * Protein Information Management System - PiMS project
 * 
 * @see http://www.pims-lims.org
 * @version: 2.3
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
package org.pimslims.model.core;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.pimslims.annotation.Role;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.people.Person;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CORE_LABBOOKENTRY", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "Lab book entry.", keyNames = {}, subclasses = {
    // Thanks to keep this list of classes grouped by package and 
    // ordered alphabetically by package name
    org.pimslims.model.core.Bookmark.class, org.pimslims.model.crystallization.CyParameterDefinition.class,
    org.pimslims.model.crystallization.Image.class,
    org.pimslims.model.crystallization.CyParameter.class,
    org.pimslims.model.crystallization.DropAnnotation.class,
    org.pimslims.model.crystallization.Score.class,
    org.pimslims.model.crystallization.ScoringScheme.class,
    org.pimslims.model.crystallization.WellImageType.class, // no, this was a mistake
    org.pimslims.model.experiment.ExperimentGroup.class, org.pimslims.model.experiment.Experiment.class,
    org.pimslims.model.experiment.InputSample.class, org.pimslims.model.experiment.Parameter.class,
    org.pimslims.model.experiment.OutputSample.class,
    org.pimslims.model.experiment.Software.class,
    org.pimslims.model.experiment.Method.class,
    org.pimslims.model.experiment.MethodParameter.class,
    org.pimslims.model.experiment.Instrument.class,

    org.pimslims.model.holder.AbstractHolder.class,
    org.pimslims.model.holder.HolderLocation.class,
    org.pimslims.model.holder.RefSamplePosition.class,
    org.pimslims.model.holder.HolderTypePosition.class,
    org.pimslims.model.holder.RefHolderOffset.class,
    org.pimslims.model.holder.RefHolderSource.class,

    //org.pimslims.model.location.Location.class,

    org.pimslims.model.molecule.MoleculeFeature.class, org.pimslims.model.molecule.Substance.class,

    org.pimslims.model.people.Group.class, org.pimslims.model.people.PersonInGroup.class,
    org.pimslims.model.people.Organisation.class, org.pimslims.model.people.Person.class,

    org.pimslims.model.protocol.Protocol.class, org.pimslims.model.protocol.RefOutputSample.class,
    org.pimslims.model.protocol.RefInputSample.class, org.pimslims.model.protocol.ParameterDefinition.class,
    org.pimslims.model.protocol.Workflow.class,

    org.pimslims.model.sample.SampleComponent.class, org.pimslims.model.sample.AbstractSample.class,
    org.pimslims.model.sample.ReagentCatalogueEntry.class,

    org.pimslims.model.schedule.SchedulePlanOffset.class, org.pimslims.model.schedule.SchedulePlan.class,
    org.pimslims.model.schedule.ScheduledTask.class,

    org.pimslims.model.target.Target.class, org.pimslims.model.target.Alias.class,
    org.pimslims.model.target.Milestone.class, org.pimslims.model.target.TargetGroup.class,
    // org.pimslims.model.target.Project.class, 
    org.pimslims.model.target.ResearchObjectiveElement.class,
    org.pimslims.model.target.ResearchObjective.class, org.pimslims.model.target.SimilarityHit.class

})
public abstract class LabBookEntry extends org.pimslims.metamodel.AbstractModelObject implements
    java.lang.Comparable, java.io.Serializable, org.pimslims.metamodel.ControlledAccess {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_DBID = "dbId";

    public static final String PROP_DETAILS = "details";

    public static final String PROP_PAGE_NUMBER = "pageNumber";

    public static final String PROP_CREATIONDATE = "creationDate";

    public static final String PROP_LASTEDITEDDATE = "lastEditedDate";

    public static final String PROP_CREATOR = "creator";

    public static final String PROP_LASTEDITOR = "lastEditor";

    public static final String PROP_ATTACHMENTS = "attachments";

    public static final String PROP_ACCESS = "access";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Id
    @Basic(optional = false)
    @Column(name = "DBID", unique = false, nullable = false)
    @org.pimslims.annotation.Attribute(helpText = "")
    private Long dbId;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#comment")
    @Column(name = "DETAILS", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Details field for comments.")
    private String details;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PAGENUMBER", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Cross-reference to paper notebook")
    private String pageNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CREATIONDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.SubPropertyOf("http://www.w3.org/ns/prov#generatedAtTime")
    @org.pimslims.annotation.EquivalentProperty("http://purl.org/dc/terms/created")
    @org.pimslims.annotation.Attribute(helpText = "Date lab book entry was created.", isChangeable = false)
    private final Calendar creationDate = Calendar.getInstance();

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "LASTEDITEDDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "Last date lab book entry was edited.", isChangeable = false)
    @org.pimslims.annotation.SubPropertyOf("http://purl.org/dc/terms/modified")
    private Calendar lastEditedDate;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CREATORID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "core_labbookentry_creator_inx")
    @org.pimslims.annotation.SuperProperties({
        @org.pimslims.annotation.SubPropertyOf("http://www.w3.org/ns/prov#wasAttributedTo"),
        @org.pimslims.annotation.SubPropertyOf("http://purl.org/dc/terms/creator") })
    @org.pimslims.annotation.Role(helpText = "The user who created this entry.", low = 0, high = 1, isChangeable = true)
    private User creator;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LASTEDITORID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "core_labbookentry_le_inx")
    @org.pimslims.annotation.SuperProperties({
        @org.pimslims.annotation.SubPropertyOf("http://www.w3.org/ns/prov#wasAttributedTo"),
        @org.pimslims.annotation.SubPropertyOf("http://purl.org/dc/terms/contributor") })
    @org.pimslims.annotation.Role(helpText = "Person who last modified Entry.", low = 0, high = 1, isChangeable = true)
    private User lastEditor;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentEntry")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "parentEntry")
    private final Set<Attachment> attachments = new HashSet<Attachment>(0);

    /* ------------------------------------------------------------ */
    /* It looks like this should be EAGER, since it is always needed.
     * But when I tried that it seemed to cause more I/O, not less.
     * If you decide to change it, test the benefits __Chris
     * */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ACCESSID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "core_labbookentry_access_inx")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = 1, isChangeable = true, reverseRoleName = "labBookEntries")
    private LabNotebook access;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected LabBookEntry() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected LabBookEntry(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
        this.creator = wVersion.getCurrentUser(); //TODO creation date
        this.setAccess(wVersion.getCurrentDefaultOwner());
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: dbId
     */
    @Override
    public Long getDbId() {
        return this.dbId;
    }

    @Override
    protected void setDbId(final Long dbId) {
        this.dbId = dbId;
    }

    /**
     * Property: details
     */
    public String getDetails() {
        return (String) get_prop(PROP_DETAILS);
    }

    public void setDetails(final String details) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DETAILS, details);
    }

    /**
     * Property: details
     */
    public String getPageNumber() {
        return (String) get_prop(PROP_PAGE_NUMBER);
    }

    public void setPageNumber(final String pageNumber) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PAGE_NUMBER, pageNumber);
    }

    /**
     * Property: creationDate
     */
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    @Deprecated
    public void setCreationDate(final Calendar creationDate)
        throws org.pimslims.exception.ConstraintException {
        throw new ConstraintException("May not change Creation Date");
    }

    /**
     * Property: lastEditedDate
     */
    public Calendar getLastEditedDate() {
        return this.lastEditedDate;
    }

    @Deprecated
    public void setLastEditedDate(final Calendar lastEditedDate)
        throws org.pimslims.exception.ConstraintException {
        throw new ConstraintException("Last Edited Date is set automatically");
    }

    /**
     * Property: creator
     */
    public User getCreator() {
        return (User) get_prop(PROP_CREATOR);
    }

    @Deprecated
    public void setCreator(final User creator) throws org.pimslims.exception.ConstraintException {
        throw new ConstraintException("Creator cannot be changed");
    }

    /**
     * Property: lastEditor
     */
    public User getLastEditor() {
        return (User) get_prop(PROP_LASTEDITOR);
    }

    @Deprecated
    public void setLastEditor(final User lastEditor) throws org.pimslims.exception.ConstraintException {
        throw new ConstraintException("Last Editor is set automatically");
    }

    /**
     * Property: attachments
     */
    public Set<Attachment> getAttachments() {
        return (Set<Attachment>) get_prop(PROP_ATTACHMENTS);
    }

    public void setAttachments(final java.util.Collection<Attachment> attachments)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ATTACHMENTS, attachments);
    }

    public void addAttachment(final Attachment attachments) throws org.pimslims.exception.ConstraintException {
        add(PROP_ATTACHMENTS, attachments);
    }

    public void removeAttachment(final Attachment attachments)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_ATTACHMENTS, attachments);
    }

    /**
     * Property: access
     */
    @Override
    public LabNotebook getAccess() {
        return (LabNotebook) get_prop(PROP_ACCESS);
    }

    public void setAccess(final LabNotebook access) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ACCESS, access);
    }

    /* ************************************************************ */
    /* EXTRA METHODS                                                */
    /* ************************************************************ */
    /**
     * Derived method: getAnnotations()
     */
    @Role(helpText = "The list of annotations related to this entry.")
    public Set<Annotation> getAnnotations() {
        final Set<Annotation> results = new HashSet<Annotation>(0);
        for (final Attachment element : getAttachments()) {
            if (element instanceof Annotation) {
                results.add((Annotation) element);
            }
        }
        return results;
    }

    /**
     * Derived method: getCitations()
     */
    @Role(helpText = "The list of citations related to this entry.")
    public Set<Citation> getCitations() {
        final Set<Citation> results = new HashSet<Citation>(0);
        for (final Attachment element : getAttachments()) {
            if (element instanceof Citation) {
                results.add((Citation) element);
            }
        }
        return results;
    }

    /**
     * Derived method: getExternalDbLinks()
     */
    @Role(helpText = "The list of external database links related to this entry.")
    public Set<ExternalDbLink> getExternalDbLinks() {
        final Set<ExternalDbLink> results = new HashSet<ExternalDbLink>(0);
        for (final Attachment element : getAttachments()) {
            if (element instanceof ExternalDbLink) {
                results.add((ExternalDbLink) element);
            }
        }
        return results;
    }

    /**
     * Derived method: getNotes()
     */
    @Role(helpText = "The list of notes related to this entry.")
    public List<Note> getNotes() {
        final List<Note> results = new LinkedList<Note>();
        for (final Attachment element : getAttachments()) {
            if (element instanceof Note) {
                results.add((Note) element);
            }
        }
        final Comparator<? super Note> comparator = new NoteComparator<Note>();
        Collections.sort(results, comparator);
        return results;
    }

    /**
     * Derived method: getCreatorPerson()
     */
    @Deprecated
    // Person is obsolete
    public Person getCreatorPerson() {
        if (this.creator == null) {
            return null;
        }
        return this.creator.getPerson();
    }

    /**
     * LabBookEntry.getName
     * 
     * @see org.pimslims.metamodel.AbstractModelObject#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * LabBookEntry.set_prop
     * 
     * @see org.pimslims.metamodel.AbstractModelObject#set_prop(java.lang.String, java.lang.Object)
     */
    @Override
    protected void set_prop(String prop_name, Object value) throws ConstraintException {
        //setLastEditedDate &&  setLastEditor
        if (!isInit && LabBookEntry.class.isAssignableFrom(this.getClass())
            && !prop_name.equals(LabBookEntry.PROP_LASTEDITEDDATE)
            && !prop_name.equals(LabBookEntry.PROP_LASTEDITOR) && !prop_name.equals(LabBookEntry.PROP_ACCESS)) {
            super.set_prop(LabBookEntry.PROP_LASTEDITEDDATE, Calendar.getInstance());
            super.set_prop(LabBookEntry.PROP_LASTEDITOR, this.get_Version().getCurrentUser());

            // could also check whether it is locked - see Experiment
        }
        super.set_prop(prop_name, value);
    }

}
