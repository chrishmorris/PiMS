package org.pimslims.diamond;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ShipmentBean {

	public static class SampleBean {

		private final Map<String, Object> properties;

		public SampleBean(Map<String, Object> properties2) {
			this.properties = properties2;
		}

		// get property value
		public Object get(String string) {
			return this.properties.get(string);
		}

	}

	public static class PuckBean {

		private final String name;
		private final List<SampleBean> samples = new ArrayList();

		public PuckBean(String string) {
			this.name = string;
		}

		public String getName() {
			return this.name;
		}

		public List<SampleBean> getSamples() {
			return this.samples;
		}

		public void add(SampleBean sampleBean) {
			this.samples.add(sampleBean);
		}

	}

	public static class DewarBean {

		private final String name;
		private List<PuckBean> pucks = new ArrayList();

		public DewarBean(String dewar) {
			this.name = dewar;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return this.name;
		}

		public List<PuckBean> getPucks() {
			return this.pucks;
		}

		public void add(PuckBean puck) {
			this.pucks.add(puck);
		}

	}

	private List<DewarBean> dewars;
	private Calendar shippingDate;
	private String destination;
	private String shipper;
	private String trackingNumber;

	public ShipmentBean(Collection<DewarBean> dewars) {
		this.dewars = new ArrayList(dewars);
	}

	public List<DewarBean> getDewars() {
		return this.dewars;
	}

	public Calendar getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Calendar instance) {
		this.shippingDate = instance;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

}
