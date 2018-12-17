/**
 * V2_0-pims-web org.pimslims.servlet.mock MockRequestDispatcher.java
 * 
 * @author cm65
 * @date 11 Dec 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 cm65
 * 
 * 
 */
package org.pimslims.presentation.mock;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.Assert;

/**
 * MockRequestDispatcher
 * 
 */
public class MockRequestDispatcher implements RequestDispatcher {

    /**
     * @param path
     */
    public MockRequestDispatcher(final String path) {
        Assert.assertFalse(path.startsWith("/Error"));
    }

    /**
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    public void forward(final ServletRequest request, final ServletResponse arg1) throws ServletException,
        IOException {
        // // COULD implement

    }

    /**
     * @see javax.servlet.RequestDispatcher#include(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    public void include(final ServletRequest arg0, final ServletResponse arg1) throws ServletException,
        IOException {
        // // COULD implement

    }

}
