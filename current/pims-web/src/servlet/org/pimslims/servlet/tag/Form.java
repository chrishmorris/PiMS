/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.pimslims.lab.Util;

/**
 * 
 * @author Petr Troshin
 * 
 */
@Deprecated
// obsolete
public class Form extends TagSupport {

    java.util.List<ValidatableElem> elementsToValidate;

    String name = null;

    String action = null;

    String method = null;

    String style = null;

    private String clas = null;

    public void setName(final String name) {
        this.name = name;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public void setStyle(final String style) {
        this.style = style;
    }

    public void setClas(final String clas) {
        this.clas = clas;
    }

    /**
     * Form.doTag
     * 
     * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
     */
    @Override
    public int doStartTag() throws JspException {

        final JspWriter writer = this.pageContext.getOut();
        this.elementsToValidate = Collections.synchronizedList(new ArrayList<ValidatableElem>());
        try {
            writer.print("<form id='" + this.name + "' " + "name='" + this.name + "' " + "action='"
                + this.action + "' " + (Util.isEmpty(this.method) ? "" : "' method='" + this.method + "' ")
                + (Util.isEmpty(this.style) ? "" : " style='" + this.style + "'")
                + (Util.isEmpty(this.clas) ? "" : " class='" + this.clas + "'")
                + " onsubmit='return validateFormFields(this)'" + " >");
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.EVAL_BODY_INCLUDE;
    }

    /**
     * Form.doEndTag
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        final JspWriter writer = this.pageContext.getOut();

        try {
            writer.print("\n</form>\n");

            if (!this.elementsToValidate.isEmpty()) {
                writer.print("<script type='text/javascript'><!--\n");
                for (final ValidatableElem elem : this.elementsToValidate) {
                    writer.print(elem.writeValidation());
                }
                writer.print("//--></script>\n");
            }

        } catch (final IOException e) {
            throw new JspException(e);
        }

        // dispose a list
        this.elementsToValidate = null;
        return Tag.EVAL_PAGE;
    }
}

class ValidatableElem {
    /**
     * Constructor for ValidatableElem
     * 
     * @param name2
     * @param alias2
     * @param methods2
     */
    public ValidatableElem(final String name, final String alias, final String methods) {
        this.name = name;
        this.alias = alias;
        this.methods = methods;
    }

    String name;

    String alias;

    String methods;

    public String writeValidation() {
        final StringBuffer validation = new StringBuffer("attachValidation(");
        validation.append(" '" + this.name + "' ");
        validation.append(",{");

        //required|wholeNumber|maximum:850 Will separate this in 3 different matches
        final String regex = "(\\w+(:*\\d+)?)|(\\w+(:*\\d+)?)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher m = pattern.matcher(this.methods);
        while (m.find()) {
            final String match = m.group();
            //System.out.println("M " + match);
            if (match.contains(":")) {
                // Validator attaches its own value
                validation.append(match + ",");
                continue;
            }

            validation.append(match + ":true,");
        }

        validation.append("alias:'" + this.alias + "'");
        validation.append("});\n");
        return validation.toString();
    }
}
