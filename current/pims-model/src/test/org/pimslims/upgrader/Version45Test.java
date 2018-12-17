/**
 * pims-model org.pimslims.upgrader Version43Test.java
 * 
 * @author cm65
 * @date 10 Feb 2012
 * 
 *       Protein Information Management System
 * @version: 4
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
import org.pimslims.model.core.Bookmark;
import org.pimslims.model.crystallization.Image;
import org.pimslims.model.crystallization.WellImageType;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.sample.Sample;

/**
 * Version45Test Tests model changes for xtalPiMS1.4 and PiMS0.7.3
 */
public class Version45Test extends TestCase {

    public static Test suite() {
        TestSuite ret = new TestSuite();
        ret.addTestSuite(Version45Test.class);
        return ret;
    }

    private static final String UNIQUE = "v45_" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private static final String LONG = "long" + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE + UNIQUE
        + UNIQUE + UNIQUE + UNIQUE;

    private final AbstractModel model;

    /**
     * @param name
     */
    public Version45Test(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    /* add attribute to existing class
    public void testUser() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(User.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(User.PROP_ISACTIVE);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            User user = new User(version, UNIQUE);
            assertTrue(user.getIsActive());
            user.setIsActive(false);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(Boolean.FALSE, user.get_Value(User.PROP_ISACTIVE));

            user.set_Value(User.PROP_ISACTIVE, true);
            assertEquals(Boolean.TRUE, user.get_Value(User.PROP_ISACTIVE));

        } finally {
            version.abort();
        }
    } */

    public void testImageOffset() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(Image.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Image.PROP_OFFSETX);
        assertNotNull(attribute);
        attribute = metaClass.getAttribute(Image.PROP_OFFSETY);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            Image image = new Image(version, "/path/", UNIQUE + ".jpg");
            assertEquals(0f, image.getOffsetX());
            assertEquals(0f, image.getOffsetY());
            image.setOffsetX(1f);
            image.setOffsetY(2f);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(1f, image.getOffsetX());
            assertEquals(2f, image.getOffsetY());

            image.set_Value(Image.PROP_OFFSETX, -1f);
            assertEquals(-1f, image.get_Value(Image.PROP_OFFSETX));
            image.set_Value(Image.PROP_OFFSETY, -2f);
            assertEquals(-2f, image.get_Value(Image.PROP_OFFSETY));

        } finally {
            version.abort();
        }
    }

    public void testImageScaling() throws ConstraintException, AccessException {
        MetaClass metaClass = this.model.getMetaClass(Image.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Image.PROP_XLENGTHPERPIXEL);
        assertNotNull(attribute);
        attribute = metaClass.getAttribute(Image.PROP_YLENGTHPERPIXEL);
        assertNotNull(attribute);

        WritableVersion version = this.model.getTestVersion();
        try {
            Image image = new Image(version, "/path/", UNIQUE + ".jpg");
            assertEquals(null, image.getXLengthPerPixel());
            assertEquals(null, image.getYLengthPerPixel());

            WellImageType imageType = new WellImageType(version, UNIQUE);
            imageType.setXlengthPerPixel(3f);
            imageType.setYlengthPerPixel(4f);
            image.setWellImageType(imageType);
            assertEquals(3f, image.getXLengthPerPixel());
            assertEquals(4f, image.getYLengthPerPixel());

            image.setXLengthPerPixel(1f);
            image.setYLengthPerPixel(2f);
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(1f, image.getXLengthPerPixel());
            assertEquals(2f, image.getYLengthPerPixel());

            image.set_Value(Image.PROP_XLENGTHPERPIXEL, -1f);
            assertEquals(-1f, image.get_Value(Image.PROP_XLENGTHPERPIXEL));
            image.set_Value(Image.PROP_YLENGTHPERPIXEL, -2f);
            assertEquals(-2f, image.get_Value(Image.PROP_YLENGTHPERPIXEL));

        } finally {
            version.abort();
        }
    }

    /* New roles
     *     public void testWorkflow() throws ConstraintException, AccessException, SecurityException,
        NoSuchFieldException {
        MetaClass metaClass = this.model.getMetaClass(Workflow.class.getName());
        assertNotNull(metaClass.getMetaRole(Workflow.PROP_PROTOCOLS));
        assertNotNull(metaClass.getMetaRole(Workflow.PROP_PROJECTS));

        WritableVersion version = this.model.getTestVersion();
        try {
            Workflow workflow = new Workflow(version, UNIQUE);
            assertTrue(workflow.getProtocols().isEmpty());

            ExperimentType type = new ExperimentType(version, UNIQUE);
            Protocol protocol = new Protocol(version, UNIQUE, type);
            workflow.addProtocol(protocol);
            version.flush(); // ensure all DB constraints are exercised

            assertTrue(((Collection<Protocol>) workflow.get_Value(Workflow.PROP_PROTOCOLS))
                .contains(protocol));

            workflow.set_Value(Workflow.PROP_PROTOCOLS, Collections.EMPTY_SET);
            assertEquals(0, workflow.getProtocols().size());

            // check a construct can have assigned workflows
            Project project = new ResearchObjective(version, UNIQUE, "test"); // VCID
            project.addWorkflow(workflow);
            version.flush(); // ensure all DB constraints are exercised
            assertTrue(workflow.getProjects().contains(project));

        } finally {
            version.abort();
        }
    }
    */

    // new class
    public void testBookmark() throws ConstraintException, AccessException {
        assertTrue(this.model.getClassNames().contains(Bookmark.class.getName()));
        MetaClass metaClass = this.model.getMetaClass(Bookmark.class.getName());
        MetaAttribute attribute = metaClass.getAttribute(Bookmark.PROP_URL);
        assertNotNull(attribute);
        assertNotNull(metaClass);

        WritableVersion version = this.model.getTestVersion();
        try {
            Bookmark object = new Bookmark(version, UNIQUE, UNIQUE + "url");

            assertEquals(UNIQUE + "url", object.getUrl());
            object.setUrl(UNIQUE + "url2");
            object.setDetails(UNIQUE + "details");
            version.flush(); // ensure all DB constraints are exercised

            assertEquals(UNIQUE + "url2", object.get_Value(Bookmark.PROP_URL));

            object.set_Value(Bookmark.PROP_URL, LONG);
            assertEquals(LONG, object.get_Value(Bookmark.PROP_URL));
            assertEquals(UNIQUE + "details", object.getDetails());

        } finally {
            version.abort();
        }
    }

    // planned test
    public void testExperimentGroup() throws ConstraintException {
        WritableVersion version = this.model.getTestVersion();
        try {
            ExperimentType type = new ExperimentType(version, UNIQUE);
            {
                // the API we have
                Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
                new InputSample(version, experiment).setSample(new Sample(version, UNIQUE + "ino"));
                Parameter setup = new Parameter(version, experiment);
                setup.setName(UNIQUE + "setup");
                setup.setValue("one");
                Parameter result = new Parameter(version, experiment);
                result.setName(UNIQUE + "result");
                result.setValue("two");
                version.flush(); // that's a perfectly good characterisation experiment
                new OutputSample(version, experiment).setSample(new Sample(version, UNIQUE + "outo"));
                version.flush(); // that's a perfectly good well experiment
                new OutputSample(version, experiment).setSample(new Sample(version, UNIQUE + "out2o"));
                version.flush(); // that's a perfectly good purification experiment

                Parameter found =
                    experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, setup.getName());
                assertEquals("one", found.getValue());
                found =
                    experiment.findFirst(Experiment.PROP_PARAMETERS, Parameter.PROP_NAME, result.getName());
                assertEquals("two", found.getValue());
            }
            /* TODO first implement this new API with only a normal upgrader
             * Then deprecate the old API
             * Then implement the new API more efficiently, with a special upgrader if necessary
            // the API we want
            {
                Protocol protocol = new Protocol(version, UNIQUE, type);
                ParameterDefinition setup =
                    new ParameterDefinition(version, UNIQUE + "setup", "String", protocol);
                ParameterDefinition result =
                    new ParameterDefinition(version, UNIQUE + "result", "String", protocol);
                ExperimentGroup group = new ExperimentGroup(version, UNIQUE, NOW, NOW, protocol);
                Experiment experiment = new Experiment(version, group);
                new InputSample(version, experiment).setSample(new Sample(version, UNIQUE + "in"));
                group.setParameter(setup, "one");
                Sample output = new Sample(version, UNIQUE + "out");
                output.setExperiment(experiment);
                output.setParameter(result, "two");
                version.flush(); // that's a perfectly good characterisation experiment
                // and that's a perfectly good well experiment
                Sample output2 = new Sample(version, UNIQUE + "out2");
                output2.setExperiment(experiment);
                version.flush(); // that's a perfectly good purification experiment

                assertEquals(NOW, experiment.getStartDate());
                assertEquals(NOW, experiment.getEndDate());
                assertEquals(group, experiment.getExperimentGroup());
                assertEquals("one", group.getParameter(setup));
                assertEquals("two", output.getParameter(result));      
                assertFalse(experiment instanceof LabBookEntry);          
            } */

        } finally {
            version.abort();
        }
    }

}
