/**
 * pims-model org.pimslims.search SearchTestSuite.java
 * 
 * @author bl67
 * @date 9 Dec 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 bl67 This library is free software; you can redistribute it and/or modify it
 *           under the terms of the GNU Lesser General Public License as published by the Free Software
 *           Foundation; either version 2.1 of the License, or (at your option) any later version.
 * 
 *           This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *           even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *           Lesser General Public License for more details.
 * 
 *           A copy of the license is in dist/docs/LGPL.txt. It is also available here:
 * @see: http://www.gnu.org/licenses/lgpl.html#SEC1
 */
package org.pimslims.search;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.pimslims.model.api.SearchTest;

/**
 * SearchTestSuite
 * 
 */
public class SearchTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for org.pimslims.search");
        suite.addTestSuite(ConditionTest.class);
        suite.addTestSuite(CountTest.class);
        suite.addTestSuite(PagingTest.class);
        suite.addTestSuite(PlateSampleSearchTest.class);
        suite.addTestSuite(SearcherTester.class);
        suite.addTestSuite(SearchTest.class);
        suite.addTestSuite(SubCriteriaTest.class);
        suite.addTestSuite(TargetFindAllTester.class);
        suite.addTestSuite(TestComparePIMSCriteriaHibernateImpl.class);
        suite.addTestSuite(TestPIMSCriteriaHibernateImpl.class);
        return suite;
    }

}
