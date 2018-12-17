/**
 * V5_0-web org.pimslims.servlet BookmarkBean.java
 * 
 * @author cm65
 * @date 20 Sep 2013
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2013 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import javax.servlet.http.HttpServletRequest;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.core.Bookmark;

/**
 * BookmarkBean
 * 
 */
public class BookmarkBean extends ModelObjectBean {

    /**
     * Constructor for BookmarkBean
     * 
     * @param modelObject
     */
    public BookmarkBean(final Bookmark modelObject) {
        super(modelObject);
    }

    /**
     * BookmarkBean.getUrl
     * 
     * @return relative url, i.e. part after http://localhost:8080/pims/
     */
    public String getUrl() {
        return (String) this.getValues().get(Bookmark.PROP_URL);
    }

    /**
     * Default action is to visit the bookmarked page BookmarkBean.getViewUrl
     * 
     * @see org.pimslims.presentation.ModelObjectShortBean#getViewUrl()
     */
    @Override
    public String getViewUrl() {
        return this.getUrl();
    }

    /**
     * BookmarkBean.getBookmark
     * 
     * @param request
     * @param version
     * @return
     */
    public static ModelObjectBean setBookmark(final HttpServletRequest request, final ReadableVersion version) {
        // request.getRequestURI() starts with "/pims/"
        String url = request.getRequestURI().substring(request.getContextPath().length());
        if (null != request.getQueryString() && !"".equals(request.getQueryString())) {
            url += "?" + request.getQueryString();
        }
        // System.out.println(Bookmark.class.getName() + "####" + url); 

        return BookmarkBean.setBookmark(url, request, version);
    }

    public static ModelObjectBean setBookmark(final String url, final HttpServletRequest request,
        final ReadableVersion version) {

        final Bookmark bookmark = version.findFirst(Bookmark.class, Bookmark.PROP_URL, url);
        if (null == bookmark) {
            return null;
        }
        final BookmarkBean ret = new BookmarkBean(bookmark);
        request.setAttribute("bookmark", ret);
        return ret;
    }

}
