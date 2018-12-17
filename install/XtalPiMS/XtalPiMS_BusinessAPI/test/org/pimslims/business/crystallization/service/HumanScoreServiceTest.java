package org.pimslims.business.crystallization.service;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.model.ScoreValue;
import org.pimslims.business.crystallization.model.TrialDrop;
import org.pimslims.business.crystallization.model.TrialPlate;
import org.pimslims.business.crystallization.model.UserScore;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.crystallization.util.ColorUtil;
import org.pimslims.business.crystallization.view.ScoreView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;

public class HumanScoreServiceTest extends AbstractXtalTest {

    private static final WellPosition D03II = new WellPosition(4, 3, 2);

    public HumanScoreServiceTest(final String methodName, final DataStorage dataStorage) {
        super(methodName, dataStorage);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetService() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final HumanScoreService service = this.dataStorage.getHumanScoreService();
            assertNotNull(service);
        } finally {
            this.dataStorage.abort();
        }
    }

    public void testCreate() throws BusinessException {
        this.dataStorage.openResources("administrator");
        try {
            final HumanScoreService service = this.dataStorage.getHumanScoreService();
            UserScore score = new UserScore();
            ScoreValue value = createScoreValue();
            score.setValue(value);
            TrialPlate plate = this.createTrialPlate(UNIQUE + "bc");
            TrialDrop drop = this.dataStorage.getTrialService().findTrialDrop(plate.getBarcode(), D03II);
            score.setDrop(drop);

            service.create(score);
            assertNotNull(score.getId());

            /* TODO 
            UserScore found = service.findHumanScore(score.getId());
            assertNotNull(found); */

            final BusinessCriteria criteria = new BusinessCriteria(service);
            criteria.add(BusinessExpression.Equals(ScoreView.PROP_BARCODE, plate.getBarcode(), true));
            final Collection<ScoreView> views = service.findViews(criteria);

            //check views
            assertEquals(1, views.size());
            final ScoreView view = views.iterator().next();
            assertEquals(plate.getBarcode(), view.getBarcode());
            assertEquals("human", view.getType());
            assertEquals(ColorUtil.convertColorToHex(score.getValue().getColour()), view.getColour());
            //TODO assertEquals(NOW, view.getDate());
            assertNull(view.getName());
            assertEquals(score.getValue().getDescription(), view.getDescription());
            //assertEquals(image.getDrop().getWellPosition().toString(), view.getWell());

            //check count
            assertEquals(1, service.findViewCount(criteria).intValue());

        } finally {
            this.dataStorage.abort();
        }
    }

    //TODO test find score for image

    //TODO test ScoreUtil

}
