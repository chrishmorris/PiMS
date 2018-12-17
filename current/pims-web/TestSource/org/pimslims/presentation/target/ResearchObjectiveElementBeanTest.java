/**
* V4_4-web org.pimslims.presentation.target ResearchObjectiveElementBeanTest.java
 * 
 * @author cm65
 * @date 3 Sep 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.target;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;

/**
 * ResearchObjectiveElementBeanTest
 * 
 */
public class ResearchObjectiveElementBeanTest extends TestCase {

    private static final String UNIQUE = "roeb" + System.currentTimeMillis();

    private final AbstractModel model;

    /**
     * Constructor for ResearchObjectiveElementBeanTest
     * 
     * @param name
     */
    public ResearchObjectiveElementBeanTest(final String name) {
        super(name);
        this.model = org.pimslims.dao.ModelImpl.getModel();
    }

    /**
     * Test method for {@link org.pimslims.presentation.target.ResearchObjectiveElementBean#getStart()}.
     */
    public void testGetStart() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {

            final Project ro =
                new ResearchObjective(version, ResearchObjectiveElementBeanTest.UNIQUE,
                    ResearchObjectiveElementBeanTest.UNIQUE);
            final ModelObject roe =
                new ResearchObjectiveElement(version, ResearchObjectiveElementBeanTest.UNIQUE,
                    ResearchObjectiveElementBeanTest.UNIQUE, ro);
            final ResearchObjectiveElementBean bean =
                (ResearchObjectiveElementBean) org.pimslims.presentation.BeanFactory.newBean(roe);
            Assert.assertNotNull(bean);
            Assert.assertEquals("", bean.getStart());
        } finally {
            version.abort();
        }
    }

}
