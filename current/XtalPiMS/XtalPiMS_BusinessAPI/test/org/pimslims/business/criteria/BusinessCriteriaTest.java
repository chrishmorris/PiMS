/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pimslims.business.criteria;

import java.util.List;

import junit.framework.TestCase;

import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class BusinessCriteriaTest extends TestCase {

    public BusinessCriteriaTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getCriteria method, of class BusinessCriteria.
     * 
     * @throws BusinessException
     */
    public void testCriteria() throws BusinessException {
        System.out.println("testCriteria");
        //This is not really a test, but a test of concept....
        BusinessCriteria criteria = new BusinessCriteria(null);

        criteria.setFirstResult(0);
        criteria.setMaxResults(100);
        criteria.add(BusinessExpression.Between("blart", Integer.valueOf(3), Integer.valueOf(5)));
        criteria.add(BusinessExpression.Null("blart2", true));

        List<BusinessCriterion> expResult = null;
        List<BusinessCriterion> result = criteria.getCriteria();
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
