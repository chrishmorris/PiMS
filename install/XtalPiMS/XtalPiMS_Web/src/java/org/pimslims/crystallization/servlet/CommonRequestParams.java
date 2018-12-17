/**
 * 
 */
package org.pimslims.crystallization.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Jon Diprose
 *
 */
public class CommonRequestParams {

	public static CommonRequestParams parseRequest(
			final HttpServletRequest request) {

		final SessionBookmark sb = new SessionBookmark();
		final CommonRequestParams crp = new CommonRequestParams(sb);
		
		crp.setSort(request.getParameter("sort"));
		crp.setSessionBookmarkName(request.getParameter("sb"));
		
		final String maxResultsStr = request.getParameter("results");
		final String firstResultStr = request.getParameter("startIndex");
		final String dirStr = request.getParameter("dir");

		if (null != firstResultStr) {
			sb.put("startIndex", firstResultStr);
			crp.setFirstResult(Integer.parseInt(firstResultStr));
		} else {
			sb.put("startIndex", "0");
			crp.setFirstResult(0);
		}
		if ((null != maxResultsStr) && (!"".equals(maxResultsStr))) {
			sb.put("results", maxResultsStr);
			crp.setMaxResults(Integer.parseInt(maxResultsStr));
		} else {
			sb.put("results", "10");
			crp.setMaxResults(10);
		}
		if (null != crp.getSort()) {
			sb.put("sort", crp.getSort());
		}
		// TODO Ignore dir when sort is unspecified
		if (null != dirStr) {
			if (dirStr.equals("asc")) {
				sb.put("dir", "asc");
				crp.setAscending(true);
			} else {
				sb.put("dir", "desc");
				crp.setAscending(false);
			}
		}

		return crp;
	}

	private int firstResult = 0;
	private int maxResults = 10;
	private String sort = null;
	private boolean ascending = false;
	private String sessionBookmarkName = null;

	final private SessionBookmark sessionBookmark;

	public CommonRequestParams() {
		this.sessionBookmark = new SessionBookmark();
	}

	public CommonRequestParams(SessionBookmark sessionBookmark) {
		this.sessionBookmark = sessionBookmark;
	}

	/**
	 * @return the firstResult
	 */
	public int getFirstResult() {
		return firstResult;
	}
	/**
	 * @param firstResult the firstResult to set
	 */
	public void setFirstResult(final int firstResult) {
		this.firstResult = firstResult;
	}
	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}
	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(final int maxResults) {
		this.maxResults = maxResults;
	}
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(final String sort) {
		this.sort = sort;
	}
	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}
	/**
	 * @param ascending the ascending to set
	 */
	public void setAscending(final boolean ascending) {
		this.ascending = ascending;
	}
	/**
	 * @return the sessionBookmarkName
	 */
	public String getSessionBookmarkName() {
		return sessionBookmarkName;
	}
	/**
	 * @param sessionBookmarkName the sessionBookmarkName to set
	 */
	public void setSessionBookmarkName(final String sessionBookmarkName) {
		this.sessionBookmarkName = sessionBookmarkName;
	}

	/**
	 * @return the sessionBookmark
	 */
	public SessionBookmark getSessionBookmark() {
		return sessionBookmark;
	}

	public void storeSessionBookmark(final HttpSession sess, final String defaultName) {
		String name = this.sessionBookmarkName;
		if ((null == name) || (0 == name.length())) {
			name = defaultName;
		}
		sess.setAttribute(name, this.sessionBookmark);
	}
}
