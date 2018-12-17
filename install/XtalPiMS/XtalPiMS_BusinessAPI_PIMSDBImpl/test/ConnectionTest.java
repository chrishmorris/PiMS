import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;

import junit.framework.TestCase;

/** 
 * xtalPiMS Business API - PIMSDB Impl  ConnectionTest.java
 * @author cm65
 * @date 14 May 2010
 *
 * Protein Information Management System
 * @version: 3.2
 *
 * Copyright (c) 2010 cm65 
 * The copyright holder has licenced the STFC to redistribute this software
 */

/**
 * ConnectionTest
 *
 */
public class ConnectionTest extends TestCase {
    
    public void test() {
        AbstractModel model = ModelImpl.getModel();
        assertNotNull(model);
    }

}
