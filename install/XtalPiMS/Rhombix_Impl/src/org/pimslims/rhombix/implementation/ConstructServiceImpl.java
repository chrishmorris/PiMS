/**
 * Rhombix_Impl org.pimslims.rhombix ConstructServiceImpl.java
 * 
 * @author cm65
 * @date 10 May 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.rhombix.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Construct;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.ConstructService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.crystallization.view.ConstructView;
import org.pimslims.business.exception.BusinessException;

/**
 * ConstructServiceImpl
 * 
 */
public class ConstructServiceImpl implements ConstructService {

    private static final String TABLE = "MACROMOLECULE";

    private final RhombixConnection connection;

    /**
     * Constructor for ConstructServiceImpl
     * 
     * @param connection
     */
    public ConstructServiceImpl(RhombixConnection connection) {
        this.connection = connection;
    }

    /**
     * ConstructServiceImpl.close
     * 
     * @see org.pimslims.business.core.service.ConstructService#close(org.pimslims.business.core.model.Construct)
     */
    @Override
    public void close(Construct construct) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.create
     * 
     * @see org.pimslims.business.core.service.ConstructService#create(org.pimslims.business.core.model.Construct)
     */
    @Override
    public void create(Construct construct) throws BusinessException {
        String insert =
            "insert into " + TABLE + " (" + TABLE + "_id, name, description) " + "values (" + TABLE
                + "_seq.nextval, ?, ? )";
        try {
            RhombixPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, construct.getName());
            statement.setString(2, construct.getDescription());
            statement.executeUpdate();
            statement.close();
            long id = PlateInspectionServiceImpl.getCurrentValue(this.connection, TABLE);
            construct.setId(id);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * ConstructServiceImpl.find
     * 
     * @see org.pimslims.business.core.service.ConstructService#find(long)
     */
    @Override
    public Construct find(long id) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.findAll
     * 
     * @see org.pimslims.business.core.service.ConstructService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Construct> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.findByName
     * 
     * @see org.pimslims.business.core.service.ConstructService#findByName(java.lang.String)
     */
    @Override
    public Construct findByName(String name) throws BusinessException {
        try {
            return getConstruct(this.connection, name);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    static Construct getConstruct(RhombixConnection rhombixConnection, String name) throws SQLException {
        Construct ret = new Construct(name, null);

        RhombixPreparedStatement statement =
            rhombixConnection.prepareStatement("select * from " + TABLE + " where name=?");
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            return null;
        }

        ret.setId(result.getLong(TABLE + "_ID"));
        ret.setDescription(result.getString("description"));
        //TODO ret.setOwner(person);

        /* Calendar createDate = Calendar.getInstance();
        createDate.setTimeInMillis(result.getDate("create_datetime").getTime()); */
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        statement.close();
        return ret;
    }

    /**
     * ConstructServiceImpl.findByUser
     * 
     * @see org.pimslims.business.core.service.ConstructService#findByUser(org.pimslims.business.core.model.Person)
     */
    @Override
    public Collection<Construct> findByUser(Person person) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.update
     * 
     * @see org.pimslims.business.core.service.ConstructService#update(org.pimslims.business.core.model.Construct)
     */
    @Override
    public void update(Construct construct) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.findViewCount
     * 
     * @see org.pimslims.business.ViewService#findViewCount(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Integer findViewCount(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.findViews
     * 
     * @see org.pimslims.business.ViewService#findViews(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<ConstructView> findViews(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * ConstructServiceImpl.convertPropertyName
     * 
     * @see org.pimslims.business.criteria.PropertyNameConvertor#convertPropertyName(java.lang.String)
     */
    @Override
    public String convertPropertyName(String property) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

}
