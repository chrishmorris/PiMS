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

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.test.AbstractTestCase;

/**
 * SpreadsheetGetterTest
 * 
 */
public class CartesianFileTest extends TestCase {

    private final AbstractModel model;

    final static String inputString =
        "Username : Background" + "\nDate     : 01-Apr-2010 16:36:40"
            + "\nProtein  : Radu Aricescu - AM21 M1 mutant (1) - 01/04/2010 (1)"
            + "\nPlate    : 441300248776" + "\nFilename : C:\\Protein Files\\Cartesian Technologies, "
            + "Inc\\AxSys\\Generak Use\\Screen 100nl P only sq2348.ad" + "\nProtVol  : 100"
            + "\nScreenVol: 100";

    final static String fileName = "20091103-230741-441290018809_cart.txt";

    CartesianFile cartesian;

    /**
     * Constructor for SpreadsheetGetterTest
     * 
     * @param name
     */
    public CartesianFileTest(final String methodName) {
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
        this.cartesian =
            new CartesianFile(CartesianFileTest.fileName, CaliperFile
                .parseStringToIS(CartesianFileTest.inputString));
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

    public void testIsCartesianFile() {

        final WritableVersion version = this.model.getTestVersion();
        try {
            //final File file = new File(CartesianFileTest.fileName);
            //final IFileType iFileType = new CartesianFile();
            Assert.assertTrue(this.cartesian.isOfThisType(version, CartesianFileTest.fileName));
            Assert.assertFalse(this.cartesian.isOfThisType(version,
                "Caliper_GX_2010-05-21_12-03-04_Peaktable.csv"));
        } finally {
            version.abort();
        }
    }

    public void testgetBarcode() {
        Assert.assertEquals("441300248776", this.cartesian.getBarcode());
    }

    public void testgetDate() {
        final Date date = this.cartesian.getDate().getTime();
        Assert.assertEquals("Thu Apr 01 16:36:40 BST 2010", date.toString());
    }

    public void testgetUser() {
        Assert.assertEquals("Background", this.cartesian.getUser());
    }

    public void testgetProtein() {
        Assert.assertEquals("Radu Aricescu - AM21 M1 mutant (1) - 01/04/2010 (1)", this.cartesian
            .getProtein());
    }

    public void testgetFilename() {
        Assert
            .assertEquals(
                "C:\\Protein Files\\Cartesian Technologies, Inc\\AxSys\\Generak Use\\Screen 100nl P only sq2348.ad",
                this.cartesian.getFilename());
    }

    public void testgetProtVol() {
        Assert.assertEquals(100, this.cartesian.getProtVol());
    }

    public void testgetScreenVol() {
        Assert.assertEquals(100, this.cartesian.getScreenVol());
    }

    public void testProcessToPiMS() throws IFileException {
        final WritableVersion version = AbstractTestCase.model.getTestVersion();
        try {
            this.cartesian.process(version);

        } finally {
            version.abort();
        }
    }

}
