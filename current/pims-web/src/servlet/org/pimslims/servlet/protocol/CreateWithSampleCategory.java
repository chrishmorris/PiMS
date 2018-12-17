package org.pimslims.servlet.protocol;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.presentation.ModelObjectShortBean;
import org.pimslims.presentation.protocol.ProtocolBean;
import org.pimslims.servlet.Create;

/**
 * Show a creation form for a type that needs a sample category, e.g. RefInputSample, RefOutputSample
 * 
 * @author cm65
 * @baseUrl /Create/org.pimslims.model.protocol.RefInputSample
 */
public abstract class CreateWithSampleCategory extends Create {

    static final int MAX_CATEGORIES = 200;

    public CreateWithSampleCategory() {
        super();
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }

        final MetaClass metaClass = this.getMetaClass(request);
        if (metaClass == null) {
            this.writeErrorHead(request, response, "type not found: " + request.getRequestURI(),
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (null == version) {
            return;
        }
        try {
            final String protocolHook = request.getParameter("protocol");
            if (null != protocolHook) {
                final Protocol protocol = version.get(protocolHook);
                request.setAttribute("protocol", new ProtocolBean(protocol));
            }

            // make a list of sample categories
            final Collection<SampleCategory> categories =
                version.getAll(SampleCategory.class, 0, CreateWithSampleCategory.MAX_CATEGORIES);
            if (CreateWithSampleCategory.MAX_CATEGORIES <= categories.size()) {
                throw new ServletException("Sorry, you have too many sample categories");
            }
            request.setAttribute("sampleCategories",
                ModelObjectShortBean.getModelObjectShortBeans(categories));
            assert null != request.getAttribute("sampleCategories");

            String url = "/JSP/create/" + metaClass.getMetaClassName() + ".jsp";
            if (metaClass.getMetaClassName().contains("ParameterDefinition")) {
                url = "/JSP/protocol/CreateEditParameterDefinition.jsp";
            } else if (metaClass.getMetaClassName().contains("RefOutputSample")) {
                url = "/JSP/protocol/CreateEditRefOutputSample.jsp";
            } else if (metaClass.getMetaClassName().contains("RefInputSample")) {
                url = "/JSP/protocol/CreateEditRefInputSample.jsp";
            }
            final RequestDispatcher dispatcher = request.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } finally {
            version.abort();
        }

    }

    MetaClass getMetaClass(final HttpServletRequest request) throws ServletException {
        return this.getModel().getMetaClass(this.getMetaClassName(request));
    }

    @Override
    protected abstract String getMetaClassName(HttpServletRequest request);

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Create a record that needs a sample category";
    }

}
