package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.ExperimentTypeUtil;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.RefSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.presentation.BeanFactory;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ServletUtil;
import org.pimslims.presentation.sample.SampleBean;
import org.pimslims.presentation.sample.SampleBeanWriter;
import org.pimslims.servlet.PIMSServlet;

/**
 * Servlet to handle the creation of a new SPOT Target
 * 
 * @author Marc Savitsky
 */
public class CreateSample extends PIMSServlet {

    /**
     * Code to satisy Serializable Interface
     */
    private static final long serialVersionUID = -5851882064372940058L;

    /**
     * @return Servlet descriptor string
     * @see javax.servlet.Servlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "PiMS new Sample page";
    }

    private static final Pattern JUST_ID = Pattern.compile("^\\d+$");

    /**
     * Show the New Sample form
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        if (!this.checkStarted(request, response)) {
            return;
        }
        String recipeHook = request.getParameter(Sample.PROP_REFSAMPLE);
        if (null != recipeHook && CreateSample.JUST_ID.matcher(recipeHook).matches()) {
            recipeHook = RefSample.class.getName() + ":" + recipeHook;
        }
        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return; // error message has already been sent
        }
        try {

            if (recipeHook != null) {

                RefSample refSample = version.get(recipeHook);
                if (null == refSample) {
                    refSample = version.findFirst(RefSample.class, AbstractSample.PROP_NAME, recipeHook);
                }
                request.setAttribute("refSampleBean", BeanFactory.newBean(refSample));
            }
            request.setAttribute("refSampleHook", recipeHook);

            final Collection<User> Users =
                this.getAllSorted(version, org.pimslims.model.accessControl.User.class);
            request.setAttribute("users", ModelObjectBean.getModelObjectBeans(Users));

            final User user = PIMSServlet.getCurrentUser(version, request);
            if (null != user) {
                request.setAttribute("currentUser", BeanFactory.newBean(user));
            }

            request.setAttribute("accessObjects", PIMSServlet.getPossibleCreateOwners(version));

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/create/org.pimslims.model.sample.Sample.jsp");
            dispatcher.forward(request, response);

            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * Post causes a Sample to be created
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        final SampleBean sb = new SampleBean();

        /*final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("request parameter [" + e.getKey() + ":" + s.toString() + "]");
        } */

        sb.setName(request.getParameter("sampleName"));

        if (ServletUtil.validString(request.getParameter("initialamount:amount"))) {
            final Measurement mm =
                Measurement.getMeasurement(request.getParameter("initialamount:value")
                    + request.getParameter("initialamount:units"));

            if (null != mm) {
                sb.setInitialAmount(new Float(mm.getValue()));
                sb.setCurrentAmount(new Float(mm.getValue()));
                sb.setAmountUnit(mm.getStorageUnit());
                sb.setAmountDisplayUnit(mm.getDisplayUnit());
            }
        }

        sb.setIsActive(true);
        sb.setDetails(request.getParameter("details"));

        if (ServletUtil.validString(request.getParameter("batch"))) {
            sb.setBatchNum(new Integer(request.getParameter("batch")));
        }

        sb.setAssignTo(request.getParameter("assignTo"));

        ModelObject modelObject;

        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final RefSample refSample = (RefSample) version.get(request.getParameter("refsampleId"));
            LabNotebook access = null;
            if (null != request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID)) {
                access = (LabNotebook) version.get(request.getParameter(PIMSServlet.LAB_NOTEBOOK_ID));
            }
            sb.setAccess(BeanFactory.newBean(access));
            modelObject =
                SampleBeanWriter.createNewSample(version, sb, refSample.getSampleCategories(), refSample);
            version.commit();

        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);

        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }

        // now show the new sample
        final String hook = modelObject.get_Hook();
        PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);

    }

    private Collection getAllSorted(final ReadableVersion version, final Class javaClass)
        throws ServletException {

        final List list = new ArrayList();
        list.addAll(PIMSServlet.getAll(version, javaClass));
        Collections.sort(list, ExperimentTypeUtil.ALPHABETICAL_ORDER_OF_NAME);
        return list;
    }

}
