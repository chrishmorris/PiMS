/**
 * xtalPiMS Business API - PIMSDB Impl org.pimslims.crystallization.tools OrderXmlLoaderTest.java
 * 
 * @author cm65
 * @date 18 May 2010
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2010 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.tools;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Group;
import org.pimslims.business.core.model.Organisation;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.PlateType;
import org.pimslims.business.crystallization.service.PlateExperimentService;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.datastorage.DataStorageFactory;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;

/**
 * OrderXmlLoaderTest
 * 
 */
public class OrderXmlLoaderTest extends AbstractXtalTest {

	/**
	 * DEPARTMENT String
	 */
	private static final String DEPARTMENT = "BI Xray Goldman";

	/**
	 * GROUP_NAME String
	 */
	static final String GROUP_NAME = "BI XRay Goldman";

	/**
	 * EMAIL String
	 */
	private static final String EMAIL = "veli-pekka.kestila@helsinki.fi";

	/**
	 * UNIQUE String
	 */
	private static final String UNIQUE = "oxl" + System.currentTimeMillis();

	private static final String ONE_PLATE = "        <tns:screens>\r\n"
			+ "            <!-- Every screen name means a separate plate -->\r\n"
			+ "            <tns:screen>\r\n"
			+ "                <tns:name>X-Tal Factorial</tns:name>\r\n"
			+ "                <tns:barcode>" + BARCODE + "</tns:barcode>\r\n"
			+ "            </tns:screen>\r\n" + "        </tns:screens>\r\n";

	private static final String TWO_PLATES = "        <tns:screens>\r\n"
			+ "            <!-- Every screen name means a separate plate -->\r\n"
			+ "            <tns:screen>\r\n"
			+ "                <tns:name>X-Tal Factorial</tns:name>\r\n"
			+ "                <tns:barcode>" + BARCODE + "one"
			+ "</tns:barcode>\r\n" + "            </tns:screen>\r\n"
			+ "            <tns:screen>\r\n"
			+ "                <tns:name>X-Tal Factorial</tns:name>\r\n"
			+ "                <tns:barcode>" + BARCODE + "two"
			+ "</tns:barcode>\r\n" + "            </tns:screen>\r\n"
			+ "        </tns:screens>\r\n";

	private static final String DESCRIPTION = "description " + UNIQUE;

	private static final String SAMPLE_NAME = "s" + UNIQUE;

	private static final String SUFFIX = "        <tns:samples>\r\n"
			+ "            <tns:sample>\r\n"
			+ "                <tns:name>"
			+ SAMPLE_NAME
			+ "</tns:name>\r\n"
			+ "                    <!-- mg/ml -->\r\n"
			+ "                <tns:concentration>5</tns:concentration>\r\n"
			+ "                    <!-- in ul -->\r\n"
			+ "                <tns:volume>23</tns:volume>\r\n"
			+ "                <tns:toxicity><!-- Free text describing sample or buffer toxicity --></tns:toxicity>\r\n"
			+ "                <tns:bufferComposition><!-- free text --></tns:bufferComposition>\r\n"
			+ "            </tns:sample>\r\n" + "        </tns:samples>\r\n"
			+ "    </tns:experimentInfo>\r\n" + "    <tns:extraInfo>"
			+ DESCRIPTION + "    </tns:extraInfo>\r\n"
			+ "</tns:experimentOrder>\r\n" + "";

	private static final String DATE = "2010-04-30";

	private static final String USER_NAME = "user" + UNIQUE;

	private static final String TYPE_NAME = "pt" + UNIQUE;

	private static final String ORGANISATION = "org" + UNIQUE;

	private static final String PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
			+ "<tns:experimentOrder xmlns:tns=\"http://www.helsinki.fi/~vpkestil/schemas/orderSchema\" \r\n"
			+ "            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n"
			+ "            xsi:schemaLocation=\"http://www.helsinki.fi/~vpkestil/schemas/orderSchema ../XML_Schemas/orderSchema.xsd \">\r\n"
			+ "    <tns:orderDate>"
			+ DATE
			+ "</tns:orderDate>\r\n"
			+ "    <!-- Username will be same as username in XtalPims -->\r\n"
			+ "    <tns:user new=\"true\">"
			+ USER_NAME
			+ "</tns:user>\r\n"
			+ "    <tns:contactInfo>\r\n"
			+ "        <tns:firstName>Veli-Pekka</tns:firstName>\r\n"
			+ "        <tns:lastName>Kestilä</tns:lastName>\r\n"
			+ "        <tns:department>"
			+ GROUP_NAME
			+ "</tns:department>\r\n"
			+ "        <tns:street>PL65</tns:street>\r\n"
			+ "        <tns:postcode>00014</tns:postcode>\r\n"
			+ "        <!-- Yes, the city part in finnish mail address can be the name of the company\r\n"
			+ "            if you pay for it. -->\r\n"
			+ "        <tns:city>University of Helsinki</tns:city>\r\n"
			+ "        <tns:email>"
			+ EMAIL
			+ "</tns:email>\r\n"
			+ "        <tns:phone>+358503000014</tns:phone>\r\n"
			+ "    </tns:contactInfo>\r\n"
			+ "    <tns:billingInfo>\r\n"
			+ "        <tns:name>Adrian Goleman H919</tns:name>\r\n"
			+ "        <tns:department>"
			+ ORGANISATION
			+ "</tns:department>\r\n"
			+ "        <tns:street>PL65</tns:street>\r\n"
			+ "        <tns:postcode>FI-00014</tns:postcode>\r\n"
			+ "        <tns:city>University of Helsinki</tns:city>\r\n"
			+ "    </tns:billingInfo>\r\n"
			+ "    <tns:experimentInfo>\r\n"
			+ "        <!-- drop size is in ul -->\r\n"
			+ "        <tns:dropSize>0.2</tns:dropSize>\r\n"
			+ "        <tns:plateType>"
			+ TYPE_NAME
			+ "</tns:plateType>\r\n"
			+ "        <tns:temperature>+4C</tns:temperature>\r\n"
			+ "        <tns:project></tns:project>\r\n";

	public OrderXmlLoaderTest(final String testName) {
		super(testName, DataStorageFactory.getDataStorageFactory()
				.getDataStorage());
	}

	public void testSaveOrder() throws UnsupportedEncodingException,
			BusinessException {
		this.dataStorage.openResources("administrator");
		try {
			save(this.dataStorage);

			// search with BARCODE
			final PlateExperimentService service = this.dataStorage
					.getPlateExperimentService();
			PlateExperimentView pv = findPlateView(service, BARCODE);

			// check the plate attributes
			assertEquals(BARCODE, pv.getBarcode());
			assertEquals(DESCRIPTION, pv.getDescription());
			assertEquals(TYPE_NAME, pv.getPlateType());
			assertEquals(USER_NAME, pv.getRunBy()); // that doesn't seem right
			assertEquals(SAMPLE_NAME, pv.getSampleName());
			// could assertEquals(4f, pv.getTemperature());
			assertEquals(DATE,
					XmlLoader.DATE_FORMAT.format(pv.getCreateDate().getTime()));
			assertEquals(GROUP_NAME, pv.getGroup());
			assertEquals(USER_NAME, pv.getOwner());

			// TODO check drop size and screen name

			// TODO check sample details
			// LATER better to pass construct name. Is this "project"?
		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	private PlateExperimentView findPlateView(
			final PlateExperimentService service, String barcode)
			throws BusinessException {
		final BusinessCriteria criteria = new BusinessCriteria(service);
		final BusinessCriterion criterion = BusinessExpression.Equals(
				PlateExperimentView.PROP_BARCODE, barcode, true);
		criteria.add(criterion);
		Collection<PlateExperimentView> result = service.findViews(criteria);
		assertNotNull(result);
		assertEquals(1, result.size());
		PlateExperimentView pv = result.iterator().next();
		return pv;
	}

	// TODO test with existing user

	public void testSaveTwoPlates() throws UnsupportedEncodingException,
			BusinessException {
		this.dataStorage.openResources("administrator");
		try {
			doSave(this.dataStorage, PREFIX + TWO_PLATES + SUFFIX);

			final PlateExperimentService service = this.dataStorage
					.getPlateExperimentService();
			findPlateView(service, BARCODE + "one");
			findPlateView(service, BARCODE + "two");

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testSaveUser() throws BusinessException,
			UnsupportedEncodingException {
		this.dataStorage.openResources("administrator");
		try {
			save(this.dataStorage);
			PersonService service = this.dataStorage.getPersonService();
			org.pimslims.business.core.model.Person user = service
					.findByUsername(USER_NAME);
			assertNotNull(user);
			// could assertEquals(EMAIL, user.getEmailAddress());
			assertEquals("Kestilä", user.getFamilyName()); // tests for UTF-8
			assertEquals("Veli-Pekka", user.getGivenName());
			// could assertEquals("+358503000014", user.getPhoneNumber());
			assertEquals(USER_NAME, user.getUsername());
			assertEquals("", user.getMailingAddress()); // TODO fix this test

			assertEquals(1, user.getGroups().size());
			Group group = user.getGroups().iterator().next();
			assertEquals(GROUP_NAME, group.getName());
			Organisation organisation = group.getOrganisation();
			assertNotNull(organisation);
			assertEquals(ORGANISATION, organisation.getName());
			/*
			 * could assertEquals("Adrian Goldman H919",
			 * organisation.getContactName()); assertEquals("PL65",
			 * organisation.getStreet()); assertEquals("University of Helsinki",
			 * organisation.getTown());
			 */

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testSaveSample() throws BusinessException,
			UnsupportedEncodingException {
		this.dataStorage.openResources("administrator");
		try {
			save(this.dataStorage);

			SampleService service = this.dataStorage.getSampleService();
			assertNotNull(service);
			Sample sample = service.findByName(SAMPLE_NAME);
			assertNotNull(sample);
			// could assertEquals(5d, sample.getConcentration());

			// could check volume
			// could check buffer composition - this is the protein additive

			// LATER better to pass construct name
			// LATER better to pass safety details

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	public void testAccessRights() throws BusinessException,
			UnsupportedEncodingException {
		this.dataStorage.openResources("administrator");
		try {
			save(this.dataStorage);
			ReadableVersion version = ((DataStorageImpl) this.dataStorage)
					.getVersion();

			String owner = version
					.findFirst(org.pimslims.model.sample.Sample.class, "name",
							SAMPLE_NAME).get_Owner();
			assertEquals(GROUP_NAME, owner);
			owner = version.findFirst(org.pimslims.model.holder.Holder.class,
					"name", BARCODE).get_Owner();
			assertEquals(GROUP_NAME, owner);
			owner = version.findFirst(
					org.pimslims.model.experiment.ExperimentGroup.class,
					"name", BARCODE).get_Owner();
			assertEquals(GROUP_NAME, owner);

		} finally {
			this.dataStorage.abort(); // not testing persistence
		}
	}

	static void save(DataStorage ds) throws BusinessException,
			UnsupportedEncodingException {
		String string = PREFIX + ONE_PLATE + SUFFIX;
		doSave(ds, string);
	}

	private static void doSave(DataStorage ds, String string)
			throws UnsupportedEncodingException, BusinessException {
		final InputStream input = new ByteArrayInputStream(
				string.getBytes("UTF-8"));
		TrialService ts1 = ds.getTrialService();
		PlateType type = new PlateType();
		type.setColumns(12);
		type.setRows(8);
		type.setSubPositions(2);
		type.setReservoir(2);
		type.setName(TYPE_NAME);
		ts1.create(type);
		new OrderXmlLoader(input, ds).save();
		ds.flush();
	}

}
