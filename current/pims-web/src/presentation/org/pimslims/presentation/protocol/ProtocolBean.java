/**
 * 
 */
package org.pimslims.presentation.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;

/**
 * @author cm65
 * 
 */
public class ProtocolBean extends ModelObjectBean {

    private final ModelObjectBean experimentType;

    private final List<RefInputSampleBean> inputSamples;

    private final List<RefOutputSampleBean> outputSamples;

    private final List<ParameterDefinitionBean> parameters;

    private final List<ParameterDefinitionBean> setupParameters = new ArrayList<ParameterDefinitionBean>();

    private final List<ParameterDefinitionBean> resultParameters = new ArrayList<ParameterDefinitionBean>();

    private final int numberOfExperiments;

    /**
     * 
     */
    public ProtocolBean(final Protocol protocol) {
        super(protocol);
        this.removeValue(LabBookEntry.PROP_CREATIONDATE);
        this.removeValue(LabBookEntry.PROP_LASTEDITEDDATE);
        final Map<String, Object> criteria = new HashMap();
        criteria.put(Experiment.PROP_PROTOCOL, protocol);

        this.experimentType = BeanFactory.newBean(protocol.getExperimentType());

        this.numberOfExperiments = protocol.get_Version().count(Experiment.class, criteria);

        this.inputSamples = new ArrayList<RefInputSampleBean>(protocol.getRefInputSamples().size());
        for (final Iterator iter = protocol.getRefInputSamples().iterator(); iter.hasNext();) {
            final RefInputSample ris = (RefInputSample) iter.next();
            this.inputSamples.add(new RefInputSampleBean(ris));
        }
        Collections.sort(this.inputSamples);

        this.outputSamples = new ArrayList(protocol.getRefOutputSamples().size());
        for (final Iterator iter = protocol.getRefOutputSamples().iterator(); iter.hasNext();) {
            final RefOutputSample ros = (RefOutputSample) iter.next();
            this.outputSamples.add(new RefOutputSampleBean(ros));
        }
        Collections.sort(this.outputSamples);

        this.parameters = new ArrayList(protocol.getParameterDefinitions().size());
        for (final Iterator iter = protocol.getParameterDefinitions().iterator(); iter.hasNext();) {
            final ParameterDefinition parm = (ParameterDefinition) iter.next();
            this.parameters.add(new ParameterDefinitionBean(parm));
        }
        Collections.sort(this.parameters);

        for (final Iterator iter = protocol.getSetupParamDefinitions().iterator(); iter.hasNext();) {
            final ParameterDefinition parm = (ParameterDefinition) iter.next();
            this.setupParameters.add(new ParameterDefinitionBean(parm));
        }
        Collections.sort(this.setupParameters);

        for (final Iterator iter = protocol.getResultParamDefinitions().iterator(); iter.hasNext();) {
            final ParameterDefinition parm = (ParameterDefinition) iter.next();
            this.resultParameters.add(new ParameterDefinitionBean(parm));
        }
        Collections.sort(this.resultParameters);

    }

    /**
     * @return Returns the numberOfExperiments.
     */
    public int getNumberOfExperiments() {
        return this.numberOfExperiments;
    }

    public ModelObjectBean getExperimentType() {
        return this.experimentType;
    }

    public List<RefInputSampleBean> getInputSamples() {
        return new ArrayList(this.inputSamples);
    }

    public List<RefOutputSampleBean> getOutputSamples() {
        return new ArrayList(this.outputSamples);
    }

    public List<ParameterDefinitionBean> getParameterDefinitions() {
        return this.parameters;
    }

    public List<ParameterDefinitionBean> getResultParameterDefinitions() {
        return this.resultParameters;
    }

    public List<ParameterDefinitionBean> getSetupParameterDefinitions() {
        return this.setupParameters;
    }

    /**
     * ProtocolBean.getMayDelete
     * 
     * @see org.pimslims.presentation.ModelObjectShortBean#getMayDelete()
     */
    @Override
    public boolean getMayDelete() {
        return 0 == this.getNumberOfExperiments() && super.getMayDelete();
    }

}
