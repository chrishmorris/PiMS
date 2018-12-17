/**
 * V5_0-web org.pimslims.servlet ConcurrentUpdateException.java
 * 
 * @author cm65
 * @date 20 Nov 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.servlet;

import javax.servlet.ServletException;

import org.pimslims.model.core.LabBookEntry;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * ConcurrentUpdateException
 * 
 * There is a special error page for these, see mapping in web.xml.
 * 
 */
public class ConcurrentUpdateException extends ServletException {

    private final ModelObjectShortBean bean;

    /**
     * Constructor for ConcurrentUpdateException
     * 
     * @param page
     */
    public ConcurrentUpdateException(final LabBookEntry page) {
        super("Someone else has just updated: " + page.get_Name());
        this.bean = new ModelObjectShortBean(page);
    }

    /**
     * @return Returns the bean.
     */
    public ModelObjectShortBean getBean() {
        return this.bean;
    }

}
