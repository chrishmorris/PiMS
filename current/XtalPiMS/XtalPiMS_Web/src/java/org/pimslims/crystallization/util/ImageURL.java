package org.pimslims.crystallization.util;

@Deprecated
// probably only for PlateDB
public class ImageURL {

	// don't use static variables, this class does not scale to the use of
	// multiple imagers
	private static String url_compositeimages = "";

	private static String url_sliceimages = "";

	private static String url_zoomedimages = "";

	/**
	 * @return Returns the url_compositeimages.
	 */
	public static String getUrl_compositeimages() {
		return url_compositeimages;
	}

	/**
	 * @param url_compositeimages
	 *            The url_compositeimages to set.
	 */
	public static void setUrl_compositeimages(final String url_compositeimages) {
		ImageURL.url_compositeimages = url_compositeimages;
	}

	/**
	 * @return Returns the url_sliceimages.
	 */
	public static String getUrl_sliceimages() {
		return url_sliceimages;
	}

	/**
	 * @param url_sliceimages
	 *            The url_sliceimages to set.
	 */
	public static void setUrl_sliceimages(final String url_sliceimages) {
		ImageURL.url_sliceimages = url_sliceimages;
	}

	/**
	 * @return Returns the url_zoomedimages.
	 */
	public static String getUrl_zoomedimages() {
		return url_zoomedimages;
	}

	/**
	 * @param url_zoomedimages
	 *            The url_zoomedimages to set.
	 */
	public static void setUrl_zoomedimages(final String url_zoomedimages) {
		ImageURL.url_zoomedimages = url_zoomedimages;
	}

}
