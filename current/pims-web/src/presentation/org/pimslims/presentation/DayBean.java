/**
 * pims-web-current org.pimslims.presentation DayBean.java
 * 
 * @author ejd53
 * @date 29-Sep-2009
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2009 ejd53 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.lab.Utils;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * DayBean
 * 
 */
public class DayBean {

    private String formattedDate;

    private String linkFormattedDate;

    private Collection<ModelObjectShortBean> experiments;

    private Collection<ModelObjectShortBean> otherObjects;

    private SortedSet<ModelObjectBeanTree> targets;

    /**
     * Constructor for DayBean
     */
    private DayBean() {
        super();
    }

    /**
     * @return Returns the experiments.
     */
    public Collection<ModelObjectShortBean> getExperiments() {
        return this.experiments;
    }

    /**
     * @return Returns the otherObjects.
     */
    public Collection<ModelObjectShortBean> getOtherObjects() {
        return this.otherObjects;
    }

    /**
     * @return Returns the targets.
     */
    public SortedSet<ModelObjectBeanTree> getTargets() {
        return this.targets;
    }

    /**
     * @param experiments The experiments to set.
     */
    private void setExperiments(final Collection<ModelObjectShortBean> experiments) {
        this.experiments = experiments;
    }

    /**
     * @param otherObjects The otherObjects to set. TODO ModelObjectShortBeans
     */
    private void setOtherObjects(final List<ModelObjectShortBean> others) {
        this.otherObjects = others;
    }

    /**
     * @return Returns the targets.
     * 
     *         public SortedSet<ModelObjectBeanTree> getTargets() { return this.targets; }
     */

    /**
     * @param targets The targets to set.
     */
    public void setTargets(final SortedSet<ModelObjectBeanTree> targets) {
        this.targets = targets;
    }

    /**
     * @return Returns the formattedDate.
     */
    public String getFormattedDate() {
        return this.formattedDate;
    }

    /**
     * @return Returns the linkFormattedDate.
     */
    public String getLinkFormattedDate() {
        return this.linkFormattedDate;
    }

    /**
     * @param linkFormattedDate The linkFormattedDate to set.
     */
    private void setLinkFormattedDate(final String linkFormattedDate) {
        this.linkFormattedDate = linkFormattedDate;
    }

    /**
     * @param formattedDate The formattedDate to set. TODO no, get from Calendar
     */
    public void setFormattedDate(final String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public static DayBean getDayBean(final Calendar day, final ReadableVersion version, final String userName) {

        final User user = version.findFirst(User.class, User.PROP_NAME, userName);
        final Calendar nextDay = (Calendar) day.clone();
        nextDay.add(Calendar.DATE, 1);

        final Collection<LabBookEntry> labBookEntries =
            LabBookEntryDAO.findLabBookEntries(day, nextDay, version, user);

        return DayBean.resort(labBookEntries, day);

    }

    public static DayBean resort(final Collection<LabBookEntry> labBookEntries, final Calendar day) {
        final DayBean dayBean = new DayBean();

        final SimpleDateFormat df = new SimpleDateFormat(Utils.day_date_month_format);
        dayBean.setFormattedDate(df.format(day.getTime()));

        final SimpleDateFormat ldf = new SimpleDateFormat(Utils.link_date_format);
        dayBean.setLinkFormattedDate(ldf.format(day.getTime()));

        final SortedSet<ModelObjectBeanTree> targets = new TreeSet<ModelObjectBeanTree>();
        final Set<ModelObject> experiments = new HashSet<ModelObject>();
        final Set<ModelObject> otherObjects = new HashSet<ModelObject>();

        final Calendar nextDay = (Calendar) day.clone();
        nextDay.add(Calendar.DATE, 1);

        for (final LabBookEntry labBookEntry : labBookEntries) {
            if ((labBookEntry.getLastEditedDate() != null && (!labBookEntry.getLastEditedDate().before(day)) && labBookEntry
                .getLastEditedDate().before(nextDay))
                || (labBookEntry.getCreationDate() != null && (!labBookEntry.getCreationDate().before(day)) && labBookEntry
                    .getCreationDate().before(nextDay))) {
                // experiments
                if (labBookEntry instanceof org.pimslims.model.experiment.Experiment) {
                    final Experiment experiment = (Experiment) labBookEntry;
                    if (null == experiment.getExperimentGroup()) {
                        experiments.add(labBookEntry);
                    }
                }
                if (labBookEntry instanceof org.pimslims.model.experiment.ExperimentGroup) {
                    experiments.add(labBookEntry);
                }
                // targets and constructs
                if (labBookEntry instanceof org.pimslims.model.target.Target) {
                    if (!targets.contains(labBookEntry)) {
                        targets.add(new ModelObjectBeanTree(labBookEntry));
                    }
                }
                if (labBookEntry instanceof org.pimslims.model.target.ResearchObjective) {
                    final ResearchObjective blueprint = (ResearchObjective) labBookEntry;
                    if (blueprint.getResearchObjectiveElements().size() > 0) {
                        final ResearchObjectiveElement component =
                            blueprint.getResearchObjectiveElements().iterator().next();

                        final Target target = component.getTarget();
                        if (null != target) {
                            if (!targets.contains(target)) {
                                targets.add(new ModelObjectBeanTree(target));
                            }

                            for (final ModelObjectBeanTree bean : targets) {
                                if (bean.equals(target)) {
                                    bean.add(new ModelObjectBeanTree(blueprint));
                                }
                            }
                        }
                    }
                }
                // other objects
                if ( // labBookEntry instanceof org.pimslims.model.people.Person                     || 
                labBookEntry instanceof org.pimslims.model.people.Organisation
                    || labBookEntry instanceof org.pimslims.model.sample.RefSample
                    || labBookEntry instanceof org.pimslims.model.sample.Sample
                    || labBookEntry instanceof org.pimslims.model.protocol.Protocol) {
                    otherObjects.add(labBookEntry);
                }
            }
        }

        dayBean.setExperiments(ModelObjectShortBean.getBeans(experiments));
        dayBean.setTargets(targets);
        dayBean.setOtherObjects(ModelObjectShortBean.getBeans(otherObjects));

        return dayBean;
    }

}
