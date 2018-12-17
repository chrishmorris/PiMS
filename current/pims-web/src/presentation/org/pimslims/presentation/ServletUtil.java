/*
 * Created on 22.07.2005 - Code Style - Code Templates
 */
package org.pimslims.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.metamodel.MetaAttribute;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.MetaClassImpl;
import org.pimslims.metamodel.MetaRole;
import org.pimslims.metamodel.MetamodelUtil;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.location.Location;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.ImageType;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.util.File;

/**
 * @author Petr Troshin
 */
public class ServletUtil {

    private static Map ClassesDef = null;

    /**
     * @return a representation of MetaInfo.xml
     */

    @Deprecated
    // fold all this information into data model
    public static synchronized Map getClassesDef() {

        return Collections.EMPTY_MAP;
    }

    public static HashMap<String, String> convert(final Map<String, String[]> prop) {
        final HashMap<String, String> wizFields = new HashMap<String, String>();
        for (final String key : prop.keySet()) {
            if (prop.get(key).length > 1) {
                System.out.println("WARNING-Property " + key
                    + " has more than one value! This is not supported. ");
                continue; // Ignore multi valued fields for now.
            }
            final String val = prop.get(key)[0];
            System.out.println("KEY " + key + " val:: " + val);
            wizFields.put(key, val);
        }
        return wizFields;
    }

    /**
     * @param metaClass representation of a type in the model
     * @return representation, as modified by MetaInfo.xml
     */

    @Deprecated
    // obsolete
    public static MetaClass getPIMSMetaClass(final MetaClass metaClass) {
        return metaClass;
    }

    /**
     * 
     */
    private ServletUtil() { /* empty */
    }

    /* only tested, not used
    @Deprecated
    public static List<TargetBeanForLists> makeTargetBeans(final Collection<Target> targets,
        final ProgressListener listener) throws java.io.IOException {
        final ArrayList targetBeans = new ArrayList();
        int count = 0;
        for (final java.util.Iterator i = targets.iterator(); i.hasNext();) {
            final ModelObject object = (ModelObject) i.next();
            final TargetBeanForLists target = new TargetBeanForLists((Target) object);
            targetBeans.add(target);
            count = count + 1;
            if (null != listener && count % 20 == 0) {
                listener.setProgress(count);
            }
        }
        Collections.sort(targetBeans);
        if (null != listener) {
            listener.setProgressHidden(true);
        }
        return targetBeans;
    } */

    /* is there a MetaClass for this attribute? */
    public static boolean isMetaClassForAttribute(final MetaClass parentMetaClass, final String attName) {

        return null != parentMetaClass.getAttribute(attName);

    }

    public static String getMetaClassNameForRole(final MetaClass parentMetaClass, final String roleName) {
        final MetaRole mrole = parentMetaClass.getMetaRole(roleName);
        return mrole.getOtherMetaClass().getMetaClassName();
    }

    public static MetaClass getMetaClassForRole(final MetaClass parentMetaClass, final String roleName) {
        final MetaRole mrole = parentMetaClass.getMetaRole(roleName);
        // System.out.println("metaName " + parentMetaClass.getMetaClassName());
        // System.out.println("roleName " + roleName);
        // System.out.println("OtherMetaClass: " +
        // mrole.getOtherMetaClass().getMetaClassName());
        // System.out.println("getOtherRole().getRoleName: " +
        // mrole.getOtherRole().getRoleName());
        // System.out.println("getOwnMetaClass: " +
        // mrole.getOwnMetaClass().getMetaClassName());
        return mrole.getOtherMetaClass();
    }

    public static boolean containsRequiredRoles(final MetaClass metaClass) {
        return ServletUtil.getMandatoryRoles(metaClass.getMetaRoles()).size() > 0 ? true : false;
    }

    // TODO public static String getMetaClassNameForRole(String
    // parentMetaClassName, String roleName)

    /**
     * Data example: org.pimslims.model.target.Target:molecule:naturalSource
     * 
     * @param parentMetaClasses = org.pimslims.model.target.Target
     * @param roleName[1] = molecule
     * @param roleName[2] = naturalSource
     * @return MetaClass org.pimslims.model.reference.Organism
     */
    public static MetaClass getMetaClassForRole(final MetaClass parentMetaClass, final String[] roleNames) {
        assert roleNames.length > 1;
        MetaClass result = ServletUtil.getMetaClassForRole(parentMetaClass, roleNames[1]);
        for (int i = 2; i < roleNames.length; i++) {
            result = ServletUtil.getMetaClassForRole(result, roleNames[i]);
        }
        return result;
    }

    /**
     * Data example: org.pimslims.model.target.Target:citations
     * 
     * @param parentMetaClasses = org.pimslims.model.target.Target
     * @param roleName = molecule
     * @param int setIndx = index of metaclass within the set
     * @return MetaClass[] org.pimslims.model.core.BookCitation,org.pimslims.model.core.JournalCitation etc..
     */
    public static MetaClass getMetaClassForAbstractRole(final MetaClass parentMetaClass,
        final String roleName, final int setIndx) {
        final MetaClass metaClass = ServletUtil.getMetaClassForRole(parentMetaClass, roleName);
        assert metaClass.isAbstract() : "Metaclass is not abstract has no subtypes! ";
        final Set subtypes = metaClass.getSubtypes();
        assert setIndx < subtypes.size() : "Wrong set index in a subclass set (greater than set size!)";
        assert setIndx >= 0 : "Wrong set index has been given";
        final MetaClass result = (MetaClass) (new ArrayList(subtypes)).get(setIndx);
        return result;
    }

    /**
     * Static method to obtain MetaClassName
     * 
     * @param fullhook - fullHook
     * @return String Metaclass name
     */
    public static String getMetaClassName(final String fullhook) {
        MetaClass metaClass = null;
        try {
            metaClass =
                MetaClassImpl.getMetaClass(Class.forName(fullhook.substring(0, fullhook.indexOf(":"))));
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return metaClass.getMetaClassName();
    }

    /**
     * Obtain name for a modelobject. If there is a name defined - use it, otherwise combine the last part of
     * a metaclass name with hook id.
     * 
     * @param rv - ReadableVersion
     * @param hook - String full modelobject hook
     * @return String modelObject name
     */
    public static String getName(final ReadableVersion rv, final String hook) {
        ModelObject mObj;
        mObj = rv.get(hook);
        assert null != mObj : "No such object: " + hook;
        String name = mObj.get_Name();
        if (name == null || name.equals("")) {
            name = ServletUtil.getMOName(hook) + hook.substring(hook.indexOf(":")); // gets
            // org.pimslims.model.target.Target:90,
            // Return
            // Target:90
        }
        return StringEscapeUtils.escapeXml(name);
    }

    public static MetaClass getMetaClass(final String metaClassName) {
        MetaClass metaClass = null;
        try {
            metaClass = MetaClassImpl.getMetaClass(Class.forName(metaClassName));
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return metaClass;
    }

    /**
     * @param mObjts a collection of model objects
     * @return a list of their hooks
     */
    public ArrayList getAllHooks(final ArrayList mObjts) {
        final ArrayList allHooks = new ArrayList();
        for (final Iterator iter = mObjts.iterator(); iter.hasNext();) {
            final ModelObject elem = (ModelObject) iter.next();
            allHooks.add(elem.get_Hook());
        }
        return allHooks;
    }

    public static String extractValue(final String pathinfo) {
        String hook = null;
        if (pathinfo != null) {
            hook = pathinfo.substring(1);
        }
        return hook;
    }

    /**
     * @param metaClass model type concerned
     * @return name to use to display objects of this type TODO rename in data model instead of here
     */
    public static String getDisplayName(final MetaClass metaClass) {
        if (ResearchObjective.class.getName().equals(metaClass.getName())) {
            return "Construct";
        } else if (RefSample.class.getName().equals(metaClass.getName())) {
            return "Recipe";
        } else if (LabNotebook.class.getName().equals(metaClass.getName())) {
            return "Lab Notebook";
        }
        return metaClass.getAlias();
    }

    public static List<String> getSelectedHooks(final Map parameterMap, final String className) {
        final List<String> selectedHooks = new LinkedList<String>();
        for (final Object key : parameterMap.keySet()) {
            if (key instanceof String) {
                final String stringKey = (String) key;
                if (stringKey.startsWith(className + ':')) {
                    final String hook = stringKey;
                    if (parameterMap.get(key) instanceof String[]) {
                        final String value = ((String[]) parameterMap.get(key))[0];
                        if (value.equalsIgnoreCase("on")) {
                            selectedHooks.add(hook);
                        }
                    }

                }

            }
        }
        return selectedHooks;
    }

    /**
     * This method will use Arrays.sort for sorting Map
     * 
     * @return outputList
     */
    public static List<AttributeToHTML> sortAttributeToHtml(final List<AttributeToHTML> list) {

        // Sort the entries with your own comparator for the values:
        Collections.sort(list, new Comparator() {
            public int compareTo(final Object lhs, final Object rhs) {
                final AttributeToHTML leAttribute = (AttributeToHTML) lhs;
                final AttributeToHTML reAttribute = (AttributeToHTML) rhs;
                final int leType = leAttribute.getHtmlType();
                final int reType = reAttribute.getHtmlType();
                int result;
                if (leType < reType) {
                    result = -1;
                } else if (leType == reType) {
                    result = 0;
                } else {
                    result = 1;
                }
                return result;
            }

            public int compare(final Object lhs, final Object rhs) {
                return this.compareTo(lhs, rhs);
            }
        });
        return list;
    }// End of sortMap

    public static ArrayList getSortedAttributes(final Map attributes) {
        return MetamodelUtil.getSortedAttributes(attributes);
    }

    public static ArrayList getMandatoryAttr(final ArrayList attributes) {
        return MetamodelUtil.getMandatoryAttr(attributes);
    }

    public static ArrayList getOptionalAttr(final ArrayList sortedAttributes) {
        final ArrayList optionalAttributes = new ArrayList();
        for (final Iterator iter = sortedAttributes.iterator(); iter.hasNext();) {
            final MetaAttribute attribute = (MetaAttribute) iter.next();
            if (!attribute.isRequired()) {
                optionalAttributes.add(attribute);
            }
        }
        return optionalAttributes;
    }

    @Deprecated
    // obsolete
    public static ArrayList joinRoles(final MetaClass metaClass) {
        final MetaClass pmeta = ServletUtil.getPIMSMetaClass(metaClass);
        final Map pmetaRoles = pmeta.getMetaRoles();
        return new ArrayList(pmetaRoles.values());
    }

    public static synchronized Object getModelObject(final String roleName, final ModelObject mObj) {
        final MetaRole metarole = mObj.get_MetaClass().getMetaRole(roleName);
        if (null == metarole) {
            return null;
        }
        final ArrayList ar = new ArrayList(metarole.get(mObj));
        if (ar.size() == 0) {
            return null;
        } else if (ar.size() == 1) {
            return ar.get(0);
        }
        return ar;
    }

    // roleName->ModelObject or roleName-> ArrayList
    // Only not null assotiation will be returned!
    public static synchronized HashMap getAssociatedObjects(final ModelObject mObj) {
        final HashMap rolemObj = new HashMap();
        final Map metaroles = mObj.get_MetaClass().getMetaRoles();
        for (final Iterator iter = metaroles.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            final String roleName = (String) elem.getKey();
            // MetaRole role = (MetaRole)elem.getValue();
            final Object o = ServletUtil.getModelObject(roleName, mObj);
            if (o != null) {
                rolemObj.put(roleName, o);
            }
        }
        return rolemObj;
    }

    /**
     * 
     * @param rolesNameObjects - roleName -> ModelObject or roleName -> List(of ModelObjects)
     * @return
     * @TODO - to be developed futher
     */
    public static String getHooksForAssotiatedObjects(final HashMap rolesNameObjects) {
        final TreeMap roleHook = new TreeMap();
        for (final Iterator iter = rolesNameObjects.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry elem = (Map.Entry) iter.next();
            // elem.getKey()
            final Object obj = elem.getValue();
            if (obj instanceof ModelObject) {
                final ModelObject mObj = (ModelObject) obj;
                roleHook.put(elem.getKey(), mObj.get_Hook());
            }

        }
        return "";
    }

    /**
     * Method to find all mandatory roles of a Metaclass
     * 
     * @param all metaRoles
     * @return mandatory roles names only (keys)
     */
    public static ArrayList getMandatoryRoles(final Map metaRoles) {
        return MetamodelUtil.getMandatoryRoles(metaRoles);
    }

    /**
     * Data example : org.pimslims.model.target.Target:2331
     * 
     * @param hook
     * @return
     */
    public static String getMOName(final String hook) {
        return hook.substring(hook.lastIndexOf(".") + 1, hook.indexOf(":"));
    }

    /**
     * @param amountAttributeName the name of a Float attribute
     * @return the name of the attribute that contains the display unit for this amount, or null if none can
     *         be found
     */
    public static String getDisplayUnitAttributeName(final String className, final String amountAttributeName) {
        if (org.pimslims.model.sample.SampleComponent.class.getName().equals(className)
            && amountAttributeName.equals(org.pimslims.model.sample.SampleComponent.PROP_CONCENTRATION)) {
            return org.pimslims.model.sample.SampleComponent.PROP_CONCDISPLAYUNIT;
        }
        if (Sample.PROP_CURRENTAMOUNT.equals(amountAttributeName)) {
            return Sample.PROP_AMOUNTDISPLAYUNIT;
        }
        if (Sample.PROP_INITIALAMOUNT.equals(amountAttributeName)) {
            return Sample.PROP_AMOUNTDISPLAYUNIT;
        }
        if (Sample.PROP_CONCENTRATION.equals(amountAttributeName)) {
            return Sample.PROP_CONCDISPLAYUNIT;
        }
        if (RefInputSample.class.getName().equals(className)) {
            return RefInputSample.PROP_DISPLAYUNIT;
        }
        if (RefOutputSample.class.getName().equals(className)) {
            return RefOutputSample.PROP_DISPLAYUNIT;
        }
        return amountAttributeName + "DisplayUnit"; // likely to be right
    }

    /**
     * @param amountAttributeName the name of a Float attribute
     * @return the name of the attribute that contains the storage unit for it, or null if none can be found
     */
    public static String getStorageUnitAttributeName(final String className, final String amountAttributeName) {
        if (Molecule.PROP_MOLECULARMASS.equals(amountAttributeName)) {
            return null;
        }
        if (Sample.PROP_CURRENTAMOUNT.equals(amountAttributeName)) {
            return Sample.PROP_AMOUNTUNIT;
        }
        if (Sample.PROP_INITIALAMOUNT.equals(amountAttributeName)) {
            return Sample.PROP_AMOUNTUNIT;
        }
        if (AbstractSample.PROP_IONICSTRENGTH.equals(amountAttributeName)
            || AbstractSample.PROP_PH.equals(amountAttributeName)) {
            return null; // just a float
        }
        if (RefInputSample.class.getName().equals(className)) {
            return RefInputSample.PROP_UNIT;
        }
        if (RefOutputSample.class.getName().equals(className)) {
            return RefOutputSample.PROP_UNIT;
        }
        if (HolderType.class.getName().equals(className)
            && HolderType.PROP_MAXVOLUME.equals(amountAttributeName)) {
            return null; // some mistake surely
        }
        if (ImageType.class.getName().equals(className)) {
            return null;
        }
        if (Primer.PROP_MELTINGTEMPERATURE.equals(amountAttributeName)
            || Primer.PROP_MELTINGTEMPERATUREGENE.equals(amountAttributeName)
            || Primer.PROP_MELTINGTEMPERATURESELLER.equals(amountAttributeName)) {
            return null;
        }
        if (Location.PROP_PRESSURE.equals(amountAttributeName)) {
            return null;
        }
        if (Location.PROP_TEMPERATURE.equals(amountAttributeName)) {
            return null;
        }
        return amountAttributeName + "Unit"; // likely to be right

    }

    public static boolean isEmpty(final Object o) {

        return MetamodelUtil.isEmpty(o);
    }

    /**
     * @param hooks an array of hooks, e.g. from a request parameter
     * @return a collection of the model objects they represent
     * @throws AccessException if the current user is not permitted to view some of these objects
     */
    public static Collection getModelObjectsByHooks(final ReadableVersion version, final String[] hooks)
        throws AccessException {
        if (null == hooks) {
            return Collections.EMPTY_LIST;
        }

        final Collection ret = new java.util.ArrayList(hooks.length);
        for (int i = 0; i < hooks.length; i++) {
            ret.add(version.get(hooks[i]));
        }
        return ret;
    }

    /**
     * @param metaRole
     * @return
     */
    public static MetaRole getPIMSMetaRole(final MetaRole metaRole) {
        final MetaClass pm = ServletUtil.getPIMSMetaClass(metaRole.getOwnMetaClass());
        return pm.getMetaRole(metaRole.getRoleName());
    }

    /**
     * Get PIMSMetaAttribute for MetaAttribute
     * 
     * @param metaAttribute
     * @return PIMSMetaAttribute if one has been defined
     */
    public static MetaAttribute getPIMSMetaAttribute(final MetaAttribute metaAttribute) {
        final MetaClass pm = ServletUtil.getPIMSMetaClass(metaAttribute.getMetaClass());
        return pm.getAttribute(metaAttribute.getName());
    }

    /**
     * This takes a Map metaAttribute.Name (key)-> MetaAttribute (value) and convert it to Map
     * metaAttribute.Name (key)-> PIMSMetaAttribute (value) if some has been defined otherwise it will stay
     * MetaAttribute
     * 
     * @param attributes TreeMap<String, MetaAttribute>
     * @return attributes TreeMap<String, MetaAttribute(PIMSMetaAttribute)>
     */
    public static TreeMap<String, MetaAttribute> toPIMSMetaAttributes(
        final Map<String, MetaAttribute> attributes) {
        final TreeMap<String, MetaAttribute> attr = new TreeMap<String, MetaAttribute>();
        for (final Iterator iter = attributes.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry<String, MetaAttribute> entry = (Map.Entry<String, MetaAttribute>) iter.next();
            //final String attrName = entry.getKey();
            final MetaAttribute attribute = entry.getValue();
            if (attribute != null) {
                final MetaAttribute pattr = ServletUtil.getPIMSMetaAttribute(attribute);
                // This cannot be only PIMSMetaAttribute
                // because for some classes there may be no such attributes defined.
                // This is more likely to be mix of both
                //System.out.println("toPIMSMetaAttributes for [" + pattr.getName() + ":" + pattr.toString()
                //    + "]");
                attr.put(pattr.getName(), pattr);
            }
        }
        return attr;
    }

    public static boolean validString(final String s) {
        if (s != null && s.length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Return file attached to ModelObject
     * 
     * Method expect to find one file only. Do no use this method for object with collection of files.
     */
    public static File getFile(final ModelObject obj) {
        if (obj == null) {
            return null;
        }
        final Collection<File> files = obj.get_Files();
        if (files == null || files.isEmpty()) {
            return null;
        }
        assert files.size() == 1 : "This method works correctly for objects with one file attached only!";

        return files.iterator().next();
    }

    /**
     * Return file attached to ModelObject
     */
    public static File getFile(final ModelObject obj, final String partOfTheFileName) {
        if (obj == null) {
            return null;
        }
        final Collection<File> files = obj.get_Files();
        if (files == null || files.isEmpty()) {
            return null;
        }
        for (final File f : files) {
            final String name = f.getName();
            if (name.toLowerCase().contains(partOfTheFileName.toLowerCase())) {
                return f;
            }
        }
        return null;
    }

    /**
     * Return file attached to ModelObject
     * 
     * public static File getFileByDescription(final ModelObject obj, final String exactName) { if (obj ==
     * null) { return null; } final Collection<File> files = obj.get_Files(); if (files == null ||
     * files.isEmpty()) { return null; } for (final File f : files) { final String name = f.getDescription();
     * if (name.equalsIgnoreCase(exactName)) { return f; } } return null; }
     */

    /**
     * Return file attached to ModelObject
     * 
     * @web function
     */
    public static File getFileByTitle(final ModelObject obj, final String exactName) {
        return ServletUtil.getFileByTitle(obj, exactName, false);
    }

    /**
     * Return file attached to ModelObject
     */
    public static File getFileByTitle(final ModelObject obj, final String nameToMatch,
        final boolean matchExactly) {
        if (obj == null) {
            return null;
        }
        final Collection<File> files = obj.get_Files();
        if (files == null || files.isEmpty()) {
            return null;
        }
        for (final File f : files) {
            final String name = f.getTitle();
            if (matchExactly) {
                if (name.equalsIgnoreCase(nameToMatch)) {
                    return f;
                }
            } else {
                if (name.toLowerCase().contains(nameToMatch.toLowerCase())) {
                    return f;
                }
            }
        }
        return null;
    }

    /**
     * Factory method for making shortBeans out of ModelObjects
     */
    public static ModelObjectShortBean getShortBean(final ModelObject mObj) {
        return new ModelObjectShortBean(mObj);
    }

}
