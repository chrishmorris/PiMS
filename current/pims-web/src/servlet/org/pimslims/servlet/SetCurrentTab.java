package org.pimslims.servlet;

/**
 * pims-web org.pimslims.command.newtarget PIMSTarget.java
 * 
 * @author Peter Troshin (aka pvt43)
 * @date September 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 pvt43
 * 
 * 
 */
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.pimslims.lab.Util;

/**
 * This request filter is used to pass current tab value to the jsp pages to display, and not having to
 * process it in each servlet
 * 
 * SetCurrentTab obsolete only for Leeds pages
 */
@Deprecated
public class SetCurrentTab implements Filter {

    public static final String tab = "_tab";

    //ServletContext scontx = null;

    public void init(final FilterConfig fconfig) throws ServletException {
        //this.scontx = fconfig.getServletContext();
    }

    public void destroy() {
        //this.scontx = null;
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final String curTab = req.getParameter(SetCurrentTab.tab);

        if (!Util.isEmpty(curTab)) {
            req.setAttribute(SetCurrentTab.tab, curTab);
            req.getSession().setAttribute(SetCurrentTab.tab, curTab);
        } else {
            req.setAttribute(SetCurrentTab.tab, req.getSession().getAttribute(SetCurrentTab.tab));
            req.getSession().removeAttribute(SetCurrentTab.tab);
        }

        chain.doFilter(req, response);
    }
}
