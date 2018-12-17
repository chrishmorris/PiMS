/**
 * pims-web org.pimslims.presentation.vector VectorBean.java
 * 
 * @author pajanne
 * @date May 8, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.ModelObjectBean;

/**
 * VectorBean
 * 
 */
public class VectorBean extends ModelObjectBean {
    /**
     * BP String
     */
    private static final String BP = "bp";

    private String name;

    private String function;

    private String details;

    private Integer length;

    private String molType;

    private String sequenceType;

    private String constructStatus;

    private Set<ComponentCategory> categories;

    private List<FeatureBean> resistances;

    private List<FeatureBean> promoters;

    private List<FeatureBean> markers;

    private final String sequence;

    /**
     * 
     * Constructor for VectorBean
     * 
     * @param construct
     */
    public VectorBean(final Construct construct) {
        super(construct);
        this.name = construct.getName();
        this.function = construct.getFunction();
        this.details = construct.getDetails();
        if (null != construct.getSequence() && !"".equals(construct.getSequence())) {
            this.length = construct.getSequence().replaceAll(" ", "").length();
        } else {
            String conDetails = construct.getDetails();
            if (null != conDetails && conDetails.contains(VectorBean.BP)) {
                conDetails = conDetails.replace(VectorBean.BP, "");
                final int lengthDetails = Integer.parseInt(conDetails);
                this.length = lengthDetails;
            }
        }
        this.molType = construct.getMolType();
        this.sequenceType = construct.getSequenceType();
        this.constructStatus = construct.getConstructStatus();
        this.categories = new HashSet<ComponentCategory>(0);
        if (construct.getCategories().size() != 0) {
            this.categories.addAll(construct.getCategories());
        }
        this.resistances = new ArrayList<FeatureBean>(0);
        this.promoters = new ArrayList<FeatureBean>(0);
        this.markers = new ArrayList<FeatureBean>(0);
        if (construct.getMoleculeFeatures().size() != 0) {
            for (final MoleculeFeature feature : construct.getMoleculeFeatures()) {
                if ("resistance".equals(feature.getFeatureType())) {
                    this.resistances.add(new FeatureBean(feature));
                } else if ("promoter".equals(feature.getFeatureType())) {
                    this.promoters.add(new FeatureBean(feature));
                } else {
                    this.markers.add(new FeatureBean(feature));
                }
            }
        }
        this.sequence = construct.getSequence().replaceAll(" ", "");

    }

    public static Construct save(final WritableVersion version, final VectorBean bean)
        throws ConstraintException {
        // Vector (construct)
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Substance.PROP_NAME, bean.getName());
        attributes.put(Construct.PROP_FUNCTION, bean.getFunction());
        attributes.put(LabBookEntry.PROP_DETAILS, bean.getDetails());
        attributes.put(Molecule.PROP_MOLTYPE, bean.getMolType());
        attributes.put(Construct.PROP_SEQUENCETYPE, bean.getSequenceType());
        attributes.put(Construct.PROP_CONSTRUCTSTATUS, bean.getConstructStatus());
        attributes.put(Substance.PROP_CATEGORIES, bean.getCategories());
        final Construct vector = new Construct(version, attributes);
        // Features - resistance
        if (bean.getResistances().size() != 0) {
            for (final FeatureBean featureBean : bean.getResistances()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(FeatureBean.getAttributes(featureBean));
                featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "resistance");
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);
            }
        }
        // Features - promoter
        if (bean.getPromoters().size() != 0) {
            for (final FeatureBean featureBean : bean.getPromoters()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(FeatureBean.getAttributes(featureBean));
                featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "promoter");
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);
            }
        }
        // Features - marker
        if (bean.getMarkers().size() != 0) {
            for (final FeatureBean featureBean : bean.getMarkers()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(FeatureBean.getAttributes(featureBean));
                featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "marker");
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);
            }
        }
        VectorBean.makeRecipe(vector);
        return vector;
    }

    public static RefSample makeRecipe(final Construct vector) throws ConstraintException {
        final WritableVersion version = (WritableVersion) vector.get_Version();
        final RefSample recipe = new RefSample(version, vector.getName());
        new SampleComponent(version, vector, recipe);
        final SampleCategory category =
            version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
        recipe.addSampleCategory(category);
        return recipe;
    }

    /**
     * @return Returns the name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the function.
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * @param function The function to set.
     */
    public void setFunction(final String function) {
        this.function = function;
    }

    /**
     * @return Returns the details.
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * @return Returns the length.
     */
    public Integer getLength() {
        return this.length;
    }

    /**
     * @param length The length to set.
     */
    public void setLength(final Integer length) {
        this.length = length;
    }

    /**
     * @return Returns the molType.
     */
    public String getMolType() {
        return this.molType;
    }

    /**
     * @param molType The molType to set.
     */
    public void setMolType(final String molType) {
        this.molType = molType;
    }

    /**
     * @return Returns the sequenceType.
     */
    public String getSequenceType() {
        return this.sequenceType;
    }

    /**
     * @param sequenceType The sequenceType to set.
     */
    public void setSequenceType(final String sequenceType) {
        this.sequenceType = sequenceType;
    }

    /**
     * @return Returns the constructStatus.
     */
    public String getConstructStatus() {
        return this.constructStatus;
    }

    /**
     * @param constructStatus The constructStatus to set.
     */
    public void setConstructStatus(final String constructStatus) {
        this.constructStatus = constructStatus;
    }

    /**
     * @return Returns the categories.
     */
    public Set<ComponentCategory> getCategories() {
        return this.categories;
    }

    /**
     * @param categories The categories to set.
     */
    public void setCategories(final Set<ComponentCategory> categories) {
        this.categories = categories;
    }

    /**
     * @return Returns the resistances.
     */
    public List<FeatureBean> getResistances() {
        return this.resistances;
    }

    /**
     * @param resistances The resistances to set.
     */
    private void setResistances(final List<FeatureBean> resistances) {
        this.resistances = resistances;
    }

    /**
     * @return Returns the promoters.
     */
    public List<FeatureBean> getPromoters() {
        return this.promoters;
    }

    /**
     * @param promoters The promoters to set.
     */
    public void setPromoters(final List<FeatureBean> promoters) {
        this.promoters = promoters;
    }

    /**
     * @return Returns the markers.
     */
    public List<FeatureBean> getMarkers() {
        return this.markers;
    }

    /**
     * @param markers The markers to set.
     */
    public void setMarkers(final List<FeatureBean> markers) {
        this.markers = markers;
    }

    public static VectorBean getVectorBean(final RefSample refSample) {
        VectorBean vectorBean = null;
        for (final Iterator iterator = refSample.getSampleComponents().iterator(); iterator.hasNext();) {
            final SampleComponent component = (SampleComponent) iterator.next();
            if (component.getRefComponent() instanceof Construct) {
                vectorBean = new VectorBean((Construct) component.getRefComponent());
                break; // assume there is just one
            }
        }
        return vectorBean;
    }

    /**
     * VectorBean.getSequence
     * 
     * @return
     */
    public String getSequence() {
        return this.sequence.toUpperCase();
    }

}
