package org.pimslims.presentation.leeds;

import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Target;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         TargetConstructsBean
 * 
 */
public class TargetConstructsBean {
    String commonName;

    String localName;

    String constructName;

    String experimentHook;

    String targetHook;

    public TargetConstructsBean(final Target target, final Experiment experiment) {
        assert target != null && experiment != null : "Target or experiment is null. Cannot make a bean";
        this.commonName = target.getName();
        this.localName = target.getName();
        this.constructName = experiment.getName();
        this.targetHook = target.get_Hook();
        this.experimentHook = experiment.get_Hook();
    }

    /**
     * @return the commonName
     */
    public final String getCommonName() {
        return this.commonName;
    }

    /**
     * @return the constructName
     */
    public final String getConstructName() {
        return this.constructName;
    }

    /**
     * @return the localName
     */
    public final String getLocalName() {
        return this.localName;
    }

    /**
     * @return the target hook
     */
    public final String getTargetHook() {
        return this.targetHook;
    }

    /**
     * @return the Experiment hook
     */
    public final String getExperimentHook() {
        return this.experimentHook;
    }

}
