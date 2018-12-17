package org.pimslims.servlet.sample;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.sample.RefSample;
import org.pimslims.presentation.sample.SampleBeanWriter;
import org.pimslims.servlet.PIMSServlet;

public class NewSampleFromRefSample extends PIMSServlet {

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

    /**
     * Post causes a Target to be created
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        System.out.println("org.pimslims.servlet.sample.NewSampleFromRefSample.doPost()");
        // Get a WritableVersion
        final WritableVersion version = this.getWritableVersion(request, response);

        final Map m = request.getParameterMap();
        for (final Iterator it = m.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final String[] values = (String[]) e.getValue();
            final StringBuffer s = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                s.append(values[i] + ",");
            }
            s.deleteCharAt(s.length() - 1);
            System.out.println("org.pimslims.servlet.sample.NewSampleFromRefSample request parameter ["
                + e.getKey() + ":" + s.toString() + "]");
        }

        final String refSampleHook = request.getParameter("org.pimslims.model.sample.Sample:refSample");
        final String name = request.getParameter("org.pimslims.model.sample.Sample:name");
        final String projectHook = request.getParameter("org.pimslims.model.sample.Sample:access");

        System.out.println("org.pimslims.servlet.sample.NewSampleFromRefSample version.get [" + refSampleHook
            + "]");
        RefSample refSample;
        LabNotebook accessObject;
        ModelObject modelObject;

        try {
            refSample = version.get(refSampleHook);
            accessObject = version.get(projectHook);
            modelObject = SampleBeanWriter.createSample(version, name, refSample, accessObject);
            version.commit();

        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
        /*
         * System.out.println("SampleComponents in refSample ["+refSample.getSampleComponents().size()+"]");
         * Collection<SampleComponent> sampleComponents = refSample.getSampleComponents(); for
         * (SampleComponent component:sampleComponents) { System.out.println("SampleComponent
         * ["+component.get_Name()+"]"); } Sample s = (Sample)modelObject;
         * System.out.println("SampleComponents in Sample ["+s.getSampleComponents().size()+"]"); Collection<SampleComponent>
         * components = s.getSampleComponents(); for (SampleComponent component:components) {
         * System.out.println("SampleComponent ["+component.get_Name()+"]"); }
         */
        // now show the new sample
        final String hook = modelObject.get_Hook();
        System.out.println("redirectPost [" + request.getContextPath() + "/View/" + hook + "]");
        PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + hook);

    }
}
