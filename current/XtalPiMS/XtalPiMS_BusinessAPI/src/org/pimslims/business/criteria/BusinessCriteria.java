/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.pimslims.business.criteria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pimslims.business.exception.BusinessException;

/**
 * 
 * @author ian
 */
public class BusinessCriteria {

    private final PropertyNameConvertor<?> nameConvertor;

    private List<BusinessCriterion> criteria = new ArrayList<BusinessCriterion>();

    private int maxResults = 0;

    private int firstResult = 0;

    /**
     * Since it is possible to have more than one sort term...
     */
    private List<BusinessOrder> orderBy = new ArrayList<BusinessOrder>();

    public BusinessCriteria(final PropertyNameConvertor<?> nameConvertor) {
        this.nameConvertor = nameConvertor;
    }

    public List<BusinessCriterion> getCriteria() {
        return criteria;
    }

    public void setCriteria(final List<BusinessCriterion> criteria) throws BusinessException {
        this.criteria = criteria;
        if (criteria != null) {
            for (final BusinessCriterion bc : this.criteria) {
                convertPropertyName(bc);
            }
        }
    }

    private void convertPropertyName(final BusinessCriterion bc) throws BusinessException {
        if (nameConvertor != null) {
            try {
                bc.setProperty(nameConvertor.convertPropertyName(bc.getProperty()));
            } catch (final BusinessException ex) {
                throw new BusinessException("Can not convert property [" + bc.getProperty() + "] due to "
                    + ex.getMessage());
            }
        }

    }

    BusinessCriterion getCriterion(final int index) {
        return criteria.get(index);
    }

    public void add(final BusinessCriterion criterion) throws BusinessException {
        if (criteria == null) {
            criteria = new ArrayList<BusinessCriterion>();
        }
        convertPropertyName(criterion);
        criteria.add(criterion);
    }

    public void remove(final BusinessCriterion criterion) {
        criteria.remove(criterion);
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(final int firstResult) {
        this.firstResult = firstResult;
    }

    public void addOrder(final BusinessOrder order) throws BusinessException {
        if (orderBy == null) {
            orderBy = new ArrayList<BusinessOrder>();
        }
        convertPropertyName(order);
        orderBy.add(order);
    }

    public List<BusinessOrder> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(final List<BusinessOrder> orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * renders the query specified by the criterion and orders.
     * 
     * @return the rendered query.
     */
    public final String renderQuery() {
        return renderWhere() + renderOrderBy();

    }

    public final String renderWhere() {
        final StringBuilder querySQL = new StringBuilder();
        if ((this.getCriteria() != null) && (!this.getCriteria().isEmpty())) {
            querySQL.append(" WHERE ");
            final Iterator<BusinessCriterion> it = this.getCriteria().iterator();
            boolean first = true;
            while (it.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    querySQL.append(" AND ");
                }
                final BusinessCriterion crit = it.next();
                querySQL.append(crit.formatClause());
            }
        }
        return querySQL.toString();
    }

    public final String renderOrderBy() {
        final StringBuilder querySQL = new StringBuilder();
        if ((this.getOrderBy() != null) && (!this.getOrderBy().isEmpty())) {
            querySQL.append(" ORDER BY ");
            final Iterator<BusinessOrder> it = this.getOrderBy().iterator();
            boolean first = true;
            while (it.hasNext()) {
                if (first) {
                    first = false;
                } else {
                    querySQL.append(" , ");
                }
                final BusinessOrder order = it.next();
                querySQL.append(order.formatClause());
            }
        }
        return querySQL.toString();
    }

    /**
     * BusinessCriteria.toString
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return renderQuery();
    }
}
