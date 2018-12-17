/*
 * Brick.java
 *
 * Created on 09 October 2007, 18:37
 */

package org.pimslims.tag;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author  Ian Berry
 * @version
 */

public class Brick extends TagSupport {
    static final long serialVersionUID = 1L;
    /**
     * Initialization of title property.
     */
    private java.lang.String title;
    
    /**
     * Initialization of headerClass property.
     */
    private java.lang.String headerClass;
    
    /**
     * Initialization of bodyClass property.
     */
    private java.lang.String bodyClass;
    
    private java.lang.String width;
    
    private java.lang.String height;
    
    private static int brickId = 0;
    
    /**
     * @return a unique string suitable for use as an XHMTL element id
     */
    private static String getUniqueId() {
        return "pimsbrick" + (brickId++);
    }
    
    /**
     * Setter for the title attribute.
     * @param value 
     */
    public void setTitle(String value) {
        this.title = value;
    }
    
    /**
     * Setter for the headerClass attribute.
     * @param value 
     */
    public void setHeaderClass(java.lang.String value) {
        this.headerClass = value;
    }
    
    /**
     * Setter for the bodyClass attribute.
     * @param value 
     */
    public void setBodyClass(java.lang.String value) {
        this.bodyClass = value;
    }
    
    public void setWidth(java.lang.String value) {
        this.width = value;
    }
    public void setHeight(java.lang.String value) {
        this.height = value;
    }
    
    @Override
    public int doAfterBody() throws JspException {
        int retValue;
        
        retValue = super.doAfterBody();
        return retValue;
    }
    
    @Override
    public int doEndTag() throws JspException {
        int retValue;
        try {
            javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.println("</div>");
            writer.println("</div>");
            writer.println("</td>");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new JspException(ex);
        }
        
        retValue = super.doEndTag();
        return retValue;
    }
    
    @Override
    public int doStartTag() throws JspException {
        try {
            BrickGrid gridTag = (BrickGrid)findAncestorWithClass(this, Class.forName("org.pimslims.tag.BrickGrid"));
            Integer columns = 100 / gridTag.getColumns();
            javax.servlet.jsp.JspWriter writer = this.pageContext.getOut();
            writer.println("<td style='width:" + columns + "%;' colspan='" + width + "' rowspan='" + height + "'>");
            String bodyHeight = "225px";
            if (height.equals("2")) {
                bodyHeight = "496px";
            } else if (height.equals("3")) {
                bodyHeight = "767px";
            }
            
            writer.println("<div style='padding-left:2px;padding-right:2px;padding-top:2px;padding-bottom:2px;'>");
            writer.println("<div class='" + headerClass + "'>" + title + "</div>");
            writer.println("<div class='" + bodyClass + "' style='height:" + bodyHeight + ";'>");
            
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new JspException(ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new JspException(ex);
        }
        
        super.doStartTag();
        return EVAL_BODY_INCLUDE;
    }
    
    @Override
    public String getId() {
        
        
        super.getId();
        return getUniqueId();
    }
    
    @Override
    public Tag getParent() {
        Tag retValue;
        
        retValue = super.getParent();
        return retValue;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getValues() {
        Enumeration retValue;
        
        retValue = super.getValues();
        return retValue;
    }
    
    @Override
    public void release() {
        super.release();
    }
    
    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
    }
    
    @Override
    public Object getValue(String k) {
        Object retValue;
        
        retValue = super.getValue(k);
        return retValue;
    }
    
    @Override
    public void removeValue(String k) {
        super.removeValue(k);
    }
    
    @Override
    public void setId(String id) {
        super.setId(id);
    }
    
    @Override
    public void setParent(Tag t) {
        super.setParent(t);
    }
    
    @Override
    public void setValue(String k, Object o) {
        super.setValue(k, o);
    }
}
