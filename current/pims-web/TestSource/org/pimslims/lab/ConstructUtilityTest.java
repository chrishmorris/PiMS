/**
 * pims-web org.pimslims.lab ConstructUtilityTest.java
 * 
 * @author cm65
 * @date 12 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.lab;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * ConstructUtilityTest
 * 
 */
public class ConstructUtilityTest extends TestCase {

    /**
     * Unique string for avoiding name clashes in tests
     */
    private static final String UNIQUE = "cu" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * @param arg0
     */
    public ConstructUtilityTest(final String arg0) {
        super(arg0);
        this.model = ModelImpl.getModel();
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.ConstructUtility#isSpotConstruct(org.pimslims.metamodel.ModelObject)}.
     * 
     * @throws ConstraintException
     */
    public final void testIsntSpotConstruct() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ModelObject object = new ResearchObjective(version, ConstructUtilityTest.UNIQUE, "test");
            final boolean result = ConstructUtility.isSpotConstruct(object);
            Assert.assertFalse(result);
        } finally {
            version.abort(); // not testing persistence
        }
    }

    /**
     * Test method for
     * {@link org.pimslims.lab.ConstructUtility#isSpotConstruct(org.pimslims.model.target.ResearchObjectiveElement)}
     * .
     */
    public final void testIsntSpotConstructResearchObjectiveElement() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final ResearchObjective eb = new ResearchObjective(version, ConstructUtilityTest.UNIQUE, "test");
            final ResearchObjectiveElement component =
                new ResearchObjectiveElement(version, "test", "test", eb);
            final boolean result = ConstructUtility.isSpotConstruct(component);
            Assert.assertFalse(result);
        } finally {
            version.abort(); // not testing persistence
        }
    }

}
