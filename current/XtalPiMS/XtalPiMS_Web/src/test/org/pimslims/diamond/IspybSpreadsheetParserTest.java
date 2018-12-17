package org.pimslims.diamond;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.pimslims.diamond.ShipmentBean.DewarBean;
import org.pimslims.diamond.ShipmentBean.PuckBean;
import org.pimslims.diamond.ShipmentBean.SampleBean;

public class IspybSpreadsheetParserTest extends TestCase {

	private static final String UNIQUE = "isp" + System.currentTimeMillis();

	public void testParse() throws IOException {
		InputStream is = this.getClass().getResourceAsStream(
				"IspybSpreadsheetExample.xls");
		assertNotNull(is);
		ShipmentBean shipment = new IspybSpreadsheetParser().getBean(is);
		// TODO assertEquals(2011,
		// shipment.getShippingDate().get(Calendar.YEAR));
		Collection<DewarBean> dewars = shipment.getDewars();
		assertEquals(2, dewars.size());
		Iterator<DewarBean> iterator = dewars.iterator();
		DewarBean dewar = iterator.next();
		if (!"Dewar2".equals(dewar.getName())) {
			dewar = iterator.next();
		}
		assertEquals("Dewar2", dewar.getName());
		Collection<PuckBean> pucks = dewar.getPucks();
		assertEquals(7, pucks.size());
		PuckBean puck = pucks.iterator().next();
		assertEquals("Puck1", puck.getName());
		Collection<SampleBean> samples = puck.getSamples();
		assertEquals(1, samples.size());
		SampleBean sample = samples.iterator().next();
		assertEquals("protein1", sample.get("Protein Name"));
		assertEquals(30.0d, sample.get("alpha"));
		assertEquals("test", sample.get("Comments"));
	}

	public void TODOtestWrite() throws IOException {
		DewarBean dewar = new DewarBean(UNIQUE + "d");
		PuckBean puck = new PuckBean(UNIQUE + "p1");
		Map<String, Object> properties = new HashMap(); // TODO add a property
		puck.add(new SampleBean(properties));
		dewar.add(puck);
		dewar.add(new PuckBean(UNIQUE + "p0"));
		ShipmentBean toWrite = new ShipmentBean(Collections.singleton(dewar));
		toWrite.setShippingDate(Calendar.getInstance());
		// TODO etc
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new IspybSpreadsheetWriter().write(toWrite, (OutputStream) os);
		os.close();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		ShipmentBean parsed = new IspybSpreadsheetParser().getBean(is);
		assertEquals(toWrite.getShippingDate(), parsed.getShippingDate());
		// TODO test Tracking Number, Courier Name
		assertEquals(1, parsed.getDewars().size());
		List<PuckBean> pucks = parsed.getDewars().iterator().next().getPucks();
		assertEquals(2, pucks.size());
		Iterator<PuckBean> iterator = pucks.iterator();
		PuckBean first = iterator.next();
		PuckBean second = iterator.next();
		assertEquals(0, second.getSamples().size());
		assertEquals(1, first.getSamples().size());
		SampleBean sample = first.getSamples().iterator().next();
		// TODO test a property
	}

}
