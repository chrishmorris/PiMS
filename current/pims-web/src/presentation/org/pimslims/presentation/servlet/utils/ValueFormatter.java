/*
 * Created on 30.08.2005 - Code Style - Code Templates
 */
package org.pimslims.presentation.servlet.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.pimslims.lab.Measurement;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.presentation.ServletUtil;

/**
 * @author Petr Troshin
 */
public class ValueFormatter {

    // Map errorMessages are not used here!
    public static final int MAX_STRING_LENGTH = 60;

    private final MetaClass metaclass;

    MetaAttribute mAttribute;

    private final ModelObject mObject;

    Object value;

    /**
     * @param metaclass
     * @param attributeName
     * @param attrValue
     */
    public ValueFormatter(final ModelObject mObject) {
        this.mObject = mObject;
        this.metaclass = mObject.get_MetaClass();
    }

    public String getFormatedValue(final String attributName) {
        this.mAttribute = this.metaclass.getAttribute(attributName);
        this.value = this.mObject.get_Value(attributName);
        if (this.value == null) {
            return ""; // this is important and then remove the if(null) from
            // the formatXXX
        }

        if (this.value instanceof java.lang.Float) {
            final String storageUnit =
                ServletUtil.getStorageUnitAttributeName(this.mObject.get_MetaClass().getMetaClassName(),
                    attributName);
            if (null == storageUnit || null == this.mObject.get_Value(storageUnit)) {
                return ((Float) this.value).toString(); // must be dimensionless
            }
            final String displayUnit =
                ServletUtil.getDisplayUnitAttributeName(this.mObject.get_MetaClass().getMetaClassName(),
                    attributName);
            final Measurement m =
                Measurement.getMeasurement(attributName, this.mObject, storageUnit, displayUnit);
            return m.toString();
        }

        return this.setup();
    }

    private String setup() {
        String formatedValue = "";
        if (this.value instanceof java.lang.String) {
            formatedValue = this.formatString();
        } else if (this.value instanceof java.lang.Boolean) {
            formatedValue = this.formatBoolean(((Boolean) this.value)); // why this
            // function
            // need pass
            // the
            // object
            // but
            // others
            // not
        } else if (this.value instanceof java.lang.Long) {
            formatedValue = ((Long) this.value).toString();
        } else if (this.value instanceof java.lang.Integer) {
            formatedValue = ((Integer) this.value).toString();
        } else if (this.value instanceof java.lang.Double) {
            formatedValue = ((Double) this.value).toString();
            // Must treat below as List for the ModelObject construction but as
            // String[]
            // for the setXXX() methods of the ModelObject
        } else if (this.value instanceof String[]) { // "[Ljava.lang.String"
            formatedValue = this.formatArray("; ");
            // } else if(this.value instanceof java.util.ArrayList) {//Not
            // needed as ArrayList is subclass of List
            // formatedValue = formatList("; ");//Not needed as ArrayList is
            // subclass of List
        } else if (this.value instanceof java.util.List) {
            formatedValue = this.formatList("; ");
        } else if (this.value instanceof java.util.Map) {
            throw new RuntimeException("MAPS are not supported "); // the info
            // of this
            // exception
            // is too
            // few
        } else if (this.value instanceof Calendar) {
            formatedValue = this.formatCalendar();
        } else {
            System.out.println("UNSUPPORTED TYPE: !!! " + this.value.getClass());
            throw new AssertionError("Data generation for class " + this.value.getClass()
                + " is unsupported ");
        }
        return formatedValue;
    }

    public String formatBoolean(final Boolean bool) {

        if (bool.booleanValue()) {
            return "Yes";
        }
        return "No";
    }

    private String formatCalendar() {
        return ValueFormatter.formatDate((Calendar) this.value);
    }

    /**
     * ValueFormatter.formatDate
     * 
     * @param date
     * @return a String representation of the date The date is passed to the browser in UTC Only javascript
     *         can do the time zone conversion.
     */
    public static String formatDate(final Calendar date) {
        return Long.toString(date.getTimeInMillis());
    }

    public String formatArray(final String delimiter) {
        String formatedValue = "";
        final String[] str = (String[]) this.value;
        for (int i = 0; i < str.length; i++) {
            formatedValue += ValueFormatter.concatDelimValue(str[i], delimiter);
        }
        return formatedValue;
    }

    public String formatList(final String delimiter) {
        final ArrayList ar = new ArrayList((List) this.value);
        String formatedValue = "";
        for (final Iterator iter = ar.iterator(); iter.hasNext();) {
            final String elem = (String) iter.next();
            formatedValue += ValueFormatter.concatDelimValue(elem, delimiter);
        }
        return formatedValue;
    }

    private static String concatDelimValue(final String value, final String delimiter) {
        if (null != value && value.trim().length() > 0) {
            return value + delimiter;
        }
        return "";
    }

    public String formatString() {
        String formatedValue = (String) this.value;
        if (formatedValue.trim().length() == 0) {
            return "";
        }
        final int flength = formatedValue.length();
        if (this.mAttribute.getName().indexOf("seqString") >= 0
            || this.mAttribute.getName().indexOf("Seq") >= 0) {
            formatedValue = ValueFormatter.getFormatedSequence((String) this.value);
        } else {
            if (flength >= ValueFormatter.MAX_STRING_LENGTH) {
                formatedValue = this.insertSpaces();
            }
        }
        return formatedValue;
    }

    /**
     * Format sequence per 80 letter in one string. Without spaces.
     * 
     * @param longSequence
     * @return multiple line formated sequence, one line 80 letters length
     */
    public static String getFormatedSequence(final String sequence) {
        if (sequence == null || sequence.equals("")) {
            return "";
        }
        final StringBuffer sb = new StringBuffer(ValueFormatter.toOneString(sequence));
        for (int i = 0; i < sb.length(); i = i + 11) {
            sb.insert(i, " ");
        }
        return sb.toString();
    }

    public static String toOneString(final String value) {
        String cleanValue = "";
        for (final StringTokenizer st = new StringTokenizer(value); st.hasMoreTokens();) {
            cleanValue += st.nextToken();
        }
        return cleanValue;
    }

    /**
     * Test whether spring separated by spaces or new lines, if not add some
     * 
     * @return
     */
    public String insertSpaces() {
        // Seek space in the interval of incr*2
        final int incr = 20;
        String str = (String) this.value;
        // No need to put spaces the string is short
        if (str != null && str.length() < ValueFormatter.MAX_STRING_LENGTH) {
            return str;
        }

        // Long string need to investigate are there spaces?
        // If no spaces find in a incr*2, add some one.
        String line = str;
        int curInd = 0;
        while (line.length() > ValueFormatter.MAX_STRING_LENGTH) {
            int spacePos;

            if (line.length() > ValueFormatter.MAX_STRING_LENGTH + incr) {
                spacePos =
                    this.getSpacePos(line.substring(ValueFormatter.MAX_STRING_LENGTH - incr,
                        ValueFormatter.MAX_STRING_LENGTH + incr));
            } else {
                spacePos =
                    this.getSpacePos(line.substring(ValueFormatter.MAX_STRING_LENGTH - incr,
                        ValueFormatter.MAX_STRING_LENGTH));
            }
            curInd = curInd + ValueFormatter.MAX_STRING_LENGTH;

            if (spacePos == -1) {
                str = (new StringBuffer(str)).insert(curInd, ' ').toString();
                curInd++;
            }
            line = str.substring(curInd);
        }

        return str;
    }

    /**
     * Seek and return the position of a space or new line character in a string
     * 
     * @param str
     * @return int position of the space character in the str. If the space or new line charecter is not found
     *         return -1
     */
    int getSpacePos(final String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ' || str.charAt(i) == Character.LINE_SEPARATOR) {
                return i;
            }
        }
        return -1;
    }

}
