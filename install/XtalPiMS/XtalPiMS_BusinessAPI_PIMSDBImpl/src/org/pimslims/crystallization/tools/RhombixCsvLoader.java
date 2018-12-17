package org.pimslims.crystallization.tools;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.Component;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.refdata.ScreenUtility;
import org.pimslims.csv.CsvParser;

public class RhombixCsvLoader extends ScreenCsvLoader {

	public RhombixCsvLoader(DataStorage dataStorage, CsvParser parser,
			String screenName) {
		super(dataStorage, parser, screenName);
	}

	@Override
	protected void processHeaders(CsvParser parser) throws IOException {
		String[] labels = parser.getLabels();
		assert "Rhombix Import Template".equals(labels[0]);
		wellLabel = labels[3];
		parser.getLine(); // skip second header line
		this.componentLabels.add(labels[1]);
	}

	/**
	 * COMPONENT_DESCRIPTION Pattern e.g. Citric Acid (1.00M pH 3.50), Buffer
	 */
	static final Pattern COMPONENT_DESCRIPTION = Pattern
			.compile("^(.*)? \\(([0-9\\.]*)(.).*?\\), (.*)$");

	@Override
	protected ComponentQuantity getComponentQuantity(CsvParser parser,
			String label) {
		ComponentQuantity ret = new ComponentQuantity();
		final Component component = new Component();
		String description = parser.getValueByLabel(label);
		Matcher m = COMPONENT_DESCRIPTION.matcher(description);
		assert m.matches();
		component.setChemicalName(ScreenUtility.getChemicalName(m.group(1),
				((DataStorageImpl) this.dataStorage).getVersion()));
		String string = m.group(2);
		double q = Double.parseDouble(string);
		ret.setQuantity(q);
		ret.processUnit(m.group(3));
		ret.setComponent(component);
		ret.setComponentType(m.group(4));
		return ret;
	}

}
