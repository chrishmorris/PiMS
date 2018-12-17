/**
 * Rhombix_Impl org.pimslims.rhombix PersonServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jan 2011
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

import org.pimslims.business.DataStorage;
import org.pimslims.business.core.model.Person;
import org.pimslims.business.core.service.PersonService;
import org.pimslims.business.criteria.BusinessCriteria;
import org.pimslims.business.exception.BusinessException;

/**
 * PersonServiceImpl
 * 
 */
public class PersonServiceImpl implements PersonService {

    private final RhombixConnection connection;

    /**
     * Constructor for PersonServiceImpl
     * 
     * @param rhombixConnection
     */
    public PersonServiceImpl(RhombixConnection rhombixConnection) {
        this.connection = rhombixConnection;
    }

    /**
     * PersonServiceImpl.close
     * 
     * @see org.pimslims.business.core.service.PersonService#close(org.pimslims.business.core.model.Person)
     */
    @Override
    public void close(Person user) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PersonServiceImpl.create
     * 
     * @see org.pimslims.business.core.service.PersonService#create(org.pimslims.business.core.model.Person)
     */
    @Override
    public void create(Person user) throws BusinessException {
        String insert =
            "insert into USERS (user_id, email, last_name, first_name, username) "
                + "values (user_seq.nextval, ?, ?, ?, ?)";
        try {
            RhombixPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, user.getEmailAddress());
            statement.setString(2, user.getFamilyName());
            statement.setString(3, user.getGivenName().substring(0,
                Math.min(16, user.getGivenName().length())));
            statement.setString(4, user.getUsername());
            statement.executeUpdate();
            statement.close();
            long id = PlateInspectionServiceImpl.getCurrentValue(this.connection, "user");
            user.setId(id);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }

    }

    /* 
        ret.setEmailAddress(result.getString("email"));
        ret.setFamilyName(result.getString("last_name"));
        ret.setGivenName(result.getString("first_name"));
        Calendar createDate = Calendar.getInstance();
        createDate.setTimeInMillis(result.getDate("create_datetime").getTime());
        ret.setStartDate(createDate);
        ret.setUsername(result.getString("username"));*/

    /**
     * PersonServiceImpl.find
     * 
     * @see org.pimslims.business.core.service.PersonService#find(long)
     */
    @Override
    public Person find(long userId) throws BusinessException {
        try {
            return getPerson(this.connection, userId);
        } catch (SQLException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * PersonServiceImpl.findAll
     * 
     * @see org.pimslims.business.core.service.PersonService#findAll(org.pimslims.business.criteria.BusinessCriteria)
     */
    @Override
    public Collection<Person> findAll(BusinessCriteria criteria) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PersonServiceImpl.findByUsername
     * 
     * @see org.pimslims.business.core.service.PersonService#findByUsername(java.lang.String)
     */
    @Override
    public Person findByUsername(String username) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PersonServiceImpl.update
     * 
     * @see org.pimslims.business.core.service.PersonService#update(org.pimslims.business.core.model.Person)
     */
    @Override
    public void update(Person user) throws BusinessException {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PersonServiceImpl.getDataStorage
     * 
     * @see org.pimslims.business.BaseService#getDataStorage()
     */
    @Override
    public DataStorage getDataStorage() {
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * PersonServiceImpl.getPerson
     * 
     * @param rhombixConnection
     * 
     * @param ownerId
     * @return
     * @throws SQLException
     */
    static Person getPerson(RhombixConnection rhombixConnection, long personId) throws SQLException {
        Person ret = new Person();
        ret.setId(personId);

        RhombixPreparedStatement statement =
            rhombixConnection.prepareStatement("select * from USERS where USER_ID=?");
        statement.setLong(1, personId);
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            statement.close();
            return null;
        }

        ret.setEmailAddress(result.getString("email"));
        ret.setFamilyName(result.getString("last_name"));
        ret.setGivenName(result.getString("first_name"));
        Calendar createDate = Calendar.getInstance();
        createDate.setTimeInMillis(result.getDate("create_datetime").getTime());
        ret.setStartDate(createDate);
        ret.setUsername(result.getString("username"));
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        statement.close();
        return ret;
    }

}
