package org.pimslims.crystallization.servlet.view;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.servlet.PIMSServlet;

public class ViewByName extends PIMSServlet {

    @Override
    public String getServletInfo() {
        return "view a model object by name";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final ReadableVersion rv = this.getReadableVersion(request, response);
        try {
            final String hook = getHook(request, rv);
            String link = "/View/" + hook;
            if (hook.contains(".Holder:")) {
                link = "/ViewTrialPlate/" + hook;
            }
            final RequestDispatcher rd = request.getRequestDispatcher(link);
            rd.forward(request, response);
        } finally {
            rv.abort();
        }

    }

    public static String getHook(final HttpServletRequest request, final ReadableVersion rv) {
        final String pathInfo = request.getPathInfo();
        final String nameHook = pathInfo.replace("/", "");
        final ModelObject mo = rv.get(nameHook);
        if (mo != null) {
            return mo.get_Hook();
        }
        final String hook = process(rv, nameHook);
        return hook;
    }

    public static String process(final ReadableVersion rv, final String nameHook) {
        final String[] names = nameHook.split(":");
        assert names.length == 2 : nameHook + "should be 'className:name'";
        try {
            final Class modelClass = Class.forName(names[0].trim());
            final ModelObject mo = rv.findFirst(modelClass, "name", names[1]);
            if (mo != null) {
                return mo.get_Hook();
            } else {
                return "";
            }
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
