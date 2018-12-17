/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view ScreeViewDAO.java
 * 
 * @author bl67
 * @date 21 May 2008
 * 
 *       Protein Information Management System
 * @version: 2.2
 * 
 *           Copyright (c) 2008 bl67 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.crystallization.dao.view;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.criteria.PropertyNameConvertor;
import org.pimslims.business.crystallization.view.ConditionView;
import org.pimslims.business.crystallization.view.ScreenView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.crystallization.implementation.view.ScreenTypeView;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.model.holder.RefHolder;
import org.pimslims.model.reference.HolderCategory;

/**
 * ScreeViewDAO
 * 
 */
public class ScreenTypeViewDAO extends AbstractSQLViewDAO<ScreenTypeView> implements
    PropertyNameConvertor<ConditionView> {

    /**
     * Constructor for ScreeViewDAO
     * 
     * @param version
     */
    public ScreenTypeViewDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * ScreeViewDAO.convertPropertyName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {

        if (propertyName.equals(ScreenView.PROP_NAME)) {
            return "holdercate2_.NAME";
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());

    }

    /**
     * ScreeViewDAO.getCountableName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getCountableName()
     */
    @Override
    String getCountableName() {
        return "refHolder";
    }

    /**
     * ScreeViewDAO.getRootClass
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getRootClass()
     */
    @Override
    Class getRootClass() {
        return RefHolder.class;
    }

    /**
     * ScreeViewDAO.getViewHQL
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getViewHQL(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select distinct holdercate2_.NAME as name "
                + "from HOLD_REFHOLDER refholder "
                + "join hold_holdca2abstholders holdercate1_ on refholder.ABSTRACTHOLDERID=holdercate1_.ABSTHOLDERID "
                + "join core_labbookentry refholder_ on refholder.ABSTRACTHOLDERID=refholder_.dbid "
                + "join REF_HOLDERCATEGORY holdercate2_ on holdercate1_.HOLDERCATEGORYID=holdercate2_.PublicEntryID "
                + "join hold_holdca2abstholders holdercate3_ on refholder.ABSTRACTHOLDERID=holdercate3_.ABSTHOLDERID "
                + "join REF_HOLDERCATEGORY holdercate4_ on holdercate3_.HOLDERCATEGORYID=holdercate4_.PublicEntryID "

        //could add to criteria                + "where holdercate2_.NAME<>'Screen' and holdercate4_.NAME='Screen' "
        ;
        return buildViewQuerySQL(criteria, selectHQL,
            " holdercate2_.NAME<>'Screen' and holdercate4_.NAME='Screen' ", "refHolder_",
            HolderCategory.class);
    }

    /**
     * ScreeViewDAO.runSearch
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#runSearch(org.hibernate.Query)
     */
    @Override
    Collection<ScreenTypeView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<ScreenTypeView> views = new LinkedList<ScreenTypeView>();
        final SQLQuery q = hqlQuery;
        //q.addScalar("name", StandardBasicTypes.STRING);
        final List<?> results = hqlQuery.list();
        // int j = 0;
        for (final Object object : results) {
            final String result = (String) object;

            final ScreenTypeView screenTypeView = new ScreenTypeView();
            screenTypeView.setName(result);

            views.add(screenTypeView);
        }
        return views;
    }
}
