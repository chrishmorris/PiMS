/**
 * pims-tools org.pimslims.command MissingInputFixer.java
 * 
 * @author cm65
 * @date 9 Feb 2011
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.command;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.protocol.Protocol;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;

/**
 * MissingInputFixer
 * 
 */
public class ExperimentFixer {

    /**
     * BAD_OUTPUT String
     */
    private static final String BAD_OUTPUT =
        "from OutputSample as os where os.refOutputSample is null and os.experiment.protocol is not null "
            + "and os.experiment.experimentType.name != 'SPOTConstructDesign' "
            + "and os.experiment.experimentType.name != 'Entry clone' "
            + "and os.experiment.experimentType.name != 'Deep-Frozen culture' ";

    /**
     * TRANSACTION_LIMIT int How many experiments to fix at one go. Keep it low enough to avoid OutOfMemory
     * errors
     */
    private static final int TRANSACTION_LIMIT = 96;

    /**
     * MissingInputFixer.findBadExperiments
     * 
     * @param version
     * @param limit
     */
    public static Collection<Experiment> findBadExperiments(ReadableVersion version, int limit) {
        String hql = "select os.experiment " + BAD_OUTPUT;
        org.hibernate.Query query = version.getSession().createQuery(hql);
        query.setMaxResults(limit);
        List ret = query.list();
        System.out.println("Found experiments with missing refoutputsamples: " + ret.size());

        if (ret.size() < limit) {
            hql =
                "select experiment from Experiment as experiment where experiment.inputSamples.size < experiment.protocol.refInputSamples.size and experiment.protocol is not null";
            query = version.getSession().createQuery(hql);
            query.setMaxResults(limit - ret.size());
            List list = query.list();
            System.out.println("Total experiments with missing inputs: " + list.size());
            ret.addAll(list);
        }
        return ret;
    }

    public static int countBadExperiments(ReadableVersion version) {
        String hql = "select count(os.experiment) " + BAD_OUTPUT;
        org.hibernate.Query query = version.getSession().createQuery(hql);
        Long count = (Long) query.list().get(0);
        System.out.println("Total experiments with missing refoutputsamples: " + count);

        hql =
            "select count(experiment) from Experiment as experiment where experiment.inputSamples.size < experiment.protocol.refInputSamples.size and experiment.protocol is not null";
        query = version.getSession().createQuery(hql);
        Long count2 = (Long) query.list().get(0);
        System.out.println("Found experiments with missing inputs: " + count2);

        return (int) (count + count2);
    }

    /**
     * MissingInputFixer.fixExperiment
     * 
     * @param next
     * @throws ConstraintException
     */
    public static void fixExperiment(Experiment experiment) throws ConstraintException {
        Protocol protocol = experiment.getProtocol();
        if (null == protocol) {
            System.out.println("No protocol for experiment: " + experiment.get_Hook());
            return;
        }

        // mend the inputs
        List<RefInputSample> riss = protocol.getRefInputSamples();
        for (Iterator iterator = riss.iterator(); iterator.hasNext();) {
            RefInputSample ris = (RefInputSample) iterator.next();
            InputSample is =
                experiment.findFirst(Experiment.PROP_INPUTSAMPLES, InputSample.PROP_REFINPUTSAMPLE, ris);
            if (null == is) {
                is = new InputSample((WritableVersion) experiment.get_Version(), experiment);
                is.setRefInputSample(ris);
                System.out.println("Made input sample for experiment: " + experiment.get_Hook());
            }
            is.setName(ris.getName());
            is.setAccess(experiment.getAccess()); // see    XP-71
        }
        Set<RefOutputSample> ross = protocol.getRefOutputSamples();
        Set<OutputSample> oss = experiment.getOutputSamples();
        for (Iterator iterator = oss.iterator(); iterator.hasNext();) {
            OutputSample os = (OutputSample) iterator.next();
            RefOutputSample ros =
                protocol.findFirst(Protocol.PROP_REFOUTPUTSAMPLES, RefOutputSample.PROP_NAME, os.getName());
            if (1 == ross.size()) {
                ros = ross.iterator().next();
            }
            if (null == ros) {
                throw new IllegalStateException("Cannot fix: " + experiment.get_Hook());
            }
            os.setRefOutputSample(ros);
            System.out.println("Fixed output sample: " + os.get_Hook());
        }

    }

    public static void main(String[] args) {
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = null;
        try {

            boolean done = false;
            while (!done) { // fix some experiments
                version = model.getWritableVersion(Access.ADMINISTRATOR);
                countBadExperiments(version);
                Collection<Experiment> bad = ExperimentFixer.findBadExperiments(version, TRANSACTION_LIMIT);
                System.out.println("Found: " + bad.size());
                done = bad.isEmpty();
                Iterator<Experiment> iterator = bad.iterator();
                while (iterator.hasNext()) {
                    Experiment experiment = iterator.next();
                    ExperimentFixer.fixExperiment(experiment);
                }
                System.out.println("Committing fixes");
                version.commit();
            }
            System.out.print(".");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (null != version && !version.isCompleted())
                version.abort();
        }
        System.out.println("OK: All experiments fixed");
    }

}
