package org.pimslims.model.sample;

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

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.crystallization.DropAnnotation;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Container;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "ABSTRACTSAMPLEID")
@org.pimslims.annotation.EquivalentClass("http://www.w3.org/ns/prov#Entity")
@org.pimslims.annotation.SuperClasses({
    @org.pimslims.annotation.SubClassOf("http://purl.org/dc/dcmitype/PhysicalObject"),
    @org.pimslims.annotation.SubClassOf("http://purl.obolibrary.org/obo/BFO_0000040") // material entity
})
@Table(name = "SAM_SAMPLE", uniqueConstraints = { @UniqueConstraint(columnNames = {}) })
@org.pimslims.annotation.MetaClass(helpText = "The information on the sample. "
    + "The sample conforms to a particular sample reference information. "
    + "The sample is the contents that has been used during an experiment that "
    + "contains the sample components which is located in an Holder."
    + "Row, column and subposition cover all the needs for crystallization trial plates."
    + "The example below describes a single physical well in a plate for a vapour diffusion"
    + "crystallization trial. Three different protein concentrations are being screened against"
    + "one screen condition. The different protein concentrations are obtained by mixing protein "
    + "sample and screen condition in different ratios. The well is A01. There is one reservoir "
    + "subposition (A01.1) and three trial subpositions (A01.2, A01.3 and A01.4): "
    + "A01.1 := 95ul reservoir of screen condition; A01.2 := (100nl Protein + 100nl Screen condition); A01.3 := (200nl Protein + 100nl Screen condition); A01.4 := (100nl Protein + 200nl Screen condition)", keyNames = {},

// obsolete
subclasses = { org.pimslims.model.sample.CrystalSample.class })
public class Sample extends org.pimslims.model.sample.AbstractSample implements java.lang.Comparable,
    java.io.Serializable, Containable {

    /* ************************************************************ */
    /* CONSTANTS                                                    */
    /* ************************************************************ */
    public static final String PROP_ROWPOSITION = "rowPosition";

    public static final String PROP_COLPOSITION = "colPosition";

    public static final String PROP_SUBPOSITION = "subPosition";

    public static final String PROP_INITIALAMOUNT = "initialAmount";

    public static final String PROP_CURRENTAMOUNT = "currentAmount";

    public static final String PROP_AMOUNTUNIT = "amountUnit";

    public static final String PROP_AMOUNTDISPLAYUNIT = "amountDisplayUnit";

    public static final String PROP_CURRENTAMOUNTFLAG = "currentAmountFlag";

    public static final String PROP_BATCHNUM = "batchNum";

    public static final String PROP_INVOICENUMBER = "invoiceNumber";

    public static final String PROP_USEBYDATE = "useByDate";

    public static final String PROP_ASSIGNTO = "assignTo";

    public static final String PROP_HOLDER = "holder";

    public static final String PROP_OUTPUTSAMPLE = "outputSample";

    public static final String PROP_REFSAMPLE = "refSample";

    public static final String PROP_DROPANNOTATIONS = "dropAnnotations";

    @Deprecated
    // can be big
    public static final String PROP_IMAGES = "images";

    public static final String PROP_INPUTSAMPLES = "inputSamples";

    public static final String PROP_CONCENTRATION = "concentration";

    // could public static final String PROP_CONCENTRATIONERROR = "concentrationError";

    public static final String PROP_CONCENTRATIONUNIT = "concentrationUnit";

    public static final String PROP_CONCDISPLAYUNIT = "concDisplayUnit";

    /* ************************************************************ */
    /* INSTANCE VARIABLES                                           */
    /* ************************************************************ */
    private static final long serialVersionUID = 1L;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "ROWPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The row position of the sample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer rowPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "COLPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The column position of the sample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer colPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "SUBPOSITION", columnDefinition = "INTEGER", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The sub-position of the sample in the holder. The first position should be 1.", constraints = { "int_value_gt_0" })
    private Integer subPosition;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "INITIALAMOUNT", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The initial amount of the sample at the creation date.")
    private Float initialAmount;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CURRENTAMOUNT", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The current amount of the sample. Can be deduced by a subtraction between the initial amount and all the amount which are implied in experiments.")
    private Float currentAmount;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNTUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "One of the three possible units: kg, m3 or number.", constraints = { "value_must_be_one_of('kg', 'L', 'number')" })
    private String amountUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "AMOUNTDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String amountDisplayUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CURRENTAMOUNTFLAG", columnDefinition = "BOOLEAN", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Was current amount validated?")
    private Boolean currentAmountFlag;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "BATCHNUM", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the batch or lot number of the sample.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String batchNum;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "INVOICENUMBER", length = 1024, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The id of the invoice in your accounting system, if you have billed a client for this sample.", constraints = { "contains_no_linebreak" })
    private String invoiceNumber;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "USEBYDATE", columnDefinition = "TIMESTAMPTZ", unique = false, nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @org.pimslims.annotation.Attribute(helpText = "The sample shelf life.")
    private Calendar useByDate;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ASSIGNTOID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sam_sample_assignto_inx")
    @org.pimslims.annotation.Role(helpText = "assigned user", low = 0, high = 1, isChangeable = true, reverseRoleName = "samples")
    private User assignTo;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "HOLDERID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sam_sample_holder_inx")
    @org.pimslims.annotation.Role(helpText = "The holder to which the samples belong.", low = 0, high = 1, isChangeable = true, reverseRoleName = "samples")
    private Holder holder;

    /* ------------------------------------------------------------ */
    @OneToOne(mappedBy = "sample", fetch = FetchType.EAGER)
    @org.pimslims.annotation.Role(helpText = "Output sample list that describes the amount of sample created by that experiment for a given sample.", low = 0, high = 1, isChangeable = true, reverseRoleName = "sample")
    private OutputSample outputSample;

    /* ------------------------------------------------------------ */
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "REFSAMPLEID", unique = false, nullable = true)
    @org.hibernate.annotations.Index(name = "sam_sample_refsample_inx")
    @org.pimslims.annotation.Role(helpText = "The RefSample that conforms to the Sample.", low = 0, high = 1, isChangeable = true, reverseRoleName = "conformings")
    private RefSample refSample;

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "sample")
    private final Set<DropAnnotation> dropAnnotations = new HashSet<DropAnnotation>(0);

    /* ------------------------------------------------------------ 
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
    @org.pimslims.annotation.Role(helpText = "N/A", low = 0, high = -1, isChangeable = true, reverseRoleName = "sample")
    private final Set<Image> images = new HashSet<Image>(0); */

    /* ------------------------------------------------------------ */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sample")
    @org.pimslims.annotation.Role(helpText = "Input sample list that describes the amount of sample used per experiment for a given sample.", low = 0, high = -1, isChangeable = true, reverseRoleName = "sample")
    private final Set<InputSample> inputSamples = new HashSet<InputSample>(0);

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCENTRATION", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "Concentration of the component in the sample.")
    private Float concentration;

    /* ------------------------------------------------------------  
    @Basic(optional = true)
    @Column(name = "CONCENTRATIONERROR", columnDefinition = "FLOAT8", unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The concentration error.")
    private Float concentrationError; */

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCENTRATIONUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "The concentration unit that should be one of these units: kg/m3 for mass density, mol/m3 for amount-of-substance concentration, m3/m3 for volume fraction, mol/mol for amount of substance fraction and kg/kg for mass fraction.", constraints = { "value_must_be_one_of('kg/m3', 'M', 'm3/m3', 'mol/mol', 'kg/kg')" })
    private String concentrationUnit;

    /* ------------------------------------------------------------ */
    @Basic(optional = true)
    @Column(name = "CONCDISPLAYUNIT", length = 32, unique = false, nullable = true)
    @org.pimslims.annotation.Attribute(helpText = "This is the unit entered by user and used for display.", constraints = {
        "contains_no_linebreak", "no_white_space" })
    private String concDisplayUnit;

    /* ************************************************************ */
    /* CONSTRUCTORS                                                 */
    /* ************************************************************ */
    /**
     * Empty constructor
     */
    protected Sample() {
        super();
    }

    /**
     * Constructor for PIMS WritableVersion
     */
    protected Sample(final WritableVersion wVersion) throws org.pimslims.exception.ConstraintException {
        super(wVersion);
    }

    /**
     * Constructor for required arguments
     */
    public Sample(final WritableVersion wVersion, final java.lang.String name)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        final java.util.Map<String, Object> attributes = new java.util.HashMap();
        attributes.put(PROP_NAME, name);

        init(attributes);
    }

    /**
     * Constructor with argument [name, value] in a map
     */
    public Sample(final WritableVersion wVersion, final java.util.Map<String, Object> attributes)
        throws org.pimslims.exception.ConstraintException {
        this(wVersion);
        init(attributes);
    }

    /* ************************************************************ */
    /* PROPERTY ACCESSORS                                           */
    /* ************************************************************ */
    /**
     * Property: rowPosition
     */
    public Integer getRowPosition() {
        return (Integer) get_prop(PROP_ROWPOSITION);
    }

    public void setRowPosition(final Integer rowPosition) throws org.pimslims.exception.ConstraintException {
        HolderType holderType = getHolderType();
        if (null != holderType) {
            Integer max = holderType.getMaxRow();
            if (null != rowPosition && null != max && max < rowPosition) {
                throw new ConstraintException(this, PROP_ROWPOSITION, rowPosition, "Maximum is: " + max);
            }
        }
        set_prop(PROP_ROWPOSITION, rowPosition);
    }

    public HolderType getHolderType() {
        if (null != this.getHolder() && null != this.getHolder().getHolderType()
            && this.getHolder().getHolderType() instanceof HolderType) {
            return (HolderType) this.getHolder().getHolderType();
        }
        return null;
    }

    /**
     * Property: colPosition
     */
    public Integer getColPosition() {
        return (Integer) get_prop(PROP_COLPOSITION);
    }

    public void setColPosition(final Integer colPosition) throws org.pimslims.exception.ConstraintException {
        HolderType holderType = getHolderType();
        if (null != holderType) {
            Integer max = holderType.getMaxColumn();
            if (null != colPosition && null != max && max < colPosition) {
                throw new ConstraintException(this, PROP_COLPOSITION, colPosition, "Maximum is: " + max);
            }
        }
        set_prop(PROP_COLPOSITION, colPosition);
    }

    /**
     * Property: subPosition
     */
    public Integer getSubPosition() {
        return (Integer) get_prop(PROP_SUBPOSITION);
    }

    public void setSubPosition(final Integer subPosition) throws org.pimslims.exception.ConstraintException {
        HolderType holderType = getHolderType();
        if (null != holderType) {
            Integer max = holderType.getMaxSubPosition();
            if (null != subPosition && null != max && max < subPosition) {
                throw new ConstraintException(this, PROP_SUBPOSITION, subPosition, "Maximum is: " + max);
            }
        }
        set_prop(PROP_SUBPOSITION, subPosition);
    }

    /**
     * Property: initialAmount
     */
    public Float getInitialAmount() {
        return (Float) get_prop(PROP_INITIALAMOUNT);
    }

    public void setInitialAmount(final Float initialAmount) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INITIALAMOUNT, initialAmount);
    }

    /**
     * Property: currentAmount
     */
    public Float getCurrentAmount() {
        return (Float) get_prop(PROP_CURRENTAMOUNT);
    }

    public void setCurrentAmount(final Float currentAmount) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CURRENTAMOUNT, currentAmount);
    }

    /**
     * Property: amountUnit
     */
    public String getAmountUnit() {
        return (String) get_prop(PROP_AMOUNTUNIT);
    }

    public void setAmountUnit(final String amountUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNTUNIT, amountUnit);
    }

    /**
     * Property: amountDisplayUnit
     */
    public String getAmountDisplayUnit() {
        return (String) get_prop(PROP_AMOUNTDISPLAYUNIT);
    }

    public void setAmountDisplayUnit(final String amountDisplayUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_AMOUNTDISPLAYUNIT, amountDisplayUnit);
    }

    /**
     * Property: currentAmountFlag
     */
    public Boolean getCurrentAmountFlag() {
        return (Boolean) get_prop(PROP_CURRENTAMOUNTFLAG);
    }

    public void setCurrentAmountFlag(final Boolean currentAmountFlag)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CURRENTAMOUNTFLAG, currentAmountFlag);
    }

    /**
     * Property: batchNum
     */
    public String getBatchNum() {
        return (String) get_prop(PROP_BATCHNUM);
    }

    public void setBatchNum(final String batchNum) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_BATCHNUM, batchNum);
    }

    /**
     * Property: useByDate
     */
    public Calendar getUseByDate() {
        return (Calendar) get_prop(PROP_USEBYDATE);
    }

    public void setUseByDate(final Calendar useByDate) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_USEBYDATE, useByDate);
    }

    /**
     * Property: assignTo
     */
    public User getAssignTo() {
        return (User) get_prop(PROP_ASSIGNTO);
    }

    public void setAssignTo(final User assignTo) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_ASSIGNTO, assignTo);
    }

    /**
     * Property: holder
     */
    public Holder getHolder() {
        return (Holder) get_prop(PROP_HOLDER);
    }

    public void setHolder(final Holder holder) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_HOLDER, holder);
    }

    /**
     * Property: outputSample
     */
    public OutputSample getOutputSample() {
        return (OutputSample) get_prop(PROP_OUTPUTSAMPLE);
    }

    public void setOutputSample(final OutputSample outputSample)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_OUTPUTSAMPLE, outputSample);
    }

    /**
     * Property: refSample
     */
    public RefSample getRefSample() {
        return (RefSample) get_prop(PROP_REFSAMPLE);
    }

    public void setRefSample(final RefSample refSample) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_REFSAMPLE, refSample);
    }

    /**
     * Property: dropAnnotations
     */
    public Set<DropAnnotation> getDropAnnotations() {
        return (Set<DropAnnotation>) get_prop(PROP_DROPANNOTATIONS);
    }

    public void setDropAnnotations(final java.util.Collection<DropAnnotation> dropAnnotations)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_DROPANNOTATIONS, dropAnnotations);
    }

    public void addDropAnnotation(final DropAnnotation dropAnnotation)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_DROPANNOTATIONS, dropAnnotation);
    }

    public void removeDropAnnotation(final DropAnnotation dropAnnotation)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_DROPANNOTATIONS, dropAnnotation);
    }

    /**
     * Property: images
     */
    public Set<Image> getImages() {
        return (Set<Image>) get_prop(PROP_IMAGES);
    }

    public void setImages(final java.util.Collection<Image> images)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_IMAGES, images);
    }

    public void addImage(final Image image) throws org.pimslims.exception.ConstraintException {
        add(PROP_IMAGES, image);
    }

    public void removeImage(final Image image) throws org.pimslims.exception.ConstraintException {
        remove(PROP_IMAGES, image);
    }

    /**
     * Property: inputSamples
     */
    public Set<InputSample> getInputSamples() {
        return (Set<InputSample>) get_prop(PROP_INPUTSAMPLES);
    }

    public void setInputSamples(final java.util.Collection<InputSample> inputSamples)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_INPUTSAMPLES, inputSamples);
    }

    public void addInputSample(final InputSample inputSample)
        throws org.pimslims.exception.ConstraintException {
        add(PROP_INPUTSAMPLES, inputSample);
    }

    public void removeInputSample(final InputSample inputSample)
        throws org.pimslims.exception.ConstraintException {
        remove(PROP_INPUTSAMPLES, inputSample);
    }

    /**
     * Sample.getInvoiceNumber
     * 
     * @return
     */
    public String getInvoiceNumber() {
        return (String) this.get_prop(PROP_INVOICENUMBER);
    }

    /**
     * Sample.setInvoiceNumber
     * 
     * @param unique
     * @throws ConstraintException
     */
    public void setInvoiceNumber(String invoiceId) throws ConstraintException {
        this.set_prop(PROP_INVOICENUMBER, invoiceId);
    }

    /**
     * Containable.getContainer
     * 
     * @see org.pimslims.model.holder.Containable#getContainer()
     */
    public Container getContainer() {
        return this.getHolder();
    }

    @Deprecated
    // use getContainer()
    Location getCurrentLocation() {
        final Holder holder = this.getHolder();
        if (null == holder) {
            return null;
        }
        return holder.getCurrentLocation();
    }

    /**
     * Containable.setContainer
     * 
     * @see org.pimslims.model.holder.Containable#setContainer(org.pimslims.model.holder.Container)
     */
    public void setContainer(Container container) throws ConstraintException {
        this.setHolder((Holder) container);
    }

    public static final String[] ROWS = new String[] { "A", "B", "C", "D", // 24 well
        "E", "F", "G", "H", // 96 well
        "I", "J", "K", "L", "M", "N", "O", "P", // 384 well plate
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", // 1536 well plate
    };

    /**
     * Sample.setRowPosition
     * 
     * @param position well, e.g. "C4", "A01", "AF24"
     * @throws ConstraintException
     */
    public void setRowPosition(String position) throws ConstraintException {
        String row = position.replaceAll("\\d", "");
        int index = Arrays.asList(ROWS).indexOf(row);
        if (-1 == index) {
            throw new ConstraintException(this, PROP_ROWPOSITION, position, "Unknown row");
        }
        this.setRowPosition(index);
    }

    /**
     * Property: concentration
     */
    public Float getConcentration() {
        return (Float) get_prop(PROP_CONCENTRATION);
    }

    public void setConcentration(Float concentration) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCENTRATION, concentration);
    }

    /**
     * Property: concentrationError
     * 
     * public Float getConcentrationError() { return (Float) get_prop(PROP_CONCENTRATIONERROR); }
     * 
     * public void setConcentrationError(Float concentrationError) throws
     * org.pimslims.exception.ConstraintException { set_prop(PROP_CONCENTRATIONERROR, concentrationError); }
     */

    /**
     * Property: concentrationUnit
     */
    public String getConcentrationUnit() {
        return (String) get_prop(PROP_CONCENTRATIONUNIT);
    }

    public void setConcentrationUnit(String concentrationUnit)
        throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCENTRATIONUNIT, concentrationUnit);
    }

    /**
     * Property: concDisplayUnit
     */
    public String getConcDisplayUnit() {
        return (String) get_prop(PROP_CONCDISPLAYUNIT);
    }

    public void setConcDisplayUnit(String concDisplayUnit) throws org.pimslims.exception.ConstraintException {
        set_prop(PROP_CONCDISPLAYUNIT, concDisplayUnit);
    }

    /**
     * Sample.getSequence
     * 
     * @return
     */
    public String getSequence() {
        Set<SampleComponent> components = this.getSampleComponents();
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SampleComponent sampleComponent = (SampleComponent) iterator.next();
            Substance substance = sampleComponent.getRefComponent();
            if (substance instanceof Molecule) {
                String sequence = ((Molecule) substance).getSequence();
                if (null != sequence) {
                    return sequence;
                }
            }
        }
        return null;
    }

    /**
     * Sample.setSequence
     * 
     * @param sequence
     * @throws ConstraintException
     */
    public void setSequence(String sequence) throws ConstraintException {
        Set<SampleComponent> components = this.getSampleComponents();
        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            SampleComponent sampleComponent = (SampleComponent) iterator.next();
            if (sampleComponent.getRefComponent() instanceof Molecule) {
                Molecule molecule = (Molecule) sampleComponent.getRefComponent();
                if (Molecule.DNA.equals(molecule.getMolType())
                    || Molecule.PROTEIN.equals(molecule.getMolType())) {
                    molecule.setSequence(sequence);
                    return;
                }
            }
        }

        String molType = Molecule.DNA;
        Set<SampleCategory> categories = this.getSampleCategories();
        for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
            SampleCategory sampleCategory = (SampleCategory) iterator.next();
            if (sampleCategory.getName().toLowerCase().contains("protein")) {
                molType = Molecule.PROTEIN;
            }
        }
        Molecule molecule = new Molecule((WritableVersion) this.get_Version(), molType, this.getName());
        molecule.setSequence(sequence);
        new SampleComponent((WritableVersion) this.get_Version(), molecule, this);
    }

}
