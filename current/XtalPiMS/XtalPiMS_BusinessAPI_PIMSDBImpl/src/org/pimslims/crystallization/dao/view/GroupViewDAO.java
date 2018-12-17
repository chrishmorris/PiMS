/**
 * xtalPiMSImpl org.pimslims.crystallization.dao.view GroupViewDAO.java
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
import org.pimslims.business.crystallization.view.GroupView;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.people.Group;

/**
 * GroupViewDAO
 * 
 */
public class GroupViewDAO extends AbstractSQLViewDAO<GroupView> {

    public GroupViewDAO(final ReadableVersion version) {
        super(version);
    }

    /**
     * GroupViewDAO.convertPropertyName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(final String propertyName) throws BusinessException {
        if (propertyName == null) {
            throw new IllegalArgumentException("Null filter name");
        }
        if (propertyName.equals(GroupView.PROP_NAME)) {
            return "pgroup.name";
        } else if (propertyName.equals(GroupView.PROP_ID)) {
            return "pgroup.LabBookEntryID";
        } else if (propertyName.equals(GroupView.PROP_ORGANISATION)) {
            return "org.name";
        } else if (propertyName.equals(GroupView.PROP_GROUP_HEAD)) {
            return "header_user.name";
        }
        throw new BusinessException("Unable to find matching property in " + this.getClass());

    }

    /**
     * GroupViewDAO.getCountableName
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getCountableName()
     */
    @Override
    String getCountableName() {
        return "pgroup.LabBookEntryID";
    }

    /**
     * GroupViewDAO.getRootClass
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getRootClass()
     */
    @Override
    Class<? extends ModelObject> getRootClass() {
        return Group.class;
    }

    /**
     * GroupViewDAO.getViewHQL
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#getViewHQL(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public String getViewSql(final BusinessCriteria criteria) {
        final String selectHQL =
            "select  pgroup.LabBookEntryID as groupid,  " + "pgroup.name as groupname,  "
                + "org.name as orgname,  " + "header_user.name as headername, " + "pgroup_base.accessid "
                + "from PEOP_GROUP pgroup "
                + "join CORE_LABBOOKENTRY pgroup_base on pgroup.LabBookEntryID=pgroup_base.DBID  "
                + "join acco_usergroup user_group on pgroup.name=user_group.name "
                + "left join acco_user header_user on header_user.systemclassid=user_group.headerid "
                + "left join PEOP_ORGANISATION org on pgroup.ORGANISATIONID=org.LabBookEntryID ";
        return buildViewQuerySQL(criteria, selectHQL, null, "pgroup_base", Group.class);
    }

    /**
     * GroupViewDAO.runSearch
     * 
     * @see org.pimslims.crystallization.dao.view.AbstractViewDAO#runSearch(org.hibernate.Query)
     */
    @Override
    Collection<GroupView> runSearch(final org.hibernate.SQLQuery hqlQuery) {
        final Collection<GroupView> views = new LinkedList<GroupView>();
        final SQLQuery q = hqlQuery;
        /* q.addScalar("groupid", StandardBasicTypes.LONG);
        q.addScalar("groupname", StandardBasicTypes.STRING);
        q.addScalar("orgname", StandardBasicTypes.STRING);
        q.addScalar("headername", StandardBasicTypes.STRING); */
        final List<?> results = hqlQuery.list();
        for (final Object object : results) {
            final Object result[] = (Object[]) object;

            final GroupView view = new GroupView();
            view.setGroupId(AbstractSQLViewDAO.getLong(result[0]));
            view.setGroupName((String) result[1]);
            view.setOrganisation((String) result[2]);
            view.setGroupHead((String) result[3]);
            views.add(view);
        }
        return views;
    }

}
