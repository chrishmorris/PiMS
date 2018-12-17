package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.servlet.PIMSServlet;

public class MountServlet extends
		org.pimslims.crystallization.servlet.XtalPIMSServlet {

	@Override
	public String getServletInfo() {
		return "Used to mount a crystal, from a trial drop";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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

			WellPosition position = null;
			final TrialService trialService = dataStorage.getTrialService();
			final BusinessCriteria criteria = new BusinessCriteria(trialService);
			if ((barcode != null) && (!barcode.equals(""))) {
				criteria.add(BusinessExpression.Equals(
						TrialDropView.PROP_BARCODE, barcode, true));
				crp.put(TrialDropView.PROP_BARCODE, barcode);
			}
			if ((well != null) && (!well.equals(""))) {
				criteria.add(BusinessExpression.Equals(TrialDropView.PROP_WELL,
						well, true));
				crp.put(TrialDropView.PROP_WELL, well);
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

			TrialDropView drop = trialDrops.iterator().next();
			List<ImageView> images = drop.getImages();
			ImageView image = images.iterator().next();

			request.setAttribute("drops", trialDrops);
			request.setAttribute("barcode", barcode);
			request.setAttribute("well", well);
			request.setAttribute("image", image);

			// Coordinates of previously-selected crystals in this drop
			Experiment trial = MountServlet.findExperiment(barcode, position,
					version);
			List<CrystalCoordinate> xys = CrystalHarvest.getXYs(trial);
			request.setAttribute("coords", xys);

			if (null != crystal) {
				int xtal = Integer.parseInt(crystal);
				if (xtal < 0 || xtal > xys.size()) {
					throw new BusinessException(
							"Crystal ID supplied doesn't exist");
				}
				request.setAttribute("crystal", xtal);

				// now get following experiments
				CrystalCoordinate cc = xys.get(xtal - 1);
				Experiment select = (Experiment) version.get(cc.getHook());
				OutputSample os = select.getOutputSamples().iterator().next();

				ArrayList experimentChain = new ArrayList();
				Set<InputSample> iss = os.getSample().getInputSamples();
				while (!iss.isEmpty()) {
					InputSample is = iss.iterator().next();
					Experiment nextExperiment = is.getExperiment();
					experimentChain.add(nextExperiment);
					String exptTypeName = nextExperiment.getExperimentType()
							.get_Name();
					if (exptTypeName != "Crystallogenesis") {
						break;
					}
					os = nextExperiment.getOutputSamples().iterator().next();
					iss = os.getSample().getInputSamples();
				}

				request.setAttribute("experimentChain", experimentChain);

				// if(milestone not achieved){
				request.setAttribute("finished", false);
				// }

				// Get suitable protocols - input and output both Crystal, but
				// not called Mount

				RequestDispatcher rd = request
						.getRequestDispatcher("/CrystalHandling/Treatment.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("/CrystalHandling/Harvesting.jsp");
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
		WritableVersion version = this.getWritableVersion(request, response);
		if (null == version) {
			return;
		}
		try {
			String hook = null;
			version.commit();
			PIMSServlet.redirectPost(response, "/ViewCrystal/" + hook);
		} catch (AbortedException e) {
			throw new ServletException(e);
		} catch (ConstraintException e) {
			throw new ServletException(e);
		} finally {
			if (!version.isCompleted()) {
				return;
			}
		}
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

}
