/**
 * pims-web org.pimslims.servlet.plateExperiment SpreadsheetGetterTest.java
 * 
 * @author Marc Savitsky
 * @date 13 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.lab.file;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;

/**
 * SpreadsheetGetterTest
 * 
 */
@Deprecated
// the importer does not work, and we don't have the resource to fix it
public class AktaFileTest extends TestCase {

    private final AbstractModel model;

    /**
     * Constructor for SpreadsheetGetterTest
     * 
     * @param name
     */
    public AktaFileTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    /**
     * OPPFExperimentNameTest.setUp
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * OPPFExperimentNameTest.tearDown
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIsExperimentAktaFile() throws IFileException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final IFileType iFileType = new AktaFile();
            Assert.assertTrue(iFileType.isOfThisType(version, "20091103-230741-441290018809_cart.akta"));
            Assert.assertFalse(iFileType
                .isOfThisType(version, "Caliper_GX_2010-05-21_12-03-04_Peaktable.csv"));
            Assert.assertTrue(iFileType.isOfThisType(version, "Sys4001_Sample1.akta"));
        } finally {
            version.abort();
        }
    }
}
