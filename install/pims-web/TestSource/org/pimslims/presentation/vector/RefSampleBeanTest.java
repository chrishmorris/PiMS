/**
 * pims-web org.pimslims.presentation.vector RefSampleBeanTest.java
 * 
 * @author pajanne
 * @date Jun 3, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation.vector;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Construct;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.ReagentCatalogueEntry;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.test.POJOFactory;

/**
 * RefSampleBeanTest
 * 
 */
public class RefSampleBeanTest extends TestCase {

    private final AbstractModel model;

    public RefSampleBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public void testSimpleCreate() throws ConstraintException, AccessException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RefSample refSample = POJOFactory.createWithFullAttributes(version, RefSample.class);
            final RefSampleBean refSampleBean = new RefSampleBean(refSample);
            Assert.assertNotNull(refSampleBean);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testCreateWithSources() throws AccessException, ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RefSample refSample = POJOFactory.create(version, RefSample.class);
            final ReagentCatalogueEntry source = POJOFactory.create(version, ReagentCatalogueEntry.class);
            final Organisation supplier = POJOFactory.createWithFullAttributes(version, Organisation.class);
            source.setSupplier(supplier);
            refSample.addRefSampleSource(source);

            final RefSampleBean refSampleBean = new RefSampleBean(refSample);
            Assert.assertNotNull(refSampleBean);
            Assert.assertEquals(refSample.getName(), refSampleBean.getName());
            Assert.assertEquals(1, refSampleBean.getRefSampleSources().size());
            final RefSampleSourceBean sourceBean = refSampleBean.getRefSampleSources().get(0);
            Assert.assertEquals(source.getCatalogNum(), sourceBean.getCatalogNum());
            Assert.assertEquals(source.getSupplier().getName(), sourceBean.getSupplier().getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

    public void testCreateWithComponent() throws AccessException, ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            final RefSample refSample = POJOFactory.create(version, RefSample.class);
            final Construct construct = POJOFactory.create(version, Construct.class);
            final SampleComponent component = new SampleComponent(version, construct, refSample);

            final RefSampleBean refSampleBean = new RefSampleBean(refSample);
            Assert.assertNotNull(refSampleBean);
            Assert.assertEquals(refSample.getName(), refSampleBean.getName());
            Assert.assertEquals(1, refSampleBean.getSampleComponents().size());
            final SampleComponentBean componentBean = refSampleBean.getSampleComponents().get(0);
            Assert.assertEquals(component.getDbId(), componentBean.getDbId());
            Assert.assertEquals(component.getRefComponent().getName(), componentBean.getVector().getName());
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
