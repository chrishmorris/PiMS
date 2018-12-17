/**
 * pims-web org.pimslims.servlet.sample DivideSample.java
 * 
 * @author Marc Savitsky
 * @date 29 Jan 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Marc Savitsky * *
 * 
 */
package org.pimslims.servlet.sample;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.AccessException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.PrimerBeanWriter;
import org.pimslims.presentation.experiment.DefaultExperimentName;
import org.pimslims.presentation.experiment.ExperimentNameFactory;
import org.pimslims.properties.PropertyGetter;
import org.pimslims.servlet.PIMSServlet;

/**
 * Divide Sample into Aliquots
 * 
 */
public class DivideSample extends PIMSServlet {

    private static final String DIVIDE_PROTOCOL = "PiMS Produce Sample";

    /**
     * @see org.pimslims.servlet.PIMSServlet#getServletInfo()
     */
    @Override
    public String getServletInfo() {
        return "split the sample";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        // ensure that PIMS was able to connect to the database
        if (!this.checkStarted(request, response)) {
            return;
        }

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash

        // get a read transaction
        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Sample sample = version.get(pathInfo);
            if (null == sample) {
                this.writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("sample", new ModelObjectBean(sample));
            version.commit();
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/sample/Divide.jsp");
            rd.forward(request, response);
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

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (null == pathInfo || 0 == pathInfo.length()) {
            this.writeErrorHead(request, response, "Sample must be specified",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        pathInfo = pathInfo.substring(1); // strip initial slash
        final int aliquots = Integer.parseInt(request.getParameter("aliquots"));
        if (0 == aliquots) {
            this.writeErrorHead(request, response, "Attempt to divide by zero",
                HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final WritableVersion version = this.getWritableVersion(request, response);
        try {
            final Sample sample = version.get(pathInfo);
            if (null == sample) {
                this.writeErrorHead(request, response, "Sample not found: " + pathInfo,
                    HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            final String username = PIMSServlet.getUsername(request);
            DivideSample.divide(version, sample, aliquots, username);
            version.commit();
            PIMSServlet.redirectPost(response, request.getContextPath() + "/View/" + sample.get_Hook());
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
    }

    public static Collection<Sample> divide(final WritableVersion version, final Sample sample,
        final int aliquots, final String username) throws AccessException, ConstraintException {

        // Change the current sample to aliquot 1
        // There can only be one outputSample
        final Collection<Sample> samples = new HashSet<Sample>();
        sample.setName(DivideSample.makeName(version, sample.getName()));

        BigDecimal amount = null;
        if (null != sample.getCurrentAmount()) {
            final BigDecimal b = new BigDecimal(sample.getCurrentAmount().toString());
            amount = b.divide(new BigDecimal(aliquots), b.scale() + 1, BigDecimal.ROUND_HALF_DOWN);
            sample.setCurrentAmount(amount.floatValue());
        }
        samples.add(sample);

        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Sample.PROP_AMOUNTDISPLAYUNIT, sample.getAmountDisplayUnit());
        attributes.put(Sample.PROP_AMOUNTUNIT, sample.getAmountUnit());
        attributes.put(Sample.PROP_ASSIGNTO, sample.getAssignTo());
        attributes.put(Sample.PROP_BATCHNUM, sample.getBatchNum());
        //attributes.put(Sample.PROP_COLPOSITION, sample.getColPosition());
        if (null != amount) {
            attributes.put(Sample.PROP_CURRENTAMOUNT, amount.floatValue());
        }
        attributes.put(Sample.PROP_CURRENTAMOUNTFLAG, sample.getCurrentAmountFlag());
        //attributes.put(Sample.PROP_DROPANNOTATIONS, sample.get_Value(Sample.PROP_DROPANNOTATIONS));
        //attributes.put(Sample.PROP_HOLDER, sample.getHolder());
        attributes.put(Sample.PROP_IMAGES, sample.getImages());
        attributes.put(Sample.PROP_INITIALAMOUNT, sample.getInitialAmount());
        attributes.put(Sample.PROP_INPUTSAMPLES, sample.getInputSamples());
        //attributes.put(Sample.PROP_OUTPUTSAMPLE, sample.getOutputSample());
        attributes.put(Sample.PROP_REFSAMPLE, sample.getRefSample());
        //attributes.put(Sample.PROP_ROWPOSITION, sample.getRowPosition());
        //attributes.put(Sample.PROP_SUBPOSITION, sample.getSubPosition());

        attributes.put(LabBookEntry.PROP_ACCESS, sample.getAccess());
        attributes.put(LabBookEntry.PROP_DETAILS, sample.getDetails());
        attributes.put(AbstractSample.PROP_HAZARDPHRASES, sample.getHazardPhrases());
        attributes.put(AbstractSample.PROP_IONICSTRENGTH, sample.getIonicStrength());
        attributes.put(AbstractSample.PROP_ISACTIVE, sample.getIsActive());
        attributes.put(AbstractSample.PROP_ISHAZARD, sample.getIsHazard());
        attributes.put(AbstractSample.PROP_PH, sample.getPh());
        //attributes.put(AbstractSample.PROP_SAMPLECATEGORIES, sample.getSampleCategories());
        //attributes.put(AbstractSample.PROP_SAMPLECOMPONENTS, sample.getSampleComponents());

        OutputSample outputSample = sample.getOutputSample();

        /**
         * PiMS 2372 Create an experiment for the sample if it hasn't one already
         */
        if (null == outputSample) {
            outputSample =
                DivideSample.makeExperimentForSample(version, sample, DivideSample.DIVIDE_PROTOCOL,
                    "Divide Sample ");
        }

        for (int i = 1; i < aliquots; i++) {

            final Sample newSample =
                SampleFactory.createSample(version, DivideSample.makeName(version, sample.getName()),
                    sample.getSampleCategories(), attributes);

            for (final SampleComponent component : sample.getSampleComponents()) {
                final Map<String, Object> scAttributes = new HashMap<String, Object>();
                scAttributes.put(SampleComponent.PROP_CONCENTRATION, component.getConcentration());
                scAttributes.put(SampleComponent.PROP_CONCENTRATIONUNIT, component.getConcentrationUnit());
                scAttributes.put(SampleComponent.PROP_CONCDISPLAYUNIT, component.getConcDisplayUnit());
                scAttributes.put(LabBookEntry.PROP_DETAILS, component.getDetails());
                scAttributes.put(SampleComponent.PROP_REFCOMPONENT, component.getRefComponent());
                scAttributes.put(SampleComponent.PROP_ABSTRACTSAMPLE, newSample);
                new SampleComponent(version, scAttributes);
            }
            DivideSample.createOutputSampleForExperiment(version, outputSample, newSample);

            org.pimslims.presentation.mru.MRUController.addObject(username, newSample);
            samples.add(newSample);
        }
        return samples;
    }

    /**
     * 
     * DivideSample.makeExperimentForSample
     * 
     * @param wv
     * @param sample
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static OutputSample makeExperimentForSample(final WritableVersion wv, final Sample sample,
        final String protocolName, final String experimentName) throws AccessException, ConstraintException {

        final Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", protocolName);
        final Protocol protocol = wv.findFirst(Protocol.class, m);
        assert null != protocol : "Could not find protocol";

        final Experiment divideExperiment =
            PrimerBeanWriter.createExperiment(wv, sample.getAccess(),
                experimentName + System.currentTimeMillis(), protocol.getExperimentType().get_Name());

        divideExperiment.setProtocol(protocol);
        final ExperimentNameFactory enf =
            PropertyGetter.getInstance("Experiment.Name.Factory", DefaultExperimentName.class);
        divideExperiment.setName(enf.suggestExperimentName(wv, divideExperiment, null));

        // add parameters and outputSamples
        //ConstructBeanWriter.createPrimerDesignParameters(wv, primerDesignExperiment, protocol, cb);
        final Collection<OutputSample> outputSamples =
            DivideSample.createOutputSamples(wv, divideExperiment, Collections.singleton(sample));

        return outputSamples.iterator().next();
    }

    /**
     * 
     * DivideSample.createOutputSamples
     * 
     * @param version
     * @param experiment
     * @param samples
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    private static Collection<OutputSample> createOutputSamples(final WritableVersion wv,
        final Experiment experiment, final Collection<Sample> samples) throws AccessException,
        ConstraintException {

        final Collection<OutputSample> outputSamples = new ArrayList<OutputSample>();
        for (final Sample sample : samples) {

            final Map<String, Object> attrMap = new HashMap<String, Object>();
            attrMap.put(OutputSample.PROP_EXPERIMENT, experiment);
            attrMap.put(OutputSample.PROP_NAME, (sample.getName()));
            attrMap.put(OutputSample.PROP_AMOUNT, sample.getCurrentAmount());
            attrMap.put(OutputSample.PROP_AMOUNTUNIT, sample.getAmountUnit());
            attrMap.put(OutputSample.PROP_SAMPLE, sample);

            final OutputSample os = wv.create(OutputSample.class, attrMap);
            outputSamples.add(os);
        }

        return outputSamples;
    }

    /**
     * 
     * DivideSample.createOutputSampleForExperiment
     * 
     * @param wv
     * @param outputSample
     * @param sample
     * @return
     * @throws AccessException
     * @throws ConstraintException
     */
    public static OutputSample createOutputSampleForExperiment(final WritableVersion wv,
        final OutputSample outputSample, final Sample sample) throws AccessException, ConstraintException {

        if (null == outputSample) {
            return null;
        }

        final Map<String, Object> attrMap = new HashMap<String, Object>();
        //attrMap.putAll(outputSample.get_Values());
        attrMap.put(OutputSample.PROP_EXPERIMENT, outputSample.getExperiment());
        //attrMap.put(OutputSample.PROP_REFOUTPUTSAMPLE, outputSample.getRefOutputSample());
        attrMap.put(OutputSample.PROP_NAME, (outputSample.getName()));
        attrMap.put(OutputSample.PROP_AMOUNT, sample.getCurrentAmount());
        attrMap.put(OutputSample.PROP_AMOUNTUNIT, sample.getAmountUnit());
        attrMap.put(OutputSample.PROP_ROLE, outputSample.getRole());
        attrMap.put(OutputSample.PROP_SAMPLE, sample);
        attrMap.put(LabBookEntry.PROP_ACCESS, outputSample.getAccess());
        final OutputSample os = wv.create(OutputSample.class, attrMap);

        os.setRefOutputSample(outputSample.getRefOutputSample());
        return os;

    }// EndOf createOutputSampleForExperiment

    /**
     * Make sure protocol name is unique
     * 
     * @param protocols
     * @param pname
     * @return
     */

    public static String makeName(final ReadableVersion version, final String pname) {
        return version.getUniqueName(Sample.class, pname);
    }

}
