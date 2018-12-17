package org.pimslims.servlet;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents a "perspective" on the PiMS database, e.g. standard perspective, expert perspective. If you want
 * to extend PiMS, it is likely you will want to add a new perspective.
 * 
 * @author cm65
 * 
 */
public interface Perspective extends Serializable // can be saved in HTTP
// session
{

    String getName();

    /**
     * The URL to view an object. The actual URL used will consist of: the context path, e.g. "pims" / the
     * value returned by this method the path info, e.g. "/org.pimslims.model.target.Target:3443" "?", if the
     * request included a query string the query string if any
     * 
     * @param request
     * @return the path to use to view this record
     */
    String getViewUrl(HttpServletRequest request, String className);

}
