package org.pimslims.model.experiment;

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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.pimslims.dao.WritableVersion;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "LabBookEntryID")
@Table(name = "EXPE_METHOD")
@org.pimslims.annotation.MetaClass(helpText = "Description of method or procedure, be it computational (calculation or processing) or real-world (e.g. purification).", keyNames = {}, subclasses = {})
public class Method extends org.pimslims.model.core.LabBookEntry implements java.lang.Comparable,
    java.io.Serializable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_NAME = "name";

    public static final String PROP_TASK = "task";

    public static final String PROP_PROCEDURE = "procedure";

    public static final String PROP_INSTRUMENT = "instrument";

    public static final String PROP_SOFTWARE = "software";

    public static final String PROP_PARAMETERS = "methodParameters";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @org.pimslims.annotation.EquivalentProperty("http://www.w3.org/2000/01/rdf-schema#label") @Column(name = "NAME", length = 80, unique = true, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Method name", constraints = { "contains_no_linebreak" })
    private String name;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "TASK", length = 80, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Operation caried out or result calculated e.g. 'Peak intensity', 'T1 fit', 'NMR processing', 'linear prediction', 'HPLC purification'.", constraints = { "contains_no_linebreak" })
    private String task;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "PROCEDURE_", columnDefinition = "TEXT", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Description of the procedure carried out. Typically in the form of a script executable by the relevant Software, but could be a more general description.")
    private String procedure;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "INSTRUMENTID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_method_instrument_inx")
    @org.pimslims.annotation.Role(helpText = "Instrument associated to a given method.", low = 0, high = 1, isChangeable = true, reverseRoleName = "methods")
    private Instrument instrument;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SOFTWAREID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "expe_method_software_inx")
    @org.pimslims.annotation.Role(helpText = "Software used to execute method. Method.procedure will frequently be a script directly executable by the Software.", low = 0, high = 1, isChangeable = true, reverseRoleName = "methods")
    private Software software;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "method")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "method")
    private final Set<MethodParameter> methodParameters = new HashSet<MethodParameter>(0);

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Method() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Method(WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Method(WritableVersion wVersion, java.util.Map<String, Object> attributes)
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
     * Property: task
     */
    public String getTask() {
        return (String) get_prop(PROP_TASK);
    }

    public void setTask(String task) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_TASK, task);
    }

    /**
     * Property: procedure
     */
    public String getProcedure() {
        return (String) get_prop(PROP_PROCEDURE);
    }

    public void setProcedure(String procedure) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PROCEDURE, procedure);
    }

    /**
     * Property: instrument
     */
    public Instrument getInstrument() {
        return (Instrument) get_prop(PROP_INSTRUMENT);
    }

    public void setInstrument(Instrument instrument) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INSTRUMENT, instrument);
    }

    /**
     * Property: software
     */
    public Software getSoftware() {
        return (Software) get_prop(PROP_SOFTWARE);
    }

    public void setSoftware(Software software) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_SOFTWARE, software);
    }

    /**
     * Property: parameters
     */
    public Set<MethodParameter> getMethodParameters() {
        return (Set<MethodParameter>) get_prop(PROP_PARAMETERS);
    }

    public void setMethodParameters(java.util.Collection<MethodParameter> parameters)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_PARAMETERS, parameters);
    }

    public void addParameter(MethodParameter parameter) throws org.pimslims.exception.ConstraintException {
        add(PROP_PARAMETERS, parameter);
    }

    public void removeParameter(MethodParameter parameter) throws org.pimslims.exception.ConstraintException {
        remove(PROP_PARAMETERS, parameter);
    }

}
