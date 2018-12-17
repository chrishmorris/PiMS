package org.pimslims.crystallization.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.diamond.IspybSpreadsheetWriter;
import org.pimslims.diamond.ShipmentBean;
import org.pimslims.diamond.ShipmentBean.DewarBean;
import org.pimslims.diamond.ShipmentBean.PuckBean;
import org.pimslims.diamond.ShipmentBean.SampleBean;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.File;

public class ShipmentCreate extends XtalPIMSServlet {

	private static final String ShipmentLabNotebookName = "Shipments";

	public static final String DIFFRACTION_PROTOCOL = "ISPyB Diffraction";

	@Override
	public String getServletInfo() {
		return "Begin to create a new crystal shipment";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ShipmentCreate.doGet()");
		String destination = "/CrystalShipping/CreateShipment.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(destination);
		ReadableVersion version = this.getReadableVersion(request, response);
		boolean canShip = true;
		LabNotebook shipperLN = version.findFirst(LabNotebook.class,
				LabNotebook.PROP_NAME, ShipmentCreate.ShipmentLabNotebookName);
		if (null == shipperLN) {
			canShip = false;
		} else {
			// TODO
			// if no CRU permissions on shipperLN
			// canShip=false
		}
		version.abort();
		if (!canShip) {
			throw new ServletException(
					"Cannot create shipments - you need to have Create permissions on a Lab Notebook called '"
							+ ShipmentCreate.ShipmentLabNotebookName + "'");
		}
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ShipmentCreate.doPost()");
		DataStorageImpl dataStorage;
		String dewarHooks = request.getParameter("dewarHooks"); // should get
																// either this..
		String plateHooks = request.getParameter("plateHooks"); // ...or this
		String destination = request.getParameter("destination");
		String shipper = request.getParameter("shipper");
		String trackingNumber = request.getParameter("trackingNumber");
		try {
			dataStorage = (DataStorageImpl) openResources(request);
			String username = PIMSServlet.getUsername(request);
			JSONObject jo;
			if (null != dewarHooks) {
				jo = doPinShipment(dataStorage, dewarHooks, destination,
						shipper, trackingNumber, username);
			} else {
				jo = doPlateShipment(dataStorage, plateHooks, destination,
						shipper, trackingNumber, username);
			}
			System.out.println("jo.getBoolean(\"success\") = "
					+ jo.getBoolean("success"));

			if (jo.getBoolean("success")) {
				dataStorage.commit();
			} else {
				dataStorage.abort();
			}
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
		} catch (BusinessException e) {

			throw new ServletException(e);
		}
	}

	private JSONObject doPinShipment(DataStorageImpl dataStorage,
			String dewarHooks, String destination, String shipper,
			String trackingNumber, String username) throws IOException {
		/*
		 * TODO mihgt get plate hooks not dewar hooks. in do Post, separate them
		 * out. Refactor this method - split into "get shipment bean" and
		 * "process it" doPost should call appropriate "get shipment bean" then
		 * "process it"
		 */

		String[] hooks = dewarHooks.split(",");
		JSONObject errors = validate(dataStorage, hooks);
		if (errors != null) {
			return errors;
		}
		ShipmentBean sb = makeShipmentBean(dataStorage, hooks, shipper,
				trackingNumber);
		WritableVersion version = dataStorage.getWritableVersion();

		// Create the ExperimentGroup
		Calendar c = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
		String shipDate = df.format(c.getTime());
		String baseName = "Shipment to " + destination + ", " + shipDate + " ("
				+ username + ")";
		String shipmentName = baseName;
		ExperimentGroup existing = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, shipmentName);
		int count = 1;
		while (null != existing) {
			count++;
			existing = version.findFirst(ExperimentGroup.class,
					ExperimentGroup.PROP_NAME, shipmentName + " " + count);
		}
		if (1 != count) {
			shipmentName = shipmentName + " " + count;
		}
		ExperimentGroup grp;
		try {
			Map<String, Object> attrs = new HashMap<String, Object>();
			attrs.put(ExperimentGroup.PROP_NAME, shipmentName);
			attrs.put(ExperimentGroup.PROP_PURPOSE, "Shipment to synchrotron");
			grp = version.create(ShipmentCreate.ShipmentLabNotebookName,
					ExperimentGroup.class, attrs);
			System.out.println("New expt group: " + grp.get_Hook());

			JSONObject jo = makeExperimentsAndSpreadsheet(version, sb, grp);

			return jo;
		} catch (ConstraintException e) {
			return makeErrorJSON("Tried to create Experiment Group called '"
					+ shipmentName
					+ "' but there was a problem (ConstraintException): \n\n"
					+ e.getMessage());
		} catch (AccessException e) {
			return makeErrorJSON("Tried to create Experiment Group called '"
					+ shipmentName
					+ "' but there was an access exception. You need permissions on the '"
					+ this.ShipmentLabNotebookName + "' Lab Notebook.");
		}
	}

	private JSONObject doPlateShipment(DataStorageImpl dataStorage,
			String plateHooks, String destination, String shipper,
			String trackingNumber, String username) throws IOException {
		/*
		 * TODO mihgt get plate hooks not dewar hooks. in do Post, separate them
		 * out. Refactor this method - split into "get shipment bean" and
		 * "process it" doPost should call appropriate "get shipment bean" then
		 * "process it"
		 */

		String[] hooks = plateHooks.split(",");
		ShipmentBean sb = makePlateShipmentBean(dataStorage, hooks, shipper,
				trackingNumber);
		WritableVersion version = dataStorage.getWritableVersion();

		// Create the ExperimentGroup
		Calendar c = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
		String shipDate = df.format(c.getTime());
		String baseName = "Shipment to " + destination + ", " + shipDate + " ("
				+ username + ")";
		String shipmentName = baseName;
		ExperimentGroup existing = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, shipmentName);
		int count = 1;
		while (null != existing) {
			count++;
			existing = version.findFirst(ExperimentGroup.class,
					ExperimentGroup.PROP_NAME, shipmentName + " " + count);
		}
		if (1 != count) {
			shipmentName = shipmentName + " " + count;
		}
		ExperimentGroup grp;
		try {
			Map<String, Object> attrs = new HashMap<String, Object>();
			attrs.put(ExperimentGroup.PROP_NAME, shipmentName);
			attrs.put(ExperimentGroup.PROP_PURPOSE, "Shipment to synchrotron");
			grp = version.create(ShipmentCreate.ShipmentLabNotebookName,
					ExperimentGroup.class, attrs);
			System.out.println("New expt group: " + grp.get_Hook());

			JSONObject jo = makeExperimentsAndSpreadsheet(version, sb, grp);

			return jo;
		} catch (ConstraintException e) {
			return makeErrorJSON("Tried to create Experiment Group called '"
					+ shipmentName
					+ "' but there was a problem (ConstraintException): \n\n"
					+ e.getMessage());
		} catch (AccessException e) {
			return makeErrorJSON("Tried to create Experiment Group called '"
					+ shipmentName
					+ "' but there was an access exception. You need permissions on the '"
					+ this.ShipmentLabNotebookName + "' Lab Notebook.");
		}
	}

	private JSONObject validate(DataStorageImpl dataStorage, String[] dewarHooks) {
		List<String> errors = new LinkedList<String>();
		if (0 == dewarHooks.length) {
			errors.add("No dewars in shipment");
		}
		ReadableVersion version = dataStorage.getVersion();
		for (int i = 0; i < dewarHooks.length; i++) {
			String hook = dewarHooks[i];
			Holder dewar = version.get(hook);
			if (null == dewar) {
				errors.add("No dewar with hook " + hook);
				continue;
			} else if (null == dewar.getHolderType()) {
				errors.add("Container with hook " + hook
						+ " has no holder category (should be 'Dewar')");
				continue;
			} else if (!"Dewar".equals(dewar.getHolderType().getName())) {
				errors.add("Container with hook " + hook + " is not a Dewar");
				continue;
			}
			// for each dewar, verify that it has pucks
			// For each puck, verify that it has pins
			// For each pin, validate all fields
		}

		if (errors.isEmpty()) {
			return null;
		} else {
			return makeErrorJSON(errors);
		}
	}

	private ShipmentBean makePlateShipmentBean(DataStorageImpl dataStorage,
			String[] plateHooks, String shipper, String trackingNumber) {
		DewarBean fakeDewar = new DewarBean("PLATESHIPMENT");
		Set<DewarBean> dewars = new HashSet<DewarBean>();
		WritableVersion version = dataStorage.getWritableVersion();
		for (int i = 0; i < plateHooks.length; i++) {
			String hook = plateHooks[i];
			Holder plateObject = version.get(hook);
			String plateBarcode = plateObject.getName();
			JSONObject plate = ShipmentAssemble.getPlateAsJSON(dataStorage,
					plateBarcode);
			PuckBean pb = new PuckBean(plateBarcode);
			JSONArray coords = plate.getJSONArray("coordinates");

			String[] props = { "well", "crystalNumber", "a", "b", "c", "alpha",
					"beta", "gamma", "spaceGroup", "experimentType",
					"heavyAtom", "comments", "treatmentURL", "imageURL",
					"sampleName", "sampleHook", "pixelX", "pixelY", "pixelR",
					"micronX", "micronY", "micronR", "micronsPerPixel",
					"proteinSequence", "targetName" };
			Iterator it = coords.iterator();
			int puckPosition = 1;
			while (it.hasNext()) {
				JSONObject xy = (JSONObject) it.next();
				Map xyProps = new HashMap();
				for (int j = 0; j < props.length; j++) {
					String key = props[j];
					xyProps.put(key, xy.get(key));
				}
				xyProps.put("puckPosition", puckPosition);
				xyProps.put("barcode", "PLATESHIPMENT");
				pb.add(new SampleBean(xyProps));
				puckPosition++;
			}

			fakeDewar.add(pb);

		}
		dewars.add(fakeDewar);
		ShipmentBean sb = new ShipmentBean(dewars);
		sb.setShippingDate(new GregorianCalendar());
		sb.setShipper(shipper);
		sb.setTrackingNumber(trackingNumber);
		return sb;
	}

	private ShipmentBean makeShipmentBean(DataStorageImpl dataStorage,
			String[] dewarHooks, String shipper, String trackingNumber) {
		Set<DewarBean> dewars = new HashSet<DewarBean>();
		WritableVersion version = dataStorage.getWritableVersion();
		for (int i = 0; i < dewarHooks.length; i++) {
			String hook = dewarHooks[i];
			Holder dewar = version.get(hook);
			DewarBean db = new DewarBean(dewar.getName());
			Collection<Containable> pucks = dewar.getContained();
			Iterator<Containable> j = pucks.iterator();
			while (j.hasNext()) {
				Holder puck = (Holder) j.next();
				Set<HolderCategory> hcs = puck.getHolderType()
						.getDefaultHolderCategories();
				if (hcs.isEmpty()) {
					continue;
				}
				HolderCategory hc = hcs.iterator().next();
				String category = hc.get_Name();
				if ("Puck".equals(category)) {
					PuckBean pb = new PuckBean(puck.getName());
					Collection<Containable> pins = puck.getContained();
					if (pins.isEmpty()) {
						continue;
					}
					System.out.println("MakeShipmentBean: Puck "
							+ puck.getName() + " has " + pins.size() + " pins");
					Iterator<Containable> k = pins.iterator();
					while (k.hasNext()) {
						Holder pin = (Holder) k.next();
						System.out.println("Pin " + pin.get_Name());
						Set<HolderCategory> hcs2 = pin.getHolderType()
								.getDefaultHolderCategories();
						if (hcs2.isEmpty()) {
							continue;
						}
						HolderCategory hc2 = hcs2.iterator().next();
						String category2 = hc2.get_Name();
						if ("Pin".equals(category2)) {
							System.out.println("Pin " + pin.get_Name()
									+ " has category 'Pin'");
							JSONObject pinProps = ShipmentAssemble
									.getPinAsJSON(dataStorage, pin,
											puck.get_Hook());
							Map<String, Object> props = new HashMap<String, Object>();
							Iterator it = pinProps.keys();
							while (it.hasNext()) {
								String key = (String) it.next();
								props.put(key, pinProps.get(key));
							}
							SampleBean sb = new SampleBean(props);
							pb.add(sb);
						}
					}
					db.add(pb);
				}
			}
			dewars.add(db);
		}
		ShipmentBean sb = new ShipmentBean(dewars);
		sb.setShippingDate(new GregorianCalendar());
		sb.setShipper(shipper);
		sb.setTrackingNumber(trackingNumber);
		return sb;
	}

	private JSONObject makeExperimentsAndSpreadsheet(WritableVersion version,
			ShipmentBean sb, ExperimentGroup grp) {

		// map parameter names in DIFFRACTION_PROTOCOL to properties of Pin JSON
		Map parameterNames = new HashMap();
		parameterNames
				.put("Data Collection: Experiment Type", "experimentType");
		parameterNames.put("Anomalous Scatterer", "heavyAtom");

		// No, not these. Don't copy predicted values over to the experiment
		// that will contain the actual values.
		// parameterNames.put("Crystal Form: A", "a");
		// parameterNames.put("Crystal Form: B", "b");
		// parameterNames.put("Crystal Form: C", "c");
		// parameterNames.put("Crystal Form: Alpha", "alpha");
		// parameterNames.put("Crystal Form: Beta", "beta");
		// parameterNames.put("Crystal Form: Gamma", "gamma");
		// parameterNames.put("Crystal Form: Spacegroup", "spaceGroup"); And
		// remove the default to P1 below...

		List<String> errors = new LinkedList<String>();

		Protocol protocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, DIFFRACTION_PROTOCOL);
		if (null == protocol) {
			return makeErrorJSON("Cannot make diffraction experiments. Protocol '"
					+ DIFFRACTION_PROTOCOL + "' is missing.");
		}
		Calendar now = new GregorianCalendar();
		List<DewarBean> dewars = sb.getDewars();
		for (DewarBean dewar : dewars) {
			String dewarName = dewar.getName();
			System.out.println("Dewar: " + dewarName);
			List<PuckBean> pucks = dewar.getPucks();
			for (PuckBean puck : pucks) {
				String puckName = puck.getName();
				System.out.println("Puck: " + puckName);
				List<SampleBean> samples = puck.getSamples();
				for (SampleBean sample : samples) {
					String barcode = (String) sample.get("barcode");
					System.out.println("Pin: " + barcode);
					String sampleName = (String) sample.get("sampleName");
					String sampleHook = (String) sample.get("sampleHook");
					if (null == sampleHook) {
						System.out
								.println("Puck: "
										+ puckName
										+ ", pin "
										+ barcode
										+ " has no sample - removing from puck, not adding to shipment");
						Holder pin = version.findFirst(Holder.class,
								Holder.PROP_NAME, barcode);
						if (null != pin) {
							try {
								pin.setContainer(null);
							} catch (ConstraintException e) {
								System.out
										.println("ConstraintException on trying to set pin's parent container to null");
							}
						}
						continue;
					}
					Sample crystal = version.get(sampleHook);
					try {
						Experiment expt = SelectCrystalServlet
								.addNextExperiment(version, crystal, protocol);
						String experimentHook = expt.get_Hook();
						System.out.println("New diffraction expt for "
								+ barcode + ": " + experimentHook);
						grp.addExperiment(expt);

						String baseName = barcode + " " + sampleName;
						String newName = baseName;
						int count = 1;
						while (null != version.findFirst(Experiment.class,
								Experiment.PROP_NAME, newName)) {
							count++;
							newName = baseName + " (" + count + ")";
						}
						expt.setName(newName);

						Set<Parameter> parameters = expt.getParameters();
						for (Parameter parameter : parameters) {
							String parameterName = parameter.getName().trim();
							String propertyName = (String) parameterNames
									.get(parameterName);
							if (null != propertyName) {
								propertyName = propertyName.trim();
								String parameterValue = (String) sample
										.get(propertyName);
								if (null != parameterValue) {
									parameter.setValue(parameterValue);
								}
							}
							if ("Session: Comments".equals(parameterName)) {
								String userComment = (String) sample
										.get("comments");
								String treatmentURL = (String) sample
										.get("treatmentURL");
								String imageURL = (String) sample
										.get("imageURL");
								Integer pixelX = (Integer) (sample
										.get("pixelX"));
								Integer pixelY = (Integer) (sample
										.get("pixelY"));

								String sessionComment = "";
								sessionComment += userComment;
								if (!"".equals(userComment)
										&& !"".equals(treatmentURL)) {
									sessionComment += " * ";
								}
								if (null != imageURL && !"".equals(imageURL)
										&& null != pixelX && null != pixelY) {
									sessionComment += " pixelX: " + pixelX;
									sessionComment += " pixelY: " + pixelY;
									sessionComment += " Image: " + imageURL;
								}
								if (!"".equals(treatmentURL)) {
									sessionComment += "xtalPiMS: "
											+ treatmentURL;
								}
								parameter.setValue(sessionComment);
							} else if ("Crystal Form: Spacegroup"
									.equals(parameterName)) {
								parameter.setValue("P1"); // sensible default
							}
						}

					} catch (ConstraintException e) {
						errors.add("Tried to make diffraction experiment for '"
								+ barcode + "' but it already exists\n\n");
						e.printStackTrace();
					} catch (AccessException e) {
						errors.add("Tried to make diffraction experiment for '"
								+ barcode + "' but did not have permission");
					}
				}
			}

		}

		if (errors.isEmpty()) {
			JSONObject jo = makeAndAttachSpreadsheet(version, sb, grp);
			return jo;
		} else {
			return makeErrorJSON(errors);
		}
	}

	private JSONObject makeAndAttachSpreadsheet(WritableVersion version,
			ShipmentBean sb, ExperimentGroup grp) {
		List<String> errors = new LinkedList<String>();

		try {

			String filename = grp.getName() + ".xls"; // version will safe this
			File f = version.createFile(new byte[0], filename, grp);
			f.setMimeType("application/vnd.ms-excel");
			OutputStream os = new FileOutputStream(f.getFile());
			IspybSpreadsheetWriter writer = new IspybSpreadsheetWriter();
			writer.write(sb, os);

		} catch (FileNotFoundException e) {
			errors.add(e.getStackTrace().toString());
		} catch (IOException e) {
			errors.add(e.getStackTrace().toString());
		} catch (AccessException e) {
			errors.add(e.getStackTrace().toString());
		} catch (ConstraintException e) {
			errors.add(e.getStackTrace().toString());
		}

		if (errors.isEmpty()) {
			JSONObject jo = new JSONObject();
			jo.put("success", true);
			jo.put("experimentGroup", grp.get_Hook());
			return jo;
		} else {
			return makeErrorJSON(errors);
		}

	}

	private JSONObject makeErrorJSON(String msg) {
		List<String> errors = new LinkedList<String>();
		errors.add(msg);
		return makeErrorJSON(errors);
	}

	private JSONObject makeErrorJSON(List<String> errors) {
		JSONObject jo = new JSONObject();
		jo.put("success", false);
		jo.put("errors", errors);
		return jo;
	}

}