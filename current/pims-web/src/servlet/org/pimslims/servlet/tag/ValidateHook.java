/**
 * 
 */
package org.pimslims.servlet.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.pimslims.lab.Util;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.dao.ReadableVersion;

/**
 * Conditional tag if the hook is valid PIMS hook than evaluate tag body, otherwise skip it
 * 
 * @param version - ReadbleVersion
 * @param hook - hook to the model object
 * @author Peter Troshin
 * 
 * Usage example: <pims:isHookValid version="${modelObject.version}"
 * hook="org.pimslims.model.location.Location:23234"> PERFORM SOME ACTIONS </pims:isHookValid>
 * 
 */
public class ValidateHook extends PIMSTag {

    private String hook = null;

    private ReadableVersion version = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {

        if (Util.isHookValid(this.hook)) {
            final ModelObject mObj = this.version.get(this.hook);
            if (mObj != null) {
                return Tag.EVAL_BODY_INCLUDE;
            }
        }

        this.log("ValueGetter: The hook " + this.hook + " seems to be invalid. ");
        return Tag.SKIP_BODY;
    }

    public void setHook(final String hook) {
        this.hook = hook;
    }

    public void setVersion(final ReadableVersion version) {
        this.version = version;
    }

}
