/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.tools ExperimentXmlLoader.java
 * 
 * @author cm65
 * @date 4 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.tools;

import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.GroupService;
import org.pimslims.business.core.service.OrganisationService;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.model.SampleQuantity;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.exception.BusinessException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * ExperimentXmlLoader
 * 
 */
public class OrderXmlLoader extends XmlLoader {

	private final Person user;

	/**
	 * Constructor for ExperimentXmlLoader
	 * 
	 * @param input
	 * @param input
	 * @param ds
	 * @throws BusinessException
	 */
	public OrderXmlLoader(InputStream input, DataStorage ds)
			throws BusinessException {
		super(ds, parse(input));
		assert "tns:experimentOrder".equals(document.getTagName()) : "Expecting <tns:experimentOrder>, got: <"
				+ document.getTagName() + ">";
		this.user = getUserDetails();

	}

	@Override
	protected String getNameSpace() {
		return "http://www.helsinki.fi/~vpkestil/schemas/orderSchema";
	}

	public Collection<TrialPlate> getTrialPlates() throws BusinessException {

		// process the order date
		Calendar createDate = null;
		try {
			String d = getChildContent(this.document, "orderDate");
			Date date = DATE_FORMAT.parse(d);
			createDate = Calendar.getInstance();
			createDate.setTime(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		String plateTypeName = getPlateType();
		TrialService ts = this.dataStorage.getTrialService();
		PlateType plateType = ts.findPlateType(plateTypeName);
		assert null != plateType : "Unknown plate type: " + plateTypeName;

		List<TrialPlate> ret = new ArrayList<TrialPlate>();

		// now process all the plates
		Element screenOrders = this.getChildElement(this.document, "screens");
		for (Iterator<Element> iterator = this.getChildrenIterator(
				screenOrders, "screen"); iterator.hasNext();) {
			Element element = iterator.next();

			TrialPlate plate = new TrialPlate(plateType);
			plate.buildAllTrialDrops();

			setPlateDetails(plate, element);
			plate.setOwner(user);
			plate.setCreateDate(createDate);
			ret.add(plate);
		}
		// process sample info
		setExperimentDetails(getChildElement(this.document, "experimentInfo"),
				ret);
		return ret;
	}

	/**
	 * ExperimentXmlLoader.setExperimentDetails
	 * 
	 * @param plate
	 * @param childElement
	 */
	private void setExperimentDetails(Element info,
			Collection<TrialPlate> plates) {
		// TODO drop size, project

		// TODO save temperature - it is a property of the instrument

		Element samples = getChildElement(info, "samples");
		for (Node sample = samples.getFirstChild(); sample != null; sample = sample
				.getNextSibling()) {
			if (sample instanceof Text) {
				// for some reason the set ignoring white space above fails
				continue;
			}
			Sample bean = new Sample();
			bean.setName(getChildContent((Element) sample, "name"));
			float concentration = getFloatContent(sample, "concentration");
			bean.setConcentration(concentration);
			float volume = getFloatContent(sample, "volume");
			final SampleQuantity sampleQuantity = new SampleQuantity(bean,
					volume / 1000000, "L");
			for (Iterator iterator = plates.iterator(); iterator.hasNext();) {
				TrialPlate plate = (TrialPlate) iterator.next();
				for (final TrialDrop trialDrop : plate.getTrialDrops()) {
					trialDrop.addSample(sampleQuantity);
				}
			}
		}
	}

	/**
	 * ExperimentXmlLoader.getUserDetails
	 * 
	 * @return
	 */
	private Person getUserDetails() {
		Person user = new Person();
		user.setUsername(getChildContent(this.document, "user"));
		user.setStartDate(Calendar.getInstance());

		// process the contact info
		Element ci = getChildElement(this.document, "contactInfo");
		user.setEmailAddress(getChildContent(ci, "email"));
		user.setFamilyName(getChildContent(ci, "lastName"));
		user.setGivenName(getChildContent(ci, "firstName"));
		user.setPhoneNumber(getChildContent(ci, "phone"));

		user.setDeliveryAddress("");

		// process the department info
		Organisation organisation = new Organisation();
		Group group = new Group();
		group.setOrganisation(organisation);
		group.setName(getChildContent(ci, "department"));
		user.addGroup(group);
		group.addUser(user);

		// process the organisation details
		Element bi = getChildElement(this.document, "billingInfo");
		organisation.setContactName(getChildContent(bi, "name"));
		organisation.setName(getChildContent(bi, "department"));

		return user;
	}

	/**
	 * ExperimentXmlLoader.setPlate
	 * 
	 * @param plate
	 * @param document
	 */
	private void setPlateDetails(TrialPlate plate, Element screenOrder) {
		String barcode = getChildContent(screenOrder, "barcode");
		plate.setBarcode(barcode);
		// TODO set screen name
		plate.setDescription(getChildContent(document, "extraInfo"));
	}

	private String getPlateType() {
		Element experiment = getChildElement(document, "experimentInfo");
		String childName = "plateType";
		return getChildContent(experiment, childName);
	}

	@Override
	public String save() throws BusinessException {
		PersonService ps = dataStorage.getPersonService();
		ps.create(user);
		Group group = user.getGroups().iterator().next();
		OrganisationService os = dataStorage.getOrganisationService();
		os.create(group.getOrganisation());
		GroupService gs = dataStorage.getGroupService();
		gs.create(group);
		TrialService ts = dataStorage.getTrialService();
		Collection<TrialPlate> trialPlates = this.getTrialPlates();
		for (Iterator iterator = trialPlates.iterator(); iterator.hasNext();) {
			TrialPlate plate = (TrialPlate) iterator.next();
			plate.setGroup(group);
			ts.saveTrialPlate(plate);
		}
		return group.getName();
	}

}
