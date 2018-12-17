/*
 * Created on 13-Sept-2006 16:24 @author Chris Morris, c.morris@dl.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 Daresbury Lab Daresbury, Warrington WA4 4AD, UK
 */
package org.pimslims.lab.sample;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.HazardPhrase;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;

/**
 * utility methods for creating and managing solutions and other samples
 * 
 */
public class SampleFactory {

    /**
     * The default unit for volume
     */
    private static final String VOLUME_UNIT = "L";

    /**
     * The default unit for concentration.
     */
    private static final String CONC_UNIT = "M";

    /**
     * Constructor
     * 
     */
    private SampleFactory() {
        // this class contains static method only
    }

    /**
     * Changes an output sample to reflect the addition of some of another input sample. It is the caller's
     * responsibility to update the instance of experiment.
     * 
     * @param output the existing output sample
     * @param input the input sample to add some of
     * @param amount the amount to add, in L
     * @throws ConstraintException
     */
    public static void mix(final Sample output, final Sample input, final float amount)
        throws ConstraintException {
        assert SampleFactory.VOLUME_UNIT.equals(output.getAmountUnit());
        assert null != input;
        assert null != input.getCurrentAmount();
        final float oldOutputVolume = output.getCurrentAmount().floatValue();

        // set the volumes
        input.setCurrentAmount(input.getCurrentAmount().floatValue() - amount);
        input.setCurrentAmountFlag(false); // calculated not measured
        output.setCurrentAmount(oldOutputVolume + amount);
        output.setCurrentAmountFlag(false); // calculated not measured

        // scale the old concentrations
        Collection<SampleComponent> components = output.getSampleComponents();
        for (final SampleComponent component : components) {
            assert SampleFactory.CONC_UNIT.equals(component.getConcentrationUnit());
            // LATER check for match with existing component of sample
            final float concentration =
                component.getConcentration().floatValue() * oldOutputVolume / (oldOutputVolume + amount);
            component.setConcentration(concentration);
        }

        // add the new sample components
        components = input.getSampleComponents();
        for (final SampleComponent component : components) {
            assert SampleFactory.CONC_UNIT.equals(component.getConcentrationUnit());
            // LATER check for match with existing component of sample
            final float concentration =
                component.getConcentration().floatValue() * amount / (oldOutputVolume + amount);
            final Map<String, Object> scAttributes = new HashMap<String, Object>();
            scAttributes.put(SampleComponent.PROP_CONCENTRATION, concentration);
            scAttributes.put(SampleComponent.PROP_CONCENTRATIONUNIT, SampleFactory.CONC_UNIT);
            scAttributes.put(SampleComponent.PROP_REFCOMPONENT, component.getRefComponent());
            scAttributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, output);
            new SampleComponent((WritableVersion) output.get_Version(), scAttributes);
        }
    }

    /**
     * Calculates the number of moles of a specific component that are present in a particular sample.
     * 
     * @param sample
     * @param component
     * @throws IllegalStateException if the calculation cannot be performed, e.g. because the concentration is
     *             stated in w/w and the molecular weight is not known.
     * @return
     * 
     */
    public static float getMoles(final Sample sample, final Substance component) {
        final SampleComponent sc =
            (SampleComponent) sample.findFirst(AbstractSample.PROP_SAMPLECOMPONENTS,
                SampleComponent.PROP_REFCOMPONENT, component);
        if (sc == null) {
            return 0.0f;
        }
        final float concentration = sc.getConcentration().floatValue();
        final float volume = sample.getCurrentAmount().floatValue();
        if (!"M".equals(sc.getConcentrationUnit())) {
            throw new IllegalStateException("only M concentration supported");
            // LATER could convert concentration if abstract component specifies
            // molecular weight
        }
        if (!SampleFactory.VOLUME_UNIT.equals(sample.getAmountUnit())) {
            throw new IllegalStateException("only L amount -volume supported");
            // LATER could convert to volumes ?
        }
        final float moles = concentration * volume;
        return moles;
    }

    /**
     * Create a sample.
     * 
     * @param version - current transaction
     * @param name of the sample
     * @param sampleCategories
     * @param attributes
     * @return the new sample
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Sample createSample(final WritableVersion version, final String name,
        final Collection<SampleCategory> sampleCategories, final Map<String, Object> attributes)
        throws ConstraintException, AccessException {

        final Map<String, Object> sampleAttrMap = new java.util.HashMap<String, Object>(attributes);
        sampleAttrMap.put(AbstractSample.PROP_NAME, name);
        sampleAttrMap.put(Sample.PROP_AMOUNTUNIT, SampleFactory.VOLUME_UNIT); // standard for
        // PIMS
        if (null == sampleAttrMap.get(Sample.PROP_CURRENTAMOUNT)) {
            sampleAttrMap.put(Sample.PROP_CURRENTAMOUNT, new Float(0f));
        }
        if (null != sampleCategories) {
            sampleAttrMap.put(AbstractSample.PROP_SAMPLECATEGORIES, sampleCategories);
        }
        if (!sampleAttrMap.containsKey(AbstractSample.PROP_ISACTIVE)) {
            sampleAttrMap.put(AbstractSample.PROP_ISACTIVE, Boolean.TRUE);
        }
        return new Sample(version, sampleAttrMap);
    }

    /**
     * Define a sample representing a solution with a specified chemical composition. This method returns a
     * new instance of Sample. It does not record the pipetting of the solution.
     * 
     * @param version - current transaction
     * @param volume L wanted
     * @param components abstract component => molarity wanted
     * @return
     */
    public static org.pimslims.model.sample.Sample defineSolution(final WritableVersion version,
        final String name, final float volume, final java.util.Map components,
        final java.util.ArrayList sampleCategories) throws ConstraintException, AccessException {

        final Map<String, Object> sampleAttrMap = new java.util.HashMap<String, Object>();
        sampleAttrMap.put(Sample.PROP_INITIALAMOUNT, new Float(volume));
        sampleAttrMap.put(Sample.PROP_CURRENTAMOUNT, new Float(volume));
        sampleAttrMap.put(Sample.PROP_AMOUNTUNIT, SampleFactory.VOLUME_UNIT);
        final Sample sample =
            SampleFactory.createSample(version, name, new HashSet(sampleCategories), sampleAttrMap);

        for (final Iterator iter = components.keySet().iterator(); iter.hasNext();) {
            final Substance refComponent = (Substance) iter.next();
            final SampleComponent sc = new SampleComponent(version, refComponent, sample);
            sc.setConcentration((Float) components.get(refComponent)); // LATER:
            // check
            // how
            // components
            // map
            // is
            // used
            sc.setConcentrationUnit(SampleFactory.CONC_UNIT);
        }
        return sample;
    }

    /**
     * Create a RefSample
     * 
     * @param version current transaction
     * @param name name of the RefSample
     * @param attributes map field name / value
     * @return the new RefSample
     * @throws ConstraintException
     * @throws AccessException
     * 
     *             public static RefSample createRefSample( final WritableVersion version, final String name,
     *             final Map attributes ) throws ConstraintException, AccessException { Map<String, Object> a
     *             = new java.util.HashMap<String, Object>(attributes); a.put(RefSample.PROP_NAME, name);
     *             return new RefSample(version, a); }
     */

    /**
     * Get a RefSample, or create it if it does not exist already
     * 
     * @param version current transaction
     * @param name the name of refSample
     * @param categories one or more sample categories it is in
     * @return the refSample
     * @throws ConstraintException
     * @throws AccessException
     * 
     */
    // use findRefSample, except when loading reference data
    public static RefSample getRefSample(final WritableVersion version, final String name,
        final Collection<SampleCategory> sampleCategories) throws ConstraintException, AccessException {
        // attributes map
        final Map<String, Object> attributes = new java.util.HashMap<String, Object>();

        // Get or create the refSample
        attributes.put(AbstractSample.PROP_NAME, name);
        // attributes.put(RefSample.PROP_SAMPLECATEGORIES, sampleCategories);
        final java.util.Collection<RefSample> refSamplesFound = version.findAll(RefSample.class, attributes);
        assert refSamplesFound.size() <= 1 : "Invalid database state: duplicate ref samples";
        RefSample refSampleFound = null;
        if (refSamplesFound.isEmpty()) {
            //causes PiMS 1632
            //if (!Access.ADMINISTRATOR.equals(version.getUsername())) {
            //    throw new IllegalArgumentException("RefSample not found: " + name);
            //}

            refSampleFound = new RefSample(version, attributes);
            refSampleFound.setSampleCategories(sampleCategories);

        } else {
            refSampleFound = refSamplesFound.iterator().next();
        }

        return refSampleFound;
    }

    public static RefSample findRefSample(final WritableVersion version, final String name)
        throws ConstraintException, AccessException {
        // attributes map
        final Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        attributes.put(AbstractSample.PROP_NAME, name);
        final java.util.Collection<RefSample> refSamplesFound = version.findAll(RefSample.class, attributes);
        assert refSamplesFound.size() <= 1 : "Invalid database state: duplicate ref samples";
        if (refSamplesFound.isEmpty()) {
            return null;
        }
        return refSamplesFound.iterator().next();
    }

    /**
     * Get or create a new HazardPhrase This method will be used rarely to create, hazard phrases are
     * reference data.
     * 
     * @param version
     * @param classification
     * @param code
     * @param attributes
     * @return the new HazardPhrase
     * @throws ConstraintException
     * @throws AccessException
     */
    public static HazardPhrase createHazardPhrase(final WritableVersion version, final String classification,
        final String code, final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put(HazardPhrase.PROP_CLASSIFICATION, classification);
        a.put(HazardPhrase.PROP_CODE, code);
        final java.util.Collection hazardPhrasesFound = version.findAll(HazardPhrase.class, a);
        if (hazardPhrasesFound.isEmpty()) {
            a.putAll(attributes);
            return new HazardPhrase(version, a);
        }
        return (HazardPhrase) hazardPhrasesFound.iterator().next();
    }

    /**
     * get the direction of sample base on outputSample
     */
    public static Boolean isForwardDirection(final Sample sample) {
        final OutputSample os = sample.getOutputSample();
        if (os == null) {
            return null;
        }
        if (os.getName() == null) {
            return null;
        }
        if (os.getName().toLowerCase().startsWith("f")) {
            return true;
        } else if (os.getName().toLowerCase().startsWith("r")) {
            return false;
        } else {
            return null;
        }
    }

    /**
     * create a molcomponent of sample
     * 
     * @throws ConstraintException
     */
    public static SampleComponent LinkSampleWithMolComponent(final WritableVersion version,
        final Sample sample, final Molecule mc) throws ConstraintException {
        return (new SampleComponent(version, mc, sample));

    }

    //@Deprecated
    /*
     * This method is not used 
     * 
     * This method has been deprecated, replaced by getSamplesInSampleCategory(ReadableVersion, Sample) below
     * in test getSamplesInCategory took 2.6 seconds, getSamplesInSampleCategory 0 seconds
     */
    /*
    public static Collection<Sample> getSamplesInCategory(final ReadableVersion version,
        final Sample thisSample) throws IOException {

        final Collection<Sample> results = version.getAll(Sample.class);
        final Set<Sample> myResults = new HashSet<Sample>();

        final Collection<SampleCategory> categories = thisSample.getSampleCategories();

        for (final SampleCategory category : categories) {
            for (final Sample sample : results) {
                if (null != sample.getIsActive() && sample.getIsActive()) {
                    for (final SampleCategory sampleCategory : sample.getSampleCategories()) {
                        if (category.getName().equals(sampleCategory.getName())) {
                            if (thisSample.getDbId() != sample.getDbId()) {
                                myResults.add(sample);
                            }
                        }
                    }
                }
            }
        }
        final List<Sample> myList = new ArrayList<Sample>();
        myList.addAll(myResults);
        Collections.sort(myList, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return myList;
    }

    public static Collection<Sample> getSamplesInSampleCategory(final ReadableVersion version,
        final Sample thisSample) throws IOException {

        final Set<Sample> myResults = new HashSet<Sample>();
        final Collection<SampleCategory> categories = thisSample.getSampleCategories();

        for (final SampleCategory category : categories) {
            for (final AbstractSample abstractSample : category.getAbstractSamples()) {
                if (abstractSample instanceof Sample) {
                    if (null != abstractSample.getIsActive() && abstractSample.getIsActive()) {
                        if (thisSample.getDbId() != abstractSample.getDbId()) {
                            myResults.add((Sample) abstractSample);
                        }
                    }
                }
            }
        }
        final List<Sample> myList = new ArrayList<Sample>();
        myList.addAll(myResults);
        Collections.sort(myList, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return myList;
    }
    */
}
