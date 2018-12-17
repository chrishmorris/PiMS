package org.pimslims.presentation.target;

import java.io.Serializable;
import java.util.Set;

import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;

/**
 * Presentation layer Target Experiment Bean
 * 
 * @author Petr Troshin
 */
public class TargetExperimentBean implements Comparable<TargetExperimentBean>, Serializable {

    /**
     * To satisfy Serializable Interface
     */
    private static final long serialVersionUID = -530441343563381722L;

    public static long getSerialVersionUID() {
        return TargetExperimentBean.serialVersionUID;
    }

    Target target;

    Experiment experiment;

    /**
     * 
     */
    public TargetExperimentBean(final Target target, final Experiment experiment) {
        this.target = target;
        assert null != target;
        this.experiment = experiment;
        assert null != experiment;
    }

    public Experiment getLastExperiment() {
        return this.experiment; // TargetUtility.getMostRecentExperiment(target);
    }

    /*
    public ModelObjectShortBean getLastExperimentAsShortBean() {
        return ModelObjectShortBean.getShortBean(getLastExperiment());
    }
    */
    public Target getTarget() {
        return this.target;
    }

    public Milestone getLastMilestone() {
        final Set<Milestone> miles = this.target.getMilestones();
        Milestone recentMile = null;
        for (final Milestone mile : miles) {
            if (recentMile == null) {
                recentMile = mile;
            } else {
                if (mile.getDate().after(recentMile.getDate())) {
                    recentMile = mile;
                }
            }
        }
        return recentMile;
    }

    public String getLastMilestoneName() {
        if (null == this.getLastMilestone()) {
            return "";
        }
        return this.getLastMilestone().getStatus().getName();
    }

    public String getCreator() {
        if (this.target.getCreator() != null) {
            return this.target.getCreator().getName();
        }
        return "";
    }

    public String getExperimentator() {

        if (this.getLastExperiment().getCreator() != null) {
            return this.getLastExperiment().getCreator().get_Name();
        }

        return "";
    }

    public int getDaysSinceLastProgress() {
        return (int) ((System.currentTimeMillis() - this.getLastExperiment().getStartDate().getTimeInMillis()) / (1000 * 3600 * 24));
    }

    public String getExperimentLab() {
        final Experiment exp = this.getLastExperiment();
        LabNotebook access = null;
        if (exp != null) {
            access = exp.getAccess();
        } else {
            System.out.println("EXP NULL" + this.target.get_Hook());
        }
        String name = "";
        if (access != null) {
            name = access.getName();
        }
        return name;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final TargetExperimentBean o) {
        return new Integer(this.getDaysSinceLastProgress()).compareTo(o.getDaysSinceLastProgress());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.target == null) ? 0 : this.target.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TargetExperimentBean other = (TargetExperimentBean) obj;
        if (this.target == null) {
            if (other.target != null) {
                return false;
            } else {
                return true;
            }
        } else if (!this.target.equals(other.target)) {
            return false;
        }
        // This method deliberately ignore experiments
        // as we do not interesting in  target info
        return true;
    }

}
