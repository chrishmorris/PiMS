package org.pimslims.diamond;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.pimslims.diamond.ShipmentBean.DewarBean;
import org.pimslims.diamond.ShipmentBean.PuckBean;
import org.pimslims.diamond.ShipmentBean.SampleBean;
import org.pimslims.spreadsheet.SpreadSheet;

public class IspybSpreadsheetWriter {

	public void write(ShipmentBean shipment, OutputStream os)
			throws IOException {

		InputStream is = this.getClass().getResourceAsStream(
				"IspybSpreadsheetTemplate.xls");
		try {
			SpreadSheet book = new SpreadSheet(is);
			Object[][] sheet = book.toArray(0);
			String name = "Shipping Date";
			Calendar c = shipment.getShippingDate();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
			String shipDate = df.format(c.getTime());
			set(sheet, name, shipDate);

			set(sheet, "Courier Name", shipment.getShipper());
			set(sheet, "Tracking Number", shipment.getTrackingNumber());

			int puckPositions = 16; // Diamond pucks
			int page = 0;
			List<DewarBean> dewars = shipment.getDewars();
			for (Iterator iterator = dewars.iterator(); iterator.hasNext();) {
				DewarBean dewar = (DewarBean) iterator.next();
				List<PuckBean> pucks = dewar.getPucks();
				for (Iterator iterator2 = pucks.iterator(); iterator2.hasNext();) {
					PuckBean puck = (PuckBean) iterator2.next();
					set(sheet, "Puck", puck.getName());
					set(sheet, "Dewar", dewar.getName());

					// find where to put the values for the samples
					int[] coords = SpreadSheet.find(sheet, "Sample position");
					Object[] keyRow = sheet[coords[0]];
					Map<String, Integer> keys = IspybSpreadsheetParser
							.getSampleKeys(coords, keyRow);
					List<SampleBean> samples = puck.getSamples();
					int firstSampleRow = coords[0];
					int lastColumn = keys.get("Comments");

					// Wipe the sheet
					for (int row = 1; row <= puckPositions; row++) {
						for (int col = 1; col <= lastColumn; col++) {
							sheet[row + firstSampleRow][col] = null;
						}
					}

					for (Iterator iterator3 = samples.iterator(); iterator3
							.hasNext();) {
						SampleBean sample = (SampleBean) iterator3.next();
						int sampleRow = firstSampleRow
								+ (Integer) sample.get("puckPosition");

						// Set<Entry<String, Integer>> entrySet =
						// keys.entrySet();
						// for (Iterator iterator4 = entrySet.iterator();
						// iterator4
						// .hasNext();) {
						// Entry<String, Integer> entry = (Entry<String,
						// Integer>) iterator4
						// .next();
						// sheet[sampleRow][entry.getValue()] = sample
						// .get(entry.getKey());
						// System.out.println(entry.getKey() + ": "
						// + sample.get(entry.getKey()));
						//
						// }
						sheet[sampleRow][keys.get("Sample position")] = sample
								.get("puckPosition");
						sheet[sampleRow][keys.get("Protein Name")] = sample
								.get("targetName");
						sheet[sampleRow][keys.get("Protein Acronym")] = sample
								.get("targetName");
						sheet[sampleRow][keys.get("Space Group")] = sample
								.get("spaceGroup");
						sheet[sampleRow][keys.get("Sample Name")] = sample
								.get("sampleName");
						String barcode = (String) sample.get("barcode");
						if (!barcode.equals("PLATESHIPMENT")
								&& !barcode.startsWith("pin_")) {
							sheet[sampleRow][keys.get("Pin Barcode")] = sample
									.get("barcode");
						}
						// 6 pre-observed resolution
						// 7 needed resolution
						// 8 Oscillation range
						sheet[sampleRow][keys.get("Experiment Type")] = sample
								.get("experimentType");
						if (((String) sample.get("heavyAtom")).length() == 2) {
							sheet[sampleRow][keys.get("Anomalous Scatterer")] = sample
									.get("heavyAtom");
						}
						if (!"".equals(sample.get("a"))) {
							sheet[sampleRow][keys.get("a")] = new Double(
									Double.parseDouble((String) sample.get("a")));
						}
						if (!"".equals(sample.get("b"))) {
							sheet[sampleRow][keys.get("b")] = new Double(
									Double.parseDouble((String) sample.get("b")));
						}
						if (!"".equals(sample.get("c"))) {
							sheet[sampleRow][keys.get("c")] = new Double(
									Double.parseDouble((String) sample.get("c")));
						}
						if (!"".equals(sample.get("alpha"))) {
							sheet[sampleRow][keys.get("alpha")] = new Double(
									Double.parseDouble((String) sample
											.get("alpha")));
						}
						if (!"".equals(sample.get("beta"))) {
							sheet[sampleRow][keys.get("beta")] = new Double(
									Double.parseDouble((String) sample
											.get("beta")));
						}
						if (!"".equals(sample.get("gamma"))) {
							sheet[sampleRow][keys.get("gamma")] = new Double(
									Double.parseDouble((String) sample
											.get("gamma")));
						}
						// 17 Loop type
						// 18 Holder Length

						String shipComment = "";
						if (!"".equals(sample.get("comments"))
								&& !"".equals(sample.get("treatmentURL"))) {
							shipComment = sample.get("comments")
									+ " * xtalPiMS: "
									+ sample.get("treatmentURL");
						} else if (!"".equals(sample.get("comments"))) {
							shipComment = (String) sample.get("comments");
						} else if (!"".equals(sample.get("treatmentURL"))) {
							shipComment = (String) sample.get("treatmentURL");
						}

						String imageURL = (String) sample.get("imageURL");
						Integer pixelX = (Integer) (sample.get("pixelX"));
						Integer pixelY = (Integer) (sample.get("pixelY"));
						if (null != imageURL && !"".equals(imageURL)
								&& null != pixelX && null != pixelY) {
							shipComment += " pixelX: " + pixelX;
							shipComment += " pixelY: " + pixelY;
							shipComment += " Image: " + imageURL;
						}

						sheet[sampleRow][keys.get("Comments")] = shipComment;
					}
					// TODO Now clear remaining sample rows

					if (page == book.getNumberOfSheets()) {
						book.addSheet();
					}
					book.fromArray(page, sheet);
					page++;
				}
			}
			book.write(os);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		}
	}

	private void set(Object[][] sheet, String name, Object value) {
		int[] coords = SpreadSheet.find(sheet, name);
		sheet[coords[0]][1 + coords[1]] = value;
	}

}
