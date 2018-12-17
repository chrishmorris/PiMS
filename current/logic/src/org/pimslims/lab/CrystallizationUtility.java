package org.pimslims.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.sample.AbstractSample;
import org.pimslims.model.sample.Sample;

public class CrystallizationUtility {

    // criteria for searches we need to do
    // See PiMS_Crystallization.xml for a full description of the protocol

    private static final Map<String, Object> PROTEIN_CRITERIA = new HashMap<String, Object>(1);
    static {
        CrystallizationUtility.PROTEIN_CRITERIA.put(InputSample.PROP_NAME, "protein");
    }

    private static final Map<String, Object> SCORE = new HashMap<String, Object>(1);
    static {
        CrystallizationUtility.SCORE.put(Parameter.PROP_NAME, "score");
    }

    private static final Map<String, Object> METHOD = new HashMap<String, Object>(1);
    static {
        CrystallizationUtility.METHOD.put(Parameter.PROP_NAME, "method");
    }

    private static final Map<String, Object> SCHEMA = new HashMap<String, Object>(1);
    static {
        CrystallizationUtility.SCHEMA.put(Parameter.PROP_NAME, "schema");
    }

    /**
     * @param version the current transaction
     * @param trial
     * @param proteinSample
     * @param scoreLimitation null, or a filter for scores of interest
     * @return all relevant scores from this experimentGroup
     */
    public static Collection<ScoreBean> getScoresFromTrial(final ReadableVersion version,
        final String trialName, final String sampleName, ScoreLimitation scoreLimitation) {
        final Collection<ScoreBean> ret = new ArrayList<ScoreBean>();
        if (null == scoreLimitation) {
            // all results are of interest
            scoreLimitation = new ScoreLimitation() {
                public boolean isOfInterest(final int score) {
                    return true;
                }
            };
        }
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put(ExperimentGroup.PROP_NAME, trialName);
        final ExperimentGroup trial = version.findFirst(ExperimentGroup.class, criteria);
        if (null == trial) {
            throw new IllegalArgumentException("no such trial: " + trialName);
        }
        criteria = new HashMap<String, Object>();
        criteria.put(AbstractSample.PROP_NAME, sampleName);
        final Sample sample = version.findFirst(Sample.class, criteria);
        if (null == sample) {
            throw new IllegalArgumentException("no such sample: " + sampleName);
        }
        final Collection<Experiment> experiments = trial.getExperiments();
        for (final Iterator iter = experiments.iterator(); iter.hasNext();) {
            final Experiment experiment = (Experiment) iter.next();

            final InputSample is =
                experiment.findFirst(Experiment.PROP_INPUTSAMPLES, CrystallizationUtility.PROTEIN_CRITERIA);
            if (is.getSample() != sample) {
                continue;
            }

            // get score
            final Parameter score =
                experiment.findFirst(Experiment.PROP_PARAMETERS, CrystallizationUtility.SCORE);
            int value = -1;
            if (null != score.getValue()) {
                value = Integer.parseInt(score.getValue());
            }
            if (!scoreLimitation.isOfInterest(value)) {
                continue;
            }
            final Parameter m =
                experiment.findFirst(Experiment.PROP_PARAMETERS, CrystallizationUtility.METHOD);
            final String method = m.getValue();
            final Parameter s =
                experiment.findFirst(Experiment.PROP_PARAMETERS, CrystallizationUtility.SCHEMA);
            final String schema = s.getValue();

            // get plate
            final OutputSample os = experiment.getOutputSamples().iterator().next();
            assert null != os : "Experiment must be in a plate";
            final Sample out = os.getSample();
            assert null != out : "output sample must be defined";
            final Holder plate = out.getHolder();

            ret.add(new ScoreBean(value, method, schema, plate.getName()));
        }

        return ret;
    }

    /**
     * A filter to get results only matching a certain score *
     */
    public static interface ScoreLimitation {

        /**
         * @param score Wilson score of this well
         * @return true if this well should be represented in the results
         */
        boolean isOfInterest(int score);

    }

    /**
     * Represents a crystallization result
     * 
     * @author cm65
     * 
     */
    public static class ScoreBean {

        final int value;

        final String method;

        final String schema;

        final String plateName;

        /**
         * @param value
         * @param method
         * @param schema
         */
        public ScoreBean(final int value, final String method, final String schema, final String plateName) {
            this.value = value;
            this.method = method;
            this.schema = schema;
            this.plateName = plateName;
        }

        /**
         * @return Returns the method.
         */
        public String getMethod() {
            return this.method;
        }

        /**
         * @return Returns the schema.
         */
        public String getSchema() {
            return this.schema;
        }

        /**
         * @return Returns the value.
         */
        public int getValue() {
            return this.value;
        }

        public String getPlateName() {
            return this.plateName;
        }

    }

}
