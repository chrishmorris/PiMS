/**
 * pims-model org.pimslims.upgrader Version43Test.java
 * 
 * @author cm65
 * @date 10 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.upgrader;

import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.CrystalSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;

/**
 * Version43Test Tests model changes for PiMS4.4 / PiMSPro0.5
 */
public class Version43Test extends TestCase {

    /**
     * PASSWORD String
     * 
     * SHA-256 digest of "password"
     */
    private static final String PASSWORD =
        "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86";

    public static Test suite() {
        TestSuite ret = new TestSuite();
        ret.addTestSuite(LocationRetirerTest.class);
        ret.addTestSuite(Version43Test.class);
        return ret;
    }

    private static final String UNIQUE = "v43_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version43Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testMilestone() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            TargetStatus status = new TargetStatus(version, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            new Milestone(version, NOW, status, experiment);
        } finally {
            version.abort();
        }
    }

    public void testMolecule() {
        MetaClass metaClass = this.model.getMetaClass(Molecule.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Molecule.PROP_SEQUENCE);
        assertNotNull(attribute);
        assertEquals(Molecule.PROP_SEQUENCE, attribute.getName());
    }

    // for PRIV-5, see http://tomcat.apache.org/tomcat-7.0-doc/realm-howto.html#Standard_Realm_Implementations
    public void testUser() throws ConstraintException, AccessException {

        //TODO also test that table TOMCATROLE is created
        MetaClass metaClass = this.model.getMetaClass(User.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(User.PROP_DIGESTED_PASSWORD);
        assertNotNull(attribute);
        assertEquals(User.PROP_DIGESTED_PASSWORD, attribute.getName());

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            assertEquals(null, user.getDigestedPassword());
            user.setDigestedPassword("password");
            version.flush(); // ensure all DB constraints are exercised

            //MetaClass mc = model.getMetaClass(User.class.getName());
            String digest = (String) user.get_Value(User.PROP_DIGESTED_PASSWORD);
            assertEquals(PASSWORD, digest);

            user.set_Value(User.PROP_DIGESTED_PASSWORD, "password");
            assertEquals(PASSWORD, user.getDigestedPassword());
            assertTrue(user.isPassword("password"));
            assertFalse(user.isPassword("wrong"));

        } finally {
            version.abort();
        }
    }

    @SuppressWarnings("deprecation")
    public void testCrystalSample() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            CrystalSample old = new CrystalSample(version, UNIQUE);
            old.setAlpha(30.0f);
            old.setRowPosition(1);
            SampleCategory category = new SampleCategory(version, UNIQUE);
            old.addSampleCategory(category);
            new CrystalSampleRetirer(version).retireAll();
            Sample copy = version.findFirst(Sample.class, AbstractSample.PROP_NAME, UNIQUE);
            assertNotNull(copy);
            assertFalse(copy instanceof CrystalSample);
            assertEquals(new Integer(1), copy.getRowPosition());
            assertEquals(" alpha: 30.0", copy.getDetails());
            assertTrue(copy.getSampleCategories().contains(category));
        } finally {
            version.abort();
        }
    }

    /* no, this was a mistake
    @SuppressWarnings("deprecation")
    public void testImageType() throws ConstraintException, AccessException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ImageType old = new ImageType(version, UNIQUE);
            old.setUrl(UNIQUE);
            new ImageTypeRetirer(version).retireAll();
            WellImageType copy = version.findFirst(WellImageType.class, WellImageType.PROP_NAME, UNIQUE);
            assertNotNull(copy);
            assertEquals(old.getUrl(), copy.getUrl());
        } finally {
            version.abort();
        }
    } */
}
