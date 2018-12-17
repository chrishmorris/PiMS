package org.pimslims.crystallization.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.presentation.plateExperiment.PlateBean;
import org.pimslims.presentation.plateExperiment.PlateCriteria;
import org.pimslims.presentation.plateExperiment.PlateExperimentDAO;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.QuickSearch;

public class ShipmentList extends PIMSServlet {

	public static final String EXPERIMENT_TYPE = "Diffraction";

	@Override
	public String getServletInfo() {
		return "List recent crystal shipments";
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		System.out.println("ShipmentList.doGet()");
		final ReadableVersion version = this.getReadableVersion(request,
				response);
		if (null == version) {
			return;
		}

		PlateCriteria criteria = new PlateCriteria();
		criteria.setOnlyGroups(true); // TODO this will get messy with in-plate
										// screening
		criteria.setPlateName(request.getParameter(QuickSearch.SEARCH_ALL));
		criteria.setExpTypeName(EXPERIMENT_TYPE);
		criteria.setStart(0);
		criteria.setLimit(30);
		// TODO sort by date - appears to happen by default
		final List<PlateBean> shipments = PlateExperimentDAO.getPlateBeanList(
				version, criteria);
		request.setAttribute("shipments", shipments);

		version.abort();
		String destination = "/CrystalShipping/RecentShipments.jsp";
		RequestDispatcher rd = request.getRequestDispatcher(destination);
		rd.forward(request, response);
	}

}