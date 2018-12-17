package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.presentation.HolderDAO;
import org.pimslims.servlet.PIMSServlet;

public class ShipmentAssemble extends PIMSServlet {

	@Override
	public String getServletInfo() {
		return "Handle assembling a shipment";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ShipmentAssemble.doGet");
		request.setAttribute("shipmentDestination",
				request.getParameter("destination")); // LATER Switch
		// UIs based on
		// this
		request.setAttribute("shipper", request.getParameter("shipper"));
		request.setAttribute("trackingId", request.getParameter("trackingid"));
		request.setAttribute("spaceGroups", SelectCrystalServlet.spaceGroups);
		final ReadableVersion version = this.getReadableVersion(request,
				response);
		if (null == version) {
			return;
		}
		request.setAttribute("heavyAtoms",
				CrystalTreatmentServlet.getHeavyAtoms(version));
		String destination = "/CrystalShipping/AssembleDiamondShipment.jsp";
		version.abort();
		RequestDispatcher rd = request.getRequestDispatcher(destination);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processPost(request, response);
	}

	private void processPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONObject jo = new JSONObject();
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return;
		}
		try {
			String action = request.getParameter("action");
			if ("add".equals(action)) {
				String position = request.getParameter("position");
				String barcode = request.getParameter("barcode");
				String parent = request.getParameter("parent");
				jo = doAddToShipment(version, barcode, parent, position);
			} else if ("removePins".equals(action)) {
				String pinsList = request.getParameter("pins");
				String[] pinsToRemove = pinsList.split(",");
				jo = doRemovePins(version, pinsToRemove);
			} else if ("removePuck".equals(action)) {
				String puckHook = request.getParameter("puckToRemove");
				jo = doRemovePuck(version, puckHook);
			} else if ("updateSampleName".equals(action)) {
				String pinHook = request.getParameter("pin");
				String sampleName = request.getParameter("sampleName");
				jo = doUpdateSampleName(version, pinHook, sampleName);
			} else if ("updateTargetName".equals(action)) {
				String pinHook = request.getParameter("pin");
				String targetName = request.getParameter("targetName");
				jo = doUpdateResearchObjectiveName(version, pinHook, targetName);
			} else if ("updatePreShipExperiment".equals(action)) {
				String pinHook = request.getParameter("pin");
				String fieldName = request.getParameter("fieldName");
				String fieldValue = request.getParameter("fieldValue");
				jo = doUpdatePreShipExperiment(version, pinHook, fieldName,
						fieldValue);
			} else {
				jo = JSONError("Unrecognised action");
			}

			version.commit();

			// Output JSON response as application/json. Prototype will put this
			// into its transport.responseJSON object. That means we can detect
			// error pages, server-side barfs, etc., by the absence of
			// transport.responseJSON - the entire server response will instead
			// be in transport.responseText. We can also navigate through the
			// JSON in Firebug, instead of reading it raw.
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());

		} catch (AbortedException e) {
			e.printStackTrace();
		} catch (ConstraintException e) {
			e.printStackTrace();
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}

	}

	private JSONObject doUpdatePreShipExperiment(WritableVersion version,
			String pinHook, String fieldName, String fieldValue)
			throws ConstraintException {
		// TODO Auto-generated method stub
		Holder pin = version.get(pinHook);
		Sample crystal = pin.getSamples().iterator().next();
		Protocol preShipProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, CrystalTreatmentServlet.PRE_SHIP_PROTOCOL);
		Experiment preShipExperiment = SelectCrystalServlet
				.findPreviousExperimentByProtocol(version, crystal,
						preShipProtocol);
		Map<String, String> parameters = new HashMap<String, String>();
		// Parameter names relate to "PiMS Diffraction Test" protocol used
		// for pre-ship expts. TODO This duplicates info in
		// CrystalTreatmentServlet.updatePreShipExperiment(request, response).
		if (fieldName.equals("a")) {
			parameters.put("a", fieldValue);
		} else if (fieldName.equals("b")) {
			parameters.put("b", fieldValue);
		} else if (fieldName.equals("c")) {
			parameters.put("c", fieldValue);
		} else if (fieldName.equals("alpha")) {
			parameters.put("alpha", fieldValue);
		} else if (fieldName.equals("beta")) {
			parameters.put("beta", fieldValue);
		} else if (fieldName.equals("gamma")) {
			parameters.put("gamma", fieldValue);
		} else if (fieldName.equals("spacegroup")) {
			parameters.put("Space group", fieldValue);
		} else if (fieldName.equals("exptype")) {
			parameters.put("Data collection type", fieldValue);
		} else if (fieldName.equals("heavyatom")) {
			parameters.put("Anomalous scatterer", fieldValue);
		} else if (fieldName.equals("comments")) {
			parameters.put("Comments", fieldValue);
		}
		CrystalTreatmentServlet.updatePreShipExperiment(version,
				preShipExperiment, parameters);
		JSONObject jo = new JSONObject();
		jo.put("updated", pinHook);
		jo.put("fieldName", fieldName);
		jo.put("fieldValue", fieldValue);
		return jo;
	}

	private JSONObject doUpdateResearchObjectiveName(WritableVersion version,
			String pinHook, String roName) throws ConstraintException {
		Holder pin = (Holder) version.get(pinHook);
		Holder puck = (Holder) pin.getContainer();
		String parentHook = puck.get_Hook();
		Sample sample = pin.getSamples().iterator().next();
		ResearchObjective ro = sample.getOutputSample().getExperiment()
				.getResearchObjective();
		System.out.println("ResearchObjective of pin: " + ro + " "
				+ ro.getName());
		ResearchObjective roWithProposedName = version.findFirst(
				ResearchObjective.class, ResearchObjective.PROP_COMMONNAME,
				roName);
		if (null != roWithProposedName
				&& !roWithProposedName.get_Hook().equals(ro.get_Hook())) {
			return JSONError("A construct/target called " + roName
					+ " already exists");
		}
		ro.setCommonName(roName);
		return ShipmentAssemble.getPinAsJSON(version, pin, parentHook);
	}

	private JSONObject doUpdateSampleName(WritableVersion version,
			String pinHook, String sampleName) throws ConstraintException {
		Holder pin = (Holder) version.get(pinHook);
		Holder puck = (Holder) pin.getContainer();
		String parentHook = puck.get_Hook();
		Sample sample = pin.getSamples().iterator().next();
		Sample sampleWithProposedName = version.findFirst(Sample.class,
				Sample.PROP_NAME, sampleName);
		if (null != sampleWithProposedName
				&& !sampleWithProposedName.get_Hook().equals(sample.get_Hook())) {
			return JSONError("A sample called " + sampleName
					+ " already exists");
		}
		sample.setName(sampleName);
		return ShipmentAssemble.getPinAsJSON(version, pin, parentHook);
	}

	private JSONObject doRemovePins(WritableVersion version,
			String[] pinsToRemove) throws ConstraintException {
		JSONObject jo = new JSONObject();
		List<JSONObject> removed = new LinkedList<JSONObject>();
		for (int i = 0; i < pinsToRemove.length; i++) {
			String pinHook = pinsToRemove[i];
			Holder pin = version.get(pinHook);
			if (null != pin) {
				JSONObject j1 = new JSONObject();
				j1.put("removedPin", pinHook);
				Holder puck = (Holder) pin.getParentHolder();
				if (null != puck) {
					j1.put("removedFromPuck", puck.get_Hook());
					j1.put("removedFromPosition", pin.getRowPosition());
				}
				pin.setContainer(null);
				pin.setParentHolder(null);
				removed.add(j1);
			}
		}
		jo.put("removedPins", removed);
		return jo;
	}

	private JSONObject doRemovePuck(WritableVersion version, String puckHook)
			throws ConstraintException {
		JSONObject jo = new JSONObject();
		Holder puck = version.get(puckHook);
		if (null != puck) {
			jo.put("removedPuck", puckHook);
			puck.setContainer(null);
			puck.setParentHolder(null);
		} else {
			jo.put("error", "Puck " + puckHook + " was not found");
		}
		return jo;
	}

	/**
	 * Does one of the following:
	 * 
	 * - retrieve the details and contents of the dewar specified by barcode,
	 * then send dewar and contents as JSON
	 * 
	 * - add the puck specified by barcode to the dewar specified by parent,
	 * then send puck and contents as JSON
	 * 
	 * - add the pin specified by barcode to the puck specified by parent, at
	 * position, then send pin as JSON
	 * 
	 * - send a JSON error if nesting conditions are not fulfilled
	 * 
	 * @param barcode
	 *            The barcode of the container we want to add
	 * @param parent
	 *            The hook of its intended parent container, if any
	 * @param position
	 *            The colPosition, if any, within the parent (rowPosition is
	 *            assumed to be 1)
	 * @throws ConstraintException
	 * @throws NumberFormatException
	 */
	private JSONObject doAddToShipment(WritableVersion version, String barcode,
			String parent, String position) throws NumberFormatException,
			ConstraintException {
		if (null == barcode || "".equals(barcode)) {
			return JSONError("No holder barcode specified");
		}
		Holder parentHolder = null;
		String parentCategory = "";
		if (null != parent && !"".equals(parent)) {
			parentHolder = version.get(parent);
			if (null == parentHolder) {
				return JSONError("Could not find parent container (" + parent
						+ ")");
			}
			Set<HolderCategory> parentCategories = parentHolder.getHolderType()
					.getDefaultHolderCategories();
			if (parentCategories.isEmpty()) {
				return JSONError("Parent holder has no HolderCategories - cannot determine what it is");
			} else if (parentCategories.size() > 1) {
				return JSONError("Parent holder has multiple HolderCategories - cannot determine what it is");
			} else {
				HolderCategory pc = parentCategories.iterator().next();
				parentCategory = pc.get_Name();
			}
		}
		Holder h = version.findFirst(Holder.class, "name", barcode);
		if (null == h) {
			return JSONError("No holder with barcode " + barcode);
		}
		Set<HolderCategory> hcs = h.getHolderType()
				.getDefaultHolderCategories();
		if (hcs.isEmpty()) {
			return JSONError("Holder with barcode " + barcode
					+ " has no HolderCategories - cannot determine what it is");
		} else if (hcs.size() > 1) {
			return JSONError("Holder with barcode "
					+ barcode
					+ " has multiple HolderCategories - cannot determine what it is");
		} else {
			try {
				if (!("false".equals(position)) && null != parentHolder) {
					int positionInParent = Integer.parseInt(position);
					h.setRowPosition(positionInParent);
				} else if (null != parentHolder) {
					int[] firstFree = HolderDAO.findFirstGap(parentHolder);
					if (null == firstFree) {
						return JSONError("Cannot add "
								+ barcode
								+ " - parent container is full or has no sub-container positions");
					}
					int p = firstFree[0]; // findFirstGap returns one position
											// [0:col, 1:row]; we assume row=1
					h.setRowPosition(p);
				}
			} catch (NumberFormatException nfe) {
				// Not interested.
			}

			HolderCategory hc = hcs.iterator().next();
			String category = hc.get_Name();
			if ("Pin".equals(category)) {
				if (!"Puck".equals(parentCategory)) {
					if ("Dewar".equals(parentCategory)) {
						return JSONError("Pins must be scanned into an available slot in a puck. They can't be put directly into a dewar.");
					} else {
						return JSONError("Pins cannot be added directly. First add a dewar, then add a puck to the dewar, then the pin into the puck.");
					}
				}
				h.setParentHolder(parentHolder);
				return ShipmentAssemble.getPinAsJSON(version, h, parent);

			} else if ("Puck".equals(category)) {

				if (!"Dewar".equals(parentCategory)) {
					return JSONError("To add a puck: First ensure that the shipment contains at least one dewar, then scan the puck barcode into the dewar's 'Add puck by barcode' box");
				}

				/*
				 * Check all pins contained in this puck, and remove any that
				 * don't contain a sample.
				 */
				Collection<Containable> subs = h.getContained();
				Iterator i = subs.iterator();
				while (i.hasNext()) {
					Holder pin = (Holder) i.next();
					Set<Sample> xtals = pin.getSamples();
					if (xtals.isEmpty()) {
						pin.setContainer(null);
					}
				}

				h.setParentHolder(parentHolder);
				return getPuckAsJSON(version, h, parent);

			} else if ("Dewar".equals(category)) {

				return getDewarAsJSON(version, h);

			} else {
				return JSONError("Holder with barcode " + barcode
						+ " is not a Puck, Pin, or Dewar");
			}
		}
	}

	protected static JSONObject getPinAsJSON(ReadableVersion version,
			Holder pin, String parentHook) {
		String a = "";
		String b = "";
		String c = "";
		String alpha = "";
		String beta = "";
		String gamma = "";
		String spaceGroup = "";
		String dataCollType = "";
		String heavyAtom = "";
		String comments = "";
		String treatmentURL = "";

		JSONObject jo = new JSONObject();
		// error if no sample
		// error if sample has no ResearchObjective
		Set<Sample> samples = pin.getSamples();
		if (samples.isEmpty()) {
			jo.put("error", "Pin " + pin.get_Name()
					+ " has no crystal in it; not adding to the shipment");
			return jo;
		} else if (1 < samples.size()) {
			jo.put("error",
					"Pin "
							+ pin.get_Name()
							+ " has more than one crystal in it; not adding to the shipment");
			return jo;
		}
		jo.put("added", "Pin");
		jo.put("hook", pin.get_Hook());
		jo.put("barcode", pin.getName());
		jo.put("parent", parentHook);
		jo.put("puckPosition", pin.getRowPosition());

		Sample crystal = samples.iterator().next();
		Experiment expt = crystal.getOutputSample().getExperiment();
		if (expt.getProtocol().getName()
				.equals(CrystalTreatmentServlet.PRE_SHIP_PROTOCOL)) {
			Set<Parameter> params = expt.getParameters();
			for (Parameter param : params) {
				String paramName = param.getName().trim();
				if (paramName.equals("a")) {
					a = param.getValue();
				} else if (paramName.equals("b")) {
					b = param.getValue();
				} else if (paramName.equals("c")) {
					c = param.getValue();
				} else if (paramName.equals("alpha")) {
					alpha = param.getValue();
				} else if (paramName.equals("beta")) {
					beta = param.getValue();
				} else if (paramName.equals("gamma")) {
					gamma = param.getValue();
				} else if (paramName.equals("Comments")) {
					comments = param.getValue();
				} else if (paramName.equals("Space group")) {
					spaceGroup = param.getValue();
				} else if (paramName.equals("Data collection type")) {
					dataCollType = param.getValue();
				} else if (paramName.equals("Anomalous scatterer")) {
					heavyAtom = param.getValue();
				} else if (paramName.equals("Treatment page URL")) {
					treatmentURL = param.getValue();
				}

			}
		} else if (expt.getProtocol().getName()
				.equals(CrystalTreatmentServlet.FINALMOUNT_PROTOCOL)) {
			jo.put("error", "Crystal in pin " + pin.getName()
					+ " has no pre-ship experiment; cannot add to shipment");
			return jo;
		} else {
			jo.put("error",
					"Crystal in pin "
							+ pin.getName()
							+ " has no pre-ship or final mount experiment; cannot add to shipment");
			return jo;
		}

		ResearchObjective ro = expt.getResearchObjective();
		if (null == ro) {
			jo.put("error",
					"Crystal in pin "
							+ pin.getName()
							+ " has no associated protein, and cannot be added to the shipment");
		} else {
			String roName = ro.get_Name();
			jo.put("targetName", roName);
			jo.put("targetHook", ro.get_Hook());
		}

		jo.put("a", a);
		jo.put("b", b);
		jo.put("c", c);
		jo.put("alpha", alpha);
		jo.put("beta", beta);
		jo.put("gamma", gamma);
		jo.put("spaceGroup", spaceGroup);
		jo.put("experimentType", dataCollType);
		jo.put("heavyAtom", heavyAtom);
		jo.put("comments", comments);
		jo.put("treatmentURL", treatmentURL);
		jo.put("sampleName", crystal.getName());
		jo.put("sampleHook", crystal.get_Hook());
		return jo;
	}

	private JSONObject getPuckAsJSON(ReadableVersion version, Holder puck,
			String parentHook) {
		JSONObject jo = new JSONObject();
		jo.put("added", "Puck");
		jo.put("barcode", puck.getName());
		jo.put("hook", puck.get_Hook());
		jo.put("parent", parentHook);
		HolderType ht = (HolderType) puck.getHolderType();
		jo.put("positions", ht.getMaxRow());

		ArrayList<JSONObject> pins = new ArrayList<JSONObject>();
		Collection<Containable> contained = puck.getContained();
		Iterator<Containable> i = contained.iterator();
		while (i.hasNext()) {
			Holder h = (Holder) i.next();
			Set<HolderCategory> hcs = h.getHolderType()
					.getDefaultHolderCategories();
			if (hcs.isEmpty()) {
				continue;
			}
			HolderCategory hc = hcs.iterator().next();
			String category = hc.get_Name();
			if ("Pin".equals(category)) {
				pins.add(ShipmentAssemble.getPinAsJSON(version, h,
						puck.get_Hook()));
			}
		}
		jo.put("contents", pins);
		return jo;
	}

	private JSONObject getDewarAsJSON(ReadableVersion version, Holder dewar) {
		JSONObject jo = new JSONObject();
		jo.put("added", "Dewar");
		jo.put("barcode", dewar.getName());
		jo.put("hook", dewar.get_Hook());

		ArrayList<JSONObject> pucks = new ArrayList<JSONObject>();
		Collection<Containable> contained = dewar.getContained();
		Iterator<Containable> i = contained.iterator();
		while (i.hasNext()) {
			Holder h = (Holder) i.next();
			Set<HolderCategory> hcs = h.getHolderType()
					.getDefaultHolderCategories();
			if (hcs.isEmpty()) {
				continue;
			}
			HolderCategory hc = hcs.iterator().next();
			String category = hc.get_Name();
			if ("Puck".equals(category)) {
				pucks.add(getPuckAsJSON(version, h, dewar.get_Hook()));
			}
		}
		jo.put("contents", pucks);
		return jo;
	}

	private JSONObject JSONError(String error) {
		JSONObject jo = new JSONObject();
		jo.put("error", error);
		return jo;
	}

}