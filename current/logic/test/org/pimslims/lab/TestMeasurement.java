/*
 * Created on 03-Mar-2005 @author: Chris Morris
 */
package org.pimslims.lab;

import junit.framework.Assert;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.sample.Sample;

/**
 * Test the represenation of a physical quantity.
 * 
 * @version 0.1
 */
public class TestMeasurement extends junit.framework.TestCase {

    /**
     * When you compare calculated floating point values, they usually aren't exactly equal, so you need a
     * tolerance.
     */
    private static final float ERROR = 1e-10f;

    private final AbstractModel model;

    /**
     * Test the factory method that expects a map
     */
    public void testMap() {
        final java.util.Map map = new java.util.HashMap();
        map.put("duration", new Float(1000f));
        map.put("durationUnit", "ms");
        map.put("durationDisplayUnit", "Ks");
        final Measurement m = Measurement.getMeasurement(map, "duration");
        Assert.assertEquals("unit", "s", m.getStorageUnit());
        Assert.assertEquals("value", 1f, m.getValue(), TestMeasurement.ERROR);
        Assert.assertEquals("unit", "Ks", m.getDisplayUnit());
    }

    /**
     * Test the method that makes a string representation
     */
    public void testToString() {

        // make a measurement
        final java.util.Map map = new java.util.HashMap();
        map.put("duration", new Float(1f));
        map.put("durationUnit", "s");
        final Measurement m = Measurement.getMeasurement(map, "duration");
        Assert.assertEquals("toString", "1s", m.toString());
    }

    /**
     * Test the factory method that takes a string
     */
    public void testParse() {
        Measurement m = Measurement.getMeasurement("0.001kg");
        Assert.assertEquals("parse", "kg", m.getStorageUnit());
        Assert.assertEquals("toString", "1g", m.toString());

        /*
         * LATER m = Measurement.getMeasurement("amount","1m2");
         * assertEquals("parse","m2",m.getStorageUnit());
         */

        m = Measurement.getMeasurement("-.2m");
        Assert.assertEquals("parse", "m", m.getStorageUnit());
        Assert.assertTrue("negative", 0.0f > m.getValue());

        try {
            // and test bad string
            Measurement.getMeasurement("nonesuch");
            Assert.fail("no error on bad string");
        } catch (final java.lang.IllegalArgumentException ex) {
            // that's fine
        }
    }

    /**
     * Test the factory method that takes a string
     */
    public void testEuropean() {
        final Measurement m = Measurement.getMeasurement("0,001kg");
        Assert.assertEquals("parse", "kg", m.getStorageUnit());
        Assert.assertEquals("toString", "1g", m.toString());

    }

    /**
     * Generic tests for simple units, i.e. ones where the conversion is scaling only. This method does not
     * test for conversion that needs supplementaty information.
     * 
     * @param unit e.g. "L" for volume
     */
    private void simpleTests(final String unit) {

        // test parsing
        Measurement m = Measurement.getMeasurement("0.001k" + unit);

        // test scaling
        Assert.assertEquals("scale", 1f, m.getValue(), TestMeasurement.ERROR);
        Assert.assertEquals("SI unit", unit, m.getStorageUnit());
        Assert.assertEquals("scale to SI unit", 1f, m.getValue(), TestMeasurement.ERROR);

        // display unit
        m.setDisplayUnit("k" + unit);
        final String s = m.toString();
        Assert.assertTrue("set display unit", s.endsWith("k" + unit));
        // this should be roughly "0.001k"+unit
        // but there will be a small rounding error,
        // so the test must parse the string
        // - it gets Measurement to do that
        final Measurement mass2 = Measurement.getMeasurement(s);
        Assert.assertEquals("displayUnit conversion", 1f, mass2.getValue(), TestMeasurement.ERROR);

        /*
         * LATER bad units Measurement mass3 = Measurement.getMeasurement(m); m.setDisplayUnit("nonesuch");
         * assertEquals("bad display unit", mass3.toString(), m.toString());
         */

        // test parsing with no prefix
        m = Measurement.getMeasurement("1" + unit);
        Assert.assertEquals("value", 1f, m.getValue(), TestMeasurement.ERROR);
        Assert.assertEquals("SI unit", unit, m.getStorageUnit());

    }

    public void testNoMeasurement() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, "meas" + System.currentTimeMillis());
            sample.setAmountUnit("L");
            sample.setAmountDisplayUnit("");
            final Measurement amount =
                Measurement.getMeasurement(Sample.PROP_CURRENTAMOUNT, sample, Sample.PROP_AMOUNTUNIT,
                    Sample.PROP_AMOUNTDISPLAYUNIT);
            Assert.assertNotNull(amount);
            Assert.assertEquals("0.0nL", amount.toString());
        } finally {
            version.abort();
        }
    }

    public void testNoStorageUnit() throws ConstraintException {
        final WritableVersion version = this.model.getTestVersion();
        try {
            final Sample sample = new Sample(version, "meas" + System.currentTimeMillis());
            // dont sample.setAmountUnit("L");
            sample.setAmountDisplayUnit("");
            final Measurement amount =
                Measurement.getMeasurement(Sample.PROP_CURRENTAMOUNT, sample, Sample.PROP_AMOUNTUNIT,
                    Sample.PROP_AMOUNTDISPLAYUNIT);
            Assert.assertEquals("0.0", amount.toString());
        } finally {
            version.abort();
        }
    }

    public void testScaleFactor() {
        Assert.assertEquals("metres", 1f, Measurement.getScaleFactor("m"), TestMeasurement.ERROR);
        Assert.assertEquals("millimetres", 0.001f, Measurement.getScaleFactor("mm"), TestMeasurement.ERROR);
    }

    public void testDefaultDisplayUnit() {
        Measurement m = new Measurement(0.0001f, "L");
        Assert.assertEquals("uL", m.getDisplayUnit());
        m = new Measurement(10000f, "g");
        Assert.assertEquals("kg", m.getDisplayUnit());
    }

    public void testNoUnit() {
        final Measurement m = new Measurement(0.0001f, "");
        Assert.assertEquals("", m.getDisplayUnit());
        Assert.assertEquals("", m.getStorageUnit());
        Assert.assertEquals("0.0001", m.toString());
    }

    public void testSIUnit() {
        Assert.assertEquals("metres", "m", Measurement.getSIUnit("m"));
        Assert.assertEquals("millimetres", "m", Measurement.getSIUnit("mm"));
        //assertEquals("gram", "Kg", Measurement.getSIUnit("g"));
    }

    public void testDefaults() {

        Measurement m;

        m = new Measurement(10.0f, "Pigs");
        Assert.assertEquals("Pigs", m.getDisplayUnit());
        Assert.assertEquals("number", m.getStorageUnit());
        Assert.assertEquals(10.0f, m.getDisplayValue());
        Assert.assertEquals("10Pigs", m.toString());

        m = new Measurement(10.0f, "IU");
        Assert.assertEquals("IU", m.getDisplayUnit());
        Assert.assertEquals("number", m.getStorageUnit());
        Assert.assertEquals(10.0f, m.getDisplayValue());
        Assert.assertEquals("10IU", m.toString());
    }

    public void testVolumes() {

        Measurement m;
        m = new Measurement(0.0001f, "L");
        Assert.assertEquals("uL", m.getDisplayUnit());
        Assert.assertEquals("L", m.getStorageUnit());
        Assert.assertEquals(100.0f, m.getDisplayValue());
        Assert.assertEquals("100uL", m.toString());

        m = new Measurement(1000.0f, "uL");
        Assert.assertEquals("mL", m.getDisplayUnit());
        Assert.assertEquals("L", m.getStorageUnit());
        Assert.assertEquals(1.0f, m.getDisplayValue());
        Assert.assertEquals("1mL", m.toString());

        m = new Measurement(0.00001f, "nl"); // common mispelling
        Assert.assertEquals("nL", m.getDisplayUnit());
        Assert.assertEquals("L", m.getStorageUnit());
        Assert.assertEquals(0.00001f, m.getDisplayValue());
        Assert.assertEquals("0.00001nL", m.toString());
    }

    public void testMass() {

        Measurement m;
        m = new Measurement(1000.0f, "g");
        Assert.assertEquals("kg", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(1.0f, m.getDisplayValue());
        Assert.assertEquals("1kg", m.toString());

        m = new Measurement(100.0f, "g");
        Assert.assertEquals("g", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(100.0f, m.getDisplayValue());
        Assert.assertEquals("100g", m.toString());

        m = new Measurement(10.0f, "mg");
        Assert.assertEquals("mg", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(10.0f, m.getDisplayValue());
        Assert.assertEquals("10mg", m.toString());

        m = new Measurement(1000000.0f, "mg");
        Assert.assertEquals("kg", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(1.0f, m.getDisplayValue());
        Assert.assertEquals("1kg", m.toString());

        m = new Measurement(0.000001f, "kg");
        Assert.assertEquals("mg", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(1.0f, m.getDisplayValue());
        Assert.assertEquals("1mg", m.toString());

        m = new Measurement(100000f, "kg");
        Assert.assertEquals("kg", m.getDisplayUnit());
        Assert.assertEquals("kg", m.getStorageUnit());
        Assert.assertEquals(100000.0f, m.getDisplayValue());
        Assert.assertEquals("100000kg", m.toString());
    }

    /**
     * Test a basic unit
     */
    public void testLength() {
        this.simpleTests("m"); // metres
    }

    /**
     * Test a basic unit
     */
    public void testVolume() {
        this.simpleTests("L");
    }

    public TestMeasurement(final String test) {
        super(test);
        this.model = ModelImpl.getModel();
    }

    public void testConcentration() {

        Measurement m;
        m = Measurement.getMeasurement("0.0");
        Assert.assertEquals("0.0", m.toString());

        m = Measurement.getMeasurement("0.3M");
        Assert.assertEquals("value", new Float("0.3"), m.getFloatValue());
        Assert.assertEquals("display unit", "M", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "M", m.getStorageUnit());
        Assert.assertEquals("storage unit", 0.3f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5mM");
        Assert.assertEquals("value", new Float("0.0035"), m.getFloatValue());
        Assert.assertEquals("display unit", "mM", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "M", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5uM");
        Assert.assertEquals("value", new Float("0.0000035"), m.getFloatValue());
        Assert.assertEquals("display unit", "uM", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "M", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5µM");
        Assert.assertEquals("value", new Float("0.0000035"), m.getFloatValue());
        Assert.assertEquals("display unit", "µM", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "M", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5g/L");
        Assert.assertEquals("value", new Float("3.5"), m.getFloatValue());
        Assert.assertEquals("display unit", "g/L", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5µg/µL");
        Assert.assertEquals("value", new Float("3.5"), m.getFloatValue());
        Assert.assertEquals("display unit", "µg/µL", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5ng/µL");
        Assert.assertEquals("value", new Float("0.0035"), m.getFloatValue());
        Assert.assertEquals("display unit", "ng/µL", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5mg/L");
        Assert.assertEquals("value", new Float("0.0035"), m.getFloatValue());
        Assert.assertEquals("display unit", "mg/L", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5µg/L");
        Assert.assertEquals("value", new Float("0.0000035"), m.getFloatValue());
        Assert.assertEquals("display unit", "µg/L", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        // be robust against common error in encoding
        m = Measurement.getMeasurement("3.5Âµg/mL");
        Assert.assertEquals("value", new Float("0.0035"), m.getFloatValue());

        m = Measurement.getMeasurement("3.5µg/mL");
        Assert.assertEquals("value", new Float("0.0035"), m.getFloatValue());
        Assert.assertEquals("display unit", "µg/mL", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5%w/v");
        Assert.assertEquals("value", new Float("3.5"), m.getFloatValue());
        Assert.assertEquals("display unit", "%w/v", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5%v/v");
        Assert.assertEquals("value", new Float("3.5"), m.getFloatValue());
        Assert.assertEquals("display unit", "%v/v", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "m3/m3", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3.5%w/w");
        Assert.assertEquals("value", new Float("3.5"), m.getFloatValue());
        Assert.assertEquals("display unit", "%w/w", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "kg/kg", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.5f, m.getDisplayValue());

        m = Measurement.getMeasurement("3pellet");
        Assert.assertEquals("value", new Float("3.0"), m.getFloatValue());
        Assert.assertEquals("display unit", "pellet", m.getDisplayUnit());
        Assert.assertEquals("storage unit", "number", m.getStorageUnit());
        Assert.assertEquals("storage unit", 3.0f, m.getDisplayValue());

    }
    /**
     * Test conversion from Molar to g/L
     * 
     * public void testConcentration() { simpleTests("M"); Molar simpleTests("g/L"); // grams per litre
     * 
     * Measurement m = Measurement.getMeasurement("concentration","0.3M"); assertTrue("concentration", m
     * instanceof Measurement.Concentration); Measurement.Concentration conc = (Measurement.Concentration)m;
     * 
     * //now test special conversions conc.setMolecularMass(37f); conc.setStorageUnit("g/L");
     * assertEquals("convert", "g/L", conc.getStorageUnit()); //LATER assertEquals("convert to g/L", ???,
     * conc.getValue()); conc.setStorageUnit("M"); assertEquals("convert to M", 0.3f, conc.getValue(), ERROR);
     * 
     * conc.canonicalize(); //LATER assertEquals("canonicalise", ??, conc.getStorageUnit()); }
     */

    /**
     * Test that the units used are appropriate to the property
     * 
     * public void testAppropriateUnit() { try { java.util.Map map = new java.util.HashMap();
     * map.put("duration", new Float(1000f)); map.put("durationUnit", "mg"); Measurement.getMeasurement(map,
     * "duration"); fail("Attribute is called duration but unit is grams"); } catch (final
     * IllegalArgumentException ex) { // that's fine } }
     */

}
