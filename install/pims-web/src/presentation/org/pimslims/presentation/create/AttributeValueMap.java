package org.pimslims.presentation.create;

/*
 * This class was added as part of PIMS-256 to handle those attribute values that do not belong to roles
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.metamodel.MetaClass;
import org.pimslims.presentation.AttributeToHTML;
import org.pimslims.presentation.ServletUtil;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         Used by Create servlet to process parameters, e.g. from ChooseForCreate
 */
public class AttributeValueMap extends HashMap {

    public AttributeValueMap(final MetaClass metaClass, final Map params) {

        for (final Iterator it = params.entrySet().iterator(); it.hasNext();) {
            final Map.Entry o = (Map.Entry) it.next();
            final String key = (String) o.getKey();
            if (key.startsWith("_")) {
                System.out.println("Special create parameter: " + key);
                continue;
            }
            final String[] value = (String[]) o.getValue();
            this.put(metaClass, key, value);
        }
    }

    public AttributeValueMap(final String metaClassName, final String parameters) {

        // System.out.println(this.getClass().getName()+"
        // ["+metaClassName+","+parameters+"]");
        if (parameters.length() < 1) {
            return;
        }

        final String[] unphook = parameters.split("&");

        for (int i = 0; i < unphook.length; i++) {
            final String roleId = unphook[i];
            final int eqs = roleId.indexOf("=");
            final String name = roleId.substring(0, eqs);
            final String[] values = { roleId.substring(eqs + 1) };

            this.put(ServletUtil.getMetaClass(metaClassName), name, values);
        }
    }

    private void put(final MetaClass metaClass, final String key, final String[] values) {

        if (!RoleHooksHolder.roleExists(metaClass, key)) {
            if (ServletUtil.isMetaClassForAttribute(metaClass, key)) {
                final String parmName =
                    metaClass.getJavaClass().getName() + AttributeToHTML.CLASS_ATTRIBUTE_SEPARATOR + key;
                // System.out.println(this.getClass().getName()+" put
                // ["+parmName+","+values[0]+"]");
                this.put(parmName, values);

            } else {
                throw new IllegalArgumentException("Unknown element: " + key + " for class: "
                    + metaClass.getMetaClassName());
            }
        }
    }

    // public String toEncodedParamString() {
    // return toEncodedParamString(this);
    // }

    public String toDecodedParamString() {
        try {
            return AttributeValueMap.toDecodedParamString(this);
        } catch (final UnsupportedEncodingException e) {
            // should not happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve parameter values from previous request Construct the name of the parameter like:
     * parameterName=parameterValue
     * 
     * @param parms Map - come from servlet with possible values
     * 
     * 
     *            public String toEncodedParamString(final RoleHooksHolder hooks) { try { if (null != hooks &&
     *            ServletUtil.validString(hooks.toDecodedParamString()) && !this.isEmpty()) {
     * 
     *            return java.net.URLEncoder.encode(AttributeToHTML.CLASS_PARAMETER_SEPARATOR +
     *            this.toDecodedParamString(), "UTF-8"); } return
     *            java.net.URLEncoder.encode(this.toDecodedParamString(), "UTF-8"); } catch (final
     *            UnsupportedEncodingException e) { // should not happen throw new RuntimeException(e); } }
     * @throws UnsupportedEncodingException
     */

    private static String toDecodedParamString(final Map parms) throws UnsupportedEncodingException {

        final StringBuffer sb = new StringBuffer();
        if (parms != null) {
            for (final Iterator it = parms.entrySet().iterator(); it.hasNext();) {
                final Map.Entry o = (Map.Entry) it.next();
                final String key = (String) o.getKey();
                final String name = key.substring(key.indexOf(AttributeToHTML.CLASS_ATTRIBUTE_SEPARATOR) + 1);
                final String[] values = (String[]) parms.get(key);
                final String value = URLEncoder.encode(values[0], "UTF-8");
                sb.append(name + "=" + value + '&');
            }

        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

}
