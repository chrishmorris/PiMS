/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view ConstructViewDAO.java
 * 
 * @author bl67
 * @date 22 May 2008
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
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.target.ResearchObjective;

/**
 * ConstructViewDAO
 * 
 */
public class ConstructViewDAO extends AbstractSQLViewDAO<ConstructView> {

    /**
     * Constructor for ConstructViewDAO
     */
    public ConstructViewDAO(final ReadableVersion version) {
        super(version);
    }

    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(ConstructView.PROP_NAME)) {
            return "construct.COMMONNAME";
        } else if (propertyName.equals(ConstructView.PROP_ID)) {
            return "construct.LabBookEntryID";
        } else if (propertyName.equals(ConstructView.PROP_DESCRIPTION)) {
            return "construct_base.DETAILS";
        } else if (propertyName.equals(ConstructView.PROP_GROUP)) {
            return "accessobject.name";
        } else if (propertyName.equals(ConstructView.PROP_OWNER)) {
            return "_user.name";
        } else if (propertyName.equals(ConstructView.PROP_TARGET_ID)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        } else if (propertyName.equals(ConstructView.PROP_TARGET_NAME)) {
            throw new UnsupportedOperationException(propertyName + " Not supported.");
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());
    }

    @Override
    String getCountableName() {
        return "construct";
    }

    @Override
    Class<? extends ModelObject> getRootClass() {
        return ResearchObjective.class;
    }

    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select construct.LabBookEntryID as constructid,  "
                + "construct.COMMONNAME as constructname,  "
                + "construct_base.DETAILS,  "
                + "_user.name as username,  "
                + "accessobject.NAME as groupname,  "
                + "construct_base.accessid "
                + "from TARG_RESEARCHOBJECTIVE construct  "
                + "inner join CORE_LABBOOKENTRY construct_base on construct.LabBookEntryID=construct_base.DBID  "
                + "left  join acco_user _user on construct.OWNERID=_user.systemclassid  "
                + "left  join CORE_ACCESSOBJECT accessobject on construct_base.ACCESSID=accessobject.SYSTEMCLASSID ";
        return buildViewQuerySQL(criteria, selectHQL, null, "construct_base", ResearchObjective.class);
    }

    @Override
    Collection<ConstructView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<ConstructView> views = new LinkedList<ConstructView>();
        final SQLQuery q = hqlQuery;
        /* q.addScalar("constructid", StandardBasicTypes.LONG);
        q.addScalar("constructname", StandardBasicTypes.STRING);
        q.addScalar("DETAILS", StandardBasicTypes.STRING);
        q.addScalar("username", StandardBasicTypes.STRING);
        q.addScalar("groupname", StandardBasicTypes.STRING); */

        final List<?> results = hqlQuery.list();
        for (final Object object : results) {
            final Object result[] = (Object[]) object;

            final ConstructView view = new ConstructView();
            view.setConstructId(AbstractSQLViewDAO.getLong(result[0]));
            view.setConstructName((String) result[1]);
            view.setDescription((String) result[2]);
            view.setOwner((String) result[3]);
            view.setGroup((String) result[4]);
            views.add(view);
        }
        return views;
    }

}
