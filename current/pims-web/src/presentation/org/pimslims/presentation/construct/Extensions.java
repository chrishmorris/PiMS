/**
 * current-pims-web org.pimslims.lab.primer Extensions.java
 * 
 * @author cm65
 * @date 17 Mar 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 cm65
 * 
 * 
 */
package org.pimslims.presentation.construct;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Utils;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Extension;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.search.Paging;
import org.pimslims.search.Paging.Order;

/**
 * Tags
 * 
 */
public class Extensions {

    //private static Properties properties = null;

    /**
     * @param rv org.pimslims.dao.WritableVersion
     * @param direction String for direction of extension -Forward or Reverse
     * @return Map java.util.Map
     */
    public static Map<String, String> getExtensions(final ReadableVersion rv, final String direction) {
        final String dir = direction;
        final Map<String, String> exMap = new java.util.LinkedHashMap<String, String>();

        final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();
        molComponentAttrMap.put(Molecule.PROP_MOLTYPE, "DNA");
        molComponentAttrMap.put(Molecule.PROP_SEQUENCE_DETAILS, dir);

        final Map<String, Object> compCatAttrMap = new java.util.HashMap<String, Object>();
        compCatAttrMap.put(ComponentCategory.PROP_NAME, dir);
        final java.util.Collection<ComponentCategory> compCats =
            rv.findAll(ComponentCategory.class, compCatAttrMap);
        //System.out.println("There are " + compCats.size() + " ComponentCategories");
        //for (final ComponentCategory compCat : compCats) {
        final java.util.Collection<Molecule> dnaMolComps = rv.findAll(Molecule.class, molComponentAttrMap);
        for (final Molecule mc : dnaMolComps) {
            exMap.put(mc.getName(), mc.getSequence());
            //System.out.println("Got one " +mc.getName()+ " " +mc.getSeqDetails());                 
        }
        //}
        return exMap;
    }

    /**
     * To create a new Extension as an Extension (was Molecule) from user-entered values
     * 
     * @param wv org.pimslims.dao.WritableVersion
     * @param extName String name of Extension
     * @param extSeq String sequence of Extension
     * @param direction String Forward or Reverse
     * @return message String message to pass to JSP
     */
    public static Extension makeNewExtension(final WritableVersion wv, final ModelObjectShortBean access,
        final String extName, final String extSeq, final String direction, final String encTag,
        final String restSite) throws ConstraintException, AccessException {
        //Attribute map for Molecule passed to the map constructor
        final Map<String, Object> molCompAttrMap = new java.util.HashMap<String, Object>();
        final Map<String, Object> shortAttrMap = new java.util.HashMap<String, Object>();
        final String name = extName;
        final String seq = extSeq;
        final String dir = direction.toLowerCase();
        final String fToUpper = direction.substring(0, 1).toUpperCase() + direction.substring(1);
        final String details = fToUpper + " Extension";
        final String relTag = encTag;
        final String rSite = restSite;

        LabNotebook accessObject = null;
        if (null != access) {
            accessObject = wv.get(access.getHook());
            molCompAttrMap.put(LabBookEntry.PROP_ACCESS, accessObject);
        }

        molCompAttrMap.put(Substance.PROP_NAME, name);
        shortAttrMap.put(Substance.PROP_NAME, name);
        molCompAttrMap.put(Molecule.PROP_MOLTYPE, "DNA");
        molCompAttrMap.put(Molecule.PROP_SEQUENCE, seq);
        molCompAttrMap.put(Molecule.PROP_SEQUENCE_DETAILS, details);
        //080709 for new class Extension
        molCompAttrMap.put(Extension.PROP_RELATEDPROTEINTAGSEQ, relTag);
        molCompAttrMap.put(Extension.PROP_RESTRICTIONENZYME, rSite);
        molCompAttrMap.put(Extension.PROP_EXTENSIONTYPE, dir); //forward or reverse -lower case

        //Attribute Map for the ComponentCategory
        final Map<String, Object> compCatAttrMap = new java.util.HashMap<String, Object>();
        // hash for ComponentCategory attributes
        final java.util.Set<ComponentCategory> componentCategoryList =
            new java.util.HashSet<ComponentCategory>();

        compCatAttrMap.put(ComponentCategory.PROP_NAME, details);
        // code to see if the category is in the database
        // retrieves component category with name & namingSystem matching value in componentCategoryAttrMap
        final java.util.Collection<ComponentCategory> componentCategoriesFound =
            wv.findAll(ComponentCategory.class, compCatAttrMap);

        if (componentCategoriesFound.isEmpty()) {
            throw new IllegalStateException(">>> Didn't find the ComponentCategory: [" + details + "]");
        } else if (1 == componentCategoriesFound.size()) {
            // Add the component Category to the list
            componentCategoryList.add(componentCategoriesFound.iterator().next());
        }
        molCompAttrMap.put(Substance.PROP_CATEGORIES, componentCategoryList);

        final Extension obj = wv.create(Extension.class, molCompAttrMap);
        return obj;
    }

    public static String save(final WritableVersion version, final ExtensionBean bean)
        throws ConstraintException, AccessException {
        String extHook = "";
        if (null != bean.getExName() && !"".equals(bean.getExName()) && null != bean.getExSeq()
            && !"".equals(bean.getExSeq())) {
            extHook =
                Extensions.makeNewExtension(version, bean.getAccess(), bean.getExName(), bean.getExSeq(),
                    bean.getDirection(), bean.getEncodedTag(), bean.getRestrictionSite()).get_Hook();
        } else {
            System.out.println("No hook");
        }
        return extHook;
    }

    /**
     * Creates a Collection of ExtensionBeans modified 210909 to populate extra attributes from Extension
     * 
     * @param rv org.pimslims.dao.ReadableVersion
     * @param direction String direction of extension
     * @return java.util.ArrayList of org.pimslims.lab.primer.ExtensionBean
     */
    public static Collection<ExtensionBean> makeExtensionBeans(final ReadableVersion rv,
        final String direction) {
        //        final ExtensionBean extb = new ExtensionBean();
        final List<ExtensionBean> extBeans = new java.util.ArrayList<ExtensionBean>();
        final Map<String, Object> extAttrMap = new java.util.HashMap<String, Object>();
        final String dir = direction + " Extension";
        extAttrMap.put(Molecule.PROP_SEQUENCE_DETAILS, dir);

        final Paging paging = new Paging(0, Integer.MAX_VALUE);
        paging.addOrderBy(Substance.PROP_NAME, Order.ASC);
        final java.util.Collection<Extension> extensions = rv.findAll(Extension.class, extAttrMap, paging);
        for (final Extension extension : extensions) {
            final ExtensionBean extb = new ExtensionBean(extension);
            extb.setDirection(extension.getSequenceDetails());
            extb.setExName(extension.get_Name());
            extb.setExSeq(extension.getSequence());
            String enctag = "";
            if (null != extension.getRelatedProteinTagSeq()
                && !"".equals(extension.getRelatedProteinTagSeq())) {
                enctag = extension.getRelatedProteinTagSeq();
            }
            extb.setEncodedTag(enctag);
            extb.setRestrictionSite(extension.getRestrictionEnzyme());
            extb.setExtensionHook(extension.get_Hook());
            extBeans.add(extb);
        }
        return extBeans;
    }

    /**
     * 
     * Extensions.duplicate
     * 
     * @param extension org.pimslims.model.molecule.Extension to be copied
     * @param rw org.pims.limsdao.WritableVersion
     * @return dupl org.pimslims.model.molecule.Extension copy
     * @throws ConstraintException
     * @throws AccessException
     */
    public static Extension duplicate(final Extension extension, final WritableVersion rw)
        throws ConstraintException, AccessException {
        Extension dupl = null;

        final Map<String, Object> attributes = new HashMap<String, Object>();
        // This name will be reset later
        attributes.put(Substance.PROP_NAME, "aa" + System.currentTimeMillis());
        attributes.put(Extension.PROP_EXTENSIONTYPE, extension.getExtensionType());
        attributes.put(Molecule.PROP_MOLTYPE, "DNA");
        attributes.put(LabBookEntry.PROP_ACCESS, rw.getFirstProject());
        dupl = new Extension(rw, attributes);

        // This name will be reset later
        //dupl = new Extension(rw, "aa" + System.currentTimeMillis(), extension.getExtensionType());
        assert dupl != null;
        final Map<String, Object> duplParam = Utils.deleteNullValues(extension.get_Values());
        // Extension name must be unique
        duplParam.put(Substance.PROP_NAME, extension.get_Name() + " copy");
        dupl.set_Values(duplParam);

        dupl.setCategories(extension.getCategories());

        return dupl;
    }

}
