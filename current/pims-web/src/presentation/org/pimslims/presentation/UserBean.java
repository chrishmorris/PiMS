/**
 * pims-web org.pimslims.presentation UserBean.java
 * 
 * @author Ed Daniel
 * @date 20.6.2014
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2014 Ed Daniel The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.presentation;

import org.pimslims.model.accessControl.User;

/**
 * UserBean
 * 
 */
public class UserBean extends ModelObjectShortBean {

    private final String username;

    private final String givenName;

    private final String familyName;

    public UserBean(final User user) {
        super(user);
        // or user.getPerson()  
        this.username = user.getName();
        this.familyName = user.getFamilyName();
        this.givenName = user.getGivenName();
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return Returns the givenName.
     */
    public String getGivenName() {
        return this.givenName;
    }

    /**
     * @return Returns the familyName.
     */
    public String getFamilyName() {
        return this.familyName;
    }
}
