/*
 * Organisation.java
 *
 * Created on 17 April 2007, 12:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.pimslims.business.core.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.pimslims.business.XtalObject;

/**
 * <p>
 * An organisation is equivalent to a lab (e.g. OPPF) or set of labs (e.g.EMBL)
 * </p>
 * TODO needs .equals and .hashCode
 * @author IMB
 */
public class Organisation extends XtalObject {
    public static String PROP_NAME  = "name";
    public static String PROP_BUILING  = "building";
    public static String PROP_STREET  = "street";
    public static String PROP_TOWN  = "town";
    public static String PROP_POSTAL_CODE  = "postalCode";
    public static String PROP_COUNTRY  = "country";
    public static String PROP_PHONE  = "phone";
    public static String PROP_FAX  = "fax";
    public static String PROP_EMAIL  = "email";
    public static String PROP_CONTACT_NAME  = "contactName";
    public static String PROP_URL  = "url";
    public static String PROP_WEB_SERVICES = "webServices";
    public static String PROP_GROUPS  = "groups";    
    
    private String name = "";

    private String building = "";

    private String street = "";

    private String town = "";

    private String postalCode = "";

    private String country = "";

    private String phone = "";

    private String fax = "";

    private String email = "";

    private String contactName = "";

    private URL URL = null;

    //Removed from bean - get using the Service objects - saves loading stuff we do not need all at once!

    private Collection<WebService> webServices = new ArrayList<WebService>();

    private Collection<Group> groups = new ArrayList<Group>();
    private String url = null;

    //private List<Location> locations = new ArrayList<Location>();

    /**
     * Creates a new instance of an Organisation
     */
    public Organisation() {

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
    public String getBuilding() {
        return building;
    }

    /**
     * 
     * @param building
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * 
     * @return
     */
    public String getStreet() {
        return street;
    }

    /**
     * 
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * 
     * @return
     */
    public String getTown() {
        return town;
    }
    
    /**
     * Web site address
     */
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url  = url;
    }
    /**
     * 
     * @param town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     */
    public String getFax() {
        return fax;
    }

    /**
     * 
     * @param fax
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * 
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * 
     * @return
     */
    public URL getURL() {
        return URL;
    }

    /**
     * 
     * @param URL
     */
    public void setURL(URL URL) {
        this.URL = URL;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group) {
        if (this.groups == null) {
            this.groups = new ArrayList<Group>();
        }
        this.groups.add(group);
    }

    public Collection<WebService> getWebServices() {
        return webServices;
    }

    public void setWebServices(Collection<WebService> webServices) {
        this.webServices = webServices;
    }

}
