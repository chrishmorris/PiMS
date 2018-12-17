package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import net.sf.json.JSONObject;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.util.SequenceManager;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.HolderDAO;
import org.pimslims.util.InstallationProperties;

public class ShipmentAssemble extends XtalPIMSServlet {

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

		InstallationProperties props = ModelImpl.getInstallationProperties();
		String plateShipmentEndpoint = props
				.getProperty("shipping.plateShipmentEndpoint");
		request.setAttribute("plateShipmentEndpoint", plateShipmentEndpoint);
		String siteUsesBarcodedPins = props
				.getProperty("customization.siteUsesBarcodedPins");
		String isBeamline = props.getProperty("customization.isBeamline");
		if ("true".equals(isBeamline)) {
			request.setAttribute("isBeamline", true);
			String plateError = "";
			String plateBarcode = request.getParameter("plateBarcode");
			if (null == plateBarcode) {
				plateError = "No plate barcode specified";
			} else {
				Holder plate = version.findFirst(Holder.class,
						Holder.PROP_NAME, plateBarcode);
				if (null == plate) {
					plateError = "Plate does not exist, or you do not have permission to see it";
				}
			}
			request.setAttribute("plateError", plateError);
			request.setAttribute("plateBarcode", plateBarcode);
		} else {
			request.setAttribute("isBeamline", false);
		}

		request.setAttribute("heavyAtoms",
				CrystalTreatmentServlet.getHeavyAtoms(version));
		String destination = "/CrystalShipping/AssembleDiamondShipment.jsp";
		if (null != request.getParameter("plateshipment")) {
			destination = "/CrystalShipping/AssembleDiamondPlateShipment.jsp";
		}
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
		PrintWriter writer = response.getWriter();
		DataStorageImpl dataStorage = null;
		try {
			dataStorage = (DataStorageImpl) openResources(request);
			String action = request.getParameter("action");
			if ("add".equals(action)) {
				String position = request.getParameter("position");
				String barcode = request.getParameter("barcode");
				String parent = request.getParameter("parent");
				jo = doAddToShipment(dataStorage, barcode, parent, position);
			} else if ("addPlate".equals(action)) {
				String plateBarcode = request.getParameter("barcode");
				jo = getPlateAsJSON(dataStorage, plateBarcode);
			} else if ("removePins".equals(action)) {
				String pinsList = request.getParameter("pins");
				String[] pinsToRemove = pinsList.split(",");
				jo = doRemovePins(dataStorage, pinsToRemove);
			} else if ("removePuck".equals(action)) {
				String puckHook = request.getParameter("puckToRemove");
				jo = doRemovePuck(dataStorage, puckHook);
			} else if ("updateSampleName".equals(action)) {
				String pinHook = request.getParameter("pin");
				String sampleName = request.getParameter("sampleName");
				jo = doUpdateSampleName(dataStorage, pinHook, sampleName);
			} else if ("updatePreShipExperiment".equals(action)) {
				String pinHook = request.getParameter("pin");
				String fieldName = request.getParameter("fieldName");
				String fieldValue = request.getParameter("fieldValue");
				jo = doUpdatePreShipExperiment(dataStorage, pinHook, fieldName,
						fieldValue);
			} else if ("emptyContainers".equals(action)) {
				String groupHook = request.getParameter("experimentGroup");
				jo = doEmptyContainers(dataStorage, groupHook);
			} else {
				jo = JSONError("Unrecognised action");
			}
			dataStorage.commit();

		} catch (ConstraintException e) {
			jo = JSONError(e.getMessage());
			jo.put("trace", e.getStackTrace());
		} catch (BusinessException e) {
			jo = JSONError(e.getMessage());
			jo.put("trace", e.getStackTrace());
		} catch (AbortedException e) {
			jo = JSONError(e.getMessage());
			jo.put("trace", e.getStackTrace());
		} finally {
			closeResources(dataStorage);
			// Output JSON response as application/json. Prototype will put this
			// into its transport.responseJSON object. That means we can detect
			// error pages, server-side barfs, etc., by the absence of
			// transport.responseJSON - the entire server response will instead
			// be in transport.responseText. We can also navigate through the
			// JSON in Firebug, instead of reading it raw.
			response.setContentType("application/json");
			writer.write(jo.toString());
		}

	}

	private JSONObject doEmptyContainers(DataStorageImpl dataStorage,
			String groupHook) throws ConstraintException, AbortedException {
		WritableVersion version = dataStorage.getWritableVersion();
		ExperimentGroup shipment = version.get(groupHook);
		if (null == shipment) {
			return JSONError("Shipment (ExperimentGroup "
					+ groupHook
					+ ") does not exist, or you do not have permission to see it");
		}
		Set<Experiment> expts = shipment.getExperiments();
		Set<Holder> holders = new HashSet<Holder>();
		Set<Holder> cantUpdateHolders = new HashSet<Holder>();
		for (Experiment e : expts) {
			Sample xtal = e.getInputSamples().iterator().next().getSample();
			if (null == xtal) {
				continue;
			}
			Holder pin = (Holder) xtal.getContainer();
			if (null != pin) {
				try {
					if (holderIsA("Pin", pin)) {
						if (version.mayUpdate(pin)) {
							holders.add(pin);
							Holder puck = (Holder) pin.getContainer();
							if (null != puck) {
								if (holderIsA("Puck", puck)) {
									if (version.mayUpdate(puck)) {
										holders.add(puck);
									} else {
										cantUpdateHolders.add(puck);
									}
								}

							}
						} else {
							cantUpdateHolders.add(pin);
						}
					}
				} catch (BusinessException ex) {
					// ignore;
				}
			}
		}
		String holderNames = "";
		if (!cantUpdateHolders.isEmpty()) {
			for (Holder h : cantUpdateHolders) {
				holderNames += " " + h.get_Name();
			}
			return JSONError("You do not have permission to update these: "
					+ holderNames);
		}
		holderNames = "";
		for (Holder h : holders) {
			holderNames += " " + h.get_Name();
			h.setSamples(null);
			h.setContainer(null);
		}
		JSONObject jo = new JSONObject();
		jo.put("success", true);
		jo.put("updated", holderNames);
		jo.put("numberUpdated", holders.size());
		return jo;
	}

	private boolean holderIsA(String type, Holder holder)
			throws BusinessException {
		Set<HolderCategory> categories = holder.getHolderType()
				.getDefaultHolderCategories();
		if (categories.isEmpty()) {
			throw new BusinessException("Holder has no categories");
		} else if (categories.size() > 1) {
			throw new BusinessException("Parent holder has multiple categories");
		}
		HolderCategory c = categories.iterator().next();
		String categoryName = c.get_Name();
		return (categoryName.equals(type));
	}

	private JSONObject doUpdatePreShipExperiment(DataStorageImpl dataStorage,
			String pinHook, String fieldName, String fieldValue)
			throws ConstraintException {
		// TODO Auto-generated method stub
		WritableVersion version = dataStorage.getWritableVersion();
		Holder pin = version.get(pinHook);
		Sample crystal = pin.getSamples().iterator().next();
		Protocol preShipProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.PRE_SHIP_PROTOCOL);
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

	private JSONObject doUpdateSampleName(DataStorageImpl dataStorage,
			String pinHook, String sampleName) throws ConstraintException {
		WritableVersion version = dataStorage.getWritableVersion();
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
		return ShipmentAssemble.getPinAsJSON(dataStorage, pin, parentHook);
	}

	private JSONObject doRemovePins(DataStorageImpl dataStorage,
			String[] pinsToRemove) throws ConstraintException {
		WritableVersion version = dataStorage.getWritableVersion();
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

	private JSONObject doRemovePuck(DataStorageImpl dataStorage, String puckHook) {
		WritableVersion version = dataStorage.getWritableVersion();
		JSONObject jo = new JSONObject();
		Holder puck = version.get(puckHook);
		if (null != puck) {
			try {
				puck.setContainer(null);
				puck.setParentHolder(null);
				jo.put("removedPuck", puckHook);
			} catch (ConstraintException e) {
				jo.put("error",
						"You don't have permission to update "
								+ puck.get_Name() + ".");
			}
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
	 * @throws BusinessException
	 */
	private JSONObject doAddToShipment(DataStorageImpl dataStorage,
			String barcode, String parent, String position)
			throws NumberFormatException, ConstraintException,
			BusinessException {
		if (null == barcode || "".equals(barcode)) {
			return JSONError("No holder barcode specified");
		}
		WritableVersion version = dataStorage.getWritableVersion();
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
				purgeEmptyPinsFromHolder(h);
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
				Set<Sample> samples = h.getSamples();
				if (1 == samples.size()) {
					h.setParentHolder(parentHolder);
					return ShipmentAssemble
							.getPinAsJSON(dataStorage, h, parent);
				} else if (0 == samples.size()) {
					return JSONError("Pin " + barcode
							+ " has no crystal in it - not adding to shipment");
				} else {
					return JSONError("Pin "
							+ barcode
							+ " has more than one crystal - not adding to shipment");
				}

			} else if ("Puck".equals(category)) {

				if (!"Dewar".equals(parentCategory)) {
					return JSONError("To add a puck: First ensure that the shipment contains at least one dewar, then scan the puck barcode into the dewar's 'Add puck by barcode' box");
				}

				/*
				 * Check all pins contained in this puck, and remove any that
				 * don't contain a sample.
				 */
				Collection<Containable> subs = h.getContained();
				Iterator<Containable> i = subs.iterator();
				while (i.hasNext()) {
					Holder pin = (Holder) i.next();
					Set<Sample> xtals = pin.getSamples();
					if (xtals.isEmpty()) {
						pin.setContainer(null);
						h.removeSubHolder(pin);
					}
				}

				h.setParentHolder(parentHolder);
				return getPuckAsJSON(dataStorage, h, parent);

			} else if ("Dewar".equals(category)) {

				return getDewarAsJSON(dataStorage, h);

			} else {
				return JSONError("Holder with barcode " + barcode
						+ " is not a Puck, Pin, or Dewar");
			}
		}
	}

	protected void purgeEmptyPinsFromHolder(Holder holder)
			throws BusinessException, ConstraintException {
		if (holderIsA("Puck", holder) & holderIsA("Dewar", holder)) {
			Collection<AbstractHolder> subHolders = holder.getSubHolders();
			Iterator<AbstractHolder> i = subHolders.iterator();
			while (i.hasNext()) {
				Holder subHolder = (Holder) i.next();
				purgeEmptyPinsFromHolder(subHolder);
			}
		} else if (holderIsA("Pin", holder)) {
			Set<Sample> samples = holder.getSamples();
			if (samples.size() != 1) {
				holder.setSamples(Collections.EMPTY_SET);
			}
		}
	}

	protected static JSONObject getPlateAsJSON(DataStorageImpl dataStorage,
			String plateBarcode) {
		JSONObject jo = new JSONObject();
		WritableVersion version = dataStorage.getWritableVersion();
		Holder plate = version.findFirst(Holder.class, "name", plateBarcode);
		ExperimentGroup trial = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, plateBarcode);
		if (null == plate || null == trial) {
			jo.put("error", "No plate with barcode " + plateBarcode);
			return jo;
		}
		HolderType ht = (HolderType) plate.getHolderType();

		SequenceManager seqMgr = new SequenceManager(dataStorage);
		String proteinSequence = seqMgr.getSequence(plateBarcode);
		if (null != proteinSequence) {
			proteinSequence = proteinSequence.toUpperCase();
		}

		Protocol selectionProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.SELECTION_PROTOCOL);
		Protocol trialsProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.TRIALS_PROTOCOL);

		LinkedList<JSONObject> coords = new LinkedList<JSONObject>();
		String[] rowLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T" };

		/*
		 * Rationale: "Add to task list" created a final mount expt with Pin
		 * barcode "PLATE" and Puck barcode=plate barcode. Since there can't be
		 * two containers with the same name, there can't be a puck with the
		 * plate's barcode. So we can imply that Pin barcode == "PLATE". So any
		 * parameter called "Puck barcode" with a value of (plate barcode) is
		 * PROBABLY hanging off a Final Mount expt. We get all of them for the
		 * given plate, then for each we
		 * 
		 * - check that its parent expt is indeed final mount
		 * 
		 * - go DOWN from that final mount expt to get pre-ship info
		 * 
		 * - go UP the chain to the selection expt, get its crystal number and
		 * micron x/y/r
		 * 
		 * - go up again to the trial expt, and get its sample's row, col,
		 * subposition
		 * 
		 * - Use the Business API criteria to get the trial drop (again! Yes,
		 * this is every bit as horrible as it sounds)
		 * 
		 * - find the drop's latest Image
		 * 
		 * - from the Image, determine its URL and the micronsPerPixel.
		 * 
		 * Phew.
		 */
		Map<String, Object> parameterCriteria = new HashMap();
		parameterCriteria.put(Parameter.PROP_NAME, "Puck barcode");
		parameterCriteria.put(Parameter.PROP_VALUE, plateBarcode);
		final Collection<Parameter> parameters = version.findAll(
				Parameter.class, parameterCriteria);

		try {

			Iterator i = parameters.iterator();
			while (i.hasNext()) {
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
				String imageURL = "";
				float micronX = 0;
				float micronY = 0;
				float micronR = 0;
				int pixelX = 0;
				int pixelY = 0;
				int pixelR = 0;
				float micronsPerPixel = 1;
				int crystalNumber = 0;

				Parameter p = (Parameter) i.next();
				Experiment finalMount = p.getExperiment();
				if (!finalMount.getProtocol().getName()
						.endsWith(SelectCrystalServlet.FINALMOUNT_PROTOCOL)) {
					continue;
				}

				// Go DOWN from finalMount to get the treatment URL and
				// other preship info
				Set<InputSample> iss = finalMount.getOutputSamples().iterator()
						.next().getSample().getInputSamples();
				if (iss.size() > 0) {
					InputSample is = iss.iterator().next();
					Experiment preShip = is.getExperiment();
					if (preShip.getProtocol().getName()
							.equals(SelectCrystalServlet.PRE_SHIP_PROTOCOL)) {
						Set<Parameter> params = preShip.getParameters();
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
					}
				}

				// Go UP from final mount to get the rest
				Sample crystal = finalMount.getOutputSamples().iterator()
						.next().getSample();
				Experiment preShip = crystal.getInputSamples().iterator()
						.next().getExperiment();
				Sample mountedCrystal = preShip.getOutputSamples().iterator()
						.next().getSample();

				Experiment selection = SelectCrystalServlet
						.findPreviousExperimentByProtocol(version, crystal,
								selectionProtocol);
				Set<Parameter> params = selection.getParameters();
				for (Parameter param : params) {
					String paramName = param.getName().trim();
					if (paramName.equals("x")) {
						micronX = Float.parseFloat(param.getValue());
					} else if (paramName.equals("y")) {
						micronY = Float.parseFloat(param.getValue());
					} else if (paramName.equals("r")) {
						micronR = Float.parseFloat(param.getValue());
					} else if (paramName.equals("Crystal number")) {
						crystalNumber = Integer.parseInt(param.getValue());
					}
				}
				Sample drop = selection.getInputSamples().iterator().next()
						.getSample();

				int row = drop.getRowPosition();
				int col = drop.getColPosition();
				int sub = drop.getSubPosition();
				String well = rowLetters[row - 1] + String.format("%02d", col)
						+ "." + sub;

				final TrialService trialService = dataStorage.getTrialService();
				final BusinessCriteria criteria = new BusinessCriteria(
						trialService);
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_BARCODE, plateBarcode, true));
				criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
						well, true));
				final Collection<TrialDropView> trialDrops = trialService
						.findViews(criteria);
				if (0 == trialDrops.size()) {
					// don't care, maybe only half the plate was set up
				} else if (1 != trialDrops.size()) {
					throw new BusinessException(
							"More than one trial drop matches supplied barcode and position");
				}
				TrialDropView dropview = trialDrops.iterator().next();
				List<ImageView> images = dropview.getImages();
				Iterator<ImageView> iter = images.iterator();
				ImageView image = null;
				while (iter.hasNext()) {
					ImageView im = (ImageView) iter.next();
					if (null == image) {
						image = im;
					} else if (im.getDate().after(image.getDate())) { // Always
																		// use
																		// the
																		// most
																		// recent
																		// image
																		// of
																		// the
																		// drop
						image = im;
					}
				}
				if (null != image && null != image.getHeightPerPixel()) {
					micronsPerPixel = image.getHeightPerPixel().floatValue();
					imageURL = image.getUrl();
				}
				pixelX = Math.round(micronX / micronsPerPixel);
				pixelY = Math.round(micronY / micronsPerPixel);
				pixelR = Math.round(micronR / micronsPerPixel);

				JSONObject xy = new JSONObject();

				SequenceManager sequenceManager = new SequenceManager(
						dataStorage);
				String sequence = sequenceManager.getSequence(plateBarcode);
				if (null == sequence) {
					throw new BusinessException(
							"Drop in well "
									+ well
									+ " has no associated protein; cannot add plate to the shipment");
				} else {
					String acronym;
					try {
						acronym = sequenceManager.getAcronym(plateBarcode);
						if (null == acronym) {
							xy.put("targetName", "???");
							xy.put("acronymWarning", "noTarget");
						} else {
							xy.put("targetName", acronym);
						}
					} catch (ConstraintException e) {
						throw new BusinessException(e.getMessage());
					} catch (AccessException e) {
						throw new BusinessException(e.getMessage());
					}
				}

				xy.put("well", well);
				xy.put("crystalNumber", crystalNumber);
				xy.put("a", a);
				xy.put("b", b);
				xy.put("c", c);
				xy.put("alpha", alpha);
				xy.put("beta", beta);
				xy.put("gamma", gamma);
				xy.put("spaceGroup", spaceGroup);
				xy.put("experimentType", dataCollType);
				xy.put("heavyAtom", heavyAtom);
				xy.put("comments", comments);
				xy.put("treatmentURL", treatmentURL);
				xy.put("imageURL", imageURL);
				xy.put("sampleName", mountedCrystal.getName());
				xy.put("sampleHook", mountedCrystal.get_Hook());
				xy.put("pixelX", pixelX);
				xy.put("pixelY", pixelY);
				xy.put("pixelR", pixelR);
				xy.put("micronX", micronX);
				xy.put("micronY", micronY);
				xy.put("micronR", micronR);
				xy.put("micronsPerPixel", micronsPerPixel);
				xy.put("proteinSequence", proteinSequence);
				coords.add(xy);
			}

		} catch (BusinessException e) {
			jo.put("error", e.getMessage());
			return jo;
		}

		jo.put("labNotebook", plate.get_Owner());
		jo.put("barcode", plateBarcode);
		jo.put("hook", plate.get_Hook());
		jo.put("plateTypeName", ht.getName());
		jo.put("coordinates", coords);
		return jo;
	}

	protected static JSONObject getPinAsJSON(DataStorageImpl dataStorage,
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
		String imageURL = "";
		String proteinSequence = "";
		float micronX = 0;
		float micronY = 0;
		float micronR = 0;
		int pixelX = 0;
		int pixelY = 0;
		int pixelR = 0;
		float micronsPerPixel = 1;

		JSONObject jo = new JSONObject();
		// error if no sample
		// error if sample has no ResearchObjective
		jo.put("hook", pin.get_Hook());
		jo.put("barcode", pin.getName());
		jo.put("parent", parentHook);
		Set<Sample> samples = pin.getSamples();
		Iterator<Sample> i = samples.iterator();
		while (i.hasNext()) {
			Sample s = (Sample) i.next();
			System.out.println(pin.getName() + "contains Sample "
					+ s.get_Name() + " " + s.get_Hook());
		}
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
		jo.put("puckPosition", pin.getRowPosition());

		Sample crystal = samples.iterator().next();
		Experiment expt = crystal.getOutputSample().getExperiment();
		if (expt.getProtocol().getName()
				.equals(SelectCrystalServlet.PRE_SHIP_PROTOCOL)) {
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
				.equals(SelectCrystalServlet.FINALMOUNT_PROTOCOL)) {
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

		ReadableVersion version = dataStorage.getVersion();
		// Go back up the chain, find the plate barcode
		Protocol trialsProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.TRIALS_PROTOCOL);
		Experiment trial = CrystalTreatmentServlet
				.findPreviousExperimentByProtocol(version, crystal,
						trialsProtocol);
		ExperimentGroup grp = trial.getExperimentGroup();
		String plateBarcode = grp.get_Name();
		SequenceManager seqMgr = new SequenceManager(dataStorage);
		proteinSequence = seqMgr.getSequence(plateBarcode);
		if (null != proteinSequence) {
			proteinSequence = proteinSequence.toUpperCase();
		}

		Sample dropSample = trial.getOutputSamples().iterator().next()
				.getSample();
		int row = dropSample.getRowPosition();
		int col = dropSample.getColPosition();
		int sub = dropSample.getSubPosition();
		String[] rowLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T" };
		String well = rowLetters[row - 1] + String.format("%02d", col) + "."
				+ sub;

		Protocol selectionProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.SELECTION_PROTOCOL);
		Experiment selection = CrystalTreatmentServlet
				.findPreviousExperimentByProtocol(version, crystal,
						selectionProtocol);
		Set<Parameter> parameters = selection.getParameters();
		Iterator<Parameter> iter1 = parameters.iterator();
		while (iter1.hasNext()) {
			Parameter p = (Parameter) iter1.next();
			String parameterName = p.getParameterDefinition().getName();
			if ("x".equals(parameterName)) {
				micronX = Float.parseFloat(p.getValue());
			} else if ("y".equals(parameterName)) {
				micronY = Float.parseFloat(p.getValue());
			} else if ("r".equals(parameterName)) {
				micronR = Float.parseFloat(p.getValue());
			}
		}

		try {
			final TrialService trialService = dataStorage.getTrialService();
			final BusinessCriteria criteria = new BusinessCriteria(trialService);
			if ((plateBarcode != null) && (!plateBarcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_BARCODE, plateBarcode, true));
			}
			if ((well != null) && (!well.equals(""))) {
				criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
						well, true));
				if (well.contains(".")) {
				}
			}
			final Collection<TrialDropView> trialDrops = trialService
					.findViews(criteria);

			if (0 == trialDrops.size()) {
				throw new BusinessException(
						"No trial drop found for supplied barcode and position");
			} else if (1 != trialDrops.size()) {
				throw new BusinessException(
						"More than one trial drop matches supplied barcode and position");
			}
			TrialDropView drop = trialDrops.iterator().next();
			List<ImageView> images = drop.getImages();
			Iterator<ImageView> iter = images.iterator();
			ImageView image = null;
			while (iter.hasNext()) {
				ImageView im = (ImageView) iter.next();
				if (null == image) {
					image = im;
				} else if (im.getDate().after(image.getDate())) { // Always use
																	// the
																	// most
																	// recent
																	// image of
																	// the
																	// drop
					image = im;
				}
			}
			if (null != image && null != image.getHeightPerPixel()) {
				micronsPerPixel = image.getHeightPerPixel().floatValue();
				imageURL = image.getUrl();
			}
		} catch (BusinessException e1) {
			JSONObject err = new JSONObject();
			jo.put("error", "BusinessException in getPinAsJSON");
			return err;
		}

		pixelX = Math.round(micronX / micronsPerPixel);
		pixelY = Math.round(micronY / micronsPerPixel);
		pixelR = Math.round(micronR / micronsPerPixel);

		SequenceManager sequenceManager = new SequenceManager(dataStorage);
		String sequence = sequenceManager.getSequence(plateBarcode);
		if (null == sequence) {
			jo.put("error",
					"Crystal in pin "
							+ pin.getName()
							+ " has no associated protein, and cannot be added to the shipment");
		} else {

			String acronym;
			try {
				acronym = sequenceManager.getAcronym(plateBarcode);
				if (null == acronym) {
					jo.put("targetName", "???");
					jo.put("acronymWarning", "noTarget");
				} else {
					jo.put("targetName", acronym);
				}
			} catch (ConstraintException e) {
				JSONObject err = new JSONObject();
				jo.put("error", "ConstraintException in getPinAsJSON");
				return err;
			} catch (AccessException e) {
				JSONObject err = new JSONObject();
				jo.put("error", "AccessException in getPinAsJSON");
				return err;
			}
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
		jo.put("imageURL", imageURL);
		jo.put("sampleName", crystal.getName());
		jo.put("sampleHook", crystal.get_Hook());
		jo.put("pixelX", pixelX);
		jo.put("pixelY", pixelY);
		jo.put("pixelR", pixelR);
		jo.put("micronX", micronX);
		jo.put("micronY", micronY);
		jo.put("micronR", micronR);
		jo.put("micronsPerPixel", micronsPerPixel);
		jo.put("proteinSequence", proteinSequence);
		return jo;
	}

	private JSONObject getPuckAsJSON(DataStorageImpl dataStorage, Holder puck,
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
				pins.add(ShipmentAssemble.getPinAsJSON(dataStorage, h,
						puck.get_Hook()));
			}
		}
		jo.put("contents", pins);
		return jo;
	}

	private JSONObject getDewarAsJSON(DataStorageImpl dataStorage, Holder dewar) {
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
				pucks.add(getPuckAsJSON(dataStorage, h, dewar.get_Hook()));
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