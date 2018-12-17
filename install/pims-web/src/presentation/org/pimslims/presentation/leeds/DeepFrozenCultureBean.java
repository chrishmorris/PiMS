package org.pimslims.presentation.leeds;

import java.util.Collections;
import java.util.Date;
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
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.people.Person;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin aka pvt43
 * 
 * 
 *         DeepFrozenCultureBean
 * 
 */
@Deprecated
// Leeds functionality is obsolete
public class DeepFrozenCultureBean extends AbstractConstructBean {

    // Information relevant for Leeds Expression constract
    String organism;

    // String genotype;

    String strain;

    private String expressionConstructNameOrHook;

    /**
     * @return the Organism
     */
    public String getOrganism() {
        return this.organism;
    }

    /*
     * Setters must declare throws ConstraintException because child Plasmid may throw it.
     */
    public DeepFrozenCultureBean() { /* Beans must have default constructor */
        this.expressionConstructNameOrHook = null;
    }

    public DeepFrozenCultureBean(final String location1, final String box1, final String position1,
        final String location2, final String box2, final String position2, final String name,
        final String organism, final String expressionConstruct, final String comments, final String strain,
        final Date date, final String designedBy) {
        super(location1, box1, position1, location2, box2, position2, name, null, comments, date, designedBy);
        this.organism = organism;
        this.strain = strain;
        this.expressionConstructNameOrHook = expressionConstruct;
    }

    /**
     * Data example pCR-MPSIL0305 + Omnimax - where Omnimax is a strain
     * 
     * @return Strain Omnimax - use plus or a first left space as a separator
     */
    public static String getStrain(final String name) {
        final int sepIdx = DeepFrozenCultureBean.getSeparatorIdx(name);
        if (sepIdx < 0) {
            return "";
        }
        return name.substring(sepIdx + 1).trim();
    }

    private static int getSeparatorIdx(String name) {
        if (Util.isEmpty(name)) {
            return -1;
        }
        name = name.trim();
        int sepIdx = name.indexOf("+");
        if (sepIdx < 0) {
            sepIdx = name.indexOf(" ");
        }
        if (sepIdx < 0) {
            return -1;
        }
        return sepIdx;
    }

    private static String cleanName(final String name) {
        final int sepIdx = DeepFrozenCultureBean.getSeparatorIdx(name);
        if (sepIdx < 0) {
            return name;
        }
        return name.substring(0, sepIdx).trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.command.leeds.AbstractConstruct#toString()
     */
    @Override
    public String toString() {
        String plasmid = super.toString();
        plasmid += "Organism        " + this.organism + "\n";
        plasmid += "Strain        " + this.strain + "\n";
        return plasmid;
    }

    /**
     * @return the strain
     */
    public String getStrain() {
        return this.strain;
    }

    /**
     * @param strain the strain to set
     */
    public void setStrain(final String strain) throws ConstraintException {
        this.strain = strain;
    }

    public void setOrganism(final String organism) {
        this.organism = organism;
    }

    @Override
    public Experiment record(final WritableVersion rw) throws AccessException, ConstraintException {
        this.checkConstraint();
        final ModelObject expType = Creator.findExperimentType(rw, FormFieldsNames.dfrozen);
        final Experiment experiment =
            AbstractConstructBean.recordExperiment(rw, expType, this.name, this.comments, this.date);

        if (null != this.designedBy) {
            final Map personProp = Util.getNewMap();

            final String fname = AbstractConstructBean.getDesignerFamilyName(this.designedBy);
            if (fname != null) {
                personProp.put(Person.PROP_FAMILYNAME, fname);
                personProp.put(Person.PROP_GIVENNAME, AbstractConstructBean.getDesignerName(this.designedBy));
            } else {
                personProp.put(Person.PROP_FAMILYNAME, this.designedBy);
            }
            final Person person = (Person) Util.getOrCreate(rw, Person.class, personProp);

            if (!person.getUsers().isEmpty()) {
                final User personUser = person.getUsers().iterator().next();
                experiment.setOperator(personUser);
            }
        }
        final SampleCategory transformedCells =
            rw.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, FormFieldsNames.TRANSFORMED_CELLS);

        final Holder holder1 = Creator.recordHolder(rw, FormFieldsNames.boxHolderType, this.box1, null);
        final Location locationObj1 = Creator.recordLocation(rw, this.location1);
        ContainerUtility.move(holder1, locationObj1);

        if (!Util.isEmpty(this.location2) && !Util.isEmpty(this.position2) && !Util.isEmpty(this.box2)) {
            final Holder holder2 = Creator.recordHolder(rw, FormFieldsNames.boxHolderType, this.box2, null);
            final Location locationObj2 = Creator.recordLocation(rw, this.location2);
            ContainerUtility.move(holder2, locationObj2);
            final Sample sample2 =
                AbstractConstructBean.recordSample(rw, transformedCells, holder2, this.name + " (2)",
                    this.position2);
            Creator.recordOutputSamples(rw, sample2, experiment, "sample2");
        }
        final Sample sample1 =
            AbstractConstructBean.recordSample(rw, transformedCells, holder1, this.name + " (1)",
                this.position1);
        Creator.recordOutputSamples(rw, sample1, experiment, "sample1");

        this.setBarcodes(sample1, holder1);

        if (!Util.isEmpty(this.getEntryCloneHook())) {
            Sample markerSample = null;
            if (Util.isHookValid(this.getEntryCloneHook())) {
                markerSample = rw.get(this.getEntryCloneHook());
            } else {
                markerSample = this.getExpressionConstructSample(rw, this.getEntryCloneHook().trim());
            }

            if (markerSample != null) {
                Creator.recordInputSamples(rw, markerSample, experiment,
                    FormFieldsNames.ENTRY_CLONE_INPUT_SAMPLE);
                //inputSamples.add(markerInSample);
            }
        }

        if (!Util.isEmpty(this.strain)) {
            Sample strainSample = this.getStrainSample(rw, this.strain);
            if (strainSample == null) {
                strainSample = DeepFrozenCultureBean.recordSample(rw, FormFieldsNames.strain, this.strain);
            }
            Creator.recordInputSamples(rw, strainSample, experiment, FormFieldsNames.STRAIN_INPUT);
            //inputSamples.add(strainInSample);
        }

        Creator.recordParameters(rw, FormFieldsNames.organism, "String", this.organism, experiment);

        // Create reference data
        // List samples = Utils.join(sample1, sample2);

        // List outSamples = Utils.join(outputSample1, outputSample2);

        // Cannot use Entry Clone to locate the target as some location will use
        // deepfrozen constructs
        // without entry clones.
        // Set target to this experiment

        final Target target = FindLeedsTarget.findCorrespondingTarget(rw, this.name);

        if (target != null) {
            AbstractConstructBean.setTarget(target, experiment);
            // System.out.println("Target found for " + name);
        }

        // Protocol protocol = Creator.recordProtocol(rw,
        // FormFieldsNames.dfrozen, Utils.makeList(plasmid),samples,
        // outSamples, inputSamples, Utils.makeList(organismParameter),
        // experiment);

        final Protocol protocol = rw.findFirst(Protocol.class, Protocol.PROP_NAME, FormFieldsNames.dfrozen);
        assert protocol != null : "Please record protocol first!";
        if (protocol != null) {
            experiment.setProtocol(protocol);
        }

        return experiment;
    }

    void checkConstraint() throws CustomConstraintException {
        if (this.name == null) {
            throw new CustomConstraintException("Name must be defined", "Name", "none",
                "org.pimslims.model.experiment.Experiment");
        }
    }

    Sample getExpressionConstructSample(final ReadableVersion rv, final String name) {
        Sample sample =
            rv.findFirst(org.pimslims.model.sample.Sample.class, AbstractSample.PROP_NAME, name + " (1)");
        if (null == sample) {
            sample = rv.findFirst(org.pimslims.model.sample.Sample.class, AbstractSample.PROP_NAME, name);
        }
        if (sample != null
            && sample.findFirst(AbstractSample.PROP_SAMPLECATEGORIES, SampleCategory.PROP_NAME,
                FormFieldsNames.RECOMBINANT_PLASMID) != null) {
            return sample;
        }
        return null;
    }

    Sample getStrainSample(final ReadableVersion rv, final String name) {
        final Map param = Util.getNewMap();
        param.put(AbstractSample.PROP_NAME, name);
        //param.put(Sample.PROP_SAMPLECATEGORIES, FormFieldsNames.strain);
        final Sample sample = rv.findFirst(org.pimslims.model.sample.Sample.class, param);
        if (sample != null
            && sample.findFirst(AbstractSample.PROP_SAMPLECATEGORIES, SampleCategory.PROP_NAME,
                FormFieldsNames.strain) != null) {
            return sample;
        }
        return null;
    }

    public final String getEntryCloneHook() {
        return this.expressionConstructNameOrHook;
    }

    public final void setExpressionConstructHook(final String expressionConstructHook) {
        this.expressionConstructNameOrHook = expressionConstructHook;
    }

    /**
     * no-op called from LeedsFormServletUtil. DeepFrozenCultureBean.setType
     */
    public final void setType() {
        // no-op called from LeedsFormServletUtil. DeepFrozenCultureBean.setType
    }

    /**
     * @param rw the current transaction
     * @param sampleCategoryName the name for the new record
     * @param name *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Sample recordSample(final WritableVersion rw, final String sampleCategoryName,
        final String name) throws AccessException, ConstraintException {

        final SampleCategory category = Creator.recordSampleCategory(rw, sampleCategoryName);

        final Map<String, Object> sampleProp = Util.getNewMap();
        sampleProp.put(AbstractSample.PROP_NAME, name);
        sampleProp.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(category));
        sampleProp.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        Sample sample = null;
        sample = (Sample) Util.getOrCreate(rw, Sample.class, sampleProp);
        // was  sample.setRefSample(refSample);
        return sample;
    }

}
