package org.pimslims.embl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.core.LabNotebook;
import org.pimslims.model.core.LabBookEntry;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.InputSample;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Parameter;
import org.pimslims.model.molecule.Molecule;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Milestone;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

/**
 * OwnerChanger Moves records from one lab notebook to another TODO could be simpler: maybe just search
 * LabBookEntry
 */
public class OwnerChanger {

    private final LabNotebook to;

    private final LabNotebook from;

    private final WritableVersion version;

    public OwnerChanger(WritableVersion version, LabNotebook from, LabNotebook to) {
        this.version = version;
        this.from = from;
        this.to = to;
    }

    public void change(LabBookEntry object) throws ConstraintException {
        if (null == object) {
            return;
        }
        System.out.println("Change to: " + to.getName() + " " + object);
        if (this.from == object.getAccess()) {
            object.setAccess(to);
        }
    }

    public void change(Sample sample) throws ConstraintException {
        this.change((LabBookEntry) sample);
        //TODO drop annotations
        // it would be nice to check that all users of sample can read it
        if (null != sample.getContainer()) {
            this.change((LabBookEntry) sample.getContainer());
        }
    }

    public void change(Target target) throws ConstraintException {
        this.change((LabBookEntry) target);
        // don't change: target.getTargetGroups(); target.getSpecies() target.getProjects()

        // navigated the other way  target.getResearchObjectiveElements()
        this.change(target.getProtein());
        this.changeMolecules(target.getNucleicAcids());

        for (Iterator iterator = target.getMilestones().iterator(); iterator.hasNext();) {
            Milestone milestone = (Milestone) iterator.next();
            this.change(milestone);
            // would be nice to check that people who can read the milestone can read its experiment
        }

    }

    /**
     * OwnerChanger.change
     * 
     * @param nucleicAcids
     * @throws ConstraintException
     */
    private void changeMolecules(Set<Molecule> molecules) throws ConstraintException {
        for (Iterator iterator = molecules.iterator(); iterator.hasNext();) {
            Molecule molecule = (Molecule) iterator.next();
            this.change(molecule);
            //TODO molecule.getRefMoleculeFeatures()
            //TODO molecule.getMoleculeFeatures()
        }
    }

    public void change(ResearchObjectiveElement roe) throws ConstraintException {
        this.change((LabBookEntry) roe);
        this.changeMolecules(roe.getTrialMolecules());
        //dont change roe.getTrialMolComponents();
        if (null != roe.getTarget()) {
            this.change(roe.getTarget());
        }
        //don't change roe.getSampleComponents();
        this.change(roe.getMolecule());
    }

    public void change(ResearchObjective ro) throws ConstraintException {
        this.change((LabBookEntry) ro);
        for (Iterator iterator = ro.getResearchObjectiveElements().iterator(); iterator.hasNext();) {
            ResearchObjectiveElement element = (ResearchObjectiveElement) iterator.next();
            this.change(element);
        }
        this.changeExperiments(ro.getExperiments());
    }

    /**
     * OwnerChanger.change
     * 
     * @param experiments
     * @throws ConstraintException
     */
    private void changeExperiments(Set<Experiment> experiments) throws ConstraintException {
        for (Iterator iterator = experiments.iterator(); iterator.hasNext();) {
            Experiment experiment = (Experiment) iterator.next();
            change(experiment);
        }

    }

    /**
     * OwnerChanger.change
     * 
     * @param experiment
     * @throws ConstraintException
     */
    public void change(Experiment experiment) throws ConstraintException {
        if (null != experiment.getExperimentGroup()) {
            this.change(experiment.getExperimentGroup());
        }
        this.change((LabBookEntry) experiment);
        for (Iterator iterator = experiment.getInputSamples().iterator(); iterator.hasNext();) {
            InputSample is = (InputSample) iterator.next();
            this.change(is);
            //don't this.change(is.getSample());
        }
        for (Iterator iterator = experiment.getOutputSamples().iterator(); iterator.hasNext();) {
            OutputSample os = (OutputSample) iterator.next();
            this.change(os);
            if (null != os.getSample()) {
                this.change(os.getSample());
            }
        }
        for (Iterator iterator = experiment.getParameters().iterator(); iterator.hasNext();) {
            Parameter p = (Parameter) iterator.next();
            this.change(p);
        }

        // navigation to milestones is via target
    }

    public static void main(String[] args) {
        if (2 != args.length) {
            System.out.println("Usage: from-labnotebook to-labnotebook");
            System.exit(2);
        }
        AbstractModel model = ModelImpl.getModel();
        WritableVersion version = model.getWritableVersion(Access.ADMINISTRATOR);
        try {
            LabNotebook from = version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, args[0]);
            LabNotebook to = version.findFirst(LabNotebook.class, LabNotebook.PROP_NAME, args[1]);
            OwnerChanger changer = new OwnerChanger(version, from, to);
            Collection<ResearchObjective> ros =
                version.findAll(ResearchObjective.class, LabBookEntry.PROP_ACCESS, from);
            for (Iterator iterator = ros.iterator(); iterator.hasNext();) {
                ResearchObjective ro = (ResearchObjective) iterator.next();
                changer.change(ro);
            }
            version.commit();
            System.out.println("OK: OwnerChanger completed");
            System.exit(0);
        } catch (ConstraintException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (AbortedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (!version.isCompleted()) {
                version.abort();
            }
        }
    }

}
