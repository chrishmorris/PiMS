/**
 * 
 */
package org.pimslims.business.crystallization.model;

import org.pimslims.business.core.model.Person;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jon Diprose
 */
// JMD Used
public class PlateExperimentInfo {
    
    private String name = null;
    private String protocolName = null;
    private Calendar runAt = null;
    private Person operator = null;
    private String details = null;
    private String instrument = null;
    private String expBlueprintName = null;
    
    // TODO org.pimslims.crystallization.model.Group
    private String groupName = null;
    
    // TODO <String, Object>
    private final Map<String, String> parameters = new HashMap<String, String>();
    
    
    
    /**
     * Constructor for PlateExperimentInfo 
     */
    public PlateExperimentInfo() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }
    
    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }
    
    /**
     * @return the instrument
     */
    public String getInstrument() {
        return instrument;
    }
    
    /**
     * @param instrument the instrument to set
     */
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    /**
     * @return the protocolName
     */
    public String getProtocolName() {
        return protocolName;
    }
    
    /**
     * @param protocolName the protocolName to set
     */
    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }
    
    /**
     * @return the runAt
     */
    public Calendar getRunAt() {
        return runAt;
    }
    
    /**
     * @param runAt the runAt to set
     */
    public void setRunAt(Calendar runAt) {
        this.runAt = runAt;
    }
    
    /**
     * @return the operator
     */
    public Person getOperator() {
        return operator;
    }
    
    /**
     * @param operator the operator to set
     */
    public void setOperator(Person operator) {
        this.operator = operator;
    }
    
    /**
     * <p>Get a parameter by name.</p>
     * 
     * @param name
     * @return
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * <p>Set a parameter by name.</p>
     * 
     * @param name
     * @return
     */
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }
    
    /**
     * @return the expBlueprintName
     */
    public String getExpBlueprintName() {
        return expBlueprintName;
    }
    
    /**
     * @param expBlueprintName the expBlueprintName to set
     */
    public void setExpBlueprintName(String expBlueprintName) {
        this.expBlueprintName = expBlueprintName;
    }
    
    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }
    
    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
}
