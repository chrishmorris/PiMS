package org.pimslims.crystallization.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.crystallization.model.ComponentQuantity;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.csv.CsvParser;

public abstract class ScreenCsvLoader {

	protected String wellLabel;
	protected ArrayList<String> componentLabels = new ArrayList(3);
	protected final DataStorage dataStorage;
	protected final CsvParser parser;
	protected final String name;

	public ScreenCsvLoader(DataStorage dataStorage, CsvParser parser,
			String screenName) {
		this.dataStorage = dataStorage;
		this.parser = parser;
		this.name = screenName;
	}

	protected abstract void processHeaders(CsvParser parser) throws IOException;

	protected abstract ComponentQuantity getComponentQuantity(CsvParser parser,
			String label);

	public Screen load() throws IOException {

		// process the headers
		processHeaders(parser);

		Map<WellPosition, Condition> conditions = new HashMap(96);
		while (parser.getLine() != null) {
			String wellName = parser.getValueByLabel(wellLabel);
			if ("".equals(wellName)) {
				continue; // for odd last line of Rhombix CSV
			}
			WellPosition well = new WellPosition(wellName);
			Condition condition = conditions.get(well);
			if (null == condition) {
				condition = new Condition();
				condition.setLocalName(this.name + ":" + wellName);
				conditions.put(well, condition);
			}
			for (Iterator iterator = componentLabels.iterator(); iterator
					.hasNext();) {
				String label = (String) iterator.next();
				ComponentQuantity cq = getComponentQuantity(parser, label);
				condition.addComponent(cq);
			}
		}
		Screen ret = new Screen();
		ret.setConditionPositions(conditions);
		return ret;
	}

	public static ScreenCsvLoader getLoader(DataStorage dataStorage2,
			InputStream input, String screenName) throws IOException {

		Reader reader = new InputStreamReader(input);
		CsvParser parser = new CsvParser(reader);
		String[] labels = parser.getLabels();
		if (labels[0].contains("Rhombix")) {
			return new RhombixCsvLoader(dataStorage2, parser, screenName);
		}
		return new PiSamplerCsvLoader(dataStorage2, parser, screenName);
	}

}
