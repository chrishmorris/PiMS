package org.pimslims.diamond;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.pimslims.diamond.ShipmentBean.PuckBean;
import org.pimslims.spreadsheet.SpreadSheet;

public class IspybSpreadsheetParser {

	public ShipmentBean getBean(InputStream is) throws IOException {
		try {
			SpreadSheet book = new SpreadSheet(is);
			// Note there are two more sheets than expected
			// System.out.println("Sheets: " + book.getNumberOfSheets());
			Object[][] sheet0 = book.toArray(0);
			assert "ehtpx5".equals(sheet0[0][0]) : "Spreadsheet format not recognised";
			// TODO get shipping date, Courier Name, Tracking Number
			Map<String, ShipmentBean.DewarBean> dewars = new HashMap();
			for (int i = 0; i < book.getNumberOfSheets(); i++) {
				Object[][] sheet = book.toArray(i);
				int[] coords = SpreadSheet.find(sheet, "Puck");
				PuckBean puck = new ShipmentBean.PuckBean(
						(String) sheet[coords[0]][1 + coords[1]]);
				coords = SpreadSheet.find(sheet, "Dewar");
				String dewar = (String) sheet[coords[0]][1 + coords[1]];
				if (!dewars.containsKey(dewar)) {
					dewars.put(dewar, new ShipmentBean.DewarBean(dewar));
				}
				dewars.get(dewar).add(puck);

				/*
				 * process samples A header row consists of keywords for
				 * columns, like an embedded csv. Except the unit cells
				 * properties are in a merge cell. They come out like
				 * this:,a,Unit Cell b c,,alpha,beta,gamma,
				 */
				coords = SpreadSheet.find(sheet, "Sample position");
				Object[] keyRow = sheet[coords[0]];
				// printRow(sheet[coords[0]]);
				Map<String, Integer> keys = getSampleKeys(coords, keyRow);
				int row = 1 + coords[0];
				while (row <= sheet.length) {
					if (null == sheet[row][1]
							|| "".equals(((String) sheet[row][1]).trim())) {
						break;
					}
					// System.out.println(sheet[row][1]);
					// printRow(sheet[row]);
					Map<String, Object> properties = new HashMap();
					Object[] sampleRow = sheet[row];
					Set<Entry<String, Integer>> entrySet = keys.entrySet();
					for (Iterator iterator = entrySet.iterator(); iterator
							.hasNext();) {
						Entry<String, Integer> entry = (Entry<String, Integer>) iterator
								.next();
						Object value = sampleRow[entry.getValue()];
						properties.put(entry.getKey(), value);
					}
					puck.add(new ShipmentBean.SampleBean(properties));
					row++;
				}
			}
			ShipmentBean ret = new ShipmentBean(dewars.values());
			return ret;
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
	}

	static Map<String, Integer> getSampleKeys(int[] coords, Object[] keyRow) {
		Map<String, Integer> keys = new HashMap();
		for (int column = coords[1]; column < keyRow.length;) {
			if ("a".equals(keyRow[column])) {
				keys.put("a", column++);
				keys.put("b", column++);
				keys.put("c", column++);
				keys.put("alpha", column++);
				keys.put("beta", column++);
				keys.put("gamma", column++);
			} else {
				keys.put((String) keyRow[column], column);
				column++;
			}
		}
		return keys;
	}

	// for testing
	private void printRow(Object[] strings) {
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i].toString();
			System.out.print(string + ",");
		}
		System.out.print("\n");
	}
}
