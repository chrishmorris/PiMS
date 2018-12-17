/*
 * Group.java
 *
 * Created on 17 April 2007, 12:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * Describes a Group
 * </p>
 * <p>
 * A Group is defined as a way of grouping users within an organisation.
 * </p>
 * <p>
 * A User may be part of several groups, at several organisations
 * </p>
 * 
 * @author IMB
 */
public class Group extends XtalObject {
    public static String PROP_NAME = "name";
    public static String PROP_ORGANISATION = "organisation";
    public static String PROP_GROUPHEAD = "groupHead";
    public static String PROP_USERS = "users";
    
    
    private String name = "";

    private Organisation organisation = null;

    private List<Person> users = new LinkedList<Person>();

    private Person groupHead = null;

    /**
     * Creates a new instance of a Group
     */
    public Group() {

    }

    public Group(String name, Organisation organisation) {
        setName(name);
        setOrganisation(organisation);
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

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<Person> getUsers() {
        return users;
    }

    public void setUsers(List<Person> users) {
        this.users = users;
    }

    public void addUser(Person user) {
        this.users.add(user);
        user.addGroup(this);
    }

    public Person getGroupHead() {
        return groupHead;
    }

    public void setGroupHead(Person groupHead) {
        this.groupHead = groupHead;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
