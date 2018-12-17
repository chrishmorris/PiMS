/**
 * current-pims-web org.pimslims.lab.experiment ExperimentFieldsEncoder.java
 * 
 * @author pvt43
 * @date 3 Jun 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 pvt43 *
 * 
 */
package org.pimslims.utils.experiment;

import java.util.HashMap;
import java.util.Map;

import org.pimslims.lab.Util;

/**
 * ExperimentFieldsDecoder
 * 
 * Please for examples of this class use @see {@link ExperimentWizardFieldsDecoderTester}
 */

public class ExperimentWizardFieldsDecoder {

    Map<String, String> fields;

    Enum sampleRecPolicy;

    /**
     * Define the behavior of wizard creator in respect of sample recording
     * 
     * always_create_sample - modify sample name if exists, add sample name if not supplied
     * 
     * not_create_if_name_exists - do not create if name supplied and exists (still can add generated name if
     * not supplied)
     * 
     * never_alter_data - do not validate use only what has been supplied
     * 
     */
    public static enum SampleRecordingBehavior {
        always_create_sample, not_create_if_name_exists, never_alter_data
    }

    /**
     * This is normal constructor
     */
    public ExperimentWizardFieldsDecoder(final Map<String, String[]> prop) {
        this.fields = ExperimentWizardFieldsDecoder.getWizardParams(prop);
    }

    /**
     * 
     * @param prop
     */
    public ExperimentWizardFieldsDecoder(final HashMap<String, String> prop) {
        this.fields = prop;
    }

    public static HashMap<String, String> getWizardParams(final Map<String, String[]> prop) {
        final HashMap<String, String> wizFields = new HashMap<String, String>();
        for (final String key : prop.keySet()) {
            if (ExperimentWizardFieldsDecoder.isValidWizardField(key)) {
                if (prop.get(key).length > 1) {
                    System.out.println("WARNING-Property " + key
                        + " has more than one value! This is not supported. ");
                    continue; // Ignore multi valued fields for now.
                }
                final String val = prop.get(key)[0];
                //System.out.println("KEY " + key + " val:: " + val);
                wizFields.put(key, val);
            }
        }
        return wizFields;
    }

    public Enum getSampleRecordingPolicy() {
        if (this.sampleRecPolicy == null) {
            return SampleRecordingBehavior.always_create_sample;
        }
        return this.sampleRecPolicy;
    }

    public void setSampleRecordingPolicy(final Enum policy) {
        this.sampleRecPolicy = policy;
    }

    /**
     * Request: Experiment.PROP_NAME Result: e.1,P:12234,Experiment.PROP_NAME
     * 
     * @param shortKey
     */
    String getExpFullKey(final String shortKey) {
        for (final String key : this.fields.keySet()) {
            final String[] kparts = key.split(",");
            assert kparts.length >= 2;
            final String k = kparts[2];
            if (k.endsWith(shortKey)) {
                return kparts[0] + "," + kparts[1] + "," + kparts[2];
            }
        }
        return null;
    }

    /**
     * Request: Experiment.PROP_NAME Result: e.1,P:12234,Experiment.PROP_NAME
     * 
     * @param shortKey
     */
    public String getExpFieldValue(final String shortKey) {
        final String fkey = this.getExpFullKey(shortKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    static boolean isValidWizardField(final String fieldName) {
        if (Util.isEmpty(fieldName)) {
            return false;
        }
        if (!fieldName.startsWith("E")) {
            return false;
        }
        if (fieldName.split(",").length <= 2) {
            return false;
        }
        return true;
    }

    /**
     * Request: Medium,Sample.PROP or Medium,Sample.PROP_DETAILS
     * 
     * Result: e.1,P:12234,I.Medium,Sample.PROP
     * 
     * @param shortKey = sampleName (equals to RefInput or RefOutput Sample names)
     * 
     *            sampleName = Sample.PROP_XXX or Sample.PROP for the sample itself
     * 
     *            name: 20 x NPSC pname: amountDisplayUnit
     * 
     *            name: 20 x NPSC pname: amount
     * 
     *            name: 20 x NPSC pname: name
     * 
     *            name: 20 x NPSC pname: currentAmount
     */

    private String getFullSampleKey(final Enum code, final String sampleKey) {
        for (final String key : this.fields.keySet()) {
            if (key.endsWith(code.name() + "." + sampleKey)) {
                return key;
            }
        }
        return null;

    }

    /**
     * Request: Medium,Sample.PROP or Medium,Sample.PROP_DETAILS
     * 
     * Result: e.1,P:12234,I.Medium,Sample.PROP
     * 
     * @param shortKey = sampleName (equals to RefInput or RefOutput Sample names)
     * 
     */
    String getSampleFullKey(final String sampleKey) {
        return this.getFullSampleKey(code.S, sampleKey);
    }

    String getRefSampleFullKey(final String sampleKey) {
        return this.getFullSampleKey(code.RS, sampleKey);
    }

    String getSampleFullKey(final String sampleName, final String sampleProperty) {
        return this.getSampleFullKey(sampleName + "," + sampleProperty);
    }

    public String getSampleComponentFieldValue(final String sampleShortKey) {
        final String fkey = this.getSampleComponentFullKey(sampleShortKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    public String getSampleComponentFieldValue(final String samplePartKey, final String propertyName) {
        return this.getSampleComponentFieldValue(samplePartKey + "," + propertyName);
    }

    String getSampleComponentFullKey(final String sampleKey) {
        return this.getFullSampleKey(code.C, sampleKey);
    }

    /**
     * Request: Medium,Sample.PROP or Medium,Sample.PROP_DETAILS
     * 
     * Result: e.1,P:12234,I.Medium,Sample.PROP
     * 
     * @param shortKey = sampleName (equals to RefInput or RefOutput Sample names)
     * 
     */
    String getInputSampleFullKey(final String inSampleKey) {
        return this.getFullSampleKey(code.I, inSampleKey);
    }

    String getOutputSampleFullKey(final String outSampleKey) {
        return this.getFullSampleKey(code.O, outSampleKey);
    }

    public String getInputSampleFieldValue(final String inSampleKey) {
        final String fkey = this.getInputSampleFullKey(inSampleKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    public String getInputSampleFieldValue(final String inSamplePartKey, final String propertyName) {
        return this.getInputSampleFieldValue(inSamplePartKey + "," + propertyName);
    }

    public String getOutputSampleFieldValue(final String outSamplePartKey, final String propertyName) {
        return this.getOutputSampleFieldValue(outSamplePartKey + "," + propertyName);
    }

    public String getOutputSampleFieldValue(final String outSampleKey) {
        final String fkey = this.getOutputSampleFullKey(outSampleKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    public String getSampleFieldValue(final String samplePartKey, final String propertyName) {
        return this.getSampleFieldValue(samplePartKey + "," + propertyName);
    }

    public String getRefSampleFieldValue(final String samplePartKey, final String propertyName) {
        return this.getRefSampleFieldValue(samplePartKey + "," + propertyName);
    }

    public String getSampleFieldValue(final String sampleShortKey) {
        final String fkey = this.getSampleFullKey(sampleShortKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    public String getRefSampleFieldValue(final String refSampleShortKey) {
        final String fkey = this.getRefSampleFullKey(refSampleShortKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    /**
     * Request: P.Speed
     * 
     * Result: e.1,P:12234,P.Speed
     * 
     * @param shortKey - simply Parameter name = (ParameterDefinition.name)
     */
    String getParameterFullKey(final String shortKey) {
        for (final String key : this.fields.keySet()) {
            if (key.endsWith(shortKey)) {
                return key;
            }
        }
        return null;
    }

    public String getParameterFieldValue(final String shortKey) {
        final String fkey = this.getParameterFullKey(shortKey);
        if (fkey != null) {
            return this.fields.get(fkey);
        }
        return null;
    }

    public Map<String, String> getPropertiesMap() {
        return this.fields;
    }

    private Map<String, String> getAllSampleProps(final Enum type, final String refSname) {
        final HashMap<String, String> sampleProps = new HashMap<String, String>();
        for (final String key : this.fields.keySet()) {
            if (key.contains("," + type + "." + refSname)) {
                if (key.split(",").length < 4) {
                    // Contain only hook or name of a sample, not other parameters
                    continue;
                }
                sampleProps.put(key.split(",")[3], this.fields.get(key));
            }
        }
        return sampleProps;
    }

    public Map<String, String> getAllSampleProps(final String refSname) {
        return this.getAllSampleProps(code.S, refSname);
    }

    public Map<String, String> getAllRefSampleProps(final String refSname) {
        return this.getAllSampleProps(code.RS, refSname);
    }

    public Map<String, String> getAllSampleComponentProps(final String refSname) {
        return this.getAllSampleProps(code.C, refSname);
    }

    public Map<String, String> getAllInputSampleProps(final String refSname) {
        return this.getAllSampleProps(code.I, refSname);
    }

    public Map<String, String> getAllOutputSampleProps(final String refSname) {
        return this.getAllSampleProps(code.O, refSname);
    }

    /**
     * P - parameters
     * 
     * I - InputSamples
     * 
     * S - Samples for Input/Output
     * 
     * C - SampleComponent for sample for input/output
     * 
     * O - OutputSample code
     * 
     * RS - Reference Sample (RefSample)
     * 
     * Please note that OC is not supported by ExperimentWizardCreator
     */
    public enum code {
        P, I, S, C, O, RS
    }

    /**
     * Returns a Parameters/Input/Output subset(s) of sent properties. Shorten the keys to parameter names
     * 
     * @return
     */
    Map<String, String> getPrmsSubset(final Enum code) {
        final Map<String, String> params = new HashMap<String, String>();
        for (final String key : this.fields.keySet()) {
            final String ppart = key.split(",")[2];
            if (ppart.startsWith(code.name() + ".")) {
                params.put(ppart.substring(code.name().length() + 1), this.fields.get(key));
            }
        }
        return params;
    }

    public static String getPropertyPrefix(final String expPart, final String protocolHook) {
        return expPart + "," + protocolHook + ",";
    }

}
