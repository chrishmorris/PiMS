package org.pimslims.crystallization.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.servlet.PIMSServlet;

public class CrystalServlet extends PIMSServlet {

	@Override
	public String getServletInfo() {
		return "View crystal treatments, and add new ones";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ReadableVersion version = this.getReadableVersion(request, response);
		if (null == version) {
			return;
		}
		try {
			version.commit();
			RequestDispatcher rd = request
					.getRequestDispatcher("/CrystalHandling/Harvesting.jsp");
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
}
