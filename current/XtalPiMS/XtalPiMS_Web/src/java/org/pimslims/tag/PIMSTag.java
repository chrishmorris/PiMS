package org.pimslims.tag;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Superclass for custom JSP tags.
 * 
 * @author: Chris Morris
 * @date: April 2006
 */
public abstract class PIMSTag extends javax.servlet.jsp.tagext.TagSupport {

    /**
     * 
     */
    protected PIMSTag() {
        super();
    }

    @Deprecated // use the even more convenient method below
    protected String getPath(ServletRequest request) {
        return ((HttpServletRequest) request).getContextPath();
    }

    protected String getPath() {
        return ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
    }
}
