package org.pimslims.crystallization.dao.view;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.AccessControllerImpl;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.logging.Logger;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.model.core.LabNotebook;

;

public abstract class AbstractViewDAO<T> implements ViewDAO<T> {
    protected static final Logger log = Logger.getLogger(AbstractViewDAO.class);

    ReadableVersion version;

    /**
     * Constructor for AbstractViewDAO
     * 
     * @param version
     */
    public AbstractViewDAO(final ReadableVersion version) {
        super();
        this.version = version;
    }

    /**
     * 
     * AbstractViewDAO.getViewHQL: get the HQL query TODO or is it SQL?
     * 
     * @param criteria
     * @return
     * @throws BusinessException
     * 
     *             public abstract String getViewHQL(BusinessCriteria criteria) throws BusinessException;
     */

    /**
     * AbstractViewDAO.convertPropertyName
     * 
     * @see org.pimslims.crystallization.dao.view.ViewDAO#convertPropertyName(java.lang.String)
     */
    public abstract String convertPropertyName(String propertyName) throws BusinessException;

    /**
     * 
     * AbstractViewDAO.getCountableName
     * 
     * @return the internal HQL name for count
     */
    abstract String getCountableName();

    /**
     * 
     * AbstractViewDAO.getRootClass
     * 
     * @return the internal pims class which is the starting point of search HQL
     */
    abstract Class<? extends ModelObject> getRootClass();

    public static String getWhereHQL(final ReadableVersion rv, final String alias,
        final List<String> conditions, final Class targetJavaClass) {
        if (conditions == null || conditions.size() == 0 && rv.isAdmin()) {
            return " ";
        }
        String whereString = "";

        if (AccessControllerImpl.isSearchAccessControlNeeded(targetJavaClass) && (!rv.isAdmin()
        //TODO || null != rv.getReadableLabNotebooks()) // no filter
            || !rv.getReadableLabNotebooks().isEmpty() //TODO remove, it should be possible to filter for reference data only
            )) {
            final ReadableVersion version = rv;

            String dbids = "";
            for (final Iterator iterator = rv.getReadableLabNotebooks().iterator(); iterator.hasNext();) {
                final LabNotebook accessObject = (LabNotebook) iterator.next();
                if (accessObject != null) {
                    dbids += accessObject.getDbId();
                    if (iterator.hasNext()) {
                        dbids += ",";
                    }
                }

            }
            if (dbids.length() == 0) {
                dbids = "-1";
            }
            if (dbids.endsWith(",")) {
                dbids = dbids.substring(0, dbids.length() - 1);
            }
            final String accessHQL =
                " (" + alias + ".access is null OR " + alias + ".access.id in (" + dbids + ") ) ";
            whereString = accessHQL + " and ";
        }
        for (final Iterator iter = conditions.iterator(); iter.hasNext();) {
            String condition = (String) iter.next();
            if (condition.trim().length() > 0) {
                condition = condition.trim();
                if (condition.toLowerCase().startsWith("where")) {
                    condition = condition.substring("where".length());
                }
                whereString += condition + " ";
                if (iter.hasNext()) {
                    whereString += " and ";
                }
            }

        }
        if (whereString.endsWith(" and ")) {
            whereString = whereString.substring(0, whereString.lastIndexOf(" and "));
        }
        if ("".equals(whereString)) {
            return "";
        }
        return " where " + whereString;
    }

    /**
     * 
     * AbstractViewDAO.buildViewQuery
     * 
     * @param criteria
     * @param joinSQl
     * @param subSelect additional where condition
     * @param accessControledMOName the alias of modelObject which need access control
     * @return
     */
    protected String buildViewQueryHQL(final BusinessCriteria criteria, final String joinSQl,
        final String subSelect, final String accessControledMOName, final Class pClass) {
        assert null != pClass;
        String whereHQL = "";
        String orderBy = "";
        if (criteria != null) {
            whereHQL = criteria.renderWhere();
            orderBy = criteria.renderOrderBy();
        }
        final List<String> conditions = new LinkedList<String>();
        if (whereHQL != null && whereHQL.trim().length() > 0) {
            conditions.add(whereHQL);
        }
        if (subSelect != null && subSelect.length() > 1) {
            conditions.add(subSelect);
        }
        final String fullWhere = getWhereHQL(this.version, accessControledMOName, conditions, pClass);
        // System.out.println(selectHQL + fullWhere + orderBy);
        return joinSQl + fullWhere + orderBy;
    }

    static Calendar getCalDate(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Calendar) {
            return (Calendar) object;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(((Timestamp) object).getTime());
        return cal;
    }

    static String getWell(final Integer row, final Integer col, final Integer sub) {
        if (sub != null) {
            return (new WellPosition(row, col, sub)).toString();
        } else if (row == null || col == null) {
            return null; // TODO no, I think null is always an error - Chris
        } else {
            return (new WellPosition(row, col, 1)).toString();
        }
    }

    static String getXPersonName(String familyName, String givenName, String familyTitle) {
        if (familyName == null) {
            familyName = "";
        }
        if (givenName == null) {
            givenName = "";
        }
        if (familyTitle == null) {
            familyTitle = "";
        }
        return givenName + " " + familyName + " " + familyTitle;

    }

    public static Double getDouble(Object number) {
        if (null == number) {
            return null;
        }
        return ((Number) number).doubleValue();
    }

    public static Float getFloat(Object number) {
        if (null == number) {
            return null;
        }
        return ((Number) number).floatValue();
    }
}
