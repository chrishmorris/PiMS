package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.pimslims.access.Access;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.experiment.ExperimentCopier;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.AbstractHolder;
import org.pimslims.model.holder.Containable;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.AbstractHolderType;
import org.pimslims.model.reference.HolderCategory;
import org.pimslims.model.reference.HolderType;
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

	public static final String COPYTREATMENT_SAME_EXPERIMENTS = "Source and destination experiments cannot be the same";
	public static final String COPYTREATMENT_NO_CHAIN = "Source crystal has no treatment history to copy";
	public static final String COPYTREATMENT_BAD_SOURCE = "Source experiment is not a "
			+ SelectCrystalServlet.SELECTION_PROTOCOL + " experiment";
	public static final String COPYTREATMENT_BAD_DESTINATION = "Destination experiment is not a "
			+ SelectCrystalServlet.SELECTION_PROTOCOL + " experiment";

	private static final Logger log = LoggerFactory
			.getLogger(CrystalTreatmentServlet.class);

	private static final String PIN_TYPE = "Pin";
	private static final String PUCK_TYPE = "Unipuck";
	private static final String DEWAR_CATEGORY = "Dewar";
	private static final String DEWAR_TYPE = "Dewar";
	private static final String CONTAINERS_LABNOTEBOOK = "Containers";

	@Override
	public String getServletInfo() {
		return "Used to display the treatment of an individual crystal after selection from within a trial drop";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (null != request.getParameter("verifyPinBarcode")) {
			JSONObject jo = verifyPinBarcode(request, response);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
		} else if (null != request.getParameter("verifyPuckBarcode")) {
			JSONObject jo = verifyPuckBarcode(request, response);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
		} else {
			super.doGet(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("CrystalTreatmentServlet.doPost: entering");

		if (null != request.getParameter("clearPuck")) {
			JSONObject jo = clearPuck(request, response);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
			return;
		} else if (null != request.getParameter("createPin")) {
			String pinBarcode = request.getParameter("createPin");
			JSONObject jo = createPin(pinBarcode);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
			return;
		} else if (null != request.getParameter("createPuck")) {
			String puckBarcode = request.getParameter("createPuck");
			JSONObject jo = createPuck(puckBarcode);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
			return;
		} else if (null != request.getParameter("createDewar")) {
			String dewarBarcode = request.getParameter("createDewar");
			JSONObject jo = createDewar(dewarBarcode);
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
			return;
		} else if (null != request.getParameter("createDummyPin")) {
			JSONObject jo = createPin("");
			response.setContentType("application/json");
			PrintWriter w = response.getWriter();
			w.write(jo.toString());
			return;
		}

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

	private JSONObject verifyPinBarcode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String barcode = request.getParameter("verifyPinBarcode");
		final ReadableVersion version = this.getReadableVersion(request,
				response);
		boolean isOK = pinExistsAndIsPin(version, barcode);
		JSONObject jo = new JSONObject();
		jo.put("pinBarcode", barcode);
		jo.put("pinExists", isOK);
		if (isOK) {
			Holder pin = version.findFirst(Holder.class, Holder.PROP_NAME,
					barcode);
			jo.put("mayUpdate", version.mayUpdate(pin));
		} else {
			jo.put("mayUpdate", false);
			jo.put("puckExists", puckExistsAndIsPuck(version, barcode));
		}
		version.abort();
		return jo;
	}

	private JSONObject createPin(String pinBarcode) throws ServletException,
			IOException {
		final WritableVersion version = ModelImpl.getModel()
				.getWritableVersion(Access.ADMINISTRATOR);
		JSONObject jo = new JSONObject();
		try {
			LabNotebook ln = version.findFirst(LabNotebook.class,
					LabNotebook.PROP_NAME,
					CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			if (null == ln) {
				throw new BusinessException("No LabNotebook called "
						+ CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			}
			final Map<String, Object> attrs = new HashMap<String, Object>();
			if ("".equals(pinBarcode) || null == pinBarcode) {
				pinBarcode = "pin_" + version.getUniqueID().toString();
			}
			attrs.put(Holder.PROP_NAME, pinBarcode);
			Holder pin = version.create(Holder.class, attrs);
			final HolderCategory hc = version.findFirst(HolderCategory.class,
					HolderCategory.PROP_NAME, PIN_CATEGORY);
			Collection<HolderCategory> categories = new HashSet<HolderCategory>();
			categories.add(hc);
			pin.setHolderCategories(categories);
			final HolderType type = version.findFirst(HolderType.class,
					HolderType.PROP_NAME, PIN_TYPE);
			pin.setHolderType(type);
			pin.setAccess(ln);
			String createdPin = pin.getName();
			version.commit();
			jo.put("createdPin", createdPin);
		} catch (AbortedException e) {
			jo.put("error", "Could not create pin - AbortedException thrown");
		} catch (ConstraintException e) {
			jo.put("error", "Could not create pin - ConstraintException thrown");
		} catch (AccessException e) {
			jo.put("error", "Could not create pin - AccessException thrown");
		} catch (BusinessException e) {
			jo.put("error", "Could not create pin - " + e.getMessage());
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
		return jo;
	}

	private JSONObject createPuck(String puckBarcode) throws ServletException,
			IOException {
		final WritableVersion version = ModelImpl.getModel()
				.getWritableVersion(Access.ADMINISTRATOR);
		JSONObject jo = new JSONObject();
		try {
			LabNotebook ln = version.findFirst(LabNotebook.class,
					LabNotebook.PROP_NAME,
					CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			if (null == ln) {
				throw new BusinessException("No LabNotebook called "
						+ CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			}
			final Map<String, Object> attrs = new HashMap<String, Object>();
			if ("".equals(puckBarcode) || null == puckBarcode) {
				jo.put("error", "No puck barcode provided for create");
			}
			attrs.put(Holder.PROP_NAME, puckBarcode);
			Holder puck = version.create(Holder.class, attrs);
			final HolderCategory hc = version.findFirst(HolderCategory.class,
					HolderCategory.PROP_NAME, PUCK_CATEGORY);
			Collection<HolderCategory> categories = new HashSet<HolderCategory>();
			categories.add(hc);
			puck.setHolderCategories(categories);
			final HolderType type = version.findFirst(HolderType.class,
					HolderType.PROP_NAME, PUCK_TYPE);
			puck.setHolderType(type);
			puck.setAccess(ln);
			String createdPuck = puck.getName();
			version.commit();
			jo.put("createdPuck", createdPuck);
		} catch (AbortedException e) {
			jo.put("error", "Could not create puck - AbortedException thrown");
		} catch (ConstraintException e) {
			jo.put("error",
					"Could not create puck - ConstraintException thrown");
		} catch (AccessException e) {
			jo.put("error", "Could not create puck - AccessException thrown");
		} catch (BusinessException e) {
			jo.put("error", "Could not create puck - " + e.getMessage());
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
		return jo;
	}

	private JSONObject createDewar(String dewarBarcode)
			throws ServletException, IOException {
		final WritableVersion version = ModelImpl.getModel()
				.getWritableVersion(Access.ADMINISTRATOR);
		JSONObject jo = new JSONObject();
		try {
			LabNotebook ln = version.findFirst(LabNotebook.class,
					LabNotebook.PROP_NAME,
					CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			if (null == ln) {
				throw new BusinessException("No LabNotebook called "
						+ CrystalTreatmentServlet.CONTAINERS_LABNOTEBOOK);
			}
			final Map<String, Object> attrs = new HashMap<String, Object>();
			if ("".equals(dewarBarcode) || null == dewarBarcode) {
				jo.put("error", "No dewar barcode provided for create");
			}
			attrs.put(Holder.PROP_NAME, dewarBarcode);
			Holder dewar = version.create(Holder.class, attrs);
			final HolderCategory hc = version.findFirst(HolderCategory.class,
					HolderCategory.PROP_NAME, DEWAR_CATEGORY);
			Collection<HolderCategory> categories = new HashSet<HolderCategory>();
			categories.add(hc);
			dewar.setHolderCategories(categories);
			final HolderType type = version.findFirst(HolderType.class,
					HolderType.PROP_NAME, DEWAR_TYPE);
			dewar.setHolderType(type);
			dewar.setAccess(ln);
			String createdDewar = dewar.getName();
			version.commit();
			jo.put("createdDewar", createdDewar);
		} catch (AbortedException e) {
			jo.put("error", "Could not create dewar - AbortedException thrown");
		} catch (ConstraintException e) {
			jo.put("error",
					"Could not create dewar - ConstraintException thrown");
		} catch (AccessException e) {
			jo.put("error", "Could not create dewar - AccessException thrown");
		} catch (BusinessException e) {
			jo.put("error", "Could not create dewar - " + e.getMessage());
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
		return jo;
	}

	private JSONObject clearPuck(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String barcode = request.getParameter("clearPuck");
		final WritableVersion version = this.getWritableVersion(request,
				response);
		boolean isOK = puckExistsAndIsPuck(version, barcode);
		JSONObject jo = new JSONObject();
		jo.put("puckBarcode", barcode);
		jo.put("puckExists", isOK);
		try {
			if (isOK) {
				Holder puck = version.findFirst(Holder.class, Holder.PROP_NAME,
						barcode);
				puck.setSubHolders(Collections.<AbstractHolder> emptySet());
				JSONObject jo2 = new JSONObject();
				HolderType ht = (HolderType) puck.getHolderType();
				jo2.put("positions", ht.getMaxRow());
				jo2.put("pins", Collections.EMPTY_SET);
				jo.put("pins", jo2);
			}
			version.commit();
		} catch (AbortedException e) {
			jo.put("error", "Could not clear puck:\n\n" + e.getMessage());
		} catch (ConstraintException e) {
			jo.put("error", "Could not clear puck:\n\n" + e.getMessage());
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
		return jo;
	}

	private JSONObject verifyPuckBarcode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String barcode = request.getParameter("verifyPuckBarcode");
		final ReadableVersion version = this.getReadableVersion(request,
				response);
		boolean isOK = puckExistsAndIsPuck(version, barcode);
		JSONObject jo = new JSONObject();
		jo.put("puckBarcode", barcode);
		jo.put("puckExists", isOK);
		if (isOK) {
			jo.put("pins", getPuckContents(version, barcode));
			Holder puck = version.findFirst(Holder.class, Holder.PROP_NAME,
					barcode);
			jo.put("mayUpdate", version.mayUpdate(puck));
		} else {
			jo.put("mayUpdate", false);
		}
		version.abort();
		return jo;
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

	private boolean puckExistsAndIsPuck(ReadableVersion version,
			String puckBarcode) {
		boolean badPuck = false;
		boolean categoryMatch = false;
		Holder puck = version.findFirst(Holder.class, Holder.PROP_NAME,
				puckBarcode);
		if (null == puck) {
			badPuck = true;
		} else {
			AbstractHolderType ht = puck.getHolderType();
			if (null == ht) {
				badPuck = true;
			} else {
				Set<HolderCategory> cats = ht.getDefaultHolderCategories();
				Iterator<HolderCategory> i = cats.iterator();
				while (i.hasNext()) {
					HolderCategory hc = (HolderCategory) i.next();
					if (hc.getName().equals(PUCK_CATEGORY)) {
						categoryMatch = true;
					}
				}
				if (!categoryMatch) {
					badPuck = true;
				}
			}
		}
		return !badPuck;
	}

	private JSONObject getPuckContents(ReadableVersion version,
			String puckBarcode) {
		Holder puck = version.findFirst(Holder.class, Holder.PROP_NAME,
				puckBarcode);
		if (null == puck) {
			return null;
		}
		JSONObject jo = new JSONObject();
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
				JSONObject pin = new JSONObject();
				pin.put("puckPosition", h.getRowPosition());
				pin.put("barcode", h.getName());
				pins.add(pin);
			}
		}
		jo.put("pins", pins);
		return jo;
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
				Protocol.PROP_NAME, SelectCrystalServlet.PRE_SHIP_PROTOCOL);
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
