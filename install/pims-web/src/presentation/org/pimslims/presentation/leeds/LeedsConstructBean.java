package org.pimslims.presentation.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.Creator;
import org.pimslims.lab.CustomConstraintException;
import org.pimslims.lab.Util;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.construct.ConstructBeanWriter;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         Represents an entry clone or an expression construct
 * 
 */

@Deprecated
// Leeds code is no longer supported
public class LeedsConstructBean extends AbstractConstructBean {

    public static final String EXPRESSION = "Expression";

    public static final String ENTRY_CLONE = "Entry Clone";

    public static final String DEEP_FROZEN_CULTURE = "DeepFrozenCulture";

    protected Experiment experiment;

    // Field name: primer
    // Field value: MPSIL0296F1 MPSIL0296R1
    // Forward primer
    String fprimer;

    // Reverse primer
    String rprimer;

    // Field name: restriction-site
    // Field value: EcoRI-NsiI
    String rsite;

    // Field name: clonesaver
    // Field value: Vincent card B8
    String csName;

    String csPosition;

    // Field name: derived
    // Field value: pTTQ18-UraA-RGS6H
    // name of vector
    String derived;

    // Field name: Tag
    // Field value: RGS-6His
    String tag;

    private String type = null;

    /**
     * entryCloneName String Only used if this is an expression construct
     */
    private String entryCloneName = null;

    /**
     * @return Returns the type, "Expression" or "Entry Clone".
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * TODO ensure the sample this names is of category Vector
     * 
     * @return the name of the vector
     */
    public String getDerived() {
        return this.derived;
    }

    /**
     * See LeedsFormServletUtils. This method is not called explicitly anywhere, but it is required. It is
     * called by introspection.
     * 
     * @param derived The derived to set.
     */
    public void setDerived(final String derived) {
        this.derived = derived;
    }

    /**
     * TODO duplicated with getForwardPrimer?
     * 
     * @return forward primers
     */
    public String getFprimer() {
        return this.fprimer;
    }

    /**
     * TODO duplicated with getReversePrimer?
     * 
     * @return reverse primers
     */
    public String getRprimer() {
        return this.rprimer;
    }

    /**
     * @return the rsite
     */
    public String getRsite() {
        return this.rsite;
    }

    // Primers used: MPSIL0167F1 MPSIL0167R1
    // Assuming forward is the first one, reverse second
    /*
     * @param primers
     */
    protected String getReversePrimer(final String primers) {
        return this.getPrimer(primers, false);
    }

    protected String getForwardPrimer(final String primers) {
        return this.getPrimer(primers, true);
    }

    /**
     * @return the tag
     */
    public final String getTag() {
        return this.tag;
    }

    /**
     * @param tag the tag to set
     */
    public final void setTag(final String tag) {
        this.tag = tag;
    }

    /*
     * Standard getters
     */
    public String getForwardPrimer() {
        return this.fprimer;
    }

    public String getReversePrimer() {
        return this.rprimer;
    }

    /**
     * 
     * @param primers
     * @param type boolean true - return forward primer, false - return reverse primer
     * @return TODO Check whether the name of the primer ends with F of R may be the key
     */
    protected String getPrimer(String primers, final boolean type) {
        if (primers == null) {
            return null;
        }
        primers = primers.trim();
        final int spaceInd = primers.indexOf(' ');
        if (spaceInd < 0) {
            throw new AssertionError("Primers must be separated with a space!");
        }
        if (type) {
            return primers.substring(0, spaceInd + 1).trim().replace("+", "");
        }
        return primers.substring(spaceInd).trim().replace("+", "");
    }

    protected String getCloneSaverName(String creator) {
        if (creator == null) {
            return null;
        }
        creator = creator.trim();
        final int spaceIdx = creator.indexOf(' ');
        if (spaceIdx > 0) {
            return creator.substring(0, spaceIdx);
        }
        return creator;
    }

    public String getCloneSaverName() {
        return this.csName;
    }

    public String getCloneSaverPosition() {
        return this.csPosition;
    }

    protected String getCloneSaverPosition(String cloneSaver) {
        if (cloneSaver == null) {
            return null;
        }
        cloneSaver = cloneSaver.trim();
        final int spaceIdx = cloneSaver.lastIndexOf(' ');
        if (spaceIdx > 0) {
            return cloneSaver.substring(spaceIdx).trim();
        }
        return cloneSaver.trim();
    }

    /*
     * Setters must declare throws ConstraintException because child Plasmid may throw it.
     */
    public LeedsConstructBean() { /* Beans must have default constructor */
        this.derived = "";
    }

    public LeedsConstructBean(final String location1, final String box1, final String position1,
        final String location2, final String box2, final String position2, final String name,
        final String derived, final String antibioticResistance, final String comments, final String primers,
        final String rsite, final String clonesaver, final Date date, final String designedBy) {
        super(location1, box1, position1, location2, box2, position2, name, antibioticResistance, comments,
            date, designedBy);
        this.derived = derived;
        this.fprimer = this.getForwardPrimer(primers);
        this.rprimer = this.getReversePrimer(primers);
        this.rsite = rsite;
        this.csName = this.getCloneSaverName(clonesaver);
        this.csPosition = this.getCloneSaverPosition(clonesaver);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.command.leeds.AbstractConstruct#toString()
     */
    @Override
    public String toString() {
        String plasmid = super.toString();
        plasmid += "derived     " + this.derived + "\n";
        plasmid += "Fprimer     " + this.fprimer + "\n";
        plasmid += "Rprimer     " + this.rprimer + "\n";
        plasmid += "rsite       " + this.rsite + "\n";
        plasmid += "clonesavername  " + this.csName + "\n";
        plasmid += "clonesaver position  " + this.csPosition + "\n";
        return plasmid;
    }

    /**
     * @param clonesaver the clonesaver to set
     */
    public void setCloneSaverName(final String csName) throws ConstraintException, AccessException {
        this.csName = csName;
    }

    public void setCloneSaverPosition(final String csPosition) throws ConstraintException {
        this.csPosition = csPosition;
    }

    /**
     * @param forward primer to set
     */
    public void setFprimer(final String fprimer) {
        this.fprimer = fprimer;
    }

    /**
     * @param reverse primer to set
     */
    public void setRprimer(final String rprimer) {
        this.rprimer = rprimer;
    }

    /**
     * @param rsite the rsite to set
     */
    public void setRsite(final String rsite) throws ConstraintException {
        this.rsite = rsite;
    }

    @Override
    public Experiment record(final WritableVersion rw) throws AccessException, ConstraintException {

        // ConstraintException is unreliable may not be thrown in some
        // circumstances need to check values myself
        this.checkConstraint();

        // Tag is required attribute if Tag is there treat this as expression
        // construct, overwise entry clone
        Protocol protocol = null;
        ExperimentType expType = null;
        String protocolName = FormFieldsNames.ENTRY_CLONE_PROTOCOL;
        if (this.isExpressionConstruct()) {
            protocolName = FormFieldsNames.EXPRESSION_CONSTRUCT_PROTOCOL;
        }
        protocol = rw.findFirst(Protocol.class, Protocol.PROP_NAME, protocolName);
        if (null == protocol) {
            throw new IllegalStateException("Protocol not found: " + protocolName);
        }
        expType = protocol.getExperimentType();
        this.experiment =
            AbstractConstructBean.recordExperiment(rw, expType, this.name, this.comments, this.date);

        assert protocol != null : "Please record protocol first!";
        if (protocol != null) {
            this.experiment.setProtocol(protocol);
        }
        if (this.designedBy != null) {
            final Map paramProp = new HashMap<String, Object>();
            paramProp.put(User.PROP_NAME, this.designedBy);
            final User operator = Util.getOrCreate(rw, User.class, paramProp);
            this.experiment.setOperator(operator);
        }

        this.saveOutputSamples(rw);

        // record entry clone, if this is an expression construct
        if (null != this.entryCloneName) {
            final Sample ec = rw.findFirst(Sample.class, AbstractSample.PROP_NAME, this.entryCloneName);
            if (null == ec) {
                throw new IllegalStateException("Sample not found: " + this.entryCloneName);
            }
            Creator.recordInputSamples(rw, ec, this.experiment, FormFieldsNames.entryClone);
        }

        // Derived from is an InputSample
        assert this.derived != null && !"".equals(this.derived) : "Vector is null cannot create sample";
        // Check whether the same sample is already recorded
        Sample derivedSample = rw.findFirst(Sample.class, AbstractSample.PROP_NAME, this.derived);
        // Record new if not
        final SampleCategory vector =
            rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, FormFieldsNames.VECTOR);
        if (derivedSample == null) {
            derivedSample = AbstractConstructBean.recordSample(rw, vector, null, this.derived, null);
        }

        Creator.recordInputSamples(rw, derivedSample, this.experiment, FormFieldsNames.VECTOR);

        // Primers are input samples as well
        // Search whether the sample with the same name has been created
        if (null != this.fprimer) {
            Sample fprimerSample = LeedsConstructBean.getSample(rw, this.fprimer);
            if (fprimerSample == null) {
                fprimerSample =
                    DeepFrozenCultureBean.recordSample(rw, ConstructBeanWriter.FPRIMER, this.fprimer);
            } else {
                if (null != fprimerSample.getOutputSample()
                    && null != fprimerSample.getOutputSample().getExperiment()) {
                    this.experiment.setProject(fprimerSample.getOutputSample().getExperiment()
                        .getResearchObjective());
                }
            }
            //final ModelObject fprimerInSample =
            Creator.recordInputSamples(rw, fprimerSample, this.experiment, ConstructBeanWriter.FPRIMER);
        }
        if (null != this.rprimer) {
            Sample rprimerSample = LeedsConstructBean.getSample(rw, this.rprimer);
            if (rprimerSample == null) {
                rprimerSample =
                    DeepFrozenCultureBean.recordSample(rw, ConstructBeanWriter.RPRIMER, this.rprimer);
            } else {
                if (null == this.experiment.getProject()) {
                    if (null != rprimerSample.getOutputSample()
                        && null != rprimerSample.getOutputSample().getExperiment()) {
                        this.experiment.setProject(rprimerSample.getOutputSample().getExperiment()
                            .getResearchObjective());
                    }
                }
            }
            //final ModelObject rprimerInSample =
            Creator.recordInputSamples(rw, rprimerSample, this.experiment, ConstructBeanWriter.RPRIMER);
        }

        final ModelObject markerParameter =
            Creator.recordParameters(rw, FormFieldsNames.ANTIBIOTIC_RESISTANCE, "String",
                this.getAntibioticResistance(), this.experiment);
        final ModelObject siteFlParameter =
            Creator.recordParameters(rw, FormFieldsNames.rsite, "String", this.rsite, this.experiment);

        final List parameters = new ArrayList();
        parameters.add(markerParameter);
        parameters.add(siteFlParameter);

        if (!Util.isEmpty(this.tag)) {
            final ModelObject tagParameter =
                Creator.recordParameters(rw, FormFieldsNames.tag, "String", this.tag, this.experiment);
            parameters.add(tagParameter);
        }

        // Create reference data
        // List samples = Utils.join(sample1, sample2);
        // List outSamples = Utils.join(outputSample1, outputSample2);
        // if(outputSampleCloneSaver != null)
        // outSamples.add(outputSampleCloneSaver);

        //final List inSamples = Util.join(fprimerInSample, rprimerInSample);
        //inSamples.add(derivedInSample);

        LeedsConstructBean.setTarget(rw, this.experiment, this.name);
        //TODO set target from input samples
        return this.experiment;
    }

    public boolean isExpressionConstruct() {
        if (LeedsConstructBean.EXPRESSION.equals(this.getType())) {
            return true;
        }
        if (LeedsConstructBean.ENTRY_CLONE.equals(this.getType())) {
            return false;
        }
        // old way to determine this:
        return this.tag != null;
    }

    private void saveOutputSamples(final WritableVersion rw) throws AccessException, ConstraintException {
        final SampleCategory plasmid =
            rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, FormFieldsNames.RECOMBINANT_PLASMID);
        assert null != plasmid : "Sample category not found: " + FormFieldsNames.RECOMBINANT_PLASMID;

        final Holder holder1 = Creator.recordHolder(rw, FormFieldsNames.boxHolderType, this.box1, null);
        final Location locationObj1 = Creator.recordLocation(rw, this.location1);

        /* Test for wrong association
        for (Iterator iterator = locationObj1.getHolderLocations().iterator(); iterator.hasNext();) {
            HolderLocation name = (HolderLocation) iterator.next();
            if (holder1.get_Hook().equals(name.getHolder().get_Hook())) {
                System.out.println("!!!!!!!!!!!");
            }
        }
        */
        ContainerUtility.move(holder1, locationObj1);

        /*  Test for wrong association
         for (Iterator iterator = locationObj1.getHolderLocations().iterator(); iterator.hasNext();) {
            HolderLocation name = (HolderLocation) iterator.next();
            if (holder1.get_Hook().equals(name.getHolder().get_Hook())) {
                System.out.println("222222222222");
            }
        }
        */
        final Sample sample1 =
            AbstractConstructBean.recordSample(rw, plasmid, holder1, this.name + " (1)", this.position1);
        //final ModelObject outputSample1 =
        Creator.recordOutputSamples(rw, sample1, this.experiment, "sample1");

        if (!Util.isEmpty(this.location2) && !Util.isEmpty(this.position2) && !Util.isEmpty(this.box2)) {
            final Holder holder2 = Creator.recordHolder(rw, FormFieldsNames.boxHolderType, this.box2, null);
            final Location locationObj2 = Creator.recordLocation(rw, this.location2);
            ContainerUtility.move(holder2, locationObj2);
            final Sample sample2 =
                AbstractConstructBean.recordSample(rw, plasmid, holder2, this.name + " (2)", this.position2);
            //final ModelObject outputSample2 =
            Creator.recordOutputSamples(rw, sample2, this.experiment, "sample2");
        }

        this.setBarcodes(sample1, holder1);

        //OutputSample outputSampleCloneSaver = null;
        if (!Util.isEmpty(this.csName)) {
            // System.out.println("SNAME " + csName);
            //outputSampleCloneSaver =
            LeedsConstructBean.recordCloneSaver(rw, this.csName, this.csPosition, plasmid, this.experiment);
        }
    }

    /**
     * @param rw
     * @throws ConstraintException
     */
    public static void setTarget(final WritableVersion rw, final Experiment experiment, final String name)
        throws ConstraintException {
        // Set target to this experiment
        final Target target = FindLeedsTarget.findCorrespondingTarget(rw, name);

        if (target != null) {
            AbstractConstructBean.setTarget(target, experiment);
            // System.out.println("Target found for " + name);
        }
    }

    // + MPSIL0343 F1
    // Can use this and track locations as long as locations for
    // entry clones themselves was track using a number of output samples
    //TODO no, this takes ages, use rv.findFirst(Sample.class, Sample.PROP_NAME, sname)
    public static Sample getSample(final ReadableVersion rv, final String sname) {
        return rv.findFirst(Sample.class, AbstractSample.PROP_NAME, sname);
    }

    void checkConstraint() throws CustomConstraintException {
        if (this.derived == null) {
            throw new CustomConstraintException("Vector must be defined", "Vector", "none",
                "org.pimslims.model.sample.Sample");
        }
        if (this.isExpressionConstruct()) {

            if (this.entryCloneName == null) {
                throw new CustomConstraintException("Entry clone must be defined", "Entry Clone", "none",
                    "org.pimslims.model.sample.Sample");
            }
            return; // otherwise OK
        }

        // it is an entry clone
        if (this.fprimer == null) {
            throw new CustomConstraintException("Forward primer must be defined", "Forward primer", "none",
                "org.pimslims.model.sample.Sample");
        }
        if (this.rprimer == null) {
            throw new CustomConstraintException("Reverse primer must be defined", "Reverse primer", "none",
                "org.pimslims.model.sample.Sample");
        }
        if (this.fprimer.equals(this.rprimer)) {
            throw new CustomConstraintException("Forward and reverse primers must have different names",
                "Forward / Reverse primer names", this.fprimer + " / " + this.rprimer,
                "org.pimslims.model.sample.Sample");
        }
        if (this.fprimer.equals(this.derived)) {
            throw new CustomConstraintException("Primer names must not be the same as the vector",
                "Forward primer / derived ", this.fprimer + " / " + this.derived,
                "org.pimslims.model.sample.Sample");
        }
        if (this.fprimer.equals(this.derived)) {
            throw new CustomConstraintException("Primer names must not be the same as the vector",
                "Forward primer / derived ", this.fprimer + " / " + this.derived,
                "org.pimslims.model.sample.Sample");
        }
        if (this.rprimer.equals(this.derived)) {
            throw new CustomConstraintException("Primer names must not be the same as the vector",
                "Reverse primer / derived ", this.rprimer + " / " + this.derived,
                "org.pimslims.model.sample.Sample");
        }
    }

    // Example name = pMPSIL0167A - Target.localName would be MPSIL0167
    // Target.commonName = UL0167
    /*
     * Find the target and associate it to this exeriment TODO complete method !

    private Target associateWithTarget(final String name) {
        throw new java.lang.UnsupportedOperationException("not implemented yet");
    } */

    public static OutputSample recordCloneSaver(final WritableVersion rw, final String cloneSaver,
        final String csPosition, final SampleCategory category, final Experiment experiment)
        throws AccessException, ConstraintException {
        final Holder holderCloneSaver = Creator.recordHolder(rw, "Clone saver card", "card", null);
        final Location locationCloneSaver = Creator.recordLocation(rw, cloneSaver);
        ContainerUtility.move(holderCloneSaver, locationCloneSaver);

        final Sample cloneSaverSample =
            AbstractConstructBean.recordSample(rw, category, holderCloneSaver, experiment.getName() + " (3)",
                csPosition);
        final ModelObject outputSampleCloneSaver =
            Creator.recordOutputSamples(rw, cloneSaverSample, experiment, "Clone Saver");
        return (OutputSample) outputSampleCloneSaver;
    }

    /**
     * will be null if record method has not been called yet.
     * 
     * @return the experiment
     */
    public Experiment getExperiment() {
        return this.experiment;
    }

    /*
     * Returns all location associated with the holder "card"
     */
    public static Collection<Location> getCloneSaverLocations(final ReadableVersion version) {
        final ArrayList<Location> locations = new ArrayList<Location>();
        final HashMap param = new HashMap();
        param.put(AbstractHolder.PROP_NAME, "card");
        final Holder holder = version.findFirst(org.pimslims.model.holder.Holder.class, param);
        assert null != holder : "You must have a holder called 'card'";
        for (final HolderLocation hl : holder.getHolderLocations()) {
            locations.add(hl.getLocation());
        }
        return locations;
    }

    /**
     * Only used if this is an expression construct
     * 
     * LeedsConstructBean.setEntryCloneName
     * 
     * @param entryClone name of the entry clone this was made from
     */
    public void setEntryCloneName(final String entryClone) {
        this.entryCloneName = entryClone;
    }
}
