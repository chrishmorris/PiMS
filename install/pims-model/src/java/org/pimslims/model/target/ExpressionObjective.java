/**
 * pims-model org.pimslims.model.target ExpressionObjective.java
 * 
 * @author cm65
 * @date 6 Sep 2011
 * 
 * Protein Information Management System
 * @version: 3.0
 * 
 * Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.model.target;

import java.util.Collection;

/**
 * ExpressionObjective
 * 
 * Something that a scientist has decided to make. Usually one or more ORFs, possibly truncated, possibily
 * with mutations. Designing the expression objective is the first stage in construct design.
 * 
 * At present, expression objectives are recorded in the ResearchObjective table.
 * 
 */
public interface ExpressionObjective {

    public abstract String getName();

    public abstract void removeExpressionObjectiveElement(
        final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException;

    public abstract void addExpressionObjectiveElement(
        final ResearchObjectiveElement expressionObjectiveElement)
        throws org.pimslims.exception.ConstraintException;

    public abstract void setExpressionObjectiveElements(
        final java.util.Collection<ResearchObjectiveElement> expressionObjectiveElements)
        throws org.pimslims.exception.ConstraintException;

    public abstract Collection<ResearchObjectiveElement> getExpressionObjectiveElements();

    /**
     * ExpressionObjective.getProject
     * 
     * @return
     */
    public abstract org.pimslims.model.experiment.Project getProject();

}
