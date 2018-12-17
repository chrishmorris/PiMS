/**
 * current-pims-web org.pimslims.lab.target TargetProgress.java
 * 
 * @author Petr Troshin
 * @date 7 Feb 2008
 * 
 *       Protein Information Management System
 * @version: 1.3
 * 
 *           Copyright (c) 2008 Petr
 * 
 * 
 */
package org.pimslims.leeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.target.TargetExperimentBean;
import org.pimslims.search.Conditions;

/*
 * TargetProgress
 */
public class TargetProgressSearcher {

    final Map<String, Object> criteria;

    final ReadableVersion rv;

    public TargetProgressSearcher(final ReadableVersion rv) {
        this.criteria = new HashMap<String, Object>();
        this.rv = rv;
        this.addDefaultCriteria();
    }

    void addDefaultCriteria() {
        this.criteria.put(Experiment.PROP_PROJECT, Conditions.notNull());
    }

    public void addCriteria(final String property, final Object value) {
        this.criteria.put(property, value);
/*
        criteria.put(Experiment.PROP_STARTDATE, Restriction.greaterThan(new Timestamp((System
            .currentTimeMillis())
            - (long) 1000 * 60 * 60 * 24 * 30)));
*/
    }

    public List<TargetExperimentBean> search() {
        final Collection<Experiment> exps = this.rv.findAll(Experiment.class, this.criteria);
        //System.out.println("CRIT " + this.criteria);
        final List<Experiment> experiments = new ArrayList<Experiment>(exps);

        final Comparator expStartDateComparator = new Comparator<Experiment>() {

            public final int compare(final Experiment o1, final Experiment o2) {
                return o2.getStartDate().compareTo(o1.getStartDate());
            }
        };
        Collections.sort(experiments, expStartDateComparator);

        return this.getTargetWithLastExperiment(experiments);

    }

    private List<TargetExperimentBean> getTargetWithLastExperiment(final Collection<Experiment> exps) {
        final List<TargetExperimentBean> expTarget = new ArrayList<TargetExperimentBean>();
        for (final Experiment exp : exps) {
            final Target t = TargetUtility.getFirstTarget(exp); // this needs revising later as this will not support complexes
            if (null != t) {
                expTarget.add(new TargetExperimentBean(t, exp));
            }
        }
        //Collections.sort(expTarget, new ExperimentStartDateComporator());
        return expTarget;
    }

    public static class ExperimentStartDateComporator implements Comparator<TargetExperimentBean> {
        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(final TargetExperimentBean o1, final TargetExperimentBean o2) {
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return -1;
            }
            return o2.getLastExperiment().getStartDate().compareTo(o1.getLastExperiment().getStartDate());
        }

    }
}
