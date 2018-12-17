/*
 * Person.java Created on 17 April 2007, 09:41 To change this template, choose Tools | Template Manager and
 * open the template in the editor.
 */

package org.pimslims.business.core.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * Defines the class for a Person
 * </p>
 * <p>
 * This Defines the information about a user
 * </p>
 * 
 * @author IMB
 */
public class Person extends XtalObject {
    public static String PROP_FAMILY_NAME = "familyName";

    public static String PROP_GIVEN_NAME = "givenName";

    public static String PROP_FAMILY_TITLE = "familyTitle";

    public static String PROP_TITLE = "title";

    public static String PROP_USERNAME = "username";

    public static String PROP_POSITION = "position";

    public static String PROP_MAILING_ADDRESS = "mailingAddress";

    public static String PROP_DELIVERY_ADDRESS = "deliveryAddress";

    public static String PROP_EMAIL_ADDRESS = "emailAddress";

    public static String PROP_PHONE_NUMBER = "phoneNumber";

    public static String PROP_FAX_NUMBER = "faxNumber";

    public static String PROP_START_DATE = "startDate";

    public static String PROP_END_DATE = "endDate";

    public static String PROP_GROUPS = "groups";

    private String familyName = "";

    private String givenName = "";

    private String familyTitle = "";

    private String title = "";

    private String username = "";

    private String position = "";

    private String mailingAddress = "";

    private String deliveryAddress = "";

    private String emailAddress = "";

    private String phoneNumber = "";

    private String faxNumber = "";

    private Calendar startDate = null;

    private Calendar endDate = null;

    private Collection<Group> groups = new LinkedList<Group>();

    private List<String> middleInitials = Collections.EMPTY_LIST;

    /**
     * LATER Need (perhaps??!) to add more data fields in here for things like: Date Last Visited (should
     * probably be at the User management level?) IP address used for last visit (should probably be at the
     * User management level?) Mailing preferences (for crystallization images) their rights (admin) although
     * that could come from a check at the webapp level using isUserInRole (i.e. for viewing all the plates
     * that have been imaged, etc.)
     */

    /**
     * Creates a new instance of a Person
     */
    public Person() {

    }

    /**
     * 
     * @return
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 
     * @param familyName
     */
    public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }
    
    /**
     * Property: middleInitials
     */
    public List<String> getMiddleInitials() {
        return this.middleInitials ;
    }

    public void setMiddleInitials(final List<String> middleInitials)  {
        this.middleInitials = new ArrayList<String>(middleInitials);
    }


    /**
     * 
     * @return
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * 
     * @param givenName
     */
    public void setGivenName(final String givenName) {
        this.givenName = givenName;
    }

    /**
     * 
     * @return
     */
    public String getFamilyTitle() {
        return familyTitle;
    }

    /**
     * 
     * @param familyTitle
     */
    public void setFamilyTitle(final String familyTitle) {
        this.familyTitle = familyTitle;
    }

    /**
     * 
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     */
    public String getPosition() {
        return position;
    }

    /**
     * 
     * @param position
     */
    public void setPosition(final String position) {
        this.position = position;
    }

    /**
     * 
     * @return
     */
    public String getMailingAddress() {
        return mailingAddress;
    }

    /**
     * 
     * @param mailingAddress
     */
    public void setMailingAddress(final String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    /**
     * 
     * @return
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * 
     * @param deliveryAddress
     */
    public void setDeliveryAddress(final String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * 
     * @return
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 
     * @param emailAddress
     */
    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 
     * @return
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * 
     * @param faxNumber
     */
    public void setFaxNumber(final String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * 
     * @return
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     */
    public void setStartDate(final Calendar startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     */
    public void setEndDate(final Calendar endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     */
    public Collection<Group> getGroups() {
        return groups;
    }

    /**
     * 
     * @param groups
     */
    public void setGroups(final Collection<Group> groups) {
        this.groups = groups;
    }

    /**
     * org.pimslims.business.core.model.Person.hashCode
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * org.pimslims.business.core.model.Person.equals
     * 
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    public void addGroup(final Group group) {
        this.groups.add(group);
    }

    @Override
    public String toString() {
        return this.getGivenName() + " " + this.getFamilyName() + " " + this.getFamilyTitle();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
