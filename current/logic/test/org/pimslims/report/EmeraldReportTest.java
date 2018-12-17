package org.pimslims.report;

import junit.framework.TestCase;

/**
 * EmeraldReportTest Verifies that PiMS can generate reports like the ones from EmBios Sharepoint
 */
@Deprecated
// no, see ThroughputReport
public class EmeraldReportTest extends TestCase {

    /*
     * Note that a subselect cannot produce a row of more than one item:
     * 
     * subquery ::= SELECT [DISTINCT] simple_select_expression
     * subquery_from_clause [where_clause] [groupby_clause] [having_clause]
     * 
     * simple_select_expression ::= single_valued_path_expression |
     * aggregate_expression | identification_variable
     */

    public EmeraldReportTest(String name) {
        super(name);
    }

    public void testVcidTarget() {
        doTest("select vcid.name, roe.target.name  from ReasearchObjective as vcid, ResearchObjectiveElement as roe where roe.researchObjective = vcid");
    }

    public void testVcidDatePrepInitiated() {
        doTest("select experiment.researchObjective.name, experiment.startDate from Experiment experiment where experiment.protocol.name='Prep Stage One' ");
    }

    // date prep finished and date report delivered are similar
    public void testVcidDatePasteAvailable() {
        // note that there is a risk of multiple responses
        doTest("select vcid.name, milestone.date from ResaerchObjective as vcid, Milestone as milestone "
            + " where milestone.researchObjective=vcid and milestone.status.name='Cell Paste Available' ");
    }

    public void testVcidStatus() {
        // TODO check performance of this query, there is a more complicated
        // alternative in TargetScoreboard
        doTest("select vcid.name, milestone.name from ResearchObjective as vcid, Milestone as milestone"
            + " where milestone.reasearchObjective = vcid "
            + " and milestone.date >=all (select other.date from Milestone other where other.researchObjective=vcid)");
    }

    private void doTest(String string) {
        throw new UnsupportedOperationException("not implemented yet");
    }

}
