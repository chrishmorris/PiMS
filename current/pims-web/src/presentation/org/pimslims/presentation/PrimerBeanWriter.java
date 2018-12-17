/**
 * pims-web org.pimslims.presentation PrimerBeanWriter.java
 * 
 * @author Marc Savitsky
 * @date 28 Sep 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FlushMode;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.dao.WritableVersionImpl;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.ContainerUtility;
import org.pimslims.lab.CustomConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.Util;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sample.SampleUtility;
import org.pimslims.leeds.FormFieldsNames;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.holder.HolderLocation;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

public class PrimerBeanWriter {

    public static Sample writePrimer(final WritableVersion version, final PrimerBean pb, Experiment experiment)
        throws AccessException, ConstraintException {
        // improve performance TODO no, do this in caller
        final boolean oldFlushModel =
            ((WritableVersionImpl) version).getFlushMode().isFlushSessionAfterCreate();
        final FlushMode flushMode = version.getSession().getFlushMode();
        version.setFlushModeCommit();
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(false);

        //System.out.println("org.pimslims.presentation.PrimerBeanWriter.writePrimer(" + pb.getName() + ":"
        //    + pb.getSequence() + ":" + pb.isDirect() + ")");

        if ("".equals(pb.getName()) || pb.getName() == null || pb.getSequence() == null
            || pb.isDirect() == null) {
            throw new AssertionError("Name, sequence and direction are required ");
        }

        final Sample sample = PrimerBeanWriter.createPrimerSample(version, pb);
        assert null != sample;
        if (null == experiment) {
            experiment =
                PrimerBeanWriter.createExperiment(version, sample.getAccess(), pb.getName(),
                    org.pimslims.leeds.FormFieldsNames.primersDesign);
        }

        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(OutputSample.PROP_SAMPLE, sample);
        attributes.put(OutputSample.PROP_EXPERIMENT, experiment);
        attributes.put(OutputSample.PROP_NAME, pb.getFormFieldsDirection());
        version.create(org.pimslims.model.experiment.OutputSample.class, attributes);
        //System.out.println("OS:" + pb.getFormFieldsDirection() + ":" + sample);  
        // save the tag
        final Parameter parm = new Parameter(version, experiment);
        parm.setName(StringUtils.capitalize(pb.getDirection()) + " Tag");
        parm.setValue(pb.getTag());

        // back to previous
        ((WritableVersionImpl) version).getFlushMode().setFlushSessionAfterCreate(oldFlushModel);

        version.flush();

        version.getSession().setFlushMode(flushMode);

        PrimerBeanWriter.update(version, pb);
        return sample;
    }

    private static Sample createPrimerSample(final WritableVersion version, final PrimerBean pb)
        throws AccessException, ConstraintException {
        assert null != pb.getSequence();
        final String direction = pb.getFormFieldsDirection();
        final SampleCategory sampleCat = PrimerBeanWriter.getSampleCategory(version, direction);
        final Sample oldSample = version.findFirst(Sample.class, AbstractSample.PROP_NAME, pb.getName());
        if (null != oldSample) {
            final Set<SampleComponent> components = oldSample.getSampleComponents();
            for (final Iterator iterator = components.iterator(); iterator.hasNext();) {
                final SampleComponent component = (SampleComponent) iterator.next();
                if (!(component.getRefComponent() instanceof Primer)) {
                    continue;
                }
                final Primer oldPrimer = (Primer) component.getRefComponent();
                if (pb.getSequence().equals(oldPrimer.getSeqString())) {
                    return oldSample;
                }
            }
            // name clash, and this one has different sequence
            final String name = version.getUniqueName(Sample.class, pb.getName());
            pb.setName(name);
        }

        final org.pimslims.model.molecule.Primer primer = PrimerBeanWriter.createPrimerMolecule(version, pb);

        final Map<String, Object> sampleProp = new HashMap<String, Object>();
        sampleProp.put(AbstractSample.PROP_NAME, pb.getName());
        sampleProp.put(AbstractSample.PROP_SAMPLECATEGORIES, Collections.singleton(sampleCat));
        sampleProp.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        if (null != pb.getAssignTo()) {
            sampleProp.put(Sample.PROP_ASSIGNTO, pb.getAssignTo());
        }
        final Sample sample = version.create(Sample.class, sampleProp);
        //sample.setRefSample(PrimerBeanWriter.getRefSample(version, direction));
        pb.setSample(sample); //TODO was to improve performance, but makes bean depend on transaction 

        /* was  If the same primer was loaded from Order do not proceed
        if (sample.getSampleComponents() != null && !sample.getSampleComponents().isEmpty()) {
            throw new CustomConstraintException("This primer '" + sampleName
                + "' has been recorded! Please update it manually, "
                + "or change a primer name to record a new primer", "Primer name", sampleName,
                "org.pimslims.model.sample.Sample");
        } */

        final Map<String, Object> sampleComponentProp = new HashMap<String, Object>();
        sampleComponentProp.put(SampleComponent.PROP_REFCOMPONENT, primer);
        sampleComponentProp.put(SampleComponent.PROP_ABSTRACTSAMPLE, sample);
        version.create(org.pimslims.model.sample.SampleComponent.class, sampleComponentProp);

        return sample;
    }

    /**
     * PrimerBeanWriter.getRefSample
     * 
     * @param version
     * @param direction
     * @return private static RefSample getRefSample(final ReadableVersion version, final String name) { final
     *         RefSample ret = version.findFirst(RefSample.class, AbstractSample.PROP_NAME, name); if (null ==
     *         ret) { throw new IllegalStateException("Please import reference data, no recipe for: " + name);
     *         } return ret; }
     */

    private static org.pimslims.model.molecule.Primer createPrimerMolecule(final WritableVersion version,
        final PrimerBean pb) throws AccessException, ConstraintException {
        final Map<String, Object> primerProp = new HashMap<String, Object>();
        primerProp.put(Substance.PROP_NAME, pb.getName());
        primerProp.put(Molecule.PROP_MOLTYPE, "DNA");
        primerProp.put(Molecule.PROP_SEQSTRING, pb.getSequence());
        primerProp.put(org.pimslims.model.molecule.Primer.PROP_DIRECTION, pb.getDirection());
        primerProp.put(org.pimslims.model.molecule.Primer.PROP_ISUNIVERSAL, false);
        primerProp.put(org.pimslims.model.molecule.Primer.PROP_MELTINGTEMPERATURE, pb.getTmfullasFloat());

        final org.pimslims.model.molecule.Primer primer = version.create(Primer.class, primerProp);

        return primer;
    }

    private static SampleCategory getSampleCategory(final WritableVersion version, final String direction)
        throws AccessException, ConstraintException {
        final Map<String, Object> sampleCatProp = new HashMap<String, Object>();
        sampleCatProp.put(SampleCategory.PROP_NAME, direction);
        final SampleCategory sampleCat =
            (SampleCategory) Util.getOrCreate(version, SampleCategory.class, sampleCatProp);
        return sampleCat;
    }

    /*
     * Be careful with the order of methods in update!!! order does matter!
     */

    public static void update(final WritableVersion version, final PrimerBean pb) throws ConstraintException,
        AccessException {

        if (!Util.isEmpty(pb.getName())) {
            PrimerBeanWriter.setName(version, pb);
        }

        final Primer primer = PrimerBeanWriter.getPrimer(version, pb);

        if (!Util.isEmpty(pb.getSequence())) {
            primer.setSeqString(pb.getSequence());
        }

        if (!Util.isEmpty(pb.getParticularity())) {
            PrimerBeanWriter.setParticularity(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getRestrictionsite())) {
            PrimerBeanWriter.setRestrictionSite(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getLengthOnGeneString())) {
            PrimerBeanWriter.setLengthOnGene(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getTmGene())) {
            PrimerBeanWriter.saveTmGene(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getGCGene())) {
            PrimerBeanWriter.saveGCGene(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getTmseller())) {
            PrimerBeanWriter.setTmSeller(version, pb, primer);
        }

        if (pb.getConcentration() != null) {
            PrimerBeanWriter.setConcentration(version, pb);
        }

        if (pb.getAmount() != null) {
            PrimerBeanWriter.setAmount(version, pb);
        }

        if (pb.getMolecularMass() != null) {
            PrimerBeanWriter.setMolecularMass(version, pb);
        }

        if (!Util.isEmpty(pb.getOD())) {
            PrimerBeanWriter.setOD(version, pb, primer);
        }

        if (!Util.isEmpty(pb.getBox())) {
            PrimerBeanWriter.setBox(version, pb);
        }

        if (!Util.isEmpty(pb.getBoxBarcode())) {
            PrimerBeanWriter.setBoxBarcode(version, pb);
        }

        if (!Util.isEmpty(pb.getPosition())) {
            PrimerBeanWriter.setPosition(version, pb);
        }

        if (!Util.isEmpty(pb.getPositionBarcode())) {
            PrimerBeanWriter.setPositionBarcode(version, pb);
        }

        if (!Util.isEmpty(pb.getLocation())) {
            PrimerBeanWriter.setLocation(version, pb);
        }

        if (!Util.isEmpty(pb.getSeller())) {
            PrimerBeanWriter.setSeller(version, pb);
        }

        if (!Util.isEmpty(pb.getTmfull())) {
            PrimerBeanWriter.setTmFull(version, pb);
        }
        //System.out.println("PrimerBeanWriter.update [END]");
        //return primer;
    }

    public static Experiment createExperiment(final WritableVersion version, final LabNotebook access,
        final String name, final String type) throws AccessException, ConstraintException {
        assert version != null : "Please provide WritableVersion in the constructor";
        final ExperimentType expType = PrimerBeanWriter.recordExperimentType(version, type);
        final Experiment experiment =
            PrimerBeanWriter.recordExperiment(version, expType, name, access, null, null);
        /*System.out.println("org.pimslims.presentation.PrimerBeanWriter createExperiment ["
            + experiment.getDbId() + ":" + experiment.get_Name() + "]"); */
        return experiment;
    }

    @Deprecated
    // this is reference data
    public static ExperimentType recordExperimentType(final WritableVersion rw, final String experimentType)
        throws AccessException, ConstraintException {
        final Map<String, Object> expTypeProp = new HashMap<String, Object>();
        expTypeProp.put(ExperimentType.PROP_NAME, experimentType);
        final ExperimentType expType =
            (ExperimentType) Util.getOrCreate(rw, ExperimentType.class, expTypeProp);
        //System.out.println("org.pimslims.presentation.PrimerBeanWriter createExperimentType [" + expType.getDbId() + ":" + expType.get_Name() + "]");
        return expType;
    }

    public static boolean isExperimentNameUnique(final String experimentName, final ReadableVersion rv)
        throws AccessException {
        final HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(Experiment.PROP_NAME, experimentName);
        return PrimerBeanWriter.isUnique(rv, param, rv.getModel().getMetaClass(Experiment.class.getName()));
    }

    public static ModelObject recordParameters(final WritableVersion rw, final String paramName,
        final String paramType, final String paramValue, final ModelObject experiment)
        throws AccessException, ConstraintException, ClassNotFoundException {
        final HashMap<String, Object> paramProp = new HashMap<String, Object>();
        paramProp.put(Parameter.PROP_NAME, paramName); // "Marker"
        // paramProp.put(Parameter.PROP_PARAMETERDEFINITION,
        // getParamDefenition(plasmid.marker));
        paramProp.put(Parameter.PROP_PARAMTYPE, paramType); // "InputSample"
        paramProp.put(Parameter.PROP_VALUE, paramValue); // this.marker
        paramProp.put(Parameter.PROP_EXPERIMENT, experiment);
        final ModelObject parameter = Util.getOrCreate(rw, Parameter.class, paramProp);
        //System.out.println("parameter [" + parameter.get_Hook() + ":" + paramName + ":" + paramValue + "]");
        return parameter;
    }

    private static Holder recordHolder(final WritableVersion rw, final String holderCategory,
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

    @Deprecated
    // location is obsolete
    public static Location recordLocation(final WritableVersion rw, final String locationName)
        throws AccessException, ConstraintException {
        final HashMap<String, Object> locProp = new HashMap<String, Object>();
        locProp.put(Location.PROP_NAME, locationName);
        final Location location =
            (Location) Util.getOrCreate(rw, org.pimslims.model.location.Location.class, locProp);
        return location;
    }

    private static void setParticularity(final WritableVersion version, final PrimerBean bean,
        final Primer primer) throws ConstraintException, AccessException {
        primer.setParticularity(bean.getParticularity());
    }

    private static Holder getSampleHolder(final WritableVersion version, final PrimerBean bean) {
        final Sample sample = bean.getSample(version);
        if (null != sample) {
            return sample.getHolder();
        }
        return null;
    }

    private static Holder getHolder(final WritableVersion version, final PrimerBean bean) {
        return (Holder) version.get(bean.getBox());
    }

    private static Organisation getOrganisation(final WritableVersion version, final PrimerBean bean) {
        return (Organisation) version.get(bean.getSeller());
    }

    private static Location getLocation(final WritableVersion version, final PrimerBean bean) {
        //Map<String, Object> locProp = new HashMap<String, Object>();
        //locProp.put(Location.PROP_NAME, bean.getLocation());
        //return (Location) version.findFirst(Location.class, locProp);
        return version.get(bean.getLocation());
    }

    private static org.pimslims.model.molecule.Primer getPrimer(final WritableVersion version,
        final PrimerBean bean) {
        final Sample sample = bean.getSample(version);
        final SampleComponent scomp = sample.getSampleComponents().iterator().next();
        return version.get(scomp.getRefComponent().get_Hook());
    }

    private static void setTmFull(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        PrimerBeanWriter.getPrimer(version, bean).setMeltingTemperature(new Float(bean.getTmfull()));
    }

    private static void setName(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        bean.getSample(version).setName(bean.getName());
        PrimerBeanWriter.getPrimer(version, bean).setName(bean.getName());
    }

    public static Organisation recordOrganisation(final WritableVersion rw, final String orgName)
        throws AccessException, ConstraintException {
        final Map<String, Object> orgProp = new HashMap<String, Object>();
        orgProp.put(Organisation.PROP_NAME, orgName);
        return (Organisation) Util.getOrCreate(rw, Organisation.class, orgProp);
    }

    private static void setSeller(final WritableVersion version, final PrimerBean bean)
        throws AccessException, ConstraintException {

        assert version != null : "Please provide WritableVersion in the constructor";

        if (Util.isEmpty(bean.getSeller())) {
            // Do nothing
            return;
        }

        Organisation organisation = null;
        if (Util.isHookValid(bean.getSeller())) {
            organisation = PrimerBeanWriter.getOrganisation(version, bean);
        }

        if (null == organisation) {
            final Map<String, Object> orgProp = new HashMap<String, Object>();
            orgProp.put(Organisation.PROP_NAME, bean.getSeller());
            organisation = version.findFirst(Organisation.class, orgProp);

            if (null == organisation) {
                organisation = PrimerBeanWriter.recordOrganisation(version, bean.getSeller());
            }
        }
        SampleUtility.linkSampleWithSupplier(version, bean.getSample(version), organisation);
    }

    private static void setRestrictionSite(final WritableVersion version, final PrimerBean bean,
        final Primer primer) throws AccessException, ConstraintException {
        primer.setRestrictionSite(bean.getRestrictionsite());
    }

    private static void setLengthOnGene(final WritableVersion version, final PrimerBean bean,
        final Primer primer) throws ConstraintException, AccessException {
        primer.setLengthOnGene(new Integer(bean.getLengthOnGeneString()));
    }

    private static void saveTmGene(final WritableVersion version, final PrimerBean bean, final Primer primer)
        throws ConstraintException, AccessException {
        primer.setMeltingTemperatureGene(bean.getTmGeneFloat());
    }

    private static void setOD(final WritableVersion version, final PrimerBean bean, final Primer primer)
        throws ConstraintException, AccessException {
        primer.setOpticalDensity(bean.getOD());
    }

    private static void saveGCGene(final WritableVersion version, final PrimerBean bean, final Primer primer)
        throws AccessException, ConstraintException {
        primer.setGcGene(bean.getGCGene());
    }

    private static void setTmSeller(final WritableVersion version, final PrimerBean bean, final Primer primer)
        throws ConstraintException, AccessException {
        primer.setMeltingTemperatureSeller(Float.valueOf(bean.getTmseller()));
    }

    private static void setConcentration(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException, AccessException {
        final Sample sample = bean.getSample(version);
        final Set<SampleComponent> sampleComps = sample.getSampleComponents();
        assert sampleComps.size() == 1 : "Primer could only have one sample component";
        final SampleComponent sampleComp = sampleComps.iterator().next();
        final Measurement m = Measurement.getMeasurement(bean.getConcentration().toString() + "mM");
        sampleComp.setConcentration(m.getFloatValue());
        sampleComp.setConcDisplayUnit(m.getDisplayUnit());
        sampleComp.setConcentrationUnit(m.getStorageUnit());
        //sampleComp.setConcentration(bean.getConcentration());
        //sampleComp.setConcDisplayUnit("mM");
        //sampleComp.setConcentrationUnit("M");
    }

    private static void setAmount(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        if (bean.getAmount() != null) {
            final Sample sample = bean.getSample(version);
            final Measurement m = Measurement.getMeasurement(bean.getAmount().toString() + "µg");
            sample.setCurrentAmount(m.getFloatValue());
            sample.setAmountDisplayUnit(m.getDisplayUnit());
            sample.setAmountUnit(m.getStorageUnit());
            //sample.setCurrentAmount(bean.getAmount());
            //sample.setAmountDisplayUnit("µg");
            //sample.setAmountUnit("kg");
        }
    }

    private static void setMolecularMass(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        if (bean.getMolecularMass() != null) {
            PrimerBeanWriter.getPrimer(version, bean).setMolecularMass(bean.getMolecularMass());
        }
    }

    private static void setBox(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException, AccessException {
        final Sample sample = bean.getSample(version);
        Holder holder = null;
        if (Util.isHookValid(bean.getBox())) {
            holder = PrimerBeanWriter.getHolder(version, bean);
        }

        if (null == holder) {
            assert version != null : "Please provide WritableVersion in the constructor";
            holder =
                PrimerBeanWriter.recordHolder(version, FormFieldsNames.boxHolderType, bean.getBox(), null);
        }
        sample.setHolder(holder);
    }

    private static void setBoxBarcode(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        final Holder box = PrimerBeanWriter.getSampleHolder(version, bean);
        Integer ibar = null;
        if (!Util.isEmpty(bean.getBoxBarcode())) {
            try {
                ibar = Integer.valueOf(bean.getBoxBarcode());
            } catch (final NumberFormatException e) {
                //  why do nothing?
                System.out.println("NumerFormatException thrown when settign a barcode for a box in Primer ["
                    + bean.getBoxBarcode() + "]");
                // e.printStackTrace();
                return;
            }
        }
        if (box != null && ibar != null) {
            box.setSubPosition(ibar);
        }
    }

    @Deprecated
    // location is obsolete
    private static void setLocation(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException, AccessException {

        //System.out.println("PrimerBeanWriter.setLocation(" + bean.getLocation() + ")");
        final Holder holder = PrimerBeanWriter.getSampleHolder(version, bean);
        assert holder != null : "Holder is null cannot set location";
        if (null == holder) {
            return;
        }

        //System.out.println("PrimerBeanWriter.setLocation holder(" + holder.get_Name() + ")");
        final Collection<HolderLocation> hlocs = holder.getHolderLocations();

        Location newLoc = PrimerBeanWriter.getLocation(version, bean);
        if (hlocs != null && null != newLoc) {

            assert newLoc != null : "Location is null cannot use it";

            ContainerUtility.move(holder, newLoc);
            //System.out.println("Location updated a " + newLoc.getName());
        } else {
            // Create HolderLocations
            assert version != null : "Please provide WritableVersion in the constructor";
            newLoc = PrimerBeanWriter.recordLocation(version, bean.getLocation());
            ContainerUtility.move(holder, newLoc);
            //System.out.println("Location updated b " + newLoc.getName());

        }
    }

    private static void setPosition(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {

        final Map<String, Object> holderProp = new HashMap<String, Object>();
        holderProp.put(AbstractHolderType.PROP_NAME, FormFieldsNames.boxHolderType);
        final HolderType holderType = version.findFirst(HolderType.class, holderProp);

        final int cpos = HolderFactory.getColumn(bean.getPosition());
        final int rpos = HolderFactory.getRow(bean.getPosition());
        /* Fix to the Leeds data positions bug -
        // some start from 0
        if (rpos == 0) {
            rpos += 1;
        }
        if (cpos == 0) {
            cpos += 1;
        } */

        final Sample sample = bean.getSample(version);
        sample.setColPosition(1 + cpos);
        sample.setRowPosition(1 + rpos);
    }

    private static void setPositionBarcode(final WritableVersion version, final PrimerBean bean)
        throws ConstraintException {
        if (!Util.isEmpty(bean.getPositionBarcode())) {
            bean.getSample(version).setBatchNum(bean.getPositionBarcode());
        }
    }

    private static Experiment recordExperiment(final WritableVersion rw, final ModelObject expType,
        final String experimentName, final LabNotebook access, final String comments, final Date date)
        throws AccessException, ConstraintException {

        final Map<String, Object> experimentProp = new HashMap();
        if (!PrimerBeanWriter.isExperimentNameUnique(experimentName, rw)) {
            throw new CustomConstraintException("Construct and/or Primer name must be unique", "name",
                experimentName, "org.pimslims.model.experiment.Experiment");
        }

        // experimentProp.put(Experiment.PROP_EXPERIMENTGROUP,
        // getExperimentGroup(plasmid));
        assert experimentName != null : "Name is null! ";
        experimentProp.put(Experiment.PROP_NAME, experimentName);
        if (access != null) {
            experimentProp.put(LabBookEntry.PROP_ACCESS, access);
        }
        experimentProp.put(Experiment.PROP_STARTDATE, Util.getCalendar(date));
        experimentProp.put(Experiment.PROP_ENDDATE, Util.getCalendar(null));
        if (comments != null) {
            experimentProp.put(LabBookEntry.PROP_DETAILS, comments.trim());
        }
        experimentProp.put(Experiment.PROP_EXPERIMENTTYPE, Util.makeList(expType));
        experimentProp.put(Experiment.PROP_STATUS, "OK");

        Experiment experiment;

        // Dataowner may not be set properly if the experiment has been already
        // created
        // experiment = (Experiment)Util.create(dataOwner, Experiment.class,
        // experimentProp);

        experiment = (Experiment) Util.create(rw, Experiment.class, experimentProp);

        return experiment;
    }

    /**
     * Was the similar object recorded?
     * 
     * @param rw the current transaction
     * @param criteria the search terms
     * @param metaClass the class to search
     * @return true if object(s) which meets criteria was found otherwise false
     */
    public static boolean isUnique(final ReadableVersion rw, final Map<String, Object> criteria,
        final MetaClass metaClass) {
        final ModelObject result = rw.findFirst(metaClass.getJavaClass(), criteria);
        if (result != null) {
            //System.out.println("Not NULL " + result.iterator().next().get_Name());
            return false;
        }
        return true;
    }

/*<<<<<<< .working
    public static boolean isHookValid(String hook) {
        if (hook == null)
            return false;
        if (hook.split("\\.").length < 4)
            return false;
        if (hook.trim().indexOf(':') < 0)
            return false;
        hook = hook.trim();
        String id = hook.substring(hook.indexOf(':') + 1).trim();
        if (id.length() == 0)
            return false;
        for (char c : id.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }
=======
>>>>>>> .merge-right.r11712 */
}
