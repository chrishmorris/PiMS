package org.pimslims.kpi;

/**
 * BarChartBean The data needed to draw a bar chart
 */
public interface BarChartBean extends LinkBean {

	public interface SliceBean extends LinkBean {

		// e.g. "ff0000"
		String getColor();

		float getLength();

	}

	public interface BarBean extends LinkBean {

		SliceBean[] getSlices();

		float getWidth();
	}

	String getXAxisCaption();

	String getYAxisCaption();

	boolean getBarsRunInYDirection();

	BarBean[] getBars();

	public void setURL(String url);

	public void setToolTip(String tooltip);

}
