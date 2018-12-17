/*
 * Created on 15.08.2005 - Code Style - Code Templates
 */
package org.pimslims.presentation.create;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.pimslims.lab.Measurement;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.mru.MRUController;

/**
 * Converts a value the user has entered from a string to the appropriate Java type to save.
 * 
 * @author Petr Troshin
 */
public class ValueConverter {

    public MetaClass metaclass;

    MetaAttribute metaAttribute;

    public Map<String, String> errorMessages;

    public ValueConverter(final MetaClass metaclass, final Map<String, String> errorMessages) {
        this.metaclass = metaclass;
        this.errorMessages = errorMessages;
    }

    public Object getConvertedValue(final String attrName, final String attrValue) {
        // System.out.println("VC MNAME " + metaclass.getMetaClassName());
        this.metaAttribute = this.metaclass.getAttribute(attrName);
        if (this.metaAttribute == null) {
            throw new IllegalArgumentException("No such attribute: " + attrName);
        }
        final Class<?> javaType = this.metaAttribute.getType();
        Object ret = null;
        try {
            if (javaType == java.lang.String.class) {
                ret = this.getString(attrValue);
            } else if (javaType == java.lang.Boolean.class) {
                ret = this.getBoolean(attrValue);
            } else if (javaType == java.lang.Long.class) {
                ret = this.getLong(attrValue);
            } else if (javaType == java.lang.Integer.class) {
                ret = this.getInt(attrValue);
            } else if (javaType == java.lang.Float.class) {
                // TODO recognise measurements more carefully
                if (attrName.equals("amount") || attrName.equals(Sample.PROP_CURRENTAMOUNT)
                    || attrName.equals("concentration")) {
                    if (null == attrValue) {
                        return null;
                    }
                    return Measurement.getMeasurement(attrValue);
                }
                ret = this.getFloat(attrValue);
            } else if (javaType == java.lang.Double.class) {
                ret = this.getDouble(attrValue);
                // Must treat below as List for the ModelObject construction but as
                // String[]
                // for the setXXX() methods of the ModelObject
            } else if (javaType == String[].class) { // "[Ljava.lang.String"
                ret = this.getStringArray(attrValue, ";|\n");
            } else if (javaType.isAssignableFrom(java.util.List.class)) {
                ret = this.getStringArray(attrValue, ";|\n");
            } else if (javaType == java.util.Calendar.class) {
                ret = this.getCalendar(attrValue);
            } else {
                System.out.println("UNSUPPORTED TYPE: !!! " + javaType);
                throw new AssertionError("Data generation for class " + javaType + " is unsupported ");
            }
        } catch (final NumberFormatException e) {
            System.out.println(e.getCause());
            this.errorMessages.put(this.metaAttribute.getName(), "Please enter a number instead of: "
                + attrValue);
        }
        return ret;
    }

    public String getString(final Object attrValue) {
        final String value = (String) attrValue;
        if (value == null || value.trim().length() == 0) {
            return ""; // for compatibility with Oracle
        }
        if (MRUController.NONE.equalsIgnoreCase(value)) {
            return "";
        }
        final int flength = this.metaAttribute.getLength();
        if (flength > 0) {
            if (value.length() > flength) {
                this.errorMessages.put(this.metaAttribute.getName(), "Too long: " + value);
                return (String) attrValue;
            }
        }
        return value;
    }

    public Boolean getBoolean(final Object attrValue) {
        Boolean value;
        final String attrvalue = ((String) attrValue).trim();
        if (attrvalue == null || attrvalue.length() == 0) {
            return null;
        }

        if (attrvalue.equalsIgnoreCase("Yes") || attrvalue.equalsIgnoreCase("true")) {
            value = new Boolean(true);
        } else if (attrvalue.equalsIgnoreCase("No") || attrvalue.equalsIgnoreCase("false")) {
            value = new Boolean(false);
        } else {
            value = null;
        }
        return value;
    }

    private String trimString(final Object attrValue) {
        return attrValue != null ? ((String) attrValue).trim() : null;
    }

    private Long getLong(final Object attrValue) throws NumberFormatException {
        if (attrValue == null || ((String) attrValue).length() == 0) {
            return null;
        }
        final String attrV = this.trimString(attrValue);
        Long attrLongV = null;

        attrLongV = new Long(Long.parseLong(attrV));

        return attrLongV;
    }

    public Integer getInt(final Object attrValue) throws NumberFormatException {
        if (attrValue == null || ((String) attrValue).length() == 0) {
            return null;
        }
        final String attrV = this.trimString(attrValue);
        Integer attrIntV = null;

        attrIntV = new Integer(Integer.parseInt(attrV));

        return attrIntV;
    }

    public Float getFloat(final Object attrValue) throws NumberFormatException {
        if (attrValue == null || ((String) attrValue).length() == 0) {
            return null;
        }
        final String attrV = this.trimString(attrValue);
        Float attrFloatV = null;

        attrFloatV = new Float(Float.parseFloat(attrV));

        return attrFloatV;
    }

    public Double getDouble(final Object attrValue) throws NumberFormatException {
        if (attrValue == null || ((String) attrValue).length() == 0) {
            return null;
        }
        final String attrV = this.trimString(attrValue);
        Double attrDoubleV = null;

        attrDoubleV = new Double(Double.parseDouble(attrV));

        return attrDoubleV;
    }

    public List<String> getStringArray(String value, final String delimiter) {
        if (null == value || "".equals(value)) {
            return Collections.EMPTY_LIST;
        }
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1);
        }
        final String[] split = value.split(delimiter);
        return Arrays.asList(split);
    }

    public Calendar getCalendar(final Object attrValue) {
        final Calendar ret = Calendar.getInstance();
        try {
            final Long timestamp = Long.valueOf((String) attrValue);
            ret.setTimeInMillis(timestamp);
            return ret;
        } catch (final NumberFormatException e) {
            new Throwable("Obsolete date value: " + attrValue).fillInStackTrace().printStackTrace();
            // now try the old format
        }
        // this code will soon be obsolete:
        final SimpleDateFormat sdf = Utils.getDateFormat();
        Date d = null;
        try {
            d = sdf.parse((String) attrValue);
        } catch (final ParseException pex) {
            this.errorMessages.put(this.metaAttribute.getName(), "Can't understand date: " + attrValue);
            return null;
        }
        ret.setTime(d);
        return ret;
    }

    /**
     * ValueConverter.doGetCalendar TODO get this under test
     * 
     * @param parameter
     * @return
     */
    public static Calendar doGetCalendar(final String parameter) {
        final Calendar ret = Calendar.getInstance();
        if (null == parameter || "".equals(parameter)) {
            return ret;
        }
        try {
            final Long timestamp = Long.valueOf(parameter);
            ret.setTimeInMillis(timestamp);
            return ret;
        } catch (final NumberFormatException e) {
            // now try alternative format, e.g. -1w
        }
        final int value = Integer.valueOf(parameter.substring(0, parameter.length() - 1));
        if (parameter.endsWith("w")) {
            //TODO what if first or last week?
            ret.add(Calendar.WEEK_OF_YEAR, value);
        } else if (parameter.endsWith("y")) {
            ret.add(Calendar.YEAR, value);
        } else if (parameter.endsWith("d")) {
            //TODO what if first or last day?
            ret.add(Calendar.DAY_OF_YEAR, value);
        }
        return ret;
    }

}
