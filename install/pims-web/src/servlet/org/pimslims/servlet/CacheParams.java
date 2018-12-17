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
 *           TODO see PIMS-3721
 */
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheParams implements Filter {

    SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public void destroy() {
        ///scontx = null;
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        final String uri = ((HttpServletRequest) request).getRequestURI();

        final String browser = ((HttpServletRequest) request).getHeader("User-Agent");
        final HttpServletResponse resp = (HttpServletResponse) response;

        //TODO update the line below for each release
        if (uri.contains("5.0.0")) {
            // under development 
            resp.setHeader("pragma", "no-cache");
            resp.setHeader("cache-control", "must-revalidate, max-age=0");
        } else if (uri.endsWith(".js") || uri.endsWith(".css") || uri.contains("/images/")) {
            resp.setHeader("pragma", "");
            resp.setHeader("cache-control", "public, max-age=" + 7 * 24 * 60 * 60);
            final Calendar expires = java.util.Calendar.getInstance();
            expires.setTimeInMillis(7 * 24 * 60 * 60 * 1000 + expires.getTimeInMillis());
            resp.setHeader("expires", this.HTTP_DATE_FORMAT.format(expires.getTime()));
            // probably better to set Expires, it seems that Firefox revalidates without it
        } else if (null != browser && browser.contains("MSIE")) {
            // cant use no-cache, see http://support.microsoft.com/kb/323308
            resp.setHeader("pragma", "");
            resp.setHeader("cache-control", "private, must-revalidate, max-age=0");
        } else { // servlet
            resp.setHeader("cache-control", "private, must-revalidate, max-age=0");
            resp.setHeader("pragma", "no-cache");
            //TODO could calculate an ETag
        }
        chain.doFilter(request, resp);
    }

    public void init(final FilterConfig fconfig) throws ServletException {
        //this.scontx = fconfig.getServletContext();
    }

}
