/**
 * pims-web org.pimslims.servlet DaySevlet.java
 * 
 * @author Marc Savitsky
 * @date 5 Sep 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 Marc Savitsky The copyright holder has licenced the STFC to redistribute this
 *           software
 */
package org.pimslims.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.accessControl.User;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.Instrument;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.LabBookEntryDAO;
import org.pimslims.presentation.ModelObjectBean;
import org.pimslims.presentation.ModelObjectBeanTree;
import org.pimslims.presentation.experiment.TimeslotBean;
import org.pimslims.search.Conditions;
import org.pimslims.servlet.experiment.CreateExperiment;

public class DayServlet extends PIMSServlet {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat linkDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DayServlet() {
        super();
    }

    @Override
    public final String getServletInfo() {
        return "Show the Day view";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.servlet.PIMSServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        // get criteria

        final long begin = System.currentTimeMillis();
        String pathInfo = request.getPathInfo();
        if (null != pathInfo && 0 < pathInfo.length()) {
            pathInfo = pathInfo.substring(1); // strip initial slash
        }

        final String userName = PIMSServlet.getUsername(request);

        Date date = null;
        try {
            date = this.dateFormat.parse(pathInfo);
        } catch (final ParseException e) {
            date = new Date();
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        try {
            final Map<String, Object> criteria = new HashMap<String, Object>();
            final SortedSet<ModelObjectBeanTree> targets = new TreeSet<ModelObjectBeanTree>();
            final Set<ModelObject> experiments = new HashSet<ModelObject>();
            final Set<ModelObject> otherObjects = new HashSet<ModelObject>();

            final User user = version.findFirst(User.class, User.PROP_NAME, userName);
            final Calendar viewDate = Calendar.getInstance();
            if (null != date) {
                viewDate.setTime(date);
            }

            final int monthDay = viewDate.get(Calendar.DAY_OF_MONTH);
            String ordinal = "th";
            if (1 == monthDay || 21 == monthDay || 31 == monthDay) {
                ordinal = "st";
            } else if (2 == monthDay || 22 == monthDay) {
                ordinal = "nd";
            } else if (3 == monthDay || 23 == monthDay) {
                ordinal = "rd";
            }

            final Calendar prevDay = (Calendar) viewDate.clone();
            prevDay.add(Calendar.DAY_OF_YEAR, -1);

            final Calendar nextDay = (Calendar) viewDate.clone();
            nextDay.add(Calendar.DAY_OF_YEAR, 1);

            System.out.println("DayServlet previous day [" + this.linkDateFormat.format(prevDay.getTime()));
            System.out.println("DayServlet     next day [" + this.linkDateFormat.format(nextDay.getTime()));

            criteria.put(LabBookEntry.PROP_CREATIONDATE, Conditions.between(viewDate, nextDay));
            if (null != user) {
                criteria.put(LabBookEntry.PROP_CREATOR, user);
            }
            final Collection<LabBookEntry> labBookEntries =
                LabBookEntryDAO.getLabBookEntryList(version, criteria);

            criteria.clear();
            criteria.put(LabBookEntry.PROP_LASTEDITEDDATE, Conditions.between(viewDate, nextDay));
            if (null != user) {
                criteria.put(LabBookEntry.PROP_LASTEDITOR, user);
            }
            labBookEntries.addAll(LabBookEntryDAO.getLabBookEntryList(version, criteria));

            for (final LabBookEntry labBookEntry : labBookEntries) {

                //final String modelObjectName = labBookEntry.get_MetaClass().getJavaClass().getName();
                //System.out.println("LabBookEntry [" + labBookEntry.get_Name() + ":" + modelObjectName + "]");

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
                /*if (labBookEntry instanceof org.pimslims.model.people.Person) {
                    otherObjects.add(labBookEntry);
                } */

                if (labBookEntry instanceof org.pimslims.model.people.Organisation) {
                    otherObjects.add(labBookEntry);
                }

                if (labBookEntry instanceof org.pimslims.model.sample.RefSample) {
                    otherObjects.add(labBookEntry);
                }

                if (labBookEntry instanceof org.pimslims.model.sample.Sample) {
                    otherObjects.add(labBookEntry);
                }

                if (labBookEntry instanceof org.pimslims.model.protocol.Protocol) {
                    otherObjects.add(labBookEntry);
                }
            }

            // For testing with little data
            /*
            targets.clear();
            final Collection<LabBookEntry> myLabBookEntries = new HashSet<LabBookEntry>();
            myLabBookEntries.add((LabBookEntry) version
                .get("org.pimslims.model.target.ResearchObjective:36048"));
            myLabBookEntries.add((LabBookEntry) version
                .get("org.pimslims.model.target.ResearchObjective:36074"));
            myLabBookEntries.add((LabBookEntry) version.get("org.pimslims.model.target.Target:99503"));
            myLabBookEntries.add((LabBookEntry) version
                .get("org.pimslims.model.target.ResearchObjective:100009"));
            myLabBookEntries.add((LabBookEntry) version
                .get("org.pimslims.model.target.ResearchObjective:100666"));
            myLabBookEntries.add((LabBookEntry) version
                .get("org.pimslims.model.target.ResearchObjective:100692"));
            myLabBookEntries.add((LabBookEntry) version.get("org.pimslims.model.target.Target:99614"));

            for (final LabBookEntry labBookEntry : myLabBookEntries) {
                if (labBookEntry instanceof org.pimslims.model.target.Target) {
                    if (!targets.contains(labBookEntry)) {
                        targets.add(new ModelObjectBeanTree(labBookEntry));
                    }
                }

                if (labBookEntry instanceof org.pimslims.model.target.ResearchObjective) {
                    final ResearchObjective blueprint = (ResearchObjective) labBookEntry;
                    final ResearchObjectiveElement component = blueprint.getResearchObjectiveElements().iterator().next();

                    if (!targets.contains(component.getTarget())) {
                        targets.add(new ModelObjectBeanTree(component.getTarget()));
                    }

                    for (final ModelObjectBeanTree bean : targets) {
                        if (bean.equals(component.getTarget())) {
                            bean.add(new ModelObjectBeanTree(blueprint));
                        }
                    }
                }
            }
            */
            final SimpleDateFormat displayDateFormatter =
                new SimpleDateFormat("d'" + ordinal + "' MMMMM yyyy");
            request.setAttribute("displaydate", displayDateFormatter.format(date));
            request.setAttribute("datetime", date.getTime());
            request.setAttribute("today", this.linkDateFormat.format(date.getTime()));
            request.setAttribute("prevday", this.linkDateFormat.format(prevDay.getTime()));
            request.setAttribute("nextday", this.linkDateFormat.format(nextDay.getTime()));
            request.setAttribute("targets", targets);
            request.setAttribute("experiments", ModelObjectBean.getModelObjectBeans(experiments));
            request.setAttribute("others", ModelObjectBean.getModelObjectBeans(otherObjects));

            final Collection<Instrument> instruments = Collections.EMPTY_LIST; //TODO find them
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            request.setAttribute("timeslots", TimeslotBean.getTimeslots(calendar, instruments));
            request.setAttribute("instruments", CreateExperiment.getInstruments(version));

            System.out.println("DayServlet [" + (System.currentTimeMillis() - begin) + "]");
            final RequestDispatcher rd = request.getRequestDispatcher("/JSP/standard/DayView.jsp");
            //was final RequestDispatcher rd = request.getRequestDispatcher("/JSP/DayView.jsp");
            rd.forward(request, response);

        } finally {
            version.abort();
        }
    }

}
