package uk.ac.sspf.spot.beans;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.Organism;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.worklist.ConstructProgressBean;

public class ConstructProgressBeanTest extends TestCase {

    private static final String ORGANISM_NAME = "testSpecies" + System.currentTimeMillis();

    private static final String PROTEIN_NAME = "testProtein" + System.currentTimeMillis();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ConstructProgressBeanTest.class);
    }

    private ConstructProgressBean bean = null;

    private Milestone milestone = null;

    private final AbstractModel model;

    private WritableVersion version;

    private Calendar now;

    private Target target = null;

    public ConstructProgressBeanTest(final String methodName) {
        super(methodName);
        this.model = ModelImpl.getModel();
    }

    @Override
    protected void setUp() {
        this.version = this.model.getTestVersion();
        try {
            this.now = java.util.Calendar.getInstance();
            final TargetStatus code = new TargetStatus(this.version, "test" + System.currentTimeMillis());
            final Molecule protein =
                new Molecule(this.version, "protein", ConstructProgressBeanTest.PROTEIN_NAME);
            this.target = new Target(this.version, "test" + System.currentTimeMillis(), protein);
            this.target.setSpecies(new Organism(this.version, ConstructProgressBeanTest.ORGANISM_NAME));
            this.milestone = new Milestone(this.version, this.now, code, this.target);
            this.bean = new ConstructProgressBean(this.milestone);
        } catch (final ConstraintException e) {
            this.version.abort();
            this.version = null;
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Override
    protected void tearDown() {
        if (null != this.version) {
            if (!this.version.isCompleted()) {
                this.version.abort(); // not testing persistence here
            }
        }
    }

    /*
     * LATER Test method for 'uk.ac.sspf.spot.beans.ConstructProgressBean.getConstructId()' public final void
     * testGetConstructId() { }
     */

    // properties that depend on target
    /*
     * Test method for 'uk.ac.sspf.spot.beans.ConstructProgressBean.getProteinName()'
     */
    public final void testGetProteinName() {
        Assert.assertEquals(ConstructProgressBeanTest.PROTEIN_NAME, this.bean.getTargetName());

    }

    /*
     * Test method for 'uk.ac.sspf.spot.beans.ConstructProgressBean.getOrganism()'
     */
    public final void testGetOrganism() {
        Assert.assertEquals(ConstructProgressBeanTest.ORGANISM_NAME, this.bean.getOrganism());
    }

    /*
     * Test method for 'uk.ac.sspf.spot.beans.ConstructProgressBean.getDateOfExperiment()'
     */
    public final void testGetDateOfExperiment() {
        Assert.assertEquals(this.now, this.bean.getDateOfExperimentDate());
    }

    /*
     * Test method for 'uk.ac.sspf.spot.beans.ConstructProgressBean.getMilestone()'
     */
    public final void testGetMilestone() {
        Assert.assertEquals(this.milestone.getStatus().getName(), this.bean.getMilestone());
    }

    public void xtestNoMilestone() {
        ExperimentType experimentType = null;
        try {
            this.now = java.util.Calendar.getInstance();
            // Status code = new Status(version, "testExp", "test");
            final Molecule protein =
                new Molecule(this.version, "protein", "expProtein" + System.currentTimeMillis());
            this.target = new Target(this.version, "test" + System.currentTimeMillis(), protein);
            final ResearchObjective eb =
                new ResearchObjective(this.version, "test" + System.currentTimeMillis(), "test");
            final ResearchObjectiveElement bpc =
                new ResearchObjectiveElement(this.version, "target", "test", eb);
            bpc.setTarget(this.target);

            // target.setSpecies(new NaturalSource(version,
            // "testSpecies"+System.currentTimeMillis()));
            experimentType = new ExperimentType(this.version, "test" + System.currentTimeMillis());
            final Experiment experiment =
                new Experiment(this.version, "test" + System.currentTimeMillis(), this.now, this.now,
                    experimentType);
            experiment.setProject(eb);
            this.bean = new ConstructProgressBean(experiment);
        } catch (final ConstraintException e) {
            this.version.abort();
            this.version = null;
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(experimentType.getName(), this.bean.getMilestone());
        Assert.assertEquals(this.now.getTime(), this.bean.getDateOfExperimentDate().getTime());
        Assert.assertEquals(this.target.get_Hook(), this.bean.getTargetId());
    }

}
