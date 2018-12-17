/**
 * 
 */
package org.pimslims.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * Start/end javascript PIMSOnLoad function.
 * 
 * @author Petr Troshin
 * 
 */
public class PIMSJS extends PIMSTag {
    static final long serialVersionUID = 1L;
    /**
     * Contents of header div.
     */
    private String header = null;

    /**
     * for the XHTML attribute "class=" for the header div.
     */
    private String headerClass = null;

    /**
     * log an error message.
     * @param message 
     */
    public void log(String message) {
        javax.servlet.ServletContext context = this.pageContext.getServletContext();
        context.log(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        String path = getPath();

        String boxId = getUniqueId();
        try {
            javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print("<div class=\"" + headerClass + "\" style=\"width:100%\" id=\"" + boxId + "head\">\r\n"
                + "    <div class=\"headObjName\">" + "    <img class=\"toggle\" alt=\"Toggle view\" src=\""
                + path + "/images/icons/minus.gif\" onclick=\"toggleView('" + id + "', '" + path
                + "')\" id=\"" + id + "img\" />\r\n" + header + "</div></div><div id=\"" + boxId
                + "body\" >\r\n");
            writer.print("<script type='text/javascript'>\n" + "if(!path || path=='') { path='" + path
                + "'; }\n" + "if(!thingsToHide) { thingsToHide=new Array(); }\n"
                + "thingsToHide[thingsToHide.length]='" + boxId + "';" + "</script>\n");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    private static int pimsJSId = 0;

    /**
     * @return a unique string suitable for use as an XHMTL element id
     */
    private static String getUniqueId() {
        return "pimsbox" + (pimsJSId++);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print("</div>");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return super.doEndTag();
    }

    /**
     * @return Returns the cssClass.
     */
    public String getHeaderClass() {
        return headerClass;
    }

    /**
     * @param cssClass The cssClass to set.
     */
    public void setHeaderClass(String cssClass) {
        this.headerClass = cssClass;
    }

    /**
     * @return Returns the header.
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header The header to set.
     */
    public void setHeader(String header) {
        this.header = header;
    }

}
