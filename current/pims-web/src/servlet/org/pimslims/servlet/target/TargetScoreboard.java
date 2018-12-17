/*
 * Created on 28.07.2005
 */
package org.pimslims.servlet.target;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pimslims.dao.ReadableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.leeds.TargetUtility;
import org.pimslims.metamodel.MetaClass;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.PimsQuery;
import org.pimslims.presentation.worklist.SampleDAO;
import org.pimslims.servlet.PIMSServlet;

/**
 * Search targets
 * 
 * @author Johan van Niekerk
 */
public class TargetScoreboard extends PIMSServlet {

    /**
     * 
     */
    public TargetScoreboard() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServletInfo() {
        return "PIMS: Target Scoreboard";
    }

    /**
     * {@inheritDoc}
     */
    protected String getTitle(final MetaClass metaClass) {
        return "Target Scoreboard";
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final java.io.PrintWriter writer = response.getWriter();
        final String metaClassName = "org.pimslims.model.target.Target";
        if (!this.checkStarted(request, response)) {
            return;
        }
        final MetaClass metaClass = this.getModel().getMetaClass(metaClassName);
        if (metaClass == null) {
            PIMSServlet.sendHttpHeaders(response, HttpServletResponse.SC_NOT_FOUND);
            writer.print("Unknown type: " + metaClassName);
            // LATER could offer drop down menu from model.getClassNames()
            return;
        }

        final ReadableVersion version = this.getReadableVersion(request, response);
        if (version == null) {
            return;
        }
        try {

            final java.util.Collection<TargetStatus> statuses = TargetUtility.getTargetStatuses(version);

            // Begin Ed code
            final java.util.Map<String, Integer> historicalScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's EVER BEEN in each status, incl. now
            final java.util.Map<String, Integer> currentScores = new java.util.HashMap<String, Integer>();
            // scores 1 for every target that's in each status NOW
            for (final TargetStatus status : statuses) {
                historicalScores.put(status.getName(), new Integer(0));
                currentScores.put(status.getName(), new Integer(0));
            }
            // End Ed code

            TargetScoreboard.searchScores(version, historicalScores, currentScores);

            String scoreboardTable = "";
            scoreboardTable += "<table>";
            scoreboardTable += "<tr><th>Status</th><th>Now</th><th>Total</th></tr>";
            String allTargetsLink = "0";
            final int numberofTargets = version.count(Target.class, Collections.EMPTY_MAP);
            if (numberofTargets > 0) {
                allTargetsLink =
                    "<a href='" + request.getContextPath() + "/Search/" + Target.class.getName() + "'>"
                        + numberofTargets + "</a>";
            }
            scoreboardTable +=
                "<tr><td>Registered in PIMS</td><td>" + allTargetsLink + "</td><td>" + allTargetsLink
                    + "</td></tr>";
            for (final Iterator i = statuses.iterator(); i.hasNext();) {
                final TargetStatus status = (TargetStatus) i.next();

                String currentViewScore = "0";
                String historicalViewScore = "0";
                final int currentScore = (currentScores.get(status.getName())).intValue();
                final int historicalScore = (historicalScores.get(status.getName())).intValue();
                if (0 != currentScore || 0 != historicalScore) {
                    // show the scores
                    if (currentScore > 0) {
                        currentViewScore =
                            "<a href='" + request.getContextPath()
                                + "/Search/org.pimslims.model.target.Target?milestones="
                                + URLEncoder.encode(status.get_Hook(), "UTF-8") + "'>" + currentScore
                                + "</a>"; // TODO proper URL in href attribute
                    }
                    if (historicalScore > 0) {
                        historicalViewScore =
                            "<a href='" + request.getContextPath()
                                + "/Search/org.pimslims.model.target.Target?status="
                                + URLEncoder.encode(status.get_Hook(), "UTF-8") + "'>" + historicalScore
                                + "</a>"; // TODO proper URL in
                        // href attribute
                    }
                    scoreboardTable +=
                        "<tr><td>" + status.getName() + "</td><td>" + currentViewScore + "</td><td>"
                            + historicalViewScore + "</td></tr>\n";
                }
            }
            scoreboardTable += "</table>";

            request.setAttribute("scoreboardTable", scoreboardTable);

            final RequestDispatcher dispatcher =
                request.getRequestDispatcher("/JSP/target/TargetScoreboard.jsp");
            dispatcher.forward(request, response);

            version.commit();

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

    static void searchScores(final ReadableVersion version,
        final java.util.Map<String, Integer> historicalScores,
        final java.util.Map<String, Integer> currentScores) {
        //historicalScores
        {
            final List<String> conditions = new LinkedList<String>();
            conditions
                .add(" milestone.dbId =(select max(milestone2.dbId) from "
                    + Milestone.class.getName()
                    + " milestone2 where milestone2.status=milestone.status and milestone2.target=milestone.target)");

            final String whereString =
                SampleDAO.getWhereHQL(version, "milestone", conditions, Milestone.class);
            final String groupBy = " group by status.name ";
            final String selectHQL =
                "select status.name, count(milestone) from " + Milestone.class.getName()
                    + " milestone left join milestone.status status " + whereString + groupBy;
            final org.pimslims.presentation.PimsQuery hqlQuery = PimsQuery.getQuery(version, selectHQL);
            final List results = hqlQuery.list();
            for (final Object object : results) {
                final Object result[] = (Object[]) object;
                final String statusName = (String) result[0];
                final Long numberofStatus = (Long) result[1];
                historicalScores.put(statusName, new Integer(numberofStatus.intValue()));
            }
        }
        //TODO test this
        //currentScores
        {
            final List<String> conditions = new LinkedList<String>();
            conditions
                .add(" milestones.date=(select max(milestone2.date) from "
                    + Milestone.class.getName()
                    + " milestone2 where milestone2.target=target) and  milestones.dbId=(select max(milestone3.dbId) from "
                    + Milestone.class.getName()
                    + " milestone3 where milestone3.target=target and milestone3.date=milestones.date )");
            final String whereString = SampleDAO.getWhereHQL(version, "target", conditions, Target.class);
            final String groupBy = " group by status.name ";
            final String selectHQL =
                "select status.name, count(milestones) from " + Target.class.getName()
                    + " target left join target.milestones milestones left join milestones.status status "
                    + whereString + groupBy;
            final org.pimslims.presentation.PimsQuery hqlQuery = PimsQuery.getQuery(version, selectHQL);
            final List results = hqlQuery.list();
            for (final Object object : results) {
                final Object result[] = (Object[]) object;
                final String statusName = (String) result[0];
                final Long numberofStatus = (Long) result[1];
                currentScores.put(statusName, new Integer(numberofStatus.intValue()));
            }
        }
        // ('Selected', 'PCR', 'Cloned', 'Expressed', 'Soluble', 'Purified',
        // 'Scale-up',
        // 'In crystallization', 'Crystallized', 'Diffraction-quality Crystals',
        // 'Native diffraction-data', 'Phasing diffraction-data', 'HSQC', 'NMR
        // Assigned',
        // 'NMR NOE', 'Crystal Structure', 'NMR Structure', 'In PDB',
        // 'Molecular Function', 'Biological Process', 'Cellular Component',
        // 'Work Stopped', 'Other')

        // For each target, add the status codes to the cumulative totals
        // Ideally, this loop should be for each status value, but looping over
        // targets is fine for now
        // Looping over targets will become possible when the getAll/findAll
        // method fot subclasses is implemented ie Target.Status
        // Return all the Targets

    }

}
