/**
 * pims-model org.pimslims.persistence PimsQuery.java
 * 
 * @author cm65
 * @date Sep 14, 2012
 * 
 *       Protein Information Management System
 * @version: 4.4
 * 
 *           Copyright (c) 2012 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.pimslims.dao.ReadableVersion;

/**
 * PimsQuery
 * 
 */
public class PimsQuery {

    protected final org.hibernate.Query hqlQuery;

    protected PimsQuery(Query hqlQuery) {
        super();
        this.hqlQuery = hqlQuery;
    }

    public List list() throws HibernateException {
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

    public static PimsQuery getQuery(final ReadableVersion version, final String selectHQL) {
        return new PimsQuery(version.getSession().createQuery(selectHQL).setCacheable(false));
    }

    public Query setString(String arg0, String arg1) {
        return this.hqlQuery.setString(arg0, arg1);
    }

}
