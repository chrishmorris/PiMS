package org.pimslims.crystallization.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.servlet.PIMSServlet;

public class IspybResults extends PIMSServlet {

	@Override
	public String getServletInfo() {
		return "Get diffraction results from DLS";
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		String shipment = request.getParameter("shipment");
		String proposal = request.getParameter("proposal");

		WritableVersion version = this.getWritableVersion(request, response);
		try {
			List<ModelObjectShortBean> beans = getShipment(userid, password,
					shipment, proposal, version);
			version.commit();
			request.setAttribute("beans", beans);
			request.setAttribute("metaClass",
					version.getMetaClass(ExperimentGroup.class));
			RequestDispatcher rd = request
					.getRequestDispatcher("/IspybResults.jsp");
			rd.forward(request, response);
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

	static List<ModelObjectShortBean> getShipment(String userid,
			String password, String shipment, String proposal,
			WritableVersion version) throws IOException {
		Object client = null;
		try {
			ClassLoader loader = getClassLoader(version.getClass()
					.getClassLoader());
			Class clientClass = loader.loadClass("org.pimslims.ispyb.Client");
			client = clientClass.newInstance();
			Method authenticate = clientClass.getMethod("authenticate",
					String.class, String.class);
			Boolean loggedIn = (Boolean) authenticate.invoke(client, userid,
					password);
			assert loggedIn : "Unknown userid/password";
			Method getResults = clientClass.getMethod("getResults",
					String.class, String.class);
			Object result = getResults.invoke(client, proposal, shipment);

			Class factoryClass = loader
					.loadClass("org.pimslims.ispyb.DiffractionExperimentFactory");
			Constructor constructor = factoryClass
					.getConstructor(WritableVersion.class);
			Object factory = constructor.newInstance(version);
			Method getExperimentGroups = factoryClass
					.getMethod(
							"getExperimentGroups",
							loader.loadClass("uk.ac.diamond.ispyb.client.IspybServiceStub$BeamlineExportedInformation"));
			Collection<ExperimentGroup> groups = (Collection<ExperimentGroup>) getExperimentGroups
					.invoke(factory, result);

			List<ModelObjectShortBean> beans = org.pimslims.presentation.ModelObjectShortBean
					.getBeans(groups);
			return beans;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}

	}

	static ClassLoader getClassLoader(ClassLoader parent)
			throws MalformedURLException {
		File library = org.pimslims.properties.PropertyGetter.getFileProperty(
				"lib-ws", "lib-ws");
		assert library.exists();
		File[] jars = library.listFiles();
		List<URL> urls = new ArrayList(jars.length);
		for (int i = 0; i < jars.length; i++) {
			if (jars[i].getName().endsWith(".jar")) {
				URL url = jars[i].toURI().toURL();
				urls.add(url);
				// System.out.println(url);
			}
		}
		ClassLoader loader = new PimsClassLoader(urls.toArray(new URL[] {}),
				parent);
		return loader;
	}
}
