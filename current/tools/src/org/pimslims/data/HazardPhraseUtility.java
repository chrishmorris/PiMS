package org.pimslims.data;

import java.io.IOException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.exception.ModelException;
import org.pimslims.model.reference.HazardPhrase;

public class HazardPhraseUtility extends AbstractLoader {

    /**
     * @param element the XML element to process
     * @return a map of name => value for its attributes
     */
    private static Map<String, Object> getAttributeValues(Element element) {
        Map<String, Object> values = new java.util.HashMap<String, Object>();
        for (Iterator iter = element.getAttributes().iterator(); iter.hasNext();) {
            Attribute attribute = (Attribute) iter.next();

            Object value = attribute.getValue(); // default for string values
            String name = attribute.getName();
            if (-1 != name.indexOf("Date")) {
                value = new java.text.SimpleDateFormat().parse((String) value, new ParsePosition(0));
            } else if (-1 != name.indexOf("Num")) {
                // probably a whole number
                value = Integer.decode((String) value);
            } else if ("amount".equals(name) || "temperature".equals(name) || name.endsWith("Temperature")
                || "duration".equals(name) || "centrifugationSpeed".equals(name)) {
                // probably a float
                try {
                    value = Float.valueOf((String) value);
                } catch (java.lang.NumberFormatException e) {
                    if (!"".equals(((String) value).trim())) {
                        System.err.println("Bad Value: " + attribute.getValue() + " for: "
                            + attribute.getName());
                    }
                    value = new Float(0f);
                }
            }
            values.put(attribute.getName(), value);

        }
        // massage type of "remarks"
        if (values.containsKey("remarks")) {
            java.util.List<String> remarks = new ArrayList<String>();
            remarks.add(element.getAttributeValue("remarks"));
            values.put("remarks", remarks);
        }

        values.remove("id"); // this crept into the XML, but definitely isn't
        // wanted for database
        return values;
    }

    /**
     * <HazardPhrase> <classification>MSDS</classification> <code>R40/20</code> <phrase>Harmful: possible risk
     * of irreversible effects through inhalation.</phrase> <details>The phrase has been deleted by ATP 28 (6
     * August 2001),but may still appear in cards not modified since then.</details> </HazardPhrase>
     * 
     * @param wv
     * @param phraseElement
     * @throws AccessException
     * @throws ConstraintException
     */
    private void processHazardPhrase(WritableVersion wv, Element phraseElement) throws AccessException,
        ConstraintException {
        assert "HazardPhrase".equals(phraseElement.getName());

        String classification = phraseElement.getChildTextNormalize("classification");
        if (!"MSDS".equals(classification)) {
            // don't make reference data for local phrases
            return;
        }
        String code = phraseElement.getChildTextNormalize("code");
        Map<String, Object> attributes = getAttributeValues(phraseElement);
        String phrase = phraseElement.getChildTextNormalize("phrase");
        attributes.put("phrase", phrase);
        String details = phraseElement.getChildTextNormalize("details");
        attributes.put("details", details);
        try {
            // print("Adding HazardPhrase: [" + classification + ",
            // " + code +"]");
            createHazardPhrase(wv, classification, code, attributes);
        } catch (ConstraintException e) {
            print(phraseElement.toString());
            throw e;
        } catch (AccessException e) {
            print(phraseElement.toString());
            throw e;
        }

        // LATER could check for any extra elements

    }

    /**
     * Get or create a new HazardPhrase This method will be used rarely to create, hazard phrases are
     * reference data.
     * 
     * @param version
     * @param classification
     * @param code
     * @param attributes
     * @return the new HazardPhrase
     * @throws ConstraintException
     * @throws AccessException
     */
    public static HazardPhrase createHazardPhrase(final WritableVersion version, final String classification,
        final String code, final Map attributes) throws ConstraintException, AccessException {
        final Map<String, Object> a = new java.util.HashMap<String, Object>();
        a.put(HazardPhrase.PROP_CLASSIFICATION, classification);
        a.put(HazardPhrase.PROP_CODE, code);
        final java.util.Collection hazardPhrasesFound = version.findAll(HazardPhrase.class, a);
        if (hazardPhrasesFound.isEmpty()) {
            a.putAll(attributes);
            return new HazardPhrase(version, a);
        }
        return (HazardPhrase) hazardPhrasesFound.iterator().next();
    }

    /**
     * @param args the URL, e.g. file:///myProtocol.xml
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            print("Usage: java ElementLister URL");
            return;
        }
        AbstractModel model = ModelImpl.getModel();
        //AbstractLoader.silent = false;
        WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);

        SAXBuilder builder = new SAXBuilder();
        HazardPhraseUtility pu = new HazardPhraseUtility();

        try {
            Document doc = builder.build(args[0]);
            Collection phraseElements = doc.getRootElement().getChildren();
            for (Iterator iter = phraseElements.iterator(); iter.hasNext();) {
                Element phrase = (Element) iter.next();
                pu.processHazardPhrase(wv, phrase);
            }

            wv.commit();
            print("processed: " + args[0]);
        } catch (JDOMException e) { // indicates a well-formedness error
            print(args[0] + " is not well-formed.");
            print(e.getMessage());
        } catch (IOException e) {
            print(e);
        } catch (ModelException e) {
            print(e);
            e.printStackTrace();
        } finally {
            if (!wv.isCompleted()) {
                wv.abort();
            }
        }

    }

    public static void loadFile(WritableVersion wv, String filename) throws JDOMException, IOException,
        AccessException, ConstraintException {
        SAXBuilder builder = new SAXBuilder();
        HazardPhraseUtility pu = new HazardPhraseUtility();

        Document doc = builder.build(filename);
        Collection phraseElements = doc.getRootElement().getChildren();
        for (Iterator iter = phraseElements.iterator(); iter.hasNext();) {
            Element phrase = (Element) iter.next();
            pu.processHazardPhrase(wv, phrase);
        }

    }

}
