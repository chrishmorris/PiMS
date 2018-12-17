package org.pimslims.presentation.mru;

import java.util.Collection;

import org.pimslims.access.Access;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.sample.Sample;
import org.pimslims.model.target.Target;
import org.pimslims.test.AbstractTestCase;

public class MRUPerformanceTest extends AbstractTestCase {

    public void testMemoryLeak() throws InterruptedException {
        System.out.println("start!");
        Thread.sleep(4000);
        this.wv = this.getWV();
        MRUController.setMaxSize(40);
        MRUController.setMaxSizeOfEachClass(15);

        try {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 20; i++) {
                    final Collection<Target> targets = this.wv.getAll(Target.class, i * 10, 10);
                    for (final Target t : targets) {
                        MRUController.addObject(Access.ADMINISTRATOR, t.get_Hook());
                    }
                    // System.out.println(targets.size()+" targets added");
                    final Collection<Sample> samples = this.wv.getAll(Sample.class, i * 10, 10);
                    MRUController.getMRUs(Access.ADMINISTRATOR);
                    for (final Sample t : samples) {
                        MRUController.addObject(Access.ADMINISTRATOR, t.get_Hook());
                    }
                    // System.out.println(targets.size()+" samples added");
                    final Collection<Experiment> exps = this.wv.getAll(Experiment.class, i * 10, 10);
                    MRUController.getMRUs(Access.ADMINISTRATOR);
                    for (final Experiment t : exps) {
                        MRUController.addObject(Access.ADMINISTRATOR, t.get_Hook());
                    }
                    // System.out.println(targets.size()+" Experiments added");
                    MRUController.getMRUs(Access.ADMINISTRATOR);
                }
                // wv.commit();
            }
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }
}
