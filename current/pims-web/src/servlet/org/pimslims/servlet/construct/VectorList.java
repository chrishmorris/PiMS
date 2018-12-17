/**
 * 
 */
package org.pimslims.servlet.construct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.servlet.ListRole;
import org.pimslims.servlet.PIMSServlet;

/**
 * Display associates e.g. read/ListRole/org.pimslims.model.target.Target:2345/molecule
 * 
 * This is designed to be used as the src= in a delayed get box
 * 
 * @author cm65
 * 
 */
public class VectorList extends PIMSServlet { //

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "Show all vectors";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final org.pimslims.dao.ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }

        try {
            //final org.pimslims.metamodel.MetaClass metaClass = version.getMetaClass(RefSample.class);

            final SampleCategory vector =
                version.findFirst(SampleCategory.class, SampleCategory.PROP_NAME, "Vector");
            final java.util.Collection associates =
                version.findAll(RefSample.class, AbstractSample.PROP_SAMPLECATEGORIES, vector);

            final List<ModelObjectBean> beans = ModelObjectBean.getModelObjectBeans(associates);
            request.setAttribute("beans", beans);

            final List<String> attributes = ListRole.getAttributeNames(beans);
            request.setAttribute("attributes", attributes);

            // Pass some extra values that might be useful for some custom lists
            request.setAttribute("size", beans.size());
            version.commit();

            request.setAttribute("metaClass", this.getModel().getMetaClass(RefSample.class.getName()));
            response.setStatus(HttpServletResponse.SC_OK);
            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/list/ListBeans.jsp/Vector");
            dispatcher.forward(request, response);

        } catch (final AbortedException e1) {
            throw new ServletException("Sorry, there has been a problem, please try again.", e1);
        } catch (final ConstraintException e1) {
            // should not happen in read
            throw new ServletException(e1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        throw new ServletException("Not implemented yet");
    }

}
