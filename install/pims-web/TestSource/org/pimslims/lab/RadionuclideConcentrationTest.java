/**
 * pims-web org.pimslims.lab RadionuclideConcentration.java
 * 
 * @author cm65
 * @date 17 Jun 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.lab;

import java.util.Calendar;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.lab.RadionuclideConcentration.Radionuclide;

/**
 * RadionuclideConcentration
 * 
 * Represents the concentration of a radionuclide, e.g. 32P, in a reagent solution. A typical value is
 * 1MBq/100uL.
 * 
 */
public class RadionuclideConcentrationTest extends TestCase {

    /**
     * error bar for comparing floating point numbers
     */
    private static final double ERROR = 0.0001;

    // use different test values each time the test is compiled.
    private static final Random RANDOM = new Random();

    /**
     * CONCENTRATION, MBq/L
     */
    private static final double CONCENTRATION = 10000 * RadionuclideConcentrationTest.RANDOM.nextDouble();

    /**
     * NOW Calendar
     */
    private static final Calendar NOW = Calendar.getInstance();

    /**
     * HALF_LIFE, milliseconds
     */
    private static final double HALF_LIFE =
        RadionuclideConcentrationTest.RANDOM.nextDouble() * 10 * 24 * 60 * 60 * 1000;

    public void testConstructor() {
        new RadionuclideConcentration(new Radionuclide("X", RadionuclideConcentrationTest.HALF_LIFE),
            RadionuclideConcentrationTest.CONCENTRATION, RadionuclideConcentrationTest.NOW);
    }

    public void testGetAmount() {
        final RadionuclideConcentration rnc =
            new RadionuclideConcentration(new Radionuclide("X", RadionuclideConcentrationTest.HALF_LIFE),
                RadionuclideConcentrationTest.CONCENTRATION, RadionuclideConcentrationTest.NOW);
        // litres used, typically a few uL
        final double amount = 0.00001 * RadionuclideConcentrationTest.RANDOM.nextDouble();
        Assert.assertEquals(RadionuclideConcentrationTest.CONCENTRATION * amount, rnc.getMBq(
            RadionuclideConcentrationTest.NOW, amount), RadionuclideConcentrationTest.ERROR);
    }

    public void testDecay() {
        // make a double strength solution
        final RadionuclideConcentration rnc =
            new RadionuclideConcentration(new Radionuclide("X", RadionuclideConcentrationTest.HALF_LIFE),
                2 * RadionuclideConcentrationTest.CONCENTRATION, RadionuclideConcentrationTest.NOW);
        // then wait for a half life
        final Calendar later = Calendar.getInstance();
        later.setTimeInMillis(RadionuclideConcentrationTest.NOW.getTimeInMillis()
            + (long) RadionuclideConcentrationTest.HALF_LIFE);
        // then check the concentration
        Assert.assertEquals(1.0d, RadionuclideConcentrationTest.CONCENTRATION / rnc.getMBqPerLitre(later),
            RadionuclideConcentrationTest.ERROR);
    }

    public void test32P() {
        Assert.assertEquals(14.262 * 24 * 60 * 60 * 1000, RadionuclideConcentration.P32.getHalfLife());
    }

    public void test14C() {
        Assert.assertEquals(31556930d * 1000 * 5730, RadionuclideConcentration.C14.getHalfLife());
    }

}
