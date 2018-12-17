package org.pimslims.presentation.worklist;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PimsQuery;

@Deprecated
// obsolete
public class ExpBlueprintDao<T> {
    static public List<ConstructProgressBean> getNoProgressExpBlueprint(final ReadableVersion rv,
        final int daysNoProgress, final Boolean isReadyForNext, final String milestoneCodeName,
        final int limit) {

        String selectHQL;
        if (daysNoProgress > 0) {
            selectHQL = ExpBlueprintDao.getExpbQuery(isReadyForNext, DAOUtils.endDateTag, milestoneCodeName);
        } else {
            selectHQL = ExpBlueprintDao.getExpbQuery(isReadyForNext, null, milestoneCodeName);
        }
        final org.pimslims.presentation.PimsQuery hqlQuery =
            PimsQuery.getQuery(rv, selectHQL, ResearchObjective.class);

        hqlQuery.setMaxResults(limit);

        if (daysNoProgress > 0) {
            final long time = System.currentTimeMillis() - daysNoProgress * 24 * 60 * 60 * 1000 + 1;
            final Calendar expDate = java.util.Calendar.getInstance();
            expDate.setTimeInMillis(time);
            hqlQuery.setParameter(DAOUtils.endDateTag, expDate);
        }
        final List list = hqlQuery.list();
        final List<ConstructProgressBean> expBlueprints = ExpBlueprintDao.getConstructProgressBeans(rv, list);
        return new LinkedList<ConstructProgressBean>(expBlueprints);
    }

    private static List<ConstructProgressBean> getConstructProgressBeans(final ReadableVersion rv,
        final List list) {
        final List<ConstructProgressBean> beans = new LinkedList<ConstructProgressBean>();
        for (final Object object : list) {
            final List<Object> results = Arrays.asList((Object[]) object);
            final ConstructProgressBean bean = new ConstructProgressBean();
            // construct name and hook
            final String hook = ResearchObjective.class.getName() + ":" + results.get(0);
            if (null == rv.get(hook)) {
                continue; // that is not readable by this user
            }
            bean.setConstructHook(hook);
            bean.setConstructId((String) results.get(1));
            // latest milestone date and name
            bean.setDateOfExperiment((Calendar) results.get(2));
            bean.setMilestone((String) results.get(3));
            // experiment status/result
            bean.setResult((String) results.get(4));
            // target name and hook
            bean.setTargetId(Target.class.getName() + ":" + results.get(5));
            bean.setTargetName((String) results.get(6));

            beans.add(bean);
        }
        return beans;
    }

    // HQL: select expBlueprint.dbId from org.pimslims.model.target.ResearchObjective
    // expBlueprint join expBlueprint.hbMilestones milestone join
    // milestone.hbExperiment exp where exp.status='OK' group by expBlueprint
    // having max(milestone.date)<:endDate order by max(milestone.date) desc
    private static String getExpbQuery(final Boolean isReadyForNext, final String endDateTag,
        final String milestoneCodeName) {

        // the elements want to be selected
        final String selectedElements =
            " expBlueprint.dbId, expBlueprint.commonName,exp.endDate, code.name, exp.status, target.dbId, target.name  ";
        String milestoneCodeString = "";
        if (milestoneCodeName != null && milestoneCodeName.length() > 0) {
            milestoneCodeString = " code.name='" + milestoneCodeName + "' ";
        }
        final String orderString = " order by max(milestone.date) desc ";
        String groupingString = " group by " + selectedElements;
        if (endDateTag != null) {
            groupingString += " having max(milestone.date)<:" + endDateTag + " ";
        }
        String statusString = "";
        if (isReadyForNext != null) {
            if (isReadyForNext == true) {
                statusString = " exp.status='OK' ";
            } else {
                statusString = " exp.status!='OK' ";
            }
        }
        // Use the subselect hql to make sure working on latest milestone
        final String subSelectString =
            " where milestone.date= (select max(milestone2.date) " + "from "
                + ResearchObjective.class.getName() + " expBlueprint2 "
                + " join expBlueprint2.experiments exp2 join exp2.milestones milestone2 "
                + "where expBlueprint2.dbId= expBlueprint.dbId)";

        // keywords to link every piece
        // String whereString="";
        // if((subSelectString+statusString+milestoneCodeString).length()>1)
        // whereString=" where ";
        String andString0 = "";
        if ((statusString + milestoneCodeString).length() > 1) {
            andString0 = " and ";
        }

        String andString1 = "";
        if (statusString.length() > 1 && milestoneCodeString.length() > 1) {
            andString1 = " and ";
        }
        /**
         * due to a bug about group by in postgres, We can not do: * Hql: select expBlueprint from
         * org.pimslims.model.target.ResearchObjective expBlueprint join expBlueprint.hbMilestones milestone
         * join milestone.hbExperiment exp where exp.status='OK' group by expBlueprint having
         * max(milestone.date)<:endDate order by max(milestone.date) desc
         * 
         * 
         * 
         * We have to use following HQL to avoid this: Hql: select expBlueprint.dbId from
         * org.pimslims.model.target.ResearchObjective expBlueprint join expBlueprint.hbMilestones milestone
         * join milestone.hbExperiment exp where exp.status='OK' group by expBlueprint having
         * max(milestone.date)<:endDate order by max(milestone.date) desc join milestone.experiment exp
         * 
         */
        final String queryString =
            "select "
                + selectedElements
                + " from "
                + ResearchObjective.class.getName()
                + " expBlueprint join expBlueprint.experiments exp join exp.milestones milestone join milestone.status code join milestone.target target "
                + subSelectString + andString0 + statusString + andString1 + milestoneCodeString
                + groupingString + orderString;

        return queryString;
    }
}
