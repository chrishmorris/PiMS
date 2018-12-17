package org.pimslims.crystallization.dao.view;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.model.WellPosition;
import org.pimslims.business.exception.BusinessException;
import org.pimslims.dao.ReadableVersion;
import org.pimslims.logging.Logger;
import org.pimslims.metamodel.ModelObject;
import org.pimslims.persistence.JpqlQuery;

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

    /**
     * 
     * AbstractViewDAO.buildViewQuery
     * 
     * @param criteria
     * @param selectHQL
     * @param subSelect additional where condition
     * @param accessControledMOName the alias of modelObject which need access control
     * @return
     */
    protected String buildViewQueryHQL(final BusinessCriteria criteria, final String selectHQL,
        final String subSelect, final String accessControledMOName, final Class pClass) {
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
        final String fullWhere =
            JpqlQuery.getWhereHQL(this.version, accessControledMOName, conditions, pClass);
        // System.out.println(selectHQL + fullWhere + orderBy);
        return selectHQL + fullWhere + orderBy;
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

    static String getFullImagePath(final String fileName, final String filePath, final String imagerPath) {
        if (filePath == null) {
            return imagerPath + fileName;
        } else if (filePath.toLowerCase().startsWith("http") || filePath.toLowerCase().startsWith("file")) {
            return filePath + fileName;
        } else if (filePath.contains("/ViewFile/")) {
            return filePath + fileName;
        } else {
            assert null != imagerPath : "Cannot determine path to image";
            return imagerPath + filePath + fileName;
        }
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
