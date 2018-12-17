/**
 * V5_0-web org.pimslims.servlet.access ViewUser.java
 * 
 * @author cm65
 * @date 23 Jan 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet.access;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.PasswordChange;
import org.pimslims.servlet.expert.View;

/**
 * ViewUser
 * 
 */
public class ViewUser extends View {

    /**
     * ViewUser.doDoGet
     * 
     * @see org.pimslims.servlet.expert.View#doDoGet(org.pimslims.dao.ReadableVersion,
     *      org.pimslims.metamodel.ModelObject, javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected void doDoGet(final ReadableVersion rv, final ModelObject object,
        final HttpServletRequest request) {
        request.setAttribute("isLeader",
            PasswordChange.isLeader(object.get_Name(), PIMSServlet.getUsername(request), rv));
        //throw new RuntimeException(object.get_Name() + "##" + PIMSServlet.getUsername(request));
    }

    /**
     * ViewUser.getServletInfo
     * 
     * @see org.pimslims.servlet.expert.View#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Custom view of a User, should only be seen by administrator or Leader";
    }

}
