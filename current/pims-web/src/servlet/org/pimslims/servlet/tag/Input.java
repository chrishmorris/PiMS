/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.constraint.Constraint;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.presentation.mru.MRUController;

/**
 * e.g.
 * 
 * <pims:input attribute="${entry.value}" value="${modelObject.values[entry.key]}" />
 * 
 * @author cm65
 * 
 */
public class Input extends PIMSTag {

    /**
     * Information about the attribute to input
     */
    private MetaAttribute attribute = null;

    /**
     * The previous or default value of the attribute to display
     */
    private Object value = null;

    /**
     * The name to use for the input field. The default is the name of the metaattribute.
     * 
     * Note that the html "name" attribute is the string passed on submit. The "id" attribute is the one used
     * for javascript, in modern html standards. (In older HTML these two were confused.)
     */
    private String name = null;

    static private int UNLIMITED = -1;

    /**
     * 
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        String v = "";
        if (this.value != null) {
            v = StringEscapeUtils.escapeXml(this.value.toString());
        }
        String field = null;
        Class type = String.class;
        if (null != this.attribute) {
            type = this.attribute.getType();
        }

        if (null != this.attribute && !this.attribute.isChangeable()) {
            if ("seqString".equals(this.attribute.getName())) {
                field = this.getImmutableSequence(v);
            } else {
                field = this.getImmutable(v);
            }
        } else if (type == Boolean.class) {
            field = this.getBoolean(v);
        } else if (type == java.lang.Float.class) {
            // LATER add javascript validation
            field = this.getDefault(v);
        } else if (type == java.lang.Integer.class) {
            // LATER add javascript validation
            field = this.getDefault(v);
        } else if (type == java.lang.String.class) {
            field = this.getString(v);
        } else if (type == String[].class) {
            field = this.getStringArray();
        } else if (type == java.sql.Timestamp.class) {
            field = this.getDate((Timestamp) this.value);
        } else if (type == Calendar.class) {
            field = this.getDate((Calendar) this.value);
            throw new AssertionError("Use pimsForm:date");
        } else {
            System.out.println(type.getName());
            field = this.getStringArray();
        } /*else {
                                                                                                                                                                                                                                                          // other type, not yet handled, display without edit
                                                                                                                                                                                                                                                          log("attribute: " + getName() + " has unsupported type: " + type.getName());
                                                                                                                                                                                                                                                          field = type.getName();
                                                                                                                                                                                                                                                      }*/

        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print("\n" + field + "\n");
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    /**
     * @param value
     * @return html to represent it
     */
    private String getImmutableSequence(final String value) {
        if ("".equals(value)) {
            return "none";
        }
        return "<span style=\"display:block; text-align: left; font-family: monospace\" title=\"sequence cannot be edited\">"
            + Sequence.getFormatedSequence(value) + "</span>";
    }

    /**
     * Make a simple input field
     * 
     * @param value the value
     * @return html
     */
    private String getDefault(final String value) {
        return "<input type=\"text\" name=\"" + this.getName() + "\" id=\"" + this.getName() + "\" value=\""
            + value + "\" />" + "<span class=\"inputnoedit\">" + value + "</span>";
/*        
        if (this.getMaxLength() == Input.UNLIMITED) {
            return "<input type=\"text\" class=\"text\" name=\"" + this.getName() + "\" id=\""
                + this.getName() + "\" value=\"" + (value) + "\" />\n";
        }
        // else
        return "<input type=\"text\" class=\"text\" name=\"" + this.getName() + "\" id=\"" + this.getName()
            + "\"  MAXLENGTH=\"" + this.getMaxLength() + "\" value=\"" + (value) + "\" />";
*/
    }

    private String getImmutable(String value) {
        if ("".equals(value)) {
            value = "none";
        }
        return "<span title=\"cannot be edited\">" + StringEscapeUtils.escapeXml(value) + "</span>";
    }

    /**
     * Output a tag for a boolean field. Note that the possible values are true, false, and null.
     * 
     * @param v
     * @return
     */
    private String getBoolean(final String v) {
        String yes = "";
        String no = "";
        String notspec = "";
        if ((this.value == Boolean.TRUE) || "Yes".equals(this.value)) {
            yes = " selected ";
        } else if ((this.value == Boolean.FALSE) || "No".equals(this.value)) {
            no = " selected ";
        } else {
            notspec = " selected ";
        }
        String ret = "<select name=\"" + this.getName() + "\" id=\"" + this.getName() + "\">";
        if (!this.attribute.isRequired()) {
            ret +=
                "<option value=\"[not specified]\" label=\"[not specified]\" " + notspec
                    + ">[not specified]</option>";
        }
        ret +=
            "<option value=Yes label=Yes " + yes + ">Yes</option>" + "<option value=No label=No " + no
                + ">No</option>" + "</select>";
        ret += "<span class=\"selectnoedit\">" + v + "</span>";
        return ret;
    }

    /**
     * 
     */
    private String getString(final String value) {
        String ret = "";
        if (this.attribute != null
            && this.attribute.getConstraint() instanceof Constraint.EnumerationConstraint) {
            final Constraint.EnumerationConstraint constraint =
                (Constraint.EnumerationConstraint) this.attribute.getConstraint();
            // make a select list
            final Collection allowedValues = new java.util.ArrayList();
            if (!this.attribute.isRequired()) {
                allowedValues.add("");
            }
            allowedValues.addAll(constraint.allowedValues);
            ret = "<select name=\"" + this.getName() + "\" id=\"" + this.getName() + "\">";
            for (final Iterator iter = allowedValues.iterator(); iter.hasNext();) {
                final String allowedValue = (String) iter.next();
                String displayValue = allowedValue;
                if ("".equals(displayValue)) {
                    displayValue = MRUController.NONE;
                }
                String selected = "";
                if (allowedValue.equals(value)) {
                    selected = "selected";
                }
                ret +=
                    "<option value=\"" + allowedValue + "\" " + selected + ">"
                        + StringEscapeUtils.escapeXml(displayValue) + "</option>\n";
            }
            return ret + "</select>";
        }

        int length = 80;
        if (null != this.attribute) {
            length = this.attribute.getLength();
        }
        if (length <= 80) {
            return this.getDefault(value);
        }
        // make a text area
        String cl = "normal";
        int rows = length / 50;
        if (rows > 20) {
            rows = 20;
            cl = "big";
        }
        return "<textarea name=\"" + this.getName() + "\" id=\"" + this.getName() + "\" rows=\"" + rows
            + "\" class=\"" + cl + "\" onkeyup=\"lineWrapAll(10, 3, this);\">"
            + StringEscapeUtils.escapeXml(value) + "</textarea>";
    }

    /**
     * Write an input field for dates
     * 
     * LATER handle time as well as just date
     * 
     */
    @Deprecated
    // use Calendar
    private String getDate(final Timestamp timestamp) {
        throw new AssertionError("Use Calendar instead of Timestamp");
        /* String v;
        if (timestamp == null) {
            v = "";
        } else {
            final SimpleDateFormat sdf = AttributeToHTML.getDateFormat();
            v = sdf.format(timestamp);
        }
        return this.getDefault(v); */
    }

    private String getDate(final Calendar date) {
        String v;
        if (date == null) {
            v = "";
        } else {
            final SimpleDateFormat sdf = Utils.getDateFormat();
            v = sdf.format(date.getTime());
        }
        return this.getDefault(v);
    }

    /**
     * Make an input field for String multi-values
     * 
     * @param v
     * @return
     */
    private String getStringArray() {
        // e.g. address        
        String v = "";
        if (null != this.value) {
            List list = null;
            if (this.value instanceof String) {

                list = java.util.Arrays.asList(((String) this.value).replace("; ", ";").split(";"));
            } else if (this.value instanceof List) {
                list = (List) this.value;
            } else {
                assert this.value instanceof String[] : this.value.getClass();
                list = java.util.Arrays.asList((String[]) this.value);
            }
            for (final Iterator iter = list.iterator(); iter.hasNext();) {
                final String line = (String) iter.next();
                v = v + line + "\n";
            }
        }
        final String n = this.getName();
        return "<textarea name=\"" + n + "\" id=\"" + n + "\" rows=5" + " >" + StringEscapeUtils.escapeXml(v)
            + "</textarea>";
    }

    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute(final MetaAttribute attribute) {
        this.attribute = attribute;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        if (null != this.name) {
            return StringEscapeUtils.escapeXml(this.name);
        }
        return StringEscapeUtils.escapeXml(this.attribute.getName());
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the maxLength
     */
    public int getMaxLength() {
        if (this.attribute != null) {
            return this.attribute.getLength();
        }
        return Input.UNLIMITED;
    }

}
