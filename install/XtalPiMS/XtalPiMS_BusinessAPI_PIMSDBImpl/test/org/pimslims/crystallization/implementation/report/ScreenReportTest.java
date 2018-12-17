package org.pimslims.crystallization.implementation.report;

import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.crystallization.view.PlateExperimentView;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.business.AbstractXtalTest;
import org.pimslims.crystallization.dao.ScreenDAO;
import org.pimslims.crystallization.dao.view.PlateExperimentViewDAO;
import org.pimslims.crystallization.dao.view.ScreenViewDAO;
import org.pimslims.test.AbstractTestCase;

import junit.framework.TestCase;

public class ScreenReportTest  extends AbstractTestCase  {

    public void testReportSuccessRateOnScreen() throws BusinessException{
        rv=getRV();
        try{
            //plate number
            PlateExperimentViewDAO plateDAO=new PlateExperimentViewDAO(rv);
            BusinessCriteria plateCriteria=new BusinessCriteria(plateDAO);
            int totoalPlate=plateDAO.findViewCount(plateCriteria);
            //screen
            ScreenViewDAO screenDAO=new ScreenViewDAO(rv);
            BusinessCriteria criteri=new BusinessCriteria(screenDAO);
            Collection<ScreenView> screens = screenDAO.findViews(criteri);  
            for(ScreenView screen:screens){
                BusinessCriteria plateCriteriaOnScreen=new BusinessCriteria(plateDAO);
                plateCriteriaOnScreen.add(BusinessExpression.Equals(PlateExperimentView.PROP_SCREEN, screen.getName(), true));
                int screenPlatesNumber=plateDAO.findViewCount(plateCriteriaOnScreen);
                if(screenPlatesNumber>0){
                plateCriteriaOnScreen.add(BusinessExpression.GreaterThan(PlateExperimentView.PROP_NUMBER_OF_CRYSTALS,
                    1, true));
                int screenSuccessPlatesNumber=plateDAO.findViewCount(plateCriteriaOnScreen);
                System.out.println("screen "+screen.getName()+" used in "+screenPlatesNumber+" plates with "+screenSuccessPlatesNumber+" success ");
                
                }
            }
        }finally{
            rv.abort();
        }
    }
    

}
