/**
 * pims-web org.pimslims.presentation.PrimerBean.java
 * 
 * @author Marc Savitsky
 * @date 28th September 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */

package org.pimslims.presentation;

import java.text.DecimalFormat;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.lab.sequence.DnaSequence;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.leeds.OrderUtility;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.construct.ConstructAnnotator;
import org.pimslims.presentation.construct.ConstructBeanWriter;

public class PrimerBean extends ModelObjectBean { //implements Comparable<PrimerBean> {

    // The Nucleic Acid sequence of the primer
    private String sequence;

    // The direction of the primer, if forward = true, reverse = false.
    private Boolean isForward;

    public static final Boolean FORWARD = Boolean.TRUE;

    public static final Boolean REVERSE = Boolean.FALSE;

    // The column position in the plate
    private Integer col;

    // The row position in the plate
    private Integer row;

    // The sub well position in the plate
    private Integer sub;

    // The box where the primer is stored
    private String box;

    // The box where the primer is stored
    private String boxName;

    // Barcode for a box
    private String boxBarcode;

    // The position of the primer in the box defined above
    private String position;

    // Barcode for a position in the box
    private String positionBarcode;

    // Concentration of the primer
    private Float concentration;

    // These values are not store and replased with the getters which calculate
    // values on the fly
    // Full GC contents
    // String gcfull;
    // Full primer length
    // String length;

    // GC contents of the part of the primer which intersects with the gene
    private String gcgene;

    // The length of the primers part which is common with the gene
    private String lengthongene;

    // In the Leeds data this is usually purity or purification method
    private String particularity;

    // The restriction site on the primer
    private String restrictionsite;

    // The seller of the primer often its manufacturer
    private String seller;

    // The location of the box where the primer is stored
    private String location;

    // The location of the box where the primer is stored
    private String locationName;

    // The melting temperature for the full length of the primer
    private Float tmfull;

    // The melting temperature for part length of the primer common with the
    // gene
    private String tmgene;

    // Primers melting temperature provided by manufacturer
    private String tmseller;

    // ***********************************
    // Values from Leeds primer order form

    // Molecular mass of the primer
    private Float molecularMass = 0.0f;

    // Amount of the primer sample in µg
    private Float amount;

    // Optical density of the solution
    private String oDensity;

    //cached Sample
    private Sample sample = null;

    private User assignedTo = null;

    private String tag = "";

    private String primerHook;

    public PrimerBean(final boolean isForward) { // Empty constructor
        this.isForward = isForward;
    }

    /*
     * Bare constructor Less details than this would not make sense Although the name seems to be not that
     * important but the primer is a sample so we must provide one property to be store as sample
     */
    public PrimerBean(final String name, final String sequence, final boolean isForward, final String tag) {
        this.name = name;
        this.setSequence(sequence);
        this.isForward = isForward;
        this.tag = tag;
    }

    @Deprecated
    // tested but not used
    public PrimerBean(final String name, final String box, final String position, final String concentration,
        final String gcgene, final String lengthgene, final String particularity,
        final String restrictionsite, final String seller, final String sequence, final String location,
        final String tmfull, final String tmgene, final String tmseller, final boolean direct,
        final String tag) {
        this.name = name;
        this.box = box;
        this.position = position;
        this.concentration = Float.parseFloat(concentration.trim());
        this.gcgene = gcgene;
        this.lengthongene = lengthgene;
        this.particularity = particularity;
        this.restrictionsite = restrictionsite;
        this.seller = seller;
        this.setSequence(sequence);
        this.location = location;
        this.tmfull = new Float(tmfull);
        this.tmgene = tmgene;
        this.tmseller = tmseller;
        this.isForward = direct;
        this.tag = tag;
    }

    /**
     * Constructor for PrimerBean
     */
    public PrimerBean() {
    }

    public ReadableVersion getVersion() {
        return this.sample.get_Version();
    }

    public final void setCol(final Integer col) {
        this.col = col;
    }

    public final Integer getCol() {
        return this.col;
    }

    public void setRow(final Integer row) {
        this.row = row;
    }

    public final Integer getRow() {
        return this.row;
    }

    public void setSub(final Integer sub) {
        this.sub = sub;
    }

    public final Integer getSub() {
        return this.sub;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    public void setSequence(final String sequence) {
        if (sequence != null) {
            this.sequence = sequence.toUpperCase();
        } else {
            this.sequence = sequence;
        }
    }

    public final String getSequence() {
        return this.sequence;
    }

    /**
     * @return the tmfull
     */
    public final String getTmfull() {
        return this.getTmFull();
    }

    public final String getTmFull() {
        if (null == this.tmfull) {
            return "";
        }
        if (this.tmfull < 0) {
            return "";
        }
        return new DecimalFormat("##0.0").format(this.tmfull);
    }

    public final float getTmfullasFloat() {
        if (null == this.tmfull) {
            return 0f;
        }
        return this.tmfull.floatValue();
    }

    /**
     * @param tmfull the tmfull to set
     */
    @Deprecated
    // tested but not used
    public final void setTmfull(final String tmfull) {
        if (ServletUtil.validString(tmfull)) {
            this.tmfull = new Float(tmfull);
        } else {
            this.tmfull = new Float(-1);
        }
    }

    public final void setTmfullfloat(final float tmfull) {
        this.tmfull = new Float(tmfull);
    }

    /**
     * @return the tmgene
     */
    public final String getTmGene() {
        return this.tmgene;
    }

    /**
     * @param tmgene the tmgene to set
     */
    public final void setTmGene(final String tmgene) {
        this.tmgene = tmgene;
    }

    /**
     * @return the tmseller
     */
    public final String getTmseller() {
        return this.tmseller;
    }

    public final String getTmSeller() {
        return this.getTmseller();
    }

    /**
     * @param tmseller the tmseller to set
     */
    public final void setTmseller(final String tmseller) {
        this.tmseller = tmseller;
    }

    /**
     * @param box the box to set
     */
    public final void setBox(final String box) {
        this.box = box;
    }

    /**
     * @param box the box to set
     */
    public final void setBoxName(final String box) {
        this.boxName = box;
    }

    public int getLength() {
        // assert sequence != null : "Sequence must be set first";
        if (this.sequence == null) {
            return 0;
        }
        return this.sequence.trim().length();
    }

    public float getGcFull() {
        if (this.sequence == null) {
            return 0;
        }
        return new DnaSequence(this.sequence).getGCContent();
    }

    public String getGCFull() {
        return new DecimalFormat("##0.0").format(this.getGcFull());
    }

    /**
     * @return the location
     */
    @Deprecated
    // Obsolete
    public final String getLocation() {
        return this.location;
    }

    /**
     * @param location the location to set
     */
    public final void setLocation(final String location) {
        //System.out.println("PrimerBean.setLocation(" + location + ")");
        this.location = location;
    }

    /**
     * @return the location name
     */
    @Deprecated
    // Obsolete
    public final String getLocationName() {
        return this.locationName;
    }

    /**
     * @param location the location to set
     */
    public final void setLocationName(final String location) {
        //System.out.println("PrimerBean.setLocation(" + location + ")");
        this.locationName = location;
    }

    /**
     * @return the particularity
     */
    public final String getParticularity() {
        return this.particularity;
    }

    /**
     * @param particularity the particularity to set
     */
    public final void setParticularity(final String particularity) {
        this.particularity = particularity;
    }

    /**
     * @return the gcgene
     */
    public final String getGCGene() {
        return this.gcgene;
    }

    public final Float getGcGeneFloat() {
        if (null == this.getGCGene()) {
            return null;
        }
        try {
            return Float.valueOf(this.getGCGene());
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * @param gcgene the gcgene to set
     */
    public final void setGcgene(final String gcgene) {
        this.gcgene = gcgene;
    }

    /**
     * @return the lengthgene
     */
    public final String getLengthOnGeneString() {
        return this.lengthongene;
    }

    /**
     * PrimerBean.getOverlapLength
     * 
     * @return the length on the gene
     */
    public int getOverlapLength() {
        if (null == this.lengthongene || "".equals(this.lengthongene)) {
            return 0;
        }
        return Integer.valueOf(this.lengthongene);
    }

    public String getOverlap() {
        if (null == this.sequence) {
            return "";
        }
        if (this.getOverlapLength() > this.sequence.length()) {
            return this.sequence;
        }
        return this.sequence.substring(this.sequence.length() - this.getOverlapLength(),
            this.sequence.length());

    }

    /**
     * PrimerBean.getExtension
     * 
     * @return the part of the DNA sequence that is not in the overlap
     */
    public String getExtension() {
        if (null == this.sequence) {
            return "";
        }
        if (this.getOverlapLength() > this.sequence.length()) {
            return "";
        }
        return this.sequence.substring(0, this.sequence.length() - this.getOverlapLength());

    }

    /**
     * PrimerBean.setExtension
     * 
     * @param extension the new extension
     */
    public void setExtension(final String extension) {
        if (null == extension) {
            throw new IllegalArgumentException();
        }
        if ("null".equals(extension)) {
            throw new IllegalArgumentException();
        }
        this.sequence = extension + this.getOverlap();
    }

    /**
     * @param lengthgene the lengthgene to set
     */
    public final void setLengthOnGeneString(final String lengthgene) {
        this.lengthongene = lengthgene;
    }

    /**
     * @param length the overlap length
     */
    public final void setLengthOnGene(final int length) {
        this.lengthongene = Integer.toString(length);
    }

    /**
     * @return the box
     */
    public final String getBox() {
        return this.box;
    }

    /**
     * @return the box name
     */
    public final String getBoxName() {
        return this.boxName;
    }

    /**
     * @return the concentration
     */
    public final Float getConcentration() {
        return this.concentration;
    }

    /**
     * @return the amount
     */
    public final Float getAmount() {
        return this.amount;
    }

    /**
     * @return the molecularMass
     */
    public final Float getMolecularMass() {
        if (null != this.sequence && this.molecularMass == 0.0) {
            final DnaSequence dna = new DnaSequence(this.sequence);
            return dna.getMass();
        }
        return this.molecularMass;
    }

    /**
     * @return the oDensity
     */
    public final String getOD() {
        return this.oDensity;
    }

    /**
     * @param concentration the concentration to set
     */
    public final void setConcentration(final String concentration) {
        if (!Util.isEmpty(concentration)) {
            try {
                this.concentration = Float.valueOf(concentration);
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("Not a number: " + concentration);
            }
        }
    }

    /**
     * @param Amount the amount to set
     */
    public final void setAmount(final String amount) {
        if (!Util.isEmpty(amount)) {
            this.amount = Float.valueOf(amount);
        }
    }

    /**
     * @param mass the MolecularMass to set
     */
    //  note: the parameter needs to be a string
    //        it is called by reflection 
    public final void setMolecularMass(final String mass) {
        this.molecularMass = new Float(mass);
    }

    /**
     * @param mass the MolecularMass to set
     */
    public final void setOD(final String oDensity) {
        if (!Util.isEmpty(oDensity)) {
            this.oDensity = oDensity;
        }
    }

    /**
     * @return the position
     */
    public final String getPosition() {
        return this.position;
    }

    /**
     * @return the boxBarcode
     */
    public final String getBoxBarcode() {
        return this.boxBarcode;
    }

    /**
     * @param boxBarcode the boxBarcode to set
     */
    public final void setBoxBarcode(final String boxBarcode) {
        this.boxBarcode = boxBarcode;
    }

    /**
     * @return the positionBarcode
     */
    public final String getPositionBarcode() {
        return this.positionBarcode;
    }

    /**
     * @param positionBarcode the positionBarcode to set
     */
    public final void setPositionBarcode(final String positionBarcode) {
        this.positionBarcode = positionBarcode;
    }

    /**
     * @param position the position to set
     */
    public final void setPosition(final String position) {
        this.position = position;
    }

    /**
     * @return the restrictionsite
     */
    public final String getRestrictionsite() {
        return this.restrictionsite;
    }

    public final String getRestrictionSite() {
        return this.getRestrictionsite();
    }

    /**
     * @param restrictionsite the restrictionsite to set
     */
    public final void setRestrictionsite(final String restrictionsite) {
        this.restrictionsite = restrictionsite;
    }

    /**
     * @return the seller
     */
    public final String getSeller() {
        return this.seller;
    }

    // getter only
    //public Float getAmountperOD() {
    public String getAmountperOD() {
        final Float m = this.getAmount();
        final String o = this.getOD();
        Float od = null;
        if (!Util.isEmpty(o)) {
            od = Float.valueOf(o);
        }

        if (m != null && m != 0 && od != null && od != 0) {
            return new DecimalFormat("##0.00").format(m / od);
        }

        return null;
    }

    /**
     * @param seller the seller to set
     */
    public final void setSeller(final String seller) {
        this.seller = seller;
    }

    public void setDirection(final String direction) {
        if (!ServletUtil.validString(direction)) {
            this.isForward = null;
            return;
        }
        if (direction.equalsIgnoreCase("forward")) {
            this.isForward = true;
        } else if (direction.equalsIgnoreCase("reverse")) {
            this.isForward = false;
        } else if (direction.equalsIgnoreCase(ConstructBeanWriter.FPRIMER)) {
            this.isForward = true;
        } else if (direction.equalsIgnoreCase(ConstructBeanWriter.RPRIMER)) {
            this.isForward = false;
        } else {
            throw new AssertionError("Incorrect primer direction!");
        }
    }

    public void setDirection(final boolean direction) {
        this.isForward = direction;
    }

    public String getDirection() {
        return this.isForward ? "forward" : "reverse";
    }

    public String getFormFieldsDirection() {
        return this.isForward ? ConstructBeanWriter.FPRIMER : ConstructBeanWriter.RPRIMER;
    }

    public String getFormFieldsSDMDirection() {
        return this.isForward ? FormFieldsNames.SPRIMER : FormFieldsNames.APRIMER;
    }

    public final boolean isForward() {
        return this.isForward.booleanValue();
    }

    public final boolean isReverse() {
        return this.isForward.booleanValue() ? false : true;
    }

    /**
     * @return the direct
     */
    public final Boolean isDirect() {
        return this.isForward;
    }

    public static String cleanName(String sname) {
        sname = sname.replace(" ", "").trim();
        sname = sname.replace("+", "").trim();
        return sname;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("name " + this.name + "\n");
        sb.append("box " + this.box + "\n");
        sb.append("position " + this.position + "\n");
        sb.append("concentration " + this.concentration + "\n");
        // values += "gcfull " + gcfull + "\n";
        sb.append("gcgene " + this.gcgene + "\n");
        // values += "length " + length + "\n";
        sb.append("lengthgene " + this.lengthongene + "\n");
        sb.append("particularity " + this.particularity + "\n");
        sb.append("restrictionsite " + this.restrictionsite + "\n");
        sb.append("seller " + this.seller + "\n");
        sb.append("sequence " + this.sequence + "\n");
        sb.append("location " + this.location + "\n");
        sb.append("tmfull " + this.tmfull + "\n");
        sb.append("tmgene " + this.tmgene + "\n");
        sb.append("tmseller " + this.tmseller + "\n");
        sb.append("direction "
            + (this.isForward == null ? "Not defined" : (this.isForward ? "Forward" : "Reverse")) + "\n");
        return sb.toString();
    }

    /**
     * PrimerBean.equals
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PrimerBean)) {
            return false;
        }
        final PrimerBean pb = (PrimerBean) obj;
        if (pb.sequence != null && pb.sequence.equalsIgnoreCase(this.sequence)) {
            return true;
        }
        return false;
    }

    /**
     * PrimerBean.hashCode
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new Double(this.getGcFull() * 10 * this.getTmfullasFloat() * 100).intValue();
    }

    /**
     * @return Returns the sample.
     */
    public Sample getSample(final ReadableVersion version) {
        /**
         * @author bl67 this improve performance the assumption is this sample would not be changed outside
         *         Hibernate, eg: though jdbc connection
         */
        //Warning
        //System.out.println("PrimerBean.getSample [" + sample + "]");
        if (this.sample == null || this.sample.get_Version() != version) {
            this.sample = version.findFirst(Sample.class, AbstractSample.PROP_NAME, this.getName());
        }
        return this.sample;
    }

    public Sample getSample() {
        return this.sample;
    }

    @Override
    public String getHook() {
        if (null == this.sample) {
            return null;
        }
        return this.sample.get_Hook();
    }

    /**
     * @param sample The sample to set.
     */
    public void setSample(final Sample sample) {
        this.sample = sample;
    }

    public User getAssignTo() {
        return this.assignedTo;
    }

    public void setAssignTo(final User person) {
        this.assignedTo = person;
    }

    public String getOrder() {
        final Holder holder = this.sample.getHolder();
        if (holder == null) {
            return null;
        }
        if (OrderUtility.isLeedsPrimerOrder(holder)) {
            return holder.get_Hook();
        }

        return null;
    }

    public Experiment getCloningDesignExperiment() {
        for (final InputSample inSam : this.sample.getInputSamples()) {
            final String experimentTypeName = inSam.getExperiment().getExperimentType().getName();
            if (experimentTypeName.equals(FormFieldsNames.leedscloningDesign)
                || experimentTypeName.equals(FormFieldsNames.oppfcloningDesign)
                || experimentTypeName.equals(FormFieldsNames.spotcloningDesign)) {
                return inSam.getExperiment();
            }
        }
        return null;
    }

    public Experiment getPrimerDesignExperiment() {
        if (this.sample.getOutputSample() != null) {
            return this.sample.getOutputSample().getExperiment();
        }
        // else
        return null;
    }

    /**
     * PrimerBean.getTmGeneFloat
     * 
     * @return
     */
    public Float getTmGeneFloat() {
        if (null == this.getTmGene()) {
            return null;
        }
        try {
            return Float.valueOf(this.getTmGene());
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    /**
     * PrimerBean.getTag
     * 
     * @return the protein tag coded by the extension
     */
    public String getTag() {
        if (this.tag == null) {
            return "";
        }
        return this.tag;
    }

    /**
     * PrimerBean.setTag
     * 
     * @param string
     */
    public void setTag(final String string) {
        this.tag = string;
    }

    /**
     * PrimerBean.getPrimerHook
     * 
     * @return
     */
    public String getPrimerHook() {
        return this.primerHook;
    }

    /**
     * @param primerHook The primerHook to set.
     */
    public void setPrimerHook(final String primerHook) {
        this.primerHook = primerHook;
    }

    /**
     * PrimerBean.getOverlapSequence
     * 
     * @return
     */
    @Deprecated
    // use this.getOverlap()
    public String getOverlapSequence() {
        return this.getOverlap();
    }

    /**
     * Comparable<PrimerBean>.compareTo
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final PrimerBean o) {
        return this.getName().compareTo(o.getName());
    }

    public static void annotatePrimer(final PrimerBean bean) {
        if (bean.getSequence() == null) {
            return;
        }
        final String seq = ConstructAnnotator.cleanPrimer(bean.getSequence());
        final DnaSequence fDna = new DnaSequence(seq);
        //was bean.setGcfull(Float.toString(fDna.getGCContent()));
        bean.setTmfullfloat(fDna.getTm());
        if (bean.getOverlap() != null && !"".equals(bean.getOverlap())) {
            final DnaSequence fOL = new DnaSequence(bean.getOverlap());
            bean.setGcgene(Float.toString(fOL.getGCContent()));
            bean.setTmGene(Float.toString(fOL.getTm()));
        }
    }

}
