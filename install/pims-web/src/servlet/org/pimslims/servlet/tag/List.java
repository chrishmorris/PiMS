/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;

/**
 * Tag for generating drop down list from records of type metaclass where labels are the values of attributes
 * delimited by delimiter
 * 
 * @param version - ReadbleVersion
 * @param attributes - list of meta attributes names separated by commas (spaces should not be used)
 * @param metaClass - MetaClass - type of object to look for values
 * @param controls (optional) - this gets recorded as it is (without processing) in the top select statement
 *            before values this allow to specify the class, name, style, id, multiple and the number of rows
 *            displayed (!)if controls has not been defined only options will be generated (no start and end
 *            of select tag)
 * @param emptyValue - Boolean true or false, (optional) if true empty value will be generated in the list as
 *            default, value for this option is set to MetaClassName
 * @param delimiter String (optional)- value which is used to separate values of different attributes in the
 *            label
 * @author Peter Troshin Usage example: <pims:list version="${modelObject.version}"
 *         attributes="name,possibleValues" metaClass="${modelObject.metaClass}" controls="name='select'
 *         style='width: 300px;' id='test' size='10' multiple " delimiter=" " emptyValue="true"/>
 * 
 *         This generate a list which allows multiple selection out of
 *         org.pimslims.model.protocol.ParameterDefinition assuming that version is available and metaclass is
 *         org.pimslims.model.protocol.ParameterDefinition
 */
@Deprecated
// obsolete
public class List extends PIMSTag {

    private String delimiter = null;

    private String metaClassName = null;

    private String selected = null;

    private String attributes = null;

    private boolean emptyValue = false;

    private ReadableVersion version = null;

    private String controls = null;

    private Collection<ModelObject> objectList = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        String select = "";
        String selectEnd = "";

        if (this.controls != null) {
            select = "<select " + this.controls + " >";
            selectEnd = "</select>";
        }

        MetaClass metaClass = null;
        if (this.objectList == null && this.metaClassName != null) {
            metaClass = this.version.getModel().getMetaClass(this.metaClassName);
            this.objectList = this.version.getAll(metaClass.getJavaClass(), 0, 1000);
            if (1000 <= this.objectList.size()) {
                throw new JspException("Too many records to list");
            }
        } else {
            if (this.objectList == null || this.objectList.size() == 0) {
                // If object list was passed but it was empty
                try {
                    final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();

                    // PiMS 1551 </select>
                    // there may be more than 1 list - don't include the </select> tag
                    //writer.print(select + "<option value=\"\" >none recorded</option></select>");
                    writer.print(select + "<option value=\"\" >none recorded</option>" + selectEnd);
                } catch (final IOException e) {
                    throw new JspException(e);
                }
                this.objectList = null; // nullify the object list so that the next
                // call does not use them
                return Tag.SKIP_BODY;
            }
            metaClass = this.objectList.iterator().next().get_MetaClass();
        }

        if (this.emptyValue) {
            select += "<option value=\"\" selected ></option>";
        }

        String val = "";
        this.delimiter = (this.delimiter == null) ? "" : this.delimiter;
        final TreeMap<String, String> sortedValues = new TreeMap<String, String>();
        for (final ModelObject mobj : this.objectList) {
            val = "";
            for (final String attributeName : this.parseAttributes()) {
                final Object value = mobj.get_Value(attributeName);
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
            sortedValues.put(val, mobj.get_Hook());
        }

        // boolean sel = false;

        for (String fval : sortedValues.keySet()) {
            final String hook = sortedValues.get(fval);
            // delete delimeter from the end of the label
            if (fval.length() > 0 && this.delimiter.length() > 0) {
                fval = fval.substring(0, fval.length() - this.delimiter.length());
            }

            fval = StringEscapeUtils.escapeXml(fval);
            if (this.selected != null && hook.equals(this.selected)) {
                select += "<option value=\"" + hook + "\" selected>" + fval + "</option>" + "\n";
                // sel=true;
            } else {
                select += "<option value=\"" + hook + "\">" + fval + "</option>" + "\n";
            }
        }

        // if(! sel) System.out.println("hook to select " + selected);

        select += selectEnd;

        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(select);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        this.objectList = null; // nullify the object list so that the next call
        // does not use them
        return Tag.SKIP_BODY;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    public void setMetaClassName(final String metaClassName) {
        this.metaClassName = metaClassName;
    }

    public void setAttributes(final String attributes) {
        this.attributes = attributes;
    }

    public void setSelected(final String selected) {
        this.selected = selected;
    }

    public void setVersion(final ReadableVersion version) {
        this.version = version;
    }

    public void setControls(final String controls) {
        this.controls = controls;
    }

    public void setObjectList(final Collection<ModelObject> objectList) {
        this.objectList = objectList;
    }

    public void setEmptyValue(final Boolean emptyValue) {
        this.emptyValue = (emptyValue != null) ? emptyValue.booleanValue() : false;
    }

    private String[] parseAttributes() {
        assert this.attributes != null : "Please specify the name of attributes for List tag";
        return this.attributes.split(",");
    }

    /*
     private String getOnMouseOver() { 
                    <option value="${sample.hook}"
                          onmouseover="experimentGroup='${sample}';

                                       thisSample={name:'<c:out value="${sample.name}"  />',
                                                   details:'<c:out value="${sample.details}" />',
                                                   componentsnumber:'${sample.sampleComponentsLength}',
                                                   components:'<c:out value="${sample.sampleComponentsString}" />',
                                                   hazardsnumber:'${sample.hazardPhasesLength}',
                                                   hazards:'<c:out value="${sample.hazardPhasesString}" />',
                                                   column:'${sample.colPosition}',
                                                   row:'${sample.rowPosition}',
                                                   subposition:'${sample.subPosition}',
                                                   amount:'${sample.currentAmount}',
                                                   units:'${sample.amountDisplayUnit}',
                                                   specification:'${sample.refSample}',
                                                   holder:'<c:out value="${sample.holderName}" />'};

                                       showSampleToolTip(event, thisSample);"

                          onmouseout="hideSampleToolTip();">
     }
     */

}
