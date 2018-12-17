/**
 * pims-web org.pimslims.xmlio VectorXmlBean.java
 * 
 * @author pajanne
 * @date Apr 21, 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.xmlio;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * VectorXmlBean
 * 
 */
@XmlRootElement(name = "vector")
public class VectorXmlBean {

    private String name;

    private String details;

    private String sampleCategory;

    private RefSampleSourceXmlBean refSampleSource;

    private SampleComponentXmlBean sampleComponent;

    private Calendar creationDate;

    private Calendar exportDate = Calendar.getInstance();

    /**
     * 
     * Constructor for VectorXmlBean
     * 
     * @param construct
     */
    public VectorXmlBean(final RefSample recipe) {
        this.name = recipe.getName();
        this.details = recipe.getDetails();
        this.sampleCategory = "";
        if (recipe.getSampleCategories().size() != 0) {
            for (final SampleCategory category : recipe.getSampleCategories()) {
                this.sampleCategory = this.sampleCategory + category.getName();
            }
        }
        if (recipe.getRefSampleSources().size() == 1) {
            this.refSampleSource = new RefSampleSourceXmlBean(recipe.getRefSampleSources().iterator().next());
        }
        if (recipe.getSampleComponents().size() == 1) {
            this.sampleComponent =
                new SampleComponentXmlBean((Construct) recipe.getSampleComponents().iterator().next()
                    .getRefComponent());
        }
        this.creationDate = recipe.getCreationDate();
    }

    // Required by jaxb
    private VectorXmlBean() {
        // Required by jaxb
    }

    public static RefSample save(final WritableVersion version, final VectorXmlBean bean)
        throws ConstraintException, AccessException {
        // Recipe
        final Map<String, Object> recipeAttributes = new HashMap<String, Object>();
        recipeAttributes.put(AbstractSample.PROP_NAME, bean.getName());
        recipeAttributes.put(LabBookEntry.PROP_DETAILS, bean.getDetails());
        final Map<String, Object> recipeAeyAttrMap = new HashMap<String, Object>();
        recipeAeyAttrMap.put(AbstractSample.PROP_NAME, bean.getName());

        final RefSample recipe =
            VectorXmlBean.UpdateOrCreate(version, RefSample.class, recipeAeyAttrMap, null, recipeAttributes);

        final SampleCategory category =
            version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, bean.getSampleCategory());
        recipe.addSampleCategory(category);

        // RefSampleSource

        final RefSampleSourceXmlBean rssBean = bean.getRefSampleSource();
        if (null != rssBean) {
            //delete old one

            for (final ReagentCatalogueEntry source : recipe.getRefSampleSources()) {
                source.delete();
            }

            //add new one
            final Organisation supplier =
                VectorXmlBean.getOrCreate(version, Organisation.class.getName(), Organisation.PROP_NAME,
                    rssBean.getSupplier());
            final ReagentCatalogueEntry source =
                new ReagentCatalogueEntry(version, recipe, rssBean.getCatalogNum(), supplier);
            source.setDataPageUrl(rssBean.getDataPageUrl());
        }

        // SampleComponent
        final SampleComponentXmlBean scBean = bean.getSampleComponent();

        // Vector (construct)
        final ComponentCategory cCategory =
            version.findFirst(ComponentCategory.class, ComponentCategory.PROP_NAME,
                scBean.getComponentCategory());

        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Substance.PROP_NAME, scBean.getName());
        attributes.put(Construct.PROP_FUNCTION, scBean.getFunction());
        attributes.put(LabBookEntry.PROP_DETAILS, scBean.getDetails());
        attributes.put(Molecule.PROP_MOLTYPE, scBean.getMolType());
        attributes.put(Construct.PROP_SEQUENCETYPE, scBean.getSequenceType());
        attributes.put(Construct.PROP_CONSTRUCTSTATUS, scBean.getConstructStatus());
        attributes.put(Substance.PROP_CATEGORIES, cCategory);

        final Map<String, Object> keyAttrMap = new HashMap<String, Object>();
        keyAttrMap.put(Substance.PROP_NAME, scBean.getName());
        final Construct vector =
            VectorXmlBean.UpdateOrCreate(version, Construct.class, keyAttrMap, null, attributes);
        //delete old MoleculeFeature
        for (final MoleculeFeature moleculeFeature : vector.getMoleculeFeatures()) {
            moleculeFeature.delete();
        }

        //add new 
        // Features - resistance
        if (scBean.getResistanceDetails().size() != 0) {
            for (final MoleculeFeatureXmlBean featureBean : scBean.getResistanceDetails()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(MoleculeFeatureXmlBean.getAttributes(featureBean));
                if (null == featureBean.getFeatureType()) {
                    featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "resistance");
                }
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);

            }
        }
        // Features - promoter
        if (scBean.getPromoterDetails().size() != 0) {
            for (final MoleculeFeatureXmlBean featureBean : scBean.getPromoterDetails()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(MoleculeFeatureXmlBean.getAttributes(featureBean));
                if (null == featureBean.getFeatureType()) {
                    featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "promoter");
                }
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);
            }
        }
        // Features - marker
        if (scBean.getMarkerDetails().size() != 0) {
            for (final MoleculeFeatureXmlBean featureBean : scBean.getMarkerDetails()) {
                final Map<String, Object> featureAttributes =
                    new HashMap<String, Object>(MoleculeFeatureXmlBean.getAttributes(featureBean));
                if (null == featureBean.getFeatureType()) {
                    featureAttributes.put(MoleculeFeature.PROP_FEATURETYPE, "marker");
                }
                featureAttributes.put(MoleculeFeature.PROP_MOLECULE, vector);
                new MoleculeFeature(version, featureAttributes);
            }
        }

        // Attach vector to recipe
        final Map<String, Object> scAttributes = new HashMap<String, Object>();
        scAttributes.put(SampleComponent.PROP_REFCOMPONENT, vector);
        scAttributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, recipe);
        //final SampleComponent sc =
        VectorXmlBean.UpdateOrCreate(version, SampleComponent.class, scAttributes, null, scAttributes);

        return recipe;
    }

    /**
     * @return Returns the name.
     */
    @XmlElement
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the sampleCategory.
     */
    @XmlElement
    public String getSampleCategory() {
        return this.sampleCategory;
    }

    /**
     * @param sampleCategory The sampleCategory to set.
     */
    public void setSampleCategory(final String sampleCategory) {
        this.sampleCategory = sampleCategory;
    }

    /**
     * @return Returns the details.
     */
    @XmlElement
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
     * @return Returns the refSampleSource.
     */
    @XmlElement
    public RefSampleSourceXmlBean getRefSampleSource() {
        return this.refSampleSource;
    }

    /**
     * @param refSampleSource The refSampleSource to set.
     */
    public void setRefSampleSource(final RefSampleSourceXmlBean refSampleSource) {
        this.refSampleSource = refSampleSource;
    }

    /**
     * @return Returns the sampleComponent.
     */
    @XmlElement
    public SampleComponentXmlBean getSampleComponent() {
        return this.sampleComponent;
    }

    /**
     * @param sampleComponent The sampleComponent to set.
     */
    public void setSampleComponent(final SampleComponentXmlBean sampleComponent) {
        this.sampleComponent = sampleComponent;
    }

    /**
     * @return Returns the creationDate.
     */
    @XmlElement
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the exportDate.
     */
    @XmlElement
    public Calendar getExportDate() {
        return this.exportDate;
    }

    /**
     * @param exportDate The exportDate to set.
     */
    public void setExportDate(final Calendar exportDate) {
        this.exportDate = exportDate;
    }

    /**
     * Create ModelObjects using the Map holding parameters
     * 
     * @param mObjname String name of the ModelObject
     * @param dataOwner (AccessObject) as String If null use default one.
     * @param parameters - Map paraneterName->value
     * @return ModelObject
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    public static <T extends ModelObject> T create(final WritableVersion rw, final String mObjname,
        final String propertyName, final String propertyValue) throws AccessException, ConstraintException {
        T mObj = null;
        assert rw != null : "Problems with the Writable version! ";
        Class javaClass;
        try {
            javaClass = Class.forName(mObjname);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("Can not create " + mObjname, e);
        }
        final HashMap prop = new HashMap();
        prop.put(propertyName, propertyValue);

        mObj = (T) rw.create(javaClass, prop);

        return mObj;
    }

    /**
     * Obtain the object from the database if there is one otherwise create the object using parameters Can
     * only be used to obtain one instance of object from the database will throw an AssertionError otherwise
     * 
     * @param rw WritableVersion
     * @param metaClassName String the name of a metaclass
     * @param params Map contains parameters
     * @param dataOwner String the name of the dataOwner (AccessObject) if null use default one
     * @return ModelObject
     * @throws AccessException
     * @throws ConstraintException
     * @throws ClassNotFoundException
     */
    public static <T extends ModelObject> T getOrCreate(final WritableVersion rw, final String metaClassName,
        final String propertyName, final String propertyValue) throws AccessException, ConstraintException {

        assert rw != null : "WritableVersion is null";
        if (propertyName == null && propertyValue == null) {
            throw new RuntimeException("Parameters are empty! ");
        }

        final MetaClass metaclass = rw.getModel().getMetaClass(metaClassName);

        T mo = (T) rw.findFirst(metaclass.getJavaClass(), propertyName, propertyValue);

        if (mo == null) {

            mo = (T) VectorXmlBean.create(rw, metaClassName, propertyName, propertyValue);
        }
        //System.out.println("getOrCreate [" + mo.get_Hook() + ":" + mo.get_Name() + "]");
        return mo;
    }

    /**
     * @param wv
     * @param keyAttrMap the attributes used to recorgnize the Object existed or not
     * @param oldNames the old names used to recorgnize the Object existed or not if keyAttrMap can not found
     *            one
     * @param fullAttrMap if this object is not existed, this fullAttrMap used to create it. Otherwise, this
     *            fullAttrMap used to update it (be careful with not changeable value)
     * @throws ConstraintException
     * @throws AccessException
     */
    public static <T extends ModelObject> T UpdateOrCreate(final WritableVersion wv,
        final Class<T> targetClass, final Map<String, Object> keyAttrMap, final Collection<String> oldNames,
        final Map<String, Object> fullAttrMap) throws AccessException, ConstraintException {
        //use keyAttrMap to recorgnize the Object existed or not
        T targetObject = wv.findFirst(targetClass, keyAttrMap);
        //use oldNames+ keyAttrMap to recorgnize the Object existed or not if keyAttrMap can not find one
        if (targetObject == null && oldNames != null && oldNames.size() > 0) {
            final Map<String, Object> oldKeyAttrMap = new HashMap<String, Object>(keyAttrMap);
            for (final String oldName : oldNames) {
                oldKeyAttrMap.put("name", oldName);
                targetObject = wv.findFirst(targetClass, oldKeyAttrMap);
                if (targetObject != null) {
                    break;
                }
            }
        }
        //if found nothing, just create a new one
        if (targetObject == null) {
            //if this object is not existed, this fullAttrMap used to create it
            targetObject = wv.create(targetClass, fullAttrMap);
            //System.out.println("+Create new " + targetClass.getName() + " : [" + keyAttrMap + "] ");
        } else {
            // Otherwise, the fullAttrMap used to update it
            //
            final org.pimslims.metamodel.MetaClass metaClass = targetObject.get_MetaClass();

            for (final String name : fullAttrMap.keySet()) {
                //update attributes
                if (null != metaClass.getAttribute(name)) {
                    Object value = fullAttrMap.get(name);
                    if (value instanceof String) {
                        if (value != null && ((String) value).trim().length() == 0) {
                            value = null;
                        }
                    }

                    if (targetObject.get_Value(name) != value) {
                        if ((targetObject.get_Value(name) == null && value != null)
                            || (targetObject.get_Value(name) != null && value == null)
                            || !targetObject.get_Value(name).toString().equals(value.toString())) {
                            if (targetObject.get_Value(name) != null
                                && targetObject.get_Value(name).toString().equals("") && value == null) {
                                /*ignored*/
                            }
                            //only update when attribute is not same
                            else {
                                System.out.println("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + value);
                                targetObject.set_Value(name, value);
                            }
                        }
                    }
                }
                //update roles
                else if (null != metaClass.getMetaRole(name)) {
                    final MetaRole role = metaClass.getMetaRole(name);
                    {
                        if (fullAttrMap.get(name) instanceof Collection) {
                            if (!role.get(targetObject).containsAll((Collection) fullAttrMap.get(name))
                                || role.get(targetObject).size() != ((Collection) fullAttrMap.get(name))
                                    .size()) {
                                //only update when role objects are not same
                                System.out.println("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + fullAttrMap.get(name));
                                targetObject.set_Role(name, (Collection) fullAttrMap.get(name));
                            }
                        } else if (fullAttrMap.get(name) instanceof ModelObject) {
                            if (!role.get(targetObject).contains(fullAttrMap.get(name))
                                || role.get(targetObject).size() != 1) {
                                //only update when role object is not same
                                System.out.println("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + ((ModelObject) fullAttrMap.get(name)).get_Name());
                                targetObject.set_Role(name, (ModelObject) fullAttrMap.get(name));
                            }
                        } else {
                            System.out.println(name + " is not a role or attribute for "
                                + targetObject.get_Name());
                        }

                    }
                }
            }
        }
        return targetObject;

    }

}
