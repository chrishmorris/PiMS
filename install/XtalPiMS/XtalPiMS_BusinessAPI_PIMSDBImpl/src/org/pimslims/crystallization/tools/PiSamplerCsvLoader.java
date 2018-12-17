package org.pimslims.crystallization.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.refdata.ScreenUtility;
import org.pimslims.csv.CsvParser;

/**
 * PiSamplerCsvLoader Loads CSV files made by Fabrice Gorrec's screen design
 * service http://pisampler.mrc-lmb.cam.ac.uk/index.html
 */
public class PiSamplerCsvLoader extends ScreenCsvLoader {

	private static final Pattern CONDITION_NAME = Pattern.compile("^(.*)Name$");

	public PiSamplerCsvLoader(DataStorage dataStorage, CsvParser parser,
			String screenName) {
		super(dataStorage, parser, screenName);
	}

	@Override
	protected void processHeaders(CsvParser parser) throws IOException {
		this.componentLabels = new ArrayList(3);
		String[] labels = parser.getLabels();
		this.wellLabel = labels[0];
		for (int i = 1; i < labels.length; i++) {
			Matcher m = CONDITION_NAME.matcher(labels[i]);
			if (m.matches()) {
				componentLabels.add(m.group(1));
			}
		}
	}

	@Override
	protected ComponentQuantity getComponentQuantity(CsvParser parser,
			String label) {
		ComponentQuantity ret = new ComponentQuantity();
		String unit = parser.getValueByLabel(label + "Concentration Unit");
		ret.processUnit(unit);
		double q = Double.parseDouble(parser.getValueByLabel(label
				+ "Concentration"));
		ret.setQuantity(q);
		final Component component = new Component();
		String alias = parser.getValueByLabel(label + "Name");
		component.setChemicalName(ScreenUtility.getChemicalName(alias,
				((DataStorageImpl) this.dataStorage).getVersion()));
		ret.setComponent(component);
		try {
			ret.setComponentType(parser.getValueByLabel(label + "Type"));
		} catch (IllegalArgumentException e) {
			// no such column, so can't set type
		}
		return ret;
	}

}
