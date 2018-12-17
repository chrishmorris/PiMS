/**
 * current-pims-web org.pimslims.data ExtensionsUtility.java
 * 
 * @author Susy Griffiths
 * @date 24-Apr-2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 susy *
 * 
 */
package org.pimslims.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.molecule.Extension;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ComponentCategory;

/**
 * ExtensionsUtility: Command line utility for adding RefData from .csv file for 5'-Primer extensions data in
 * the format: name,seqString,EncodedTag,RestrictionSite,category,Direction
 * 
 */
public class ExtensionsUtility extends AbstractLoader {
    /**
     * @param filename Name of file with a list of Extensions
     * @param wv org.pimslims.WritableVersion
     * @throws java.io.IOException IO exception
     * @throws AccessException org.pimslims.exception.AccessException
     * @throws ConstraintException org.pimslims.exception.ConstraintException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);

        final List<String> labels = Arrays.asList(lcsvp.getLabels());
        if (!labels.contains("name")) {
            throw new RuntimeException(
                "ExtensionsUtility can not handle data file which does not have a column for 'name'!");
        }

        while (lcsvp.getLine() != null) {
            final String extensionName = lcsvp.getValueByLabel("name");
            final String extensionSeq = lcsvp.getValueByLabel("seqString").toUpperCase().trim();
            final String extensionCat = lcsvp.getValueByLabel("category");
            //EXTRA Attributes for Extension class
            final String encTag = lcsvp.getValueByLabel("EncodedTag");
            final String restSite = lcsvp.getValueByLabel("RestrictionSite");
            final String dir = lcsvp.getValueByLabel("Direction");

            //**** name of the Extension AbstractComponent.PROP_NAME
            if ("".equals(extensionName.trim())) {
                // spacer line
                continue;
            }
            ExtensionsUtility.create(wv, extensionName, extensionSeq, extensionCat, dir, encTag, restSite);
        }
        //Find any user-created Extension Molecules and convert to Extensions
        final List<String> compCats = new java.util.ArrayList();
        compCats.add("Forward Extension");
        compCats.add("Reverse Extension");
        for (final String compCat : compCats) {
            final java.util.Collection<ComponentCategory> componentCategoryList =
                ExtensionsUtility.makeCompCatList(wv, compCat);
            ExtensionsUtility.molToExtension(wv, componentCategoryList);
        }
    }

    static Extension create(final WritableVersion wv, final String extensionName, final String extensionSeq,
        final String extensionCat, final String dir, final String encTag, final String restSite)
        throws AccessException, ConstraintException {

        //**** CREATE EXTENSION ****
        //Attribute maps for Molecule and Extension passed to the map constructor
        //final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();
        final Map<String, Object> extAttrMap =
            ExtensionsUtility
                .makeExtMap(wv, extensionName, extensionSeq, extensionCat, dir, encTag, restSite);

        // ****Need to delete Molecules which match the map from the file or Make a new map if they don't
        //Find a matching molecule delete and create Extension
        final Molecule molToDelete = wv.findFirst(Molecule.class, Substance.PROP_NAME, extensionName);
        final Extension matchingExtension =
            wv.findFirst(Extension.class, Substance.PROP_NAME, extensionName);
        if (null != molToDelete && null == matchingExtension) {
            System.out.println("Matching Molecule called " + extensionName + " in DB");
            System.out.println("Delete Molecule called " + molToDelete.get_Name());
            wv.delete(molToDelete);
            wv.flush();
        }

        //****Now need to either create or update existing Extensions
        // IGNORE identical records will be ignored
        if (wv.findAll(Extension.class, extAttrMap).size() == 1) {
            AbstractLoader.print("Extension: [" + extAttrMap.get("name") + "] is already in DB");
            return wv.findFirst(Extension.class, extAttrMap);
        }
        // CREATE NEW
        if (wv.findAll(Extension.class, Substance.PROP_NAME, extensionName).size() == 0) {
            AbstractLoader.print("+Adding Extension: [" + extAttrMap.get("name") + "]");
            // was Extension extension = wv.create(Extension.class, extAttrMap);
            final Extension extension = new Extension(wv, extensionName, dir);
            extension.set_Values(extAttrMap);
            return extension;
        }

        //UPDATE
        final Collection<Extension> extensionsByName =
            wv.findAll(Extension.class, Substance.PROP_NAME, extensionName);

        if (extensionsByName.size() != 1) {
            throw new IllegalStateException("Found more than 1 Extension with the same name: "
                + extensionName);
        }
        //If Extension with name found but not identical to attr Map, update
        Extension extension = null;
        if (wv.findAll(Extension.class, extAttrMap).size() == 0 && extensionsByName.size() == 1) {
            AbstractLoader.print("++Updating Extension: [" + extAttrMap.get("name") + "]");
            extension = ExtensionsUtility.updateExtension(extAttrMap, extensionsByName);
            //return extension;
        }
        return extension;
    }

    /**
     * ExtensionsUtility.updateExtension
     * 
     * @param extAttrMap
     * @param extensionsByName
     * @return
     * @throws ConstraintException
     */
    public static Extension updateExtension(final Map<String, Object> extAttrMap,
        final Collection<Extension> extensionsByName) throws ConstraintException {
        final Extension extension = extensionsByName.iterator().next();
        extension.setSeqString((String) extAttrMap.get(Molecule.PROP_SEQSTRING));
        extension.setMolType((String) extAttrMap.get(Molecule.PROP_MOLTYPE));
        extension.setSeqDetails((String) extAttrMap.get(Molecule.PROP_SEQDETAILS));
        extension.setCategories((Collection<ComponentCategory>) extAttrMap
            .get(Substance.PROP_CATEGORIES));
        extension.setExtensionType((String) extAttrMap.get(Extension.PROP_EXTENSIONTYPE));
        extension.setRelatedProteinTagSeq((String) extAttrMap.get(Extension.PROP_RELATEDPROTEINTAGSEQ));
        extension.setRestrictionEnzyme((String) extAttrMap.get(Extension.PROP_RESTRICTIONENZYME));
        return extension;
    }

    /**
     * ExtensionsUtility.makeCompCatList
     * 
     * @param wv
     * @param extensionCat
     * @return
     * @throws ConstraintException
     */
    static Collection<ComponentCategory> makeCompCatList(final WritableVersion wv, final String extensionCat)
        throws ConstraintException {
        // code to see if the category is in the database
        // retrieves component category with name  matching value in componentCategoryAttrMap
        final java.util.Collection<ComponentCategory> componentCategoriesFound =
            wv.findAll(ComponentCategory.class, ComponentCategory.PROP_NAME, extensionCat);
        if (componentCategoriesFound.size() != 1) {
            throw new IllegalStateException(">>> Didn't find the ComponentCategory: [" + extensionCat + "]");
        }
        return componentCategoriesFound;
    }

    /**
     * Method to replace Extension as molecule.Molecule with molecule.Extension Need to find all Molecule
     * Extensions, create a Map, delete Molecule then create an Extension ExtensionsUtility.molToExtension
     * 
     * @param wv
     * @param componentCategoryList
     */
    static void molToExtension(final WritableVersion wv,
        final java.util.Collection<ComponentCategory> componentCategoryList) throws AccessException,
        ConstraintException {
        final Map<String, Object> extAsMolAttrMap = new java.util.HashMap<String, Object>();
        extAsMolAttrMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
        final Collection<Molecule> molExts = wv.findAll(Molecule.class, extAsMolAttrMap);

        if (molExts.size() >= 1) {
            final int numMols = molExts.size();
            final Map molExtMap = new java.util.HashMap<String, Object>();
            System.out.println("DB contains " + numMols + " Extensions as Molecules");
            for (final Molecule molExt : molExts) {
                //only convert if there isn't an Extension with the same name
                final Collection<Extension> matchByName =
                    wv.findAll(Extension.class, Substance.PROP_NAME, molExt.get_Name());
                if (matchByName.size() == 0) {

                    ExtensionsUtility.doConvertMoleculeToExtension(wv, componentCategoryList, molExtMap,
                        molExt);
                }
            }
        }
    }

    private static void doConvertMoleculeToExtension(final WritableVersion wv,
        final java.util.Collection<ComponentCategory> componentCategoryList, final Map molExtMap,
        final Molecule molExt) throws AccessException, ConstraintException {
        System.out.println("The Molecule SeqDetails is " + molExt.getSeqDetails());
        final String[] fOrR = molExt.getSeqDetails().trim().split(" ");
        final String exType = fOrR[0].toLowerCase().trim();
        System.out.println("ExtensionType is " + exType);
        molExtMap.put(Extension.PROP_EXTENSIONTYPE, exType);
        molExtMap.put(Substance.PROP_NAME, molExt.get_Name());
        molExtMap.put(Molecule.PROP_MOLTYPE, "DNA");
        molExtMap.put(Molecule.PROP_SEQDETAILS, molExt.getSeqDetails());
        molExtMap.put(Molecule.PROP_SEQSTRING, molExt.getSeqString());
        molExtMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
        final Boolean isForUse = Boolean.valueOf("true");
        molExtMap.put(Extension.PROP_ISFORUSE, isForUse);
        if (null != molExt.getAccess()) {
            molExtMap.put(LabBookEntry.PROP_ACCESS, molExt.getAccess());
        }
        wv.delete(molExt);
        wv.flush();
        wv.create(Extension.class, molExtMap);
    }

    /**
     * Method to create a Map from the values in the file ExtensionsUtility.makeExtMap
     * 
     * @param wv
     * @param extensionName java.lang.String name of Extension
     * @param extensionSeq java.lang.String DNA sequence of Extension
     * @param extensionCat java.lang.String Component category name for teh Extension
     * @param dir java.lang.String Direction of the Extension forward or reverse
     * @param encTag java.lang.String protein Tag sequence encoded by or related to Extension
     * @param restSite java.lang.String Name of restriction enzyme encoded by the extension
     * @return extAttrMap java.util.HashMap
     * @throws ConstraintException
     */
    public static Map makeExtMap(final WritableVersion wv, final String extensionName,
        final String extensionSeq, final String extensionCat, final String dir, final String encTag,
        final String restSite) throws ConstraintException {
        final Map<String, Object> extAttrMap = new java.util.HashMap<String, Object>();
        extAttrMap.put(Substance.PROP_NAME, extensionName);
        //**** sequence of the Extension Molecule.PROP_SEQSTRING
        extAttrMap.put(Molecule.PROP_SEQSTRING, extensionSeq);
        // **** moltype is DNA Molecule.PROP_MOLTYPE
        extAttrMap.put(Molecule.PROP_MOLTYPE, "DNA");
        // **** ComponentCategory of the Extension, also SeqDetails
        extAttrMap.put(Molecule.PROP_SEQDETAILS, extensionCat);
        // **** Extension specific attributes
        // **** ExtensionType is required for Extension class
        extAttrMap.put(Extension.PROP_EXTENSIONTYPE, dir);
        // **** Related protein tag
        extAttrMap.put(Extension.PROP_RELATEDPROTEINTAGSEQ, encTag);
        // **** Restriction enzyme site
        extAttrMap.put(Extension.PROP_RESTRICTIONENZYME, restSite);
        // **** Is for use, set to true
        final Boolean isForUse = Boolean.valueOf("true");
        extAttrMap.put(Extension.PROP_ISFORUSE, isForUse);

        // Attribute map for ComponentCategory passed to the map constructor
        final java.util.Collection<ComponentCategory> componentCategoryList =
            ExtensionsUtility.makeCompCatList(wv, extensionCat);
        //molComponentAttrMap.put(AbstractComponent.PROP_CATEGORIES, componentCategoryList);
        extAttrMap.put(Substance.PROP_CATEGORIES, componentCategoryList);
        return extAttrMap;
    }

    /**
     * Method to convert Extensions Map to Molecule Map ExtensionsUtility.molMapFromExtMap
     * 
     * @param wv org.pimslims.dao.WritableVersion
     * @param extMap java.util.HashMap created from file
     * @return molMap java.util.HashMap created extMap
     */
    public static Map molMapFromExtMap(final WritableVersion wv, final java.util.Map extMap) {
        final Map<String, Object> molMap = extMap;
        extMap.remove(Extension.PROP_EXTENSIONTYPE);
        extMap.remove(Extension.PROP_ISFORUSE);
        extMap.remove(Extension.PROP_RELATEDPROTEINTAGSEQ);
        extMap.remove(Extension.PROP_RESTRICTIONENZYME);
        return molMap;
    }

    /**
     * @param args arguments
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ExtensionsUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                ExtensionsUtility.loadFile(wv, filename);
                wv.commit();
                System.out.println("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                System.out.println("Unable to read from file: " + filename);
                ex.printStackTrace();
            } catch (final ModelException ex) {
                AbstractLoader.print("Unable to add details from file: " + filename);
                ex.printStackTrace();
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        System.out.println("Extensions utility has finished");

    }

}
