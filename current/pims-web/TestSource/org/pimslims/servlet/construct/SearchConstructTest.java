/**
 * pims-web org.pimslims.servlet.construct SearchConstructTest.java
 * 
 * @author cm65
 * @date 1 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.construct.ConstructBeanForList;

/**
 * SearchConstructTest
 * 
 */
public class SearchConstructTest extends TestCase {

    private static final String UNIQUE = "sc" + System.currentTimeMillis();

    private final AbstractModel model;

    public SearchConstructTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    public final void testJustOne() throws ServletException, IOException, ConstraintException {
        final WritableVersion version =
            this.model.getTestVersion();
        try {
            new ResearchObjective(version, SearchConstructTest.UNIQUE, SearchConstructTest.UNIQUE);
            final Map criteria = new HashMap();
            criteria.put("commonName", SearchConstructTest.UNIQUE);
            final Collection<ConstructBeanForList> results =
                SearchConstruct.getResults(version, criteria, "", 0, 2);
            Assert.assertEquals(1, results.size());
        } finally {
            version.abort();
        }
    }
}
