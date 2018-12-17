/**
 *  PersonServiceImpl.java
 * 
 * @author cm65
 * @date 13 Jan 2011
 * 
 *       Protein Information Management System
 * @version: 4.1
 * 
 *           Copyright (c) 2011 cm65 The copyright holder has licenced the STFC to redistribute this software
 */
package org.pimslims.formulatrix.implementation;

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

    private final ManufacturerConnection connection;

    /**
     * Constructor for PersonServiceImpl
     * 
     * @param mfrConnection
     */
    public PersonServiceImpl(ManufacturerConnection mfrConnection) {
        this.connection = mfrConnection;
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
            /* FIXME rewrite */ "INSERT INTO Users (Name, PasswordHash, PasswordSalt, EmailAddress, NotificationTypeID)" +
            		" OUTPUT INSERTED.*" +
            		" VALUES (?, ?, ?, ?, 1)";
        try {
            ManufacturerPreparedStatement statement = this.connection.prepareStatement(insert);
            statement.setString(1, user.getFamilyName());
            statement.setString(2, "g1bB3r1sH");
            statement.setString(3, "1234");
            statement.setString(4, user.getEmailAddress());
            ResultSet result=statement.executeQuery();
            result.next();
//            long id = PlateInspectionServiceImpl.getCurrentValue(this.connection, "user");
            long id = result.getLong("ID");
            statement.close();
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
     * @param mfrConnection
     * 
     * @param ownerId
     * @return
     * @throws SQLException
     */
    static Person getPerson(ManufacturerConnection mfrConnection, long personId) throws SQLException {
        Person ret = new Person();
        ret.setId(personId);

        ManufacturerPreparedStatement statement =
            mfrConnection.prepareStatement("SELECT * FROM Users WHERE ID=?");
        statement.setLong(1, personId);
        ResultSet result = statement.executeQuery();
        boolean found = result.next();
        if (!found) {
            statement.close();
            return null;
        }
        ret.setEmailAddress(result.getString("EmailAddress")); /* FIXME column name */
// Not in Formulatrix DB table
//        ret.setFamilyName(result.getString("last_name")); /* FIXME column name */
//        ret.setGivenName(result.getString("first_name")); /* FIXME column name */
//        Calendar createDate = Calendar.getInstance();
//        createDate.setTimeInMillis(result.getDate("create_datetime").getTime()); /* FIXME column name */
//        ret.setStartDate(createDate);
        ret.setUsername(result.getString("Name"));
        ret.setFamilyName(result.getString("Name"));
        assert !result.next() : "More than one result for: " + statement.toString();
        result.close();
        statement.close();
        return ret;
    }

}
