package org.pimslims.presentation.leeds;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.PropertyResourceBundle;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.PersonUtility;
import org.pimslims.lab.Util;
import org.pimslims.lab.Utils;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.leeds.LeedsFormServletUtils;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PrimerBeanWriter;

/**
 * A bean, representing the properties of a Leeds construct
 * 
 * @author Petr Troshin aka pvt43
 */
@Deprecated
// Leeds functionality is not longer supported
public abstract class AbstractConstructBean {

    // Name 1
    // Field value: pMPSIL0296A
    String name;

    // Field name: comments
    /*
     * Field value: Target ID MPSIL0296, Description nptA protein, Substrate sodium and phosphate Source
     * Vibrio cholerae O1 biovar eltor str. N16961 Gene name nptA Protein accession NP_230325 Nucleotide
     * accession NC_002505 REGION: complement (722512..723660) Cloned in pTTQ18RGSH6
     */
    String comments;

    // For EntryClone
    // Field name: marker
    // Field value: Amp
    private String antibioticResistance;

    // Field name: date
    // Field value: 09/02/2005
    Date date;

    // Field name: designed by
    // Field value: Vincent Postis

    String designedBy;

    // Property of Sample
    // Field name: position1
    // Field value: F6
    // Field name: position2
    // Field value: F6
    String position1;

    // Barcode for position1
    String position1Barcode;

    String position2;

    // Field name: location1
    // Field value: Freezer MPSI 1
    // Field name: location2
    // Field value: Freezer MPSI 3
    String location1;

    String location2;

    // Field name: box1
    // Field value: 1
    // Field name: box2
    // Field value: A
    String box1;

    // Barcode for box1
    String box1Barcode;

    String box2;

    // clonesaver Vincent card A2
    // String holderCategory;

    //

    /*
     * Resources - names of Entry Clone & Expression Construct forms
     */
    private static PropertyResourceBundle entryCloneRb;

    private static PropertyResourceBundle deepFrozenCultureRb;

    private static void initializeResourceBundles() {
        InputStream in_stream = null;
        InputStream in2_stream = null;
        try {
            in_stream = AbstractConstructBean.class.getResourceAsStream("EntryClone_en.properties");
            AbstractConstructBean.entryCloneRb = new PropertyResourceBundle(in_stream);
            in2_stream = AbstractConstructBean.class.getResourceAsStream("DeepFrozenCulture_en.properties");
            AbstractConstructBean.deepFrozenCultureRb = new PropertyResourceBundle(in2_stream);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (in_stream != null) {
                try {
                    in_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
            if (in2_stream != null) {
                try {
                    in2_stream.close();
                } catch (final IOException isc) {
                    // do nothing
                }
            }
        }
    }

    public abstract Experiment record(WritableVersion rw) throws AccessException, ConstraintException,
        ClassNotFoundException;

    // Load categories from properties
    static final Map<String, String> prop = LeedsFormServletUtils.resourceToMap(AbstractConstructBean
        .getEntryCloneRb());

    static final Map<String, String> expprop = LeedsFormServletUtils.resourceToMap(AbstractConstructBean
        .getDeepFrozenCultureRb());

    public AbstractConstructBean() {/*
                                                                                                                                                             * This is to allow EntryCloneConstruct to be used as a bean
                                                                                                                                                             */
    }

    // Entry Clone construct
    public AbstractConstructBean(final String location1, final String box1, final String position1,
        final String location2, final String box2, final String position2, final String name,
        final String antibioticResistance, final String comments, final Date date, final String designedBy) {
        this.location1 = location1;
        this.box1 = box1;
        this.position1 = position1;
        this.location2 = location2;
        this.box2 = box2;
        this.position2 = position2;
        this.name = name;
        this.antibioticResistance = antibioticResistance;
        this.comments = comments;
        this.date = date;
        this.designedBy = designedBy;
    }

    String getLocationName(final Holder holder) {
        return ContainerUtility.getCurrentLocation(holder).getName(); // getLocation(holder).getName();
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return Utils.getDate(this.date);
    }

    /**
     * @return the designedBy
     */
    public String getDesignedBy() {
        return this.designedBy;
    }

    /**
     * TODO check uses - sometimes Marker is Entry Clone, sometimes it is antibiotic resistance
     * 
     * @return
     * 
     * @Deprecated public final String getMarker() { return this.marker; }
     */

    public final String getAntibioticResistance() {
        return this.antibioticResistance;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the box1
     */
    public String getBox1() {
        return this.box1;
    }

    /**
     * @return the box2
     */
    public String getBox2() {
        return this.box2;
    }

    /**
     * @return the location1
     */
    public String getLocation1() {
        return this.location1;
    }

    /**
     * @return the location2
     */
    public String getLocation2() {
        return this.location2;
    }

    /**
     * @return the position1
     */
    public String getPosition1() {
        return this.position1;
    }

    /**
     * @return the box1Barcode
     */
    public final String getBox1Barcode() {
        return this.box1Barcode;
    }

    /**
     * @param box1Barcode the box1Barcode to set
     */
    public final void setBox1Barcode(final String box1Barcode) {
        this.box1Barcode = box1Barcode;
    }

    /**
     * @return the position1Barcode
     */
    public final String getPosition1Barcode() {
        return this.position1Barcode;
    }

    /**
     * @param position1Barcode the position1Barcode to set
     */
    public final void setPosition1Barcode(final String position1Barcode) {
        this.position1Barcode = position1Barcode;
    }

    /**
     * @return the position2
     */
    public String getPosition2() {
        return this.position2;
    }

    @Override
    public String toString() {
        String plasmid = "";
        plasmid += "Location1: " + this.location1 + "\n";
        plasmid += "Box1: " + this.box1 + "\n";
        plasmid += "Position1: " + this.position1 + "\n";
        plasmid += "Location2 " + this.location2 + "\n";
        plasmid += "Box2: " + this.box2 + "\n";
        plasmid += "Position2: " + this.position2 + "\n";
        plasmid += "Name        " + this.name + "\n";
        plasmid += "resistence      " + this.getAntibioticResistance() + "\n";
        //plasmid += "entry clone     " + this.getEntryCloneHook() + "\n";
        plasmid += "comments    " + this.comments + "\n";
        plasmid += "designed by " + this.designedBy + "\n";
        plasmid += "Date        " + this.date + "\n";
        return plasmid;
    }

    static AbstractModel model = ModelImpl.getModel();

    /*
     * @return null if cannot tell familyname
     */
    static String getDesignerFamilyName(final String designedBy) {
        return PersonUtility.getPersonFamilyName(designedBy);
    }

    static String getDesignerName(final String designedBy) {
        return PersonUtility.getPersonName(designedBy);
    }

    static void setTarget(final Target target, final Experiment experiment) throws ConstraintException {
        TargetUtility.setTarget(target, experiment);
    }

    public void setLocation1(final String location1) throws ConstraintException, AccessException {
        this.location1 = location1;
    }

    public void setLocation2(final String location2) throws ConstraintException, AccessException {
        this.location2 = location2;
    }

    public void setBox1(final String box1) throws ConstraintException {
        this.box1 = box1;
    }

    public void setBox2(final String box2) throws ConstraintException {
        this.box2 = box2;
    }

    public void setPosition1(final String position1) throws ConstraintException {
        this.position1 = position1.toUpperCase();
    }

    public void setPosition2(final String position2) throws ConstraintException {
        this.position2 = position2.toUpperCase();
    }

    /**
     * @param designedBy the designedBy to set
     */
    public void setDesignedBy(final String designedBy) throws ConstraintException {
        this.designedBy = designedBy;
    }

    /**
     * 
     * @param marker the marker to set
     */
    public final void setAntibioticResistance(final String marker) throws ConstraintException {
        this.antibioticResistance = marker;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) throws ConstraintException {
        this.name = name;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(final String comments) throws ConstraintException {
        this.comments = comments;
    }

    /**
     * @param date the date to set
     */
    public void setDate(final String date) throws ConstraintException {
        this.date = AbstractConstructBean.parseDate(date);
    }

    public void setBarcodes(final Sample sample1, final Holder holder1) throws ConstraintException {
        if (!Util.isEmpty(this.position1Barcode)) {
            sample1.setBatchNum(this.position1Barcode);
        }
        if (holder1 != null && !Util.isEmpty(this.box1Barcode)) {
            holder1.setDetails(this.box1Barcode);
        }
    }

    /**
     * @param rw the current transaction
     * @param expType the experimen type
     * @param experimentName the name for the new record
     * @param comments details
     * @param date *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Experiment recordExperiment(final WritableVersion rw, final ModelObject expType,
        final String experimentName, final String comments, final Date date) throws AccessException,
        ConstraintException {
        return PrimerBeanWriter.recordExperiment(rw, expType, experimentName, null, comments, date);
    }

    /**
     * @param rw the current transaction
     * @param refSample
     * @param holder
     * @param name
     * @param position *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do thatn
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ConstraintException if the name is already in use
     */
    public static Sample recordSample(final WritableVersion rw, final SampleCategory plasmid,
        final Holder holder, final String name, String position) throws AccessException, ConstraintException {
        assert !"".equals(position) : "Position in holder not specified for: " + name;

        final Map<String, Object> sampleProp = Util.getNewMap();
        sampleProp.put(AbstractSample.PROP_NAME, name);
        if (holder != null) {
            sampleProp.put(Sample.PROP_HOLDER, holder);
        }
        if (position != null) {
            position = position.toUpperCase();
            final int cpos = HolderFactory.getColumn(position);
            final int rpos = HolderFactory.getRow(position);
            sampleProp.put(Sample.PROP_COLPOSITION, cpos);
            sampleProp.put(Sample.PROP_ROWPOSITION, rpos);
        }
        sampleProp.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        Sample sample = null;
        // try {
        sample = (Sample) Util.getOrCreate(rw, Sample.class, sampleProp);
        sample.addSampleCategory(plasmid);
        // } catch (ConstraintException e) {
        /* String uname = refSample.get_Name();
        // TODO SHould not be unique
        
         * if(uname.equalsIgnoreCase(FormFieldsNames.plasmid)) uname = "Derived from "; throw new
         * CustomConstraintException("Sample name must be unique", uname, name, "org.pimslims.model.sample.Sample");
         */
        // }
        return sample;
    }

    @Deprecated
    // COULD use Calendar
    public static Timestamp parseDate(final String datevalue) {
        Timestamp date;
        try {
            date = new Timestamp(((new SimpleDateFormat(Utils.date_format)).parse(datevalue)).getTime());
        } catch (final ParseException e) {
            e.printStackTrace();
            date = new Timestamp(new Date().getTime());
        }
        return date;
    }

    /**
     * @return Returns the entryCloneRb.
     */
    public synchronized static PropertyResourceBundle getEntryCloneRb() {
        if (null == AbstractConstructBean.entryCloneRb) {
            AbstractConstructBean.initializeResourceBundles();
        }
        return AbstractConstructBean.entryCloneRb;
    }

    /**
     * @return Returns the deepFrozenCultureRb.
     */
    public synchronized static PropertyResourceBundle getDeepFrozenCultureRb() {
        if (null == AbstractConstructBean.deepFrozenCultureRb) {
            AbstractConstructBean.initializeResourceBundles();
        }
        return AbstractConstructBean.deepFrozenCultureRb;
    }

}
