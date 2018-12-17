package org.pimslims.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.location.Location;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.ModelObjectShortBean;

/**
 * Barcode ModelObject works together with BarcodeHolder.jsp
 * 
 * 
 * @author Marc Savitsky
 * @date November 2006
 * 
 */

public class Barcode extends PIMSServlet {

    /*
     * Set a Map 
     * of classes to search for barcode
     * and properties to map
     */
    private static final Map<Class, String> BARCLASSMAP = new HashMap();
    static {
        Barcode.BARCLASSMAP.put(Holder.class, "name");
        Barcode.BARCLASSMAP.put(Location.class, "name");
        Barcode.BARCLASSMAP.put(Sample.class, "name");
    }

    public Barcode() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) requires classname=org.pimslims.model.holder.Holder&barcode=123456 in
     *      request parameters
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // log(this.getClass().getName()+".doGet()");
        if (!this.checkStarted(request, response)) {
            return;
        }
        final PrintWriter writer = response.getWriter();

        final Map params = request.getParameterMap();
        String className = "";
        String barcode = "";
        if (params.size() > 0) {
            barcode = ((String[]) params.get("barcode"))[0];
            className = ((String[]) params.get("classname"))[0];
        }

        if (null == className || className.length() == 0) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_BAD_REQUEST);
            writer.print("classname is not provided"); // LATER invalid request
            // page
            return;
        }

        if (null == barcode || barcode.length() == 0) {
            this.writeErrorHead(request, response, "Barcode missing", HttpServletResponse.SC_BAD_REQUEST);
            writer.print("barcode is not provided");
            return;
        }

        final ReadableVersion rv = this.getReadableVersion(request, response);
        if (rv == null) {
            return;
        }
        try {
            final Collection<ModelObject> objects = Barcode.getRequiredObjects(rv, barcode);

            if (objects.isEmpty()) {
                // PIMS-555 
                // redirectPost(response, request.getContextPath() + "/Search/" + className);
                PIMSServlet.redirectPost(response, request.getContextPath() + "/Search/" + className
                    + "?barcodeSearch=true");

            } else if (objects.size() == 1) {
                final ModelObject object = objects.iterator().next();
                PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + object.get_Hook());

            } else {
                request.setAttribute("barcode", barcode);
                request.setAttribute("modelObjects", ModelObjectShortBean.getModelObjectShortBeans(objects));
                final ServletContext context = this.getServletContext();
                final RequestDispatcher dispatcher =
                    context.getRequestDispatcher("/JSP/listModelObjects.jsp");
                dispatcher.forward(request, response);
            }

        } finally {
            if (!rv.isCompleted()) {
                rv.abort();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Search by Barcode";
    }

    static Collection<ModelObject> getRequiredObjects(final ReadableVersion version, final String barcode)
        throws ServletException, IOException {

        final Collection<ModelObject> objects = new HashSet<ModelObject>();
        if (null == version || null == barcode) {
            return objects;
        }

        for (final Iterator iter = Barcode.BARCLASSMAP.entrySet().iterator(); iter.hasNext();) {
            final Map.Entry element = (Map.Entry) iter.next();
            final Class javaClass = (Class) element.getKey();
            final Map<String, Object> attr = new HashMap<String, Object>();
            attr.put((String) element.getValue(), barcode);
            objects.addAll(version.findAll(javaClass, attr));
        }

        return objects;
    }

}
