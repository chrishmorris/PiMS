/**
 * pims-model org.pimslims.model.experiment Project.java
 * 
 * @author cm65
 * @date 6 Sep 2011
 * 
 *       Protein Information Management System
 * @version: 3.0
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.experiment;

import java.util.Collection;
import java.util.Set;

import org.pimslims.exception.ConstraintException;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.protocol.Workflow;
import org.pimslims.model.target.ExpressionObjective;

/**
 * Project
 * 
 * A group of experiments, coducted for a common purpose. These might be all the experiments for a specific
 * construct. Alternatively, they might be all the experiments for a specific client purchase order.
 * 
 */
public interface Project {

    /**
     * Project.getClientCostCode
     * 
     * @return purchase order id; reference number used by the customers' accounting system
     */
    public String getClientCostCode();

    /**
     * Project.setClientCostCode
     * 
     * @param clientCostCode purchase order id; reference number used by the customers' accounting system
     * @throws ConstraintException
     */
    public void setClientCostCode(String clientCostCode) throws ConstraintException;

    public abstract String getName();

    public abstract void removeExperiment(final Experiment experiment)
        throws org.pimslims.exception.ConstraintException;

    public abstract void addExperiment(final Experiment experiment)
        throws org.pimslims.exception.ConstraintException;

    public abstract void setExperiments(final java.util.Collection<Experiment> experiments)
        throws org.pimslims.exception.ConstraintException;

    public abstract Collection<Experiment> getExperiments();

    public abstract void setOwner(final User owner) throws org.pimslims.exception.ConstraintException;

    public abstract User getOwner();

    /**
     * Project.getExpressionObjectives
     * 
     * @return
     */
    public Collection<ExpressionObjective> getExpressionObjectives();

    public Set<Workflow> getWorkflows();

    public void setWorkflows(final java.util.Collection<Workflow> workflows) throws ConstraintException;

    public void addWorkflow(final Workflow workflow) throws ConstraintException;

    public void removeWorkflow(final Workflow workflow) throws ConstraintException;

}
