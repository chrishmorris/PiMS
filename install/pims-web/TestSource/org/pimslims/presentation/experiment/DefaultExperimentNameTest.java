/**
 * V3_1-pims-web org.pimslims.presentation.experiment DefaultExperimentNameTest.java
 * 
 * @author cm65
 * @date 16 Mar 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.experiment;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.target.ResearchObjective;

/**
 * DefaultExperimentNameTest
 * 
 */
public class DefaultExperimentNameTest extends TestCase {

    private static final String UNIQUE = "den" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for DefaultExperimentNameTest
     * 
     * @param name
     */
    public DefaultExperimentNameTest(final String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.experiment.DefaultExperimentName#makeAcronym(java.lang.String)}.
     */
    public void testMakeAcronym() {
        Assert.assertEquals("", DefaultExperimentName.makeAcronym(""));
        Assert.assertEquals("Pcr", DefaultExperimentName.makeAcronym("Polymerase chain reaction type 2"));
        Assert.assertEquals("pur", DefaultExperimentName.makeAcronym("purification"));
        Assert.assertEquals("Exp", DefaultExperimentName.makeAcronym("Expression"));
        Assert.assertEquals("PP", DefaultExperimentName.makeAcronym("PP"));
    }

    /**
     * Test method for
     * {@link org.pimslims.presentation.experiment.DefaultExperimentName#makeAcronym(java.lang.String)}.
     */
    public void testMakeConstructNmae() {
        Assert.assertEquals("T00013.full", DefaultExperimentName.makeConstructName("T00013.full-PCR 2"));
        Assert.assertEquals("xxx", DefaultExperimentName.makeConstructName("xxx-pcr"));
    }

    public void testKeepLabelling() {
        final String name = DefaultExperimentName.makeConstructName("T00013.full-15N-Supernatant");
        Assert.assertTrue(name, name.contains("15N"));
    }

    public void testConstruct() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final Project project =
                new ResearchObjective(version, DefaultExperimentNameTest.UNIQUE + "ro", "test");
            final ExperimentType type = new ExperimentType(version, DefaultExperimentNameTest.UNIQUE + "et");
            final Protocol protocol = new Protocol(version, DefaultExperimentNameTest.UNIQUE + "prot", type);
            final String name = new DefaultExperimentName().suggestExperimentName(version, protocol, project);
            Assert.assertEquals(project.getName() + " " + type.getName() + " "
                + org.pimslims.access.Access.ADMINISTRATOR + " 1", name);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
