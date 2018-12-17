/**
 * Rhombix_Impl org.pimslims.rhombix SampleServiceImpl.java
 * 
 * @author cm65
 * @date 12 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.model.Sample;
import org.pimslims.business.core.service.SampleService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.SampleQuantityView;
import org.pimslims.business.crystallization.view.SampleView;
import org.pimslims.business.exception.BusinessException;

/**
 * SampleServiceImpl
 * 
 * This accesses the Rhombix table MACROMOLECULE_VERSION. The rhombix schema also has a table called SAMPLE,
 * but this is not populated.
 * 
 */
public class SampleServiceImpl implements SampleService {

    /**
     * TABLE String
     */
    private static final String TABLE = "macromolecule_version";

    private final RhombixConnection con;

    private final Map<Long, Sample> cache = new HashMap<Long, Sample>();

    /**
     * Constructor for SampleServiceImpl
     * 
     * @param rhombixConnection
     */
    SampleServiceImpl(RhombixConnection rhombixConnection) {
        super();
        this.con = rhombixConnection;
    }

    /**
     * SampleServiceImpl.close
     * 
     * @see org.pimslims.business.core.service.SampleService#close(org.pimslims.business.core.model.Sample)
     */
    @Override
    public void close(Sample sample) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.create
     * 
     * @see org.pimslims.business.core.service.SampleService#create(org.pimslims.business.core.model.Sample)
     */
    @Override
    public void create(Sample sample) throws BusinessException {
        // we need this for testing
        try {
            String sql =
                "insert into " + TABLE + " (" + TABLE + "_ID, "
                    + "sample_version, sample_version_desc) values (" + TABLE + "_SEQ.nextval, ?, ?)";
            RhombixPreparedStatement statement = this.con.prepareStatement(sql);
            statement.setString(1, sample.getName());
            statement.setString(2, sample.getDescription());
            statement.executeUpdate();
            statement.close();

            statement = this.con.prepareStatement("select " + TABLE + "_SEQ.currval from dual");
            ResultSet result = statement.executeQuery();
            result.next();
            sample.setId(result.getLong(1));
            statement.close();
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * SampleServiceImpl.find
     * 
     * @see org.pimslims.business.core.service.SampleService#find(long)
     */
    @Override
    public Sample find(long sampleId) throws BusinessException {
        try {
            return doFind(this.con, sampleId, this.cache);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    static Sample doFind(RhombixConnection connection, long sampleId, Map<Long, Sample> cache)
        throws SQLException {
        if (null != cache && cache.containsKey(sampleId)) {
            return cache.get(sampleId);
        }
        RhombixPreparedStatement statement =
            connection.prepareStatement("select * from " + TABLE + " where " + TABLE + "_id=?");
        statement.setLong(1, sampleId);
        Sample bean = makeBean(statement);
        statement.close();
        if (null != cache) {
            cache.put(sampleId, bean);
        }
        return bean;
    }

    static Sample makeBean(RhombixPreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        boolean hasNext = result.next();
        assert hasNext;

        Sample ret = new Sample();
        ret.setId(result.getLong("" + TABLE + "_id"));
        Calendar createDate = Calendar.getInstance();
        createDate.setTimeInMillis(result.getDate("create_datetime").getTime());
        ret.setCreateDate(createDate);

        ret.setName(result.getString("sample_version")); // but sometimes this is null!
        ret.setDescription(result.getString("sample_version_desc"));
        //ret.setMolecularWeight(molecularWeight);
        //ret.setOwner(owner);
        //ret.setSafetyInformation(safetyInformation);
        //ret.setConcentration(result.get("new_concentration"));
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        return ret;
    }

    /**
     * SampleServiceImpl.findAll
     * 
     * @see org.pimslims.business.core.service.SampleService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Sample> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.findByName
     * 
     * @see org.pimslims.business.core.service.SampleService#findByName(java.lang.String)
     */
    @Override
    public Sample findByName(String name) throws BusinessException {
        try {
            RhombixPreparedStatement statement =
                this.con.prepareStatement("select * from " + TABLE + " where sample_version=?");
            statement.setString(1, name);
            Sample ret = makeBean(statement);
            statement.close();
            return ret;
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * SampleServiceImpl.findSampleQuantities
     * 
     * @see org.pimslims.business.core.service.SampleService#findSampleQuantities(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<SampleQuantityView> findSampleQuantities(BusinessCriteria criteria)
        throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.getOwner
     * 
     * @see org.pimslims.business.core.service.SampleService#getOwner(org.pimslims.business.core.model.Sample)
     */
    @Override
    public Person getOwner(Sample sample) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.setConstructForSample
     * 
     * @see org.pimslims.business.core.service.SampleService#setConstructForSample(org.pimslims.business.core.model.Sample,
     *      org.pimslims.business.core.model.Construct)
     */
    @Override
    public void setConstructForSample(Sample sample, Construct construct) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.setOperatorForSample
     * 
     * @see org.pimslims.business.core.service.SampleService#setOperatorForSample(org.pimslims.business.core.model.Sample,
     *      org.pimslims.business.core.model.Person)
     */
    @Override
    public void setOperatorForSample(Sample sample, Person person) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.setOwnerForSample
     * 
     * @see org.pimslims.business.core.service.SampleService#setOwnerForSample(org.pimslims.business.core.model.Sample,
     *      org.pimslims.business.core.model.Person)
     */
    @Override
    public void setOwnerForSample(Sample sample, Person person) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.update
     * 
     * @see org.pimslims.business.core.service.SampleService#update(org.pimslims.business.core.model.Sample)
     */
    @Override
    public void update(Sample sample) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<SampleView> findViews(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * SampleServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

}
