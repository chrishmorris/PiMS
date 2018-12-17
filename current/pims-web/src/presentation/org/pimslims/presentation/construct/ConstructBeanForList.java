/**
 * V4_3-web org.pimslims.presentation.construct ConstructBeanForList.java
 * 
 * @author cm65
 * @date 6 Jan 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation.construct;

import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PrimerBean;
import org.pimslims.presentation.TargetBean;

/**
 * ConstructBeanForList
 * 
 */
public class ConstructBeanForList extends ModelObjectBean {

    public static final String PROTEIN = "Protein";

    protected final TargetBean targetBean;

    protected final PrimerBean forwardPrimerBean;

    protected final PrimerBean reversePrimerBean;

    protected Milestone latestMilestone;

    /**
     * Constructor for ConstructBeanForList
     * 
     * @param tb
     * @param forward
     * @param reverse
     */
    public ConstructBeanForList(final TargetBean tb, final PrimerBean forward, final PrimerBean reverse) {
        this.targetBean = tb;
        this.forwardPrimerBean = forward;
        this.reversePrimerBean = reverse;
    }

    /**
     * Constructor for ConstructBeanForList
     * 
     * @param ro
     * @param tb
     * @param forward
     * @param reverse
     */
    public ConstructBeanForList(final ResearchObjective ro, final TargetBean tb, final PrimerBean forward,
        final PrimerBean reverse) {
        super(ro);
        this.targetBean = tb;
        this.forwardPrimerBean = forward;
        this.reversePrimerBean = reverse;
    }

    public PrimerBean getFwdPrimerBean() {
        return this.forwardPrimerBean;
    }

    public PrimerBean getRevPrimerBean() {
        return this.reversePrimerBean;
    }

    public TargetBean getTargetBean() {
        return this.targetBean;
    }

    public ModelObjectBean getLatestMilestoneExperiment() {
        if (null == this.latestMilestone || null == this.latestMilestone.getExperiment()) {
            return null;
        }
        return BeanFactory.newBean(this.latestMilestone.getExperiment());
    }

}
