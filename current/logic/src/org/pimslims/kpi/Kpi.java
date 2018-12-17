package org.pimslims.kpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.kpi.BarChartBean.BarBean;
import org.pimslims.kpi.BarChartBean.SliceBean;
import org.pimslims.report.Report;

public class Kpi {

	private static final String QUERY_PREFIX = " input.experiment.protocol.name, input.experiment.status, "
			+
			// "abs" in the line below makes the results look sensible, even if
			// there are errors in dates
			" avg(abs(extract(epoch from  (input.experiment.endDate - input.sample.outputSample.experiment.endDate)))), "
			+ " count(input.experiment) "
			+ " from InputSample as input"
			+ " where input.experiment in (";

	private static final String QUERY_POSTFIX = ") group by input.experiment.protocol.name, input.experiment.status";

	static class AttritionSliceBean implements SliceBean {

		private final String caption;
		private final String url;
		private final String color;
		private final float length;
		private final String status;

		/**
		 * Constructor for AttritionSliceBean
		 * 
		 * @param caption
		 * @param url
		 * @param color
		 * @param length
		 */
		public AttritionSliceBean(String caption, String url, String color,
				float length, String status) {
			super();
			this.caption = caption;
			this.url = url;
			this.color = color;
			this.length = length;
			this.status = status;
		}

		@Override
		public String getCaption() {
			return this.caption;
		}

		@Override
		public String getURL() {
			return this.url;
		}

		@Override
		public String getToolTip() {
			return this.status + " experiments with protocol:  " + this.caption;
		}

		@Override
		public String getColor() {
			return this.color;
		}

		@Override
		public float getLength() {
			return this.length;
		}

	}

	protected static final SliceBean EMPTY_SLICE = new AttritionSliceBean(
			"No experiments", "javascript:alert('no experiments found')",
			"black", 0f, "No");

	private static final Comparator<BarBean> LONGEST_FIRST = new Comparator<BarBean>() {

		@Override
		public int compare(BarBean bar1, BarBean bar2) {
			return -Float.compare(length(bar1), length(bar2));
		}

		private float length(BarBean bar) {
			float length = 0;
			SliceBean[] slices = bar.getSlices();
			for (int i = 0; i < slices.length; i++) {
				length += slices[i].getLength();
			}
			return length;
		}
	};

	public static BarChartBean getAttrition(Report report, final String chartUrl) {

		List results;
		try {
			results = report.wrap(QUERY_PREFIX, QUERY_POSTFIX);
		} catch (PersistenceException e) {
			Throwable cause = e;
			while (null != cause.getCause()) {
				cause = cause.getCause();
			}
			cause.printStackTrace();
			throw (e);
		}
		return getChart(chartUrl, results);
	}

	public static BarChartBean getAttrition(ReadableVersion version,
			String query1, Map<String, Object> parameters, final String chartUrl) {

		EntityManager em = version.getEntityManager();
		// note that this query may be postgresql specific
		// without the extract, the value is "1 day", producing an exception in
		// getResultList
		Query query = em.createQuery(QUERY_PREFIX
				+ query1
				+ (version.isAdmin() ? ""
						: " and input.experiment.access in (:notebooks)")
				+ QUERY_POSTFIX);
		if (!version.isAdmin()) {
			query.setParameter("notebooks", version.getReadableLabNotebooks());
		}
		Set<Entry<String, Object>> entries = parameters.entrySet();
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator
					.next();
			query.setParameter(entry.getKey(), entry.getValue());
		}

		List results;
		try {
			results = query.getResultList();
		} catch (PersistenceException e) {
			Throwable cause = e;
			while (null != cause.getCause()) {
				cause = cause.getCause();
			}
			cause.printStackTrace();
			throw (e);
		}
		return getChart(chartUrl, results);
	}

	/**
	 * Kpi.getChart
	 * 
	 * @param chartUrl
	 * @param results
	 * @return
	 */
	private static BarChartBean getChart(final String chartUrl, List results) {
		final Map<String, SliceBean> succeeded = new HashMap();
		final Map<String, SliceBean> failed = new HashMap();
		final Map<String, Double> seconds = new HashMap(); // average time to
															// success
		for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
			Object[] row = (Object[]) iterator.next();
			final String protocol = (String) row[0];
			final String status = (String) row[1];
			// seconds is row[2];
			final Long count = (Long) row[3];
			if ("OK".equals(status)) {
				succeeded.put(protocol, new AttritionSliceBean(
						"Successful experiments for protocol: " + protocol,
						chartUrl + "&status=OK&protocol=" + protocol, "green",
						count.floatValue(), "OK"));
				seconds.put(protocol, ((Double) row[2]));
			} else if ("Failed".equals(status)) {
				failed.put(protocol, new AttritionSliceBean(
						"Failed experiments for protocol: " + protocol,
						chartUrl + "&status=Failed&protocol=" + protocol,
						"red", count.floatValue(), "Failed"));
				if (!seconds.containsKey(protocol)) {
					// no one seems to care about this number, but we can't
					// leave the width undefined
					seconds.put(protocol, ((Double) row[2]));
				}
			}

		}

		Set<String> protocols = new HashSet(succeeded.keySet());
		protocols.addAll(failed.keySet());
		List<String> protocolList = new ArrayList(protocols);
		final List<BarBean> bars = new ArrayList(protocols.size());
		for (Iterator iterator = protocolList.iterator(); iterator.hasNext();) {
			final String protocol = (String) iterator.next();
			bars.add(new BarBean() {

				@Override
				public String getCaption() {
					return protocol;
				}

				@Override
				public String getURL() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getToolTip() {
					return "Click to see report";
				}

				@Override
				public SliceBean[] getSlices() {
					SliceBean okSlice = succeeded.get(protocol);
					if (null == okSlice) {
						okSlice = EMPTY_SLICE;
					}
					SliceBean failedSlice = failed.get(protocol);
					if (null == failedSlice) {
						failedSlice = EMPTY_SLICE;
					}
					return new SliceBean[] { okSlice, failedSlice };
				}

				@Override
				public float getWidth() {
					return seconds.get(protocol).floatValue() / (60f * 60f);
				}
			});
		}

		Collections.sort(bars, LONGEST_FIRST);
		return new BarChartBean() {

			private String tooltip;
			private String url = chartUrl;

			@Override
			public String getCaption() {
				return "Attrition and average duration of these experiments, by protocol";
			}

			@Override
			public String getURL() {
				return url;
			}

			@Override
			public String getToolTip() {
				return this.tooltip;
			}

			@Override
			public String getXAxisCaption() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getYAxisCaption() {
				return "Protocol";
			}

			@Override
			public boolean getBarsRunInYDirection() {
				return true;
			}

			@Override
			public BarBean[] getBars() {
				return bars.toArray(new BarBean[] {});
			}

			@Override
			public void setURL(String url) {
				this.url = url;
			}

			@Override
			public void setToolTip(String tooltip) {
				this.tooltip = tooltip;
			}
		};
	}

}
