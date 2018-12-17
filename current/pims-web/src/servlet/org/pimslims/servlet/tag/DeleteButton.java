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
// delete is on the menu made by pimsWidget:link
public class DeleteButton extends PIMSTag {

    private ModelObject modelObject = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag() Please reflect changes in this method to the
     *      method below !!!
     */
    @Override
    public int doStartTag() throws JspException {

        final String alias = this.modelObject.get_MetaClass().getAlias();
        final String hook = this.modelObject.get_Hook();
        final String mObjName = StringEscapeUtils.escapeHtml(this.modelObject.get_Name());
        final String path = this.getPath();
        String button = "";
        if (this.modelObject.get_MayDelete()) {
            button =
                "<form action=\"" + path + "/update/DeleteAndBackToReferer?" + hook
                    + "\" method=\"post\" class=\"singlebutton\">" + "<input src=\"" + path
                    + "/images/icons/deleteup.gif\" title=\"Delete this " + mObjName + " " + alias + "\""
                    + " alt=\"Delete this " + mObjName + " \""
                    + " onclick=\"return confirm('Are you sure you want to delete " + alias + ": " + mObjName
                    + " " + "?')\"" + " onmousedown=\"this.src='" + path + "/images/icons/deletedn.gif'\""
                    + " onmouseup=\"this.src='" + path + "/images/icons/deleteup.gif'\""
                    + " onmouseout=\"this.src='" + path + "/images/icons/deleteup.gif'\""
                    + " class=\"button\"" + " type=\"image\" />" + "</form>";
        } else {
            button =
                "<img class=\"button\" src=\"" + path + "/images/icons/deleteno.gif\""
                    + " alt=\"Can't delete this " + mObjName + " " + alias + "\" title=\"Can't delete this "
                    + mObjName + " " + alias + "\" />";

        }
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
