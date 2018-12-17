package org.pimslims.presentation.order;

import java.util.Collection;
import java.util.Set;

import org.pimslims.lab.experiment.HolderFactory;
import org.pimslims.lab.sample.SampleFactory;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.experiment.OutputSample;
import org.pimslims.model.experiment.Project;
import org.pimslims.model.molecule.Primer;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.sample.SampleComponent;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;

public class SavedOrderBean implements IOrderBean, Comparable {
    // experiment
    final String orderName;

    final String orderHook;

    // target
    final String targetName;

    final String targetHook;

    // sample
    final String sampleName;

    final String sampleHook;

    final String mcHook;

    final String scHook;

    final Boolean isForwardDirection;

    final String sequence;

    final String molType;

    final int rowPosition;

    final int colPosition;

    final String comments;

    IPlateOrderBean poBean;

    public SavedOrderBean(final Experiment exp) {

        this.comments = exp.getDetails();
        // order name & hook
        this.orderName = exp.getName();
        this.orderHook = exp.get_Hook();

        // Target
        // TODO get target from exp must have a standard way?
        final Project expBluePrint = exp.getProject();
        Collection<ResearchObjectiveElement> bcs = null;
        if (expBluePrint != null) {
            bcs = ((ResearchObjective) expBluePrint).getResearchObjectiveElements();
        }
        Target t = null;
        if (bcs != null && bcs.size() > 0) {
            t = bcs.iterator().next().getTarget();
        }
        if (t != null) {
            this.targetName = t.getName();
            this.targetHook = t.get_Hook();

        } else {
            this.targetName = null;
            this.targetHook = null;
        }

        // sample, position, direction
        final Set<OutputSample> outputSamples = exp.getOutputSamples();
        Sample sample = null;
        if (outputSamples != null && outputSamples.size() > 0) {
            sample = outputSamples.iterator().next().getSample();
        }

        if (sample != null) {
            this.sampleName = sample.getName();
            this.sampleHook = sample.get_Hook();
            if (null == sample.getRowPosition()) {
                this.rowPosition = -1;
            } else {
                this.rowPosition = sample.getRowPosition();
            }
            if (null == sample.getColPosition()) {
                this.colPosition = -1;
            } else {
                this.colPosition = sample.getColPosition();
            }
            this.isForwardDirection = SampleFactory.isForwardDirection(sample);
        } else {
            this.sampleName = null;
            this.sampleHook = null;
            this.rowPosition = 0;
            this.colPosition = 0;
            this.isForwardDirection = null;
        }

        // moltype,seq&Direction
        SampleComponent sc = null;
        if (sample != null && sample.getSampleComponents() != null && sample.getSampleComponents().size() > 0) {
            sc = sample.getSampleComponents().iterator().next();
        }
        if (sc != null) {
            this.scHook = sc.get_Hook();
        } else {
            this.scHook = null;
        }
        //MolComponent mc = null;
        Primer pr = null;
        if (sc != null) {
            final ModelObject mo = sc.get_Version().get(sc.getRefComponent().get_Hook());
            //if (mo.get_MetaClass().getJavaClass() == Molecule.class)
            //    mc = (Molecule) mo;
            if (mo.get_MetaClass().getJavaClass() == Primer.class) {
                pr = (Primer) mo;
            }
        }
        //if (mc != null) {
        //    // molType
        //    molType = mc.getMolType();
        //    sequence = mc.getSeqString();
        //    mcHook = mc.get_Hook();
        if (pr != null) {
            // molType
            this.molType = pr.getMolType();
            this.sequence = pr.getSequence();
            this.mcHook = pr.get_Hook();

        } else {
            this.molType = null;
            this.sequence = null;
            this.mcHook = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getPoBean()
     */
    public IPlateOrderBean getPoBean() {
        return this.poBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#setPoBean(org.pimslims.presentation.order.IPlateOrderBean)
     */
    public void setPoBean(final IPlateOrderBean poBean) {
        this.poBean = poBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getTargetName()
     */
    public String getTargetName() {
        return this.targetName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getColPosition()
     */
    public int getColPosition() {
        return this.colPosition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getIsForwardDirection()
     */

    public Boolean getIsForwardDirection() {
        return this.isForwardDirection;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getRowPosition()
     */

    public int getRowPosition() {

        return this.rowPosition;
    }

    public String getRowPositionInLetter() {
        return HolderFactory.getRow(this.rowPosition - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.pimslims.presentation.order.IOrderBean#getSequence()
     */

    public String getSequence() {
        return this.sequence;
    }

    public int compareTo(final Object obj) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (!(obj instanceof SavedOrderBean)) {
            throw new ClassCastException("obj" + obj.toString() + " is not a SavedOrderBean! ");
        }
        final SavedOrderBean anotherOrder = (SavedOrderBean) obj;
        if (this.getRowPosition() < anotherOrder.getRowPosition()) {
            return BEFORE;
        } else if (this.getRowPosition() > anotherOrder.getRowPosition()) {
            return AFTER;
        } else {
            if (this.getColPosition() < anotherOrder.getColPosition()) {
                return BEFORE;
            } else if (this.getColPosition() > anotherOrder.getColPosition()) {
                return AFTER;
            } else {
                return EQUAL;
            }
        }
    }

    public String getMolType() {
        return this.molType;
    }

    public String getOrderHook() {
        return this.orderHook;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public String getSampleHook() {
        return this.sampleHook;
    }

    public String getSampleName() {
        return this.sampleName;
    }

    public String getTargetHook() {
        return this.targetHook;
    }

    /**
     * @return the mcHook
     */
    public String getMcHook() {
        return this.mcHook;
    }

    /**
     * @return the scHook
     */
    public String getScHook() {
        return this.scHook;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return this.comments;
    }

}
