package org.pimslims.servlet.tag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

public class Import extends PIMSTag {

    private Class clazz = null;

    /**
     * Import.setClassName
     * 
     * @param name
     */
    public void setClassName(final String name) {
        try {
            this.clazz = Class.forName(name);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("No such class: " + name);
        }
    }

    private static final int PUBLIC_STATIC_FINAL = Modifier.STATIC | Modifier.FINAL | Modifier.PUBLIC;

    @Override
    public int doStartTag() throws JspException {
        final Map<String, Object> constants = new HashMap<String, Object>();
        constants.put("class", this.clazz);

        final Field[] fields = this.clazz.getFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (Import.PUBLIC_STATIC_FINAL == (Import.PUBLIC_STATIC_FINAL & field.getModifiers())) {
                    constants.put(field.getName(), field.get(null));
                }
            }
        } catch (final IllegalArgumentException e) {
            // should not happen
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        final String simpleName = this.clazz.getSimpleName();
        this.pageContext.setAttribute(simpleName, constants);

        return Tag.SKIP_BODY;
    }

}
