/**
 * pims-web org.pimslims.data ProtocolXmlBean.java
 * 
 * @author pajanne
 * @date Jan 12, 2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 pajanne The copyright holder has licenced the STFC to redistribute this
 *           software
 * 
 *           TODO this is insane. Add these annotations to the DM class instead.
 */
package org.pimslims.xmlio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.ExperimentType;

/**
 * ProtocolXmlBean
 * 
 */
@XmlRootElement(name = "protocol")
@XmlType(name = "", propOrder = { "name", "experimentType", "objective", "method", "remarks", "details",
    "creationDate", "isForUse", "exportDate", "parameterDefinitions", "refInputSamples", "refOutputSamples" })
public class ProtocolXmlBean {
    private String name;

    private String experimentType;

    private String objective;

    private String method;

    private String remarks;

    private String details;

    private Calendar creationDate;

    private Boolean isForUse = true;

    private Calendar exportDate = Calendar.getInstance();

    private List<ParameterDefinitionXmlBean> parameterDefinitions =
        new ArrayList<ParameterDefinitionXmlBean>(0);

    private List<RefSampleXmlBean> refInputSamples = new ArrayList<RefSampleXmlBean>(0);

    private List<RefSampleXmlBean> refOutputSamples = new ArrayList<RefSampleXmlBean>(0);

    /**
     * Constructor for ProtocolXmlBean
     * 
     * @param org.pimslims.model.protocol.Protocol
     */
    public ProtocolXmlBean(final Protocol protocol) {
        this.name = protocol.getName();
        this.experimentType = protocol.getExperimentType().getName();
        this.objective = protocol.getObjective();
        this.method = protocol.getMethodDescription();
        this.remarks = "";
        if (protocol.getRemarks().size() != 0) {
            for (final String remark : protocol.getRemarks()) {
                this.remarks.concat(remark).concat(" ");
            }
        }
        this.details = protocol.getDetails();
        this.creationDate = protocol.getCreationDate();
        this.isForUse = protocol.getIsForUse();
        if (protocol.getParameterDefinitions().size() != 0) {
            for (final ParameterDefinition paramDef : protocol.getParameterDefinitions()) {
                this.parameterDefinitions.add(new ParameterDefinitionXmlBean(paramDef));
            }
        }
        if (protocol.getRefOutputSamples().size() != 0) {
            for (final RefOutputSample refOutputSample : protocol.getRefOutputSamples()) {
                this.refOutputSamples.add(new RefSampleXmlBean(refOutputSample));
            }
        }
        if (protocol.getRefInputSamples().size() != 0) {
            for (final RefInputSample refInputSample : protocol.getRefInputSamples()) {
                this.refInputSamples.add(new RefSampleXmlBean(refInputSample));
            }
        }
    }

    /**
     * Constructor for ProtocolXmlBean
     * 
     * @param name
     */
    public ProtocolXmlBean(final String name) {
        this.name = name;
    }

    // Required by jaxb
    private ProtocolXmlBean() {
        // Required by jaxb
    }

    /**
     * @return Returns the name.
     */
    @XmlElement
    public String getName() {
        return this.name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return Returns the experimentType.
     */
    @XmlElement
    public String getExperimentType() {
        return this.experimentType;
    }

    /**
     * @param experimentType The experimentType to set.
     */
    public void setExperimentType(final String experimentType) {
        this.experimentType = experimentType;
    }

    /**
     * @return Returns the objective.
     */
    @XmlElement
    public String getObjective() {
        return this.objective;
    }

    /**
     * @param objective The objective to set.
     */
    public void setObjective(final String objective) {
        this.objective = objective;
    }

    /**
     * @return Returns the method.
     */
    @XmlElement
    public String getMethod() {
        return this.method;
    }

    /**
     * @param method The method to set.
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * @return Returns the remarks.
     */
    @XmlElement
    public String getRemarks() {
        return this.remarks;
    }

    /**
     * @param remarks The remarks to set.
     */
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return Returns the details.
     */
    @XmlElement
    public String getDetails() {
        return this.details;
    }

    /**
     * @param details The details to set.
     */
    public void setDetails(final String details) {
        this.details = details;
    }

    /**
     * @return Returns the creationDate.
     */
    @XmlElement
    public Calendar getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(final Calendar creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the isForUse.
     */
    @XmlElement
    public Boolean getIsForUse() {
        return this.isForUse;
    }

    /**
     * @param isForUse The isForUse to set.
     */
    public void setIsForUse(final Boolean isForUse) {
        this.isForUse = isForUse;
    }

    /**
     * @param exportDate The exportDate to set.
     */
    @XmlElement
    public void setExportDate(final Calendar exportDate) {
        this.exportDate = exportDate;
    }

    /**
     * @return Returns the exportDate.
     */
    public Calendar getExportDate() {
        return this.exportDate;
    }

    /**
     * @param parameterDefinitions The parameterDefinitions to set.
     */
    @XmlElementWrapper(name = "parameterDefinitions")
    @XmlElement(name = "parameterDefinition")
    public void setParameterDefinitions(final List<ParameterDefinitionXmlBean> parameterDefinitions) {
        this.parameterDefinitions = parameterDefinitions;
    }

    /**
     * @return Returns the parameterDefinitions.
     */
    public List<ParameterDefinitionXmlBean> getParameterDefinitions() {
        return this.parameterDefinitions;
    }

    /**
     * @param refInputSamples The refInputSamples to set.
     */
    @XmlElementWrapper(name = "inputSamples")
    @XmlElement(name = "inputSample")
    public void setRefInputSamples(final List<RefSampleXmlBean> refInputSamples) {
        this.refInputSamples = refInputSamples;
    }

    /**
     * @return Returns the refInputSamples.
     */
    public List<RefSampleXmlBean> getRefInputSamples() {
        return this.refInputSamples;
    }

    /**
     * @param refOutputSamples The refOutputSamples to set.
     */
    @XmlElementWrapper(name = "outputSamples")
    @XmlElement(name = "outputSample")
    public void setRefOutputSamples(final List<RefSampleXmlBean> refOutputSamples) {
        this.refOutputSamples = refOutputSamples;
    }

    /**
     * @return Returns the refOutputSamples.
     */
    public List<RefSampleXmlBean> getRefOutputSamples() {
        return this.refOutputSamples;
    }

    public Protocol getProtocol(final WritableVersion version) throws ConstraintException, AccessException {
        final Map<String, Object> attrMap = new HashMap<String, Object>();
        attrMap.put(Protocol.PROP_NAME, this.name);
        attrMap.put(Protocol.PROP_EXPERIMENTTYPE, this.getExperimentType(version));
        attrMap.put(Protocol.PROP_OBJECTIVE, this.objective);
        attrMap.put(Protocol.PROP_METHODDESCRIPTION, this.method);
        attrMap.put(Protocol.PROP_REMARKS, Collections.singletonList(this.remarks));
        attrMap.put(LabBookEntry.PROP_DETAILS, this.details);
        attrMap.put(LabBookEntry.PROP_CREATIONDATE, this.creationDate);
        attrMap.put(Protocol.PROP_ISFORUSE, this.isForUse);
        final Protocol protocol = new Protocol(version, attrMap);
        if (this.parameterDefinitions.size() != 0) {
            for (final ParameterDefinitionXmlBean paramDefXmlBean : this.parameterDefinitions) {
                final Map<String, Object> paramDefAttrMap = paramDefXmlBean.getAttributeMap();
                paramDefAttrMap.put(ParameterDefinition.PROP_PROTOCOL, protocol);
                new ParameterDefinition(version, paramDefAttrMap);
            }
        }
        if (this.refOutputSamples.size() != 0) {
            for (final RefSampleXmlBean refOutputSampleXmlBean : this.refOutputSamples) {
                final Map<String, Object> outSampleAttrMap = refOutputSampleXmlBean.getAttributeMap(version);
                outSampleAttrMap.put(RefOutputSample.PROP_PROTOCOL, protocol);
                new RefOutputSample(version, outSampleAttrMap);
            }
        }
        if (this.refInputSamples.size() != 0) {
            for (final RefSampleXmlBean refInputSampleXmlBean : this.refInputSamples) {
                final Map<String, Object> inSampleAttrMap = refInputSampleXmlBean.getAttributeMap(version);
                inSampleAttrMap.put(RefInputSample.PROP_PROTOCOL, protocol);
                new RefInputSample(version, inSampleAttrMap);
            }
        }
        return protocol;
    }

    /**
     * Returns the associated experiment type model object from a string. It should already exist within the
     * db, and be unique otherwise throws an error.
     * 
     * @param version
     * @return the associated experiment type
     * @throws AccessException
     * @throws ConstraintException
     */
    private ExperimentType getExperimentType(final WritableVersion version) throws AccessException,
        ConstraintException {
        final Collection<ExperimentType> expTypesFound =
            version.findAll(ExperimentType.class, ExperimentType.PROP_NAME, this.experimentType);
        if (0 == expTypesFound.size()) {
            throw new RuntimeException("ERROR: ExperimentType [" + this.experimentType
                + "] must exist (loaded as reference)");
        } else if (1 == expTypesFound.size()) {
            return expTypesFound.iterator().next();
        } else {
            throw new RuntimeException("ERROR: ExperimentType [" + this.experimentType
                + "] is duplicated (reference must be unique)");
        }
    }

}
