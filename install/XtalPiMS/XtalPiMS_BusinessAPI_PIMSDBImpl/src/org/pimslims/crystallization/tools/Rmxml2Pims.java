/**
 * rmxml2pims org.pimslims.crystallization.tools Rmxml2Pims.java
 * 
 * @author Jon
 * @date 23 Aug 2011
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2011 Jon The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.pimslims.business.DataStorage;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.xml.rmxml.Aliases;
import org.pimslims.crystallization.xml.rmxml.CasNumbers;
import org.pimslims.crystallization.xml.rmxml.Condition;
import org.pimslims.crystallization.xml.rmxml.ConditionIngredient;
import org.pimslims.crystallization.xml.rmxml.Conditions;
import org.pimslims.crystallization.xml.rmxml.Ingredient;
import org.pimslims.crystallization.xml.rmxml.Ingredients;
import org.pimslims.crystallization.xml.rmxml.Stock;
import org.pimslims.crystallization.xml.rmxml.Stocks;
import org.pimslims.crystallization.xml.screen.ComponentList;
import org.pimslims.crystallization.xml.screen.ComponentType;
import org.pimslims.crystallization.xml.screen.ConcentrationUnitType;
import org.pimslims.crystallization.xml.screen.SolutionList;
import org.pimslims.crystallization.xml.screen.SolutionType;
import org.pimslims.crystallization.xml.screen.TypeType;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.molecule.Substance;
import org.pimslims.search.Searcher;
import org.xml.sax.SAXException;

/**
 * Rmxml2Pims - convert a screen description in Formulatrix's RMXML into xtalPiMS Screen format.
 */
public class Rmxml2Pims {

    /**
     * RMXML schema
     */
    Schema rmxmlSchema;

    /**
     * XtalPiMS screen schema
     */
    Schema pimsSchema;

    /**
     * JAXB context for RMXML schema
     */
    JAXBContext rmxmlJc;

    /**
     * JAXB context for XtalPiMS screen schema
     */
    JAXBContext pimsJc;

    /**
     * Rmxml2Pims.main
     * 
     * @param args
     * @throws SAXException
     * @throws JAXBException
     * @throws IOException
     * @throws AccessException
     * @throws ConstraintException
     */
    public static void main(String[] args) throws JAXBException, SAXException, BusinessException, IOException {
        Rmxml2Pims instance = new Rmxml2Pims();
        instance.processFile("./data/screens-from-rmxml/rmxml2pims/xtalPiMS_Hampton_PEG_Ion_HT.properties");
    }

    /**
     * Constructor for Rmxml2Pims - calls initJAXBContexts.
     * 
     * @throws JAXBException
     * @throws SAXException
     */
    public Rmxml2Pims() throws JAXBException, SAXException {
        initJAXBContexts();
    }

    /**
     * Rmxml2Pims.initJAXBContexts - initialize both JAXBContexts and Schemas.
     * 
     * @throws JAXBException
     * @throws SAXException
     */
    public void initJAXBContexts() throws JAXBException, SAXException {
        rmxmlJc = JAXBContext.newInstance("org.pimslims.crystallization.xml.rmxml");
        pimsJc = JAXBContext.newInstance("org.pimslims.crystallization.xml.screen");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        rmxmlSchema = schemaFactory.newSchema(getClass().getResource("/org/pimslims/xsd/rmxml/RMXML.xsd"));
        pimsSchema =
            schemaFactory
                .newSchema(getClass().getResource("/org/pimslims/xsd/xtalpims/XtalPiMS_Screens.xsd"));
    }

    /**
     * Rmxml2Pims.processFile - process an RMXML document into an XtalPiMS Screen document and write to
     * System.out.
     * 
     * @param filename - the file to process
     * @throws JAXBException
     * @throws SAXException
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     * @throws BusinessException
     */
    public void processFile(String filename) throws JAXBException, SAXException, IOException,
        BusinessException {

        DataStorage ds = DataStorageFactory.getDataStorageFactory().getDataStorage();
        ds.connectResources();
        ds.openResources("administrator", true, true);
        WritableVersion wv =
            ((org.pimslims.crystallization.datastorage.DataStorageImpl) ds).getWritableVersion();

        try {
            System.err.println("Processing " + filename);
            processFile(filename, wv);
        } catch (ConstraintException e) {
            throw new BusinessException(e);
        } catch (AccessException e) {
            throw new BusinessException(e);
        } finally {
            wv.abort();
        }
    }

    /**
     * Rmxml2Pims.processFile - process an RMXML document into an XtalPiMS Screen document and write to
     * System.out.
     * 
     * @param filename - the file to process
     * @param wv - the WritableVersion to write to
     * @throws JAXBException
     * @throws SAXException
     * @throws AccessException
     * @throws ConstraintException
     * @throws IOException
     */
    public void processFile(String filename, WritableVersion wv) throws JAXBException, SAXException,
        ConstraintException, AccessException, IOException {

        Properties prop = new Properties();
        File f = new File(filename);
        System.err.println("Processing " + f.getAbsolutePath());
        prop.load(new FileInputStream(new File(filename)));

        org.pimslims.crystallization.xml.screen.Screen p = createPims(prop);

        int count = 1;
        if (prop.containsKey("rmxml.count")) {
            count = Integer.parseInt(prop.getProperty("rmxml.count"));
        }

        for (int i = 1; i <= count; i++) {
            String stub = "rmxml." + i + ".";
            org.pimslims.crystallization.xml.rmxml.Screen r = readRmxml(prop.getProperty(stub + "file"));
            String prefix = prop.getProperty(stub + "prefix");
            int cols = Integer.parseInt(prop.getProperty(stub + "cols"));
            int rowoffset = Integer.parseInt(prop.getProperty(stub + "rowoffset"));
            int coloffset = Integer.parseInt(prop.getProperty(stub + "coloffset"));
            mapRmxml2Pims(p, r, prefix, cols, rowoffset, coloffset, wv);
        }
        writePims(p);

    }

    /**
     * Rmxml2Pims.readRmxml - read an RMXML document.
     * 
     * @param filename - the file to read
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws FileNotFoundException
     */
    public org.pimslims.crystallization.xml.rmxml.Screen readRmxml(String filename) throws JAXBException,
        SAXException, FileNotFoundException {
        Unmarshaller u = rmxmlJc.createUnmarshaller();
        u.setSchema(rmxmlSchema);
        File f = new File(filename);
        System.err.println("Processing " + f.getAbsolutePath());
        return (org.pimslims.crystallization.xml.rmxml.Screen) u.unmarshal(new FileInputStream(filename));
    }

    /**
     * 
     * Rmxml2Pims.readPims - read an XtalPiMS Screen document.
     * 
     * @param filename - the file to read
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws FileNotFoundException
     */
    public org.pimslims.crystallization.xml.screen.Screen readPims(String filename) throws JAXBException,
        SAXException, FileNotFoundException {
        Unmarshaller u = pimsJc.createUnmarshaller();
        u.setSchema(pimsSchema);
        return (org.pimslims.crystallization.xml.screen.Screen) u.unmarshal(new FileInputStream(filename));
    }

    /**
     * Rmxml2Pims.writePims - write an XtalPiMS Screen document to System.out.
     * 
     * @param screen - the document to write
     * @throws JAXBException
     */
    public void writePims(org.pimslims.crystallization.xml.screen.Screen screen) throws JAXBException {
        Marshaller m = pimsJc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION,
            "http://www.pims-lims.org/xsd/xtalpims/screen_v1.0.xsd");
        m.marshal(screen, System.out);
        //m.marshal(new JAXBElement<org.pimslims.crystallization.xml.screen.Screen>(new QName("http://www.pims-lims.org/xsd/xtalpims/screen_v1.0.xsd","screen",XMLConstants.DEFAULT_NS_PREFIX), org.pimslims.crystallization.xml.screen.Screen.class, screen), System.out);
    }

    /**
     * Rmxml2Pims.createPims - create an XtalPiMS Screen document with no solutions, populating relevant
     * elements (screenCatNum, screenDetails, screenName, screenSupplier, screenReference) with values from
     * prop.
     * 
     * @param prop - the properties bag from which to obtain settings
     * @return An empty XtalPiMS Screen document with no solutions
     */
    public org.pimslims.crystallization.xml.screen.Screen createPims(Properties prop) {

        // Get a new XtalPiMS Screen document
        org.pimslims.crystallization.xml.screen.ObjectFactory pimsObjectFactory =
            new org.pimslims.crystallization.xml.screen.ObjectFactory();
        org.pimslims.crystallization.xml.screen.Screen pimsScreen = pimsObjectFactory.createScreen();

        // Create a SolutionList and attach it to the Screen
        SolutionList pimsSolutions = pimsObjectFactory.createSolutionList();
        pimsScreen.setSolutions(pimsSolutions);

        // Set screen properties to empty string as a suitable default
        pimsScreen.setScreenCatNum("");
        pimsScreen.setScreenDetails("");
        pimsScreen.setScreenName("");
        pimsScreen.setScreenReference("");
        pimsScreen.setScreenSupplier("");

        // Set the number of solutions on the screen - we know its zero a this point
        pimsScreen.setScreenNumberOfSolutions(0);

        // Override with value from prop, if available
        if (null != prop) {
            if (prop.containsKey("screenCatNum")) {
                pimsScreen.setScreenCatNum((String) prop.get("screenCatNum"));
            }
            if (prop.containsKey("screenDetails")) {
                pimsScreen.setScreenDetails((String) prop.get("screenDetails"));
            }
            if (prop.containsKey("screenName")) {
                pimsScreen.setScreenName((String) prop.get("screenName"));
            }
            if (prop.containsKey("screenReference")) {
                pimsScreen.setScreenReference((String) prop.get("screenReference"));
            }
            if (prop.containsKey("screenSupplier")) {
                pimsScreen.setScreenSupplier((String) prop.get("screenSupplier"));
            }
        }

        // Return Screen document
        return pimsScreen;

    }

    /**
     * Rmxml2Pims.mapRmxml2Pims - convert an RMXML document into an XtalPiMS Screen document To put a 96
     * condition rmxml screen into pimsScreen, call as mapRmxml2Pims(pimsScreen, rmxml, "", 12, 0, 0). To put
     * a 24 condition rmxml screen into the bottom right quadrant of pimsScreen with solution ids myPrefix1
     * through myPrefix24, call as mapRmxml2Pims(pimsScreen, rmxml, "myPrefix", 6, 4, 6).
     * 
     * @param pimsScreen - the screen to populate
     * @param rmxml - the screen from which condition information will be taken
     * @param idPrefix - a prefix to prepend to the one-based condition number to generate the solutionID
     * @param cols - the number of columns across which these conditions should be laid out
     * @param rowoffset - the row offset to the start of this screen in pimsScreen
     * @param coloffset - the col offset to the start of this screen in pimsScreen
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public org.pimslims.crystallization.xml.screen.Screen mapRmxml2Pims(
        org.pimslims.crystallization.xml.screen.Screen pimsScreen,
        org.pimslims.crystallization.xml.rmxml.Screen rmxml, String idPrefix, int cols, int rowoffset,
        int coloffset, WritableVersion wv) throws ConstraintException, AccessException {

        // LocalID-to-name/units map declarations
        Map<BigInteger, String> id2name = new HashMap<BigInteger, String>();
        Map<BigInteger, ConcentrationUnitType> id2units = new HashMap<BigInteger, ConcentrationUnitType>();

        // Loop over all stocks of all ingredients
        Ingredients is = rmxml.getIngredients();
        List<Ingredient> ilist = is.getIngredient();
        for (Ingredient i : ilist) {
            Stocks ss = i.getStocks();
            List<Stock> slist = ss.getStock();
            for (Stock s : slist) {
                BigInteger id = s.getLocalID();

                // Build localID-to-name map
                id2name.put(id, this.getNameForIngredient(i, wv));

                // Convert units and build localID-to-unit map
                if ("M".equals(s.getUnits())) {
                    id2units.put(id, ConcentrationUnitType.M);
                } else if ("mM".equals(s.getUnits())) {
                    id2units.put(id, ConcentrationUnitType.mM);
                } else if ("%w/v".equals(s.getUnits())) {
                    id2units.put(id, ConcentrationUnitType.PERCENT_W_V);
                } else if ("%v/v".equals(s.getUnits())) {
                    id2units.put(id, ConcentrationUnitType.PERCENT_V_V);
                } else if ("%v\\v".equals(s.getUnits())) {
                    id2units.put(id, ConcentrationUnitType.PERCENT_V_V);
                } else {
                    throw new IllegalArgumentException("Unmappable RMXML unit " + s.getUnits()
                        + " in stock localID " + id.toString());
                }
            }
        }

        // Get an ObjectFactory
        org.pimslims.crystallization.xml.screen.ObjectFactory pimsObjectFactory =
            new org.pimslims.crystallization.xml.screen.ObjectFactory();

        // Get the SolutionList and dig out the underlying List
        SolutionList pimsSolutions = pimsScreen.getSolutions();
        List<SolutionType> pimsSolutionList = pimsSolutions.getSolutions();

        // Loop over all the conditions
        // Note - RMXML does not have a condition id so we have to assume they are in
        // Formulatrix's standard well order (along columns then down rows) and count them ourselves
        Conditions cs = rmxml.getConditions();
        List<Condition> clist = cs.getCondition();
        int index = 0;
        for (Condition c : clist) {

            // Well num, assuming its being mapped into a 96 well plate
            int zx = coloffset + index % cols;
            int zy = rowoffset + index / cols;
            int wellnum = zx + (zy * 12);

            // Create a SolutionType and add it to the list
            SolutionType pimsSolution = pimsObjectFactory.createSolutionType();
            pimsSolutionList.add(pimsSolution);

            // Assign the solution id
            pimsSolution.setSolutionId(idPrefix + (index + 1));

            // Map Formulatrix well number to XtalPiMS plate position
            pimsSolution.setLocalNum(formatLocalNum(wellnum));

            // Create a ComponentList, add it to the SolutionType and dig out the underlying list
            ComponentList pimsComponents = pimsObjectFactory.createComponentList();
            pimsSolution.setComponents(pimsComponents);
            List<ComponentType> pimsComponentList = pimsComponents.getComponents();

            // Loop over each ConditionIngredient in the Condition
            List<ConditionIngredient> cilist = c.getConditionIngredient();
            for (ConditionIngredient ci : cilist) {

                // Create a ComponentType and add it to the ComponentList
                ComponentType pimsComponent = pimsObjectFactory.createComponentType();
                pimsComponentList.add(pimsComponent);

                // Apply the units (mapped from id)
                BigInteger id = ci.getStockLocalID();
                ConcentrationUnitType cut = id2units.get(id);

                if (ConcentrationUnitType.mM.equals(cut)) {
                    // Fix and apply the concentration and units
                    double conc = ci.getConcentration().doubleValue() / 1000D;
                    pimsComponent.setConcentration(BigDecimal.valueOf(conc));
                    pimsComponent.setConcentrationUnits(ConcentrationUnitType.M);
                }

                else {
                    // Apply the concentration and units
                    pimsComponent.setConcentration(ci.getConcentration());
                    pimsComponent.setConcentrationUnits(id2units.get(id));
                }

                // Apply the name (mapped from id)
                pimsComponent.setName(id2name.get(id));

                // Apply the pH
                if (null != ci.getPH()) {
                    pimsComponent.setPH(ci.getPH());
                }

                // Apply the type
                if (null != ci.getType()) {
                    if ("Polymer".equals(ci.getType())) {
                        ci.setType("precipitant");
                    }
                    pimsComponent.setType(TypeType.fromValue(ci.getType().toLowerCase()));
                }

            }

            // Increment the well number
            index++;

        }

        // Update count
        pimsScreen.setScreenNumberOfSolutions(pimsSolutionList.size());

        // Return Screen document
        return pimsScreen;

    }

    /**
     * Rmxml2Pims.formatLocalNum - convert Formulatrix wellnum into plate position. Note - assumes 96 well
     * plate layout.
     * 
     * @param wellnum
     * @return
     */
    public String formatLocalNum(int wellnum) {
        // wellnum runs from 0 (=A1) to 95 (=H12) going along rows first, so 11 = A12, 12=B1, etc.
        char r = (char) (65 + wellnum / 12);
        long c = 1 + wellnum % 12;
        DecimalFormat df = new DecimalFormat("00");
        return String.valueOf(r) + df.format(c);
    }

    /**
     * Rmxml2Pims.getNameForIngredient - get the name to use for this ingredient, creating a new Molecule or
     * updating an existing Molecule if need be. Calls {@link #getMolecule(Ingredient, ReadableVersion)}
     * followed by {@link #createMolecule(Ingredient, WritableVersion)} or
     * {@link #updateMolecule(Ingredient, Molecule, WritableVersion)} as appropriate.
     * 
     * @param ingredient
     * @param wv
     * @return
     * @throws ConstraintException
     * @throws AccessException
     * @see #getMolecule(Ingredient, ReadableVersion)
     * @see #createMolecule(Ingredient, WritableVersion)
     * @see #updateMolecule(Ingredient, Molecule, WritableVersion)
     */
    public String getNameForIngredient(Ingredient ingredient, WritableVersion wv) throws ConstraintException,
        AccessException {
        System.err.println("Processing " + ingredient.getName());
        if (ingredient.getName().startsWith("Polyethylene glycol")) {
            String tmp = ingredient.getName().toLowerCase();
            tmp = tmp.replace(",", "");
            ingredient.setName(tmp);
        }
        Molecule molecule = getMolecule(ingredient, wv);
        if (null == molecule) {
            molecule = createMolecule(ingredient, wv);
        } else {
            updateMolecule(ingredient, molecule, wv);
        }
        return molecule.getName();
    }

    /**
     * Rmxml2Pims.getMolecule - attempts to match ingredient to an existing molecule using the following
     * checks and returning the first match: 1 - Check name against molecule.name; 2 - Check name against
     * molecule.synonyms; 3 - Check shortName against molecule.synonyms; 4 - Check casNumbers against
     * molecule.casNumber; 5 - Check casNumbers against molecule.synonyms; and 6 - Check aliases against
     * molecule.synonyms.
     * 
     * @param ingredient - the ingredient for which to find a matching molecule
     * @param version - the version to search
     * @return
     */
    public Molecule getMolecule(Ingredient ingredient, ReadableVersion version) {

        Molecule molecule = null;

        // 1. Check name against name
        molecule = getMoleculeByName(ingredient.getName(), version);
        if (molecule != null) {
            return molecule;
        }

        // 1a. Check name against lowercase name
        molecule = getMoleculeByName(ingredient.getName().toLowerCase(), version);
        if (molecule != null) {
            return molecule;
        }

        // 2. Check name against synonyms
        molecule = getMoleculeBySynonym(ingredient.getName(), version);
        if (molecule != null) {
            return molecule;
        }

        // 3. Check short name against synonyms
        molecule = getMoleculeBySynonym(ingredient.getShortName(), version);
        if (molecule != null) {
            return molecule;
        }

        // 4. Check cas numbers against cas number
        CasNumbers casNumbers = ingredient.getCasNumbers();
        if (null != casNumbers) {
            for (String cas : casNumbers.getCasNumber()) {
                molecule = getMoleculeByCasNum(cas, version);
                if (molecule != null) {
                    return molecule;
                }
            }

            // 5. Check cas numbers against synonyms
            for (String cas : casNumbers.getCasNumber()) {
                molecule = getMoleculeBySynonym("CAS:" + cas, version);
                if (molecule != null) {
                    return molecule;
                }
            }
        }

        // 6. Check aliases against synonyms
        Aliases aliases = ingredient.getAliases();
        if (null != aliases) {
            for (String alias : aliases.getAlias()) {
                molecule = getMoleculeBySynonym(alias, version);
                if (molecule != null) {
                    return molecule;
                }
            }
        }

        return null;
    }

    /**
     * Rmxml2Pims.createMolecule - create a Molecule for the specified ingredient, populating the synonyms
     * with shortName, any aliases and any CAS numbers (prepended with "CAS:"). Molecule.casNum is set to the
     * first casNumber from the ingredient. Molecule.PROP_MOLTYPE is set to "other".
     * 
     * @param ingredient - the ingredient for which to create a molecule
     * @param wv - the WritableVersion to write to
     * @return The newly created molecule
     * @throws AccessException
     * @throws ConstraintException
     */
    public Molecule createMolecule(Ingredient ingredient, WritableVersion wv) throws AccessException,
        ConstraintException {

        List<String> synonyms = new ArrayList<String>();
        synonyms.add(ingredient.getShortName());

        Aliases aliases = ingredient.getAliases();
        if (null != aliases) {
            for (String alias : aliases.getAlias()) {
                synonyms.add(alias);
            }
        }

        CasNumbers casNumbers = ingredient.getCasNumbers();
        String firstCas = null;
        if (null != casNumbers) {
            for (String cas : casNumbers.getCasNumber()) {
                synonyms.add("CAS:" + cas);
                if (null == firstCas) {
                    firstCas = cas;
                }
            }
        }

        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(Substance.PROP_NAME, ingredient.getName());
        attr.put(Substance.PROP_SYNONYMS, synonyms);
        if (null != firstCas) {
            attr.put(Molecule.PROP_CASNUM, firstCas);
        }
        attr.put(Molecule.PROP_MOLTYPE, "other");

        return wv.create(Molecule.class, attr);
    }

    /**
     * Rmxml2Pims.updateMolecule - update a Molecule from the specified ingredient. Updates the synonyms in
     * line with {@link #createMolecule(Ingredient, WritableVersion)}, additionally adding ingredient.name if
     * it doesn't match molecule.name.
     * 
     * @param ingredient - the ingredient from which to update the Molecule
     * @param molecule - the molecule to be updated
     * @param wv - the WritableVersion to write to
     * @throws ConstraintException
     */
    public void updateMolecule(Ingredient ingredient, Molecule molecule, WritableVersion wv)
        throws ConstraintException {

        List<String> synonyms = molecule.getSynonyms();

        // If this isn't a name match, add the name to the synonyms
        if (!molecule.getName().equals(ingredient.getName())) {
            if (!synonyms.contains(ingredient.getName())) {
                synonyms.add(ingredient.getName());
            }
        }

        if (!synonyms.contains(ingredient.getShortName())) {
            synonyms.add(ingredient.getShortName());
        }

        // Add any missing aliases to the synonyms
        Aliases aliases = ingredient.getAliases();
        if (null != aliases) {
            for (String alias : aliases.getAlias()) {
                if (!synonyms.contains(alias)) {
                    synonyms.add(alias);
                }
            }
        }

        // Add any missing CAS numbers to the synonyms
        CasNumbers casNumbers = ingredient.getCasNumbers();
        String firstCas = null;
        if (null != casNumbers) {
            for (String cas : casNumbers.getCasNumber()) {
                String cascas = "CAS:" + cas;
                if (!synonyms.contains(cascas)) {
                    synonyms.add(cascas);
                }
                if (null == firstCas) {
                    firstCas = cas;
                }
            }
        }

        // Set a CAS number if it hasn't been done already
        if ((null != firstCas) && (null == molecule.getCasNum())) {
            molecule.setCasNum(firstCas);
        }

        wv.save(molecule);

    }

    /**
     * Rmxml2Pims.getMoleculeByName - find a Molecule by name.
     * 
     * @param name - the name to search on
     * @param version - the version to search
     * @return
     */
    public static Molecule getMoleculeByName(String name, ReadableVersion version) {
        return version.findFirst(Molecule.class, Substance.PROP_NAME, name);
    }

    /**
     * Rmxml2Pims.getMoleculeByCasNum - find a Molecule by CAS Number.
     * 
     * @param casNum - the CAS Number to search on
     * @param version - the version to search
     * @return
     */
    public static Molecule getMoleculeByCasNum(String casNum, ReadableVersion version) {
        // TODO Molecule.casNumber should be unique
        return version.findFirst(Molecule.class, Molecule.PROP_CASNUM, casNum);
    }

    /**
     * Rmxml2Pims.getMoleculeBySynonym - find a Molecule by synonym.
     * 
     * @param synonym - the alias to search on
     * @param version - the version to search
     * @return
     */
    public static Molecule getMoleculeBySynonym(String synonym, ReadableVersion version) {
        final Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(Substance.PROP_SYNONYMS, org.pimslims.search.Conditions.listContains(synonym));
        Searcher s = new Searcher(version);
        Collection<ModelObject> results = s.search(criteria, version.getMetaClass(Molecule.class));
        if (!results.isEmpty()) {
            if (1 < results.size()) {
                throw new IllegalStateException("More than one molecule matches synonym: " + synonym);
            }
            return (Molecule) results.iterator().next();
        }
        return null;
    }

}
