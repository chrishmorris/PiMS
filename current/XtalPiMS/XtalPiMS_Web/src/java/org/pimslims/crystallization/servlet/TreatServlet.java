package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.pimslims.servlet.PIMSServlet;

public class TreatServlet extends
		org.pimslims.crystallization.servlet.XtalPIMSServlet {

	@Override
	public String getServletInfo() {
		return "Used to show and record crystal treatments, after selection from from a trial drop";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		DataStorageImpl dataStorage = null;
		try {
			dataStorage = (DataStorageImpl) openResources(request);
			ReadableVersion version = dataStorage.getVersion();

			// ensure that PIMS was able to connect to the database
			if (!this.checkStarted(request, response)) {
				return;
			}

			final java.io.PrintWriter writer = response.getWriter();
			String pathInfo = request.getPathInfo();
			if (null == pathInfo || 0 == pathInfo.length()) {
				this.writeErrorHead(request, response,
						"Experiment must be specified",
						HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			pathInfo = pathInfo.substring(1); // strip initial slash

			Experiment selection = version.get(pathInfo);
			if (null == selection) {
				this.writeErrorHead(request, response, "Experiment not found: "
						+ pathInfo, HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			List<CrystalCoordinate> xys = new ArrayList();
			xys.add(CrystalHarvest.getSingleXY(selection));
			request.setAttribute("coords", xys);

			Collection<InputSample> inputs = selection.getInputSamples();
			if (inputs.size() == 0) {
				throw new BusinessException("No input samples for experiment: "
						+ selection.get_Hook() + " - expected one trial drop");
			} else if (inputs.size() > 1) {
				throw new BusinessException(
						"Too many input samples for experiment: "
								+ selection.get_Hook()
								+ " - expected one trial drop only");
			}
			InputSample drop = inputs.iterator().next();

			// get the first one - there should be only one, type

			// if no protocol specified

			// Full"view" page of xtal plus treatments
			// get the xtal and stuff as for Mount - hang on, am I better just
			// doing all this there?
			// extract hook from URI - see generic View, Search, etc.
			// get experiment specified by hook
			// get follow-on expts of type Mount Crystal
			// need function to represent expt/protocol in box
			// get follow-on protocols and set list to request
			// forward to Treatment.jsp

			// else

			// want form for one experiment - return it as HTML? JSON? HTML
			// sounds easier.
			request.setAttribute("experiment", selection);
			request.setAttribute("input", drop);

			// if (false) {
			// throw new BusinessException();
			// }

			RequestDispatcher rd = request
					.getRequestDispatcher("/CrystalHandling/Treatment.jsp");
			rd.forward(request, response);
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
			// TODO Auto-generated method stub
			version.commit();
			PIMSServlet.redirectPost(response, "/update/Treat/" + hook);
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

}
