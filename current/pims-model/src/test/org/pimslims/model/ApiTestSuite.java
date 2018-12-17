/*
 * Created on 25-Oct-2004 @author: Chris Morris
 */
package org.pimslims.model;

import java.io.FileNotFoundException;

import junit.textui.TestRunner;

import org.pimslims.dao.ModelImpl;
import org.pimslims.model.api.AbstractComponentTester;
import org.pimslims.model.api.AccessObjectTester;
import org.pimslims.model.api.AnnotationTester;
import org.pimslims.model.api.BookCitationTester;
import org.pimslims.model.api.CascadeDeleteTester;
import org.pimslims.model.api.CitationMetaClassTester;
import org.pimslims.model.api.CitationTester;
import org.pimslims.model.api.CollectionTester;
import org.pimslims.model.api.DeleteTester;
import org.pimslims.model.api.ExceptionTester;
import org.pimslims.model.api.ExperimentMetaClassTester;
import org.pimslims.model.api.ExperimentTester;
import org.pimslims.model.api.ExperimentTypeTester;
import org.pimslims.model.api.ExtensionTester;
import org.pimslims.model.api.HolderTester;
import org.pimslims.model.api.HolderTypeTester;
import org.pimslims.model.api.HostTester;
import org.pimslims.model.api.InputSampleTester;
import org.pimslims.model.api.JournalCitationTester;
import org.pimslims.model.api.LabBookEntryTester;
import org.pimslims.model.api.LocationTester;
import org.pimslims.model.api.MRUTester;
import org.pimslims.model.api.MolComponentTester;
import org.pimslims.model.api.NameConstraintTester;
import org.pimslims.model.api.OrderedByNameTester;
import org.pimslims.model.api.OrganisationTester;
import org.pimslims.model.api.OrganismTester;
import org.pimslims.model.api.ParameterTester;
import org.pimslims.model.api.PerformanceTester;
import org.pimslims.model.api.PermissionTester;
import org.pimslims.model.api.PersonTester;
import org.pimslims.model.api.ProtocolTester;
import org.pimslims.model.api.ReagentTester;
import org.pimslims.model.api.RefSampleSourceTester;
import org.pimslims.model.api.ReturnValueTester;
import org.pimslims.model.api.SampleComponentTester;
import org.pimslims.model.api.SampleMetaClassTester;
import org.pimslims.model.api.SampleTester;
import org.pimslims.model.api.SearchTest;
import org.pimslims.model.api.SimilarityHitTester;
import org.pimslims.model.api.TargetGroupTester;
import org.pimslims.model.api.TargetMetaClassTester;
import org.pimslims.model.api.TargetTester;
import org.pimslims.model.api.UserGroupTester;
import org.pimslims.model.api.WorkflowItemTester;
import org.pimslims.model.attribute.DerivedAttributeTest;
import org.pimslims.model.attribute.GetValueTest;
import org.pimslims.model.attribute.MultipleAttributeTest;
import org.pimslims.model.constructor.ConstructorTester;
import org.pimslims.model.core.LabBookEntryTest;
import org.pimslims.model.role.DerivedRoleTest;
import org.pimslims.model.role.HolderLocationTest;
import org.pimslims.model.role.ListRoleTest;
import org.pimslims.model.role.ManyToManyTester;
import org.pimslims.model.role.ManyToOneTester;
import org.pimslims.model.role.MultiValueAttributesTester;
import org.pimslims.model.role.OneToOneTester;
import org.pimslims.model.role.OneWayTester;
import org.pimslims.model.role.OutputSampleTest;
import org.pimslims.model.role.RefSampleToHazardPhrases;
import org.pimslims.model.role.removeTest;
import org.pimslims.model.schedule.ScheduledTaskTest;

// import org.pimslims.servlet.TestListener;

/**
 * Test suite for the implementation of the PIMS back end.
 */
public class ApiTestSuite extends junit.framework.TestSuite {

    /**
     * 
     */
    public ApiTestSuite() {
        super("Testing org.pimslims.model");
    }

    /**
     * @return test suite
     */
    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new ApiTestSuite();

        // api
        suite.addTestSuite(LabBookEntryTest.class);
        suite.addTestSuite(ScheduledTaskTest.class);
        suite.addTestSuite(AbstractComponentTester.class);
        suite.addTestSuite(AccessObjectTester.class);
        suite.addTestSuite(AnnotationTester.class);
        suite.addTestSuite(BookCitationTester.class);
        suite.addTestSuite(CascadeDeleteTester.class);
        suite.addTestSuite(HolderLocationTest.class);
        suite.addTestSuite(CitationMetaClassTester.class);
        suite.addTestSuite(CitationTester.class);
        suite.addTestSuite(CollectionTester.class);
        suite.addTestSuite(DeleteTester.class);
        suite.addTestSuite(ExceptionTester.class);
        suite.addTestSuite(ExperimentMetaClassTester.class);
        suite.addTestSuite(ExperimentTester.class);
        suite.addTestSuite(ExperimentTypeTester.class);
        suite.addTestSuite(ExtensionTester.class);
        suite.addTestSuite(HolderTester.class);
        suite.addTestSuite(HolderTypeTester.class);
        suite.addTestSuite(HostTester.class);
        suite.addTestSuite(InputSampleTester.class);
        suite.addTestSuite(JournalCitationTester.class);
        suite.addTestSuite(LabBookEntryTester.class);

        suite.addTestSuite(LocationTester.class);
        suite.addTestSuite(MolComponentTester.class);
        suite.addTestSuite(NameConstraintTester.class);
        suite.addTestSuite(MRUTester.class);
        suite.addTestSuite(OrganisationTester.class);
        suite.addTestSuite(SimilarityHitTester.class);
        suite.addTestSuite(OrderedByNameTester.class);
        suite.addTestSuite(OrganismTester.class);
        suite.addTestSuite(ParameterTester.class);
        suite.addTestSuite(WorkflowItemTester.class);
        suite.addTestSuite(PerformanceTester.class);
        suite.addTestSuite(PermissionTester.class);
        suite.addTestSuite(TargetGroupTester.class);
        suite.addTestSuite(TargetMetaClassTester.class);
        suite.addTestSuite(PersonTester.class);
        suite.addTestSuite(TargetTester.class);
        //suite.addTestSuite(ProjectTester.class);
        suite.addTestSuite(UserGroupTester.class);
        suite.addTestSuite(ProtocolTester.class);
        suite.addTestSuite(ReagentTester.class);
        suite.addTestSuite(RefSampleSourceTester.class);
        suite.addTestSuite(ReturnValueTester.class);
        suite.addTestSuite(SampleComponentTester.class);
        suite.addTestSuite(SampleMetaClassTester.class);
        suite.addTestSuite(SampleTester.class);
        suite.addTestSuite(SearchTest.class);

        // attribute
        suite.addTestSuite(DerivedAttributeTest.class);
        suite.addTestSuite(GetValueTest.class);
        suite.addTestSuite(MultipleAttributeTest.class);

        // constructor
        suite.addTestSuite(ConstructorTester.class);

        // role
        suite.addTestSuite(DerivedRoleTest.class);
        suite.addTestSuite(ListRoleTest.class);
        suite.addTestSuite(ManyToManyTester.class);
        suite.addTestSuite(ManyToOneTester.class);
        suite.addTestSuite(MultiValueAttributesTester.class);
        suite.addTestSuite(OneToOneTester.class);
        suite.addTestSuite(OneWayTester.class);
        suite.addTestSuite(OutputSampleTest.class);
        suite.addTestSuite(RefSampleToHazardPhrases.class);
        suite.addTestSuite(removeTest.class);

        return suite;
    }

    public static void oneTimeTearDown() {
        /* could HibernateUtilTester.showStatistics(model);
        Statistics stat = HibernateUtilTester.getStatistics();
        // make sure every session is closed
        assert stat.getSessionCloseCount() == stat.getSessionOpenCount(); */

    }

    /**
     * Runs these tests from the command line
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        try {
            ModelImpl.getModel(new java.io.File("conf/Properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        TestRunner.run(suite());

    }
}
