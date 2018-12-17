/**
 * 
 */
package org.pimslims.crystallization.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jon Diprose
 * 
 */
@Deprecated
// obsolete
public interface UserPreferences {

	/**
	 * Get a preference, returning null if no preference has been set for this
	 * key
	 * 
	 * @param key
	 * @return
	 */
	public String getPreference(String key);

	/**
	 * Set a preference for this key.
	 * 
	 * @param key
	 * @param value
	 */
	public void setPreference(String key, String value);

	/**
	 * Get a Map of the preferences. The Map is unmodifiable.
	 * 
	 * @return
	 */
	public Map<String, String> getPreferences();

	/**
	 * Set a Map of the preferences. Any existing preferences not referenced in
	 * the Map will be cleared
	 * 
	 * @return
	 */
	public void setPreferences(Map<String, String> preferences);

	/**
	 * Merge a Map of the preferences. Any existing preferences not referenced
	 * in the Map will remain unchanged
	 * 
	 * @return
	 */
	public void mergePreferences(Map<String, String> preferences);

	/**
	 * Persist current state of preferences
	 */
	public void save(HttpServletRequest request);

	/**
	 * Reset preferences to previous persisted state
	 */
	public void reset(HttpServletRequest request);

}
