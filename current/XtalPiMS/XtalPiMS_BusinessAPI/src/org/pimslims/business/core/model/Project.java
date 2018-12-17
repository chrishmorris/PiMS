/*
 * Project.java
 *
 * Created on 17 April 2007, 12:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import java.util.ArrayList;
import java.util.Collection;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * Details about a project
 * </p>
 * <p>
 * A project is a a group of samples belonging to a group
 * </p>
 * <p>
 * Each project will have an owner and a localcontact (for projects that are outside the lab where the
 * experimentation is being done, a local contact is needed to manage the samples in-house)
 * </p>
 * 
 * @author IMB
 */
public class Project extends XtalObject {
    public static String PROP_NAME  = "name";
    public static String PROP_DESCRIPTION  = "description";
    public static String PROP_OWNER  = "owner";
    public static String PROP_LOCAL_CONTACT  = "localContact";
    public static String PROP_TARGETS  = "targets";
    public static String PROP_GROUP  = "group";
    
    private String name = "";

    private String description = "";

    private Person owner = null;

    private Person localContact = null;

    private Collection<Target> targets = new ArrayList<Target>();

    //Removed from bean - get using the Service objects - saves loading stuff we do not need all at once!
    private Group group = null;

    //private List<Sample> samples = new ArrayList<Sample>();

    /**
     * Creates a new instance of a Project
     */
    public Project() {

    }

    public Project(String name, Group group) {
        setName(name);
        setGroup(group);
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getLocalContact() {
        return localContact;
    }

    public void setLocalContact(Person localContact) {
        this.localContact = localContact;
    }

    public Collection<Target> getTargets() {
        return targets;
    }

    public void setTargets(Collection<Target> targets) {
        this.targets = targets;
    }

}
