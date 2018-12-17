/**
 * 
 */
package org.pimslims.servlet.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.presentation.CsrfDefence;
import org.pimslims.presentation.ModelObjectShortBean;

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
        return ModelObjectShortBean.escapeJS(string);
    }

    private static final Pattern LAST_WORD = Pattern.compile("(.+)([A-Z][^A-Z]+)$");

    /**
     * Utils.deCamelCase
     * 
     * @param string
     * @return
     */
    public static String deCamelCase(final String string) {
        String ret = "";
        String rest = string;
        while (true) {
            final Matcher m = Utils.LAST_WORD.matcher(rest);
            if (!m.matches()) {
                break;
            }
            rest = m.group(1);
            ret = " " + m.group(2).toLowerCase() + ret;
        }
        return rest.substring(0, 1).toUpperCase() + rest.substring(1) + ret;
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
