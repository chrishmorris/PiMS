package org.pimslims.leeds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.pimslims.lab.Util;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         LeedsFormServletUtils
 * 
 */
@Deprecated
// Leeds code is no longer supported
public class LeedsFormServletUtils {

    private static final Set<String> IGNORED = new HashSet();
    static {
        LeedsFormServletUtils.IGNORED.add("save");
        LeedsFormServletUtils.IGNORED.add("type");
        LeedsFormServletUtils.IGNORED.add("_tab");
        LeedsFormServletUtils.IGNORED.add("expHook");
        LeedsFormServletUtils.IGNORED.add("to");
    }

    /**
     * @param request
     * @param resource
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    // COULD take request.getParameterMap() instead of request, to make this testable
    public static Object updateProperties(final Class beanClass, final HttpServletRequest request,
        final Map<String, String> resource) throws InstantiationException, IllegalAccessException,
        ClassNotFoundException, InvocationTargetException {

        final Object bean = Class.forName(beanClass.getName()).newInstance();
        final Method[] methods = bean.getClass().getMethods();
        System.out.println("params " + request.getParameterMap());
        System.out.println("bean " + beanClass.getName());
        final HashMap<String, Boolean> match = new HashMap<String, Boolean>();

        for (final String key : resource.keySet()) {
            match.put(key, new Boolean(false));
            //System.out.println("Updating property: " + key);
            for (final Method method : methods) {
                final String mname = "set" + StringUtils.capitalize(key);
                //System.out.println("Method_name_constructed " + mname);
                if (method.getName().equals(mname)) {
                    final String val = request.getParameter(resource.get(key));
                    //System.out.println("update method " + mname + " property " + key + " with value " + val);
                    if (val != null) {
                        //COULD use org.apache.commons.beanutils.BeanUtils.setProperty
                        method.invoke(bean, val);
                    }
                    match.put(key, new Boolean(true));
                    break;
                }
            }
        }
        // check all parameters were processed
        /*
        final Set parms = request.getParameterMap().keySet();
        final Set ok = new HashSet(resource.values());
        ok.addAll(LeedsFormServletUtils.IGNORED);
        for (final Iterator iterator = parms.iterator(); iterator.hasNext();) {
            final String parm = (String) iterator.next();
            if (!ok.contains(parm)) {
                throw new IllegalArgumentException("Unexpected parameter: " + parm);
            }
        }
        */
        // Check whether all properties has corresponding setters in the bean
        /*
          for (String key : match.keySet()) {
              // assert match.get(key) : "For the property "+ key + " the setter
              // has not been found in the bean " + beanClass.getName();
              if (match.get(key))
                  System.out.println("For the property " + key + " the setter has not been found in the bean "
                      + beanClass.getName());
          }
        */
        return bean;
    }

    public static Map<String, String> resourceToMap(final ResourceBundle rb) {
        final Map<String, String> resource = new HashMap<String, String>();
        final Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final String value = rb.getString(key);
            resource.put(key, value);
        }
        return resource;
    }

    /**
     * This method to check whether the hook is valid It assumes that the hook stracture is the following
     * org.pimslims.model.location.Location:2323 This would not work for packages less then 4 levels deep!
     */
    public static boolean isHookValid(final String hook) {
        return Util.isHookValid(hook);
    }

}
