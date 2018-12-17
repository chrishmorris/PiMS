/**
 * pims-dm org.pimslims.model.role AbstractComponentTest.java
 * 
 * @author cm65
 * @date 3 Jul 2009
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.role;

import java.util.Collection;
import java.util.Map;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.test.AbstractTestCase;

/**
 * AbstractComponentTest
 * 
 */
public class AbstractComponentTest extends AbstractTestCase {

    public void testFindAll() {
        wv = getWV();
        try {

            final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();
            // molComponentAttrMap.put(AbstractComponent.PROP_CATEGORIES, componentCategoryList);}

            wv.findAll(Molecule.class, molComponentAttrMap);
        } finally {
            wv.abort(); // not testing persistence
        }

    }

    public void testFindByCategory() throws ConstraintException {
        wv = getWV();
        try {

            // hash for ComponentCategory attributes
            final java.util.Set<ComponentCategory> componentCategoryList =
                new java.util.HashSet<ComponentCategory>();
            componentCategoryList.add(new ComponentCategory(wv, UNIQUE));
            final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();
            molComponentAttrMap.put(Substance.PROP_CATEGORIES, componentCategoryList);

            wv.findAll(Molecule.class, molComponentAttrMap);
        } finally {
            wv.abort(); // not testing persistence
        }
    }

    public void testAssociate() throws ConstraintException {
        wv = getWV();
        try {
            ComponentCategory category = new ComponentCategory(wv, UNIQUE);
            Substance molecule = new Molecule(wv, "DNA", UNIQUE);
            molecule.addCategory(category);
            wv.flush();

        } finally {
            wv.abort(); // not testing persistence
        }
    }

    public void testCreateExtension() throws AccessException, ConstraintException {
        this.wv = this.getWV();
        try {
            final ComponentCategory category = new ComponentCategory(this.wv, "cc" + UNIQUE);
            create(this.wv, UNIQUE, UNIQUE, category.getName());
        } finally {
            this.wv.abort();
        }
    }

    static void create(final WritableVersion wv, final String extensionName, final String extensionSeq,
        final String extensionCat) throws AccessException, ConstraintException {

        //**** CREATE MOLCOMPONENT ****
        //Attribute map for Molecule passed to the map constructor
        final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();

        molComponentAttrMap.put(Substance.PROP_NAME, extensionName);

        //**** sequence of the Extension Molecule.PROP_SEQSTRING
        molComponentAttrMap.put(Molecule.PROP_SEQUENCE, extensionSeq);

        // **** moltype is DNA Molecule.PROP_MOLTYPE
        molComponentAttrMap.put(Molecule.PROP_MOLTYPE, "DNA");

        // **** ComponentCategory of the Extension, also SeqDetails
        molComponentAttrMap.put(Molecule.PROP_SEQUENCE_DETAILS, extensionCat);

        // Attribute map for ComponentCategory passed to the map constructor
        final Map<String, Object> componentCategoryAttrMap = new java.util.HashMap<String, Object>();

        // hash for ComponentCategory attributes
        final java.util.Set<ComponentCategory> componentCategoryList =
            new java.util.HashSet<ComponentCategory>();

        componentCategoryAttrMap.put(ComponentCategory.PROP_NAME, extensionCat);
        // code to see if the category is in the database
        // retrieves component category with name  matching value in componentCategoryAttrMap
        final java.util.Collection<ComponentCategory> componentCategoriesFound =
            wv.findAll(ComponentCategory.class, componentCategoryAttrMap);

        if (componentCategoriesFound.isEmpty()) {
            throw new IllegalStateException(">>> Didn't find the ComponentCategory: [" + extensionCat + "]");
        }
        if (1 == componentCategoriesFound.size()) {
            // Add the component Category to the list
            componentCategoryList.add(componentCategoriesFound.iterator().next());
        }
        molComponentAttrMap.put(Substance.PROP_CATEGORIES, componentCategoryList);

        //****Now need to either create or update existing Extensions
        // identical records will be ignored
        assert (wv.findAll(Molecule.class, molComponentAttrMap).size() == 0);

        if (wv.findAll(Molecule.class, Substance.PROP_NAME, extensionName).size() == 0) {
            wv.create(Molecule.class, molComponentAttrMap);
        }
        // update Extension which has the same name
        else {
            final Collection<Molecule> extensionsByName =
                wv.findAll(Molecule.class, Substance.PROP_NAME, extensionName);
            final Collection<Molecule> extensionsExact = wv.findAll(Molecule.class, molComponentAttrMap);
            if (extensionsByName.size() == 1 && extensionsExact.size() == 0) {
                for (final Molecule extension : extensionsByName) {
                    extension.setSequence((String) molComponentAttrMap.get(Molecule.PROP_SEQUENCE));
                    extension.setMolType((String) molComponentAttrMap.get(Molecule.PROP_MOLTYPE));
                    extension.setSequenceDetails((String) molComponentAttrMap
                        .get(Molecule.PROP_SEQUENCE_DETAILS));
                    extension.setCategories((Collection<ComponentCategory>) molComponentAttrMap
                        .get(Substance.PROP_CATEGORIES));
                }
            } else {
                System.err.println("Found more than 1 Extension with the same name: " + extensionName);
                System.out.println("Found more than 1 Extension with the same name: " + extensionName);

            }
        }
    }

}
