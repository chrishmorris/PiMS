package org.pimslims.servlet.experiment;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.pimslims.access.Access;
import org.pimslims.exception.ConstraintException;
import org.pimslims.model.experiment.Experiment;
import org.pimslims.model.target.ResearchObjective;
import org.pimslims.model.target.ResearchObjectiveElement;
import org.pimslims.model.target.Target;
import org.pimslims.presentation.mru.MRUController;
import org.pimslims.test.AbstractTestCase;
import org.pimslims.test.POJOFactory;

public class ViewExperimentTest extends AbstractTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetExpbFromMru() {
        this.wv = this.getWV();
        try {
            MRUController.clearAll();
            MRUController.setMaxSize(4);
            MRUController.setMaxSizeOfEachClass(2);
            final Experiment exp = POJOFactory.createExperiment(this.wv);
            final ResearchObjective expb = POJOFactory.createExpBlueprint(this.wv);
            MRUController.addObject(Access.ADMINISTRATOR, expb);

            Map<String, String> expbCandidates =
                ViewExperiment.getExpbFromMru(this.wv, Access.ADMINISTRATOR, exp);
            // make sure found the correct candidatae
            Assert.assertTrue(1 <= expbCandidates.size());
            final Iterator iter = expbCandidates.entrySet().iterator();
            final Entry firstEntry = (Entry) iter.next();
            Assert.assertEquals("[none]", firstEntry.getKey());
            Assert.assertEquals("[none]", firstEntry.getValue());
            Assert.assertEquals(2, expbCandidates.size());
            final Entry<String, String> SecondEntry = (Entry) iter.next();
            Assert.assertEquals(expb.get_Hook(), SecondEntry.getKey());
            Assert.assertEquals(expb.get_Name(), SecondEntry.getValue());

            exp.setProject(expb);
            // this expb is in use now
            expbCandidates = ViewExperiment.getExpbFromMru(this.wv, Access.ADMINISTRATOR, exp);
            Assert.assertEquals(1, expbCandidates.size());

            // test get expb from target
            final Target target = POJOFactory.createTarget(this.wv);
            MRUController.addObject(Access.ADMINISTRATOR, target);
            final ResearchObjective expb2 = POJOFactory.createExpBlueprint(this.wv);
            final ResearchObjectiveElement bc =
                new ResearchObjectiveElement(this.wv, "component type", "why chosen", expb2);
            target.addResearchObjectiveElement(bc);

            expbCandidates = ViewExperiment.getExpbFromMru(this.wv, Access.ADMINISTRATOR, exp);
            Assert.assertTrue(expbCandidates.containsKey(expb2.get_Hook()));

            // wv.commit();
        } catch (final ConstraintException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            if (!this.wv.isCompleted()) {
                this.wv.abort();
            }
        }
    }
}
