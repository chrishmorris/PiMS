package org.pimslims.embl;

import java.util.Calendar;

import junit.framework.TestCase;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.ConstraintException;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.ExperimentGroup;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.holder.Holder;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.people.Organisation;
import org.pimslims.model.reference.ExperimentType;
import org.pimslims.model.reference.TargetStatus;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

public class OwnerChangerTest extends TestCase {

    private static final String UNIQUE = "oc" + System.currentTimeMillis();

    private static final Calendar NOW = Calendar.getInstance();

    private final AbstractModel model;

    /**
     * @param name
     */
    public OwnerChangerTest(String name) {
        super(name);
        this.model = ModelImpl.getModel();
    }

    public void testConstructor() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            LabNotebook from = new LabNotebook(version, "from" + UNIQUE);
            new OwnerChanger(version, from, to);
        } finally {
            version.abort();
        }
    }

    public void test() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            Organisation object = new Organisation(version, UNIQUE);
            new OwnerChanger(version, from, to).change(object);
            assertEquals(to.getName(), object.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testRO() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ModelObject object = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            new OwnerChanger(version, from, to).change(ro);
            assertEquals(to.getName(), object.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testTargetDetails() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Molecule dna = new Molecule(version, "DNA", "dna" + UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            target.addNucleicAcid(dna);
            TargetStatus status = new TargetStatus(version, UNIQUE);
            Milestone milestone = new Milestone(version, NOW, status, target);
            new OwnerChanger(version, from, to).change(target);
            assertEquals(to.getName(), dna.get_Owner());
            assertEquals(to.getName(), milestone.get_Owner());
            assertEquals(Access.REFERENCE, status.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testTarget() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ResearchObjectiveElement element = new ResearchObjectiveElement(version, UNIQUE, UNIQUE, ro);
            Molecule protein = new Molecule(version, "protein", UNIQUE);
            Target target = new Target(version, UNIQUE, protein);
            element.setTarget(target);
            new OwnerChanger(version, from, to).change(ro);
            assertEquals(to.getName(), protein.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testExperimentDetails() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            Sample is = new Sample(version, "in" + UNIQUE);
            new InputSample(version, experiment).setSample(is);
            Sample os = new Sample(version, "out" + UNIQUE);
            new OutputSample(version, experiment).setSample(os);
            Parameter p = new Parameter(version, experiment);
            version.flush();
            new OwnerChanger(version, from, to).change(experiment);
            assertEquals(Access.REFERENCE, is.get_Owner());
            assertEquals(to.getName(), os.get_Owner());
            assertEquals(to.getName(), p.get_Owner());
            assertEquals(Access.REFERENCE, type.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testChangeGroup() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            experiment.setResearchObjective(ro);
            ExperimentGroup group = new ExperimentGroup(version, UNIQUE, UNIQUE);
            experiment.setExperimentGroup(group);
            version.flush();
            new OwnerChanger(version, from, to).change(ro);
            assertEquals(to.getName(), group.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testHolder() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            Sample sample = new Sample(version, UNIQUE);
            Holder holder = new Holder(version, UNIQUE, null);
            sample.setContainer(holder);
            new OwnerChanger(version, from, to).change(sample);
            assertEquals(to.getName(), sample.get_Owner());
            assertEquals(to.getName(), holder.get_Owner());
        } finally {
            version.abort();
        }
    }

    public void testExperiment() throws ConstraintException {
        WritableVersion version = this.model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = null;
            LabNotebook to = new LabNotebook(version, "to" + UNIQUE);
            ResearchObjective ro = new ResearchObjective(version, UNIQUE, UNIQUE);
            ExperimentType type = new ExperimentType(version, UNIQUE);
            Experiment experiment = new Experiment(version, UNIQUE, NOW, NOW, type);
            experiment.setResearchObjective(ro);
            new OwnerChanger(version, from, to).change(ro);
            assertEquals(to.getName(), experiment.get_Owner());
            assertEquals(Access.REFERENCE, type.get_Owner());
        } finally {
            version.abort();
        }
    }

}
