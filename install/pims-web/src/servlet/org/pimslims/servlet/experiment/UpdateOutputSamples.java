/**
 * pims-web org.pimslims.servlet.experiment UpdateOutputSamples.java
 * 
 * @author Marc Savitsky
 * @date 7 Nov 2007
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2007 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.Measurement;
import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.sample.Sample;
import org.pimslims.servlet.PIMSServlet;

/**
 * UpdateOutputSamples
 * 
 */

public class UpdateOutputSamples extends PIMSServlet {

    public UpdateOutputSamples() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Update values in database and refresh";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // a user may end up here if their browser does not support the referer header
        // TODO send a page that does a javascript back
        throw new ServletException(this.getClass().getName() + " does not support GET");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        try {
            this.processRequest(version, parms);
            version.commit();
            // TODO add _parameters to GET e.g. to preserve tab
            PIMSServlet.redirectPostToReferer(request, response, "samples");
        } catch (final AbortedException e) {
            throw new ServletException(e);
        } catch (final ConstraintException e) {
            throw new ServletException(e);
        } catch (final AccessException e) {
            throw new ServletException(e);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            this.writeErrorHead(request, response, "Invalid values", HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

    /**
     * ${hook}:${fieldName}
     */
    //private static final Pattern KEY = Pattern.compile("^([a-zA-Z.]+:\\d+):(\\w+)$");
    private static final Pattern KEY = Pattern.compile("^([a-zA-Z.]+:\\d+):([\\w.]+)$");

    private void processRequest(final WritableVersion version, final Map<String, String[]> parms)
        throws ServletException, AccessException, ConstraintException {

        for (final Iterator iter = parms.keySet().iterator(); iter.hasNext();) {
            final String key = (String) iter.next();
            if (key.startsWith("_")) {
                continue; // allow special parms for future extensions
            }
            String value = null; // can use this method to set to null
            final String[] values = parms.get(key);
            if (1 == values.length && !"".equals(values[0])) {
                value = values[0];
            }
            if (1 < values.length) {
                throw new ServletException("Too many values for: " + key);
            }

            System.out.println("UpdateOutputSamples.processRequest [" + key + ":" + value + "]");

            final Matcher m = UpdateOutputSamples.KEY.matcher(key);
            if (!m.matches()) {
                throw new ServletException("Invalid parameter name: " + key);
            }
            final String hook = m.group(1);
            final Sample sample = version.get(hook);
            final String name = m.group(2);
            final ModelObject object = version.get(hook);
            if (null == object) {
                throw new ServletException("Object not found: " + hook);
            }

            if ("units".equals(name)) { // ignore units changed
                continue;

            } else if ("value".equals(name)) { // ignore value changed
                continue;

            } else if ("currentAmount".equals(name)) {
                final Measurement amount = Measurement.getMeasurement(value);
                sample.setCurrentAmount(amount.getValue());
                sample.setAmountUnit(amount.getStorageUnit());
                sample.setAmountDisplayUnit(amount.getDisplayUnit());

            } else if ("name".equals(name)) {
                sample.setName(value);

            } else if ("holder".equals(name)) {
                final Holder sh = version.get(value);
                sample.setHolder(sh);

            } else if ("colPosition".equals(name)) {
                if (null != value || "".equals(value)) {
                    sample.setColPosition(Integer.parseInt(value));
                }

            } else if ("rowPosition".equals(name)) {
                if (null != value || "".equals(value)) {
                    sample.setRowPosition(HolderFactory.getRow(value.toUpperCase()) + 1);
                }

            } else if ("copy.x".equals(name)) {
                continue;
            } else if ("copy.y".equals(name)) {
                continue;
            } else if ("copy".equals(name)) {
                System.out.println("UpdateOutputSamples.processRequest copy [" + hook + "]");
                final OutputSample os = sample.getOutputSample();
                this.createOutputSampleForExperiment(version, os.getExperiment(), os.getRefOutputSample(),
                    sample.getName());
                continue;

            } else {
                throw new ServletException("Unexpected attribute: " + name + "for input sample: " + hook);
            }
        }

    }

    private void createOutputSampleForExperiment(final WritableVersion wv, final Experiment experiment,
        final RefOutputSample refOutputSample, final String name) throws AccessException, ConstraintException {

        // find all the existing ones
        /*
        Collection<OutputSample> outputSamples = experiment.getOutputSamples();
        Collection<String> names = new HashSet(outputSamples.size());
        Collection<RefOutputSample> refOutputSamples = new HashSet<RefOutputSample>(outputSamples.size());

        for (OutputSample os : outputSamples) {
            names.add(os.getName());
            refOutputSamples.add(os.getRefOutputSample());
        }
        */
        if (null == refOutputSample) {
            return;
        }

        final StringBuffer sb = new StringBuffer(name);
        sb.append(" " + System.currentTimeMillis());

        final Float amount = refOutputSample.getAmount();
        final String unit = refOutputSample.getUnit();
        final String displayUnit = refOutputSample.getDisplayUnit();

        final Map<String, Object> attrMap = new HashMap<String, Object>();
        attrMap.put(OutputSample.PROP_EXPERIMENT, experiment);
        attrMap.put(OutputSample.PROP_REFOUTPUTSAMPLE, refOutputSample);
        attrMap.put(OutputSample.PROP_NAME, (name.toString()));
        attrMap.put(OutputSample.PROP_AMOUNT, amount);
        attrMap.put(OutputSample.PROP_AMOUNTUNIT, unit);
        attrMap.put(LabBookEntry.PROP_ACCESS, experiment.getAccess());
        final OutputSample os = wv.create(OutputSample.class, attrMap);

        // make a sample if possible
        final SampleCategory ref = refOutputSample.getSampleCategory();
        /*
         * if (null==ref) { //LATER a data model change will make this test unnecessary continue; // can't
         * make a sample without sample categories }
         */
        final Sample sample = new Sample(wv, sb.toString());
        sample.setOutputSample(os);
        sample.setIsActive(Boolean.TRUE);

        final Set<SampleCategory> categories = Collections.singleton(ref);
        sample.setSampleCategories(categories);

        // copy values from the RefSample and OutputSample
        sample.setAmountDisplayUnit(displayUnit);
        sample.setAmountUnit(unit);
        sample.setInitialAmount(amount);
        sample.setCurrentAmount(amount);
        sample.setIsActive(true);
        // LATER sample.setSampleComponents();

    }// EndOf createOutputSampleForExperiment

}
