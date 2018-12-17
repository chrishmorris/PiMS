package uk.ac.ox.oppf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {

	
	public static Test suite()
	/*    */   {
	    TestSuite suite = new TestSuite(uk.ac.ox.oppf.optic.PackageTestSuite.class.getName());
	    suite.addTestSuite(uk.ac.ox.oppf.pims.PackageTestSuite.class);
	    suite.addTestSuite(uk.ac.ox.oppf.pims.PackageTestSuite.class);
	    suite.addTestSuite(uk.ac.ox.oppf.pims.inserter.PackageTestSuite.class);
	    suite.addTestSuite(uk.ac.ox.oppf.pims.optic.PackageTestSuite.class);
		/*    */     
	/*    */ 
	/*    */ 
	/* 32 */     return suite;
	/*    */   }
}
