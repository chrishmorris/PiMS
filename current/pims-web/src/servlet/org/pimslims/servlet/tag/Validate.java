/**
 * 
 */
package org.pimslims.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * e.g.
 * 
 * @author Petr Troshin
 * 
 */
public class Validate extends TagSupport {

    String name = null;

    String alias = null;

    String methods = null;

    public void setName(final String name) {
        this.name = name;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public void setMethods(final String methods) {
        this.methods = methods;
    }

    /**
     * Validate.doTag
     * 
     * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
     */
    @Override
    public int doStartTag() throws JspException {
        final Form tag = (Form) TagSupport.findAncestorWithClass(this, Form.class);
        if (tag == null) {
            throw new JspException("Validate tag cannot be used outside the form!");
        }
        tag.elementsToValidate.add(new ValidatableElem(this.name, this.alias, this.methods));
        return Tag.SKIP_BODY;
    }

}
