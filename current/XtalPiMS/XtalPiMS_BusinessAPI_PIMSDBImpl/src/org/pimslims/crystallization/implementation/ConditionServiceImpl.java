/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package org.pimslims.crystallization.implementation;

import java.util.Collection;
import java.util.List;

import org.pimslims.business.DataStorage;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.BusinessCriterion;
import org.pimslims.business.criteria.BusinessExpression;
import org.pimslims.business.criteria.LikeExpression;
import org.pimslims.business.crystallization.model.Condition;
import org.pimslims.business.crystallization.model.Screen;
import org.pimslims.business.crystallization.service.ConditionService;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.dao.ConditionDAO;
import org.pimslims.crystallization.dao.view.ConditionViewDAO;
import org.pimslims.crystallization.dao.view.ViewDAO;

/**
 * 
 * @author ian
 */
public class ConditionServiceImpl extends BaseServiceImpl implements ConditionService {
    private final ConditionDAO conditionDAO;

    public ConditionServiceImpl(final DataStorage baseStorage) {
        super(baseStorage);
        conditionDAO = new ConditionDAO(getVersion());
    }

    public Collection<Condition> findAll() throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> findConditions(final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> findConditions(final Screen screen, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> findConditions(final String screenName, final BusinessCriteria criteria)
        throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Condition> findConditionsForManufacturerScreen(final String manufacturer,
        final BusinessCriteria criteria) throws BusinessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer findViewCount(final BusinessCriteria criteria) throws BusinessException {
        return getViewDAO().findViewCount(criteria);
    }

    public Collection<ConditionView> findViews(final BusinessCriteria criteria) throws BusinessException {
        {
            //in xtalPiMS web, ConditionServlet, the localName search is a 'like' search due to limitation of plateDB impl
            //following code  translate it to a 'equal' search which is more accurate and faster 
            LikeExpression localNameExperssion = null;
            final List<BusinessCriterion> criterias = criteria.getCriteria();
            for (final BusinessCriterion businessCriterion : criterias) {
                if (businessCriterion.getProperty().equals(ConditionView.PROP_LOCAL_NAME)
                    && businessCriterion instanceof LikeExpression) {
                    localNameExperssion = (LikeExpression) businessCriterion;
                    break;
                }
            }
            if (localNameExperssion != null) {
                criterias.remove(localNameExperssion);
                criterias.add(BusinessExpression.Equals(localNameExperssion.getProperty(),
                    localNameExperssion.getValue(), true));
                criteria.setCriteria(criterias);
            }
        }
        return getViewDAO().findViews(criteria);
    }

    public String convertPropertyName(final String property) throws BusinessException {
        return getViewDAO().convertPropertyName(property);
    }

    public void create(final Condition condition) throws BusinessException {
        conditionDAO.createPO(condition);
    }

    private ViewDAO<ConditionView> viewDAO;

    private ViewDAO<ConditionView> getViewDAO() {
        if (viewDAO == null) {
            viewDAO = new ConditionViewDAO(version);
        }
        return viewDAO;
    }

}
