/**
 * pims-web org.pimslims.presentation.servlet.utils.EscapeHTMLWrapper.java
 * 
 * @author Marc Savitsky
 * @date 26 Nov 2009
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2009 Marc Savitsky * *
 * 
 */
package org.pimslims.presentation.servlet.utils;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * ComplexDecorator
 * 
 */
public class EscapeHTMLWrapper implements DisplaytagColumnDecorator {

    /**
     * 
     */

    public Object decorate(final Object columnValue, final PageContext pageContext, final MediaTypeEnum media)
        throws DecoratorException {

        System.out.println("EscapeHTMLWrapper.decorate");
        if (columnValue == null) {
            return columnValue;
        } else {
            return EscapeHTMLWrapper.escapeXml((String) columnValue);
        }
    }

    /**
     * Safely escapes XML reserved characters from the input string.
     * 
     * @param s the input string.
     * @return the escaped string.
     */
    public static String escapeXml(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (final char c : s.toCharArray()) {
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&#034;");
                    break;
                case '\'':
                    sb.append("&#039;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
