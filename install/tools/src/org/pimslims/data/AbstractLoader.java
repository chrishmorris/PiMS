/**
 * current-pims-web org.pimslims.command AbstractLoader.java
 * 
 * @author bl67
 * @date 4 Oct 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.reference.SampleCategory;

/**
 * AbstractLoader
 * 
 */
public class AbstractLoader {
    public static boolean silent = false;

    static void print(final Object object) {
        if (!AbstractLoader.silent) {
            System.out.println(object.toString());
        }
    }

    static void debug(final Object object) {
        // could print(object);
    }

    static SampleCategory getSampleCategory(final WritableVersion wv, final String sampleCatName) {
        SampleCategory sampleCategory;
        final Map<String, Object> sampleCatAttrMap = new java.util.HashMap<String, Object>();

        sampleCatAttrMap.put(SampleCategory.PROP_NAME, sampleCatName);

        final java.util.Collection sampleCategoriesFound = wv.findAll(SampleCategory.class, sampleCatAttrMap);
        assert 0 != sampleCategoriesFound.size() : "ERROR: SampleCategory [" + sampleCatName
            + "] must exist (loaded as ref data)";
        sampleCategory = (SampleCategory) sampleCategoriesFound.iterator().next();
        return sampleCategory;
    }

    /**
     * @param wv
     * @param keyAttrMap the attributes used to recorgnize the Object existed or not
     * @param oldNames the old names used to recorgnize the Object existed or not if keyAttrMap can not found
     *            one
     * @param fullAttrMap if this object is not existed, this fullAttrMap used to create it. Otherwise, this
     *            fullAttrMap used to update it (be careful with not changeable value)
     * @throws ConstraintException
     * @throws AccessException
     */
    static <T extends ModelObject> T UpdateOrCreate(final WritableVersion wv, final Class<T> targetClass,
        final Map<String, Object> keyAttrMap, final Collection<String> oldNames,
        final Map<String, Object> fullAttrMap) throws AccessException, ConstraintException {
        //use keyAttrMap to recorgnize the Object existed or not
        T targetObject = wv.findFirst(targetClass, keyAttrMap);
        //use oldNames+ keyAttrMap to recorgnize the Object existed or not if keyAttrMap can not find one
        if (targetObject == null && oldNames != null && oldNames.size() > 0) {
            final Map<String, Object> oldKeyAttrMap = new HashMap<String, Object>(keyAttrMap);
            for (final String oldName : oldNames) {
                oldKeyAttrMap.put("name", oldName);
                targetObject = wv.findFirst(targetClass, oldKeyAttrMap);
                if (targetObject != null) {
                    break;
                }
            }
        }
        //if found nothing, just create a new one
        if (targetObject == null) {
            //if this object is not existed, this fullAttrMap used to create it
            targetObject = wv.create(targetClass, fullAttrMap);
            //AbstractLoader.print("+Create new " + targetClass.getName() + " : [" + keyAttrMap + "] ");
        } else {
            // Otherwise, the fullAttrMap used to update it
            //TODO improve set_values of AbstractModelObject
            final org.pimslims.metamodel.MetaClass metaClass = targetObject.get_MetaClass();

            for (final String name : fullAttrMap.keySet()) {
                //update attributes
                if (null != metaClass.getAttribute(name)) {
                    Object value = fullAttrMap.get(name);
                    if (value instanceof String) {
                        if (value != null && ((String) value).trim().length() == 0) {
                            value = null;
                        }
                    }

                    if (targetObject.get_Value(name) != value) {
                        if ((targetObject.get_Value(name) == null && value != null)
                            || (targetObject.get_Value(name) != null && value == null)
                            || !targetObject.get_Value(name).toString().equals(value.toString())) {
                            if (targetObject.get_Value(name) != null
                                && targetObject.get_Value(name).toString().equals("") && value == null) {
                                /*ignored*/
                            }
                            //only update when attribute is not same
                            else {
                                AbstractLoader.print("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + value);
                                targetObject.set_Value(name, value);
                            }
                        }
                    }
                }
                //update roles
                else if (null != metaClass.getMetaRole(name)) {
                    final MetaRole role = metaClass.getMetaRole(name);
                    {
                        if (fullAttrMap.get(name) instanceof Collection) {
                            if (!role.get(targetObject).containsAll((Collection) fullAttrMap.get(name))
                                || role.get(targetObject).size() != ((Collection) fullAttrMap.get(name))
                                    .size()) {
                                //only update when role objects are not same
                                AbstractLoader.print("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + fullAttrMap.get(name));
                                targetObject.set_Role(name, (Collection) fullAttrMap.get(name));
                            }
                        } else if (fullAttrMap.get(name) instanceof ModelObject) {
                            if (!role.get(targetObject).contains(fullAttrMap.get(name))
                                || role.get(targetObject).size() != 1) {
                                //only update when role object is not same
                                AbstractLoader.print("^Update " + name + " of " + targetObject.get_Name()
                                    + " to " + ((ModelObject) fullAttrMap.get(name)).get_Name());
                                targetObject.set_Role(name, (ModelObject) fullAttrMap.get(name));
                            }
                        } else {
                            AbstractLoader.print(name + " is not a role or attribute for "
                                + targetObject.get_Name());
                        }

                    }
                }
            }
        }
        return targetObject;

    }
}
