package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.datastorage.DataStorageImpl;
import org.pimslims.crystallization.util.SequenceManager;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.diamond.ShipmentItemBean;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectShortBean;

public class ShipmentView extends XtalPIMSServlet {

	@Override
	public String getServletInfo() {
		return "View a shipment";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DataStorageImpl dataStorage = null;
		try {
			dataStorage = (DataStorageImpl) openResources(request);
			String groupHook = request.getParameter("shipment");
			if (null == groupHook) {
				throw new ServletException(
						"No experiment group hook specified (should be ?shipment=hook.of.ExperimentGroup:1234");
			}
			Map<String, Object> requestAttributes = processGet(dataStorage,
					groupHook);
			Iterator<String> i = requestAttributes.keySet().iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				request.setAttribute(key, requestAttributes.get(key));
			}
			dataStorage.closeResources();
			String destination = "/CrystalShipping/ViewDiamondShipment.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(destination);
			rd.forward(request, response);
		} catch (final BusinessException ex) {
			throw new ServletException(ex);
		} finally {
			closeResources(dataStorage);
		}
	}

	private Map<String, Object> processGet(DataStorageImpl ds, String groupHook)
			throws ServletException, BusinessException {
		Map<String, Object> requestAttributes = new HashMap<String, Object>();
		ReadableVersion version = ds.getVersion();
		ExperimentGroup grp = version.get(groupHook);
		if (null == grp) {
			throw new ServletException("No shipment found with hook "
					+ groupHook);
		}
		requestAttributes.put("shipment", BeanFactory.newBean(grp));
		Protocol selectionProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, SelectCrystalServlet.SELECTION_PROTOCOL);
		Protocol finalMountProtocol = version
				.findFirst(Protocol.class, Protocol.PROP_NAME,
						CrystalTreatmentServlet.FINALMOUNT_PROTOCOL);
		Protocol preShipProtocol = version.findFirst(Protocol.class,
				Protocol.PROP_NAME, CrystalTreatmentServlet.PRE_SHIP_PROTOCOL);
		Protocol protocol = null;
		Set<Experiment> expts = grp.getExperiments();
		if (expts.isEmpty()) {
			throw new ServletException("No experiments in shipment");
		}
		Set<ShipmentItemBean> items = new HashSet<ShipmentItemBean>();
		Iterator<Experiment> i = expts.iterator();
		while (i.hasNext()) {
			Experiment e = i.next();
			ShipmentItemBean item = new ShipmentItemBean();
			if (null == protocol) {
				Protocol p = e.getProtocol();
				if (!p.getName().equals(ShipmentCreate.DIFFRACTION_PROTOCOL)) {
					throw new ServletException(
							"Protocol of diffraction experiments should be "
									+ ShipmentCreate.DIFFRACTION_PROTOCOL);
				}
				requestAttributes.put("protocol", new ModelObjectShortBean(p));
				protocol = p;
			}
			Sample xtal = e.getInputSamples().iterator().next().getSample();
			item.setSampleName(xtal.get_Name());
			Experiment preShip = CrystalTreatmentServlet
					.findPreviousExperimentByProtocol(version, xtal,
							preShipProtocol);
			Sample mounted = preShip.getInputSamples().iterator().next()
					.getSample();
			Experiment finalMount = CrystalTreatmentServlet
					.findPreviousExperimentByProtocol(version, mounted,
							finalMountProtocol);

			/*
			 * NEW Need barcode from trials expt.plate Need to add sequence and
			 * name (or tagret) to shipment item bean
			 */
			Experiment selectionExperiment = CrystalTreatmentServlet
					.findPreviousExperimentByProtocol(version, mounted,
							selectionProtocol);
			List<InputSample> iss = (List<InputSample>) selectionExperiment
					.getInputSamples();
			Sample trialDrop = null;
			String cat = SelectCrystalServlet.DROP_CATEGORY;
			String barcode = "";
			for (InputSample is : iss) {
				SampleCategory c = is.getSample().getSampleCategories()
						.iterator().next();
				if (cat.equals(c.getName())) {
					trialDrop = is.getSample();
					barcode = trialDrop.getHolder().getName();
					if ("".equals(barcode)) {
						throw new BusinessException(
								"Could not find trial drop for sample "
										+ xtal.getName());
					}
					SequenceManager manager = new SequenceManager(ds);
					String sequence = manager.getSequence(barcode);
					sequence = sequence.replaceAll(".{10}", "$0 ");
					item.setProteinSequence(sequence.toUpperCase());
					ModelObjectShortBean ro = new ModelObjectShortBean(
							selectionExperiment.getResearchObjective());
					item.setProject(ro);
				}
			}

			Iterator<Parameter> j = preShip.getParameters().iterator();
			while (j.hasNext()) {
				Parameter param = (Parameter) j.next();
				String pname = param.getName().trim();
				String v = param.getValue();
				if ("Treatment page URL".equals(pname)) {
					item.setTreatmentURL(v);
				} else if ("Comments".equals(pname)) {
					item.setShippingComment(v);
				} else if ("Space group".equals(pname)) {
					item.setSpaceGroup(v);
				} else if ("Anomalous scatterer".equals(pname)) {
					item.setHeavyAtom(v);
				} else if ("Data collection type".equals(pname)) {
					item.setDataCollectionType(v);
				} else if ("a".equals(pname)) {
					item.setA(v);
				} else if ("b".equals(pname)) {
					item.setB(v);
				} else if ("c".equals(pname)) {
					item.setC(v);
				} else if ("alpha".equals(pname)) {
					item.setAlpha(v);
				} else if ("beta".equals(pname)) {
					item.setBeta(v);
				} else if ("gamma".equals(pname)) {
					item.setGamma(v);
				}
			}

			Iterator<Parameter> k = finalMount.getParameters().iterator();
			while (k.hasNext()) {
				Parameter param = (Parameter) k.next();
				String pname = param.getName();
				String v = param.getValue();
				if ("Pin barcode".equals(pname)) {
					item.setPinBarcode(v);
				} else if ("Puck barcode".equals(pname)) {
					item.setPuckBarcode(v);
				} else if ("Puck Position".equals(pname)) {
					item.setPuckPosition(v);
				}
			}

			items.add(item);
		}
		requestAttributes.put("samples", items);
		version.abort();
		return requestAttributes;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}