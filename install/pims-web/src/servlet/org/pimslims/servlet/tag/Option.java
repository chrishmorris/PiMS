/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * Utility function for writing an option tag
 * 
 * @author Ed Daniel
 * 
 */
@Deprecated
//obsolete
public class Option extends PIMSTag {

    /**
     * Contents of header
     */
    private String value = "";

    private String label = "";

    private String selectedValue = "";

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            String isSelected = "";
            if (this.value.equals(this.selectedValue)) {
                isSelected = " selected=\"selected\"";
            }
            writer.print("<option value=\"".concat(this.value).concat("\" ").concat(isSelected).concat(">")
                .concat(this.label).concat("</option>"));
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.EVAL_BODY_INCLUDE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        return super.doEndTag();
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param header The value to set.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param header The label to set.
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return Returns the selected value.
     */
    public String getSelectedValue() {
        return this.selectedValue;
    }

    /**
     * @param header The selected value to set.
     */
    public void setSelectedValue(final String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
