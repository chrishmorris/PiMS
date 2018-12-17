package org.pimslims.servlet.tag;

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

    protected String getPath() {
        return ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
    }

    /**
     * log an error message
     */
    public void log(final String message) {
        final javax.servlet.ServletContext context = this.pageContext.getServletContext();
        context.log(message);
    }

}
