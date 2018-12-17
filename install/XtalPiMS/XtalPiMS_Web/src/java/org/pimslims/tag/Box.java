/**
 * 
 */
package org.pimslims.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

/**
 * open/close box.
 * 
 * @author cm65
 * 
 */
public class Box extends PIMSTag {

    /**
     * 
     */
    private static final long serialVersionUID = -5942761481473971741L;
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
        String path = this.getPath();

        
        try {
            javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.print("<div class=\"" + headerClass + "\" style=\"width:100%\" id=\"" + id + "head\">\r\n"
                + "    <div class=\"headObjName\">" + "    <img class=\"toggle\" alt=\"Toggle view\" src=\""
                + path + "/images/icons/minus.gif\" onclick=\"toggleView('" + id + "', '" + path
                + "')\" id=\"" + id + "img\" />\r\n" + header + "</div></div><div id=\"" + id
                + "body\" >\r\n");
            writer.print("<script type='text/javascript'>\n" + "if(!path || path=='') { path='" + path
                + "'; }\n" + "if(!thingsToHide) { thingsToHide=new Array(); }\n"
                + "thingsToHide[thingsToHide.length]='" + id + "';" + "</script>\n");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
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
