package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.service.TrialService;
import org.pimslims.business.crystallization.view.ImageView;
import org.pimslims.business.crystallization.view.TrialDropView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.CrystalHarvest;
import org.pimslims.crystallization.CrystalHarvest.CrystalCoordinate;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.util.SequenceManager;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sequence.ProteinSequence;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.ParameterDefinition;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.schedule.ScheduledTask;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.experiment.ExperimentWriter;
import org.pimslims.presentation.experiment.InputSampleBean;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.search.Searcher;
import org.pimslims.servlet.PIMSServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectCrystalServlet extends
		org.pimslims.crystallization.servlet.XtalPIMSServlet {

	private static final Logger log = LoggerFactory
			.getLogger(CrystalTreatmentServlet.class);

	// Show protocols with input and output samples in this category...
	public static final String DROP_CATEGORY = "TrialDrop";
	public static final String SAMPLE_CATEGORY = "Crystal";

	// ...and this experiment type
	public static final String EXPERIMENTTYPE = "Crystal Treatment";

	public static final String FINALMOUNT_PROTOCOL = "Finish crystal treatment";

	// These are the same as on the Diamond spreadsheet.
	public static final String[] spaceGroups = { "P1", "P2", "P21", "C2",
			"P222", "P2221", "P21212", "P212121", "C222", "C2221", "F222",
			"I222", "I212121", "P4", "P41", "P42", "P43", "P422", "P4212",
			"P4122", "P41212", "P4222", "P42212", "P4322", "P43212", "I4",
			"I41", "I422", "I4122", "P3", "P31", "P32", "P312", "P321",
			"P3112", "P3121", "P3212", "P3221", "P6", "P61", "P65", "P62",
			"P64", "P63", "P622", "P6122", "P6522", "P6222", "P6422", "P6322",
			"R3", "R32", "P23", "P213", "P432", "P4232", "P4332", "P4132",
			"F23", "F432", "F4132", "I23", "I213", "I432", "I4132" };

	@Override
	public String getServletInfo() {
		return "Used to record the selection of an individual crystal from within a trial drop";
	}

	public static final String SELECTION_PROTOCOL = "Select Crystal";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		log.info("SelectCrystalServlet.doGet: entering");

		final CommonRequestParams crp = CommonRequestParams
				.parseRequest(request);
		String barcode = request.getParameter("barcode");
		String well = request.getParameter("well"); // A01.1
		String subPosition = request.getParameter("subPosition");
		if (subPosition == null || subPosition.length() == 0) {
			subPosition = "1";
		}
		String crystal = request.getParameter("crystal");

		DataStorageImpl dataStorage = null;
		try {
			dataStorage = (DataStorageImpl) openResources(request);
			ReadableVersion version = dataStorage.getVersion();

			String temperature = "";

			WellPosition position = null;
			final TrialService trialService = dataStorage.getTrialService();
			final BusinessCriteria criteria = new BusinessCriteria(trialService);
			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_BARCODE, barcode, true));
				crp.getSessionBookmark().put(TrialDropView.PROP_BARCODE,
						barcode);
			}
			if ((well != null) && (!well.equals(""))) {
				criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
						well, true));
				crp.getSessionBookmark().put(TrialDropView.PROP_WELL, well);
				if (well.contains(".")) {
					position = new WellPosition(well);
					subPosition = "" + position.getSubPosition();
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

			// Make sure that the protocol we'll use has all the needed
			// parameters
			Protocol protocol = version
					.findFirst(Protocol.class, Protocol.PROP_NAME,
							SelectCrystalServlet.SELECTION_PROTOCOL);
			assert protocol != null : "protocol should not be null";
			List<ParameterDefinition> pds = protocol.getParameterDefinitions();
			boolean hasX = false;
			boolean hasY = false;
			boolean hasR = false;
			boolean hasConditions = false;
			boolean hasBuffer = false;
			boolean hasCrystalNumber = false;
			boolean hasBarcode = false;
			Iterator<ParameterDefinition> it = pds.iterator();
			while (it.hasNext()) {
				ParameterDefinition pd = (ParameterDefinition) it.next();
				if ("x".equals(pd.getName())) {
					hasX = true;
				} else if ("y".equals(pd.getName())) {
					hasY = true;
				} else if ("r".equals(pd.getName())) {
					hasR = true;
				} else if ("Conditions".equals(pd.getName())) {
					hasConditions = true;
				} else if ("Protein buffer".equals(pd.getName())) {
					hasBuffer = true;
					request.setAttribute("showBuffer", true);
				} else if ("Crystal number".equals(pd.getName())) {
					hasCrystalNumber = true;
				} else if ("Pin barcode".equals(pd.getName())) {
					hasBarcode = true;
				}
			}
			assert (hasX && hasY && hasR && hasConditions && hasCrystalNumber && hasBarcode) : "Select Crystal protocol should have the following parameters: x, y, r, Conditions, Crystal number, Pin barcode";

			TrialDropView drop = trialDrops.iterator().next();
			List<ImageView> images = drop.getImages();
			Iterator<ImageView> iter = images.iterator();
			ImageView image = null;
			while (iter.hasNext()) {
				ImageView im = (ImageView) iter.next();
				if (null == image) {
					image = im;
				} else if (im.getDate().after(image.getDate())) { // Always use
																	// the most
																	// recent
																	// image of
																	// the drop
					image = im;
				}
			}
			assert (null != image) : "Could not find any images for this drop";

			request.setAttribute("drops", trialDrops);
			request.setAttribute("barcode", barcode);
			request.setAttribute("well", well);
			request.setAttribute("image", image);
			request.setAttribute("foundImages", images);

			SequenceManager manager = new SequenceManager(dataStorage);
			String sequence = manager.getSequence(barcode);
			request.setAttribute("proteinSequence", sequence);

			// failure parameters if redirecting back here (pin didn't exist)
			request.setAttribute("newX", request.getParameter("newX"));
			request.setAttribute("newY", request.getParameter("newY"));

			// used in top "harvesting box"
			request.setAttribute("failedPinBarcode",
					request.getParameter("failedPinBarcode"));
			// used in final mount box
			request.setAttribute("nonExistentPin",
					request.getParameter("nonExistentPin"));
			request.setAttribute("nonExistentPuck",
					request.getParameter("nonExistentPuck"));

			// protocols for follow-on treatments
			request.setAttribute("protocols", findProtocols(version));

			// lab notebooks for user
			boolean mayCreate = false;
			Collection<ModelObjectShortBean> labNotebooks = PIMSServlet
					.getPossibleCreateOwners(version);
			request.setAttribute("labNotebooks", labNotebooks);
			if (labNotebooks.size() > 0) {
				mayCreate = true;
			}
			request.setAttribute("mayCreate", mayCreate);

			// Coordinates of previously-selected crystals in this drop
			Experiment trial = SelectCrystalServlet.findExperiment(barcode,
					position, version);
			Sample trialDropSample = trial.getOutputSamples().iterator().next()
					.getSample();
			request.setAttribute("trialDropSample", trialDropSample);

			String imagerName = "";
			String imagerTemperature = "";

			Holder plate = (Holder) trialDropSample.getContainer();
			ScheduledTask inspection = version.findFirst(ScheduledTask.class,
					ScheduledTask.PROP_HOLDER, plate);
			Instrument imager = inspection.getInstrument();
			if (null != imager) {
				imagerName = imager.getName();
				Float temp = imager.getTemperature();
				if (null != temp) {
					imagerTemperature = temp.toString();
				} else {
				}
			} else {
				imagerName = "No imager";
			}

			List<CrystalCoordinate> xys = CrystalHarvest.getXYs(trial);
			request.setAttribute("coords", xys);

			Experiment first = SelectCrystalServlet.getFirstExperiment(version,
					barcode);
			Project project = first.getProject();

			if (null != project) {
				ModelObjectShortBean msb = new ModelObjectShortBean(
						(ModelObject) project);
				request.setAttribute("project", msb);
			}

			if (null != crystal) {
				int xtal = Integer.parseInt(crystal);
				String selectionExperimentHook = "";
				List<CrystalCoordinate> xysWithTreatmentHistory = new ArrayList<CrystalCoordinate>();
				Iterator<CrystalCoordinate> xtals = xys.iterator();
				while (xtals.hasNext()) {
					CrystalCoordinate cc = (CrystalCoordinate) xtals.next();
					if (Integer.parseInt(cc.getCrystalNumber()) == xtal) {
						selectionExperimentHook = cc.getHook();
					} else {
						// determine whether the selection has following
						// experiments, and if so, whether the
						// first one is NOT "final mount" - in other words, is
						// there a treatment history that
						// can be copied?
						Experiment otherSelection = version.get(cc.getHook());
						Sample out = otherSelection.getOutputSamples()
								.iterator().next().getSample();
						Set<InputSample> ins = out.getInputSamples();
						if (1 == ins.size()) {
							Experiment firstFollowing = ins.iterator().next()
									.getExperiment();
							if (null != firstFollowing.getProtocol()
									&& !firstFollowing.getProtocol().getName()
											.equals(FINALMOUNT_PROTOCOL)) {
								xysWithTreatmentHistory.add(cc);
							}
						}
					}
				}
				if (selectionExperimentHook.equals("")) {
					throw new BusinessException(
							"Crystal ID supplied doesn't exist");
				}
				request.setAttribute("crystal", xtal);
				request.setAttribute("otherCoordsWithTreatmentHistory",
						xysWithTreatmentHistory);

				// now get following experiments
				Experiment select = (Experiment) version
						.get(selectionExperimentHook);
				request.setAttribute("selectionExperiment",
						new ModelObjectBean(select));

				String conditions = "";
				String buffer = "";
				String pinBarcode = "";
				Set<Parameter> parameters = select.getParameters();
				Iterator<Parameter> iter1 = parameters.iterator();
				while (iter1.hasNext()) {
					Parameter p = (Parameter) iter1.next();
					String parameterName = p.getParameterDefinition().getName();
					if ("Conditions".equals(parameterName)) {
						conditions = p.getValue();
					} else if ("Temperature".equals(parameterName)) {
						temperature = p.getValue();
					} else if ("Protein buffer".equals(parameterName)) {
						buffer = p.getValue();
					} else if ("Pin barcode".equals(parameterName)) {
						pinBarcode = p.getValue();
					}
				}
				if ("".equals(temperature)) {
					temperature = imagerTemperature;
				}

				request.setAttribute("conditions", conditions);
				request.setAttribute("buffer", buffer);
				request.setAttribute("pinBarcode", pinBarcode);
				request.setAttribute("spaceGroups",
						SelectCrystalServlet.spaceGroups);

				request.setAttribute("finalMountExperiment", null);
				request.setAttribute("finalMountSample", null);
				request.setAttribute("preShipExperiment", null);
				request.setAttribute("preShipSample", null);
				request.setAttribute("diffractionExperiment", null);
				request.setAttribute("diffractionSample", null);

				OutputSample os = select.getOutputSamples().iterator().next();

				ArrayList<Experiment> experimentChain = new ArrayList<Experiment>();
				String lastOutput = os.getSample().get_Hook();
				String lastExperiment = os.getExperiment().get_Hook();
				request.setAttribute("selectedCrystalSample", lastOutput);
				request.setAttribute("pinReused", true);
				request.setAttribute("crystalShipped", false);
				Set<InputSample> iss = os.getSample().getInputSamples();
				while (!iss.isEmpty()) {
					InputSample is = iss.iterator().next();
					Experiment nextExperiment = (Experiment) is.getExperiment();
					if (null != nextExperiment.getProtocol()) {
						String protocolName = nextExperiment.getProtocol()
								.getName();
						parameters = nextExperiment.getParameters();
						Iterator<Parameter> paramIterator = parameters
								.iterator();

						if (FINALMOUNT_PROTOCOL.equals(protocolName)) {

							while (paramIterator.hasNext()) {
								Parameter p = (Parameter) paramIterator.next();
								String parameterName = p
										.getParameterDefinition().getName();
								if ("Pin barcode".equals(parameterName)) {
									pinBarcode = p.getValue();
									request.setAttribute("pinBarcode",
											pinBarcode);
								}
							}
							OutputSample out = nextExperiment
									.getOutputSamples().iterator().next();
							Sample output = out.getSample();
							request.setAttribute("finalMountExperiment",
									new ModelObjectShortBean(nextExperiment));
							request.setAttribute("finalMountSample",
									new ModelObjectShortBean(output));

						} else if (CrystalTreatmentServlet.PRE_SHIP_PROTOCOL
								.equals(protocolName)) {

							while (paramIterator.hasNext()) {
								Parameter p = (Parameter) paramIterator.next();
								String parameterName = p
										.getParameterDefinition().getName();
								parameterName = parameterName.replaceAll(
										"[^a-zA-Z0-9]", "");
								request.setAttribute("ship_" + parameterName,
										p.getValue());
							}
							OutputSample out = nextExperiment
									.getOutputSamples().iterator().next();
							Sample output = out.getSample();
							request.setAttribute("preShipExperiment",
									new ModelObjectShortBean(nextExperiment));
							request.setAttribute("preShipSample",
									new ModelObjectShortBean(output));

						} else if (ShipmentCreate.DIFFRACTION_PROTOCOL
								.equals(protocolName)) {

							request.setAttribute("crystalShipped", true);
							while (paramIterator.hasNext()) {
								Parameter p = (Parameter) paramIterator.next();
							}
							request.setAttribute("diffractionExperiment",
									new ModelObjectShortBean(nextExperiment));
							// No output sample from a diffraction experiment
						} else {
							experimentChain.add(nextExperiment);
						}
					}

					Set<OutputSample> oss = nextExperiment.getOutputSamples();
					if (oss.isEmpty()) {
						break;
					}
					os = oss.iterator().next();

					Holder pin = (Holder) os.getSample().getHolder();
					if (null != pin && pinBarcode.equals(pin.getName())) {
						request.setAttribute("pinReused", false);
					}

					lastOutput = os.getSample().get_Hook();
					lastExperiment = nextExperiment.get_Hook();
					iss = os.getSample().getInputSamples();
				}

				request.setAttribute("heavyAtoms",
						CrystalTreatmentServlet.getHeavyAtoms(version));
				request.setAttribute("lastOutputSample", lastOutput);
				request.setAttribute("lastExperiment", lastExperiment);
				request.setAttribute("experimentChain", experimentChain);

				// if(milestone not achieved){
				request.setAttribute("finished", false);
				// }

				Map<String, InputSampleBean> inputSampleBeans = new HashMap<String, InputSampleBean>();
				Iterator<Experiment> i = experimentChain.iterator();
				while (i.hasNext()) {
					Experiment e = (Experiment) i.next();
					Iterator<?> j = e.getInputSamples().iterator();
					while (j.hasNext()) {
						InputSample is = (InputSample) j.next();
						InputSampleBean isb = new InputSampleBean(is);
						inputSampleBeans.put(is.get_Hook(), isb);
					}
				}
				request.setAttribute("inputSampleBeans", inputSampleBeans);

				request.setAttribute("imager", imagerName);
				request.setAttribute("temperature", temperature);

				String destination = "/CrystalHandling/Treatment.jsp";
				if (!mayCreate) {
					destination = "/CrystalHandling/CantHarvest.jsp";
				}
				RequestDispatcher rd = request
						.getRequestDispatcher(destination);
				rd.forward(request, response);
			} else {
				// see if there are any previous Selection experiments. If so,
				// pre-fill the conditions
				String conditions = "";
				Iterator<CrystalCoordinate> i = xys.iterator();
				while (i.hasNext()) {
					CrystalCoordinate cc = (CrystalCoordinate) i.next();
					Experiment select = version.get(cc.getHook());
					Set<Parameter> parameters = select.getParameters();
					Iterator<Parameter> iter1 = parameters.iterator();
					while (iter1.hasNext()) {
						Parameter p = (Parameter) iter1.next();
						String parameterName = p.getParameterDefinition()
								.getName();
						if ("Conditions".equals(parameterName)) {
							conditions = p.getValue();
						} else if ("Temperature".equals(parameterName)) {
							temperature = p.getValue();
						}
					}
					if (!"".equals(conditions)) {
						break;
					}
				}
				request.setAttribute("conditions", conditions);

				if ("".equals(temperature)) {
					temperature = imagerTemperature;
				}
				request.setAttribute("imager", imagerName);
				request.setAttribute("temperature", temperature);

				String destination = "/CrystalHandling/Harvesting.jsp";
				if (!mayCreate) {
					destination = "/CrystalHandling/CantHarvest.jsp";
				}
				RequestDispatcher rd = request
						.getRequestDispatcher(destination);
				rd.forward(request, response);
			}

		} catch (final BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			closeResources(dataStorage);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PIMSServlet.validatePost(request);

		log.info("doPost: entering");

		DataStorageImpl dataStorage = null;
		dataStorage = (DataStorageImpl) openResources(request);
		WritableVersion version = dataStorage.getWritableVersion();
		if (null == version) {
			return;
		}
		try {

			final CommonRequestParams crp = CommonRequestParams
					.parseRequest(request);
			String barcode = request.getParameter("barcode");
			String well = request.getParameter("well"); // A01.1
			String subPosition = request.getParameter("subPosition");
			if (subPosition == null || subPosition.length() == 0) {
				subPosition = "1";
			}
			String destination = request.getContextPath()
					+ "/update/CrystalTreatment/?barcode=" + barcode + "&well="
					+ well;

			if (null != request.getParameter("delete")) {

				log.info("delete found");
				deleteHooks(request, response);
				destination = request.getContextPath()
						+ "/update/CrystalTreatment/?barcode=" + barcode
						+ "&well=" + well;
				version.commit();

			} else {

				// validate the sequence - should have been caught in JS, so
				// just try this raw.
				// will throw BioException - catch below.
				new ProteinSequence(request.getParameter("proteinsequence"));

				// Set the target sequence - or create a new PiMS record if none
				// exists
				SequenceManager manager = new SequenceManager(dataStorage);
				manager.setSequence(barcode,
						request.getParameter("proteinsequence"));

				String experimentHook = addOrUpdateSelectCrystalExperiment(
						request, response, version);
				if (null == experimentHook) {
					// bounce back to selection UI - pin was specified and
					// didn't
					// exist?
					version.abort();
					String failedPinBarcode = request
							.getParameter("pinbarcode");
					String newX = request.getParameter("x");
					String newY = request.getParameter("y");
					destination = request.getContextPath()
							+ "/update/SelectCrystal/?barcode=" + barcode
							+ "&well=" + well + "&newX=" + newX + "&newY="
							+ newY + "&failedPinBarcode=" + failedPinBarcode;
				} else {
					log.info("doPost: experiment hook is " + experimentHook
							+ ", about to get xtal number for redirect");
					// Need the crystal ID of the one we just made/updated.
					int crystal = 0;
					Sample drop = version.get(request
							.getParameter("inputSample"));
					Experiment expt = version.get(experimentHook);
					Set<Parameter> parameters = expt.getParameters();
					Iterator<Parameter> i = parameters.iterator();
					while (i.hasNext()) {
						Parameter p = (Parameter) i.next();
						String parameterName = p.getParameterDefinition()
								.getName();
						if ("Crystal number".equals(parameterName)) {
							crystal = Integer.parseInt(p.getValue());
						}
					}

					if (0 == crystal) {
						throw new BusinessException(
								"Selection experiment does not exist");
					}

					destination = request.getContextPath()
							+ "/update/CrystalTreatment/?barcode=" + barcode
							+ "&well=" + well + "&crystal=" + crystal;
					version.commit();
				}
			}

			log.info("doPost: ended, about to redirect");
			PIMSServlet.redirectPost(response, destination);

		} catch (AbortedException e) {
			throw new ServletException(e);
		} catch (ConstraintException e) {
			throw new ServletException(e);
		} catch (NumberFormatException e) {
			throw new ServletException(e);
		} catch (BusinessException e) {
			throw new ServletException(e);
		} catch (AccessException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	private String addOrUpdateSelectCrystalExperiment(
			HttpServletRequest request, HttpServletResponse response,
			WritableVersion version) throws ServletException, IOException,
			BusinessException, AccessException, ConstraintException {

		int x = Integer.parseInt(request.getParameter("x"));
		int y = Integer.parseInt(request.getParameter("y"));
		int r = Integer.parseInt(request.getParameter("r"));
		String conditions = request.getParameter("conditions");
		String buffer = request.getParameter("buffer");
		String temperature = request.getParameter("temperature");
		String pinBarcode = request.getParameter("pinbarcode");
		String sampleHook = "Drop containing crystal:"
				+ request.getParameter("inputSample");
		String projectHook = request.getParameter("_OWNER");
		String experimentName = request.getParameter("barcode")
				+ request.getParameter("well") + " Select Crystal "
				+ request.getParameter("crystalnumber");

		if ("" != pinBarcode
				&& !SelectCrystalServlet.pinExistsInPims(pinBarcode, version)) {
			request.setAttribute("pinErrorOnSelect", "Pin " + pinBarcode
					+ " does not exist in PiMS");
			return null;
		}
		int nextCrystalNumber = 0;
		String experimentHook = "";
		if (null != request.getParameter("selectionExperiment")) {
			experimentHook = request.getParameter("selectionExperiment");
		} else {
			sampleHook = request.getParameter("inputSample");
			Sample sample = version.get(sampleHook);
			assert null != sample : "Cannot find sample with hook "
					+ sampleHook;
			Protocol protocol = version
					.findFirst(Protocol.class, Protocol.PROP_NAME,
							SelectCrystalServlet.SELECTION_PROTOCOL);
			Experiment nextExperiment = SelectCrystalServlet.addNextExperiment(
					version, sample, protocol);
			experimentHook = nextExperiment.get_Hook();
		}

		Experiment experiment = version.get(experimentHook);
		Sample drop = version.get(request.getParameter("inputSample"));
		SelectCrystalServlet
				.updateConditionsAndTemperatureInSelectionExperiments(drop,
						conditions, temperature, buffer);
		if (null != request.getParameter(experimentHook + ":pageNumber")) {
			experiment.setPageNumber(request.getParameter(experimentHook
					+ ":pageNumber"));
		}
		if (null != request.getParameter(experimentHook + ":startDate")) {
			String sd = request.getParameter(experimentHook + ":startDate");
			long lng = Long.parseLong(sd);
			Calendar startDate = new GregorianCalendar();
			startDate.setTimeInMillis(lng);
			experiment.setStartDate(startDate);
			experiment.setEndDate(startDate); // same as start date
		}

		if (null == request.getParameter("selectionExperiment")) {
			Experiment trial = drop.getOutputSample().getExperiment();
			List<CrystalCoordinate> previous = CrystalHarvest.getXYs(trial);
			Iterator<CrystalCoordinate> i = previous.iterator();
			while (i.hasNext()) {
				CrystalCoordinate prev = (CrystalCoordinate) i.next();
				int xnum = Integer.parseInt(prev.getCrystalNumber());
				if (xnum > nextCrystalNumber) {
					nextCrystalNumber = xnum;
				}
			}
			nextCrystalNumber += 1;
		}
		log.info("Determined next crystal number: " + nextCrystalNumber);

		Set<Parameter> parameters = experiment.getParameters();
		Iterator<Parameter> i = parameters.iterator();
		while (i.hasNext()) {
			Parameter p = (Parameter) i.next();
			String parameterName = p.getParameterDefinition().getName();
			if ("x".equals(parameterName)) {
				p.setValue(x + "");
			} else if ("y".equals(parameterName)) {
				p.setValue(y + "");
			} else if ("r".equals(parameterName)) {
				p.setValue(r + "");
			} else if ("Conditions".equals(parameterName)) {
				p.setValue(conditions);
			} else if ("Pin barcode".equals(parameterName)) {
				p.setValue(pinBarcode);
			} else if ("Crystal number".equals(parameterName)
					&& 0 != nextCrystalNumber) {
				p.setValue(new Integer(nextCrystalNumber).toString());
			}
		}

		log.info("Successfully set crystal number " + nextCrystalNumber
				+ " and returning experiment hook " + experimentHook);
		return experimentHook;

	}

	/**
	 * For the supplied trial drop, sets the "Conditions" parameter on the child
	 * Select Crystal experiments. It would make more sense to store the
	 * conditions in the Trial experiment, but in Oulu the Formulatrix importer
	 * creates experiments that are readable to all users. We need to put the
	 * conditions in the selection experiment so that they are under access
	 * control.
	 * 
	 * @param trialDrop
	 *            The trial drop from which the crystals are selected
	 * @param conditions
	 *            The crystallization conditions to set
	 * @throws ConstraintException
	 */
	private static void updateConditionsAndTemperatureInSelectionExperiments(
			Sample trialDrop, String conditions, String temperature,
			String buffer) throws ConstraintException {
		Set<InputSample> inputs = trialDrop.getInputSamples();
		Iterator<InputSample> i = inputs.iterator();
		while (i.hasNext()) {
			InputSample input = (InputSample) i.next();
			Experiment expt = input.getExperiment();
			Set<Parameter> parameters = expt.getParameters();
			Iterator<Parameter> j = parameters.iterator();
			while (j.hasNext()) {
				Parameter p = (Parameter) j.next();
				String parameterName = p.getParameterDefinition().getName();
				if ("Conditions".equals(parameterName)) {
					p.setValue(conditions);
				} else if ("Temperature".equals(parameterName)) {
					p.setValue(temperature);
				} else if (null != buffer
						&& "Protein buffer".equals(parameterName)) {
					p.setValue(buffer);
				}
			}
		}
	}

	public static Protocol findProtocol(ReadableVersion version)
			throws BusinessException {
		Protocol protocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SELECTION_PROTOCOL);
		if (null == protocol) {
			throw new BusinessException("No protocol found with name "
					+ SELECTION_PROTOCOL);
		}
		return protocol;
	}

	public static Experiment findExperiment(final String barcode,
			final WellPosition wellPosition, ReadableVersion version) {
		Holder holder = version.findFirst(Holder.class, Holder.PROP_NAME,
				barcode);
		if (null == holder) {
			return null;
		}
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put(org.pimslims.model.sample.Sample.PROP_ROWPOSITION,
				wellPosition.getRow());
		criteria.put(org.pimslims.model.sample.Sample.PROP_COLPOSITION,
				wellPosition.getColumn());
		criteria.put(org.pimslims.model.sample.Sample.PROP_SUBPOSITION,
				wellPosition.getSubPosition());
		org.pimslims.model.sample.Sample sample = holder.findFirst(
				Holder.PROP_SAMPLES, criteria);
		if (null == sample) {
			return null;
		}
		if (null == sample.getOutputSample()) {
			return null;
		}
		return sample.getOutputSample().getExperiment();
	}

	protected boolean deleteHooks(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		log.info("deleteHooks: entering");
		PIMSServlet.validatePost(request);
		final WritableVersion version = this.getWritableVersion(request,
				response);
		if (null == version) {
			return false;
		}

		try {
			String[] hooks = request.getParameterValues("hook");
			for (int i = 0; i < hooks.length; i++) {
				final org.pimslims.metamodel.ModelObject object = this
						.getRequiredObject(version, request, response, hooks[i]);
				if (object == null) {
					return false;
				}
				version.delete(object);
				MRUController.deleteObject(object.get_Hook());
			}
			version.commit();
			log.info("deleteHooks: finished");
			return true;
		} catch (AbortedException e) {
			throw new ServletException(e);
		} catch (ConstraintException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		} catch (AccessException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				version.abort();
			}
		}
	}

	protected Collection<ModelObject> findProtocols(ReadableVersion version) {
		final Searcher searcher = new Searcher(version);
		final MetaClass metaClass = version.getMetaClass(Protocol.class);
		final Map<String, Object> criteria = new HashMap<String, Object>();
		final Map<String, Object> categoryCriteria = new HashMap<String, Object>();
		final SampleCategory category = version.findFirst(SampleCategory.class,
				SampleCategory.PROP_NAME, SAMPLE_CATEGORY);
		final ExperimentType type = version.findFirst(ExperimentType.class,
				ExperimentType.PROP_NAME, EXPERIMENTTYPE);
		categoryCriteria.put(RefInputSample.PROP_SAMPLECATEGORY, category);
		criteria.put(Protocol.PROP_REFINPUTSAMPLES, categoryCriteria);
		criteria.put(Protocol.PROP_REFOUTPUTSAMPLES, categoryCriteria);
		criteria.put(Protocol.PROP_EXPERIMENTTYPE, type);
		final Collection<ModelObject> results = searcher.search(criteria,
				metaClass);
		return results;
	}

	public static boolean pinExistsInPims(String pinBarcode,
			ReadableVersion version) {
		Holder holder = version.findFirst(Holder.class, Holder.PROP_NAME,
				pinBarcode);
		if (null == holder) {
			return false;
		}
		return true;
	}

	static Experiment getFirstExperiment(ReadableVersion version, String barcode) {
		Experiment experiment;
		ExperimentGroup group = version.findFirst(ExperimentGroup.class,
				ExperimentGroup.PROP_NAME, barcode);
		experiment = group.findFirst(ExperimentGroup.PROP_EXPERIMENTS,
				java.util.Collections.EMPTY_MAP);
		return experiment;
	}

	/**
	 * Creates a new Experiment using the supplied Protocol, and sets the
	 * supplied Sample as the InputSample in the first role for which it is
	 * suitable.
	 * 
	 * @param version
	 * @param sample
	 * @param protocol
	 * @return the new Experiment
	 * @throws AccessException
	 * @throws ConstraintException
	 * @throws IllegalArgumentException
	 *             if the supplied Sample is not suitable for use as an input to
	 *             the protocol.
	 * @see comment on CreateExperiment.setSample for rationale
	 */
	public static Experiment addNextExperiment(WritableVersion version,
			Sample sample, Protocol protocol) throws AccessException,
			ConstraintException {

		assert null != sample : "Sample cannot be null";
		assert null != protocol : "Protocol cannot be null";
		ExperimentType type = protocol.getExperimentType();
		assert null != type : "Protocol's ExperimentType cannot be null";
		OutputSample os = sample.getOutputSample();
		assert null != os : "Sample has no OutputSample";
		Experiment prev = os.getExperiment();
		assert null != prev : "Sample has no previous experiment";

		String owner = sample.get_Owner();
		final Map<String, Object> params = new HashMap<String, Object>();
		Calendar startDate = new GregorianCalendar();

		String ename = version.getUniqueName(Experiment.class, type.get_Name()
				.replace(" ", "_"));
		params.put(Experiment.PROP_NAME, ename);
		params.put(Experiment.PROP_EXPERIMENTTYPE, type);
		params.put(Experiment.PROP_STARTDATE, startDate);
		params.put(Experiment.PROP_ENDDATE, startDate);
		params.put(Experiment.PROP_STATUS, "To_be_run");

		final Experiment nextExperiment = version.create(owner,
				Experiment.class, params);

		ExperimentWriter.createOutputSamplesForExperiment(version,
				nextExperiment, protocol);
		HolderFactory.createInputSamplesForExperiment(version, nextExperiment,
				protocol);
		ExperimentFactory.createProtocolParametersForExperiment(version,
				protocol, nextExperiment);
		boolean inputSet = false;
		final Set<SampleCategory> categories = sample.getSampleCategories();
		final List<InputSample> iss = nextExperiment.getInputSamples();
		for (final Iterator<InputSample> iterator = iss.iterator(); iterator
				.hasNext();) {
			final InputSample is = (InputSample) iterator.next();
			if (categories.contains(is.getRefInputSample().getSampleCategory())) {
				is.setSample(sample);
				inputSet = true;
				break;
			}
		}
		if (!inputSet) {
			throw new IllegalArgumentException("No suitable role for sample: "
					+ sample.get_Name() + " in Protocol " + protocol.get_Name());
		}

		final Set<OutputSample> oss = nextExperiment.getOutputSamples();
		for (final Iterator<OutputSample> iterator = oss.iterator(); iterator
				.hasNext();) {
			final OutputSample out = (OutputSample) iterator.next();
			String sname = version.getUniqueName(Sample.class,
					protocol.getName());
			System.out.println(sname);
			out.getSample().setName(sname);
		}

		nextExperiment.setProject(prev.getProject());
		nextExperiment.setProtocol(protocol);

		return nextExperiment;
	}

	/**
	 * Goes back up the experiment chain from startSample, via "Crystal" and
	 * "Mounted Crystal" samples, returning the closest previous Experiment
	 * matching the supplied Protocol.
	 * 
	 * @param version
	 * @param startSample
	 * @param protocol
	 * @return The closest previous Experiment matching the Protocol, or null if
	 *         none found.
	 */
	public static Experiment findPreviousExperimentByProtocol(
			ReadableVersion version, Sample startSample, Protocol neededProtocol) {

		Sample sample = startSample;
		// Check that we're starting with a Crystal or MountedCrystal
		boolean categoryOK = false;
		Set<SampleCategory> scs = sample.getSampleCategories();
		for (SampleCategory sc : scs) {
			String name = sc.getName();
			if (name.equals("Crystal") || name.equals("Mounted Crystal")) {
				categoryOK = true;
				break;
			}
		}
		if (!categoryOK) {
			return null;
		}
		while (null != sample) {
			System.out.println("sample: " + sample.getName());
			OutputSample os = sample.getOutputSample();
			if (null == os) {
				return null;
			}
			System.out.println("outputSample: " + os.getName());

			Experiment expt = os.getExperiment();
			if (null == expt) {
				return null;
			}
			System.out.println("experiment: " + expt.getName());

			Protocol p = expt.getProtocol();
			if (null == p) {
				return null;
			}
			System.out.println("protocol: " + p.getName());
			if (p.get_Hook().equals(neededProtocol.get_Hook())) {
				// Found it!
				System.out.println("found expt, returning");
				return expt;
			}

			// Keep looking... Null out sample and try to find an appropriate
			// one in the InputSamples; if we find one, go round again
			sample = null;
			List<InputSample> iss = expt.getInputSamples();
			for (InputSample is : iss) {
				System.out.println("inputsample: " + is.getName());
				Sample s = is.getSample();
				if (null == s) {
					continue;
				}
				categoryOK = false;
				scs = s.getSampleCategories();
				for (SampleCategory sc : scs) {
					System.out.println("samplecategory: " + sc.getName());
					String name = sc.getName();
					if (name.equals("Crystal")
							|| name.equals("Mounted Crystal")) {
						categoryOK = true;
						sample = s;
						break;
					}
				}
			}
		}
		System.out.println("found nothing, returning null");
		return null;
	}

}
