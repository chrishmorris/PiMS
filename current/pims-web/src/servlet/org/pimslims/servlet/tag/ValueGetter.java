/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;

/**
 * Tag for getting the value(s) recorded in the model object labels are the values of attributes delimited by
 * delimiter
 * 
 * @param version - ReadbleVersion
 * @param attributes - list of meta attributes names separated by commas (spaces should not be used)
 * @param hook - hook to the model object
 * @param delimiter String (optional)- value which is used to separate values of different attributes in the
 *            label
 * @author Peter Troshin
 * 
 * Usage example: <pims:list version="${modelObject.version}" attributes="name" delimiter=" "
 * hook="org.pimslims.model.location.Location:23234"/>
 * 
 * This generate a list which allows multiple selection out of org.pimslims.model.protocol.ParameterDefinition
 * assuming that version is available and metaclass is org.pimslims.model.protocol.ParameterDefinition
 */
public class ValueGetter extends PIMSTag {

    private String delimiter = null;

    private String hook = null;

    private String attributes = null;

    private ReadableVersion version = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        if (!Util.isHookValid(this.hook)) {
            this.log("ValueGetter: The hook " + this.hook + " seems to be invalid. Attributes: "
                + this.attributes);
            return Tag.SKIP_BODY;
        }
        final ModelObject mObj = this.version.get(this.hook);
        if (mObj == null) {
            this.log("ValueGetter: Cannot obtain model object by hook: " + this.hook);
            return Tag.SKIP_BODY;
        }

        String val = "";
        this.delimiter = (this.delimiter == null) ? " " : this.delimiter;
        for (final String attributeName : this.parseAttributes()) {
            final Object value = mObj.get_Value(attributeName);
            if (value == null) {
                continue;
            }
            if (value instanceof String) {
                val += (String) value + this.delimiter;
            } else if (value instanceof Collection) {
                final Collection values = (Collection) value;
                for (final Object strValue : values) {
                    val += strValue + this.delimiter;
                }
            } else {
                // Do not know how to handle -> skip
                continue;
            }
        }
        // delete delimeter from the end of the label
        if (val.length() > 0 && this.delimiter.length() > 0) {
            val = val.substring(0, val.length() - this.delimiter.length());
        }

        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(val);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    public void setAttributes(final String attributes) {
        this.attributes = attributes;
    }

    public void setHook(final String hook) {
        this.hook = hook;
    }

    public void setVersion(final ReadableVersion version) {
        this.version = version;
    }

    private String[] parseAttributes() {
        assert this.attributes != null : "Please specify the name of attributes for List tag";
        return this.attributes.split(",");
    }
}
