/**
 * pims-tools org.pimslims.tools CaliperImporterTest.java
 * 
 * @author Marc Savitsky
 * @date 18 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.tools;

import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;

/**
 * CaliperImporterTest
 * 
 */
public class FileImporterTest extends TestCase {

    private final AbstractModel model;

    /**
     * Constructor for LoaderTest
     * 
     * @param name
     */
    public FileImporterTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testProcessFolder() throws Exception {

        FileImporter.processFolder("C:\\Temp\\Caliper");
    }

}
