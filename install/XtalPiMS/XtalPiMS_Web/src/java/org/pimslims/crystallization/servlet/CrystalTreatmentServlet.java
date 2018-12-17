package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentCopier;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.experiment.ExperimentUpdate;
import org.pimslims.servlet.experiment.UpdateInputSamples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrystalTreatmentServlet extends SelectCrystalServlet {

	private static final String PROJECT_HOOK = "expBlueprintHook";
	private static final String PARAMETER_CLASS = "org.pimslims.model.experiment.Parameter";
	private static final String INPUTSAMPLE_CLASS = "org.pimslims.model.experiment.InputSample";
	private static final String FINAL_MOUNT_PROTOCOL = "Finish crystal treatment";
	protected static final String PRE_SHIP_PROTOCOL = "Test diffraction and ship";

	public static final String COPYTREATMENT_SAME_EXPERIMENTS = "Source and destination experiments cannot be the same";
	public static final String COPYTREATMENT_NO_CHAIN = "Source crystal has no treatment history to copy";
	public static final String COPYTREATMENT_BAD_SOURCE = "Source experiment is not a "
			+ SelectCrystalServlet.SELECTION_PROTOCOL + " experiment";
	public static final String COPYTREATMENT_BAD_DESTINATION = "Destination experiment is not a "
			+ SelectCrystalServlet.SELECTION_PROTOCOL + " experiment";

	private static final Logger log = LoggerFactory
			.getLogger(CrystalTreatmentServlet.class);

	private static final String PIN_CATEGORY = "Pin";
	private static final String PUCK_CATEGORY = "Puck";

	@Override
	public String getServletInfo() {
		return "Used to display the treatment of an individual crystal after selection from within a trial drop";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("CrystalTreatmentServlet.doPost: entering");

		String barcode = request.getParameter("barcode");
		String well = request.getParameter("well"); // A01.1
		String subPosition = request.getParameter("subPosition");
		if (subPosition == null || subPosition.length() == 0) {
			subPosition = "1";
		}
		String crystal = request.getParameter("crystal");
		String nextProtocol = request.getParameter("protocolName");
		String boxToOpen = "";
		if (null != request.getParameter("delete")) {
			deleteHooks(request, response);
		} else if (null != request.getParameter("copyTreatmentHistory")) {
			copyTreatmentHistory(request, response);
		} else if (null != request.getParameter("ship")) {
			addFinalMountExperiment(request, response);
			boxToOpen = "finalmount";
		} else if (null != request.getParameter("updatePreShipInfo")) {
			updatePreShipExperiment(request, response);
			boxToOpen = "preship";
		} else if (nextProtocol != null && nextProtocol.length() != 0) {
			boxToOpen = addNextTreatmentExperiment(request, response,
					nextProtocol);
		} else {
			log.info("About to call update");
			boxToOpen = updateTreatmentExperiment(request, response);
		}
		String destination = request.getContextPath()
				+ "/update/CrystalTreatment/?barcode=" + barcode + "&well="
				+ well + "&crystal=" + crystal;
		if (null != request.getAttribute("nonExistentPin")) {
			destination += "&nonExistentPin="
					+ request.getAttribute("nonExistentPin");
		}
		if (null != request.getAttribute("nonExistentPuck")) {
			destination += "&nonExistentPuck="
					+ request.getAttribute("nonExistentPuck");
		}
		destination += "#" + boxToOpen;

		this.redirect(response, destination);

	}

	private String addNextTreatmentExperiment(HttpServletRequest request,
			HttpServletResponse response, String nextProtocol)
			throws ServletException, IOException {

		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return "";
		}

		try {
			final String username = PIMSServlet.getUsername(request);

			Protocol protocol = version.findFirst(Protocol.class,
					Protocol.PROP_NAME, nextProtocol);
			assert protocol != null : "protocol should not be null";

			String inputSample = request.getParameter("inputSample"); // Crystal:org.pimslims.model.sample.Sample:1464788
			int firstColon = inputSample.indexOf(":");
			String sampleHook = inputSample.substring(firstColon + 1);
			System.out.println("Sample hook:" + sampleHook);

			Sample in = version.get(sampleHook);

			Experiment next = addNextExperiment(version, in, protocol);
			final String hook = next.get_Hook();
			org.pimslims.presentation.mru.MRUController.addObject(username,
					protocol);
			version.commit();
			return hook;
		} catch (final AccessException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	private String addFinalMountExperiment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return "";
		}

		try {
			Holder pin = null;
			String plateBarcode = request.getParameter("plateBarcode");
			String well = request.getParameter("well");
			String crystalNumber = request.getParameter("crystalNumber");

			String pinBarcode = request.getParameter("pin");
			String puckBarcode = request.getParameter("puck");
			String puckPosition = request.getParameter("puckPosition");

			String inputSample = request.getParameter("inputSample");
			int firstColon = inputSample.indexOf(":");
			String sampleHook = inputSample.substring(firstColon + 1);
			Sample crystal = version.get(sampleHook);
			assert null != crystal : "Cannot find crystal " + sampleHook;

			if ("".equals(pinBarcode)) {
				throw new BusinessException(
						"No pin barcode supplied for final mount experiment");
			} else {
				// && !SelectCrystalServlet.pinExistsInPims(pinBarcode,
				// version)) {
				boolean badPin = false;
				pin = version.findFirst(Holder.class, Holder.PROP_NAME,
						pinBarcode);
				if (null == pin) {
					badPin = true;
				} else {
					boolean categoryMatch = false;
					Set<HolderCategory> cats = pin.getHolderType()
							.getDefaultHolderCategories();
					Iterator<HolderCategory> i = cats.iterator();
					while (i.hasNext()) {
						HolderCategory hc = (HolderCategory) i.next();
						if (hc.getName().equals(PIN_CATEGORY)) {
							categoryMatch = true;
						}
					}
					if (!categoryMatch) {
						badPin = true;
					}
				}
				if (badPin) {
					request.setAttribute("nonExistentPin", pinBarcode);
					return null;
				}
			}
			if (("".equals(puckPosition) && !"".equals(puckBarcode))
					|| (!"".equals(puckPosition) && "".equals(puckBarcode))) {
				throw new BusinessException(
						"Only one of Puck barcode and puck position supplied - should be neither or both");
			} else if ("".equals(puckPosition) && "".equals(puckBarcode)) {
				// do nothing
			} else {
				// put the pin into the relevant puck slot
				boolean badPuck = false;
				Holder puck = version.findFirst(Holder.class, Holder.PROP_NAME,
						puckBarcode);

				if (null == puck) {
					badPuck = true;
				} else {
					boolean categoryMatch = false;
					Set<HolderCategory> cats = puck.getHolderType()
							.getDefaultHolderCategories();
					Iterator<HolderCategory> i = cats.iterator();
					while (i.hasNext()) {
						HolderCategory hc = (HolderCategory) i.next();
						System.out.println("HolderCategory name: "
								+ hc.getName());
						if (hc.getName().equals(PUCK_CATEGORY)) {
							categoryMatch = true;
						}
					}
					if (!categoryMatch) {
						badPuck = true;
					}
				}
				if (badPuck) {
					request.setAttribute("nonExistentPuck", puckBarcode);
					return null;
				}

				// check puck position is numeric and within acceptable range
				// for puck
				Integer positionInPuck = Integer.parseInt(puckPosition);
				HolderType holderType = (HolderType) puck.getHolderType();

				if (1 > positionInPuck
						|| holderType.getMaxRow() < positionInPuck) {
					throw new BusinessException(
							"Position in puck must be between 1 and "
									+ holderType.getMaxRow());
				}

				// If we get here, puck is good, pin is good, and puck
				// position
				// is OK.
				// First, empty all existing pin records from the puck if
				// appropriate
				if (null != request.getParameter("clearPuck")) {
					puck.setSubHolders(Collections.<AbstractHolder> emptySet());
				}

				// Then insert the pin into the specified position.
				pin.setContainer(puck);
				pin.setColPosition(1);
				pin.setRowPosition(positionInPuck);

			}

			Protocol protocol = version.findFirst(Protocol.class,
					Protocol.PROP_NAME, FINAL_MOUNT_PROTOCOL);
			assert protocol != null : "protocol should not be null";

			Experiment mountExperiment = addNextExperiment(version, crystal,
					protocol);

			Set<Parameter> parameters = mountExperiment.getParameters();
			Iterator<Parameter> i = parameters.iterator();
			while (i.hasNext()) {
				Parameter p = (Parameter) i.next();
				String parameterName = p.getParameterDefinition().getName();
				if ("Pin barcode".equals(parameterName)) {
					p.setValue(pinBarcode);
				} else if ("Puck barcode".equals(parameterName)) {
					p.setValue(puckBarcode);
				} else if ("Position in puck".equals(parameterName)) {
					p.setValue(puckPosition);
				}
			}

			// go back up the chain, and set the pin barcode in the Select
			// Crystal experiment too.
			Sample in = version.get(request.getParameter("inputSample"));
			if (null != in) {
				Experiment selectionExperiment = null;
				while (null == selectionExperiment) {
					Experiment e = in.getOutputSample().getExperiment();
					if (null == e) {
						break;
					}
					if (e.getExperimentType().getName()
							.equals(CrystalTreatmentServlet.EXPERIMENTTYPE)) {
						selectionExperiment = e;
						break;
					}
				}
				if (null != selectionExperiment) {
					Set<Parameter> params = selectionExperiment.getParameters();
					Iterator<Parameter> j = params.iterator();
					while (j.hasNext()) {
						Parameter p = (Parameter) j.next();
						String parameterName = p.getParameterDefinition()
								.getName();
						if ("Pin barcode".equals(parameterName)) {
							p.setValue(pinBarcode);
						}
					}
				}
			}

			Sample mountedCrystal = null;
			Set<OutputSample> oss = mountExperiment.getOutputSamples();
			Iterator<OutputSample> j = oss.iterator();
			while (j.hasNext()) {
				OutputSample os = (OutputSample) j.next();
				String osName = os.getRefOutputSample().get_Name();
				if ("Mounted crystal".equals(osName)) {
					mountedCrystal = os.getSample();
				}
			}

			if (!"".equals(pinBarcode) && null != mountedCrystal) {
				// already tested for its existence at top of try block
				pin = version.findFirst(Holder.class, Holder.PROP_NAME,
						pinBarcode);
				Iterator<Sample> k = pin.getSamples().iterator();
				while (k.hasNext()) {
					pin.removeSample((Sample) k.next());
				}
				mountedCrystal.setHolder(pin);
				mountedCrystal.setRowPosition(1);
				mountedCrystal.setColPosition(1);

			}

			Experiment preShip = addPreShipExperiment(version, mountedCrystal);
			if (null != request.getParameter("treatmentURL")) {
				Set<Parameter> params = preShip.getParameters();
				for (Parameter param : params) {
					String value = param.getValue();
					if (param.getName().equals("Treatment page URL")) {
						param.setValue(value);
						break;
					}
				}
			}

			version.commit();
			return mountExperiment.get_Hook();
		} catch (final AccessException e) {
			throw new ServletException(e);
		} catch (final ConstraintException e) {
			throw new ServletException(e);
		} catch (final AbortedException e) {
			throw new ServletException(e);
		} catch (final BusinessException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	public void updatePreShipExperiment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return;
		}
		try {
			String experimentHook = request.getParameter("preShipExperiment");
			Experiment experiment = version.get(experimentHook);
			Map<String, String> parameters = new HashMap<String, String>();
			// Parameter names relate to "PiMS Diffraction Test" protocol used
			// for pre-ship expts.
			if (null != request.getParameter("a")) {
				parameters.put("a", request.getParameter("a"));
			}
			if (null != request.getParameter("b")) {
				parameters.put("b", request.getParameter("b"));
			}
			if (null != request.getParameter("c")) {
				parameters.put("c", request.getParameter("c"));
			}
			if (null != request.getParameter("alpha")) {
				parameters.put("alpha", request.getParameter("alpha"));
			}
			if (null != request.getParameter("beta")) {
				parameters.put("beta", request.getParameter("beta"));
			}
			if (null != request.getParameter("gamma")) {
				parameters.put("gamma", request.getParameter("gamma"));
			}
			if (null != request.getParameter("spacegroup")) {
				parameters.put("Space group",
						request.getParameter("spacegroup"));
			}
			if (null != request.getParameter("heavyatom")) {
				parameters.put("Anomalous scatterer",
						request.getParameter("heavyatom"));
			}
			if (null != request.getParameter("comments")) {
				parameters.put("Comments", request.getParameter("comments"));
			}
			if (null != request.getParameter("treatmentURL")) {
				parameters.put("Treatment page URL",
						request.getParameter("treatmentURL"));
			}
			if (null != request.getParameter("datacolltype")) {
				parameters.put("Data collection type",
						request.getParameter("datacolltype"));
			}
			CrystalTreatmentServlet.updatePreShipExperiment(version,
					experiment, parameters);
			version.commit();
			return;
		} catch (AbortedException e) {
			throw new ServletException(e);
		} catch (ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	public static List<String> getHeavyAtoms(ReadableVersion version) {
		Protocol protocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, CrystalTreatmentServlet.PRE_SHIP_PROTOCOL);
		List<ParameterDefinition> params = protocol.getParameterDefinitions();
		for (ParameterDefinition pd : params) {
			if (pd.getName().equals("Anomalous scatterer")) {
				return pd.getPossibleValues();
			}

		}
		return null;
	}

	/**
	 * 
	 * @param version
	 * @param experiment
	 * @param values
	 *            A map of parameterDefinition.name to value. Any unsupplied
	 *            parameters will not be updated.
	 * @throws ConstraintException
	 */
	public static void updatePreShipExperiment(WritableVersion version,
			Experiment experiment, Map<String, String> values)
			throws ConstraintException {
		Set<Parameter> parameters = experiment.getParameters();
		for (Parameter p : parameters) {
			String pname = p.getParameterDefinition().getName().trim();
			String value = values.get(pname);
			if (null != value) {
				p.setValue(value);
			}
		}
	}

	public static Experiment addPreShipExperiment(WritableVersion version,
			Sample crystal) throws BusinessException, ConstraintException,
			AccessException {
		Set<SampleCategory> categories = crystal.getSampleCategories();
		SampleCategory sc = categories.iterator().next();
		if (!sc.getName().equals("Mounted Crystal")) {
			throw new BusinessException(
					"Attempted to add shipping info to sample, but sample was not a Mounted Crystal");
		}
		Experiment mountExperiment = crystal.getOutputSample().getExperiment();
		String protocolName = mountExperiment.getProtocol().getName();
		if (!protocolName.equals(FINAL_MOUNT_PROTOCOL)) {
			throw new BusinessException(
					"Attempted to add shipping info to sample, but its experiment was not "
							+ FINAL_MOUNT_PROTOCOL);
		}
		Protocol protocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, PRE_SHIP_PROTOCOL);
		if (null == protocol) {
			throw new BusinessException(
					"Attempted to add shipping info to sample, but Protocol '"
							+ FINAL_MOUNT_PROTOCOL + "' is not present");
		}

		Holder pin = crystal.getHolder();

		// Add the new experiment
		Experiment preShip = SelectCrystalServlet.addNextExperiment(version,
				crystal, protocol);

		Set<OutputSample> oss = preShip.getOutputSamples();
		OutputSample os = (OutputSample) oss.iterator().next();
		Sample crystalToShip = os.getSample();

		// Put the output of the new experiment into the previous output
		// sample's container. It's the same physical crystal, but a different
		// PiMS sample.
		crystal.setHolder(null);
		crystalToShip.setHolder(pin);
		crystalToShip.setRowPosition(1);
		crystalToShip.setColPosition(1);

		// Attempt to name the sample something sensible, like 9098A01-1_c2
		// This means going up the chain to find the drop, plate, etc.
		Protocol selectionProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.SELECTION_PROTOCOL);
		Experiment selection = SelectCrystalServlet
				.findPreviousExperimentByProtocol(version, crystalToShip,
						selectionProtocol);
		List<InputSample> iss = (List<InputSample>) selection.getInputSamples();
		Sample trialDrop = null;
		String cat = SelectCrystalServlet.DROP_CATEGORY;
		for (InputSample is : iss) {
			SampleCategory c = is.getSample().getSampleCategories().iterator()
					.next();
			if (cat.equals(c.getName())) {
				trialDrop = is.getSample();
			}
		}

		// Give the output a DLS-compliant sample name
		String sampleName = "";
		if (null == trialDrop) {
			// Set lousy, but DLS-compliant, default name
			sampleName = "crystal_";
		} else {

			Holder h = trialDrop.getHolder();
			sampleName += h.get_Name();
			sampleName += "_";

			String[] letters = new String[] { "A", "B", "C", "D", "E", "F",
					"G", "H" };
			int row = trialDrop.getRowPosition();
			if (row > letters.length) {
				// 9098_r1_c1_d1
				sampleName += "r" + row;
				sampleName += "_";
				sampleName += "c" + trialDrop.getRowPosition();
				sampleName += "_";
				sampleName += "d" + trialDrop.getSubPosition();
			} else {
				// 9098_A01-1
				sampleName += letters[row - 1];
				sampleName += trialDrop.getColPosition();
				sampleName += "-" + trialDrop.getSubPosition();
			}

		}

		// append sample's dbId - ensure unique
		sampleName += "_" + crystalToShip.getDbId();

		// safety for DLS compliance - weird barcode with a space?
		sampleName = sampleName.replaceAll("[^-_a-zA-Z0-9]", "");

		crystalToShip.setName(sampleName);

		return preShip;
	}

	/**
	 * Updates the experiment specified by the parameters, and returns its hook.
	 * Assumes all parameters relate to the same experiment!
	 * 
	 * @param request
	 * @param response
	 * @return the hook of the experiment
	 * @throws ServletException
	 * @throws IOException
	 */
	private String updateTreatmentExperiment(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		log.info("in updateTreatmentExperiment");

		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return "";
		}

		final Map<String, String[]> parms = request.getParameterMap();
		Map<String, String[]> parameters = new HashMap<String, String[]>();
		Map<String, String[]> sampleParameters = new HashMap<String, String[]>();

		// Remove duration-specific keys
		Iterator<String> i = parms.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			if (key.endsWith(":sec") || key.endsWith(":min")
					|| key.endsWith(":hr") || key.endsWith(":days")
					|| key.endsWith(":displayUnit")) {
				// skip
			} else if (key.startsWith(INPUTSAMPLE_CLASS)) {
				sampleParameters.put(key, parms.get(key));
			} else {
				parameters.put(key, parms.get(key));
			}
		}

		try {
			String exptHook = "";
			final HttpSession session = request.getSession();
			ExperimentUpdate.processRequest(version, parameters, session);
			UpdateInputSamples.processRequest(version, sampleParameters);
			log.info("end of updateTreatmentExperiment");

			// TODO return Hook of the experiment.
			Iterator<String> j = parameters.keySet().iterator();
			while (j.hasNext()) {
				String param = (String) j.next();
				if (param.startsWith(PARAMETER_CLASS)
						&& param.endsWith(":value")) {
					param = param.substring(0, param.length() - 6); // lose the
																	// ":value"
					Parameter p = (Parameter) version.get(param);
					exptHook = p.getExperiment().get_Hook();
				}
			}
			version.commit();
			return exptHook;

		} catch (Exception e) {
			log.info("in updateTreatmentExperiment catch block", e);
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}

	}

	private void copyTreatmentHistory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {

		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return;
		}

		try {
			String sourceExptHook = request.getParameter("sourceExperiment");
			String destinationExptHook = request
					.getParameter("selectionExperiment");

			Experiment sourceExperiment = version.get(sourceExptHook);
			Experiment destinationExperiment = version.get(destinationExptHook);

			doCopyTreatmentHistory(sourceExperiment, destinationExperiment,
					version);

			version.commit();
		} catch (BusinessException e) {
			throw new ServletException(e);
		} catch (AbortedException e) {
			throw new ServletException(e);
		} catch (ConstraintException e) {
			throw new ServletException(e);
		} catch (AccessException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	public static void doCopyTreatmentHistory(Experiment sourceExperiment,
			Experiment destinationExperiment, WritableVersion version)
			throws BusinessException, ConstraintException, AccessException {
		Sample sourceSample = sourceExperiment.getOutputSamples().iterator()
				.next().getSample();
		Sample destinationSample = destinationExperiment.getOutputSamples()
				.iterator().next().getSample();

		Experiment sourceNextExperiment;
		Experiment destinationNextExperiment;

		if (sourceExperiment.get_Hook()
				.equals(destinationExperiment.get_Hook())) {
			throw new BusinessException(
					CrystalTreatmentServlet.COPYTREATMENT_SAME_EXPERIMENTS);
		}
		if (!sourceExperiment.getProtocol().getName()
				.equals(SELECTION_PROTOCOL)) {
			throw new BusinessException(
					CrystalTreatmentServlet.COPYTREATMENT_BAD_SOURCE);
		}
		if (!destinationExperiment.getProtocol().getName()
				.equals(SELECTION_PROTOCOL)) {
			throw new BusinessException(
					CrystalTreatmentServlet.COPYTREATMENT_BAD_DESTINATION);
		}

		if (!destinationSample.getInputSamples().isEmpty()) {
			throw new BusinessException(
					"Destination crystal already has treatment history");
		}
		if (sourceSample.getInputSamples().isEmpty()) {
			throw new BusinessException(
					CrystalTreatmentServlet.COPYTREATMENT_NO_CHAIN);
		}

		while (!sourceSample.getInputSamples().isEmpty()) {
			Set<InputSample> inputs = sourceSample.getInputSamples();
			if (inputs.isEmpty()) {
				// we're done here - end of the chain
				break;
			}
			if (inputs.size() != 1) {
				// not the end of the chain, but we've come to a branch -
				// shouldn't happen
				log.info("inputs: " + inputs);
				throw new BusinessException(
						"Output of one treatment experiment has been used in multiple experiments - should be a simple chain, can't copy");
			}

			sourceNextExperiment = inputs.iterator().next().getExperiment();
			if (null != sourceNextExperiment.getProtocol()
					&& sourceNextExperiment.getProtocol().get_Name()
							.equals(FINAL_MOUNT_PROTOCOL)) {
				// don't clone the final mount experiment
				break;
			}
			destinationNextExperiment = ExperimentCopier.duplicate(
					sourceNextExperiment, version);
			log.info("old expt: " + sourceNextExperiment);
			log.info("new expt: " + destinationNextExperiment);
			List<InputSample> destinationInputs = destinationNextExperiment
					.getInputSamples();
			Iterator<InputSample> i = destinationInputs.iterator();
			while (i.hasNext()) {
				InputSample is = (InputSample) i.next();
				if (is.getName().equals("Crystal")) {
					is.setSample(destinationSample);
				}
			}

			sourceExperiment = sourceNextExperiment;
			assert sourceNextExperiment.getOutputSamples().size() == 1 : "Experiment "
					+ sourceNextExperiment.get_Hook()
					+ " should have exactly one OutputSample";
			sourceSample = sourceNextExperiment.getOutputSamples().iterator()
					.next().getSample();
			destinationExperiment = destinationNextExperiment;
			destinationSample = destinationNextExperiment.getOutputSamples()
					.iterator().next().getSample();

		}

	}

}
