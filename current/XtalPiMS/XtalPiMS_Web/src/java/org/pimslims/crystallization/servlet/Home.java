package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.util.InstallationProperties;

public class Home extends PIMSServlet {

	@Override
	public String getServletInfo() {
		return "The xtalPiMS homepage";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ReadableVersion version = this.getReadableVersion(request, response);
		if (null == version) {
			return;
		}

		String homepageTopLeft = "inspections";
		String homepageTopRight = "annotations";
		String homepageBottomLeft = "microscopeImages";
		String homepageBottomRight = "groups";
		try {
			InstallationProperties props = ModelImpl
					.getInstallationProperties();
			String tl = props.getProperty("homepage.topLeft");
			String tr = props.getProperty("homepage.topRight");
			String bl = props.getProperty("homepage.bottomLeft");
			String br = props.getProperty("homepage.bottomRight");
			if (null != tl) {
				homepageTopLeft = tl;
			}
			if (null != tr) {
				homepageTopRight = tr;
			}
			if (null != bl) {
				homepageBottomLeft = bl;
			}
			if (null != br) {
				homepageBottomRight = br;
			}
		} catch (final RuntimeException e) {
			// no override in context file, or problem reading - accept default
		}

		request.setAttribute("homepageTopLeft", homepageTopLeft);
		request.setAttribute("homepageTopRight", homepageTopRight);
		request.setAttribute("homepageBottomLeft", homepageBottomLeft);
		request.setAttribute("homepageBottomRight", homepageBottomRight);

		try {
			version.commit();
			RequestDispatcher rd = request.getRequestDispatcher("/Home.jsp");
			rd.forward(request, response);
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
