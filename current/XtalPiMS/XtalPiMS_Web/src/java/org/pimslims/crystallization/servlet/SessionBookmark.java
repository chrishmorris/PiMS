/**
 * 
 */
package org.pimslims.crystallization.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jon Diprose
 * 
 *         Contains query parameters
 * 
 */
public class SessionBookmark {

	private static final String UTF_8 = "UTF-8";
	private final Map<String, String> properties = new HashMap<String, String>();

	public void put(final String key, final String value) {
		if ((null != value) && (0 != value.length())) {
			this.properties.put(key, value);
		}
	}

	@Deprecated
	// Never called
	public String get(final String key) {
		return this.properties.get(key);
	}

	public String remove(final String key) {
		return this.properties.remove(key);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		String sep = "";
		try {
			for (Map.Entry<String, String> entry : this.properties.entrySet()) {
				sb.append(sep);
				sb.append(URLEncoder.encode(entry.getKey(), UTF_8));
				sb.append("=");
				System.out.println(entry.getKey() + " => " + entry.getValue());
				sb.append(URLEncoder.encode(entry.getValue(), UTF_8));
				sep = "&";
			}
		} catch (final UnsupportedEncodingException e) {
			// Should never get here unless UTF-8 is not supported
			e.printStackTrace();
		}

		return sb.toString();
	}
}
