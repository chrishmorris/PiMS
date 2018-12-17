/**
 * pims-web InputAmount.java
 * 
 * @author Marc Savitsky
 * @date 5 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * InputAmount
 * 
 */
@Deprecated
// replace this with a tag that also supports concentrations
public class InputAmount extends javax.servlet.jsp.tagext.TagSupport {

    private String name = null;

    private String prop = null;

    private Float amount = null;

    private String displayUnit = null;

    private final String noUnits = "[No Units]";

    /**
     * log an error message
     */
    public void log(final String message) {
        final javax.servlet.ServletContext context = this.pageContext.getServletContext();
        context.log(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        //System.out.println("org.pimslims.tag.InputAmount doStartTag [" + this.name + ":" + this.amount + ":"
        //    + this.displayUnit + "]");

        String value = "";
        if (this.name != null) {
            value = this.name.toString();
        }

        //final char unitType = this.getDisplayUnitChar(this.displayUnit);

        final StringBuffer sb = new StringBuffer();

        //sb.append("<td>");
        sb.append("<input class='amount' type='text' name='");
        sb.append(value);
        sb.append(":value' ");
        sb.append("value='" + this.getValue() + "' onChange='sampleOnChange(this, \"" + this.getProp()
            + "\")' />");

        sb.append("<select class='units' name='");
        sb.append(value);
        sb.append(":units' onChange='sampleOnChange(this, \"" + this.getProp() + "\")'>");

        sb.append("<option value='" + this.noUnits + "' selected='selected'>" + this.noUnits + "</option>");

        //if (unitType == 'g' || unitType == ']') { // no unit type is [No Units] 
        sb.append("<optgroup label='Mass'>");
        sb.append(this.option("kg", false) + "\n");
        sb.append(this.option("g", false) + "\n");
        sb.append(this.option("mg", false) + "\n");
        sb.append(this.option("ug", false) + "\n");
        sb.append(this.option("ng", false) + "\n");
        sb.append(this.option("pg", false) + "\n");
        sb.append("</optgroup>");
        //}

        //if (unitType == 'L' || unitType == ']') { // no unit type is [No Units]
        sb.append("<optgroup label='Volume'>");
        sb.append(this.option("L", false) + "\n");
        sb.append(this.option("mL", false) + "\n");
        sb.append(this.option("uL", false) + "\n");
        sb.append(this.option("nL", false) + "\n");
        sb.append("</optgroup>");
        //}

        //sb.append("<optgroup label='Concentration'>\n");
        //sb.append(option("M", true) + "\n");
        //sb.append(option("g/L", true) + "\n");
        //sb.append(option("mg/mL", true) + "\n");
        //sb.append("</optgroup>\n");

        sb.append("<optgroup label='Others'>");
        sb.append(this.option("IU", false) + "\n");
        sb.append(this.option("tube", false) + "\n");
        sb.append(this.option("pellet", false) + "\n");
        sb.append(this.option("colonies", false) + "\n");
        sb.append("</optgroup>");

        sb.append("</select>");
        // sb.append("</span>");

        sb.append("<input type='hidden' name='");
        //sb.append("<input name='");
        sb.append(value);
        sb.append(":" + this.getProp() + "' value='" + this.getValue() + this.getDisplayUnit() + "'/>");
        sb.append("\n");

        try {

            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(sb.toString());
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    public void setName(final String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public void setProp(final String s) {
        this.prop = s;
    }

    public String getProp() {
        return this.prop;
    }

    public void setValue(final String s) {
        if (!InputAmount.validString(s)) {
            return;
        }

        final char[] chars = s.toCharArray();

        int i;
        for (i = chars.length - 1; i >= 0; i--) {
            final char c = chars[i];
            if (c >= '0' && c <= '9') {
                break;
            }
        }

        this.amount = new Float(s.substring(0, i + 1));
        this.displayUnit = s.substring(i + 1);
    }

    public String getValue() {
        if (null == this.amount) {
            return "";
        }
        //System.out.println("InputAmount.getValue [" + this.amount + ":"
        //    + new DecimalFormat("#####0.######").format(this.amount) + "]");

        //System.out.println("InputAmount.getValue [" + this.amount + ":"
        //    + new BigDecimal(this.amount.toString()).toString() + "]");

        //System.out.println("InputAmount.getValue [" + this.amount + ":"
        //    + NumberFormat.getInstance().format(this.amount) + "]");

        // PiMS 1410 the Float.toString method returns scientific notation 
        //           ie 1.0E-5 instead of 0.00001 
        return new BigDecimal(this.amount.toString()).toString();
    }

    public void setDisplayUnit(final String s) {
        this.displayUnit = s;
    }

    public String getDisplayUnit() {
        if (!InputAmount.validString(this.displayUnit)) {
            return this.noUnits;
        }
        return this.displayUnit;
    }

    private String option(final String s, final boolean disabled) {
        if (s.equals(this.displayUnit)) {
            return "<option value='" + s + "' selected='selected'>" + s + "</option>";
        }
        if (disabled) {
            return "<option value='" + s + "' disabled='disabled'>" + s + "</option>";
        }
        return "<option value='" + s + "'>" + s + "</option>";
    }

/*
    private char getDisplayUnitChar(final String s) {

        if (!InputAmount.validString(this.displayUnit)) {
            return ']';
        }

        final char c = this.displayUnit.charAt(this.displayUnit.length() - 1);

        switch (c) {

            case 'L':
                return 'L';

            case 'g':
                return 'g';

            default:
                return ']';
        }
    } */

    private static boolean validString(final String s) {
        if (null == s || "".equals(s)) {
            return false;
        }
        return true;
    }
}
