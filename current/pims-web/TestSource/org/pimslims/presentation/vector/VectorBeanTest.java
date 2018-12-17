/**
 * pims-web org.pimslims.presentation.vector VectorBeanTest.java
 * 
 * @author pajanne
 * @date May 13, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import java.util.Collection;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.molecule.MoleculeFeature;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.test.POJOFactory;

/**
 * VectorBeanTest
 * 
 */
public class VectorBeanTest extends TestCase {

    private static final String SEQUENCE = "CATG";

    private static final String SEQUENCE2 = "ca tg";

    private static final String DETAILS = "123bp";

    private final AbstractModel model;

    public VectorBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testSimpleCreate() throws ConstraintException, AccessException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Construct construct = POJOFactory.createWithFullAttributes(version, Construct.class);
            final VectorBean vectorBean = new VectorBean(construct);
            Assert.assertNotNull(vectorBean);
        } finally {
            version.abort(); // not testing persistence
        }

    }

    public void testCreateWithFeatures() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Construct construct = POJOFactory.create(version, Construct.class);
            final MoleculeFeature feature = POJOFactory.create(version, MoleculeFeature.class);
            Assert.assertTrue(feature.get_MayDelete());
            feature.setFeatureType("resistance");
            construct.addMoleculeFeature(feature);

            final VectorBean vectorBean = new VectorBean(construct);
            Assert.assertNotNull(vectorBean);
            final FeatureBean featureBean = vectorBean.getResistances().get(0);
            Assert.assertEquals("resistance", featureBean.getFeatureType());
            Assert.assertFalse("No sequence in bean", VectorBeanTest.SEQUENCE.equals(4));
            Assert.assertTrue(featureBean.getMayDelete());

            //211009 Testing length in Construct with a sequence
            construct.setSequence(VectorBeanTest.SEQUENCE);
            final VectorBean vectorBean2 = new VectorBean(construct);
            Assert.assertSame(4, vectorBean2.getLength());

            //211009 Testing length in a Construct with length in Details field
            final Construct construct2 = POJOFactory.create(version, Construct.class);
            construct2.setDetails(VectorBeanTest.DETAILS);
            final VectorBean vectorBean3 = new VectorBean(construct2);
            Assert.assertEquals(VectorBeanTest.DETAILS, vectorBean3.getDetails());
            Assert.assertTrue(vectorBean3.getLength() == 123);

            //211009 Finally testing when both are set
            final Construct construct3 = POJOFactory.create(version, Construct.class);
            construct3.setDetails(VectorBeanTest.DETAILS);
            construct3.setSequence(VectorBeanTest.SEQUENCE);
            final VectorBean vectorBean4 = new VectorBean(construct);
            Assert.assertSame(4, vectorBean4.getLength());
            Assert.assertTrue(vectorBean.getMayDelete());

        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testSaveRecipe() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Construct vector = POJOFactory.createWithFullAttributes(version, Construct.class);
            final RefSample recipe = VectorBean.makeRecipe(vector);
            Assert.assertNotNull(recipe);
            Assert.assertEquals(1, recipe.getSampleCategories().size());
            Assert.assertEquals("Vector", recipe.getSampleCategories().iterator().next().getName());
            final VectorBean bean2 = VectorBean.getVectorBean(recipe);
            Assert.assertEquals(vector.getName(), bean2.getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testSimpleSave() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Construct construct = POJOFactory.createWithFullAttributes(version, Construct.class);
            construct.setSequence(VectorBeanTest.SEQUENCE2);
            final VectorBean vectorBean = new VectorBean(construct);
            Assert.assertEquals(VectorBeanTest.SEQUENCE, vectorBean.getSequence());
            vectorBean.setName("TestName");
            final Construct savedConstruct = VectorBean.save(version, vectorBean);
            Assert.assertNotNull(savedConstruct);
            Assert.assertEquals(vectorBean.getName(), savedConstruct.getName());
            final Collection<SampleComponent> components =
                version.findAll(SampleComponent.class, SampleComponent.PROP_REFCOMPONENT, savedConstruct);
            Assert.assertEquals(1, components.size());
            final AbstractSample recipe = components.iterator().next().getAbstractSample();
            final Set<SampleCategory> categories = recipe.getSampleCategories();
            Assert.assertEquals(1, categories.size());
            Assert.assertEquals("Vector", categories.iterator().next().getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testSaveWithFeatures() throws AccessException, ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Construct construct = POJOFactory.create(version, Construct.class);
            final MoleculeFeature feature = POJOFactory.create(version, MoleculeFeature.class);
            feature.setFeatureType("resistance");
            construct.addMoleculeFeature(feature);
            Assert.assertEquals(1, construct.getMoleculeFeatures().size());

            final VectorBean vectorBean = new VectorBean(construct);
            vectorBean.setName("TestName");
            final FeatureBean featureBean = vectorBean.getResistances().get(0);
            featureBean.setName("FeatureName");
            Assert.assertEquals(1, vectorBean.getResistances().size());
            Assert.assertEquals(0, vectorBean.getMarkers().size());
            Assert.assertEquals(0, vectorBean.getPromoters().size());

            final Construct savedConstruct = VectorBean.save(version, vectorBean);
            Assert.assertNotNull(savedConstruct);
            Assert.assertEquals("TestName", savedConstruct.getName());
            Assert.assertEquals(1, savedConstruct.getMoleculeFeatures().size());
            final MoleculeFeature molFeature = savedConstruct.getMoleculeFeatures().iterator().next();
            Assert.assertEquals("resistance", molFeature.getFeatureType());
            Assert.assertEquals("FeatureName", molFeature.getName());

        } finally {
            version.abort(); // not testing persistence
        }
    }

}
