package org.pimslims.presentation.worklist;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.HookUtility;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.sample.Sample;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.presentation.PimsQuery;

/**
 * SampleDAO TODO these methods can give out of memory errors
 */
public class SampleDAO {

    public static List<SampleBean> getSampleBeanList(final ReadableVersion rv, final SampleCriteria criteria) {
        assert criteria != null;

        // get samples
        final List<Sample> samples =
            SampleDAO.getNoProgressSamples(rv, criteria.getDaysNoProgress(), criteria.getReadyForNext(),
                criteria.getLimit(), criteria.getExpTypeNameReadyFor(), criteria.getAlreadyInUse(),
                criteria.getActive(), criteria.getUserHookAssignedTo());
        // create sampleBeans from samples
        final List<SampleBean> sampleBeans = new LinkedList<SampleBean>();
        for (final Sample sample : samples) {
            sampleBeans.add(new SampleBean(sample));
        }
        return sampleBeans;
    }

    /**
     * get a list of samples which days of no progress> daysNoProgress and ready for next is isReadyForNext
     * sorted by days from new to old max numerber of sample in this list is limit
     * 
     * @param daysNoProgress days of no progress, when it <=0, include all
     * @param isReadyForNext ready for next or not, when null, inlcude all
     * @param limit max number of sample will be return
     * @param expTypeName the expTypeName which sample is ready for, when null, include all
     * @param active
     * @return
     */
    static List<Sample> getNoProgressSamples(final ReadableVersion rv, final int daysNoProgress,
        final Boolean isReadyForNext, final int limit, final String expTypeName, final Boolean alreadyInUse,
        final Boolean active, final String userHookAssignedTo) {
        String selectHQL;
        // create HQL string
        if (daysNoProgress > 0) {
            selectHQL =
                SampleDAO.getSampleQuery(isReadyForNext, DAOUtils.endDateTag, expTypeName, alreadyInUse,
                    active, userHookAssignedTo, rv);
        } else {
            selectHQL =
                SampleDAO.getSampleQuery(isReadyForNext, null, expTypeName, alreadyInUse, active,
                    userHookAssignedTo, rv);
        }
        // speedupQuery(rv,isReadyForNext,alreadyInUse,active,personHookAssignedTo);

        // create query

        final org.pimslims.presentation.PimsQuery hqlQuery = PimsQuery.getQuery(rv, selectHQL, Sample.class);
        if (limit > 0) {
            hqlQuery.setMaxResults(limit);
        }

        // set parameter
        if (daysNoProgress > 0) {
            final long time = System.currentTimeMillis() - daysNoProgress * 24 * 60 * 60 * 1000L + 1;
            final Calendar expDate = java.util.Calendar.getInstance();
            expDate.setTimeInMillis(time);
            hqlQuery.setParameter(DAOUtils.endDateTag, expDate);
        }
        // get and filte the results for access control
        //Long begin = System.currentTimeMillis();
        final Collection<Sample> samples = DAOUtils.getSamplesFromIDList(rv, hqlQuery.list());
        // System.out.println((System.currentTimeMillis()-begin)/1000.0+"s for
        // Sample bean Query");
        // resort
        for (final Sample sample : new LinkedList<Sample>(samples)) {
            if (sample.getOutputSample() == null || sample.getOutputSample().getExperiment() == null
                || sample.getOutputSample().getExperiment().getEndDate() == null) {
                samples.remove(sample);
                samples.add(sample);
            }
            //remove for alreadyInUse
            //TODO this may cause problem which return less number of result than asked
            if (alreadyInUse != null && !alreadyInUse) {
                boolean notFailed = false;
                for (final InputSample is : sample.getInputSamples()) {
                    if (is.getExperiment().getStatus() != null
                        && !is.getExperiment().getStatus().equalsIgnoreCase("failed")) {
                        notFailed = true;
                        continue;
                    }
                }
                if (notFailed) {
                    samples.remove(sample);
                }
            }
        }
        return new LinkedList<Sample>(samples);
    }

    /**
     * 
     * @param alreadyInUse when true, sample has inputSample; when null inputsample is not counted
     * @param active
     * @param personHookAssignedTo
     * @param version
     */
    static String getSampleQuery(final Boolean isReadyForNext, final String endDateTag,
        final String expTypeName, final Boolean alreadyInUse, final Boolean active,
        final String userHookAssignedTo, final ReadableVersion version) {
        final List<String> conditions = new LinkedList<String>();
        // sample assignedTo
        if (userHookAssignedTo != null) {
            conditions.add("sample.assignTo=" + HookUtility.getID(userHookAssignedTo));
        }

        // sample active
        if (active != null) {
            if (active == true) {
                conditions.add(" sample.isActive=true ");
            } else {
                conditions.add(" sample.isActive=false ");
            }
        }
        // group by sampleID
        final String groupString = " group by sample ";
        // alreadyInUse
        String alreadyInUseString_part1 = "";
        if (alreadyInUse != null && alreadyInUse == true) {
            alreadyInUseString_part1 = " join sample.inputSamples ";
        } else if (alreadyInUse != null && alreadyInUse == false) {
            alreadyInUseString_part1 =
                " left join sample.inputSamples inputsample left join inputsample.experiment nextExp ";
            conditions.add(" (inputsample is null OR nextExp.status='Failed') ");
        }
        // order by
        /**
         * can not use "order by exp.endDate desc" due to a bug in hibernate which can not put NULL value
         * after "not-null" "order by ('2000-01-01 00:00:00'-exp.endDate) asc" equals to "order by exp.endDate
         * desc NULL First "
         */
        //
        final String orderString =
            " order by max(" + SampleDAO.getSysTime() + "-exp.endDate) asc, sample.id desc ";

        // ready for next
        if (isReadyForNext != null) {
            if (isReadyForNext == true) {
                conditions.add("exp.status='OK'");
            } else {
                conditions.add("exp.status!='OK'");
            }
        }
        // next exp type
        String expTypeString_part1 = "";
        String from2 = "";
        if (expTypeName != null) {
            expTypeString_part1 = " join refIS.protocol protocol join protocol.experimentType expType ";
            // expTypeString_part2=" and expType.name='"+expTypeName+"' ";
            conditions.add(" expType.name='" + expTypeName + "' ");
            conditions.add(" refIs.sampleCategory=sc.dbId ");
            from2 = ", " + RefInputSample.class.getName() + " refIs ";
        }
        // no progress time
        if (endDateTag != null) {
            conditions.add(" exp.endDate<:" + endDateTag);
        }

        // where conditines
        final String whereString = SampleDAO.getWhereHQL(version, "sample", conditions, Sample.class);
        // final query string
        final String queryString =
            "select sample.id from "
                + Sample.class.getName()
                + " sample "
                + from2
                + " left join sample.outputSample outputSample left join outputSample.experiment exp left join sample.sampleCategories as sc "
                + expTypeString_part1 + alreadyInUseString_part1 + whereString + groupString + orderString;

        return queryString;
    }

    //TODO move to DM project
    static String getSysTime() {
        if (ModelImpl.getInstallationProperties().getProperty("db.url").contains("oracle")) {
            return "sysdate";
        }
        return "'2000-01-01 00:00:00+00'";

    }

    public static String getWhereHQL(final ReadableVersion rv, final String alias,
        final List<String> conditions, final Class targetJavaClass) {
        return JpqlQuery.getWhereHQL(rv, alias, conditions, targetJavaClass);
    }
}
