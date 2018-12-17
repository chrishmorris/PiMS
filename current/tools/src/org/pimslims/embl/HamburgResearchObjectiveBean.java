package org.pimslims.embl;

import java.util.Collection;
import java.util.HashSet;

/**
 * The details of a Research Objective, as represented in an xml file from EMBL
 * Hamburg
 * 
 * These elements are ignored: annotationUrl, workpackage
 * 
 * @author cm65
 * 
 */
public class HamburgResearchObjectiveBean {

    /**
 * Target ID 
     */
    private final String targetId;

    private final String description;

    /**
     * More than one if it is a complex
     */
    private final Collection<HamburgOrfBean> orfs;

    private final Collection<HamburgProjectBean> projects;

    private final String workpackage;

    public HamburgResearchObjectiveBean(String name, String description,
            Collection<HamburgOrfBean> orfs,
            Collection<HamburgProjectBean> projects, String workpackage) {
        super();
        this.targetId = name;
        this.description = description;
        this.orfs = new HashSet<HamburgOrfBean>(orfs);
        this.projects = new HashSet<HamburgProjectBean>(projects);
        this.workpackage = workpackage;
    }

    /**
     * HamburgResearchObjectiveBean.getName
     * @return the XMTB number from PLUMS
     */
    public final String getName() {
        return targetId;
    }

    public final String getRvNumber() {
        return description;
    }

    public final Collection<HamburgOrfBean> getOrfs() {
        return orfs;
    }

    public final Collection<HamburgProjectBean> getProjects() {
        return projects;
    }

    public String getWorkpackage() {
        return this.workpackage;
    }

}
