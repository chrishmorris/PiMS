/**
 * 
 */
package org.pimslims.crystallization.servlet.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jon Diprose
 * @see http://www.onjava.com/onjava/2004/03/03/examples/ResponseHeaderFilter.java
 */
public class ResponseHeaderFilter implements Filter {

	private FilterConfig filterConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse r = (HttpServletResponse) response;
		Enumeration<?> e = filterConfig.getInitParameterNames();
		while (e.hasMoreElements()) {
			String headerName = (String) e.nextElement();
			r.addHeader(headerName, filterConfig.getInitParameter(headerName));
		}
		chain.doFilter(request, response);
	}

}
