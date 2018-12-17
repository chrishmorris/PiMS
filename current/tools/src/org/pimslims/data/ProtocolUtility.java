package org.pimslims.data;

import java.io.IOException;
import java.io.Reader;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;

public class ProtocolUtility extends AbstractLoader {

    //private static String NAMING_SYSTEM = "default";

    private final WritableVersion wv;

    /**
     * @param wv
     */
    public ProtocolUtility(final WritableVersion wv) {
        super();
        this.wv = wv;
    }

    /**
     * From protocol xml file: e.g. <experimentType>Miniprep</experimentType> Returns the associated
     * experiement type. It should already exist within the db, otherwise throws an error.
     * 
     * @param wv
     * @param element e.g. <experimentType>Miniprep</experimentType>
     * @return the experiment type
     * @throws AccessException
     * @throws ConstraintException
     */
    ExperimentType processExperimentType(final WritableVersion wv, final Element element)
        throws AccessException, ConstraintException {
        assert "experimentType".equals(element.getName());
        final String expTypeName = element.getTextNormalize();
        final java.util.Collection<ExperimentType> expTypesFound =
            wv.findAll(ExperimentType.class, ExperimentType.PROP_NAME, expTypeName);
        assert 0 != expTypesFound.size() : "ERROR: ExperimentType [" + expTypeName
            + "] must exist (loaded as ref data)";
        return expTypesFound.iterator().next();
    }

    /**
     * From protocol xml file: e.g. <outputSample sampleCatNamingSys="default" sampleCatName="DNA" amount=""
     * unit="L" displayUnit="mL" name="LIC-product"/>
     * 
     * 
     * 
     * @param wv
     * @param protocol
     * @param element
     * @throws AccessException
     * @throws ConstraintException
     */
    void processOutputSample(final WritableVersion wv, final Protocol protocol, final Element element)
        throws AccessException, ConstraintException {
        assert "outputSample".equals(element.getName());

        final String sampleCatName = element.getAttributeValue("sampleCatName");
        final SampleCategory sampleCategory = AbstractLoader.getSampleCategory(wv, sampleCatName);

        // Protocol.RefOutputSample
        // constraint: name of output sample should be mandatory
        final String outputSampleName = element.getAttributeValue("name");
        assert !"".equals(outputSampleName) : "ERROR: OutputSample must have a name";
        assert null != outputSampleName : "ERROR: OutputSample must have a name";
        final Map<String, Object> refOutputSampleAttrMap = new java.util.HashMap<String, Object>();
        refOutputSampleAttrMap.put(RefOutputSample.PROP_PROTOCOL, protocol);
        refOutputSampleAttrMap.put(RefOutputSample.PROP_SAMPLECATEGORY, sampleCategory);
        refOutputSampleAttrMap.put(RefOutputSample.PROP_NAME, outputSampleName);

        if ((null != element.getAttributeValue("amount"))
            && (!"".equals(element.getAttributeValue("amount")))) {
            final float amount = Float.valueOf(element.getAttributeValue("amount"));
            refOutputSampleAttrMap.put(RefOutputSample.PROP_AMOUNT, new Float(amount));
            // convert to litres
            if ("m3".equals(element.getAttributeValue("unit"))) {
                refOutputSampleAttrMap.put(RefOutputSample.PROP_AMOUNT, new Float(1000f * amount));
            }
        }
        // convert cubic meter to litres
        if ("m3".equals(element.getAttributeValue("unit"))) {
            refOutputSampleAttrMap.put(RefOutputSample.PROP_UNIT, "L");
        } else {
            refOutputSampleAttrMap.put(RefOutputSample.PROP_UNIT, element.getAttributeValue("unit"));
        }
        refOutputSampleAttrMap
            .put(RefOutputSample.PROP_DISPLAYUNIT, element.getAttributeValue("displayUnit"));

        final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
        keyAttrMap.put(RefOutputSample.PROP_PROTOCOL, protocol);
        keyAttrMap.put(RefOutputSample.PROP_NAME, outputSampleName);
        AbstractLoader.UpdateOrCreate(wv, RefOutputSample.class, keyAttrMap, null, refOutputSampleAttrMap);

    }

    /**
     * From protocol xml file: e.g. <inputSample sampleCatNamingSys="default" sampleCatName="Culture media"
     * amount="0.000025" unit="L" displayUnit="mL" name="Culture media" />
     * 
     * @param wv
     * @param protocol
     * @param element
     * @return the refInputSample
     * @throws AccessException
     * @throws ConstraintException
     */
    void processInputSample(final WritableVersion wv, final Protocol protocol, final Element element)
        throws AccessException, ConstraintException {
        assert "inputSample".equals(element.getName());

        // Sample.SampleCategory
        final String sampleCatName = element.getAttributeValue("sampleCatName");
        final SampleCategory sampleCategory = AbstractLoader.getSampleCategory(wv, sampleCatName);

        // Protocol.RefInputSampleSample
        // constraint: name of input sample should be mandatory
        final String inputSampleName = element.getAttributeValue("name");
        assert !"".equals(inputSampleName) : "ERROR: InputSample must have a name";
        assert null != inputSampleName : "ERROR: InputSample must have a name";
        final Map<String, Object> refInputSampleAttrMap = new java.util.HashMap<String, Object>();
        refInputSampleAttrMap.put(RefInputSample.PROP_PROTOCOL, protocol);
        refInputSampleAttrMap.put(RefInputSample.PROP_SAMPLECATEGORY, sampleCategory);
        refInputSampleAttrMap.put(RefInputSample.PROP_NAME, inputSampleName);

        if ((null != element.getAttributeValue("amount"))
            && (!"".equals(element.getAttributeValue("amount")))) {
            final float amount = Float.valueOf(element.getAttributeValue("amount"));
            refInputSampleAttrMap.put(RefInputSample.PROP_AMOUNT, new Float(amount));
            // convert to litres
            if ("m3".equals(element.getAttributeValue("unit"))) {
                refInputSampleAttrMap.put(RefInputSample.PROP_AMOUNT, new Float(1000f * amount));
            }
        }
        // convert cubic meter to litres
        if ("m3".equals(element.getAttributeValue("unit"))) {
            refInputSampleAttrMap.put(RefInputSample.PROP_UNIT, "L");
        } else {
            refInputSampleAttrMap.put(RefInputSample.PROP_UNIT, element.getAttributeValue("unit"));
        }
        final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
        keyAttrMap.put(RefOutputSample.PROP_PROTOCOL, protocol);
        keyAttrMap.put(RefOutputSample.PROP_NAME, inputSampleName);

        refInputSampleAttrMap.put(RefInputSample.PROP_DISPLAYUNIT, element.getAttributeValue("displayUnit"));
        AbstractLoader.UpdateOrCreate(wv, RefInputSample.class, keyAttrMap, null, refInputSampleAttrMap);

    }

    /**
     * e.g. <parameterDefinition id="1" name="Is the culture shaken?" parameterType="Boolean"
     * previousName="shaken?" defaultValue="true" possibleValues="Gel filtration,Affinity,Ion
     * Exchange,Hydrophobic Interaction" />
     * 
     * @param wv
     * @param protocol
     * @param element
     * @return the parameter definition
     * @throws AccessException
     * @throws ConstraintException
     */
    void processParameterDefinitions(final WritableVersion wv, final Protocol protocol, final Element element)
        throws AccessException, ConstraintException {
        // get new ParameterDefinition Element: name
        final Collection<String> newNames = new HashSet<String>();
        final Collection<String> previousNames = new HashSet<String>();
        for (final Iterator iter = element.getChildren().iterator(); iter.hasNext();) {
            final Element paramDefElement = (Element) iter.next();
            assert "parameterDefinition".equals(paramDefElement.getName());
            final String name = paramDefElement.getAttributeValue("name");
            newNames.add(name);
            if (paramDefElement.getAttributeValue("previousName") != null) {
                previousNames.addAll(Arrays.asList(paramDefElement.getAttributeValue("previousName").split(
                    ";")));
            }
            //a convenient way to update 'XXXX' to 'a. XXXX' or 'XXXX' to 'Aa. XXXX'
            if (name.length() > 4) {
                previousNames.add(name.subSequence(3, name.length()).toString());
                previousNames.add(name.subSequence(4, name.length()).toString());
            }
        }

        // get old ParameterDefinition:name
        final Map<String, ParameterDefinition> oldPDs = new HashMap<String, ParameterDefinition>();
        for (final ParameterDefinition pd : protocol.getParameterDefinitions()) {
            oldPDs.put(pd.getName(), pd);
        }

        for (final String oldName : oldPDs.keySet()) {
            if (!newNames.contains(oldName) && !previousNames.contains(oldName)) {
                ParameterDefinition oldPd = oldPDs.get(oldName);
                Map<String, Object> criteria = new HashMap();
                criteria.put(Parameter.PROP_PARAMETERDEFINITION, oldPd);
                if (0 == wv.count(Parameter.class, criteria)) {
                    oldPd.delete();// delete old ParameterDefinition which isn't in new's
                    AbstractLoader.print("-Delete old ParameterDefinition(" + oldName + ") for protocol("
                        + protocol.getName() + ")");
                } else {
                    AbstractLoader.print("-Unable to Delete old ParameterDefinition(" + oldName
                        + ") for protocol(" + protocol.getName() + ")");
                }
            }
        }
        for (final Iterator iter = element.getChildren().iterator(); iter.hasNext();) {
            final Element paramDefElement = (Element) iter.next();
            //new attributes
            assert "parameterDefinition".equals(paramDefElement.getName());
            final Map<String, Object> attrMap = new java.util.HashMap<String, Object>();
            attrMap.put(ParameterDefinition.PROP_NAME, paramDefElement.getAttributeValue("name"));
            attrMap.put(ParameterDefinition.PROP_LABEL, paramDefElement.getAttributeValue("label"));
            attrMap.put(ParameterDefinition.PROP_DISPLAYUNIT,
                paramDefElement.getAttributeValue("displayUnit"));
            attrMap.put(ParameterDefinition.PROP_ISMANDATORY, this.getBooleanValue(paramDefElement));
            attrMap.put(ParameterDefinition.PROP_PARAMTYPE,
                paramDefElement.getAttributeValue("parameterType"));
            attrMap.put(ParameterDefinition.PROP_DEFAULTVALUE,
                paramDefElement.getAttributeValue("defaultValue"));
            attrMap.put(ParameterDefinition.PROP_POSSIBLEVALUES, this.getPossibleValues(paramDefElement));
            attrMap.put(ParameterDefinition.PROP_PROTOCOL, protocol);

            final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();
            keyAttrMap.put(ParameterDefinition.PROP_PROTOCOL, protocol);
            final String newName = paramDefElement.getAttributeValue("name");
            keyAttrMap.put(ParameterDefinition.PROP_NAME, newName);

            final HashSet<String> oldNames = new HashSet<String>();
            oldNames.addAll(Arrays.asList(paramDefElement.getAttributeValue("previousName")));
            //a convenient way to update 'XXXX' to 'a. XXXX'
            if (newName.length() > 4) {
                oldNames.add(newName.subSequence(3, newName.length()).toString());
                oldNames.add(newName.subSequence(4, newName.length()).toString());
            }

            AbstractLoader.UpdateOrCreate(wv, ParameterDefinition.class, keyAttrMap, oldNames, attrMap);

        }
    }

    List<String> getPossibleValues(final Element paramDefElement) {
        if (paramDefElement == null || paramDefElement.getAttribute("possibleValues") == null) {
            return Collections.EMPTY_LIST;
        }
        String possibleValues = paramDefElement.getAttributeValue("possibleValues");
        possibleValues = possibleValues.replace(",", ";");//TODO remove this replace
        final List<String> possibleValuesList = Arrays.asList(possibleValues.split(";"));
        final List<String> improvedValuesList = new LinkedList<String>();
        for (final String string : possibleValuesList) {
            if (string.trim().length() > 0) {
                improvedValuesList.add(string.trim());
            }
        }
        return improvedValuesList;

    }

    private Boolean getBooleanValue(final Element paramDefElement) {

        if (paramDefElement == null || paramDefElement.getAttribute("isMandatory") == null) {
            return Boolean.TRUE;
        }
        if (paramDefElement.getAttributeValue("isMandatory").equals("false")) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static final String LEEDS_ORDER = "Leeds Order Primer Plate";

    String PIMS_ORDER = "Primer Order Plate";

    /**
     * <protocol name="PiMS Culture" objective="Growth of E.coli in liquid culture" creationDate="14-11-06"
     * remarks="Use appropriate selective medium and container" >
     * 
     * @param wv
     * @param element
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    @SuppressWarnings("unchecked")
    Protocol processProtocol(final Element element) throws AccessException, ConstraintException {
        assert "protocol".equals(element.getName());

        final ExperimentType experimentType =
            this.processExperimentType(this.wv, element.getChild("experimentType"));

        //Collection<Attribute> attributes = element.getAttributes();
        //System.out.println("element [" + element.getName() + ":" + experimentType.getName() + ":"
        //    + attributes.size() + "]");
        //for (Attribute attribute : attributes) {
        //    System.out.println("attribute [" + attribute.getName() + ":" + attribute.getValue() + "]");
        //}
        final String name = element.getAttributeValue("name");
        assert null != name : "ERROR: protocol must have a name";

        final Map<String, Object> protocolAttrMap = new java.util.HashMap<String, Object>();
        protocolAttrMap.put(Protocol.PROP_NAME, name);
        protocolAttrMap.put(Protocol.PROP_EXPERIMENTTYPE, experimentType);
        protocolAttrMap.put(Protocol.PROP_OBJECTIVE, element.getAttributeValue("objective"));

        final java.util.List<String> remarks = new ArrayList<String>();
        if (element.getAttributeValue("remarks") != null && element.getAttributeValue("remarks").length() > 0) {
            remarks.add(element.getAttributeValue("remarks"));
        }

        final String details = element.getAttributeValue("details");
        protocolAttrMap.put(Protocol.PROP_REMARKS, remarks);
        protocolAttrMap.put(LabBookEntry.PROP_DETAILS, details);
        // convert String into Date
        final Object dateValue =
            new java.text.SimpleDateFormat().parse(element.getAttributeValue("creationDate"),
                new ParsePosition(0));
        protocolAttrMap.put(LabBookEntry.PROP_CREATIONDATE, dateValue);

        final Map<String, Object> keyAttrMap = new java.util.HashMap<String, Object>();

        //a temp fix for v3.1
        if (name.equalsIgnoreCase(PIMS_ORDER)) {
            if (this.wv.findFirst(Protocol.class, Protocol.PROP_NAME, PIMS_ORDER) != null) {
                keyAttrMap.put(Protocol.PROP_NAME, PIMS_ORDER);
            } else {
                keyAttrMap.put(Protocol.PROP_NAME, ProtocolUtility.LEEDS_ORDER);
            }
        } else {
            keyAttrMap.put(Protocol.PROP_NAME, name);
        }

        final Protocol protocol =
            AbstractLoader.UpdateOrCreate(this.wv, Protocol.class, keyAttrMap, null, protocolAttrMap);
        //  PIMS-2151 Process output samples
        final Element outputSamplesElement = element.getChild("outputSamples");
        if (null != outputSamplesElement) {
            outputSampleProcess(outputSamplesElement, protocol);
        }

        outputSampleProcess(element, protocol);

        // Process input samples
        final Element inputSamples = element.getChild("inputSamples");
        if (null != inputSamples) {
            // print("Protocol has input samples");
            for (final Iterator iter = inputSamples.getChildren().iterator(); iter.hasNext();) {
                final Element inputSampleElement = (Element) iter.next();
                this.processInputSample(this.wv, protocol, inputSampleElement);
            }
        }

        // Process parameter definitions
        final Element parameterDefinitions = element.getChild("parameterDefinitions");
        if (null != parameterDefinitions) {
            this.processParameterDefinitions(this.wv, protocol, parameterDefinitions);

        }
        return protocol;

    }

    /**
     * ProtocolUtility.outputSampleProcess
     * 
     * @param element
     * @param protocol
     * @throws AccessException
     * @throws ConstraintException
     */
    private void outputSampleProcess(final Element element, final Protocol protocol) throws AccessException,
        ConstraintException {
        final Collection outputSamples = element.getChildren("outputSample");
        for (final Iterator iter = outputSamples.iterator(); iter.hasNext();) {
            final Element outputSampleElement = (Element) iter.next();
            this.processOutputSample(this.wv, protocol, outputSampleElement);
        }
    }

    /**
     * @param args a list of file names e.g. data/protocols/PIMS_Culture1.xml
     */
    public static void main(final String[] args) {
        if (args.length == 0) {
            AbstractLoader.print("Usage: java ProtocolUtility fileName ...");
            return;
        }
        //AbstractLoader.silent = false;
        final AbstractModel model = ModelImpl.getModel();
        String fileName = null;
        for (int i = 0; i < args.length; i++) {
            final WritableVersion wv = model.getWritableVersion(org.pimslims.access.Access.ADMINISTRATOR);
            try {
                fileName = args[i];
                final java.io.Reader reader = new java.io.FileReader(fileName);
                final SAXBuilder builder = new SAXBuilder();
                final ProtocolUtility pu = new ProtocolUtility(wv);
                final Document doc = builder.build(reader);
                pu.processProtocol(doc.getRootElement());
                wv.commit();
                AbstractLoader.print("processed: " + args[i]);
            } catch (final JDOMException e) { // indicates a well-formedness error
                AbstractLoader.print(args[0] + " is not well-formed.");
                AbstractLoader.print("Could not process: " + args[0]);
                AbstractLoader.print("Could not process: " + args[0]);
                e.printStackTrace();
                throw new RuntimeException("Error for file: " + fileName, e);
            } catch (final IOException e) {
                AbstractLoader.print("Could not process: " + args[0]);
                e.printStackTrace();
                throw new RuntimeException("Error for file: " + fileName, e);
            } catch (final ModelException e) {
                AbstractLoader.print("Could not process: " + args[0]);
                e.printStackTrace();
                throw new RuntimeException("Error for file: " + fileName, e);
            } catch (final AssertionError e) {
                AbstractLoader.print("Could not process: " + args[0]);
                e.printStackTrace();
                throw new RuntimeException("Error for file: " + fileName, e);
            } finally {
                if (!wv.isCompleted()) {
                    wv.abort();
                }
            }
        }
        AbstractLoader.print("Protocol utility has finished");
    }

    public Protocol loadFile(final Reader reader) throws JDOMException, IOException, AccessException,
        ConstraintException {
        //System.out.println("Processing protocol: " + filename);
        final SAXBuilder builder = new SAXBuilder();
        final Document doc = builder.build(reader);
        return this.processProtocol(doc.getRootElement());

    }

}
