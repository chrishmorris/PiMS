package org.pimslims.utils;

@Deprecated
public class PIMSDM {
    public static final String METACLASSNAME_EXPERIMENT = org.pimslims.model.experiment.Experiment.class
        .getName();

    public static final String METACLASSNAME_PROTOCOL = org.pimslims.model.protocol.Protocol.class.getName();

    @Deprecated
    // target.project is no longer used 
    public static final String METACLASSNAME_PERSON = org.pimslims.model.people.Person.class.getName();

    // public static final String
    // METACLASSNAME_PARAMETER=org.pimslims.model.experiment.Parameter.class.getName();
    public static final String METACLASSNAME_PARAMETERDEFINITION =
        org.pimslims.model.protocol.ParameterDefinition.class.getName();

    public static final String METACLASSNAME_INPUTSAMPLE = org.pimslims.model.experiment.InputSample.class
        .getName();

    // public static final String
    // METACLASSNAME_OUTPUTSAMPLE=org.pimslims.model.experiment.OutputSample.class.getName();
    public static final String METACLASSNAME_INPUTSAMPLEDEF =
        org.pimslims.model.protocol.RefInputSample.class.getName();

    public static final String METACLASSNAME_OUTPUTSAMPLEDEF =
        org.pimslims.model.protocol.RefOutputSample.class.getName();

    public static final String METACLASSNAME_ABSTRACTSAMPLE = org.pimslims.model.sample.AbstractSample.class
        .getName();

    public static final String METACLASSNAME_SAMPLE = org.pimslims.model.sample.Sample.class.getName();

    // public static final String METACLASSNAME_REFSAMPLE =
    // org.pimslims.model.sample.RefSample.class.getName();

    // public static final String CCP_API_PROTOCOL_EXPTYPE_ROLE_EXPERIMENTS =
    // "experiments";

    // public static final String
    // CCP_API_PROTOCOL_PROTOCOL_ROLE_OUTPUTSAMPLEDEFS="outputSampleDefs" ;

    // public static final String
    // CCP_API_EXPERIMENT_EXPERIMENT="org.pimslims.model.experiment.Experiment";

    // public static final String
    // CCP_API_EXPERIMENT_EXPERIMENT_PROP_NAME="name";
    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_DETAILS = "details";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_STARTDATE = "startDate";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_ENDDATE = "endDate";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_LASTEDITEDDATE = "lastEditedDate";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_QUALIFIEDNAME = "qualifiedName";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_PROP_STATUS = "status";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_PROTOCOL = "protocol";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_EXPERIMENTTYPE = "experimentType";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_INSTRUMENT = "instrument";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_BLUEPRINTSTATUSS = "blueprintStatuses";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_PREVIOUS = "previous";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_OUTPUTSAMPLES = "outputSamples";

    // public static final String
    // CCP_API_EXPERIMENT_EXPERIMENT_ROLE_INPUTSAMPLES="inputSamples";
    //public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_PARAMERERS = "parameters";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_CREATOR = "creator";

    public static final String CCP_API_EXPERIMENT_EXPERIMENT_ROLE_SAMPLEIOS = "sampleIos";

    public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_ROLE_SAMPLE = "sample";

    // public static final String
    // CCP_API_EXPERIMENT_INPUTSAMPLE_ROLE_INPUTSAMPLEDEF="inputSampleDef";
    //public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_ROLE_EXPERIMENT = "experiment";

    public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_PROP_NAME = "name";

    public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_PROP_AMOUNT = "amount";

    public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_PROP_AMOUNTUNIT = "amountUnit";

    public static final String CCP_API_EXPERIMENT_OUTPUTSAMPLE_ROLE_SAMPLE = "sample";

    // public static final String
    // CCP_API_EXPERIMENT_OUTPUTSAMPLE_ROLE_OUTPUTSAMPLEDEF="outputSampleDef";
    //public static final String CCP_API_EXPERIMENT_OUTPUTSAMPLE_ROLE_EXPERIMENT = "experiment";

    public static final String CCP_API_EXPERIMENT_OUTPUTSAMPLE_PROP_NAME = "name";

    public static final String CCP_API_EXPERIMENT_OUTPUTSAMPLE_PROP_AMOUNT = "amount";

    public static final String CCP_API_EXPERIMENT_OUTPUTSAMPLE_PROP_AMOUNTUNIT = "amountUnit";

    public static final String CCP_API_PROTOCOL_INPUTSAMPLEDEF_ROLE_PROTOCOL = "protocol";

    public static final String CCP_API_PROTOCOL_INPUTSAMPLEDEF_ROLE_SAMPLECATEGORY = "sampleCategory";

    public static final String CCP_API_PROTOCOL_INPUTSAMPLEDEF_PROP_NAME = "name";

    public static final String CCP_API_PROTOCOL_INPUTSAMPLEDEF_PROP_AMOUNT = "amount";

    public static final String CCP_API_PROTOCOL_INPUTSAMPLEDEF_PROP_UNIT = "unit";

    public static final String CCP_API_PROTOCOL_OUTPUTSAMPLEDEF_ROLE_PROTOCOL = "protocol";

    public static final String CCP_API_PROTOCOL_OUTPUTSAMPLEDEF_ROLE_SAMPLECATEGORY = "sampleCategory";

    public static final String CCP_API_PROTOCOL_OUTPUTSAMPLEDEF_PROP_NAME = "name";

    public static final String CCP_API_PROTOCOL_OUTPUTSAMPLEDEF_PROP_AMOUNT = "amount";

    public static final String CCP_API_PROTOCOL_OUTPUTSAMPLEDEF_PROP_UNIT = "unit";

    public static final String CCP_API_EXPERIMENT_PARAMETER_PROP_VALUE = "value";

    // public static final String
    // CCP_API_EXPERIMENT_PARAMETER_ROLE_PARAMETERDEFINITION
    // ="parameterDefinition";
    // public static final String CCP_API_EXPERIMENT_PARAMETER_ROLE_EXPERIMENT =
    // "experiment";

    public static final String CCP_API_EXPERIMENT_INPUTSAMPLE_ROLE_CONFORMTO = "conformTo";

    // public static final String
    // CCP_API_SAMPLE_SAMPLE_ROLE_SAMPLECATEGORIES="sampleCategories";
    // public static final String CCP_API_SAMPLE_SAMPLE_PROP_NAME="name";
    public static final String CCP_API_SAMPLE_SAMPLECATEGORY_PROP_NAME = "name";

    public static final String PARAMETERDEFINITION_PARAMTYPE_INT = "Int";

    public static final String PARAMETERDEFINITION_PARAMTYPE_FLOAT = "Float";

    public static final String PARAMETERDEFINITION_PARAMTYPE_DOUBLE = "Double";

    public static final String PARAMETERDEFINITION_PARAMTYPE_LONG = "Long";

    public static final String PARAMETERDEFINITION_PARAMTYPE_STRING = "String";

    public static final String PARAMETERDEFINITION_PARAMTYPE_BOOLEAN = "Boolean";

    public static final String PARAMETERDEFINITION_PARAMTYPE_FILENAME = "Filename";

    public static final String PARAMETERDEFINITION_PARAMTYPE_DATETIME = "DateTime";

    public static final String PARAMETERDEFINITION_PARAMTYPE_INTERVAL = "Interval";

    public static final String PARAMETERDEFINITION_PARAMTYPE_INPUTSAMPLE = "InputSample";

    public static final String PARAMETERDEFINITION_PARAMTYPE_INTVALUE = "Int";

}
