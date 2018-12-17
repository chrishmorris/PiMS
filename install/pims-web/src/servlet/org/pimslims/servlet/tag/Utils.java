/**
 * 
 */
package org.pimslims.servlet.tag;


import javax.servlet.http.HttpServletRequest;

import org.pimslims.presentation.CsrfDefence;
import org.pimslims.presentation.ServletUtil;

/**
 * @author ejd53
 * 
 */
public class Utils {

    public static String replace(final String subject, final String target, final String replacement) {
        return subject.replaceAll(target, replacement);
    }

    public static String truncate(String str, final int maxLength) {
        if ("".equals(str)) {
            return "";
        }
        if (maxLength <= 3) {
            return "...";
        }
        if (str.length() > maxLength) {
            str = str.substring(0, maxLength - 3).concat("...");
        }
        return str;
    }

    /**
     * Utils.escapeJS
     * 
     * @param string
     * @return
     */
    public static String escapeJS(final String string) {
        final String ret = string.replace("\\", "\\\\").replace("'", "\\'").replace("\"", "&quot;");
        return ret;
    }

    /**
     * Utils.deCamelCase
     * 
     * @param string
     * @return
     */
    public static String deCamelCase(final String string) {
        return ServletUtil.deCamelCase(string);
    }

    /**
     * Utils.csrfToken
     * 
     * @param request
     * @param string
     * @return
     */
    public static String csrfToken(final HttpServletRequest request, final String string) {
        return CsrfDefence.getReusableToken(request, string);
    }
}
