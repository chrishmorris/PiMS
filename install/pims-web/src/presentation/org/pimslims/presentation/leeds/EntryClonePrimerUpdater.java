package org.pimslims.presentation.leeds;

import java.util.Collection;

import org.pimslims.access.Access;
import org.pimslims.dao.AbstractModel;
import org.pimslims.dao.ModelImpl;
import org.pimslims.dao.WritableVersion;
import org.pimslims.exception.AbortedException;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.Target;

/*
 * This needs to be run to associate target with entry clone constructs entered manually as they have another
 * names, they was not associated automatically
 */
/**
 * @author Petr Troshin aka pvt43
 */

@Deprecated
// Leeds code is no longer supported
public class EntryClonePrimerUpdater {

    AbstractModel model = null;

    WritableVersion wv = null;

    public EntryClonePrimerUpdater() {
        this.model = ModelImpl.getModel();
    }

    public void update() {
        this.wv = this.model.getWritableVersion(Access.ADMINISTRATOR);
        final Collection<Experiment> exps = SavedPlasmid.getEntryCloneExperiments(this.wv);
        try {
            for (final Experiment exp : exps) {
                final SavedPlasmid pl = new SavedPlasmid(exp);
                if (pl.getTarget() != null) {
                    // System.out.println("Target exist for " + exp.getName());
                    continue;
                }
                final Target target = FindLeedsTarget.findCorrespondingTarget(this.wv, exp.getName());
                if (target != null) {
                    AbstractConstructBean.setTarget(target, exp);
                    // System.out.println("Target "+ target.getName()
                    // + " found for "+ exp.getName());
                }
            }
            this.wv.commit();
        } catch (final AbortedException e) {
            e.printStackTrace();
        } catch (final ConstraintException e) {
            e.printStackTrace();
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }

    }

    public static final void main(final String[] args) {
        final EntryClonePrimerUpdater up = new EntryClonePrimerUpdater();
        up.update();
    }

}
