/**
 * pims-model org.pimslims FactoryTest.java
 * 
 * @author cm65
 * @date 6 Jul 2012
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims;

import junit.framework.TestCase;

import org.pimslims.dao.ModelImpl;

/**
 * FactoryTest
 * 
 */
public class FactoryTest extends TestCase {

    public void testClassLoader() throws ClassNotFoundException {
        ClassLoader loader = Factory.getClassLoader(Factory.class.getClassLoader());
        loader.loadClass(ModelImpl.class.getName());
    }

    public void test() {
        Factory.getModel();
    }

}
