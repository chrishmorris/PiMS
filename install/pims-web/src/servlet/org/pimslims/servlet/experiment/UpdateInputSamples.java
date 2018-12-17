package org.pimslims.servlet.experiment;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import org.pimslims.lab.create.ExperimentFactory;
import org.pimslims.lab.experiment.ExperimentUtility;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.servlet.PIMSServlet;
import org.pimslims.servlet.Update;

/**
 * Update values in database and refresh TODO move special logic about project
 * 
 * @see JSP/experiment/inputsamples.jsp
 * 
 * @author cm65
 * 
 */
public class UpdateInputSamples extends PIMSServlet {

    public UpdateInputSamples() {
        super();
    }

    @Override
    public String getServletInfo() {
        return "Update values in database and refresh";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // a user may end up here if their browser does not support the referer
        // header
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
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        PIMSServlet.validatePost(request);
        final WritableVersion version = this.getWritableVersion(request, response);
        if (null == version) {
            return;
        }
        final Map<String, String[]> parms = request.getParameterMap();
        try {
            UpdateInputSamples.processRequest(version, parms);
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
        } catch (final ParseException e) {
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
    private static final Pattern KEY = Pattern.compile("^([a-zA-Z.]+:\\d+):(\\w+)$");

    /**
     * TODO move this logic to data model, so you can use standard update UpdateInputSamples.processRequest
     * 
     * @param version
     * @param parms
     * @return
     * @throws ServletException
     * @throws AccessException
     * @throws ConstraintException
     * @throws ParseException
     */
    public static Map<ModelObject, Map<String, Object>> processRequest(final WritableVersion version,
        final Map<String, String[]> parms) throws ServletException, AccessException, ConstraintException,
        ParseException {
        final Map<ModelObject, Map<String, Object>> ret = new HashMap<ModelObject, Map<String, Object>>();

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

            //System.out.println("UpdateInputSamples.processRequest [" + key + ":" + value + "]");

            final Matcher m = UpdateInputSamples.KEY.matcher(key);
            if (!m.matches()) {
                throw new ServletException("Invalid parameter name: " + key);
            }
            final String hook = m.group(1);
            final String name = m.group(2);
            final ModelObject object = version.get(hook);
            if (null == object) {
                throw new ServletException("Object not found: " + hook);
            }

            if (object instanceof Experiment && "researchObjective".equals(name)) {
                final Experiment experiment = (Experiment) object;
                final Project thing = experiment.getProject();
                final ResearchObjective objective = (ResearchObjective) version.get(value);

                if (!UpdateInputSamples.equalsResearchObjective(thing, objective)) {
                    ExperimentUtility.clearExpBlueprintSamples(experiment);
                    Update.updateValue(version, value, name, object);
                    Update.updateChangedMap(ret, value, name, object);
                    ExperimentUtility.setExpBlueprintSamples(experiment, objective, true);
                }
                continue;
            }

            if (!(object instanceof InputSample)) {
                Update.updateValue(version, value, name, object);
                Update.updateChangedMap(ret, value, name, object);
                continue;
            }

            final InputSample inputSample = (InputSample) object;
            if ("units".equals(name)) {
                continue;
            }

            if ("value".equals(name)) {
                continue;
            }

            if ("amount".equals(name)) {
                final Measurement amount = Measurement.getMeasurement(value);
                inputSample.setAmount(amount.getValue());
                inputSample.setAmountUnit(amount.getStorageUnit());
                inputSample.setAmountDisplayUnit(amount.getDisplayUnit());

            } else if ("sample".equals(name)) {

                if (null == value) {
                    inputSample.setSample(null);
                } else {
                    final Sample sample = (Sample) version.get(value);
                    inputSample.setSample(sample);
                    final Experiment experiment = inputSample.getExperiment();

                    ExperimentFactory.propagateExperimentProperties(sample, experiment);
                    /* was if (null != sample && null == experiment.getProject()) {
                         experiment.setProject(ExperimentFactory.getTargetOrConstruct(sample));
                     } */

                    if (ExperimentFactory.isComplexExperiment(experiment)) {
                        final ResearchObjective complex = ExperimentFactory.getComplex(experiment);
                        if (null != complex) {
                            experiment.setProject(complex);
                        }
                    }
                }

            } else {
                throw new ServletException("Unexpected attribute: " + name + "for input sample: " + hook);
            }
        }
        return ret;
    }

    private static boolean equalsResearchObjective(final Project thing, final ResearchObjective r2) {
        if (null == thing && null == r2) {
            return true;
        }
        if (null == thing) {
            return false;
        }
        return thing.equals(r2);

    }
}
