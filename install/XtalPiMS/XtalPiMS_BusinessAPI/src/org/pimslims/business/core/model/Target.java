/*
 * Target.java
 *
 * Created on 17 April 2007, 12:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import org.pimslims.business.Link;
import org.pimslims.business.XtalObject;

/**
 * <p>
 * Describes a Target
 * </p>
 * <p>
 * LATER: this would be better represented by a link which would go to the appropriate page in Core PIMS which
 * would describe the Target more fully.
 * </p>
 * 
 * @author IMB
 */
public class Target extends XtalObject {
    public static String PROP_NAME  = "name";
    public static String PROP_DESCRIPTION  = "description";
    public static String PROP_GROUP  = "group";
    public static String PROP_PROJECT  = "project";
    public static String PROP_PROTEIN  = "protein";
    private String name = "";

    private String description = "";

    private Group group = null;

    private Project project = null;

    private Protein protein = null;

    /**
     * Link through to the relevant target page in core PIMS
     */
    private Link targetLink = new Link();

    /**
     * Creates a new instance of a Target
     */
    public Target() {

    }

    public Target(String name, Protein protein) {
        setName(name);
        setProtein(protein);
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Link getTargetLink() {
        return targetLink;
    }

    public void setTargetLink(Link targetLink) {
        this.targetLink = targetLink;
    }

    /**
     * @return Returns the protein.
     */
    public Protein getProtein() {
        return protein;
    }

    /**
     * @param protein The protein to set.
     */
    public void setProtein(Protein protein) {
        this.protein = protein;
    }

}
