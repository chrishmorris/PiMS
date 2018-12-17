package org.pimslims.crystallization.servlet.view;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.Holder;
import org.pimslims.servlet.PIMSServlet;

public class ViewTrialPlate extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "view a model object by name";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ReadableVersion rv = this.getReadableVersion(request, response);
        try {
            final String pathInfo = request.getPathInfo();
            final String hook = pathInfo.replace("/", "");
            final Holder holder = rv.get(hook);

            final RequestDispatcher rd = request.getRequestDispatcher(hook);
            rd.forward(request, response);
        } finally {
            rv.abort();
        }

    }

    public static String process(final ReadableVersion rv, final String nameHook) {
        final String[] names = nameHook.split(":");
        assert names.length == 2 : nameHook + "should be 'className:name'";
        try {
            final Class modelClass = Class.forName(names[0].trim());
            final ModelObject mo = rv.findFirst(modelClass, "name", names[1]);
            return mo.get_Hook();
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
