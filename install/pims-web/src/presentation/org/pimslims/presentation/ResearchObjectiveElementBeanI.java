/**
 * V4_3-web org.pimslims.presentation BlueprintComponentBeanI.java
 * 
 * @author cm65
 * @date Sep 4, 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

/**
 * BlueprintComponentBeanI
 * 
 */
public interface ResearchObjectiveElementBeanI extends Comparable<Object> {

    public abstract String getHook();

    public abstract String getTargetName();

    public abstract String getWhyChosen();

    public abstract String getProteinName();

    public abstract String getShortWhyChosen();

    public abstract String getStart();

    public abstract String getEnd();

    /**
     * BlueprintComponentBeanI.getTargetHook
     * 
     * @return
     */
    public abstract String getTargetHook();

}