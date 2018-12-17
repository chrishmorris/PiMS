/*
 * Created on 03-Mar-2005 @author: Chris Morris
 */
package org.pimslims.lab;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.metamodel.ModelObject;

/**
 * Represents a physical quantity.
 * 
 * In most cases, the data model stores a floating point number, a string representing the units it is in, and
 * a string representing the prefered units for display.
 * 
 * This class provides methods to support operations on values represented in this way.
 * 
 * @version 0.1
 */
public class Measurement implements Cloneable {

    /**
     * List of quantities that are represented by data model attributes.
     * 
     * For each class with a quantity attribute, there are attributes Float xxxx String xxxxUnit String
     * xxxxDisplayUnit
     * 
     * private static final java.util.Collection<String> QUANTITIES = Arrays.asList(new String[] { "amount",
     * "concentration", "duration", "flowrate", "pressure", "temperature", "volume" //LATER and ? memCutoff,
     * tauE, tauS });
     */

    /**
     * Pattern for unit string. Examples are: 3M 1.0m3 .2g/L -2mm
     */
    private static final Pattern REGEXP = Pattern
        .compile("^((?:[+-]|)(?:\\d*[.]\\d+)|\\d+)Â{0,1}([%a-zA-Zµ\\[].*)$");

    /**
     * "^((?:[+-]|)(?:\\d*[.]\\d+)|\\d+)([a-zA-Zµ].*)$"
     * 
     * group 1 ((?:[+-]|)(?:\\d*[.]\\d+)|\\d+) (?:[+-]|) (?:\\d*[.]\\d+) |\\d+
     * 
     * group2 ([a-zA-Zµ].*) // a thru z or A thru Z or micro
     */

    /**
     * Factory method that parses a string
     * 
     * @param name name of the attribute, e.g. "duration"
     * @param string value entered by the user, e.g. "300ms"
     * @return a Measurement
     * @throws NumberFormatException if a number cannot be found
     * @throws IllegalArgumentException if a unit cannot be reconsided
     */
    public static final Measurement getMeasurement(String string) {

        if ("0.0".equals(string)) {
            return new Measurement(0f, "");
        }

        //System.out.println("Measurement getMeasurement [" + name + ":" + string + "]");
        if (null == string || "".equals(string) || Measurement.noUnits(string)) {
            return null;
            //return new Measurement(name, new Float(0), "");
        }
        string = string.replace(',', '.'); // European decimal point
        final Matcher matcher = Measurement.REGEXP.matcher(Measurement.strim(string));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Should be like nnn.nnnUnit e.g. 0.3s or 300ms, not: "
                + string);
        }

        final String v = matcher.group(1);
        String unit = matcher.group(2);
        if (Measurement.noUnits(unit)) {
            unit = "";
        }

        final Float value = new Float(v); // don't expect an exception, because of regexp
        //NumberFormat.getNumberInstance(locale.GERMAN).parse("-1.234,56")

        return new Measurement(value, unit);
    }

    /**
     * Factory method that obtains the value, uit, and display unit from a Map of values.
     * 
     * @param values Map of attribute name => value
     * @param name name of measurement attribute
     * @return a new instance of Measurement
     */
    public static final Measurement getMeasurement(final java.util.Map values, final String name) {
        final Measurement measurement =
            new Measurement((Float) values.get(name), (String) values.get(name + "Unit"));
        final String displayUnit = (String) values.get(name + "DisplayUnit");
        if (null != displayUnit) {
            measurement.displayUnit = displayUnit;
        }

        return measurement;
    }

    /**
     * Factory method to make a copy
     * 
     * @param measurement the object to copy
     * @return a Measurement
     */
    public static final Measurement getMeasurement(final Measurement measurement) {
        try {
            return (Measurement) measurement.clone();
        } catch (final CloneNotSupportedException ex) {
            // should not happen
            throw new RuntimeException(ex);
        }
    }

    /**
     * The quantity represented.
     */
    private Float value = null;

    /**
     * The unit in which value is currently represented, e.g. "ms"
     */
    private String storageUnit = null;

    /**
     * The preferred unit for output.
     */
    private String displayUnit = null;

    /**
     * @param name name of attribute, e.g. "concentration"
     * @param value value
     * @param unit unit the value is in
     */
    protected Measurement(final Float value, final String unit) {

        super();

        assert null != unit;
        final Double d =
            new Double(Measurement.getScaleFactor(unit) + "").doubleValue()
                * new Double(value + "").doubleValue();
        this.value = d.floatValue();
        this.storageUnit = Measurement.getSIUnit(unit);

        if (Measurement.specialUnit(unit)) {
            this.displayUnit = unit;
        } else if (this.storageUnit.equals("number")) {
            this.displayUnit = unit;
        } else if (this.value == 0f) {
            this.displayUnit = unit;
        } else {
            this.displayUnit = this.getDisplayUnit();
        }

        //System.out.println("Measurement [" + name + "," + value + " " + unit + " stored as " + this.value
        //    + " " + this.storageUnit + " (scale factor " + Measurement.getScaleFactor(unit)
        //    + ") (display unit " + this.displayUnit + ")]");

        // LATER check that unit is appropriate to the name
    }

    private static final Map<String, Double> PREFIXES = new HashMap<String, Double>();
    {
        Measurement.PREFIXES.put("G", new Double(1E9));
        Measurement.PREFIXES.put("M", new Double(1E6));
        Measurement.PREFIXES.put("k", new Double(1E3));
        Measurement.PREFIXES.put("K", new Double(1E3));
        Measurement.PREFIXES.put("m", new Double(1E-3));
        Measurement.PREFIXES.put("u", new Double(1E-6));
        Measurement.PREFIXES.put("µ", new Double(1E-6));
        Measurement.PREFIXES.put("�", new Double(1E-6));
        Measurement.PREFIXES.put("n", new Double(1E-9));
        Measurement.PREFIXES.put("p", new Double(1E-12));
        Measurement.PREFIXES.put("f", new Double(1E-15));
    }

    /**
     * LATER support rations like mg/mL, and m2, m3
     * 
     * @param unit e.g "ms", "km"
     * @return the SI unit, e,g, "s", "m"
     */
    static String getSIUnit(final String unit) {

        if (unit == null) {
            return "";
        }

        if (Measurement.wvConcentrationUnit(unit)) {
            return "kg/m3";
        }
        if (unit.equals("%w/v")) {
            return "kg/m3";
        }
        if (unit.equals("%w/w")) {
            return "kg/kg";
        }
        if (unit.equals("%v/v")) {
            return "m3/m3";
        }

        if (unit.endsWith("g")) {
            return "kg";
        }

        if (1 >= unit.length()) {
            return unit;
        }

        if (unit.equals("IU") || 2 < unit.length()) {
            return "number";
        }

        if (unit.endsWith("l")) {
            return "L"; // common mispelling
        }

        for (final Iterator iter = Measurement.PREFIXES.keySet().iterator(); iter.hasNext();) {
            final String scale = (String) iter.next();
            if (unit.startsWith(scale)) {
                return unit.substring(scale.length());
            }
        }
        throw new IllegalArgumentException("Unrecognised Units: " + unit);
        // return unit;
    }

    /**
     * 
     * Later support m2, m3
     * 
     * @param unit e.g "ms", "km"
     * @return the conversion factor to the SI unit, e.g 0.001, 1000 respectively
     */
    /*
     * public static float getScaleFactor(String unit) { if (null==unit || 1>=unit.length()) {return 1f;} for
     * (Iterator iter = PREFIXES.entrySet().iterator(); iter.hasNext();) { Map.Entry entry = (Entry)
     * iter.next(); if (unit.startsWith((String)entry.getKey())) { return
     * ((Float)entry.getValue()).floatValue(); } } return 1f; }
     */

    static double getScaleFactor(final String unit) {

        final double scale = 1f;

        if (Measurement.wvConcentrationUnit(unit)) {
            final String[] units = unit.split("/");
            if (units.length != 2) {
                System.out.println("Invalid unit string [" + unit + "]");
                return scale;
            }
            return Measurement.getScale(units[0]) / Measurement.getScale(units[1]);
        }

        if (null == unit || unit.length() > 2) {
            return scale;
        }

        //Special case for Mass where SI unit is Kg
        if (unit.endsWith("g")) {
            return Measurement.getScale(unit) / 1000;
        }

        // for unit of m
        if (unit.length() == 1) {
            return scale;
        }

        return Measurement.getScale(unit);
    }

    private static double getScale(final String unit) {
        double scale = 1f;
        for (final Iterator iter = Measurement.PREFIXES.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry entry = (Entry) iter.next();
            if (unit.startsWith((String) entry.getKey())) {
                scale = ((Double) entry.getValue()).doubleValue();
            }
        }
        return scale;
    }

    /**
     * @return Returns the value.
     */
    public float getValue() {
        //System.out.println("Measurement.getValue");
        return this.value.floatValue();
    }

    /**
     * @return Returns the value.
     */
    public Float getFloatValue() {
        //System.out.println("Measurement.getFloatValue");
        return this.value;
    }

    /**
     * @return Returns the storageUnit.
     */
    public String getStorageUnit() {
        return this.storageUnit;
    }

    /**
     * @return Returns the displayUnit.
     */
    public String getDisplayUnit() {

        if (null != this.displayUnit && !"".equals(this.displayUnit)) {
            return this.displayUnit;
        }

        if ("L".equals(this.storageUnit)) {

            // calculate the right display unit
            if (this.value > 1000000000f) {
                return "G" + this.storageUnit;
            }

            if (this.value > 1000000f) {
                return "M" + this.storageUnit;
            }

            if (this.value > 1000f) {
                return "K" + this.storageUnit;
            }

            if (this.value < 0.000001f) {
                return "n" + this.storageUnit;
            }

            if (this.value < 0.001f) {
                return "u" + this.storageUnit;
            }

            if (this.value < 1f) {
                return "m" + this.storageUnit;
            }
        }

        if ("kg".equals(this.storageUnit)) {

            // calculate the right display unit
            //if (this.value > 1000000f) {
            //    return "Gg";
            //}

            //if (this.value > 1000f) {
            //    return "Mg";
            //}

            if (this.value < 0.000000000001f) {
                return "pg";
            }

            if (this.value < 0.000000001f) {
                return "ng";
            }

            if (this.value < 0.000001f) {
                return "ug";
            }

            if (this.value < 0.001f) {
                return "mg";
            }

            if (this.value < 1f) {
                return "g";
            }

            return "kg";
        }

        if ("M".equals(this.storageUnit)) {

            // calculate the right display unit
            if (this.value < 0.000000000001f) {
                return "f" + this.storageUnit;
            }

            if (this.value < 0.000000001f) {
                return "p" + this.storageUnit;
            }

            if (this.value < 0.000001f) {
                return "n" + this.storageUnit;
            }

            if (this.value < 0.001f) {
                return "u" + this.storageUnit;
            }

            if (this.value < 0.1f) {
                return "m" + this.storageUnit;
            }
        }

        return this.getStorageUnit();
    }

    /**
     * @param displayUnit The displayUnit to set.
     */
    public void setDisplayUnit(final String displayUnit) {
        this.displayUnit = displayUnit;
    }

    public float getDisplayValue() {

        //System.out.println("Measurement.getDisplayValue [" + this.value + ":" + this.getStorageUnit() + ":"
        //    + this.getDisplayUnit() + "]");
        if (null == this.value) {
            return 0;
        }

        final Float f =
            new Float(new BigDecimal(this.value.toString()).divide(
                new BigDecimal(Measurement.getScaleFactor(this.getDisplayUnit())), 12,
                BigDecimal.ROUND_HALF_UP).floatValue());

        //System.out.println("Measurement.getDisplayValue [" + f + "]");
        return f;
    }

    public static String strim(final String string) {
        return string.replace("\\w", "");
    }

    private static boolean specialUnit(final String unit) {
        if (Measurement.wvConcentrationUnit(unit)) {
            return true;
        }
        if (unit.equals("µM") || unit.equals("µL") || unit.equals("µg") || unit.equals("%w/v")
            || unit.equals("%w/w") || unit.equals("%v/v")) {
            return true;
        }
        return false;
    }

    private static boolean wvConcentrationUnit(final String unit) {
        if (unit.equals("g/L") || unit.equals("mg/mL") || unit.equals("µg/µL") || unit.equals("ug/uL")
            || unit.equals("ng/µL") || unit.equals("mg/L") || unit.equals("µg/L") || unit.equals("ug/L")
            || unit.equals("ng/mL") || unit.equals("µg/mL")) {
            return true;
        }
        return false;
    }

    private static boolean noUnits(final String unit) {
        if (unit.equals("[No Units]") || unit.equals("[NoUnits]") || unit.equals("[none]")) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {

        if (null == this.value) {
            return "";
        }

        Float f = new Float(this.getDisplayValue());

        // PiMS 394 display 100 - 999nl as 0.1 - 0.999uL
        if (this.getDisplayUnit().equals("nL") && f < 1000f && f > 99f) {
            f =
                new Float(new BigDecimal(f.toString()).divide(new BigDecimal(1000), 12,
                    BigDecimal.ROUND_HALF_UP).floatValue());
            return f.toString() + "uL";
        }

        //System.out.println("float [" + f.toString() + "]");
        //final NumberFormat form = NumberFormat.getInstance(Locale.ENGLISH);
        //System.out.println("format [" + form.format(f) + "]");

        if (f > 1000f) {
            final DecimalFormat form = new DecimalFormat("######0.###");
            return form.format(f) + this.getDisplayUnit();
        }

        if (f > 0.001f) {
            final NumberFormat form = NumberFormat.getInstance(Locale.ENGLISH);
            return form.format(f) + this.getDisplayUnit();
        }

        if (f > 0.000001f) {
            final DecimalFormat form = new DecimalFormat("0.0000##");
            return form.format(f) + this.getDisplayUnit();
        }

        return f.toString() + this.getDisplayUnit();
    }

    /**
     * An implementation of Measurement for concentrations. Supports conversion from density to Molar
     * concentration.
     * 
     * Inner type of Measurement
     * 
     * @version 0.1
     */
    public static class Concentration extends Measurement {

        /**
         * Mass of the substance, if known. If this is set, it will be possibel to convert between density
         * units and molar units.
         */
        private float molecularMass = 0f;

        /**
         * @param name
         * @param value
         * @param storageUnit
         */
        protected Concentration(final String name, final Float value, final String storageUnit) {
            super(value, storageUnit);
            assert ("concentration".equals(name));
        }

        /**
         * @return Returns the molecularMass.
         */
        public float getMolecularMass() {
            return this.molecularMass;
        }

        /**
         * @param molecularMass The molecularMass to set.
         */
        public void setMolecularMass(final float molecularMass) {
            this.molecularMass = molecularMass;
        }
    }

    /**
     * @param name attribute name
     * @param object model object
     * @param storageUnit attribute name for the Unit
     * @param displayUnit attribute name for the Display Unit
     * @return an instance of Measurement representing the value
     */
    public static Measurement getMeasurement(final String name, final ModelObject object,
        final String storageUnit, final String displayUnit) {

        //System.out.println("Measurement getMeasurement [" + object.get_Value(name) + "]");
        //System.out.println("Measurement getMeasurement [" + object.get_Value(storageUnit) + "]");
        //System.out.println("Measurement getMeasurement [" + object.get_Value(displayUnit) + "]");

        Float v = ((Float) object.get_Value(name));
        final String sUnit = (String) object.get_Value(storageUnit);

        if (null == v) {
            //if (null == sUnit || 0 == sUnit.length()) {
            //    return null;
            //}
            v = new Float(0f);
        }
        final Measurement measurement = new Measurement(v.floatValue(), sUnit);

        if (null != displayUnit) {
            measurement.displayUnit = (String) object.get_Value(displayUnit);
        }

        return measurement;
    }

    public static boolean isAmount(final String str) {
        try {
            Measurement.getMeasurement(str);
        } catch (final Exception e) {
            return false;
        }
        return true;
    }
}
