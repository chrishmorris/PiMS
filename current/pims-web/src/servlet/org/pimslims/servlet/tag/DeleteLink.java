/**
 * 
 */
package org.pimslims.servlet.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.pimslims.metamodel.ModelObject;
import org.pimslims.servlet.PIMSServlet;

/**
 * @author Ed Daniel
 * 
 */
@Deprecated
// no longer used
public class DeleteLink extends PIMSTag {

    private ModelObject modelObject = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        final String alias = this.modelObject.get_MetaClass().getAlias();
        final String hook = this.modelObject.get_Hook();

        final String path = this.getPath();
        String link = "";
        if (this.modelObject.get_MayDelete() && true) {
            link =
                "<form action=\"" + path + "/Delete?" + hook + "\" id=\"delete_" + hook
                    + "\" method=\"post\" style=\"display:none\"></form>";
            link +=
                "<a class=\"headObjName\" style=\"color: black;\" href=\"javascript:if (confirm('Are you sure you want to delete this "
                    + alias + "?')) { document.forms['delete_" + hook + "'].submit(); }\">Delete</a> /";
        }
        try {
            final javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print(link);
        } catch (final IOException e) {
            throw new JspException(e);
        }
        return Tag.SKIP_BODY;
    }

    public void setModelObject(final ModelObject mobj) {
        this.modelObject = mobj;
    }

}
