/*
 * Created on 04-Apr-2005 @author: Chris Morris
 */
package org.pimslims.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Deliberate crash while owning a transaction, for testing error recovery.
 * 
 * @version 0.1
 */
public class Crash extends PIMSServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "deliberate carch, for testing";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        getWritableVersion(request, response);
        throw new RuntimeException("Deliberate crash while owning transaction");
    }
}
