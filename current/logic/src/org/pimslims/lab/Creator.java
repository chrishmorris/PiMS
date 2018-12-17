package org.pimslims.lab;

/**
 * current-pims-web org.pimslims.command.leeds.fermentation FermentationFormBean.java
 * 
 * @author Petr Troshin aka pvt43
 * @date 21 May 2007
 * 
 *       Protein Information Management System
 * 
 * 
 *       Copyright (c) 2008 pvt43 *
 * 
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;

@Deprecated
// used only in unsupoorted code
public class Creator {

    /**
     * Make a holder
     * 
     * @param rw the current transaction
     * @param holderCategory name of the holder category
     * @param boxName holder name
     * @return the new holder
     * @throws AccessException if the current user is not allowed to do that
     * @throws ConstraintException if there is already a holder of that name
     */
    @Deprecated
    // only for Leeds
    public static Holder recordHolder(final WritableVersion rw, final String holderCategory,
        final String boxName, final HolderType type) throws AccessException, ConstraintException {
        final Map<String, Object> holderCatProp = Util.getNewMap();
        holderCatProp.put(HolderCategory.PROP_NAME, holderCategory);
        final HolderCategory holderCat =
            Util.getOrCreate(rw, org.pimslims.model.reference.HolderCategory.class, holderCatProp);

        Holder holder = rw.findFirst(Holder.class, AbstractHolder.PROP_NAME, boxName);
        if (null == holder) {
            holder = new Holder(rw, boxName, type);
            holder.setHolderCategories(Collections.singleton(holderCat));
        }
        return holder;

    }

    /**
     * Make a holder
     * 
     * @param rw the current transaction
     * @param holderCategory name of the holder category
     * @param boxName holder name
     * @return the new holder
     * @throws AccessException if the current user is not allowed to do that
     * @throws ConstraintException if there is already a holder of that name
     */
    public static Holder recordHolder(final WritableVersion rw, final HolderCategory holderCategory,
        final String boxName, final HolderType holderType) throws AccessException, ConstraintException {

        final HashMap params = new HashMap();
        params.put(AbstractHolder.PROP_NAME, boxName);
        final Holder holder = Util.getOrCreate(rw, Holder.class, params);

        holder.setHolderCategories(Collections.singleton(holderCategory));
        holder.setHolderType(holderType);
        return holder;
    }

    /**
     * @param rw the current transaction
     * @param locationName the name of the new location
     * @return the new location
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    @Deprecated
    // Location is obsolete
    public static Location recordLocation(final WritableVersion rw, final String locationName)
        throws AccessException, ConstraintException {
        final Map<String, Object> locProp = Util.getNewMap();
        locProp.put(Location.PROP_NAME, locationName);
        final Location location =
            (Location) Util.getOrCreate(rw, org.pimslims.model.location.Location.class, locProp);
        return location;
    }

    /**
     * @param rw the current transaction
     * @param experimentType the name of the experiment type to create
     * @return the new experiment type
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    // this creates an experiment type with the wrong owner
    public static ExperimentType findExperimentType(final ReadableVersion version, final String experimentType)
        throws AccessException {
        /*final Map<String, Object> expTypeProp = Util.getNewMap();
        expTypeProp.put(ExperimentType.PROP_NAME, experimentType);
        final ExperimentType expType =
            (ExperimentType) Util.getOrCreate(rw, ExperimentType.class, expTypeProp);
        return expType; */
        return version.findFirst(ExperimentType.class, ExperimentType.PROP_NAME, experimentType);
    }

    /**
     * @param rw the current transaction
     * @param sampleCategory the Sample Category for the new record
     * @param name *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Sample recordSample(final WritableVersion rw, final SampleCategory category,
        final String name) throws AccessException, ConstraintException {

        final Map<String, Object> sampleProp = Util.getNewMap();
        sampleProp.put(AbstractSample.PROP_NAME, name);
        sampleProp.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(category));
        sampleProp.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        Sample sample = null;
        sample = (Sample) Util.getOrCreate(rw, Sample.class, sampleProp);
        // was  sample.setRefSample(refSample);
        return sample;
    }

    /**
     * @param rw the current transaction
     * @param orgName *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do thatn
     * @throws ConstraintException if the name is already in use
     * @throws ClassNotFoundException
     */
    public static Organisation recordOrganisation(final WritableVersion rw, final String orgName)
        throws AccessException, ConstraintException {
        final Map<String, Object> orgProp = Util.getNewMap();
        orgProp.put(Organisation.PROP_NAME, orgName);
        return (Organisation) Util.getOrCreate(rw, Organisation.class, orgProp);
    }

    /**
     * @param rw the current transaction
     * @param rsample the ref sample
     * @param organisation *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static ReagentCatalogueEntry recordRefSampleSource(final WritableVersion rw,
        final RefSample rsample, final Organisation organisation) throws AccessException, ConstraintException {

        final Map<String, Object> rsProp = Util.getNewMap();
        rsProp.put(ReagentCatalogueEntry.PROP_REAGENT, rsample);
        rsProp.put(ReagentCatalogueEntry.PROP_CATALOGNUM, "999");
        rsProp.put(ReagentCatalogueEntry.PROP_SUPPLIER, organisation);
        ReagentCatalogueEntry rfsource;

        rfsource = (ReagentCatalogueEntry) Util.create(rw, ReagentCatalogueEntry.class, rsProp);

        return rfsource;
    }

    /**
     * @param version the current transaction
     * @param sample the sample
     * @param experiment the experiment
     * @param outputSampleName *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static OutputSample recordOutputSamples(final WritableVersion version, final Sample sample,
        final Experiment experiment, final String outputSampleName) throws AccessException,
        ConstraintException {

        final Map<String, Object> attrMap = new HashMap<String, Object>();

        if (null != experiment.getProtocol()) {
            final Map<String, Object> search = new HashMap<String, Object>();
            search.put(RefOutputSample.PROP_NAME, outputSampleName);
            final RefOutputSample ris =
                experiment.getProtocol().findFirst(Protocol.PROP_REFOUTPUTSAMPLES, search);
            //  assert null != ris : "No such input sample in protocol: " + outputSampleName;
            if (ris != null) {
                attrMap.put(OutputSample.PROP_REFOUTPUTSAMPLE, ris);
            }
        }

        attrMap.put(OutputSample.PROP_EXPERIMENT, experiment);
        attrMap.put(OutputSample.PROP_NAME, outputSampleName);
        attrMap.put(OutputSample.PROP_AMOUNT, new Float(0f));
        attrMap.put(OutputSample.PROP_AMOUNTUNIT, "L");

        OutputSample outputSample;

        outputSample = (OutputSample) Util.create(version, OutputSample.class, attrMap);

        outputSample.setSample(sample);
        return outputSample;
    }

    /**
     * @param rw the current transaction
     * @param paramName the name
     * @param paramType the type, e.g. String
     * @param paramValue the value
     * @param experiment *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static ModelObject recordParameters(final WritableVersion rw, final String paramName,
        final String paramType, final String paramValue, final ModelObject experiment)
        throws AccessException, ConstraintException {
        final Map<String, Object> paramProp = Util.getNewMap();
        paramProp.put(Parameter.PROP_NAME, paramName);
        paramProp.put(Parameter.PROP_PARAMTYPE, paramType); // "InputSample"
        paramProp.put(Parameter.PROP_VALUE, paramValue);
        paramProp.put(Parameter.PROP_EXPERIMENT, experiment);
        final ModelObject parameter = Util.getOrCreate(rw, Parameter.class, paramProp);
        //System.out.println("parameter [" + parameter.get_Hook() + ":" + paramName + ":" + paramValue + "]");
        return parameter;
    }

    /**
     * @param rw the current transaction
     * @param paramName the name
     * @param paramType the type, e.g. String
     * @param paramValue the value
     * @param experiment *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Parameter recordParameters(final WritableVersion rw, final ParameterDefinition paramDef,
        final String paramValue, final ModelObject experiment) throws AccessException, ConstraintException {
        final Map<String, Object> paramProp = Util.getNewMap();
        paramProp.put(Parameter.PROP_NAME, paramDef.getName());
        paramProp.put(Parameter.PROP_PARAMETERDEFINITION, paramDef);
        paramProp.put(Parameter.PROP_PARAMTYPE, paramDef.getParamType()); // "InputSample"
        paramProp.put(Parameter.PROP_VALUE, paramValue);
        paramProp.put(Parameter.PROP_EXPERIMENT, experiment);
        final Parameter parameter = Util.getOrCreate(rw, Parameter.class, paramProp);
        //System.out.println("parameter [" + parameter.get_Hook() + ":" + paramName + ":" + paramValue + "]");
        return parameter;
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record
     * @param sequence *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Molecule recordDNA(final WritableVersion rw, final String name, final String sequence)
        throws AccessException, ConstraintException {

        return Creator.recordMolComponent(rw, name, sequence, "DNA");
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record
     * @param sequence *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static Molecule recordProtein(final WritableVersion rw, final String name, final String sequence)
        throws AccessException, ConstraintException {

        return Creator.recordMolComponent(rw, name, sequence, "protein");
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record
     * @param sequence the sequence as a string
     * @param type *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do thatn
     * @throws ConstraintException if the name is already in use
     * @throws ClassNotFoundException
     */
    public static Molecule recordMolComponent(final WritableVersion rw, final String name,
        final String sequence, final String type) throws AccessException, ConstraintException {
        final Map<String, Object> molcompProp = Util.getNewMap();
        molcompProp.put(Substance.PROP_NAME, name);
        molcompProp.put(Molecule.PROP_SEQUENCE, sequence);
        molcompProp.put(Molecule.PROP_MOLTYPE, type);
        // molcompProp.put(Molecule.PROP_CATEGORIES,Utils.makeList(value));
        final Molecule molComponent =
            (Molecule) Util.getOrCreate(rw, org.pimslims.model.molecule.Molecule.class, molcompProp);
        return molComponent;
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record
     * @param molType the type
     * @param sequence the sequence as a string
     * @param direction ?
     * @param universal ?
     * @param meltingTemp *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do thatn
     * @throws ConstraintException if the name is already in use
     * @throws ClassNotFoundException
     */
    public static Primer recordPrimer(final WritableVersion rw, final String name, final String molType,
        final String sequence, final String direction, final boolean universal, final float meltingTemp)
        throws AccessException, ConstraintException, ClassNotFoundException {

        final Map<String, Object> primerProp = Util.getNewMap();
        // HashMap<String, Object> attributes = new HashMap<String, Object>();
        primerProp.put(Substance.PROP_NAME, name);
        primerProp.put(Molecule.PROP_MOLTYPE, molType);
        primerProp.put(Molecule.PROP_SEQUENCE, sequence);
        primerProp.put(Primer.PROP_DIRECTION, direction);
        primerProp.put(Primer.PROP_ISUNIVERSAL, universal);
        primerProp.put(Primer.PROP_MELTINGTEMPERATURE, meltingTemp);
        // return rw.create(org.pimslims.model.molecule.Primer.class, attributes);
        return (Primer) Util.getOrCreate(rw, org.pimslims.model.molecule.Primer.class, primerProp);
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record *
     * @return an object representing the new record the new RefSample
     * @throws AccessException if the user is not allowed to do than
     * @throws ConstraintException if the name is already in use
     */
    public static RefSample recordRefSample(final WritableVersion rw, final String name)
        throws AccessException, ConstraintException {
        final SampleCategory sampleCat = Creator.recordSampleCategory(rw, name);
        //System.out.println("recordRefSample [" + sampleCat.get_Hook() + "]");
        final RefSample refSample = SampleFactory.getRefSample(rw, name, Collections.singleton(sampleCat));
        return refSample;
    }

    /**
     * @param rw the current transaction
     * @param name the name for the new record
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do thatn
     * @throws ConstraintException if the name is already in use //TODO this will fail if user is not admin
     */
    @Deprecated
    public static SampleCategory recordSampleCategory(final WritableVersion rw, final String name)
        throws AccessException, ConstraintException {
        final Map<String, Object> sampleCatProp = Util.getNewMap();
        sampleCatProp.put(SampleCategory.PROP_NAME, name);
        final SampleCategory sampleCat =
            (SampleCategory) Util.getOrCreate(rw, SampleCategory.class, sampleCatProp);
        return sampleCat;
    }

    /**
     * @param version the current transaction
     * @param sample the sample
     * @param experiment the experiment
     * @param inputSampleName *
     * @return an object representing the new record
     * @throws AccessException if the user is not allowed to do that
     * @throws ConstraintException if the name is already in use
     */
    public static InputSample recordInputSamples(final WritableVersion version, final Sample sample,
        final Experiment experiment, final String inputSampleName) throws AccessException,
        ConstraintException {
        final Map<String, Object> attrMap = new HashMap<String, Object>();
        assert sample != null : "Sample is Null!";
        if (null != experiment.getProtocol()) {
            final Map<String, Object> search = new HashMap<String, Object>();
            search.put(RefInputSample.PROP_NAME, inputSampleName);
            final RefInputSample ris =
                experiment.getProtocol().findFirst(Protocol.PROP_REFINPUTSAMPLES, search);
            assert null != ris : "No such input sample in protocol: " + inputSampleName;
            attrMap.put(InputSample.PROP_REFINPUTSAMPLE, ris);
        }

        attrMap.put(InputSample.PROP_EXPERIMENT, experiment);
        attrMap.put(InputSample.PROP_NAME, inputSampleName);
        attrMap.put(InputSample.PROP_AMOUNT, new Float(0f));
        attrMap.put(InputSample.PROP_AMOUNTUNIT, "L");
        final InputSample inputSample = (InputSample) Util.create(version, InputSample.class, attrMap);
        inputSample.setSample(sample);

        // process research objective
        if (null == experiment.getProject() && null != sample.getOutputSample()) {
            final Experiment source = sample.getOutputSample().getExperiment();
            if (null != source) {
                experiment.setProject(source.getResearchObjective());
            }
        }

        return inputSample;
    }

}
