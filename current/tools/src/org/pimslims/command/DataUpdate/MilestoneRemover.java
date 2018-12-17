/**
 * current-pims-web org.pimslims.command.DataUpdate MilestoneRemover.java
 * 
 * @author bl67
 * @date 21 Dec 2007
 * 
 * Protein Information Management System
 * @version: 1.3
 * 
 * Copyright (c) 2007 bl67
 * 
 * 
 */
package org.pimslims.command.DataUpdate;

import java.util.Collection;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;

/**
 * MilestoneRemover
 * 
 */
public class MilestoneRemover implements IDataFixer {

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#fixData(org.pimslims.dao.WritableVersion)
     */
    public Boolean fixData(final WritableVersion wv) throws ConstraintException, AccessException {

        final Collection<Target> targets = wv.getAll(Target.class, 0, Integer.MAX_VALUE);
        int i = 0;
        for (final Target t : targets) {
            //get all miltsones of each target
            for (final Milestone m : t.getMilestones()) {
                //for milstone which do not have exp and blueprint
                if (m.getResearchObjective() == null && m.getExperiment() == null) {
                    //look for another milestone which has same target, date,code and not null exp+blueprint
                    if (this.foundDuplicatedMilestone(t, m)) {
                        m.delete();
                        i++;
                        System.out.println(m + " is deleted!");
                    }
                }
            }
        }
        if (i != 0) {
            System.out.println(i + " milestones are deleted!");
        }
        return i == 0;
    }

    /**
     * @param t
     * @param m
     * @return
     */
    private boolean foundDuplicatedMilestone(final Target t, final Milestone milestone) {
        for (final Milestone m : t.getMilestones()) {
            if (m.getResearchObjective() != null || m.getExperiment() != null)//not null exp+blueprint
            {
                //same code and date
                if (m.getDate().equals(milestone.getDate()) && m.getStatus().equals(milestone.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @see org.pimslims.command.DataUpdate.IDataFixer#getDescription()
     */
    public String getDescription() {
        return "Delete duplicated milestones which do not have experiment and construct!";
    }

}
