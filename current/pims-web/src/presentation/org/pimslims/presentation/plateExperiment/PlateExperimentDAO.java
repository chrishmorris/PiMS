package org.pimslims.presentation.plateExperiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.protocol.RefInputSample;
import org.pimslims.model.protocol.RefOutputSample;
import org.pimslims.model.reference.SampleCategory;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.persistence.JpqlQuery;
import org.pimslims.presentation.PimsQuery;
import org.pimslims.presentation.protocol.RefInputSampleBean;
import org.pimslims.presentation.worklist.SampleDAO;

public class PlateExperimentDAO {
/*
    private static List<PlateBean> getPlateBeanListOld(final ReadableVersion rv, final PlateCriteria criteria) {
        assert criteria != null;
        // get expGroups
        final Collection<Long> expGroupids = PlateExperimentDAO.getExpGroup(rv, criteria);

        // get beans
        final List<PlateBean> plateExperiments = new LinkedList<PlateBean>();
        for (final Long dbid : expGroupids) {
            final ExperimentGroup expGroup = rv.get(ExperimentGroup.class.getName() + ":" + dbid); //TODO makeHook()
            plateExperiments.add(new PlateBean(expGroup));
        }
        return plateExperiments;
    } */

    public static List<PlateBean> getPlateBeanList(final ReadableVersion rv, final PlateCriteria criteria) {
        assert criteria != null;
        // get expGroups
        final Collection<ExperimentGroup> expGroups = PlateExperimentDAO.getExpGroupObject(rv, criteria);

        // get beans
        final List<PlateBean> plateExperiments = new LinkedList<PlateBean>();
        for (final ExperimentGroup expGroup : expGroups) {
            plateExperiments.add(new PlateBean(expGroup));
        }
        return plateExperiments;
    }

    public static int countPlateBeanList(final ReadableVersion rv, final PlateCriteria criteria) {
        assert criteria != null;
        return PlateExperimentDAO.countExpGroup(rv, criteria);
    }

    public static int getTotalPlateExperimentCount(final ReadableVersion rv) {

        String selectHQL =
            " select count (distinct exp.experimentGroup.dbId) from "
                + OutputSample.class.getName()
                + " as output join output.experiment exp left join output.sample sample left join sample.holder holder";

        final PlateCriteria criteria = new PlateCriteria();

        final String whereString =
            SampleDAO.getWhereHQL(rv, "exp.experimentGroup", PlateExperimentDAO.getConditions(criteria),
                ExperimentGroup.class);

        selectHQL += whereString;
        //Long start = System.currentTimeMillis();
        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL);
        // System.out.println("createQuery using " + (System.currentTimeMillis() - start) + "ms");

        //start = System.currentTimeMillis();
        //System.out.println("getTotalPlateExperimentCount [" + query.getQueryString() + "]");
        final Long count = (Long) query.uniqueResult();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");

        return count.intValue();
    }

    /**
     * PlateExperimentDAO.runSpeedupQuery
     * 
     * @param rv
     * @param criteria
     */
    private static Collection<ExperimentGroup> getExpGroupObject(final ReadableVersion rv,
        final PlateCriteria criteria) {
        String selectHQL =
            "from " + ExperimentGroup.class.getName() + " eg where eg.id in ("
                + " select distinct exp.experimentGroup.dbId from " + Experiment.class.getName()
                + "  exp left join exp.creator " + "left join exp.experimentType expType "
                + "left join exp.outputSamples output " + "left join output.sample sample "
                + "left join sample.holder holder " + "left join  holder.holderType holderType";

        String whereString =
            SampleDAO.getWhereHQL(rv, "exp.experimentGroup", PlateExperimentDAO.getConditions(criteria),
                ExperimentGroup.class);
        //whereString += " order by exp.experimentGroup.dbId desc ";
        whereString += ") order by eg.id desc ";
        selectHQL += whereString;
        //Long start = System.currentTimeMillis();
        final Query query = rv.getSession().createQuery(selectHQL).setCacheable(false);
        //System.out.println("createQuery using " + (System.currentTimeMillis() - start) + "ms");
        if (criteria.getLimit() != PlateCriteria.UNLIMITED) {
            query.setMaxResults(criteria.getLimit());
            query.setFirstResult(criteria.getStart());
        }
        if (criteria.getExpTypeName() != null) {
            query.setParameter("expType", criteria.getExpTypeName());
        }
        if (criteria.getHoldTypeName() != null) {
            query.setParameter("holderType", criteria.getHoldTypeName());
        }
        if (criteria.getPlateName() != null) {
            query.setParameter("plateName", "%" + criteria.getPlateName() + "%");
        }

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Collection results = query.list();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
        return results;
    }

    /**
     * PlateExperimentDAO.runSpeedupQuery
     * 
     * @param rv
     * @param criteria
     * 
     *            private static Collection<Long> getExpGroup(final ReadableVersion rv, final PlateCriteria
     *            criteria) { String selectHQL = " select distinct exp.experimentGroup.dbId from " +
     *            Experiment.class.getName() + "  exp left join exp.creator " +
     *            "left join exp.experimentType expType " + "left join exp.outputSamples output " +
     *            "left join output.sample sample " + "left join sample.holder holder " +
     *            "left join  holder.holderType holderType";
     * 
     *            String whereString = SampleDAO.getWhereHQL(rv, "exp.experimentGroup",
     *            PlateExperimentDAO.getConditions(criteria), ExperimentGroup.class); whereString +=
     *            " order by exp.experimentGroup.dbId desc "; selectHQL += whereString; //Long start =
     *            System.currentTimeMillis(); final org.pimslims.presentation.PimsQuery query =
     *            PimsQuery.getQuery(rv, selectHQL, ExperimentGroup.class);
     *            //System.out.println("createQuery using " + (System.currentTimeMillis() - start) + "ms"); if
     *            (criteria.getLimit() != PlateCriteria.UNLIMITED) { query.setMaxResults(criteria.getLimit());
     *            query.setFirstResult(criteria.getStart()); } if (criteria.getExpTypeName() != null) {
     *            query.setParameter("expType", criteria.getExpTypeName()); } if (criteria.getHoldTypeName()
     *            != null) { query.setParameter("holderType", criteria.getHoldTypeName()); } if
     *            (criteria.getPlateName() != null) { query.setParameter("plateName", "%" +
     *            criteria.getPlateName() + "%"); }
     * 
     *            //start = System.currentTimeMillis(); //System.out.println("query.list() using [" +
     *            query.getQueryString() + "]"); final Collection results = query.list();
     *            //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
     *            return results; }
     */

    /**
     * PlateExperimentDAO.runSpeedupQuery
     * 
     * @param rv
     * @param criteria
     */
    private static int countExpGroup(final ReadableVersion rv, final PlateCriteria criteria) {
        String selectHQL =
            " select count (distinct expGroup) from "
                + ExperimentGroup.class.getName()
                + " expGroup left join expGroup.experiments exp left join exp.creator left join exp.experimentType expType left join exp.outputSamples output left join output.sample sample left join sample.holder holder left join holder.holderType holderType";

        final String whereString =
            SampleDAO.getWhereHQL(rv, "expGroup", PlateExperimentDAO.getConditions(criteria),
                ExperimentGroup.class);

        selectHQL += whereString;
        //Long start = System.currentTimeMillis();
        final org.pimslims.presentation.PimsQuery query =
            PimsQuery.getQuery(rv, selectHQL, ExperimentGroup.class);
        // System.out.println("createQuery using " + (System.currentTimeMillis() - start) + "ms");

        if (criteria.getExpTypeName() != null) {
            query.setParameter("expType", criteria.getExpTypeName());
        }
        if (criteria.getHoldTypeName() != null) {
            query.setParameter("holderType", criteria.getHoldTypeName());
        }
        if (criteria.getPlateName() != null) {
            query.setParameter("plateName", "%" + criteria.getPlateName() + "%");
        }

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Long count = (Long) query.uniqueResult();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");

        return count.intValue();
    }

    private static List<String> getConditions(final PlateCriteria criteria) {
        final List<String> conditions = new LinkedList<String>();
        conditions.add("exp.experimentGroup is not null");
        if (criteria.isOnlyGroups()) {
            conditions.add("holder is null");
        } else {
            if (criteria.getHoldTypeName() != null) {
                conditions.add("holderType.name=:holderType");
            }
            conditions.add("holder is not null");
        }
        //conditions.add("exp.dbId=(select max(exp2.dbId) from " + Experiment.class.getName()
        //    + " exp2 where exp2.experimentGroup=expGroup)");
        if (criteria.getExpTypeName() != null) {
            conditions.add("expType.name=:expType");
        }
        if (criteria.getPlateName() != null) {
            conditions.add(" lower(exp.experimentGroup.name) like lower(:plateName)");
        }
        return conditions;
    }

    /**
     * PlateExperimentDAO.getAssociates
     * 
     * @param group an experiment group
     * @param clazz a Class extending ModelObject, with a method getExperiment
     * @return all the associates of experiments in the group
     */
    public static <T extends ModelObject> Map<Experiment, Collection<T>> getAssociates(
        final ExperimentGroup group, final Class<T> clazz) {
        final String selectHQL =
            "from " + clazz.getName() + " as p " + "where p.experiment.experimentGroup = :group";
        final ReadableVersion rv = group.get_Version();

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL, clazz);
        query.setEntity("group", group);
        final Collection<T> results = query.list();
        final Map<Experiment, Collection<T>> ret = new HashMap(96);
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final T associate = (T) iterator.next();
            final Experiment experiment = (Experiment) associate.get_Value("experiment");
            if (!ret.containsKey(experiment)) {
                ret.put(experiment, new ArrayList());
            }
            ret.get(experiment).add(associate);
        }
        return ret;
    }

    /**
     * PlateExperimentDAO.getExperimentsAndResearchObjectives
     * 
     * @param group
     * @return
     */
    public static Map<Experiment, ResearchObjective> getExperimentsAndResearchObjectives(
        final ExperimentGroup group) {
        // note that the user already has legitimate access to the group
        final String selectHQL =
            "from " + Experiment.class.getName() + " as experiment, " + ResearchObjective.class.getName()
                + " as ro "
                + "  where experiment.experimentGroup = :group and experiment.researchObjective = ro";
        final ReadableVersion rv = group.get_Version();

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL);
        query.setEntity("group", group);
        final List results = query.list();

        final Map<Experiment, ResearchObjective> ret = new HashMap(96);
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final Object[] pair = (Object[]) iterator.next();
            final Experiment experiment = (Experiment) pair[0];
            final ResearchObjective ro = (ResearchObjective) pair[1];
            ret.put(experiment, ro);
        }
        return ret;
    }

    /**
     * PlateExperimentDAO.getPcrProduct
     * 
     * @param group
     * @return
     */
    public static Map<Experiment, Collection<Molecule>> getTrialMolecules(final ExperimentGroup group) {
        // note that the user already has legitimate access to the group
        final String selectHQL =
            "select Experiment" + " as experiment, " + " roe.trialMolecules as molecule "
                + " from Experiment as experiment, ResearchObjectiveElement" + " as roe "
                + "  where  experiment.experimentGroup = :group "
                // does not work: + " and ac.name like '%"                 + SequenceGetter.PCR_PRODUCT 
                + "  and roe.researchObjective = experiment.researchObjective ";
        final ReadableVersion rv = group.get_Version();

        final org.pimslims.presentation.PimsQuery query = PimsQuery.getQuery(rv, selectHQL);
        query.setEntity("group", group);
        final List results = query.list();

        final HashMap<Experiment, Collection<Molecule>> ret =
            new HashMap<Experiment, Collection<Molecule>>(96);
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final Object[] pair = (Object[]) iterator.next();
            final Experiment experiment = (Experiment) pair[0];
            final Molecule molecule = (Molecule) pair[1];
            if (!ret.containsKey(experiment)) {
                ret.put(experiment, new HashSet());
            }
            ret.get(experiment).add(molecule);
        }
        return ret;
    }

    /**
     */
    public static Collection<ExperimentGroup> getCrystallizationPlates(final ResearchObjective ro) {
        final String selectHQL =
            " select distinct eg from ExperimentGroup as eg, Experiment as exp "
                + " where exp.researchObjective=:ro and exp.experimentType.name='Trials' and exp.experimentGroup=eg";

        final ReadableVersion rv = ro.get_Version();
        final org.pimslims.presentation.PimsQuery query =
            PimsQuery.getQuery(rv, selectHQL, ExperimentGroup.class);
        query.setEntity("ro", ro);

        //start = System.currentTimeMillis();
        //System.out.println("query.list() using [" + query.getQueryString() + "]");
        final Collection results = query.list();
        //System.out.println("query.list() using " + (System.currentTimeMillis() - start) + "ms");
        return results;
    }

    // TODO modify this for NMR experiments
    public static Collection<RefInputSampleBean> getRefInputBeans(final RefOutputSample out) {
        if (null == out) {
            return Collections.EMPTY_LIST;
        }
        final ReadableVersion version = out.get_Version();
        final Collection<RefInputSampleBean> ret = new ArrayList();

        // get all the relevant RefInputSamples
        String selectHQL =
            " select refInputSample" + " from RefInputSample as refInputSample "
                + " where refInputSample.sampleCategory=:category"
                + " and refInputSample.protocol.isForUse=true" + " order by refInputSample.protocol.name";

        PimsQuery query =
            PimsQuery.getQuery(version, selectHQL, org.pimslims.model.protocol.RefInputSample.class);
        query.setEntity("category", out.getSampleCategory());
        final Collection<RefInputSample> results = query.list();

        // get the ones that have experiments, sorted by number of experiments
        selectHQL =
            " select refInputSample.dbId, count(experiment) as c "
                + " from RefInputSample as refInputSample, Experiment as Experiment "
                + " where refInputSample.sampleCategory=:category"
                + " and experiment.protocol = refInputSample.protocol "
                + " and refInputSample.protocol.isForUse=true"
                + " group by refInputSample.dbId order by count(experiment) desc";

        query = PimsQuery.getQuery(version, selectHQL);
        query.setEntity("category", out.getSampleCategory());

        final Collection sorted = query.list();
        for (final Iterator iterator = sorted.iterator(); iterator.hasNext();) {
            final Long dbid = (Long) ((Object[]) iterator.next())[0];
            final RefInputSample in = version.get(RefInputSample.class.getName() + ":" + dbid);
            if (null == in) {
                continue; // not viewable
            }
            results.remove(in);
            ret.add(new RefInputSampleBean(in));
        }
        // now put in the unused ones
        for (final Iterator iterator = results.iterator(); iterator.hasNext();) {
            final RefInputSample in = (RefInputSample) iterator.next();
            ret.add(new RefInputSampleBean(in));
        }

        return ret;
    }

    /**
     * PlateExperimentDAO.getExperimentGroups TODO no, this results in disclosure
     * 
     * @param category
     * @return
     */
    public static Collection<ExperimentGroup> getExperimentGroupsForPlates(final SampleCategory category) {
        final String selectHQL =
            "  distinct A" + " from ExperimentGroup as A " + "join A.experiments as experiment "
                + " join experiment.outputSamples as os "
                + " join os.sample as sample join sample.sampleCategories as category " + " where "
                //TODO + JpqlQuery.getAccessControlHQL(category.get_Version(), "eg", ExperimentGroup.class)
                + " category=:category" + " and sample.isActive=true" + " and sample.holder is not null";
        final ReadableVersion rv = category.get_Version();

        final JpqlQuery query = new JpqlQuery(selectHQL, rv, ExperimentGroup.class);
        query.setEntity("category", category);
        return query.list();
    }

    public static Collection<ExperimentGroup> getParentGroups(final ExperimentGroup group) {
        final String selectHQL =
            " select distinct eg" + " from ExperimentGroup as eg join eg.experiments as experiment "
                + " join experiment.outputSamples as os join os.sample as sample"
                + " join sample.inputSamples as ins join ins.experiment as exp "
                + " join exp.experimentGroup as group" + " where group=:group";

        final ReadableVersion rv = group.get_Version();

        final org.pimslims.presentation.PimsQuery query =
            PimsQuery.getQuery(rv, selectHQL, ExperimentGroup.class);
        query.setEntity("group", group);
        return query.list();

    }
}
