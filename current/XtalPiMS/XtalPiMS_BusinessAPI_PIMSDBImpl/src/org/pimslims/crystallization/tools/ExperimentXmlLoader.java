package org.pimslims.crystallization.tools;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.w3c.dom.Element;

/**
 * ExperimentXmlLoader TODO share a superclass with OrderXmlLoader
 */
public class ExperimentXmlLoader extends XmlLoader {

	public ExperimentXmlLoader(InputStream input, DataStorage dataStorage) {
		super(dataStorage, parse(input));

	}

	protected TrialPlate getTrialPlate() throws BusinessException {
		TrialService ts = dataStorage.getTrialService();
		String barcode = this.getChildContent(document, "barcode");
		TrialPlate plate = ts.findTrialPlate(barcode);
		assert null != plate : "No order found for bar code: " + barcode;
		plate.setDescription(this.getChildContent(document, "description"));
		return plate;
	}

	@Override
	protected String getNameSpace() {
		return "http://www.helsinki.fi/~vpkestil/schemas/experimentSchema";
	}

	@Override
	public String save() throws BusinessException {
		final TrialPlate plate = this.getTrialPlate();

		Element wells = getChildElement(this.document, "plate");
		assert null != wells;
		processPlate(wells, plate);
		TrialService ts = dataStorage.getTrialService();
		ts.updateTrialPlate(plate);
		List<TrialDrop> drops = plate.getTrialDrops();
		for (Iterator iterator = drops.iterator(); iterator.hasNext();) {
			TrialDrop drop = (TrialDrop) iterator.next();
			ts.updateTrialDrop(drop);
		}
		return plate.getBarcode();
	}

	private void processPlate(Element wells, TrialPlate plate)
			throws BusinessException {
		TrialService ts = this.dataStorage.getTrialService();

		for (Iterator<Element> iterator = this.getChildrenIterator(wells,
				"dropsite"); iterator.hasNext();) {
			Element element = iterator.next();

			String row = element.getAttribute("row");
			String column = element.getAttribute("column");
			WellPosition well = new WellPosition(row + column);

			for (Iterator<Element> it2 = this
					.getChildrenIterator(element, null); it2.hasNext();) {
				Element drop = it2.next();
				if ("drop".equals(drop.getLocalName())) {
					int subPosition = getIntAttribute(drop, "location");
					WellPosition position = new WellPosition(well, subPosition);
					TrialDrop trialDrop = ts.findTrialDrop(plate.getBarcode(),
							position);
					plate.addTrialDrop(trialDrop);
					processDrop(trialDrop, drop);
				}
			}

		}
	}

	private void processDrop(TrialDrop trialDrop, Element drop)
			throws BusinessException {
		TrialService ts = this.dataStorage.getTrialService();

		for (Iterator<Element> iterator = this.getChildrenIterator(drop, null); iterator
				.hasNext();) {
			Element element = iterator.next();
			if ("sample".equals(element.getLocalName())) {
				Sample sample = getSample(element);
				SampleQuantity sq = new SampleQuantity(sample, 0d, "L");
				trialDrop.addSample(sq);
			} else if ("ratios".equals(element.getLocalName())) {
				// TODO process ratios
			} else if ("additive".equals(element.getLocalName())) {
				// TODO process additives

			}
		}
		ts.updateTrialDrop(trialDrop);
	}

	private Sample getSample(Element element) throws BusinessException {
		String name = getChildContent(element, "name");
		SampleService ss = this.dataStorage.getSampleService();
		Sample sample = ss.findByName(name);
		if (null == sample) {
			sample = new Sample();
			sample.setName(name);
			sample.setDescription(getChildContent(element, "description"));
			ss.create(sample);
		}
		return sample;
	}

}
