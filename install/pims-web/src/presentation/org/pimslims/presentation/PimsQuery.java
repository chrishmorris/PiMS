/**
 * V4_3-web org.pimslims.presentation PimsQuery.java
 * 
 * @author cm65
 * @date Sep 14, 2012
 * 
 *       Protein Information Management System
 * @version: 3.2
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.presentation;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.pimslims.dao.ReadableVersion;

/**
 * PimsQuery
 */
@Deprecated
// use org.pimslims.persistence.PimsQuery
public class PimsQuery {

    private final Query hqlQuery;

    /**
     * Constructor for PimsQuery
     * 
     * @param setCacheable
     */
    private PimsQuery(final Query hqlQuery) {
        this.hqlQuery = hqlQuery;
    }

    public List list() throws HibernateException {
        //TODO filter for access control
        return this.hqlQuery.list();
    }

    public PimsQuery setCacheRegion(final String arg0) {
        return new PimsQuery(this.hqlQuery.setCacheRegion(arg0));
    }

    public Query setEntity(final String arg0, final Object arg1) {
        return this.hqlQuery.setEntity(arg0, arg1);
    }

    public Query setLong(final String arg0, final long arg1) {
        return this.hqlQuery.setLong(arg0, arg1);
    }

    public Query setParameter(final String arg0, final Object arg1) throws HibernateException {
        return this.hqlQuery.setParameter(arg0, arg1);
    }

    public String getQueryString() {
        return this.hqlQuery.getQueryString();
    }

    public Query setFirstResult(final int arg0) {
        return this.hqlQuery.setFirstResult(arg0);
    }

    public Query setMaxResults(final int arg0) {
        return this.hqlQuery.setMaxResults(arg0);
    }

    public Object uniqueResult() throws HibernateException {
        return this.hqlQuery.uniqueResult();
    }

    // TODO return a org.pimslims.persistence.PimsQuery
    public static PimsQuery getQuery(final ReadableVersion version, final String selectHQL) {
        return new PimsQuery(version.getSession().createQuery(selectHQL).setCacheable(false));
    }

    public static PimsQuery getQuery(final ReadableVersion version, final String selectHQL,
        final Class javaClass) {
        return new PimsQuery(version.getSession().createQuery(selectHQL).setCacheable(false));
    }

}
