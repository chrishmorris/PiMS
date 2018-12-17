/*
 * Created on 27-Feb-2006 2:45:20 @author Susy Griffiths, susy@ysbl.york.ac.uk PIMS project, www.pims-lims.org
 * Copyright (C) 2006 YSBL - University of York, Department of Chemistry Henlington, York, YO UK
 */
package org.pimslims.data;

import java.util.Map;
import java.util.regex.Pattern;

import org.pimslims.csv.CsvParser;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.model.reference.ComponentCategory;
import org.pimslims.model.reference.HazardPhrase;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.SampleComponent;

/**
 * 
 * Command line utility for loading Chemicals data from a CSV file for RefSampleComponent.MolComponent and
 * creating a associated RefSample per Molecule created
 * 
 * Data for MolComponent: Chemical,Synonyms,Category,FORMULA,CAS,Mass (Daltons),molType molType is 'other'
 * except for sugars = carbohydrate, enzymes = protein
 * 
 * Data for RefSample: Chemical,Is_HAZARD,CODE,Risk No.,Safety phrases,'Flammable,Toxic/IrritantCorrosive'
 * SampleCategory is Chemical/Biochemical for all plus detergent etc. where appropriate Hazard information
 * also needs separate code.
 */
//121207 remove call to replaceTm, see PIMS-605.  lines 73, 85, 266
public class ChemicalUtility extends AbstractLoader {

    /**
     * @param CSV filename with a list of Molcomponents
     * @throws java.io.IOException
     */
    public static void loadFile(final WritableVersion wv, final String filename) throws java.io.IOException,
        AccessException, ConstraintException {

        final java.io.Reader reader = new java.io.FileReader(filename);
        final CsvParser lcsvp = new CsvParser(reader);

        while (lcsvp.getLine() != null) {

            AbstractLoader.print("====================");
            // ********************************************************************************
            // *** 1/ Create Molecule ***
            // ********************************************************************************
            // Attribute map for Molecule passed to the map constructor
            final Map<String, Object> molComponentAttrMap = new java.util.HashMap<String, Object>();

            // >>> Molecule.PROP_NAME
            final String molComponentName = lcsvp.getValueByLabel("Chemical");
            if ("".equals(molComponentName.trim())) {
                // spacer line
                continue;
            }
            // call replaceTm to replace [R] in file with &reg; HTML code for
            // reg TM sign
            // LATER may also need to convert unregistered TM sign, using
            // &trade;
            //molComponentAttrMap.put(Molecule.PROP_NAME, replaceTm(molComponentName));
            molComponentAttrMap.put(Substance.PROP_NAME, molComponentName);

            // >>> Molecule.PROP_SYNONYMS
            // parse synonyms, list sparated by ";"
            // Also, [R] needs to be converted to 'reg TM' sign
            final java.util.List<String> synonymList = new java.util.ArrayList<String>();
            final String synonymsStr = lcsvp.getValueByLabel("Synonyms");
            // using regular expressions and split
            final String regex = ";";
            final String[] splitsyns = Pattern.compile(regex).split(synonymsStr);
            for (int i = 0; i < splitsyns.length; i++) {
                //String synonym = replaceTm(splitsyns[i]); // replace [R] with &reg;
                final String synonym = splitsyns[i];
                if (80 >= synonym.length() && synonym.length() > 0) {
                    synonymList.add(synonym);
                }
            }
            molComponentAttrMap.put(Substance.PROP_SYNONYMS, synonymList);

            // >>> Molecule.PROP_EMPIRICALFORMULA
            molComponentAttrMap.put(Molecule.PROP_EMPIRICALFORMULA, lcsvp.getValueByLabel("FORMULA").trim());

            // >>> Molecule.PROP_CASNUM
            String casNum = lcsvp.getValueByLabel("CAS").trim();
            if ("".equals(casNum)) {
                casNum = null;
            }
            molComponentAttrMap.put(Molecule.PROP_CASNUM, casNum);

            // >>> Molecule.PROP_MOLECULARMASS
            if (!"".equals(lcsvp.getValueByLabel("Mass (Daltons)").trim())) {
                molComponentAttrMap.put(Molecule.PROP_MOLECULARMASS,
                    Float.valueOf((lcsvp.getValueByLabel("Mass (Daltons)").trim())));
            }

            // >>> Molecule.PROP_MOLTYPE
            molComponentAttrMap.put(Molecule.PROP_MOLTYPE, lcsvp.getValueByLabel("molType").trim());

            // ********************************************************************************
            // *** ComponentCategories associated with the Molecule ***
            // ********************************************************************************
            // Get the list of ComponentCategories from db to associate to the
            // molComponent
            // Split Category on ',' create an array of strings called
            // componentCategoryList
            final String componentCategoriesStr = lcsvp.getValueByLabel("Category");
            final String regex2 = ",";
            final String[] componentCategoryStrArray = Pattern.compile(regex2).split(componentCategoriesStr);

            // Attribute map for ComponentCategory passed to the map constructor
            final Map<String, Object> componentCategoryAttrMap = new java.util.HashMap<String, Object>(); // hash
            // for  ComponentCategory  attributes
            final java.util.Set<ComponentCategory> componentCategoryList =
                new java.util.HashSet<ComponentCategory>(); // create
            // a list for the individual CompCats
            for (int i = 0; i < componentCategoryStrArray.length; i++) {
                if ("".equals(componentCategoryStrArray[i].trim())) {
                    continue;
                }
                componentCategoryAttrMap
                    .put(ComponentCategory.PROP_NAME, componentCategoryStrArray[i].trim());
                // code to see if the category is in the database
                // retrieves component category with name 
                // matching value in componentCategoryAttrMap
                final java.util.Collection<ComponentCategory> componentCategoriesFound =
                    wv.findAll(ComponentCategory.class, componentCategoryAttrMap);

                if (componentCategoriesFound.isEmpty()) {
                    AbstractLoader.print(">>> Didn't find the ComponentCategory: ["
                        + componentCategoryStrArray[i] + "]");
                } else if (1 == componentCategoriesFound.size()) {
                    // Add the component Category to the list
                    componentCategoryList.add(componentCategoriesFound.iterator().next());
                }
            }

            // Create Molecule if does not already exist.
            Molecule molComponent = null;
            final java.util.Collection molComponentList = wv.findAll(Molecule.class, molComponentAttrMap);
            if (molComponentList.isEmpty()) {
                final java.util.Collection<Molecule> molComponentsFound =
                    wv.findAll(Molecule.class, Substance.PROP_NAME,
                        molComponentAttrMap.get(Substance.PROP_NAME));
                // find by name only and update its details
                if (molComponentsFound.size() > 0) {
                    assert (molComponentsFound.size() == 1);
                    molComponent = molComponentsFound.iterator().next();
                    molComponent.setSynonyms((java.util.List) molComponentAttrMap
                        .get(Substance.PROP_SYNONYMS));
                    molComponent.setEmpiricalFormula((String) molComponentAttrMap
                        .get(Molecule.PROP_EMPIRICALFORMULA));
                    molComponent.setCasNum((String) molComponentAttrMap.get(Molecule.PROP_CASNUM));
                    molComponent.setMolType((String) molComponentAttrMap.get(Molecule.PROP_MOLTYPE));
                    if (molComponentAttrMap.containsKey(Molecule.PROP_MOLECULARMASS)) {
                        molComponent.setMolecularMass((Float) molComponentAttrMap
                            .get(Molecule.PROP_MOLECULARMASS));
                    }
                    AbstractLoader.print("MolComponent already exists: [" + molComponent.getName()
                        + "] - other attributes updated");

                } else {
                    // create new MolComponent
                    molComponent = new Molecule(wv, molComponentAttrMap);
                    AbstractLoader.print("+Creating MolComponent: [" + molComponent.getName() + "]");
                }
            } else if (1 == molComponentList.size()) {
                final java.util.Iterator i = molComponentList.iterator();
                molComponent = (Molecule) i.next();
                AbstractLoader.debug("MolComponent already exists: [" + molComponent.getName() + "]");
            }
            // Associate the ComponentCategories
            molComponent.setCategories(componentCategoryList);

            // ********************************************************************************
            // *** HazardPhrases associated with the RefSample ***
            // ********************************************************************************
            // Create Set of Hazard phrases to associate with RefSample
            // Includes R S and Categories of danger together
            // TODO: Need to handle R and S phrases separately later, Hidden in
            // MetaInfo
            // Create hash 'hazardPhraseAttrMap'for HazardPhrase attributes from
            // csv parser
            // Create a Set 'hazardPhraseList'for the list of HazardPhrases to
            // associate with RefSample
            final String rPhrases = lcsvp.getValueByLabel("Risk No.").trim();
            final String sPhrases = lcsvp.getValueByLabel("Safety phrases").trim();
            final String catDanger = lcsvp.getValueByLabel("Flammable,Toxic/IrritantCorrosive").trim();
            final String hazreg = ",";
            final Map<String, Object> hazardPhraseAttrMap = new java.util.HashMap<String, Object>();
            final java.util.Set<HazardPhrase> hazardPhraseList = new java.util.HashSet<HazardPhrase>();
            // concatenate all hazard phrases then split on '
            final String hazPhrases = rPhrases + "," + sPhrases + "," + catDanger;
            final String[] hazardPhraseStrArray = Pattern.compile(hazreg).split(hazPhrases);
            for (int i = 0; i < hazardPhraseStrArray.length; i++) {
                if ("".equals(hazardPhraseStrArray[i].trim())) {
                    continue;
                }
                hazardPhraseAttrMap.put(HazardPhrase.PROP_CODE, hazardPhraseStrArray[i].trim());
                // code to see if the hazard phrase is in the database
                // retrieves hazardphrase with code matching value in
                // hazardPhraseAttrMap
                final java.util.Collection<HazardPhrase> hazardPhrasesFound =
                    wv.findAll(HazardPhrase.class, hazardPhraseAttrMap);
                if (hazardPhrasesFound.isEmpty()) {
                    AbstractLoader.print(">>> Didn't find the HazardPhrase: [" + hazardPhraseStrArray[i]
                        + "]");
                } else if (1 == hazardPhrasesFound.size()) {
                    // Add the HazardPhrase to the list
                    hazardPhraseList.add(hazardPhrasesFound.iterator().next());
                }
            }

            // ********************************************************************************
            // *** SampleCategories associated with RefSample ***
            // ********************************************************************************
            // Create a Set of sample categories for RefSample
            // These are separated by ,
            final String sampleCategoriesStr = lcsvp.getValueByLabel("SampleCategory").trim();
            final String samCatreg = ",";
            final Map<String, Object> sampleCategoryAttrMap = new java.util.HashMap<String, Object>();
            final java.util.Set<SampleCategory> sampleCategoryList = new java.util.HashSet<SampleCategory>();
            final String[] sampleCategoryStrArray = Pattern.compile(samCatreg).split(sampleCategoriesStr);
            for (int i = 0; i < sampleCategoryStrArray.length; i++) {
                if ("".equals(sampleCategoryStrArray[i].trim())) {
                    continue;
                }
                sampleCategoryAttrMap.put(SampleCategory.PROP_NAME, sampleCategoryStrArray[i].trim());
                // code to see if the category is in the database
                // retrieves sample category with code matching value in
                // sampleCategoryAttrMap
                final java.util.Collection<SampleCategory> sampleCategoriesFound =
                    wv.findAll(SampleCategory.class, sampleCategoryAttrMap);
                if (sampleCategoriesFound.isEmpty()) {
                    AbstractLoader.print(">>> Didn't find the SampleCategory: [" + sampleCategoryStrArray[i]
                        + "]");
                } else if (1 == sampleCategoriesFound.size()) {
                    // Add the Samplecategories to the list
                    sampleCategoryList.add(sampleCategoriesFound.iterator().next());
                }
            }

            // ********************************************************************************
            // *** 2/ Create RefSample ***
            // ********************************************************************************
            // Create RefSample using RefSampleName=name and Is_HAZARD=isHazard
            final Map<String, Object> refSampleAttrMap = new java.util.HashMap<String, Object>();
            final String refSampleName = lcsvp.getValueByLabel("RefSampleName").trim();
            //refSampleName = replaceTm(refSampleName);
            refSampleAttrMap.put(AbstractSample.PROP_NAME, refSampleName);

            // Now create the RefSample
            RefSample refSample = null;
            final java.util.Collection refSamplesFound = wv.findAll(RefSample.class, refSampleAttrMap);

            // add hazard phrase
            boolean isHarzard = false;
            final String isHarzardStr = lcsvp.getValueByLabel("Is_HAZARD").trim();
            if (!"".equals(isHarzardStr)) {
                isHarzard = true;
            }
            refSampleAttrMap.put(AbstractSample.PROP_ISHAZARD, isHarzard);
            if (refSamplesFound.isEmpty()) {
                // add the Sample categories and hazard phrases
                refSampleAttrMap.put(AbstractSample.PROP_SAMPLECATEGORIES, sampleCategoryList);
                refSampleAttrMap.put(AbstractSample.PROP_HAZARDPHRASES, hazardPhraseList);
                // create new RefSample
                refSample = new RefSample(wv, refSampleAttrMap);
                AbstractLoader.print("Creating RefSample: [" + refSample.getName() + "]");

                // Create a SampleComponent for the Molecule and associate
                // it to the RefSample
                // Attribute map for SampleComponent passed to the map
                // constructor
                final Map<String, Object> sampleComponentAttrMap = new java.util.HashMap<String, Object>();
                sampleComponentAttrMap.put(SampleComponent.PROP_ABSTRACTSAMPLE, refSample);
                sampleComponentAttrMap.put(SampleComponent.PROP_REFCOMPONENT, molComponent);
                // always create new SampleComponent because it can only be
                // associated to one RefSample
                final SampleComponent sampleComponent = new SampleComponent(wv, sampleComponentAttrMap);
                AbstractLoader.print("Creating SampleComponent: ["
                    + sampleComponent.getRefComponent().getName() + "]");
            } else if (1 == refSamplesFound.size()) {
                final java.util.Iterator i = refSamplesFound.iterator();
                refSample = (RefSample) i.next();
                // Associate the roles
                refSample.setSampleCategories(sampleCategoryList);
                refSample.setHazardPhrases(hazardPhraseList);
                AbstractLoader.print("RefSample already exists: [" + refSample.getName()
                    + "] - associated roles updated");
            }

        }
    }

    /*
     * method to check if string contains [R] -used to represent 'reg TM' sign if so then replace with html
     * code &reg;
     */
    public static String replaceTm(final String chemicalName) {
        final String reg = new String("[R]");
        final String tm = new String("&reg;");
        String newChemicalName = "";
        if (chemicalName.length() > 2) {
            for (int i = 0; i < chemicalName.length() - 2; i++) {
                final String match = chemicalName.substring(i, i + 3);
                if (!reg.equals(match)) {
                    newChemicalName = chemicalName;
                }

                else {
                    newChemicalName = (chemicalName.substring(0, i)).concat(tm);
                    final String end = chemicalName.substring(i + 3);
                    newChemicalName = newChemicalName.concat(end);
                    break;
                }
            }
            return newChemicalName;
        }
        return chemicalName;
    }

    public static void main(final String[] args) {

        if (args.length == 0) {
            AbstractLoader.print("Usage: ChemicalUtility filename.csv");
        }

        final AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        for (int i = 0; i < args.length; i++) {
            final String filename = args[i];
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                ChemicalUtility.loadFile(wv, filename);
                wv.commit();
                AbstractLoader.print("Loaded details from file: " + filename);
            } catch (final java.io.IOException ex) {
                AbstractLoader.print("Unable to read from file: " + filename);
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
        AbstractLoader.print("Chemical utility has finished");
    }

}
