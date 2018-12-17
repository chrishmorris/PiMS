package org.pimslims.servlet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Petr Troshin aka pvt43
 * 
 *         HeaderParamsWrapper
 * 
 */
@Deprecated
public class HeaderParamsWrapper extends HttpServletResponseWrapper {

    public HeaderParamsWrapper(final HttpServletResponse response) {
        super(response);
    }

    @Override
    @Deprecated
    // see comment below
    public void setHeader(final String arg0, final String arg1) {
        // better not to use
        // throw new NotImplementedException("setHeader() method must not be
        // used!");
        // System.out.println("set Headers is used to set name " + arg0
        // + " value " + arg1);
        super.setHeader(arg0, arg1);
    }

}
