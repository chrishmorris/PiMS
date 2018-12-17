/**
 * pims-web org.pimslims.lab RadioNuclideConcentration.java
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
import java.util.SimpleTimeZone;

/**
 * RadioNuclideConcentration
 * 
 * Represents the concentration of a radionuclide in a solution, allowing for radioactive decay.
 * 
 */
public class RadionuclideConcentration {

    /**
     * SECONDS_IN_SI_YEAR http://physics.nist.gov/Pubs/SP811/appenB9.html#TIME
     */
    private static final double SECONDS_IN_SI_YEAR = 3.155693E+07;

    /**
     * Radionuclide
     * 
     */
    public static class Radionuclide {
        final String name;

        /**
         * halfLife For our convenience, this is recorded in milliseconds, although typical values are tens of
         * days.
         */
        final double halfLife;

        Radionuclide(final String name, final double halfLife) {
            super();
            this.name = name;
            this.halfLife = halfLife;
        }

        /**
         * @return Returns the name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return Returns the halfLife.
         */
        public double getHalfLife() {
            return this.halfLife;
        }

    }

    /* 
     * Nuclides commonly used
     * */

    // 13C is stable
    public static final Radionuclide C14 =
        new Radionuclide("14C", (RadionuclideConcentration.SECONDS_IN_SI_YEAR * 1000) * 5730);

    // 19F is stable
    public static final Radionuclide H3 = new Radionuclide("3H", 12.4 * 365 * 24 * 60 * 60 * 1000);

    public static final Radionuclide I125 = new Radionuclide("125I", 59.408 * 24 * 60 * 60 * 1000);

    // 15N is stable
    public static final Radionuclide P32 = new Radionuclide("32P", 14.262 * 24 * 60 * 60 * 1000);

    public static final Radionuclide P33 = new Radionuclide("33P", 25.34 * 24 * 60 * 60 * 1000);

    public static final Radionuclide S35 = new Radionuclide("35S", 87.51 * 24 * 60 * 60 * 1000);

    /**
     * DATUM Calendar Zero time for this calculator. We could save both concentration and time. To simplify
     * the database, we scale the concentration to a standard time.
     */
    private static final Calendar DATUM = Calendar.getInstance();
    static {
        RadionuclideConcentration.DATUM.setTimeZone(new SimpleTimeZone(0, "GMT"));
        RadionuclideConcentration.DATUM.setTimeInMillis(1308000000000L);
    }

    final private Radionuclide nuclide;

    /**
     * concentration MBq/L that was in this solution at datum time
     */
    final private double concentration;

    /**
     * Constructor for RadionuclideConcentration
     * 
     * @param nuclide The Radionuclide represented
     * @param concentration The current concentration, in MBq/L
     * @param now The current time
     */
    RadionuclideConcentration(final Radionuclide nuclide, final double concentration, final Calendar now) {
        super();
        this.nuclide = nuclide;
        final double halfLives = this.getHalfLivesSinceDatum(now);
        this.concentration = concentration / Math.pow(2d, -halfLives);
    }

    private double getHalfLivesSinceDatum(final Calendar now) {
        final long duration = now.getTimeInMillis() - RadionuclideConcentration.DATUM.getTimeInMillis();
        final double halfLives = duration / this.nuclide.halfLife;
        return halfLives;
    }

    /**
     * RadionuclideConcentration.getConcentration
     * 
     * @param now current time
     * @return activity of this nuclide in the solution, in MBq/litre
     */
    public double getMBqPerLitre(final Calendar now) {
        final double halfLives = this.getHalfLivesSinceDatum(now);
        return this.concentration * Math.pow(2d, -halfLives);
    }

    /**
     * RadionuclideConcentration.getAmount
     * 
     * @param now current time
     * @param litres vlume of aliquot, in litres
     * @return MBq radioactivity in aliquot, in MBq
     */
    public double getMBq(final Calendar now, final double litres) {
        return litres * this.getMBqPerLitre(now);
    }

}
