/*
 * Construct.java Created on 17 April 2007, 12:52 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */
package org.pimslims.business.core.model;

import org.pimslims.business.Link;
import org.pimslims.business.XtalObject;

/**
 * <p>
 * A Class describing a construct
 * </p>
 * <p>
 * LATER: This does not describe a construct very well at all, however how much detail do we actually want to
 * provide within the crystallization module? My feeling is that what we need here is a link generated linking
 * back to the appopriate page in the Core PIMS site that has the details of the Construct
 * </p>
 * 
 * @author IMB
 */
public class Construct extends XtalObject {
    public static String PROP_NAME = "name";

    public static String PROP_DESCRIPTION = "description";

    public static String PROP_TARGET = "target";

    public static String PROP_OWNER = "owner";

    public static String PROP_GROUP = "group";

    /**
     * The name of this construct.
     */
    private String name = "";

    /**
     * The description of this construct.
     */
    private String description = "";

    /**
     * The target for theis construct.
     */
    private final Target target;

    /**
     * The scientist who is responsible for this Construct (the person with whom contact should be made for
     * further information.
     */
    private Person owner;

    /**
     * This will be a link through to the relevant page in core PIMS.
     */
    private Link constructLink = new Link();

    /**
     * Adding a group here so that a construct should belong to both an "owner" and a group.
     */
    private Group group;

    /**
     * Temporary for oppf platedb implementation where we don't know about targets.... (yet).
     * 
     * @param name
     * 
     */
    public Construct(String name) {
        super();
        this.target = null;
        this.name = name;
    }

    /**
     * Constructor.
     * 
     * @param name
     * @param target
     */
    public Construct(String name, final Target target) {
        super();
        this.name = name;
        this.target = target;
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

    public Target getTarget() {
        return target;
    }

    public void setOwner(Person person) {
        this.owner = person;
    }

    public Person getOwner() {
        return this.owner;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Link getConstructLink() {
        return constructLink;
    }

    public void setConstructLink(Link constructLink) {
        this.constructLink = constructLink;
    }
}
