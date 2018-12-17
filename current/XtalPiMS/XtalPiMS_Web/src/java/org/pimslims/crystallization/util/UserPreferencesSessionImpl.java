/**
 * 
 */
package org.pimslims.crystallization.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jon Diprose
 * 
 *         TODO Implement an HttpSessionListener to setup defaults and load from
 *         and persist to longer-term storage.
 */
@Deprecated
// obsolete
public class UserPreferencesSessionImpl implements UserPreferences {

	private static final String ATTR_NAME = "_USER_PREFS";
	private Map<String, String> prefs;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.util.UserPreferences#getPreference(java.
	 * lang.String)
	 */
	public String getPreference(String key) {
		// TODO Auto-generated method stub
		return prefs.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.util.UserPreferences#setPreference(java.
	 * lang.String, java.lang.Object)
	 */
	public void setPreference(String key, String value) {
		prefs.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pimslims.crystallization.util.UserPreferences#getPreferences()
	 */
	public Map<String, String> getPreferences() {
		return Collections.unmodifiableMap(prefs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.util.UserPreferences#setPreferences(java
	 * .util.Map)
	 */
	public void setPreferences(Map<String, String> preferences) {
		prefs.clear();
		mergePreferences(preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pimslims.crystallization.util.UserPreferences#mergePreferences(java
	 * .util.Map)
	 */
	public void mergePreferences(Map<String, String> preferences) {
		prefs.putAll(preferences);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pimslims.crystallization.util.UserPreferences#reset()
	 */
	@SuppressWarnings("unchecked")
	public void reset(HttpServletRequest request) {
		Map<String, String> tmp = (Map<String, String>) request.getSession()
				.getAttribute(ATTR_NAME);
		if (null == tmp) {
			tmp = new HashMap<String, String>();
			request.getSession().setAttribute(ATTR_NAME, tmp);
		}
		prefs = new HashMap<String, String>(tmp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pimslims.crystallization.util.UserPreferences#save()
	 */
	public void save(HttpServletRequest request) {
		Map<String, String> tmp = new HashMap<String, String>(prefs);
		request.getSession().setAttribute(ATTR_NAME, tmp);
	}

}
