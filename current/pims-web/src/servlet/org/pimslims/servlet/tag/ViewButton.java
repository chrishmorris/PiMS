/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.metamodel.ModelObject;

/**
 * @author Ed Daniel
 * 
 */
@Deprecated
// obsolete
public class ViewButton extends PIMSTag {

    private ModelObject modelObject = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        final String alias = this.modelObject.get_Name();
        final String hook = this.modelObject.get_Hook();
        final String objectAlias = this.modelObject.get_MetaClass().getAlias();

        final String path = this.getPath();
        final String button =
            "<a href=\"" + path + "/View/" + hook + "\" style=\"vertical-align:middle\" >"
                + "\n<img class=\"icon\" src=\"" + path + "/skins/default/images/icons/actions/view.gif\" "
                + "\ntitle=\"View all information on " + objectAlias + " "
                + StringEscapeUtils.escapeXml(alias) + "\" " + "\nalt=\"View all information on "
                + StringEscapeUtils.escapeXml(alias) + "\" /></a>";
        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(button);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    public void setModelObject(final ModelObject mobj) {
        this.modelObject = mobj;
    }

}
