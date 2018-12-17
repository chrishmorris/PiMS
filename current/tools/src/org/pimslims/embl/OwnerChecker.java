/**
 * tools org.pimslims.embl OwnerChecker.java
 * 
 * @author cm65
 * @date 21 Sep 2009
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2009 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.embl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.target.Project;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * OwnerChecker
 * 
 */
public class OwnerChecker {

    private final WritableVersion version;

    /**
     * Constructor for OwnerChecker
     * 
     * @param version
     */
    public OwnerChecker(WritableVersion version) {
        this.version = version;
    }

    /**
     * OwnerChecker.check
     * 
     * @param ro
     * @throws ConstraintException
     */
    public void check(ResearchObjective ro) throws ConstraintException {
        assert Access.REFERENCE.equals(ro.get_Owner());
        Set<ResearchObjectiveElement> elements = ro.getResearchObjectiveElements();
        for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
            ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            Target target = element.getTarget();
            if (null != target) {
                this.check(target, ro);
            }
        }
    }

    /**
     * OwnerChecker.check
     * 
     * @param target
     * @param ro
     * @throws ConstraintException
     */
    private void check(Target target, ResearchObjective ro) throws ConstraintException {
        Set<Project> projects = target.getProjects();
        assert 1 == projects.size() : "Must be one project for target: " + target.getName();
        String projectName = projects.iterator().next().getName();
        LabNotebook owner = this.version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, projectName);
        if (null == owner) {
            owner = this.version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, projectName + " data");
        }
        assert null != owner : "No data owner for project: " + projectName;
        new OwnerChanger(version, null, owner).change(ro);
    }

    public static void main(String[] args) {
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            Collection<ResearchObjective> ros = version.getAll(ResearchObjective.class, 0, 500);
            for (Iterator iterator = ros.iterator(); iterator.hasNext();) {
                ResearchObjective ro = (ResearchObjective) iterator.next();
                if (null == ro.getAccess()) {
                    new OwnerChecker(version).check(ro);
                }
            }
            version.commit();
            System.exit(0);
        } catch (ConstraintException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (AbortedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }
}
